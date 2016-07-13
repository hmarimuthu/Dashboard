package com.etaap.domain;

public class Defects {

	private int app_id;
	private String buildDate;
	private String severity;
	private String status;
	private String env_id;
	private int statusCount;
	private int severityCount;
	private int priorityCount;

	private int id;
	private int appId;
	private int envId;
	private String key;
	private String projectName;
	private String priority;
	private String issueType;
	private String created;
	private String updated;
	private String priorityLabel;

	public String getPriorityLabel() {
		return priorityLabel;
	}

	public void setPriorityLabel(String priorityLabel) {
		this.priorityLabel = priorityLabel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public int getStatusCount() {
		return statusCount;
	}

	public void setStatusCount(int statusCount) {
		this.statusCount = statusCount;
	}

	public int getSeverityCount() {
		return severityCount;
	}

	public void setSeverityCount(int severityCount) {
		this.severityCount = severityCount;
	}

	public int getApp_id() {
		return app_id;
	}

	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnv_id() {
		return env_id;
	}

	public void setEnv_id(String env_id) {
		this.env_id = env_id;
	}

	public int getPriorityCount() {
		return priorityCount;
	}

	public void setPriorityCount(int priorityCount) {
		this.priorityCount = priorityCount;
	}
}
