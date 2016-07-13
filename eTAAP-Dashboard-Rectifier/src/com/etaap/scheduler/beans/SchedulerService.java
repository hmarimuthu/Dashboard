package com.etaap.scheduler.beans;

import java.util.HashMap;
import java.util.Map;

public class SchedulerService {
	Map<String,Object> configurations = new HashMap<String,Object>();
	Map<String,Object> jobs = new HashMap<String,Object>();
	public Map<String, Object> getConfigurations() {
		return configurations;
	}
	public void setConfigurations(Map<String, Object> configurations) {
		this.configurations = configurations;
	}
	public Map<String, Object> getJobs() {
		return jobs;
	}
	public void setJobs(Map<String, Object> jobs) {
		this.jobs = jobs;
	}
	
	
}
