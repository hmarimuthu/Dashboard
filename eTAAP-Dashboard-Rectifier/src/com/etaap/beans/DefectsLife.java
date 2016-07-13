package com.etaap.beans;

import java.util.ArrayList;
import java.util.List;

public class DefectsLife {
	List<Series> series = new ArrayList<Series>();
	List<String> categories = new ArrayList<String>();
	
	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
}
