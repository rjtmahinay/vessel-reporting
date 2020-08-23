package com.karagathon.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

@Entity
public class Notification implements IModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="notification_status", columnDefinition="tinyint(1) default 1")
	private boolean notificationStatus;
	
	private String subject;
	
	@Length(max = 1000)
	private String text;
	
	private String url;
	
	public Notification() {}
	
	public Notification(boolean notificationStatus, String subject, @Length(max = 1000) String text, String url) {
		this.notificationStatus = notificationStatus;
		this.subject = subject;
		this.text = text;
		this.url = url;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(boolean notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", notificationStatus=" + notificationStatus + ", subject=" + subject
				+ ", text=" + text + ", url=" + url + "]";
	}
	
	
}
