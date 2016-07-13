package com.etaap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.etaap.beans.Data;
import com.etaap.beans.Environments;
import com.etaap.beans.Priority;
import com.etaap.beans.Severity;
import com.etaap.beans.Status;
import com.etaap.common.ApplicationConstants;
import com.etaap.domain.Application;
import com.etaap.domain.Defects;
import com.etaap.domain.Environment;
import com.etaap.domain.TestBed;
import com.etaap.domain.TestSuite;
import com.etaap.domain.TimePeriod;
import com.etaap.services.ApplicationService;
import com.etaap.services.ChartService;
import com.etaap.services.DefectsService;
import com.etaap.services.EnvironmentService;
import com.etaap.utils.enums.ChartType;
import com.etaap.utils.enums.PriorityColor;
import com.etaap.utils.enums.StatusColor;
import com.etaap.utils.enums.SystemAPI;
import com.etaap.utils.enums.SeverityColor;
import com.etaap.utils.gsonUtils.Gson;

@Controller
public class JiraController /* extends AbstractController */ {

	@Autowired
	ChartService chartService;

	//@Autowired
	ServletContext servletContext;
	
	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	@Autowired
	@Qualifier("env")
	private EnvironmentService environmentService;

	@Autowired
	@Qualifier("defectsService")
	private DefectsService defectsService;

	

	private static final Logger logger = Logger.getLogger(JiraController.class);

	@RequestMapping("/defects-redesign")
	public ModelAndView _defectsCall(Model model, RedirectAttributes redirectAttributes) {
		logger.info("Inside JiraController :: defectsCall()");
		// code inside this method will get optimized by calling method of
		// AbstractController a.k.a Maincontroller and changing
		// /home to /ciResutls

		String appId = null;
		String from = null;
		String to = null;

		ModelAndView modelAndView = new ModelAndView("defects");
		try {
			List<Application> applicationList = this.applicationService
					.getApplicationListSpecificSystemApi(SystemAPI.JIRA.getSystemName());

			// int monthId = ((Application)
			// applicationService.getApplication(1,0,null)).getMonthId();

			if (applicationList != null) {
				Application app = applicationList.get(0);
				int monthId = ((Application) applicationService.getApplication(app.getAppId(), 0, null)).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(applicationList.get(0).getAppId());
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
			} else {
				int monthId = ((Application) applicationService.getApplication(1, 0, null)).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(applicationList.get(0).getAppId());
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
			}

			// mapId >> based on appId
			String map_id = applicationService.getMapIdByAppId(appId, SystemAPI.JIRA.getSystemName());
			redirectAttributes.addFlashAttribute("map_id", map_id);
			redirectAttributes.addFlashAttribute("app_id", applicationList);

			// modelAndView = new ModelAndView("redirect:/app/" + appId +
			// "/jira/" + map_id + ".html/?from=" + from + "&to=" + to);
			modelAndView = new ModelAndView("redirect:/app/" + appId + "/" + ApplicationConstants.JIRA_QA_REDESIGN + "/"
					+ map_id + ".html/?from=" + from + "&to=" + to);
		} catch (Exception e) {
			logger.error("ERROR :: defectsCall() :: " + e.getMessage());
		}

		return modelAndView;
	}
	
	
	@RequestMapping("/defects")
	public ModelAndView defectsCall(Model model, RedirectAttributes redirectAttributes) {
		logger.info("Inside JiraController :: defectsCall()");
		// code inside this method will get optimized by calling method of
		// AbstractController a.k.a Maincontroller and changing
		// /home to /ciResutls

		String appId = null;
		String from = null;
		String to = null;

		ModelAndView modelAndView = new ModelAndView("defects");
		try {
			List<Application> applicationList = this.applicationService
					.getApplicationListSpecificSystemApi(SystemAPI.JIRA.getSystemName());

			// int monthId = ((Application)
			// applicationService.getApplication(1,0,null)).getMonthId();

			if (applicationList != null) {
				Application app = applicationList.get(0);
				int monthId = ((Application) applicationService.getApplication(app.getAppId(), 0, null)).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(applicationList.get(0).getAppId());
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
			} else {
				int monthId = ((Application) applicationService.getApplication(1, 0, null)).getMonthId();
				List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

				appId = String.valueOf(applicationList.get(0).getAppId());
				from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
				to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

				from = from.substring(0, 10);
				to = to.substring(0, 10);
			}

			// mapId >> based on appId
			String map_id = applicationService.getMapIdByAppId(appId, SystemAPI.JIRA.getSystemName());
			redirectAttributes.addFlashAttribute("map_id", map_id);
			redirectAttributes.addFlashAttribute("app_id", applicationList);

			// modelAndView = new ModelAndView("redirect:/app/" + appId +
			// "/jira/" + map_id + ".html/?from=" + from + "&to=" + to);
			modelAndView = new ModelAndView("redirect:/app/" + appId + "/" + ApplicationConstants.JIRA_QA+ "/"
					+ map_id + ".html/?from=" + from + "&to=" + to);
		} catch (Exception e) {
			logger.error("ERROR :: defectsCall() :: " + e.getMessage());
		}

		return modelAndView;
	}

