package com.maogousoft.wuliu.controller;

import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.maogousoft.wuliu.service.PushService;

/**
 * @author yangfan(kenny0x00@gmail.com) 2013-5-12 上午2:58:42
 */
public class PushController extends Controller {

	@ClearInterceptor(ClearLayer.ALL)
	public void pushSysMsg() {
		String to = getPara("to");
		int msgType = getParaToInt("msgType");
		String msgBody = getPara("msgBody");
		PushService.pushSysMsgByDriverPhone(new String[] {to}, msgBody, msgType);

		renderNull();
	}
}
