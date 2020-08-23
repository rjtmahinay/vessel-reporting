package com.karagathon.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "report")
//@SecondaryTable(name = "media")
public class Report implements IModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Length(max = 1000)
	private String location;
	
	@Column(name="date_of_report")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateOfReport;
	
	@OneToMany(mappedBy = "report")
	@JsonManagedReference
	private List<Media> media;
	
	@OneToOne(mappedBy = "report")
	private Location locationPosition;
	
	@Length(max = Integer.MAX_VALUE)
	@Column(length=Integer.MAX_VALUE)
	private String description;
	

	public Report() {}

	public Report(String name, @Length(max = 1000) String location, Date dateOfReport,
			@Length(max = 2147483647) String description) {
		super();
		this.name = name;
		this.location = location;
		this.dateOfReport = dateOfReport;
		this.description = description;
	}
	
	public Report( Long id, String name, List<Media> media, String description ) {
		
	}

	public Report() {}
	
	public Report(Long id, String name, List<Media> media, @Length(max = 2147483647) String description) {
		this.id = id;
		this.name = name;
		this.media = media;
		this.description = description;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Media> getMedia() {
		return media;
	}


	public void setMedia(List<Media> media) {
		this.media = media;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}




	public String getLocation() {
		return location;
	}



	public void setLocation(String location) {
		this.location = location;
	}



	public Date getDateOfReport() {
		return dateOfReport;
	}



	public void setDateOfReport(Date dateOfReport) {
		this.dateOfReport = dateOfReport;
	}
	
	

	@Override
	public String toString() {
		return "Report [id=" + id + ", name=" + name + ", location=" + location + ", dateOfReport=" + dateOfReport
				+ ", description=" + description + "]";
	}

}
