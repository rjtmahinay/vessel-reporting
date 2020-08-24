package com.karagathon.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.karagathon.model.Vessel;
import com.karagathon.model.Violator;
import com.karagathon.repository.VesselRepository;

@Service
public class VesselService implements ApplicationService {

	@Autowired 
	VesselRepository vesselRepository;
	
	public List<Vessel> getAllVessels() {
		return vesselRepository.findAll();
	}
	
	public Vessel saveAndFlush( Vessel vessel ) {
		return vesselRepository.saveAndFlush(vessel);
	}

	@Override
	public Vessel findById(Long id) {
		return vesselRepository.findById(id).orElse(null);
	}
	
	public List<Vessel> findVesselsOwnedByViolator( Violator violator ){
		return vesselRepository.findVesselByViolator(violator);
	}
	
	public List<Vessel> findVesselByName( final String name ) {
		if(Objects.isNull(name) || name.trim().isEmpty()) {
			return vesselRepository.findAll();
		}
		
		return vesselRepository.searchVesselByName(name);
	}
	
//	public List<Vessel> findByViolation(Violation violation) {
//		return vesselRepository.findByViolation(violation);
//	}
	
}
