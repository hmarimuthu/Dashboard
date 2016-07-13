package com.etaap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.etaap.beans.BurndownPerSprint;
import com.etaap.beans.ChartModel;
import com.etaap.beans.Data;
import com.etaap.beans.UserStoriesPerSprint;
import com.etaap.beans.Sprint;
import com.etaap.common.ApplicationConstants;
import com.etaap.beans.Application;
import com.etaap.services.ApplicationService;
import com.etaap.services.BurndownChartService;
import com.etaap.services.ChartService;
import com.etaap.services.IterationsChartService;
import com.etaap.utils.enums.ChartType;

@Controller
public class IterationsController {

	@Autowired
	ChartService chartService;

	//@Autowired
	ServletContext servletContext;
	
	@Autowired
	private IterationsChartService iterationsChartService;
	
	@Autowired
	private BurndownChartService burndownChartService;

	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	public IterationsChartService getIterationsChartService() {
		return iterationsChartService;
	}

	public void setIterationsChartService(
			IterationsChartService iterationsChartService) {
		this.iterationsChartService = iterationsChartService;
	}
	
	public BurndownChartService getBurndownChartService() {
		return burndownChartService;
	}

	public void setBurndownChartService(BurndownChartService burndownChartService) {
		this.burndownChartService = burndownChartService;
	}
	

