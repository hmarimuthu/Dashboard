package com.etaap.mail;
import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.mail.SimpleMailMessage;

public interface MailComposer {
	
	public String getFromAddress() throws AddressException;
	public ArrayList<String> getTOAddresses() throws AddressException;
	public ArrayList<String> getCCAddresses() throws AddressException;
	public ArrayList<String> getBCCAddresses() throws AddressException;
	
	public String getSubject();
	public StringBuffer getEmailBody();
	
	public SimpleMailMessage getSimpleMailMessage() throws AddressException;

}
