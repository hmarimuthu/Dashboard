package com.etaap.services;

import java.util.List;

import com.etaap.domain.TimePeriod;

public interface TimePeriodService {
	public void insertData(TimePeriod timePeriod);

	public List<TimePeriod> getAllTimePeriodList(int offset, int noOfRecords);

	public int getTotalRowCount();

	public List<TimePeriod> getTimePeriodListByAppId(int appId);

	public void updateData(TimePeriod timePeriod);

	public void deleteData(int periodId);

	public TimePeriod getTimePeriod(int periodId);
}
