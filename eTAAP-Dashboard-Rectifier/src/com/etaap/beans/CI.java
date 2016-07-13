package com.etaap.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CI {
	List<Integer> buildNumberCSV = new ArrayList<Integer>();
	List<Integer> passCountCSV = new ArrayList<Integer>();
	List<Integer> failCountCSV = new ArrayList<Integer>();
	List<Integer> skipCountCSV = new ArrayList<Integer>();
	List<Integer> buildDateCSV = new ArrayList<Integer>();
	List<String> appNameCSV = new ArrayList<String>();
	List<Integer> buildCountCSV = new ArrayList<Integer>();
	
	public List<Integer> getBuildNumberCSV() {
		return buildNumberCSV;
	}
	public void setBuildNumberCSV(List<Integer> buildNumberCSV) {
		this.buildNumberCSV = buildNumberCSV;
	}
	public List<Integer> getPassCountCSV() {
		return passCountCSV;
	}
	public void setPassCountCSV(List<Integer> passCountCSV) {
		this.passCountCSV = passCountCSV;
	}
	public List<Integer> getFailCountCSV() {
		return failCountCSV;
	}
	public void setFailCountCSV(List<Integer> failCountCSV) {
		this.failCountCSV = failCountCSV;
	}
	public List<Integer> getSkipCountCSV() {
		return skipCountCSV;
	}
	public void setSkipCountCSV(List<Integer> skipCountCSV) {
		this.skipCountCSV = skipCountCSV;
	}
	public List<Integer> getBuildDateCSV() {
		return buildDateCSV;
	}
	public void setBuildDateCSV(List<Integer> buildDateCSV) {
		this.buildDateCSV = buildDateCSV;
	}
	public List<String> getAppNameCSV() {
		return appNameCSV;
	}
	public void setAppNameCSV(List<String> appNameCSV) {
		this.appNameCSV = appNameCSV;
	}
	public List<Integer> getBuildCountCSV() {
		return buildCountCSV;
	}
	public void setBuildCountCSV(List<Integer> buildCountCSV) {
		this.buildCountCSV = buildCountCSV;
	}

	/*Map<String, Object> ciResuls = new HashMap<String, Object>();

	public Map<String, Object> getCiResuls() {
		return ciResuls;
	}

	public void setCiResuls(Map<String, Object> ciResuls) {
		this.ciResuls = ciResuls;
	}*/
	
	
	
	
	
}
