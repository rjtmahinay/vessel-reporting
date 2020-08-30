package com.karagathon.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.karagathon.helper.ListConversionHelper;
import com.karagathon.helper.LocationParser;
import com.karagathon.model.Location;
import com.karagathon.model.Report;
import com.karagathon.repository.LocationRepository;

@Service
public class LocationService implements ApplicationService {
	
	@Autowired LocationRepository locationRepository;

	@Override
	public Location findById(Long id) {
		return locationRepository.findById(id).orElse(null);
	}
	
	public Location saveAndFlushLocation( Location location ) {
		return locationRepository.saveAndFlush(location);
	}
	
	public List<Location> getAllLocations() {
		return locationRepository.findAll();
	}

	public Location getLocation( final String locationString ) {
		Location location = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		String returnedJsonData = "";
		
		try {
			String uri = URLEncoder.encode(LocationParser.parseLocationString(locationString), StandardCharsets.UTF_8.toString());
			String finalUri = "https://nominatim.openstreetmap.org/search?q="+uri+"&format=geojson";
			System.out.println(finalUri);
			JsonParser jsonParser = JsonParserFactory.getJsonParser();
			returnedJsonData = restTemplate.exchange(finalUri, HttpMethod.GET, entity, String.class).getBody();
			
			Map<String, Object> parsedJsonMap = jsonParser.parseMap(returnedJsonData);
			// jackson
			if( !ListConversionHelper.objectToListObject(parsedJsonMap.get("features")).isEmpty() ) {
				Map features = ((Map)ListConversionHelper.objectToListObject(parsedJsonMap.get("features")).get(0)); 
				Map geometry = (Map)features.get("geometry");
				List coordinates = ListConversionHelper.objectToListObject(geometry.get("coordinates"));
				
				return new Location( BigDecimal.valueOf( (Double)coordinates.get(0) ) , BigDecimal.valueOf( (Double)coordinates.get(1) ));
			}else {
				
			}
			
		}catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }

		return location;
	}
	
	public Location getLocationFromReport( final Report report ) {
		return locationRepository.findByReport(report).orElse(null);
	}

	
}
