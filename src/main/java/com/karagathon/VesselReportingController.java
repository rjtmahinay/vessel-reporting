package com.karagathon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.karagathon.aws.service.AWSS3Service;
import com.karagathon.helper.BucketBeanHelper;
import com.karagathon.helper.DateFormatter;
import com.karagathon.helper.FileHelper;
import com.karagathon.helper.ListConversionHelper;
import com.karagathon.helper.LocationParser;
import com.karagathon.helper.SpecificServiceHelper;
import com.karagathon.model.Location;
import com.karagathon.model.Media;
import com.karagathon.model.Notification;
import com.karagathon.model.Report;
import com.karagathon.model.Violator;
import com.karagathon.service.LocationService;
import com.karagathon.service.MediaService;
import com.karagathon.service.NotificationService;
import com.karagathon.aws.service.AWSS3Service;
import com.karagathon.helper.BucketBeanHelper;
import com.karagathon.helper.FileHelper;
import com.karagathon.model.Media;
import com.karagathon.model.Report;
import com.karagathon.model.Violation;
import com.karagathon.service.MediaService;

import com.karagathon.service.ReportService;

@Controller
public class VesselReportingController {

	@Autowired
	MediaService mediaService;

	@Autowired
	FileHelper fileHelper;
	
	@Autowired
	AWSS3Service s3Service;
	
	@Value("${aws.s3.report.filePath}")
	String filePath;
	
	@Value("${aws.s3.report.bucket}")
	String bucketName;
	
	
	@Autowired
	NotificationService notificationService;


	@Value("${mobile.android.date.format}")
	String dateFormatFromMobile;

	@Autowired
	SpecificServiceHelper serviceHelper;
	
	@Autowired
	LocationService locationService;
	
	@Autowired

	AWSS3Service s3Service;
	
	@Autowired
	NotificationService notificationService;

	@Value("${aws.s3.report.filePath}")
	String filePath;

	@Value("${aws.s3.report.bucket}")
	String bucketName;

	@Value("${mobile.android.date.format}")
	String dateFormatFromMobile;

	@Autowired
	ReportService reportService;

	@RequestMapping("/")
	public String login() {
		return "login.html";
	}

	@RequestMapping("/reports")
	public String reportsDashboard(Model model) {
		List<Report> reports = reportService.getAllReports().stream()
				.map(report -> {
					String description;
					
					if(report.getDescription().length() > 99) {
						description = report.getDescription().substring(0, 99).concat("...");
					}
					else {
						description = report.getDescription();
					}
					
					return new Report( report.getId(), report.getName(), report.getMedia(), description );
				})
				.collect(Collectors.toList());
		model.addAttribute("reports", reports);
		
		return "reports-dashboard.html";
	}
	
	@GetMapping("/report/image/{id}")
	public void showViolationImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jfif"); 
		Media medium = mediaService.findById(id);

		if( !Objects.isNull(medium) ) {
			InputStream is = new ByteArrayInputStream( s3Service.downloadFile( medium.getMediaFilePath(), new BucketBeanHelper(bucketName, filePath) ) );
			IOUtils.copy(is, response.getOutputStream());

		}
	}

	@PostMapping("/upload")
	@ResponseBody
	public String upload(@RequestBody String jsonData) throws IOException {
		JsonParser jsonParser = JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJsonMap = jsonParser.parseMap(jsonData);
		final String locationString = parsedJsonMap.get("location").toString().trim();
		Report savedReport = reportService.saveAndFlushReport(
				new Report(parsedJsonMap.get("name").toString().trim(), locationString,
						DateFormatter.format(parsedJsonMap.get("date").toString().trim(), dateFormatFromMobile),
						parsedJsonMap.get("description").toString().trim()));
		Location savedLocation = locationService.saveAndFlushLocation( new Location( 
																		new BigDecimal( parsedJsonMap.get("longitude").toString().trim() ) , 
																		new BigDecimal( parsedJsonMap.get("latitude").toString().trim() ) , 
																		savedReport ) );
		System.out.println(savedLocation);
		
		mediaService.saveAll(ListConversionHelper
				.stringToMedia(ListConversionHelper.objectToList(parsedJsonMap.get("files")), savedReport));
		
		System.out.println(savedReport);
		
//		Location location = locationService.getLocation( locationString );
//		
//		if(Objects.nonNull(location)) {
//			location.setReport( savedReport );
//			
//			Location updatedSavedLocation = locationService.saveAndFlushLocation( location );
//			
//			System.out.println(updatedSavedLocation);
//		}
		
		Notification savedNotification = notificationService.saveAndFlush( 
					new Notification( false, "New Report Reported! Report #" + savedReport.getId().toString(), 
											savedReport.getDescription(), 
											"/report/".concat(savedReport.getId().toString()) ) );
		System.out.println(savedNotification);
		
		// add sms logic
		
		return "Upload Video Success";
	}

	@GetMapping("/report/{id}")
	public ModelAndView getSpecificReport(@PathVariable("id") Long id) {
		return serviceHelper.getSpecific(reportService, id);
	}

	@GetMapping("/map")
	public String test(Model model) {
		List<Location> locations = locationService.getAllLocations();

		model.addAttribute("locations", locations);
		
		return "map.html";
	}

	@GetMapping("/consume/{locationString}")
	public @ResponseBody String consume( @PathVariable("locationString") String locationString ) {
		System.out.println(locationService.getLocation( locationString ));
		return "";
	}
	
	@GetMapping("/search-report")
	public ModelAndView searchViolator( @RequestParam("keyword") String description ) {
		if( Objects.isNull(description) || description.isEmpty() || description.isBlank()) {
			return new ModelAndView( "redirect:/reports" );
		}
		
		ModelAndView mav = new ModelAndView();
		List<Report> reports = reportService.findReportsByDescription(description);
		mav.addObject("reports", reports);
		mav.addObject("keyword", description);
		mav.setViewName("reports-dashboard");
		
		return mav;
	}
	

}
