package com.etaap.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import com.etaap.db.statements.ETAAPPrepareStatement;
import com.etaap.db.statements.DMLPreparedStatementCallback;
import com.etaap.db.statements.ETAAPPreparedStatementCreator;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;
import com.etaap.utils.enums.SystemAPI;

public class ApplicationDaoImpl implements ApplicationDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(ApplicationDaoImpl.class);
	
	public void insertData(Application app) {
		logger.info("Inside ApplicationDaoImpl :: insertData()");

		String query = "insert into application (app_name, status, created_dt, updated_dt, default_env_id, default_bed_id, default_suite_id, quarter_starting_month_id, quarter_starting_month_name)" +
			" values (?, ?, current_timestamp, current_timestamp, ?, ?, ?, ?, ?)";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Object[] args = new Object[] {app.getAppName().trim(), app.getStatus(), 0, 0, 0, app.getMonthId(), app.getMonthName().trim()};

		int out = jdbcTemplate.update(query, args);

		query = "select max(app_id) from application";
		int app_id = jdbcTemplate.queryForInt(query);

		if (app.getMapId() != null) {
			int apiId = app.getApiId();
			String deptType = app.getDeptType();
	        List<String> systemIdList = Arrays.asList(app.getSystemId().split(","));
	        List<String> urlAliasList = Arrays.asList(app.getUrlAlias().split(","));
	        List<String> envIdList = Arrays.asList(app.getEnvIds().split(","));
	        List<String> suiteIdList = Arrays.asList(app.getSuiteIds().split(","));
	        List<String> bedIdList = Arrays.asList(app.getBedIds().split(","));
	        List<String> isDefaultList = Arrays.asList(app.getIsDefault().split(","));

	        for (int i = 0; i < systemIdList.size(); i++) {
	        	if (apiId == 2) { // 2=Jira
	        		if (deptType.equalsIgnoreCase("dev")) {
						query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, 0, 0, 0, 0, 1, ?)";
	    				args = new Object[] {app_id, systemIdList.get(i), urlAliasList.get(i).trim(), deptType};
		        		jdbcTemplate.update(query, args);
	        		} else {
	        			/*query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, 0, 0, ?, 1, ?)";
	    				args = new Object[] {app_id, systemIdList.get(i), urlAliasList.get(i).trim(), envIdList.get(i), isDefaultList.get(i), deptType};
		        		jdbcTemplate.update(query, args);*/
	        			
	        			/**/
	        			final KeyHolder newlyAddedRecordIds = new GeneratedKeyHolder();
        			    ArrayList mapIdsOfAddedUrlAlias = new ArrayList();

        				////New Start
						query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, 0, 0, 0, 1, ?)";
//						args = new Object[] {app_id, tabIdList.get(j), urlAliasList.get(j).trim(), envIdList.get(j)};		        				

        			    ArrayList types = new ArrayList();
        			    ArrayList values = new ArrayList();

        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);

        			    values.add(app_id);
        			    values.add(Integer.parseInt(systemIdList.get(i)));
        			    values.add(urlAliasList.get(i).trim());
        			    values.add(Integer.parseInt(envIdList.get(i)));
        			    values.add(deptType);

        			    ETAAPPreparedStatementCreator insertPreparedStatement = new ETAAPPreparedStatementCreator(query, types, values);

        				jdbcTemplate.update(insertPreparedStatement, newlyAddedRecordIds);

        				mapIdsOfAddedUrlAlias.add(newlyAddedRecordIds.getKey());

        				int sys_id = Integer.parseInt(systemIdList.get(i));
        			    int isDefault = Integer.parseInt(isDefaultList.get(i));
        				setIsDefault(app_id, sys_id, isDefault, mapIdsOfAddedUrlAlias);
	        			/**/
	        		}
	        	} else if (apiId == 1) { // 1=Jenkins
    		        /*String urlAliasElementsStr = urlAliasList.get(i);
    		        List<String> urlAliasElementsToAdd = Arrays.asList(urlAliasElementsStr.split(";"));
    				List<String> uraliasListDb = new ArrayList<String>();
    				uraliasListDb= getUrlAliasFromDb(systemIdList.get(i));
    				//Get list of all url_alias where sys_id = tabIdList.get(j);
    				for (int k = 0; k < urlAliasElementsToAdd.size(); k++) {
    					String urlAliasName = urlAliasElementsToAdd.get(k);
    					boolean urlAliasFlag= uraliasListDb.contains(urlAliasName);
    					if (urlAliasFlag) {
    						//displayAlert
    					} else {
        					//if urlAliasName does not exists into url_alias list from db then add  
        					query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, ?, ?, ?, 1, ?)";
	        				//args = new Object[] {app_id, tabIdList.get(j), urlAliasList.get(j).trim(), envIdList.get(j), suiteIdList.get(j), bedIdList.get(j), isDefaultList.get(j)};
	        				args = new Object[] {app_id, systemIdList.get(i), urlAliasName.trim(), envIdList.get(i), suiteIdList.get(i), bedIdList.get(i), isDefaultList.get(i), deptType};

	        				jdbcTemplate.update(query, args);
    					}
    				}*/

	        		/* Sharmila's code*/
	        		String urlAliasElementsStr = urlAliasList.get(i);
    				List<String> urlAliasElementsToAdd = Arrays.asList(urlAliasElementsStr.split(";"));
      				List<String> uraliasListDb = getUrlAliasFromDb(systemIdList.get(i));

    				//Get list of all url_alias where sys_id = tabIdList.get(j);
    			    final KeyHolder newlyAddedRecordIds = new GeneratedKeyHolder();
    			    ArrayList mapIdsOfAddedUrls = new ArrayList();
    			    int isDefault = Integer.parseInt(isDefaultList.get(i));
    				for (int k = 0; k < urlAliasElementsToAdd.size(); k++) {
    					String urlAliasName = urlAliasElementsToAdd.get(k);
    					boolean urlAliasContainsIntoDatabaseFlag= uraliasListDb.contains(urlAliasName);
    					if(urlAliasContainsIntoDatabaseFlag) {
    						//Send error message to UI
	        		        System.out.println("********CCCCCCCCCC*********URL Alias contains into DB for System API "+systemIdList.get(i));	        
    					}
    					else {
    						//Get env_id, suite_id, bed_id against rest of the application than app_id
    						//If it exists, then do not add this new urlAlias row.
    						boolean envIdSuiteIdBedIdAlreadyExsists = 
    								doesEnvIdSuiteIdBedIdUniqueForSysId(Integer.parseInt(envIdList.get(i)), Integer.parseInt(suiteIdList.get(i)), Integer.parseInt(bedIdList.get(i)), systemIdList.get(i));
    						if(envIdSuiteIdBedIdAlreadyExsists) {
        						//Send error message to UI
    	        		        System.out.println("********CCCCCCCCCC*********Event Id, Suite Id, Bed Id exists into database for other app_id(s) than "+app_id);	        
    						}
    						else {
	        					query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, ?, ?, 0, 1, ?)";

    	        			    ArrayList types = new ArrayList();
    	        			    ArrayList values = new ArrayList();

    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);

    	        			    values.add(app_id);
    	        			    values.add(Integer.parseInt(systemIdList.get(i)));
    	        			    values.add(urlAliasName.trim());
    	        			    values.add(Integer.parseInt(envIdList.get(i)));
    	        			    values.add(Integer.parseInt(suiteIdList.get(i)));
    	        			    values.add(Integer.parseInt(bedIdList.get(i)));
    	        			    values.add(deptType);

    	        			    ETAAPPreparedStatementCreator insertPreparedStatement = new ETAAPPreparedStatementCreator(query, types, values);

		        				jdbcTemplate.update(insertPreparedStatement, newlyAddedRecordIds);

		        				mapIdsOfAddedUrls.add(newlyAddedRecordIds.getKey());
	        				}
    					}
    				}
    				int sys_id = Integer.parseInt(systemIdList.get(i));
    				setIsDefault(app_id, sys_id, isDefault, mapIdsOfAddedUrls);
	        		/**/
	        	}
	        }
		}
	}

	
	private boolean doesEnvIdSuiteIdBedIdUniqueForSysId(int env_id, int suite_id, int bed_id, String sys_id) {
		boolean retVal = true;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT env_id, suite_id, bed_id FROM application_system_map where sys_id != "+sys_id+
				" and env_id = "+env_id+" and suite_id = "+suite_id+" and bed_id = "+bed_id);
		System.out.println("******&*&*&***xxxxxxxxxxx********* "+queryBuffer);
		List envIdSuiteIdBedIdList = jdbcTemplate.queryForList(queryBuffer.toString());
		if(envIdSuiteIdBedIdList.size() == 0) {
			retVal = false;
		}
		return retVal;
	}
	
	private boolean doesEnvIdSuiteIdBedIdUniqueForSysId(int env_id, int suite_id, int bed_id, String sys_id, int map_id) {
		boolean retVal = true;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT env_id, suite_id, bed_id FROM application_system_map where sys_id != "+sys_id+
				" and env_id = "+env_id+" and suite_id = "+suite_id+" and bed_id = "+bed_id+" and map_id != "+map_id);
		List envIdSuiteIdBedIdList = jdbcTemplate.queryForList(queryBuffer.toString());
		if(envIdSuiteIdBedIdList.size() == 0) {
			retVal = false;
		}
		return retVal;
	}
	
	
