package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Violation;
import com.karagathon.repository.ViolationRepository;

@Service
public class ViolationService {
	
	@Autowired  
	private ViolationRepository violationRepository;  
	
	public List<Violation> getAllViolations() {
		return violationRepository.findAll();
	}
	
	public void save(Violation violation) {
		violation.setId(Long.valueOf(0L));
		violationRepository.save(violation);
	}
	
	public void update(Violation violation) {
		violationRepository.save(violation);
	}
	
	public void delete(Long id) {
		violationRepository.deleteById(id);
	}
}
