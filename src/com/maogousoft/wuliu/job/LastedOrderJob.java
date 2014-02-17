package com.maogousoft.wuliu.job;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.maogousoft.wuliu.service.OrderAdminService;

/**
 * 处理即将到期的任务，通知用户
 * @author yangfan(kenny0x00@gmail.com) 2013-5-26 下午11:01:40
 */
public class LastedOrderJob implements Job{

private static final Logger log = Logger.getLogger(LastedOrderJob.class);

	public void execute(JobExecutionContext arg) throws JobExecutionException {
		log.info("订单定时器执行：日期-->" + DateTime.now());
		OrderAdminService orderService = new OrderAdminService();

		log.info("开始处理即将过期订单.");
		//3、	在货单有效时间的30分钟内，每5分钟推送消息---请改为，提前30分钟提醒一次，最后5分钟再醒一次即可。
		//处理快过期的货单状态
		orderService.processLastedOrders();
		log.info("即将过期订单已处理完毕.");
	}
}