/*	private List<String> getUrlAliasFromDb(String sys_id) {
		List urlAliasListToRet = new ArrayList();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT url_alias FROM application_system_map where sys_id="+sys_id);
		List urlAliasList = jdbcTemplate.queryForList(queryBuffer.toString());
		for(int i = 0; i < urlAliasList.size(); i++) {
			HashMap map = (HashMap)urlAliasList.get(i);
			urlAliasListToRet.add(map.get("url_alias"));
		}
		return urlAliasListToRet;
	}
*/	



	private List<String> getUrlAliasFromDb(String sys_id) {
		List urlAliasListToRet = new ArrayList();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT url_alias FROM application_system_map where sys_id=" + sys_id);
		List urlAliasList = jdbcTemplate.queryForList(queryBuffer.toString());
		for (int i = 0; i < urlAliasList.size(); i++) {
			HashMap map = (HashMap) urlAliasList.get(i);
			urlAliasListToRet.add(map.get("url_alias"));
		}
		return urlAliasListToRet;
	}
	
	
	public List<String> getUrlAlias() {
		List urlAliasListToRet = new ArrayList();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT url_alias FROM application_system_map");
		
		List urlAliasList = jdbcTemplate.queryForList(queryBuffer.toString());
		for (int i = 0; i < urlAliasList.size(); i++) {
			HashMap map = (HashMap) urlAliasList.get(i);
			urlAliasListToRet.add(map.get("url_alias"));
		}
		return urlAliasListToRet;
	}

	public List<Application> getDashBoardApplicationList(){
		List<Application> appList = new ArrayList<Application>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select * from application where status = 1");
		queryBuffer.append(" group by app_id");
		try {
		List<Map<String,Object>> appRows = jdbcTemplate.queryForList(queryBuffer.toString());
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setAppName(String.valueOf(appRow.get("app_name")));
            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
            app.setMonthId(Integer.parseInt(String.valueOf(appRow.get("quarter_starting_month_id"))));
            app.setMonthName(String.valueOf(appRow.get("quarter_starting_month_name")));
            appList.add(app);
        }
        for (Application app_obj : appList) {
			int monthId = app_obj.getMonthId();
			 List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);
			 String from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
			 String to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());
			 from = from.substring(0, 10);
			 to = to.substring(0, 10);
			 queryBuffer = new StringBuffer("SELECT  IF( EXISTS(");
			 queryBuffer.append(" SELECT 1 FROM jenkins WHERE app_id = "+app_obj.getAppId()+" AND build_date >= '"+from+" 00:00:00' AND build_date <= '"+to+ " 23:59:59'), 'jenkins', ");
			 queryBuffer.append(" IF( EXISTS(");
			 queryBuffer.append(" SELECT 1 FROM jira WHERE app_id = "+app_obj.getAppId()+" AND active = 1 AND created >= '"+from+" 00:00:00' AND created <= '"+to+ " 23:59:59'), 'jira',");
			 queryBuffer.append(" IF( EXISTS(");
			 queryBuffer.append(" SELECT 1 FROM tcm WHERE app_id = "+app_obj.getAppId()+" AND quarter_start_date >= '"+from+" 00:00:00' AND quarter_end_date <= '"+to+ " 23:59:59'), 'tcm', 'none')");
			 queryBuffer.append(" )");
			 queryBuffer.append(" ) as Priority ");
			 String priority = jdbcTemplate.queryForObject(queryBuffer.toString(),String.class);
			 app_obj.setPriority(priority);
		}
		} catch (Exception e) {
			logger.error("ERROR :: getDashBoardApplicationList() :: " + e.getMessage());
		}
        return appList;
	}

	
	public Application getApplication(int appId, int apiId, String deptType) {
		logger.info("Inside ApplicationDaoImpl :: getApplication()"+appId);
		 Application app = null;
		try{
			String query = "select * from application where app_id = ?";
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
	        app = jdbcTemplate.queryForObject(query, new Object[]{appId}, new RowMapper<Application>() {
	        	
	
	        	public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
	            	Application app = new Application();
	            	app.setAppId(rs.getInt(1));
	                app.setAppName(rs.getString(2));
	                app.setCreatedDt(rs.getString(3));
	                app.setUpdatedDt(rs.getString(4));
	                app.setStatus(rs.getInt(5));
	                app.setEnvId(rs.getInt(6));
	                app.setBedId(rs.getInt(7));
	                app.setSuiteId(rs.getInt(8));
	                app.setMonthId(rs.getInt(9));
	                app.setMonthName(rs.getString(10));
	
	                return app;
	            }});	
	        
	        
///////////////////////////////////****************************
/*	        List<Application> appList = new ArrayList<Application>();
	        List<Map<String,Object>> appRows = jdbcTemplate.queryForList(query);
	        for (Map<String,Object> appRow : appRows) {
	        	app = new Application();
	         	app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
	            app.setAppName(String.valueOf(appRow.get("app_name")));
	            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
	            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
	            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
	            app.setEnvId(Integer.parseInt(String.valueOf(appRow.get("default_env_id"))));
	            app.setEnvName(String.valueOf(appRow.get("env_name")));
	            app.setBedId(Integer.parseInt(String.valueOf(appRow.get("default_bed_id"))));
	            app.setBedName(String.valueOf(appRow.get("bed_name")));
	            app.setSuiteId(Integer.parseInt(String.valueOf(appRow.get("default_suite_id"))));
	            app.setSuiteName(String.valueOf(appRow.get("suite_name")));
	            app.setMonthId(Integer.parseInt(String.valueOf(appRow.get("quarter_starting_month_id"))));
	            app.setMonthName(String.valueOf(appRow.get("quarter_starting_month_name")));
	            appList.add(app);
	            
	        }*/

	        
	        ///////////////////////////////////****************************
        
		

        // query = "select * from application_system_map where app_id = " + appId + " and is_active = 1";
        /*query = "SELECT GROUP_CONCAT(url_alias) AS url_alias, GROUP_CONCAT(map_id) AS map_id,"+
                " env_id, suite_id, bed_id, asm.sys_id, is_default, sa.api_id FROM application_system_map asm, system_api sa"+
        		" where is_active = 1 and app_id = "+appId+" and asm.sys_id = sa.sys_id";*/
        
       /* query = "SELECT GROUP_CONCAT(url_alias) AS url_alias , GROUP_CONCAT(map_id SEPARATOR ';') AS map_id , GROUP_CONCAT(url_alias SEPARATOR ';') AS url_aliasConcated, "+
                " env_id , suite_id , bed_id, asm.sys_id, is_default,sa.api_id FROM application_system_map asm, system_api sa "+
        		" where is_active = 1 and app_id= "+appId+" and asm.sys_id = sa.sys_id";

        if (apiId != 0 && (deptType != null || !deptType.equalsIgnoreCase("")))
        	query = query + " and sa.api_id = "+apiId+" and asm.type = '"+deptType+"'";

        query = query + " GROUP BY env_id, suite_id, bed_id ORDER By is_default desc";
        */
        if(apiId == 1){
            query = "SELECT GROUP_CONCAT(url_alias) AS url_alias , GROUP_CONCAT(map_id SEPARATOR ';') AS map_id , GROUP_CONCAT(url_alias SEPARATOR ';') AS url_aliasConcated, "+
                 " env_id , suite_id , bed_id, asm.sys_id, is_default,sa.api_id FROM application_system_map asm, system_api sa "+
           " where is_active = 1 and app_id= "+appId+" and asm.sys_id = sa.sys_id";
            if (apiId != 0 && (deptType != null || !deptType.equalsIgnoreCase("")))
               query = query + " and sa.api_id = "+apiId+" and asm.type = '"+deptType+"'";
            
               query = query + " GROUP BY env_id, suite_id, bed_id, asm.sys_id , is_default  ORDER By is_default desc";

         }else
         {
          query = "SELECT  url_alias, map_id, env_id , suite_id , bed_id, asm.sys_id, is_default,sa.api_id FROM application_system_map asm, system_api sa "+
               " where is_active = 1 and app_id= "+appId+" and asm.sys_id = sa.sys_id";
          
          if (apiId != 0 && (deptType != null || !deptType.equalsIgnoreCase("")))
                query = query + " and sa.api_id = "+apiId+" and asm.type = '"+deptType+"'";
         }
	        ArrayList<String> mapIdList = new ArrayList<String>();
	        ArrayList<String> sysList = new ArrayList<String>();
	        ArrayList<String> urlAliasList = new ArrayList<String>();
	        ArrayList<String> envIdList = new ArrayList<String>();
	        ArrayList<String> suiteIdList = new ArrayList<String>();
	        ArrayList<String> bedIdList = new ArrayList<String>();
	        ArrayList<String> isDefaultList = new ArrayList<String>();
	        ArrayList<String> apiList = new ArrayList<String>();
	        ArrayList<String> concatedUrlAliasList = new ArrayList<String>();
	
	        List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);
	        if (rows.size() > 0) {
		        for (Map<String,Object> row : rows) {
		        	if (row.get("map_id") != null)
		        		mapIdList.add(String.valueOf(row.get("map_id")));
		        	if (row.get("sys_id") != null)
		        		sysList.add(String.valueOf(row.get("sys_id")));
		        	if (row.get("url_alias") != null && !row.get("url_alias").toString().trim().equalsIgnoreCase(""))
		        		urlAliasList.add(String.valueOf(row.get("url_alias")));
		        	if (row.get("env_id") != null)
		        		envIdList.add(String.valueOf(row.get("env_id")));
		        	if (row.get("suite_id") != null)
		        		suiteIdList.add(String.valueOf(row.get("suite_id")));
		        	if (row.get("bed_id") != null)
		        		bedIdList.add(String.valueOf(row.get("bed_id")));
		        	if (row.get("is_default") != null)
		        		isDefaultList.add(String.valueOf(row.get("is_default")));
		        	if (row.get("api_id") != null)
		        		apiList.add(String.valueOf(row.get("api_id")));
		        	if (row.get("url_aliasConcated") != null)
		        		concatedUrlAliasList.add(String.valueOf(row.get("url_aliasConcated")));
		        }
		        /*HashSet<String> hashSet = new HashSet<String>(sysList);
		        ArrayList<String> sysList2 = new ArrayList<String>(hashSet);*/
	
		        //app.setSysIdList(sysList2);
		        app.setMapIdList(mapIdList);
		        app.setTabIdList(sysList);
		        app.setUrlAliasList(urlAliasList);
		        app.setEnvIdList(envIdList);
		        app.setSuiteIdList(suiteIdList);
		        app.setBedIdList(bedIdList);
		        app.setIsDefaultList(isDefaultList);
		        app.setApiIdList(apiList);
		        app.setConcatedUrlAliasList(concatedUrlAliasList);
	        }
		}
		catch(EmptyResultDataAccessException e){
			return null;
			//logger.info("Inside ApplicationDaoImpl :: getApplication()"+e.getMessage());
			
		}
	       return app;
	}
	

	public void updateData(Application app) {
		logger.info("Inside ApplicationDaoImpl :: updateData()");

		String query = "update application set app_name = ?, status = ?, default_env_id = ?, default_bed_id = ?, default_suite_id = ?, quarter_starting_month_id = ?, quarter_starting_month_name = ?, updated_dt = current_timestamp where app_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[] {app.getAppName().trim(), app.getStatus(), 0, 0, 0, app.getMonthId(), app.getMonthName().trim(), app.getAppId()};

        int out = jdbcTemplate.update(query, args);

        //String systemIds = app.getSystemId().substring(1, app.getSystemId().length());
        //String systemNames = app.getSysName().substring(1, app.getSysName().length());

        if (app.getMapId() != null) {
        	int apiId = app.getApiId();
			String deptType = app.getDeptType();
	        List<String> systemIdList = Arrays.asList(app.getSystemId().split(","));
	        List<String> urlAliasList = Arrays.asList(app.getUrlAlias().split(","));
	        //System.out.println("after split urlAlias "+app.getUrlAlias());
	        List<String> envIdList = Arrays.asList(app.getEnvIds().split(","));
	        List<String> suiteIdList = Arrays.asList(app.getSuiteIds().split(","));
	        List<String> bedIdList = Arrays.asList(app.getBedIds().split(","));
	        List<String> isDefaultList = Arrays.asList(app.getIsDefault().split(","));
	        List<String> mapIdList = Arrays.asList(app.getMapId().split(","));
	        //System.out.println("after split tabids "+app.getTabId());
	        //System.out.println("after split mapids "+app.getMapId());

	        query = "select map_id from application_system_map where app_id = " + app.getAppId() + " and type = '" + deptType + "' and is_active = 1";
	        ArrayList<String> mapIdListFrmDb = new ArrayList<String>();

	        List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);
	        if (rows.size() > 0) {
		        for (Map<String,Object> row : rows) {
		        	if (row.get("map_id") != null)
		        		mapIdListFrmDb.add(String.valueOf(row.get("map_id")));
		        }
	        }

	        /*query = "update application_system_map set is_default = 0, is_active = 0 where app_id = ?";*/
	        //query = "update application_system_map set is_active = 0 where app_id = ?";
	        /*jdbcTemplate.update(query, app.getAppId());*/

	        for (int i = 0; i < systemIdList.size(); i++) {
	        	if (apiId == 2) { // 2=Jira
					//New Start
    			    ArrayList types = new ArrayList();
    			    ArrayList values = new ArrayList();
    			    ArrayList mapIdsOfAddedUrlAlias = new ArrayList();
    			    boolean insertedFlag = false;
					//New End
    				if (mapIdList.get(i).equalsIgnoreCase("0")) {
    					int mapId = 0;
    					if (deptType.equalsIgnoreCase("dev"))
    						getDeactiveMapId(2, app.getAppId(), 0, 0, 0);
    					else
    						getDeactiveMapId(2, app.getAppId(), Integer.parseInt(envIdList.get(i)), 0, 0);

    					if (deptType.equalsIgnoreCase("dev")) {
	    					if (mapId > 0) {
	    						System.out.println(" 1: trying to update application_system_map" );
	    						query = "update application_system_map set sys_id = ?, url_alias = ?, is_default = 0, is_active = 1 where map_id = ?";
	    						args = new Object[] {systemIdList.get(i), urlAliasList.get(i).trim(), mapId};
	    						jdbcTemplate.update(query, args);
	    					}
	    					else {
	        					query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, 0, 0, 0, 0, 1, ?)";
				        		args = new Object[] {app.getAppId(), systemIdList.get(i), urlAliasList.get(i).trim(), deptType};
				        		jdbcTemplate.update(query, args);
	    					}
    					} else {
    						if (mapId > 0) {
	    						/*query = "update application_system_map set sys_id = ?, url_alias = ?, is_default = ?, is_active = 1 where map_id = ?";
	    						args = new Object[] {systemIdList.get(i), urlAliasList.get(i).trim(), isDefaultList.get(i), mapId};
	    						jdbcTemplate.update(query, args);*/

    							/*Sharmila's code*/
    							
    							System.out.println(" 2: trying to update application_system_map" );
    							query = "update application_system_map set sys_id = ?, url_alias = ?, is_default = 0, is_active = 1 where map_id = ?";
//        						args = new Object[] {tabIdList.get(j), urlAliasList.get(j).trim(), isDefaultList.get(j), mapId};
//        						jdbcTemplate.update(query, args);
        						args = new Object[] {systemIdList.get(i), urlAliasList.get(i).trim(), mapId};

        						//New Start
        						insertedFlag = false;
		        			    mapIdsOfAddedUrlAlias.add(mapId);

    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);

    	        			    values.add(Integer.parseInt(systemIdList.get(i)));
    	        			    values.add(urlAliasList.get(i).trim());
    	        			    values.add(mapId);
    	        			    //New End
    	        			    /**/
	    					}
	    					else {
	        					/*query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, 0, 0, ?, 1, ?)";
				        		args = new Object[] {app.getAppId(), systemIdList.get(i), urlAliasList.get(i).trim(), envIdList.get(i), isDefaultList.get(i), deptType};
				        		jdbcTemplate.update(query, args);*/

	    						/*Sharmila's code*/
	    						query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, 0, 0, 0, 1, ?)";
				        		args = new Object[] {app.getAppId(), systemIdList.get(i), urlAliasList.get(i).trim(), envIdList.get(i)};
//				        		args = new Object[] {app.getAppId(), tabIdList.get(j), urlAliasList.get(j).trim(), envIdList.get(j), isDefaultList.get(j)};
//				        		jdbcTemplate.update(query, args);

        						//New Start
				        		insertedFlag = true;
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
    	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);

    	        			    values.add(app.getAppId());
    	        			    values.add(Integer.parseInt(systemIdList.get(i)));
    	        			    values.add(urlAliasList.get(i).trim());
    	        			    values.add(Integer.parseInt(envIdList.get(i)));
    	        			    values.add(deptType);
    	        			    //New End
	    						/**/
	    					}
    					}
    				} else if (mapIdListFrmDb.contains(mapIdList.get(i))) {
    					if (deptType.equalsIgnoreCase("dev")) {
    						query = "update application_system_map set sys_id = ?, url_alias = ?, env_id = 0, suite_id = 0, bed_id = 0, is_default = 0, is_active = 1 where map_id = ?";
        					args = new Object[] {systemIdList.get(i), urlAliasList.get(i).trim(), mapIdList.get(i)};
        					jdbcTemplate.update(query, args);
    					} else {
    						/*query = "update application_system_map set sys_id = ?, url_alias = ?, env_id = ?, suite_id = 0, bed_id = 0, is_default = ?, is_active = 1 where map_id = ?";
        					args = new Object[] {systemIdList.get(i), urlAliasList.get(i).trim(), envIdList.get(i), isDefaultList.get(i), mapIdList.get(i)};
        					jdbcTemplate.update(query, args);*/

    						/*Sharmila's code*/
    						System.out.println(" 3: trying to update application_system_map" );
    						query = "update application_system_map set sys_id = ?, url_alias = ?, env_id = ?, suite_id = 0, bed_id = 0, is_default = 0, is_active = 1 where map_id = ?";
//        					args = new Object[] {tabIdList.get(j), urlAliasList.get(j).trim(), envIdList.get(j), isDefaultList.get(j), mapIdList.get(j)};
//        					jdbcTemplate.update(query, args);

    						//New Start
        					insertedFlag = false;
	        			    mapIdsOfAddedUrlAlias.add(Integer.parseInt(mapIdList.get(i)));
	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_STRING);
	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);
	        			    types.add(ETAAPPrepareStatement.VALUE_TYPE_BIG_INT);

	        			    values.add(Integer.parseInt(systemIdList.get(i)));
	        			    values.add(urlAliasList.get(i).trim());
	        			    values.add(Integer.parseInt(envIdList.get(i)));
	        			    values.add(Integer.parseInt(mapIdList.get(i)));
	        			    //New End
	        			    /**/
    					}
    				}

    				if (deptType.equalsIgnoreCase("qa")) {
	    				//Get list of all url_alias where sys_id = tabIdList.get(j);
	    			    final KeyHolder newlyAddedRecordIds = new GeneratedKeyHolder();
	
	    			    ETAAPPreparedStatementCreator insertPreparedStatement = new ETAAPPreparedStatementCreator(query, types, values);
	
	    				jdbcTemplate.update(insertPreparedStatement, newlyAddedRecordIds);
	    				if(insertedFlag) {
	    					mapIdsOfAddedUrlAlias.add(newlyAddedRecordIds.getKey());
	    				}
	
	    				int sys_id = Integer.parseInt(systemIdList.get(i));
	    			    int isDefault = Integer.parseInt(isDefaultList.get(i));
	    				setIsDefault(app.getAppId(), sys_id, isDefault, mapIdsOfAddedUrlAlias);
	    				////New End
    				}
	        	} else if (apiId == 1) { // 1=Jenkins
	        		/*Shambhu's code*/
    				/*if (mapIdList.get(i).equalsIgnoreCase("0")) {
    					//Adding new row here
    					//Get urlalias
    					String urlAliasElementsStr = urlAliasList.get(i);
    					//Split urlalias into arraylist
        			    List<String> urlAliasElementsToAdd = Arrays.asList(urlAliasElementsStr.split(";"));	

        			    for (int k = 0; k < urlAliasElementsToAdd.size(); k++) {
							String UpdateUrlAliasName = urlAliasElementsToAdd.get(k);
							List<String> updateUraliasListDb = new ArrayList<String>();
							updateUraliasListDb = getUrlAliasFromDb(systemIdList.get(i));
							boolean urlAliasFlag = updateUraliasListDb.contains(UpdateUrlAliasName);
							if (!urlAliasFlag) {
								addSingleApplicationSystemMap(UpdateUrlAliasName, systemIdList.get(i), isDefaultList.get(i), app.getAppId(), envIdList.get(i), suiteIdList.get(i), bedIdList.get(i), deptType);
							}

        			        //Get single urlalias
							/*		String UpdateUrlAliasName = urlAliasElementsToAdd.get(k);
		        					//Execute following code for each urlalias.
		        					int mapId = getDeactiveMapId(1, app.getAppId(), Integer.parseInt(envIdList.get(j)), Integer.parseInt(suiteIdList.get(j)), Integer.parseInt(bedIdList.get(j)));
		        					if (mapId > 0) {
		        						query = "update application_system_map set sys_id = ?, url_alias = ?, is_default = ?, is_active = 1 where map_id = ?";
//		        						args = new Object[] {tabIdList.get(j), urlAliasList.get(j).trim(), isDefaultList.get(j), mapId};
		        						args = new Object[] {tabIdList.get(j), UpdateUrlAliasName.trim(), isDefaultList.get(j), mapId};

		        						jdbcTemplate.update(query, args);
		        					} //
		        					else {
			        					query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active) values (?, ?, ?, ?, ?, ?, ?, 1)";
			        					args = new Object[] {app.getAppId(), tabIdList.get(j), UpdateUrlAliasName.trim(), envIdList.get(j), suiteIdList.get(j), bedIdList.get(j), isDefaultList.get(j)};
			        					jdbcTemplate.update(query, args);
		        					}
		        					*/
        			    /*}
    				} else if (mapIdListFrmDb.contains(mapIdList.get(i))) {
    					
//	        					System.out.println("update mapidlist "+mapIdList.get(j));
//	        					System.out.println("update urlAliaslist "+urlAliasList.get(j));
//	        					
//	        					String dummyMapIds = "1,2";
//	        					String dummyUrlAlias = "one,two,three";
////	        					HashMap<String, String>  addUrlAliasHasmap =getMApIdsUrlAlisOrdered(dummyMapIds,dummyUrlAlias);

    					int count = 0;
    					Application mapDetail = getJobMapping(Integer.parseInt(mapIdList.get(i)));

    					query = "update application_system_map set sys_id = ?, url_alias = ?, env_id = ?, suite_id = ?, bed_id = ?, is_default = ?, is_active = 1 where map_id = ?";
    					args = new Object[] {systemIdList.get(i), urlAliasList.get(i).trim(), envIdList.get(i), suiteIdList.get(i), bedIdList.get(i), isDefaultList.get(i), mapIdList.get(i)};
    					jdbcTemplate.update(query, args);

    					// if changing env_id / suite_id / bed_id for same job
    					// then jenkins data should also get changed to new env_id / suite_id / bed_id for same job.
    					query = "select count(*) from jenkins where app_id = "+mapDetail.getAppId()+" and env_id = "+mapDetail.getEnvId()+" and suite_id = "+mapDetail.getSuiteId()+" and bed_id = "+mapDetail.getBedId()+"";
    					count = jdbcTemplate.queryForInt(query);
    					if (count > 0) {
        					query = "update jenkins set env_id = ?, suite_id = ?, bed_id = ? where app_id = ? and env_id = ? and suite_id = ? and bed_id = ?";
        					args = new Object[] {envIdList.get(i), suiteIdList.get(i), bedIdList.get(i), mapDetail.getAppId(), mapDetail.getEnvId(), mapDetail.getSuiteId(), mapDetail.getBedId()};
        					jdbcTemplate.update(query, args);
    					} else {
    						mapDetail = null;
    					}
    					/////

    					// added by Shambhu on 3-Jun-2015 for Bug#14
    					int mapId = getDeactiveMapId(1, app.getAppId(), Integer.parseInt(envIdList.get(i)), Integer.parseInt(suiteIdList.get(i)), Integer.parseInt(bedIdList.get(i)));
    					if (mapId > 0) {
    						query = "delete from application_system_map where map_id = ?";
    						args = new Object[] {mapId};
        					jdbcTemplate.update(query, args);
    					}
    					//
    				}*/
    				
    				/*Shambhu's code*/
	        		
	        		/*Sharmila's code*/

	        		String concatedMapIdsUrlAlias = (String)mapIdList.get(i);
    		        String urlAliasesFromClientUITextField = (String)urlAliasList.get(i);
	        		HashMap addDeleteURLAliasMap = getJenkinsJobToDeleteAddUpdate(concatedMapIdsUrlAlias, urlAliasesFromClientUITextField, app.getAppId(), systemIdList.get(i));
	        		ArrayList toAdd = (ArrayList)addDeleteURLAliasMap.get("TO_ADD");
	        		ArrayList toUpdate = (ArrayList)addDeleteURLAliasMap.get("TO_UPDATE");
	        		ArrayList toDelete = (ArrayList)addDeleteURLAliasMap.get("TO_DELETE");

    		        if(toAdd.size() > 0) {
						boolean envIdSuiteIdBedIdAlreadyExsists = doesEnvIdSuiteIdBedIdUniqueForSysId(Integer.parseInt(envIdList.get(i)), Integer.parseInt(suiteIdList.get(i)), Integer.parseInt(bedIdList.get(i)), systemIdList.get(i));
						if (envIdSuiteIdBedIdAlreadyExsists) {
							//Send error message to UI
	        		        System.out.println("********CCCCCCCCCC*********Event Id, Suite Id, Bed Id exists into database for other app_id(s) than "+app.getAppId());	        
						}
						else {
        					//New Start
	        			    final KeyHolder newlyAddedRecordIds = new GeneratedKeyHolder();
	        			    ArrayList mapIdsOfAddedUrlAlias = new ArrayList();
        					//New End
    						for(int nx = 0; nx < toAdd.size(); nx++) {
        			        	String urlAliasFromClient = (String)toAdd.get(nx);
        			        	/*****/
        			        	List<String> uraliasListDb = getUrlAliasFromDb(systemIdList.get(i));
                                System.out.println("******************VVVVVVVVVVVVV************ "+uraliasListDb);

                                boolean urlAliasContainsIntoDatabaseFlag= uraliasListDb.contains(urlAliasFromClient);
                                if(urlAliasContainsIntoDatabaseFlag) {
                                	//Send error message to UI
                                	System.out.println("********CCCCCCCCCC*********URL Alias contains into DB for System API "+systemIdList.get(i));         
                                }
                                else {
        			        	/*****/
			        				//query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active) values (?, ?, ?, ?, ?, ?, ?, 1)";
		        					String insertQuery = "insert into application_system_map (map_id, app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) "
		        							+ "values (null, "+app.getAppId()+","+systemIdList.get(i)+",'"+urlAliasFromClient.trim()+"',"+envIdList.get(i)+","+suiteIdList.get(i)+","+bedIdList.get(i)
		        							+",0,1,'"+deptType+"')";
				        			//jdbcTemplate.update(insertQuery);
		        					
		        					//New Start
	    	        			    ETAAPPreparedStatementCreator insertPreparedStatement = new ETAAPPreparedStatementCreator(insertQuery, null, null);
			        				jdbcTemplate.update(insertPreparedStatement, newlyAddedRecordIds);
			        				mapIdsOfAddedUrlAlias.add(newlyAddedRecordIds.getKey());
		        					//New End
                                }
    						}
        					//New Start
	        				int sys_id = Integer.parseInt(systemIdList.get(i));
	        			    int isDefault = Integer.parseInt(isDefaultList.get(i));
	        				setIsDefault(app.getAppId(), sys_id, isDefault, mapIdsOfAddedUrlAlias);
        					//New End
						}
    		        }

					if(toUpdate.size() > 0)	{
        			    ArrayList mapIdsOfUpdatedUrlAlias = new ArrayList();
						for(int nx = 0; nx < toUpdate.size(); nx++) {
    						boolean envIdSuiteIdBedIdAlreadyExsists = doesEnvIdSuiteIdBedIdUniqueForSysId(Integer.parseInt(envIdList.get(i)), 
									Integer.parseInt(suiteIdList.get(i)), Integer.parseInt(bedIdList.get(i)) ,systemIdList.get(i), Integer.parseInt((String)toUpdate.get(nx)));
							if(!envIdSuiteIdBedIdAlreadyExsists) {
								//Send error message to UI
		        		        System.out.println("********CCCCCCCCCC*********Event Id, Suite Id, Bed Id exists into database for other sys_id(s) and map_id(s)");	        
							}
							else {

	        					//Update
								System.out.println(" 4: trying to update application_system_map" );
	        					String updateQuery = "update application_system_map set sys_id = ?, env_id = ?, suite_id = ?, bed_id = ?, is_default = 0, is_active = 1 where map_id = ?";
	        					args = new Object[] {systemIdList.get(i), envIdList.get(i), suiteIdList.get(i), bedIdList.get(i), toUpdate.get(nx)};
	        					jdbcTemplate.update(updateQuery, args);
	        					int mapIdUpdated = Integer.parseInt((String)toUpdate.get(nx));
	        					mapIdsOfUpdatedUrlAlias.add(mapIdUpdated);

/*		        					query = "update jenkins set env_id = ?, suite_id = ?, bed_id = ? where app_id = ? and env_id = ? and suite_id = ? and bed_id = ?";
	        					args = new Object[] {envIdList.get(i), suiteIdList.get(i), bedIdList.get(i), app.getAppId(), envIdList.get(i), suiteIdList.get(i), bedIdList.get(i)};
	        					jdbcTemplate.update(query, args);
	        					*/

	    						updateJenkinsTable(String.valueOf(mapIdUpdated), String.valueOf(app.getAppId()), envIdList.get(i), suiteIdList.get(i), bedIdList.get(i));
	        					//New End
							}
						}
    					//New Start
        				int sys_id = Integer.parseInt(systemIdList.get(i));
        			    int isDefault = Integer.parseInt(isDefaultList.get(i));
        				setIsDefault(app.getAppId(), sys_id, isDefault, mapIdsOfUpdatedUrlAlias);
    					//New End
    		        }

					if(toDelete.size() > 0) {
        		        String toDeleteStr = StringUtils.collectionToCommaDelimitedString(toDelete);
        		        //Delete
        		        System.out.println(" 5: trying to update application_system_map" );
    					String deleteQuery = "update application_system_map set is_active = 0 where map_id IN("+toDeleteStr+")";
    					jdbcTemplate.update(deleteQuery);
    					
    					//New Start
        				int sys_id = Integer.parseInt(systemIdList.get(i));
//        			    int isDefault = Integer.parseInt(isDefaultList.get(i));
        				setIsDefault(app.getAppId(), sys_id, 0, toDelete);
    					//New End
					}

	        		/**/
    				
    				//jdbcTemplate.update(query, args);
	        	}
	        }
		}/* else {
			//query = "update application_system_map set is_default = 0, is_active = 0 where app_id = ?";
			query = "update application_system_map set is_active = 0 where app_id = ?";
	        jdbcTemplate.update(query, app.getAppId());
		}*/
	}

	
	
	private void updateJenkinsTable(String mapId, String appId, String newEnvId, String newSuiteId, String newBedId) {
		String env_id1 = "";
		String suite_id1 = "";
		String bed_id1 = "";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query1 = "select env_id, suite_id, bed_id from application_system_map where map_id ="+mapId;
        List<Map<String,Object>> rows1 = jdbcTemplate.queryForList(query1);
        if (rows1.size() > 0) {
	        Map<String,Object> row = rows1.get(0);
	        
        	env_id1 = ((Integer)row.get("env_id")).toString();
        	suite_id1 = ((Integer)row.get("suite_id")).toString();
        	bed_id1 = ((Integer)row.get("bed_id")).toString();
        	
        	boolean update = false;
        	if(!env_id1.equals(newEnvId)) {
        		update = true;
        	}
        	else if(!suite_id1.equals(newSuiteId)) {
        		update = true;
        	}
        	else if(!bed_id1.equals(newBedId)) {
        		update = true;
        	}
        	
        	if(update) {
	    		String query = "update jenkins set env_id = ?, suite_id = ?, bed_id = ? where app_id = ? and env_id = ? and suite_id = ? and bed_id = ?";
	    		Object args[] = new Object[] {newEnvId, newSuiteId, newBedId, appId, env_id1, suite_id1, bed_id1};
	    		jdbcTemplate.update(query, args);
        	}
        }
	}

	
	

	private HashMap getMApIdsUrlAlisOrdered(String mapIds, String urlAlias){
		//Get ArrayList of map ids using split
		List<String> mapIdList = Arrays.asList(mapIds.split(","));
		//Get ArrayList of urlAlias using split
		List<String> urlAliasList = Arrays.asList(urlAlias.split(","));

		//Write SQL query to validate existance of mapids and urlalias into application_system_map table

		//If there urlAlias does not exists, then consider it as new urlalias to add.

//		//Match mapids and urlalias exists into dabase - sharmila
//		//If number of urlalias > number of mapids
//		HashMap<String, String> mapIdsUrlAliasHasmap = new HashMap<String, String>();
//		 System.out.println("urlAliasList size :"+urlAliasList.size());
//		 System.out.println("mapIdList size :"+mapIdList.size());
//		if(urlAliasList.size() > mapIdList.size()){
//			String lastElement = urlAliasList.get(urlAliasList.size()-1);
//			mapIdsUrlAliasHasmap.put("Toadd", lastElement);
//		}
			//Then hashmap should say to add the urlalias	
		return null;
	}
	
	private void addSingleApplicationSystemMap(String urlAlias,  String sysId, String isdefault, int appId, String envId, String suiteId, String bedId, String deptType) {
		 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	     Object[] args = null;
		 String query;

//		int mapId = getDeactiveMapId(1, app.getAppId(), Integer.parseInt(envIdList.get(j)), Integer.parseInt(suiteIdList.get(j)), Integer.parseInt(bedIdList.get(j)));
		int mapId = getDeactiveMapId(1, appId, Integer.parseInt(envId), Integer.parseInt(suiteId), Integer.parseInt(bedId));
		if (mapId > 0) {
			System.out.println(" 7: trying to update application_system_map" );
			query = "update application_system_map set sys_id = ?, url_alias = ?, is_default = ?, is_active = 1 where map_id = ?";
//			args = new Object[] {tabIdList.get(j), urlAliasList.get(j).trim(), isDefaultList.get(j), mapId};
			args = new Object[] {sysId, urlAlias.trim(), isdefault, mapId};

			jdbcTemplate.update(query, args);
		} 
		else {
			query = "insert into application_system_map (app_id, sys_id, url_alias, env_id, suite_id, bed_id, is_default, is_active, type) values (?, ?, ?, ?, ?, ?, ?, 1, ?)";
			args = new Object[] {appId, sysId, urlAlias.trim(), envId, suiteId, bedId, isdefault, deptType};
			jdbcTemplate.update(query, args);
		}
		
	}

	public void deleteData(int appId) {
		logger.info("Inside ApplicationDaoImpl :: deleteData()");

		String query = "update application set status = 0, updated_dt = current_timestamp where app_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query, appId);
	}

	public List<Application> getAllApplicationList(String orderBy, String orderType, int offset, int noOfRecords) {
		logger.info("Inside ApplicationDaoImpl :: getAllApplicationList()");

		String query = "select app.*, env.env_name, bed.bed_name, suite.suite_name from application app" +
			" LEFT JOIN environment env on app.default_env_id=env.env_id" +
			" LEFT JOIN test_bed bed on app.default_bed_id=bed.bed_id" +
			" LEFT JOIN test_suite suite on app.default_suite_id=suite.suite_id";

		if (!orderBy.equalsIgnoreCase("") && orderBy != null && !orderType.equalsIgnoreCase("") && orderType != null)
			query = query + " order by " + orderBy + " " + orderType + " limit " + offset + ", " + noOfRecords;
		else
			query = query + " order by app.updated_dt desc limit " + offset + ", " + noOfRecords; //, app.status

		logger.info("Inside ApplicationDaoImpl :: getAllApplicationList() :: query :: " + query);
		System.out.println("query ::: "+query);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Application> appList = new ArrayList<Application>();

        List<Map<String,Object>> appRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setAppName(String.valueOf(appRow.get("app_name")));
            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
            app.setEnvId(Integer.parseInt(String.valueOf(appRow.get("default_env_id"))));
            app.setEnvName(String.valueOf(appRow.get("env_name")));
            app.setBedId(Integer.parseInt(String.valueOf(appRow.get("default_bed_id"))));
            app.setBedName(String.valueOf(appRow.get("bed_name")));
            app.setSuiteId(Integer.parseInt(String.valueOf(appRow.get("default_suite_id"))));
            app.setSuiteName(String.valueOf(appRow.get("suite_name")));
            app.setMonthId(Integer.parseInt(String.valueOf(appRow.get("quarter_starting_month_id"))));
            app.setMonthName(String.valueOf(appRow.get("quarter_starting_month_name")));
            appList.add(app);
        }

        return appList;
	}

	public int getTotalRowCount() {
		logger.info("Inside ApplicationDaoImpl :: getTotalRowCount()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select count(*) from application";
        int count = jdbcTemplate.queryForInt(query);

        return count;
	}

	public List<Application> getApplicationList() {
		logger.info("Inside ApplicationDaoImpl :: getApplicationList()");

		String query = "select * from application where status = 1";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Application> appList = new ArrayList<Application>();
 
        List<Map<String,Object>> appRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setAppName(String.valueOf(appRow.get("app_name")));
            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
            appList.add(app);
        }

        return appList;
	}

	public List<Application> getUrlAliasList(int appId, String apiName, String deptType) {
		logger.info("Inside ApplicationDaoImpl :: getUrlAlias()");

		/*String query = "select asm.*, sa.url from application_system_map asm, system_api sa" +
			" where asm.sys_id = sa.sys_id and asm.app_id = " + appId + " and asm.is_active = 1 and lower(sa.api_name) = '" + apiName + "'";*/

		String query = "select asm.*, sa.* from application_system_map asm, system_api sa" +
		" where asm.sys_id = sa.sys_id and asm.is_active = 1 and sa.status = 1 and lower(sa.api_name) = '" + apiName  + "' and type = '" + deptType + "'";

		System.out.println("query -----> "+query);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Application> appList = new ArrayList<Application>();

        List<Map<String,Object>> appRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setEnvId(Integer.parseInt(String.valueOf(appRow.get("env_id"))));
            app.setSuiteId(Integer.parseInt(String.valueOf(appRow.get("suite_id"))));
            app.setBedId(Integer.parseInt(String.valueOf(appRow.get("bed_id"))));
            app.setSysId(Integer.parseInt(String.valueOf(appRow.get("sys_id"))));
            app.setUrl(String.valueOf(appRow.get("url")));
            app.setUrlAlias(String.valueOf(appRow.get("url_alias")));
            app.setUserId(String.valueOf(appRow.get("user_id")));
            app.setPassword(String.valueOf(appRow.get("password")));
            app.setMapId(String.valueOf(appRow.get("map_id")));
            
            //For email start
            app.setSysName(String.valueOf(appRow.get("sys_name")));
           
            //For email end
            
            appList.add(app);
        }

        return appList;
	}

	public List<Application> getApplicationMapDetails(int appId) {
		logger.info("Inside ApplicationDaoImpl :: getApplicationMapDetails()");

		String query = "select * from application_system_map where app_id = " + appId;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Application> appList = new ArrayList<Application>();

		Application app = new Application();
		ArrayList<String> envIdList = new ArrayList<String>();
        ArrayList<String> suiteIdList = new ArrayList<String>();
        ArrayList<String> bedIdList = new ArrayList<String>();
        System.out.println("getApplicationMapDetails query @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ "+query);
        List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);
        if (rows.size() > 0) {
	        for (Map<String,Object> row : rows) {
	        	if (row.get("env_id") != null)
	        		envIdList.add(String.valueOf(row.get("env_id")));
	        	if (row.get("suite_id") != null)
	        		suiteIdList.add(String.valueOf(row.get("suite_id")));
	        	if (row.get("bed_id") != null)
	        		bedIdList.add(String.valueOf(row.get("bed_id")));
	        	if (row.get("is_default") != null && Integer.parseInt((row.get("is_default").toString())) == 1) {
	        		app.setEnvId(Integer.parseInt((row.get("env_id").toString())));
	        		app.setSuiteId(Integer.parseInt((row.get("suite_id").toString())));
	        		app.setBedId(Integer.parseInt((row.get("bed_id").toString())));
	        	}
	        }
	        HashSet<String> hashSet = new HashSet<String>(envIdList);
	        ArrayList<String> envIdList2 = new ArrayList<String>(hashSet);
	        HashSet<String> hashSet2 = new HashSet<String>(suiteIdList);
	        ArrayList<String> suiteIdList2 = new ArrayList<String>(hashSet2);
	        HashSet<String> hashSet3 = new HashSet<String>(bedIdList);
	        ArrayList<String> bedIdList2 = new ArrayList<String>(hashSet3);

	        app.setEnvIdList(envIdList2);
	        app.setSuiteIdList(suiteIdList2);
	        app.setBedIdList(bedIdList2);
	        appList.add(app);
        }

		return appList;
	}
	
	public List<Application> getApplicationMapDetailsForJenkins(int appId) {
		logger.info("Inside ApplicationDaoImpl :: getApplicationMapDetails()");

//		String query = "select * from application_system_map where app_id = " + appId;
		
		String query = "select distinct asm.env_id, asm.suite_id, asm.bed_id, asm.is_default from "+ 
		" application_system_map asm, system_api sa where asm.app_id = "+appId+ 
		" and asm.sys_id = sa.sys_id and sa.api_name = 'Jenkins'";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Application> appList = new ArrayList<Application>();

		Application app = new Application();
		ArrayList<String> envIdList = new ArrayList<String>();
        ArrayList<String> suiteIdList = new ArrayList<String>();
        ArrayList<String> bedIdList = new ArrayList<String>();

        List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);
        if (rows.size() > 0) {
	        for (Map<String,Object> row : rows) {
	        	if (row.get("env_id") != null)
	        		envIdList.add(String.valueOf(row.get("env_id")));
	        	if (row.get("suite_id") != null)
	        		suiteIdList.add(String.valueOf(row.get("suite_id")));
	        	if (row.get("bed_id") != null)
	        		bedIdList.add(String.valueOf(row.get("bed_id")));
	        	if (row.get("is_default") != null && Integer.parseInt((row.get("is_default").toString())) == 1) {
	        		app.setEnvId(Integer.parseInt((row.get("env_id").toString())));
	        		app.setSuiteId(Integer.parseInt((row.get("suite_id").toString())));
	        		app.setBedId(Integer.parseInt((row.get("bed_id").toString())));
	        	}
	        }
	        HashSet<String> hashSet = new HashSet<String>(envIdList);
	        ArrayList<String> envIdList2 = new ArrayList<String>(hashSet);
	        HashSet<String> hashSet2 = new HashSet<String>(suiteIdList);
	        ArrayList<String> suiteIdList2 = new ArrayList<String>(hashSet2);
	        HashSet<String> hashSet3 = new HashSet<String>(bedIdList);
	        ArrayList<String> bedIdList2 = new ArrayList<String>(hashSet3);

	        app.setEnvIdList(envIdList2);
	        app.setSuiteIdList(suiteIdList2);
	        app.setBedIdList(bedIdList2);
	        appList.add(app);
        }

		return appList;
	}
	

	public String getMapIdByAppId(String appId) {
		// TODO Auto-generated method stub
		logger.info("Inside ApplicationDaoImpl :: getMapIdByAppId()");

		String mapId = null;
		try {
			StringBuffer gettingMapIdString = new StringBuffer("select map_id from application_system_map where app_id = ");
			gettingMapIdString.append("'" + appId + "'");
			gettingMapIdString.append(" and is_default = '1'");
			gettingMapIdString.append(" and sys_id = (select sys_id from system_api where api_name = 'jira')"); 

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			Long map_Id = jdbcTemplate.queryForLong(gettingMapIdString.toString());

			mapId = map_Id != null ? map_Id.toString() : null;
		} catch (Exception errors) {
			errors.printStackTrace();
			logger.error("ERROR : getMapIdByAppId() ");
		}

		return mapId;
	}

	public List<Map<String, Object>> getApplicationSystemMapService(String appId) {
		logger.info("Inside ApplicationDaoImpl :: getApplicationSystemMapService()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from application_system_map where app_id = " + appId + " order by env_id");

		return rows;
	}
	
	public List<Map<String, Object>> getApplicationSystemMap(String appId, String envId, String suiteId, String bedId) {
		logger.info("Inside ApplicationDaoImpl :: getApplicationSystemMap()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select env_id,suite_id,bed_id,app_id,is_default from application_system_map where app_id = " + appId +
				" and env_id = "+envId+" and suite_id = "+suiteId+ " and bed_id = "+bedId );//+ "order by is_default desc"

		return rows;
		
	}

	public List<Map<String, Object>> getApplicationSystemMapBasedOnMapId(String mapId) {
		logger.info("Inside ApplicationDaoImpl :: getApplicationSystemMapBasedOnMapId()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select env_id,suite_id,bed_id,app_id from application_system_map where map_id = " + mapId);

		return rows;
	}

	public List<Application> getApplicationListSpecificSystemApi(String systemName) {
		// TODO Auto-generated method stub
		logger.info("Inside ApplicationDaoImpl :: getApplicationListSpecificSystemApi()");

		List<Application> appList = new ArrayList<Application>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select a.* from application a, system_api s, application_system_map m , " + systemName.toLowerCase() + " j");
		queryBuffer.append(" where a.app_id = m.app_id");
		queryBuffer.append(" and m.sys_id in (select sys_id from system_api where api_name = '" + systemName.toLowerCase() + "')");
		queryBuffer.append(" and a.status=1");
		if (systemName.equalsIgnoreCase(SystemAPI.JENKINS.getSystemName())) {
			queryBuffer.append(" and j.app_id=m.app_id and j.env_id=m.env_id and j.suite_id=m.suite_id and j.bed_id=m.bed_id");
		} else if (systemName.equalsIgnoreCase(SystemAPI.JIRA.getSystemName())) {
			queryBuffer.append(" and j.app_id=m.app_id and j.env_id=m.env_id");
		}
		queryBuffer.append(" group by a.app_id");
		
		System.out.println("*********** GET Application List="+queryBuffer);

		List<Map<String, Object>> appRows = jdbcTemplate.queryForList(queryBuffer.toString());
		for (Map<String, Object> appRow : appRows) {
			Application app = new Application();
			app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
			app.setAppName(String.valueOf(appRow.get("app_name")));
			app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
			app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
			app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
			appList.add(app);
		}
		
		

		return appList;
	}

	public String getMapIdByAppId(String appId, String systemApiName) {
		// TODO Auto-generated method stub
		logger.info("Inside ApplicationDaoImpl :: getMapIdByAppId()");

		String mapId = null;
		try {
			StringBuffer gettingMapIdString = new StringBuffer("select app_sys_map.map_id from application_system_map app_sys_map where app_sys_map.app_id = ");
			gettingMapIdString.append("'" + appId + "'");
			gettingMapIdString.append(" and app_sys_map.is_default = '1'");
			gettingMapIdString.append(" and app_sys_map.sys_id = (select sys_api.sys_id from system_api sys_api where sys_api.api_name = '"+systemApiName+"' and app_sys_map.sys_id=sys_api.sys_id) "); 

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

			Long map_Id = jdbcTemplate.queryForLong(gettingMapIdString.toString());

			mapId = map_Id != null ? map_Id.toString() : null;
		} catch (Exception errors) {
			errors.printStackTrace();
			logger.error("ERROR : getMapIdByAppId() ");
		}

		return mapId;
	}
	
	
	public HashMap getDefaultActiveEnvIdSuiteIdBedIdByAppId(String appId, String systemApiName) {
		// TODO Auto-generated method stub
		logger.info("Inside ApplicationDaoImpl :: getMapIdByAppId()");

		HashMap map = null;
		StringBuffer gettingEnvIdSuiteIdBedIdString = new StringBuffer("select app_sys_map.env_id, app_sys_map.suite_id, app_sys_map.bed_id from application_system_map app_sys_map where app_sys_map.app_id = ");
		gettingEnvIdSuiteIdBedIdString.append("'" + appId + "'");
		gettingEnvIdSuiteIdBedIdString.append(" and app_sys_map.is_default = 1 and app_sys_map.is_active = 1 ");
		gettingEnvIdSuiteIdBedIdString.append(" and app_sys_map.sys_id = (select sys_api.sys_id from system_api sys_api where sys_api.api_name = '"+systemApiName+"' and app_sys_map.sys_id=sys_api.sys_id) "); 

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(gettingEnvIdSuiteIdBedIdString.toString());
		if(rows.size() > 0) {
			map = (HashMap)rows.get(0);
		}
		
		System.out.println("&*&*&*&* Default Active "+rows);
		return map;
	}

	public List<Map<String, Object>> getApplicationSystemMapService(String appId, String systemApiName) {
		logger.info("Inside ApplicationDaoImpl :: getApplicationSystemMapService()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = null;
		//Start New
		if(systemApiName.trim().equals("Jira"))
			System.out.println("appId :: "+appId);
		System.out.println("systemApiName :: "+systemApiName);
			rows = jdbcTemplate.queryForList("select * from application_system_map a where a.app_id = " + appId + " and a.sys_id = (select b.sys_id from system_api b where b.api_name = '"+systemApiName+"' and a.sys_id=b.sys_id) order by is_default desc");
		if(systemApiName.trim().equals("Jenkins"))
			rows = jdbcTemplate.queryForList("select distinct env_id, suite_id, bed_id, is_default from application_system_map a where a.app_id = " + appId + " and a.sys_id = (select b.sys_id from system_api b where b.api_name = '"+systemApiName+"' and a.sys_id=b.sys_id) order by is_default desc"); //order by env_id
		//End New
		return rows;
	}

	public int getMonthId(int appId) {
		logger.info("Inside ApplicationDaoImpl :: getMonthId()");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "select quarter_starting_month_id from application where app_id = " + appId;
		System.out.println("getMonthId :: "+query);
        int monthId = jdbcTemplate.queryForInt(query);

        return monthId;
	}

	public String isNameExistChkByAjaxCall(int id, String name) {
		logger.info("Inside ApplicationDaoImpl :: isNameExistChkByAjaxCall()");

		String query = "";
		int count = 0;

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		if (id > 0)
			query = "select count(*) from application where app_id != " + id + " and app_name like '" + name + "'";
		else
			query = "select count(*) from application where app_name like '" + name + "'";

		count = jdbcTemplate.queryForInt(query);

		return (count > 0 ? "true" : "false");
	}

	@Override
	public void deleteData(String appIds) {
		// TODO Auto-generated method stub

		logger.info("Inside ApplicationDaoImpl :: deleteData()");

		String query = "update application set status = 0, updated_dt = current_timestamp where app_id in ("+appIds+")";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        int out = jdbcTemplate.update(query/*, appId*/);
	
	}

	public List<Application> getApplicationListToCreateTestCase() {
		logger.info("Inside ApplicationDaoImpl :: getApplicationListToCreateTC()");

		String query = "select * from application where status = 1 and app_id not in (select app_id from tcm)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Application> appList = new ArrayList<Application>();
 
        List<Map<String,Object>> appRows = jdbcTemplate.queryForList(query);
        for (Map<String,Object> appRow : appRows) {
        	Application app = new Application();
            app.setAppId(Integer.parseInt(String.valueOf(appRow.get("app_id"))));
            app.setAppName(String.valueOf(appRow.get("app_name")));
            app.setCreatedDt(String.valueOf(appRow.get("created_dt")));
            app.setUpdatedDt(String.valueOf(appRow.get("updated_dt")));
            app.setStatus(Integer.parseInt(String.valueOf(appRow.get("status"))));
            appList.add(app);
        }

        return appList;
	}

	public Application getJobMapping (int mapId) {
		logger.info("Inside ApplicationDaoImpl :: getJobMapping()");

		String query = "select * from application_system_map where map_id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Application app = jdbcTemplate.queryForObject(query, new Object[]{mapId}, new RowMapper<Application>() {

        	public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
            	Application app = new Application();
            	app.setAppId(rs.getInt(2));
                app.setEnvId(rs.getInt(5));
                app.setSuiteId(rs.getInt(6));
                app.setBedId(rs.getInt(7));

                return app;
            }});

        return app;
	}
	
	public int getDeactiveMapId (int apiId, int appId, int envId, int suiteId, int bedId) {
		logger.info("Inside ApplicationDaoImpl :: getDeactiveMapId()");

		int mapId = 0;
		String query = "";
		Object[] args = null;

		if (apiId == 1) {
	        query = "select map_Id from application_system_map where app_id = ? and env_id = ? and suite_id = ? and bed_id = ? and is_active = 0";
	        args = new Object[] {appId, envId, suiteId, bedId};
		} else {
			query = "select map_Id from application_system_map where app_id = ? and env_id = ? and suite_id = 0 and bed_id = 0 and is_active = 0";
	        args = new Object[] {appId, envId};
		}

		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        	mapId = jdbcTemplate.queryForInt(query, args);
		} catch(Exception e) {
			mapId = 0;
		}
		return mapId;
	}
	
