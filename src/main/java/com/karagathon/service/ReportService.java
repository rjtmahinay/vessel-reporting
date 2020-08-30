package com.karagathon.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karagathon.model.Report;
import com.karagathon.repository.ReportsRepository;

@Service

public class ReportService implements ApplicationService {

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
		if( Objects.isNull(description) || description.trim().isEmpty()) {
			return reportsRepository.findAll();
		}
		
		return reportsRepository.searchReportByDescription(description);
	}
	
	public int getCountOfReports(Date currentDate) {
		return reportsRepository.getCountOfReports(currentDate);
	}
}
