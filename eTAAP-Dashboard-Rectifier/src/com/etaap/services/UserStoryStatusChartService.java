package com.etaap.services;

import java.util.List;
import java.util.Map;
import com.etaap.beans.UserStoryStatus;

public interface UserStoryStatusChartService {
//	public List<Map<String, Object>> getApplicationAndSprintDetails(int appId, int sprintId);
	public List<Map<String, Object>> getUserStoryDetails(int appId, int sprintId, int rapidviewId);
	
//	public List<Map<String, Object>> getTotalStoryPoints(int sprintId);
//	public int getNumberOfStoryPointsBeforeSprintStart(int sprintId, long startMiliSecs);
//	public long getSprintStartDatetimeInMili(int sprintId);
	
	public List<Map<String, Object>> getSprintDetailsWithURL();
	
/*	public List<Map<String, Object>> getBurndownDetails(int sprintId, long sprintStartDateTime);
	public List<Map<String, Object>> getUserStoryId(int sprintId, int rapidviewId);
	public void deleteOldStoryPoints(int sprintId, int rapidviewId);
	public void addStoryPointsToDatabase(int sprintId, int rapidviewId, final List<StoryPoint> storyPointList);
	public void saveSprintStartEndDateTime(int sprintId, int rapidviewId, long startDateTime, long endDateTime);
	public long getMaxStoryPointActivityDatetimeMili(int sprintId);
*/
	
	public void addUserStoryStatusToDatabase(int sprintId, int rapidviewId, final List<UserStoryStatus> userstoryStatusList);
	
	public void deleteOldUserStoryStatus(int appId, int sprintId, int rapidviewId);
	
}
