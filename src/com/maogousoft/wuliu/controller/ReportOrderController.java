/**
 * @filename ReportOrder.java
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
 * @description 订单报表
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 13, 2013 6:23:12 PM
 */
public class ReportOrderController extends BaseController{


	/**
	 *
	 * @description  下单量日报页面
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 13, 2013 6:31:14 PM
	 */
	public void dayReport(){
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
		String sql = "select count(*) as num,left(create_time,10) as count_day  from logistics_order where create_time >= ? and  create_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		for(Record record : list){
			System.out.println("-count_day-" + record.getStr("count_day"));
		}
		System.out.println("下单量日报:" + list);
		setAttr("list", list);
		render("dayReport.ftl");
	}

	/**
	 *
	 * @description 下单量月报表
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 13, 2013 9:06:13 PM
	 */
	public void monthReport(){
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
		String sql = "select count(*) as num,left(create_time,7) as count_day  from logistics_order where create_time >= ? and  create_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("monthReport.ftl");
	}

	/**
	 *
	 * @description 下单量年报
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 13, 2013 9:09:09 PM
	 */
	public void yearReport(){
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
		String sql = "select count(*) as num,left(create_time,4) as count_day  from logistics_order where create_time >= ? and  create_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("yearReport.ftl");
	}

	/**
	 *
	 * @description 下单金额日报表
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 13, 2013 9:33:14 PM
	 */
	public void dayMoneyReport(){
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
		String sql = "select sum(price) as num,left(create_time,10) as count_day  from logistics_order where create_time >= ? and  create_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("dayMoneyReport.ftl");
	}

	/**
	 *
	 * @description 下单金额月报表
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 14, 2013 2:24:49 PM
	 */
	public void monthMoneyReport(){
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
		String sql = "select sum(price)  as num,left(create_time,7) as count_day  from logistics_order where create_time >= ? and  create_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("monthMoneyReport.ftl");
	}

	/**
	 *
	 * @description 下单金额年报表
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 14, 2013 2:26:15 PM
	 */
	public void yearMoneyReport(){
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
		String sql = "select sum(price) as num,left(create_time,4) as count_day  from logistics_order where create_time >= ? and  create_time <= ? group by count_day";
		List<Record> list = Db.find(sql , start , end);
		setAttr("list", list);
		render("yearMoneyReport.ftl");
	}
}
