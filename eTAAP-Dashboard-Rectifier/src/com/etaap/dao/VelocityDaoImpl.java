package com.etaap.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.etaap.beans.Tcm;
import com.etaap.beans.Velocity;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;
import com.etaap.utils.gsonUtils.Gson;

public class VelocityDaoImpl implements VelocityDao{

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(VelocityDaoImpl.class);

	private static Map<String,String> tcTypeMap = new HashMap<String,String>();

	static {
	/*	tcTypeMap.put("Automated", "Automated");
		tcTypeMap.put("Manual", "Manual");*/
	}

	public String getVelocityChartString(int app_id, String from_dt, String to_dt) {
		logger.info("Inside VelocityDaoImpl :: getVelocityChartString()");
		String gson = "";
		StringBuffer queryBuffer = new StringBuffer("SELECT s.app_id, s.rapidview_id, s.sprint_id, s.sprint_name,  s.start_date, s.end_date, v.estimated, v.completed");
		queryBuffer.append(" FROM jira_sprint s, jira_velocity v");
		queryBuffer.append(" WHERE s.sprint_id = v.sprint_id");
		queryBuffer.append(" and s.rapidview_id = v.rapidview_id ");
		queryBuffer.append(" and s.app_id = v.app_id ");
		queryBuffer.append(" AND s.app_id = '"+app_id+"'");
		/*queryBuffer.append(" AND s.start_date >= '"+from_dt+" 00:00:00'");
		queryBuffer.append(" AND s.end_date <= '"+to_dt+" 23:59:59'");
		queryBuffer.append(" ORDER BY s.sprint_id");*/
		queryBuffer.append(" AND s.start_date between '"+from_dt+" 00:00:00' and '"+to_dt+" 23:59:59'");
		queryBuffer.append(" ORDER BY s.sprint_name desc");
		
		try {
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Velocity> VelocityList = new ArrayList<Velocity>();
			
			List<Map<String, Object>> VelocityRows = jdbcTemplate.queryForList(queryBuffer.toString());
			for (Map<String, Object> velocityRow : VelocityRows) {
				Velocity v = new Velocity();
				
				v.setAppId(Integer.parseInt(String.valueOf(velocityRow.get("app_id"))));
				v.setSprintId(Integer.parseInt(String.valueOf(velocityRow.get("sprint_id"))));
				v.setRapidViewId(Integer.parseInt(String.valueOf(velocityRow.get("rapidview_id"))));
				v.setCompleted(Integer.parseInt(String.valueOf(velocityRow.get("completed"))));
				v.setEstimated(Integer.parseInt(String.valueOf(velocityRow.get("estimated"))));
				v.setSprintName(String.valueOf(velocityRow.get("sprint_name")));
				
				VelocityList.add(v);
				
				
				gson = Gson.getGsonString(VelocityList);
				
			}
			
		}catch(Exception e){
			logger.error("ERROR :: getVelocityChartString() :: " + e.getMessage());
			return null;
		}

	
		return gson;
	}

	public List<Velocity> getVelocityChartDetails(int app_id, String from_dt, String to_dt) {
		logger.info("Inside VelocityDaoImpl :: getVelocityChartString()");
		List<Velocity> VelocityList = new ArrayList<Velocity>();
		
		StringBuffer queryBuffer = new StringBuffer("SELECT distinct s.app_id, s.rapidview_id, s.sprint_id, s.sprint_name,  s.start_date, s.end_date, v.estimated, v.completed");
		queryBuffer.append(" FROM jira_sprint s, jira_velocity v");
		queryBuffer.append(" WHERE s.sprint_id = v.sprint_id");
		queryBuffer.append(" and s.rapidview_id = v.rapidview_id ");
		queryBuffer.append(" and s.app_id = v.app_id ");
		queryBuffer.append(" AND s.app_id = '"+app_id+"'");
		queryBuffer.append(" AND s.start_date between '"+from_dt+" 00:00:00' and '"+to_dt+" 23:59:59'");
		//queryBuffer.append(" AND s.start_date >= '"+from_dt+" 00:00:00'");
		//queryBuffer.append(" AND s.end_date <= '"+to_dt+" 23:59:59'");
		//queryBuffer.append(" ORDER BY v.estimated, v.completed, s.sprint_id"); 
		queryBuffer.append(" ORDER BY s.sprint_name desc");
		
		try {
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
			System.out.println(" query : "+queryBuffer.toString());
			List<Map<String, Object>> VelocityRows = jdbcTemplate.queryForList(queryBuffer.toString());
			
			for (Map<String, Object> velocityRow : VelocityRows) {
				Velocity v = new Velocity();
				
				v.setAppId(Integer.parseInt(String.valueOf(velocityRow.get("app_id"))));
				v.setSprintId(Integer.parseInt(String.valueOf(velocityRow.get("sprint_id"))));
				v.setRapidViewId(Integer.parseInt(String.valueOf(velocityRow.get("rapidview_id"))));
				v.setCompleted(Integer.parseInt(String.valueOf(velocityRow.get("completed"))));
				v.setEstimated(Integer.parseInt(String.valueOf(velocityRow.get("estimated"))));
				v.setSprintName(String.valueOf(velocityRow.get("sprint_name")));
				
				VelocityList.add(v);
				
			}
			
		}catch(Exception e){
			logger.error("ERROR :: getVelocityChartString() :: " + e.getMessage());
			return null;
		}

	
		return VelocityList;
	}
	
	
	
	
	public List<Application> getVelocityApplicationList() {
		logger.info("Inside TcmDaoImpl :: getVelocityApplicationList()");

		List<Application> appList = new ArrayList<Application>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select a.* from application a, jira_velocity v");
		queryBuffer.append(" where a.app_id = v.app_id");
		queryBuffer.append(" group by a.app_id");
		logger.debug("queryBuffer "+queryBuffer.toString());
		List<Map<String,Object>> appRows = jdbcTemplate.queryForList(queryBuffer.toString());
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setAppName(String.valueOf(appRow.get("app_name")));
            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
            app.setMonthId(Integer.parseInt(String.valueOf(appRow.get("quarter_starting_month_id"))));
            app.setMonthName(String.valueOf(appRow.get("quarter_starting_month_name")));
            appList.add(app);
        }

        return appList;
	}

	public List<Application> getVelocityApplicationList(int app_id) {
		logger.info("Inside TcmDaoImpl :: getVelocityApplicationList(int)");

		List<Application> appList = new ArrayList<Application>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		StringBuffer queryBuffer = new StringBuffer("select a.* from application a, jira_velocity v");
		queryBuffer.append(" where a.app_id = v.app_id");
		queryBuffer.append(" group by a.app_id");
		queryBuffer.append(" order by (a.app_id = "+app_id+") DESC");
		List<Map<String,Object>> appRows = jdbcTemplate.queryForList(queryBuffer.toString());
		
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setAppName(String.valueOf(appRow.get("app_name")));
            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
            app.setMonthId(Integer.parseInt(String.valueOf(appRow.get("quarter_starting_month_id"))));
            app.setMonthName(String.valueOf(appRow.get("quarter_starting_month_name")));
            appList.add(app);
        }

        return appList;
	}
}
