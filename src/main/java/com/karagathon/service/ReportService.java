package com.karagathon.service;

import java.util.List;

import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Report;
import com.karagathon.repository.ReportsRepository;

@Service

public class ReportService implements ApplicationService {

public class ReportService {

	
	@Autowired
	ReportsRepository reportsRepository;
	
	public List<Report> getAllReports() {
		return reportsRepository.findAll();
	}

	
	public Report saveAndFlushReport(Report report) {
		return reportsRepository.saveAndFlush(report);
	}
	
	public Report findById(Long id) {
		return reportsRepository.findById(id).orElse(null);
	}
	
	public List<Report> findReportsByDescription(final String description) {
		if( Objects.isNull(description) || description.isEmpty() || description.isBlank()) {
			return reportsRepository.findAll();
		}
		
		return reportsRepository.searchReportByDescription(description);
	}

}
