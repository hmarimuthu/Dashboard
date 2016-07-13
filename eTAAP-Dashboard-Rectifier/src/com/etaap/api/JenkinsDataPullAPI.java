package com.etaap.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.etaap.dao.CIDaoImpl;
import com.etaap.domain.Application;
import com.etaap.domain.CI;
import com.etaap.mail.JenkinsSchedularErrorMailComposer;
import com.etaap.mail.SendMailServer;
import com.etaap.scheduler.SchedulerConstants;
import com.etaap.security.BasicAuthentication;
import com.etaap.security.EncryptionDecryptionAES;
import com.etaap.services.ApplicationService;
import com.etaap.services.CIService;
import com.etaap.services.SchedulerJobsService;
import com.etaap.utils.ExceptionUtility;



@Controller
public class JenkinsDataPullAPI {

	@Autowired
	//@Qualifier("app")
	public ApplicationService applicationService;

	@Autowired
	//@Qualifier("ciService")
	private CIService ciService;

	@Autowired
	private SchedulerJobsService schedulerJobsService;
	
	private SendMailServer sendMailServer;
	
	
/*	public void setSendMailServer(SendMailServer sendMailServer) {
		this.sendMailServer = sendMailServer;
	}
	
	public SendMailServer getSendMailServer() {
		return this.sendMailServer;
	}
*/	
	
	private static final Logger logger = Logger.getLogger(JenkinsDataPullAPI.class);

	ApplicationContext context = null;

	private static Map<String,String> urlNameMap = new HashMap<String,String>();
	
	static {
		urlNameMap.put("testReport","testReport");
		urlNameMap.put("testngreports","testngreports");
	}
	
	private String jobId = "0";

	private String triggerFireTime = "";

	public void execute() {
		logger.info("Inside JenkinsDataPullAPI :: execute()");
		System.out.println("Inside JenkinsDataPullAPI :: execute()");
		
		try {
			context = new ClassPathXmlApplicationContext("spring-servlet.xml");
			JenkinsDataPullAPI j = (JenkinsDataPullAPI) context.getBean("jenkinsDataPullAPI");
			sendMailServer = (SendMailServer) context.getBean("senderThread");
			
			applicationService = j.getApplicationService();
			List<Application> applicationList = applicationService.getUrlAliasList(3, "jenkins", "qa");
			List<CI> ciList = pullJenkinsData(applicationList);
			ciService = (CIService) context.getBean("ciService");
			ciService.insertData(ciList);

		} catch (IOException e) {
			System.out.println("************************Inside Error***********");
//			e.printStackTrace();
			logger.error("ERROR :: execute() :IOException: " + e.getMessage());
		} catch (JSONException e) {
//			e.printStackTrace();
			logger.error("ERROR :: execute() :JSONException: " + e.getMessage());
		}
	}
	
	public void updateTriggerJobDetails(Map<String,Object> params){
//		System.out.println("*************&*&*&    JOB PARAMS ******  "+params);
		logger.info("Inside JenkinsDataPullAPI :: updateTriggerJobDetails()");

		if(context==null)
			context = new ClassPathXmlApplicationContext("spring-servlet.xml");
		
		JenkinsDataPullAPI j = (JenkinsDataPullAPI) context.getBean("jenkinsDataPullAPI");
		schedulerJobsService = j.getSchedulerJobsService();
		schedulerJobsService.updateScheduledJobsRecords(params);
	}

