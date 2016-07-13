package com.etaap.beans;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class TestSuits {
	/*Map<String,List<TestSuite>> testSuits = new HashMap<String,List<TestSuite>>();

	public Map<String, List<TestSuite>> getTestSuits() {
		return testSuits;
	}

	public void setTestSuits(Map<String, List<TestSuite>> testSuits) {
		this.testSuits = testSuits;
	}*/
	
	List<TestSuite> testSuit = new ArrayList<TestSuite>();

	public List<TestSuite> getTestSuites() {
		return testSuit;
	}

	public void setTestSuites(List<TestSuite> testSuites) {
		this.testSuit = testSuites;
	}
}
