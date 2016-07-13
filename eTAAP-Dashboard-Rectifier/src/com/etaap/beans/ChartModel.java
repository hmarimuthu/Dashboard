/*
 *Copyright (C) ETouch Systems 2015
 */

package com.etaap.beans;

import java.util.List;

import com.etaap.utils.enums.ChartType;

/*
 * File name		:	ChartModel.java
 * Description		: 	This file contains the properties of 
 * Chart Query Parameters for eTAAP Dashboard application.
   
 * Version			:	1.0 Draft
 * Author(s)		:	ETouch Systems - Shreekanta Pradhan
 * Created On		:	Sep 29th 2015
 *					
 * Modification History  :
 * 
 * Sl.No.           Date                  Author                Modification
 *    1          Sep 29th 2015	 		ETouch Systems      Initial Version  
 */

 /**
  * This class provides getters/setters for the specific Result of
  * eTAAP application.
  */


public class ChartModel {
	
	private String chartName;
	private ChartType chartType;
	private List<String> chartCategories;
	private Object[] aggregateQueryObj;
	private Object[] secondAggrQueryObj;
	private Object[] drillDownQueryObj;

	public ChartModel(String chartName,	ChartType chartType, 
			List<String> chartCategories, Object[] aggregateQueryObj) {
		this.chartName = chartName;
		this.chartType = chartType;
		this.chartCategories = chartCategories;
		this.aggregateQueryObj = aggregateQueryObj;
	}
	
	public ChartModel(String chartName,	ChartType chartType, 
			List<String> chartCategories, Object[] aggregateQueryObj, Object[] secondAggrQueryObj) {
		this.chartName = chartName;
		this.chartType = chartType;
		this.chartCategories = chartCategories;
		this.aggregateQueryObj = aggregateQueryObj;
		this.secondAggrQueryObj = secondAggrQueryObj;
	}
	
	public ChartModel(String chartName,	ChartType chartType, 
			Object[] aggregateQueryObj) {
		this.chartName = chartName;
		this.chartType = chartType;
		this.aggregateQueryObj = aggregateQueryObj;
	}
	
	public ChartModel(String chartName,	ChartType chartType, 
			Object[] aggregateQueryObj, Object[] drillDownQueryObj) {
		this.chartName = chartName;
		this.chartType = chartType;
		this.aggregateQueryObj = aggregateQueryObj;
		this.drillDownQueryObj = drillDownQueryObj;
	}
	
	public ChartModel(String chartName,	ChartType chartType, Object[] aggregateQueryObj, 
			Object[] secondAggrQueryObj, Object[] drillDownQueryObj) {
		this.chartName = chartName;
		this.chartType = chartType;
		this.aggregateQueryObj = aggregateQueryObj;
		this.secondAggrQueryObj = secondAggrQueryObj;
		this.drillDownQueryObj = drillDownQueryObj;
	}
	
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public ChartType getChartType() {
		return chartType;
	}
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}
	public List<String> getChartCategories() {
		return chartCategories;
	}
	public void setChartCategories(List<String> chartCategories) {
		this.chartCategories = chartCategories;
	}
	public Object[] getAggregateQueryObj() {
		return aggregateQueryObj;
	}
	public void setAggregateQueryObj(Object[] aggregateQueryObj) {
		this.aggregateQueryObj = aggregateQueryObj;
	}
	public Object[] getSecondAggrQueryObj() {
		return secondAggrQueryObj;
	}
	public void setSecondAggrQueryObj(Object[] secondAggrQueryObj) {
		this.secondAggrQueryObj = secondAggrQueryObj;
	}
	public Object[] getDrillDownQueryObj() {
		return drillDownQueryObj;
	}
	public void setDrillDownQueryObj(Object[] drillDownQueryObj) {
		this.drillDownQueryObj = drillDownQueryObj;
	}
}
