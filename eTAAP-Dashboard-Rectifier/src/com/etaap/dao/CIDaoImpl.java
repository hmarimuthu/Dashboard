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

import com.etaap.domain.Application;
import com.etaap.domain.CI;

public class CIDaoImpl implements CIDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(CIDaoImpl.class);

	public void insertData(final List<CI> ciList) {
		logger.info("Inside CIDaoImpl :: insertData()");

		String query = "insert into jenkins values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";  

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		/*for (CI ci : ciList) {
			Object[] args = new Object[] {ci.getAppId(), ci.getEnvId(), ci.getSuiteId(), ci.getBedId(), ci.getBuildId(), ci.getBuildName(),
					ci.getBuildNumber(), ci.getBuildUrl(), ci.getBuildDate(), ci.getResult(), ci.getFailCount(), ci.getPassCount(), ci.getSkipCount(),
					ci.getTotalCount()};
	
			int out = jdbcTemplate.update(query, args);
		}*/

		int[] listSize = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
		        CI ci = ciList.get(i);
		        ps.setObject(1, ci.getAppId());
		        ps.setObject(2, ci.getEnvId());
		        ps.setObject(3, ci.getSuiteId());
		        ps.setObject(4, ci.getBedId());
		        ps.setObject(5, ci.getBuildId());
		        ps.setObject(6, ci.getBuildName());
		        ps.setObject(7, ci.getBuildNumber());
		        ps.setObject(8, ci.getBuildUrl());
		        ps.setObject(9, ci.getBuildDate());
		        ps.setObject(10, ci.getResult());
		        ps.setObject(11, ci.getFailCount());
		        ps.setObject(12, ci.getPassCount());
		        ps.setObject(13, ci.getSkipCount());
		        ps.setObject(14, ci.getTotalCount());
		    }

		    public int getBatchSize() {
		    	return ciList.size();
		    }
		});

		logger.info("Inside CIDaoImpl :: insertData() :: listSize :: " + listSize);
		System.out.println("listSize :: " + listSize);
	}

	public List<CI> getDetails(HashMap<String, String> params) {
		logger.info("Inside CIDaoImpl :: getDetails()");

		StringBuffer query = new StringBuffer("select j.* from jenkins j, application app, environment env, test_suite suite, test_bed bed");
		query.append(" where j.app_id = app.app_id");		
		query.append(" and j.env_id = env.env_id");
		query.append(" and j.suite_id = suite.suite_id");
		query.append(" and j.bed_id = bed.bed_id");

		if (params.get("appId") != null)
			query.append(" and app.app_id = " + params.get("appId"));
		else
			query.append(" and app.app_id = 1");

		if (params.get("envId") != null)
			query.append(" and env.env_id = " + params.get("envId"));
		else
			query.append(" and env.env_id = 1");

		if (params.get("suiteId") != null)
			query.append(" and suite.suite_id = " + params.get("suiteId"));
		else
			query.append(" and suite.suite_id = 1");

		if (params.get("bedId") != null)
			query.append(" and bed.bed_id = " + params.get("bedId"));
		else
			query.append(" and bed.bed_id = 1");

		/*if (params.get(("periodId")) != null) {
			query = query + " and tp.period_id = " + params.get(("periodId"));
			query = query + " and j.build_date between tp.start_dt and tp.end_dt";
		} else {
			query = query + " and tp.period_id = 1";
			query = query + " and j.build_date between tp.start_dt and tp.end_dt";
		}*/
		if (params.get("periodStrtDt") != null && params.get("periodEndDt") != null) {
			query.append(" and j.build_date between '" + params.get("periodStrtDt") + " 00:00:00' and '" + params.get("periodEndDt") + " 23:59:59'");
		}
		
		if (params.get("appId") != null && params.get("envId") != null && params.get("suiteId") != null && params.get("bedId") != null) {
			query.append(" and j.build_date in (select max(build_date) from jenkins where app_id = " + params.get("appId"));
			query.append(" and env_id = " + params.get("envId") + " and suite_id = " + params.get("suiteId") + " and bed_id = " + params.get("bedId")); 
			query.append(" group by date(build_date))");
		}

		query.append(" order by j.build_date desc");

		logger.info("Inside CIDaoImpl :: getDetails() :: query :: " + query);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<CI> ciList = new ArrayList<CI>();
 
        List<Map<String,Object>> ciRows = jdbcTemplate.queryForList(query.toString());
        for (Map<String,Object> ciRow : ciRows) {
        	CI ci = new CI();
        	ci.setAppId(Integer.parseInt(String.valueOf(ciRow.get("app_id"))));
        	ci.setEnvId(Integer.parseInt(String.valueOf(ciRow.get("env_id"))));
        	ci.setSuiteId(Integer.parseInt(String.valueOf(ciRow.get("suite_id"))));
        	ci.setBedId(Integer.parseInt(String.valueOf(ciRow.get("bed_id"))));
        	ci.setBuildId(String.valueOf(ciRow.get("build_id")));
        	ci.setBuildName(String.valueOf(ciRow.get("build_name")));
        	ci.setBuildNumber(Integer.parseInt(String.valueOf(ciRow.get("build_number"))));
        	ci.setBuildUrl(String.valueOf(ciRow.get("build_url")));
        	ci.setBuildDate(String.valueOf(ciRow.get("build_date")));
        	ci.setResult(String.valueOf(ciRow.get("result")));
        	ci.setFailCount(Integer.parseInt(String.valueOf(ciRow.get("fail_count"))));
        	ci.setPassCount(Integer.parseInt(String.valueOf(ciRow.get("pass_count"))));
        	ci.setSkipCount(Integer.parseInt(String.valueOf(ciRow.get("skip_count"))));
        	ci.setTotalCount(Integer.parseInt(String.valueOf(ciRow.get("total_count"))));
        	//ci.setCreatedDt(String.valueOf(ciRow.get("created_dt")));
        	//ci.setUpdatedDt(String.valueOf(ciRow.get("updated_dt")));
        	//ci.setStatus(Integer.parseInt(String.valueOf(ciRow.get("status"))));
        	ciList.add(ci);
        }

        return ciList;
	}
	
	/*public boolean isRecordExist(Application app) {
		logger.info("Inside CIDaoImpl :: isRecordExist()");

		String query = "select count(*) from jenkins where app_id = ? and env_id = ? and suite_id = ? and bed_id = ?";  
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {app.getAppId(), app.getEnvId(), app.getSuiteId(), app.getBedId()};

		Integer count = jdbcTemplate.queryForObject(query, args, Integer.class);

		return (count != null && count > 0);
	}*/

	public boolean isRecordExist(Application app, String buildNum) {
		logger.info("Inside CIDaoImpl :: isRecordExist()");

		String query = "select count(*) from jenkins where app_id = ? and env_id = ? and suite_id = ? and bed_id = ? and build_number = ?";  
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {app.getAppId(), app.getEnvId(), app.getSuiteId(), app.getBedId(), buildNum};

		Integer count = jdbcTemplate.queryForObject(query, args, Integer.class);

		return (count != null && count > 0);
	}

	@Override
	public boolean isRecordAvail(Map params) {
		logger.info("Inside CIDaoImpl :: isRecordAvail()");

		// TODO Auto-generated method stub
		boolean isAvail = false;
		try{
			StringBuffer queryStringBuffer = new StringBuffer("select count(*) from jenkins j");
			if(params!=null && params.size()>0){
				queryStringBuffer.append(" where");
				if(params.get("app_id")!=null)
				queryStringBuffer.append(" j.app_id = " + params.get("app_id"));
				
				if(params.get("env_id")!=null)
				queryStringBuffer.append(" and j.env_id = " + params.get("env_id"));
				
				if(params.get("suite_id")!=null)
				queryStringBuffer.append(" and j.suite_id = " + params.get("suite_id"));
				
				if(params.get("bed_id")!=null)
				queryStringBuffer.append(" and j.bed_id = " + params.get("bed_id"));
			}
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			int count = jdbcTemplate.queryForInt(queryStringBuffer.toString());
			if(count > 0)
				isAvail = true;

		} catch(Exception e) {
			e.printStackTrace();
			isAvail= false;
		}

		return isAvail;
	}
}
