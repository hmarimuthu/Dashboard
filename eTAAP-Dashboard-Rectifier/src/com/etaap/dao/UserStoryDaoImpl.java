package com.etaap.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.etaap.domain.UserStory;

public class UserStoryDaoImpl implements UserStoryDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(UserStoryDaoImpl.class);

	public List<UserStory> getDetails(HashMap<String, Object> params) {
		logger.info("Inside UserStoryDaoImpl :: getDetails()");

		StringBuffer queryStringBuffer = null;
		List<Map<String, Object>> queryForList = null;
		List<UserStory> userStoryList = null;

		return userStoryList;
	}
	
//		query = "insert into jira_userstory (app_id, created_dt, updated_dt, rapidview_id, sprint_id, `key`, summary, priority, status, jira_id) values (?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?)";
	
	private List<UserStory> updateExistingUserStoryRecords(List<UserStory> userStoryList) {
		System.out.println("XXXXX - Before Update Userstory List Size - "+userStoryList.size());
		
		final List<UserStory> updateList = new ArrayList<UserStory>();
		final List<UserStory> toRemoveList = new ArrayList<UserStory>();
		
		String selectSql = "select ju.app_id, ju.sprint_id, ju.rapidview_id, ju.key, ju.jira_id from jira_userstory ju;";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(selectSql);
		
		if((dataFromDB != null) && (dataFromDB.size() > 0)) {
			for(Map mapFromDB : dataFromDB) {
				int appIdFromDB = (Integer)mapFromDB.get("app_id");
				int rapidviewIdFromDB = (Integer)mapFromDB.get("rapidview_id");
				int sprintIdFromDB = (Integer)mapFromDB.get("sprint_id");
	//			String keyNameFromDB = (String)mapFromDB.get("key");
				int jiraIdFromDB = (Integer)mapFromDB.get("jira_id");
				
				for(UserStory us : userStoryList) {
					int appId = us.getAppId();
					int rapidviewId = us.getRapidViewId();
					int sprintId = us.getSprintId();
	//				String keyName = us.getKey();
					int jiraId = us.getJiraId();
				
					if((appIdFromDB == appId) && (rapidviewIdFromDB == rapidviewId) 
							&& (sprintIdFromDB == sprintId) && (jiraIdFromDB == jiraId)) {
						updateList.add(us);
						toRemoveList.add(us);
					}
				}
			}
		}
		
		userStoryList.removeAll(toRemoveList);
		System.out.println("XXXXX - AFter Remove existing records Userstory List Size - "+userStoryList.size());
		
//		query = "insert into jira_userstory (app_id, created_dt, updated_dt, rapidview_id, sprint_id, `key`, summary, priority, status, jira_id) values (?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?)";
		
		if(updateList.size() > 0) {
			String query = "update jira_userstory "+
					" set updated_dt = current_timestamp, "+
					" app_id = ?, "+
					" rapidview_id = ?, "+
					" sprint_id = ?,"+
					" `key` = ?, "+
					" summary = ?,"+
					" priority = ?, "+
					" status = ?, "+
					" jira_id = ? "+
					" where app_id = ? and "+
					" rapidview_id = ? and "+
					" sprint_id = ? and "+
					" jira_id = ? ";
			
			int[] updatedData = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					UserStory us = updateList.get(i);
					ps.setInt(1, us.getAppId());
					ps.setInt(2, us.getRapidViewId());
					ps.setInt(3, us.getSprintId());
					ps.setString(4, us.getKey());
					ps.setString(5, us.getSummary());
					ps.setString(6, us.getPriority());
					ps.setString(7, us.getStatus());
					ps.setInt(8, us.getJiraId());
					
					ps.setInt(9, us.getAppId());
					ps.setInt(10, us.getRapidViewId());
					ps.setInt(11, us.getSprintId());
					ps.setInt(12, us.getJiraId());
					
					System.out.println("XXXXX - Updated Userstory - app_id="+us.getAppId()+", rapidview_id="+us.getRapidViewId()+
							", sprint_id="+us.getSprintId()+", key="+us.getKey()+
							", status="+us.getStatus()+", Summary="+us.getSummary()+
							", Priority="+us.getPriority()+", Jira_id="+us.getJiraId());
					
				}

				public int getBatchSize() {
					return updateList.size();
				}
			});
		}
		
		return userStoryList;
	}	
	
	private List<UserStory> updateExistingJiraSprintRecords(List<UserStory> sprintList) {
		System.out.println("VVVVV - Before Update Sprint List Size - "+sprintList.size());
		
		final List<UserStory> updateList = new ArrayList<UserStory>();
		final List<UserStory> toRemoveList = new ArrayList<UserStory>();
		
		String selectSql = "select app_id, rapidview_id, sprint_id from jira_sprint";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(selectSql);
		
		for(Map mapFromDB : dataFromDB) {
			int appIdFromDB = (Integer)mapFromDB.get("app_id");
			int rapidviewIdFromDB = (Integer)mapFromDB.get("rapidview_id");
			int sprintIdFromDB = (Integer)mapFromDB.get("sprint_id");
			
			for(UserStory us : sprintList) {
				int appId = us.getAppId();
				int rapidviewId = us.getRapidViewId();
				int sprintId = us.getSprintId();
			
				if((appIdFromDB == appId) && (rapidviewIdFromDB == rapidviewId) 
						&& (sprintIdFromDB == sprintId)) {
					updateList.add(us);
					toRemoveList.add(us);
				}
			}
		}
		
		sprintList.removeAll(toRemoveList);
		System.out.println("VVVVV - AFter Remove existing records Sprint List Size - "+sprintList.size());
		
		if(updateList.size() > 0) {
			String query = "update jira_sprint "+
					" set updated_dt = current_timestamp, "+
					" sprint_name = ?, "+
					" status = ?, "+
					" start_date = ?, "+
					" end_date = ? "+
					" where app_id = ? and "+
					" rapidview_id = ? and "+
					" sprint_id = ?";
			
			int[] listSizeForSprint = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					UserStory us = updateList.get(i);
					ps.setObject(1, us.getSprintName());
					ps.setObject(2, us.getStatus());
					ps.setObject(3, us.getStartDt());
					ps.setObject(4, us.getEndDt());
					
					ps.setObject(5, us.getAppId());
					ps.setObject(6, us.getRapidViewId());
					ps.setObject(7, us.getSprintId());
					
					System.out.println("VVVVV - Updated Sprint - app_id="+us.getAppId()+", rapidview_id="+us.getRapidViewId()+
							", sprint_id="+us.getSprintId()+", sprint_name="+us.getSprintName()+
							", status="+us.getStatus()+", start_date="+us.getStartDt()+
							", end_date="+us.getEndDt());
				}

				public int getBatchSize() {
					return updateList.size();
				}
			});
		}
		
		return sprintList;
	}
	
	
	private List<UserStory> updateExistingJiraVelocityRecords(List<UserStory> velocityList) {
		System.out.println("VVVVV - Before Update Jira Velocity List Size - "+velocityList.size());
		
		final List<UserStory> updateList = new ArrayList<UserStory>();
		final List<UserStory> toRemoveList = new ArrayList<UserStory>();
		
		String selectSql = "select app_id, rapidview_id, sprint_id from jira_velocity";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(selectSql);
		
		for(Map mapFromDB : dataFromDB) {
			int appIdFromDB = (Integer)mapFromDB.get("app_id");
			int rapidviewIdFromDB = (Integer)mapFromDB.get("rapidview_id");
			int sprintIdFromDB = (Integer)mapFromDB.get("sprint_id");
			
			for(UserStory us : velocityList) {
				int appId = us.getAppId();
				int rapidviewId = us.getRapidViewId();
				int sprintId = us.getSprintId();
			
				if((appIdFromDB == appId) && (rapidviewIdFromDB == rapidviewId) 
						&& (sprintIdFromDB == sprintId)) {
					updateList.add(us);
					toRemoveList.add(us);
				}
			}
		}

		velocityList.removeAll(toRemoveList);
		System.out.println("VVVVV - After Remove Existing Jira Velocity List Size- "+velocityList.size());
		
		
		if(updateList.size() > 0) {
			String query = "update jira_velocity "+
					" set updated_dt = current_timestamp, "+
					" estimated = ?, "+
					" completed = ? "+
					" where app_id = ? and "+
					" rapidview_id = ? and "+
					" sprint_id = ?";
			
			int[] listSizeForSprint = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					UserStory us = updateList.get(i);
					ps.setObject(1, us.getEstimated());
					ps.setObject(2, us.getCompleted());
					
					ps.setObject(3, us.getAppId());
					ps.setObject(4, us.getRapidViewId());
					ps.setObject(5, us.getSprintId());
					
					System.out.println("VVVVV - Updated Jira Velocity - app_id="+us.getAppId()+", rapidview_id="+us.getRapidViewId()+
							", sprint_id="+us.getSprintId()+", sprint_name="+us.getEstimated()+
							", status="+us.getCompleted());
				}

				public int getBatchSize() {
					return updateList.size();
				}
			});
		}
		
		return velocityList;
	}
	

	public void insertData(final Map<String, List<UserStory>> map) {
		logger.info("Inside UserStoryDaoImpl :: insertData()");
		
		List<UserStory> sprintList1 = map.get("sprintDetail");
		sprintList1 = updateExistingJiraSprintRecords(sprintList1);
		final List<UserStory> sprintList = sprintList1;
		

		String query = "insert into jira_sprint (app_id, created_dt, updated_dt, rapidview_id, sprint_id, sprint_name, status, start_date, end_date) values (?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		int[] listSizeForSprint = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserStory us = sprintList.get(i);
				ps.setObject(1, us.getAppId());
				ps.setObject(2, us.getRapidViewId());
				ps.setObject(3, us.getSprintId());
				ps.setObject(4, us.getSprintName());
				ps.setObject(5, us.getStatus());
				ps.setObject(6, us.getStartDt());
				ps.setObject(7, us.getEndDt());
				
				System.out.println("VVVVV - Inserted - app_id="+us.getAppId()+", rapidview_id="+us.getRapidViewId()+
						", sprint_id="+us.getSprintId()+", sprint_name="+us.getSprintName()+
						", status="+us.getStatus()+", start_date="+us.getStartDt()+
						", end_date="+us.getEndDt());
				
			}

			public int getBatchSize() {
				return sprintList.size();
			}
		});
		
		
		List<UserStory> userStoryList1 = map.get("userStoryDetail");
		userStoryList1 = updateExistingUserStoryRecords(userStoryList1);
		final List<UserStory> userStoryList = userStoryList1;
		

