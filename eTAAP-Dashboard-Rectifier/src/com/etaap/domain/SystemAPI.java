package com.etaap.domain;

import java.util.ArrayList;
import java.util.List;

public class SystemAPI {
	private int sysId;
	private String sysName;
	private int apiId;
	private String apiName;
	private String url;
	private String userId;
	private String password;
	private String createdDt;
	private String updatedDt;
	private int status;
	private int customId;
	private String customKey;
	private String customValue;
	private List customKeyList;
	private List customValueList;
	private int isDev;
	private int isQa;
	private int isOperations;
	private String department;
	private List departmentList;
	private String statusNewValue;
	private String statusClosedValue;
	private String statusInProgressValue;
	private String statusVerifyValue;
	private String priorityOneValue;
	private String priorityTwoValue;
	private List priorityNameList;
	private List statusNameList;

	public int getSysId() {
		return sysId;
	}
	public void setSysId(int sysId) {
		this.sysId = sysId;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public int getApiId() {
		return apiId;
	}
	public void setApiId(int apiId) {
		this.apiId = apiId;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getCustomKey() {
		return customKey;
	}
	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}
	public String getCustomValue() {
		return customValue;
	}
	public void setCustomValue(String customValue) {
		this.customValue = customValue;
	}
	public List getCustomKeyList() {
		return customKeyList;
	}
	public void setCustomKeyList(ArrayList customKeyList) {
		this.customKeyList = customKeyList;
	}
	public List getCustomValueList() {
		return customValueList;
	}
	public void setCustomValueList(ArrayList customValueList) {
		this.customValueList = customValueList;
	}
	public int getCustomId() {
		return customId;
	}
	public void setCustomId(int customId) {
		this.customId = customId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getIsDev() {
		return isDev;
	}
	public void setIsDev(int isDev) {
		this.isDev = isDev;
	}
	public int getIsQa() {
		return isQa;
	}
	public void setIsQa(int isQa) {
		this.isQa = isQa;
	}
	public int getIsOperations() {
		return isOperations;
	}
	public void setIsOperations(int isOperations) {
		this.isOperations = isOperations;
	}
	public List getDepartmentList() {
		return departmentList;
	}
	public void setDepartmentList(List departmentList) {
		this.departmentList = departmentList;
	}
	public String getStatusNewValue() {
		return statusNewValue;
	}
	public void setStatusNewValue(String statusNewValue) {
		this.statusNewValue = statusNewValue;
	}
	public String getStatusClosedValue() {
		return statusClosedValue;
	}
	public void setStatusClosedValue(String statusClosedValue) {
		this.statusClosedValue = statusClosedValue;
	}
	public String getStatusInProgressValue() {
		return statusInProgressValue;
	}
	public void setStatusInProgressValue(String statusInProgressValue) {
		this.statusInProgressValue = statusInProgressValue;
	}
	public String getStatusVerifyValue() {
		return statusVerifyValue;
	}
	public void setStatusVerifyValue(String statusVerifyValue) {
		this.statusVerifyValue = statusVerifyValue;
	}
	public String getPriorityOneValue() {
		return priorityOneValue;
	}
	public void setPriorityOneValue(String priorityOneValue) {
		this.priorityOneValue = priorityOneValue;
	}
	public String getPriorityTwoValue() {
		return priorityTwoValue;
	}
	public void setPriorityTwoValue(String priorityTwoValue) {
		this.priorityTwoValue = priorityTwoValue;
	}
	public List getPriorityNameList() {
		return priorityNameList;
	}
	public void setPriorityNameList(List priorityNameList) {
		this.priorityNameList = priorityNameList;
	}
	public List getStatusNameList() {
		return statusNameList;
	}
	public void setStatusNameList(List statusNameList) {
		this.statusNameList = statusNameList;
	}
}
