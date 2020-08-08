package com.karagathon.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;

public final class DateFormatter {
	
	private static final String PATTERN = "yyMMddHHmmssSSSS";

	public static final String format(LocalDateTime localDateTime) {
		 return DateTimeFormatter.ofPattern(PATTERN).format(localDateTime).toString();
	}
	
}
