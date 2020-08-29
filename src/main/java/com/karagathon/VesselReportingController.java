package com.karagathon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.ResponseEntity;
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

import com.karagathon.aws.service.AWSS3Service;
import com.karagathon.helper.BucketBeanHelper;
import com.karagathon.helper.DateFormatter;
import com.karagathon.helper.FileHelper;
import com.karagathon.helper.ListConversionHelper;
import com.karagathon.helper.SpecificServiceHelper;
import com.karagathon.model.IModel;
import com.karagathon.model.Location;
import com.karagathon.model.Media;
import com.karagathon.model.Notification;
import com.karagathon.model.Report;
import com.karagathon.model.User;
import com.karagathon.repository.UserRepository;
import com.karagathon.service.LocationService;
import com.karagathon.service.MediaService;
import com.karagathon.service.NotificationService;
import com.karagathon.service.ReportService;
import com.karagathon.service.SMSService;

@Controller
public class VesselReportingController {

	@Autowired
	MediaService mediaService;

	@Autowired
	FileHelper fileHelper;

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

	@Autowired
	UserRepository userRepository;

	@Autowired
	SMSService smsService;

	@RequestMapping("/login")
	public String login(Principal principal) {

		if (Objects.nonNull(principal)) {
			return "redirect:/";
		}
		return "login.html";
	}

	@RequestMapping("/")
	public String home(Model model, Principal principal) {
		User user = userRepository.getUser(principal.getName());

		model.addAttribute("user", user);

		return "home.html";
	}

	@RequestMapping("/reports")
	public String reportsDashboard(Model model) {
		List<Report> reports = reportService.getAllReports().stream().map(report -> {
			String description;

			if (report.getDescription().length() > 99) {
				description = report.getDescription().substring(0, 99).concat("...");
			} else {
				description = report.getDescription();
			}

			return new Report(report.getId(), report.getName(), report.getMedia(), description);
		}).collect(Collectors.toList());
		model.addAttribute("reports", reports);

		return "reports-dashboard.html";
	}

	@GetMapping("/report/media/{id}")
	public void showViolationImage(@PathVariable Long id, HttpServletResponse response, Model model)
			throws IOException {
		response.setContentType("image/jfif");
		response.setContentType("video/mp4");

		Media medium = mediaService.findById(id);

		if (!Objects.isNull(medium)) {
			InputStream is = new ByteArrayInputStream(
					s3Service.downloadFile(medium.getMediaFilePath(), new BucketBeanHelper(bucketName, filePath)));
			IOUtils.copy(is, response.getOutputStream());

		}

	}

	@PostMapping("/upload")
	@ResponseBody
	public ResponseEntity<String> upload(@RequestBody String jsonData) throws IOException {

		System.out.println("JSON DATA" + jsonData);

		JsonParser jsonParser = JsonParserFactory.getJsonParser();
		Map<String, Object> parsedJsonMap = jsonParser.parseMap(jsonData);
		final String locationString = parsedJsonMap.get("location").toString().trim();
		Report savedReport = reportService.saveAndFlushReport(new Report(parsedJsonMap.get("name").toString().trim(),
				locationString, DateFormatter.format(parsedJsonMap.get("date").toString().trim(), dateFormatFromMobile),
				parsedJsonMap.get("description").toString().trim()));
		Location savedLocation = locationService
				.saveAndFlushLocation(new Location(new BigDecimal(parsedJsonMap.get("longitude").toString().trim()),
						new BigDecimal(parsedJsonMap.get("latitude").toString().trim()), savedReport));
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

		Notification savedNotification = notificationService
				.saveAndFlush(new Notification(false, "New Report Reported! Report #" + savedReport.getId().toString(),
						savedReport.getDescription(), "/report/".concat(savedReport.getId().toString())));
		System.out.println(savedNotification);

		// add sms logic
		ZoneId defaultZoneId = ZoneId.systemDefault();

		LocalDate localDate = LocalDate.now();

		Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		int count = reportService.getCountOfReports(date);

		if (count == 1) {
			smsService.sendSMS(smsService.singleReportMessage());
		} else if (count == 2) {
			smsService.sendSMS(smsService.moreThanOneReportMessage());
		}

		return ResponseEntity.ok("{action: \"Success\"}");
	}

	@GetMapping("/report/{id}")
	public ModelAndView getSpecificReport(@PathVariable("id") Long id) {

		ModelAndView mav = serviceHelper.getSpecific(reportService, id);

		IModel model = reportService.findById(id);

		List<Media> media = mediaService.findMediaByModel(model);

		media.stream().distinct().forEach(medium -> {
			boolean isVideo = false;
			boolean isImage = false;

			if (medium.getMediaType().equals("VIDEO")) {
				isVideo = true;
				mav.addObject("isVideo", isVideo);
			}
			if (medium.getMediaType().equals("PICTURE")) {
				isImage = true;
				mav.addObject("isImage", isImage);

			}
		});

		return mav;
	}

	@GetMapping("/map")
	public String test(Model model) {
		List<Location> locations = locationService.getAllLocations();

		model.addAttribute("locations", locations);

		return "map.html";
	}

	@GetMapping("/consume/{locationString}")
	public @ResponseBody String consume(@PathVariable("locationString") String locationString) {
		System.out.println(locationService.getLocation(locationString));
		return "";
	}

	@GetMapping("/search-report")
	public ModelAndView searchViolator(@RequestParam("keyword") String description) {
		if (Objects.isNull(description) || description.trim().isEmpty()) {
			return new ModelAndView("redirect:/reports");
		}

		ModelAndView mav = new ModelAndView();
		List<Report> reports = reportService.findReportsByDescription(description);
		mav.addObject("reports", reports);
		mav.addObject("keyword", description);
		mav.setViewName("reports-dashboard");

		return mav;
	}

}
