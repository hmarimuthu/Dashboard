package com.etaap.dao;

import java.util.List;
import java.util.Map;

import com.etaap.domain.TestSuite;

public interface TestSuiteDao {
	public void insertData(TestSuite suite);

	public List<TestSuite> getAllTestSuiteList(String orderBy, String orderType, int offset, int noOfRecords);

	public int getTotalRowCount();

	public List<TestSuite> getTestSuiteList();
	
	public List<TestSuite> getTestSuiteList(Map<String, Object> paramsMap);

	public void updateData(TestSuite suite);

	public void deleteData(int suiteId);

	public TestSuite getTestSuite(int suiteId);

	public String isNameExistChkByAjaxCall(int id, String name);
	
	public void deleteData(String suiteIds);
}
