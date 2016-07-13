package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class DefectsStatistics {
	List<Series> series = new ArrayList<Series>();
	List<String> categories = null;
	public List<Series> getSeries() {
		return series;
	}
	public void setSeries(List<Series> series) {
		this.series = series;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	
}
