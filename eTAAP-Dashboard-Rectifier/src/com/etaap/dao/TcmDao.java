package com.etaap.dao;

import java.util.List;

import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;

public interface TcmDao{
	

	public String getTcmChartString(int app_id,String from_dt, String to_dt);
	
	public String getPrevQuaterChartString(int app_id,String to, String prevQuaterEndDate, List<TimePeriod> actualTimePeriodList);
	
	public List<Application> getTcmApplicationList();
	
	public List<Application> getTcmApplicationList(int app_id);
}
