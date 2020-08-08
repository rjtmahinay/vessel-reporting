package com.karagathon.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "media")
public class Media {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "report_id")
	private Report report;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "violation_id")
	private Violation violation;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "violator_id")
	private Violator violator;
	
	private String mediaFilePath;
	
	public Media() {}
	
	public Media(String mediaFilePath) {
		setMediaFilePath(mediaFilePath);
	}
	
	public Media(String mediaFilePath, Violation violation) {
		setMediaFilePath(mediaFilePath);
		setViolation(violation);
	}
	
	public Media(String mediaFilePath, Violator violator) {
		setMediaFilePath(mediaFilePath);
		setViolator(violator);
	}

	public String getMediaFilePath() {
		return mediaFilePath;
	}

	public void setMediaFilePath(String mediaFilePath) {
		this.mediaFilePath = mediaFilePath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Violation getViolation() {
		return violation;
	}

	public void setViolation(Violation violation) {
		this.violation = violation;
	}

	public Violator getViolator() {
		return violator;
	}

	public void setViolator(Violator violator) {
		this.violator = violator;
	}
	
	
}
