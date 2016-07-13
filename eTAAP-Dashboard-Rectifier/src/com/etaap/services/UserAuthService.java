package com.etaap.services;

import com.etaap.beans.UserSession;

public interface UserAuthService {
	
	public String authUser(String userName, String password);
	
	public UserSession getUserDetails(String userName, String password);
	
}
