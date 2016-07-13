package com.etaap.controller;

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
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.etaap.dao.CIDaoImpl;
import com.etaap.domain.Application;
import com.etaap.domain.CI;
//import com.etaap.domain.TestBed;
//import com.etaap.domain.TestSuite;
//import com.etaap.domain.Environment;

public class JenkinsDataPullAPI {
	
	
	public List<CI> pullJenkinsData(List<Application> applicationList/*, List<Environment> environmentList, List<TestSuite> testSuiteList, List<TestBed> testBedList*/) throws IOException, JSONException {

		System.out.println("inside expected");

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
		List<CI> ciList = new ArrayList<CI>();

		for (Application app : applicationList) {
			appId = app.getAppId();
			envId = app.getEnvId();
			suiteId = app.getSuiteId();
			bedId = app.getBedId();
			url = app.getUrl();
			urlAlias = app.getUrlAlias();

			//System.out.println("envId:"+ envId +":suiteId:"+ suiteId +":bedId:"+ bedId);

			boolean isRecordExist = false;
			//CIDaoImpl ciDao = new CIDaoImpl();

			/*isRecordExist = ciDao.isRecordExist(app);
			if (isRecordExist)
				continue;*/

			JSONObject job = readJsonFromUrl(url + "/job/" + urlAlias.replace(" ", "%20") + "/api/json?pretty=true");

			if (job != null) {
				//System.out.println(job.get("builds"));
				JSONArray builds = job.getJSONArray("builds");

				for (int i = 0; i < builds.length(); i++) {
					JSONObject objectInBuilds = builds.getJSONObject(i);
					String[] elementNames = JSONObject.getNames(objectInBuilds);
					//System.out.printf("%d ELEMENTS IN CURRENT OBJECT:\n", elementNames.length);

					for (String elementName : elementNames) {
						String value = objectInBuilds.getString(elementName);
						//System.out.printf("name=%s, value=%s\n", elementName, value);

						if (elementName.equalsIgnoreCase("url")) {
							JSONObject build = readJsonFromUrl(value + "/api/json?pretty=true");

							String buildId = build.getString("id");
							String buildName = build.getString("fullDisplayName");
							String buildNumber = build.getString("number");
							String buildUrl = build.getString("url");

							//String buildDate = build.getString("id").replace("_", " ");
							String timestamp = build.getString("timestamp");
							String buildDate = getPSTFromTimestamp(Long.parseLong(timestamp));

							String buildResult = build.getString("result");

							isRecordExist = ciDao.isRecordExist(app, buildNumber);
							if (isRecordExist)
								continue;

							//System.out.println(buildName);

							int failCount = 0;
							int passCount = 0;
							int skipCount = 0;
							int totalCount = 0;
							String urlName = "";
							boolean flag = true;

							//System.out.println(build.get("actions"));
							JSONArray actions = build.getJSONArray("actions");

							for (int j = 0; j < actions.length(); j++) {
								JSONObject objectInActions = actions.getJSONObject(j);
								String[] eNames = JSONObject.getNames(objectInActions);
								//System.out.println(eNames);

								if (eNames != null) {
									if (flag) {
										for (String eName : eNames) {
											String eValue = objectInActions.getString(eName);
											//System.out.printf("name=%s, value=%s\n", eName, eValue);

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

										if (urlName.equalsIgnoreCase("testReport")) {
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

										//System.out.println(urlName +":fail:"+ failCount +":pass:"+ passCount +":skip:"+ skipCount +":total:"+ totalCount);
										//System.out.println("ciList size :: " + ciList.size());
									}
								}
							}
						}
					}
				}
			}
		}

		return ciList;
	}

	public JSONObject readJsonFromUrl(String url) {
		InputStream is = null;
		JSONObject json = null;
	    try {
	    	is = new URL(url).openStream();
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
}
