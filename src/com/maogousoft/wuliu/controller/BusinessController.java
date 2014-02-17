/**
 * @filename BusinessController.java
 */
package com.maogousoft.wuliu.controller;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Business;

/**
 * @description 交易记录
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 31, 2013 5:23:40 PM
 */
public class BusinessController extends BaseController {
	
	/**
	 * 
	 * @description 货主交易记录 页面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 5:34:07 PM
	 */
	public void userBusiness(){
		setAttr("userId", getParaToInt()) ;
		render("userBusiness.ftl");
	}

	/**
	 * 
	 * @description 货主交易记录 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 5:33:12 PM
	 */
	public void queryUserBusiness(){
		int userId = getParaToInt();
		query(Business.TARGET_TYPE_USER, userId);
	}
	
	/**
	 * 
	 * @description 司机交易记录，页面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 6:12:14 PM
	 */
	public void driverBusiness(){
		setAttr("driverId", getParaToInt());
		render("driverBusiness.ftl");
	}
	
	/**
	 * 
	 * @description 司机交易记录
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 6:14:33 PM
	 */
	public void queryDriverBusiness(){
		int driverId = getParaToInt();
		query(Business.TARGET_TYPE_DRIVER, driverId);
	}
	
	/**
	 * 
	 * @description 查询交易记录
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 31, 2013 6:13:21 PM
	 * @param target
	 * @param account
	 */
	private void query(int target , int account){
		String select = "SELECT a.* ,b.user_name as sysUserName";
		String from = " from logistics_business a left join system_user b on a.system_user_id=b.id  where a.business_target = " + target + " and a.account = " + account + " order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select, from );
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page, "id|business_target|account|business_type|business_amount|before_balance|after_balance|create_time|sysUserName"));
	}
}
