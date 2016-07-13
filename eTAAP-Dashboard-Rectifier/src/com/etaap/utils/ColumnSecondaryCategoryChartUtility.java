package com.etaap.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import com.etaap.beans.CommitedCompletedUserStories;
import com.etaap.beans.CommitedCompletedUserStoryCategory;
import com.etaap.beans.Series;
import com.etaap.utils.gsonUtils.Gson;
import com.google.gson.JsonObject;

public class ColumnSecondaryCategoryChartUtility {
	
	@SuppressWarnings("unchecked")
	public static JSONObject generateChartJSON(List<String> seriesList, List<Map<String, Object>> firstAggrList, 
			List<Map<String, Object>> secondAggrList, JSONObject chartInfo) throws Exception {
		
		String jsonString;
		boolean isSecondQuery = false;
		if(firstAggrList == null || firstAggrList.size() == 0) {
			isSecondQuery = true;
			jsonString = Gson.getGsonString(createAggregateJson(secondAggrList, seriesList));
		} else {
			jsonString = Gson.getGsonString(createAggregateJson(firstAggrList, seriesList));
		}
		 
		Gson.getGsonObject(jsonString, JsonObject.class);
		JSONObject chartJson = (JSONObject) Gson.getGsonObject(jsonString, JSONObject.class);
		chartJson.put("title", chartInfo.get("title"));
		chartJson.put("subTitle", chartInfo.get("subTitle"));
		chartJson.put("seriesName", chartInfo.get("seriesName"));
		if(isSecondQuery) {
			chartJson.put("isSecondQueryResult", "true");
		}
		
		
		return chartJson;
	}
	
	@SuppressWarnings("unchecked")
	private static CommitedCompletedUserStories createAggregateJson(List<Map<String,Object>> dbObject, List<String> seriesList) {
		
		LinkedHashMap<String, Object> dataMap = getProcessedDataset(dbObject, seriesList);
		CommitedCompletedUserStories chartStatistics = new CommitedCompletedUserStories();
		try {
			List<CommitedCompletedUserStoryCategory> categories = new ArrayList<CommitedCompletedUserStoryCategory>();
			List<Series> series = new ArrayList<Series>();
			
			Map<String, Series> seriesMap = new HashMap<String, Series>();
			for(String name:seriesList) {
				Series seriesObj = new Series();
				seriesObj.setName(name);
				seriesObj.setData(new ArrayList<Integer>());
				seriesMap.put(name, seriesObj);
			}
			if(dataMap != null && dataMap.size() > 0) {
				Set<String> keys = dataMap.keySet();
				Iterator<String> keysIt = keys.iterator();
				while(keysIt.hasNext()) {
					CommitedCompletedUserStoryCategory category = new CommitedCompletedUserStoryCategory();
					List<String> categoryData = new ArrayList<String>();
					
					String appName =  (String)keysIt.next();
					category.setName(appName);
					
					Map<String, Map<String, String>> sprintMap = ((Map<String, Map<String, String>>)dataMap.get(appName));
					Set<String> sprintKeys = sprintMap.keySet();
					Iterator<String> sprintKeysIt = sprintKeys.iterator();
					while(sprintKeysIt.hasNext()) {
						String sprintName = (String)sprintKeysIt.next();
						Map<String, String> pointMap = sprintMap.get(sprintName);
						String nameSection = sprintName.replaceAll("[()]", "");
						categoryData.add(nameSection);
						for(String name:seriesList) {
							seriesMap.get(name).getData().add(new Integer((String)pointMap.get(name)));
						}
					}
					category.setCategories(categoryData);
					categories.add(category);
				}
				
				Set<String> seriesKeys = seriesMap.keySet();
				Iterator<String> seriesKeysIt = seriesKeys.iterator();
				while(seriesKeysIt.hasNext()) {
					String seriesKeyName = (String)seriesKeysIt.next();
					series.add(seriesMap.get(seriesKeyName));
				}
				
				chartStatistics.setCategories(categories);
				chartStatistics.setSeries(series);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return chartStatistics;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static LinkedHashMap<String, Object> getProcessedDataset(List<Map<String,Object>> dbObject, List<String> seriesList) {
		
		LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
		if(dbObject != null && dbObject.size() > 0) {
			for(Map data: dbObject) {
				String appName = (String)data.get("primary_category");
				String sprintName = (String)data.get("secondary_category");
				
				if(dataMap.containsKey(appName)) {
					Map<String, Map> sprintMap = ((Map<String, Map>)dataMap.get(appName));
					Map<String, String> pointMap = new HashMap<String, String>();
					
					for(String name:seriesList) {
						pointMap.put(name, String.valueOf(data.get(name)));
					}
					sprintMap.put(sprintName, pointMap);
				} else {
					Map<String, Map> sprintMap = new LinkedHashMap<String, Map>();
					Map<String, String> pointMap = new HashMap<String, String>();
					
					for(String name:seriesList) {
						pointMap.put(name, String.valueOf(data.get(name)));
					}
					sprintMap.put(sprintName, pointMap);
					dataMap.put(appName, sprintMap);
				}
			}
		}
		
		return dataMap;
	}
}
