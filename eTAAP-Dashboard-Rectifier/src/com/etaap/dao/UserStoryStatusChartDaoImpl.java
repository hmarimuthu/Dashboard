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

import com.etaap.beans.UserStoryStatus;
import com.etaap.domain.StoryPoint;

public class UserStoryStatusChartDaoImpl implements UserStoryStatusChartDao {
	
	@Autowired
	DataSource dataSource;
	
	private static final Logger logger = Logger.getLogger(UserStoryStatusChartDaoImpl.class);
	
	@Override
	public List<Map<String, Object>> getSprintDetailsWithURL() {
		List<Map<String, Object>> dataFromDB = new ArrayList<Map<String, Object>>();
		try {
			String appIdSql = "select distinct sa.url, js.sprint_id, js.rapidview_id, sa.user_id, sa.password, "
					+ " js.sprint_name, js.app_id from system_api sa, "
					+ " application_system_map asm, jira_sprint js where js.app_id = asm.app_id "
					+"  and asm.type = 'Dev'"
					+ " and asm.sys_id = sa.sys_id group by sprint_id, rapidview_id,url, user_id, password, sprint_name, app_id";
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			dataFromDB = jdbcTemplate.queryForList(appIdSql);
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR :: BurndownChartDaoImpl::getSprintDetailsWithURL() :: " + e.getMessage());
		}
		return dataFromDB;
	}

	public List<Map<String, Object>> getUserStoryDetails(int appId, int sprintId, int rapidviewId) {
		String sql = "select j.id, j.key from jira_userstory j "
				+ " where j.app_id = "+appId
				+ " and j.sprint_id = "+sprintId
				+ " and j.rapidview_id = "+rapidviewId;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> dataFromDB = jdbcTemplate.queryForList(sql);
		return dataFromDB;
	}
	
	public void deleteOldUserStoryStatus(int appId, int sprintId, int rapidviewId) {
/*		String deleteSql = "delete jus from jira_userstory_status jus, jira_userstory ju "+ 
		" where ju.sprint_id = "+sprintId+
		" and ju.rapidview_id = "+rapidviewId+
		" and ju.app_id = "+appId+
		" and jus.userstory_id = ju.id ";
*/		
		String deleteSql = "delete from jira_userstory_status where userstory_id IN "+
        "( select ju.id from jira_userstory ju  where ju.sprint_id = "+sprintId+
        " and ju.rapidview_id = "+rapidviewId+" and ju.app_id = "+appId+")";
		
		System.out.println("********&&&&&&&***********Delete statement "+deleteSql);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int deletedCount = jdbcTemplate.update(deleteSql);
		System.out.println("********&&&&&&&***********Deleted "+deletedCount);
	}
	
	
	public void addUserStoryStatusToDatabase(int sprintId, int rapidviewId, final List<UserStoryStatus> userstoryStatusList) {
		String insertSql = " INSERT INTO jira_userstory_status "+
		" (userstory_id, status_code, status_datetime) "+
		" VALUES (?,?,?)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserStoryStatus userStoryStatus = userstoryStatusList.get(i);
				ps.setLong(1, userStoryStatus.getUserstoryId());
				ps.setInt(2, userStoryStatus.getStatusCode());
				ps.setString(3, userStoryStatus.getDateTime());
			}
					
			@Override
			public int getBatchSize() {
				return userstoryStatusList.size();
			}
		});
	}
	
}
