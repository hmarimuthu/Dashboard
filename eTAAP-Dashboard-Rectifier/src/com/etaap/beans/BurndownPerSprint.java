package com.etaap.beans;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.etaap.services.BurndownChartService;
import com.etaap.services.StoryPointService;
import com.etaap.utils.Utils;

public class BurndownPerSprint {
	
	private String titleText = "Burndown Chart for Sprint-";
	private String subTitleText = "";
	private long xAxisSprintStartDateTime; 
	private long xAxisMaxDate;
	private int yAxisMaxStoryPoints;
	private String sprintName;
	private String applicationName;
	
	private List<BurndownSeries> series = new ArrayList<BurndownSeries>();
	
	public static BurndownPerSprint getBurndownChart(BurndownChartService burndownChartService, int appId, int sprintId) {
		
		BurndownPerSprint retVal = new BurndownPerSprint();
		try {
			List<Map<String,Object>> applicationSprintDetailsLi = burndownChartService.getApplicationAndSprintDetails(appId, sprintId);
			if(applicationSprintDetailsLi.size() == 0) {
				throw new RuntimeException("No sprint found for application. Application Id="+appId+". Sprint Id="+sprintId);
			}
			
			HashMap applicationSprintDetailsMap = (HashMap)applicationSprintDetailsLi.get(0);
			String applicationName = (String)applicationSprintDetailsMap.get("app_name");
			String sprintName = (String)applicationSprintDetailsMap.get("sprint_name");
			long startMiliSecs = (Long)applicationSprintDetailsMap.get("start_datetime_mili");
			
			long endMiliSecs = (Long)applicationSprintDetailsMap.get("end_datetime_mili");
			
			retVal.setxAxisSprintStartDateTime(startMiliSecs);
			retVal.setxAxisMaxDate(endMiliSecs);
			retVal.setApplicationName(applicationName);
			retVal.setSprintName(sprintName);
			retVal.setTitleText(retVal.getTitleText()+retVal.getSprintName());
			retVal.setSubTitleText(retVal.getApplicationName());
			
			retVal.populateIdealBurnSeries(retVal, burndownChartService, sprintId, startMiliSecs, endMiliSecs, appId);
			int max = retVal.populateActualBurnSeries(retVal, burndownChartService, sprintId, startMiliSecs, endMiliSecs, appId);
//			System.out.println("KKKKKKKKKK XXXXXXXX Maximum storypoint "+max);
			retVal.setyAxisMaxStoryPoints(max);
			
			long maxStoryPointActivityDatetimeMili = burndownChartService.getMaxStoryPointActivityDatetimeMili(appId, sprintId);
			if(maxStoryPointActivityDatetimeMili > retVal.getxAxisMaxDate()) {
				retVal.setxAxisMaxDate(maxStoryPointActivityDatetimeMili);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return retVal;
	}
	
	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public String getSubTitleText() {
		return subTitleText;
	}

	public void setSubTitleText(String subTitleText) {
		this.subTitleText = this.subTitleText + subTitleText;
	}

	public long getxAxisSprintStartDateTime() {
		return xAxisSprintStartDateTime;
	}

	public void setxAxisSprintStartDateTime(long xAxisSprintStartDateTime) {
		this.xAxisSprintStartDateTime = xAxisSprintStartDateTime;
	}

	public long getxAxisMaxDate() {
		return xAxisMaxDate;
	}

	public void setxAxisMaxDate(long xAxisMaxDate) {
		this.xAxisMaxDate = xAxisMaxDate;
	}

	public int getyAxisMaxStoryPoints() {
		return yAxisMaxStoryPoints;
	}

	public void setyAxisMaxStoryPoints(int yAxisMaxStoryPoints) {
		this.yAxisMaxStoryPoints = yAxisMaxStoryPoints;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	private void populateIdealBurnSeries(BurndownPerSprint retVal, BurndownChartService burndownChartService, int sprintId, long startMiliSecs,
			long sprintEndTime, int appId) {
		//Get number of story points before sprint start
		int storyPointsBeforeSprintStart = burndownChartService.getNumberOfStoryPointsBeforeSprintStart(appId, sprintId, startMiliSecs);
		System.out.println(" KKKKKKKKKK XXXXXXXX Number of story points before sprint start="+storyPointsBeforeSprintStart);

		//Get number of working days for sprint time duration.
		int numberofWorkingDays = Utils.getNumberOfWorkingDays(retVal.getxAxisSprintStartDateTime(), retVal.getxAxisMaxDate());
		System.out.println(" KKKKKKKKKK XXXXXXXX Number of working days="+numberofWorkingDays);
		
		//Get list of working days.
		List<Long> workingDays =  Utils.getWorkingDays(retVal.getxAxisSprintStartDateTime(), retVal.getxAxisMaxDate());
		System.out.println(" KKKKKKKKKK XXXXXXXX working days="+workingDays);
		
		//If working days are more than story points, one story point should be completed in one day.
		//If more than 0 days are pending free, then initial few story points can be completed in two days.
		BurndownSeries idealBurnSeries = new BurndownSeries();
		idealBurnSeries.setColor("rgba(255,0,0,0.25)");
		idealBurnSeries.setName("Ideal Burn");
		idealBurnSeries.setLineWidth(1);
		series.add(idealBurnSeries);
		
		List<BurndownSeriesElement> nestedStringList = idealBurnSeries.getData();
		
		if(storyPointsBeforeSprintStart < numberofWorkingDays) {
			int numberOfDaysPerStoryPoint = 0;
			int pendingDays = 0;
			int storyPointsToDecrease = storyPointsBeforeSprintStart;
			int yValue = storyPointsToDecrease;
			if(storyPointsBeforeSprintStart > 0) {
				numberOfDaysPerStoryPoint = numberofWorkingDays/storyPointsBeforeSprintStart;
				pendingDays = numberofWorkingDays%storyPointsBeforeSprintStart;
				System.out.println(" KKKKKKKKKK XXXXXXXX pending days="+pendingDays);
				if(pendingDays == 0) {
					for(int x = 0; ((x < workingDays.size()) && (storyPointsToDecrease >= 0)); x =  x + numberOfDaysPerStoryPoint) {
					    long xValue = workingDays.get(x);
					    yValue = storyPointsToDecrease;
					    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(yValue, xValue);
						nestedStringList.add(burndownSeriesElement);
					    addSaturdaySunday(yValue, xValue, nestedStringList);					    
						storyPointsToDecrease--;
					}
				}
				else if(pendingDays > 0) {
					int moveDateAhead = 1;
					for(int x = 0; ((x < workingDays.size()) && (storyPointsToDecrease >= 0)); x =  x + (numberOfDaysPerStoryPoint + moveDateAhead)) {
					    yValue = storyPointsToDecrease;
					    long xValue = workingDays.get(x);
					    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(yValue, xValue);
						nestedStringList.add(burndownSeriesElement);
					    addSaturdaySunday(yValue, xValue, nestedStringList);					    
					    storyPointsToDecrease--;
						if(pendingDays <= 0)
							moveDateAhead = 0;
						pendingDays--;
					}
				}
			    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(0, sprintEndTime);
				nestedStringList.add(burndownSeriesElement);
			}
		}
		else if(storyPointsBeforeSprintStart >= numberofWorkingDays) {
			int storyPointsToDecrease = storyPointsBeforeSprintStart;
			int yValue = storyPointsToDecrease;
			int numberOfStoryPointsPerDay = storyPointsBeforeSprintStart/numberofWorkingDays;
			int pendingStoryPoints = storyPointsBeforeSprintStart%numberofWorkingDays;
			System.out.println(" KKKKKKKKKK XXXXXXXX pending story points="+pendingStoryPoints);
			
			for(int x = 0; ((x < workingDays.size()) && (storyPointsToDecrease >= 0)); x++) {
				if((x > 0) && (pendingStoryPoints > 0)) {
					storyPointsToDecrease--;
					yValue = storyPointsToDecrease;
					pendingStoryPoints--;
				}
			    long xValue = workingDays.get(x);
			    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(yValue, xValue);
				nestedStringList.add(burndownSeriesElement);
			    addSaturdaySunday(yValue, xValue, nestedStringList);					    
			    storyPointsToDecrease = storyPointsToDecrease - numberOfStoryPointsPerDay;
				yValue = storyPointsToDecrease;
			}
		    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(0, sprintEndTime);
			nestedStringList.add(burndownSeriesElement);
		}
		System.out.println(" KKKKKKKKKK XXXXXXXX Guideline when pending = 0 "+nestedStringList);
	}
	
	private void addSaturdaySunday(int yValue, long xValue, List<BurndownSeriesElement> nestedStringList) {
		java.util.Date currentDatetime = new java.util.Date(xValue);
	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(currentDatetime); 
	    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
	        startCal.add(Calendar.DAY_OF_MONTH, 2);
		    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(yValue, startCal.getTimeInMillis());
			nestedStringList.add(burndownSeriesElement);
	    }
	}
	
	private BurndownSeriesElement getBurndownSeriesElement(int y, long x) {
		BurndownSeriesElement burndownSeriesElement = new BurndownSeriesElement();
		burndownSeriesElement.setX(x);
		burndownSeriesElement.setY(y);
		burndownSeriesElement.setAddedRemoved(""+y);
		return burndownSeriesElement;
	}
	
	private BurndownSeriesElement getBurndownSeriesElement(int y, long x, String key, String statusMessage) {
		BurndownSeriesElement burndownSeriesElement = new BurndownSeriesElement();
		burndownSeriesElement.setX(x);
		burndownSeriesElement.setY(y);
		if(key != null) {
			burndownSeriesElement.setKeys(key);
		}
		burndownSeriesElement.setAddedRemoved(statusMessage);
		return burndownSeriesElement;
	}
	
	private int populateActualBurnSeries(BurndownPerSprint retVal, BurndownChartService burndownChartService, int sprintId, 
			long startMiliSecs, long endMiliSecs, int appId) {
		int storyPointsBeforeSprintStart = burndownChartService.getNumberOfStoryPointsBeforeSprintStart(appId, sprintId, startMiliSecs);
		int maxStoryPoints =  storyPointsBeforeSprintStart;
		
		BurndownSeries actualBurnSeries = new BurndownSeries();
		actualBurnSeries.setColor("rgba(0,120,200,0.75)");
		actualBurnSeries.setName("Actual Burn");
		actualBurnSeries.setMarkerRadius(4);
		actualBurnSeries.setLineWidth(3);
		actualBurnSeries.setStep(true);
		series.add(actualBurnSeries);
		
		List<BurndownSeriesElement> nestedStringList = actualBurnSeries.getData();
		
		List<Map<String, Object>> burndownDetailsList = burndownChartService.getBurndownDetails(appId, sprintId, startMiliSecs);
		int yValue = storyPointsBeforeSprintStart;
		
		BurndownSeriesElement burndownSeriesElementFirst = getBurndownSeriesElement(yValue, startMiliSecs, null, "Sprint Start");
		nestedStringList.add(burndownSeriesElementFirst);

		for(int i  = 0; i < burndownDetailsList.size(); i++) {
			HashMap burndownDetailsMap = (HashMap)burndownDetailsList.get(i);
			int userstoryId = (Integer)burndownDetailsMap.get("userstory_id");
			int increment = (Integer)burndownDetailsMap.get("increment");
			int decrement = (Integer)burndownDetailsMap.get("decrement");
			String key = (String)burndownDetailsMap.get("key");
			long xValue = (Long)burndownDetailsMap.get("activity_mili");
			String statusMessage = (String)burndownDetailsMap.get("status_message");
			Integer statusCode = (Integer)burndownDetailsMap.get("status_code");
			Integer isAdded = (Integer)burndownDetailsMap.get("isAdded");
			Integer isDeleted = (Integer)burndownDetailsMap.get("isDeleted");
			if(statusCode == StoryPointService.STATUS_CODE_ISSUE_CREATED) {
				if(isAdded == 0) {
					//Display in chart if it is added to sprint in future
					int addedToSprint = burndownChartService.isAddedToSprint(userstoryId);
					if(addedToSprint != StoryPointService.ADDED_TO_SPRINT) {
						continue;
					}
				}
			}
			
			if((isAdded == 0) && (isDeleted == 0)) {
				if(statusCode == StoryPointService.STATUS_CODE_NO_STATUS) {
					if((increment <= 0) && (decrement <= 0)) {
						continue;
					}
					int addedBefore = burndownChartService.isAddedToSprint(userstoryId, xValue);
					int addedAfter = burndownChartService.isAddedInFuture(userstoryId, xValue);
					//Display in chart if it is added to sprint in previous storypoint
					if((addedBefore != StoryPointService.ADDED_TO_SPRINT) && 
							(addedAfter != StoryPointService.ADDED_TO_SPRINT)) {
						continue;
					}
				}
			}
			
			if((isAdded == StoryPointService.ADDED_TO_SPRINT) && (increment == 0)) {
				if(burndownChartService.isAddedFirstTime(userstoryId, xValue)) {				
					//Get sum in case increment is available before added to sprint
					increment = burndownChartService.getIncrementBeforeSprintStart(userstoryId, xValue, startMiliSecs);
					statusMessage = statusMessage+"<br>Story Points: "+increment;
				}
				else if(burndownChartService.isUserstoryReadded(userstoryId, xValue)) {
					//Change increment = decrement of previous record. 
					increment = burndownChartService.getIncrementForReAddedStoryPoint(userstoryId, xValue);
					statusMessage = statusMessage+"<br>Story Points: "+increment;
				}
			}
			
			if((isDeleted == StoryPointService.REMOVED_FROM_SPRINT) && (decrement == 0)) {
				if(burndownChartService.isUserstoryReDeleted(userstoryId, xValue)) {
					decrement = burndownChartService.getDecrementForReDeletedStoryPoint(userstoryId, xValue);
					statusMessage = "Burndown.<br>Issue removed from sprint.<br>Story Points: -"+decrement;
				}
			}
			
			
			if(increment > 0) {
				yValue = yValue + increment;
			}
			else if(decrement > 0) {
				yValue = yValue - decrement;
			}
			
			if(maxStoryPoints < yValue) {
				maxStoryPoints = yValue;
			}
		    BurndownSeriesElement burndownSeriesElement = getBurndownSeriesElement(yValue, xValue, key, statusMessage);
			nestedStringList.add(burndownSeriesElement);
		}
		return maxStoryPoints;
	}
	
}
