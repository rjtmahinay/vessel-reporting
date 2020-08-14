package com.karagathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Report;
import com.karagathon.repository.ReportsRepository;

@Service
public class ReportService {
	
	@Autowired
	ReportsRepository reportsRepository;
	
	public List<Report> getAllReports() {
		return reportsRepository.findAll();
	}
}
