package com.etaap.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
	private int appId;
	private String appName;
	private String systemId;
	private String sysName;
	private String url;
	private String userId;
	private String password;
	private String urlAlias;
	private int envId;
	private String envName;
	private int bedId;
	private String bedName;
	private int suiteId;
	private String suiteName;
	private int monthId;
	private String monthName;
	private String createdDt;
	private String updatedDt;
	private int status;
	private List sysIdList;
	private List urlAliasList;
	private String envIds;
	private String bedIds;
	private String suiteIds;
	private String isDefault;
	private List envIdList;
	private List suiteIdList;
	private List bedIdList;
	private List isDefaultList;
	private int lastAppId;
	private String tabId;
	private List mapIdList;
	private List tabIdList;
	private Map mapOfTabs;
	private String mapId;
	private int sysId;
	private String priority;
	private List apiIdList;
	private int apiId;
	private String deptType;
	private List concatedUrlAliasList;
	private String action;
	private boolean flag;
	
	
	

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
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
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
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
	public String getUrlAlias() {
		return urlAlias;
	}
	public void setUrlAlias(String urlAlias) {
		this.urlAlias = urlAlias;
	}
	public int getEnvId() {
		return envId;
	}
	public void setEnvId(int envId) {
		this.envId = envId;
	}
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public int getBedId() {
		return bedId;
	}
	public void setBedId(int bedId) {
		this.bedId = bedId;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public int getSuiteId() {
		return suiteId;
	}
	public void setSuiteId(int suiteId) {
		this.suiteId = suiteId;
	}
	public String getSuiteName() {
		return suiteName;
	}
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	public int getMonthId() {
		return monthId;
	}
	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
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
	public List getSysIdList() {
		return sysIdList;
	}
	public void setSysIdList(ArrayList sysIdList) {
		this.sysIdList = sysIdList;
	}
	public List getUrlAliasList() {
		return urlAliasList;
	}
	public void setUrlAliasList(ArrayList urlAliasList) {
		this.urlAliasList = urlAliasList;
	}
	public String getEnvIds() {
		return envIds;
	}
	public void setEnvIds(String envIds) {
		this.envIds = envIds;
	}
	public String getSuiteIds() {
		return suiteIds;
	}
	public void setSuiteIds(String suiteIds) {
		this.suiteIds = suiteIds;
	}
	public String getBedIds() {
		return bedIds;
	}
	public void setBedIds(String bedIds) {
		this.bedIds = bedIds;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public List getEnvIdList() {
		return envIdList;
	}
	public void setEnvIdList(ArrayList envIdList) {
		this.envIdList = envIdList;
	}
	public List getSuiteIdList() {
		return suiteIdList;
	}
	public void setSuiteIdList(ArrayList suiteIdList) {
		this.suiteIdList = suiteIdList;
	}
	public List getBedIdList() {
		return bedIdList;
	}
	public void setBedIdList(ArrayList bedIdList) {
		this.bedIdList = bedIdList;
	}
	public List getIsDefaultList() {
		return isDefaultList;
	}
	public void setIsDefaultList(ArrayList isDefaultList) {
		this.isDefaultList = isDefaultList;
	}
	public int getLastAppId() {
		return lastAppId;
	}
	public void setLastAppId(int lastAppId) {
		this.lastAppId = lastAppId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public List getMapIdList() {
		return mapIdList;
	}
	public void setMapIdList(ArrayList mapIdList) {
		this.mapIdList = mapIdList;
	}
	public List getTabIdList() {
		return tabIdList;
	}
	public void setTabIdList(ArrayList tabIdList) {
		this.tabIdList = tabIdList;
	}
	public Map getMapOfTabs() {
		return mapOfTabs;
	}
	public void setMapOfTabs(Map mapOfTabs) {
		this.mapOfTabs = mapOfTabs;
	}
	public String getMapId() {
		return mapId;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	public int getSysId() {
		return sysId;
	}
	public void setSysId(int sysId) {
		this.sysId = sysId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public List getApiIdList() {
		return apiIdList;
	}
	public void setApiIdList(ArrayList apiIdList) {
		this.apiIdList = apiIdList;
	}
	public int getApiId() {
		return apiId;
	}
	public void setApiId(int apiId) {
		this.apiId = apiId;
	}
	public String getDeptType() {
		return deptType;
	}
	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public List getConcatedUrlAliasList() {
		return concatedUrlAliasList;
	}
	public void setConcatedUrlAliasList(List concatedUrlAliasList) {
		this.concatedUrlAliasList = concatedUrlAliasList;
	}
}
