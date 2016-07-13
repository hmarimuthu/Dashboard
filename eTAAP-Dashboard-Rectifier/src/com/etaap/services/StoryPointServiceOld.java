package com.etaap.services;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.etaap.domain.StoryPoint;
import com.etaap.services.BurndownChartService;

public class StoryPointServiceOld extends APIPullService {
	
	public static final int STATUS_CODE_ISSUE_CREATED = 10000;
	private static final int STATUS_CODE_COMPLETED = 6;
	private static final int STATUS_CODE_ISSUE_REOPENED = 3;
	
	
	public StoryPointServiceOld(BurndownChartService burndownChartService) {
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
	
	private static final Logger logger = Logger.getLogger(StoryPointServiceOld.class);
	
/*	public static void main(String args[]) throws IOException, JSONException {
		StoryPointService spsp = new StoryPointService();
		spsp.pullStroyPointDetails(null);
	}
*/	
	public void pullStroyPointDetails() throws IOException, JSONException {
		logger.info("Inside StoryPointService :: pullStroyPointDetails");
		System.out.println("MNMNMNMNMNM 1");
		List<Map<String, Object>> sprintDetailsWithURLList = this.burndownChartService.getSprintDetailsWithURL();
//		for(Map map : sprintDetailsWithURLList) {
			try {
//				String url = (String)map.get("url");
//				int sprintId = (Integer)map.get("sprint_id");
//				int rapidviewId = (Integer)map.get("rapidview_id");
				
				int sprintId = 19984;
				int rapidviewId = 9425;
				
				String actualUrl = "https://jira.intuit.com"+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId=9425&sprintId=19984";
//				String actualUrl = "https://jira.intuit.com"+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId=9425&sprintId=19984";
//				String actualUrl = url+"/rest/greenhopper/1.0/rapid/charts/scopechangeburndownchart?rapidViewId="+rapidviewId+"&sprintId="+sprintId;
				JSONObject json_output = readJsonFromUrl(actualUrl, "sbmdashboard", "sbmdashboard");
				System.out.println("MNMNMNMNMNM 2 "+json_output.toString());
				
				
				
				JSONObject changesObject = (JSONObject)json_output.get("changes");
				Iterator changesObjectIterator = changesObject.keys();
				while(changesObjectIterator.hasNext()) {
					String key =  (String)changesObjectIterator.next();
					if(key != null) {
						long activityTime = Long.parseLong(key);
						JSONArray recordJSONObject = (JSONArray)changesObject.get(key);
						System.out.println("MNMNMNMNMNM 3 "+recordJSONObject.toString());
						if(recordJSONObject != null) {
							for (int k = 0; k < recordJSONObject.length(); k++) {
								JSONObject storyPointJSONObject = recordJSONObject.getJSONObject(k);
								System.out.println("MNMNMNMNMNM 4 "+storyPointJSONObject.toString());
								collectStoryPoints(storyPointJSONObject, activityTime);
							}
						}
					}
				}
				
//				setReopenedData();				
				System.out.println("MNMNMNMNMNM 4.1 ");
				collectUserStoryId(sprintId, rapidviewId);
				System.out.println("MNMNMNMNMNM 5 ");
				setStatusMessageOnDelete();
				System.out.println("MNMNMNMNMNM 6 ");
				setStatusMessageOnCompletion();
				System.out.println("MNMNMNMNMNM 7 ");
				System.out.println("All Data: ");
				System.out.println(toAddStoryPointsRecords);
//				this.burndownChartService.addStoryPointsToDatabase(sprintId, rapidviewId, this.getStoryPoints());
				System.out.println("MNMNMNMNMNM 8 ");
				toAddStoryPointsRecords.clear();
			}
			catch(Exception e) {
				e.printStackTrace();
				logger.error("ERROR :: StoryPointService :: pullStroyPointDetails " + e.getMessage());
			}
//		}
	}
	
	
	
	private void collectStoryPoints(JSONObject storyPointJSONObject, long activityTime) throws JSONException {
		StoryPoint currentStoryPoint = null;
		
		String userStoryName = storyPointJSONObject.getString("key");
		int isAddedInt = 0;
		int isDeletedInt = 0;
		int statusCode = -1;
		int newValue = -1;
		int oldValue = -1;
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
				isAddedInt = 1;
			}
			else {
				isDeletedInt = 1;
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
				System.out.println("Status Code "+statusCode);
			}
		}						
		currentStoryPoint.setStatusCode(statusCode);
		
