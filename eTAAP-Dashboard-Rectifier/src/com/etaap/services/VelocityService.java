package com.etaap.services;

import java.util.List;

import com.etaap.beans.Velocity;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;

public interface VelocityService{

	public String getVelocityChartString(int app_id,String from_dt, String to_dt);
	
	public List<Velocity> getVelocityChartDetails(int app_id,String from_dt, String to_dt);
	
	public List<Application> getVelocityApplicationList(int app_id);
	
	public List<Application> getVelocityApplicationList();
}
