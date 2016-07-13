package com.etaap.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.etaap.beans.UserSession;
import com.etaap.services.UserAuthService;


@Controller
@SessionAttributes("userSession")
public class LoginController {
	
	@Autowired
	UserAuthService authService;
	
	private static final Logger logger = Logger.getLogger(LoginController.class);
	
	@RequestMapping(value = "login", method= RequestMethod.GET)
	public String doLogin(HttpSession session) {
		logger.info("Inside loginController :: doLogin()");
		return checkLoggedInSession(session);
	}

	@RequestMapping(value = "logout")
	public String doLogout(HttpSession session,SessionStatus status) {
		logger.info("Inside loginController :: doLogout()");
		if(session != null){
			session.removeAttribute("userSession");
		}
		status.setComplete();
		return "login";
	}
	
	@RequestMapping(value = "loginAuth", method = RequestMethod.GET)
	public String showLogin(HttpSession session) {
		logger.info("Inside loginController :: showLogin()");
		return checkLoggedInSession(session);
	}
	
	@RequestMapping(value = "loginAuth", method = RequestMethod.POST)
	public String loginAuthorization(@RequestParam("userName") String userName,@RequestParam("password") String password,Model uiModel) {
		logger.info("Inside loginController :: loginAuthorization()");
		try {
			String authenticateUser = authService.authUser(userName, password);
			if(authenticateUser == null){
				uiModel.addAttribute("exception", "Invalid");
				return "login"; 
			}else{
				UserSession userSession = authService.getUserDetails(userName, authenticateUser);
				if(userSession == null){
					uiModel.addAttribute("exception", "Invalid");
					return "login";
				}else{
					uiModel.addAttribute("userSession", userSession );
					return "redirect:/home-redesign/";
				}
			}
		} catch (Exception e) {
			logger.info("ERROR :: handleHomeReqduest () method,"+ e.getMessage());
			uiModel.addAttribute("exception", "Invalid");
			return "login";
		}
	}
	
	private String checkLoggedInSession(HttpSession session){
		if(session != null && session.getAttribute("userSession") != null){
			return "redirect:/home-redesign/";
		}else{
			return "login";
		}
	}
}
