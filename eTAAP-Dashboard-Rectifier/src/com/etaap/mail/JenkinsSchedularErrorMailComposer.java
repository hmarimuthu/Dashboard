package com.etaap.mail;

import java.util.ArrayList;

import javax.mail.internet.AddressException;

import org.springframework.mail.SimpleMailMessage;

public class JenkinsSchedularErrorMailComposer implements MailComposer {
	
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
		arr.add("nbheda@etouch.net");
		arr.add("gnoronha@etouch.net");
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
	
	public void setEmailBody(String serverName, String url, String jobName, String buildId, String buildName, 
			String buildNumber, String buildUrl, String errorMessage) {
		body = new StringBuffer();
		body.append("Server-"+serverName+"\n");
		body.append("URL-"+url+"\n");
		body.append("Job Name-"+jobName+"\n");
		
		if((buildId != null) && (!buildId.trim().equals(""))) {
			body.append("Build ID-"+buildId+"\n");
		}
		if((buildName != null) && (!buildName.trim().equals(""))) {
			body.append("Build Name-"+buildName+"\n");
		}
		if((buildNumber != null) && (!buildNumber.trim().equals(""))) {
			body.append("Build Number-"+buildNumber+"\n");
		}
		if((buildUrl != null) && (!buildUrl.trim().equals(""))) {
			body.append("Build URL-"+buildUrl+"\n");
		}
		body.append("Error Message-"+errorMessage);
	}
	

}
