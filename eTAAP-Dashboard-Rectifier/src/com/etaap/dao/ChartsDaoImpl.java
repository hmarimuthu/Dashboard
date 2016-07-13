package com.etaap.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.etaap.utils.Utils;

@Repository("chartsDao")
public class ChartsDaoImpl implements ChartsDao {
	
	@Autowired
	DataSource dataSource;
	
	private static final Logger logger = Logger.getLogger(ChartsDaoImpl.class);
	
	@Override
	public List<Map<String, Object>> fetchChartData(String query, Object[] values) {
		List<Map<String, Object>> queryForList = null;
		System.out.println("fetchChartData Query: "+query );
		try {
			if(!Utils.isNull(query)) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);						
				queryForList = jdbcTemplate.queryForList(query, values);
			}
		} catch (Exception e) {
			logger.error("ERROR :: fetchChartData() :: " + e.getMessage());
		}
		
		return queryForList;
	}
}
