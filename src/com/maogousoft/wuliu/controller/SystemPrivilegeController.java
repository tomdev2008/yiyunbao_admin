/**
 * @filename PrivilegeController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;

/**
 * @description 权限管理
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 4, 2013 9:48:17 PM
 */
public class SystemPrivilegeController extends BaseController{
	
	/**
	 * 
	 * @description 用户权限页面 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 4, 2013 6:20:06 PM
	 */
	public void userPrivilege(){
		setAttr("userId", getParaToInt());
		render("user_privilege.ftl");
	}
	
	/**
	 * 
	 * @description 获取用户权限 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 4, 2013 10:20:36 PM
	 */
	public void queryByUser(){
		int userId = getParaToInt();
		List<Record> allList = Db.find("select * from system_privilege where status=0 order by order_num desc , id");
		List<Record> userPrivilegeList = Db.find("select * from system_user_privilege where user_id = ?" , userId);
		for(Record privilege : allList){
			privilege.set("isLeaf", privilege.getInt("is_leaf") == 1);
			privilege.set("expanded", true);
			privilege.set("checked", false);
			for(Record userPrivilege : userPrivilegeList){
				if(privilege.getInt("id") == userPrivilege.getInt("privilege_id")){
					privilege.set("checked", true);
				}
			}
		}
		String json  = JSONUtils.toJSONStringUsingRecord(allList, "id|pid|privilege_name|privilege_code|url|order_num|privilege_type|isLeaf|status|expanded|checked");
		renderText(json);
	}
	
	/**
	 * 
	 * @description 更新权限
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 4, 2013 11:03:38 PM
	 */
	public void updateUserPrivilege(){
		int userId = getParaToInt("userId");
		String[] privileges = getPara("privilege").split(",");
		Db.update("delete from system_user_privilege where user_id = ? " , userId) ;
			for(String privilege : privileges){
				int privilegeId = NumberUtils.toInt(privilege) ;
				if(privilegeId > 0){
					Db.update("insert into system_user_privilege (user_id , privilege_id) values (? , ?)" , userId , NumberUtils.toInt(privilege));
				}
			}
	}
}
