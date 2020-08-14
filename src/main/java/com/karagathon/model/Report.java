package com.karagathon.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "report")
//@SecondaryTable(name = "media")
public class Report{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "report")
	private List<Media> media;
	
	
	@Length(max = Integer.MAX_VALUE)
	private String description;
	
	
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
}