/*	private void setIsDefault(int app_id, int sys_id, int is_default, ArrayList newMapIdsToDefault) {
		  if (newMapIdsToDefault.size() <= 0)
		   return;

		  if(is_default == 1) {
		         JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		   String apiNameQuery = "SELECT api_name FROM system_api where sys_id = "+sys_id;
		   String apiName = null;
		   List apiNameList = jdbcTemplate.queryForList(apiNameQuery.toString());
		   if (apiNameList.size() == 0) {
		    System.out.println("No server is configured for sys_id "+sys_id);
		   }
		   else {
		    Map<String,Object> apiNameRow = (Map)apiNameList.get(o);
		    apiName = ((String)apiNameRow.get("api_name")).trim();
		   }

		   if(apiName == null) {
		    System.out.println("Could not found API name for "+sys_id);
		   }

		   String sysIdForAPITypeQuery = "select asm.sys_id from application_system_map asm, system_api sa "+ 
		   " where asm.app_id = "+app_id+" and asm.sys_id = sa.sys_id and sa.api_name = '"+apiName+"'";

		         List<Map<String,Object>> sysIdRows = jdbcTemplate.queryForList(sysIdForAPITypeQuery);
		         ArrayList<String> sysIdsTosetDefault = new ArrayList<String>();
		         for (Map<String,Object> sysIdRow : sysIdRows) {
		          sysIdsTosetDefault.add(String.valueOf(sysIdRow.get("sys_id")));
		         }

		         String sysIsToSetDefault = StringUtils.collectionToCommaDelimitedString(sysIdsTosetDefault);
		   String updateQuery = "update application_system_map set is_default = 0 where app_id = "+ 
		   app_id+ " and sys_id IN ("+sysIsToSetDefault+")";
		   jdbcTemplate.update(updateQuery);

		         String newMapIdsToSetDefault = StringUtils.collectionToCommaDelimitedString(newMapIdsToDefault);
		   String updateQuery1 = "update application_system_map set is_default = "+is_default+
		     " where sys_id = "+sys_id+" and map_id IN("+newMapIdsToSetDefault+")";
		   jdbcTemplate.update(updateQuery1);
		  }
		  else if(is_default == 0) {
		         String newMapIdsToSetDefault = StringUtils.collectionToCommaDelimitedString(newMapIdsToDefault);
		   String updateQuery = "update application_system_map set is_default = 0 where map_id IN("+newMapIdsToSetDefault+")";
		         JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		   jdbcTemplate.update(updateQuery);
		  }
		 }
*/
	
	
	public Map<String, String> getAPIList() {
		logger.info("Inside ApplicationDaoImpl :: getAPIList()");

		Map<String, String> apiList = new HashMap<String, String>();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select api_id, api_name from system_api group by api_id, api_name");

		List<Map<String, Object>> appRows = jdbcTemplate.queryForList(queryBuffer.toString());
		for (Map<String, Object> appRow : appRows) {
			String key = (String.valueOf(appRow.get("api_id")));
			String value = (String.valueOf(appRow.get("api_name")));
			apiList.put(key, value);
		}

		return (new TreeMap<String, String>(apiList));
	}

	public Map<String, String> getDepartmentList() {
		logger.info("Inside ApplicationDaoImpl :: getDepartmentList()");		

		int count = 0;
		Map<String, String> deptList = new HashMap<String, String>();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select is_dev from system_api where is_dev = 1 group by is_dev");

		try {
			count = jdbcTemplate.queryForInt(queryBuffer.toString());
	        if (count == 1)
	        	deptList.put("1", "Dev");
        } catch (Exception e) {}

		queryBuffer = new StringBuffer("select is_qa from system_api where is_qa = 1 group by is_qa");
		try {
	        count = jdbcTemplate.queryForInt(queryBuffer.toString());
	        if (count == 1)
	        	deptList.put("2", "QA");
		} catch (Exception e) {}

		queryBuffer = new StringBuffer("select is_operations from system_api where is_operations = 1 group by is_operations");
		try {
			count = jdbcTemplate.queryForInt(queryBuffer.toString());
	        if (count == 1)
	        	deptList.put("3", "Operations");
		} catch (Exception e) {}

        return (new TreeMap<String, String>(deptList));
	}

	private void setIsDefault(int app_id, int sys_id, int is_default, ArrayList newMapIdsToDefault) {
		  if (newMapIdsToDefault.size() <= 0)
		   return;

		  if(is_default == 1) {
			   JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			   String apiNameQuery = "SELECT api_name FROM system_api where sys_id = "+sys_id;
			   String apiName = null;
			   List apiNameList = jdbcTemplate.queryForList(apiNameQuery.toString());
			   if (apiNameList.size() == 0) {
				   System.out.println("No server is configured for sys_id "+sys_id);
			   }
			   else {
					Map<String,Object> apiNameRow = (Map)apiNameList.get(0);
					apiName = ((String)apiNameRow.get("api_name")).trim();
			   }
	
			   if(apiName == null) {
				   System.out.println("Could not found API name for "+sys_id);
			   }
	
			   String sysIdForAPITypeQuery = "select asm.sys_id from application_system_map asm, system_api sa "+ 
			   " where asm.app_id = "+app_id+" and asm.sys_id = sa.sys_id and sa.api_name = '"+apiName+"'";
	
     			 List<Map<String,Object>> sysIdRows = jdbcTemplate.queryForList(sysIdForAPITypeQuery);
				 ArrayList<String> sysIdsTosetDefault = new ArrayList<String>();
				 for (Map<String,Object> sysIdRow : sysIdRows) {
					 sysIdsTosetDefault.add(String.valueOf(sysIdRow.get("sys_id")));
				 }
	
				String sysIsToSetDefault = StringUtils.collectionToCommaDelimitedString(sysIdsTosetDefault);
				String updateQuery = "update application_system_map set is_default = 0 where app_id = "+ 
				app_id+ " and sys_id IN ("+sysIsToSetDefault+")";
				jdbcTemplate.update(updateQuery);
	
               String newMapIdsToSetDefault = StringUtils.collectionToCommaDelimitedString(newMapIdsToDefault);
			   String updateQuery1 = "update application_system_map set is_default = "+is_default+
			     " where sys_id = "+sys_id+" and map_id IN("+newMapIdsToSetDefault+")";
			   jdbcTemplate.update(updateQuery1);
		  }
		  else if(is_default == 0) {
				String newMapIdsToSetDefault = StringUtils.collectionToCommaDelimitedString(newMapIdsToDefault);
				String updateQuery = "update application_system_map set is_default = 0 where map_id IN("+newMapIdsToSetDefault+")";
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				jdbcTemplate.update(updateQuery);
		  }
	 }

	private HashMap<String, Object> getJenkinsJobToDeleteAddUpdate(String concatedMapIdUrlAliasFromClient, String urlAliasFromClientUITextField, 
			int app_id, String sys_id) {
		HashMap retVal = new HashMap();
		ArrayList toAdd = new ArrayList();
		ArrayList toDelete = new ArrayList();
		ArrayList toUpdate = new ArrayList();
		retVal.put("TO_ADD", toAdd);
		retVal.put("TO_DELETE", toDelete);
		retVal.put("TO_UPDATE", toUpdate);

        String mapIds = null;
        String urlAlias = null;
        
		if(concatedMapIdUrlAliasFromClient != null) {
			String arr[] = concatedMapIdUrlAliasFromClient.split(":");
			if(arr.length == 2) {
		        mapIds = arr[0];
		        urlAlias = arr[1];
			}
		}
        
        List<String> mapIdsListFromClientUI = new ArrayList<String>();
        if(mapIds != null) {
        	mapIdsListFromClientUI = Arrays.asList(mapIds.split(";"));
        }
        
        
        List<String> urlAliasListFromClientUIInMapIds = new ArrayList<String>();
        if(urlAlias != null) {
        	urlAliasListFromClientUIInMapIds = Arrays.asList(urlAlias.split(";"));
        }
        
        List<String> urlAliasListFromClientInTextField = new ArrayList<String>();
        if((urlAliasFromClientUITextField != null) && (!urlAliasFromClientUITextField.trim().equals(""))) {
        	List<String> urlAliasListFromClientInTextFieldWithSpaces = Arrays.asList(urlAliasFromClientUITextField.split(";"));
	        for (String row : urlAliasListFromClientInTextFieldWithSpaces) {
	        	if(!row.trim().equals("")) {
	        		urlAliasListFromClientInTextField.add(row.trim());
	        	}
	        }
        }
        
        
        ArrayList<String> mapIdListFrmDb = new ArrayList<String>();
        ArrayList<String> urlAliasListFrmDb = new ArrayList<String>();
        
        if(mapIdsListFromClientUI.size() > 0) {
            String commaSeparatedMapIds = mapIds.replaceAll(";",",");
        	
	        String query = "select map_id, url_alias from application_system_map where map_id IN("+commaSeparatedMapIds+")";
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	        List<Map<String,Object>> rows = jdbcTemplate.queryForList(query);
	        
	        String query1 = "select map_id, url_alias from application_system_map where app_id = "+app_id;
	        System.out.println("apsm query" + query1);
	        		
	        List<Map<String,Object>> rows1 = jdbcTemplate.queryForList(query1);
	        
	        rows.addAll(rows1);
	        
	        if (rows.size() > 0) {
		        for (Map<String,Object> row : rows) {
		        	if (row.get("map_id") != null) {
		        		mapIdListFrmDb.add((String.valueOf(row.get("map_id"))).trim());
		        		urlAliasListFrmDb.add((String.valueOf(row.get("url_alias"))).trim());
		        	}
		        }
	        }
        }
        
        for(int i = 0; i < mapIdsListFromClientUI.size(); i++) {
        	String mapIdFromClient1 = (mapIdsListFromClientUI.get(i)).trim();
        	String urlAliasFromClient1 = (urlAliasListFromClientUIInMapIds.get(i)).trim();
        	
        	if(mapIdListFrmDb.indexOf(mapIdFromClient1) == -1) {
        		//corrupted information
        	}
        	if(urlAliasListFrmDb.indexOf(urlAliasFromClient1) == -1) {
        		//corrupted information
        	}
        }
        
        for(int j = 0; j < urlAliasListFromClientInTextField.size(); j++) {
        	String urlAliasFromClient2 = (urlAliasListFromClientInTextField.get(j)).trim();
        	if(!urlAliasListFrmDb.contains(urlAliasFromClient2)) {
        		//To Add
        		toAdd.add(urlAliasFromClient2);
        	}
        	else if(urlAliasListFrmDb.contains(urlAliasFromClient2)) {
        		int index = urlAliasListFrmDb.indexOf(urlAliasFromClient2);
        		toUpdate.add(mapIdListFrmDb.get(index));
        	}
        }
        
        for(int j = 0; j < urlAliasListFrmDb.size(); j++) {
        	String urlAliasFromDB1 = (urlAliasListFrmDb.get(j)).trim();
        	if(!urlAliasListFromClientInTextField.contains(urlAliasFromDB1)) {
        		//To Delete
        		toDelete.add(mapIdListFrmDb.get(j));
        	}
        }
        return retVal;
	}

