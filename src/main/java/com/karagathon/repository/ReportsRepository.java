package com.karagathon.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Report;
import com.karagathon.model.Violation;

@Repository
public interface ReportsRepository extends JpaRepository<Report, Long>{
	
	@Query("SELECT r FROM Report r")
	List<Report> getReports();
	
	@Modifying
	@Query(value = "INSERT INTO Report (picture) VALUES (:uploadedPicture)", nativeQuery = true)
	@Transactional
	void uploadImage(@Param("uploadedPicture") byte[] picture);
	
	
	@Query("SELECT v FROM Report v where v.description LIKE CONCAT('%',:description,'%')")    
    List<Report> searchReportByDescription(@Param("description") String description);
} 
