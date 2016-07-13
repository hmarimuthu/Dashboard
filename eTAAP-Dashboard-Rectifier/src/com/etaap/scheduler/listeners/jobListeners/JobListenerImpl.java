package com.etaap.scheduler.listeners.jobListeners;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class JobListenerImpl implements JobListener {

	String name;
	
	public JobListenerImpl(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void jobExecutionVetoed(JobExecutionContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void jobToBeExecuted(JobExecutionContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void jobWasExecuted(JobExecutionContext arg0,
			JobExecutionException arg1) {
		// TODO Auto-generated method stub
		
	}
	
}