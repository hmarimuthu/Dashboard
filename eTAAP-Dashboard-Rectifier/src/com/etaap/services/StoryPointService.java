package com.etaap.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.etaap.domain.StoryPoint;
import com.etaap.services.BurndownChartService;

public class StoryPointService extends APIPullService {
	
	public static final int STATUS_CODE_ISSUE_VERIFY = 10003;	
	public static final int STATUS_CODE_ISSUE_FIXED = 1;
	
	
	public static final int STATUS_CODE_ISSUE_CREATED = 10000;
	public static final int STATUS_CODE_COMPLETED = 6;
	public static final int STATUS_CODE_ISSUE_REOPENED = 3;
	public static final int STATUS_CODE_NO_STATUS = 0;
	public static final int ADDED_TO_SPRINT = 1;
	public static final int REMOVED_FROM_SPRINT = 1;
	
	public StoryPointService(BurndownChartService burndownChartService) {
		this.burndownChartService = burndownChartService;
	}
	
	private BurndownChartService burndownChartService;
	
	/**key = UserStoryName  value = List
	 *                             StoryPoint1   
	 *                             StoryPoint2   
	 *                             StoryPoint3
	 *                                .
	 *                                .
	 *                                .   
	 *                             StoryPointN
	 */
    private LinkedHashMap<String, List<StoryPoint>> toAddStoryPointsRecords = new LinkedHashMap<String, List<StoryPoint>>();
	
	private static final Logger logger = Logger.getLogger(StoryPointService.class);
	
	public static void main(String args[]) throws IOException, JSONException {
		StoryPointService spsp = new StoryPointService(null);
		spsp.pullStroyPointDetails();
	}
	
