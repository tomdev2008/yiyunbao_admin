/**
 * @filename AuthInterceptor.java
 */
package com.maogousoft.wuliu.interceptor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.maogousoft.wuliu.cache.PrivilegeCache;
import com.maogousoft.wuliu.common.utils.CookieUtil;
import com.maogousoft.wuliu.common.utils.DesCipher;
import com.maogousoft.wuliu.domain.Constant;

/**
 * @description 登录拦截器
 * @author shevliu
 * @email shevliu@gmail.com
 * Jul 26, 2012 9:47:40 PM
 */
public class PrivileteInterceptor implements Interceptor{

	private Log log = LogFactory.getLog(PrivileteInterceptor.class) ;
	
	public void intercept(ActionInvocation ai) {
		long now = System.currentTimeMillis() ;
		String privilegeStr = CookieUtil.getCookie(ai.getController().getRequest(), Constant.COOKIE_PRIVILEGE);
		DesCipher desCipher = new DesCipher(Constant.COOKIE_DES_KEY);
		String[] privileges = desCipher.decrypt(privilegeStr).split(",");
		String uri = ai.getController().getRequest().getRequestURI();
		int privilegeId = PrivilegeCache.getPrivilegeIdByUri(uri);
		log.info("PrivileteInterceptor耗时----> " + (System.currentTimeMillis() - now));
		if(privilegeId == 0){
			ai.invoke();
		}else{
			boolean flag = false ;
			for(String privilege : privileges){
				if(privilege.equals(privilegeId + "")){
					flag = true ;
					break ;
				}
			}
			if(flag){
				ai.invoke();
			}
			else{
				log.error("用户没有权限，返回登录界面");
				ai.getController().redirect("/admin/login");
				return ;
			}
		}
		
		
	}

}
