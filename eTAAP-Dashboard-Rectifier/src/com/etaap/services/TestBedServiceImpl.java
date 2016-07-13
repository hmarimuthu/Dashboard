package com.etaap.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.TestBedDao;
import com.etaap.domain.TestBed;

public class TestBedServiceImpl implements TestBedService {

	@Autowired
	TestBedDao testBedDao;

	public void insertData(TestBed bed) {
		testBedDao.insertData(bed);
	}

	public List<TestBed> getAllTestBedList(String orderBy, String orderType, int offset, int noOfRecords) {
		return testBedDao.getAllTestBedList(orderBy, orderType, offset, noOfRecords);
	}

	public int getTotalRowCount() {
		return testBedDao.getTotalRowCount();
	}

	public List<TestBed> getTestBedList() {
		return testBedDao.getTestBedList();
	}

	public void updateData(TestBed bed) {
		testBedDao.updateData(bed);
	}

	public void deleteData(int bedId) {
		testBedDao.deleteData(bedId);
	}

	public TestBed getTestBed(int bedId) {
		return testBedDao.getTestBed(bedId);
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		return testBedDao.isNameExistChkByAjaxCall(id, name);
	}

	@Override
	public void deleteData(String bedIds) {
		// TODO Auto-generated method stub
		testBedDao.deleteData(bedIds);
	}

	@Override
	public List<TestBed> getTestBedList(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return testBedDao.getTestBedList(paramsMap);
	}
}
