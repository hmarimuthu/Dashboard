package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class UserStoriesPerSprintSeries {
	private String name;
	private String type = "spline";
	
	private List<UserStoryPerSprintSeriesElement> data = new ArrayList<UserStoryPerSprintSeriesElement>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<UserStoryPerSprintSeriesElement> getData() {
		return data;
	}
	public void setData(List<UserStoryPerSprintSeriesElement> data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
