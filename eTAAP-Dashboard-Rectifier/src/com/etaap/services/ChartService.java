package com.etaap.services;

import java.util.List;

import javax.servlet.ServletContext;

import com.etaap.beans.ChartModel;

public interface ChartService {	
	public String getCharts(ServletContext servletContext, List<ChartModel> chartsList);
}