	@RequestMapping(value = "/app/{appId}/" + ApplicationConstants.JIRA_QA_REDESIGN
			+ "/*.html", method = RequestMethod.GET)
	public String _getDefectsChart(@PathVariable String appId, @RequestParam("from") String fromParams,
			@RequestParam("to") String toParams, Model model) {
		String responseString = null;

		String app_Id = appId;
		String from = fromParams;
		String to = toParams;
		String map_id = null;

		if (model.asMap() != null && model.asMap().size() > 0 && model.asMap().get("map_id") != null)
			map_id = (String) model.asMap().get("map_id");
		else
			map_id = getFromServletRequestAttribute();

		List<Application> applicationList = (List<Application>) ((model.asMap() != null && model.asMap().size() > 0
				&& model.asMap().get("app_id") != null) ? model.asMap().get("app_id")
						: applicationService.getApplicationListSpecificSystemApi(
								com.etaap.utils.enums.SystemAPI.JIRA.getSystemName()));

		List<Environment> environmentList = environmentService.getEnvironmentList(null);
		List<Environment> envClone = new ArrayList<Environment>(environmentList);
		
		List<Application> applicationMapDetails = applicationService.getApplicationMapDetails(Integer.parseInt(app_Id));

		HashMap<String, Object> filterOptions = new HashMap<String, Object>();

		for (Application app : applicationMapDetails) {
			ArrayList abc = (ArrayList) app.getEnvIdList();
			System.out.println("**&*&*&*Environment List for " + appId + " " + abc);
			for (Environment e : envClone) {
				if (!abc.contains(String.valueOf(e.getEnvId()))) {
					environmentList.remove(e);
					System.out.println("**&xxxxxx*&*&*Removed Environment " + e.getEnvId() + " from " + appId);
				}
			}
		}

		int monthId = ((Application) applicationService.getApplication(Integer.parseInt(app_Id), 0, null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		String envId = null;
		String suiteId = null;
		String bedId = null;

		List<Map<String, Object>> applicationSystemMap = applicationService.getApplicationSystemMapBasedOnMapId(map_id);
		if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
			envId = applicationSystemMap.get(0).get("env_id").toString();
		}


		// RE-Design changes
		List<ChartModel> chartsList = new ArrayList<ChartModel>();
		chartsList.add(new ChartModel("Defects_severity", ChartType.NORMAL_PIE, new Object[] { app_Id, from, to }));
		chartsList.add(new ChartModel("Defects_priority", ChartType.NORMAL_PIE, new Object[] { app_Id, from, to }));
		String jsonString = chartService.getCharts(servletContext, chartsList);

		HashMap<String, Object> csvForCIChart = /*
												 * mainController
												 * .getCsvForCIChart(ciDetails);
												 */null;

		model.addAttribute("appList", applicationList);

		String envjsonObj = _getDataString(app_Id, envId, suiteId, bedId, environmentList, null, null,
				null, null, model);

		model.addAttribute("jsonString", jsonString);
		model.addAttribute("envJsonString", envjsonObj);
		model.addAttribute("envList", environmentList);
		/*
		 * model.addAttribute("suiteList", testSuiteList);
		 * model.addAttribute("bedList", testBedList);
		 */
		model.addAttribute("periodList", getActualTimePeriodList);
		Application application = applicationService.getApplication(Integer.parseInt(appId), 0, null);
		model.addAttribute("requestedApp", application.getAppName());
		model.addAttribute("csvForCIChart", csvForCIChart);
		model.addAttribute("periodStrtDt", from + " 00:00:01");
		model.addAttribute("periodEndDt", to + " 23:59:59");
		model.addAttribute("to", to);
		model.addAttribute("systemAPI", SystemAPI.JIRA.getSystemName());
		// return "home";

		return responseString = "defects_redesign";

	}

	@RequestMapping(value = "/app/{appId}/" + ApplicationConstants.JIRA_QA_REDESIGN
			+ "/*.html", method = RequestMethod.POST)
	@ResponseBody
	public String __getDefectsChart(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("mapId") String mapId, Model model) {
		String jsonString = null;
		String map_Id = mapId;
		String env_Id = null;
		String app_Id = null;
		try {
			List<Map<String, Object>> applicationSystemMap = applicationService
					.getApplicationSystemMapBasedOnMapId(map_Id);
			if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
				env_Id = applicationSystemMap.get(0).get("env_id").toString();
				app_Id = applicationSystemMap.get(0).get("app_id").toString();
			}
			// RE-Design changes
			List<ChartModel> chartsList = new ArrayList<ChartModel>();
			chartsList.add(new ChartModel("Defects_severity_env", ChartType.NORMAL_PIE,
					new Object[] { app_Id, env_Id, from, to }));
			chartsList.add(new ChartModel("Defects_priority_env", ChartType.NORMAL_PIE,
					new Object[] { app_Id, env_Id, from, to }));
			jsonString = chartService.getCharts(servletContext, chartsList);
			jsonString = chartService.getCharts(servletContext, chartsList);
			
			
		} catch (Exception errors) {
			errors.printStackTrace();
		}
		return jsonString;
	}

