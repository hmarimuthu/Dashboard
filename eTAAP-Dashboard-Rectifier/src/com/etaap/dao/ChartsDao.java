package com.etaap.dao;

import java.util.List;
import java.util.Map;

public interface ChartsDao {
	public List<Map<String, Object>> fetchChartData(String viewName, Object[] values);
}
