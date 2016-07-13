package com.etaap.services;

import java.util.List;
import java.util.Map;

import com.etaap.domain.TestBed;

public interface TestBedService {
	public void insertData(TestBed bed);

	public List<TestBed> getAllTestBedList(String orderBy, String orderType, int offset, int noOfRecords);

	public int getTotalRowCount();

	public List<TestBed> getTestBedList();
	
	public List<TestBed> getTestBedList(Map<String,Object> paramsMap);

	public void updateData(TestBed bed);

	public void deleteData(int bedId);

	public TestBed getTestBed(int bedId);

	public String isNameExistChkByAjaxCall(int id, String name);
	
	public void deleteData(String bedIds);
}
