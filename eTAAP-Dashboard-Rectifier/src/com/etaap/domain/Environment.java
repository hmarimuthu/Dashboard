package com.etaap.domain;

public class Environment {
	private int envId;
	private String envName;
	private String createdDt;
	private String updatedDt;
	private int status;

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
}
