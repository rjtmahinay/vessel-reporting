package com.karagathon.helper;

public class LocationParser {
	
	public static final String parseLocationString( final String locationString ) {
		String parsedLocationString = locationString.replaceAll(",", "");
		parsedLocationString = parsedLocationString.replaceAll("\\.", "");
		
		return parsedLocationString;
	}
}
