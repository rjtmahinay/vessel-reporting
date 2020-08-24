package com.karagathon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Vessel;
import com.karagathon.model.Violator;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Long> {
	@Query("SELECT v FROM Vessel v WHERE v.violator = :violator")    
    List<Vessel> findVesselByViolator(@Param("violator") Violator violator);
	
	@Query("SELECT v FROM Vessel v where CONCAT( v.vesselName ) LIKE CONCAT('%',:name,'%')")    
    List<Vessel> searchVesselByName(@Param("name") String name);
}