		if(storyPointJSONObject.has("statC")) {
			JSONObject statCJSONObject = (JSONObject)storyPointJSONObject.get("statC");
/*			int newValue = -1;
			int oldValue = -1;
*/			boolean oldValuePresent = false;
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
		
			
/*			int increment = 0;
			int decrement = 0;
			*/
			if(oldValuePresent && newValuePresent) {
				if(oldValue > newValue) {
					decrement = oldValue - newValue;
				}
				else if(oldValue < newValue) {
					increment = newValue - oldValue;
				}
			}
			else if(!oldValuePresent && newValuePresent) {
				increment = newValue;
			}
			
			currentStoryPoint.setIncrement(increment);
			currentStoryPoint.setDecrement(decrement);
		}
		
		if((statusCode == -1) && (isDeletedInt == 0)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Estimate of "+increment+" has been added.<br>Story Points: "+increment);
			}
			
			if(oldValue > newValue) {
				if(decrement > 0) {
					currentStoryPoint.setStatusMessage("Scope change.<br>Estimate changed from "+oldValue+" to "+newValue+".<br>Story Points: -"+decrement);
				}
			}
		}
		
		if((statusCode == -1) && (isAddedInt == 1)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.<br>Story Points: "+increment);
			}
			else {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.");
			}
		}
		if((statusCode == STATUS_CODE_ISSUE_CREATED) && (isAddedInt == 1)) {
			currentStoryPoint.setStatusMessage("Scope change.<br>Issue added to sprint.");
		}
		if((statusCode == STATUS_CODE_ISSUE_CREATED) && (isAddedInt != 1)) {
			if(increment > 0) {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue created.<br>Story Points: -"+increment);
			}
			else {
				currentStoryPoint.setStatusMessage("Scope change.<br>Issue created.");
			}
		}
		
		
/*		System.out.println("activityTime="+activityTime+", userStoryName="+userStoryName+", isAddedInt="+isAddedInt+", isDeletedInt="+isDeletedInt+
				", statusCode="+statusCode+", newValue="+newValue+", oldValue="+oldValue+", increment="+increment+", decrement="+decrement+
				", incrementSum="+incrementSum+", decrementSum="+decrementSum);
*/		
		
	}
	
/*	private StoryPoint getLatestClosedStoryPoint(StoryPoint currentStoryPoint, String userstoryName) {
		List<StoryPoint> storyPointsList1 = toAddStoryPointsRecords.get(userstoryName);
		List<Long> timeList = new ArrayList<Long>();
		for(StoryPoint  spt1 : storyPointsList1) {
			long activityInMili = spt1.getActivityMiliSecs();
			timeList.add(activityInMili);
		}
		Collections.sort(timeList);
		System.out.println("NNNNNNNNNNNNNNN "+userstoryName+" "+timeList);
		
		
		
		long currentActivityMili = currentStoryPoint.getActivityMiliSecs();
		int indexOfCurrentStoryPoint = timeList.indexOf(currentActivityMili);
		
		List<StoryPoint> sortedStoryPoints = new ArrayList<StoryPoint>();
		
		List<StoryPoint> storyPointsList2 = toAddStoryPointsRecords.get(userstoryName);
		for(StoryPoint  spt2 : storyPointsList2) {
			for(Long time : timeList) {
				
			}
		}
	}
*/	
/*	private void setReopenedDataOld() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				long activityTimeWhileReopening = currentStoryPoint.getActivityMiliSecs();
				if(currentStoryPoint.getStatusCode() == STATUS_CODE_ISSUE_REOPENED) {
					System.out.println("VVVVVVVVVVVVVVVVV "+currentStoryPoint.getUserStoryName()+" REOPENED.....");
					if(currentStoryPoint.getIncrement() > 0) {
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue reopened<br>Story Points: -"+currentStoryPoint.getIncrement());
					}
					else {
						int sumOfIncrement = 0;
						List<StoryPoint> storyPointsListForThisUserstory = toAddStoryPointsRecords.get(userstoryName);
						//Order the list in ascending order of timelist
						List<Long> timeList = new ArrayList<Long>();
						for(StoryPoint  spt1 : storyPointsListForThisUserstory) {
							
						}
						
						
						for(StoryPoint  spt : storyPointsListForThisUserstory) {
							long activityMs = spt.getActivityMiliSecs();
							if(activityMs < activityTimeWhileReopening) {
								if(spt.getIncrement() > 0) {
									sumOfIncrement = sumOfIncrement + spt.getIncrement();
								}
							}
						}
						currentStoryPoint.setIncrement(sumOfIncrement);
					}
				}
			}
		}		
	}*/
	
	private void setStatusMessageOnCompletion() {
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
						//Get sum(increment) for this userstory within this pull.
						//Get sum(decrement) for this userstory within this pull.
						List<StoryPoint> allStoryPointsList = toAddStoryPointsRecords.get(userstoryName);
						for(StoryPoint  spt : allStoryPointsList) {
	/*						System.out.println("Increment in "+spt.getIncrement());
							System.out.println("Decrement in "+spt.getDecrement());
	*/						incrementSum = incrementSum + spt.getIncrement();
							decrementSum = decrementSum + spt.getDecrement();
						}
						
						if(incrementSum == decrementSum) {
							currentStoryPoint.setDecrement(decrementSum);
						}
						if(incrementSum > decrementSum) {
							decrementSum = incrementSum - decrementSum;
							currentStoryPoint.setDecrement(decrementSum);
						}
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue completed<br>Story Points: -"+decrementSum);
					}
				}
			}
		}		
	}
	
	
	private void setStatusMessageOnDelete() {
		for(String userstoryName : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPointsList = toAddStoryPointsRecords.get(userstoryName);
			for(int j = 0; j < storyPointsList.size(); j++) {
				StoryPoint currentStoryPoint = (StoryPoint)storyPointsList.get(j);
				if((currentStoryPoint.getIsDelelted() == 1) && (currentStoryPoint.getStatusCode() == -1)) {
					if(currentStoryPoint.getDecrement() > 0) {
						currentStoryPoint.setStatusMessage("Burndown.<br>Issue removed from sprint.<br>Story Points: -"+currentStoryPoint.getDecrement());
					}
					else {
						int incrementSum = 0;
						int decrementSum = 0;
						//Get sum(increment) for this userstory within this pull.
						//Get sum(decrement) for this userstory within this pull.
						List<StoryPoint> allStoryPointsList = toAddStoryPointsRecords.get(userstoryName);
						for(StoryPoint  spt : allStoryPointsList) {
	/*						System.out.println("Increment in "+spt.getIncrement());
							System.out.println("Decrement in "+spt.getDecrement());
	*/						incrementSum = incrementSum + spt.getIncrement();
							decrementSum = decrementSum + spt.getDecrement();
						}
						
						if(incrementSum == decrementSum) {
							currentStoryPoint.setDecrement(decrementSum);
						}
						if(incrementSum > decrementSum) {
							decrementSum = incrementSum - decrementSum;
							currentStoryPoint.setDecrement(decrementSum);
						}
						currentStoryPoint.setStatusMessage("Scope change.<br>Issue removed from sprint.<br> Story Points: -"+decrementSum);
					}
				}
			}
		}		
	}
	
