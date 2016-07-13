package com.etaap.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.etaap.services.StoryPointService;
import com.etaap.dao.ApplicationDaoImpl;
import com.etaap.dao.DefectsDaoImpl;
import com.etaap.dao.EnvironmentDaoImpl;
import com.etaap.dao.SystemAPIDaoImpl;
import com.etaap.dao.UserStoryDaoImpl;
import com.etaap.domain.Application;
import com.etaap.domain.Defects;
import com.etaap.domain.Environment;
import com.etaap.domain.SystemAPI;
import com.etaap.domain.TimePeriod;
import com.etaap.domain.UserStory;
import com.etaap.security.BasicAuthentication;
import com.etaap.security.EncryptionDecryptionAES;
import com.etaap.services.ApplicationService;
import com.etaap.services.BurndownChartService;
import com.etaap.services.DefectsService;
import com.etaap.services.SchedulerJobsService;
import com.etaap.services.UserStoryService;
import com.etaap.services.UserStoryStatusChartService;
import com.etaap.services.UserStoryStatusService;
import com.etaap.utils.Utils;

@Controller
public class JiraDataPullAPI {

	@Autowired
	public ApplicationService applicationService;

	@Autowired
	private DefectsService defectsService;

	@Autowired
	private UserStoryService userStoryService;

	@Autowired
	private SchedulerJobsService schedulerJobsService;
	
	@Autowired
	private BurndownChartService burndownChartService;

	@Autowired
	private UserStoryStatusChartService userStoryStatusChartService;

	private static final Logger logger = Logger.getLogger(JiraDataPullAPI.class);

	ApplicationContext context = null;