	public List<CI> pullJenkinsData(List<Application> applicationList/*, List<Environment> environmentList, List<TestSuite> testSuiteList, List<TestBed> testBedList*/) throws IOException, JSONException {
		logger.info("Inside JenkinsDataPullAPI :: pullJenkinsData()");
		//List<Application> applicationList = applicationService.getUrlAliasList(Integer.parseInt(args[0].toString()), "jenkins");

		// need to put spring-servlet.xml in src folder
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-servlet.xml");
	    BeanFactory factory = context;
	    CIDaoImpl ciDao = (CIDaoImpl) factory.getBean("ciDao");

		int appId = 0;
		int envId = 0;
		int suiteId = 0;
		int bedId = 0;
		String url = "";
		String urlAlias = "";
		String mapId = "0";
		String sysId = "";
		String sysName = "";
		
		//-----New Start---------
		String username = null;
		String password = null;
		//-----New End-----------
		
		String buildIdForMail = "";
		String buildNameForMail = "";
		String buildNumberForMail = "";
		String buildUrlForMail = "";
		
		
		List<CI> ciList = new ArrayList<CI>();

		for (Application app : applicationList) {
			try {
				mapId = app.getMapId();
				sysId = app.getSystemId();
				
				
				appId = app.getAppId();
				envId = app.getEnvId();
				suiteId = app.getSuiteId();
				bedId = app.getBedId();
				url = app.getUrl();
				urlAlias = app.getUrlAlias();
				sysName = app.getSysName();
				
				buildIdForMail = "";
				buildNameForMail = "";
				buildNumberForMail = "";
				buildUrlForMail = "";
				
				//-----New Start---------
				username = app.getUserId();
				
				password = app.getPassword();
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
				
				//-----New End-----------
				
	
	
				boolean isRecordExist = false;
				//CIDaoImpl ciDao = new CIDaoImpl();
	
				/*isRecordExist = ciDao.isRecordExist(app);
				if (isRecordExist)
					continue;*/
	
				String jobUrl = url + "/job/" + urlAlias.replace(" ", "%20") + "/api/json?pretty=true";
				logger.info("Inside JenkinsDataPullAPI :: Job url :: " + jobUrl);
	
				JSONObject job = readJsonFromUrl(jobUrl, username, password);
	
				if (job != null) {
					JSONArray builds = job.getJSONArray("builds");
	
					for (int i = 0; i < builds.length(); i++) {
						try {
							buildIdForMail = "";
							buildNameForMail = "";
							buildNumberForMail = "";
							buildUrlForMail = "";
							
							JSONObject objectInBuilds = builds.getJSONObject(i);
							String[] elementNames = JSONObject.getNames(objectInBuilds);
							//System.out.printf("%d ELEMENTS IN CURRENT OBJECT:\n", elementNames.length);
		
							for (String elementName : elementNames) {
								String value = objectInBuilds.getString(elementName);
								//System.out.printf("name=%s, value=%s\n", elementName, value);
		
								if (elementName.equalsIgnoreCase("url")) {
									String build_url = value + "/api/json?pretty=true";
									logger.info("Inside JenkinsDataPullAPI :: Build url :: " + build_url);
		
									JSONObject build = readJsonFromUrl(build_url, username, password);
		
									String buildId = build.getString("id");
									String buildName = build.getString("fullDisplayName");
									String buildNumber = build.getString("number");
									String buildUrl = build.getString("url");
									
									buildIdForMail = buildId;
									buildNameForMail = buildName;
									buildNumberForMail = buildNumber;
									buildUrlForMail = buildUrl;
									
									
									//String buildDate = build.getString("id").replace("_", " ");
									String timestamp = build.getString("timestamp");
									String buildDate = getPSTFromTimestamp(Long.parseLong(timestamp));
		
									String buildResult = build.getString("result");
		
									isRecordExist = ciDao.isRecordExist(app, buildNumber);
									if (isRecordExist)
										continue;
		
		
									int failCount = 0;
									int passCount = 0;
									int skipCount = 0;
									int totalCount = 0;
									String urlName = "";
									boolean flag = true;
		
									JSONArray actions = build.getJSONArray("actions");
		
									for (int j = 0; j < actions.length(); j++) {
										JSONObject objectInActions = actions.getJSONObject(j);
										String[] eNames = JSONObject.getNames(objectInActions);
		
										if (eNames != null) {
											if (flag) {
												for (String eName : eNames) {
													String eValue = objectInActions.getString(eName);
		
													if (eName.equalsIgnoreCase("failCount"))
														failCount = Integer.parseInt(eValue);
													else if (eName.equalsIgnoreCase("skipCount"))
														skipCount = Integer.parseInt(eValue);
													else if (eName.equalsIgnoreCase("totalCount"))
														totalCount = Integer.parseInt(eValue);
													else if (eName.equalsIgnoreCase("urlName"))
														urlName = eValue;
												}
												passCount = totalCount - failCount - skipCount;
		
												if (urlNameMap.get(urlName)!=null) {
													CI ci = new CI();
													ci.setAppId(appId);
													ci.setEnvId(envId);
													ci.setSuiteId(suiteId);
													ci.setBedId(bedId);
													ci.setBuildId(buildId);
													ci.setBuildName(buildName);
													ci.setBuildNumber(Integer.parseInt(buildNumber));
													ci.setBuildUrl(buildUrl);
													ci.setBuildDate(buildDate);
													ci.setResult(buildResult);
													ci.setFailCount(failCount);
													ci.setPassCount(passCount);
													ci.setSkipCount(skipCount);
													ci.setTotalCount(totalCount);
													ciList.add(ci);
		
													flag = false;
												}
											}
										}
									}
								}
							}
						}
						catch(Exception e) {
							ExceptionUtility exUtility = new ExceptionUtility(e);
							e.printStackTrace();
							try {
								SchedulerJobsService schedulerJobsService = (SchedulerJobsService) context.getBean("schedulerJobsService");
								schedulerJobsService.insertScheduledJobRecords(this.jobId, triggerFireTime, SchedulerConstants.SCHEDULER_JOB_STATUS_FAILED, exUtility.getMessage(), mapId);
								
								JenkinsSchedularErrorMailComposer email = new JenkinsSchedularErrorMailComposer();
								email.setSubject(sysName, urlAlias);
								email.setEmailBody(sysName, url, urlAlias, buildIdForMail, buildNameForMail, buildNumberForMail, buildUrlForMail, exUtility.getMessage());
								sendMailServer.sendMail(email.getSimpleMailMessage());
							}catch(Exception e1) {
						    	logger.error("ERROR While sending mail :: pullJenkinsData " + e1.getMessage());
							}
						}
					}
				}
			}
			catch(Exception e) {
				ExceptionUtility exUtility = new ExceptionUtility(e);
				e.printStackTrace();
				try {
					SchedulerJobsService schedulerJobsService = (SchedulerJobsService) context.getBean("schedulerJobsService");
					schedulerJobsService.insertScheduledJobRecords(this.jobId, triggerFireTime, SchedulerConstants.SCHEDULER_JOB_STATUS_FAILED, exUtility.getMessage(), mapId);
					
					JenkinsSchedularErrorMailComposer email = new JenkinsSchedularErrorMailComposer();
					email.setSubject(sysName, urlAlias);
					email.setEmailBody(sysName, url, urlAlias, buildIdForMail, buildNameForMail, buildNumberForMail, buildUrlForMail, exUtility.getMessage());
					sendMailServer.sendMail(email.getSimpleMailMessage());
				}catch(Exception e1) {
			    	logger.error("ERROR While sending mail :: pullJenkinsData " + e1.getMessage());
				}
			}
		}

		return ciList;
	}
	
	
/*	public JSONObject readJsonFromUrl(String url) {
		logger.info("Inside JenkinsDataPullAPI :: readJsonFromUrl()");

		InputStream is = null;
		JSONObject json = null;
	    try {
	    	is = new URL(url).openStream();
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	String jsonText = readAll(rd);
	    	json = new JSONObject(jsonText);
	    } catch (IOException e) {
	    	logger.error("ERROR :: readJsonFromUrl() :IOException: " + e.getMessage());
	    } catch (JSONException e) {
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
	}
	*/
	
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
		logger.info("Inside JenkinsDataPullAPI :: readJsonFromUrl()");
		boolean authenticationRequired = true;
		
