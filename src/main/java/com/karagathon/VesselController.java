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
import com.karagathon.service.ViolatorService;

@Controller
public class VesselController {
	
	@Autowired
	FileHelper fileHelper;
	
	@Autowired
	ViolatorService violatorService;
	
	@Autowired
	VesselService vesselService;
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	AWSS3Service s3Service;
	
	@Autowired
	SpecificServiceHelper serviceHelper;
	
	@Value("${aws.s3.vessel.filePath}")
	String filePath;
	
	@Value("${aws.s3.vessel.bucket}")
	String bucketName;
	
	@RequestMapping("/vessels")
	public String dashboard(Model model) {
		List<Vessel> vessels = vesselService.getAllVessels();
		
		model.addAttribute("vessels", vessels);
		
		return "vessel-dashboard.html";
	}
	
	@RequestMapping("/add-vessel")
	public String addVessel(Model model) {
		Vessel vessel = new Vessel();
		
		model.addAttribute("vessel", vessel);
		model.addAttribute("owners", violatorService.getAllViolators());
		
		return "add-vessel.html";
	}	
	
	
	@GetMapping("/vessel/{id}")
	public ModelAndView getSpecificViolation(@PathVariable("id") Long id) {
		return serviceHelper.getSpecific(vesselService, id);
	}
	
	@RequestMapping(value="/vesselsAutoComplete")
	@ResponseBody
	public List<LabelValueBeanHelper> vesselsAutoComplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {	
		String upperCaseTerm = term.toUpperCase();
		return vesselService.getAllVessels()
							.stream()
							.filter(vessel -> !Objects.isNull(vessel.getVesselName()) && vessel.getVesselName().toUpperCase().startsWith(upperCaseTerm))
							.map(vessel -> new LabelValueBeanHelper(vessel.getId().toString(), vessel.getVesselName() ))
							.collect(Collectors.toList());
 	}
	
	@PostMapping("/submit-vessel")
	public String saveVessels(@ModelAttribute("vessel") Vessel vessel, @RequestParam(value="files", required=false) List<MultipartFile> files, 
											@RequestParam(value="media_id[]", required=false) List<String> removedMediaIds, @RequestParam(value="owner") Violator violator) {
		vessel.setViolator( violator );
		Vessel savedVessel = vesselService.saveAndFlush(vessel);
		
		System.out.println("saved vessel");
		System.out.println(savedVessel);
		
		mediaService.saveAll( ListConversionHelper.stringToMedia( fileHelper.uploadMultipleFiles(files, new BucketBeanHelper(bucketName, filePath) ), savedVessel ) );
		
		if( !Objects.isNull(removedMediaIds) && !removedMediaIds.isEmpty() ) {
			removedMediaIds.forEach( mediumId -> mediaService.deleteMedium( Long.parseLong(mediumId) ) );
		}
		return "redirect:/add-vessel";
	}
	
	@GetMapping("/vessel/image/{id}")
	public void showVesselImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
		response.setContentType("image/jfif"); 
		Media medium = mediaService.findById(id);

		if( !Objects.isNull(medium) ) {
			InputStream is = new ByteArrayInputStream( s3Service.downloadFile( medium.getMediaFilePath(), new BucketBeanHelper(bucketName, filePath) ) );
			IOUtils.copy(is, response.getOutputStream());
		}
	}
	
	@GetMapping("/edit/vessel/{id}")
	public String updateVessel(Model model, @PathVariable("id") Long id) {
		Vessel vessel = vesselService.findById(id);
		List<Media> media = mediaService.findMediaByModel(vessel);
		
		model.addAttribute("vessel", vessel);
		model.addAttribute("media", media);
		model.addAttribute("owners", violatorService.getAllViolators());
		model.addAttribute("update", true);
		return "add-vessel.html";
	}
	
	@GetMapping("/search-vessel")
	public ModelAndView searchVessel( @RequestParam("keyword") String name ) {
		if( Objects.isNull(name) || name.trim().isEmpty()) {
			return new ModelAndView( "redirect:/vessels" );
		}
		
		ModelAndView mav = new ModelAndView();
		List<Vessel> vessels = vesselService.findVesselByName(name);
		mav.addObject("keyword", name);
		mav.addObject("vessels", vessels);
		mav.setViewName("vessel-dashboard.html");
		
		return mav;
	}
	
	
}
