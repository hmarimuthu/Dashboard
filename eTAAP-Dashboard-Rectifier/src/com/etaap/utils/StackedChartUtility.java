package com.etaap.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.etaap.beans.DefectsStatistics;
import com.etaap.beans.Series;
import com.etaap.services.DashboardService;
import com.etaap.utils.gsonUtils.Gson;
import com.google.gson.JsonObject;

public class StackedChartUtility {

	@Autowired
	@Qualifier("dashboardService")
	private DashboardService dashboardService;

	@SuppressWarnings("unchecked")
	public static JSONObject generateChartJSON(List<Map<String, Object>> firstAggrList, 
			List<Map<String, Object>> secondAggrList, JSONObject chartInfo) throws Exception {
		
		String jsonString;
		boolean isSecondQuery = false;
		if(firstAggrList == null || firstAggrList.size() == 0) {
			isSecondQuery = true;
			jsonString = Gson.getGsonString(createAggregateJson(getCategoryList(secondAggrList), secondAggrList));
		} else {
			jsonString = Gson.getGsonString(createAggregateJson(getCategoryList(firstAggrList), firstAggrList));
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
	
	private static List<String> getCategoryList(List<Map<String,Object>> dbObject) {
		List<String> categoryList = new ArrayList<String>();
		if(dbObject != null && dbObject.size() > 0) {
			for(Map<String,Object> map : dbObject) {
				String category = map.get("categories")!=null ? map.get("categories").toString() : null;
				if(!categoryList.contains(category)) {
					categoryList.add(category);
				}
			}
		}
		
		return categoryList;
	}

	private static DefectsStatistics createAggregateJson(List<String> chartCategories, List<Map<String,Object>> dbObject) {
		
		DefectsStatistics chartStatistics = new DefectsStatistics();
		if(dbObject != null && dbObject.size() > 0) {
			chartStatistics.setCategories(chartCategories);
			Map<String,Integer[]> chartStatsMap = new HashMap<String,Integer[]>();
			
			for(Map<String,Object> map : dbObject) {
				int statisticsCount = Integer.parseInt(map.get("data").toString());
				String statisticsName = map.get("name")!=null ? map.get("name").toString() : null;
				String category = map.get("categories")!=null ? map.get("categories").toString() : null;
				if(statisticsName != null) {
					if((chartStatsMap.size() == 0) || (chartStatsMap.get(statisticsName) == null)) {
						Integer[] categoryArr = new Integer[chartCategories.size()]; 
						int ii = 0;
						for(String str : chartCategories) {
							if(str.contains(category)){
								categoryArr[ii] = statisticsCount;
							}
							ii++;
						}
						chartStatsMap.put(statisticsName, categoryArr);
					} else if(chartStatsMap.get(statisticsName) != null) {
						Integer[] categoryArr = chartStatsMap.get(statisticsName);
						int ii = 0;
						for(String str : chartCategories) {
							if(str.contains(category)) {
								categoryArr[ii] = statisticsCount;
							}
							ii++;
						}
						chartStatsMap.put(statisticsName, categoryArr);
					}
				}
			}
			
			Set<String> set = chartStatsMap.keySet();
			Iterator<String> itr = set.iterator();
			while(itr.hasNext()) {
				String statisticsNameVal = itr.next();
				Integer[] categoryArr = chartStatsMap.get(statisticsNameVal);
				categoryArr = Utils.getRefinedDataArr(categoryArr);
				Series series = new Series();
				series.setName(statisticsNameVal);
				series.setData((Arrays.asList(categoryArr)));
				chartStatistics.getSeries().add(series);
			}
		}
		
		return chartStatistics;
	}
}
