package com.etaap.utils;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class WaterTankChartUtility {
	
	@SuppressWarnings("unchecked")
	public static JSONObject generateChartJSON(List<Map<String, Object>> totalCountList, List<Map<String, Object>> statusCountList,
			JSONObject chartInfo) throws Exception {

		JSONObject chartJson = new JSONObject();
		chartJson.put("title", chartInfo.get("title"));
		chartJson.put("subTitle", chartInfo.get("subTitle"));
		chartJson.put("seriesName", chartInfo.get("seriesName"));
		
		chartJson.put("total_Count", totalCountList.get(0).get("COUNT"));
		chartJson.put("status_Count", statusCountList.get(0).get("COUNT"));
		
		return chartJson;
	}
}
