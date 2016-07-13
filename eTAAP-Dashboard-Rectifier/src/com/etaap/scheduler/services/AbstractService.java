package com.etaap.scheduler.services;

import org.quartz.Scheduler;



public abstract class AbstractService {

	private static Scheduler scheduler;
	public abstract Object schedule(String request);
	public abstract Object unSchedule(Object request);
	public abstract Object scheduleImmediate(Object request);
	
}
