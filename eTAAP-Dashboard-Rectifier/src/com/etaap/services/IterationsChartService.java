package com.etaap.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.beans.Application;
import com.etaap.beans.Sprint;

public interface IterationsChartService {
	public List<Sprint> getSprintList(int appId);
	public List<Application> getApplicationList();
	public List<Application> getApplicationList(int appId);
	public int getUserStoriesCount(Map params);
	public Application getApplication(int appId);
	public HashMap<String, Object> getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(int appId, int sprintId, int rapidviewId);
	public Sprint getSprint(int sprintId);
	public int getRapidViewId(int appId, int sprintId);
}
