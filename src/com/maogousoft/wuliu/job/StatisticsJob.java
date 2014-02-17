/**
 * @filename StatisticsJob.java
 */
package com.maogousoft.wuliu.job;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.maogousoft.wuliu.service.DriverStatisticsService;
import com.maogousoft.wuliu.service.UserStatisticsService;
import com.maogousoft.wuliu.service.VenderStatisticsService;

/**
 * @description 统计相关任务
 * @author shevliu
 * @email shevliu@gmail.com
 * May 3, 2013 7:46:35 PM
 */
public class StatisticsJob implements Job{

private static final Logger log = Logger.getLogger(StatisticsJob.class);
	
	public void execute(JobExecutionContext arg) throws JobExecutionException {
		log.info("定时器执行：日期-->" + DateTime.now());
		//货主统计
		UserStatisticsService userStatisticsService = new UserStatisticsService();
		userStatisticsService.userStatistics();
		//司机统计
		DriverStatisticsService driverStatisticsService = new DriverStatisticsService();
		driverStatisticsService.driverStatistics();
		//商家统计
		VenderStatisticsService venderStatisticsService = new VenderStatisticsService();
		venderStatisticsService.venderStatistics();
	}
}