	private static final Logger logger = Logger.getLogger(IterationsController.class);

	
	@RequestMapping(value = "iterations-redesign")
	public ModelAndView _firstRequestHandler(Model model, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView("iterations");
		try {
			int appId = -1;
			int sprintId = -1;
			String sprintName = "";
			List<Application> applicationList = iterationsChartService.getApplicationList();
			Application application = null;
			if (applicationList.size() > 0) {
				application = applicationList.get(0);
				appId = application.getAppId();
			}

			com.etaap.domain.Application applicationD = applicationService.getApplication(appId, 0, null);
			redirectAttributes.addFlashAttribute("requestedApp", applicationD.getAppName());

			List<Sprint> sprintList = iterationsChartService.getSprintList(appId);
			if (sprintList.size() > 0) {
				Sprint sprint = null;
				sprint = sprintList.get(0);
				sprintId = sprint.getSprintId();
				sprintName = sprint.getSprintName();
			}

			redirectAttributes.addFlashAttribute("app_id", applicationList);
			redirectAttributes.addFlashAttribute("sprintList", sprintList);
			redirectAttributes.addFlashAttribute("selectedSprint", sprintName);

			modelAndView = new ModelAndView("redirect:/app/" + appId + "/" + ApplicationConstants.JIRA_DEV_REDESIGN + "/iterations/" + appId + ".html?sprintId=" + sprintId);
		} catch (Exception e) {
			logger.error("ERROR :: firstRequestHandler ::" + e.getMessage());
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "iterations")
	public ModelAndView firstRequestHandler(Model model, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView("iterations");
		try {
			int appId = -1;
			int sprintId = -1;
			String sprintName = "";
			List<Application> applicationList = iterationsChartService.getApplicationList();
			Application application = null;
			if (applicationList.size() > 0) {
				application = applicationList.get(0);
				appId = application.getAppId();
			}

			com.etaap.domain.Application applicationD = applicationService.getApplication(appId, 0, null);
			redirectAttributes.addFlashAttribute("requestedApp", applicationD.getAppName());

			List<Sprint> sprintList = iterationsChartService.getSprintList(appId);
			if (sprintList.size() > 0) {
				Sprint sprint = null;
				sprint = sprintList.get(0);
				sprintId = sprint.getSprintId();
				sprintName = sprint.getSprintName();
			}

			redirectAttributes.addFlashAttribute("app_id", applicationList);
			redirectAttributes.addFlashAttribute("sprintList", sprintList);
			redirectAttributes.addFlashAttribute("selectedSprint", sprintName);

			modelAndView = new ModelAndView("redirect:/app/" + appId + "/" + ApplicationConstants.JIRA_DEV + "/iterations/" + appId + ".html?sprintId=" + sprintId);
		} catch (Exception e) {
			logger.error("ERROR :: firstRequestHandler ::" + e.getMessage());
		}
		return modelAndView;
	}

	@RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JIRA_DEV_REDESIGN+"/iterations/*.html", method = RequestMethod.GET)
	 public String _getInitialisedIterationJspPage(@PathVariable String appId, @RequestParam("sprintId") String sprintId, Model model) {
		 Data dataMap = new Data();
		 Sprint sprint = new Sprint();
		 String jsonString = null;
		 int appIdFromDB = -1;
		 int sprintIdFromDB = -1;
		 String sprintName = "";
		 Application application = null;
		 try {
				System.out.println("NMNMNMNMNM Sprint Id in IterationsController "+appId+","+sprintId);
			 
			    List<Application> applicationList = iterationsChartService.getApplicationList();
				model.addAttribute("appList", applicationList);
				
				if((appId == null) && (sprintId == null)) {
					if(applicationList.size() > 0) {
						application = applicationList.get(0);
						appIdFromDB = application.getAppId();
//						model.addAttribute("requestedApp", application.getAppName());
					}
				}
				else {
					appIdFromDB = Integer.parseInt(appId);
					sprintIdFromDB = Integer.parseInt(sprintId);
					Sprint sprintDetails = iterationsChartService.getSprint(sprintIdFromDB);
					sprintName = sprintDetails.getSprintName();
					com.etaap.domain.Application applicationD = applicationService.getApplication(appIdFromDB,0,null);
					model.addAttribute("requestedApp", applicationD.getAppName());
				}
				
				List<Sprint> sprintList = iterationsChartService.getSprintList(appIdFromDB);
				model.addAttribute("sprintList", sprintList);
				
				if((appId == null) && (sprintId == null)) {
					if(sprintList.size() > 0) {
						sprint = sprintList.get(0);
						sprintIdFromDB = sprint.getSprintId();
						sprintName = sprint.getSprintName();
					}
				}
				System.out.println("NMNMNMNMNM selectedSprint in IterationsController "+sprintName);
				model.addAttribute("selectedSprint", sprintName);
				application = iterationsChartService.getApplication(appIdFromDB);
				model.addAttribute("requestedApp", application.getAppName());
				// Redesign changes
				List<ChartModel> chartsList = new ArrayList<ChartModel>();
				chartsList.add(new ChartModel("iterations_userStories", ChartType.STACKED_COLUMN, new Object[] {appIdFromDB, sprintIdFromDB}));
				 chartsList.add(new ChartModel("eTAAPHomeWaterTank", ChartType.WATER_TANK, 
							new Object[] {appId,sprintId}, new Object[] {appId,sprintId}));
				String user_stories_json = chartService.getCharts(servletContext, chartsList);
				
				
				/*UserStoriesPerSprint userStoriesPerSprintObj =
						UserStoriesPerSprint.getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(iterationsChartService, 
						appIdFromDB, sprintIdFromDB);
				
				dataMap.getData().put("iterationJson", userStoriesPerSprintObj);
*/				
				
				BurndownPerSprint burndownPerSprintObj =
						BurndownPerSprint.getBurndownChart(burndownChartService, appIdFromDB, sprintIdFromDB);
				
				dataMap.getData().put("burndownChartJson", burndownPerSprintObj);
				
				// fetch actual user stories and completed user stories for sprint
				/*Map<String, String> params = new HashMap<>();
				params.put("app_id", appId);
				params.put("sprint_id", sprintId);
				int actualUserStories = iterationsChartService.getUserStoriesCount(params);
				 
				params.put("app_id", appId);
				params.put("sprint_id", sprintId);
				params.put("status", "closed");
				int completedUserStories = iterationsChartService.getUserStoriesCount(params);
				
				sprint.setUserStoriesCountAll(actualUserStories);
				sprint.setUserStoriesCountCompleted(completedUserStories);
				
				dataMap.getData().put("Sprint", sprint);*/
				
				jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
				
				System.out.println("JSON STRING: "+jsonString);
				
				model.addAttribute("jsonString", jsonString);
				model.addAttribute("user_stories", user_stories_json);
							
		} catch (Exception e) {
			logger.error("ERROR :: " + e.getMessage());
		}
	return "iterations";
	}
	
	
	 @RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JIRA_DEV+"/iterations/*.html", method = RequestMethod.GET)
	 public String getInitialisedIterationJspPage(@PathVariable String appId, @RequestParam("sprintId") String sprintId, Model model) {
		 Data dataMap = new Data();
		 Sprint sprint = new Sprint();
		 String jsonString = null;
		 int appIdFromDB = -1;
		 int sprintIdFromDB = -1;
		 String sprintName = "";
		 Application application = null;
		 try {
				System.out.println("NMNMNMNMNM Sprint Id in IterationsController "+appId+","+sprintId);
			 
			    List<Application> applicationList = iterationsChartService.getApplicationList();
				model.addAttribute("appList", applicationList);
				
				if((appId == null) && (sprintId == null)) {
					if(applicationList.size() > 0) {
						application = applicationList.get(0);
						appIdFromDB = application.getAppId();
//						model.addAttribute("requestedApp", application.getAppName());
					}
				}
				else {
					appIdFromDB = Integer.parseInt(appId);
					sprintIdFromDB = Integer.parseInt(sprintId);
					Sprint sprintDetails = iterationsChartService.getSprint(sprintIdFromDB);
					sprintName = sprintDetails.getSprintName();
					com.etaap.domain.Application applicationD = applicationService.getApplication(appIdFromDB,0,null);
					model.addAttribute("requestedApp", applicationD.getAppName());
				}
				
				List<Sprint> sprintList = iterationsChartService.getSprintList(appIdFromDB);
				model.addAttribute("sprintList", sprintList);
				
				if((appId == null) && (sprintId == null)) {
					if(sprintList.size() > 0) {
						sprint = sprintList.get(0);
						sprintIdFromDB = sprint.getSprintId();
						sprintName = sprint.getSprintName();
					}
				}
				System.out.println("NMNMNMNMNM selectedSprint in IterationsController "+sprintName);
				model.addAttribute("selectedSprint", sprintName);
				application = iterationsChartService.getApplication(appIdFromDB);
				model.addAttribute("requestedApp", application.getAppName());
				//
				UserStoriesPerSprint userStoriesPerSprintObj =
						UserStoriesPerSprint.getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(iterationsChartService, 
						appIdFromDB, sprintIdFromDB);
				
				dataMap.getData().put("iterationJson", userStoriesPerSprintObj);
				
				
				BurndownPerSprint burndownPerSprintObj =
						BurndownPerSprint.getBurndownChart(burndownChartService, appIdFromDB, sprintIdFromDB);
				
				dataMap.getData().put("burndownChartJson", burndownPerSprintObj);
				
				// fetch actual user stories and completed user stories for sprint
				Map<String, String> params = new HashMap<>();
				params.put("app_id", appId);
				params.put("sprint_id", sprintId);
				int actualUserStories = iterationsChartService.getUserStoriesCount(params);
				 
				params.put("app_id", appId);
				params.put("sprint_id", sprintId);
				params.put("status", "closed");
				int completedUserStories = iterationsChartService.getUserStoriesCount(params);
				
				sprint.setUserStoriesCountAll(actualUserStories);
				sprint.setUserStoriesCountCompleted(completedUserStories);
				
				dataMap.getData().put("Sprint", sprint);
				
				jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
				
				System.out.println("JSON STRING: "+jsonString);
				
				model.addAttribute("jsonString", jsonString);
							
		} catch (Exception e) {
			logger.error("ERROR :: " + e.getMessage());
		}
	return "iterations";
	}

