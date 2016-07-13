package com.etaap.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import com.etaap.beans.DefectsStatistics;
import com.etaap.beans.Series;
import com.etaap.utils.gsonUtils.Gson;
import com.google.gson.JsonObject;

public class ColumnChartUtility {
	
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
	
	private static DefectsStatistics createAggregateJson(List<Map<String,Object>> dbObject, List<String> seriesList) {
		
		DefectsStatistics chartStatistics = new DefectsStatistics();
		if(dbObject != null && dbObject.size() > 0) {
			List<String> chartCategories = new ArrayList<>();
			List<Series> series = new ArrayList<Series>();
			Map<String, Series> seriesMap = new HashMap<String, Series>();
			for(String name:seriesList) {
				Series seriesObj = new Series();
				seriesObj.setName(name);
				seriesObj.setData(new ArrayList<Integer>());
				seriesMap.put(name, seriesObj);
			}
			
			for(Map<String,Object> map : dbObject) {
				chartCategories.add(map.get("categories") != null ? map.get("categories").toString() : "");
				for(String name:seriesList) {
					seriesMap.get(name).getData().add(new Integer(map.get(name).toString()));
				}
			}
			
			Set<String> seriesKeys = seriesMap.keySet();
			Iterator<String> seriesKeysIt = seriesKeys.iterator();
			while(seriesKeysIt.hasNext()) {
				String seriesKeyName = (String)seriesKeysIt.next();
				series.add(seriesMap.get(seriesKeyName));
			}
			chartStatistics.setCategories(chartCategories);
			chartStatistics.setSeries(series);
		}
		
		return chartStatistics;
	
	}
}
