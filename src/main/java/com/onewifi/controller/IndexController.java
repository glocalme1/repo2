package com.onewifi.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("request")
public class IndexController {

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(HttpServletResponse response) {
		 response.setCharacterEncoding("UTF-8");  
		 ModelAndView view = new ModelAndView(); 
		 view.setViewName("home");
		 return view;
	}
}
