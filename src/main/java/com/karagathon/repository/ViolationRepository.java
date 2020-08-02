package com.karagathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.karagathon.model.Violation;

public interface ViolationRepository extends JpaRepository<Violation, Long> {

}
