package com.etaap.domain;

public class TimePeriod {
	private int periodId;
	private String periodName;
	private int appId;
	private String appName;
	private int monthId;
	private String monthName;
	private String startDt;
	private String endDt;
	private String createdDt;
	private String updatedDt;
	private int status;

	public int getPeriodId() {
		return periodId;
	}
	public void setPeriodId(int periodId) {
		this.periodId = periodId;
	}
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getMonthId() {
		return monthId;
	}
	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
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
}