	public void execute() {
		
		
		logger.info("Inside JiraDataPullAPI :: execute()" );
		try {
			if (true) {
				context = new ClassPathXmlApplicationContext("spring-servlet.xml");
				JiraDataPullAPI jiradataPullApi = (JiraDataPullAPI) context.getBean("jiraDataPullAPI");
				applicationService = jiradataPullApi.getApplicationService();
				defectsService = jiradataPullApi.getDefectsService();
				userStoryService = jiradataPullApi.getUserStoryService();
				burndownChartService = (BurndownChartService)jiradataPullApi.getBurndownChartService();
				this.userStoryStatusChartService = (UserStoryStatusChartService)jiradataPullApi.getUserStoryStatusChartService();
				JiraDataPullAPI pullData = new JiraDataPullAPI();
				
				List<Application> applicationListForQA = applicationService.getUrlAliasList(3, "jira", "qa");
				List<Defects> defectList = null;
				if (applicationListForQA.size() > 0) {
					defectList = pullData.pullJiraData(applicationListForQA);
					defectsService.insertData(defectList);
				}
				

				List<Application> applicationListForDev = applicationService.getUrlAliasList(3, "jira", "dev");
				Map<String, List<UserStory>> userStoryMap = null;
				if (applicationListForDev.size() > 0) {
					userStoryMap = pullData.pullJiraUserStoryData(applicationListForDev);
					userStoryService.insertData(userStoryMap);
				}
				
				UserStoryStatusService userStoryStatusService = new UserStoryStatusService(this.userStoryStatusChartService);
				userStoryStatusService.pullUserStoryStatusDetails();
				
				
				StoryPointService storyPointService = new StoryPointService(this.burndownChartService);
				storyPointService.pullStroyPointDetails();
				
			} else {
				context = new ClassPathXmlApplicationContext("spring-servlet.xml");
				for(int i=0;i<=5;i++) {
					System.out.println("---- JIRADATAPULLAPI -- CALLED---------");
					try {
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						logger.error("ERROR :: execute() :: " + e.getMessage());
					}
				}
			}
		} catch (BeansException e) {
			e.printStackTrace();
			logger.error("ERROR :: execute() :BeansException: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("ERROR :: execute() :IOException: " + e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			logger.error("ERROR :: execute() :JSONException: " + e.getMessage());
		}
	}
	
	public void updateTriggerJobDetails(Map<String,Object> params){
		logger.info("Inside JiraDataPullAPI :: updateTriggerJobDetails()");

		if(context==null)
			context = new ClassPathXmlApplicationContext("spring-servlet.xml");

		JiraDataPullAPI j = (JiraDataPullAPI) context.getBean("jiraDataPullAPI");
		schedulerJobsService = j.getSchedulerJobsService();
		schedulerJobsService.updateScheduledJobsRecords(params);
	}
	
	public List<Defects> pullJiraData(List<Application> applicationList) throws IOException, JSONException {
		logger.info("Inside JiraDataPullAPI :: pullJiraData()");
		// need to put spring-servlet.xml in src folder
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-servlet.xml");
	    BeanFactory factory = context;

	    ApplicationDaoImpl applicationDaoImpl = (ApplicationDaoImpl) factory.getBean("applicationDao");
	    EnvironmentDaoImpl environmentDaoImpl = (EnvironmentDaoImpl) factory.getBean("environmentDao");
	    SystemAPIDaoImpl systemAPIDaoImpl = (SystemAPIDaoImpl) factory.getBean("systemAPIDao");
	    DefectsDaoImpl defectsDaoImpl = (DefectsDaoImpl) factory.getBean("defectsDao");

		int appId = 0;
		int sysId = 0;
		int envId = 0;
		int monthId = 0;
		String url = "";
		String urlAlias = "";
		String user = "";
		String pass = "";

		int previousSysId = 0;
		String previousUrlAlias = "";

		List<Defects> defectList = new ArrayList<Defects>();
		//List<Environment> envList = environmentDaoImpl.getEnvironmentList();

		for (Application app : applicationList) {
			appId = app.getAppId();
			sysId = app.getSysId();
			envId = app.getEnvId();
			url = app.getUrl();
			urlAlias = app.getUrlAlias();
			user = app.getUserId();
			pass = app.getPassword();
			
			//------New Start----------
			try {
				if(pass == null) {
					pass = "";
				}
				pass = pass.trim();
				if(!pass.equals("")) {
					EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
					pass = encryptionDecryptionAES.decrypt(pass);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				RuntimeException ex = new RuntimeException(e.getMessage());
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			//------New End--------
			
			

			if (/*sysId == previousSysId && */urlAlias.equalsIgnoreCase(previousUrlAlias))
				continue;

			List<Environment> envList = environmentDaoImpl.getEnvironmentList(appId);
			List<SystemAPI> customFieldList = systemAPIDaoImpl.getCustomFieldList(sysId);

			monthId = applicationDaoImpl.getMonthId(appId);
			List<TimePeriod> getActualTimePeriodList = Utils.getTimePeriod(monthId);

			for (TimePeriod tp : getActualTimePeriodList) {
				String from = null;
				String to = null;

				from = String.valueOf(tp.getStartDt()).substring(0, 10);
				to = String.valueOf(tp.getEndDt()).substring(0, 10);
				
			

				String actualUrl = url+"/rest/api/2/search?jql=project+%3D+%22"+urlAlias.replace(" ", "%20")+"%22+AND+createdDate+%3E=+"+from+"+AND+createdDate+%3C+"+to+"&maxResults=0";
				System.out.println("actualUrl :: " + actualUrl);
				logger.info("Inside JiraDataPullAPI :: actualUrl :1: " + actualUrl);
				
				System.out.println("BMBMBMBMBM  &********** "+user+","+pass);
				JSONObject json_output = readJsonFromUrl(actualUrl, user, pass);
				//System.out.println("jira output :: " + json_output);
				logger.info("Inside JiraDataPullAPI :: json_output :: " + json_output);

				if (json_output != null) {
					int total_issues = Integer.parseInt(json_output.getString("total").toString());
					//System.out.println("jira total_issues :: " + total_issues);
					logger.info("Inside JiraDataPullAPI :: total_issues :: " + total_issues);

					if (total_issues > 0) {
						actualUrl = actualUrl + total_issues;
						//System.out.println("actualUrl :: " + actualUrl);
						logger.info("Inside JiraDataPullAPI :: actualUrl :2: " + actualUrl);

						json_output = readJsonFromUrl(actualUrl, user, pass);

						JSONArray issues = json_output.getJSONArray("issues");

						for (int i = 0; i < issues.length(); i++) {
							JSONObject objectInIssues = issues.getJSONObject(i);
							//System.out.println("objectInIssues :: " + objectInIssues);

							String[] elementNames = JSONObject.getNames(objectInIssues);
							//System.out.printf("%d ELEMENTS IN CURRENT OBJECT:\n", elementNames.length);

							String key = "", created = "", updated = "", projectName = "", issueType = "", priority = "", status = "", severity = "", env = "", envIds = "";

							for (String elementName : elementNames) {
								String value = objectInIssues.getString(elementName);
								//System.out.printf("name=%s, value=%s\n", elementName, value);

								if (elementName.equalsIgnoreCase("key")) {
									key = value;
								}
								if (elementName.equalsIgnoreCase("fields")) {
									json_output = objectInIssues.getJSONObject(elementName);
									//System.out.println("fields\n" + json_output);

									created = json_output.getString("created").substring(0, 10);
									updated = json_output.getString("updated").substring(0, 10);
									
									try {
										JSONObject project = json_output.getJSONObject("project");
										projectName = project.getString("name");
									} catch (Exception e) {
										projectName = null;
										logger.error("ERROR :: pullJiraData() :try inside catch: " + e.getMessage());
									}

									try {
										JSONObject issuetype = json_output.getJSONObject("issuetype");
										issueType = issuetype.getString("name");
									} catch (Exception e) {
										issueType = null;
										logger.error("ERROR :: pullJiraData() :try inside catch: " + e.getMessage());
									}

									try {
										JSONObject priorityy = json_output.getJSONObject("priority");
										priority = priorityy.getString("name");
									} catch (Exception e) {
										priority = null;
										logger.error("ERROR :: pullJiraData() :try inside catch: " + e.getMessage());
									}

									try {
										JSONObject statuss = json_output.getJSONObject("status");
										status = statuss.getString("name");
									} catch (Exception e) {
										status = null;
										logger.error("ERROR :: pullJiraData() :try inside catch: " + e.getMessage());
									}

									JSONArray environment = null;
									for (int j = 0; j < customFieldList.size(); j++) {
										String customKey = customFieldList.get(j).getCustomKey();
										String customValue = customFieldList.get(j).getCustomValue();

										try {
											if (json_output.getString(customKey) != "null") {
												if (customValue.equalsIgnoreCase("Severity")) {
													JSONObject severityy = json_output.getJSONObject(customKey);
													severity = severityy.getString("value");
												}
												if (customValue.equalsIgnoreCase("Environment")) {
													environment = json_output.getJSONArray(customKey);
													for (int k = 0; k < environment.length(); k++) {
														JSONObject jsonObj = environment.getJSONObject(k);
														for (Environment e : envList) {
															//if(envId == e.getEnvId()) {
																env = jsonObj.getString("value");
																if (env.equalsIgnoreCase(e.getEnvName())) {
																	envIds = envIds + "," + e.getEnvId();
																	break;
																}
															//}
														}
													}
												}
											}
										} catch (Exception e) {
											///// modified by Shambhu on 22-May-2015 as per jira used by sonam
											try {
												if (customValue.equalsIgnoreCase("Severity")) {
													JSONObject severityy = json_output.getJSONObject(customKey);
													severity = severityy.getString("value");
												}
												if (customValue.equalsIgnoreCase("Environment")) {
													env = json_output.getString("environment");
													for (Environment ee : envList) {
														//if(envId == ee.getEnvId()) {
															if (env.equalsIgnoreCase(ee.getEnvName())) {
																envIds = envIds + "," + ee.getEnvId();
																break;
															}
														//}
													}
												}
											/////
											} catch (Exception exc) {
												severity = null;
												envIds = "";
												exc.printStackTrace();
												logger.error("ERROR :: pullJiraData() :try inside catch: " + exc.getMessage());
											}
										}
									}
									/*if (environment == null) {
										try {
											JSONObject env = json_output.getJSONObject("priority");
											priority = priorityy.getString("name");
										} catch (Exception e) {
											environment = null;
										}
									}*/

									String[] envIdArray = envIds.split(",");
									for (int l = 0; l < envIdArray.length; l++) {
										if (!envIdArray[l].trim().equalsIgnoreCase("")) {
											Defects defect = new Defects();
											defect.setAppId(appId);
											defect.setEnvId(Integer.parseInt(envIdArray[l]));
											defect.setKey(key);
											defect.setProjectName(projectName);
											defect.setSeverity(severity);
											defect.setPriority(priority);
											defect.setStatus(status);
											defect.setIssueType(issueType);
											defect.setCreated(created);
											defect.setUpdated(updated);

											defectList.add(defect);
										}
									}
								}
							}
						}

						if (defectList.size() > 0)
							defectsDaoImpl.deActivateData(appId, from, to);
					}
				}
			}

			previousSysId = sysId;
			previousUrlAlias = urlAlias;
		}

		return defectList;
	}

	public Map<String, List<UserStory>> pullJiraUserStoryData(List<Application> applicationList) throws IOException, JSONException {
		logger.info("Inside JiraDataPullAPI :: pullJiraUserStoryData()");

		ApplicationContext context = new ClassPathXmlApplicationContext("spring-servlet.xml");
	    BeanFactory factory = context;

	    UserStoryDaoImpl userStoryDaoImpl = (UserStoryDaoImpl) factory.getBean("userStoryDao");

		int appId = 0;
		String url = "";
		String rapidViewId = "";
		String user = "";
		String pass = "";

		List<UserStory> listSprintDetails = new ArrayList<UserStory>();
		List<UserStory> listUserStoryDetails = new ArrayList<UserStory>();
		List<UserStory> listVelocityDetails = new ArrayList<UserStory>();
		Map <String, List<UserStory>> map = new HashMap<String, List<UserStory>>();

		//int previousSysId = 0;
		//String previousUrlAlias = "";

		for (Application app : applicationList) {
			try {
				appId = app.getAppId();
				url = app.getUrl();
				rapidViewId = app.getUrlAlias();
				user = app.getUserId();
				pass = app.getPassword();
	
				// ------New Start----------
				try {
					if (pass == null) {
						pass = "";
					}
					pass = pass.trim();
					if (!pass.equals("")) {
						EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();
						pass = encryptionDecryptionAES.decrypt(pass);
					}
				} catch (Exception e) {
					e.printStackTrace();
					RuntimeException ex = new RuntimeException(e.getMessage());
					ex.setStackTrace(e.getStackTrace());
					throw ex;
				}
				// ------New End--------
	
				// URL to get all sprint detail for specific rapidViewId
				//String actualUrl = url+"/rest/greenhopper/1.0/sprintquery/"+rapidViewId+"?includeFutureSprints=true&includeHistoricSprints=true";
				String actualUrl = url+"/rest/greenhopper/1.0/rapid/charts/velocity?rapidViewId="+rapidViewId;
				logger.info("Inside JiraDataPullAPI :: pullJiraUserStoryData() :: Going to read JSON for rapidViewId = " + rapidViewId);
				System.out.println("Going to read JSON for rapidViewId = " + rapidViewId);
	
				JSONObject json_output = readJsonFromUrl(actualUrl, user, pass);
	//			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX jira output :jira_us: " + json_output);
	
				if (json_output != null) {
					JSONArray sprints = json_output.getJSONArray("sprints");
					logger.info("Inside JiraDataPullAPI :: pullJiraUserStoryData() :: Got JSON for all sprints.");
					System.out.println("Inside JiraDataPullAPI :: pullJiraUserStoryData() :: Got JSON for all sprints.");
	
					for (int i = 0; i < sprints.length(); i++) {
						try {
							JSONObject objectInSprints = sprints.getJSONObject(i);
							//System.out.println("objectInSprints :jira_us: " + objectInSprints);
		
							//String[] elementNames = JSONObject.getNames(objectInSprints);
							//System.out.printf("%d ELEMENTS IN CURRENT OBJECT:\n", elementNames.length);
		
							String sprintId = "", sprint_name = "", sprint_state = "", sprint_start_date = "", sprint_end_date = "";
		
							sprintId = objectInSprints.getString("id");
		
							// URL to get all user story detail for specific rapidViewId and specific sprintId
							String actualUrl2 = url+"/rest/greenhopper/1.0/rapid/charts/sprintreport?rapidViewId="+rapidViewId+"&sprintId="+sprintId;
							logger.info("Inside JiraDataPullAPI :: pullJiraUserStoryData() :: Going to pull sprint and userstories from - "+actualUrl2);
							System.out.println("Inside JiraDataPullAPI :: pullJiraUserStoryData() :: Going to pull sprint and userstories from - "+actualUrl2);
							//System.out.println("actualUrl2 :jira_us: " + actualUrl2);
		
							JSONObject json_output_us = readJsonFromUrl(actualUrl2, user, pass);
							//System.out.println("jira output :jira_us: " + json_output_us);
		
							if (json_output_us != null) {
								JSONObject contents = json_output_us.getJSONObject("contents");
		
								JSONObject sprint = json_output_us.getJSONObject("sprint");
								sprint_name = sprint.getString("name");
								sprint_state = sprint.getString("state");
								sprint_start_date = sprint.getString("startDate");
								sprint_end_date = sprint.getString("endDate");
		
								try {
									SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yy HH:mm a");
									SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
									Date d = format.parse(sprint_start_date);
									sprint_start_date = newFormat.format(d);
		
									d = format.parse(sprint_end_date);
									sprint_end_date = newFormat.format(d);
		
									UserStory us = new UserStory();
									us.setAppId(appId);
									us.setRapidViewId(Integer.parseInt(rapidViewId));
									us.setSprintId(Integer.parseInt(sprintId));
									us.setSprintName(sprint_name);
									us.setStatus(sprint_state);
									us.setStartDt(sprint_start_date);
									us.setEndDt(sprint_end_date);
									listSprintDetails.add(us);
									logger.info("Got Sprint Details :: "+rapidViewId+ ":" + sprintId + ":" + sprint_name + ":" + sprint_state + ":" + sprint_start_date + ":" + sprint_end_date);
									System.out.println("Got Sprint Details :: "+rapidViewId+ ":" + sprintId + ":" + sprint_name + ":" + sprint_state + ":" + sprint_start_date + ":" + sprint_end_date);
								} catch (Exception e) {
									e.printStackTrace();
									logger.error("ERROR :: pullJiraUserStoryData() : Error while reading a sprint : "+rapidViewId+","+sprintId+","+sprint_name+" "+ e.getMessage());
								}
		
								JSONArray completedIssues = contents.getJSONArray("completedIssues");
								for (int k = 0; k < completedIssues.length(); k++) {
									try {
										JSONObject objectInCompleted = completedIssues.getJSONObject(k);
			
										String us_key = "", us_summary = "", us_priority = "", us_status = "", jira_id = "";
			
										us_key = objectInCompleted.getString("key");
										us_summary = objectInCompleted.getString("summary");
										us_priority = objectInCompleted.getString("priorityName");
			
										JSONObject status = objectInCompleted.getJSONObject("status");
										us_status = status.getString("name");
										jira_id = objectInCompleted.getString("id");
			
										//System.out.println("Completed :: " + us_key + ":" + us_summary + ":" + us_priority + ":" + us_status);
			
										UserStory us1 = new UserStory();
										us1.setAppId(appId);
										us1.setRapidViewId(Integer.parseInt(rapidViewId));
										us1.setKey(us_key);
										us1.setSummary(us_summary);
										us1.setPriority(us_priority);
										us1.setStatus(us_status);
										us1.setSprintId(Integer.parseInt(sprintId));
										us1.setJiraId(Integer.parseInt(jira_id));
										listUserStoryDetails.add(us1);
									} catch (Exception e) {
										e.printStackTrace();
										logger.error("ERROR :: pullJiraUserStoryData() : Error while reading a completedIssues :: "+e.getMessage());
									}
								}
		
								JSONArray incompletedIssues = contents.getJSONArray("incompletedIssues");
								for (int l = 0; l < incompletedIssues.length(); l++) {
									try {	
										JSONObject objectInIncompleted = incompletedIssues.getJSONObject(l);
			
										String us_key = "", us_summary = "", us_priority = "", us_status = "", jira_id = "";
			
										us_key = objectInIncompleted.getString("key");
										us_summary = objectInIncompleted.getString("summary");
										us_priority = objectInIncompleted.getString("priorityName");
			
										JSONObject status = objectInIncompleted.getJSONObject("status");
										us_status = status.getString("name");
										jira_id = objectInIncompleted.getString("id");
			
										//System.out.println("Incompleted :: " + us_key + ":" + us_summary + ":" + us_priority + ":" + us_status);
			
										UserStory us2 = new UserStory();
										us2.setAppId(appId);
										us2.setRapidViewId(Integer.parseInt(rapidViewId));
										us2.setKey(us_key);
										us2.setSummary(us_summary);
										us2.setPriority(us_priority);
										us2.setStatus(us_status);
										us2.setSprintId(Integer.parseInt(sprintId));
										us2.setJiraId(Integer.parseInt(jira_id));
										listUserStoryDetails.add(us2);
									} catch (Exception e) {
										e.printStackTrace();
										logger.error("ERROR :: pullJiraUserStoryData() : Error while reading a incompletedIssues :: "+e.getMessage());
									}
								}
								
								
								//santosh
								JSONArray puntedIssues = contents.getJSONArray("puntedIssues");
								for (int l = 0; l < puntedIssues.length(); l++) {
									try {
										JSONObject objectInpunted = puntedIssues.getJSONObject(l);
			
										String us_key = "", us_summary = "", us_priority = "", us_status = "",jira_id = "";
			
										us_key = objectInpunted.getString("key");
										us_summary = objectInpunted.getString("summary");
										us_priority = objectInpunted.getString("priorityName");
			
										JSONObject status = objectInpunted.getJSONObject("status");
										us_status = status.getString("name");
										jira_id = objectInpunted.getString("id");
										//System.out.println("Incompleted :: " + us_key + ":" + us_summary + ":" + us_priority + ":" + us_status);
			
										UserStory us3 = new UserStory();
										us3.setAppId(appId);
										us3.setRapidViewId(Integer.parseInt(rapidViewId));
										us3.setKey(us_key);
										us3.setSummary(us_summary);
										us3.setPriority(us_priority);
										us3.setStatus(us_status);
										us3.setSprintId(Integer.parseInt(sprintId));
										us3.setJiraId(Integer.parseInt(jira_id));
										listUserStoryDetails.add(us3);
									} catch (Exception e) {
										e.printStackTrace();
										logger.error("ERROR :: pullJiraUserStoryData() : Error while reading a puntedIssues :: "+e.getMessage());
									}
								}
							}
						}
						catch(Exception e2) {
							e2.printStackTrace();
							logger.error("ERROR :: pullJiraUserStoryData() : Error while reading JSON for a sprint : " + e2.getMessage());
						}
					}
				}
	/*			if (listUserStoryDetails.size() > 0) {
					userStoryDaoImpl.deActivateData(appId, rapidViewId, "userstory");
				}
	*/			getVelocityDetails(appId, rapidViewId, url, user, pass, listVelocityDetails);

			}
			catch(Exception e1) {
				e1.printStackTrace();
				logger.error("ERROR :: pullJiraUserStoryData() : Error while reading application details or reading json from jira server : " + e1.getMessage());
			}
		}

		map.put("sprintDetail", listSprintDetails);
		map.put("userStoryDetail", listUserStoryDetails);
		map.put("velocityDetail", listVelocityDetails);

/*		if (listSprintDetails.size() > 0)
			userStoryDaoImpl.deActivateData(appId, rapidViewId, "sprint");
		if (listUserStoryDetails.size() > 0)
			userStoryDaoImpl.deActivateData(appId, rapidViewId, "userstory");
		if (listVelocityDetails.size() > 0)
			userStoryDaoImpl.deActivateData(appId, rapidViewId, "velocity");
*/

		return map;
	}

	public void getVelocityDetails(int appId, String rapidViewId, String url, String user, String pass, List<UserStory> listVelocityDetails) throws IOException, JSONException {
	//		System.out.println("|||||&*&*&*&* JIRA Velocity details "+appId+","+rapidViewId+","+url+","+user+","+pass);
			
			logger.info("Inside JiraDataPullAPI :: getVelocityDetails()");
	
	//		List<UserStory> listVelocityDetails = new ArrayList<UserStory>();
	
			// URL to get all sprint detail of velocity chart for specific rapidViewId
			String actualUrl = url+"/rest/greenhopper/1.0/rapid/charts/velocity?rapidViewId="+rapidViewId;
			System.out.println("Going to read velocity details from " + actualUrl);
			try {
			JSONObject json_output = readJsonFromUrl(actualUrl, user, pass);
	//		System.out.println("jira output :jira_us: " + json_output);
			
			if (json_output != null) {
				JSONArray sprints = json_output.getJSONArray("sprints");
	
				String sprint_id = "", estimated_count = "", completed_count = "";
				for (int i = 0; i < sprints.length(); i++) {
					try { 
						JSONObject objectInSprints = sprints.getJSONObject(i);
						//System.out.println("objectInSprints :jira_us: " + objectInSprints);
						
						String[] elementNames = JSONObject.getNames(objectInSprints);
						//System.out.println(" ELEMENTS IN CURRENT OBJECT:"+ elementNames.length);
		
						sprint_id = ""; estimated_count = ""; completed_count = "";
		
						sprint_id = objectInSprints.getString("id");
						//System.out.println("sprint_id: "+sprint_id);
						
						String sprint_name = objectInSprints.getString("name");
						//System.out.println("sprint_name: "+sprint_name);
		
						JSONObject velocityStatEntries = json_output.getJSONObject("velocityStatEntries");
						JSONObject sprintId = velocityStatEntries.getJSONObject(sprint_id);
		
						JSONObject estimated = sprintId.getJSONObject("estimated");
						estimated_count = estimated.getString("value").substring(0, estimated.getString("value").indexOf("."));
						
		
						JSONObject completed = sprintId.getJSONObject("completed");
						completed_count = completed.getString("value").substring(0, completed.getString("value").indexOf("."));
		
						UserStory us = new UserStory();
						us.setAppId(appId);
						us.setRapidViewId(Integer.parseInt(rapidViewId));
						us.setSprintId(Integer.parseInt(sprint_id));
						us.setEstimated(Integer.parseInt(estimated_count));
						us.setCompleted(Integer.parseInt(completed_count));
						listVelocityDetails.add(us);
					}
					catch(Exception e1) {
						e1.printStackTrace();
						logger.error("ERROR :: getVelocityDetails() : Error while reading a velocity record for :: '"+sprint_id+"', '"+rapidViewId+"' :: "+ e1.getMessage());
					}
				}
			}
			
		}
		catch(Exception e1) {
			e1.printStackTrace();
			logger.error("ERROR :: getVelocityDetails() : Error while reading velocity details for "+rapidViewId+", from "+actualUrl+" :: "+ e1.getMessage());
			throw e1;
		}
	}

	/*public JSONObject readJsonFromUrl(String url, String user, String pass) {
		logger.info("Inside JiraDataPullAPI :: readJsonFromUrl()");

		InputStream is = null;
		JSONObject json = null;
	    try {
    	System.out.println("VVVVVVVVVVVVVVVVVVVV "+url+user+","+pass);
	    	URLConnection urlConnection = setUsernamePassword(new URL(url), user, pass);
            is = urlConnection.getInputStream();
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	String jsonText = readAll(rd);
	    	System.out.println("VVVVVVVVVVVVVVVVVVVV " + jsonText);
	    	json = new JSONObject(jsonText);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	logger.error("ERROR :: readJsonFromUrl() :IOException: " + e.getMessage());
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    	logger.error("ERROR :: readJsonFromUrl() :JSONException: " + e.getMessage());
	    } finally {
	    	try {
	    		if (is != null) {
	    			is.close();
	    		}
	    	} catch (Exception e) {
	    		logger.error("ERROR :: readJsonFromUrl() :: " + e.getMessage());
			}
	    }

	    return json;
	}*/
	
	
	//--------New Start------

	/**@return JSONObject
	 * @param url
	 * @param username This can be null or empty. In case value of this parameter is null or empty, data is read without
	 * authentication.
	 * @param password This can be null or empty. In case value of this parameter is null or empty, data is read without
	 * authentication.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * */
	public JSONObject readJsonFromUrl(String url, String username, String password) throws JSONException, ClientProtocolException, IOException {
		logger.info("Inside JiraDataPullAPI :: readJsonFromUrl()");
		boolean authenticationRequired = true;
		
		if(((username == null) || (username.trim().equals(""))) || ((password == null) && (password.trim().equals("")))) {
			authenticationRequired  = false;
		}
		
		System.out.println("URL:" +url);
		
		JSONObject json = null;
		if(authenticationRequired) {
			BasicAuthentication basicAuthentication = new BasicAuthentication();
			basicAuthentication.setHttpUrlString(url);
			basicAuthentication.setUserName(username);
			basicAuthentication.setPassword(password);
			
			
			System.out.println(username+" + "+password+" + "+url);
//		    try {
			String jsonText = basicAuthentication.getResponseForJiraUsingAuthetication(url);
		    	//String jsonText = basicAuthentication.getTextResponseUsingPreemptiveAuthentication();
		    	System.out.println("****(*(*(*(*with*authentication**************"+jsonText);
		    	if(jsonText != null) {
		    		if(!jsonText.trim().equals("")) {
		    			if(jsonText.trim().startsWith("<html>")) {
		    				throw new RuntimeException(jsonText);
		    			}
		    		}
		    	}
		    	
		    	json = new JSONObject(jsonText);
//		    } catch (IOException e) {
//		    	logger.error("ERROR :: readJsonFromUrl() :IOException: " + e.getMessage());
//		    } catch (JSONException e) {
//		    	logger.error("ERROR :: readJsonFromUrl() :JSONException: " + e.getMessage());
//		    }
		}
		else {
			InputStream is = null;
		    try {
		    	is = new URL(url).openStream();
		    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    	String jsonText = readAll(rd);
		    	System.out.println("****(*(*(*(*without*authentication**************"+jsonText);
		    	if(jsonText != null) {
		    		if(!jsonText.trim().equals("")) {
		    			if(jsonText.trim().startsWith("<html>")) {
		    				throw new RuntimeException(jsonText);
		    			}
		    		}
		    	}
		    	json = new JSONObject(jsonText);
		    } finally {
		    	try {
		    		if (is != null) {
		    			is.close();
		    		}
		    	} finally {
//		    		logger.error("ERROR :: readJsonFromUrl() :: " + e.getMessage());
				}
		    }
		}
	    return json;
	}
	
	//--------New End------


	private URLConnection setUsernamePassword(URL url, String user, String pass) throws IOException {
		URLConnection urlConnection = url.openConnection();
	    String authString = user + ":" + pass;
	    
	    String authStringEnc = new String(org.springframework.security.crypto.codec.Base64.encode(authString.getBytes()));
	    urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
//	    System.out.println("BMBMBMBMBMKKKKKKKKK  "+urlConnection.getURL());
	    return urlConnection;
	}

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
	    while ((cp = rd.read()) != -1) {
	    	sb.append((char) cp);
	    }

	    return sb.toString();
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public DefectsService getDefectsService() {
		return defectsService;
	}

	public void setDefectsService(DefectsService defectsService) {
		this.defectsService = defectsService;
	}

	public UserStoryService getUserStoryService() {
		return userStoryService;
	}

	public void setUserStoryService(UserStoryService userStoryService) {
		this.userStoryService = userStoryService;
	}

	public SchedulerJobsService getSchedulerJobsService() {
		return schedulerJobsService;
	}

	public void setSchedulerJobsService(SchedulerJobsService schedulerJobsService) {
		this.schedulerJobsService = schedulerJobsService;
	}
	
	public BurndownChartService getBurndownChartService() {
		return burndownChartService;
	}

	public void setBurndownChartService(BurndownChartService burndownChartService) {
		this.burndownChartService = burndownChartService;
	}
	
	public UserStoryStatusChartService getUserStoryStatusChartService() {
		return userStoryStatusChartService;
	}

	public void setUserStoryStatusChartService(
			UserStoryStatusChartService userStoryStatusChartService) {
		this.userStoryStatusChartService = userStoryStatusChartService;
	}
	
	
}
