package com.etaap.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.etaap.domain.Application;
import com.etaap.domain.TestCase;
import com.etaap.domain.TimePeriod;
import com.etaap.services.ApplicationService;

public class TestCaseDaoImpl implements TestCaseDao {

	@Autowired
	DataSource dataSource;

	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	private static final Logger logger = Logger.getLogger(TestCaseDaoImpl.class);

	public void insertData(TestCase testCase) {
		logger.info("Inside TestCaseDaoImpl :: insertData()");

		int monthId = ((Application) applicationService.getApplication(testCase.getAppId(),0,null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		List<String> tabIdList = Arrays.asList(testCase.getTabIds().split(","));
		List<String> suiteIdList = Arrays.asList(testCase.getSuiteIds().split(","));
		List<String> tcTypeList = Arrays.asList(testCase.getTcTypes().split(","));
        List<String> tcCountList = Arrays.asList(testCase.getTcCount().split(","));

        String query = null;
        Object[] args = null;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        for (int i = 0; i < getActualTimePeriodList.size(); i++) {
        	for (int j = 0; j < tabIdList.size(); j++) {
	        	if (getActualTimePeriodList.get(i).getPeriodId() == Integer.parseInt(tabIdList.get(j))) {
					query = "insert into tcm (app_id, suite_id, test_case_type, test_case_count, quarter_start_date, quarter_end_date, created_dt, updated_dt, user_id) values (?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp, 1)";
					args = new Object[] {testCase.getAppId(), suiteIdList.get(j), tcTypeList.get(j), tcCountList.get(j), getActualTimePeriodList.get(i).getStartDt(), getActualTimePeriodList.get(i).getEndDt()};
	        		jdbcTemplate.update(query, args);
				}
        	}
        }
	}

	public List<TestCase> getAllTestCaseList(String orderBy, String orderType, int offset, int noOfRecords) {
		logger.info("Inside TestCaseDaoImpl :: getAllTestCaseList()");
		
		String query = "select tc.app_id, app.app_name, sum(tc.test_case_count) total_test_case_count" +
				" from tcm tc, application app where tc.app_id = app.app_id group by app.app_id, tc.updated_dt ";

		if (!orderBy.equalsIgnoreCase("") && orderBy != null && !orderType.equalsIgnoreCase("") && orderType != null)
			query = query + " order by " + orderBy + " " + orderType + " limit " + offset + ", " + noOfRecords;
		else
			query = query + " order by tc.updated_dt desc limit " + offset + ", " + noOfRecords;

		logger.info("Inside TestCaseDaoImpl :: getAllTestCaseList() :: query :: " + query);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestCase> tcList = new ArrayList<TestCase>();

        List<Map<String,Object>> tcRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> tcRow : tcRows) {
        	TestCase tc = new TestCase();
        	tc.setAppId(Integer.parseInt(String.valueOf(tcRow.get("app_id"))));
            tc.setAppName(String.valueOf(tcRow.get("app_name")));
            tc.setTcCount(String.valueOf(tcRow.get("total_test_case_count")));
            tcList.add(tc);
        }

		return tcList;
	}

	public TestCase getTestCaseByAppId(int appId) {
		logger.info("Inside TestCaseDaoImpl :: getTestCaseByAppId()");

		int monthId = ((Application) applicationService.getApplication(appId,0,null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		String query = "select * from application where app_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        TestCase tc = jdbcTemplate.queryForObject(query, new Object[]{appId}, new RowMapper<TestCase>() {

        	public TestCase mapRow(ResultSet rs, int rowNum) throws SQLException {
        		TestCase tc = new TestCase();
            	tc.setAppId(rs.getInt(1));
                tc.setAppName(rs.getString(2));

                return tc;
        	}
        });

        ArrayList<String> idList = new ArrayList<String>();
        ArrayList<String> suiteIdList = new ArrayList<String>();
        ArrayList<String> tcTypeList = new ArrayList<String>();
        ArrayList<String> tcCountList = new ArrayList<String>();
        ArrayList<String> tabIdList = new ArrayList<String>();

        for (int i = 0; i < getActualTimePeriodList.size(); i++) {
        	query = "select * from tcm where app_id = " + appId + " and quarter_start_date = '" + getActualTimePeriodList.get(i).getStartDt() + "' and quarter_end_date = '" + getActualTimePeriodList.get(i).getEndDt() + "'";

        	List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);
            if (rows.size() > 0) {
            	for (Map<String,Object> row : rows) {
     	        	if (row.get("id") != null)
     	        		idList.add(String.valueOf(row.get("id")));
     	        	if (row.get("suite_id") != null)
     	        		suiteIdList.add(String.valueOf(row.get("suite_id")));
     	        	if (row.get("test_case_type") != null)
     	        		tcTypeList.add(String.valueOf(row.get("test_case_type")));
     	        	if (row.get("test_case_count") != null)
     	        		tcCountList.add(String.valueOf(row.get("test_case_count")));

     	        	tabIdList.add(String.valueOf(getActualTimePeriodList.get(i).getPeriodId()));
            	}
            }
        }

        tc.setIdList(idList);
        tc.setSuiteIdList(suiteIdList);
        tc.setTcTypeList(tcTypeList);
        tc.setTcCountList(tcCountList);
        tc.setTabIdList(tabIdList);
        tc.setQuarterList(getActualTimePeriodList);

		return tc;
	}

