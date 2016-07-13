package com.etaap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.etaap.beans.ChartModel;
import com.etaap.beans.CommitedCompletedUserStories;
import com.etaap.beans.Data;
import com.etaap.beans.DefectsLife;
import com.etaap.beans.DefectsStatistics;
import com.etaap.beans.Environments;
import com.etaap.beans.Priority;
import com.etaap.beans.Series;
import com.etaap.beans.Severity;
import com.etaap.beans.Sprint;
import com.etaap.beans.Tcm;
import com.etaap.beans.TestSuits;
import com.etaap.beans.Testbed;
import com.etaap.common.ApplicationConstants;
import com.etaap.dao.ApplicationDaoImpl;
import com.etaap.domain.Application;
import com.etaap.domain.CI;
import com.etaap.domain.Defects;
import com.etaap.domain.Environment;
import com.etaap.domain.SystemAPI;
import com.etaap.domain.TestBed;
import com.etaap.domain.TestCase;
import com.etaap.domain.TestSuite;
import com.etaap.domain.TimePeriod;
import com.etaap.mail.SendMailServer;
import com.etaap.security.EncryptionDecryptionAES;
import com.etaap.services.ApplicationService;
import com.etaap.services.CIService;
import com.etaap.services.ChartService;
import com.etaap.services.DashboardService;
import com.etaap.services.EnvironmentService;
import com.etaap.services.IterationsChartService;
import com.etaap.services.SystemAPIService;
import com.etaap.services.TcmServiceImpl;
import com.etaap.services.TestBedService;
import com.etaap.services.TestCaseService;
import com.etaap.services.TestSuiteService;
import com.etaap.services.TimePeriodService;
import com.etaap.services.VelocityServiceImpl;
import com.etaap.utils.Utils;
import com.etaap.utils.enums.ApplicationColor;
import com.etaap.utils.enums.ChartType;
import com.etaap.utils.enums.PriorityColor;
import com.etaap.utils.enums.SeverityColor;

@Controller
public class MainController {

	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	@Autowired
	@Qualifier("env")
	private EnvironmentService environmentService;

	@Autowired
	@Qualifier("testBed")
	private TestBedService testBedService;

	@Autowired
	@Qualifier("testSuite")
	private TestSuiteService testSuiteService;

	@Autowired
	@Qualifier("timePeriodService")
	private TimePeriodService timePeriodService;

	@Autowired
	@Qualifier("systemAPI")
	private SystemAPIService systemAPIService;

	@Autowired
	@Qualifier("ciService")
	private CIService ciService;

	@Autowired
	@Qualifier("testCaseService")
	private TestCaseService testCaseService;

	@Autowired
	@Qualifier("dashboardService")
	private DashboardService dashboardService;

	@Autowired
	TcmServiceImpl tcmServiceImpl;

	@Autowired
	VelocityServiceImpl velocityService;
	
	@Autowired
	ChartService chartService;

	private static final Logger logger = Logger.getLogger(MainController.class);

	@Autowired
	private ApplicationContext appContext;
	
	//@Autowired
	ServletContext servletContext;
	
	@Autowired
	private IterationsChartService iterationsChartService;
	
	@Autowired
	private ApplicationDaoImpl applicationDaoImpl;
	
    static boolean initFlag = false;

    @PostConstruct
    public void init() /*throws ClassNotFoundException, IOException*/ {
    	if(!initFlag) {
    		initFlag = true;
                try {
                    EncryptionDecryptionAES.initialiseSecretKey();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
    	}
    }

//	@RequestMapping(value = "/app/{appId}/{appName}/*.html", method = RequestMethod.POST)
	//Code changes required into this method if jenkins_dev is required to implement.
	@RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JENKINS_QA+"/*.html", method = RequestMethod.POST)
	@ResponseBody

