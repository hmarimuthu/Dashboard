package com.etaap.scheduler.jobs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.etaap.api.JenkinsDataPullAPI;
import com.etaap.scheduler.SchedulerConstants;

public class Job implements Jobs {

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String className = dataMap.getString("className");
		String groupName = dataMap.getString("groupName");
		System.out.println(className + " - " + groupName);

		
		String jobId = (String)context.get(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY);
		String fireTime = ((Date)context.get(SchedulerConstants.SCHEDULER_JOB_TRIGGER_FIRETIME)).toString();
		
		try {
			Object classObj = Class.forName(className).newInstance();
			if(classObj instanceof JenkinsDataPullAPI) {
				JenkinsDataPullAPI jenkins = (JenkinsDataPullAPI)classObj;
				jenkins.setJobId(jobId);
				jenkins.setTriggerFireTime(fireTime);
			}
			Method method = classObj.getClass().getMethod("execute", null);
			method.invoke(classObj, null);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