/*	public void addStoryPointsToDatabase(int sprintId, int rapidviewId, final List<StoryPoint> storyPointList) {
		this.burndownChartService.deleteOldStoryPoints(sprintId, rapidviewId);
		
		String insertSql = "INSERT INTO `jira_storypoint` (`userstory_id`,`increment`,`decrement`,`status_code`,`isAdded`,`isDeleted`,`status_message`,`activity_mili`) VALUES "+
		"(?,?,?,?,?,?,?,?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
				
		@Override
		public void setValues(PreparedStatement ps, int i) throws SQLException {
			StoryPoint storyPoint = storyPointList.get(i);
			ps.setInt(1, storyPoint.getUserstoryId());
			ps.setInt(2, storyPoint.getIncrement());
			ps.setInt(3, storyPoint.getDecrement());
			ps.setInt(4, storyPoint.getStatusCode());
			ps.setInt(5, storyPoint.getIsAdded());
			ps.setInt(6, storyPoint.getIsDelelted());
			ps.setString(7, storyPoint.getStatusMessage());
			ps.setLong(8, storyPoint.getActivityMiliSecs());
		}
				
		@Override
		public int getBatchSize() {
			return storyPointList.size();
		}
	  });
	}
*/	
	private List<StoryPoint> getStoryPoints() {
		List<StoryPoint> allStoryPoints = new ArrayList<StoryPoint>();
		for(String key : toAddStoryPointsRecords.keySet()) {
			List<StoryPoint> storyPoints = toAddStoryPointsRecords.get(key);
			allStoryPoints.addAll(storyPoints);
		}
		return allStoryPoints;
	}

/*	private void deleteOldStoryPoints(int sprintId, int rapidviewId) {
		String deleteSql = "delete from jira_storypoint where userstory_id IN "
				+ "(select id from jira_userstory where sprint_id = "+sprintId +" and "+
				" rapidview_id = "+rapidviewId+")";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(deleteSql);
	}
*/	

	private void collectUserStoryId(int sprintId, int rapidviewId) {
		List<Map<String, Object>> dataFromDB = null; //burndownChartService.getUserStoryId(sprintId, rapidviewId);
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
