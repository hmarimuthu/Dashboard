package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.beans.Tcm;
import com.etaap.dao.DashboardDao;
import com.etaap.domain.CI;
import com.etaap.domain.Defects;

public class DashboardServiceImpl implements DashboardService {

	@Autowired
	DashboardDao dashboardDao;

	public List<Defects> getDetails(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return dashboardDao.getDetails(params);
	}
	
	public List<CI> getJenkinsDetails(HashMap<String, Object> params) {
		return dashboardDao.getJenkinsDetails(params);
	}
	
	public List<Tcm> getTcmDashBoardChartString(){
		return  dashboardDao.getTcmDashBoardChartString();
	}

	@Override
	public List<Map<String,Object>> getDefectsLife(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dashboardDao.getDefectsLife(params);
	}

	@Override
	public List<Map<String, Object>> getDefectsStatistics(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dashboardDao.getDefectsStatistics(params);
	}

	@Override
	public Map<String, Object> getCommitedCompletedUserStories(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return dashboardDao.getCommitedCompletedUserStories(params);
	}
	
}
