package com.etaap.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.commons.lang3.StringEscapeUtils;

import com.etaap.services.DashboardService;
//import org.apache.commons.lang3.StringEscapeUtils;

public class CommitedCompletedUserStories {
	
	private List<Series> series = new ArrayList<Series>();
	
	private List<CommitedCompletedUserStoryCategory> categories = new ArrayList<CommitedCompletedUserStoryCategory>();
	
	public List<Series> getSeries() {
		return series;
	}
	public void setSeries(List<Series> series) {
		this.series = series;
	}
	public List<CommitedCompletedUserStoryCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<CommitedCompletedUserStoryCategory> categories) {
		this.categories = categories;
	}
	
	public static CommitedCompletedUserStories getCommitedCompletedUserStories(DashboardService dashboardService) {
		CommitedCompletedUserStories retVal = new CommitedCompletedUserStories();
		try {
		List<CommitedCompletedUserStoryCategory> categories = new ArrayList<CommitedCompletedUserStoryCategory>();
		
		List<Series> series = new ArrayList<Series>();
		Series commitedSeries = new Series();
		Series completedSeries = new Series();
//		series.add(commitedSeries);
//		series.add(completedSeries);
		commitedSeries.setName("Commited");
		commitedSeries.setColor("#FF8080");
		completedSeries.setName("Completed");
		completedSeries.setColor("#008000");
//		completedSeries.setPointWidth("10");
//		commitedSeries.setPointWidth("10");
		
		List<Integer> commitedData = new ArrayList<Integer>();
		List<Integer> completedData = new ArrayList<Integer>();
		commitedSeries.setData(commitedData);
		completedSeries.setData(completedData);
		
		LinkedHashMap allAppsMap = (LinkedHashMap)dashboardService.getCommitedCompletedUserStories(null);
		Set<String> keys = allAppsMap.keySet();
		Iterator<String> keysIt = keys.iterator();
		while(keysIt.hasNext()) {
			CommitedCompletedUserStoryCategory category = new CommitedCompletedUserStoryCategory();
			List<String> categoryData = new ArrayList<String>();
			
			String appName =  (String)keysIt.next();
			category.setName(appName);
			
			Map<String, Map> sprintMap = (Map<String, Map>)allAppsMap.get(appName);
			Set<String> sprintKeys = sprintMap.keySet();
			Iterator<String> sprintKeysIt = sprintKeys.iterator();
			while(sprintKeysIt.hasNext()) {
				String sprintName = (String)sprintKeysIt.next();
				Map<String, String> pointMap = sprintMap.get(sprintName);
				String startDate = (String)pointMap.get("StartDate");
				String endDate = (String)pointMap.get("EndDate");
//				startDate = startDate.replaceAll(" ", "&nbsp");
//				endDate = endDate.replaceAll(" ", "&nbsp");
//				String nameSection = sprintName+"<br>"+startDate+" to "+endDate;
				String nameSection = sprintName.replaceAll("[()]", "");//+"<br>"+startDate+" to "+endDate;
//				nameSection = StringEscapeUtils.escapeHtml4(nameSection);
				System.out.println("BNBNBNBNBN "+nameSection);
				categoryData.add(nameSection);
				
				String commited = (String)pointMap.get("Commited");
				String completed = (String)pointMap.get("Completed");
				commitedData.add(new Integer(commited));
				completedData.add(new Integer(completed));
			}
			category.setCategories(categoryData);
			categories.add(category);
		}

		if(commitedData.size() > 0)
		series.add(commitedSeries);
		if(completedData.size() > 0)
		series.add(completedSeries);
		
		retVal.setCategories(categories);
		retVal.setSeries(series);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
}
