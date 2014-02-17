package com.maogousoft.wuliu.job;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.maogousoft.wuliu.service.OrderAdminService;

/**
 * @author yangfan(kenny0x00@gmail.com) 2013-5-26 下午11:01:40
 */
public class OrderJob implements Job{

private static final Logger log = Logger.getLogger(OrderJob.class);

	public void execute(JobExecutionContext arg) throws JobExecutionException {
		log.info("订单定时器执行：日期-->" + DateTime.now());
		OrderAdminService orderService = new OrderAdminService();

		log.info("开始处理过期订单.");
		//更新已过期的货单状态
		orderService.processExpiredOrders();
		log.info("过期订单已处理完毕.");

		log.info("开始处理未评价订单.");
		orderService.processUncommentOrder();
		log.info("未评价订单已处理完毕.");
	}
}
