package com.etaap.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
@Controller
public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = Logger.getLogger(SessionInterceptor.class);
	
	@Override
	@RequestMapping(value="*")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("Inside sessionInterceptor :: preHandle()");
		HttpSession session  = request.getSession();
	
		if(session == null || session.getAttribute("userSession") == null){
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}else{
			return true;
		}
		
	}	

}
