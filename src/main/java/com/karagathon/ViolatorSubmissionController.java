package com.karagathon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.service.ViolatorService;

@RestController
public class ViolatorSubmissionController {
	
	@Autowired
	ViolatorService violatorService;
	
	@PostMapping("/add-violator")
	@ResponseBody
	public Long addViolator(@RequestBody Violator violator) {
		violatorService.save(violator);
		return violator.getId();
	}
	
	@GetMapping("/violators")
	public List<Violator> getViolators() {
		return violatorService.getAllViolators();
	}
	
	@PutMapping("/update-violator")
	public Violator updateViolation(@RequestBody Violator violator) {
		violatorService.update(violator);
		return violator;
	}
	
	@DeleteMapping("/violator/{violatorId}")  
	private void deleteViolation(@PathVariable("violatorId") Long violatorId)   
	{  
		violatorService.delete(violatorId);
	}  
	
}
