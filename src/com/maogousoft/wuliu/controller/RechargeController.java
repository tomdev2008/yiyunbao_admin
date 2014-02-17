/**
 * @filename RechargeController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.UserDataHandler;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.TimeUtil;
import com.maogousoft.wuliu.domain.Business;
import com.maogousoft.wuliu.domain.Driver;
import com.maogousoft.wuliu.domain.User;
import com.maogousoft.wuliu.domain.sys.SystemUser;

/**
 * @description 充值
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 6, 2013 4:31:15 PM
 */
public class RechargeController extends BaseController{

	private static final Log log = LogFactory.getLog(RechargeController.class);

	/**
	 *
	 * @description 货主充值界面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 4:32:58 PM
	 */
	public void user(){
		setAttr("userId", getParaToInt());
		render("user_recharge.ftl");
	}



	/**
	 *
	 * @description 货主充值
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 4:32:34 PM
	 */
	@Before(Tx.class)
	public void userRecharge(){
		int userId = getParaToInt("userId");
		Record record = getMiniData().getRecord();
		int recharge = record.getInt("recharge");
		int sysUserId = getUserId();
		if(sysUserId == 0 || userId ==0 ){
			renderText(JSONUtils.toMsgJSONString("用户数据不正确", false)) ;
			return ;
		}
		if(!validateMaxMoney(recharge, sysUserId)){
			renderText(JSONUtils.toMsgJSONString("已经超出本日充值限额", false)) ;
			return;
		}
		//开始充值
		try{
			User user = User.dao.findById(userId);
			double beforeMoney = user.get("gold") == null ? 0 : user.getDouble("gold");
			double afterMoney = beforeMoney + recharge ;
			Db.update("update logistics_user set gold = ? where id = ?" , afterMoney ,userId );
			String businessSql = "insert into logistics_business (business_target , account , business_type , business_amount , before_balance , after_balance , create_time , system_user_id) values (?,?,?,?,?,?,?,?)" ;
			Db.update(businessSql , Business.TARGET_TYPE_USER , userId , Business.BUSINESS_TYPE_RECHARGE , recharge , beforeMoney , afterMoney , new Date(),sysUserId);
			renderText(JSONUtils.toMsgJSONString("充值成功", true));
		}
		catch(RuntimeException re){
			log.error("充值失败" , re);
			renderText(JSONUtils.toMsgJSONString("充值失败，稍候再试试", false));
		}
	}

	/**
	 *
	 * @description 货主充值界面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 6:09:19 PM
	 */
	public void driver(){
		setAttr("driverId", getParaToInt());
		render("driver_recharge.ftl");
	}

	/**
	 *
	 * @description 司机充值
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 6:10:26 PM
	 */
	@Before(Tx.class)
	public void driverRecharge(){
		int driverId = getParaToInt("driverId");
		Record record = getMiniData().getRecord();
		int recharge = record.getInt("recharge");
		int sysUserId = getUserId();
		if(sysUserId == 0 || driverId ==0 ){
			renderText(JSONUtils.toMsgJSONString("用户数据不正确", false)) ;
			return ;
		}
		if(!validateMaxMoney(recharge, sysUserId)){
			renderText(JSONUtils.toMsgJSONString("已经超出本日充值限额", false)) ;
			return;
		}
		//开始充值
		try{
			Driver driver = Driver.dao.findById(driverId);
			double beforeMoney = driver.getDouble("gold");
			double afterMoney = beforeMoney + recharge ;
			Db.update("update logistics_driver set gold = ? where id = ?" , afterMoney ,driverId );
			String businessSql = "insert into logistics_business (business_target , account , business_type , business_amount , before_balance , after_balance , create_time , system_user_id) values (?,?,?,?,?,?,?,?)" ;
			Db.update(businessSql , Business.TARGET_TYPE_DRIVER , driverId , Business.BUSINESS_TYPE_RECHARGE , recharge , beforeMoney , afterMoney , new Date(),sysUserId);
			renderText(JSONUtils.toMsgJSONString("充值成功", true));
		}
		catch(RuntimeException re){
			log.error("充值失败" , re);
			renderText(JSONUtils.toMsgJSONString("充值失败，稍候再试试", false));
		}
	}


	/**
	 *
	 * @description 验证最大充值限额
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 6:12:08 PM
	 * @param recharge
	 * @param sysUserId
	 */
	private boolean validateMaxMoney(int recharge, int sysUserId) {
		String today = TimeUtil.format(new Date(), "yyyy-MM-dd") + " 00:00:00" ;
		//验证操作员当日已充值金额
		Record todayRecharge = Db.findFirst("select sum(business_amount) as todaySum from logistics_business where system_user_id = ? and create_time >= ? ", sysUserId , today);
		log.info("todayRecharge:" + todayRecharge);
		SystemUser sysUser = SystemUser.dao.findById(sysUserId);
		double todayMoney = todayRecharge.get("todaySum") == null ? 0 : todayRecharge.getDouble("todaySum") ;
		int maxMoney = sysUser.getInt("max_money");
		log.info("todayMoney + recharge=" + (todayMoney + recharge));
		log.info("maxMoney=" + maxMoney);
		if((todayMoney + recharge) > maxMoney){
			return false;
		}
		else{
			return true ;
		}
	}
}
