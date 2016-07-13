package com.etaap.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.TcmDaoImpl;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;

public class TcmServiceImpl implements TcmService{

	@Autowired
	TcmDaoImpl tcmDaoImpl;

	public String getTcmChartString(int app_id,String from_dt, String to_dt){
		return  tcmDaoImpl.getTcmChartString(app_id, from_dt, to_dt);
	}
	public String getPrevQuaterChartString(int app_id,String from_dt, String to_dt, List<TimePeriod> actualTimePeriodList){
		return  tcmDaoImpl.getPrevQuaterChartString(app_id, from_dt, to_dt, actualTimePeriodList);
	}
	public List<Application> getTcmApplicationList(int app_id){
		return  tcmDaoImpl.getTcmApplicationList(app_id);
	}

	public List<Application> getTcmApplicationList(){
		return  tcmDaoImpl.getTcmApplicationList();
	}
}
