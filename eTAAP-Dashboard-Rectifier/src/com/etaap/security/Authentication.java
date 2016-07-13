package com.etaap.security;

public class Authentication {

	private String userName = null;
	private String password = null;
	
	/**Tested only with http and NOT with https.
	 */
	private String httpUrlString = null;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHttpUrlString() {
		return httpUrlString;
	}

	public void setHttpUrlString(String httpUrlString) {
		this.httpUrlString = httpUrlString;
	}
	
}
