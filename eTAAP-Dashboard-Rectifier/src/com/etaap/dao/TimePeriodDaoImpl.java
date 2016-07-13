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

import com.etaap.domain.TimePeriod;

public class TimePeriodDaoImpl implements TimePeriodDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(TimePeriodDaoImpl.class);

	public void insertData(TimePeriod timePeriod) {
		logger.info("Inside TimePeriodDaoImpl :: insertData()");

		String query = "insert into time_period (app_id, month_id, month_name, created_dt, updated_dt) values (?, ?, ?, current_timestamp, current_timestamp)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {timePeriod.getAppId(), timePeriod.getMonthId(), timePeriod.getMonthName()};

		int out = jdbcTemplate.update(query, args);
	}

	public TimePeriod getTimePeriod(int periodId) {
		logger.info("Inside TimePeriodDaoImpl :: getTimePeriod()");

		String query = "select * from time_period where period_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        TimePeriod timePeriod = jdbcTemplate.queryForObject(query, new Object[]{periodId}, new RowMapper<TimePeriod>() {

        	public TimePeriod mapRow(ResultSet rs, int rowNum) throws SQLException {
        		TimePeriod timePeriod = new TimePeriod();
        		timePeriod.setPeriodId(rs.getInt(1));
        		timePeriod.setAppId(rs.getInt(2));
        		timePeriod.setMonthId(rs.getInt(3));
        		timePeriod.setMonthName(rs.getString(4));
        		timePeriod.setCreatedDt(rs.getString(5));
        		timePeriod.setUpdatedDt(rs.getString(6));

                return timePeriod;
            }});
   
        return timePeriod;
	}

	public void updateData(TimePeriod timePeriod) {
		logger.info("Inside TimePeriodDaoImpl :: updateData()");

		String query = "update time_period set app_id = ?, month_id = ?, month_name = ?, updated_dt = current_timestamp where period_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[] {timePeriod.getAppId(), timePeriod.getMonthId(), timePeriod.getMonthName(), timePeriod.getPeriodId()};

        int out = jdbcTemplate.update(query, args);
	}

	public void deleteData(int periodId) {
		logger.info("Inside TimePeriodDaoImpl :: deleteData()");

		String query = "delete from time_period where period_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, periodId);
	}

	public List<TimePeriod> getAllTimePeriodList(int offset, int noOfRecords) {
		logger.info("Inside TimePeriodDaoImpl :: getAllTimePeriodList()");

		String query = "select tp.*, app.app_name from time_period tp, application app where app.app_id = tp.app_id limit " + offset + ", " + noOfRecords;

		logger.info("Inside TimePeriodDaoImpl :: getAllTimePeriodList() :: query :: " + query);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TimePeriod> timePeriodList = new ArrayList<TimePeriod>();

        List<Map<String,Object>> timePeriodRows = jdbcTemplate.queryForList(query);

        for (Map<String,Object> timePeiord : timePeriodRows) {
        	TimePeriod timePeriod = new TimePeriod();
        	timePeriod.setPeriodId(Integer.parseInt(String.valueOf(timePeiord.get("period_id"))));
        	timePeriod.setAppId(Integer.parseInt(String.valueOf(timePeiord.get("app_id"))));
        	timePeriod.setAppName(String.valueOf(timePeiord.get("app_name")));
        	timePeriod.setMonthId(Integer.parseInt(String.valueOf(timePeiord.get("month_id"))));
        	timePeriod.setMonthName(String.valueOf(timePeiord.get("month_name")));
        	timePeriod.setCreatedDt(String.valueOf(timePeiord.get("created_dt")));
        	timePeriod.setUpdatedDt(String.valueOf(timePeiord.get("updated_dt")));
        	timePeriodList.add(timePeriod);
        }

        return timePeriodList;
	}

	public int getTotalRowCount() {
		logger.info("Inside TimePeriodDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from time_period";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}

	public List<TimePeriod> getTimePeriodListByAppId(int appId) {
		logger.info("Inside TimePeriodDaoImpl :: getTimePeriodListByAppId()");

		String query = "select tp.* from time_period tp, application app" +
				" where app.app_id = tp.app_id" +
				" and app.app_id = " + appId +
				" and app.status = 1";// +
				//" and tp.status = 1" +
				//" order by tp.app_id";

		logger.info("Inside TimePeriodDaoImpl :: getTimePeriodListByAppId() :: query :: " + query);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TimePeriod> timePeriodList = new ArrayList<TimePeriod>();
 
        List<Map<String,Object>> timePeriodRows = jdbcTemplate.queryForList(query);
        
        for (Map<String,Object> timePeiord : timePeriodRows) {
        	TimePeriod timePeriod = new TimePeriod();
        	timePeriod.setPeriodId(Integer.parseInt(String.valueOf(timePeiord.get("period_id"))));
        	//timePeriod.setPeriodName(String.valueOf(timePeiord.get("period_name")));
        	timePeriod.setAppId(Integer.parseInt(String.valueOf(timePeiord.get("app_id"))));
        	timePeriod.setMonthId(Integer.parseInt(String.valueOf(timePeiord.get("month_id"))));
        	timePeriod.setMonthName(String.valueOf(timePeiord.get("month_name")));
        	//timePeriod.setStartDt(String.valueOf(timePeiord.get("start_dt")));
        	//timePeriod.setEndDt(String.valueOf(timePeiord.get("end_dt")));
        	timePeriod.setCreatedDt(String.valueOf(timePeiord.get("created_dt")));
        	timePeriod.setUpdatedDt(String.valueOf(timePeiord.get("updated_dt")));
        	//timePeriod.setStatus(Integer.parseInt(String.valueOf(timePeiord.get("status"))));
        	timePeriodList.add(timePeriod);
        }

        return timePeriodList;
	}
}
