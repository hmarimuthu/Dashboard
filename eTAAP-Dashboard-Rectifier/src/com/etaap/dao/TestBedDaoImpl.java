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

import com.etaap.domain.TestBed;

public class TestBedDaoImpl implements TestBedDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(TestBedDaoImpl.class);

	public void insertData(TestBed bed) {
		logger.info("Inside TestBedDaoImpl :: insertData()");

		String query = "insert into test_bed (bed_name, status, created_dt, updated_dt) values (?, ?, current_timestamp, current_timestamp)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {bed.getBedName().trim(), bed.getStatus()};

		int out = jdbcTemplate.update(query, args);
	}

	public TestBed getTestBed(int bedId) {
		logger.info("Inside TestBedDaoImpl :: getTestBed()");

		String query = "select * from test_bed where bed_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        TestBed bed = jdbcTemplate.queryForObject(query, new Object[]{bedId}, new RowMapper<TestBed>() {

        	public TestBed mapRow(ResultSet rs, int rowNum) throws SQLException {
            	TestBed bed = new TestBed();
            	bed.setBedId(rs.getInt(1));
            	bed.setBedName(rs.getString(2));
                bed.setCreatedDt(rs.getString(3));
                bed.setUpdatedDt(rs.getString(4));
                bed.setStatus(rs.getInt(5));

                return bed;
            }});

        return bed;
	}

	public void updateData(TestBed bed) {
		logger.info("Inside TestBedDaoImpl :: updateData()");

		String query = "update test_bed set bed_name = ?, status = ?, updated_dt = current_timestamp where bed_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[] {bed.getBedName().trim(), bed.getStatus(), bed.getBedId()};

        int out = jdbcTemplate.update(query, args);
	}

	public void deleteData(int bedId) {
		logger.info("Inside TestBedDaoImpl :: deleteData()");

		String query = "update test_bed set status=0, updated_dt=current_timestamp where bed_id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, bedId);
	}

	public List<TestBed> getAllTestBedList(String orderBy, String orderType, int offset, int noOfRecords) {
		logger.info("Inside TestBedDaoImpl :: getAllTestBedList()");

		String query = "select * from test_bed";

		if (!orderBy.equalsIgnoreCase("") && orderBy != null && !orderType.equalsIgnoreCase("") && orderType != null)
			query = query + " order by " + orderBy + " " + orderType + " limit " + offset + ", " + noOfRecords;
		else
			query = query + " order by updated_dt desc limit " + offset + ", " + noOfRecords;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestBed> bedList = new ArrayList<TestBed>();
 
        List<Map<String,Object>> bedRows = jdbcTemplate.queryForList(query);
        
        for (Map<String,Object> bedRow : bedRows) {
        	TestBed bed = new TestBed();
        	bed.setBedId(Integer.parseInt(String.valueOf(bedRow.get("bed_id"))));
        	bed.setBedName(String.valueOf(bedRow.get("bed_name")));
        	bed.setCreatedDt(String.valueOf(bedRow.get("created_dt")));
        	//bed.setUpdatedDt(String.valueOf(bedRow.get("updated_dt")));
        	bed.setUpdatedDt((String.valueOf(bedRow.get("updated_dt")).toString()).substring(0, (String.valueOf(bedRow.get("updated_dt")).toString().indexOf('.'))));
        	bed.setStatus(Integer.parseInt(String.valueOf(bedRow.get("status"))));
            bedList.add(bed);
        }

        return bedList;
	}

	public int getTotalRowCount() {
		logger.info("Inside TestBedDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from test_bed";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}

	public List<TestBed> getTestBedList() {
		logger.info("Inside TestBedDaoImpl :: getTestBedList()");

		String query = "select * from test_bed where status = 1";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestBed> bedList = new ArrayList<TestBed>();
 
        List<Map<String,Object>> bedRows = jdbcTemplate.queryForList(query);
        
        for (Map<String,Object> bedRow : bedRows) {
        	TestBed bed = new TestBed();
        	bed.setBedId(Integer.parseInt(String.valueOf(bedRow.get("bed_id"))));
        	bed.setBedName(String.valueOf(bedRow.get("bed_name")));
        	bed.setCreatedDt(String.valueOf(bedRow.get("created_dt")));
        	bed.setUpdatedDt(String.valueOf(bedRow.get("updated_dt")));
        	bed.setStatus(Integer.parseInt(String.valueOf(bedRow.get("status"))));
            bedList.add(bed);
        }

        return bedList;
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		logger.info("Inside TestBedDaoImpl :: isNameExistChkByAjaxCall()");

		String query = "";
		int count = 0;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if (id > 0)
			query = "select count(*) from test_bed where bed_id != " + id + " and bed_name like '" + name + "'";
		else
			query = "select count(*) from test_bed where bed_name like '" + name + "'";

		count = jdbcTemplate.queryForInt(query);

		return (count > 0 ? "true" : "false");
	}

	@Override
	public void deleteData(String bedIds) {
		// TODO Auto-generated method stub

		logger.info("Inside TestBedDaoImpl :: deleteData()");

		String query = "update test_bed set status=0, updated_dt=current_timestamp where bed_id in ("+bedIds+")";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query/*, bedId*/);
	
		
	}

	@Override
	public List<TestBed> getTestBedList(Map<String, Object> paramsMap) {

		logger.info("Inside TestBedDaoImpl :: getTestBedList()");

		StringBuffer queryBuffer = new StringBuffer("select * from test_bed");
		if(paramsMap!=null && paramsMap.size()>0){
			queryBuffer.append(" where");
			if(paramsMap.get("status")!=null){
				queryBuffer.append(" status = 1");
			}
		}
		
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<TestBed> bedList = new ArrayList<TestBed>();
 
        List<Map<String,Object>> bedRows = jdbcTemplate.queryForList(queryBuffer.toString());
        
        for (Map<String,Object> bedRow : bedRows) {
        	TestBed bed = new TestBed();
        	bed.setBedId(Integer.parseInt(String.valueOf(bedRow.get("bed_id"))));
        	bed.setBedName(String.valueOf(bedRow.get("bed_name")));
        	bed.setCreatedDt(String.valueOf(bedRow.get("created_dt")));
        	bed.setUpdatedDt(String.valueOf(bedRow.get("updated_dt")));
        	bed.setStatus(Integer.parseInt(String.valueOf(bedRow.get("status"))));
            bedList.add(bed);
        }

        return bedList;
	
	}
}