	public String getCsiChart(@PathVariable String appId, @RequestParam("from") String from,
//			@RequestParam("to") String to, @RequestParam("mapId") String mapId,
			@RequestParam("to") String to, @RequestParam("envId") String envId, 
			@RequestParam("suiteId") String suiteId,
			@RequestParam("bedId") String bedId,
			Model model) {
		String jsonString = null;

		String map_Id = null;
		String _from = from;
		String _to = to;
		String env_Id = null;
		String suite_Id = null;
		String bed_Id = null;
		String app_Id = null;

		try {
			System.out.println("**********&*&*&*&**** CSIChart*****");
			
/*			List<Map<String, Object>> applicationSystemMap = applicationService
					.getApplicationSystemMapBasedOnMapId(map_Id);
*/			
			List<Map<String, Object>> applicationSystemMap = applicationService
					.getApplicationSystemMap(appId, envId, suiteId, bedId);
			
			
//			List<Map<String, Object>> applicationSystemMap = applicationService.getApplicationSystemMapBasedOnMapId(map_Id);

			if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
				env_Id = applicationSystemMap.get(0).get("env_id").toString();
				suite_Id = applicationSystemMap.get(0).get("suite_id").toString();
				bed_Id = applicationSystemMap.get(0).get("bed_id").toString();
				app_Id = applicationSystemMap.get(0).get("app_id").toString();

				HashMap<String, String> filterOptions = new HashMap<String, String>();
				filterOptions.put("appId", app_Id);
				filterOptions.put("envId", env_Id);
				filterOptions.put("suiteId", suite_Id);
				filterOptions.put("bedId", bed_Id);
				filterOptions.put("periodStrtDt", _from);
				filterOptions.put("periodEndDt", _to);

				List<CI> ciDetails = ciService.getDetails(filterOptions);

				HashMap<String, Object> csvForCIChart = getCsvForCIChart(ciDetails);
				Data dataMap = new Data();
				// dataMap.getData().put("Environments", envs);
				com.etaap.beans.CI ci = new com.etaap.beans.CI();
				ci.setFailCountCSV((List<Integer>) csvForCIChart.get("failCountCSV"));
				ci.setPassCountCSV((List<Integer>) csvForCIChart.get("passCountCSV"));
				ci.setSkipCountCSV((List<Integer>) csvForCIChart.get("skipCountCSV"));
				ci.setBuildNumberCSV((List<Integer>) csvForCIChart.get("buildNumberCSV"));
				ci.setBuildDateCSV((List<Integer>) csvForCIChart.get("buildDateCSV"));
				dataMap.getData().put("CI", ci);
				jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
			}
		} catch (Exception errors) {
			errors.printStackTrace();
			logger.error("ERROR :: " + errors.getMessage());
		}
		return jsonString;
	}
	
	@RequestMapping(value = "/app/{appId}/" + ApplicationConstants.JENKINS_QA_REDESIGN + "/*.html", method = RequestMethod.POST)
	@ResponseBody

	public String getCIResultsDataByQuarter(@PathVariable String appId, @RequestParam("from") String from,
			@RequestParam("to") String to, @RequestParam("envId") String envId, 
			@RequestParam("suiteId") String suiteId,
			@RequestParam("bedId") String bedId,
			Model model) {
		
		String jsonString = null;
		String env_Id = null;
		String suite_Id = null;
		String bed_Id = null;
		String app_Id = null;

		try {
			List<Map<String, Object>> applicationSystemMap = applicationService
					.getApplicationSystemMap(appId, envId, suiteId, bedId);

			if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
				env_Id = applicationSystemMap.get(0).get("env_id").toString();
				suite_Id = applicationSystemMap.get(0).get("suite_id").toString();
				bed_Id = applicationSystemMap.get(0).get("bed_id").toString();
				app_Id = applicationSystemMap.get(0).get("app_id").toString();
				
				List<ChartModel> chartsList = new ArrayList<ChartModel>();
				chartsList.add(new ChartModel("eTAAPCIResults", ChartType.COLUMN, 
						Arrays.asList(new String[]{"Passed","Failed","Skipped"}),
						new Object[] {app_Id, env_Id, suite_Id, bed_Id, from + " 00:00:00", to + " 23:59:59", 
							app_Id, env_Id, suite_Id, bed_Id}));
				
				jsonString = chartService.getCharts(servletContext, chartsList);
			}
		} catch (Exception errors) {
			errors.printStackTrace();
			logger.error("ERROR :: " + errors.getMessage());
		}
		
		return jsonString;
	}

	public String getDataString(String app_Id, String envId, String suiteId,
			String bedId, List<Environment> environmentList,
				List<TestSuite> testSuiteList, List<TestBed> testBedList,
				HashMap<String, Object> csvForCIChart, Model model) {
			String dataString = null;
			Map paramsMap = new HashMap();
			try {
				
//				System.out.println("***&*&*&**********&*&*&***** getDataString "+envId+","+suiteId+","+bedId);
//				System.out.println("***&*&*&**********&*&*&***** getDataString "+environmentList+","+testSuiteList+","+testBedList);
				
				 List<Application> applicationMapDetails = applicationService
				  .getApplicationMapDetails(Integer.parseInt(app_Id));
				 
				List<Map<String, Object>> applicationSystemMapServiceList = applicationService
						.getApplicationSystemMapService(app_Id,com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
				//Start New
//				Map<String, Map> envMap = new LinkedHashMap<String, Map>();
				
				Map<String, Object> envMap = new LinkedHashMap<String, Object>();
				//End New
				
				String envNameFirst = null;
				String suiteNameFirst = null;
				String bedNameFirst = null;
//				String mapIdFirst = null;
				
				//Start New
//				Map<String, Map> suiteMapFirst = new LinkedHashMap<String, Map>();
				Map<String, Object> suiteMapFirst = new LinkedHashMap<String, Object>();
				
				//Start New
//				Map<String, String> bedMapFirst = new LinkedHashMap<String, String>();
				Map<String, Object> bedMapFirst = new LinkedHashMap<String, Object>();
				//End New
				
				//Start New
				Map<String, Object> envMapFirst = new LinkedHashMap<String, Object>();
				//End New
				
				ArrayList suiteListFirst = new ArrayList();
				
				
				paramsMap.put("app_id", Integer.parseInt(app_Id));
				if (envMap.size() == 0) {
					for (Iterator<Map<String, Object>> itr = applicationSystemMapServiceList
							.iterator(); itr.hasNext();) {
						Map row = itr.next();
						if (row.get("env_id").toString().equals(envId)) {
							if (envNameFirst == null) {
								for (Environment envVar : environmentList) {
									if (envVar.getEnvId() == Integer.parseInt(row
											.get("env_id").toString())) {
										envNameFirst = envVar.getEnvName();
										paramsMap.put("env_id", envVar.getEnvId());
									}
								}
							}
							if (row.get("suite_id").toString().equals(suiteId)) {
								if (suiteNameFirst == null) {
									for (TestSuite testSuite : testSuiteList) {
										if (testSuite.getSuiteId() == Integer
												.parseInt(row.get("suite_id")
														.toString())) {
											suiteNameFirst = testSuite
													.getSuiteName();
											paramsMap.put("suite_id", testSuite.getSuiteId());
											break;
										}
									}
								}
							}
							if (row.get("bed_id").toString().equals(bedId)) {
								if (bedNameFirst == null) {
									for (TestBed testBed : testBedList) {
										if (testBed.getBedId() == Integer
												.parseInt(row.get("bed_id")
														.toString())) {
											bedNameFirst = testBed.getBedName();
	/*										mapIdFirst = row.get("map_id")
													.toString();
	*/										paramsMap.put("bed_id", testBed.getBedId());
											break;
										}
									}
								}
			
							}
							if (envNameFirst != null && suiteNameFirst != null
									&& bedNameFirst != null) {
								// check for record in jenkins against -- app_id,env_id,suiteId,bedId
								
									if(ciService.isRecordAvail(paramsMap)) {
										//Start New
										bedMapFirst.put(String.valueOf(paramsMap.get("bed_id")), bedNameFirst);
//										bedMapFirst.put("ID", ((Integer)paramsMap.get("bed_id")));
										//End New
										
										suiteMapFirst.put(suiteNameFirst, bedMapFirst);
										suiteMapFirst.put("ID", ((Integer)paramsMap.get("suite_id")).toString());
										suiteMapFirst.put("NAME", suiteNameFirst);
										
										suiteListFirst.add(suiteMapFirst);
												
										envMapFirst.put(envNameFirst, suiteListFirst);
										envMapFirst.put("ID", ((Integer)paramsMap.get("env_id")).toString());
										envMapFirst.put("NAME", envNameFirst);
										
										envMap.put(envNameFirst, envMapFirst);
									}
								
								itr.remove();
								break;
							}
						}
					}
					
//					System.out.println("*****&&&&&&&&&****First Enviornment Map** "+envMap);
//					System.out.println("*****&&&&&&&&&****First Parameters Map** "+paramsMap);
				}
				if (applicationSystemMapServiceList.size() > 0) {
					for (Map<String, Object> row : applicationSystemMapServiceList) {
						String envKey = row.get("env_id").toString();
						String suiteKey = row.get("suite_id").toString();
						String bedKey = row.get("bed_id").toString();
//						String mapId = row.get("map_id").toString();
						
						paramsMap.put("env_id", Integer.parseInt(envKey));
						paramsMap.put("suite_id", Integer.parseInt(suiteKey));
						paramsMap.put("bed_id", Integer.parseInt(bedKey));
						//paramsMap.put("env_id", Integer.parseInt(envKey));
						
							if(ciService.isRecordAvail(paramsMap)) {
								String envName = null;
								String suiteName = null;
								String bedName = null;
								
								//Start New
								String envIdNext = null;
								String suiteIdNext = null;
								String bedIdNext = null;
								//End New
			
								for (Environment envVar : environmentList) {
									if (envVar.getEnvId() == Integer.parseInt(envKey)) {
										
										//Start New
										envIdNext = String.valueOf(envVar.getEnvId());
										//End New
										
										envName = envVar.getEnvName();
										for (TestSuite testSuite : testSuiteList) {
											if (testSuite.getSuiteId() == Integer
													.parseInt(suiteKey)) {
												
												//Start New
												suiteIdNext = String.valueOf(testSuite.getSuiteId());
												//End New
												
												suiteName = testSuite.getSuiteName();
												break;
											}
										}
										for (TestBed testBed : testBedList) {
											if (testBed.getBedId() == Integer
													.parseInt(bedKey)) {
												
												//Start New
												bedIdNext = String.valueOf(testBed.getBedId());
												//End New
												
												bedName = testBed.getBedName();
												break;
											}
										}
									}
								}
								// check for record in jenkins against -- app_id,env_id,suiteId,bedId
								if (envMap.get(envName) == null) {
									if(ciService.isRecordAvail(paramsMap)){
										Map suiteMap = new LinkedHashMap<String, Map>();
										Map bedMap = new LinkedHashMap<String, Map>();
										Map envMapInner = new LinkedHashMap<String, Map>();
										ArrayList suiteList = new ArrayList();
										
										//Start New
//										bedMap.put(mapId, bedName);
										bedMap.put(bedIdNext, bedName);
										//Start End
										
										suiteMap.put(suiteName, bedMap);
										//Start New
										suiteMap.put("ID", suiteIdNext);
										suiteMap.put("NAME", suiteName);
										//Start End
										
										suiteList.add(suiteMap);
										//Start New
										envMapInner.put(envName, suiteList);
										envMapInner.put("ID", envIdNext);
										envMapInner.put("NAME", envName);
										//Start End
										
										envMap.put(envName, envMapInner);
									}
									
								} else {
//									Map<String, Map> suiteMap = (Map) envMap.get(envName);
									//Start New
									Map envMapInner = (Map) envMap.get(envName);
									ArrayList suiteList = (ArrayList)envMapInner.get(envName);
									Map<String, Object> suiteMap = null;
									boolean suiteNameFound = false;
									for(int i = 0; ((i < suiteList.size()) && (!suiteNameFound)); i++) {
										suiteMap = (Map)suiteList.get(i);
										if(suiteMap.containsKey(suiteName)) {
											suiteNameFound = true;
										}
									}
									
									//Start End
//									if (suiteMap.get(suiteName) == null) {
									if (!suiteNameFound) {
										// different suite
										if(ciService.isRecordAvail(paramsMap)){
											suiteMap = new LinkedHashMap();
											Map bedMap = new LinkedHashMap();
											//Start New
//											bedMap.put(mapId, bedName);
											bedMap.put(bedIdNext, bedName);
											//Start End
											suiteMap.put(suiteName, bedMap);
											//Start New
											suiteMap.put("ID", suiteIdNext);
											suiteMap.put("NAME", suiteName);
											suiteList.add(suiteMap);
										}
									
									} else {
										// same suite
										if(ciService.isRecordAvail(paramsMap)){
											//Start New
//											suiteMap.put("ID", suiteIdNext);
//											suiteMap.get(suiteName).put(mapId, bedName);
											((Map)suiteMap.get(suiteName)).put(bedIdNext, bedName);
											//Start End
										}
										
									}
			
								}
							}
					}
				}
				
//				System.out.println("*****&&&&&&&&&****Final Enviornment** "+envMap);
				
				if (envMap != null && envMap.size() > 0) {
					Set<String> envMapKeys = envMap.keySet();
					Iterator<String> envMapKeysIterator = envMapKeys.iterator();
					
					List<com.etaap.beans.Environment> environmentListObj = new ArrayList<com.etaap.beans.Environment>();
					Environments envs = new Environments();
					envs.setEnvironment(environmentListObj);
			
					while (envMapKeysIterator.hasNext()) {
						com.etaap.beans.Environment envObjToSet = new com.etaap.beans.Environment();
						
						List<TestSuits> testSuitsToSet = new ArrayList<TestSuits>();
						TestSuits testSuitsObjToSet = new TestSuits(); 
						List<com.etaap.beans.TestSuite> testSuitesToSet = new ArrayList<com.etaap.beans.TestSuite>();
						testSuitsObjToSet.setTestSuites(testSuitesToSet);
						testSuitsToSet.add(testSuitsObjToSet);
						
						String envKey = envMapKeysIterator.next();
						LinkedHashMap envHashMap = (LinkedHashMap)envMap.get(envKey);
						
						String envIdToSet = (String)envHashMap.get("ID");
						String envNameToSet = (String)envHashMap.get("NAME");
						envObjToSet.setEnvId(envIdToSet);
						envObjToSet.setName(envNameToSet);
						envObjToSet.setTestSuits(testSuitsToSet);
						
						ArrayList suiteList = (ArrayList)envHashMap.get(envKey);
						for(int k = 0; k < suiteList.size(); k++) {
							LinkedHashMap suiteHash =  (LinkedHashMap)suiteList.get(k);
							String suiteIdToSet = (String)suiteHash.get("ID");
							String suiteNameToSet = (String)suiteHash.get("NAME");
							com.etaap.beans.TestSuite testSuiteToSet = new com.etaap.beans.TestSuite();
							testSuiteToSet.setSuiteId(suiteIdToSet);
							testSuiteToSet.setName(suiteNameToSet);
							testSuitesToSet.add(testSuiteToSet);
							
							List<Testbed> testBedsToSet = new ArrayList<Testbed>();
							testSuiteToSet.setTestBed(testBedsToSet);
							
							LinkedHashMap bedHashMap = (LinkedHashMap)suiteHash.get(suiteNameToSet);
							Iterator bedHashMapIt = bedHashMap.keySet().iterator();
							while(bedHashMapIt.hasNext()) {
								String bedIdToSet = (String)bedHashMapIt.next();
								String bedNameToSet = (String)bedHashMap.get(bedIdToSet);
								com.etaap.beans.Testbed testBedToSet = new com.etaap.beans.Testbed();
								testBedToSet.setId(bedIdToSet);
								testBedToSet.setText(bedNameToSet);
								testBedsToSet.add(testBedToSet);
							}
						}
						environmentListObj.add(envObjToSet);
					}
					
					Data dataMap = new Data();
					dataMap.getData().put("Environments", envs);
					dataMap.getData().put("CI", csvForCIChart);
					dataString = com.etaap.utils.gsonUtils.Gson
							.getGsonString(dataMap);
//					System.out.println("***************JSON String***&*&*&*& Data Map "+dataMap);
//					System.out.println("***************JSON String***&*&*&*& JSON String "+dataString);
					
				}
						
	/*					Map SuiteMap = (Map)envMap.get(envKey);
						List<TestSuits> testSuitsList = new ArrayList<TestSuits>();
						com.etaap.beans.Environment environment = new com.etaap.beans.Environment();
						environment.setName(envKey);
						
			
						List<com.etaap.beans.TestSuite> testSuiteListObj = new ArrayList<com.etaap.beans.TestSuite>();
						TestSuits testSuitsObj = new TestSuits();
						Set<String> SuiteMapKeys = SuiteMap.keySet();
						Iterator<String> SuiteMapKeysIterator = SuiteMapKeys
								.iterator();
						while (SuiteMapKeysIterator.hasNext()) {
							String suiteKey = SuiteMapKeysIterator.next();
							com.etaap.beans.TestSuite testSuite = new com.etaap.beans.TestSuite();
							testSuite.setName(suiteKey);
			
							List<Testbed> testBedListObj = new ArrayList<Testbed>();
			
							Map<String, String> bedMap = (Map<String, String>) SuiteMap
									.get(suiteKey);
			
							Set<String> bedKeys = bedMap.keySet();
							Iterator<String> bedKeysIterator = bedKeys.iterator();
			
							while (bedKeysIterator.hasNext()) {
								String mapId = bedKeysIterator.next();
								String bedName = bedMap.get(mapId);
			
								Testbed testBedObj = new Testbed();
								testBedObj.setId(mapId);
								testBedObj.setText(bedName);
								testBedListObj.add(testBedObj);
							}
			
							testSuite.setTestBed(testBedListObj);
							testSuiteListObj.add(testSuite);
						}
			
						testSuitsObj.setTestSuites(testSuiteListObj);
						testSuitsList.add(testSuitsObj);
						environment.setTestSuits(testSuitsList);
						environmentListObj.add(environment);
					}
					envs.setEnvironment(environmentListObj);
					Data dataMap = new Data();
					dataMap.getData().put("Environments", envs);
					dataMap.getData().put("CI", csvForCIChart);
					dataString = com.etaap.utils.gsonUtils.Gson
							.getGsonString(dataMap);
					System.out.println("***************JSON String***&*&*&*& Data Map "+dataMap);
					System.out.println("***************JSON String***&*&*&*& JSON String "+dataString);
				}*/
			} catch (Exception errors) {
				errors.printStackTrace();
				logger.error("ERROR :: getDataString() , " + errors.getMessage());
			}
			return dataString;
		}
	
	@RequestMapping(value = "/app/{appId}/{appName}/*.json", method = RequestMethod.GET)
	@ResponseBody
	public String getJsonString(@PathVariable String appId,
			@PathVariable String appName,
			@RequestParam("from") String fromParams,
			@RequestParam("to") String toParams, Model model) {
//		System.out.println("***********&*&*&******* getJsonString ");
		String jsonString = null;
		String envId = null;
		String suiteId = null;
		String bedId = null;

		try {
			String map_id = null;
			if (model.asMap() != null && model.asMap().size() > 0 && model.asMap().get("map_id") != null)
				map_id = (String) model.asMap().get("map_id");
			else
				map_id = getFromServletRequestAttribute();

			List<Application> applicationList = applicationService.getApplicationList();
			List<Application> applicationMapDetails = applicationService.getApplicationMapDetails(Integer.parseInt(appId));

			List<Environment> environmentList = environmentService.getEnvironmentList();
			List<Environment> envClone = new ArrayList<Environment>(environmentList);
			List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();
			List<TestSuite> suiteClone = new ArrayList<TestSuite>(testSuiteList);
			List<TestBed> testBedList = testBedService.getTestBedList();
			List<TestBed> bedClone = new ArrayList<TestBed>(testBedList);

			HashMap<String, String> filterOptions = new HashMap<String, String>();
			int monthId = ((Application) applicationService.getApplication(Integer.parseInt(appId),0,null)).getMonthId();
			List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

			List<Map<String, Object>> applicationSystemMap = applicationService.getApplicationSystemMapBasedOnMapId(map_id);
			if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
				envId = applicationSystemMap.get(0).get("env_id").toString();
				suiteId = applicationSystemMap.get(0).get("suite_id").toString();
				bedId = applicationSystemMap.get(0).get("bed_id").toString();

				filterOptions.put("appId", appId);
				filterOptions.put("envId", envId);
				filterOptions.put("suiteId", suiteId);
				filterOptions.put("bedId", bedId);
				filterOptions.put("periodId", String.valueOf(getActualTimePeriodList.get(0).getPeriodId()));
				filterOptions.put("periodStrtDt", fromParams + " 00:00:00");
				filterOptions.put("periodEndDt", toParams + " 23:59:59");
			}

			List<CI> ciDetails = ciService.getDetails(filterOptions);

			HashMap<String, Object> csvForCIChart = getCsvForCIChart(ciDetails);

			model.addAttribute("appList", applicationList);

			List<Map<String, Object>> applicationSystemMapServiceList = applicationService.getApplicationSystemMapService(appId);
			Map<String, Map> envMap = new LinkedHashMap<String, Map>();

			for (Map row : applicationSystemMapServiceList) {
				if (envMap.size() == 0) {
					for (Environment envVar : environmentList) {
						if (envVar.getEnvId() == Integer.parseInt(row.get("env_id").toString())) {
							String envName = envVar.getEnvName();
							String suiteName = null;
							String bedName = null;
							String mapId = null;
							Map suiteMap = new LinkedHashMap();
							Map bedMap = new LinkedHashMap();
							for (TestSuite testSuite : testSuiteList) {
								if (testSuite.getSuiteId() == Integer.parseInt(row.get("suite_id").toString())) {
									suiteName = testSuite.getSuiteName();
									break;
								}
							}
							for (TestBed testBed : testBedList) {
								if (testBed.getBedId() == Integer.parseInt(row.get("bed_id").toString())) {
									bedName = testBed.getBedName();
									mapId = row.get("map_id").toString();
									bedMap.put(mapId, bedName);
									suiteMap.put(suiteName, bedMap);
									envMap.put(envName, suiteMap);
									break;
								}
							}
						}
					}

				} else {
					String envKey = row.get("env_id").toString();
					String suiteKey = row.get("suite_id").toString();
					String bedKey = row.get("bed_id").toString();
					String mapId = row.get("map_id").toString();

					String envName = null;
					String suiteName = null;
					String bedName = null;

					for (Environment envVar : environmentList) {
						if (envVar.getEnvId() == Integer.parseInt(envKey)) {
							envName = envVar.getEnvName();
							for (TestSuite testSuite : testSuiteList) {
								if (testSuite.getSuiteId() == Integer.parseInt(suiteKey)) {
									suiteName = testSuite.getSuiteName();
									break;
								}
							}
							for (TestBed testBed : testBedList) {
								if (testBed.getBedId() == Integer.parseInt(bedKey)) {
									bedName = testBed.getBedName();
									break;
								}
							}
						}
					}

					if (envMap.get(envName) == null) {
						Map suiteMap = new HashMap();
						Map bedMap = new HashMap();
						bedMap.put(mapId, bedName);
						suiteMap.put(suiteName, bedMap);
						envMap.put(envName, suiteMap);
					} else {
						Map<String, Map> suiteMap = (Map) envMap.get(envName);
						if (suiteMap.get(suiteName) == null) {
							// different suite
							Map bedMap = new HashMap();
							bedMap.put(mapId, bedName);
							suiteMap.put(suiteName, bedMap);
							envMap.put(envName, suiteMap);
						} else {
							// same suite
							suiteMap.get(suiteName).put(mapId, bedName);
							envMap.put(envName, suiteMap);
						}
					}
				}
			}

			if (envMap != null && envMap.size() > 0) {
				Set<String> envMapKeys = envMap.keySet();
				Iterator<String> envMapKeysIterator = envMapKeys.iterator();
				List<com.etaap.beans.Environment> environmentListObj = new ArrayList<com.etaap.beans.Environment>();
				Environments envs = new Environments();

				while (envMapKeysIterator.hasNext()) {
					String envKey = envMapKeysIterator.next();
					Map SuiteMap = envMap.get(envKey);
					List<TestSuits> testSuitsList = new ArrayList<TestSuits>();
					com.etaap.beans.Environment environment = new com.etaap.beans.Environment();
					environment.setName(envKey);

					List<com.etaap.beans.TestSuite> testSuiteListObj = new ArrayList<com.etaap.beans.TestSuite>();
					TestSuits testSuitsObj = new TestSuits();
					Set<String> SuiteMapKeys = SuiteMap.keySet();
					Iterator<String> SuiteMapKeysIterator = SuiteMapKeys.iterator();
					while (SuiteMapKeysIterator.hasNext()) {
						String suiteKey = SuiteMapKeysIterator.next();
						com.etaap.beans.TestSuite testSuite = new com.etaap.beans.TestSuite();
						testSuite.setName(suiteKey);

						List<Testbed> testBedListObj = new ArrayList<Testbed>();

						Map<String, String> bedMap = (Map<String, String>) SuiteMap.get(suiteKey);

						Set<String> bedKeys = bedMap.keySet();
						Iterator<String> bedKeysIterator = bedKeys.iterator();

						while (bedKeysIterator.hasNext()) {
							String mapId = bedKeysIterator.next();
							String bedName = bedMap.get(mapId);

							Testbed testBedObj = new Testbed();
							testBedObj.setId(mapId);
							testBedObj.setText(bedName);
							testBedListObj.add(testBedObj);
						}

						testSuite.setTestBed(testBedListObj);
						testSuiteListObj.add(testSuite);
					}

					testSuitsObj.setTestSuites(testSuiteListObj);
					testSuitsList.add(testSuitsObj);
					environment.setTestSuits(testSuitsList);
					environmentListObj.add(environment);
				}
				envs.setEnvironment(environmentListObj);
				Data dataMap = new Data();
				dataMap.getData().put("Environments", envs);
				dataMap.getData().put("CI", csvForCIChart);

				jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonString;
	}

	@RequestMapping(value = "/changeApp", method = RequestMethod.GET)
	public ModelAndView hhh(@RequestParam("id") String id, @RequestParam("api") String api,Model model, RedirectAttributes redirectAttributes) {

		BasicConfigurator.configure();
		logger.info("Inside MainController :: handleRequest()");

		String appId = null;
		String from = null;
		String to = null;
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ModelAndView modelAndView = null;
		String apiName = api;
		try {
			if(api.toLowerCase().equals("tcm")) {
				List<Application> applicationList = tcmServiceImpl.getTcmApplicationList(Integer.parseInt(id));
				
				int monthId = applicationList.get(0).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(applicationList.get(0).getAppId());
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
				
				Application application = applicationService.getApplication(Integer.parseInt(appId),0,null);
				redirectAttributes.addFlashAttribute("requestedApp", application.getAppName());
				  
				redirectAttributes.addFlashAttribute("app_id", tcmServiceImpl.getTcmApplicationList());
				
				modelAndView = new ModelAndView("redirect:/app/" + appId + "/TCM/" + appId + ".html/?from=" + from + "&to=" + to);
			} 
			else if (api.toLowerCase().equals("velocity")) {
				List<Application> applicationList = velocityService.getVelocityApplicationList(Integer.parseInt(id));
				
				int monthId = applicationList.get(0).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(applicationList.get(0).getAppId());
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
				
				Application application = applicationService.getApplication(Integer.parseInt(appId),0,null);
				redirectAttributes.addFlashAttribute("requestedApp", application.getAppName());
				  
				redirectAttributes.addFlashAttribute("app_id", velocityService.getVelocityApplicationList());
				
				modelAndView = new ModelAndView("redirect:/app/" + appId + "/velocity/" + appId + ".html/?from=" + from + "&to=" + to);
			} 
			else if(api.equalsIgnoreCase(com.etaap.utils.enums.SystemAPI.JIRA.getSystemName())) {
/*				List<Application> applicationList = applicationService
						.getApplicationList();
*/				List<Application> applicationList = applicationService
						.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JIRA.getSystemName());
				int monthId = ((Application) applicationService
						.getApplication(Integer.parseInt(id),2,"QA")).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils
						.getTimePeriod(monthId);

				appId = String.valueOf(Integer.parseInt(id));
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);

				String map_id = applicationService.getMapIdByAppId(appId,apiName.equalsIgnoreCase(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName()) ? 
						                                                com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName() : 
						                                        	    com.etaap.utils.enums.SystemAPI.JIRA.getSystemName());

				redirectAttributes.addFlashAttribute("map_id", map_id);
/*				modelAndView = new ModelAndView("redirect:/app/" + appId
						+ "/"+apiName.toLowerCase()+"/" + map_id + ".html/?from=" + from + "&to="
						+ to);
*/				modelAndView = new ModelAndView("redirect:/app/" + appId
						+ "/"+ApplicationConstants.JIRA_QA_REDESIGN+"/" + map_id + ".html/?from=" + from + "&to="
						+ to);
			}
			else if(api.equalsIgnoreCase(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName())) {
//				List<Application> applicationList = applicationService.getApplicationList();
				List<Application> applicationList = 
						applicationService.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
				
				int monthId = ((Application) applicationService.getApplication(Integer.parseInt(id),1,"QA")).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(Integer.parseInt(id));
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
				
				
				///////////////////
				HashMap envIdSuiteIdBedIdMap = applicationService.getDefaultActiveEnvIdSuiteIdBedIdByAppId(appId,com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
				String enviornmentId = "0";
				String suiteId = "0";
				String bedId = "0";
							
				if(envIdSuiteIdBedIdMap != null) {
					enviornmentId = String.valueOf(envIdSuiteIdBedIdMap.get("env_id"));
					suiteId = String.valueOf(envIdSuiteIdBedIdMap.get("suite_id"));
					bedId = String.valueOf(envIdSuiteIdBedIdMap.get("bed_id"));

					redirectAttributes.addFlashAttribute("env_id", enviornmentId);
					redirectAttributes.addFlashAttribute("suite_id", suiteId);
					redirectAttributes.addFlashAttribute("bed_id", bedId);

					redirectAttributes.addFlashAttribute("app_id", applicationList);

				}
				
//				modelAndView = new ModelAndView("redirect:/app/" + appId + "/jenkins/" + enviornmentId+"_"+suiteId+"_"+bedId + ".html/?from=" + from + "&to=" + to+
//				"&envId="+enviornmentId+"&suiteId="+suiteId+"&bedId="+bedId);
				modelAndView = new ModelAndView("redirect:/app/" + appId + "/"+ApplicationConstants.JENKINS_QA_REDESIGN+"/" + enviornmentId+"_"+suiteId+"_"+bedId + ".html/?from=" + from + "&to=" + to+
				"&envId="+enviornmentId+"&suiteId="+suiteId+"&bedId="+bedId);
				//////////////
				
				
/*				String map_id = applicationService.getMapIdByAppId(appId,com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());

				redirectAttributes.addFlashAttribute("map_id", map_id);
				modelAndView = new ModelAndView("redirect:/app/" + appId
						+ "/"+apiName.toLowerCase()+"/" + map_id + ".html/?from=" + from + "&to="
						+ to);
*/
			}	
			else if(api.toLowerCase().trim().equals("iterations")) {
				
				logger.info(":: Inside maincontroller :: iterations :: "+api.toLowerCase());
				int app_Id = -1;
				int sprintId = -1;
				int appIdInt = Integer.parseInt(id);
				int sprintIdFromDB = -1;
				
				List<Sprint> sprintList = iterationsChartService.getSprintList(appIdInt);
				if(sprintList.size() > 0) {
					Sprint sprint = null;
					sprint = sprintList.get(0);
					sprintIdFromDB = sprint.getSprintId();
				}
				List<com.etaap.beans.Application> applicationList = iterationsChartService.getApplicationList(Integer.parseInt(id));
				
				//List<Application> applicationList = velocityService.getVelocityApplicationList(Integer.parseInt(id));
				
				if(applicationList.size() > 0) {
					app_Id = applicationList.get(0).getAppId();
				}
				
				Application application = applicationService.getApplication(app_Id,0,null);
				redirectAttributes.addFlashAttribute("requestedApp", application.getAppName());
				
				redirectAttributes.addFlashAttribute("app_id", applicationList);
				redirectAttributes.addFlashAttribute("sprintList", sprintList);
				
				modelAndView = new ModelAndView("redirect:/app/"+appIdInt +"/"+ApplicationConstants.JIRA_DEV+"/iterations/"+appIdInt+".html?sprintId="
						+sprintIdFromDB);
			}
/*			else {
				List<Application> applicationList = applicationService.getApplicationList();
				int monthId = ((Application) applicationService.getApplication(Integer.parseInt(id),0,null)).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(Integer.parseInt(id));
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);

				String map_id = applicationService.getMapIdByAppId(appId,apiName.equalsIgnoreCase(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName()) ? 
						com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName() : com.etaap.utils.enums.SystemAPI.JIRA.getSystemName());

				redirectAttributes.addFlashAttribute("map_id", map_id);
				modelAndView = new ModelAndView("redirect:/app/" + appId
						+ "/"+apiName.toLowerCase()+"/" + map_id + ".html/?from=" + from + "&to="
						+ to);
			}
			*/
		} catch (Exception e) {
			logger.info("ERROR :: handleHomeRequest () method," + e.getMessage());
		}
		return modelAndView;
	}
	
//	@RequestMapping(value = "/app/{appId}/jenkins/*.html", method = RequestMethod.GET)
//	public String getCSVDataHtmlGetData(
//			@PathVariable String appId,
//			@RequestParam("from") String fromParams,
//			@RequestParam("to") String toParams,
//			Model model,
//			org.springframework.web.context.request.RequestContextListener requestContextListener) {
//		logger.info("Inside MainController :: getCSVDataHtmlGetData() method ");
//
//		String app_Id = appId;
//		String from = fromParams;
//		String to = toParams;
//		String map_id = null;
//
//		if (model.asMap() != null && model.asMap().size() > 0
//				&& model.asMap().get("map_id") != null)
//			map_id = (String) model.asMap().get("map_id");
//		else
//			map_id = getFromServletRequestAttribute();
//
//		List<Application> applicationList = (List<Application>) ((model.asMap() != null
//				&& model.asMap().size() > 0 && model.asMap().get("app_id") != null) ? model
//				.asMap().get("app_id")
//				: applicationService
//						.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JENKINS
//								.getSystemName()));
//
//		List<Environment> environmentList = environmentService
//				.getEnvironmentList(null);
//		List<Environment> envClone = new ArrayList<Environment>(environmentList);
//		List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList(null);
//		List<TestSuite> suiteClone = new ArrayList<TestSuite>(testSuiteList);
//		List<TestBed> testBedList = testBedService.getTestBedList(null);
//		List<TestBed> bedClone = new ArrayList<TestBed>(testBedList);
//
//		List<Application> applicationMapDetails = applicationService
//				.getApplicationMapDetails(Integer.parseInt(app_Id));
//
//		HashMap<String, String> filterOptions = new HashMap<String, String>();
//
//		for (Application app : applicationMapDetails) {
//			ArrayList abc = (ArrayList) app.getEnvIdList();
//			for (Environment e : envClone) {
//				if (!abc.contains(String.valueOf(e.getEnvId())))
//					environmentList.remove(e);
//			}
//			ArrayList a = (ArrayList) app.getSuiteIdList();
//			for (TestSuite ts : suiteClone) {
//				if (!a.contains(String.valueOf(ts.getSuiteId())))
//					testSuiteList.remove(ts);
//			}
//			ArrayList bc = (ArrayList) app.getBedIdList();
//			for (TestBed tb : bedClone) {
//				if (!bc.contains(String.valueOf(tb.getBedId())))
//					testBedList.remove(tb);
//			}
//
//			filterOptions.put("envId", String.valueOf(app.getEnvId()));
//			filterOptions.put("suiteId", String.valueOf(app.getSuiteId()));
//			filterOptions.put("bedId", String.valueOf(app.getBedId()));
//		}
//
//		int monthId = ((Application) applicationService.getApplication(Integer
//				.parseInt(app_Id))).getMonthId();
//		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils
//				.getTimePeriod(monthId);
//
//		String envId = null;
//		String suiteId = null;
//		String bedId = null;
//
//		List<Map<String, Object>> applicationSystemMap = applicationService
//				.getApplicationSystemMapBasedOnMapId(map_id);
//		if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
//			envId = applicationSystemMap.get(0).get("env_id").toString();
//			suiteId = applicationSystemMap.get(0).get("suite_id").toString();
//			bedId = applicationSystemMap.get(0).get("bed_id").toString();
//
//			filterOptions.put("envId", envId);
//			filterOptions.put("suiteId", suiteId);
//			filterOptions.put("bedId", bedId);
//
//		}
//
//		filterOptions.put("appId", app_Id);
//		filterOptions.put("periodId",
//				String.valueOf(getActualTimePeriodList.get(0).getPeriodId()));
//		filterOptions.put("periodStrtDt", from + " 00:00:00");
//		filterOptions.put("periodEndDt", to + " 23:59:59");
//
//		List<CI> ciDetails = ciService.getDetails(filterOptions);
//
//		HashMap<String, Object> csvForCIChart = getCsvForCIChart(ciDetails);
//
//		model.addAttribute("appList", applicationList);
//
//		String jsonObj = getDataString(app_Id, envId, suiteId, bedId,
//				environmentList, testSuiteList, testBedList, csvForCIChart,
//				model);
//
//		model.addAttribute("jsonString", jsonObj);
//		model.addAttribute("envList", environmentList);
//		model.addAttribute("suiteList", testSuiteList);
//		model.addAttribute("bedList", testBedList);
//		model.addAttribute("periodList", getActualTimePeriodList);
//		Application application = applicationService.getApplication(Integer
//				.parseInt(appId));
//		model.addAttribute("requestedApp", application.getAppName());
//		model.addAttribute("csvForCIChart", csvForCIChart);
//		model.addAttribute("periodStrtDt", from + " 00:00:00");
//		model.addAttribute("periodEndDt", to + " 23:59:59");
//		model.addAttribute("to", to);
//		model.addAttribute("systemAPI",com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
//		return "home";
//	} 
	
	

	@RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JENKINS_QA+"/*.html", method = RequestMethod.GET)
	//Code changes required into this method if jenkins_dev is required to implement.
