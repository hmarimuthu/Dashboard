package com.etaap.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.beans.Velocity;
import com.etaap.dao.TcmDaoImpl;
import com.etaap.dao.VelocityDaoImpl;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;

public class VelocityServiceImpl implements VelocityService{

	@Autowired
	VelocityDaoImpl velocityDao;

	public String getVelocityChartString(int app_id,String from_dt, String to_dt){
		return  velocityDao.getVelocityChartString(app_id, from_dt, to_dt);
	}
	public List<Velocity> getVelocityChartDetails(int app_id, String from_dt, String to_dt){
		return  velocityDao.getVelocityChartDetails(app_id, from_dt, to_dt);
	}
	
	public List<Application> getVelocityApplicationList(int app_id){
		return  velocityDao.getVelocityApplicationList(app_id);
	}

	public List<Application> getVelocityApplicationList(){
		return  velocityDao.getVelocityApplicationList();
	}
}
