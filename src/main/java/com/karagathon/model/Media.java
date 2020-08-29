package com.karagathon.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "media")
public class Media implements IModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "report_id")
	@JsonBackReference
	private Report report;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "violation_id")
	private Violation violation;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "violator_id")
	private Violator violator;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vessel_id")
	private Vessel vessel;

	private String mediaFilePath;

	private String mediaType;

	public Media() {
	}

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

	public Media(String mediaFilePath, Report report) {
		setMediaFilePath(mediaFilePath);
		setReport(report);
	}

	public Media(String mediaFilePath, IModel model) {
		setMediaFilePath(mediaFilePath);
		setMediaTypeOfMediaFile(mediaFilePath);

		if (model instanceof Violator) {
			setViolator((Violator) model);
		} else if (model instanceof Violation) {
			setViolation((Violation) model);
		} else if (model instanceof Report) {
			setReport((Report) model);
		} else {
			setVessel((Vessel) model);
		}

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

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Vessel getVessel() {
		return vessel;
	}

	public void setVessel(Vessel vessel) {
		this.vessel = vessel;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", mediaFilePath=" + mediaFilePath + "]";
	}

	private void setMediaTypeOfMediaFile(String mediaFilePath) {

		if (FilenameUtils.getExtension(mediaFilePath).equals("mp4")) {
			setMediaType("VIDEO");
		} else {
			setMediaType("PICTURE");
		}
		System.out.println("MEDIA: " + getMediaFilePath());
		System.out.println("MEDIA TYPE: " + getMediaType());
	}
}
