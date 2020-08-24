package com.karagathon.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public final class DateFormatter {
	
	private static final String PATTERN = "yyMMddHHmmssSSSS";

	public static final String format(LocalDateTime localDateTime) {
		 return DateTimeFormatter.ofPattern(PATTERN).format(localDateTime).toString();
	}
	
 	public static final Date format(String localDateString, String pattern) {
 		
 		
 		return java.sql.Date.valueOf(LocalDate.parse(localDateString, DateTimeFormatter.ofPattern(pattern)));
 	}
 	
 	
 	
	
}
