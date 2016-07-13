package com.etaap.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.etaap.domain.Environment;

public class EnvironmentDaoImpl implements EnvironmentDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(EnvironmentDaoImpl.class);

	public void insertData(Environment env) {
		logger.info("Inside EnvironmentDaoImpl :: insertData()");

		String query = "insert into environment (env_name, status, created_dt, updated_dt) values (?, ?, current_timestamp, current_timestamp)";  

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {env.getEnvName().trim(), env.getStatus()};

		int out = jdbcTemplate.update(query, args);
	}

	public Environment getEnvironment(int envId) {
		logger.info("Inside EnvironmentDaoImpl :: getEnvironment()");

		String query = "select * from environment where env_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Environment env = jdbcTemplate.queryForObject(query, new Object[]{envId}, new RowMapper<Environment>() {

        	public Environment mapRow(ResultSet rs, int rowNum) throws SQLException {
        		Environment env = new Environment();
            	env.setEnvId(rs.getInt(1));
                env.setEnvName(rs.getString(2));
                env.setCreatedDt(rs.getString(3));
                env.setUpdatedDt(rs.getString(4));
                env.setStatus(rs.getInt(5));

                return env;
            }});

        return env;
	}

	public void updateData(Environment env) {
		logger.info("Inside EnvironmentDaoImpl :: updateData()");

		String query = "update environment set env_name = ?, status = ?, updated_dt = current_timestamp where env_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[] {env.getEnvName().trim(), env.getStatus(), env.getEnvId()};

        int out = jdbcTemplate.update(query, args);
	}

	public void deleteData(int envId) {
		logger.info("Inside EnvironmentDaoImpl :: deleteData()");

		String query = "update environment set status = 0, updated_dt = current_timestamp where env_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, envId);
	}

	public List<Environment> getAllEnvironmentList(String orderBy, String orderType, int offset, int noOfRecords) {
		logger.info("Inside EnvironmentDaoImpl :: getAllEnvironmentList()");

		String query = "select * from environment";

		if (!orderBy.equalsIgnoreCase("") && orderBy != null && !orderType.equalsIgnoreCase("") && orderType != null)
			query = query + " order by " + orderBy + " " + orderType + " limit " + offset + ", " + noOfRecords;
		else
			query = query + " order by updated_dt desc limit " + offset + ", " + noOfRecords;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Environment> envList = new ArrayList<Environment>();
        System.out.println("query "+query);
        List<Map<String,Object>> envRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> envRow : envRows) {
        	Environment env = new Environment();
            env.setEnvId(Integer.parseInt(String.valueOf(envRow.get("env_id"))));
            env.setEnvName(String.valueOf(envRow.get("env_name")));
            env.setCreatedDt((String.valueOf(envRow.get("created_dt")).toString()).
            		substring(0, (String.valueOf(envRow.get("created_dt")).toString().indexOf('.'))));
            //env.setUpdatedDt(String.valueOf(envRow.get("updated_dt")));
            env.setUpdatedDt((String.valueOf(envRow.get("updated_dt")).toString()).
            		substring(0, (String.valueOf(envRow.get("updated_dt")).toString().indexOf('.'))));
            env.setStatus(Integer.parseInt(String.valueOf(envRow.get("status"))));
            envList.add(env);
        }

        return envList;
	}

	public int getTotalRowCount() {
		logger.info("Inside EnvironmentDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from environment";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}

	public List<Environment> getEnvironmentList() {
		logger.info("Inside EnvironmentDaoImpl :: getEnvironmentList()");

		String query = "select * from environment where status = 1";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Environment> envList = new ArrayList<Environment>();

        List<Map<String,Object>> envRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> envRow : envRows) {
        	Environment env = new Environment();
            env.setEnvId(Integer.parseInt(String.valueOf(envRow.get("env_id"))));
            env.setEnvName(String.valueOf(envRow.get("env_name")));
            env.setCreatedDt(String.valueOf(envRow.get("created_dt")));
            env.setUpdatedDt(String.valueOf(envRow.get("updated_dt")));
            env.setStatus(Integer.parseInt(String.valueOf(envRow.get("status"))));
            envList.add(env);
        }

        return envList;
	}
	
	public String isNameExistChkByAjaxCall(int id, String name) {
		logger.info("Inside EnvironmentDaoImpl :: isNameExistChkByAjaxCall()");

		String query = "";
		int count = 0;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if (id > 0)
			query = "select count(*) from environment where env_id != " + id + " and env_name like '" + name + "'";
		else
			query = "select count(*) from environment where env_name like '" + name + "'";

		count = jdbcTemplate.queryForInt(query);

		return (count > 0 ? "true" : "false");
	}

	@Override
	public void deleteData(String suiteIds) {
		// TODO Auto-generated method stub

		logger.info("Inside EnvironmentDaoImpl :: deleteData()");

		String query = "update environment set status = 0, updated_dt = current_timestamp where env_id in ("+suiteIds+")";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query/*, envId*/);
	
	}

	@Override
	public List<Environment> getEnvironmentList(Map<String, Object> paramsMap) {
		logger.info("Inside EnvironmentDaoImpl :: getEnvironmentList(map)");

		StringBuffer queryBuffer = new StringBuffer("select * from environment");
		if(paramsMap!=null && paramsMap.size()>0){
			queryBuffer.append(" where");
			if(paramsMap.get("status")!= null){
				queryBuffer.append(" status = 1");
			}
		}
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Environment> envList = new ArrayList<Environment>();

        List<Map<String,Object>> envRows = jdbcTemplate.queryForList(queryBuffer.toString());
        for (Map<String,Object> envRow : envRows) {
        	Environment env = new Environment();
            env.setEnvId(Integer.parseInt(String.valueOf(envRow.get("env_id"))));
            env.setEnvName(String.valueOf(envRow.get("env_name")));
            env.setCreatedDt(String.valueOf(envRow.get("created_dt")));
            env.setUpdatedDt(String.valueOf(envRow.get("updated_dt")));
            env.setStatus(Integer.parseInt(String.valueOf(envRow.get("status"))));
            envList.add(env);
        }

        return envList;
	}

	public List<Environment> getEnvironmentList(int appId) {
		logger.info("Inside EnvironmentDaoImpl :: getEnvironmentList(int)");

		StringBuffer query = new StringBuffer("select e.* from environment e, application_system_map asm, system_api sa");
		query.append(" where e.env_id = asm.env_id");
		query.append(" and asm.sys_id = sa.sys_id");
		query.append(" and lower(sa.api_name) = 'jira'");
		query.append(" and asm.app_id = " + appId);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Environment> envList = new ArrayList<Environment>();

        List<Map<String,Object>> envRows = jdbcTemplate.queryForList(query.toString());
        for (Map<String,Object> envRow : envRows) {
        	Environment env = new Environment();
            env.setEnvId(Integer.parseInt(String.valueOf(envRow.get("env_id"))));
            env.setEnvName(String.valueOf(envRow.get("env_name")));
            env.setStatus(Integer.parseInt(String.valueOf(envRow.get("status"))));
            envList.add(env);
        }

        return envList;
	}
}
