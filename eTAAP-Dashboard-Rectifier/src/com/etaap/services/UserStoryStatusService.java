package com.etaap.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etaap.beans.UserStoryStatus;
import com.etaap.services.BurndownChartService;

public class UserStoryStatusService extends APIPullService {
	
//	public static final int STATUS_CODE_ISSUE_CREATED = 10000;
//	public static final int STATUS_CODE_COMPLETED = 6;
//	public static final int STATUS_CODE_ISSUE_REOPENED = 3;
//	public static final int STATUS_CODE_NO_STATUS = 0;
//	public static final int ADDED_TO_SPRINT = 1;
//	public static final int REMOVED_FROM_SPRINT = 1;
	
	private UserStoryStatusChartService userStoryStatusChartService;
	
	public UserStoryStatusService(UserStoryStatusChartService userStoryStatusChartService) {
		this.userStoryStatusChartService = userStoryStatusChartService;
	}
	
	private BurndownChartService burndownChartService;
	
	/**key = userstory_id  value =      List 
	 * 								UserStoryStatus1
	 *									.
	 *									. 								
	 * 								UserStoryStatusN
	 */
    private LinkedHashMap<Integer, List<UserStoryStatus>> toAddUserStoryStatusRecords = new LinkedHashMap<Integer, List<UserStoryStatus>>();
	
	private static final Logger logger = Logger.getLogger(UserStoryStatusService.class);
	
	public static void main(String args[]) throws IOException, JSONException {
/*		UserStoryStatusService spsp = new UserStoryStatusService(null);
		spsp.pullUserStoryStatusDetails();
*/	}
	
	public void pullUserStoryStatusDetails() throws IOException, JSONException {
		try {
			
			logger.info("Inside StoryPointService :: pullStroyPointDetails");
			List<Map<String, Object>> sprintDetailsWithURLList = this.userStoryStatusChartService.getSprintDetailsWithURL();
			for(Map map : sprintDetailsWithURLList) {
				try {
					String url = (String)map.get("url");
					int sprintId = (Integer)map.get("sprint_id");
					int rapidviewId = (Integer)map.get("rapidview_id");
					String userId = (String)map.get("user_id");
					String encryptedPassword = (String)map.get("password");
					String sprintName = (String)map.get("sprint_name");
					int appId  = (Integer)map.get("app_id");
					
/*					int appId = 10;
					int rapidviewId = 9419;
					int sprintId = 25322;
					String url = "https://jira.intuit.com";
*/					
					this.userStoryStatusChartService.deleteOldUserStoryStatus(appId, sprintId, rapidviewId);
					
					List<UserStoryStatus> userstoryStatusList = new ArrayList<UserStoryStatus>();
					
					//Get all userstorynames related to sprint_id
					List<Map<String, Object>> userstoryNames = this.userStoryStatusChartService.getUserStoryDetails(appId, sprintId, rapidviewId);
					for(Map userStoryMap : userstoryNames) {
						boolean issueCreated = false;
						String userstoryName = (String)userStoryMap.get("key");
						int userstoryId = (Integer)userStoryMap.get("id");
//						System.out.println("LLLLLLLLLLL "+userstoryName);
						String actualUrl = url+"/rest/api/2/issue/"+userstoryName+"?expand=changelog";
//						String actualUrl = "https://jira.intuit.com/rest/api/2/issue/"+userstoryName+"?expand=changelog";
						try {
//							JSONObject json_output = readJsonFromUrl(actualUrl, "sbmdashboard", "sbmdashboard");
							JSONObject json_output = readJsonFromUrl(actualUrl, userId, encryptedPassword);
							JSONObject changesObject = (JSONObject)json_output.get("changelog");
							JSONArray histories = (JSONArray)changesObject.get("histories");
							for (int ix = 0; ix < histories.length(); ix++) {
								try {
									JSONObject history = histories.getJSONObject(ix);
									String created = history.getString("created");
									if((created != null) && (!created.trim().equals(""))) {
										String date = created.substring(0, 10);
										String time = created.substring(11,23);
										created = date+" "+time;
//										System.out.println("***************&&&&&&&****** "+created);
										
									}
									JSONArray items = history.getJSONArray("items");
									for (int ixx = 0; ixx < items.length(); ixx++) {
										try {
											JSONObject item = items.getJSONObject(ixx);
											String status = item.getString("field");
											if((status != null) && ((status.trim().equals("status")))) { 
//													|| (status.trim().equals("resolution")))) {
												String toStr = item.getString("to");
												String toFrom = item.getString("from");
												if((toStr != null) && (!toStr.trim().equals("null"))) {
													int to = Integer.parseInt(toStr);
													UserStoryStatus userStoryStatus = new UserStoryStatus();
													userStoryStatus.setUserStoryName(userstoryName);
													userStoryStatus.setStatusCode(to);
													userStoryStatus.setDateTime(created);
													userStoryStatus.setUserstoryId(userstoryId);
													userstoryStatusList.add(userStoryStatus);
//													System.out.println(userStoryStatus.toString());
												}
												if((toFrom != null) && (!toFrom.trim().equals("null"))) {
													int from = Integer.parseInt(toFrom);
													if(from == StoryPointService.STATUS_CODE_ISSUE_CREATED) {
														if(!issueCreated) {
															UserStoryStatus userStoryStatus = new UserStoryStatus();
															userStoryStatus.setUserStoryName(userstoryName);
															userStoryStatus.setStatusCode(from);
															userStoryStatus.setDateTime(created);
															userStoryStatus.setUserstoryId(userstoryId);
															userstoryStatusList.add(userStoryStatus);
//															System.out.println(userStoryStatus.toString());
															issueCreated = true;
														}
													}
												}
											}
										}
										catch(Exception e1) {
											e1.printStackTrace();
										}
									}
								}
								catch(Exception e12) {
									e12.printStackTrace();
								}
							}
						}
						catch(Exception e2) {
							e2.printStackTrace();
							logger.error("ERROR :: pullUserStoryStatusDetails() : Fetching UserStoryStatus: " + e2.getMessage());
						}
					}
					//Add UserStoryStatus to database
					this.userStoryStatusChartService.addUserStoryStatusToDatabase(sprintId, rapidviewId, userstoryStatusList);
				}
				catch(Exception e3) {
					e3.printStackTrace();
					logger.error("ERROR :: pullUserStoryStatusDetails() : Fetching  UserStory Details : " + e3.getMessage());
				}
			}
		}
		catch(Exception e4) {
			e4.printStackTrace();
			logger.error("ERROR :: pullUserStoryStatusDetails() : Fetching Sprint Details : " + e4.getMessage());
		}
	}
}
