package com.etaap.beans;

public class Sprint {
	
	private int sprintId;
	private String sprintName;
	private int userStoriesCountAll;
	private int userStoriesCountCompleted;
	
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
	public int getUserStoriesCountAll() {
		return userStoriesCountAll;
	}
	public void setUserStoriesCountAll(int userStoriesCountAll) {
		this.userStoriesCountAll = userStoriesCountAll;
	}
	public int getUserStoriesCountCompleted() {
		return userStoriesCountCompleted;
	}
	public void setUserStoriesCountCompleted(int userStoriesCountCompleted) {
		this.userStoriesCountCompleted = userStoriesCountCompleted;
	}
	
}
