package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class Environment {
	
	String name;
	String envId;
	String mapId;
	
	List<TestSuits> testSuits = new ArrayList<TestSuits>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TestSuits> getTestSuits() {
		return testSuits;
	}
	public void setTestSuits(List<TestSuits> testSuits) {
		this.testSuits = testSuits;
	}
	public String getMapId() {
		return mapId;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	public String getEnvId() {
		return envId;
	}
	public void setEnvId(String envId) {
		this.envId = envId;
	}
	
	
}
