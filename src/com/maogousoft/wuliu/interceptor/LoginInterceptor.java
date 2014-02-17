/**
 * @filename AuthInterceptor.java
 */
package com.maogousoft.wuliu.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.maogousoft.wuliu.common.utils.CookieUtil;
import com.maogousoft.wuliu.domain.Constant;

/**
 * @description 登录拦截器
 * @author shevliu
 * @email shevliu@gmail.com
 * Jul 26, 2012 9:47:40 PM
 */
public class LoginInterceptor implements Interceptor{

	private Log log = LogFactory.getLog(LoginInterceptor.class) ;
	
	public void intercept(ActionInvocation ai) {
		String userId = CookieUtil.getCookie(ai.getController().getRequest(), Constant.COOKIE_ID);
		if(userId == null || "".equals(userId)){
			log.error("用户未登录，返回登录界面");
			ai.getController().redirect("/admin/login");
			return ;
		}
		ai.invoke();
	}

}
