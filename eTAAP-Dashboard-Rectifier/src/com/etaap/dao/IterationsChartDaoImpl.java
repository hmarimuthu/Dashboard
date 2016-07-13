package com.etaap.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.etaap.services.StoryPointService;
import com.etaap.beans.Application;
import com.etaap.beans.Sprint;
import com.etaap.utils.Utils;

public class IterationsChartDaoImpl implements IterationsChartDao {
	
	@Autowired
	DataSource dataSource;
	private static final Logger logger = Logger.getLogger(IterationsChartDaoImpl.class);
	

	public int getUserStoriesCount(Map params) {
		int count = 0;
		try {
			StringBuffer queryStringBuffer = new StringBuffer("select count(*) from jira_userstory us, jira_sprint s ");
			if (params != null && params.size() > 0) {
				queryStringBuffer.append(" where");
				queryStringBuffer.append(" us.app_id = s.app_id ");
				queryStringBuffer.append(" and us.sprint_id = s.sprint_id ");
			//	queryStringBuffer.append(" and s.status = 'ACTIVE' ");
				
				System.out.println("getUserStoriesCount ::  app_id :: "+params.get("app_id"));
				System.out.println("getUserStoriesCount ::  sprint_id :: "+params.get("sprint_id"));
				if (params.get("app_id") != null){
					queryStringBuffer.append(" and us.app_id = " + params.get("app_id") + " ");
				}
				if (params.get("sprint_id") != null){
					queryStringBuffer.append(" and us.sprint_id = " + params.get("sprint_id") + " ");
				}
				if (params.get("status") != null){
					queryStringBuffer.append(" and us.status in ('Closed')");
				}
			}
			System.out.println(":: queryStringBuffer.toString() :: "+queryStringBuffer.toString());
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			count = jdbcTemplate.queryForInt(queryStringBuffer.toString());
		} catch (Exception e) {
			logger.error("ERROR :: getUserStoriesCount() :: " + e.getMessage());
		}
		return count;
	}
	