		if(((username == null) || (username.trim().equals(""))) || ((password == null) && (password.trim().equals("")))) {
			authenticationRequired  = false;
		}
		
		JSONObject json = null;
		if(authenticationRequired) {
			BasicAuthentication basicAuthentication = new BasicAuthentication();
			basicAuthentication.setHttpUrlString(url);
			basicAuthentication.setUserName(username);
			basicAuthentication.setPassword(password);
			System.out.println(username+" + "+password+" + "+url);
//		    try {
		    	String jsonText = basicAuthentication.getTextResponseUsingPreemptiveAuthentication();
		    	System.out.println("with authentication"+jsonText);
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
	

	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
	    while ((cp = rd.read()) != -1) {
	    	sb.append((char) cp);
	    }
	    return sb.toString();
	}

	private String getPSTFromTimestamp(long input) {
        Date date = new Date(input);
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("PST"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setCalendar(cal);
        cal.setTime(date);
        return sdf.format(date);
    }

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public CIService getCiService() {
		return ciService;
	}

	public void setCiService(CIService ciService) {
		this.ciService = ciService;
	}

	public SchedulerJobsService getSchedulerJobsService() {
		return schedulerJobsService;
	}

	public void setSchedulerJobsService(SchedulerJobsService schedulerJobsService) {
		this.schedulerJobsService = schedulerJobsService;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getTriggerFireTime() {
		return triggerFireTime;
	}

	public void setTriggerFireTime(String triggerFireTime) {
		this.triggerFireTime = triggerFireTime;
	}
}
