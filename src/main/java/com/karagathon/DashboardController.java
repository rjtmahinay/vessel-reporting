package com.karagathon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.karagathon.helper.ModelStatistics;
import com.karagathon.helper.Utilities;
import com.karagathon.repository.ReportsRepository;
import com.karagathon.repository.ViolationRepository;

@Controller
public class DashboardController {
	
	@Autowired
	ReportsRepository reportsRepository;
	
	@Autowired
	ViolationRepository violationRepository;
	
	@Value("${chart.properties.years.interval}")
	Integer yearInterval;
	
	@GetMapping("/chart")
	public String chart( Model model ) {
		// TODO REFACTOR GENERATE YEARS IN FRONT END INSTEAD
		List<String> years = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		years.add( String.valueOf(currentDate.getYear()) );
		for( int currentYear = 1; currentYear < yearInterval; currentYear++ ) {
			years.add( Integer.toString( currentDate.plusYears( currentYear * -1 ).getYear() ) );
		}
		
		model.addAttribute("years", years);
		return "chart";
	}
	
	@RequestMapping("/linechartdata")
	public @ResponseBody String getChartData() {
		
		JsonArray month = new JsonArray();
		JsonArray values = new JsonArray();
		JsonObject jsonOutput = new JsonObject();
		List<ModelStatistics> reportStatisticsByMonth = reportsRepository.getMonthStatisticsFromReport("");
		String monthName = "";
		Iterator<ModelStatistics> modelStatIterator = reportStatisticsByMonth.iterator();
		for( int i = 1; i <= 12; i++ ) {
			monthName = Utilities.getMonth( i );
			month.add( monthName );
			if( Utilities.containsObject(reportStatisticsByMonth, i) ) 
			{
				if( modelStatIterator.hasNext() ) {
					values.add( modelStatIterator.next().getValue() );
				}
				
			}else {
				values.add( 0L );
			}
		}
		
		jsonOutput.add("month", month);
		jsonOutput.add("values", values);
		
		return jsonOutput.toString();
	}
	
	
	@RequestMapping("/multilinechart")
	public ResponseEntity<?> getMultiLineData(@RequestParam("year") String year ){
		List<ModelStatistics> reportsData = reportsRepository.getMonthStatisticsFromReport(year);
		List<ModelStatistics> violationData = violationRepository.getMonthStatisticsFromViolation(year);
		
		Map<String, List<ModelStatistics>> mappedData = new HashMap<>();
		mappedData.put("Reports", reportsData);
		mappedData.put("Violations", violationData);
		
		return ResponseEntity.ok(mappedData);
	}
}
