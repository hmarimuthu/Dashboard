package com.etaap.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etaap.domain.Application;
import com.etaap.scheduler.beans.Job;
import com.etaap.scheduler.beans.SchedulerService;
import com.etaap.services.ApplicationService;
import com.etaap.services.SchedulerJobsService;
import com.etaap.services.SystemAPIService;
import com.etaap.utils.ClassFinder;
import com.etaap.utils.gsonUtils.Gson;
import com.google.gson.internal.LinkedTreeMap;
//import com.etouch.services.ScheduleService;
@Controller
public class SchedulerController {

	@Autowired
	public SchedulerJobsService schedulerJobsService;
	
	@Autowired
	public SystemAPIService systemApiService;
	
	@Autowired
	@Qualifier("app")
	public ApplicationService applicationService;
	
	
	public static final String SCHEDULER_TYPE = "Cron";
	public static final String SCHEDULER_API_LOCATION = "com.etaap.api";
	
	@RequestMapping("/scheduler")
	public String getSchedulerJobsFromDB(Model model){
		String viewName = "scheduler";
		try{
			Map paramsMap = new HashMap();
			paramsMap.put("jobs", "jobs");
			List<Map<String,Object>> noOfJobsList1 = schedulerJobsService._getScheduledJobs(paramsMap);
			List<Map<String,Object>>  noOfJobsList = getRecordsList(noOfJobsList1);
			List<Application> applicationList = applicationService.getApplicationList();
			
			List<String> intervalList = new ArrayList<String>();
			intervalList.add("6");
			intervalList.add("12");
			intervalList.add("24");
			Map<String,List<Map<String, String>>> schedulerJobsMap = null;
			String jsonString = null;
			if(noOfJobsList!=null && noOfJobsList.size()>0){
				schedulerJobsMap = new HashMap<String,List<Map<String, String>>>();
				
				List<Map<String, String>> schedulerJobsList = new ArrayList<Map<String, String>>();
				//String json = Gson.getGsonString(noOfJobsList);
				
				for(Map<String,Object> map : noOfJobsList){
					Long pk_jobId = Long.valueOf(map.get("pk_jobId").toString());
					String jobName = map.get("jobName").toString();
					String api_name = map.get("api_name").toString();;
					int job_interval = map.get("job_interval")!=null ? Integer.parseInt(map.get("job_interval").toString()) : 0;
					String jobStatus = map.get("job_status").toString();
					Long pk_recordId = map.get("pk_recordId")!=null ? Long.valueOf(map.get("pk_recordId").toString()) : null;
					Long fk_jobId = map.get("fk_jobId")!=null ? Long.valueOf(map.get("fk_jobId").toString()) : null;
					String executionDate = map.get("executionDateValue")!=null ? map.get("executionDateValue").toString() : null;
					String status = map.get("status")!=null ? map.get("status").toString() : null;
					String log = map.get("log")!=null ? map.get("log").toString() : null;
					
					Map<String, String> schedulerJobMap = new HashMap<String, String>(); 
					schedulerJobMap.put("pk_jobId", pk_jobId+"");
					schedulerJobMap.put("jobName", jobName);
					schedulerJobMap.put("api_name", api_name);
					schedulerJobMap.put("job_interval",job_interval+"");
					schedulerJobMap.put("jobStatus", jobStatus);
					schedulerJobMap.put("pk_recordId", pk_recordId+"");
					schedulerJobMap.put("fk_jobId",fk_jobId+"");
					schedulerJobMap.put("executionDate", executionDate);
					schedulerJobMap.put("status", status);
					schedulerJobMap.put("log", log);
					schedulerJobsList.add(schedulerJobMap);
				}
				schedulerJobsMap.put("SchedulerJobs", schedulerJobsList);
				jsonString = Gson.getGsonString(schedulerJobsMap);
				model.addAttribute("SchedulerJobsJsonString", jsonString);
				model.addAttribute("interval_List", intervalList);
				List<Map<String,Object>> systemApiList = systemApiService.getSystemApiList();
				model.addAttribute("jobs_List",getSystemApiNames(systemApiList));
				model.addAttribute("api_List",getSchedulerApiNames(SCHEDULER_API_LOCATION));
				model.addAttribute("app_List", applicationList.size());
			}else{
				schedulerJobsMap = new HashMap<String, List<Map<String,String>>>();
				schedulerJobsMap.put("SchedulerJobs", new ArrayList());
				jsonString = Gson.getGsonString(schedulerJobsMap);
				model.addAttribute("SchedulerJobsJsonString",jsonString);
				model.addAttribute("interval_List", intervalList);
				List<Map<String,Object>> systemApiList = systemApiService.getSystemApiList();
				model.addAttribute("jobs_List",getSystemApiNames(systemApiList));
				model.addAttribute("api_List",getSchedulerApiNames(SCHEDULER_API_LOCATION));
				model.addAttribute("app_List", applicationList.size());
			}
		}catch(Exception e){
			e.getMessage();
		}
		return viewName;
	}
	
