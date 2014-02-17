package com.maogousoft.wuliu.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.maogousoft.wuliu.common.plugin.quartz.QuartzPlugin;
import com.maogousoft.wuliu.controller.AdController;
import com.maogousoft.wuliu.controller.BusinessController;
import com.maogousoft.wuliu.controller.CouponController;
import com.maogousoft.wuliu.controller.DictController;
import com.maogousoft.wuliu.controller.DriverController;
import com.maogousoft.wuliu.controller.DriverReplyController;
import com.maogousoft.wuliu.controller.IMControler;
import com.maogousoft.wuliu.controller.IndexController;
import com.maogousoft.wuliu.controller.InsuranceController;
import com.maogousoft.wuliu.controller.MsgController;
import com.maogousoft.wuliu.controller.OrderController;
import com.maogousoft.wuliu.controller.PushController;
import com.maogousoft.wuliu.controller.RechargeController;
import com.maogousoft.wuliu.controller.ReportOrderController;
import com.maogousoft.wuliu.controller.ReportUserController;
import com.maogousoft.wuliu.controller.SuggestController;
import com.maogousoft.wuliu.controller.SystemPrivilegeController;
import com.maogousoft.wuliu.controller.SystemUserController;
import com.maogousoft.wuliu.controller.UserController;
import com.maogousoft.wuliu.controller.UserReplyController;
import com.maogousoft.wuliu.controller.VenderController;
import com.maogousoft.wuliu.controller.VenderReplyController;
import com.maogousoft.wuliu.domain.Ad;
import com.maogousoft.wuliu.domain.Area;
import com.maogousoft.wuliu.domain.Business;
import com.maogousoft.wuliu.domain.Coupon;
import com.maogousoft.wuliu.domain.Dict;
import com.maogousoft.wuliu.domain.Driver;
import com.maogousoft.wuliu.domain.DriverReply;
import com.maogousoft.wuliu.domain.Msg;
import com.maogousoft.wuliu.domain.Order;
import com.maogousoft.wuliu.domain.OrderLog;
import com.maogousoft.wuliu.domain.OrderVie;
import com.maogousoft.wuliu.domain.Suggest;
import com.maogousoft.wuliu.domain.User;
import com.maogousoft.wuliu.domain.UserReply;
import com.maogousoft.wuliu.domain.sys.SystemUser;
import com.maogousoft.wuliu.interceptor.LoginInterceptor;
import com.maogousoft.wuliu.interceptor.PrivileteInterceptor;

/**
 * @description 基础配置信息
 * @author shevliu
 * @email shevliu@gmail.com Jul 26, 2012 9:26:46 PM
 */
public class BaseConfig extends JFinalConfig {

	private static BaseConfig me;

	public BaseConfig() {
		super();
		BaseConfig.me = this;
	}

	public static BaseConfig me() {
		return me;
	}

	/**
	 *
	 */
	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("config.properties");
		me.setFreeMarkerViewExtension(".ftl");
		me.setBaseViewPath("WEB-INF/pages");
		me.setDevMode(true);
	}

	@Override
	public void configHandler(Handlers me) {
		
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new LoginInterceptor());
		me.add(new PrivileteInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {

		// 配置C3p0数据库连接池插件
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"), getProperty("jdbc.driverClass"));
		me.add(c3p0Plugin);

		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("logistics_dict", Dict.class);
		arp.addMapping("logistics_area", Area.class);
		arp.addMapping("logistics_suggest", Suggest.class);
		arp.addMapping("logistics_user", User.class);
		arp.addMapping("logistics_user_reply", UserReply.class);
		arp.addMapping("logistics_driver_reply", DriverReply.class);
		arp.addMapping("logistics_driver", Driver.class);
		arp.addMapping("logistics_order_vie", OrderVie.class);
		arp.addMapping("logistics_order", Order.class);
		arp.addMapping("logistics_sys_msg", Msg.class);
		arp.addMapping("logistics_business", Business.class);
		arp.addMapping("logistics_ad", Ad.class);
		arp.addMapping("log_order", OrderLog.class);
		arp.addMapping("logistics_coupon", Coupon.class);

		//系统管理
		arp.addMapping("system_user", SystemUser.class);
		arp.setShowSql(false);

		//配置缓存
		EhCachePlugin cachePlugin = new EhCachePlugin();
		me.add(cachePlugin);

		QuartzPlugin quartzPlugin = new QuartzPlugin();
		me.add(quartzPlugin);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/" , IndexController.class);
		me.add("/dict" , DictController.class);
		me.add("/suggest" , SuggestController.class);
		me.add("/user" , UserController.class);
		me.add("/userReply" , UserReplyController.class);
		me.add("/driver" , DriverController.class);
		me.add("/driverReply" , DriverReplyController.class);
		me.add("/order" , OrderController.class);
		me.add("/im" , IMControler.class);
		me.add("/business" , BusinessController.class);
		me.add("/recharge" , RechargeController.class);
		me.add("/reportOrder" , ReportOrderController.class);
		me.add("/reportUser" , ReportUserController.class);
		me.add("/msg" , MsgController.class);
		me.add("/push" , PushController.class);
		me.add("/coupon" , CouponController.class);
		me.add("/insurance" , InsuranceController.class);
		me.add("/vender" , VenderController.class);
		me.add("/venderReply" , VenderReplyController.class);
		//系统管理
		me.add("/sys/user" , SystemUserController.class);
		me.add("/sys/privilege" , SystemPrivilegeController.class);
		me.add("/ad" , AdController.class);
	}

}
