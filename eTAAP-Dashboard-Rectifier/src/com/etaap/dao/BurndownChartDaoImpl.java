package com.etaap.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.etaap.domain.StoryPoint;
import com.etaap.services.StoryPointService;
import com.etaap.utils.Utils;


public class BurndownChartDaoImpl implements BurndownChartDao {
	
	@Autowired
	DataSource dataSource;
	
	private static final Logger logger = Logger.getLogger(BurndownChartDaoImpl.class);
	

	@Override
	public List<Map<String, Object>> getApplicationAndSprintDetails(int appId, int sprintId) {
		// TODO Auto-generated method stub
		String sql = "select app.app_name, js.start_datetime_mili, js.end_datetime_mili, js.sprint_name "+
				" from jira_sprint js, application app where sprint_id = "+sprintId+" and js.app_id = app.app_id "+
				" and app.app_id = "+appId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
		return dataFromDB;
	}

/*	@Override
	public List<Map<String, Object>> getTotalStoryPoints(int sprintId) {
		// TODO Auto-generated method stub
		String sql = "select userstory_id, increment, status_code, activity_mili, isDeleted from jira_storypoint where userstory_id IN(select id "+
		" from jira_userstory where sprint_id = "+sprintId+") order by userstory_id, activity_mili";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> retVal = jdbcTemplate.queryForList(sql);
		return retVal;
	}
*/
	@Override
	public int getNumberOfStoryPointsBeforeSprintStart(int appId, int sprintId, long startMiliSecs) {
		// TODO Auto-generated method stub
		
		String sql = "select sum(increment) from jira_storypoint js, jira_userstory ju "+ 
		" where activity_mili < "+startMiliSecs +
		" and js.userstory_id = ju.id "+
		" and ju.sprint_id = "+sprintId +
		" and ju.app_id = "+appId+
		" and js.userstory_id IN (select js1.userstory_id from jira_storypoint js1 "+ 
		" where isAdded = 1 and activity_mili < "+startMiliSecs+")";
		
//		long startDateTime =  getSprintStartDatetimeInMili(sprintId);
/*		String sql = "select sum(increment) from jira_storypoint js, jira_userstory ju " + 
				" where activity_mili < "+startMiliSecs+ 
				" and js.userstory_id = ju.id "+
				" and ju.sprint_id = "+sprintId; 
*/		System.out.println(" KKKKKKKKKK XXXXXXXX "+sql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int retVal = jdbcTemplate.queryForInt(sql);
		
		//Get sum of decrement if issue is closed before sprint start.
		String sqlDecre = "select sum(js.decrement) from jira_storypoint js, jira_userstory ju  where activity_mili < "+startMiliSecs+
				" and js.userstory_id = ju.id and ju.sprint_id = "+sprintId+
				" and ju.app_id = "+appId+
				" and js.userstory_id IN (select js1.userstory_id from "+
				" jira_storypoint js1 where isAdded = 1 and activity_mili < "+startMiliSecs+") and status_code = 6"; 
		int decrement = jdbcTemplate.queryForInt(sqlDecre);
		retVal = retVal - decrement;
		return retVal;
	}

/*	@Override
	public long getSprintStartDatetimeInMili(int sprintId) {
		long retVal = 0;
		try {
			// TODO Auto-generated method stub
			String sql = "select start_date from jira_sprint where sprint_id = "+sprintId;
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
			if(dataFromDB.size() == 0)
				throw new RuntimeException("Sprint with id "+sprintId+" not found in jira_sprint.");
			Map dataMap = (Map)dataFromDB.get(0);
			String dateStr = (String)dataMap.get("start_date");
			java.util.Date startDate = Utils.getDate(dateStr, "yyyy-MM-dd hh:mm:ss");
			retVal = startDate.getTime();
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getSprintStartDatetimeInMili() :: " + e.getMessage());
		}
		return retVal;
	}
*/
	@Override
	public List<Map<String, Object>> getBurndownDetails(int appId, int sprintId, long sprintStartDateTime) {
		List<Map<String, Object>> dataFromDB = new ArrayList<Map<String, Object>>();
		try {
			// TODO Auto-generated method stub
			String sql = "select js.userstory_id, ju.key, js.increment, js.decrement, js.activity_mili,js.status_message, js.status_code, js.isAdded, js.isDeleted "+  
			" from jira_userstory ju, jira_storypoint js where ju.sprint_id = "+sprintId+ 
			" and ju.id = js.userstory_id and js.activity_mili >= "+sprintStartDateTime+
			" and ju.app_id = "+appId+
			" order by js.activity_mili";
			System.out.println("NNNNNN Burndown details "+sql);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			dataFromDB = jdbcTemplate.queryForList(sql);
		}
		catch(Exception e) {
			logger.error("ERROR :: BurndownChartDaoImpl::getBurndownDetails() :: " + e.getMessage());
		}
		return dataFromDB;
	}


	@Override
	public List<Map<String, Object>> getSprintDetailsWithURL() {
		List<Map<String, Object>> dataFromDB = new ArrayList<Map<String, Object>>();
		try {
			String appIdSql = "select distinct sa.url, js.sprint_id, js.rapidview_id, sa.user_id, sa.password, "
					+ " js.sprint_name, js.app_id from system_api sa, "
					+ " application_system_map asm, jira_sprint js where js.app_id = asm.app_id "
					+"  and asm.type = 'Dev'"
					+ " and asm.sys_id = sa.sys_id group by sprint_id, rapidview_id,url, user_id, password, sprint_name, app_id;";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			dataFromDB = jdbcTemplate.queryForList(appIdSql);
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getSprintDetailsWithURL() :: " + e.getMessage());
		}
		return dataFromDB;
	}

	@Override
	public List<Map<String, Object>> getUserStoryId(int appId, int sprintId, int rapidviewId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> dataFromDB = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select jira_userstory.id, jira_userstory.key from jira_userstory "
					+ " where rapidview_id = "+rapidviewId+" and sprint_id = "+sprintId
					+" and app_id = "+appId;
			System.out.println("MNMNMNMNMNM 55 "+sql);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			dataFromDB = jdbcTemplate.queryForList(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getSprintDetailsWithURL() :: " + e.getMessage());
		}
		return dataFromDB;
	}
	
	@Override
	public void deleteOldStoryPoints(int appId, int sprintId, int rapidviewId) {
		String deleteSql = "delete from jira_storypoint where userstory_id IN "
				+ "(select id from jira_userstory where sprint_id = "+sprintId +" and "+
				" rapidview_id = "+rapidviewId+" and app_id = "+appId+")";
		System.out.println("********VVVV&&&&&&&***********Delete jira_storypoint  "+deleteSql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int deletedCount = jdbcTemplate.update(deleteSql);
		System.out.println("********VVVV&&&&&&&***********Deleted "+deletedCount);
	}
	
	public void addStoryPointsToDatabase(int appId, int sprintId, int rapidviewId, final List<StoryPoint> storyPointList) {
		deleteOldStoryPoints(appId, sprintId, rapidviewId);
		
		String insertSql = "insert into `jira_storypoint` (`userstory_id`,`increment`,`decrement`,`status_code`,`isAdded`,`isDeleted`,`status_message`,`activity_mili`) VALUES "+
		"(?,?,?,?,?,?,?,?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				StoryPoint storyPoint = storyPointList.get(i);
				ps.setInt(1, storyPoint.getUserstoryId());
				ps.setInt(2, storyPoint.getIncrement());
				ps.setInt(3, storyPoint.getDecrement());
				ps.setInt(4, storyPoint.getStatusCode());
				ps.setInt(5, storyPoint.getIsAdded());
				ps.setInt(6, storyPoint.getIsDelelted());
				ps.setString(7, storyPoint.getStatusMessage());
				ps.setLong(8, storyPoint.getActivityMiliSecs());
			}
					
			@Override
			public int getBatchSize() {
				return storyPointList.size();
			}
		});
		