	public void pullStroyPointDetails() throws IOException, JSONException {
		logger.info("Inside StoryPointService :: pullStroyPointDetails");
		List<Map<String, Object>> sprintDetailsWithURLList = this.burndownChartService.getSprintDetailsWithURL();
		for(Map map : sprintDetailsWithURLList) {
			try {
				String url = (String)map.get("url");
				int sprintId = (Integer)map.get("sprint_id");
				int rapidviewId = (Integer)map.get("rapidview_id");
				String userId = (String)map.get("user_id");
				String encryptedPassword = (String)map.get("password");
				String sprintName = (String)map.get("sprint_name");
				int appId = (Integer)map.get("app_id");
				
/*				int sprintId = 25546;
				int rapidviewId = 9421;
*/				
//				String actualUrl = "https://jira.intuit.com"+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId=9421&sprintId=25546";
//				String actualUrl = "https://jira.intuit.com"+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId=9425&sprintId=24204";
//				String actualUrl = "https://jira.intuit.com"+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId=9425&sprintId=19984";
				String actualUrl = url+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId="+rapidviewId+"&sprintId="+sprintId;
//				JSONObject json_output = readJsonFromUrl(actualUrl, "sbmdashboard", "sbmdashboard");
				JSONObject json_output = readJsonFromUrl(actualUrl, userId, encryptedPassword);
				
				long sprintStartDateTime = json_output.getLong("startTime");
				long sprintEndDateTime = json_output.getLong("endTime");
				
				this.burndownChartService.saveSprintStartEndDateTime(appId, sprintId, rapidviewId, sprintStartDateTime, sprintEndDateTime);
				System.out.println("MNMNMNMNMNM 3.1 "+sprintId+","+rapidviewId+","+sprintStartDateTime+","+sprintEndDateTime+","+sprintName);
				
				JSONObject changesObject = (JSONObject)json_output.get("changes");
				Iterator changesObjectIterator = changesObject.keys();
				while(changesObjectIterator.hasNext()) {
					String key =  (String)changesObjectIterator.next();
					if(key != null) {
						long activityTime = Long.parseLong(key);
						JSONArray recordJSONObject = (JSONArray)changesObject.get(key);
						if(recordJSONObject != null) {
							for (int k = 0; k < recordJSONObject.length(); k++) {
								JSONObject storyPointJSONObject = recordJSONObject.getJSONObject(k);
								collectStoryPoints(storyPointJSONObject, activityTime);
							}
						}
					}
				}
				
				
				orderStoryPointByActivityMili();
				processOldValueStorypoints();
				processReopendStorypoints();
				processCompletedStorypoints();
				processRemovedFromSprintStorypoints();
				collectUserStoryId(appId, sprintId, rapidviewId);
				System.out.println(toAddStoryPointsRecords);
				this.burndownChartService.addStoryPointsToDatabase(appId, sprintId, rapidviewId, this.getStoryPoints());
				toAddStoryPointsRecords.clear();
			}
			catch(Exception e) {
				e.printStackTrace();
				logger.error("ERROR :: StoryPointService :: pullStroyPointDetails " + e.getMessage());
			}
		}
	}
	
/*	private void removeUnrelatedStoryPoints() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			ArrayList<StoryPoint> toRemove = new ArrayList<StoryPoint>(); 
			boolean added = false;
			boolean active = false;
			int previousStoryPointRemovedFromSprint = 0;
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				if((currentStoryPoint.getStatusCode() == this.STATUS_CODE_ISSUE_CREATED) 
				 || (currentStoryPoint.getStatusCode() == this.STATUS_CODE_ISSUE_REOPENED)
				 || (currentStoryPoint.getIsAdded() == this.ADDED_TO_SPRINT)) {
					added = true;
					active = true;
					previousStoryPointRemovedFromSprint = 0;
				}
				else if((currentStoryPoint.getStatusCode() == this.STATUS_CODE_COMPLETED) 
						 || (currentStoryPoint.getIsDelelted() == this.REMOVED_FROM_SPRINT)) {
					if(!active) {
						if(!((previousStoryPointRemovedFromSprint == this.REMOVED_FROM_SPRINT) && 
								(currentStoryPoint.getStatusCode() == this.STATUS_CODE_COMPLETED))) {
							toRemove.add(currentStoryPoint);
							System.out.println("To remove "+currentStoryPoint.toString());
						}
					}
					if(added) {
						added = false;
					}
					active = false;
					previousStoryPointRemovedFromSprint = currentStoryPoint.getIsDelelted();
				}
				else {
					if(!active) {
						toRemove.add(currentStoryPoint);
						System.out.println("To remove "+currentStoryPoint.toString());
					}
					previousStoryPointRemovedFromSprint = 0;
				}
			}
			
			storyPointsList.removeAll(toRemove);
		}
		*/
		
				
				
/*				if(currentStoryPoint.getStatusCode() == this.STATUS_CODE_ISSUE_REOPENED) {
					if(currentStoryPoint.getIncrement() > 0) {
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue reopened.<br>Story Points: "+currentStoryPoint.getIncrement());
					}
					else {
						int incrementSum = 0;
						//Get sum of increment from this storypoint till same storypoint is created
						boolean sessionEnd = false;
						middle: for(int x = (j - 1); ((x >= 0) && (!sessionEnd)); x--) {
							StoryPoint orderedStoryPoint = storyPointsList.get(x);
							int status = orderedStoryPoint.getStatusCode();
							if((status == this.STATUS_CODE_ISSUE_CREATED) || (status == this.STATUS_CODE_ISSUE_REOPENED)) {
								sessionEnd = true;
							}
							incrementSum = incrementSum + orderedStoryPoint.getIncrement();
						}
						
						currentStoryPoint.setIncrement(incrementSum);
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue reopened.<br>Story Points: "+currentStoryPoint.getIncrement());
					}
				}
			}
			
		}			
	}*/
	
	private void orderStoryPointByActivityMili() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> unOrderedStoryPointsList = toAddStoryPointsRecords.get(userstoryName);
			List<Long> timeList = new ArrayList<Long>();
			for(StoryPoint  spt1 : unOrderedStoryPointsList) {
				long activityInMili = spt1.getActivityMiliSecs();
				timeList.add(activityInMili);
			}
			Collections.sort(timeList);
			
			ArrayList<StoryPoint> orderedStoryPointsList = new ArrayList<StoryPoint>();
			for(long time : timeList) {
				orderedStoryPointsList.add(null);
			}
//			orderedStoryPointsList.ensureCapacity(timeList.size());
			
