package com.etaap.services;

import java.util.List;
import java.util.Map;

public interface SchedulerJobsService {
	public List<Map<String,Object>> getScheduledJobs(Map Params);
	public int updateScheduledJobsRecords(Map params);
	public int insertScheduledJobs(Map params);
	public int updateScheduledJobs(Map params);
	public int getScheduledJobsStatusBasedOnPk(int pk);
	public List<Map<String,Object>> getScheduledJobsOnPk(int pk);
	public List<Map<String,Object>> _getScheduledJobs(Map Params);
	public void insertScheduledJobRecords(String fkjobId, String triggerFireTime, String status, String log, String mapId);
}
