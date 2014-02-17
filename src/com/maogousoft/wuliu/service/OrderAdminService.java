package com.maogousoft.wuliu.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maogousoft.wuliu.domain.Business;
import com.maogousoft.wuliu.domain.Driver;
import com.maogousoft.wuliu.domain.DriverReply;
import com.maogousoft.wuliu.domain.GoldResult;
import com.maogousoft.wuliu.domain.Msg;
import com.maogousoft.wuliu.domain.Order;
import com.maogousoft.wuliu.domain.OrderLog;
import com.maogousoft.wuliu.domain.OrderVie;
import com.maogousoft.wuliu.domain.User;
import com.maogousoft.wuliu.domain.UserReply;

/**
 * @author yangfan(kenny0x00@gmail.com) 2013-5-26 下午11:04:35
 */
public class OrderAdminService {

	private static final Logger log = LoggerFactory.getLogger(OrderAdminService.class);

	/**
	 * 处理已过期订单状态
	 * @return
	 */
	public int processExpiredOrders() {
		log.debug("开始更新已过期订单状态...");
		List<Order> expiredOrders = Order.findExpiredOrders();
		int success = 0;
		for (Order order : expiredOrders) {
			try {
				processExpiredOrder(order);
				success++;
			}catch(Throwable t) {
				log.error("处理过期订单:" + order.getInt("id") + "出现异常:" + t.getMessage(),t);
			}
		}
		log.debug("完成更新已过期订单状态,共：" + expiredOrders.size() + "个订单,共成功" + success);
		return success;
	}

	private void processExpiredOrder(Order order) {
		final int order_id = order.getInt("id");
		final int user_id = order.getInt("user_id");
		final double user_bond = order.asDoubleValue("user_bond", 0);

		//获取已抢单司机列表
		final List<OrderVie> vieList = OrderVie.dao.getActiveVieListByOrder(order_id);

		//没有抢单，直接让订单过期失效 (如果到期没有审核,也回认为是没有抢单,自动失效)2013-06-06 23:24:03 by yangfan)
		if(vieList.isEmpty()) {
			order.set("status", Order.STATUS_EXPIRED);
			order.update();

			//解冻保证金与信息费
			User user = User.dao.loadUserById(user_id);
			GoldResult goldResult = user.adjustGold(user_bond);
			user.update();

			Business.dao.addUserBusiness(user_id, Business.BUSINESS_TYPE_DEPOSIT_RETURN, user_bond, goldResult.getBeforeGold(), goldResult.getAfterGold());
			Msg.dao.addUserMsg(Msg.TYPE_BUSINIESS, "订单保证金已返还", "订单" + order_id + "已过期，无人抢单，返还保证金：" + user_bond, user_id);
			Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单已过期", "订单已过期，无人抢单，订单编号：" + order_id, user_id);

			OrderLog.logOrder(order.getInt("id"), "系统", "无人抢单，订单已过期，系统将退还保证金与运输费用.");
		//有抢单，赔付给司机
		}else {
			int vieCount = vieList.size();
			final double paid = user_bond / vieCount;//每位司机赔付的金额
			for (OrderVie orderVie : vieList) {
				//将货主的赔付金额付给司机
				int driver_id = orderVie.getInt("driver_id");
				Driver driver = Driver.dao.loadDriverById(driver_id);
				GoldResult gr1 = driver.adjustGold(paid);

				//返回司机保证金
				final double driver_bond = orderVie.asDoubleValue("driver_bond", 0);
				GoldResult gr2 = driver.adjustGold(driver_bond);

				Business.dao.addDriverBusiness(driver_id, Business.BUSINESS_TYPE_VIOLATE_RETURN, driver_bond, gr1.getBeforeGold(), gr1.getAfterGold());
				Business.dao.addDriverBusiness(driver_id, Business.BUSINESS_TYPE_VIOLATE_PAID, paid, gr2.getBeforeGold(), gr2.getAfterGold());
				Msg.dao.addDriverMsg(Msg.TYPE_BUSINIESS, "保证金已返还", "货主过期未确认，保证金:" + driver_bond + "已返还,货单号：" + order_id, driver_id);
				Msg.dao.addDriverMsg(Msg.TYPE_BUSINIESS, "保证金奖励", "货主过期未确认，违约，奖励保证金:" + paid + ",货单号：" + order_id, driver_id);
				Msg.dao.addDriverMsg(Msg.TYPE_ORDER, "货单取消", "订单已经取消，订单编号：" + order_id, driver_id);
			}
			//订单过期
			order.set("status", Order.STATUS_EXPIRED);
			order.update();

			Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单已过期", "订单已过期，货主未确认，相关的保证金：" + user_bond + "赔付给了抢单司机，货单号：" + order_id, user_id);

			//记录
			OrderLog.logOrder(order.getInt("id"), "系统", "订单已过期且货主未确认，系统将保证金补偿给司机.");
		}
	}

	/**
	 * 处理快过期的订单，提醒货主
	 * @return
	 */
	public int processLastedOrders() {
		log.debug("开始处理即将过期订单状态...");
		//获取24小时内快过期的订单
		List<Order> lastedOrders = Order.findLastedOrders();
		int success = 0;
		for (Order order : lastedOrders) {
			try {
				int user_id = order.getInt("user_id");
				int order_id = order.getInt("id");
				Date validate_time = order.getTimestamp("validate_time");
				long dis = validate_time.getTime() - System.currentTimeMillis();
				if(OrderVie.dao.hasVieByOrderId(order_id)) {
					Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单即将过期", "订单即将过期，请尽快确认，货单号：" + order_id, user_id);
				}else {
					Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单即将过期", "订单即将过期，暂无人抢单，货单号：" + order_id, user_id);
				}
				success++;
			}catch(Throwable t) {
				log.error("处理即将过期订单:" + order.getInt("id") + "出现异常:" + t.getMessage(),t);
			}
		}
		log.debug("完成对即将过期订单的处理,共：" + lastedOrders.size() + "个订单,共成功" + success);
		return success;
	}

	public int processUncommentOrder() {
		log.debug("开始处理10天未评论订单状态...");
		List<Order> orders = Order.findUncommentOrders();
		int success = 0;
		for (Order order : orders) {
			try {
				final int order_id = order.getInt("id");
				final int user_id = order.getInt("user_id");
				final int driver_id = order.getInt("driver_id");

				if(order.getInt("user_reply") == 0) {//司机未评价(对货主)
					order.replyByDriver(5, 5, 5 , "默认好评");

					Msg.dao.addUserMsg(Msg.TYPE_ORDER, "货单已评价", "货单:" + order_id + "过期未评价，默认好评", user_id);
				}
				if(order.getInt("driver_reply") == 0) {//货主未评价(对司机)
					order.replyByUser(5, 5, 5 , "默认好评");

					Msg.dao.addDriverMsg(Msg.TYPE_ORDER, "货单已评价", "货单:" + order_id + "过期未评价，默认好评", driver_id);
				}

				//记录订单变更
				Driver driver = Driver.dao.loadDriverById(driver_id);
				OrderLog.logOrder(order.getInt("id"), "系统", "货单过期未评价，默认好评");
				success++;
			}catch(Throwable t) {
				log.error("处理未评论订单:" + order.getInt("id") + "出现异常:" + t.getMessage(),t);
			}
		}
		log.debug("完成对10天未评论订单的处理,共：" + orders.size() + "个订单,共成功" + success);
		return success;
	}
}
