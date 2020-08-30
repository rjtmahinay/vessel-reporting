package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		Pageable topLimit = PageRequest.of(0, limit,  Sort.by(Sort.Direction.DESC, "id"));
		return notificationRepository.findAllByNotificationStatus( false, topLimit );
	}
	
	public List<Notification> getUnseenNotifications( final boolean notificationStatus ){
		return notificationRepository.findByNotificationStatus(notificationStatus);
	}
	
	public void updateUnseenNotifications() {
		notificationRepository.updateUnseenNotifications();
	}
	
}
