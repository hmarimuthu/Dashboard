package com.etaap.controller;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController {
	
	private static final Logger logger = Logger.getLogger(ExceptionController.class);
	
	@RequestMapping(value = "uncaughtException")
	public String handleUncaughtException() {
		logger.info("Inside ExceptionController :: handleUncaughtException()");
		return "login";
	}

	@RequestMapping(value = "resourceNotFound")
	public String handleResourceNotFound() {
		logger.info("Inside ExceptionController :: handleResourceNotFound()");
		return "login";
	}
	
}
