package com.etaap.dao;

import java.util.List;
import java.util.Map;

import com.etaap.domain.StoryPoint;

public interface BurndownChartDao {
	public List<Map<String,Object>> getApplicationAndSprintDetails(int appId, int sprintId);
//	public List<Map<String, Object>> getTotalStoryPoints(int sprintId);
	public int getNumberOfStoryPointsBeforeSprintStart(int appId, int sprintId, long startMiliSecs);
//	public long getSprintStartDatetimeInMili(int sprintId);
	public List<Map<String, Object>> getSprintDetailsWithURL();
	public List<Map<String, Object>> getBurndownDetails(int appId, int sprintId, long sprintStartDateTime);
	public List<Map<String, Object>> getUserStoryId(int appId, int sprintId, int rapidviewId);
	public void deleteOldStoryPoints(int appId, int sprintId, int rapidviewId);
	public void addStoryPointsToDatabase(int appId, int sprintId, int rapidviewId, final List<StoryPoint> storyPointList);
	public void saveSprintStartEndDateTime(int appId, int sprintId, int rapidviewId, long startDateTime, long endDateTime);
	public long getMaxStoryPointActivityDatetimeMili(int appId, int sprintId);
	public int isAddedToSprint(long userstoryId);
	public int isAddedToSprint(long userstoryId, long storyPointActivityTime);
	public int isAddedInFuture(long userstoryId, long storyPointActivityTime);	
	public int getIncrementBeforeSprintStart(long userstoryId, long storyPointActivityTime, long sprintStartTime);
	public boolean isAddedFirstTime(long userstoryId, long storyPointActivityTime);
	public boolean isUserstoryReadded(long userstoryId, long storyPointActivityTime);
	public int getIncrementForReAddedStoryPoint(long userstoryId, long storyPointActivityTime);	
	public int getDecrementForReDeletedStoryPoint(long userstoryId, long storyPointActivityTime);
	public boolean isUserstoryReDeleted(long userstoryId, long storyPointActivityTime);	
}
