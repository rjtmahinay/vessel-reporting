package com.karagathon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.karagathon.aws.service.AWSS3Service;
import com.karagathon.helper.BucketBeanHelper;
import com.karagathon.helper.FileHelper;
import com.karagathon.helper.ListConversionHelper;
import com.karagathon.helper.SpecificServiceHelper;
import com.karagathon.helper.Utilities;
import com.karagathon.model.Media;
import com.karagathon.model.Vessel;
import com.karagathon.model.Violator;
import com.karagathon.service.MediaService;
import com.karagathon.service.VesselService;
import com.karagathon.service.ViolatorService;

@Controller
public class ViolatorSubmissionController {
	
	@Autowired
	ViolatorService violatorService;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	FileHelper fileHelper;
	
	@Autowired
	AWSS3Service s3Service;
	
	@Autowired
	VesselService vesselService;
	
	@Autowired
	SpecificServiceHelper serviceHelper;
	
	@Value("${violation.file.path}")
	String destination;
	
	@Value("${aws.s3.violator.filePath}")
	String filePath;
	
	@Value("${aws.s3.violation.bucket}")
	String bucketName;
	
	@RequestMapping("/violators")
	public String dashboard(Model model) {
		List<Violator> violators = violatorService.getAllViolators();
		model.addAttribute("violators", violators);
		
		violators.forEach(System.out::println);
		
		return "violator-dashboard.html";
	}
	
	@GetMapping("/search-violator")
	public ModelAndView searchViolator( @RequestParam("keyword") String name ) {

		if( Objects.isNull(name) || name.trim().isEmpty()) {
			return new ModelAndView( "redirect:/violators" );
		}
		
		ModelAndView mav = new ModelAndView();
		List<Violator> violators = violatorService.findViolatorsByName(name);
		mav.addObject("keyword", name);
		mav.addObject("violators", violators);
		mav.setViewName("violator-dashboard.html");
		
		return mav;
	}
	
	@RequestMapping("/add-violator")
	public String addViolator(Model model) {
		
		model.addAttribute("violator", new Violator());
		
		return "add-violator.html";
	}
	
	@GetMapping("/violator/{id}")
	public ModelAndView getSpecificViolator(@PathVariable("id") Long id) {
		
		List<Vessel> vesselsOwned = vesselService.findVesselsOwnedByViolator(violatorService.findById(id));
		ModelAndView mav = serviceHelper.getSpecific(violatorService, id);
		Violator violator = violatorService.findById(id);
		boolean isOwner = false;
		if( !vesselsOwned.isEmpty() ) {
			isOwner = true;
			mav.addObject("vessels", vesselsOwned);
		}
		
		mav.addObject("isOwner", isOwner);
		mav.addObject("age", Utilities.calculateAge(violator.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()));
		return mav;
	}
	
	@PostMapping("/submit-violator")
	public String saveViolator(@ModelAttribute("violator") Violator violator, @RequestParam(value="files", required=false) List<MultipartFile> files, 
			@RequestParam(value="media_id[]", required=false) List<String> removedMediaIds) {
		
		Violator savedViolator = violatorService.saveAndFlush(violator);
		
		mediaService.saveAll( ListConversionHelper.stringToMedia( fileHelper.uploadMultipleFiles(files, new BucketBeanHelper(bucketName, filePath) ), savedViolator ) );
		
		if( !Objects.isNull(removedMediaIds) && !removedMediaIds.isEmpty() ) {	
			removedMediaIds.forEach( mediumId -> mediaService.deleteMedium( Long.parseLong(mediumId) ) );
		}
		
		System.out.println("new violator: " + savedViolator);
		
		return "redirect:/add-violator";
	}
	
	@GetMapping("/violator/image/{id}")
	public void showViolationImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jfif"); 
		Media medium = mediaService.findById(id);

		if( !Objects.isNull(medium) ) {
			InputStream is = new ByteArrayInputStream( s3Service.downloadFile( medium.getMediaFilePath(), new BucketBeanHelper(bucketName, filePath) ) );
			IOUtils.copy(is, response.getOutputStream());
		}
	}
	
	@GetMapping("/edit/violator/{id}")
	public String updateViolation(Model model, @PathVariable("id") Long id) {
		Violator violator = violatorService.findById(id);
		List<Media> media = mediaService.findMediaByViolator(violator);
		
		model.addAttribute("violator", violator);
		model.addAttribute("media", media);
		model.addAttribute("update", true);
		return "add-violator.html";
	}

	
//	@PostMapping("/add-violator")
//	@ResponseBody
//	public Long addViolator(@RequestBody Violator violator) {
//		violatorService.save(violator);
//		return violator.getId();
//	}
//	
//	@GetMapping("/violators")
//	public List<Violator> getViolators() {
//		return violatorService.getAllViolators();
//	}
//	
//	@PutMapping("/update-violator")
//	public Violator updateViolation(@RequestBody Violator violator) {
//		violatorService.update(violator);
//		return violator;
//	}
//	
//	@DeleteMapping("/violator/{violatorId}")  
//	private void deleteViolation(@PathVariable("violatorId") Long violatorId)   
//	{  
//		violatorService.delete(violatorId);
//	}  
	
}
