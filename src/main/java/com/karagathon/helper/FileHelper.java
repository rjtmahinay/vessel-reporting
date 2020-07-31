package com.karagathon.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHelper {
	@Value("${report.file.path}")
	String destination;

	public void moveUploadedFile(List<MultipartFile> files) {

		Path destinationDir = Paths.get(destination);

		if (!Files.exists(destinationDir)) {
			try {
				Files.createDirectory(destinationDir);
			} catch (IOException e1) {
				//
			}
		}

		files.forEach(file -> moveUploadedFile(file));

	}

	private void moveUploadedFile(MultipartFile file) {
		StringBuffer destinationString = new StringBuffer().append(StringUtils.cleanPath(destination))
				.append(File.separator).append(file.getOriginalFilename());

		Path destinationLocation = Paths.get(destinationString.toString());

		System.out.println("Path: " + destinationLocation.toAbsolutePath().toString());

		try {
			Files.copy(file.getInputStream(), destinationLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
