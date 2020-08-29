package com.karagathon.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.karagathon.model.IModel;
import com.karagathon.model.Media;

public class ListConversionHelper {

	public static List<Long> stringToLong(Collection<String> listOfString) {
		return listOfString.stream().map(Long::parseLong).collect(Collectors.toList());
	}

	public static List<Media> stringToMedia(Collection<String> listOfString, IModel model) {
		return listOfString.stream().map(string -> new Media(string, model)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public static List<String> objectToList(Object obj) {
		List<String> list = new ArrayList<>();
		if (obj instanceof Collection) {
			list = new ArrayList<>((Collection<String>) obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<Object> objectToListObject(Object obj) {
		List<Object> list = new ArrayList<>();
		if (obj instanceof Collection) {
			list = new ArrayList<>((Collection<Object>) obj);
		}
		return list;
	}

}