		//Update Userstories for status and timestamp.
	  	
	}

	@Override
	public void saveSprintStartEndDateTime(int appId, int sprintId, int rapidviewId, long startDateTime, long endDateTime) {
		String updateSql = "update jira_sprint set start_datetime_mili = "+ startDateTime+
				", end_datetime_mili = "+endDateTime +" where sprint_id = "+sprintId +" and "+
				" rapidview_id = "+rapidviewId+" and app_id = "+appId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(updateSql);
	}
	
	public long getMaxStoryPointActivityDatetimeMili(int appId, int sprintId) {
		long retVal = 0;
		try {
			// TODO Auto-generated method stub
			String sql = "select  max(js.activity_mili) from jira_userstory ju, jira_storypoint js "+ 
						 " where ju.id = js.userstory_id and ju.sprint_id = "+sprintId+
						 " and ju.app_id = "+appId;
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			retVal = jdbcTemplate.queryForObject(sql, Long.class);
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getMaxStoryPointActivityDatetimeMili() :: " + e.getMessage());
		}
		return retVal;
		
	}
	
	@Override
	public int isAddedToSprint(long userstoryId) {
		int retVal = 0;
		try {
			// TODO Auto-generated method stub
			String sql = "select isAdded from jira_storypoint where isAdded = "+StoryPointService.ADDED_TO_SPRINT+
					" and userstory_id = "+userstoryId;
			System.out.println("KKKKKKKKKKKKKKKKKKK OOOOOOOOOOOOO "+sql);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			retVal = jdbcTemplate.queryForObject(sql, Integer.class);
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::isAddedToSprint() :: " + e.getMessage());
		}
		return retVal;
	}
	
	@Override
	public int isAddedInFuture(long userstoryId, long storyPointActivityTime) {
		int retVal = 0;
		try {
						
			String sql = "select status_code, isAdded, isDeleted, activity_mili from jira_storypoint where userstory_id = "+userstoryId+" and "+
					" activity_mili > "+storyPointActivityTime +" order by activity_mili";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
			checkloop: for(Map<String, Object> map : dataFromDB) {
//				int increment = (Integer)map.get("increment");
				int statusCode = (Integer)map.get("status_code");
				int isAdded = (Integer)map.get("isAdded");
				int isDeleted = (Integer)map.get("isDeleted");
//				int decrement = (Integer)map.get("decrement");
				if(isDeleted == StoryPointService.REMOVED_FROM_SPRINT) {
					retVal = 0;
					break checkloop;
				}
				else if(isAdded == StoryPointService.ADDED_TO_SPRINT) {
					retVal = StoryPointService.ADDED_TO_SPRINT;
					break checkloop;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::isAddedToSprint() :: " + e.getMessage());
		}
		return retVal;
	}	

	@Override
	public int isAddedToSprint(long userstoryId, long storyPointActivityTime) {
		int retVal = 0;
		try {
						
			String sql = "select status_code, isAdded, isDeleted, activity_mili from jira_storypoint where userstory_id = "+userstoryId+" and "+
					"activity_mili < "+storyPointActivityTime +" order by activity_mili desc";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
			checkloop: for(Map<String, Object> map : dataFromDB) {
//				int increment = (Integer)map.get("increment");
				int statusCode = (Integer)map.get("status_code");
				int isAdded = (Integer)map.get("isAdded");
				int isDeleted = (Integer)map.get("isDeleted");
//				int decrement = (Integer)map.get("decrement");
				if(isDeleted == StoryPointService.REMOVED_FROM_SPRINT) {
					retVal = 0;
					break checkloop;
				}
				else if(isAdded == StoryPointService.ADDED_TO_SPRINT) {
					retVal = StoryPointService.ADDED_TO_SPRINT;
					break checkloop;
				}
			}
			
			// TODO Auto-generated method stub
/*			String sql = "select isAdded from jira_storypoint where isAdded = "+StoryPointService.ADDED_TO_SPRINT+
					" and userstory_id = "+userstoryId;
			System.out.println("KKKKKKKKKKKKKKKKKKK OOOOOOOOOOOOO "+sql);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			retVal = jdbcTemplate.queryForObject(sql, Integer.class);
*/		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::isAddedToSprint() :: " + e.getMessage());
		}
		return retVal;
	}
	
	public int getIncrementForReAddedStoryPoint(long userstoryId, long storyPointActivityTime) {
		int sumIncrement = 0;
		try {
			// TODO Auto-generated method stub
			String sql = "select increment, status_code, activity_mili, isDeleted, decrement from jira_storypoint where userstory_id = "+userstoryId+
					" and activity_mili < "+storyPointActivityTime+
					" order by activity_mili desc";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
			middle: for(Map<String, Object> map : dataFromDB) {
				int increment = (Integer)map.get("increment");
				int statusCode = (Integer)map.get("status_code");
				int isDeleted = (Integer)map.get("isDeleted");
				int decrement = (Integer)map.get("decrement");
				if(isDeleted == StoryPointService.REMOVED_FROM_SPRINT) {
					sumIncrement = sumIncrement + decrement;
					break middle;
				}
				else {
					if(statusCode == StoryPointService.STATUS_CODE_NO_STATUS) {
						sumIncrement = sumIncrement + increment;
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getIncrementBeforeSprintStart() :: " + e.getMessage());
		}
		return sumIncrement;		
	}
	
	public int getDecrementForReDeletedStoryPoint(long userstoryId, long storyPointActivityTime) {
		int sumIncrement = 0;
		try {
			// TODO Auto-generated method stub
			String sql = "select increment, status_code, activity_mili, isAdded, isDeleted, decrement from jira_storypoint where userstory_id = "+userstoryId+
					" and activity_mili < "+storyPointActivityTime+
					" order by activity_mili desc";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> map : dataFromDB) {
				int increment = (Integer)map.get("increment");
				int statusCode = (Integer)map.get("status_code");
				int isDeleted = (Integer)map.get("isDeleted");
				int isAdded = (Integer)map.get("isAdded");
				int decrement = (Integer)map.get("decrement");
				if(isAdded == StoryPointService.ADDED_TO_SPRINT) {
					sumIncrement = sumIncrement + increment;
				}
				else {
					if(statusCode == StoryPointService.STATUS_CODE_NO_STATUS) {
						sumIncrement = sumIncrement + increment;
					}
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getIncrementBeforeSprintStart() :: " + e.getMessage());
		}
		return sumIncrement;		
	}
	
	
	public boolean isUserstoryReadded(long userstoryId, long storyPointActivityTime) {
		boolean retVal = false;
		try {
			// TODO Auto-generated method stub
			String sql = "select count(*) from jira_storypoint where userstory_id = "+userstoryId+
					" and activity_mili < "+storyPointActivityTime+" and isDeleted = 1";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			int count = jdbcTemplate.queryForObject(sql, Integer.class);
			if(count > 0) {
				retVal = true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::isAddedFirstTime() :: " + e.getMessage());
		}
		return retVal;
	}
	
	public boolean isUserstoryReDeleted(long userstoryId, long storyPointActivityTime) {
		boolean retVal = false;
		try {
			// TODO Auto-generated method stub
			String sql = "select count(*) from jira_storypoint where userstory_id = "+userstoryId+
					" and activity_mili < "+storyPointActivityTime+" and isDeleted = 1";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			int count = jdbcTemplate.queryForObject(sql, Integer.class);
			if(count > 0) {
				retVal = true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::isAddedFirstTime() :: " + e.getMessage());
		}
		return retVal;
	}
	
	
	
	
	public boolean isAddedFirstTime(long userstoryId, long storyPointActivityTime) {
		boolean retVal = false;
		try {
			// TODO Auto-generated method stub
			String sql = "select count(*) from jira_storypoint where userstory_id = "+userstoryId+
					" and activity_mili <= "+storyPointActivityTime+" and isDeleted = 1";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			int count = jdbcTemplate.queryForObject(sql, Integer.class);
			if(count == 0) {
				retVal = true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::isAddedFirstTime() :: " + e.getMessage());
		}
		return retVal;
	}
	
	@Override
	public int getIncrementBeforeSprintStart(long userstoryId, long storyPointActivityTime, long sprintStartTime) {
		// TODO Auto-generated method stub
		int sum = 0;
		try {
			// TODO Auto-generated method stub
			String sql = "select increment, status_code, activity_mili from jira_storypoint where userstory_id = "+userstoryId+
					" and activity_mili < "+storyPointActivityTime+" order by activity_mili desc";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
			for(Map<String, Object> map : dataFromDB) {
				int increment = (Integer)map.get("increment");
				int statusCode = (Integer)map.get("status_code");
				if(statusCode == StoryPointService.STATUS_CODE_ISSUE_REOPENED) {
					break;
				}
				long activityMili = (Long)map.get("activity_mili");
				if(activityMili < sprintStartTime) {
					if(statusCode == StoryPointService.STATUS_CODE_NO_STATUS) {
						if(increment > 0) {
							sum = sum + increment;
						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getIncrementBeforeSprintStart() :: " + e.getMessage());
		}
		return sum;
	}
	
}
