package com.karagathon.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.helper.ModelStatistics;
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
	
	@Query(value="SELECT months.month as month, COUNT(r.date_of_report) as value" + 
			"            FROM" + 
			"            (" + 
			"						SELECT 1 AS MONTH" + 
			"                       UNION SELECT 2 AS MONTH" + 
			"                       UNION SELECT 3 AS MONTH" + 
			"                       UNION SELECT 4 AS MONTH" + 
			"                       UNION SELECT 5 AS MONTH" + 
			"                       UNION SELECT 6 AS MONTH" + 
			"                       UNION SELECT 7 AS MONTH" + 
			"                       UNION SELECT 8 AS MONTH" + 
			"                       UNION SELECT 9 AS MONTH" + 
			"                       UNION SELECT 10 AS MONTH" + 
			"                       UNION SELECT 11 AS MONTH" + 
			"                       UNION SELECT 12 AS MONTH ) as months" + 
			"			LEFT JOIN report r ON months.month = MONTH(r.date_of_report)" + 
			"			AND YEAR(r.date_of_report) = :year"	 +		
			"            GROUP BY months.month", nativeQuery=true)
	List<ModelStatistics> getMonthStatisticsFromReport(@Param("year") String year);
} 
