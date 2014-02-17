/**
 * @filename DictController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Dict;

/**
 * @description 字典
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 15, 2013 9:01:34 PM
 */
public class DictController extends BaseController{

	public void list(){
		String type = getPara();
		setAttr("type", type);
		render("dict.ftl");
	} 
	
	public void getListByType(){
		List<Dict> list = Dict.dao.find("select * from logistics_dict where status = 0 and dict_type= ? order by id desc" , getPara());
		String json  = JSONUtils.toJSONString(list, "id|name|dict_type|dict_desc");
		renderText(json);
	}
	
	/**
	 * 
	 * @description 根据上级编号查询 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 8:46:02 PM
	 */
	public void getByPid(){
		int pid = getParaToInt("id" , 0);
		getByPid(pid);
	}
	
	private void getByPid(int pid){
		String dictType = getPara("dict_type") ;
		String sql = "select * from logistics_dict where status = 0 and pid= " + pid ;
		if(StringUtils.isNotBlank(dictType)){
			sql += " and dict_type = '" + dictType + "'"  ;
		}
		sql += " order by id desc" ;
		List<Dict> list = Dict.dao.find(sql);
		for(Dict dict : list){
			dict.put("isLeaf", false);
			dict.put("expanded", false);
		}
		String json  = JSONUtils.toJSONString(list, "id|name|dict_type|dict_desc|pid|isLeaf|expanded");
		renderText(json);
	}
	
	public void getArea(){
		int pid = getParaToInt("id" , 0);
		String sql = "select * from logistics_area where status = 0 and pid= " + pid + " order by id";
		List<Record> list = Db.find(sql);
		for(Record area : list){
			if(area.getInt("deep") <3){
				area.set("isLeaf", false);
				area.set("expanded", false);
			}else{
				area.set("isLeaf", true);
				area.set("expanded", true);
			}
		}
		String json  = JSONUtils.toJSONStringUsingRecord(list, "id|name|short_name|pid|isLeaf|expanded");
		renderText(json);
		
	}
	
	/**
	 * 
	 * @description 查询所有车型 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 8:47:03 PM
	 */
	public void getAllCarType(){
		getAllByType(Dict.TYPE_CAR);
	}
	
	/**
	 * 
	 * @description 查询所有货物类型
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 11:06:32 PM
	 */
	public void getAllCargoType(){
		getAllByType(Dict.TYPE_CARGO);
	}
	
	/**
	 * 
	 * @description 根据类型查询 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 7:30:32 PM
	 */
	private void getAllByType(String type){
		List<Dict> list = Dict.dao.find("select * from logistics_dict where status = 0 and dict_type= ? order by id desc" , type);
		Dict first = new Dict();
		first.put("id", "");
		first.put("name", "--请选择--");
		String json  = JSONUtils.toJSONString(list, "id|name|dict_type|dict_desc");
		renderText(json);
	}
	
	public void save(){
		String type=getPara("dict_type");
		System.out.println("type.... " + type);
		MiniData data = getMiniData();
		Record record = data.getRecord() ;
		record.set("dict_type", type);
		if(data.getPageState().equals(MiniData.ADD)){
			Db.save("logistics_dict", record);
		}
		else{
			Db.update("update logistics_dict set name= ? , dict_desc=? where id = ? " , record.getStr("name") , record.getStr("dict_desc") , record.getInt("id")) ;
		}
	}
	
	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_dict set status = -1 where id = ? " , id);
	}
	
	/**
	 * 
	 * @description 地区树 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 18, 2013 10:37:10 PM
	 */
	public void areaTree(){
		render("area_tree.ftl");
	}
}
