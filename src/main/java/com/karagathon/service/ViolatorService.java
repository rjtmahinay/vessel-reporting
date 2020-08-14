package com.karagathon.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.karagathon.model.Violator;
import com.karagathon.repository.ViolatorRepository;

@Service
public class ViolatorService {
	
	@Autowired  
	private ViolatorRepository violatorRepository;  
	
	public List<Violator> getAllViolators() {	
		return violatorRepository.findAll();
	}
	
	public Violator save(Violator violator) {
		violator.setId(Long.valueOf(0L));
		return violatorRepository.save(violator);
	}
	
	public Violator saveAndFlush(Violator violator) {
		violator.setId(Long.valueOf(0L));
		return violatorRepository.saveAndFlush(violator);
	}
	
	public void update(Violator violator) {
		violatorRepository.save(violator);
	}
	
	public void delete(Long id) {
		violatorRepository.deleteById(id);
	}
	
	public List<Violator> getViolatorsByIds(List<Long> violatorIds) {
		return violatorRepository.findByViolatorIds(violatorIds);
	}
	
	public Violator findById(Long id) {
		return violatorRepository.findById(id).orElse(null);
	}
} 
