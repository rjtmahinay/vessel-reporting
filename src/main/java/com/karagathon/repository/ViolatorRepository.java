package com.karagathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Violator;

@Repository
public interface ViolatorRepository extends JpaRepository<Violator, Long> {

}
