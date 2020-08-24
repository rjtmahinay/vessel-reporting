package com.karagathon;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.karagathon.model.Report;
import com.karagathon.repository.ReportsRepository;

@Controller
public class DashboardController {
	
	@Autowired
	ReportsRepository reportsRepository;
	
	@GetMapping("/chart")
	public String test() {
		
		return "chart";
	}
	
	@RequestMapping("/linechartdata")
	public @ResponseBody String getChartData() {
		List<Report> reports = reportsRepository.findAll();
		
		JsonArray description = new JsonArray();
		JsonArray values = new JsonArray();
		JsonObject jsonOutput = new JsonObject();
		reports.forEach(report -> {
			description.add( report.getDescription() );
			values.add( Math.random() * 3 + Math.random() * Math.random());
		});
		
		jsonOutput.add("description", description);
		jsonOutput.add("values", values);
		
		return jsonOutput.toString();
	}
	
	
	
	
	
}
