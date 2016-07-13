package com.etaap.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.etaap.services.DashboardService;
import com.etaap.services.IterationsChartService;
import com.etaap.utils.Utils;

public class UserStoriesPerSprint {

	private List<UserStoriesPerSprintSeries> series = new ArrayList<UserStoriesPerSprintSeries>();
	private List<String> categories = new ArrayList<String>();
	
//	public long minUTCDate;
//	public long maxUTCDate;
	
	private String sprintName;
	private String applicationName;
	
	public String getSprintName() {
		return sprintName;
	}
	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}	
	
	public List<UserStoriesPerSprintSeries> getSeries() {
		return series;
	}
	public void setSeries(List<UserStoriesPerSprintSeries> series) {
		this.series = series;
	}
	
/*	private static List<String> getChartCompatibleDates(List<String> allDatesOfSprint) {
		List<String> arrayList = new 
		for(int i = 0; i < allDatesOfSprint.size(); i++) {
			
		}
	}
*/	
	private static long getMiliSeconds(String dateStr) {

	    String dateArr[] = dateStr.split("-");
	    
	    String year = dateArr[0];
	    String month = dateArr[1];
	    String date = dateArr[2];
	    
	    int yearInt = Integer.parseInt(year);
	    int monthInt = Integer.parseInt(month);
	    monthInt--;
	    int dateInt = Integer.parseInt(date);
	    
	    Calendar startCal = Calendar.getInstance();
//	    startCal.set(yearInt, monthInt, dateInt);
	    startCal.set(yearInt, monthInt, dateInt, 0, 0, 1);
	    
	    return startCal.getTimeInMillis();
	}
	
	private static List<UserStoryPerSprintSeriesElement> getChartCompatibleSeries(List<Map<String, Object>> records, List<String> allDatesOfSprint) {
		List<UserStoryPerSprintSeriesElement> series = new ArrayList<UserStoryPerSprintSeriesElement>();
		for(int i = 0; i < allDatesOfSprint.size(); i++) {
			series.add(null);
		}
		List<String> presentDatesOfSprint = new ArrayList<String>();
		
		for(Map<String, Object> map : records) {
			int count1 = ((Long)map.get("count")).intValue();
			String onDate = (String)map.get("onDate");
			presentDatesOfSprint.add(onDate);
			System.out.println("************ON Date "+onDate);
			int indexOfOnDateInAllDatesOfSprint = allDatesOfSprint.indexOf(onDate);
//			long onDateMili1 = getMiliSeconds(onDate);
			UserStoryPerSprintSeriesElement ele = new UserStoryPerSprintSeriesElement();
			ele.setY(count1);
//			ele.setX(onDateMili1);
			series.set(indexOfOnDateInAllDatesOfSprint, ele);
		}
		
		for(int j = 0; j < allDatesOfSprint.size(); j++) {
			String allDate = allDatesOfSprint.get(j);
			if(!presentDatesOfSprint.contains(allDate)) {
				int count2 = 0;
				System.out.println("************ON Date Pending "+allDate);
//				long onDateMili2 = getMiliSeconds(allDate);
				UserStoryPerSprintSeriesElement ele = new UserStoryPerSprintSeriesElement();
				ele.setY(count2);
//				ele.setX(onDateMili2);
				series.set(j, ele);
			}
		}
		
		return series;
	}
	
	public static UserStoriesPerSprint 
	getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(IterationsChartService iterationsChartService, 
			int appId, int sprintId) {
		UserStoriesPerSprint retVal = new UserStoriesPerSprint();
		try {
			
			List<UserStoriesPerSprintSeries> series = new ArrayList<UserStoriesPerSprintSeries>();
			
			UserStoriesPerSprintSeries commitedSeries = new UserStoriesPerSprintSeries();
			UserStoriesPerSprintSeries completedSeries = new UserStoriesPerSprintSeries();
			UserStoriesPerSprintSeries inProgressSeries = new UserStoriesPerSprintSeries();
			
			series.add(commitedSeries);
			series.add(completedSeries);
			series.add(inProgressSeries);
			
			commitedSeries.setName("Defined");
			completedSeries.setName("Completed");
			inProgressSeries.setName("In-Progress");
			
			int rapidviewId = iterationsChartService.getRapidViewId(appId, sprintId);
			
			HashMap<String, Object> data = iterationsChartService.getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(appId, sprintId, rapidviewId);
			String sprintName = (String)data.get("SprintName");
			String applicationName = (String)data.get("ApplicationName");
			
			String minUTCDateStr = (String)data.get("minUTCDate");
			String maxUTCDateStr = (String)data.get("maxUTCDate");
			
			List<Map<String, Object>> commitedRecords = (List<Map<String, Object>>)data.get("DefinedSeries");
			List<Map<String, Object>> inProgressRecords = (List<Map<String, Object>>)data.get("InProgressSeries");
			List<Map<String, Object>> closedRecords = (List<Map<String, Object>>)data.get("CompletedSeries");
			
			List<String> allDatesOfSprint = Utils.getDates(minUTCDateStr, maxUTCDateStr);
			retVal.categories = allDatesOfSprint;
			
			List<UserStoryPerSprintSeriesElement> chartCompatibleCommitedRecords = getChartCompatibleSeries(commitedRecords, allDatesOfSprint);
			commitedSeries.setData(chartCompatibleCommitedRecords);
			
			List<UserStoryPerSprintSeriesElement> chartCompatibleInProgressRecords = getChartCompatibleSeries(inProgressRecords, allDatesOfSprint);
			inProgressSeries.setData(chartCompatibleInProgressRecords);
			
			List<UserStoryPerSprintSeriesElement> chartCompatibleClosedRecords = getChartCompatibleSeries(closedRecords, allDatesOfSprint);
			completedSeries.setData(chartCompatibleClosedRecords);
			
			retVal.setSeries(series);
			retVal.setSprintName(sprintName);
			retVal.setApplicationName(applicationName);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return retVal;
	}

}
