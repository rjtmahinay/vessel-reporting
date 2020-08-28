package com.karagathon.helper;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

public class Utilities {
	
	public static final String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
	
	
	public static int calculateAge( LocalDate birthDate, LocalDate currentDate ) {
		if( Objects.nonNull(birthDate) && Objects.nonNull(currentDate) ) {
			System.out.println(birthDate.toString() + " " + currentDate.toString());
			return Period.between(birthDate, currentDate).getYears();
		}
		
		return 0;
	}
	
	public static boolean containsObject(List<ModelStatistics> modelStatisticsList, Integer monthNumber) {
		boolean isContains = false;
		for( ModelStatistics modelStat : modelStatisticsList ) {
			if(modelStat.getMonth().intValue() == monthNumber.intValue()) isContains = true;
		}
		
		return isContains;
	}
	
}