			for(StoryPoint  unOrderedStoryPoint : unOrderedStoryPointsList) {
				long activityInMili = unOrderedStoryPoint.getActivityMiliSecs();
				int indexOfStoryPoint = timeList.indexOf(activityInMili);
				orderedStoryPointsList.set(indexOfStoryPoint, unOrderedStoryPoint);
			}
//			System.out.println("MNMNMNMNMNM "+userstoryName+"="+timeList);
//			System.out.println("MNMNMNMNMNM "+userstoryName+"="+orderedStoryPointsList);
			toAddStoryPointsRecords.put(userstoryName, orderedStoryPointsList);
		}
	}
	
	private void collectStoryPoints(JSONObject storyPointJSONObject, long activityTime) throws JSONException {
		StoryPoint currentStoryPoint = null;
		
		String userStoryName = storyPointJSONObject.getString("key");
		if(userStoryName.trim().equals("QBKS-2669")) {
			System.out.println("Start");
		}
		int isAddedInt = 0;
		int isDeletedInt = 0;
		int statusCode = STATUS_CODE_NO_STATUS;
		int newValue = 0;
		int oldValue = 0;
		int increment = 0;
		int decrement = 0;
		
		int incrementSum = 0;
		int decrementSum = 0;
		
		if(this.toAddStoryPointsRecords.containsKey(userStoryName)) {
			List<StoryPoint> storyPointList = toAddStoryPointsRecords.get(userStoryName);
			boolean currentStoryPtPresent = false;
			for(int j = 0; ((j < storyPointList.size()) && (!currentStoryPtPresent)); j++) {
				StoryPoint spt = (StoryPoint)storyPointList.get(j);
				if(spt.getActivityMiliSecs() == activityTime) {
					currentStoryPoint = spt;
					currentStoryPtPresent = true;
				}
			}
			if(!currentStoryPtPresent) {
				currentStoryPoint = new StoryPoint();
				storyPointList.add(currentStoryPoint);
			}
		}
		else {
			currentStoryPoint = new StoryPoint();
			List<StoryPoint> storyPointList = new ArrayList<StoryPoint>();
			storyPointList.add(currentStoryPoint);
			this.toAddStoryPointsRecords.put(userStoryName, storyPointList);
		}
		
		currentStoryPoint.setActivityMiliSecs(activityTime);
		currentStoryPoint.setUserStoryName(userStoryName);
		
		if(storyPointJSONObject.has("added")) {
			//if added = true, issued is added to sprint.
			//if added = false, issued is removed from sprint.
			boolean isAdded = storyPointJSONObject.getBoolean("added");
			if(isAdded) {
				isAddedInt = this.ADDED_TO_SPRINT;
			}
			else {
				isDeletedInt = this.REMOVED_FROM_SPRINT;
			}
		}
		currentStoryPoint.setIsAdded(isAddedInt);
		currentStoryPoint.setIsDelelted(isDeletedInt);
			
		if(storyPointJSONObject.has("column")) {
			JSONObject columnJSONObject = (JSONObject)storyPointJSONObject.get("column");
			if(columnJSONObject.has("newStatus")) {
				//status == 10000, issue added to sprint
				//status == 6, issue is completed
				//status == 3, issue is reopened
				statusCode = columnJSONObject.getInt("newStatus");
			}
		}						
		currentStoryPoint.setStatusCode(statusCode);
		
		if(storyPointJSONObject.has("statC")) {
			JSONObject statCJSONObject = (JSONObject)storyPointJSONObject.get("statC");
			boolean oldValuePresent = false;
			boolean newValuePresent = false;
			if(statCJSONObject.has("newValue")) {
				newValuePresent = true;
				//new story points added when newValue is present
				newValue = statCJSONObject.getInt("newValue");
			}
			if(statCJSONObject.has("oldValue")) {
				oldValuePresent = true;
				//Estimate is changed from oldValue to newValue.
				oldValue = statCJSONObject.getInt("oldValue");
			}
			
			if(oldValuePresent && newValuePresent) {
				if(oldValue > newValue) {
					decrement = oldValue - newValue;
				}
				else if(oldValue < newValue) {
					increment = newValue - oldValue;
				}
				else if(oldValue == newValue) {
					increment = newValue;
				}
			}
			else if(!oldValuePresent && newValuePresent) {
				increment = newValue;
			}
			else if(oldValuePresent && !newValuePresent) {
				currentStoryPoint.setOldValue(oldValue);
			}
			
			currentStoryPoint.setIncrement(increment);
			currentStoryPoint.setDecrement(decrement);
			
			if(isDeletedInt == this.REMOVED_FROM_SPRINT) {
				currentStoryPoint.setIncrement(0);
				currentStoryPoint.setDecrement(0);
			}			
		}
		
		if((statusCode == this.STATUS_CODE_NO_STATUS) && (isDeletedInt == 0)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Estimate of "+increment+" has been added.<br>Story Points: "+increment);
			}
			
			if(oldValue > newValue) {
				if(decrement > 0) {
					currentStoryPoint.setStatusMessage("Scope change.<br>Estimate changed from "+oldValue+" to "+newValue+".<br>Story Points: -"+decrement);
				}
			}
		}
		
		if((statusCode == this.STATUS_CODE_NO_STATUS) && (isAddedInt == this.ADDED_TO_SPRINT)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.<br>Story Points: "+increment);
			}
			else {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.");
			}
		}
		if((statusCode == STATUS_CODE_ISSUE_CREATED) && (isAddedInt == this.ADDED_TO_SPRINT)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.<br>Story Points: "+increment);
			}
			else {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.");
			}
		}
		
		if((statusCode == STATUS_CODE_ISSUE_CREATED) && (isAddedInt != this.ADDED_TO_SPRINT)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue created.<br>Story Points: "+increment);
			}
			else {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue created.");
			}
		}
	}
	
	private void processOldValueStorypoints() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				int oldValue = currentStoryPoint.getOldValue();
				if(oldValue > 0) {
					boolean sessionEnd = false;
					middle: for(int x = (j - 1); ((x >= 0) && (!sessionEnd)); x--) {
						StoryPoint orderedStoryPoint = storyPointsList.get(x);
						int status = orderedStoryPoint.getStatusCode();
						if(status == this.STATUS_CODE_ISSUE_CREATED) {
							sessionEnd = true;
							orderedStoryPoint.setIncrement(0);
							continue middle;
						}
						else if(status == this.STATUS_CODE_ISSUE_REOPENED) {
							sessionEnd = true;
							continue middle;
						}
						else if(orderedStoryPoint.getIsDelelted() == this.REMOVED_FROM_SPRINT) {
							sessionEnd = true;
							continue middle;
						}
						else {
							orderedStoryPoint.setIncrement(0);
						}
					}
					currentStoryPoint.setIncrement(0);
				}
			}
		}		
	}	
	
	
	private void processReopendStorypoints() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				if(currentStoryPoint.getStatusCode() == this.STATUS_CODE_ISSUE_REOPENED) {
					if(currentStoryPoint.getIncrement() > 0) {
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue reopened.<br>Story Points: "+currentStoryPoint.getIncrement());
					}
					else {
						int incrementSum = 0;
						//Get sum of increment from this storypoint till same storypoint is created
						boolean sessionEnd = false;
						middle: for(int x = (j - 1); ((x >= 0) && (!sessionEnd)); x--) {
							StoryPoint orderedStoryPoint = storyPointsList.get(x);
							int status = orderedStoryPoint.getStatusCode();
							if((status == this.STATUS_CODE_ISSUE_CREATED) || (status == this.STATUS_CODE_ISSUE_REOPENED)) {
								sessionEnd = true;
							}
							incrementSum = incrementSum + orderedStoryPoint.getIncrement();
						}
						
						currentStoryPoint.setIncrement(incrementSum);
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue reopened.<br>Story Points: "+currentStoryPoint.getIncrement());
					}
				}
			}
		}		
	}	
	
	
	private void processRemovedFromSprintStorypoints() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				if(currentStoryPoint.getIsDelelted() == REMOVED_FROM_SPRINT) {
					if(currentStoryPoint.getDecrement() > 0) {
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue removed from sprint.<br>Story Points: -"+currentStoryPoint.getDecrement());
					}
					else {
						int incrementSum = 0;
						int decrementSum = 0;
						//Get sum of increment and decrement from this storypoint till same storypoint is created or deleted
						//from sprint
						boolean sessionEnd = false;
						middle: for(int x = (j - 1); ((x >= 0) && (!sessionEnd)); x--) {
							StoryPoint orderedStoryPoint = storyPointsList.get(x);
							int status = orderedStoryPoint.getStatusCode();
							if(orderedStoryPoint.getIsDelelted() == REMOVED_FROM_SPRINT) {
								sessionEnd = true;
								continue middle;
							}
							if((status == this.STATUS_CODE_ISSUE_CREATED) || (status == this.STATUS_CODE_ISSUE_REOPENED)) {
								sessionEnd = true;
							}
							incrementSum = incrementSum + orderedStoryPoint.getIncrement();
							decrementSum = decrementSum + orderedStoryPoint.getDecrement();
						}
						
						if(incrementSum == decrementSum) {
//							currentStoryPoint.setDecrement(decrementSum);
							currentStoryPoint.setDecrement(0);
						}
						if(incrementSum > decrementSum) {
							decrementSum = incrementSum - decrementSum;
							currentStoryPoint.setDecrement(decrementSum);
						}
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue removed from sprint.<br>Story Points: -"+currentStoryPoint.getDecrement());
					}
				}
			}
		}		
	}	
	
	private void processCompletedStorypoints() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				if(currentStoryPoint.getStatusCode() == this.STATUS_CODE_COMPLETED) {
					if(currentStoryPoint.getDecrement() > 0) {
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue completed<br>Story Points: -"+currentStoryPoint.getDecrement());
					}
					else {
						int incrementSum = 0;
						int decrementSum = 0;
						//Get sum of increment and decrement from this storypoint till same storypoint is created or deleted
						//from sprint
						boolean sessionEnd = false;
						middle: for(int x = (j - 1); ((x >= 0) && (!sessionEnd)); x--) {
							StoryPoint orderedStoryPoint = storyPointsList.get(x);
							int status = orderedStoryPoint.getStatusCode();
							if(orderedStoryPoint.getIsDelelted() == REMOVED_FROM_SPRINT) {
								sessionEnd = true;
								continue middle;
							}
							if((status == this.STATUS_CODE_ISSUE_CREATED) || (status == this.STATUS_CODE_ISSUE_REOPENED)) {
								sessionEnd = true;
							}
							incrementSum = incrementSum + orderedStoryPoint.getIncrement();
							decrementSum = decrementSum + orderedStoryPoint.getDecrement();
						}
						
						if(incrementSum == decrementSum) {
							currentStoryPoint.setDecrement(decrementSum);
						}
						if(incrementSum > decrementSum) {
							decrementSum = incrementSum - decrementSum;
							currentStoryPoint.setDecrement(decrementSum);
						}
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue completed<br>Story Points: -"+currentStoryPoint.getDecrement());
					}
				}
			}
		}		
	}
	
	
	
	private List<StoryPoint> getStoryPoints() {
		List<StoryPoint> allStoryPoints = new ArrayList<StoryPoint>();
		for(String key : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPoints = toAddStoryPointsRecords.get(key);
			allStoryPoints.addAll(storyPoints);
		}
		return allStoryPoints;
	}

	private void collectUserStoryId(int appId, int sprintId, int rapidviewId) {
		List<Map<String, Object>> dataFromDB = burndownChartService.getUserStoryId(appId, sprintId, rapidviewId);
		System.out.println("MNMNMNMNMNM 55 "+dataFromDB);
		for(Map map : dataFromDB) {
			String key = (String)map.get("key");
			Integer id = (Integer)map.get("id");
			System.out.println("ID="+id+" ,KEY="+key);
			if(id.intValue() > 0) {
			    if(toAddStoryPointsRecords.containsKey(key)) {
					List<StoryPoint> storyPoints = toAddStoryPointsRecords.get(key);
					for(StoryPoint spt :  storyPoints) {
						spt.setUserstoryId(id);
					}
				}
			}
			else {
				System.out.println("MNMNMNMNMNM 56 UserStory="+key+" does not exist into jira_userstory.");
				logger.error("ERROR :: StoryPointService :: collectUserStoryId :: UserStory="+key+" does not exist into jira_userstory.");
			}
		}
	}
}
