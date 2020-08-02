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
import com.karagathon.service.ViolationService;

@RestController
public class ViolationSubmissionController {
	
	@Autowired
	ViolationService violationService;
	
	@PostMapping("/submit-violation")
	@ResponseBody
	public Long submitViolation(@RequestBody Violation violation) {
		violationService.save(violation);
		return violation.getId();
	}
	
	@GetMapping("/violations")
	public List<Violation> getViolations() {
		return violationService.getAllViolations();
	}
	
	@PutMapping("/update-violation")
	public Violation updateViolation(@RequestBody Violation violation) {
		violationService.update(violation);
		return violation;
	}
	
	@DeleteMapping("/violation/{violationId}")  
	private void deleteViolation(@PathVariable("violationId") Long violationId)   
	{  
		violationService.delete(violationId);
	}  
	
}
