package com.karagathon.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
	
	public List<Notification> findByNotificationStatus( boolean notificationStatus );
	
	@Modifying
	@Transactional
	@Query(value="UPDATE notification SET notification_status = 1 where notification_status = 0", nativeQuery=true)
	public void updateUnseenNotifications();
	
	public List<Notification> findAllByNotificationStatus( boolean notificationStatus, Pageable pageable );
} 
