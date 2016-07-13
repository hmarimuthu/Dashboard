package com.etaap.utils;

import java.net.UnknownHostException;

public class ExceptionUtility {
	private Exception e;
	public ExceptionUtility(Exception e) {
		this.e = e;
	}
	
	public String getMessage() {
		String retVal = "";
		if(e != null) {
			if(e instanceof UnknownHostException) {
				retVal = e.getMessage()+" is unknown host.";
			}
			else {
				retVal = e.getMessage();
				if(retVal.trim().startsWith("<html>")) {
					StringBuffer buf = new StringBuffer(retVal);
					int lastIndexOfStart = buf.lastIndexOf("<u>");
					int lastIndexOfEnd = buf.lastIndexOf("</u>");
					retVal = buf.substring(lastIndexOfStart+3, lastIndexOfEnd);
				}
			} 
				
		}
		return retVal;
		
	}
}
