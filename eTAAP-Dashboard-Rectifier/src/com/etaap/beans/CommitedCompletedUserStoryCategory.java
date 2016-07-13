package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class CommitedCompletedUserStoryCategory {
	
	String name;
	
	List<String> categories = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
}