//	@RequestMapping(value = "/app/{appId}/{appName}/*.html", method = RequestMethod.GET)
	public String getCSVDataHtmlGetData(
			@PathVariable String appId,
//			@PathVariable String appName,
			@RequestParam("from") String fromParams,
			@RequestParam("to") String toParams,
			@RequestParam("envId") String envIdParam,
			@RequestParam("suiteId") String suiteIdParam,
			@RequestParam("bedId") String bedIdParam,
			Model model,
			org.springframework.web.context.request.RequestContextListener requestContextListener) {
		logger.info("Inside MainController :: getCSVDataHtmlGetData() method ");
		System.out.println("*****&*&*&********getCSVDataHtmlGetData****** "+appId+" "+fromParams+" , "+
				toParams+" , "+envIdParam+" , "+suiteIdParam+" , "+bedIdParam);
		String app_Id = appId;
		/* String app_Name = appName; */
		String from = fromParams;
		String to = toParams;
//		String map_id = null;

		List<Application> applicationList = (List<Application>) ((model.asMap() != null
				&& model.asMap().size() > 0 && model.asMap().get("app_id") != null) ? model.asMap().get("app_id")
				: applicationService.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName()));

		List<Environment> environmentList = environmentService.getEnvironmentList(null);
		List<Environment> envClone = new ArrayList<Environment>(environmentList);
		List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList(null);
		List<TestSuite> suiteClone = new ArrayList<TestSuite>(testSuiteList);
		List<TestBed> testBedList = testBedService.getTestBedList(null);
		List<TestBed> bedClone = new ArrayList<TestBed>(testBedList);

		List<Application> applicationMapDetails = applicationService
				.getApplicationMapDetailsForJenkins(Integer.parseInt(app_Id));
		
