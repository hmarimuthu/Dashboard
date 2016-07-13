package com.etaap.beans;

public class Tcm {
	
	private int appId;
	private int suiteId;
	private String testCaseType;
	private int testCaseCount;
	private String testSuiteName;
	private String appName;
	private String quarterStartDate;

	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getSuiteId() {
		return suiteId;
	}
	public void setSuiteId(int suiteId) {
		this.suiteId = suiteId;
	}
	public String getTestCaseType() {
		return testCaseType;
	}
	public void setTestCaseType(String testCaseType) {
		this.testCaseType = testCaseType;
	}
	public int getTestCaseCount() {
		return testCaseCount;
	}
	public void setTestCaseCount(int testCaseCount) {
		this.testCaseCount = testCaseCount;
	}
	public String getTestSuiteName() {
		return testSuiteName;
	}
	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getQuarterStartDate() {
		return quarterStartDate;
	}
	public void setQuarterStartDate(String quarterStartDate) {
		this.quarterStartDate = quarterStartDate;
	}
}
