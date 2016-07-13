package com.etaap.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.SchedulerJobsDao;

public class SchedulerJobsServiceImpl implements SchedulerJobsService {
	
	@Autowired
	SchedulerJobsDao schedulerJobsDao;

	public List<Map<String, Object>> getScheduledJobs(Map Params) {
		// TODO Auto-generated method stub
		return schedulerJobsDao.getScheduledJobs(Params);
	}

	public int updateScheduledJobsRecords(Map params) {
		// TODO Auto-generated method stub
		return schedulerJobsDao.insertScheduledJobsRecords(params);
	}

	public int insertScheduledJobs(Map params) {
		// TODO Auto-generated method stub
		return schedulerJobsDao.insertScheduledJobs(params);
	}

	public int updateScheduledJobs(Map params) {
		// TODO Auto-generated method stub
		return schedulerJobsDao.updateScheduledJobs(params);
	}

	public int getScheduledJobsStatusBasedOnPk(int pk) {
		// TODO Auto-generated method stub
		return schedulerJobsDao.getScheduledJobsStatusBasedOnPk(pk);
	}

	public List<Map<String, Object>> getScheduledJobsOnPk(int pk) {
		// TODO Auto-generated method stub
		return schedulerJobsDao.getScheduledJobsOnPk(pk);
	}

//	@Override
	public List<Map<String, Object>> _getScheduledJobs(Map Params) {
		// TODO Auto-generated method stub
		return schedulerJobsDao._getScheduledJobs(Params);
	}

	@Override
	public void insertScheduledJobRecords(String fkjobId,
			String triggerFireTime, String status, String log, String mapId) {
		// TODO Auto-generated method stub
		schedulerJobsDao.insertScheduledJobRecords(fkjobId, triggerFireTime, status, log, mapId);
		
	}
}
