package com.karagathon.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Violation;
import com.karagathon.model.Violator;

@Repository
public interface ViolatorRepository extends JpaRepository<Violator, Long> {
	
	@Query("SELECT v FROM Violator v WHERE v.id IN (:violatorIds)")     // 2. Spring JPA In cause using @Query
    List<Violator> findByViolatorIds(@Param("violatorIds") List<Long> violatorIds);
	
	
	@Query("SELECT v FROM Violator v where CONCAT( v.firstName, ' ', v.lastName ) LIKE CONCAT('%',:name,'%')")    
    List<Violator> searchViolatorByName(@Param("name") String name);
}