//	@RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JIRA_DEV+"/iterations/*.html", method = RequestMethod.POST)	
//	@ResponseBody
//	public String getJsonChartData(@PathVariable String appId,@RequestParam("sprintId") String sprintId, Model model) {
//		String jsonString = null;
//		Sprint sprint = new Sprint();
//		int actualUserStories = 0;
//		int completedUserStories = 0;
//		try {
//			System.out.println("NMNMNMNMNM Sprint Id in IterationsController "+appId+","+sprintId);
//			List<Sprint> sprintList = iterationsChartService.getSprintList(Integer.parseInt(appId));
//			model.addAttribute("sprintList", sprintList);
//			Data dataMap = new Data();
//			UserStoriesPerSprint
//			userStoriesPerSprintObj =
//					UserStoriesPerSprint.getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(iterationsChartService, 
//							Integer.parseInt(appId), Integer.parseInt(sprintId));
//			dataMap.getData().put("iterationJson", userStoriesPerSprintObj);
//			
//			
//			BurndownPerSprint burndownPerSprintObj =
//					BurndownPerSprint.getBurndownChart(burndownChartService, Integer.parseInt(appId), Integer.parseInt(sprintId));
//			
//			dataMap.getData().put("burndownChartJson", burndownPerSprintObj);
//			
//			
//			Map<String, String> params = new HashMap<>();
//			// fetch actual user stories and completed user stories for sprint
//			params.put("app_id", appId);
//			params.put("sprint_id", sprintId);
//			actualUserStories = iterationsChartService.getUserStoriesCount(params);
//			
//			params.put("app_id", appId);
//			params.put("sprint_id", sprintId);
//			params.put("status", "closed");
//		    completedUserStories = iterationsChartService.getUserStoriesCount(params);
//		    
//			sprint.setUserStoriesCountAll(actualUserStories);
//			sprint.setUserStoriesCountCompleted(completedUserStories);
//			 
//			dataMap.getData().put("Sprint", sprint);
//			
//			jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
//			model.addAttribute("jiten", "jiten2ewww");
//			System.out.println("JSON STRING: "+jsonString);
//		}
//		catch(Exception e){
//			logger.error("ERROR :: " + e.getMessage());
//		}
//		return jsonString;
//	}
	
	
	@RequestMapping(value = "/app/{appId}/"+ApplicationConstants.JIRA_DEV_REDESIGN+"/iterations/*.html", method = RequestMethod.POST)	
	@ResponseBody
	public String _getJsonChartData(@PathVariable String appId,@RequestParam("sprintId") String sprintId, Model model) {
		String jsonString = null;
		Sprint sprint = new Sprint();
		int actualUserStories = 0;
		int completedUserStories = 0;
		try {
			System.out.println("NMNMNMNMNM Sprint Id in IterationsController "+appId+","+sprintId);
			List<Sprint> sprintList = iterationsChartService.getSprintList(Integer.parseInt(appId));
			model.addAttribute("sprintList", sprintList);
			Data dataMap = new Data();
			
			
			
			// Redesign changes
			List<ChartModel> chartsList = new ArrayList<ChartModel>();
			chartsList.add(new ChartModel("iterations_userStories", ChartType.STACKED_COLUMN, new Object[] {appId,sprintId}));
			chartsList.add(new ChartModel("eTAAPHomeWaterTank", ChartType.WATER_TANK, 
						new Object[] {appId,sprintId}, new Object[] {appId,sprintId}));
			String user_stories_json = chartService.getCharts(servletContext, chartsList);
			Object obj = com.etaap.utils.gsonUtils.Gson.getGsonObject(user_stories_json, new Object().getClass());
			
			
			//UserStoriesPerSprint	userStoriesPerSprintObj =
					//UserStoriesPerSprint.getDatewiseCommitedCompletedInProgressUserStoriesPerSprint(iterationsChartService, 
							//Integer.parseInt(appId), Integer.parseInt(sprintId));
			dataMap.getData().put("iterationJson", obj);
			
			
			BurndownPerSprint burndownPerSprintObj =
					BurndownPerSprint.getBurndownChart(burndownChartService, Integer.parseInt(appId), Integer.parseInt(sprintId));
			
			dataMap.getData().put("burndownChartJson", burndownPerSprintObj);
			
			
			/*Map<String, String> params = new HashMap<>();
			// fetch actual user stories and completed user stories for sprint
			params.put("app_id", appId);
			params.put("sprint_id", sprintId);
			actualUserStories = iterationsChartService.getUserStoriesCount(params);
			
			params.put("app_id", appId);
			params.put("sprint_id", sprintId);
			params.put("status", "closed");
		    completedUserStories = iterationsChartService.getUserStoriesCount(params);
		    
			sprint.setUserStoriesCountAll(actualUserStories);
			sprint.setUserStoriesCountCompleted(completedUserStories);
			 
			dataMap.getData().put("Sprint", sprint);*/
			
			
			jsonString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
			
			System.out.println("JSON STRING: "+jsonString);
		}
		catch(Exception e){
			logger.error("ERROR :: " + e.getMessage());
		}
		return jsonString;
	}

}
