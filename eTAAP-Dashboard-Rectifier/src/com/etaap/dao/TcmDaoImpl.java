package com.etaap.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.etaap.beans.Tcm;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;
import com.etaap.utils.gsonUtils.Gson;

public class TcmDaoImpl implements TcmDao{

	@Autowired
	DataSource dataSource;

	private static final Logger logger = Logger.getLogger(TcmDaoImpl.class);

	private static Map<String,String> tcTypeMap = new HashMap<String,String>();

	static {
		tcTypeMap.put("Automated", "Automated");
		tcTypeMap.put("Manual", "Manual");
	}

	public String getTcmChartString(int app_id, String from_dt, String to_dt) {
		logger.info("Inside TcmDaoImpl :: getTcmChartString()");

		StringBuffer queryBuffer = new StringBuffer("SELECT t.app_id, t.suite_id, t.test_case_type, t.test_case_count, s.suite_name FROM tcm t, test_suite s");
		queryBuffer.append(" WHERE t.app_id = "+app_id);
		queryBuffer.append(" AND t.quarter_start_date = '"+from_dt+ " 00:00:00'");
		queryBuffer.append(" AND t.quarter_end_date = '"+to_dt+ " 23:59:59'");
		queryBuffer.append(" AND t.suite_id = s.suite_id");
		queryBuffer.append(" order by t.suite_id");
		String gson = "";
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Tcm> TcmList = new ArrayList<Tcm>();
			List<Map<String, Object>> TcmRows = jdbcTemplate.queryForList(queryBuffer.toString());
			for (Map<String, Object> tcmRow : TcmRows) {
				Tcm t = new Tcm();
				t.setAppId(Integer.parseInt(String.valueOf(tcmRow.get("app_id"))));
				t.setSuiteId(Integer.parseInt(String.valueOf(tcmRow.get("suite_id"))));
				t.setTestCaseType((String.valueOf(tcmRow.get("test_case_type"))));
				t.setTestCaseCount(Integer.parseInt(String.valueOf(tcmRow.get("test_case_count"))));
				t.setTestSuiteName((String.valueOf(tcmRow.get("suite_name"))));
				TcmList.add(t);
			}
			if (TcmList.size() != 0) {
				List<Tcm> FinalTcmList = new ArrayList<Tcm>(); 
				int i = 0;

				if (TcmList.size() > 1) {
					while (TcmList.size() > i)  {
						Tcm tcm1 = (Tcm) TcmList.get(i);
						Tcm tcm2 = (Tcm) TcmList.get(i+1);

						if (tcm1.getAppId() == tcm2.getAppId() && tcm1.getSuiteId() == tcm2.getSuiteId() && !tcm1.getTestCaseType().equalsIgnoreCase(tcm2.getTestCaseType())) {
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

					FinalTcmList.add(tcm4);
					FinalTcmList.add(tcm5);
				}
				gson = Gson.getGsonString(FinalTcmList);
				//gson = Gson.getGsonString(TcmList);
			}
		} catch (Exception e) {
			logger.error("ERROR :: getTcmChartString() :: " + e.getMessage());
			return null;
		}
		return gson;
	}

	public String getPrevQuaterChartString(int app_id, String to, String prevQuaterEndDate, List<TimePeriod> timePeriodList) {
		logger.info("Inside TcmDaoImpl :: getPrevQuaterChartString()");

		StringBuffer queryBuffer = new StringBuffer("SELECT t.app_id, t.test_case_type, SUM(t.test_case_count) as test_case_count, t.quarter_start_date FROM tcm t");
		queryBuffer.append(" WHERE t.app_id = "+app_id);
		queryBuffer.append(" AND t.quarter_start_date >= '"+prevQuaterEndDate+"'");
		queryBuffer.append(" AND t.quarter_end_date <= '"+to+ " 23:59:59'");
		queryBuffer.append(" GROUP BY t.test_case_type, t.quarter_start_date");
		queryBuffer.append(" ORDER BY t.quarter_start_date, t.test_case_type ");
		String gson = "";
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Tcm> TcmList = new ArrayList<Tcm>();
			List<Map<String, Object>> TcmRows = jdbcTemplate.queryForList(queryBuffer.toString());
			for (Map<String, Object> tcmRow : TcmRows) {
				Tcm t = new Tcm();
				t.setAppId(Integer.parseInt(String.valueOf(tcmRow.get("app_id"))));
				t.setTestCaseType(String.valueOf(tcmRow.get("test_case_type")));
				t.setTestCaseCount(Integer.parseInt(String.valueOf(tcmRow.get("test_case_count"))));
				t.setQuarterStartDate(String.valueOf(tcmRow.get("quarter_start_date")));
				TcmList.add(t);
			}
			if (TcmList.size() != 0) {
				List<Tcm> FinalTcmList = new ArrayList<Tcm>();
				int i = 0;

				if (TcmList.size() > 1) {
					while (TcmList.size() > i)  {
						Tcm tcm1 = (Tcm) TcmList.get(i);
						Tcm tcm2 = (Tcm) TcmList.get(i+1);

						if (tcm1.getAppId() == tcm2.getAppId() && tcm1.getQuarterStartDate().equalsIgnoreCase(tcm2.getQuarterStartDate()) && !tcm1.getTestCaseType().equalsIgnoreCase(tcm2.getTestCaseType())) {
							FinalTcmList.add(tcm1);
							FinalTcmList.add(tcm2);
							i = i + 2;
						} else {
							Tcm tcm3 = new Tcm();
							tcm3.setAppId(tcm1.getAppId());
							tcm3.setTestCaseType(tcm1.getTestCaseType().equalsIgnoreCase(tcTypeMap.get("Automated")) ? tcTypeMap.get("Manual") : tcTypeMap.get("Automated"));
							tcm3.setTestCaseCount(0);
							tcm3.setQuarterStartDate(tcm1.getQuarterStartDate());
							tcm3.setTestSuiteName(tcm1.getTestSuiteName());

							FinalTcmList.add(tcm1);
							FinalTcmList.add(tcm3);
							i = i + 1;
						}

						// to check last record of list
						if (i + 1 == TcmList.size()) {
							Tcm tcm4 = (Tcm) TcmList.get(i);
							Tcm tcm5 = new Tcm();
							tcm5.setAppId(tcm4.getAppId());
							tcm5.setTestCaseType(tcm4.getTestCaseType().equalsIgnoreCase(tcTypeMap.get("Automated")) ? tcTypeMap.get("Manual") : tcTypeMap.get("Automated"));
							tcm5.setTestCaseCount(0);
							tcm5.setQuarterStartDate(tcm4.getQuarterStartDate());
							tcm5.setTestSuiteName(tcm4.getTestSuiteName());

							FinalTcmList.add(tcm4);
							FinalTcmList.add(tcm5);
							break;
						}
					}
				} else {
					Tcm tcm4 = (Tcm) TcmList.get(i);
					Tcm tcm5 = new Tcm();
					tcm5.setAppId(tcm4.getAppId());
					tcm5.setTestCaseType(tcm4.getTestCaseType().equalsIgnoreCase(tcTypeMap.get("Automated")) ? tcTypeMap.get("Manual") : tcTypeMap.get("Automated"));
					tcm5.setTestCaseCount(0);
					tcm5.setQuarterStartDate(tcm4.getQuarterStartDate());
					tcm5.setTestSuiteName(tcm4.getTestSuiteName());

					FinalTcmList.add(tcm4);
					FinalTcmList.add(tcm5);
				}

				TcmList = FinalTcmList;
				FinalTcmList = new ArrayList<Tcm>();
				if (timePeriodList != null) {
					for (int j = 3; j <= timePeriodList.size()-1; j--) {
						if (j < 0)
							break;
						TimePeriod tp = (TimePeriod) timePeriodList.get(j);
						String startDt = tp.getStartDt().toString().indexOf(" ") > 0 ? tp.getStartDt().substring(0, 10): tp.getStartDt();
						prevQuaterEndDate = prevQuaterEndDate.indexOf(" ") > 0 ? prevQuaterEndDate.substring(0, 10): prevQuaterEndDate;
						System.out.println("startDt :: " + startDt);
						if (startDt.equalsIgnoreCase(prevQuaterEndDate)) {
							int ii=0;
							for (int k = j; k <= timePeriodList.size()-1; k--) {
								if (k < 0)
									break;

								tp = (TimePeriod) timePeriodList.get(k);
								startDt = tp.getStartDt().substring(0, 10);
								Tcm tcm6 = null;
								while (ii < TcmList.size()) {
									/*if (ii == TcmList.size()) {
										Tcm tcm = new Tcm();
										tcm.setAppId(app_id);
										tcm.setTestCaseType(tcTypeMap.get("Automated"));
										tcm.setTestCaseCount(0);
										tcm.setQuarterStartDate(startDt);
										tcm.setTestSuiteName("");
										FinalTcmList.add(tcm);

										tcm = new Tcm();
										tcm.setAppId(app_id);
										tcm.setTestCaseType(tcTypeMap.get("Manual"));
										tcm.setTestCaseCount(0);
										tcm.setQuarterStartDate(startDt);
										tcm.setTestSuiteName("");
										FinalTcmList.add(tcm);
										ii = ii + 2;
										break;
									}*/

									tcm6 = (Tcm) TcmList.get(ii);
									if (tcm6.getQuarterStartDate().substring(0, 10).equalsIgnoreCase(startDt)) {
										FinalTcmList.add(tcm6);
										FinalTcmList.add(TcmList.get(ii+1));
										ii = ii + 2;
										break;
									} else {
										Tcm tcm = new Tcm();
										tcm.setAppId(tcm6.getAppId());
										tcm.setTestCaseType(tcTypeMap.get("Automated"));
										tcm.setTestCaseCount(0);
										tcm.setQuarterStartDate(startDt);
										tcm.setTestSuiteName("");
										FinalTcmList.add(tcm);

										tcm = new Tcm();
										tcm.setAppId(tcm6.getAppId());
										tcm.setTestCaseType(tcTypeMap.get("Manual"));
										tcm.setTestCaseCount(0);
										tcm.setQuarterStartDate(startDt);
										tcm.setTestSuiteName("");
										FinalTcmList.add(tcm);
										//ii = ii + 2;
										break;
									}
								}
							}
						}
					}
				}

				gson = Gson.getGsonString(FinalTcmList);
				//gson = Gson.getGsonString(TcmList);
			}
		} catch (Exception e) {
			logger.error("ERROR :: getPrevQuaterChartString() :: " + e.getMessage());
			return null;
		}

		return gson;
	}

	public List<Application> getTcmApplicationList() {
		logger.info("Inside TcmDaoImpl :: getTcmApplicationList()");

		List<Application> appList = new ArrayList<Application>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select a.* from application a, tcm t");
		queryBuffer.append(" where a.app_id = t.app_id");
		queryBuffer.append(" group by a.app_id");
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

        return appList;
	}

	public List<Application> getTcmApplicationList(int app_id) {
		logger.info("Inside TcmDaoImpl :: getTcmApplicationList(int)");

		List<Application> appList = new ArrayList<Application>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuffer queryBuffer = new StringBuffer("select a.* from application a, tcm t");
		queryBuffer.append(" where a.app_id = t.app_id");
		queryBuffer.append(" group by a.app_id");
		queryBuffer.append(" order by (a.app_id = "+app_id+") DESC");
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

        return appList;
	}
}
