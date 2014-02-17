/**
 * @filename IMControler.java
 */
package com.maogousoft.wuliu.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;

/**
 * @description 聊天记录
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 22, 2013 10:22:09 PM
 */
public class IMControler extends BaseController{

	public void index(){
		setAttr("orderId", getParaToInt());
		render("im.ftl");
	}
	
	/**
	 * 
	 * @description 查询聊天记录
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 22, 2013 10:27:14 PM
	 */
	public void queryIm(){
		int orderId = getParaToInt();
		String select = "select a.*  " ;
		String from = " from logistics_im a  where  a.order_id = " + orderId + " order by a.id desc" ;
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(),
				select , from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}
}
