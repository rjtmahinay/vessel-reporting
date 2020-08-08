package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Media;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;
import com.karagathon.repository.MediaRepository;

@Service
public class MediaService {
	
	@Autowired  
	private MediaRepository mediaRepository;
	
	public List<Media> findMediaByViolation(Violation violation) {
		return mediaRepository.findByViolation(violation);
	}
	
	public List<Media> findMediaByViolator(Violator violator) {
		return mediaRepository.findByViolator(violator);
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
	

} 
