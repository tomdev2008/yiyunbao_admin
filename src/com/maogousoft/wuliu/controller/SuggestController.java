/**
 * @filename SuggestController.java
 */
package com.maogousoft.wuliu.controller;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Suggest;

/**
 * @description 意见反馈
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 15, 2013 11:54:35 PM
 */
public class SuggestController extends BaseController{

	public void index(){
		render("suggest.ftl");
	}
	
	/**
	 * 
	 * @description 查询列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 15, 2013 11:56:59 PM
	 */
	public void query(){
		String phone = getPara("phone");
		String beginDate = getPara("beginDate");
		String endDate = getPara("endDate");
		String sortField = getPara("sortField");
		String sortOrder = getPara("sortOrder");
		String from = "from logistics_suggest where status = 0 " ;
		
		if(StringUtils.isNotBlank(phone)){
			from += " and phone = '" + phone + "'" ;
		}
		
		if(StringUtils.isNotBlank(beginDate)){
			from += " and create_time >= '" + beginDate + "'" ;
		}
		
		if(StringUtils.isNotBlank(endDate)){
			endDate += " 23:59:59";
			from += " and create_time <= '" + endDate + "'" ;
		}
		
		if(StringUtils.isNotBlank(sortField)){
			from += " order by " + sortField + " " + sortOrder;
		}else{
			from += " order by id desc ";
		}
		
		Page<Suggest> page = Suggest.dao.paginate(getPageIndex(), getPageSize(),
				"select * ", from );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|phone|suggest_content|create_time"));
	}
	
	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_suggest set status = -1 where id = ? " , id);
	}
}
