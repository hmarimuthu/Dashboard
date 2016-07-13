package com.etaap.scheduler.listeners.triggerListeners;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;

import com.etaap.scheduler.SchedulerConstants;

public class TriggerListenerImpl implements org.quartz.TriggerListener {

	String name;

	public TriggerListenerImpl(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void triggerComplete(Trigger arg0, JobExecutionContext arg1,
			CompletedExecutionInstruction arg2) {
		// TODO Auto-generated method stub

		try {
			System.out.println();
			// TODO Auto-generated method stub
			System.out.println("------ triggerComplete --------- ");
			JobDataMap jobDataMap = arg1.getJobDetail().getJobDataMap();
			System.out.println(arg1.getFireTime());
			System.out.println(arg1.getJobRunTime());
			if (jobDataMap != null && jobDataMap.size() > 0) {
				String className = null;
				String group_name = null;
				String job_type = jobDataMap
						.getString(SchedulerConstants.SCHEDULER_JOB_TYPE);
				String job_uniqueKey = (String) jobDataMap.get(
						SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY).toString();
				className = jobDataMap.get(
						SchedulerConstants.SCHEDULER_JOB_CLASS_NAME).toString();
				group_name = jobDataMap.get(
						SchedulerConstants.SCHEDULER_JOB_GROUP_NAME).toString();

				if (job_type != null
						&& (job_type
								.equals(SchedulerConstants.SCHEDULER_JOB_IMMEDIATE))
						|| job_type
								.equals(SchedulerConstants.SCHEDULER_JOB_SCHEDULE)) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
							job_uniqueKey);
					arg1.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
							job_uniqueKey);
					params.put(
							SchedulerConstants.SCHEDULER_JOB_TRIGGER_FIRETIME,
							arg1.getFireTime());
					arg1.put(SchedulerConstants.SCHEDULER_JOB_TRIGGER_FIRETIME,
							arg1.getFireTime());
					
					
					params.put(
							SchedulerConstants.SCHEDULER_JOB_TRIGGER_STATUS,
							SchedulerConstants.SCHEDULER_JOB_TRIGGER_STATUS_SUCCESS);
					params.put(SchedulerConstants.SCHEDULER_JOB_MAP_ID, "0");

					Object classObj = Class.forName(className).newInstance();

					Method[] method = classObj.getClass().getDeclaredMethods();
					for (Method method1 : method) {
						if (method1
								.getName()
								.equals(SchedulerConstants.SCHEDULER_JOB_IMMEDIATE_INVOKE_CLASS_METHOD)) {
							Object responseInvoked = method1.invoke(classObj,
									params);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void triggerFired(Trigger arg0, JobExecutionContext arg1) {
		JobDataMap jobDataMap = arg1.getJobDetail().getJobDataMap();
		if (jobDataMap != null && jobDataMap.size() > 0) {
			try {

				String className = null;
				String group_name = null;
				String job_type = jobDataMap
						.getString(SchedulerConstants.SCHEDULER_JOB_TYPE);
				String job_uniqueKey = (String) jobDataMap.get(
						SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY).toString();
				className = jobDataMap.get(
						SchedulerConstants.SCHEDULER_JOB_CLASS_NAME).toString();
				group_name = jobDataMap.get(
						SchedulerConstants.SCHEDULER_JOB_GROUP_NAME).toString();

				if (job_type != null
						&& (job_type
								.equals(SchedulerConstants.SCHEDULER_JOB_IMMEDIATE))
						|| job_type
								.equals(SchedulerConstants.SCHEDULER_JOB_SCHEDULE)) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
							job_uniqueKey);
					params.put(SchedulerConstants.SCHEDULER_JOB_TRIGGER_FIRETIME,
							arg1.getFireTime());
					
					arg1.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
							job_uniqueKey);
					arg1.put(SchedulerConstants.SCHEDULER_JOB_TRIGGER_FIRETIME,
							arg1.getFireTime());
					
					params.put(
							SchedulerConstants.SCHEDULER_JOB_TRIGGER_STATUS,
							SchedulerConstants.SCHEDULER_JOB_TRIGGER_STATUS_RUNNING);
					params.put(SchedulerConstants.SCHEDULER_JOB_MAP_ID, "0");

					Object classObj = Class.forName(className).newInstance();

					Method[] method = classObj.getClass().getDeclaredMethods();
					for (Method method1 : method) {
						if (method1
								.getName()
								.equals(SchedulerConstants.SCHEDULER_JOB_IMMEDIATE_INVOKE_CLASS_METHOD)) {
							Object responseInvoked = method1.invoke(classObj,
									params);
							break;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void triggerMisfired(Trigger arg0) {
		// TODO Auto-generated method stub

	}

	public boolean vetoJobExecution(Trigger arg0, JobExecutionContext arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
