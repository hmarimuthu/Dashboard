package com.etaap.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.etaap.domain.Defects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

public class DefectsDaoImpl implements DefectsDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(DefectsDaoImpl.class);

	public List<Defects> getDetails(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		logger.info("Inside DefectDaoImpl :: getDetails()");

		// "defectCase", "severity"

		StringBuffer queryStringBuffer = null;
		List<Map<String, Object>> queryForList = null;
		List<Defects> defectsList = null;

		try {
			queryStringBuffer = new StringBuffer("select app_id,created_dt,project_name,severity,env_id,status");
			if (params.get("defectCase") != null && params.get("defectCase").equals("severity"))
				queryStringBuffer.append(",count(severity) as counted_severity from jira");
			else if (params.get("defectCase") != null && params.get("defectCase").equals("status"))
				queryStringBuffer.append(",count(status) as counted_status from jira");

			if (params != null && params.size() > 0) {
				queryStringBuffer.append(" where");
				if (params.get("envId") != null) {
					queryStringBuffer.append(" env_id= " + params.get("envId").toString());
				}
				if (params.get("appId") != null) {
					if (params.get("envId") != null)
						queryStringBuffer.append(" and");

					queryStringBuffer.append(" app_id=" + params.get("appId").toString());
				}
				if (params.get("periodStrtDt") != null && params.get("periodEndDt") != null) {
					// periodStrtDt
					queryStringBuffer.append(" and created between '" + params.get("periodStrtDt").toString() + " 00:00:00' and '" + params.get("periodEndDt").toString() + " 23:59:59'");
				}
			}
			if (params.get("defectCase") != null && params.get("defectCase").equals("severity"))
				queryStringBuffer.append(" and severity != '' group by severity");
			else if (params.get("defectCase") != null && params.get("defectCase").equals("status"))
				queryStringBuffer.append(" group by status");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());

			// START -> if severity is blank then priority
			boolean priorityFlag = false;
			if (queryForList.size() == 0) {
				queryStringBuffer = new StringBuffer("select app_id, created_dt, project_name, priority, env_id, status, count(priority) as counted_priority from jira");

				if (params != null && params.size() > 0) {
					queryStringBuffer.append(" where");
					if (params.get("envId") != null) {
						queryStringBuffer.append(" env_id= " + params.get("envId").toString());
					}
					if (params.get("appId") != null) {
						if (params.get("envId") != null)
							queryStringBuffer.append(" and");

						queryStringBuffer.append(" app_id=" + params.get("appId").toString());
					}
					if (params.get("periodStrtDt") != null && params.get("periodEndDt") != null) {
						// periodStrtDt
						queryStringBuffer.append(" and created between '" + params.get("periodStrtDt").toString() + " 00:00:00' and '" + params.get("periodEndDt").toString() + " 23:59:59'");
					}
				}
				queryStringBuffer.append(" group by priority");

				jdbcTemplate = new JdbcTemplate(dataSource);
				queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());

				if (queryForList.size() > 0)
					priorityFlag = true;
			}
			// END -> if severity is blank then priority

			defectsList = new ArrayList<Defects>();

			for (Map<String, Object> sysRow : queryForList) {
				// defects.set
				Defects defects = new Defects();
				if (params.get("defectCase") != null && params.get("defectCase").equals("severity")) {
					if (priorityFlag) {
						defects.setPriorityCount(Integer.parseInt(sysRow.get("counted_priority").toString()));
						defects.setPriority(sysRow.get("priority").toString());
					} else {
						if (!sysRow.get("severity").toString().trim().equalsIgnoreCase("")) {
							defects.setSeverityCount(Integer.parseInt(sysRow.get("counted_severity").toString()));
							defects.setSeverity(sysRow.get("severity").toString());
						}
					}
				} else if (params.get("defectCase") != null && params.get("defectCase").equals("status")) {
					defects.setStatus(sysRow.get("status").toString());
					defects.setStatusCount(Integer.parseInt(sysRow.get("counted_status").toString()));
				}

				defects.setApp_id(Integer.parseInt(sysRow.get("app_id").toString()));
				defectsList.add(defects);
			}
		} catch (Exception e) {
			logger.error("ERROR :: isRecordAvail() :: " + e.getMessage());
		}
		return defectsList;
	}

	 
	public void insertData(final List<Defects> defectList) {
		System.out.println("Inside DefectDaoImpl :: insertData()");

		String query = "insert into jira (app_id, env_id, created_dt, updated_dt, `key`, project_name, severity, priority, status, issue_type, created, updated) values (?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		int[] listSize = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Defects defect = defectList.get(i);
				ps.setObject(1, defect.getAppId());
				ps.setObject(2, defect.getEnvId());
				ps.setObject(3, defect.getKey());
				ps.setObject(4, defect.getProjectName());
				ps.setObject(5, defect.getSeverity());
				ps.setObject(6, defect.getPriority());
				ps.setObject(7, defect.getStatus());
				ps.setObject(8, defect.getIssueType());
				ps.setObject(9, defect.getCreated());
				ps.setObject(10, defect.getUpdated());
			}

			public int getBatchSize() {
				return defectList.size();
			}
		});

		logger.info("Inside DefectDaoImpl :: insertData() :: listSize :: " + listSize);

		if (listSize.length > 0) {
			query = "delete from jira where active = 0";
			int out = jdbcTemplate.update(query);

			logger.info("Inside DefectDaoImpl :: insertData() :: " + query);
		}
	}
	  
	public void deActivateData(int appId, String startDt, String endDt) {
		System.out.println("Inside DefectDaoImpl :: deActivateData()");

		String query = "update jira set active = 0 where app_id = ? and created >= ? and created < ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] { appId, startDt, endDt };

		int out = jdbcTemplate.update(query, args);
	}

	@Override
	public boolean isRecordAvail(Map params) {
		// TODO Auto-generated method stub
		logger.info("Inside DefectDaoImpl :: isRecordAvail()");

		boolean isAvail = false;
		try {
			StringBuffer queryStringBuffer = new StringBuffer("select count(*) from jira j");
			if (params != null && params.size() > 0) {
				queryStringBuffer.append(" where");
				if (params.get("app_id") != null)
					queryStringBuffer.append(" j.app_id = " + params.get("app_id"));

				if (params.get("env_id") != null)
					queryStringBuffer.append(" and j.env_id = " + params.get("env_id"));
			}
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			int count = jdbcTemplate.queryForInt(queryStringBuffer.toString());
			if (count > 0)
				isAvail = true;
		} catch (Exception e) {
			logger.error("ERROR :: isRecordAvail() :: " + e.getMessage());
			isAvail = false;
		}

		return isAvail;
	}
}
