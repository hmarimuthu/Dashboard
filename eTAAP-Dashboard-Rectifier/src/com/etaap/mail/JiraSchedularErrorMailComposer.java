package com.etaap.mail;

import java.util.ArrayList;

import javax.mail.internet.AddressException;

import org.springframework.mail.SimpleMailMessage;

public class JiraSchedularErrorMailComposer implements MailComposer {
	
	private String subject;
	private StringBuffer body;

	@Override
	public String getFromAddress() throws AddressException {
		// TODO Auto-generated method stub
		return "spunde@etouch.net";
	}

	@Override
	public ArrayList<String> getTOAddresses() throws AddressException {
		// TODO Auto-generated method stub
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("sharmilaspx@gmail.com");
		return arr;
	}

	@Override
	public ArrayList<String> getCCAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getBCCAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubject() {
		// TODO Auto-generated method stub
		return subject;
	}

	@Override
	public StringBuffer getEmailBody() {
		// TODO Auto-generated method stub
		return body;
	}

	@Override
	public SimpleMailMessage getSimpleMailMessage() throws AddressException {
		// TODO Auto-generated method stub
        SimpleMailMessage message = new SimpleMailMessage();  
        message.setFrom(this.getFromAddress());
        String arr[] = new String[getTOAddresses().size()]; 
        getTOAddresses().toArray(arr);
        message.setTo(arr);  
        message.setSubject(subject);  
        message.setText(getEmailBody().toString());
        return message;
	}
	
	public void setSubject(String sysApiServerName, String jobName) {
		subject = "Server-"+sysApiServerName+". Job-"+jobName+". Build failed.";
	}
	
	public void setEmailBody(String serverName, String url, String jobName, String fromTime, String toTime,  String fetchUrl, String errorMessage) {
		body = new StringBuffer();
		body.append("Server-"+serverName+"\n");
		body.append("URL-"+url+"\n");
		body.append("Job Name-"+jobName+"\n");
		
		if((fromTime != null) && (!fromTime.trim().equals(""))) {
			body.append("From Time-"+fromTime+"\n");
		}
		if((toTime != null) && (!toTime.trim().equals(""))) {
			body.append("To Time-"+toTime+"\n");
		}
		if((fetchUrl != null) && (!fetchUrl.trim().equals(""))) {
			body.append("Fetch URL-"+fetchUrl+"\n");
		}
		body.append("Error Message-"+errorMessage);
	}
	

}
