/**
 * @filename UserReplyController.java
 */
package com.maogousoft.wuliu.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;

/**
 * @description 司机评价
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 17, 2013 4:06:10 PM
 */
public class DriverReplyController  extends BaseController{

	public void index(){
		int driverId = getParaToInt();
		setAttr("driver_id", driverId);
		render("driver_reply.ftl");
	}
	
	/**
	 * 
	 * @description 查看评价 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:27:37 PM
	 */
	public void query(){
		int driverId = getParaToInt();
		String select = "SELECT a.* , b.name ";
		String from = " from logistics_driver_reply a left join logistics_user b on a.user_id = b.id where a.driver_id = " + driverId + " and a.status!=-1 order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select, from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page, "id|user_id|driver_id|name|score1|score2|score3|reply_content|reply_time"));
	}
	
	/**
	 * 
	 * @description 删除 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 4:24:18 PM
	 */
	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_driver_reply set status = -1 where id = ? " , id);
	}
	
	/**
	 * 
	 * @description 查询订单评价，页面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 5:19:24 PM
	 */
	public void orderReply(){
		setAttr("orderId", getParaToInt());
		render("order_driver_reply.ftl");
	}
	
	/**
	 * 
	 * @description 查询订单评价 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:27:37 PM
	 */
	public void queryOrderReply(){
		int orderId = getParaToInt();
		String select = "SELECT a.* , b.name ";
		String from = " from logistics_driver_reply a left join logistics_user b on a.user_id = b.id where a.order_id = " + orderId + " and a.status!=-1 order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select, from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page, "id|user_id|driver_id|name|score1|score2|score3|reply_content|reply_time"));
	}
}
