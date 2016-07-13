package com.etaap.dao;

import com.etaap.beans.UserSession;

public interface UserAuthorizationDao {
	
	String authenticateUser(String userName, String loginPassword);
	
	UserSession getUserAuthDetails(String username, String password);
	
}
