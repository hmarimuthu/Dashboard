package com.etaap.dao;

import java.util.List;

import com.etaap.domain.TestCase;

public interface TestCaseDao {
	public void insertData(TestCase testCase);

	public List<TestCase> getAllTestCaseList(String orderBy, String orderType, int offset, int noOfRecords);

	public TestCase getTestCaseByAppId(int appId);

	public int getTotalRowCount();

	public void updateData(TestCase testCase);

	public void deleteData(int appId);

	public String deleteUpdateTCMByAjaxCall(String action, String recordId, String suiteId, String tcType, String tcCount);
	
	public void deleteData(String appId);
}
