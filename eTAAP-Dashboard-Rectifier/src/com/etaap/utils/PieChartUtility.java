package com.etaap.utils;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PieChartUtility {
	
	@SuppressWarnings("unchecked")
	public static JSONObject generateChartJSON(List<Map<String, Object>> firstAggrList, List<Map<String, Object>> secondAggrList, 
			List<Map<String, Object>> drilldownList, JSONObject chartInfo) throws Exception {
		
		JSONObject chartJson = new JSONObject();
		JSONObject finalAggrObj;
		boolean isSecondQuery = false;
		if(firstAggrList == null || firstAggrList.size() == 0) {
			isSecondQuery = true;
			finalAggrObj = createAggregateJson(secondAggrList);
		} else {
			finalAggrObj = createAggregateJson(firstAggrList);
		}
		
		chartJson.put("total_Count", finalAggrObj.get("totalCount") != null?finalAggrObj.get("totalCount"):0);
		chartJson.put("chart_aggregate", finalAggrObj.get("aggregateObjs"));
		if(finalAggrObj != null && finalAggrObj.size() > 0) {
			if(drilldownList != null && drilldownList.size() > 0) {
				chartJson.put("chart_drillDown", createDrillDownJson(drilldownList).get("drilldownObjs"));
			}
		}
		
		chartJson.put("title", chartInfo.get("title"));
		chartJson.put("subTitle", chartInfo.get("subTitle"));
		chartJson.put("seriesName", chartInfo.get("seriesName"));
		
		if(isSecondQuery) {
			chartJson.put("isSecondQueryResult", "true");
		}
		
		return chartJson;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject createAggregateJson(List<Map<String,Object>> dbObject) {
		
		JSONObject finalAggregateObject = new JSONObject();
		if(dbObject != null && dbObject.size() > 0) {
			JSONArray aggregateObjs = new JSONArray();
			int colourCount = 1;
			long totalCount = 0;
			for(Map<String,Object> row: dbObject) {
				JSONObject aggregateObj = new JSONObject();
				for(String key: row.keySet()){
					aggregateObj.put(key, row.get(key));
				}
				totalCount = totalCount + ((Long)row.get("y"));
				aggregateObj.put("drilldown", ((String)row.get("name")).toLowerCase());
				aggregateObj.put("showInLegend", false);
				//aggregateObj.put("color", ApplicationColor.INDEX.getColorCode(String.valueOf(colourCount)));
				aggregateObjs.add(aggregateObj);
				colourCount++;
			}
			
			finalAggregateObject.put("aggregateObjs", aggregateObjs);
			finalAggregateObject.put("totalCount", totalCount);
		}
		
		
		return finalAggregateObject;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject createDrillDownJson(List<Map<String,Object>> dbObject) {
		
		JSONObject finalObject = new JSONObject();
		if(dbObject != null && dbObject.size() > 0) {
			JSONObject refererenceObj = new JSONObject();
			int colourCount = 0;
			for(Map<String,Object> row: dbObject) {
				JSONArray childs = null;			
				JSONObject childObj = new JSONObject();
				childObj.put("name", ((String)row.get("data")));
				childObj.put("y", (row.get("y")));
				//totalCount = totalCount + ((Long)row.get("y"));
				//childObj.put("color", ApplicationColor.INDEX.getColorCode(String.valueOf(colourCount)));
				childObj.put("showInLegend", false);
				colourCount = colourCount + 1;
				if(refererenceObj.containsKey(row.get("name"))) {
					JSONObject parentObject = (JSONObject) refererenceObj.get(row.get("name"));
					childs = (JSONArray) parentObject.get("data");				
					childs.add(childObj);
				} else {
					JSONObject parentObject = new JSONObject();
					parentObject.put("name",row.get("name"));
					parentObject.put("y",0);
					parentObject.put("id",((String)row.get("name")).toLowerCase());
					childs = new JSONArray();
					childs.add(childObj);
					parentObject.put("data",childs);
					parentObject.put("showInLegend", true);
					refererenceObj.put((String)row.get("name"), parentObject);
				}
			}
			
			
			JSONArray drillDownJSon = new JSONArray();
			for(Object key: refererenceObj.keySet()) {
				drillDownJSon.add(refererenceObj.get(key));
			}
			finalObject.put("drilldownObjs", drillDownJSon);
			//finalObject.put("totalCount", totalCount);
		}
		
		return finalObject;
	}
}
