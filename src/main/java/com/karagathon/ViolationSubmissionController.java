package com.karagathon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
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

import com.karagathon.aws.service.AWSS3Service;
import com.karagathon.helper.BucketBeanHelper;
import com.karagathon.helper.FileHelper;
import com.karagathon.helper.LabelValueBeanHelper;
import com.karagathon.helper.ListConversionHelper;
import com.karagathon.helper.SpecificServiceHelper;
import com.karagathon.model.Media;
import com.karagathon.model.Vessel;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.service.MediaService;
import com.karagathon.service.VesselService;
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
	VesselService vesselService;
	
	@Autowired
	FileHelper fileHelper;
	
	@Autowired
	AWSS3Service s3Service;
	
	@Autowired
	SpecificServiceHelper serviceHelper;
	
	@Value("${violation.file.path}")
	String destination;
	
	@Value("${aws.s3.violation.filePath}")
	String filePath;
	
	@Value("${aws.s3.violation.bucket}")
	String bucketName;
	
	@RequestMapping("/violations")
	public String dashboard(Model model) {
		List<Violation> violations = violationService.getAllViolations();		
		model.addAttribute("violations", violations);
		return "violation-dashboard.html";
	}
	
	@GetMapping("/search-violation")
    public ModelAndView searchViolation(@RequestParam("keyword") String title) {
		
		if( Objects.isNull(title) || title.isEmpty() || title.isBlank() ) {
			return new ModelAndView("redirect:/violations");
		}
		ModelAndView mav = new ModelAndView();
		List<Violation> violations = violationService.findViolationsByTitle(title);		
		
		mav.addObject("violations", violations);
		mav.addObject("keyword", title);
		mav.setViewName("violation-dashboard");
		
		return mav;
    }
	
	@GetMapping("/violation/{id}")
	public ModelAndView getSpecificViolation(@PathVariable("id") Long id) {
		return serviceHelper.getSpecific(violationService, id);
	}
	
	@RequestMapping("/add-violation")
	public String addViolation(Model model) {
		Violation violation = new Violation();
		
		model.addAttribute("violation", violation);
		
		return "add-violation.html";
	}
	
	@PostMapping("/submit-violation")
	public String saveViolation(@ModelAttribute("violation") Violation violation, @RequestParam("violator_id[]") List<Violator> violators, 
								@RequestParam(value="files", required=false) List<MultipartFile> files, @RequestParam(value="media_id[]", required=false) List<String> removedMediaIds, 
								@RequestParam(value="vessel_id[]") List<Vessel> vessels) {
		violation.setViolators( violators );
		violation.setVessels( vessels );
		System.out.println(violation);
		
		Violation savedViolation = violationService.saveAndFlush(violation);
		
		System.out.println("saved violation");
		System.out.println(savedViolation);
		
		mediaService.saveAll( ListConversionHelper.stringToMedia( fileHelper.uploadMultipleFiles(files, new BucketBeanHelper(bucketName, filePath) ), savedViolation ) );
		
		if( !Objects.isNull(removedMediaIds) && !removedMediaIds.isEmpty() ) {
			removedMediaIds.forEach( mediumId -> mediaService.deleteMedium( Long.parseLong(mediumId) ) );
		}
		return "redirect:/add-violation";
	}
	

	@GetMapping("/violation/image/{id}")
	public void showViolationImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
		// tantan - generalize to include videos
		response.setContentType("image/jfif"); 
		Media medium = mediaService.findById(id);

		if( !Objects.isNull(medium) ) {
			InputStream is = new ByteArrayInputStream( s3Service.downloadFile( medium.getMediaFilePath(), new BucketBeanHelper(bucketName, filePath) ) );
			IOUtils.copy(is, response.getOutputStream());
		}
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
	
	
}
