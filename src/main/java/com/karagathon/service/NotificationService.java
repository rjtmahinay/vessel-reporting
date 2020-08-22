package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.karagathon.model.Notification;
import com.karagathon.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	NotificationRepository notificationRepository;
	
	public Notification saveAndFlush( final Notification notification ) {
		return notificationRepository.saveAndFlush(notification);
	}
	
	public List<Notification> getNotifications( final int limit ) {
		return notificationRepository.findAll( Sort.by(Sort.Direction.DESC, "id") );
	}
	
	public List<Notification> getUnseenNotifications( final boolean notificationStatus ){
		return notificationRepository.findByNotificationStatus(notificationStatus);
	}
	
	
}
