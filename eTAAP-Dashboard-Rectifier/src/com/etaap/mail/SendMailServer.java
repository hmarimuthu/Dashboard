package com.etaap.mail;

import java.util.ArrayList;

import javax.annotation.PreDestroy;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;


public class SendMailServer implements Runnable {

	private static boolean runFlag = false;
	private ArrayList<SimpleMailMessage> mailQueue = new ArrayList();
	private static Thread server;
	private static boolean createdInstance = false;
	private static SendMailServer instance;
	private static boolean mailsPending = true;
	
	public static SendMailServer getInstance() {
		if(!createdInstance) {
			createdInstance = true;
			instance = new SendMailServer();
		}
		return instance;
	}
	
	@Autowired
	public MailSender mailSender;
	
	public MailSender getMailSender() {
		return mailSender;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	
	private SendMailServer() {
//		System.out.println("MMMMMMMMMMMMMMFFFFFFFFFFFFFFFFFFFFFFFFFFFFFMMMMMMMMMMMMMM");
		server = new Thread(this);
		server.setDaemon(false);
		runFlag = true;
		server.start();
	}
	
	public static void main(String[] args) throws AddressException {
		try {
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/test.xml");			
			
			JenkinsSchedularErrorMailComposer schedularErrorMailComposer = new JenkinsSchedularErrorMailComposer();
			schedularErrorMailComposer.setSubject("ServerName", "JobName");
//			schedularErrorMailComposer.setEmailBody("Error Message", "ServerName", "JobName", "http://192.168.5.30:8080/");
			SimpleMailMessage message = schedularErrorMailComposer.getSimpleMailMessage();
			
			SendMailServer server = (SendMailServer) applicationContext.getBean("senderThread");
			server.sendMail(message);
			
			Thread.sleep(40000);
			
			applicationContext.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(runFlag || mailsPending) {
//			System.out.println("Send Mail Process running... "+runFlag);
			synchronized(this) {
				try {
					wait(1000);
				} 
				catch(InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					if(mailQueue.size() > 0) {
						System.out.println("Send Mail Process About to Send Mail");
						SimpleMailMessage message = mailQueue.remove(0);
						System.out.println("From "+message.getFrom());
						System.out.println("To "+message.getTo());
						mailSender.send(message);
						System.out.println("Send Mail Process Sent Mail");
					}
					if(!runFlag) {
						if(mailQueue.size() == 0) {
							mailsPending = false;
							System.out.println("No Mails Pending while shutting down.");
						}
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Send Mail Process stopped... "+runFlag);

	}
	
	public void sendMail(SimpleMailMessage message) {
		if(message != null) {
			mailQueue.add(message);
		}
		synchronized(this) {
			notify();
		}
	}
	
	public static void stop() {
		System.out.println("Going to stop....");
		runFlag = false;
	}

}
