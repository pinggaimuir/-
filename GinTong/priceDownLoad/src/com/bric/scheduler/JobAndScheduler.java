package com.bric.scheduler;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.bric.crawler.Beginner;


public class JobAndScheduler {
//	private String CRON_DLCC   = "0 22 16 * * ?";
	private String CRON_DLCC   = "0 40 16 * * ?";
	private String CRON_21FOOD = "0 30 17 * * ?";
	private String CRON_CZCE = "20 32 17 * * ?";
	private String CRON_MOFCOM = "0 00 20 1 * ?";
	private Logger logger = Logger.getLogger(JobAndScheduler.class);
	
	//一亩田价格行情
//	public static class JobSta implements Job{
//
//		@Override
//		public void execute(JobExecutionContext arg0)
//				throws JobExecutionException {
//			new YmtDataFetch().getAndAppendCurrentData();
//			System.out.println("第" + i++ +"次");
//		}
//	}
	
	//Mofcom 每月价格行情
	public static class JobMofcom implements Job{

		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {
			// TODO Auto-generated method stub
			new Beginner().fetchAndSaveMofcomData();
		}
		
	}
	
	//大连持仓量排名
	public static class JobDLCC implements Job{

		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {			
			new Beginner().fetchAndSaveDLCCData();
		}
	}
	
	//大连持仓量排名
	public static class JobCZCE implements Job{

		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {			
			new Beginner().fetchAndSaveCZCEData();
		}
	}
	
	//食品商务网价格行情
	public static class Job21food implements Job{

		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {			
			new Beginner().fetchAndSave21foodData();
		}
		
	}
	
	public void start() throws SchedulerException{
		try {
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
			System.out.println("JobDLCC is running,don't close this window...");
			JobDetail DLCCJobDetail = new JobDetail("DLCCJob", JobDLCC.class);
			CronTrigger triggerDLCC = new CronTrigger("crawl", "group1");
			triggerDLCC.setCronExpression(new CronExpression(
					CRON_DLCC));
			scheduler.scheduleJob(DLCCJobDetail, triggerDLCC);

			System.out.println("JobCZCE is running,don't close this window...");
			JobDetail CZCEJobDetail = new JobDetail("CZCEJob", JobCZCE.class);
			CronTrigger triggerCZCE = new CronTrigger("craw3", "group3");
			triggerCZCE.setCronExpression(new CronExpression(
					CRON_CZCE));
			scheduler.scheduleJob(CZCEJobDetail, triggerCZCE);
			
			System.out.println("Job21food is running,don't close this window...");
			JobDetail job21foodDetail = new JobDetail("Job21food", Job21food.class);
			CronTrigger trigger21food = new CronTrigger("crawl2", "group2");
			trigger21food.setCronExpression(new CronExpression(
					CRON_21FOOD));
			scheduler.scheduleJob(job21foodDetail, trigger21food);
			
			System.out.println("JobMofcom is running,don't close this window...");
			JobDetail jobMofcomDetail = new JobDetail("JobMofcom", JobMofcom.class);
			CronTrigger triggerMofcom = new CronTrigger("crawl4", "group4");
			triggerMofcom.setCronExpression(new CronExpression(
					CRON_MOFCOM));
			scheduler.scheduleJob(jobMofcomDetail, triggerMofcom);
			
			scheduler.start();
		} catch (ParseException e) {
			logger.error("ParseException:", e);
		}		
	}
}
