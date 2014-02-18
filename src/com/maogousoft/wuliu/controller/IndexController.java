/**
 * @filename IndexController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.List;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.CookieUtil;
import com.maogousoft.wuliu.common.utils.DesCipher;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.MD5Util;
import com.maogousoft.wuliu.domain.Constant;

/**
 * @description 首页
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 14, 2013 10:07:43 PM
 */
public class IndexController extends BaseController{

	public void index(){
		String userName = getUserName();
		setAttr("userName", userName);
		render("main.ftl");
	}
	
	/**
	 * 
	 * @description 登录页面 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:55:51 PM
	 */
	@ClearInterceptor(ClearLayer.ALL)
	public void login(){
		render("login.ftl");
	}
	
	/**
	 * 
	 * @description 登录 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 12:42:06 PM
	 */
	@ClearInterceptor(ClearLayer.ALL)
	public void doLogin(){
		String account = getPara("account");
		String pwd = MD5Util.MD5(getPara("pwd"));
		Record record = Db.findFirst("select * from system_user where user_account = ? and user_password = ?" , account , pwd);
		if(record == null){
			renderText(JSONUtils.toMsgJSONString("账号或密码不正确", false));
		}else{
			int userId = record.getInt("id");
			List<Record> privilegeList = Db.find("select * from system_user_privilege where user_id = ?" , userId);
			if(privilegeList == null || privilegeList.size() == 0){
				renderText(JSONUtils.toMsgJSONString("该账号无任何权限", false));
				return ;
			}
			String privileges = "";
			for(Record privilege :privilegeList){
				privileges += privilege.getInt("privilege_id") + ",";
			}
			privileges = privileges.substring(0 , privileges.length() -1);
			
			DesCipher desCipher = new DesCipher(Constant.COOKIE_DES_KEY);
			String cookieId = desCipher.encrypt(userId + "");
			String cookieAccount = desCipher.encrypt(account);
			String cookieUserName = desCipher.encrypt(record.getStr("user_name"));
			String cookiePrivileges = desCipher.encrypt(privileges);
			CookieUtil.addCookie(getResponse(), Constant.COOKIE_ID, cookieId);
			CookieUtil.addCookie(getResponse(), Constant.COOKIE_ACCOUNT, cookieAccount);
			CookieUtil.addCookie(getResponse(), Constant.COOKIE_USER_NAME, cookieUserName);
			CookieUtil.addCookie(getResponse(), Constant.COOKIE_PRIVILEGE, cookiePrivileges);
			renderText(JSONUtils.toMsgJSONString("成功", true));
		}
	}
	
	/**
	 * 
	 * @description 注销
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:31:42 PM
	 */
	public void logout(){
		CookieUtil.clearCookie(getRequest(), getResponse());
		redirect("/admin");
	}
	
	/**
	 * 
	 * @description 修改密码页面 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:35:07 PM
	 */
	public void newPWD(){
		render("newPWD.ftl");
	}
	
	/**
	 * 
	 * @description 修改密码 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:45:32 PM
	 */
	public void updatePWD(){
		Record form = getMiniData().getRecord();
		int userId = getUserId() ;
		String oldPWD = MD5Util.MD5(form.getStr("oldPWD")) ;
		String newPWD = form.getStr("newPWD") ;
		String newPWD2 = form.getStr("newPWD2") ;
		
		if(!newPWD.equals(newPWD2)){
			renderText(JSONUtils.toMsgJSONString("两次输入的新密码不一致", false)) ;
			return ;
		}
		Record record = Db.findFirst("select * from system_user where id = ? ", userId) ;
		if(record == null){
			renderText(JSONUtils.toMsgJSONString("用户不存在", false)) ;
			return ;
		}
		if(!record.getStr("user_password").equals(oldPWD)){
			renderText(JSONUtils.toMsgJSONString("原密码不正确", false)) ;
			return ;
		}
		Db.update("update system_user set user_password = ? where id = ?" , MD5Util.MD5(newPWD) , userId) ;
		renderText(JSONUtils.toMsgJSONString("修改成功", true)) ;
	}
	
	/**
	 * 
	 * @description 用户权限
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 3:44:49 PM
	 */
	public void userPrivilege(){
		String sql = "select b.* from system_user_privilege a left join system_privilege b on a.privilege_id = b.id where b.privilege_type = 0 and b.status=0 and a.user_id = ?" ;
		List<Record > list = Db.find(sql , getUserId());
		for(Record privilege : list){
			privilege.set("isLeaf", privilege.getInt("is_leaf") == 1);
			privilege.set("expanded", false);
		}
		String json  = JSONUtils.toJSONStringUsingRecord(list, "id|pid|privilege_name|privilege_code|url|order_num|privilege_type|isLeaf|status|expanded|checked");
		renderText(json);
	}
}
