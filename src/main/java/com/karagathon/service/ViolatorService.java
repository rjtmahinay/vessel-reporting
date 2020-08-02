package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.repository.ViolatorRepository;

@Service
public class ViolatorService {
	
	@Autowired  
	private ViolatorRepository violatorRepository;  
	
	public List<Violator> getAllViolators() {
		return violatorRepository.findAll();
	}
	
	public void save(Violator violator) {
		violator.setId(Long.valueOf(0L));
		violatorRepository.save(violator);
	}
	
	public void update(Violator violator) {
		violatorRepository.save(violator);
	}
	
	public void delete(Long id) {
		violatorRepository.deleteById(id);
	}
}
