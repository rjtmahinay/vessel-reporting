package com.karagathon.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Location implements IModel {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "report_id")
	private Report report;
	
	private BigDecimal longitude;
	
	private BigDecimal latitude;
	
	public Location() {}

	public Location(BigDecimal longitude, BigDecimal latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	
	public Location(BigDecimal longitude, BigDecimal latitude, Report report) {
		this( longitude, latitude );
		setReport(report);
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", report=" + report + ", longitude=" + longitude + ", latitude=" + latitude
				+ "]";
	}
	
	
	
}
