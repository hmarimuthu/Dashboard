package com.etaap.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.beans.Tcm;
import com.etaap.domain.CI;
import com.etaap.domain.Defects;

public interface DashboardDao {

	public List<Defects> getDetails(HashMap<String, Object> params);

	public List<CI> getJenkinsDetails(HashMap<String, Object> params);
	
	public List<Tcm> getTcmDashBoardChartString();
	
	public List<Map<String,Object>> getDefectsLife(Map params);
	
	public List<Map<String, Object>> getDefectsStatistics(Map<String,Object> params);
	
	public Map<String, Object> getCommitedCompletedUserStories(Map<String,Object> params);
	
}
