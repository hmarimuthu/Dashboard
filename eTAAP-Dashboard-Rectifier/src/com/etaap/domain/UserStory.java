package com.etaap.domain;

public class UserStory {
	private int appId;
	private int rapidViewId;
	private String rapidViewName;
	private int sprintId;
	private String sprintName;
	private String key;
	private String summary;
	private String priority;
	private String startDt;
	private String endDt;
	private int estimated;
	private int completed;
	private String status;

	private int jiraid;
	
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getRapidViewId() {
		return rapidViewId;
	}
	public void setRapidViewId(int rapidViewId) {
		this.rapidViewId = rapidViewId;
	}
	public String getRapidViewName() {
		return rapidViewName;
	}
	public void setRapidViewName(String rapidViewName) {
		this.rapidViewName = rapidViewName;
	}
	public int getSprintId() {
		return sprintId;
	}
	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
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
	public int getEstimated() {
		return estimated;
	}
	public void setEstimated(int estimated) {
		this.estimated = estimated;
	}
	public int getCompleted() {
		return completed;
	}
	public void setCompleted(int completed) {
		this.completed = completed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setJiraId(int jiraid) {
		this.jiraid = jiraid;
	}
	public int getJiraId() {
		return jiraid;
	}
	
}
