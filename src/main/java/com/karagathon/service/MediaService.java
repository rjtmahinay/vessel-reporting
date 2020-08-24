package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.IModel;
import com.karagathon.model.Media;
import com.karagathon.model.Report;
import com.karagathon.model.Vessel;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.repository.MediaRepository;

@Service
public class MediaService implements ApplicationService{
	
	@Autowired  
	private MediaRepository mediaRepository;
	
	public List<Media> findMediaByViolation(Violation violation) {
		return mediaRepository.findByViolation(violation);
	}
	
	public List<Media> findMediaByViolator(Violator violator) {
		return mediaRepository.findByViolator(violator);
	}
	
	private List<Media> findMediaByReport(Report report) {
		return mediaRepository.findByReport(report);
	}
	
	public List<Media> findMediaByModel( IModel model ) {
		if( model instanceof Violator ) {
			return mediaRepository.findByViolator( (Violator) model );
		}else if( model instanceof Violation ) {
			return mediaRepository.findByViolation( (Violation) model );
		}else if( model instanceof Report ) {
			return mediaRepository.findByReport( (Report) model );
		}else {
			return mediaRepository.findByVessel( (Vessel) model );
		}
	}
	
	public void save(Media medium) {
		mediaRepository.save(medium);
	}
	
	public List<Media> saveAll(Iterable<Media> media) {
		return mediaRepository.saveAll(media);
	}
	
	public void deleteMedium(Long id) {
		mediaRepository.deleteById(id);
	}
	
	public Media findById(Long id) {
		return mediaRepository.findById(id).orElse(null);
	}
	

} 