	public void updateData(TestCase tc) {
		logger.info("Inside TestCaseDaoImpl :: updateData()");

		int monthId = ((Application) applicationService.getApplication(tc.getAppId(),0,null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		List<String> idList = Arrays.asList(tc.getIds().split(","));
        List<String> suiteIdList = Arrays.asList(tc.getSuiteIds().split(","));
        List<String> tcTypeList = Arrays.asList(tc.getTcTypes().split(","));
        List<String> tcCountList = Arrays.asList(tc.getTcCount().split(","));
        List<String> tabIdList = Arrays.asList(tc.getTabIds().split(","));
        List<String> recordTypeList = Arrays.asList(tc.getRecordType().split(","));

        String query = null;
        Object[] args = null;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        for (int i = 0; i < getActualTimePeriodList.size(); i++) {
        	for (int j = 0; j < tabIdList.size(); j++) {
        		if (getActualTimePeriodList.get(i).getPeriodId() == Integer.parseInt(tabIdList.get(j))) {
        			//if (idList.get(j).equalsIgnoreCase("0")) {
        			if (recordTypeList.get(j).equalsIgnoreCase("new")) {
						query = "insert into tcm (app_id, suite_id, test_case_type, test_case_count, quarter_start_date, quarter_end_date, created_dt, updated_dt, user_id) values (?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp, 1)";
						args = new Object[] {tc.getAppId(), suiteIdList.get(j), tcTypeList.get(j), tcCountList.get(j), getActualTimePeriodList.get(i).getStartDt(), getActualTimePeriodList.get(i).getEndDt()};
		        		jdbcTemplate.update(query, args);
        			} else if (recordTypeList.get(j).equalsIgnoreCase("update")) {
        				query = "update tcm set app_id = ?, suite_id = ?, test_case_type = ?, test_case_count = ?, quarter_start_date = ?, quarter_end_date = ?, updated_dt = current_timestamp, user_id = 1 where id = ?";
						args = new Object[] {tc.getAppId(), suiteIdList.get(j), tcTypeList.get(j), tcCountList.get(j), getActualTimePeriodList.get(i).getStartDt(), getActualTimePeriodList.get(i).getEndDt(), idList.get(j)};
		        		jdbcTemplate.update(query, args);
        			} else if (recordTypeList.get(j).equalsIgnoreCase("delete")) {
        				query = "delete from tcm where id = ?";
        				args = new Object[] {idList.get(j)};
        				jdbcTemplate.update(query, args);
        			}
				}
        	}
        }
	}

	public void deleteData(int appId) {
		logger.info("Inside TestCaseDaoImpl :: deleteData()");

		String query = "delete from tcm where app_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, appId);
	}

	public int getTotalRowCount() {
		logger.info("Inside TestCaseDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from (select tc.app_id from tcm tc, application app where tc.app_id = app.app_id group by tc.app_id) x";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}
	
	public String deleteUpdateTCMByAjaxCall(String action, String recordId, String suiteId, String tcType, String tcCount) {
		logger.info("Inside TestCaseDaoImpl :: deleteUpdateTCMByAjaxCall()");

		String msg = "";
		String query = "";
		int count = 0;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if (action != null && action.equalsIgnoreCase("delete")) {
			query = "delete from tcm where id = " + recordId;
			count = jdbcTemplate.update(query);
			if (count > 0)
				msg = "Delete successfull";
		} else if (action != null && action.equalsIgnoreCase("update")) {
			query = "update tcm set suite_id = " + suiteId + ", test_case_type = '" + tcType + "', test_case_count = "+ tcCount +" where id = " + recordId;
			count = jdbcTemplate.update(query);
			if (count > 0)
				msg = "Update successfull";
		}

		return msg;
	}

	@Override
	public void deleteData(String appId) {
		logger.info("Inside TestCaseDaoImpl :: deleteData()");

		String query = "delete from tcm where app_id in ("+appId+")";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query);
	}
}
