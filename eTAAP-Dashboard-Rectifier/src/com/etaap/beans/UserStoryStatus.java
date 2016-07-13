package com.etaap.beans;
import java.util.Date;

public class UserStoryStatus {
//	private long jiraId;
	private int userstoryId;
	private String userStoryName;
	private int statusCode;
	private String statusName;
	private String dateTime;
	
/*	public long getJiraId() {
		return jiraId;
	}
	public void setJiraId(long jiraId) {
		this.jiraId = jiraId;
	}
*/	
	public String getUserStoryName() {
		return userStoryName;
	}
	public void setUserStoryName(String userStoryName) {
		this.userStoryName = userStoryName;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public int getUserstoryId() {
		return userstoryId;
	}
	public void setUserstoryId(int userstoryId) {
		this.userstoryId = userstoryId;
	}
	
	
	public String toString() {
		String toString = "UserStoryStatus: userstory_id="+userstoryId+", key="+userStoryName+", status_code="+statusCode+", "+
				"status_datetime="+dateTime+"\n";
		return toString;
	}
}
