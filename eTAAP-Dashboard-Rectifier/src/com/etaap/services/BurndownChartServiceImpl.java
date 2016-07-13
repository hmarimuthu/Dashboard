package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.BurndownChartDao;
import com.etaap.domain.StoryPoint;



public class BurndownChartServiceImpl implements BurndownChartService {

	@Autowired
	BurndownChartDao burndownChartDao;

	
	@Override
	public List<Map<String, Object>> getApplicationAndSprintDetails(
			int appId, int sprintId) {
		// TODO Auto-generated method stub
		return burndownChartDao.getApplicationAndSprintDetails(appId, sprintId);
	}

/*	@Override
	public List<Map<String, Object>> getTotalStoryPoints(int sprintId) {
		// TODO Auto-generated method stub
		return burndownChartDao.getTotalStoryPoints(sprintId);
	}
*/
	@Override
	public int getNumberOfStoryPointsBeforeSprintStart(int appId, int sprintId, long startMiliSecs) {
		// TODO Auto-generated method stub
		return burndownChartDao.getNumberOfStoryPointsBeforeSprintStart(appId, sprintId, startMiliSecs);
	}

/*	@Override
	public long getSprintStartDatetimeInMili(int sprintId) {
		// TODO Auto-generated method stub
		return burndownChartDao.getSprintStartDatetimeInMili(sprintId);
	}
*/
	@Override
	public List<Map<String, Object>> getBurndownDetails(int appId, int sprintId,
			long sprintStartDateTime) {
		// TODO Auto-generated method stub
		return burndownChartDao.getBurndownDetails(appId, sprintId, sprintStartDateTime);
	}
	
	@Override
	public List<Map<String, Object>> getSprintDetailsWithURL() {
		// TODO Auto-generated method stub
		return burndownChartDao.getSprintDetailsWithURL();
	}

	@Override
	public List<Map<String, Object>> getUserStoryId(int appId, int sprintId, int rapidviewId) {
		// TODO Auto-generated method stub
		return burndownChartDao.getUserStoryId(appId, sprintId, rapidviewId);
	}
	
	@Override
	public void deleteOldStoryPoints(int appId, int sprintId, int rapidviewId) {
		// TODO Auto-generated method stub
		burndownChartDao.deleteOldStoryPoints(appId, sprintId, rapidviewId);
	}
	
	@Override
	public void addStoryPointsToDatabase(int appId, int sprintId, int rapidviewId, final List<StoryPoint> storyPointList) {
		// TODO Auto-generated method stub
		burndownChartDao.addStoryPointsToDatabase(appId, sprintId, rapidviewId, storyPointList);
	}

	@Override
	public void saveSprintStartEndDateTime(int appId, int sprintId, int rapidviewId, long startDateTime, long endDateTime) {
		// TODO Auto-generated method stub
		burndownChartDao.saveSprintStartEndDateTime(appId, sprintId, rapidviewId, startDateTime, endDateTime);
	}

	@Override
	public long getMaxStoryPointActivityDatetimeMili(int appId, int sprintId) {
		// TODO Auto-generated method stub
		return burndownChartDao.getMaxStoryPointActivityDatetimeMili(appId, sprintId);
	}

	public int isAddedToSprint(long userstoryId) {
		// TODO Auto-generated method stub
		return burndownChartDao.isAddedToSprint(userstoryId);
	}
	
	public int isAddedToSprint(long userstoryId, long storyPointActivityTime) {
		// TODO Auto-generated method stub
		return burndownChartDao.isAddedToSprint(userstoryId, storyPointActivityTime);
	}
	
	public int isAddedInFuture(long userstoryId, long storyPointActivityTime) {
		// TODO Auto-generated method stub
		return burndownChartDao.isAddedInFuture(userstoryId, storyPointActivityTime);
	}	

	@Override
	public int getIncrementBeforeSprintStart(long userstoryId, long storyPointActivityTime, long sprintStartTime) {
		// TODO Auto-generated method stub
		return burndownChartDao.getIncrementBeforeSprintStart(userstoryId, storyPointActivityTime, sprintStartTime);
	}
	
	public boolean isAddedFirstTime(long userstoryId, long storyPointActivityTime) {
		return burndownChartDao.isAddedFirstTime(userstoryId, storyPointActivityTime);
	}
	
	public boolean isUserstoryReadded(long userstoryId, long storyPointActivityTime) {
		return burndownChartDao.isUserstoryReadded(userstoryId, storyPointActivityTime);
	}
	
	public int getIncrementForReAddedStoryPoint(long userstoryId, long storyPointActivityTime) {
		return burndownChartDao.getIncrementForReAddedStoryPoint(userstoryId, storyPointActivityTime);
	}

	@Override
	public int getDecrementForReDeletedStoryPoint(long userstoryId,
			long storyPointActivityTime) {
		// TODO Auto-generated method stub
		return burndownChartDao.getDecrementForReDeletedStoryPoint(userstoryId, storyPointActivityTime);
	}

	@Override
	public boolean isUserstoryReDeleted(long userstoryId,
			long storyPointActivityTime) {
		// TODO Auto-generated method stub
		return burndownChartDao.isUserstoryReDeleted(userstoryId, storyPointActivityTime);
	}


}
