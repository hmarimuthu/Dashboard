package com.etaap.controller;

import java.util.ArrayList;
import java.util.LinkedList;
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
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.etaap.beans.ChartModel;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;
import com.etaap.services.ApplicationService;
import com.etaap.services.ChartService;
import com.etaap.services.TcmServiceImpl;
import com.etaap.utils.Utils;
import com.etaap.utils.enums.ChartType;

@Controller
public class TcmController {

	
	
	@Autowired
	ChartService chartService;
	
	
	private ServletContext servletContext;
	
	@Autowired
	TcmServiceImpl tcmServiceImpl;

	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	private List<TimePeriod> actualTimePeriodList = null;

	private static final Logger logger = Logger.getLogger(TcmController.class);

	@RequestMapping(value = "tcm")
	public ModelAndView showTcm(Model model, RedirectAttributes redirectAttributes){
		logger.info("Inside TcmController :: showTcm()");
		String appId = null;
		String from = null;
		String to = null;

		ModelAndView modelAndView = new ModelAndView("tcm");
		try {
			List<Application> applicationList = tcmServiceImpl.getTcmApplicationList();
			int monthId = applicationList.get(0).getMonthId();
			List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

			appId = String.valueOf(applicationList.get(0).getAppId());
			from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
			to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

			from = from.substring(0, 10);
			to = to.substring(0, 10);

			redirectAttributes.addFlashAttribute("app_id", applicationList);
			redirectAttributes.addFlashAttribute("periodList", getActualTimePeriodList);

			modelAndView = new ModelAndView("redirect:/app/" + appId + "/TCM/" + appId + ".html/?from=" + from + "&to=" + to);
		} catch(Exception e) {
			logger.error("ERROR :: showTcm() :: " + e.getMessage());
		}

		return modelAndView;
	}

	@RequestMapping(value = "/app/{appId}/TCM/*.html", method = RequestMethod.GET)
	public String displayTcmGet(@PathVariable String appId, @RequestParam("from") String fromParams, @RequestParam("to") String toParams, Model model) {
		logger.info("Inside TcmController :: displayTcmGet()");

		String app_Id = appId;
		String from = fromParams;
		String to = toParams;
	    List<Application> applicationList = (List<Application>) ((model.asMap() != null  && model.asMap().size() > 0 && model.asMap().get("app_id") != null) 

				  ? model.asMap().get("app_id") : tcmServiceImpl.getTcmApplicationList());
	    Application application = applicationService.getApplication(Integer.parseInt(appId),0,null);
	    
	    model.addAttribute("appList", applicationList);
	    model.addAttribute("requestedApp", application.getAppName());
		List<ChartModel> chartsList = new ArrayList<ChartModel>();
		
		chartsList.add(new ChartModel("eTAAPTcmCurrQuarter", ChartType.STACKED_COLUMN, 
				new Object[] {app_Id, from + " 00:00:00", to + " 23:59:59"}));
		
		String currQtrCharts = chartService.getCharts(servletContext, chartsList);
		model.addAttribute("curQuaterJsonString", currQtrCharts);
		
		chartsList.add(new ChartModel("eTAAPTcmPrevQuarter", ChartType.STACKED_COLUMN, 
				new Object[] {app_Id, to+" 00:00:00", from +" 23:59:59"}));

		String prevQtrCharts = chartService.getCharts(servletContext, chartsList);
		model.addAttribute("prevQuaterJsonString", prevQtrCharts);
		
		return "tcm";
	}
	
	@RequestMapping(value = "/app/{appId}/TCM/*.html", method = RequestMethod.POST)
	@ResponseBody
	public String displayTcmPost(@PathVariable String appId,@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("prevQuaterEndDate") String prevQuaterEndDate, Model model) {
		logger.info("Inside TcmController :: displayTcmPost()");

		List<ChartModel> chartsList = new ArrayList<ChartModel>();
		
		chartsList.add(new ChartModel("eTAAPTcmCurrQuarter", ChartType.STACKED_COLUMN, 
				new Object[] {appId, from + " 00:00:00", to + " 23:59:59"}));
		
		String curQuaterTcmGson = chartService.getCharts(servletContext, chartsList);
		
		chartsList.add(new ChartModel("eTAAPTcmPrevQuarter", ChartType.STACKED_COLUMN, 
				new Object[] {appId, to+" 00:00:00", from +" 23:59:59"}));

		String prevQuaterTcmGson = chartService.getCharts(servletContext, chartsList);

		System.out.println("curQuaterJsonString ^^^ prevQuaterJsonString --->  "+curQuaterTcmGson+"^^^"+prevQuaterTcmGson);
		
		return curQuaterTcmGson+"^^^"+prevQuaterTcmGson;
	}

	@RequestMapping(value = "/app/{appId}/TCM/*.json")
	@ResponseBody
	public String displayTcmJson(@PathVariable String appId,@RequestParam("from") String from, @RequestParam("to") String to, Model model) {
		logger.info("Inside TcmController :: displayTcmJson()");

		String curQuaterTcmGson = tcmServiceImpl.getTcmChartString(Integer.parseInt(appId), from, to);

		List<Application> applicationList = tcmServiceImpl.getTcmApplicationList();
		int monthId = applicationList.get(0).getMonthId();
		List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

		this.actualTimePeriodList = getActualTimePeriodList;

		String prevQuaterEndDate = getActualTimePeriodList.get(3).getStartDt();
		String prevQuaterTcmGson = tcmServiceImpl.getPrevQuaterChartString(Integer.parseInt(appId), to.split(" ")[0], prevQuaterEndDate.split(" ")[0], getActualTimePeriodList);

		if (curQuaterTcmGson == null || curQuaterTcmGson == "") {
			curQuaterTcmGson = "No Current Quater Graph";
		} else {
			curQuaterTcmGson = "Current Quater Graph : " + curQuaterTcmGson;
		}

		if (prevQuaterTcmGson == null || prevQuaterTcmGson == "") {
			prevQuaterTcmGson = "\r\n No Current to Previous Quater Graph";
		} else {
			prevQuaterTcmGson = "\r\n Current to Previous Quater Graph: "+prevQuaterTcmGson;  
		}

		return curQuaterTcmGson+prevQuaterTcmGson;
	}

}
