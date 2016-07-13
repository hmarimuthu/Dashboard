package com.etaap.utils.enums;

public enum SystemAPI {
	JENKINS("Jenkins"),JIRA("Jira");
	
	private String systemName;
	 
	private SystemAPI(String s) {
		systemName = s;
	}
 
	public String getSystemName() {
		return systemName;
	}
	
}
