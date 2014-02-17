/**
 * @filename SystemUserController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.MD5Util;
import com.maogousoft.wuliu.domain.sys.SystemUser;

/**
 * @description 系统用户管理
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 4, 2013 3:32:28 PM
 */
public class SystemUserController extends BaseController{

	public void index(){
		render("user.ftl");
	}
	
	public void list(){
		StringBuffer from = new StringBuffer();
		from.append("from system_user where status != -1 ") ;
		from.append(createAnd("user_account|user_name|cell"));
		from.append(createOrder()) ;
		
		Page<SystemUser> page = SystemUser.dao.paginate(getPageIndex(), getPageSize(), "select * ", from.toString() );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|user_account|user_name|id_card|email|cell|tel|create_time|max_money|status"));
	}
	
	public void add(){
		render("user_add.ftl");
	}
	
	public void save(){
		MiniData data = getMiniData();
		Record record = data.getRecord() ;
		String password = record.getStr("user_password");
		record.set("user_password", MD5Util.MD5(password));
		record.set("create_time", new Date());
		record.remove("id");
		
		String sql = "select count(1) as count_num from system_user where user_account = ?" ;
		long count = SystemUser.dao.findFirst(sql, record.get("user_account")).getLong("count_num");
		System.out.println("count ......... " + count);
		if(count > 0){
			renderText(JSONUtils.toMsgJSONString("用户名重复", false));
		}else{
			Db.save("system_user", record);
			renderText(JSONUtils.toMsgJSONString("", true));
		}
	}
	
	public void update(){
		MiniData data = getMiniData();
		Record record = data.getRecord() ;
		String sql = "update system_user set user_name = ?, id_card= ?,email=? ,cell= ? , tel = ? , max_money = ? where id = ?" ;
		Db.update(sql, record.getStr("user_name") , record.getStr("id_card") , record.getStr("email") , record.getStr("cell") , record.getStr("tel") , record.get("max_money") , record.getInt("id"));
	}
	
	public void delete(){
		int id = getParaToInt();
		Db.update("update system_user set status = -1 where id = ? " , id);
	}
	
}
