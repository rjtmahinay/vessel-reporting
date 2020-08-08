package com.karagathon;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.karagathon.helper.FileHelper;
import com.karagathon.helper.LabelValueBeanHelper;
import com.karagathon.helper.ListConversionHelper;
import com.karagathon.model.Media;
import com.karagathon.model.Violation;
import com.karagathon.repository.MediaRepository;
import com.karagathon.service.MediaService;
import com.karagathon.service.ViolationService;
import com.karagathon.service.ViolatorService;

@Controller
public class ViolationSubmissionController {
	
	@Autowired
	ViolationService violationService;
	
	@Autowired
	ViolatorService violatorService;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	FileHelper fileHelper;
	
	@Value("${violation.file.path}")
	String destination;
	
	@RequestMapping("/violation-dashboard")
	public String dashboard(Model model) {
		List<Violation> violations = violationService.getAllViolations();
		
		model.addAttribute("violations", violations);
		
		return "violation-dashboard.html";
	}
	
	@GetMapping("/violation/{id}")
	public ModelAndView getSpecificViolation(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView();
		Violation violation = violationService.findById(id);
		
		if(!Objects.isNull(violation)) {
			List<Media> media = mediaService.findMediaByViolation(violation);
			mav.addObject("violation", violation);
			mav.addObject("media", media);
			mav.setViewName("violation-specific.html");
		}else{
			mav.addObject("error", "error");
			mav.setViewName("error.html");
		}
		
		return mav;
	}
	
	@RequestMapping("/add-violation")
	public String addViolation(Model model) {
		Violation violation = new Violation();
		
		model.addAttribute("violation", violation);
		
		return "add-violation.html";
	}
	
	@PostMapping("/submit-violation")
	public String saveViolation(@ModelAttribute("violation") Violation violation, @RequestParam("violator_id[]") List<String> violatorIds, 
								@RequestParam(value="files", required=false) List<MultipartFile> files, @RequestParam(value="media_id[]", required=false) List<String> removedMediaIds) {
		
		violation.setViolators(violatorService.getViolatorsByIds( ListConversionHelper.stringToLong(violatorIds) ));
		
		System.out.println(violation);
		
		Violation savedViolation = violationService.saveAndFlush(violation);
		
		System.out.println("saved violation");
		System.out.println(savedViolation);
		
		files.forEach(f -> System.out.println(f.getOriginalFilename()));
		
		mediaService.saveAll( ListConversionHelper.stringToMedia( fileHelper.moveUploadedFile(files, destination), savedViolation ) );
		
		
		if( !Objects.isNull(removedMediaIds) && !removedMediaIds.isEmpty() ) {
			removedMediaIds.forEach( mediumId -> mediaService.deleteMedium( Long.parseLong(mediumId) ) );
		}
		return "redirect:/add-violation";
	}
	
	
	@RequestMapping(value="/violatorsAutoComplete")
	@ResponseBody
	public List<LabelValueBeanHelper> violatorsAutoComplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {	
		String upperCaseTerm = term.toUpperCase();
		return violatorService.getAllViolators()
							.stream()
							.filter(violator -> !Objects.isNull(violator.getFirstName()) && violator.getFirstName().toUpperCase().startsWith(upperCaseTerm) || 
									!Objects.isNull(violator.getLastName()) && violator.getLastName().toUpperCase().startsWith(upperCaseTerm))
							.map(violator -> new LabelValueBeanHelper(violator.getId().toString(), violator.getLastName() + ", " + violator.getFirstName() ))
							.collect(Collectors.toList());
 	}
	
	@GetMapping("/edit/violation/{id}")
	public String updateViolation(Model model, @PathVariable("id") Long id) {
		Violation violation = violationService.findById(id);
		List<Media> media = mediaService.findMediaByViolation(violation);
		
		model.addAttribute("violation", violation);
		model.addAttribute("media", media);
		model.addAttribute("update", true);
		return "add-violation.html";
	}
	
//	@PostMapping("/submit-violation")
//	@ResponseBody
//	public Long submitViolation(@RequestBody Violation violation) {
//		violationService.save(violation);
//		return violation.getId();
//	}
//	
//	@GetMapping("/violations")
//	public List<Violation> getViolations() {
//		return violationService.getAllViolations();
//	}
//	
//	@PutMapping("/update-violation")
//	public Violation updateViolation(@RequestBody Violation violation) {
//		violationService.update(violation);
//		return violation;
//	}
//	
//	@DeleteMapping("/violation/{violationId}")  
//	private void deleteViolation(@PathVariable("violationId") Long violationId)   
//	{  
//		violationService.delete(violationId);
//	}  
	
}