	// @RequestMapping(value = "/app/{appId}/jira/*.html", method =
	// RequestMethod.POST)
	@RequestMapping(value = "/app/{appId}/" + ApplicationConstants.JIRA_QA + "/*.html", method = RequestMethod.POST)
	@ResponseBody
	public String getDefectsChart(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("mapId") String mapId, Model model) {
		String jsonString = null;
		String map_Id = mapId;
		String _from = from;
		String _to = to;
		String env_Id = null;
		String app_Id = null;
		try {
			List<Map<String, Object>> applicationSystemMap = applicationService
					.getApplicationSystemMapBasedOnMapId(map_Id);
			if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
				env_Id = applicationSystemMap.get(0).get("env_id").toString();
				app_Id = applicationSystemMap.get(0).get("app_id").toString();
			}
			HashMap<String, Object> filterOptions = new HashMap<String, Object>();
			filterOptions.put("appId", app_Id);
			filterOptions.put("envId", env_Id);
			filterOptions.put("periodStrtDt", _from);
			filterOptions.put("periodEndDt", _to);
			filterOptions.put("systemApi", SystemAPI.JIRA.getSystemName());
			filterOptions.put("defectCase", "severity");

			List<Defects> defectsSeverityDetails = defectsService.getDetails(filterOptions); // details
																								// from
																								// jira
																								// table
																								// for
																								// displaying
																								// pie-chart
			// for status ---
			filterOptions.put("defectCase", "status");
			List<Defects> defectsStatusDetails = defectsService.getDetails(filterOptions); // details
																							// from
																							// jira
																							// table
																							// for
																							// displaying
																							// pie-chart

			com.etaap.beans.Defects defects = new com.etaap.beans.Defects();
			List<Severity> defectsSeverityCaseList = new ArrayList<Severity>();
			List<Priority> defectsPriorityCaseList = new ArrayList<Priority>();
			List<Status> defectsStatusCaseList = new ArrayList<Status>();
			boolean priorityFlag = false;

			for (Defects def : defectsSeverityDetails) {
				if (def.getSeverity() != null) {
					Severity severity = new Severity();
					severity.setColor(SeverityColor.SEVERITY_NAME.getColorCode(def.getSeverity()));
					severity.setName(def.getSeverity());
					severity.setCount(def.getSeverityCount());
					defectsSeverityCaseList.add(severity);
				} else {
					Priority priority = new Priority();
					priority.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def.getPriority()));
					priority.setName(def.getPriority());
					priority.setCount(def.getPriorityCount());
					defectsPriorityCaseList.add(priority);
					priorityFlag = true;
				}
				// defectsSeverityCaseMap.put(def.getSeverity(),
				// def.getSeverityCount());
			}
			if (defectsSeverityCaseList.size() > 0)
				defects.getDefectCases().put("Severity", defectsSeverityCaseList);
			else if (defectsPriorityCaseList.size() > 0)
				defects.getDefectCases().put("Priority", defectsPriorityCaseList);

			for (Defects def : defectsStatusDetails) {
				Status status = new Status();
				status.setColor(StatusColor.STATUS_NAME.getColorCode(def.getStatus()));
				status.setCount(def.getStatusCount());
				status.setName(def.getStatus());
				defectsStatusCaseList.add(status);
				// defectsStatusCaseMap.put(def.getStatus(),
				// def.getStatusCount());
			}
			defects.getDefectCases().put("Status", defectsStatusCaseList);
			Data dataMap = new Data();
			dataMap.getData().put("Defects", defects);

			jsonString = Gson.getGsonString(dataMap);
		} catch (Exception errors) {
			errors.printStackTrace();
		}

		return jsonString;
	}

	// @RequestMapping(value = "/app/{appId}/jira/*.html", method =
	// RequestMethod.GET)
	@RequestMapping(value = "/app/{appId}/" + ApplicationConstants.JIRA_QA + "/*.html", method = RequestMethod.GET)
	public String getCSVDataHtmlGetData(@PathVariable String appId, @RequestParam("from") String fromParams,
			@RequestParam("to") String toParams, Model model) {
		String responseString = null;

		String app_Id = appId;
		String from = fromParams;
		String to = toParams;
		String map_id = null;

		MainController mainController = new MainController(); // needs to remove
																// this
																// afterwards

		if (model.asMap() != null && model.asMap().size() > 0 && model.asMap().get("map_id") != null)
			map_id = (String) model.asMap().get("map_id");
		else
			map_id = getFromServletRequestAttribute();

		List<Application> applicationList = (List<Application>) ((model.asMap() != null && model.asMap().size() > 0
				&& model.asMap().get("app_id") != null) ? model.asMap().get("app_id")
						: applicationService.getApplicationListSpecificSystemApi(
								com.etaap.utils.enums.SystemAPI.JIRA.getSystemName()));

		List<Environment> environmentList = environmentService.getEnvironmentList(null);
		List<Environment> envClone = new ArrayList<Environment>(environmentList);
		/*
		 * List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();
		 * List<TestSuite> suiteClone = new ArrayList<TestSuite>(testSuiteList);
		 * List<TestBed> testBedList = testBedService.getTestBedList();
		 * List<TestBed> bedClone = new ArrayList<TestBed>(testBedList);
		 */

		List<Application> applicationMapDetails = applicationService.getApplicationMapDetails(Integer.parseInt(app_Id));

		HashMap<String, Object> filterOptions = new HashMap<String, Object>();

		for (Application app : applicationMapDetails) {
			ArrayList abc = (ArrayList) app.getEnvIdList();
			System.out.println("**&*&*&*Environment List for " + appId + " " + abc);
			for (Environment e : envClone) {
				if (!abc.contains(String.valueOf(e.getEnvId()))) {
					environmentList.remove(e);
					System.out.println("**&xxxxxx*&*&*Removed Environment " + e.getEnvId() + " from " + appId);
				}
			}
			/*
			 * ArrayList a = (ArrayList) app.getSuiteIdList(); for (TestSuite ts
			 * : suiteClone) { if (!a.contains(String.valueOf(ts.getSuiteId())))
			 * testSuiteList.remove(ts); } ArrayList bc = (ArrayList)
			 * app.getBedIdList(); for (TestBed tb : bedClone) { if
			 * (!bc.contains(String.valueOf(tb.getBedId())))
			 * testBedList.remove(tb); }
			 */

			// todeletecomment filterOptions.put("envId",
			// String.valueOf(app.getEnvId()));
			/*
			 * filterOptions.put("suiteId", String.valueOf(app.getSuiteId()));
			 * filterOptions.put("bedId", String.valueOf(app.getBedId()));
			 */
		}

		int monthId = ((Application) applicationService.getApplication(Integer.parseInt(app_Id), 0, null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		String envId = null;
		String suiteId = null;
		String bedId = null;

		List<Map<String, Object>> applicationSystemMap = applicationService.getApplicationSystemMapBasedOnMapId(map_id);
		if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
			envId = applicationSystemMap.get(0).get("env_id").toString();

			System.out.println("envId****************************" + envId);
			/*
			 * suiteId = applicationSystemMap.get(0).get("suite_id").toString();
			 * bedId = applicationSystemMap.get(0).get("bed_id").toString();
			 */

			// todeletecomment filterOptions.put("envId", envId);
			/*
			 * filterOptions.put("suiteId", suiteId); filterOptions.put("bedId",
			 * bedId);
			 */

		}

		filterOptions.put("appId", app_Id);
		filterOptions.put("periodId", String.valueOf(getActualTimePeriodList.get(0).getPeriodId()));
		filterOptions.put("periodStrtDt", from);
		filterOptions.put("periodEndDt", to);
		filterOptions.put("systemApi", SystemAPI.JIRA.getSystemName());
		filterOptions.put("defectCase", "severity");

		List<Defects> defectsSeverityDetails = defectsService.getDetails(filterOptions); // details
																							// from
																							// jira
																							// table
																							// for
																							// displaying
																							// pie-chart
		// for status ---
		filterOptions.put("defectCase", "status");
		List<Defects> defectsStatusDetails = defectsService.getDetails(filterOptions); // details
																						// from
																						// jira
																						// table
																						// for
																						// displaying
																						// pie-chart

		HashMap<String, Object> csvForCIChart = /*
												 * mainController
												 * .getCsvForCIChart(ciDetails);
												 */null;

		model.addAttribute("appList", applicationList);

		String jsonObj = getDataString(app_Id, envId, suiteId, bedId, environmentList, null, null,
				defectsSeverityDetails, defectsStatusDetails, model);

		System.out.println("*&*&*&*&*&*NNNN Jira JSON objects " + jsonObj);
		model.addAttribute("jsonString", jsonObj);
		model.addAttribute("envList", environmentList);
		/*
		 * model.addAttribute("suiteList", testSuiteList);
		 * model.addAttribute("bedList", testBedList);
		 */
		model.addAttribute("periodList", getActualTimePeriodList);
		Application application = applicationService.getApplication(Integer.parseInt(appId), 0, null);
		model.addAttribute("requestedApp", application.getAppName());
		model.addAttribute("csvForCIChart", csvForCIChart);
		model.addAttribute("periodStrtDt", from + " 00:00:01");
		model.addAttribute("periodEndDt", to + " 23:59:59");
		model.addAttribute("to", to);
		model.addAttribute("systemAPI", SystemAPI.JIRA.getSystemName());
		// return "home";

		return responseString = "defects";

	}

	// @RequestMapping(value = "/app/{appId}/jira/*.json", method =
	// RequestMethod.GET)
	@RequestMapping(value = "/app/{appId}/" + ApplicationConstants.JIRA_QA + "/*.json", method = RequestMethod.GET)
	@ResponseBody
	public String getJsonString(@PathVariable String appId, @RequestParam("from") String fromParams,
			@RequestParam("to") String toParams, Model model) {
		String responseString = null;

		String app_Id = appId;
		String from = fromParams;
		String to = toParams;
		String map_id = null;

		MainController mainController = new MainController(); // needs to remove
																// this
																// afterwards

		if (model.asMap() != null && model.asMap().size() > 0 && model.asMap().get("map_id") != null)
			map_id = (String) model.asMap().get("map_id");
		else
			map_id = getFromServletRequestAttribute();

		List<Application> applicationList = (List<Application>) ((model.asMap() != null && model.asMap().size() > 0
				&& model.asMap().get("app_id") != null) ? model.asMap().get("app_id")
						: applicationService.getApplicationListSpecificSystemApi(
								com.etaap.utils.enums.SystemAPI.JIRA.getSystemName()));

		List<Environment> environmentList = environmentService.getEnvironmentList();
		List<Environment> envClone = new ArrayList<Environment>(environmentList);
		/*
		 * List<TestSuite> testSuiteList = testSuiteService.getTestSuiteList();
		 * List<TestSuite> suiteClone = new ArrayList<TestSuite>(testSuiteList);
		 * List<TestBed> testBedList = testBedService.getTestBedList();
		 * List<TestBed> bedClone = new ArrayList<TestBed>(testBedList);
		 */

		List<Application> applicationMapDetails = applicationService.getApplicationMapDetails(Integer.parseInt(app_Id));

		HashMap<String, Object> filterOptions = new HashMap<String, Object>();

		for (Application app : applicationMapDetails) {
			ArrayList abc = (ArrayList) app.getEnvIdList();
			for (Environment e : envClone) {
				if (!abc.contains(String.valueOf(e.getEnvId())))
					environmentList.remove(e);
			}
			/*
			 * ArrayList a = (ArrayList) app.getSuiteIdList(); for (TestSuite ts
			 * : suiteClone) { if (!a.contains(String.valueOf(ts.getSuiteId())))
			 * testSuiteList.remove(ts); } ArrayList bc = (ArrayList)
			 * app.getBedIdList(); for (TestBed tb : bedClone) { if
			 * (!bc.contains(String.valueOf(tb.getBedId())))
			 * testBedList.remove(tb); }
			 */

			filterOptions.put("envId", String.valueOf(app.getEnvId()));
			/*
			 * filterOptions.put("suiteId", String.valueOf(app.getSuiteId()));
			 * filterOptions.put("bedId", String.valueOf(app.getBedId()));
			 */
		}

		int monthId = ((Application) applicationService.getApplication(Integer.parseInt(app_Id), 0, null)).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		String envId = null;
		String suiteId = null;
		String bedId = null;

		List<Map<String, Object>> applicationSystemMap = applicationService.getApplicationSystemMapBasedOnMapId(map_id);
		if (applicationSystemMap != null && applicationSystemMap.size() > 0) {
			envId = applicationSystemMap.get(0).get("env_id").toString();
			/*
			 * suiteId = applicationSystemMap.get(0).get("suite_id").toString();
			 * bedId = applicationSystemMap.get(0).get("bed_id").toString();
			 */

			/*
			 * filterOptions.put("suiteId", suiteId); filterOptions.put("bedId",
			 * bedId);
			 */

		}

		filterOptions.put("appId", app_Id);
		filterOptions.put("periodId", String.valueOf(getActualTimePeriodList.get(0).getPeriodId()));
		filterOptions.put("periodStrtDt", from + " 00:00:01");
		filterOptions.put("periodEndDt", to + " 23:59:59");
		filterOptions.put("systemApi", SystemAPI.JIRA.getSystemName());
		filterOptions.put("defectCase", "severity");

		List<Defects> defectsSeverityDetails = defectsService.getDetails(filterOptions); // details
																							// from
																							// jira
																							// table
																							// for
																							// displaying
																							// pie-chart
		// for status ---
		filterOptions.put("defectCase", "status");
		List<Defects> defectsStatusDetails = defectsService.getDetails(filterOptions); // details
																						// from
																						// jira
																						// table
																						// for
																						// displaying
																						// pie-chart

		HashMap<String, Object> csvForCIChart = /*
												 * mainController
												 * .getCsvForCIChart(ciDetails);
												 */null;

		model.addAttribute("appList", applicationList);

		String jsonObj = getDataString(app_Id, envId, suiteId, bedId, environmentList, null, null,
				defectsSeverityDetails, defectsStatusDetails, model);
		// return "home";
		return responseString = jsonObj;
	}

	public String _getDataString(String app_Id, String envId, String suiteId, String bedId,
			List<Environment> environmentList, List<TestSuite> testSuiteList, List<TestBed> testBedList,
			List<Defects> defectsSeverityDetails, List<Defects> defectsStatusDetails, Model model) {
		String dataString = null;
		List<Map<String, Object>> applicationSystemMapServiceList = applicationService
				.getApplicationSystemMapService(app_Id, SystemAPI.JIRA.getSystemName());

		List<com.etaap.beans.Environment> environmentListObj = new ArrayList<com.etaap.beans.Environment>();
		Environments envs = new Environments();
		Data dataMap = new Data();
		/*
		 * Map paramsMap = new HashMap(); paramsMap.put("app_id",
		 * Integer.parseInt(app_Id)); paramsMap.put("env_id",
		 * Integer.parseInt(envId));
		 * 
		 */
		System.out.println("environmentListObj.size() :: " + environmentListObj.size());
		if (environmentListObj.size() == 0) {

			System.out.println("applicationSystemMapServiceList size :: " + applicationSystemMapServiceList.size());
			for (Iterator<Map<String, Object>> itr = applicationSystemMapServiceList.iterator(); itr.hasNext();) {
				Map row = itr.next();
				Map paramsMap = new HashMap();
				paramsMap.put("app_id", row.get("app_id"));
				paramsMap.put("env_id", row.get("env_id"));
				if (defectsService.isRecordAvail(paramsMap)) {
					// todeletecomment if (row.get("env_id")!=null &&
					// row.get("env_id").toString().equals(envId)){
					if (row.get("env_id") != null) {
						// environmentLabel :
						for (Environment envVar : environmentList) {
							if (envVar.getEnvId() == Integer.parseInt(row.get("env_id").toString())) {
								com.etaap.beans.Environment environment = new com.etaap.beans.Environment();
								environment.setMapId(row.get("map_id").toString());
								environment.setName(envVar.getEnvName());
								environmentListObj.add(environment);
								// break environmentLabel;
								itr.remove();
								break;
							}
						}
					}
				}
			}
		}

		/*
		 * if(environmentListObj.size() > 0){ for (Iterator<Map<String, Object>>
		 * itr = applicationSystemMapServiceList .iterator(); itr.hasNext();) {
		 * Map row = itr.next(); int env_idVal = Integer.parseInt(row
		 * .get("env_id").toString()); paramsMap.put("env_id", env_idVal);
		 * if(defectsService.isRecordAvail(paramsMap)){ for (Environment envVar
		 * : environmentList) { if (envVar.getEnvId() == env_idVal){
		 * com.etaap.beans.Environment environment = new
		 * com.etaap.beans.Environment();
		 * environment.setMapId(row.get("map_id").toString());
		 * environment.setName(envVar.getEnvName());
		 * environmentListObj.add(environment); break; } } } } }
		 */

		envs.setEnvironment(environmentListObj);
		dataMap.getData().put("Environments", envs);

		/*com.etaap.beans.Defects defects = new com.etaap.beans.Defects();
		List<Severity> defectsSeverityCaseList = new ArrayList<Severity>();
		List<Priority> defectsPriorityCaseList = new ArrayList<Priority>();
		List<Status> defectsStatusCaseList = new ArrayList<Status>();
		boolean priorityFlag = false;*/

		/*try {
			for (Defects def : defectsSeverityDetails) {
				if (def.getSeverity() != null) {
					Severity severity = new Severity();
					severity.setColor(SeverityColor.SEVERITY_NAME.getColorCode(def.getSeverity()));
					severity.setName(def.getSeverity());
					severity.setCount(def.getSeverityCount());
					defectsSeverityCaseList.add(severity);
				} else {
					Priority priority = new Priority();
					priority.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def.getPriority()));
					priority.setName(def.getPriority());
					priority.setCount(def.getPriorityCount());
					defectsPriorityCaseList.add(priority);
					priorityFlag = true;
				}
				// defectsSeverityCaseMap.put(def.getSeverity(),
				// def.getSeverityCount());
			}
		} catch (Exception e) {
			logger.error("ERROR :: getDataString() : for Severity : " + e.getMessage());
		}
		if (priorityFlag)
			defects.getDefectCases().put("Priority", defectsPriorityCaseList);
		else
			defects.getDefectCases().put("Severity", defectsSeverityCaseList);*/

		/*try {
			for (Defects def : defectsStatusDetails) {
				Status status = new Status();
				status.setColor(StatusColor.STATUS_NAME.getColorCode(def.getStatus()));
				status.setCount(def.getStatusCount());
				status.setName(def.getStatus());
				defectsStatusCaseList.add(status);
				// defectsStatusCaseMap.put(def.getStatus(),
				// def.getStatusCount());
			}
		} catch (Exception e) {
			logger.error("ERROR :: getDataString() : for Priority : " + e.getMessage());
		}*/
		//defects.getDefectCases().put("Status", defectsStatusCaseList);

		//dataMap.getData().put("Defects", defects);

		try {
			dataString = com.etaap.utils.gsonUtils.Gson.getGsonString(dataMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataString;
	}
	
	public String getDataString(String app_Id, String envId, String suiteId, String bedId,
			List<Environment> environmentList, List<TestSuite> testSuiteList, List<TestBed> testBedList,
			List<Defects> defectsSeverityDetails, List<Defects> defectsStatusDetails, Model model) {String dataString = null;
			List<Map<String, Object>> applicationSystemMapServiceList = applicationService
					.getApplicationSystemMapService(app_Id,SystemAPI.JIRA.getSystemName());
					
			List<com.etaap.beans.Environment> environmentListObj = new ArrayList<com.etaap.beans.Environment>();
			Environments envs = new Environments();
			Data dataMap = new Data();
	/*		Map paramsMap = new HashMap();
			paramsMap.put("app_id", Integer.parseInt(app_Id));
			paramsMap.put("env_id", Integer.parseInt(envId));
			
	*/		
			System.out.println("environmentListObj.size() :: "+environmentListObj.size());
			if(environmentListObj.size() == 0){
				
				System.out.println("applicationSystemMapServiceList size :: "+applicationSystemMapServiceList.size());
				for (Iterator<Map<String, Object>> itr = applicationSystemMapServiceList
						.iterator(); itr.hasNext();) {
					Map row = itr.next();
					Map paramsMap = new HashMap();
					paramsMap.put("app_id", row.get("app_id"));
					paramsMap.put("env_id", row.get("env_id"));
					if(defectsService.isRecordAvail(paramsMap)){
	//todeletecomment					if (row.get("env_id")!=null && row.get("env_id").toString().equals(envId)){
						if (row.get("env_id")!=null) {
							//environmentLabel :
							for (Environment envVar : environmentList) {
								if (envVar.getEnvId() == Integer.parseInt(row
										.get("env_id").toString())){
									com.etaap.beans.Environment environment = new com.etaap.beans.Environment();
									environment.setMapId(row.get("map_id").toString());
									environment.setName(envVar.getEnvName());
									environmentListObj.add(environment);
									//break environmentLabel;
									itr.remove();
									break;
								}
							}
						}
					}
				}
			}
			
	/*			if(environmentListObj.size() > 0){
					for (Iterator<Map<String, Object>> itr = applicationSystemMapServiceList
							.iterator(); itr.hasNext();) {
						Map row = itr.next();
						int env_idVal = Integer.parseInt(row
								.get("env_id").toString());
						paramsMap.put("env_id", env_idVal);
						if(defectsService.isRecordAvail(paramsMap)){
							for (Environment envVar : environmentList) {
								if (envVar.getEnvId() == env_idVal){
									com.etaap.beans.Environment environment = new com.etaap.beans.Environment();
									environment.setMapId(row.get("map_id").toString());
									environment.setName(envVar.getEnvName());
									environmentListObj.add(environment);
									break;
								}
							}
						}
					}
				}
	*/				
				
		
			envs.setEnvironment(environmentListObj);
			dataMap.getData().put("Environments", envs);
			
			com.etaap.beans.Defects defects = new com.etaap.beans.Defects();
			List<Severity> defectsSeverityCaseList = new ArrayList<Severity>();
			List<Priority> defectsPriorityCaseList = new ArrayList<Priority>();
			List<Status> defectsStatusCaseList = new ArrayList<Status>();
			boolean priorityFlag = false;

			try {
				for(Defects def : defectsSeverityDetails){
					if (def.getSeverity() != null) {
						Severity severity = new Severity();
						severity.setColor(SeverityColor.SEVERITY_NAME.getColorCode(def.getSeverity()));
						severity.setName(def.getSeverity());
						severity.setCount(def.getSeverityCount());
						defectsSeverityCaseList.add(severity);
					} else {
						Priority priority = new Priority();
						priority.setColor(PriorityColor.PRIORITY_NAME.getColorCode(def.getPriority()));
						priority.setName(def.getPriority());
						priority.setCount(def.getPriorityCount());
						defectsPriorityCaseList.add(priority);
						priorityFlag = true;
					}
					//defectsSeverityCaseMap.put(def.getSeverity(), def.getSeverityCount());
				}
			} catch(Exception e) {
				logger.error("ERROR :: getDataString() : for Severity : " + e.getMessage());
			}
			if (priorityFlag)
				defects.getDefectCases().put("Priority", defectsPriorityCaseList);
			else
				defects.getDefectCases().put("Severity", defectsSeverityCaseList);

			try {
				for(Defects def : defectsStatusDetails){
					Status status = new Status();
					status.setColor(StatusColor.STATUS_NAME.getColorCode(def.getStatus()));
					status.setCount(def.getStatusCount());
					status.setName(def.getStatus());
					defectsStatusCaseList.add(status);
					//defectsStatusCaseMap.put(def.getStatus(), def.getStatusCount());
				}
			} catch(Exception e) {
				logger.error("ERROR :: getDataString() : for Priority : " + e.getMessage());
			}
			defects.getDefectCases().put("Status", defectsStatusCaseList);
			
			dataMap.getData().put("Defects", defects);
			
			try {
				dataString = com.etaap.utils.gsonUtils.Gson
						.getGsonString(dataMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return dataString;
			}

	// needs to move to super class
	private String getFromServletRequestAttribute() {
		// TODO Auto-generated method stub
		String pageName = null;
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		try {
			String requestedURI = requestAttributes.getRequest().getRequestURI();
			String[] splittedVal = requestedURI.split("/");
			pageName = splittedVal[splittedVal.length - 1];
			pageName = pageName.split("\\.")[0];
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*
			 * logger.info("ERROR :: getFromServletRequestAttribute() " +
			 * e.getMessage());
			 */
		}
		return pageName;
	}

	/*
	 * @RequestMapping(value = "/jira") public String handleJiraRequest(Model
	 * model) throws IOException, JSONException { System.out.println(
	 * "Inside MainController :: handleJiraRequest");
	 * 
	 * List<Application> applicationList = applicationService.getUrlAliasList(3,
	 * "jira"); // List<Environment> environmentList = //
	 * environmentService.getAllEnvironmentList(0, 0); // List<TestSuite>
	 * testSuiteList = // testSuiteService.getAllTestSuiteList(0, 0); //
	 * List<TestBed> testBedList = testBedService.getAllTestBedList(0, 0);
	 * 
	 * JiraDataPullAPI pullData = new JiraDataPullAPI(); List<Defects>
	 * defectList = pullData.pullJiraData(applicationList);
	 * 
	 * defectsService.insertData(defectList);
	 * 
	 * return "create/create_home"; }
	 */
}