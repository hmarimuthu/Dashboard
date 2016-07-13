package com.etaap.scheduler.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.etaap.scheduler.SchedulerConstants;
import com.etaap.scheduler.beans.Job;
import com.etaap.scheduler.listeners.jobListeners.JobListenerImpl;
import com.etaap.scheduler.listeners.schedulerListeners.SchedulerListenerImpl;
import com.etaap.scheduler.listeners.triggerListeners.TriggerListenerImpl;
import com.etaap.utils.gsonUtils.Gson;


public class ScheduleService extends AbstractService {

	// System.setProperty("org.terracotta.quartz.skipUpdateCheck",
	// Boolean.TRUE.toString());

	@Override
	public String schedule(String request) {
		String response = null;
		try {
			com.etaap.scheduler.beans.SchedulerService schedulerSeviceBeanObj = (com.etaap.scheduler.beans.SchedulerService) Gson
					.getGsonObject(request,
							com.etaap.scheduler.beans.SchedulerService.class);
			Map<String, Object> jobMap = schedulerSeviceBeanObj.getJobs();
			if (jobMap != null && jobMap.size() > 0) {
				List<Job> jobsList = (List<Job>) jobMap.get("Job");
				if (jobsList != null && jobsList.size() > 0) {
					Scheduler scheduler = new StdSchedulerFactory()
							.getScheduler();
					List<Scheduler> schedulerList = new ArrayList<Scheduler>();
					List<Scheduler> unschedulerList = new ArrayList<Scheduler>();
					for (int i = 0; i < jobsList.size(); i++) {
						Map dataMap = (Map) jobsList.get(i);
						String className = dataMap.get("className").toString();
						String groupName = dataMap.get("groupName").toString();
						String status = dataMap.get("status").toString();
						Map<String, Object> scheduleTime = (Map<String, Object>) dataMap
								.get("ScheduleTime");
						StringBuffer scheduleTimeStringBuffer = new StringBuffer(
								scheduleTime.get("seconds") + " ");
						scheduleTimeStringBuffer.append(scheduleTime
								.get("minute") + " ");
						scheduleTimeStringBuffer.append(scheduleTime
								.get("hour") + " ");
						scheduleTimeStringBuffer.append(scheduleTime
								.get("dayOfMonth") + " ");
						scheduleTimeStringBuffer.append(scheduleTime
								.get("month") + " ");
						// scheduleTimeStringBuffer.append(scheduleTime.get("dayOfWeek")
						// + " ");
						scheduleTimeStringBuffer.append("?");
						String timingString = scheduleTimeStringBuffer
								.toString();// "*/5 * * * * ?";
						if (isJobExists(className, groupName)) {
							if (status.equals("1")) {
								// simply update trigger
								TriggerKey triggerKey = new TriggerKey(
										className, groupName);
								Trigger oldTrigger = scheduler
										.getTrigger(triggerKey);
								// obtain a builder that would produce the
								// trigger
								TriggerBuilder tb = oldTrigger
										.getTriggerBuilder();
								TriggerBuilder tb1 = (TriggerBuilder) tb
										.withSchedule(CronScheduleBuilder
												.cronSchedule(timingString));
								Trigger newtrigger = tb1.build();
								scheduler.rescheduleJob(oldTrigger.getKey(),
										newtrigger);
							} else {
								// simply update trigger
								TriggerKey triggerKey = new TriggerKey(
										className, groupName);
								Trigger oldTrigger = scheduler
										.getTrigger(triggerKey);
								// obtain a builder that would produce the
								// trigger
								TriggerBuilder tb = oldTrigger
										.getTriggerBuilder();
								TriggerBuilder tb1 = tb
										.withSchedule(CronScheduleBuilder
												.cronSchedule(scheduleTimeStringBuffer
														.toString()));
								Trigger newtrigger = tb1.build();
								// scheduler.rescheduleJob(oldTrigger.getKey(),
								// newtrigger);
								scheduler.unscheduleJob(newtrigger.getKey());
								// unscheduled job
							}
						} else {
							if (status.equals("1")) {
								// create new scheduler & start
								JobBuilder j = JobBuilder
										.newJob(com.etaap.scheduler.jobs.Job.class);
								JobDataMap jobDataMap = new JobDataMap();
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_CLASS_NAME,
												className);
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_GROUP_NAME,
												groupName);
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_TYPE,
												SchedulerConstants.SCHEDULER_JOB_SCHEDULE);
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
												Long.valueOf(
														dataMap.get(
																"unique_key")
																.toString())
														.intValue());

								JobDetail jobDetail = j
										.withIdentity(className, groupName)
										.setJobData(jobDataMap).build();

								Trigger trigger = TriggerBuilder
										.newTrigger()
										.withIdentity(className, groupName)
										.withSchedule(
												CronScheduleBuilder
														.cronSchedule(scheduleTimeStringBuffer
																.toString()))
										.build();

								JobListenerImpl jobListerImpl = new JobListenerImpl(
										className);
								scheduler.getListenerManager().addJobListener(
										jobListerImpl);

								TriggerListenerImpl triggerListenerImpl = new TriggerListenerImpl(
										className);
								scheduler
										.getListenerManager()
										.addTriggerListener(triggerListenerImpl);

								scheduler.scheduleJob(jobDetail, trigger);
								schedulerList.add(scheduler);
							} else {
								// create new Scheduler and unschedule it
								JobBuilder j = JobBuilder
										.newJob(com.etaap.scheduler.jobs.Job.class);
								JobDataMap jobDataMap = new JobDataMap();
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_CLASS_NAME,
												className);
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_GROUP_NAME,
												groupName);
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_TYPE,
												SchedulerConstants.SCHEDULER_JOB_SCHEDULE);
								jobDataMap
										.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
												Long.valueOf(
														dataMap.get(
																"unique_key")
																.toString())
														.intValue());

								JobDetail jobDetail = j
										.withIdentity(className, groupName)
										.setJobData(jobDataMap).build();

								Trigger trigger = TriggerBuilder
										.newTrigger()
										.withIdentity(className, groupName)
										.withSchedule(
												CronScheduleBuilder
														.cronSchedule(scheduleTimeStringBuffer
																.toString()))
										.build();

								JobListenerImpl jobListerImpl = new JobListenerImpl(
										className);
								scheduler.getListenerManager().addJobListener(
										jobListerImpl);

								TriggerListenerImpl triggerListenerImpl = new TriggerListenerImpl(
										className);
								scheduler
										.getListenerManager()
										.addTriggerListener(triggerListenerImpl);

								// trigger.getKey();

								// scheduler.scheduleJob(jobDetail, trigger);
								scheduler.unscheduleJob(trigger.getKey());

								unschedulerList.add(scheduler);

								// unschedulerList
							}
						}
					}
					if (schedulerList != null && schedulerList.size() > 0) {
						for (Scheduler scheduler1 : schedulerList) {
							scheduler1.start();
						}
					}
					if (unschedulerList != null && unschedulerList.size() > 0) {
						for (Scheduler scheduler1 : schedulerList) {
							// scheduler1.unscheduleJob(scheduler1.getJ)
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	public boolean isJobExists(String className, String groupName) {
		boolean isJobExists = false;
		System.out.println("------- isJobExists ---------- jobName = "
				+ className + " --- groupName = " + groupName);
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			if (scheduler.getJobGroupNames() != null
					&& scheduler.getJobGroupNames().size() > 0) {
				for (String groupNameValue : scheduler.getJobGroupNames()) {
					for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher
							.jobGroupEquals(groupNameValue))) {
						String jobName = jobKey.getName();
						String jobGroup = jobKey.getGroup();
						if (jobName.equals(className)
								&& jobGroup.equals(groupName)) {
							isJobExists = true;
							break;
						}

					}
				}
			}

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isJobExists;
	}

	public void listAllJobs() {
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();

			for (String groupName : scheduler.getJobGroupNames()) {

				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher
						.jobGroupEquals(groupName))) {

					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();

					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) scheduler
							.getTriggersOfJob(jobKey);
					Date nextFireTime = triggers.get(0).getNextFireTime();

					System.out.println("[jobName] : " + jobName
							+ " [groupName] : " + jobGroup + " - "
							+ nextFireTime);

				}

			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Object unSchedule(Object request) {
		// TODO Auto-generated method stub
		String triggerName = "test.ClassToRunAsApi";
		String triggerGroup = "group1";

		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
			boolean isUnscheduledSuccess = scheduler.unscheduleJob(triggerKey);
			System.out.println("unSchedule ---- " + isUnscheduledSuccess);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object scheduleImmediate(Object request) {
		System.out.println("under ---- scheduleImmediate ---- ");
		Scheduler scheduler;
		String responseString = null;
		try {
			if (request != null) {
				if (request instanceof java.util.Map) {
					Map requestMap = (Map) request;
					if (requestMap != null && requestMap.size() > 0) {
						String className = requestMap.get(
								SchedulerConstants.SCHEDULER_JOB_API_NAME)
								.toString();
						String groupName = requestMap.get(
								SchedulerConstants.SCHEDULER_JOB_JOB_NAME)
								.toString();
						String unique_key = requestMap.get(
								SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY)
								.toString();
						if (!isJobRunning(className, groupName)) {
							scheduler = new StdSchedulerFactory()
									.getScheduler();
							scheduler.getListenerManager()
									.addSchedulerListener(
											new SchedulerListenerImpl());
							scheduler.getListenerManager().addJobListener(
									new JobListenerImpl(className));
							scheduler.getListenerManager().addTriggerListener(
									new TriggerListenerImpl(className));
							JobKey jobKey = JobKey.jobKey(groupName, className);
							JobDetail job = JobBuilder
									.newJob(com.etaap.scheduler.jobs.Job.class)
									.withIdentity(jobKey).storeDurably()
									.build();
							job.getJobDataMap()
									.put(SchedulerConstants.SCHEDULER_JOB_CLASS_NAME,
											className);
							job.getJobDataMap()
									.put(SchedulerConstants.SCHEDULER_JOB_GROUP_NAME,
											groupName);
							job.getJobDataMap().put(
									SchedulerConstants.SCHEDULER_JOB_TYPE,
									SchedulerConstants.SCHEDULER_JOB_IMMEDIATE);
							job.getJobDataMap()
									.put(SchedulerConstants.SCHEDULER_JOB_UNIQUE_KEY,
											unique_key);
							scheduler.addJob(job, true);
							scheduler.triggerJob(jobKey);
							scheduler.start();
							responseString = SchedulerConstants.SCHEDULER_JOB_STARTED_RUNNING;
						} else {
							responseString = SchedulerConstants.SCHEDULER_JOB_ALREADY_RUNNING;
						}
					}
				}
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}

	private boolean isJobRunning(String className, String groupName) {
		// TODO Auto-generated method stub
		boolean alreadyRunning = false;
		try {
			if (className != null && groupName != null) {
				Scheduler scheduler = new StdSchedulerFactory().getScheduler();
				List<JobExecutionContext> currentJobs = scheduler
						.getCurrentlyExecutingJobs();
				if (currentJobs != null && currentJobs.size() > 0) {
					for (JobExecutionContext jobCtx : currentJobs) {
						JobKey jobKey = jobCtx.getJobDetail().getKey();
						String jobName = jobKey.getName();
						String apiName = jobKey.getGroup();

						// System.out.println("class Name :- " jobName +
						// "grop");
						if (jobName.equals(groupName)
								&& apiName.equals(className)) {
							alreadyRunning = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return alreadyRunning;
	}
}
