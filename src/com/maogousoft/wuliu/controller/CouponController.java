/**
 * @filename CouponController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.Random;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.jfinal.plugin.activerecord.Page;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Coupon;
import com.maogousoft.wuliu.service.SmsService;

/**
 * @description 充值卡
 * @author shevliu
 * @email shevliu@gmail.com
 * Jun 12, 2013 11:30:37 PM
 */
public class CouponController extends BaseController{
	
	private static Log log = LogFactory.getLog(CouponController.class);

	public void index(){
		render("coupon.ftl");
	}

	public void list(){
		String status = getPara("status");
		String from = "from logistics_coupon  " ;
		if(StringUtils.hasText(status)){
			from += " where status = " + status;
		}
		from += " order by id desc";
		Page<Coupon> page = Coupon.dao.paginate(getPageIndex(), getPageSize(),
				"select * ", from );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|card_no|uid|status|send_mobile|use_time"));
	}
	
	public void add(){
		try{
			String card_no = random();
			String card_pwd = random();
			Coupon coupon = new Coupon();
			coupon.set("card_no", card_no);
			coupon.set("card_pwd", card_pwd);
			coupon.set("status", 0);
			coupon.save();
			renderJson(JSONUtils.toMsgJSONString("生成充值卡成功", true));
		}
		catch(RuntimeException re){
			log.error(re);
			renderJson(JSONUtils.toMsgJSONString("生成充值卡失败，请稍后再试", false));
		}
	}
	
	public void sms(){
		try{
			String mobile = getPara("phone");
			Coupon coupon = Coupon.dao.findById(getParaToInt("coupon_id"));
			if(coupon.getInt("status") == Coupon.STATUS_USED){
				renderJson(JSONUtils.toMsgJSONString("充值卡已消费，不能再次发送", false));
				return;
			}
			String content = "易运宝充值卡号：" + coupon.getStr("card_no") + "，密码：" + coupon.getStr("card_pwd");
			coupon.set("send_mobile", mobile);
			coupon.set("status", Coupon.STATUS_SENDED);
			coupon.update();
			log.debug("content:" + content);
			SmsService.send(mobile, content);
			renderJson(JSONUtils.toMsgJSONString("发送短信成功", true));
		}catch(RuntimeException re){
			log.error("发送短信失败：", re);
			renderJson(JSONUtils.toMsgJSONString("发送短信失败", false));
		}
	}
	
	
	private String random(){
		String s = RandomUtils.nextInt(1000000000) + "";
		if(s.length() < 9 ){
			return random();
		}
		return s ;
	}
	
}
