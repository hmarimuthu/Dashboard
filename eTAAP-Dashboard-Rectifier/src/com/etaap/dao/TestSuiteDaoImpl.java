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

import com.etaap.domain.TestSuite;

public class TestSuiteDaoImpl implements TestSuiteDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(TestSuiteDaoImpl.class);

	public void insertData(TestSuite suite) {
		logger.info("Inside TestSuiteDaoImpl :: insertData()");

		String query = "insert into test_suite (suite_name, status, created_dt, updated_dt) values (?, ?, current_timestamp, current_timestamp)";  

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {suite.getSuiteName().trim(), suite.getStatus()};

		int out = jdbcTemplate.update(query, args);
	}

	public TestSuite getTestSuite(int suiteId) {
		logger.info("Inside TestSuiteDaoImpl :: getTestSuite()");

		String query = "select * from test_suite where suite_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        TestSuite suite = jdbcTemplate.queryForObject(query, new Object[]{suiteId}, new RowMapper<TestSuite>() {

        	public TestSuite mapRow(ResultSet rs, int rowNum) throws SQLException {
            	TestSuite suite = new TestSuite();
            	suite.setSuiteId(rs.getInt(1));
            	suite.setSuiteName(rs.getString(2));
                suite.setCreatedDt(rs.getString(3));
                suite.setUpdatedDt(rs.getString(4));
                suite.setStatus(rs.getInt(5));

                return suite;
            }});

        return suite;
	}

	public void updateData(TestSuite suite) {
		logger.info("Inside TestSuiteDaoImpl :: updateData()");

		String query = "update test_suite set suite_name = ?, status = ?, updated_dt = current_timestamp where suite_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[] {suite.getSuiteName().trim(), suite.getStatus(), suite.getSuiteId()};

        int out = jdbcTemplate.update(query, args);
	}

	public void deleteData(int suiteId) {
		logger.info("Inside TestSuiteDaoImpl :: deleteData()");

		String query = "update test_suite set status=0, updated_dt=current_timestamp where suite_id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, suiteId);
	}

	public List<TestSuite> getAllTestSuiteList(String orderBy, String orderType, int offset, int noOfRecords) {
		logger.info("Inside TestSuiteDaoImpl :: getAllTestSuiteList()");

		String query = "select * from test_suite";

		if (!orderBy.equalsIgnoreCase("") && orderBy != null && !orderType.equalsIgnoreCase("") && orderType != null)
			query = query + " order by " + orderBy + " " + orderType + " limit " + offset + ", " + noOfRecords;
		else
			query = query + " order by updated_dt desc limit " + offset + ", " + noOfRecords;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestSuite> suiteList = new ArrayList<TestSuite>();
 
        List<Map<String,Object>> suiteRows = jdbcTemplate.queryForList(query);
        
        for (Map<String,Object> suiteRow : suiteRows) {
        	TestSuite suite = new TestSuite();
        	suite.setSuiteId(Integer.parseInt(String.valueOf(suiteRow.get("suite_id"))));
        	suite.setSuiteName(String.valueOf(suiteRow.get("suite_name")));
        	suite.setCreatedDt(String.valueOf(suiteRow.get("created_dt")));
        	//suite.setUpdatedDt(String.valueOf(suiteRow.get("updated_dt")));
        	suite.setUpdatedDt((String.valueOf(suiteRow.get("updated_dt")).toString()).substring(0, (String.valueOf(suiteRow.get("updated_dt")).toString().indexOf('.'))));
        	suite.setStatus(Integer.parseInt(String.valueOf(suiteRow.get("status"))));
            suiteList.add(suite);
        }

        return suiteList;
	}

	public int getTotalRowCount() {
		logger.info("Inside TestSuiteDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from test_suite";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}

	public List<TestSuite> getTestSuiteList() {
		logger.info("Inside TestSuiteDaoImpl :: getTestSuiteList()");

		String query = "select * from test_suite where status=1";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestSuite> suiteList = new ArrayList<TestSuite>();
 
        List<Map<String,Object>> suiteRows = jdbcTemplate.queryForList(query);
        
        for (Map<String,Object> suiteRow : suiteRows) {
        	TestSuite suite = new TestSuite();
        	suite.setSuiteId(Integer.parseInt(String.valueOf(suiteRow.get("suite_id"))));
        	suite.setSuiteName(String.valueOf(suiteRow.get("suite_name")));
        	suite.setCreatedDt(String.valueOf(suiteRow.get("created_dt")));
        	suite.setUpdatedDt(String.valueOf(suiteRow.get("updated_dt")));
        	suite.setStatus(Integer.parseInt(String.valueOf(suiteRow.get("status"))));
            suiteList.add(suite);
        }

        return suiteList;
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		logger.info("Inside TestSuiteDaoImpl :: isNameExistChkByAjaxCall()");

		String query = "";
		int count = 0;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if (id > 0)
			query = "select count(*) from test_suite where suite_id != " + id + " and suite_name like '" + name + "'";
		else
			query = "select count(*) from test_suite where suite_name like '" + name + "'";

		count = jdbcTemplate.queryForInt(query);

		return (count > 0 ? "true" : "false");
	}

	@Override
	public void deleteData(String suiteIds) {
	  logger.info("Inside TestSuiteDaoImpl :: deleteData()");
	
	  String query = "update test_suite set status=0, updated_dt=current_timestamp where suite_id in ("+suiteIds+")";
	  JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
	  int out = jdbcTemplate.update(query/*, suiteId*/);		
	}

	@Override
	public List<TestSuite> getTestSuiteList(Map<String, Object> paramsMap) {
		logger.info("Inside TestSuiteDaoImpl :: getTestSuiteList()");

		StringBuffer queryBuffer = new StringBuffer("select * from test_suite");
		if(paramsMap!=null && paramsMap.size()>0){
			queryBuffer.append(" where");
			if(paramsMap.get("status")!=null){
				queryBuffer.append(" status=1");
			}
		}
		
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestSuite> suiteList = new ArrayList<TestSuite>();
 
        List<Map<String,Object>> suiteRows = jdbcTemplate.queryForList(queryBuffer.toString());
        
        for (Map<String,Object> suiteRow : suiteRows) {
        	TestSuite suite = new TestSuite();
        	suite.setSuiteId(Integer.parseInt(String.valueOf(suiteRow.get("suite_id"))));
        	suite.setSuiteName(String.valueOf(suiteRow.get("suite_name")));
        	suite.setCreatedDt(String.valueOf(suiteRow.get("created_dt")));
        	suite.setUpdatedDt(String.valueOf(suiteRow.get("updated_dt")));
        	suite.setStatus(Integer.parseInt(String.valueOf(suiteRow.get("status"))));
            suiteList.add(suite);
        }

        return suiteList;
	}
	
	
}