//		List<Application> applicationMapDetails = applicationService.getApplicationMapDetails(Integer.parseInt(app_Id));

		HashMap<String, String> filterOptions = new HashMap<String, String>();

		for (Application app : applicationMapDetails) {
			ArrayList abc = (ArrayList) app.getEnvIdList();
			for (Environment e : envClone) {
				if (!abc.contains(String.valueOf(e.getEnvId())))
					environmentList.remove(e);
			}
			ArrayList a = (ArrayList) app.getSuiteIdList();
			for (TestSuite ts : suiteClone) {
				if (!a.contains(String.valueOf(ts.getSuiteId())))
					testSuiteList.remove(ts);
			}
			ArrayList bc = (ArrayList) app.getBedIdList();
			for (TestBed tb : bedClone) {
				if (!bc.contains(String.valueOf(tb.getBedId())))
					testBedList.remove(tb);
			}

			filterOptions.put("envId", String.valueOf(app.getEnvId()));
			filterOptions.put("suiteId", String.valueOf(app.getSuiteId()));
			filterOptions.put("bedId", String.valueOf(app.getBedId()));
		}

		int monthId = ((Application) applicationService.getApplication(Integer.parseInt(app_Id),0,null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

/*		String envId = null;
		String suiteId = null;
		String bedId = null;

		List<Map<String, Object>> applicationSystemMap = applicationService.getApplicationSystemMapBasedOnMapId(map_id);
		if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
			envId = applicationSystemMap.get(0).get("env_id").toString();
			suiteId = applicationSystemMap.get(0).get("suite_id").toString();
			bedId = applicationSystemMap.get(0).get("bed_id").toString();

			filterOptions.put("envId", envId);
			filterOptions.put("suiteId", suiteId);
			filterOptions.put("bedId", bedId);
		}
		*/
		
		filterOptions.put("envId", envIdParam);
		filterOptions.put("suiteId", suiteIdParam);
		filterOptions.put("bedId", bedIdParam);

		filterOptions.put("appId", app_Id);
		filterOptions.put("periodId", String.valueOf(getActualTimePeriodList.get(0).getPeriodId()));
		filterOptions.put("periodStrtDt", from + " 00:00:00");
		filterOptions.put("periodEndDt", to + " 23:59:59");

		List<CI> ciDetails = ciService.getDetails(filterOptions);

		HashMap<String, Object> csvForCIChart = getCsvForCIChart(ciDetails);

		model.addAttribute("appList", applicationList);

		String jsonObj = getDataString(app_Id, envIdParam, suiteIdParam, bedIdParam,
				environmentList, testSuiteList, testBedList, csvForCIChart,
				model);
		
//		String jsonObj = getDataString(app_Id, envId, suiteId, bedId, environmentList, testSuiteList, testBedList, csvForCIChart, model);

		System.out.println("***************^^^^^^^ JSON "+jsonObj);
		model.addAttribute("jsonString", jsonObj);
		model.addAttribute("envList", environmentList);
		model.addAttribute("suiteList", testSuiteList);
		model.addAttribute("bedList", testBedList);
		model.addAttribute("periodList", getActualTimePeriodList);
		Application application = applicationService.getApplication(Integer.parseInt(appId),0,null);
		model.addAttribute("requestedApp", application.getAppName());
		model.addAttribute("csvForCIChart", csvForCIChart);
		model.addAttribute("periodStrtDt", from + " 00:00:00");
		model.addAttribute("periodEndDt", to + " 23:59:59");
		model.addAttribute("to", to);
		model.addAttribute("systemAPI",com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
		return "home";
	}
	
	@RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JENKINS_QA_REDESIGN+"/*.html", method = RequestMethod.GET)
	public String getCIResultsData(
			@PathVariable String appId,
			@RequestParam("from") String fromParams,
			@RequestParam("to") String toParams,
			@RequestParam("envId") String envIdParam,
			@RequestParam("suiteId") String suiteIdParam,
			@RequestParam("bedId") String bedIdParam,
			Model model, org.springframework.web.context.request.RequestContextListener requestContextListener) {
		
		logger.info("Inside MainController :: getCSVDataHtmlGetData() method ");
		String app_Id = appId;
		String from = fromParams;
		String to = toParams;

		List<Application> applicationList = (List<Application>) ((model.asMap() != null
				&& model.asMap().size() > 0 && model.asMap().get("app_id") != null) ? model.asMap().get("app_id")
				: applicationService.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName()));

		List<Environment> environmentList = environmentService.getEnvironmentList(null);
		List<Environment> envClone = new ArrayList<Environment>(environmentList);
		List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList(null);
		List<TestSuite> suiteClone = new ArrayList<TestSuite>(testSuiteList);
		List<TestBed> testBedList = testBedService.getTestBedList(null);
		List<TestBed> bedClone = new ArrayList<TestBed>(testBedList);

		List<Application> applicationMapDetails = applicationService
				.getApplicationMapDetailsForJenkins(Integer.parseInt(app_Id));

		for (Application app : applicationMapDetails) {
			ArrayList abc = (ArrayList) app.getEnvIdList();
			for (Environment e : envClone) {
				if (!abc.contains(String.valueOf(e.getEnvId())))
					environmentList.remove(e);
			}
			ArrayList a = (ArrayList) app.getSuiteIdList();
			for (TestSuite ts : suiteClone) {
				if (!a.contains(String.valueOf(ts.getSuiteId())))
					testSuiteList.remove(ts);
			}
			ArrayList bc = (ArrayList) app.getBedIdList();
			for (TestBed tb : bedClone) {
				if (!bc.contains(String.valueOf(tb.getBedId())))
					testBedList.remove(tb);
			}
		}

		int monthId = ((Application) applicationService.getApplication(Integer.parseInt(app_Id),0,null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);
		
		//Chart Re-Design Code
		List<ChartModel> chartsList = new ArrayList<ChartModel>();
		chartsList.add(new ChartModel("eTAAPCIResults", ChartType.COLUMN, 
				Arrays.asList(new String[]{"Passed","Failed","Skipped"}),
				new Object[] {app_Id, envIdParam, suiteIdParam, bedIdParam, 
					from + " 00:00:00", to + " 23:59:59", app_Id, envIdParam, suiteIdParam, bedIdParam}));
		
		String envjsonObj = getDataString(app_Id, envIdParam, suiteIdParam, bedIdParam,
				environmentList, testSuiteList, testBedList, null,
				model);
		
		model.addAttribute("appList", applicationList);
		model.addAttribute("jsonString", chartService.getCharts(servletContext, chartsList));
		model.addAttribute("envJsonString", envjsonObj);
		model.addAttribute("envList", environmentList);
		model.addAttribute("suiteList", testSuiteList);
		model.addAttribute("bedList", testBedList);
		model.addAttribute("periodList", getActualTimePeriodList);
		Application application = applicationService.getApplication(Integer.parseInt(appId),0,null);
		model.addAttribute("requestedApp", application.getAppName());
		//model.addAttribute("csvForCIChart", csvForCIChart);
		model.addAttribute("periodStrtDt", from + " 00:00:00");
		model.addAttribute("periodEndDt", to + " 23:59:59");
		model.addAttribute("to", to);
		model.addAttribute("systemAPI",com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
		
		return "home_redesign";
	}

	private String getFromServletRequestAttribute() {
		String pageName = null;
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		try {
			String requestedURI = requestAttributes.getRequest().getRequestURI();
			String[] splittedVal = requestedURI.split("/");
			pageName = splittedVal[splittedVal.length - 1];
			pageName = pageName.split("\\.")[0];
		} catch (Exception e) {
			logger.info("ERROR :: getFromServletRequestAttribute() " + e.getMessage());
		}
		return pageName;
	}

	public HashMap<String, Object> getCsvForCIChart(List<CI> ciDetails) {
		logger.info("Inside MainController :: getCsvForCIChart()");

		List<String> buildNumberCSV = new ArrayList<String>();
		List<String> failCountCSV = new ArrayList<String>();
		List<String> passCountCSV = new ArrayList<String>();
		List<String> skipCountCSV = new ArrayList<String>();
		List<String> buildDateCSV = new ArrayList<String>();

		for (CI ci : ciDetails) {
			buildNumberCSV.add(String.valueOf(ci.getBuildNumber()));
			failCountCSV.add(String.valueOf(ci.getFailCount()));
			passCountCSV.add(String.valueOf(ci.getPassCount()));
			skipCountCSV.add(String.valueOf(ci.getSkipCount()));
			buildDateCSV.add(/* "'"+ */String.valueOf(ci.getBuildDate()).substring(0, 10)/* +"'" */);
		}

		logger.info("buildNumberCSV :: " + buildNumberCSV);
		logger.info("failCountCSV :: " + failCountCSV);
		logger.info("passCountCSV :: " + passCountCSV);
		logger.info("skipCountCSV :: " + skipCountCSV);

		HashMap<String, Object> csvMap = new HashMap<String, Object>();
		csvMap.put("buildNumberCSV", buildNumberCSV);
		csvMap.put("failCountCSV", failCountCSV);
		csvMap.put("passCountCSV", passCountCSV);
		csvMap.put("skipCountCSV", skipCountCSV);
		csvMap.put("buildDateCSV", buildDateCSV);

		logger.info("Inside MainController :: getCsvForCIChart() :: csvMap :: " + csvMap);

		return csvMap;
	}

	@RequestMapping(value = "/create")
	public String handleCreateRequest(
			/*@RequestParam("paramName") String paramName,
			@RequestParam("apiId") String apiId,
			@RequestParam("deptType") String deptType,*/
			HttpServletRequest request,
			@ModelAttribute Application application,
			@ModelAttribute Environment environment,
			@ModelAttribute TestSuite testSuite,
			@ModelAttribute TestBed testBed,
			@ModelAttribute TimePeriod timePeriod,
			@ModelAttribute TestCase testCase,
			@ModelAttribute SystemAPI systemAPI, Model model) {
		
		

		logger.info("Inside MainController :: handleCreateRequest() :: paramName" + request.getParameter("paramName"));

		HashMap<String, String> pName = new HashMap<String, String>();
		pName.put("paramName", String.valueOf(request.getParameter("paramName")));
		model.addAttribute("paramName", pName);

		String redirectUrl = "redirect:/manage?paramName=" + request.getParameter("paramName") + "&page=&orderBy=&orderType=";
	//	Application application = new Application();

		/*if (String.valueOf(paramName).equalsIgnoreCase("app")) {
			List<Environment> environmentList = environmentService.getEnvironmentList();
			List<TestBed> testBedList = testBedService.getTestBedList();
			List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();
			List<SystemAPI> systemAPIList = systemAPIService.getSystemAPIList();

//			System.out.println(application.getUrl());

			model.addAttribute("env_list", environmentList);
			model.addAttribute("bed_list", testBedList);
			model.addAttribute("suite_list", testSuiteList);
			model.addAttribute("sys_list", systemAPIList);

			model.addAttribute("apiList", applicationService.getAPIList());
			model.addAttribute("deptList", applicationService.getDepartmentList());

			if (application.getAppName() != null) {
				applicationService.insertData(application);
				return redirectUrl;
			}
		}*/
		if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("app")) {
			List<Environment> environmentList = environmentService.getEnvironmentList();
			List<TestBed> testBedList = testBedService.getTestBedList();
			List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();
			//List<SystemAPI> systemAPIList = systemAPIService.getSystemAPIList();

			//System.out.println(application.getUrl());

			model.addAttribute("env_list", environmentList);
			model.addAttribute("bed_list", testBedList);
			model.addAttribute("suite_list", testSuiteList);

			/*model.addAttribute("apiList", applicationService.getAPIList());
			model.addAttribute("deptList", applicationService.getDepartmentList());*/

			Map<String, String> apiList = applicationService.getAPIList();
			
			
			
			String firstApi = "0";
			if (apiList.size() > 0) {
				Map.Entry<String,String> entry1 = apiList.entrySet().iterator().next();
				firstApi = entry1.getKey();
				model.addAttribute("apiList", apiList);
	
				String apiId = request.getParameter("apiId");
				if (apiId != null && !apiId.equalsIgnoreCase("") && !apiId.equalsIgnoreCase("0")) {
					firstApi = apiId;
				}
			}

			Map<String, String> deptList = applicationService.getDepartmentList();
			String firstDeptType = "";
			if (deptList.size() > 0) {
				Map.Entry<String,String> entry2 = deptList.entrySet().iterator().next();
				firstDeptType = entry2.getValue();
				model.addAttribute("deptList", deptList);
	
				String deptType = request.getParameter("deptType");
				if (deptType != null && !deptType.equalsIgnoreCase("")) {
					firstDeptType = deptType;
				}
			}

			List<SystemAPI> systemAPIList = systemAPIService.getSystemAPIList(Integer.parseInt(firstApi));
			model.addAttribute("sys_list", systemAPIList);
			
			
			List urlAliasList = applicationDaoImpl.getUrlAlias();
			model.addAttribute("urlAlias_listfromDB", urlAliasList);
			
			
			application.setApiId(Integer.parseInt(firstApi));
			application.setDeptType(firstDeptType);
			
			if (application.getAppName() != "" && application.getAppName() != null) {
				if(application.getAppName().contains(",")){
					
					String appNameArr[] =application.getAppName().split(",");
					String appNameActual = appNameArr[0];
					application.setAppName(appNameActual);
				}
				applicationService.insertData(application);
				return redirectUrl;
			}
			
			String app_Name = request.getParameter("app_Name");
			application.setAppName(app_Name);
			
			String month_Name = request.getParameter("month_Name");
			application.setMonthName(month_Name);
			
			application.setStatus(1);
			
			/*String month_id = request.getParameter("monthId");
			application.setMonthId(Integer.parseInt(month_id));*/
			
			model.addAttribute("appDetails", application);
			
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("env")) {
			if (environment.getEnvName() != null) {
				environmentService.insertData(environment);
				return redirectUrl;
			}
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("suite")) {
			if (testSuite.getSuiteName() != null) {
				testSuiteService.insertData(testSuite);
				return redirectUrl;
			}
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("bed")) {
			if (testBed.getBedName() != null) {
				testBedService.insertData(testBed);
				return redirectUrl;
			}
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("period")) {
			List<Application> applicationList = applicationService.getApplicationList();
			model.addAttribute("app_list", applicationList);

			if (timePeriod.getMonthName() != null ) {
				timePeriodService.insertData(timePeriod);
				return redirectUrl;
			}
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("sys")) {
			model.addAttribute("dept_list", Utils.getDepartmentList());

			if (systemAPI.getSysName() != null) {
				systemAPIService.insertData(systemAPI);
				return redirectUrl;
			}
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("tc")) {
			List<Application> applicationList = applicationService.getApplicationListToCreateTestCase();
			List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();

			Map<String, String> tabs = new LinkedHashMap<String, String>();
			tabs.put("0", "Tab_1");
			tabs.put("1", "Tab_2");
			tabs.put("2", "Tab_3");
			tabs.put("3", "Tab_4");
			Map<String, String> sorted_tabs = new TreeMap<String, String>(tabs);

			Map<String, String> tcTypeList = new LinkedHashMap<String, String>();
			tcTypeList.put("Automated", "Automated");
			tcTypeList.put("Manual", "Manual");
			Map<String, String> sorted_tcTypeList = new TreeMap<String, String>(tcTypeList);

			model.addAttribute("app_list", applicationList);
			model.addAttribute("tabs", sorted_tabs);
			model.addAttribute("suite_list", testSuiteList);
			model.addAttribute("tcType_list", sorted_tcTypeList);

			if (testCase.getAppId() != 0) {
				testCaseService.insertData(testCase);
				return redirectUrl;
			}
		}

		return "create/create_home";
	}

	@RequestMapping(value = "/edit")
	public String handleEditRequest(
			@RequestParam("recordId") String recordId,
			/*@RequestParam("paramName") String paramName,
			/*@RequestParam("apiId") String apiId,
			@RequestParam("deptType") String deptType,*/
			HttpServletRequest request,
			@ModelAttribute Application application,
			@ModelAttribute Environment environment,
			@ModelAttribute TestSuite testSuite,
			@ModelAttribute TestBed testBed,
			@ModelAttribute TimePeriod timePeriod,
			@ModelAttribute TestCase testCase,
			@ModelAttribute SystemAPI systemAPI, Model model) {
		
		System.out.println("/edit inside");

		logger.info("Inside MainController :: handleEditRequest() :: paramName" + request.getParameter("paramName"));

		HashMap<String, String> pName = new HashMap<String, String>();
		pName.put("paramName", String.valueOf(request.getParameter("paramName")));
		model.addAttribute("paramName", pName);

		if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("app")) {
			List<Environment> environmentList = environmentService.getEnvironmentList();
			List<TestBed> testBedList = testBedService.getTestBedList();
			List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();
			//List<SystemAPI> systemAPIList = systemAPIService.getSystemAPIList();

//			System.out.println(application.getUrl());

			model.addAttribute("env_list", environmentList);
			model.addAttribute("bed_list", testBedList);
			model.addAttribute("suite_list", testSuiteList);
			//model.addAttribute("sys_list", systemAPIList);

			environmentList = environmentService.getEnvironmentList(null);
			model.addAttribute("_env_list", environmentList);

			testSuiteList = testSuiteService.getTestSuiteList(null);
			model.addAttribute("_suite_list", testSuiteList);

			testBedList = testBedService.getTestBedList(null);
			model.addAttribute("_bed_list", testBedList);

			Map<String, String> apiList = applicationService.getAPIList();
			String firstApi = "0";
			if (apiList.size() > 0) {
				Map.Entry<String,String> entry1 = apiList.entrySet().iterator().next();
				firstApi = entry1.getKey();
				model.addAttribute("apiList", apiList);
	
				String apiId = request.getParameter("apiId");
				if (apiId != null && !apiId.equalsIgnoreCase("") && !apiId.equalsIgnoreCase("0")) {
					firstApi = apiId;
				}
			}

			Map<String, String> deptList = applicationService.getDepartmentList();
			String firstDeptType = "";
			if (deptList.size() > 0) {
				Map.Entry<String,String> entry2 = deptList.entrySet().iterator().next();
				firstDeptType = entry2.getValue();
				model.addAttribute("deptList", deptList);
				String deptType = request.getParameter("deptType");
				if (deptType != null && !deptType.equalsIgnoreCase("")) {
					firstDeptType = deptType;
				}
			}
			List<SystemAPI> systemAPIList = systemAPIService.getSystemAPIList(Integer.parseInt(firstApi));
			model.addAttribute("sys_list", systemAPIList);
			
			List urlAliasList = applicationDaoImpl.getUrlAlias();
			//System.out.println(urlAliasList);
			model.addAttribute("urlAlias_listfromDB", urlAliasList);

			application = applicationService.getApplication(Integer.parseInt(recordId), Integer.parseInt(firstApi), firstDeptType);
			application.setApiId(Integer.parseInt(firstApi));
			application.setDeptType(firstDeptType);
			
			application.setAction("edit");
			application.setFlag(true);
			//------------
			model.addAttribute("appDetails", application);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("env")) {
			environment = environmentService.getEnvironment(Integer.parseInt(recordId));
			model.addAttribute("envDetails", environment);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("suite")) {
			testSuite = testSuiteService.getTestSuite(Integer.parseInt(recordId));
			model.addAttribute("suiteDetails", testSuite);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("bed")) {
			testBed = testBedService.getTestBed(Integer.parseInt(recordId));
			model.addAttribute("bedDetails", testBed);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("period")) {
			List<Application> applicationList = applicationService.getApplicationList();
			model.addAttribute("app_list", applicationList);

			timePeriod = timePeriodService.getTimePeriod(Integer.parseInt(recordId));
			model.addAttribute("periodDetails", timePeriod);

			for (Application apps : applicationList) {
				if (timePeriod.getAppId() == apps.getAppId()) {
					model.addAttribute("appName", apps.getAppName());
				}
			}
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("sys")) {
			systemAPI = systemAPIService.getSystemAPI(Integer.parseInt(recordId));
			model.addAttribute("sysDetails", systemAPI);
			model.addAttribute("dept_list", Utils.getDepartmentList());
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("tc")) {
			List<Application> applicationList = applicationService.getApplicationList();
			List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();

			Map<String, String> tabs = new LinkedHashMap<String, String>();
			tabs.put("0", "Tab_1");
			tabs.put("1", "Tab_2");
			tabs.put("2", "Tab_3");
			tabs.put("3", "Tab_4");
			Map<String, String> sorted_tabs = new TreeMap<String, String>(tabs);

			Map<String, String> tcTypeList = new LinkedHashMap<String, String>();
			tcTypeList.put("Automated", "Automated");
			tcTypeList.put("Manual", "Manual");
			Map<String, String> sorted_tcTypeList = new TreeMap<String, String>(tcTypeList);
			
			model.addAttribute("app_list", applicationList);
			model.addAttribute("tabs", sorted_tabs);
			model.addAttribute("suite_list", testSuiteList);
			model.addAttribute("tcType_list", sorted_tcTypeList);

			testSuiteList = testSuiteService.getTestSuiteList(null);
			model.addAttribute("_suite_list", testSuiteList);

			testCase = testCaseService.getTestCaseByAppId(Integer.parseInt(recordId));
			model.addAttribute("tcDetails", testCase);
		}

		return "create/create_home";
	}

	@RequestMapping(value = "/update")
	public String handleUpdateRequest(
			/*@RequestParam("paramName") String paramName,
			@RequestParam("apiId") String apiId,
			@RequestParam("deptType") String deptType,*/
			HttpServletRequest request,
			@ModelAttribute Application application,
			@ModelAttribute Environment environment,
			@ModelAttribute TestSuite testSuite,
			@ModelAttribute TestBed testBed,
			@ModelAttribute TimePeriod timePeriod,
			@ModelAttribute TestCase testCase,
			@ModelAttribute SystemAPI systemAPI, Model model) {

		logger.info("Inside MainController :: handleUpdateRequest() :: paramName" + request.getParameter("paramName"));

		HashMap<String, String> pName = new HashMap<String, String>();
		pName.put("paramName", String.valueOf(request.getParameter("paramName")));
		model.addAttribute("paramName", pName);

		String redirectUrl = "redirect:/manage?paramName=" + request.getParameter("paramName") + "&page=&orderBy=&orderType=";

		if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("app")) {
			applicationService.updateData(application);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("env")) {
			environmentService.updateData(environment);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("suite")) {
			testSuiteService.updateData(testSuite);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("bed")) {
			testBedService.updateData(testBed);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("period")) {
			timePeriodService.updateData(timePeriod);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("sys")) {
			systemAPIService.updateData(systemAPI);
		} else if (String.valueOf(request.getParameter("paramName")).equalsIgnoreCase("tc")) {
			testCaseService.updateData(testCase);
		}

		return redirectUrl;
	}

	@RequestMapping(value = "/delete")
	public String handleDeleteRequest(
			@RequestParam("paramName") String paramName,
			@RequestParam("recordId") String recordId, Model model) {
		logger.info("Inside MainController :: handleDeleteRequest() :: paramName" + paramName);

		HashMap<String, String> pName = new HashMap<String, String>();
		pName.put("paramName", String.valueOf(paramName));
		model.addAttribute("paramName", pName);

		String redirectUrl = "redirect:/manage?paramName=" + paramName + "&page=&orderBy=&orderType=";

		if (String.valueOf(paramName).equalsIgnoreCase("app")) {
			applicationService.deleteData(/*Integer.parseInt(*/recordId/*)*/);
		} else if (String.valueOf(paramName).equalsIgnoreCase("env")) {
			environmentService.deleteData(/*Integer.parseInt(*/recordId/*)*/);
		} else if (String.valueOf(paramName).equalsIgnoreCase("suite")) {
			testSuiteService.deleteData(/*Integer.parseInt(*/recordId/*)*/);
		} else if (String.valueOf(paramName).equalsIgnoreCase("bed")) {
			testBedService.deleteData(/*Integer.parseInt(*/recordId/*)*/);
		} else if (String.valueOf(paramName).equalsIgnoreCase("period")) {
			timePeriodService.deleteData(Integer.parseInt(recordId));
		} else if (String.valueOf(paramName).equalsIgnoreCase("sys")) {
			systemAPIService.deleteData(/*Integer.parseInt(*/recordId/*)*/);
		} else if (String.valueOf(paramName).equalsIgnoreCase("tc")) {
			testCaseService.deleteData(/*Integer.parseInt(*/recordId/*)*/);
		}

		return redirectUrl;
	}

	@RequestMapping(value = "/manage")
	public String handleManageRequest(
			@RequestParam("paramName") String paramName,
			@RequestParam("page") String page,
			@RequestParam("orderBy") String orderBy,
			@RequestParam("orderType") String orderType, Model model) {
		logger.info("Inside MainController :: handleManageRequest() :: paramName" + paramName);

		HashMap<String, String> pName = new HashMap<String, String>();
		pName.put("paramName", String.valueOf(paramName));
		model.addAttribute("paramName", pName);

		List<Application> applicationList = null;
		List<Environment> environmentList = null;
		List<TestSuite> testSuiteList = null;
		List<TestBed> testBedList = null;
		List<TimePeriod> timePeriodList = null;
		List<SystemAPI> systemList = null;
		List<TestCase> testCaseList = null;

		int pageNo = 1;
		int recordsPerPage = 7;
		int noOfRecords = 0;
		int noOfPages = 0;

		if (page != null && !page.equalsIgnoreCase(""))
			pageNo = Integer.parseInt(page);

		if (String.valueOf(paramName).equalsIgnoreCase("app")) {
			applicationList = applicationService.getAllApplicationList(orderBy, orderType, ((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = applicationService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("app_list", applicationList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orderBy", orderBy);
			model.addAttribute("orderType", orderType);
		} else if (String.valueOf(paramName).equalsIgnoreCase("env")) {
			environmentList = environmentService.getAllEnvironmentList(orderBy, orderType, ((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = environmentService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("envList", environmentList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orderBy", orderBy);
			model.addAttribute("orderType", orderType);
		} else if (String.valueOf(paramName).equalsIgnoreCase("suite")) {
			testSuiteList = testSuiteService.getAllTestSuiteList(orderBy, orderType, ((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = testSuiteService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("suiteList", testSuiteList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orderBy", orderBy);
			model.addAttribute("orderType", orderType);
		} else if (String.valueOf(paramName).equalsIgnoreCase("bed")) {
			testBedList = testBedService.getAllTestBedList(orderBy, orderType, ((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = testBedService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("bedList", testBedList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orderBy", orderBy);
			model.addAttribute("orderType", orderType);
		} else if (String.valueOf(paramName).equalsIgnoreCase("period")) {
			timePeriodList = timePeriodService.getAllTimePeriodList(((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = timePeriodService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("periodList", timePeriodList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
		} else if (String.valueOf(paramName).equalsIgnoreCase("sys")) {
			systemList = systemAPIService.getAllSystemAPIList(orderBy, orderType, ((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = systemAPIService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("sysList", systemList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orderBy", orderBy);
			model.addAttribute("orderType", orderType);
		} else if (String.valueOf(paramName).equalsIgnoreCase("tc")) {
			testCaseList = testCaseService.getAllTestCaseList(orderBy, orderType, ((pageNo - 1) * recordsPerPage), recordsPerPage);
			noOfRecords = testCaseService.getTotalRowCount();
			noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

			model.addAttribute("tcList", testCaseList);
			model.addAttribute("noOfPages", noOfPages);
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("orderBy", orderBy);
			model.addAttribute("orderType", orderType);
		}

		return "manage/manage_home";
	}

/*	@RequestMapping(value = "/jenkins")
	public String handleJenkinsRequest(Model model) throws IOException, JSONException {
//		System.out.println("Inside MainController :: handleJenkinsRequest");

		List<Application> applicationList = applicationService.getUrlAliasList(3, "jenkins");
		// List<Environment> environmentList =
		// environmentService.getAllEnvironmentList(0, 0);
		// List<TestSuite> testSuiteList =
		// testSuiteService.getAllTestSuiteList(0, 0);
		// List<TestBed> testBedList = testBedService.getAllTestBedList(0, 0);

		JenkinsDataPullAPI pullData = new JenkinsDataPullAPI();
		List<CI> ciList = pullData.pullJenkinsData(applicationList);

		ciService.insertData(ciList);

		return "create/create_home";
	}*/

	@RequestMapping(value = "/GetQuarterList", method = RequestMethod.POST)
	@ResponseBody
	public String GetQuarterList(@RequestParam("app_id") String app_id) {
		int monthId = ((Application) applicationService.getApplication(Integer.parseInt(app_id),0,null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		String jsonString = null;
		Data dataMap = new Data();
		dataMap.getData().put("QuarterList", getActualTimePeriodList);
		try {
			jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
		} catch (Exception e) {
		}
		return jsonString;
	}

	@RequestMapping(value = "/deleteUpdateTCMByAjaxCall", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUpdateTCMByAjaxCall(@RequestParam("action") String action,
			@RequestParam("recordId") String recordId,
			@RequestParam("suiteId") String suiteId,
			@RequestParam("tcType") String tcType,
			@RequestParam("tcCount") String tcCount) {
//		System.out.println("Inside MainController :: deleteUpdateTCMByAjaxCall");

		String msg = testCaseService.deleteUpdateTCMByAjaxCall(action, recordId, suiteId, tcType, tcCount);

		return msg;
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String getDashboard(Model model) {
		System.out.println("Inside MainController :: getDashboard");

		HashMap<String, Object> filterOptions = new HashMap<String, Object>();

		try {
		// for priority
		filterOptions.put("defectCase", "priority");
		List<Defects> defectsPriorityDetails = dashboardService.getDetails(filterOptions); // details from jira table for displaying pie-chart
		
		// for severity
		filterOptions.put("defectCase", "severity");
		List<Defects> defectsSeverityDetails = dashboardService.getDetails(filterOptions); // details from jira table for displaying pie-chart
		
		// for jenkins build
		filterOptions.put("jenkins", "");
		List<CI> jenkinsDetails = dashboardService.getJenkinsDetails(filterOptions);

		List<Application> applicationList = applicationService.getDashBoardApplicationList();
		model.addAttribute("appList",applicationList);

		String jsonObj = getDataString(defectsPriorityDetails,defectsSeverityDetails, jenkinsDetails, model);
		model.addAttribute("jsonString", jsonObj);
		} catch (Exception e) {
			logger.error("ERROR :: getDashboard() :: " + e.getMessage());
		}
		return "dashboard";
	}
	
	@RequestMapping(value = "/dashboardhome", method = RequestMethod.GET)
	public String getDashboardUI(Model model) {

		HashMap<String, Object> filterOptions = new HashMap<String, Object>();

		try {
			List<ChartModel> chartsList = new ArrayList<ChartModel>();
			List<com.etaap.domain.CI> jenkinsDetails =  new ArrayList<com.etaap.domain.CI>();
			
			chartsList.add(new ChartModel("eTAAPHomePriorityDrillDown", ChartType.DRILLDOWN_PIE, 
					new Object[] {Utils.getFirstDayOfPrevTwoMonths()}, new Object[] {Utils.getFirstDayOfPrevTwoMonths()}));
			
			chartsList.add(new ChartModel("eTAAPHomeSeverityDrillDown", ChartType.DRILLDOWN_PIE, 
					new Object[] {Utils.getFirstDayOfPrevTwoMonths()}, new Object[] {Utils.getFirstDayOfPrevTwoMonths()}));
			
			chartsList.add(new ChartModel("eTAAPHomeJenkinsStackColumn", ChartType.COLUMN,
					Arrays.asList(new String[]{"Passed","Failed","Skipped"}),
					new Object[] {Utils.getFirstDayOfPrevTwoMonths()}));
			
			chartsList.add(new ChartModel("eTAAPHomeTestCaseStackQuery", ChartType.STACKED_COLUMN, 
					new Object[] {Utils.getFirstDayOfPrevTwoMonths()}));
			
			chartsList.add(new ChartModel("eTAAPHomeDefectsStatistics", ChartType.MULTILINE_STACKEDBAR_FIXED_CATEGORY, 
					getDefectsStatisticsCategoriesValue(),
					new Object[] {
						Utils.getDate(false, "MONTH_DAYOFMONTH_ADD_2_AS_VALUE", "yyyy-MM-dd").concat(" 00:00:00"),
						Utils.getDate(false, "MONTH_DAYOFMONTH_LAST_DAY_CURRENT_Month", "yyyy-MM-dd").concat(" 23:59:59")}));
			
			chartsList.add(new ChartModel("eTAAPHomeDefectsLife", ChartType.MULTILINE_STACKEDBAR_FIXED_CATEGORY, 
					Arrays.asList(new String[]{"Day","Week","Month"}),
					new Object[] {
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 00:00:00"),
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 23:59:59"),
						Utils.getDate(false, "WEEK", "yyyy-MM-dd").concat(" 00:00:00"),
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 23:59:59"),
						Utils.getDate(false, "MONTH", "yyyy-MM-dd").concat(" 00:00:00"),
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 23:59:59")},
					new Object[] {
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 00:00:00"),
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 23:59:59"),
						Utils.getDate(false, "WEEK", "yyyy-MM-dd").concat(" 00:00:00"),
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 23:59:59"),
						Utils.getDate(false, "MONTH", "yyyy-MM-dd").concat(" 00:00:00"),
						Utils._getDate(true, null, "yyyy-MM-dd").concat(" 23:59:59")}
					));
			
			chartsList.add(new ChartModel("eTAAPHomeSprintVelocity", ChartType.COLUMN_SECONDARY_CATEGORY, 
					Arrays.asList(new String[]{"Commited","Completed"}), new Object[] {}));
			
			/*chartsList.add(new ChartModel("eTAAPHomeWaterTank", ChartType.WATER_TANK, 
					new Object[] {"12","25773"}, new Object[] {"12","25773"}));*/
			
			String charts = chartService.getCharts(servletContext, chartsList);
			model.addAttribute("jsonString", charts);
		} catch (Exception e) {
			logger.error("ERROR :: getDashboard() :: " + e.getMessage());
		}
		
		return "dashboard_home";
	}

	public String getDataString(List<Defects> defectsPriorityDetails, List<Defects> defectsSeverityDetails, List<CI> jenkinsDetails, Model model) {
		String dataString = null;
		Data dataMap = new Data();
		boolean severityflag = false;
		com.etaap.beans.Defects defects = new com.etaap.beans.Defects();
		List<Priority> defectsPriorityList = new ArrayList<Priority>();
		List<Object> defectsPriorityDrilldownList = new ArrayList<Object>();
		int allPriorityCount = 0;
		List<Severity> defectsSeverityList = new ArrayList<Severity>();
		List<Object> defectsSeverityDrilldownList = new ArrayList<Object>();
		int AllSeverityCount = 0;
		
		int allPrioritySCount = 0;
		List<Priority> defectsPrioritySList = new ArrayList<Priority>();
		List<Object> defectsPrioritySDrilldownList = new ArrayList<Object>();
		
		
		int k = 1;
		int i=0;
		while(i < defectsPriorityDetails.size()) {
			Defects def = (Defects) defectsPriorityDetails.get(i);
			int count = def.getPriorityCount();

			Priority priority = new Priority();
			priority.setColor(ApplicationColor.INDEX.getColorCode(String.valueOf(k)));
			priority.setName(def.getProjectName());
			
			priority.setPriority(def.getPriority());
			
			priority.setDrilldown(def.getProjectName().toLowerCase());
			
			priority.setPriorityLabel(def.getPriorityLabel());

			Priority priorityDrilldown = new Priority();
			priorityDrilldown.setId(def.getProjectName().toLowerCase());
			priorityDrilldown.setName(def.getProjectName());
			priorityDrilldown.setShowInLegend(true);

			List<Priority> dataObj = new ArrayList<Priority>();
			Priority dataDrillObj = new Priority();
			dataDrillObj.setName(def.getPriority());
			dataDrillObj.setCount(def.getPriorityCount());
			dataDrillObj.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def.getPriority()));
			dataObj.add(dataDrillObj);

			int j = i+1;
			while (j<defectsPriorityDetails.size()) {
				Defects def1 = (Defects) defectsPriorityDetails.get(j);
				if (def.getAppId()==def1.getAppId()) {
					dataDrillObj = new Priority();
					dataDrillObj.setName(def1.getPriority());
					dataDrillObj.setCount(def1.getPriorityCount());
					dataDrillObj.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def1.getPriority()));
					dataObj.add(dataDrillObj);

					count = count + def1.getPriorityCount();
					j++;
				} else {
					i = j;
					break;
				}
			}

			k++;
			allPriorityCount = allPriorityCount + count;
			priority.setCount(count);
			defectsPriorityList.add(priority);
			priorityDrilldown.setData(dataObj);
			defectsPriorityDrilldownList.add(priorityDrilldown);

			if (j == defectsPriorityDetails.size())
				break;
		}
		defects.getDefectCases().put("Priority", defectsPriorityList);
		defects.getDefectCases().put("PriorityDrilldown", defectsPriorityDrilldownList);
		defects.getDefectCases().put("AllPriorityCount", allPriorityCount);
		defects.getDefectCases().put("PriorityKey", "Priority");
		//defects.getDefectCases().put("PriorityWiseCount", defectsPriorityDrilldownList);

		k=1;
		i=0;
		logger.info("defectsSeverityDetails size :: "+defectsSeverityDetails.size());
		
		while(i < defectsSeverityDetails.size()) {
			Defects def = (Defects) defectsSeverityDetails.get(i);
			
			int count = def.getSeverityCount();
			
			if(count > 0)
			{
			severityflag = true;
			Severity severity = new Severity();
			severity.setColor(ApplicationColor.INDEX.getColorCode(String.valueOf(k)));
			severity.setName(def.getProjectName());
			severity.setDrilldown(def.getProjectName().toLowerCase());
			
			Severity severityDrilldown = new Severity();
			severityDrilldown.setId(def.getProjectName().toLowerCase());
			severityDrilldown.setName(def.getProjectName());
			severityDrilldown.setShowInLegend(true);

			List<Severity> dataObj = new ArrayList<Severity>();
			Severity dataDrillObj = new Severity();
			dataDrillObj.setName(def.getSeverity());
			dataDrillObj.setCount(def.getSeverityCount());
			if(def.getSeverity() != null)
			dataDrillObj.setColor(SeverityColor.SEVERITY_NAME.getColorCode(def.getSeverity()));
			dataObj.add(dataDrillObj);

			int j = i+1;
			while (j<defectsSeverityDetails.size()) {
				Defects def1 = (Defects) defectsSeverityDetails.get(j);
				
				if (def.getAppId()==def1.getAppId()) {
					dataDrillObj = new Severity();
					
					dataDrillObj.setName(def1.getSeverity());
					dataDrillObj.setCount(def1.getSeverityCount());
					if(def1.getSeverity() != null)
					dataDrillObj.setColor(SeverityColor.SEVERITY_NAME.getColorCode(def1.getSeverity()));
					dataObj.add(dataDrillObj);
					count = count + def1.getSeverityCount();
					j++;
				} else {
					i = j;
					break;
				}
			}

			k++;
			AllSeverityCount = AllSeverityCount + count;
			severity.setCount(count);
			defectsSeverityList.add(severity);
			severityDrilldown.setData(dataObj);
			defectsSeverityDrilldownList.add(severityDrilldown);

			if (j == defectsSeverityDetails.size())
				break;
			} 
		else {
				int countP = def.getPriorityCount();
				Priority priority = new Priority();
				priority.setColor(ApplicationColor.INDEX.getColorCode(String.valueOf(k)));
				priority.setName(def.getProjectName());
				priority.setDrilldown(def.getProjectName().toLowerCase());
				
				Priority priorityDrilldown = new Priority();
				priorityDrilldown.setId(def.getProjectName().toLowerCase());
				priorityDrilldown.setName(def.getProjectName());
				priorityDrilldown.setShowInLegend(true);

				List<Priority> dataObj = new ArrayList<Priority>();
				Priority dataDrillObj = new Priority();
				
				dataDrillObj.setName(def.getPriority());
				dataDrillObj.setCount(def.getPriorityCount());
				dataDrillObj.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def.getPriority()));
				dataObj.add(dataDrillObj);

				int j = i+1;
				while (j<defectsSeverityDetails.size()) {
					Defects def1 = (Defects) defectsSeverityDetails.get(j);
					
					if (def.getAppId()==def1.getAppId()) {
						dataDrillObj = new Priority();
						
						dataDrillObj.setName(def1.getPriority());
						dataDrillObj.setCount(def1.getPriorityCount());
						dataDrillObj.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def1.getPriority()));
						
						dataObj.add(dataDrillObj);
						countP = countP + def1.getPriorityCount();
						
						j++;
					} else {
						i = j;
						break;
					}
				}

				k++;
								
				allPrioritySCount = allPrioritySCount + countP;
				priority.setCount(countP);
				defectsPrioritySList.add(priority);
				priorityDrilldown.setData(dataObj);
				defectsPrioritySDrilldownList.add(priorityDrilldown);

				
				if (j == defectsSeverityDetails.size())
					break;
			}
			
		}
		if(severityflag){
		defects.getDefectCases().put("Severity", defectsSeverityList);
		defects.getDefectCases().put("SeverityDrilldown", defectsSeverityDrilldownList);
		defects.getDefectCases().put("AllSeverityCount", AllSeverityCount);
		defects.getDefectCases().put("Key", "Severity");
		//defects.getDefectCases().put("SeverityWiseCount", defectsSeverityDrilldownList);
		} else {
		defects.getDefectCases().put("Severity", defectsPrioritySList);
		defects.getDefectCases().put("SeverityDrilldown", defectsPrioritySDrilldownList);
		defects.getDefectCases().put("AllSeverityCount", allPrioritySCount);
		defects.getDefectCases().put("Key", "Priority");
		}
		
		dataMap.getData().put("Defects", defects);

		HashMap<String, Object> csvForJenkinsChart = getCsvForJenkinsChart(jenkinsDetails);
		com.etaap.beans.CI ci = new com.etaap.beans.CI();
		ci.setAppNameCSV((List<String>) csvForJenkinsChart.get("appNameCSV"));
		ci.setBuildCountCSV((List<Integer>) csvForJenkinsChart.get("buildCountCSV"));
		ci.setFailCountCSV((List<Integer>) csvForJenkinsChart.get("failCountCSV"));
		ci.setPassCountCSV((List<Integer>) csvForJenkinsChart.get("passCountCSV"));
		ci.setSkipCountCSV((List<Integer>) csvForJenkinsChart.get("skipCountCSV"));
		dataMap.getData().put("Jenkins", ci);
		
		/* ******************************** TCM  ********************************** */
		List<Tcm> tcmGsonString = dashboardService.getTcmDashBoardChartString();
		dataMap.getData().put("Tcm", tcmGsonString);
		
		/* 6th graph on Dashboard -- Defects Statistics */
		DefectsStatistics defectsStatistics = getDefectsStatistics();
		dataMap.getData().put("defectsStatistics", defectsStatistics);
		/* 6th graph on Dashboard -- Defects Statistics */
		
		/* 5th graph on Dashboard -- Defects Life */
		DefectsLife defectsLife = dashboard_DefectsLife();
		dataMap.getData().put("defectsLife", defectsLife);
		/* 5th graph on Dashboard -- Defects Life */
		
		CommitedCompletedUserStories commitedCompletedUserStories = CommitedCompletedUserStories.getCommitedCompletedUserStories(dashboardService);
		dataMap.getData().put("commitedCompletedUserStories", commitedCompletedUserStories);
		
		try {
			dataString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
			//System.out.println("*&*&*&*& JSON "+dataString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataString;
	}

	public HashMap<String, Object> getCsvForJenkinsChart(List<CI> ciDetails) {
		logger.info("Inside MainController :: getCsvForJenkinsChart()");

		List<String> appNameCSV = new ArrayList<String>();
		//List<Integer> buildCountCSV = new ArrayList<Integer>();
		List<String> failCountCSV = new ArrayList<String>();
		List<String> passCountCSV = new ArrayList<String>();
		List<String> skipCountCSV = new ArrayList<String>();

		for (CI ci : ciDetails) {
			appNameCSV.add(String.valueOf(ci.getAppName()));
			//buildCountCSV.add(Integer.parseInt(String.valueOf(ci.getBuildCount())));
			failCountCSV.add(String.valueOf(ci.getFailCount()));
			passCountCSV.add(String.valueOf(ci.getPassCount()));
			skipCountCSV.add(String.valueOf(ci.getSkipCount()));
		}

		HashMap<String, Object> csvMap = new HashMap<String, Object>();
		csvMap.put("appNameCSV", appNameCSV);
		//csvMap.put("buildCountCSV", buildCountCSV);
		csvMap.put("failCountCSV", failCountCSV);
		csvMap.put("passCountCSV", passCountCSV);
		csvMap.put("skipCountCSV", skipCountCSV);

		return csvMap;
	}

	public List<String> getDefectsStatisticsStatusValue() {
		List<String> statusList = null;
		try {
			statusList = new ArrayList<String>();
			statusList.add("Closed");
			statusList.add("New");
			statusList.add("In Progress");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusList;
	}

	public List<String> getDefectsStatisticsCategoriesValue() {
		List<String> categoriesList = null;
		try {
			categoriesList = new LinkedList<String>();
			categoriesList.add(Utils.getMonthName(-2));
			categoriesList.add(Utils.getMonthName(-1));
			categoriesList.add(Utils.getMonthName(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoriesList;
	}

	public DefectsLife dashboard_DefectsLife() {
		DefectsLife defectsLife = new DefectsLife();
		Map<String,List<Integer>> defectsMap = new HashMap<String,List<Integer>> ();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", "Verify");
		
		params.put("date", Utils._getDate(true, null, "yyyy-MM-dd"));
		
		List<Map<String, Object>> dayDefectsLifeList = dashboardService.getDefectsLife(params);
		logger.info("Inside DashboardDaoImpl :: dayDefectsLifeList() :: " + dayDefectsLifeList.size());
		params.remove("date");
		
		//params.put("to", Utils._getDate(true, null, "yyyy-MM-dd") + " 23:59:59");
		params.put("to", Utils.getDate(false, "PREVIOUS_DAY", "yyyy-MM-dd") + " 23:59:59");
		//String val = Utils.getDate(false, "WEEK", "yyyy-MM-dd");
		 String val = Utils.getDate(false, "PRE_ONE_WEEK", "yyyy-MM-dd");
		params.put("from", val+ " 00:00:00");
		List<Map<String, Object>> weekDefectsLifeList = dashboardService.getDefectsLife(params);
		logger.info("Inside DashboardDaoImpl :: weekDefectsLifeList() :: " + weekDefectsLifeList.size());
		//params.put("to", Utils._getDate(true, null, "yyyy-MM-dd") + " 23:59:59");
		params.put("to", Utils.getDate(false, "PRE_WEEK_DAY", "yyyy-MM-dd") + " 23:59:59");
		val = Utils.getDate(false, "PRE_ONE_MONTH", "yyyy-MM-dd");
		params.put("from", val+ " 00:00:00");
		
		
		List<Map<String, Object>> monthDefectsLifeList = dashboardService.getDefectsLife(params);
		logger.info("Inside DashboardDaoImpl :: monthDefectsLifeList() :: " + monthDefectsLifeList.size());
		
	// code for making json object specific defectsLife
		
		defectsLife.getCategories().add("Day");
		defectsLife.getCategories().add("Week");
		defectsLife.getCategories().add("Month");
		
		if(dayDefectsLifeList!=null && dayDefectsLifeList.size()>0){
			List<Integer> daySeverList = new ArrayList<Integer>();
			for(Map map : dayDefectsLifeList){
				if(map.get("severityCount") != null){
					int severityCount = (Integer.parseInt(map.get("severityCount").toString()));
					String severity = map.get("severity").toString();
					List<Integer> a = new ArrayList<Integer>();
					a.add(severityCount);
					defectsMap.put(severity, a);
					defectsLife.getCategories().add("severity");
				} else {
					int priorityCount = (Integer.parseInt(map.get("priorityCount").toString()));
					String priority = map.get("priority").toString();
					List<Integer> a = new ArrayList<Integer>();
					a.add(priorityCount);
					defectsMap.put(priority, a);
				}
				defectsLife.getCategories().add("priority");
				
			}
		}

		if(weekDefectsLifeList!=null && weekDefectsLifeList.size() > 0){
			for(Map map : weekDefectsLifeList){
				
				if(map.get("severityCount") != null){
					int severityCount = (Integer.parseInt(map.get("severityCount").toString()));
					String severity = map.get("severity").toString();
					if(defectsMap.get(severity)!=null){
						List getList = defectsMap.get(severity);
						getList.add(severityCount);
						/*if(getList!=null && getList.size()>1){
							getList.set(1, severityCount);
						}else{
							getList.add(0);
							getList.add(severityCount);
						}*/
					}else{
						List<Integer> weekSeverList = new ArrayList<Integer>();
						weekSeverList.add(0);
						weekSeverList.add(severityCount);
						defectsMap.put(severity, weekSeverList);
					}
					defectsLife.getCategories().add("severity");
				} else {
					int priorityCount = (Integer.parseInt(map.get("priorityCount").toString()));
					String priority = map.get("priority").toString();
					if(defectsMap.get(priority)!=null){
						List getList = defectsMap.get(priority);
						getList.add(priorityCount);
						/*if(getList!=null && getList.size()>1){
							getList.set(1, severityCount);
						}else{
							getList.add(0);
							getList.add(severityCount);
						}*/
					}else{
						List<Integer> weekSeverList = new ArrayList<Integer>();
						weekSeverList.add(0);
						weekSeverList.add(priorityCount);
						defectsMap.put(priority, weekSeverList);
					}
					defectsLife.getCategories().add("priority");
				}
			
			}
		}
		
		if(monthDefectsLifeList!=null && monthDefectsLifeList.size()>0){
			
			for(Map map : monthDefectsLifeList){
			
				if(map.get("severityCount") != null){
					int severityCount = (Integer.parseInt(map.get("severityCount").toString()));
					String severity = map.get("severity").toString();
					
					if(defectsMap.get(severity)!=null){
						List<Integer> getList = defectsMap.get(severity);
						//getList.set(2, severityCount);
						if(getList!=null && getList.size() == 1){
							getList.add(0);
							getList.add(severityCount);
						}else if(getList!=null && getList.size() == 2){
							
							getList.add(severityCount);
						}
						/*else{
							getList.add(0);
							getList.add(severityCount);
						}*/
					}else{
						List<Integer> monthSeverList = new ArrayList<Integer>();
						monthSeverList.add(0);
						monthSeverList.add(0);
						monthSeverList.add(severityCount);
						defectsMap.put(severity, monthSeverList);
					}
					defectsLife.getCategories().add("severity");
				} else {
					int priorityCount = (Integer.parseInt(map.get("priorityCount").toString()));
					String priority = map.get("priority").toString();
					
					if(defectsMap.get(priority)!=null){
						List<Integer> getList = defectsMap.get(priority);
						//getList.set(2, severityCount);
						if(getList!=null && getList.size() == 1){
							getList.add(0);
							getList.add(priorityCount);
						}else if(getList!=null && getList.size() == 2){
							
							getList.add(priorityCount);
						}
						/*else{
							getList.add(0);
							getList.add(severityCount);
						}*/
					}else{
						List<Integer> monthSeverList = new ArrayList<Integer>();
						monthSeverList.add(0);
						monthSeverList.add(0);
						monthSeverList.add(priorityCount);
						defectsMap.put(priority, monthSeverList);
					}
					defectsLife.getCategories().add("priority");
				}
				
			}
		}

		// code for making json object specific defectsLife
		
		defectsLife.getCategories().add("Day");
		defectsLife.getCategories().add("Week");
		defectsLife.getCategories().add("Month");

		Set<String> set = defectsMap.keySet();
		Iterator<String> setItr = set.iterator();
		while(setItr.hasNext()){
			String key = setItr.next();
			List<Integer> severList = defectsMap.get(key);
			Series series = new Series();
			series.setName(key);
			series.setData(severList);
			defectsLife.getSeries().add(series);
		}
		
		return defectsLife;
	}

	/*
	 * jiten -- added for logic change -- defects Statistics Chart - Dashboard
	 */
	static private Map<Integer, String> mapMonth = new HashMap<Integer, String>();
	static {
		mapMonth.put(1, "Jan");
		mapMonth.put(2, "Feb");
		mapMonth.put(3, "Mar");
		mapMonth.put(4, "Apr");
		mapMonth.put(5, "May");
		mapMonth.put(6, "Jun");
		mapMonth.put(7, "Jul");
		mapMonth.put(8, "Aug");
		mapMonth.put(9, "Sep");
		mapMonth.put(10, "Oct");
		mapMonth.put(11, "Nov");
		mapMonth.put(12, "Dec");
	}

	public DefectsStatistics getDefectsStatistics(){
		DefectsStatistics defectsStatistics = new DefectsStatistics();
		try{
			Map paramsMap = new HashMap();
			paramsMap.put("statusIn",getDefectsStatisticsStatusValue());
			String val = null;//Utils.getDate(true, null, "yyyy-MM-dd");
			/*paramsMap.put("to", val+ " 23:59:59");
			val = Utils.getDate(false,"MONTH_DAYOFMONTH", "yyyy-MM-dd");
			paramsMap.put("from", val+ " 00:00:00");*/

			val = Utils.getDate(true, null, "yyyy-MM-dd");
			paramsMap.put("from", val+ " 00:00:00");
			val = Utils.getDate(false, "MONTH_DAYOFMONTH_LAST_DAY_CURRENT_Month", "yyyy-MM-dd");
			paramsMap.put("to", val+ " 23:59:59");

			List<Map<String, Object>> defectsStatisticsList = new ArrayList<Map<String, Object>>();
			
			List <Map<String, Object>> firstList = dashboardService.getDefectsStatistics(paramsMap);
			
			val = Utils.getDate(false, "MONTH_DAYOFMONTH_ADD_1_AS_VALUE", "yyyy-MM-dd");
			paramsMap.put("from", val+ " 00:00:00" );
			val = Utils.getDate(false, "MONTH_DAYOFMONTH_LAST_DAY", "yyyy-MM-dd");
			paramsMap.put("to", val+ " 23:59:59" );

			List<Map<String,Object>> secondList = dashboardService.getDefectsStatistics(paramsMap);

			val = Utils.getDate(false, "MONTH_DAYOFMONTH_ADD_2_AS_VALUE", "yyyy-MM-dd");
			paramsMap.put("from", val+ " 00:00:00" );
			val = Utils.getDate(false, "MONTH_DAYOFMONTH_LAST_DAY_Month2Minus", "yyyy-MM-dd");
			paramsMap.put("to", val+ " 23:59:59" );

			List<Map<String,Object>> thirdList = dashboardService.getDefectsStatistics(paramsMap);

			if(firstList!=null && firstList.size()>0){
				for(Map<String,Object> map : firstList){
					defectsStatisticsList.add(map);
				}
			}
			if(secondList!=null && secondList.size()>0){
				for(Map<String,Object> map : secondList){
					defectsStatisticsList.add(map);
				}
			}
			if(thirdList!=null && thirdList.size()>0){
				for(Map<String,Object> map : thirdList){
					defectsStatisticsList.add(map);
				}
			}
			
			//DefectsStatistics defectsStatistics = new DefectsStatistics();
			if(defectsStatisticsList!=null && defectsStatisticsList.size()>0){
				
			 List<String> defectsStatisticsCategoriesVal = getDefectsStatisticsCategoriesValue();
			 defectsStatistics.setCategories(defectsStatisticsCategoriesVal);
			 
			 Map<String,Integer[]> defectsStatsMap = new HashMap<String,Integer[]>();
				
				for(Map map : defectsStatisticsList){
					int statisticsCount = Integer.parseInt(map.get("status_count").toString());
					String statisticsName = map.get("status")!=null ? map.get("status").toString() : null;
					int orderMonth = Integer.parseInt(map.get("OrderMonth").toString());
					if(statisticsName!=null){
						if((defectsStatsMap.size() == 0) || (defectsStatsMap.get(statisticsName) == null)){
							Integer[] monthArr = new Integer[defectsStatisticsCategoriesVal.size()]; 
							int ii=0;
							for(String str : defectsStatisticsCategoriesVal){
								if(str.contains(mapMonth.get(orderMonth).toString())){
									monthArr[ii] = statisticsCount;
								}
								ii++;
							}
							defectsStatsMap.put(statisticsName, monthArr);
						}
						else if(defectsStatsMap.get(statisticsName)!=null){
							Integer[] monthArr = defectsStatsMap.get(statisticsName);
							int ii=0;
							for(String str : defectsStatisticsCategoriesVal){
								if(str.contains(mapMonth.get(orderMonth).toString())){
									monthArr[ii] = statisticsCount;
								}
								ii++;
							}
							defectsStatsMap.put(statisticsName, monthArr);
						}
					}
				}
				
				Set<String> set = defectsStatsMap.keySet();
				Iterator<String> itr = set.iterator();
				while(itr.hasNext()){
					String statisticsNameVal = itr.next();
					Integer[] monthArr = defectsStatsMap.get(statisticsNameVal);
					monthArr = getRefinedDefectsStatsArr(monthArr);
					Series series = new Series();
					series.setName(statisticsNameVal);
					series.setData((Arrays.asList(monthArr)));
					defectsStatistics.getSeries().add(series);
				}
				
			}
			//String jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(defectsStatistics);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return defectsStatistics;
	}
	
	private Integer[] getRefinedDefectsStatsArr(Integer[] monthArr) {
		Integer[] monthArrVal = monthArr;
		Integer[] monthArrValToReturn = null;
		int monthCount = 0;
		if (monthArrVal != null && monthArrVal.length > 0) {
			monthArrValToReturn = new Integer[monthArr.length];
			for (Integer intval : monthArrVal) {
				Integer a;
				if (intval == null || intval.equals("null"))
					a = 0;
				else
					a = intval;

				monthArrValToReturn[monthCount] = a;
				monthCount++;
			}
		}
		return monthArrValToReturn;
	}

	@RequestMapping(value = "/isNameExist", method = RequestMethod.POST)
	@ResponseBody
	public String isNameExistChkByAjaxCall(@RequestParam("type") String type, @RequestParam("id") int id, @RequestParam("name") String name) {
//		System.out.println("Inside MainController :: isNameExistChkByAjaxCall");

		String flag = "";
		if (type.equalsIgnoreCase("app"))
			flag = applicationService.isNameExistChkByAjaxCall(id, name);
		else if (type.equalsIgnoreCase("env"))
			flag = environmentService.isNameExistChkByAjaxCall(id, name);
		else if (type.equalsIgnoreCase("suite"))
			flag = testSuiteService.isNameExistChkByAjaxCall(id, name);
		else if (type.equalsIgnoreCase("bed"))
			flag = testBedService.isNameExistChkByAjaxCall(id, name);
		else if (type.equalsIgnoreCase("sys"))
			flag = systemAPIService.isNameExistChkByAjaxCall(id, name);

		return flag;
	}
	
	@PreDestroy
	public void stop() {
		SendMailServer.stop();
	}
	
	@RequestMapping(value = "/disableRecordForApp", method = RequestMethod.POST)
	 @ResponseBody
	 public String disableRecordForAppChkByAjaxCall(@RequestParam("mapids") String mapids) {
	  System.out.println("Inside MainController :: disableRecordForAppcall");

	  String flag = "";

	   flag = applicationService.disableRecordForApp(mapids);

	  return flag;
	 }
	

}
