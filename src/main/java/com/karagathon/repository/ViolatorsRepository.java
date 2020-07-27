package com.karagathon.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Violator;

@Repository
public interface ViolatorsRepository extends JpaRepository<Violator, Long>{
	
	@Query("SELECT v FROM Violator v WHERE v.name =: name")
	Violator getViolatorByName(@Param("name") String name);
	
	@Query(value = "SELECT v from Violator v")
	List<Violator> getAllViolators();
}
