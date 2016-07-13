package com.etaap.domain;

public class TestBed {
	private int bedId;
	private String bedName;
	private String createdDt;
	private String updatedDt;
	private int status;

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
