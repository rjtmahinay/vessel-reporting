package com.karagathon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.karagathon.repository.ReportsRepository;

@Controller
public class VesselReportingController {

	@Autowired
	ReportsRepository reportRepo;

	@RequestMapping("/")
	public String login() {
		return "login.html";
	}

	@PostMapping("/upload")
	@ResponseBody
	public String uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
		
		byte[] picture = compressFile(file.getBytes());
		
		reportRepo.uploadImage(picture);
		
//		return ResponseEntity.status(HttpStatus.OK);
		return "success";
	}
	
	//standard compress file
	private byte[] compressFile(byte[] file) {
		Deflater deflater = new Deflater();
		deflater.setInput(file);
		deflater.finish();
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
		
		byte[] buffer = new byte[1024];
		
		while(!deflater.finished()) {
			int count = deflater.deflate(buffer);
			stream.write(buffer, 0, count);
		}
		
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream.toByteArray();
	}
}
