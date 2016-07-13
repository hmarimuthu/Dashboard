package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.beans.Application;
import com.etaap.beans.Sprint;
import com.etaap.dao.IterationsChartDao;

public class IterationsChartServiceImpl implements IterationsChartService {
	
	@Autowired
	IterationsChartDao iterationsChartDao;
	
	public List<Sprint> getSprintList(int appId) {
		return iterationsChartDao.getSprintList(appId);
	}
	
	public List<Application> getApplicationList() {
		return iterationsChartDao.getApplicationList();
	}
	
	public List<Application> getApplicationList(int appId) {
		return iterationsChartDao.getApplicationList(appId);
	}
	
	public int getUserStoriesCount(Map params) {
		return iterationsChartDao.getUserStoriesCount(params);
	}
	
	public HashMap<String, Object> getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(int appId, int sprintId, int rapidviewId) {
		return iterationsChartDao.getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(appId, sprintId, rapidviewId);
	}
	
	@Override
	public Application getApplication(int appId) {
		return iterationsChartDao.getApplication(appId);
	}
	
	public Sprint getSprint(int sprintId) {
		return iterationsChartDao.getSprint(sprintId);
	}
	
	public int getRapidViewId(int appId, int sprintId) {
		return iterationsChartDao.getRapidViewId(appId, sprintId);
	}	
	
	
}
