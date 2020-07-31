package com.karagathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.karagathon.helper.FileHelper;
import com.karagathon.repository.ReportsRepository;

@Controller
public class VesselReportingController {

	@Autowired
	FileHelper fileHelper;

	@Autowired
	ReportsRepository reportsRepository;

	@RequestMapping("/")
	public String login() {
		return "login.html";
	}

	@PostMapping("/upload")
	@ResponseBody
	public String upload(@RequestParam("file") List<MultipartFile> files) throws IOException {

		List<String> fileWithPath = new ArrayList<>();

		fileHelper.moveUploadedFile(files);

//		return ResponseEntity.status(HttpStatus.OK);
		return "Upload Video Success";
	}

}
