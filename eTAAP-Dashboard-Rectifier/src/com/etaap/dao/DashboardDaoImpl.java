package com.etaap.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import com.etaap.domain.CI;
import com.etaap.domain.Defects;
import com.etaap.beans.Tcm;
import com.etaap.utils.Utils;

public class DashboardDaoImpl implements DashboardDao {

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(DashboardDaoImpl.class);

	private static Map<String,String> tcTypeMap = new HashMap<String,String>();

	static {
		tcTypeMap.put("Automated", "Automated");
		tcTypeMap.put("Manual", "Manual");
	}

	public List<Defects> getDetails(HashMap<String, Object> params) {
		logger.info("Inside DashboardDaoImpl :: getDetails() "+params.get("defectCase"));
		boolean flag = false;
		String fromDate = new Utils().getFirstDayOfPrevTwoMonths();

		StringBuffer queryStringBuffer = null;
		List<Map<String, Object>> queryForList = null;
		List<Map<String, Object>> queryForPriorityList = null;
		String priorityName = "";
		Map<String, Object> priorityNameMap = null;
		List<Defects> defectsList = new ArrayList<Defects>();;
		String priorityLabel = "";
		
		try {
			
			System.out.println("inside dao impl" +params);
			queryStringBuffer = new StringBuffer("select ");
			if (params.get("defectCase") != null && params.get("defectCase").equals("priority")) {
				queryStringBuffer.append("a.app_id, a.app_name, j.priority, count(j.priority) count ");
				queryStringBuffer.append("from jira j, application a ");
				queryStringBuffer.append("where j.priority in ( ");
				queryStringBuffer.append("select p.priority_name ");
				queryStringBuffer.append("from priority p, application_system_map asm ");
				queryStringBuffer.append("where p.sys_id = asm.sys_id ");
				queryStringBuffer.append("and p.sys_id in (select sys_id from application_system_map am where am.app_id=a.app_id ) ");
				queryStringBuffer.append("and asm.app_id = a.app_id ");
				queryStringBuffer.append("and asm.is_active = 1 ");
				queryStringBuffer.append("group by p.equivalent_to ) ");
				queryStringBuffer.append("and j.created < current_timestamp and j.created >= '" + fromDate + " 00:00:00' ");
				queryStringBuffer.append("and j.app_id = a.app_id ");
				queryStringBuffer.append("and a.status = 1 ");
				queryStringBuffer.append("group by a.app_id, j.priority ");
				
				System.out.println("firstttt  dao if :: jira :: " + queryStringBuffer.toString() +System.currentTimeMillis());
			}
			else if (params.get("defectCase") != null && params.get("defectCase").equals("severity")) {
				queryStringBuffer.append("a.app_id, a.app_name, j.severity, count(j.severity) count ");
				queryStringBuffer.append("from jira j, application a ");
				queryStringBuffer.append("where j.created < current_timestamp and j.created >= '" + fromDate + " 00:00:00' ");
				queryStringBuffer.append("and j.app_id = a.app_id ");
				queryStringBuffer.append("and a.status = 1 ");
				queryStringBuffer.append("group by a.app_id, j.severity ");
				
				System.out.println("first dao else :: jira :: " + queryStringBuffer.toString() +System.currentTimeMillis());
			}

		    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());
			
			if (params.get("defectCase") != null && params.get("defectCase").equals("priority")) {
				int app_id = 0;
				String priority1Name = "";
				String priority2Name = "";
				
				if(queryForList.size() >= 0 )
				{ 
					Map<String, Object> priorityMap = queryForList.get(0);
					app_id = (Integer) priorityMap.get("app_id");

					queryStringBuffer = new StringBuffer("select a.app_id, p.priority_name ");
					queryStringBuffer.append("from priority p, application_system_map asm, application a ");
					queryStringBuffer.append("where p.sys_id = asm.sys_id  ");
					queryStringBuffer.append("and asm.app_id = a.app_id ");
					queryStringBuffer.append("and asm.is_active = 1 ");
					queryStringBuffer.append("and a.app_id = "+app_id);
					queryStringBuffer.append(" group by p.equivalent_to , p.priority_name ");
					
					System.out.println("first dao after else :: jira :: " + queryStringBuffer.toString());
					
					jdbcTemplate = new JdbcTemplate(dataSource);
					queryForPriorityList = jdbcTemplate.queryForList(queryStringBuffer.toString());
					
					priorityNameMap = queryForPriorityList.get(0);
					priority1Name = (String) priorityNameMap.get("priority_name");
				
					priorityNameMap = queryForPriorityList.get(1);
					priority2Name = (String) priorityNameMap.get("priority_name");
					
					priorityLabel = priority1Name + " + " +priority2Name;
					
				}
			}
			
			
			if (params.get("defectCase") != null && params.get("defectCase").equals("severity")) {
			
				if(queryForList.size() >= 0 ){
					for (int i = 0; i<queryForList.size(); i++) 
					{ 
						Map<String, Object> severityMap = queryForList.get(i);
						String severity = (String) severityMap.get("severity");
						
						
						if(severity.equals("") || severity == null){
							flag = true;
							break;
						}else { 
							flag = false;
						}
					
					}
				}
				
				if(flag){
					queryStringBuffer = new StringBuffer("select ");
					queryStringBuffer.append("a.app_id, a.app_name, j.priority, count(j.priority) count ");
					queryStringBuffer.append("from jira j, application a ");
					queryStringBuffer.append("where j.created < current_timestamp and j.created >= '" + fromDate + " 00:00:00' ");
					queryStringBuffer.append("and j.app_id = a.app_id ");
					queryStringBuffer.append("and a.status = 1 ");
					queryStringBuffer.append("group by a.app_id, j.priority ");
					
					System.out.println("dao after after :: jira :: " + queryStringBuffer.toString());
					
					
					jdbcTemplate = new JdbcTemplate(dataSource);
					queryForList = jdbcTemplate.queryForList(queryStringBuffer.toString());
					
					for (Map<String, Object> sysRow : queryForList) {
						Defects defects = new Defects();
						defects.setAppId(Integer.parseInt(sysRow.get("app_id").toString()));
						defects.setProjectName(sysRow.get("app_name").toString());
						
						if(sysRow.get("severity") != null){
						defects.setSeverity(sysRow.get("severity").toString());
						defects.setSeverityCount(Integer.parseInt(sysRow.get("count").toString()));
						}
						else if(sysRow.get("priority") != null){
						defects.setPriority(sysRow.get("priority").toString());
						defects.setPriorityCount(Integer.parseInt(sysRow.get("count").toString()));
						}
						defectsList.add(defects);
					}
				}
				}
		
			if(!flag){
			for (Map<String, Object> sysRow : queryForList) {
				Defects defects = new Defects();
				if (params.get("defectCase") != null && params.get("defectCase").equals("priority")) {
					
					defects.setAppId(Integer.parseInt(sysRow.get("app_id").toString()));
					defects.setProjectName(sysRow.get("app_name").toString());
					defects.setPriority(sysRow.get("priority").toString());
					defects.setPriorityCount(Integer.parseInt(sysRow.get("count").toString()));
					defects.setPriorityLabel(priorityLabel);
				}
				else if (params.get("defectCase") != null && params.get("defectCase").equals("severity")  && !flag) {
					defects.setAppId(Integer.parseInt(sysRow.get("app_id").toString()));
					defects.setProjectName(sysRow.get("app_name").toString());
					defects.setSeverity(sysRow.get("severity").toString());
					defects.setSeverityCount(Integer.parseInt(sysRow.get("count").toString()));
				}
				defectsList.add(defects);
				System.out.println("defectslist :: jira :: " + defectsList.size());
			}
			}
			
		} catch (Exception e) {
			logger.error("ERROR :: getDetails() :: " + e.getMessage());
		}

