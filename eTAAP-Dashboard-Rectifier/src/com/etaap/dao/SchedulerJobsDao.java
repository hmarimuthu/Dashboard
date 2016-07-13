package com.etaap.dao;

import java.util.List;
import java.util.Map;

public interface SchedulerJobsDao {
	public List getScheduledJobs(Map params);
	public int insertScheduledJobsRecords(Map params);
	public int insertScheduledJobs(Map params);
	public int updateScheduledJobs(Map params);
	public int getScheduledJobsStatusBasedOnPk(int pk);
	public List<Map<String,Object>> getScheduledJobsOnPk(int pk);
	public List _getScheduledJobs(Map params);
	public void insertScheduledJobRecords(String fkjobId, String triggerFireTime, String status, String log, String mapId);
	
}
