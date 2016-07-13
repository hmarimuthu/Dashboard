package com.etaap.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

import com.etaap.beans.ChartModel;
import com.etaap.beans.Data;
import com.etaap.beans.Velocity;
import com.etaap.domain.Application;
import com.etaap.domain.CI;
import com.etaap.domain.TimePeriod;
import com.etaap.services.ApplicationService;
import com.etaap.services.ChartService;
import com.etaap.services.VelocityServiceImpl;
import com.etaap.utils.Utils;
import com.etaap.utils.enums.ChartType;
import com.etaap.utils.gsonUtils.Gson;

@Controller
public class VelocityController {

	@Autowired
	VelocityServiceImpl velocityService;
	
	@Autowired
	ChartService chartService;
	
	//@Autowired
	ServletContext servletContext;
	
	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	private List<TimePeriod> actualTimePeriodList = null;

	private static final Logger logger = Logger.getLogger(VelocityController.class);

	@RequestMapping(value = "velocity")
	public ModelAndView showVelocity(Model model, RedirectAttributes redirectAttributes){
		logger.info("Inside VelocityController :: showVelocity()");
		String appId = null;
		String from = null;
		String to = null;

		ModelAndView modelAndView = new ModelAndView("velocity");
		try {
			List<Application> applicationList = velocityService.getVelocityApplicationList();
			int monthId = applicationList.get(0).getMonthId();
			List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

			appId = String.valueOf(applicationList.get(0).getAppId());
			from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
			to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

			from = from.substring(0, 10);
			to = to.substring(0, 10);

			redirectAttributes.addFlashAttribute("app_id", applicationList);
			redirectAttributes.addFlashAttribute("periodList", getActualTimePeriodList);
			
			modelAndView = new ModelAndView("redirect:/app/" + appId + "/velocity/" + appId + ".html/?from=" + from + "&to=" + to);
		} catch(Exception e) {
			logger.error("ERROR :: showVelocity() :: " + e.getMessage());
		}

		return modelAndView;
	}

	@RequestMapping(value = "/app/{appId}/velocity/*.html", method = RequestMethod.GET)
	public String displayVelocityGet(@PathVariable String appId, @RequestParam("from") String fromParams, @RequestParam("to") String toParams, Model model) {
		logger.info("Inside VelocityController :: displayVelocityGet()");

		String app_Id = appId;
		String from = fromParams;
		String to = toParams;
		try
		{
		List<Application> applicationList = (List<Application>) ((model.asMap() != null  && model.asMap().size() > 0 && model.asMap().get("app_id") != null) 
			  ? model.asMap().get("app_id") : velocityService.getVelocityApplicationList());

		Application application = applicationService.getApplication(Integer.parseInt(appId),0,null);
		int monthId = application.getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		this.actualTimePeriodList = getActualTimePeriodList;

		model.addAttribute("appList", applicationList);
		model.addAttribute("periodList", getActualTimePeriodList);
		model.addAttribute("requestedApp", application.getAppName());

		List<ChartModel> chartsList = new ArrayList<ChartModel>();
		
		chartsList.add(new ChartModel("eTAAPHomeVelocity", ChartType.COLUMN,
				Arrays.asList(new String[]{"Committed","Completed"}), new Object[] {app_Id, from + " 00:00:00", to + " 23:59:59"}));
		
		String charts = chartService.getCharts(servletContext, chartsList);
		model.addAttribute("curQuaterJsonString", charts);

		} 
		catch(Exception e){
			logger.error("ERROR :: " + e.getMessage());
		}
		return "velocity";
	}

	@RequestMapping(value = "/app/{appId}/velocity/*.html", method = RequestMethod.POST)
	@ResponseBody
	public String displayVelocityPost(@PathVariable String appId,@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("prevQuaterEndDate") String prevQuaterEndDate, Model model) {
		logger.info("Inside VelocityController :: displayVelocityPost()");
		System.out.println("Inside VelocityController :: displayVelocityPost()");
		String jsonString = null;
		Data dataMap = new Data();
		Velocity velocity = new Velocity();
		String charts = null;
		try
		{

		List<ChartModel> chartsList = new ArrayList<ChartModel>();
		chartsList.add(new ChartModel("eTAAPHomeVelocity", ChartType.COLUMN,
				Arrays.asList(new String[]{"Committed","Completed"}), new Object[] {appId, from + " 00:00:00", to + " 23:59:59"}));
		
		charts = chartService.getCharts(servletContext, chartsList);
		model.addAttribute("curQuaterJsonString", charts);
		}
		catch(Exception e){
			logger.error("ERROR :: " + e.getMessage());
		}
		return charts;
	}

	
	public HashMap<String, Object> getCsvForVelocityChart(List<Velocity> velocityDetails) {
		logger.info("Inside MainController :: getCsvForVelocityChart()");

		List<Integer> estimatedCSV = new ArrayList<Integer>();
		List<Integer> completedCSV = new ArrayList<Integer>();
		List<String> sprintNameCSV = new ArrayList<String>();
		
		for (Velocity velocity : velocityDetails) {
			estimatedCSV.add(Integer.valueOf(String.valueOf(velocity.getEstimated())));
			completedCSV.add(Integer.valueOf(String.valueOf(velocity.getCompleted())));
			sprintNameCSV.add(String.valueOf(velocity.getSprintName()));
		}

		logger.info("estimatedCSV :: " + estimatedCSV);
		logger.info("completedCSV :: " + completedCSV);
		logger.info("sprintNameCSV :: " + sprintNameCSV);
		
		HashMap<String, Object> csvMap = new HashMap<String, Object>();
		csvMap.put("estimatedCSV", estimatedCSV);
		csvMap.put("completedCSV", completedCSV);
		csvMap.put("sprintNameCSV", sprintNameCSV);
	
		logger.info("Inside MainController :: getCsvForVelocityChart() :: csvMap :: "
				+ csvMap);

		return csvMap;
	}
	
	@RequestMapping(value = "/app/{appId}/velocity/*.json")
	@ResponseBody
	public String displayVelocityJson(@PathVariable String appId,@RequestParam("from") String from, @RequestParam("to") String to, Model model) {
		logger.info("Inside VelocityController :: displayVelocityJson()");

		String curQuaterVelocityGson = velocityService.getVelocityChartString(Integer.parseInt(appId), from, to);

		List<Application> applicationList = velocityService.getVelocityApplicationList();
		int monthId = applicationList.get(0).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		this.actualTimePeriodList = getActualTimePeriodList;

		String prevQuaterEndDate = getActualTimePeriodList.get(3).getStartDt();

		if (curQuaterVelocityGson == null || curQuaterVelocityGson == "") {
			curQuaterVelocityGson = "No Current Quater Graph";
		} else {
			curQuaterVelocityGson = "Current Quater Graph : " + curQuaterVelocityGson;
		}

		return curQuaterVelocityGson;
	}
}
