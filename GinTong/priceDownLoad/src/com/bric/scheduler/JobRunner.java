package com.bric.scheduler;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

public class JobRunner {//从这里启动整个任务
	private static Logger logger = Logger.getLogger(JobRunner.class);
	public static void main(String[] a){
		try {
			new JobAndScheduler().start();
		} catch (SchedulerException e) {
			logger.error("Error While Starting Scheduler:", e);
		}
	}
}
