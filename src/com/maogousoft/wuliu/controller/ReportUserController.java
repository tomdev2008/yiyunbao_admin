/**
 * @filename ReportUserController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.TimeUtil;

/**
 * @description 用户相关报表
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 14, 2013 2:18:58 PM
 */
public class ReportUserController extends BaseController{

	/**
	 * 
	 * @description 货主注册量日报 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 14, 2013 2:47:31 PM
	 */
	public void userRegDayReport(){
		String start = getPara("start");
		String end = getPara("end");
		
		if(StringUtils.isBlank(start)){
			start  = TimeUtil.format(new Date(), "yyyy-MM") + "-01";
		}
		if(StringUtils.isBlank(end)){
			end  = TimeUtil.format(new Date(), "yyyy-MM") + "-31";
		}
		start += " 00:00:00 " ;
		end += " 23:59:59 ";
		String sql = "select count(*) as num,left(regist_time,10) as count_day  from logistics_user where regist_time >= ? and  regist_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("userRegDayReport.ftl");
	}
	
	/**
	 * 
	 * @description  货主注册量月报 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 14, 2013 2:56:52 PM
	 */
	public void userRegMonthReport(){
		String start = getPara("start");
		String end = getPara("end");
		
		if(StringUtils.isBlank(start)){
			start  = TimeUtil.format(new Date(), "yyyy") + "-01-01";
		}
		if(StringUtils.isBlank(end)){
			end  = TimeUtil.format(new Date(), "yyyy-") + "-12-31";
		}
		start += " 00:00:00 " ;
		end += " 23:59:59 ";
		String sql = "select count(*) as num,left(regist_time,7) as count_day  from logistics_user where regist_time >= ? and  regist_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("userRegMonthReport.ftl");
	}
	
	/**
	 * 
	 * @description  货主注册量年报 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 14, 2013 2:56:52 PM
	 */
	public void userRegYearReport(){
		String start = getPara("start");
		String end = getPara("end");
		
		if(StringUtils.isBlank(start)){
			start  = TimeUtil.format(new Date(), "yyyy") + "-01-01";
		}
		if(StringUtils.isBlank(end)){
			end  = TimeUtil.format(new Date(), "yyyy-") + "-12-31";
		}
		start += " 00:00:00 " ;
		end += " 23:59:59 ";
		String sql = "select count(*) as num,left(regist_time,4) as count_day  from logistics_user where regist_time >= ? and  regist_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("userRegYearReport.ftl");
	}
	
	/**
	 * 
	 * @description 司机注册量日报 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 14, 2013 3:12:04 PM
	 */
	public void driverRegDayReport(){
		String start = getPara("start");
		String end = getPara("end");
		
		if(StringUtils.isBlank(start)){
			start  = TimeUtil.format(new Date(), "yyyy-MM") + "-01";
		}
		if(StringUtils.isBlank(end)){
			end  = TimeUtil.format(new Date(), "yyyy-MM") + "-31";
		}
		start += " 00:00:00 " ;
		end += " 23:59:59 ";
		String sql = "select count(*) as num,left(regist_time,10) as count_day  from logistics_driver where regist_time >= ? and  regist_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("driverRegDayReport.ftl");
	}
	
	/**
	 * 
	 * @description 司机注册量月报 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 15, 2013 9:33:15 PM
	 */
	public void driverRegMonthReport(){
		String start = getPara("start");
		String end = getPara("end");
		
		if(StringUtils.isBlank(start)){
			start  = TimeUtil.format(new Date(), "yyyy") + "-01-01";
		}
		if(StringUtils.isBlank(end)){
			end  = TimeUtil.format(new Date(), "yyyy-") + "-12-31";
		}
		start += " 00:00:00 " ;
		end += " 23:59:59 ";
		String sql = "select count(*) as num,left(regist_time,7) as count_day  from logistics_driver where regist_time >= ? and  regist_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("driverRegMonthReport.ftl");
	}
	
	/**
	 * 
	 * @description  司机注册量年报 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 15, 2013 9:34:16 PM
	 */
	public void driverRegYearReport(){
		String start = getPara("start");
		String end = getPara("end");
		
		if(StringUtils.isBlank(start)){
			start  = TimeUtil.format(new Date(), "yyyy") + "-01-01";
		}
		if(StringUtils.isBlank(end)){
			end  = TimeUtil.format(new Date(), "yyyy-") + "-12-31";
		}
		start += " 00:00:00 " ;
		end += " 23:59:59 ";
		String sql = "select count(*) as num,left(regist_time,4) as count_day  from logistics_driver where regist_time >= ? and  regist_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("driverRegYearReport.ftl");
	}
}
