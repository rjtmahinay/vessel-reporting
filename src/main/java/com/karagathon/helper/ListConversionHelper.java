package com.karagathon.helper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.karagathon.model.Media;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;

public class ListConversionHelper {
	
	public static List<Long> stringToLong( Collection<String> listOfString ) {
		return listOfString.stream().map(Long::parseLong).collect(Collectors.toList());
	}
	
	public static List<Media> stringToMedia( Collection<String> listOfString, Violation violation ) {
		return listOfString.stream().map( string -> new Media(string, violation) ).collect(Collectors.toList());
	}
	
	public static List<Media> stringToMedia( Collection<String> listOfString, Violator violator ) {
		return listOfString.stream().map( string -> new Media(string, violator) ).collect(Collectors.toList());
	}
	 
}
