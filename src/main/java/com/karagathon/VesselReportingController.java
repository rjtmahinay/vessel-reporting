package com.karagathon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	public String upload(@RequestParam("file") List<MultipartFile> files) throws IOException {

		List<String> fileWithPath = new ArrayList<>();

		// fileHelper.moveUploadedFile(files);

//		return ResponseEntity.status(HttpStatus.OK);
		return "Upload Video Success";
	}

}
