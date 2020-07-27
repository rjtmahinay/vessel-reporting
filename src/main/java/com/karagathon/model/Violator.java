package com.karagathon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table
public class Violator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long violatorId;
	private String name;
	private String vesselId;
	@Lob
	private byte[] picture;
	@Lob
	private byte[] video;
	
	
	
	public long getViolatorId() {
		return violatorId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVesselId() {
		return vesselId;
	}
	public void setVesselId(String vesselId) {
		this.vesselId = vesselId;
	}
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
