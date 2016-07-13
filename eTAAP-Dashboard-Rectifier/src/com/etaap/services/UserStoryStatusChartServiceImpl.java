package com.etaap.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.beans.UserStoryStatus;
import com.etaap.dao.UserStoryStatusChartDao;

public class UserStoryStatusChartServiceImpl implements UserStoryStatusChartService {

	@Autowired
	UserStoryStatusChartDao userStoryStatusChartDao;
	
	public List<Map<String, Object>> getUserStoryDetails(int appId, int sprintId, int rapidviewId) {
		return userStoryStatusChartDao.getUserStoryDetails(appId, sprintId, rapidviewId);
	}
	
	@Override
	public List<Map<String, Object>> getSprintDetailsWithURL() {
		// TODO Auto-generated method stub
		return userStoryStatusChartDao.getSprintDetailsWithURL();
	}
	
	public void addUserStoryStatusToDatabase(int sprintId, int rapidviewId, final List<UserStoryStatus> userstoryStatusList) {
		userStoryStatusChartDao.addUserStoryStatusToDatabase(sprintId, rapidviewId, userstoryStatusList);
	}
	
	public void deleteOldUserStoryStatus(int appId, int sprintId, int rapidviewId) {
		userStoryStatusChartDao.deleteOldUserStoryStatus(appId, sprintId, rapidviewId);
	}
	
}
