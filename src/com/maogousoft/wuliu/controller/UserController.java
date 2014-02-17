/**
 * @filename SuggestController.java
 */
package com.maogousoft.wuliu.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.MD5Util;
import com.maogousoft.wuliu.common.utils.TimeUtil;
import com.maogousoft.wuliu.common.utils.WuliuStringUtils;
import com.maogousoft.wuliu.domain.Dict;
import com.maogousoft.wuliu.domain.Driver;
import com.maogousoft.wuliu.domain.User;

/**
 * @description 货主
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 15, 2013 11:54:35 PM
 */
public class UserController extends BaseController{

	public void index(){
		render("user.ftl");
	}
	
	/**
	 * 
	 * @description 查询列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 15, 2013 11:56:59 PM
	 */
	public void query(){
		StringBuffer from = new StringBuffer();
		from.append("from logistics_user where status != -1 ") ;
		from.append(createAnd("phone|name|company_name|recommender"));
		from.append(createOrder()) ;
		
		Page<User> page = User.dao.paginate(getPageIndex(), getPageSize(), "select * ", from.toString() );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|phone|name|licence_photo|recommender|regist_time|login_time|id_card|bank|account_name|bank_account|gold|total_deal|user_level|score1|score2|score3|company_name|telcom|address|fleet_count"));
	}
	
	/**
	 * 
	 * @description 根据推荐人查询 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * 2013年10月12日 下午11:44:06
	 */
	public void queryByRecommenderPage(){
		render("user_recommender.ftl");
	}
	
	/**
	 * 
	 * @description 根据推荐人查询 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * 2013年10月12日 下午11:36:18
	 */
	public void queryByRecommender(){
		StringBuffer from = new StringBuffer();
		String account = getUserPhone();
		from.append("from logistics_user where status != -1 and recommender = '" + account + "' order by id desc") ;
		Page<User> page = User.dao.paginate(getPageIndex(), getPageSize(), "select * ", from.toString() );
		renderText(JSONUtils.toPagedGridJSONString(page, "id|phone|name|licence_photo|recommender|regist_time|login_time|id_card|bank|account_name|bank_account|gold|total_deal|user_level|score1|score2|score3|company_name|telcom|address|fleet_count"));
	}
	
	/**
	 * 
	 * @description 删除 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:51:06 PM
	 */
	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_user set status = -1 where id = ? " , id);
	}
	
	/**
	 * 添加
	 */
	public void add(){
		render("user_add.ftl");
	}
	
	public void save(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord() ;
			String password = MD5Util.MD5(record.getStr("password"));
			
			record.remove("id");
			record.set("password", password);
			record.set("regist_time", new Date());
			record.set("device_type", 3);
			record.set("gold", 8);
			record.set("total_deal", 0);
			record.set("user_level", 0);
			record.set("status", 0);
			record.set("score1", 3);
			record.set("score2", 3);
			record.set("score3", 3);
			record.set("score", 3);
			record.set("last_read_msg", new Date());
			

			String sql = "select count(1) as count_num from logistics_user where phone = ?" ;
			String sqlDriver = "select count(1) as count_num from logistics_driver where phone = ?" ;
			long count = User.dao.findFirst(sql, record.getStr("phone")).getLong("count_num") + Driver.dao.findFirst(sqlDriver , record.getStr("phone")).getLong("count_num");
			if(count > 0){
				renderJson(JSONUtils.toMsgJSONString("手机号已被注册了，请换一个号码", false));
				return;
			}
			else{
				Db.save("logistics_user", record);

				//记录全局用户
				Record globalUser = new Record();
				globalUser.set("uid", "u" + record.getLong("id"));
				globalUser.set("password", password);
				globalUser.set("user_type", 0);//0-货主,1-司机
				globalUser.set("data_id", record.getLong("id"));
				globalUser.set("create_time", new Date());
				Db.save("logistics_global_user", globalUser);
				renderJson(JSONUtils.toMsgJSONString("成功", true));
			}
			renderJson(JSONUtils.toMsgJSONString("保存成功", true));
		}catch(Exception re){
			re.printStackTrace();
			renderJson(JSONUtils.toMsgJSONString("保存失败，请稍候再试", false));
		}
	}
	
	public void edit(){
		int user_id = getParaToInt();
		Record user = Db.findById("logistics_user", user_id);
		setAttr("user", user);
		render("user_edit.ftl");
	}
	
	public void update(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord();
			record.set("status", 0);
			Db.update("logistics_user", record);
			renderJson(JSONUtils.toMsgJSONString("保存成功", true));
		}catch(Exception re){
			re.printStackTrace();
			renderJson(JSONUtils.toMsgJSONString("保存失败，请稍候再试", false));
		}
	}
	
	public void exportExcel() throws IOException {
		StringBuffer from = new StringBuffer();
		from.append("from logistics_user where status = 0 ") ;
		from.append(createAnd("id|phone|company_name|name|recommender|regist_time|id_card|gold|telcom|address"));
		from.append(createOrder()) ;
		Page<Record> page = Db.paginate(getPageIndex(), 100000, "select * ", from.toString());
		List<Record> list = page.getList();

		String headers = "编号|手机号|公司名称|联系人|推荐人|注册时间|身份证号码|物流币|座机|地址";
		String attributes = "id|phone|company_name|name|recommender|regist_time|id_card|gold|telcom|address";

		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row headerRow = sheet.createRow(0);
		List<String> headerList = WuliuStringUtils.parseVertical(headers);
		for(int j = 0; j < headerList.size(); j++) {
			String attr = headerList.get(j);
			Cell cell = headerRow.createCell(j);
			cell.setCellValue(attr);
		}

		for(int i = 0; i < list.size(); i++) {
			Record record = list.get(i);
			Row row = sheet.createRow(i + 1);

			List<String> attrList = WuliuStringUtils.parseVertical(attributes);
			for(int j = 0; j < attrList.size(); j++) {
				String attr = attrList.get(j);
				Cell cell = row.createCell(j);
				Object value = getValue(record, attr);
				cell.setCellValue(value + "");
			}
		}

		HttpServletResponse resp = getResponse();
		String filename = TimeUtil.format(new Date(), "'货主导出'yyyyMMdd_HHmmss'.xls'");
		resp.addHeader("Content-Disposition","attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));
		ServletOutputStream out = resp.getOutputStream();
		wb.write(out);
		out.close();
		renderNull();
	}
	
	private Object getValue(Record record, String attr) {
		Object value = record.get(attr, "");
		return value;
	}
}