		return defectsList;
	}

	public List<CI> getJenkinsDetails(HashMap<String, Object> param) {
		logger.info("Inside DashboardDaoImpl :: getJenkinsDetails()");

		String fromDate = new Utils().getFirstDayOfPrevTwoMonths();
		List<CI> ciList = new ArrayList<CI>();

		try {
			/*StringBuffer queryStringBuffer = new StringBuffer("select a.app_id, a.app_name, count(*) build_count from jenkins j, application a ");
			queryStringBuffer.append("where j.app_id = a.app_id ");
			queryStringBuffer.append("and j.build_date between '" + fromDate + " 00:00:00' and current_timestamp ");
			queryStringBuffer.append("group by j.app_id ");*/

			StringBuffer queryStringBuffer = new StringBuffer("select temp.app_name, je.* from jenkins je, (select app.app_name, app.status, app_map.* from application app, application_system_map app_map where app.app_id=app_map.app_id and app_map.is_default=1 and sys_id in (select sys_id from system_api where api_id=1)) temp ");
			queryStringBuffer.append("where je.app_id = temp.app_id ");
			queryStringBuffer.append("and je.env_id = temp.env_id ");
			queryStringBuffer.append("and je.suite_id = temp.suite_id ");
			queryStringBuffer.append("and je.bed_id = temp.bed_id ");
			queryStringBuffer.append("and je.build_date = (select max(build_date) from jenkins j where temp.app_id=j.app_id and temp.env_id=j.env_id and temp.suite_id=j.suite_id and temp.bed_id=j.bed_id and j.build_date < current_timestamp and j.build_date > '" + fromDate + " 00:00:00' order by j.build_date desc) ");
			queryStringBuffer.append("and temp.status = 1 ");
			queryStringBuffer.append("order by je.app_id ");

			logger.info("Inside DashboardDaoImpl :: getJenkinsDetails() :: " + queryStringBuffer.toString());
			
			System.out.println("Inside DashboardDaoImpl :: getJenkinsDetails() :: " + queryStringBuffer.toString());

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

	        List<Map<String,Object>> ciRows = jdbcTemplate.queryForList(queryStringBuffer.toString());
	        for (Map<String,Object> ciRow : ciRows) {
	        	CI ci = new CI();
	        	ci.setAppId(Integer.parseInt(String.valueOf(ciRow.get("app_id"))));
	        	ci.setAppName(String.valueOf(ciRow.get("app_name")));
	        	//ci.setBuildCount(Integer.parseInt(String.valueOf(ciRow.get("build_count"))));
	        	ci.setFailCount(Integer.parseInt(String.valueOf(ciRow.get("fail_count"))));
	        	ci.setPassCount(Integer.parseInt(String.valueOf(ciRow.get("pass_count"))));
	        	ci.setSkipCount(Integer.parseInt(String.valueOf(ciRow.get("skip_count"))));

	        	ciList.add(ci);
	        }
		} catch(Exception e) {
			logger.error("ERROR :: getJenkinsDetails() :: " + e.getMessage());
		}

		return ciList;
	}
	
	public List<Tcm> getTcmDashBoardChartString() {
		logger.info("Inside DashboardDaoImpl :: getTcmChartString()");

		String fromDate = new Utils().getFirstDayOfPrevTwoMonths();
		StringBuffer queryBuffer = new StringBuffer("SELECT t.app_id, t.suite_id, t.test_case_type, SUM(t.test_case_count) as test_case_count, a.app_name FROM tcm t, application a ");
		queryBuffer.append(" WHERE t.quarter_start_date >= '"+fromDate+" 00:00:00'");
		queryBuffer.append(" AND t.app_id = a.app_id");
		queryBuffer.append(" AND a.status = 1");
		queryBuffer.append(" GROUP BY t.app_id, t.test_case_type, t.suite_id;");

		List<Tcm> TcmList = new LinkedList<Tcm>();
		List<Tcm> FinalTcmList = new ArrayList<Tcm>();
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		    List<Map<String,Object>> TcmRows = jdbcTemplate.queryForList(queryBuffer.toString());
	        for (Map<String,Object> tcmRow : TcmRows) {
	        	Tcm t = new Tcm();
	        	t.setAppId(Integer.parseInt(String.valueOf(tcmRow.get("app_id"))));
	        	t.setSuiteId(Integer.parseInt(String.valueOf(tcmRow.get("suite_id"))));
	        	t.setTestCaseType((String.valueOf(tcmRow.get("test_case_type"))));
	        	t.setTestCaseCount(Integer.parseInt(String.valueOf(tcmRow.get("test_case_count"))));
	        	t.setAppName((String.valueOf(tcmRow.get("app_name"))));
	        	TcmList.add(t);
	        }

	        if (TcmList.size() != 0) {
				int i = 0;
				if (TcmList.size() > 1) {
					while (TcmList.size() > i)  {
						Tcm tcm1 = (Tcm) TcmList.get(i);
						Tcm tcm2 = (Tcm) TcmList.get(i+1);

						if (tcm1.getAppId() == tcm2.getAppId() && !tcm1.getTestCaseType().equalsIgnoreCase(tcm2.getTestCaseType())) {
							FinalTcmList.add(tcm1);
							FinalTcmList.add(tcm2);
							i = i + 2;
						} else {
							Tcm tcm3 = new Tcm();
							tcm3.setAppId(tcm1.getAppId());
							tcm3.setSuiteId(tcm1.getSuiteId());
							tcm3.setTestCaseType(tcm1.getTestCaseType().equalsIgnoreCase(tcTypeMap.get("Automated")) ? tcTypeMap.get("Manual") : tcTypeMap.get("Automated"));
							tcm3.setTestCaseCount(0);
							tcm3.setTestSuiteName(tcm1.getTestSuiteName());
							tcm3.setAppName(tcm1.getAppName());

							FinalTcmList.add(tcm1);
							FinalTcmList.add(tcm3);
							i = i + 1;
						}

						// to check last record of list
						if (i + 1 == TcmList.size()) {
							Tcm tcm4 = (Tcm) TcmList.get(i);
							Tcm tcm5 = new Tcm();
							tcm5.setAppId(tcm4.getAppId());
							tcm5.setSuiteId(tcm4.getSuiteId());
							tcm5.setTestCaseType(tcm4.getTestCaseType().equalsIgnoreCase(tcTypeMap.get("Automated")) ? tcTypeMap.get("Manual") : tcTypeMap.get("Automated"));
							tcm5.setTestCaseCount(0);
							tcm5.setTestSuiteName(tcm4.getTestSuiteName());
							tcm5.setAppName(tcm4.getAppName());

							FinalTcmList.add(tcm4);
							FinalTcmList.add(tcm5);
							break;
						}
					}
				} else {
					Tcm tcm4 = (Tcm) TcmList.get(i);
					Tcm tcm5 = new Tcm();
					tcm5.setAppId(tcm4.getAppId());
					tcm5.setSuiteId(tcm4.getSuiteId());
					tcm5.setTestCaseType(tcm4.getTestCaseType().equalsIgnoreCase(tcTypeMap.get("Automated")) ? tcTypeMap.get("Manual") : tcTypeMap.get("Automated"));
					tcm5.setTestCaseCount(0);
					tcm5.setTestSuiteName(tcm4.getTestSuiteName());
					tcm5.setAppName(tcm4.getAppName());

					FinalTcmList.add(tcm4);
					FinalTcmList.add(tcm5);
				}
			}
		} catch (Exception e) {
			logger.error("ERROR :: getTcmDashBoardChartString() :: " + e.getMessage());
		}

		if (FinalTcmList.size() != 0) {
			return FinalTcmList;
		} else {
			return TcmList;
		}
	}

	@Override
	public List<Map<String,Object>> getDefectsLife(Map params) {
		// TODO Auto-generated method stub
		logger.info("Inside DashboardDaoImpl :: getDefectsLife() "+params.get("defectCase"));
		String severity = "";
		List<Map<String, Object>> defectsList1 = null;
		try {
		//	if(params.get("defectCase") != null && params.get("defectCase").equals("severity")){
				StringBuffer queryBuffer = new StringBuffer("select count(j.severity) as severityCount,j.severity from jira j");
				if (params != null && params.size() > 0) {
					queryBuffer.append(" where ");
	
					/*if (params.get("status") != null) {
						queryBuffer.append(" j.status='" + params.get("status").toString() + "' ");
					}*/
					queryBuffer.append("j.status in ( ");
					queryBuffer.append("select s.status_name  ");
					queryBuffer.append("from status s ");
					queryBuffer.append("where s.equivalent_to in ('Verify','New','In Progress')  ");
					queryBuffer.append("group by s.equivalent_to ) ");
	
					if (params.get("date") != null) {
						queryBuffer.append(" and j.created >= '" + params.get("date").toString() + " 00:00:00' and " + "j.created <= '" + params.get("date").toString() + " 23:59:59'");
					}
	
					if (params.get("from") != null) {
						if (params.get("to") != null)
							queryBuffer.append(" and j.created >= '" + params.get("from").toString() + "' and j.created <= '" + params.get("to").toString() + "'");
						else
							queryBuffer.append(" and j.created = " + params.get("from").toString());
					}
				}
				queryBuffer.append(" group by j.severity");
				String finalQueryString = queryBuffer.toString();
	
				logger.info("Inside DashboardDaoImpl :: getDefectsLife() :: " + finalQueryString);
	
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				defectsList1 = jdbcTemplate.queryForList(finalQueryString);
				logger.info("Inside DashboardDaoImpl :: defectsList1() :: size :: " + defectsList1.size());
				
				
				if (defectsList1 != null ){
					
					for (int k =0; k < defectsList1.size(); k++ ){
						
						Map sevirityMap = defectsList1.get(k);
						severity = sevirityMap.get("severity").toString();
						logger.info("Inside DashboardDaoImpl :: defectsList1() :: severity :: " + severity);
						if(severity == null || severity.equals("")){
							break;
						}
					}
				}
				if(severity == null || severity.equals("")){
					logger.info("Inside DashboardDaoImpl :: defectsList1() :: severity is null :: " + severity);
					queryBuffer = new StringBuffer("select count(j.priority) as priorityCount,j.priority from jira j");
					if (params != null && params.size() > 0) {
						queryBuffer.append(" where ");
		
						/*if (params.get("status") != null) {
							queryBuffer.append(" j.status='" + params.get("status").toString() + "' ");
						}*/
						queryBuffer.append("j.status in ( ");
						queryBuffer.append("select s.status_name  ");
						queryBuffer.append("from status s ");
						queryBuffer.append("where s.equivalent_to in ('Verify','New','In Progress')  ");
						queryBuffer.append("group by s.equivalent_to ) ");
		
						if (params.get("date") != null) {
							queryBuffer.append(" and j.created >= '" + params.get("date").toString() + " 00:00:00' and " + "j.created <= '" + params.get("date").toString() + " 23:59:59'");
						}
		
						if (params.get("from") != null) {
							if (params.get("to") != null)
								queryBuffer.append(" and j.created >= '" + params.get("from").toString() + "' and j.created <= '" + params.get("to").toString() + "'");
							else
								queryBuffer.append(" and j.created = " + params.get("from").toString());
						}
					}
					queryBuffer.append(" group by j.priority");
					 finalQueryString = queryBuffer.toString();
		
					System.out.println("Inside DashboardDaoImpl :: getDefectsLife() :: " + finalQueryString);
		
					 jdbcTemplate = new JdbcTemplate(dataSource);
					defectsList1 = jdbcTemplate.queryForList(finalQueryString);
					System.out.println("Inside DashboardDaoImpl :: defectsList1() :: " + defectsList1.size());
				}
			
			//}
		} catch (Exception e) {
			logger.error("ERROR :: getDefectsLife() :: " + e.getMessage());
		}

		return defectsList1;
	}

	@Override
	public List<Map<String, Object>> getDefectsStatistics(Map<String, Object> params) {
		// TODO Auto-generated method stub
		logger.info("Inside DashboardDaoImpl :: getDefectsStatistics()");

		List<Map<String, Object>> defectsStatictics = null;
		try {
			StringBuffer queryStringBuffer = new StringBuffer("select count(j.status) as status_count,j.status,EXTRACT(MONTH FROM j.created) AS OrderMonth from jira j");
			/*if (params.get("statusIn") != null) {
				queryStringBuffer.append(" where j.status in (");
				Object object = params.get("statusIn");
				if (object instanceof java.util.List) {
					java.util.List<String> list = (List<String>) object;
					for (int i = 0; i < list.size(); i++) {
						queryStringBuffer.append("'" + list.get(i) + "'");
						if (!(list.size() != 0 && (i == list.size() - 1))) {
							queryStringBuffer.append(",");
						}
					}
					queryStringBuffer.append(")");
				} else {
					queryStringBuffer.append("'" + params.get("statusIn").toString() + "')");
				}
			}*/
			
			queryStringBuffer.append(" where j.status in (");
			queryStringBuffer.append(" select s.status_name ");
			queryStringBuffer.append(" from status s ");
			queryStringBuffer.append(" where s.equivalent_to in ('Closed','New','In Progress') ");
			queryStringBuffer.append(" group by s.equivalent_to ) ");
			
			if (params.get("from") != null && params.get("to") != null) {
				queryStringBuffer.append(" and j.created >= '" + params.get("from").toString() + "' and j.created <= '" + params.get("to").toString() + "'");
			}
			queryStringBuffer.append(" group by OrderMonth asc,j.status");
			String finalQueryString = queryStringBuffer.toString();

			System.out.println("Inside DashboardDaoImpl :: getDefectsStatistics() :: " + finalQueryString);

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			defectsStatictics = jdbcTemplate.queryForList(finalQueryString);
		} catch (Exception e) {
			logger.error("ERROR :: getDefectsStatistics() :: " + e.getMessage());
		}

		return defectsStatictics;
	}	
	
	/**@return Map<String, Object>>
	 *                   |
	 *         Key=Application1, Value=Map
	 *                   |              |
	 *                   |        Key=SprintName1, Value=Map
	 *                   |                                |
	 *                   |                         Key=Commited Points, Value=Number Of Points
	 *                   |                         Key=Completed Points, Value=Number Of Points
	 *                   |
	 *                   |        Key=SprintName2, Value=Map                   
	 *                   |                                |
	 *                   |                         Key=Commited Points, Value=Number Of Points
	 *                   |                         Key=Completed Points, Value=Number Of Points
	 *                   |             .
	 *                   |             .
	 *         Key=Application2, Value=Map
	 *                   |              |
	 *                   |        Key=SprintName3, Value=Map
	 *                   |                                |
	 *                   |                         Key=Commited Points, Value=Number Of Points
	 *                   |                         Key=Completed Points, Value=Number Of Points
	 *                   |
	 *                   |        Key=SprintName4, Value=Map                   
	 *                   |                                |
	 *                   |                         Key=Commited Points, Value=Number Of Points
	 *                   |                         Key=Completed Points, Value=Number Of Points
	 *                   |             .
	 *                   |             .
	 * @throws  
	 *                                 
	 * */
	@Override
	public Map<String, Object> getCommitedCompletedUserStories(Map<String, Object> params) {
		LinkedHashMap<String, Object> retMap = new LinkedHashMap<String, Object>();
		ArrayList<Integer> sprintIdList = new ArrayList<Integer>(); 
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		//Get last three applications
//		String lastApplicationsListSql = "select app_id from application order by created_dt DESC limit 3";
//		String lastApplicationsListSql = "select distinct a.app_id from application a, jira_sprint js where a.app_id = js.app_id order by js.start_date DESC limit 3";
		String lastApplicationsListSql = "select distinct jv.app_id, js.start_date from jira_velocity jv, application a, jira_sprint js  "
				+ " where a.app_id = js.app_id "
				+ " and jv.app_id = js.app_id "
				+ " and js.sprint_id = jv.sprint_id "
				+ " order by js.start_date DESC limit 3";
		System.out.println("XXXXXXXXXXCCCCCCCCCC "+lastApplicationsListSql);

		List<Map<String, Object>> lastApplications = jdbcTemplate.queryForList(lastApplicationsListSql);
		System.out.println("XXXXXXXXXXCCCCCCCCCC "+lastApplications);
		//Get last three sprints of selected applications.
		for(int i = 0; i < lastApplications.size(); i++) {
			Map appMap = (Map)lastApplications.get(i);
			int app_id = (Integer)appMap.get("app_id");
//			String lastSprintsListSql = "select id from jira_sprint where app_id = "+app_id+" order by start_date DESC limit 3";
//			String lastSprintsListSql = "select id from jira_sprint where app_id = "+app_id+" order by start_date DESC limit 3";
//			String lastSprintsListSql = "select js.id, js.sprint_id, js.rapidview_id from jira_sprint js, jira_velocity jv "+
			String lastSprintsListSql = "select js.id from jira_sprint js, jira_velocity jv "+
			"where js.app_id = "+app_id+
			" and js.app_id = jv.app_id "+
			" and js.sprint_id = jv.sprint_id "+
			" and js.rapidview_id = jv.rapidview_id "+
			" order by js.start_date DESC limit 3 ";
			System.out.println("XXXXXXXXXXCCCCCCCCCC Sprints for app "+app_id+"="+lastSprintsListSql);
			
			List<Map<String, Object>> lastSprints = jdbcTemplate.queryForList(lastSprintsListSql);
			System.out.println("XXXXXXXXXXCCCCCCCCCC Sprints for app "+app_id+"="+lastSprints);
			for(int j = 0; j < lastSprints.size(); j++) {
				Map sprintMap = (Map)lastSprints.get(j);
				int id = (Integer)sprintMap.get("id");
				//Add all id of jira_sprint into array.
				sprintIdList.add(id);
			}
		}
		
		if(sprintIdList.size() > 0) {
			//Get comma separated list of id(s)
			String commaSepartedIds = StringUtils.collectionToCommaDelimitedString(sprintIdList);
			String requiredRecords = "select a.app_name, js.sprint_name, js.start_date, js.end_date,"+ 
			"jv.estimated, jv.completed from application a, jira_sprint js, jira_velocity jv "+
			" where js.app_id = a.app_id "+
			" and a.app_id = jv.app_id "+
			" and js.sprint_id = jv.sprint_id "+
			" and js.id IN ("+commaSepartedIds+")"+
			" order by js.start_date DESC ";
			
			System.out.println("XXXXXXXXXXCCCCCCCCCC "+requiredRecords);
			
			List<Map<String, Object>> recordsForGraph = jdbcTemplate.queryForList(requiredRecords);
			for(Map rec: recordsForGraph) {
				String appName = (String)rec.get("app_name");
				String sprintName = (String)rec.get("sprint_name");
//				sprintName = StringEscapeUtils.escapeHtml3(sprintName);
				String startDate = (String)rec.get("start_date");
				String endDate = (String)rec.get("end_date");
				String commited = String.valueOf(rec.get("estimated"));
				String completed = String.valueOf(rec.get("completed"));
				
				startDate = Utils.getDate(startDate, "yyyy-MM-dd", "dd MMM");
				endDate = Utils.getDate(endDate, "yyyy-MM-dd", "dd MMM");
				
				System.out.println("XXXXXXXXXXCCCCCCCCCC "+appName+","+sprintName+","+startDate+","+
						endDate+","+commited+","+completed);
				
				if(retMap.containsKey(appName)) {
					Map<String, Map> sprintMap = (Map<String, Map>)retMap.get(appName);
					Map<String, String> pointMap = new HashMap<String, String>();
					pointMap.put("Commited", commited);
					pointMap.put("Completed", completed);
					pointMap.put("StartDate", startDate);
					pointMap.put("EndDate", endDate);
					sprintMap.put(sprintName, pointMap);
					System.out.println("Added Sprint when application available "+sprintName);
				}
				else {
					Map<String, Map> sprintMap = new LinkedHashMap<String, Map>();
					Map<String, String> pointMap = new HashMap<String, String>();
					pointMap.put("Commited", commited);
					pointMap.put("Completed", completed);
					pointMap.put("StartDate", startDate);
					pointMap.put("EndDate", endDate);
					sprintMap.put(sprintName, pointMap);
					System.out.println("Added Sprint when application not available "+sprintName);
					retMap.put(appName, sprintMap);
				}
			}
		}
		System.out.println("XXXXXXXXXXCCCCCCCCCC "+retMap);
		return retMap;
	}	
}