	public List<String> getSystemApiNames(List<Map<String,Object>> systemApiObjectsList){
		List<String> systemApiList = null;
		try{
			if(systemApiObjectsList!=null && systemApiObjectsList.size()>0){
				systemApiList = new ArrayList<String>();
				for(Map<String, Object> listSystemAPI : systemApiObjectsList){
					systemApiList.add("'"+listSystemAPI.get("api_name").toString()+"'");
				}
			}
		}catch(Exception e){
			
		}
		return systemApiList;
	}
	
	private List getSchedulerApiNames(String schedulerApiLocation) {
		// TODO Auto-generated method stub
		List<String> apiNameList = new ArrayList<String>();
		List<Class<?>> classes = ClassFinder.find(schedulerApiLocation);
		for(Class<?> c : classes){
			apiNameList.add("'"+c.getSimpleName()+"'");
		}
		return apiNameList;
	}

	@ResponseBody
	@RequestMapping(value="/scheduleJobs",method = RequestMethod.POST)
	public  String scheduleJobs(Model model,@RequestParam("request") String request){
		
		String response = null;
		try{
			Object obj = Gson.getGsonObject(request,new Object().getClass());
			LinkedTreeMap linkedTreeMap = (LinkedTreeMap) obj;
			Map map = (Map) linkedTreeMap.get("ScheduledJobs");
			List<Map<String,String>> scheduledJobList = (List<Map<String,String>>) map.get("ScheduledJob");
			List<Job> jobsList = new ArrayList<Job>();
			Map<String,Object>  jobsMap = new HashMap<String,Object> ();
			SchedulerService scheduleServiceObj = new SchedulerService();
			for(Map<String,String> scheduleInfo : scheduledJobList){
				Long pk = (scheduleInfo.get("pk")!=null && !scheduleInfo.get("pk").toString().equalsIgnoreCase("") &&!scheduleInfo.get("pk").toString().equalsIgnoreCase("null")) ?
						   Long.valueOf(scheduleInfo.get("pk").toString()) : null;
						   int pk1 = 0;
				String jobName = scheduleInfo.get("jobName").toString(); 	
				String interval = scheduleInfo.get("interval").toString();
				String status = scheduleInfo.get("status").toString();
				String apiName = scheduleInfo.get("apiName").toString();
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("pk", pk);
				params.put("jobName", jobName);
				params.put("interval",interval);
				params.put("status",  status);
				params.put("apiName", apiName);
				
				if(pk == null)
					pk1 = schedulerJobsService.insertScheduledJobs(params);
				else
					pk1 = schedulerJobsService.updateScheduledJobs(params);
				
				Job job = new Job();
				job.setUnique_key(pk1+"");
				job.setName(apiName);
				job.setClassName(SCHEDULER_API_LOCATION+"."+apiName);
				job.setGroupName(jobName);
				job.setStatus(status);
				
				Map<String,Object> ScheduleTime = new HashMap<String,Object>(); 
				ScheduleTime.put("seconds","0");
				ScheduleTime.put("minute", interval);
				ScheduleTime.put("hour", "0");
				ScheduleTime.put("dayOfMonth", "*");
				ScheduleTime.put("month", "*");
				//ScheduleTime.put("dayOfWeek", "*");
				job.setScheduleTime(ScheduleTime);
				
				job.setCommand(null);
				jobsList.add(job);
				jobsMap.put("Job", jobsList);
			}
			scheduleServiceObj.setJobs(jobsMap);
			Map<String,Object> configurationMap = new HashMap<String,Object> ();
			configurationMap.put("Type", "Cron");
			scheduleServiceObj.setConfigurations(configurationMap);
			
			String jsonRequest = Gson.getGsonString(scheduleServiceObj);
			com.etaap.scheduler.services.ScheduleService scheduleService = new com.etaap.scheduler.services.ScheduleService();
			scheduleService.schedule(jsonRequest);
		}
		catch(Exception errors){
			errors.printStackTrace();
		}
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/runNow",method = RequestMethod.POST)
	public String runNow(Model model,@RequestParam("jobId") String jobId){
		String runNowResponseString = null;
		Object response = null;
		try{
			Map params = new HashMap();
			//params.put("pk_jobId", Integer.parseInt(jobId));
			List<Map<String,Object>> scheduledJobs = schedulerJobsService.getScheduledJobsOnPk(Integer.parseInt(jobId));
			Map<String,Object> scheduledMap = scheduledJobs.get(0);
			String jobName = scheduledMap.get("jobName").toString();
			String apiName = scheduledMap.get("api_name").toString();
			Map scheduleImmediateParamsMap = new HashMap();
			scheduleImmediateParamsMap.put("jobName", jobName);
			scheduleImmediateParamsMap.put("apiName", SCHEDULER_API_LOCATION+"."+apiName);
			scheduleImmediateParamsMap.put("unique_key",jobId);
			Map paramsMap = new HashMap();
			paramsMap.put("pk_jobId", jobId);
			int status = schedulerJobsService.getScheduledJobsStatusBasedOnPk(Integer.parseInt(jobId));
			if(/*list!=null && list.size()>0*/true){
				/*Map ab = list.get(0);
				String jobStatus = ab.get("job_status").toString();*/
				if(status == 1){
					com.etaap.scheduler.services.ScheduleService scheduleService = new com.etaap.scheduler.services.ScheduleService();
					response = scheduleService.scheduleImmediate(scheduleImmediateParamsMap);
				}else{
					response = "Please Enable the Job and Save";
				}
			}
		}catch(Exception e){
			e.getMessage();
		}
		return runNowResponseString=response.toString();
	}

	private List<Map<String, Object>> getRecordsList(List<Map<String, Object>> noOfJobsList) {
		Map paramsMap = new HashMap();
		paramsMap.put("records", "records");
		if (noOfJobsList != null && noOfJobsList.size() > 0) {
			int i = 0;
			for (Map<String, Object> map : noOfJobsList) {
				Map map1 = map;
				Long pk_jobId = Long.valueOf(map.get("pk_jobId").toString());
				paramsMap.put("jobId", pk_jobId);
				List<Map<String, Object>> listMap = schedulerJobsService._getScheduledJobs(paramsMap);
				if (listMap != null && listMap.size() > 0) {
					Map map2 = listMap.get(0);
					map1.put("pk_recordId", map2.get("pk_recordId") != null ? map2.get("pk_recordId").toString() : null);
					map1.put("fk_jobId", map2.get("fk_jobId") != null ? map2.get("fk_jobId").toString() : null);
					map1.put("executionDateValue", map2.get("executionDateValue") != null ? map2.get("executionDateValue").toString() : null);
					map1.put("status", map2.get("status") != null ? map2.get("status").toString() : null);
					map1.put("log", map2.get("log") != null ? map2.get("log").toString() : null);
					noOfJobsList.set(i, map1);
					i++;
				}
			}
		}

		return noOfJobsList;
	}
}
