package com.karagathon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.helper.ModelStatistics;
import com.karagathon.model.Media;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long> {
	
	@Query("SELECT v FROM Violation v where v.title LIKE CONCAT('%',:title,'%')")    
    List<Violation> searchViolationByTitle(@Param("title") String title);
	
	@Query(value="SELECT months.month as month, COUNT(v.date_of_violation) as value" + 
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
			"			LEFT JOIN violation v ON months.month = MONTH(v.date_of_violation)" + 
			"			AND YEAR(v.date_of_violation) = :year"	 +		
			"            GROUP BY months.month;", nativeQuery=true)
	List<ModelStatistics> getMonthStatisticsFromViolation(@Param("year") String year);
}
