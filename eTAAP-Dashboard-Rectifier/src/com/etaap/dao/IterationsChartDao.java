package com.etaap.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.beans.Application;
import com.etaap.beans.Sprint;

public interface IterationsChartDao {
	public List<Sprint> getSprintList(int appId);
	public List<Application> getApplicationList();
	public List<Application> getApplicationList(int appId);
	public int getUserStoriesCount(Map params);
	public HashMap<String, Object> getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(int appId, int sprintId, int rapidviewId);
	public Application getApplication(int appId);
	public Sprint getSprint(int sprintId);
	public int getRapidViewId(int appId, int sprintId);
}
