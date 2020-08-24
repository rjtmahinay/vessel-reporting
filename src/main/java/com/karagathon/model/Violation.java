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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name="violation")
public class Violation implements IModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Length(max = 100)
	private String title;
	
	@Length(max = Integer.MAX_VALUE)
	private String description;
	
	@Length(max = 1000)
	private String location;
	
	@Column(name="date_of_violation")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateOfViolation;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			 CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="violation_violator",
			joinColumns=@JoinColumn(name="violation_id"),
			inverseJoinColumns=@JoinColumn(name="violator_id")
			)
	private List<Violator> violators;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.PERSIST, CascadeType.MERGE,
			 CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="violation_vessel",
			joinColumns=@JoinColumn(name="violation_id"),
			inverseJoinColumns=@JoinColumn(name="vessel_id")
			)
	private List<Vessel> vessels;
	
	@OneToMany(mappedBy = "violation")
	private List<Media> media;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Violator> getViolators() {
		return violators;
	}

	public void setViolators(List<Violator> violators) {
		this.violators = violators;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}

	public Date getDateOfViolation() {
		return dateOfViolation;
	}

	public void setDateOfViolation(Date dateOfViolation) {
		this.dateOfViolation = dateOfViolation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Vessel> getVessels() {
		return vessels;
	}

	public void setVessels(List<Vessel> vessels) {
		this.vessels = vessels;
	}

	@Override
	public String toString() {
		return "Violation [id=" + id + ", title=" + title + ", description=" + description + ", location=" + location
				+ ", dateOfViolation=" + dateOfViolation + ", violators=" + violators + ", vessels=" + vessels
				+ ", media=" + media + "]";
	}

	

}
