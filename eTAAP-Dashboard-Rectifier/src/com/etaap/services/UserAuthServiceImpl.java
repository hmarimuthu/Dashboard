package com.etaap.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.beans.UserSession;
import com.etaap.dao.UserAuthorizationDao;

public class UserAuthServiceImpl implements UserAuthService {

	@Autowired
	UserAuthorizationDao userAuthDao;
	
	public String authUser(String userName, String password){
		return  userAuthDao.authenticateUser(userName,password);
	}
	
	public UserSession getUserDetails(String userName, String password){
		return userAuthDao.getUserAuthDetails(userName,password);
	}	
	
}
