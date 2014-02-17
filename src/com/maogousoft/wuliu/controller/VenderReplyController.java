package com.maogousoft.wuliu.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;

/**
 * 商户评价
 */
public class VenderReplyController  extends BaseController{

	public void index(){
		int vender_id = getParaToInt();
		setAttr("vender_id", vender_id);
		render("vender_reply.ftl");
	}

	/**
	 *
	 * 查看评价
	 */
	public void query(){
		int vender_id = getParaToInt();
		String select = "SELECT a.* , b.name as driver_name ";
		String from = " from logistics_vender_reply a left join logistics_driver b on a.driver_id = b.id where a.vender_id = " + vender_id + " order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select, from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * 删除
	 */
	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_vender_reply set status = -1 where id = ? " , id);
	}
}
