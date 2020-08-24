package com.karagathon.helper;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.karagathon.model.IModel;
import com.karagathon.model.Media;
import com.karagathon.service.ApplicationService;
import com.karagathon.service.MediaService;


@Component
public class SpecificServiceHelper {
	
	@Autowired MediaService mediaService;
	
	
	public ModelAndView getSpecific(ApplicationService applicationService, Long id) {
		ModelAndView mav = new ModelAndView();
		IModel model = applicationService.findById(id);
		final String modelName = model.getClass().getSimpleName().toLowerCase();
		
		if(!Objects.isNull(model)) {
			List<Media> media = mediaService.findMediaByModel(model);
			mav.addObject(modelName, model);
			mav.addObject("media", media);
		 	mav.setViewName(modelName.concat("-specific.html"));
		}else{
			mav.addObject("error", "error");
			mav.setViewName("error.html");
		}
		
		return mav;
	}
}