	public List<Sprint> getSprintList(int appId) {
		List<Sprint> sprintList = new ArrayList<Sprint>();
		
//		String sql = "select distinct sprint_id, sprint_name from jira_sprint where app_id="+appId+" order by sprint_name desc";
		
		String sql = "select distinct js.sprint_id, js.sprint_name from jira_sprint js, jira_userstory ju "+
		" where js.app_id = "+appId+ 
		" and js.sprint_id = ju.sprint_id "+
		" and js.app_id = ju.app_id "+
		" and js.rapidview_id = ju.rapidview_id "+
		" order by js.sprint_name desc";
		
		System.out.println("sql : "+sql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> sprintFromDB = jdbcTemplate.queryForList(sql);
		
		for(int i = 0; i < sprintFromDB.size(); i++) {
			Map sprintMap = (Map)sprintFromDB.get(i);
			int sprintId = (Integer)sprintMap.get("sprint_id");
			String sprintName = (String)sprintMap.get("sprint_name");
			
			Sprint sprint = new Sprint();
			sprint.setSprintId(sprintId);
			sprint.setSprintName(sprintName);
			sprintList.add(sprint);
		}
		return sprintList;
	}
	
	public List<Application> getApplicationList() {
		List<Application> appList = new ArrayList<Application>();
		
		String sql = "select a.app_id, a.app_name from application a, jira_sprint j where "+
				" a.app_id = j.app_id group by app_id";
		
		logger.info("getApplicationList :: sql :: "+sql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> applications = jdbcTemplate.queryForList(sql);
		
		for(int i = 0; i < applications.size(); i++) {
			Map appMap = (Map)applications.get(i);
			int appId = (Integer)appMap.get("app_id");
			String appName = (String)appMap.get("app_name");
			
			Application application = new Application();
			application.setAppId(appId);
			application.setAppName(appName);
			appList.add(application);
		}
		return appList;
	}
	
	
	public List<Application> getApplicationList(int app_id) {
		logger.info("Inside IterationsChartDaoImpl :: getApplicationList(int)");
		List<Application> appList = new ArrayList<Application>();
		StringBuffer queryBuffer = new StringBuffer("select a.app_id, a.app_name from application a, jira_sprint j where");
		queryBuffer.append(" a.app_id = j.app_id ");
		queryBuffer.append(" group by app_id ");
		if(app_id > 0){
		queryBuffer.append(" order by (a.app_id = "+app_id+") DESC");
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String,Object>> applications = jdbcTemplate.queryForList(queryBuffer.toString());
		for(int i = 0; i < applications.size(); i++) {
			Map appMap = (Map)applications.get(i);
			int appId = (Integer)appMap.get("app_id");
			String appName = (String)appMap.get("app_name");
			Application application = new Application();
			application.setAppId(appId);
			application.setAppName(appName);
			appList.add(application);
		}
        return appList;
	}
	
	private String getDatewiseCommitedCompletedInProgressUserStoriesPerSprintQuery(int appId, int sprintId, int rapidviewId, ArrayList statusList) {
		String commaDelimitedStatus = StringUtils.collectionToCommaDelimitedString(statusList);
		String sql = "select count(distinct jus.userstory_id) as count, DATE_FORMAT(jus.status_datetime,'%Y-%m-%d') as onDate, "+ 
		" CASE jus.status_code "+  
		" WHEN 10000 THEN 'New' "+  
		" WHEN 3 THEN 'In Progress' "+ 
		" WHEN 1 THEN 'In Progress' "+ 
		" WHEN 6 THEN 'Closed' "+ 
		" WHEN 10003 THEN 'Closed' "+ 
		" END AS status from jira_userstory_status jus, jira_userstory ju, jira_sprint js "+
		" where jus.status_code IN ("+commaDelimitedStatus+") "+
		" and jus.userstory_id = ju.id "+ 
		" and ju.sprint_id = js.sprint_id "+ 
		" and ju.app_id = js.app_id "+
		" and ju.rapidview_id = js.rapidview_id "+
		" and ju.sprint_id = "+sprintId+
		" and ju.rapidview_id = "+rapidviewId+
		" and ju.app_id = "+appId+
		" and DATE_FORMAT(jus.status_datetime,'%Y-%m-%d') >= DATE_FORMAT(js.start_date,'%Y-%m-%d') "+
		" and DATE_FORMAT(jus.status_datetime,'%Y-%m-%d') <= DATE_FORMAT(js.end_date,'%Y-%m-%d') "+
		" group by onDate "+ 
		" order by onDate ";		
		
		return sql;
	}	

	
	@Override
	public Application getApplication(int appId) {
		// TODO Auto-generated method stub
		String appNameSql = "select app_name from application "+ 
				" where app_id = "+appId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> sprintNameAppNameRecords = jdbcTemplate.queryForList(appNameSql);
		if(sprintNameAppNameRecords.size() == 0)
			throw new RuntimeException("Application with id "+appId+" does not exists.");
		HashMap<String, Object> appMap = (HashMap)sprintNameAppNameRecords.get(0);
		Application retApp = new Application();
		retApp.setAppId(appId);
		String appName = (String)appMap.get("app_name");
		retApp.setAppName(appName);
		return retApp;
	}
	
	public Sprint getSprint(int sprintId) {
		String sprintQuerySql = "select sprint_id, sprint_name from jira_sprint "+ 
				" where sprint_id = "+sprintId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> recs = jdbcTemplate.queryForList(sprintQuerySql);
		if(recs.size() == 0)
			throw new RuntimeException("Sprint with id "+sprintId+" does not exists.");
		HashMap<String, Object> sprintMap = (HashMap)recs.get(0);
		Sprint sprint = new Sprint();
		sprint.setSprintId(sprintId);
		sprint.setSprintName((String)sprintMap.get("sprint_name"));
		return sprint;
	}
	
	
	/**@return HashMap key      				value
	 *                 SprintName				SprintName
	 *                 ApplicationName 			ApplicationName
	 *                 minUTCDate				yyyy-mm-dd
	 *                 maxUTCDate				yyyy-mm-dd
	 *                 DefinedSeries			List 	HashMap1      key          value
	 *                                                    		     count		   Integer		   
	 *																 onDate		   yyyy-mm-dd			   
	 *                                                     .
	 *                                                     .
	 *													HashMapN      key          value
	 *                                                    		     count		   Integer		   
	 *																 onDate		   yyyy-mm-dd			   
	 *                                                      	
	 *                 InProgressSeries			List 	HashMap1      key          value
	 *                                                    		     count		   Integer		   
	 *																 onDate		   yyyy-mm-dd			   
	 *                                                     .
	 *                                                     .
	 *													HashMapN      key          value
	 *                                                    		     count		   Integer		   
	 *																 onDate		   yyyy-mm-dd			   
	 *
	 *                 CompletedSeries			List 	HashMap1      key          value
	 *                                                    		     count		   Integer		   
	 *																 onDate		   yyyy-mm-dd			   
	 *                                                     .
	 *                                                     .
	 *													HashMapN      key          value
	 *                                                    		     count		   Integer		   
	 *																 onDate		   yyyy-mm-dd			   
	 *
	 * @throws ParseException 
	 *                                          	
	 */
	public HashMap<String, Object> getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(int appId, int sprintId, int rapidviewId) {
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		try {
			String sprintNameAppNameSql = "select js.sprint_name, DATE_FORMAT(js.start_date,'%Y-%m-%d') as start_date, "
					+ "DATE_FORMAT(js.end_date,'%Y-%m-%d') as end_date, app.app_name from jira_sprint js, application app "+ 
					" where js.sprint_id = "+sprintId +
					" and js.app_id = "+appId +
					" and js.rapidview_id = "+rapidviewId+
					" and js.app_id = app.app_id";
//			System.out.println("BBBBBBBBBB "+sprintNameAppNameSql);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> sprintNameAppNameRecords = jdbcTemplate.queryForList(sprintNameAppNameSql);
			if(sprintNameAppNameRecords.size() == 0)
				throw new RuntimeException("Sprint and Application with id "+sprintId+","+appId+","+rapidviewId+" respectively does not exists.");
			Map<String, Object> sprintAppMap = (HashMap)sprintNameAppNameRecords.get(0);
			
			String sprintName = (String)sprintAppMap.get("sprint_name");
			retMap.put("SprintName", sprintName);
			
			String appName = (String)sprintAppMap.get("app_name");
			retMap.put("ApplicationName", appName);
			
			String startDate = (String)sprintAppMap.get("start_date");
			retMap.put("minUTCDate", startDate);
			
			String endDate = (String)sprintAppMap.get("end_date");
			retMap.put("maxUTCDate", endDate);
			
			ArrayList<Integer> definedStatus = new ArrayList<Integer>();
			definedStatus.add(StoryPointService.STATUS_CODE_ISSUE_CREATED);
			
			ArrayList<Integer> inProgressStatus = new ArrayList<Integer>();
			inProgressStatus.add(StoryPointService.STATUS_CODE_ISSUE_REOPENED);
			inProgressStatus.add(StoryPointService.STATUS_CODE_ISSUE_FIXED);
			
			ArrayList<Integer> closedStatus = new ArrayList<Integer>();
			closedStatus.add(StoryPointService.STATUS_CODE_COMPLETED);
			closedStatus.add(StoryPointService.STATUS_CODE_ISSUE_VERIFY);
			
			String definedSql = this.getDatewiseCommitedCompletedInProgressUserStoriesPerSprintQuery(appId, sprintId, rapidviewId, definedStatus);
			String InProgressSql = this.getDatewiseCommitedCompletedInProgressUserStoriesPerSprintQuery(appId, sprintId, rapidviewId, inProgressStatus);
			String closedSql = this.getDatewiseCommitedCompletedInProgressUserStoriesPerSprintQuery(appId, sprintId, rapidviewId, closedStatus);
			
			System.out.println("definedSql :: "+definedSql);
			System.out.println("InProgressSql :: "+InProgressSql);
			System.out.println("closedSql :: "+closedSql);
			
			List<Map<String, Object>> definedRecords = jdbcTemplate.queryForList(definedSql);
			List<Map<String, Object>> InProgressRecords = jdbcTemplate.queryForList(InProgressSql);
			List<Map<String, Object>> closedRecords = jdbcTemplate.queryForList(closedSql);
			
			retMap.put("DefinedSeries", definedRecords);
			retMap.put("InProgressSeries", InProgressRecords);
			retMap.put("CompletedSeries", closedRecords);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); 			
		}
		return retMap;
	}

	public int getRapidViewId(int appId, int sprintId) {
		String sprintQuerySql = "select rapidview_id from jira_sprint "+ 
				" where sprint_id = "+sprintId+
				" and app_id = "+appId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> recs = jdbcTemplate.queryForList(sprintQuerySql);
		if(recs.size() == 0)
			throw new RuntimeException("Rapidview with Application="+appId+" and Sprint="+sprintId+" does not exists.");
		HashMap<String, Object> sprintMap = (HashMap)recs.get(0);
		int retVal = (Integer)sprintMap.get("rapidview_id");
		return retVal;
	}	

}
