package com.etaap.dao;

import java.util.List;
import java.util.Map;
import com.etaap.beans.UserStoryStatus;

public interface UserStoryStatusChartDao {
	public List<Map<String, Object>> getSprintDetailsWithURL();
	public List<Map<String, Object>> getUserStoryDetails(int appId, int sprintId, int rapidviewId);
	public void addUserStoryStatusToDatabase(int sprintId, int rapidviewId, final List<UserStoryStatus> userstoryStatusList);
	public void deleteOldUserStoryStatus(int appId, int sprintId, int rapidviewId);
}
