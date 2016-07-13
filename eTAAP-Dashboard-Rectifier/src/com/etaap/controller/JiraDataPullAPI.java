package com.etaap.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.etaap.dao.ApplicationDaoImpl;

import com.etaap.dao.DefectsDaoImpl;
import com.etaap.dao.EnvironmentDaoImpl;
import com.etaap.dao.SystemAPIDaoImpl;
import com.etaap.domain.Application;

import com.etaap.domain.Defects;
import com.etaap.domain.Environment;
import com.etaap.domain.SystemAPI;
import com.etaap.domain.TimePeriod;
import com.etaap.utils.Utils;
import com.sun.org.apache.xml.internal.security.utils.*;

public class JiraDataPullAPI {

	public List<Defects> pullJiraData(List<Application> applicationList) throws IOException, JSONException {

		// need to put spring-servlet.xml in src folder
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-servlet.xml");
	    BeanFactory factory = context;

	    
	    ApplicationDaoImpl applicationDaoImpl = (ApplicationDaoImpl) factory.getBean("applicationDao");
	    EnvironmentDaoImpl environmentDaoImpl = (EnvironmentDaoImpl) factory.getBean("environmentDao");
	    SystemAPIDaoImpl systemAPIDaoImpl = (SystemAPIDaoImpl) factory.getBean("systemAPIDao");
	    DefectsDaoImpl defectsDaoImpl = (DefectsDaoImpl) factory.getBean("defectsDao");

		int appId = 0;
		int sysId = 0;
		int monthId = 0;
		String url = "";
		String urlAlias = "";
		String user = "";
		String pass = "";

		int previousSysId = 0;
		String previousUrlAlias = "";

		List<Defects> defectList = new ArrayList<Defects>();
		List<Environment> envList = environmentDaoImpl.getEnvironmentList();

		for (Application app : applicationList) {
			appId = app.getAppId();
			sysId = app.getSysId();
			url = app.getUrl();
			urlAlias = app.getUrlAlias();
			user = app.getUserId();
			pass = app.getPassword();

			if (sysId == previousSysId && urlAlias.equalsIgnoreCase(previousUrlAlias))
				continue;

			List<SystemAPI> customFieldList = systemAPIDaoImpl.getCustomFieldList(sysId);

			monthId = applicationDaoImpl.getMonthId(appId);
			List<TimePeriod> getActualTimePeriodList = Utils.getTimePeriod(monthId);

			for (TimePeriod tp : getActualTimePeriodList) {
				String from = null;
				String to = null;

				from = String.valueOf(tp.getStartDt()).substring(0, 10);
				to = String.valueOf(tp.getEndDt()).substring(0, 10);

				String actualUrl = url+"/rest/api/2/search?jql=project+%3D+%22"+urlAlias+"%22+AND+createdDate+>=+"+from+"+AND+createdDate+<+"+to+"&maxResults=0";
				System.out.println("actualUrl :: " + actualUrl);

				JSONObject json_output = readJsonFromUrl(actualUrl, user, pass);
				System.out.println("jira output :: " + json_output);

				if (json_output != null) {
					int total_issues = Integer.parseInt(json_output.getString("total").toString());
					System.out.println("jira total_issues :: " + total_issues);

					if (total_issues > 0) {
						actualUrl = actualUrl + total_issues;
						System.out.println("actualUrl :: " + actualUrl);
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
										projectName = "";
									}

									try {
										JSONObject issuetype = json_output.getJSONObject("issuetype");
										issueType = issuetype.getString("name");
									} catch (Exception e) {
										issueType = "";
									}

									try {
										JSONObject priorityy = json_output.getJSONObject("priority");
										priority = priorityy.getString("name");
									} catch (Exception e) {
										priority = "";
									}

									try {
										JSONObject statuss = json_output.getJSONObject("status");
										status = statuss.getString("name");
									} catch (Exception e) {
										status = "";
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
															env = jsonObj.getString("value");
															if (env.equalsIgnoreCase(e.getEnvName())) {
																envIds = envIds + "," + e.getEnvId();
																break;
															}
														}
													}
												}
											}
										} catch (Exception e) {
											severity = "";
											envIds = "";
										}
									}

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

	public JSONObject readJsonFromUrl(String url, String user, String pass) {
		InputStream is = null;
		JSONObject json = null;
	    try {
	    	URLConnection urlConnection = setUsernamePassword(new URL(url), user, pass);
            is = urlConnection.getInputStream();
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	String jsonText = readAll(rd);
	    	json = new JSONObject(jsonText);
	    } catch (IOException e) {
	    	System.out.println("IOException :: " + e);
	    } catch (JSONException e) {
	    	System.out.println("JSONException :: " + e);
	    } finally {
	    	try {
	    		if (is != null) {
	    			is.close();
	    		}
	    	} catch (Exception e) {
			}
	    }

	    return json;
	}

	private URLConnection setUsernamePassword(URL url, String user, String pass) throws IOException {
		URLConnection urlConnection = url.openConnection();
	    String authString = user + ":" + pass;
	    String authStringEnc = new String(Base64.encode(authString.getBytes()));
	    urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

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
}
