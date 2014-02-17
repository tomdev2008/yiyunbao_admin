/**
 * @filename Order.java
 */
package com.maogousoft.wuliu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.jfinal.plugin.activerecord.Model;
import com.maogousoft.wuliu.common.BusinessException;

/**
 * @description 订单
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 18, 2013 9:50:56 PM
 */
public class Order extends BaseModel<Order>{

	/**
	 * 已删除
	 */
	public static int STATUS_DELETED = -1 ;

	/**
	 * 已创建，待审核
	 */
	public static int STATUS_CREATED = 0 ;

	/**
	 * 审核通过，待抢单
	 */
	public static int STATUS_PASS = 1 ;

	/**
	 * 审核未通过
	 */
	public static int STATUS_REJECT = 2 ;

	/**
	 * 已中标，订单执行中
	 */
	public static int STATUS_DEAL = 3;

	/**
	 * 未达成条件，已正常取消
	 */
	public static int STATUS_CANCEL = 4;

	/**
	 * 订单已完成
	 */
	public static int STATUS_FINISH = 99;

	/**
	 * 订单已过期
	 */
	public static int STATUS_EXPIRED = 98 ;

	public static final Order dao = new Order();

	/**
	 *
	 * @description 获取除删除以外的所有状态list
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 18, 2013 10:02:06 PM
	 * @return
	 */
	public static List<Map<String , String>> getAllStatus(){
		List<Map<String , String>> list = new ArrayList<Map<String,String>>();
		list.add(createStatus(STATUS_CREATED, "已创建"));
		list.add(createStatus(STATUS_PASS, "审核通过"));
		list.add(createStatus(STATUS_REJECT, "审核未通过"));
		list.add(createStatus(STATUS_CANCEL, "已取消"));
		list.add(createStatus(STATUS_DEAL, "已中标，进行中"));
		list.add(createStatus(STATUS_FINISH, "订单已完成"));
		return list ;
	}

	private static Map<String , String> createStatus(int status , String text){
		Map<String , String> map = new HashMap<String , String>();
		map.put("status", status + "") ;
		map.put("text", text);
		return map ;
	}

	public Order loadOrder(int orderId) {
		Order order = findById(orderId);
		if(order == null) {
			throw new BusinessException("货单" + orderId + "不存在");
		}
		return order;
	}

	/**
	 * 判断订单是否在特定状态
	 * @param expectedStatus
	 * @return
	 */
	public boolean isStauts(int expectedStatus) {
		final int orderStatus = this.getInt("status");
		return orderStatus == expectedStatus;
	}

	/**
	 * 获取信息费，每笔为运费的3%，最高不超过200
	 * @return
	 */
	public double getFee() {
		double price = this.get("price");
		return Math.min(price * 0.03, 200);
	}

	/**
	 * 获取已过期的订单列表
	 * @return
	 */
	public static List<Order> findExpiredOrders() {
		String sql = "select * from logistics_order where validate_time<now() and (status=0 or status=1)";//仅处理审核通过或者未审核的订单 by yangfan 2013-06-06 23:22:26
		List<Order> orders = Order.dao.find(sql);
		return orders;
	}

	/**
	 * 获取即将过期的订单列表
	 * 5分钟扫描一次
	 * 3、	在货单有效时间的30分钟内，每5分钟推送消息---请改为，提前30分钟提醒一次，最后5分钟再醒一次即可。
	 * 5分钟扫描一次。
	 * 1. 如果还有25分钟至30分钟到期，就发信息。
	 * 2. 如果还有0分钟至5分钟到期，就发信息。
	 * @return
	 */
	public static List<Order> findLastedOrders() {
		Date time1 = DateTime.now().plusMinutes(25).toDate();
		Date time2 = DateTime.now().plusMinutes(25).toDate();
		Date time3 = DateTime.now().plusMinutes(0).toDate();
		Date time4 = DateTime.now().plusMinutes(5).toDate();
		String sql = "select * from logistics_order where ((validate_time>=? and validate_time<?) or (validate_time>=? and validate_time<?)) and status=1";
		List<Order> orders = Order.dao.find(sql, time1, time2, time3, time4);
		return orders;
	}

	/**
	 * 获取已完成,且未评价超过10天的订单列表
	 * @return
	 */
	public static List<Order> findUncommentOrders() {
		Date time = DateTime.now().plusDays(-10).toDate();
		String sql = "select * from logistics_order where finish_time<? and status=? and (user_reply = 0 or driver_reply = 0)";
		List<Order> orders = Order.dao.find(sql, time, Order.STATUS_FINISH);
		return orders;
	}

	/**
	 * 被司机评价
	 * @param score1
	 * @param score2
	 * @param score3
	 * @param reply_content
	 */
	public void replyByDriver(int score1, int score2, int score3, String reply_content) {
		final int order_id = this.getInt("id");
		final int user_id = this.getInt("user_id");
		final int driver_id = this.getInt("driver_id");

		UserReply reply = new UserReply();
		reply.put("order_id", order_id);
		reply.put("score1", score1);
		reply.put("score2", score2);
		reply.put("score3", score3);
		reply.put("reply_content", reply_content);
		reply.put("user_id", user_id);
		reply.put("driver_id", driver_id);
		reply.put("reply_time", new Date());
		reply.save();

		//更新评价情况
		this.set("user_reply", 1);//司机对货主评价完成
		this.update();
	}

	/**
	 * 被货主评价
	 * @param score1
	 * @param score2
	 * @param score3
	 * @param reply_content
	 */
	public void replyByUser(int score1, int score2, int score3, String reply_content) {
		final int order_id = this.getInt("id");
		final int user_id = this.getInt("user_id");
		final int driver_id = this.getInt("driver_id");

		DriverReply reply = new DriverReply();
		reply.put("order_id", order_id);
		reply.put("score1", score1);
		reply.put("score2", score2);
		reply.put("score3", score3);
		reply.put("reply_content", reply_content);
		reply.put("user_id", user_id);
		reply.put("driver_id", driver_id);
		reply.put("reply_time", new Date());
		reply.save();

		//更新评价情况
		this.set("driver_reply", 1);
		this.update();
	}
}