/*	private boolean doesEnvIdSuiteIdBedIdUniqueForSysId(int env_id, int suite_id, int bed_id, String sys_id) {
		boolean retVal = true;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT env_id, suite_id, bed_id FROM application_system_map where sys_id != "+sys_id+
				" and env_id = "+env_id+" and suite_id = "+suite_id+" and bed_id = "+bed_id);
		List envIdSuiteIdBedIdList = jdbcTemplate.queryForList(queryBuffer.toString());
		if(envIdSuiteIdBedIdList.size() == 0) {
			retVal = false;
		}
		return retVal;
	}
*/	
/*	private boolean doesEnvIdSuiteIdBedIdUniqueForSysId(int env_id, int suite_id, int bed_id, String sys_id, int map_id) {
		boolean retVal = true;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("SELECT env_id, suite_id, bed_id FROM application_system_map where sys_id != "+sys_id+
				" and env_id = "+env_id+" and suite_id = "+suite_id+" and bed_id = "+bed_id+" and map_id != "+map_id);
		List envIdSuiteIdBedIdList = jdbcTemplate.queryForList(queryBuffer.toString());
		if(envIdSuiteIdBedIdList.size() == 0) {
			retVal = false;
		}
		return retVal;
	}
*/
/*	private void updateJenkinsTable(String mapId, String appId, String newEnvId, String newSuiteId, String newBedId) {
		String env_id1 = "";
		String suite_id1 = "";
		String bed_id1 = "";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String query1 = "select env_id, suite_id, bed_id from application_system_map where map_id ="+mapId;
        List<Map<String,Object>> rows1 = jdbcTemplate.queryForList(query1);
        if (rows1.size() > 0) {
	        Map<String,Object> row = rows1.get(0);

        	env_id1 = ((Integer)row.get("env_id")).toString();
        	suite_id1 = ((Integer)row.get("suite_id")).toString();
        	bed_id1 = ((Integer)row.get("bed_id")).toString();

        	boolean update = false;
        	if(!env_id1.equals(newEnvId)) {
        		update = true;
        	}
        	else if(!suite_id1.equals(newSuiteId)) {
        		update = true;
        	}
        	else if(!bed_id1.equals(newBedId)) {
        		update = true;
        	}

        	if(update) {
	    		String query = "update jenkins set env_id = ?, suite_id = ?, bed_id = ? where app_id = ? and env_id = ? and suite_id = ? and bed_id = ?";
	    		Object args[] = new Object[] {newEnvId, newSuiteId, newBedId, appId, env_id1, suite_id1, bed_id1};
	    		jdbcTemplate.update(query, args);
        	}
        }
	}*/
	
	public String disableRecordForApp(String mapids) {
		  // TODO Auto-generated method stub

		  logger.info("Inside ApplicationDaoImpl :: deleteData()");

		  String query = "update application_system_map set is_active = 0 where map_id in ("+mapids+")";
		        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		        int count = jdbcTemplate.update(query);
		        return (count > 0 ? "true" : "false");
		 
		 }
	
}
