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

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name="violation")
public class Violation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
	
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
	
	@OneToMany(mappedBy = "violation")
	private List<Media> media;

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

	@Override
	public String toString() {
		return "Violation [id=" + id + ", description=" + description + ", dateOfViolation=" + dateOfViolation
				+ ", violators=" + violators + ", media=" + media + "]";
	}
	
	
	
	
}
