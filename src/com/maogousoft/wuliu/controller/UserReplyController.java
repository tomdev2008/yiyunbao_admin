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
 * @description 货主评价
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 17, 2013 4:06:10 PM
 */
public class UserReplyController  extends BaseController{

	public void index(){
		int userId = getParaToInt();
		setAttr("user_id", userId);
		render("user_reply.ftl");
	}
	
	/**
	 * 
	 * @description 查看评价 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:27:37 PM
	 */
	public void query(){
		int userId = getParaToInt();
		String select = "SELECT a.* , b.name as driver_name ";
		String from = " from logistics_user_reply a left join logistics_driver b on a.driver_id = b.id where a.user_id = " + userId + " and a.status!=-1 order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select, from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page, "id|user_id|driver_id|driver_name|score1|score2|score3|reply_content|reply_time"));
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
		Db.update("update logistics_user_reply set status = -1 where id = ? " , id);
	}
	
	/**
	 * 
	 * @description 查询订单评价，页面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 5:04:44 PM
	 */
	public void orderReply(){
		setAttr("orderId", getParaToInt());
		render("order_user_reply.ftl");
	}
	
	/**
	 * 
	 * @description 查询订单评价
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 5:05:43 PM
	 */
	public void queryOrderReply(){
		int orderId = getParaToInt();
		String select = "SELECT a.* , b.name as driver_name ";
		String from = " from logistics_user_reply a left join logistics_driver b on a.driver_id = b.id where a.order_id = " + orderId + " and a.status!=-1 order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select, from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page, "id|user_id|driver_id|driver_name|score1|score2|score3|reply_content|reply_time"));
	}
}
