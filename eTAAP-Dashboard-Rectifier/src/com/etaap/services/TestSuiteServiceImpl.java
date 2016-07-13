package com.etaap.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.etaap.dao.TestSuiteDao;
import com.etaap.domain.TestSuite;

public class TestSuiteServiceImpl implements TestSuiteService {

	@Autowired
	TestSuiteDao testSuiteDao;

	public void insertData(TestSuite suite) {
		testSuiteDao.insertData(suite);
	}

	public List<TestSuite> getAllTestSuiteList(String orderBy, String orderType, int offset, int noOfRecords) {
		return testSuiteDao.getAllTestSuiteList(orderBy, orderType, offset, noOfRecords);
	}

	public int getTotalRowCount() {
		return testSuiteDao.getTotalRowCount();
	}

	public List<TestSuite> getTestSuiteList() {
		return testSuiteDao.getTestSuiteList();
	}

	public void updateData(TestSuite suite) {
		testSuiteDao.updateData(suite);
	}

	public void deleteData(int suiteId) {
		testSuiteDao.deleteData(suiteId);
	}

	public TestSuite getTestSuite(int suiteId) {
		return testSuiteDao.getTestSuite(suiteId);
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		return testSuiteDao.isNameExistChkByAjaxCall(id, name);
	}

	@Override
	public void deleteData(String suiteIds) {
		testSuiteDao.deleteData(suiteIds);
	}

	@Override
	public List<TestSuite> getTestSuiteList(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return testSuiteDao.getTestSuiteList(paramsMap);
	}
	
	
}
