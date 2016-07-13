package com.etaap.domain;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
	private int appId;
	private String appName;
	private String ids;
	private String tabIds;
	private String suiteIds;
	private String tcTypes;
	private String tcCount;
	private List idList;
	private List suiteIdList;
	private List tcTypeList;
	private List tcCountList;
	private List tabIdList;
	private List<TimePeriod> quarterList;
	private String recordType;

	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getTabIds() {
		return tabIds;
	}
	public void setTabIds(String tabIds) {
		this.tabIds = tabIds;
	}
	public String getSuiteIds() {
		return suiteIds;
	}
	public void setSuiteIds(String suiteIds) {
		this.suiteIds = suiteIds;
	}
	public String getTcTypes() {
		return tcTypes;
	}
	public void setTcTypes(String tcTypes) {
		this.tcTypes = tcTypes;
	}
	public String getTcCount() {
		return tcCount;
	}
	public void setTcCount(String tcCount) {
		this.tcCount = tcCount;
	}
	public List getIdList() {
		return idList;
	}
	public void setIdList(ArrayList idList) {
		this.idList = idList;
	}
	public List getSuiteIdList() {
		return suiteIdList;
	}
	public void setSuiteIdList(ArrayList suiteIdList) {
		this.suiteIdList = suiteIdList;
	}
	public List getTcTypeList() {
		return tcTypeList;
	}
	public void setTcTypeList(ArrayList tcTypeList) {
		this.tcTypeList = tcTypeList;
	}
	public List getTcCountList() {
		return tcCountList;
	}
	public void setTcCountList(ArrayList tcCountList) {
		this.tcCountList = tcCountList;
	}
	public List getTabIdList() {
		return tabIdList;
	}
	public void setTabIdList(ArrayList tabIdList) {
		this.tabIdList = tabIdList;
	}
	public List<TimePeriod> getQuarterList() {
		return quarterList;
	}
	public void setQuarterList(List<TimePeriod> quarterList) {
		this.quarterList = quarterList;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
}
