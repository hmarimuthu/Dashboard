package com.etaap.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.TimePeriodDao;
import com.etaap.domain.TimePeriod;

public class TimePeriodServiceImpl implements TimePeriodService {

	@Autowired
	TimePeriodDao timePeriodDao;

	public void insertData(TimePeriod timePeriod) {
		timePeriodDao.insertData(timePeriod);
	}

	public List<TimePeriod> getAllTimePeriodList(int offset, int noOfRecords) {
		return timePeriodDao.getAllTimePeriodList(offset, noOfRecords);
	}

	public int getTotalRowCount() {
		return timePeriodDao.getTotalRowCount();
	}

	public List<TimePeriod> getTimePeriodListByAppId(int appId) {
		return timePeriodDao.getTimePeriodListByAppId(appId);
	}

	public void updateData(TimePeriod timePeriod) {
		timePeriodDao.updateData(timePeriod);
	}

	public void deleteData(int periodId) {
		timePeriodDao.deleteData(periodId);
	}

	public TimePeriod getTimePeriod(int periodId) {
		return timePeriodDao.getTimePeriod(periodId);
	}
}