//		logger.info("Inside UserStoryDaoImpl :: insertData() :: listSizeForSprint :: " + listSizeForSprint);

/*		if (listSizeForSprint.length > 0) {
			query = "delete from jira_sprint where active = 0";
			jdbcTemplate.update(query);

			logger.info("Inside UserStoryDaoImpl :: insertData() :: " + query);
		}
*/
		query = "insert into jira_userstory (app_id, created_dt, updated_dt, rapidview_id, sprint_id, `key`, summary, priority, status, jira_id) values (?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?)";

		int[] listSizeForUS = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserStory us = userStoryList.get(i);
				ps.setObject(1, us.getAppId());
				ps.setObject(2, us.getRapidViewId());
				ps.setObject(3, us.getSprintId());
				ps.setObject(4, us.getKey());
				ps.setObject(5, us.getSummary());
				ps.setObject(6, us.getPriority());
				ps.setObject(7, us.getStatus());
				ps.setObject(8, us.getJiraId());
			}

			public int getBatchSize() {
				return userStoryList.size();
			}
		});

/*		logger.info("Inside UserStoryDaoImpl :: insertData() :: listSizeForUS :: " + listSizeForUS);

		if (listSizeForUS.length > 0) {
			query = "delete from jira_userstory where active = 0";
			jdbcTemplate.update(query);

			logger.info("Inside UserStoryDaoImpl :: insertData() :: " + query);
		}
*/
		
		List<UserStory> velocityList1 = map.get("velocityDetail");
		final List<UserStory> velocityList = this.updateExistingJiraVelocityRecords(velocityList1);
		
		query = "insert into jira_velocity (app_id, created_dt, updated_dt, rapidview_id, sprint_id, estimated, completed) values (?, current_timestamp, current_timestamp, ?, ?, ?, ?)";

		int[] listSizeForVelocity = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserStory us = velocityList.get(i);
				ps.setObject(1, us.getAppId());
				ps.setObject(2, us.getRapidViewId());
				ps.setObject(3, us.getSprintId());
				ps.setObject(4, us.getEstimated());
				ps.setObject(5, us.getCompleted());

				System.out.println("VVVVV - Inserted Jira Velocity - app_id="+us.getAppId()+", rapidview_id="+us.getRapidViewId()+
						", sprint_id="+us.getSprintId()+", sprint_name="+us.getEstimated()+
						", status="+us.getCompleted());
			}

			public int getBatchSize() {
				return velocityList.size();
			}
		});

//		logger.info("Inside UserStoryDaoImpl :: insertData() :: listSizeForVelocity :: " + listSizeForVelocity);

/*		if (listSizeForVelocity.length > 0) {
			query = "delete from jira_velocity where active = 0";
			jdbcTemplate.update(query);

			logger.info("Inside UserStoryDaoImpl :: insertData() :: " + query);
		}
*/	
	}

	public void deActivateData(int appId, String rapidViewId, String type) {
		logger.info("Inside UserStoryDaoImpl :: deActivateData()");

		String tableName = "userstory";
/*		if (type.equalsIgnoreCase("sprint"))
			tableName = "jira_sprint";
*/		/*else*/ if (type.equalsIgnoreCase("userstory")) {
				tableName = "jira_userstory";
	/*		else if (type.equalsIgnoreCase("velocity"))
				tableName = "jira_velocity";
	*/
			String query = "update " + tableName + " set active = 0 where app_id = ? and rapidview_id = ?";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			Object[] args = new Object[] {appId, rapidViewId};
	
			jdbcTemplate.update(query, args);
		}
	}
}
