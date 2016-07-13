package com.etaap.services;

import java.util.List;

import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;

public interface TcmService{

	public String getTcmChartString(int app_id,String from_dt, String to_dt);
	
	public String getPrevQuaterChartString(int app_id,String from_dt, String to_dt, List<TimePeriod> actualTimePeriodList);
	
	public List<Application> getTcmApplicationList(int app_id);
	
	public List<Application> getTcmApplicationList();
}
