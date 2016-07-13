package com.etaap.scheduler.beans;

import java.util.HashMap;
import java.util.Map;

public class Job {
	String unique_key;
	String name;
	String className;
	String groupName;
	String status;
	Map<String,Object> ScheduleTime = new HashMap<String,Object>(); 
	Map<String,Object> command = new HashMap<String,Object>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Map<String, Object> getScheduleTime() {
		return ScheduleTime;
	}
	public void setScheduleTime(Map<String, Object> scheduleTime) {
		ScheduleTime = scheduleTime;
	}
	public Map<String, Object> getCommand() {
		return command;
	}
	public void setCommand(Map<String, Object> command) {
		this.command = command;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUnique_key() {
		return unique_key;
	}
	public void setUnique_key(String unique_key) {
		this.unique_key = unique_key;
	}
	
}
