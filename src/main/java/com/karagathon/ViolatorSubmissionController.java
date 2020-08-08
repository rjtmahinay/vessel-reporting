package com.karagathon;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

import com.karagathon.helper.FileHelper;
import com.karagathon.helper.ListConversionHelper;
import com.karagathon.model.Media;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.service.MediaService;
import com.karagathon.service.ViolationService;
import com.karagathon.service.ViolatorService;

@Controller
public class ViolatorSubmissionController {
	
	@Autowired
	ViolatorService violatorService;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	FileHelper fileHelper;
	
	@Value("${violation.file.path}")
	String destination;
	
	@RequestMapping("/violator-dashboard")
	public String dashboard(Model model) {
		List<Violator> violators = violatorService.getAllViolators();
		model.addAttribute("violators", violators);
		
		return "violator-dashboard.html";
	}
	
	@RequestMapping("/add-violator")
	public String addViolator(Model model) {
		
		model.addAttribute("violator", new Violator());
		
		return "add-violator.html";
	}
	
	@GetMapping("/violator/{id}")
	public ModelAndView getSpecificViolator(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		Violator violator = violatorService.findById(id);
		
		if(!Objects.isNull(violator)) {
			List<Media> media = mediaService.findMediaByViolator(violator);
			mav.addObject("violator", violator);
			mav.addObject("media", media);
			mav.setViewName("violator-specific.html");
		}else{
			mav.addObject("error", "error");
			mav.setViewName("error.html");
		}
		
		return mav;
	}
	
	@PostMapping("/submit-violator")
	public String saveViolator(@ModelAttribute("violator") Violator violator, @RequestParam(value="files", required=false) List<MultipartFile> files, 
			@RequestParam(value="media_id[]", required=false) List<String> removedMediaIds) {
		
		Violator savedViolator = violatorService.saveAndFlush(violator);
		
		mediaService.saveAll( ListConversionHelper.stringToMedia( fileHelper.moveUploadedFile(files, destination), savedViolator ) );
		
		if( !Objects.isNull(removedMediaIds) && !removedMediaIds.isEmpty() ) {	
			removedMediaIds.forEach( mediumId -> mediaService.deleteMedium( Long.parseLong(mediumId) ) );
		}
		
		return "redirect:/add-violator";
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
