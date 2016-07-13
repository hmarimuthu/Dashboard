package com.etaap.domain;

public class CI {
	private int appId;
	private int envId;
	private int suiteId;
	private int bedId;
	private String buildId;
	private String buildName;
	private int buildNumber;
	private String buildUrl;
	private String buildDate;
	private String result;
	private int failCount;
	private int passCount;
	private int skipCount;
	private int totalCount;
	private String createdDt;
	private String updatedDt;
	private int status;
	private String appName;
	private int buildCount;

	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getEnvId() {
		return envId;
	}
	public void setEnvId(int envId) {
		this.envId = envId;
	}
	public int getSuiteId() {
		return suiteId;
	}
	public void setSuiteId(int suiteId) {
		this.suiteId = suiteId;
	}
	public int getBedId() {
		return bedId;
	}
	public void setBedId(int bedId) {
		this.bedId = bedId;
	}
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	public String getBuildName() {
		return buildName;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	public int getBuildNumber() {
		return buildNumber;
	}
	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}
	public String getBuildUrl() {
		return buildUrl;
	}
	public void setBuildUrl(String buildUrl) {
		this.buildUrl = buildUrl;
	}
	public String getBuildDate() {
		return buildDate;
	}
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public int getPassCount() {
		return passCount;
	}
	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}
	public int getSkipCount() {
		return skipCount;
	}
	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}
	public String getUpdatedDt() {
		return updatedDt;
	}
	public void setUpdatedDt(String updatedDt) {
		this.updatedDt = updatedDt;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public int getBuildCount() {
		return buildCount;
	}
	public void setBuildCount(int buildCount) {
		this.buildCount = buildCount;
	}
}
