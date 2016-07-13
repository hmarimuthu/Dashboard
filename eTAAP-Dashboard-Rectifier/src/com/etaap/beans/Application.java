package com.etaap.beans;

/**Represents only application table. Must not add fields to represent columns of other table.
 * */
public class Application {
	
	private int appId;
	private String appName;
	
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
}
