package com.karagathon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Violation;
import com.karagathon.model.Violator;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long> {
	
}
