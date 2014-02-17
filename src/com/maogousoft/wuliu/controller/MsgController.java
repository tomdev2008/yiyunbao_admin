/**
 * @filename MsgController.java
 */
package com.maogousoft.wuliu.controller;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Msg;
import com.maogousoft.wuliu.service.PushService;
import com.maogousoft.wuliu.service.PushService2;

/**
 * @description 系统消息
 * @author shevliu
 * @email shevliu@gmail.com
 * May 6, 2013 9:29:13 PM
 */
public class MsgController extends BaseController{

	private static final Log log = LogFactory.getLog(MsgController.class);

	public void index(){
		render("msg.ftl");
	}

	public void list(){
		String from = "from logistics_sys_msg where msg_type=0 and  status = 0 order by id desc" ;
		Page<Msg> page = Msg.dao.paginate(getPageIndex(), getPageSize(),
				"select * ", from );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|msg_title|msg_content|msg_time"));
	}

	public void add(){
		render("msg_add.ftl");
	}

	public void save(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord() ;
			record.set("msg_type", 0);
			record.set("msg_time", new Date());
			record.set("status", 0);
			record.remove("id");
			Db.save("logistics_sys_msg", record);
			JSONObject json = new JSONObject();
			json.put("msg_type", PushService.TYPE_NEW_PUSH_MSG);
			json.put("msg_title", "[公告]" + record.getStr("msg_title"));
			json.put("msg_count", 1);
			json.put("last_read_msg", 0);
//			PushService.pushToAll(json.toString(), PushService.TYPE_NEW_PUSH_MSG);
			PushService2.pushToAll(json.toString());
			renderJson(JSONUtils.toMsgJSONString("发布公告成功", true));
		}catch(Exception re){
			log.error("发布公告失败" , re);
			renderJson(JSONUtils.toMsgJSONString("发布公告失败，请稍候再试", false));
		}
	}

	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_sys_msg set status = -1 where id = ? " , id);
	}
}
