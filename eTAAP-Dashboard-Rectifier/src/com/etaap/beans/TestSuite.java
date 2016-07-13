package com.etaap.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSuite {
	public String name;
	
	public String suiteId;
	//public Testbeds testBeds;
	

	List<Testbed> testBed = new ArrayList<Testbed>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Testbed> getTestBed() {
		return testBed;
	}

	public void setTestBed(List<Testbed> testBed) {
		this.testBed = testBed;
	}
	
	public String getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(String suiteId) {
		this.suiteId = suiteId;
	}
	
	
	
	
	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Testbeds getTestBeds() {
		return testBeds;
	}
	public void setTestBeds(Testbeds testBeds) {
		this.testBeds = testBeds;
	}*/
	
	
}
