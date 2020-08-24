package com.karagathon.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.repository.ViolationRepository;

@Service
public class ViolationService implements ApplicationService{
	
	@Autowired  
	private ViolationRepository violationRepository;  
	
	public List<Violation> getAllViolations() {
		return violationRepository.findAll();
	}
	
	public void save(Violation violation) {
		violationRepository.save(violation);
	}
	
	public Violation saveAndFlush(Violation violation) {
		return violationRepository.saveAndFlush(violation);
	}
	
	public void update(Violation violation) {
		violationRepository.save(violation);
	}
	
	public void delete(Long id) {
		violationRepository.deleteById(id);
	}
	
	public Violation findById(Long id) {
		return violationRepository.findById(id).orElse(null);
	}
	
	public List<Violation> findViolationsByTitle( String title ) {
		if( Objects.isNull(title) || title.isEmpty() ) {
			return violationRepository.findAll();
		}
		
		return violationRepository.searchViolationByTitle(title);
	}
	
}
