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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.etaap.domain.SystemAPI;
import com.etaap.security.EncryptionDecryptionAES;

public class SystemAPIDaoImpl implements SystemAPIDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(SystemAPIDaoImpl.class);

	public void insertData(SystemAPI sys) {
		logger.info("Inside SystemAPIDaoImpl :: insertData()");

		String query = "insert into system_api (sys_name, api_id, api_name, url, user_id, password, status, created_dt, updated_dt, is_dev, is_qa, is_operations) values (?, ?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp, ?, ?, ?)";

		//-------New Start---------
		String password = sys.getPassword();
		try {
			if(password == null) {
				password = "";
			}
			password = password.trim();
			if(!password.equals("")) {
				EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
				password = encryptionDecryptionAES.encrypt(password);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			RuntimeException ex = new RuntimeException(e.getMessage());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
		//-------New End-----------

		int is_dev = 0, is_qa = 0, is_operations = 0;
		List<String> departmentList = Arrays.asList(sys.getDepartment().split(","));
		for (String deptName : departmentList) {
			if (deptName.equalsIgnoreCase("Dev"))
				is_dev = 1;
			else if (deptName.equalsIgnoreCase("QA"))
				is_qa = 1;
			else if (deptName.equalsIgnoreCase("Operations"))
				is_operations = 1;
		}

		String sysApiUrl = "";

		StringBuilder apiUrlBuilder = new StringBuilder(sys.getUrl().trim());
		int index = sys.getUrl().trim().lastIndexOf("/");
		logger.info("Inside SystemAPIDaoImpl :: insertData() ::substring :: "+sys.getUrl().trim().substring(index+1));
		
		if(sys.getUrl().trim().substring(index+1).equals("")){
			apiUrlBuilder.setCharAt(sys.getUrl().trim().lastIndexOf("/"), ' ');
			sysApiUrl = apiUrlBuilder.toString().trim();
		}
		else {
			sysApiUrl = sys.getUrl().trim();
		}
		
		logger.info("Inside SystemAPIDaoImpl :: insertData() ::sysApiUrl :: "+sysApiUrl);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//		Object[] args = new Object[] {sys.getSysName().trim(), sys.getApiId(), sys.getApiName().trim(), sys.getUrl().trim(), sys.getUserId().trim(), sys.getPassword().trim(), sys.getStatus()};
		Object[] args = new Object[] {sys.getSysName().trim(), sys.getApiId(), sys.getApiName().trim(), sysApiUrl, sys.getUserId().trim(), password, sys.getStatus(), is_dev, is_qa, is_operations};

		int out = jdbcTemplate.update(query, args);

		if (sys.getApiName().trim().toLowerCase().contains("ji") && is_qa == 1) {
			query = "select max(sys_id) from system_api";
	        int sys_id = jdbcTemplate.queryForInt(query);

	        if (sys.getCustomKey() != null) {
				List<String> customKeyList = Arrays.asList(sys.getCustomKey().split(","));
		        List<String> customValueList = Arrays.asList(sys.getCustomValue().split(","));

		        for (int i=0; i<customKeyList.size(); i++) {
		        	if (!customKeyList.get(i).trim().equalsIgnoreCase("")) {
			        	query = "insert into custom_field (sys_id, custom_field_key, custom_field_value) values (?, ?, ?)";
			    		args = new Object[] {sys_id, customKeyList.get(i).trim(), customValueList.get(i).trim()};
			    		jdbcTemplate.update(query, args);
		        	}
		        }
	        }
	        if (sys.getStatusNewValue() != null) {
	        	query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys_id, sys.getStatusNewValue().trim(), "New"};
	    		jdbcTemplate.update(query, args);
	        }
	        if (sys.getStatusClosedValue() != null) {
	            query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys_id, sys.getStatusClosedValue().trim(), "Closed"};
	    		jdbcTemplate.update(query, args);
	        }
	        if (sys.getStatusInProgressValue() != null) {
	        	query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys_id, sys.getStatusInProgressValue().trim(), "In Progress"};
	    		jdbcTemplate.update(query, args);
	        }
	        if (sys.getStatusVerifyValue() != null) {
	        	query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys_id, sys.getStatusVerifyValue().trim(), "Verify"};
	    		jdbcTemplate.update(query, args);
	        }
	        
	       //Inserting data for priority field
	        if (sys.getPriorityOneValue() != null) {
		        query = "insert into priority(sys_id, priority_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys_id, sys.getPriorityOneValue().trim(), "P1"};
	    		jdbcTemplate.update(query, args);
    		}
	        
	        if (sys.getPriorityTwoValue() != null) {
	        	query = "insert into priority(sys_id, priority_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys_id, sys.getPriorityTwoValue().trim(), "P2"};
	    		jdbcTemplate.update(query, args);
	    	  }
		}
	}

	public SystemAPI getSystemAPI(int sysId) {
		logger.info("Inside SystemAPIDaoImpl :: getSystem()");

		String query = "select * from system_api where sys_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        SystemAPI sys = jdbcTemplate.queryForObject(query, new Object[]{sysId}, new RowMapper<SystemAPI>() {

        	public SystemAPI mapRow(ResultSet rs, int rowNum) throws SQLException {
        		SystemAPI sys = new SystemAPI();
            	sys.setSysId(rs.getInt(1));
                sys.setSysName(rs.getString(2));
                sys.setApiId(rs.getInt(3));
                sys.setApiName(rs.getString(4));
                sys.setUrl(rs.getString(5));
                sys.setUserId(rs.getString(6));
                
                //-------New Start---------
                String password = rs.getString(7);
    			try {
    				if(password == null) {
    					password = "";
    				}
    				password = password.trim();
    				if(!password.equals("")) {
    					EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
    					password = encryptionDecryptionAES.decrypt(password);
    				}
    			}
    			catch(Exception e) {
    				e.printStackTrace();
    				RuntimeException ex = new RuntimeException(e.getMessage());
    				ex.setStackTrace(e.getStackTrace());
    				throw ex;
    			}
//              sys.setPassword(rs.getString(7));
                sys.setPassword(password);
                //-------New End---------

                sys.setCreatedDt(rs.getString(8));
                sys.setUpdatedDt(rs.getString(9));
                sys.setStatus(rs.getInt(10));
                sys.setIsDev(Integer.parseInt(String.valueOf(rs.getInt(11))));
                sys.setIsQa(Integer.parseInt(String.valueOf(rs.getInt(12))));
                sys.setIsOperations(Integer.parseInt(String.valueOf(rs.getInt(13))));

                ArrayList<String> deptList = new ArrayList<String>();
                if (sys.getIsDev() == 1)
                	deptList.add("Dev");
                if (sys.getIsQa() == 1)
                	deptList.add("QA");
                if (sys.getIsOperations() == 1)
                	deptList.add("Operations");

                sys.setDepartmentList(deptList);

                return sys;
            }});

        query = "select * from custom_field where sys_id = " + sysId;
       
        
        

        ArrayList<String> customKeyList = new ArrayList<String>();
        ArrayList<String> customValueList = new ArrayList<String>();
        List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);

        if (rows.size() > 0) {
	        for (Map<String,Object> row : rows) {
	        	if (row.get("custom_field_key") != null && !row.get("custom_field_key").toString().trim().equalsIgnoreCase(""))
	        		customKeyList.add(String.valueOf(row.get("custom_field_key")));
	        	if (row.get("custom_field_value") != null && !row.get("custom_field_value").toString().trim().equalsIgnoreCase(""))
	        		customValueList.add(String.valueOf(row.get("custom_field_value")));
	        }

	        sys.setCustomKeyList(customKeyList);
	        sys.setCustomValueList(customValueList);
        }
        query = "select * from priority where sys_id = " + sysId; 
       
        ArrayList<String> priorityNameList = new ArrayList<String>();
        List<Map<String,Object>> priorityRows = jdbcTemplate.queryForList(query);
        if (priorityRows.size() > 0) {
	        for (Map<String,Object> row : priorityRows) {
	        	if (row.get("priority_name") != null && !row.get("priority_name").toString().trim().equalsIgnoreCase(""))
	        		priorityNameList.add(String.valueOf(row.get("priority_name")));
	        }
	        sys.setPriorityNameList(priorityNameList);
        }
        
        query = "select * from status where sys_id = " + sysId; 
        ArrayList<String> statusNameList = new ArrayList<String>();
        List<Map<String,Object>> statusRows = jdbcTemplate.queryForList(query);
        if (statusRows.size() > 0) {
	        for (Map<String,Object> row : statusRows) {
	        	if (row.get("status_name") != null && !row.get("status_name").toString().trim().equalsIgnoreCase(""))
	        		statusNameList.add(String.valueOf(row.get("status_name")));
	        }
	        sys.setStatusNameList(statusNameList);
        }
        return sys;
	}

	public void updateData(SystemAPI sys) {
		logger.info("Inside SystemAPIDaoImpl :: updateData()");

		String query = "update system_api set sys_name = ?, api_id = ?, api_name = ?, url = ?, user_id = ?, password = ?, status = ?, updated_dt = current_timestamp, is_dev = ?, is_qa = ?, is_operations = ? where sys_id = ?";

		//-------New Start---------
		String password = sys.getPassword();
		try {
			if(password == null) {
				password = "";
			}
			password = password.trim();
			if(!password.equals("")) {
				EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
				password = encryptionDecryptionAES.encrypt(password);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			RuntimeException ex = new RuntimeException(e.getMessage());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
		//-------New End-----------

		int is_dev = 0, is_qa = 0, is_operations = 0;
		List<String> departmentList = Arrays.asList(sys.getDepartment().split(","));
		for (String deptName : departmentList) {
			if (deptName.equalsIgnoreCase("Dev"))
				is_dev = 1;
			else if (deptName.equalsIgnoreCase("QA"))
				is_qa = 1;
			else if (deptName.equalsIgnoreCase("Operations"))
				is_operations = 1;
		}

		String sysApiUrl = "";
		
		StringBuilder apiUrlBuilder = new StringBuilder(sys.getUrl().trim());
		int index = sys.getUrl().trim().lastIndexOf("/");
		logger.info("Inside SystemAPIDaoImpl :: updateData() ::substring :: "+sys.getUrl().trim().substring(index+1));
		
		if(sys.getUrl().trim().substring(index+1).equals("")){
			apiUrlBuilder.setCharAt(sys.getUrl().trim().lastIndexOf("/"), ' ');
			sysApiUrl = apiUrlBuilder.toString().trim();
		}
		else {
			sysApiUrl = sys.getUrl().trim();
		}
		
		logger.info("Inside SystemAPIDaoImpl :: updateData() :: sysApiUrl :: "+sysApiUrl);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//      Object[] args = new Object[] {sys.getSysName().trim(), sys.getApiId(), sys.getApiName().trim(), sys.getUrl().trim(), sys.getUserId().trim(), sys.getPassword().trim(), sys.getStatus(), sys.getSysId()};
        Object[] args = new Object[] {sys.getSysName().trim(), sys.getApiId(), sys.getApiName().trim(), sysApiUrl, sys.getUserId().trim(), password, sys.getStatus(), is_dev, is_qa, is_operations, sys.getSysId()};

        int out = jdbcTemplate.update(query, args);

        if (sys.getApiName().trim().toLowerCase().contains("ji") && is_qa == 1) {
        	query = "delete from custom_field where sys_id = " + sys.getSysId();
            jdbcTemplate.update(query);

			if (sys.getCustomKey() != null) {
	            List<String> customKeyList = Arrays.asList(sys.getCustomKey().split(","));
		        List<String> customValueList = Arrays.asList(sys.getCustomValue().split(","));
	
		        for (int i=0; i<customKeyList.size(); i++) {
		        	if (!customKeyList.get(i).trim().equalsIgnoreCase("")) {
			        	query = "insert into custom_field (sys_id, custom_field_key, custom_field_value) values (?, ?, ?)";
			    		args = new Object[] {sys.getSysId(), customKeyList.get(i).trim(), customValueList.get(i).trim()};
			    		jdbcTemplate.update(query, args);
		        	}
		        }
			}
			
			// Updating priority fields on system API edit page 
			query = "delete from priority where sys_id = " + sys.getSysId();
            jdbcTemplate.update(query);

			if (sys.getPriorityOneValue() != null ) {
		        	query = "insert into priority(sys_id, priority_name, equivalent_to) values (?, ?, ?)";
		    		args = new Object[] {sys.getSysId(), sys.getPriorityOneValue().trim(), "P1"};
		    		jdbcTemplate.update(query, args);
			}
			
			if (sys.getPriorityTwoValue() != null ) {
	        	query = "insert into priority(sys_id, priority_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys.getSysId(), sys.getPriorityTwoValue().trim(), "P2"};
	    		jdbcTemplate.update(query, args);
			}
			
			
			// Updating status fields on system API edit page 
			
			query = "delete from status where sys_id = " + sys.getSysId();
            jdbcTemplate.update(query);
            
			if (sys.getStatusNewValue() != null ) {
				query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys.getSysId(), sys.getStatusNewValue().trim(), "New"};
	    		jdbcTemplate.update(query, args);
			}
			
			if (sys.getPriorityTwoValue() != null ) {
	        	query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys.getSysId(), sys.getStatusClosedValue().trim(), "Closed"};
	    		jdbcTemplate.update(query, args);
			}
			
			if (sys.getPriorityOneValue() != null ) {
	        	query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
	    		args = new Object[] {sys.getSysId(), sys.getStatusInProgressValue().trim(), "In Progress"};
	    		jdbcTemplate.update(query, args);
			}
		
			if (sys.getPriorityTwoValue() != null ) {
		    	query = "insert into status(sys_id, status_name, equivalent_to) values (?, ?, ?)";
				args = new Object[] {sys.getSysId(), sys.getStatusVerifyValue().trim(), "Verify"};
				jdbcTemplate.update(query, args);
			}
			
        }

        
	}

	public void deleteData(int sysId) {
		logger.info("Inside SystemAPIDaoImpl :: deleteData()");

		String query = "update system_api set status = 0, updated_dt = current_timestamp where sys_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, sysId);
	}

	public List<SystemAPI> getAllSystemAPIList(String orderBy, String orderType, int offset, int noOfRecords) {
		logger.info("Inside SystemAPIDaoImpl :: getAllSystemList()");

		String query = "select * from system_api";

		if (!orderBy.equalsIgnoreCase("") && orderBy != null && !orderType.equalsIgnoreCase("") && orderType != null)
			query = query + " order by " + orderBy + " " + orderType + " limit " + offset + ", " + noOfRecords;
		else
			query = query + " order by updated_dt desc limit " + offset + ", " + noOfRecords;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<SystemAPI> sysList = new ArrayList<SystemAPI>();

        List<Map<String,Object>> sysRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> sysRow : sysRows) {
        	SystemAPI sys = new SystemAPI();
            sys.setSysId(Integer.parseInt(String.valueOf(sysRow.get("sys_id"))));
            sys.setSysName(String.valueOf(sysRow.get("sys_name")));
            sys.setApiId(Integer.parseInt(String.valueOf(sysRow.get("api_id"))));
            sys.setApiName(String.valueOf(sysRow.get("api_name")));
            sys.setUrl(String.valueOf(sysRow.get("url")));
            sys.setUserId(String.valueOf(sysRow.get("user_id")));
            sys.setPassword(String.valueOf(sysRow.get("password")));
            sys.setCreatedDt(String.valueOf(sysRow.get("created_dt")));
            sys.setUpdatedDt(String.valueOf(sysRow.get("updated_dt")));
            sys.setStatus(Integer.parseInt(String.valueOf(sysRow.get("status"))));
            sysList.add(sys);
        }

        return sysList;
	}

	public int getTotalRowCount() {
		logger.info("Inside SystemAPIDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from system_api";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}

	public List<SystemAPI> getSystemAPIList() {
		logger.info("Inside SystemAPIDaoImpl :: getSystemList()");

		String query = "select * from system_api where status = 1";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<SystemAPI> sysList = new ArrayList<SystemAPI>();

        List<Map<String,Object>> sysRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> sysRow : sysRows) {
        	SystemAPI sys = new SystemAPI();
        	sys.setSysId(Integer.parseInt(String.valueOf(sysRow.get("sys_id"))));
            sys.setSysName(String.valueOf(sysRow.get("sys_name")));
            sys.setApiId(Integer.parseInt(String.valueOf(sysRow.get("api_id"))));
            sys.setApiName(String.valueOf(sysRow.get("api_name")));
            sys.setUrl(String.valueOf(sysRow.get("url")));
            sys.setUserId(String.valueOf(sysRow.get("user_id")));
            sys.setPassword(String.valueOf(sysRow.get("password")));
            sys.setCreatedDt(String.valueOf(sysRow.get("created_dt")));
            sys.setUpdatedDt(String.valueOf(sysRow.get("updated_dt")));
            sys.setStatus(Integer.parseInt(String.valueOf(sysRow.get("status"))));
            sysList.add(sys);
        }

        return sysList;
	}

	public List<SystemAPI> getCustomFieldList(int sysId) {
		logger.info("Inside SystemAPIDaoImpl :: getCustomFieldList()");

		String query = "select * from custom_field where sys_id = " + sysId;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<SystemAPI> sysList = new ArrayList<SystemAPI>();

        List<Map<String,Object>> sysRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> sysRow : sysRows) {
        	SystemAPI sys = new SystemAPI();
        	sys.setCustomId(Integer.parseInt(String.valueOf(sysRow.get("custom_field_id"))));
        	sys.setSysId(Integer.parseInt(String.valueOf(sysRow.get("sys_id"))));
            sys.setCustomKey(String.valueOf(sysRow.get("custom_field_key")));
            sys.setCustomValue(String.valueOf(sysRow.get("custom_field_value")));
            sysList.add(sys);
        }

        return sysList;
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		logger.info("Inside SystemAPIDaoImpl :: isNameExistChkByAjaxCall()");

		String query = "";
		int count = 0;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if (id > 0)
			query = "select count(*) from system_api where sys_id != " + id + " and sys_name like '" + name + "'";
		else
			query = "select count(*) from system_api where sys_name like '" + name + "'";

		count = jdbcTemplate.queryForInt(query);

		return (count > 0 ? "true" : "false");
	}

	public List<Map<String, Object>> getSystemApiList() {
		StringBuffer stringBuffer = null;
		List<Map<String, Object>> systemApiList = null;
		try {
			stringBuffer = new StringBuffer("select api_name from system_api group by api_name");
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			systemApiList = jdbcTemplate.queryForList(stringBuffer.toString());
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return systemApiList;
	}

	@Override
	public void deleteData(String sysId) {
		logger.info("Inside SystemAPIDaoImpl :: deleteData()");

		String query = "update system_api set status = 0, updated_dt = current_timestamp where sys_id in ("+sysId+")";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query/*, sysId*/);
	
	}

	public List<SystemAPI> getSystemAPIList(int apiId) {
		logger.info("Inside SystemAPIDaoImpl :: getSystemList(apiId)");

		String query = "select * from system_api where status = 1 and api_id = " + apiId;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<SystemAPI> sysList = new ArrayList<SystemAPI>();

        List<Map<String,Object>> sysRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> sysRow : sysRows) {
        	SystemAPI sys = new SystemAPI();
        	sys.setSysId(Integer.parseInt(String.valueOf(sysRow.get("sys_id"))));
            sys.setSysName(String.valueOf(sysRow.get("sys_name")));
            sys.setApiId(Integer.parseInt(String.valueOf(sysRow.get("api_id"))));
            sys.setApiName(String.valueOf(sysRow.get("api_name")));
            sys.setUrl(String.valueOf(sysRow.get("url")));
            sys.setUserId(String.valueOf(sysRow.get("user_id")));
            sys.setPassword(String.valueOf(sysRow.get("password")));
            sys.setCreatedDt(String.valueOf(sysRow.get("created_dt")));
            sys.setUpdatedDt(String.valueOf(sysRow.get("updated_dt")));
            sys.setStatus(Integer.parseInt(String.valueOf(sysRow.get("status"))));
            sysList.add(sys);
        }

        return sysList;
	}
}
