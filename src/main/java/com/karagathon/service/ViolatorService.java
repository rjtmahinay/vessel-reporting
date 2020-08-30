package com.karagathon.service;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.karagathon.model.Violator;
import com.karagathon.repository.ViolatorRepository;

@Service
public class ViolatorService implements ApplicationService{
	
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
	
	public List<Violator> findViolatorsByName( String name ) {
		if( Objects.isNull(name) || name.trim().isEmpty() ) {
			return violatorRepository.findAll();
		}
		
		return violatorRepository.searchViolatorByName(name);
	}
} 
