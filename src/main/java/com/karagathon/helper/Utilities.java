package com.karagathon.helper;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Utilities {
	
	
	public static int calculateAge( LocalDate birthDate, LocalDate currentDate ) {
		if( Objects.nonNull(birthDate) && Objects.nonNull(currentDate) ) {
			System.out.println(birthDate.toString() + " " + currentDate.toString());
			return Period.between(birthDate, currentDate).getYears();
		}
		
		return 0;
	}
	
}
