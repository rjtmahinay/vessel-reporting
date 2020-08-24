package com.karagathon.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="vessel")
public class Vessel implements IModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="vessel_name")
	private String vesselName;
	
	@Column(name="license_id")
	private String licenseId;
	
	@Column(name="license_expiry")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date licenseExpiry;
	
	@Column(name="license_type")
	private String licenseType;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			 CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="violation_vessel",
			joinColumns=@JoinColumn(name="vessel_id"),
			inverseJoinColumns=@JoinColumn(name="violation_id")
			)
	private List<Violation> violations;
	
	@ManyToOne
	@JoinColumn(name = "violator_id")
	private Violator violator;
	
	@OneToMany(mappedBy = "vessel")
	private List<Media> media;
	
	public Vessel() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public Date getLicenseExpiry() {
		return licenseExpiry;
	}

	public void setLicenseExpiry(Date licenseExpiry) {
		this.licenseExpiry = licenseExpiry;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public List<Violation> getViolations() {
		return violations;
	}

	public void setViolations(List<Violation> violations) {
		this.violations = violations;
	}

	public Violator getViolator() {
		return violator;
	}

	public void setViolator(Violator violator) {
		this.violator = violator;
	}

	public String getLicenseId() {
		return licenseId;
	}



	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}



	public List<Media> getMedia() {
		return media;
	}



	public void setMedia(List<Media> media) {
		this.media = media;
	}

	@Override
	public String toString() {
		return "Vessel [id=" + id + ", vesselName=" + vesselName + ", licenseId=" + licenseId + ", licenseExpiry="
				+ licenseExpiry + ", licenseType=" + licenseType + "]";
	}
	
	
}
