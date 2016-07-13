package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class Velocity {
	
	private int appId;
	private int sprintId;
	private int rapidViewId;
	private int estimated;
	private int completed;
	private String sprintName;
	private String quarterStartDate;
	private String quarterEndDate;
	List<Integer> estimatedCSV = new ArrayList<Integer>();
	List<Integer> completedCSV = new ArrayList<Integer>();
	List<String> sprintNameCSV = new ArrayList<String>();
	
	
	public List<Integer> getEstimatedCSV() {
		return estimatedCSV;
	}
	public void setEstimatedCSV(List<Integer> estimatedCSV) {
		this.estimatedCSV = estimatedCSV;
	}
	public List<Integer> getCompletedCSV() {
		return completedCSV;
	}
	public void setCompletedCSV(List<Integer> completedCSV) {
		this.completedCSV = completedCSV;
	}
	public List<String> getSprintNameCSV() {
		return sprintNameCSV;
	}
	public void setSprintNameCSV(List<String> sprintNameCSV) {
		this.sprintNameCSV = sprintNameCSV;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getSprintId() {
		return sprintId;
	}
	public void setSprintId(int sprintId) {
		this.sprintId = sprintId;
	}
	public int getRapidViewId() {
		return rapidViewId;
	}
	public void setRapidViewId(int rapidViewId) {
		this.rapidViewId = rapidViewId;
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
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public String getQuarterStartDate() {
		return quarterStartDate;
	}
	public void setQuarterStartDate(String quarterStartDate) {
		this.quarterStartDate = quarterStartDate;
	}
	public String getQuarterEndDate() {
		return quarterEndDate;
	}
	public void setQuarterEndDate(String quarterEndDate) {
		this.quarterEndDate = quarterEndDate;
	}


}
