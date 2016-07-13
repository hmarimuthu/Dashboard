package com.etaap.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.etaap.common.ApplicationConstants;
import com.etaap.domain.Application;
import com.etaap.domain.TimePeriod;
import com.etaap.services.ApplicationService;
import com.etaap.utils.enums.SystemAPI;

@Controller
public class JenkinsController /*extends MainController*/ {

	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;

	private static final Logger logger = Logger.getLogger(JenkinsController.class);

	//Code changes required into this method if jenkins_dev is required to implement.
	@RequestMapping("/home")
	public ModelAndView handleHomeRequest(Model model, RedirectAttributes redirectAttributes) {
		logger.info("Inside JenkinsController :: handleHomeRequest()");

		String appId = null;
		String from = null;
		String to = null;

		ModelAndView modelAndView = new ModelAndView("home");
		try {
			List<Application> applicationList = applicationService.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
			/*if(applicationList.size() == 0) {
				throw new RuntimeException("Any application not found. Please create a Application!");
			}*/
			appId = String.valueOf(applicationList.get(0).getAppId());
			int appIdInt = Integer.parseInt(appId);
//			int monthId = ((Application) applicationService.getApplication(appIdInt)).getMonthId();

			int monthId = ((Application) applicationService.getApplication(appIdInt,0,null)).getMonthId();
			List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

			from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
			to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

			from = from.substring(0, 10);
			to = to.substring(0, 10);

			// mapId >> based on appId
			//commented to include env_id, suite_id, bed_id as request parameters
//			String map_id = applicationService.getMapIdByAppId(appId,SystemAPI.JENKINS.getSystemName());
			
			HashMap envIdSuiteIdBedIdMap = applicationService.getDefaultActiveEnvIdSuiteIdBedIdByAppId(appId,SystemAPI.JENKINS.getSystemName());			

			//commented to include env_id, suite_id, bed_id as request parameters
//			redirectAttributes.addFlashAttribute("map_id", map_id);
			
			String enviornmentId = "0";
			String suiteId = "0";
			String bedId = "0";
			
			if(envIdSuiteIdBedIdMap != null) {
				enviornmentId = String.valueOf(envIdSuiteIdBedIdMap.get("env_id"));
				suiteId = String.valueOf(envIdSuiteIdBedIdMap.get("suite_id"));
				bedId = String.valueOf(envIdSuiteIdBedIdMap.get("bed_id"));
				
				redirectAttributes.addFlashAttribute("env_id", enviornmentId);
				redirectAttributes.addFlashAttribute("suite_id", suiteId);
				redirectAttributes.addFlashAttribute("bed_id", bedId);
				
				redirectAttributes.addFlashAttribute("app_id", applicationList);
	
				//commented to include env_id, suite_id, bed_id as request parameters
				//modelAndView = new ModelAndView("redirect:/app/" + appId + "/jenkins/" + map_id + ".html/?from=" + from + "&to=" + to);
			}
			
/*			modelAndView = new ModelAndView("redirect:/app/" + appId + "/jenkins/" + enviornmentId+"_"+suiteId+"_"+bedId + ".html/?from=" + from + "&to=" + to+
					"&envId="+enviornmentId+"&suiteId="+suiteId+"&bedId="+bedId);
*/			modelAndView = new ModelAndView("redirect:/app/" + appId + "/"+ApplicationConstants.JENKINS_QA+"/" + enviornmentId+"_"+suiteId+"_"+bedId + ".html/?from=" + from + "&to=" + to+
					"&envId="+enviornmentId+"&suiteId="+suiteId+"&bedId="+bedId);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("ERROR :: handleHomeRequest() :: " + e.getMessage());
		}

		return modelAndView;
	}
	
	@RequestMapping("/home-redesign")
	public ModelAndView handleHomeReDesignRequest(Model model, RedirectAttributes redirectAttributes) {
		logger.info("Inside JenkinsController :: handleHomeRequest()");

		String appId = null;
		String from = null;
		String to = null;

		ModelAndView modelAndView = new ModelAndView("home_redesign");
		try {
			List<Application> applicationList = applicationService.getApplicationListSpecificSystemApi(com.etaap.utils.enums.SystemAPI.JENKINS.getSystemName());
			appId = String.valueOf(applicationList.get(0).getAppId());
			int appIdInt = Integer.parseInt(appId);

			int monthId = ((Application) applicationService.getApplication(appIdInt,0,null)).getMonthId();
			List<TimePeriod> getActualTimePeriodList = com.etaap.utils.Utils.getTimePeriod(monthId);

			from = String.valueOf(getActualTimePeriodList.get(0).getStartDt());
			to = String.valueOf(getActualTimePeriodList.get(0).getEndDt());

			from = from.substring(0, 10);
			to = to.substring(0, 10);
			
			HashMap envIdSuiteIdBedIdMap = applicationService.getDefaultActiveEnvIdSuiteIdBedIdByAppId(appId,SystemAPI.JENKINS.getSystemName());
			
			String enviornmentId = "0";
			String suiteId = "0";
			String bedId = "0";
			
			if(envIdSuiteIdBedIdMap != null) {
				enviornmentId = String.valueOf(envIdSuiteIdBedIdMap.get("env_id"));
				suiteId = String.valueOf(envIdSuiteIdBedIdMap.get("suite_id"));
				bedId = String.valueOf(envIdSuiteIdBedIdMap.get("bed_id"));
				
				redirectAttributes.addFlashAttribute("env_id", enviornmentId);
				redirectAttributes.addFlashAttribute("suite_id", suiteId);
				redirectAttributes.addFlashAttribute("bed_id", bedId);
				
				redirectAttributes.addFlashAttribute("app_id", applicationList);
			}
			modelAndView = new ModelAndView("redirect:/app/" + appId + "/" + ApplicationConstants.JENKINS_QA_REDESIGN 
					+ "/" + enviornmentId + "_" + suiteId + "_" + bedId + ".html/?from=" + from + "&to=" + to 
					+	"&envId="+enviornmentId + "&suiteId=" + suiteId + "&bedId=" + bedId);
		} catch (Exception e) {
			logger.error("ERROR :: handleHomeRequest() :: " + e.getMessage());
		}

		return modelAndView;
	}
}
