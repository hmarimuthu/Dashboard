package com.etaap.domain;

public class StoryPoint {
	
	private int userstoryId;
	private String userStoryName;
	private int increment;
	private int decrement;
	private int statusCode;
	private int isAdded;
	private int isDelelted;
	private String statusMessage;
	private long activityMiliSecs;
	private int oldValue;
	
	public int getUserstoryId() {
		return userstoryId;
	}
	public void setUserstoryId(int userstoryId) {
		this.userstoryId = userstoryId;
	}
	public int getIncrement() {
		return increment;
	}
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	public int getDecrement() {
		return decrement;
	}
	public void setDecrement(int decrement) {
		this.decrement = decrement;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getIsAdded() {
		return isAdded;
	}
	public void setIsAdded(int isAdded) {
		this.isAdded = isAdded;
	}
	public int getIsDelelted() {
		return isDelelted;
	}
	public void setIsDelelted(int isDelelted) {
		this.isDelelted = isDelelted;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public long getActivityMiliSecs() {
		return activityMiliSecs;
	}
	public void setActivityMiliSecs(long activityMiliSecs) {
		this.activityMiliSecs = activityMiliSecs;
	}
	public String getUserStoryName() {
		return userStoryName;
	}
	public void setUserStoryName(String userStoryName) {
		this.userStoryName = userStoryName;
	}
	
	public String toString() {
		return "userstoryId="+userstoryId+", userStoryName="+userStoryName+", increment="+increment+", decrement="+decrement
				+", statusCode="+statusCode+", isAdded="+isAdded+", isDelelted="+isDelelted
				+", activityMiliSecs="+activityMiliSecs+", statusMessage="+statusMessage
				+" END--"+"\n";
	}
	public int getOldValue() {
		return oldValue;
	}
	public void setOldValue(int oldValue) {
		this.oldValue = oldValue;
	}
}
