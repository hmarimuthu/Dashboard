package com.etaap.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.TestCaseDao;
import com.etaap.domain.TestCase;

public class TestCaseServiceImpl implements TestCaseService {

	@Autowired
	TestCaseDao testCaseDao;

	public void insertData(TestCase testCase) {
		testCaseDao.insertData(testCase);
	}

	public List<TestCase> getAllTestCaseList(String orderBy, String orderType, int offset, int noOfRecords) {
		return testCaseDao.getAllTestCaseList(orderBy, orderType, offset, noOfRecords);
	}

	public TestCase getTestCaseByAppId(int appId) {
		return testCaseDao.getTestCaseByAppId(appId);
	}

	public int getTotalRowCount() {
		return testCaseDao.getTotalRowCount();
	}

	public void updateData(TestCase testCase) {
		testCaseDao.updateData(testCase);
	}

	public void deleteData(int appId) {
		testCaseDao.deleteData(appId);
	}

	public String deleteUpdateTCMByAjaxCall(String action, String recordId, String suiteId, String tcType, String tcCount) {
		return testCaseDao.deleteUpdateTCMByAjaxCall(action, recordId, suiteId, tcType, tcCount);
	}

	@Override
	public void deleteData(String appId) {
		// TODO Auto-generated method stub
		testCaseDao.deleteData(appId);
	}
}
