/**
 * @filename DriverController.java
 */
package com.maogousoft.wuliu.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.CookieUtil;
import com.maogousoft.wuliu.common.utils.ExcelUtil;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.MD5Util;
import com.maogousoft.wuliu.common.utils.TimeUtil;
import com.maogousoft.wuliu.common.utils.WuliuStringUtils;
import com.maogousoft.wuliu.domain.Area;
import com.maogousoft.wuliu.domain.Business;
import com.maogousoft.wuliu.domain.Dict;
import com.maogousoft.wuliu.domain.Driver;
import com.maogousoft.wuliu.domain.GoldResult;
import com.maogousoft.wuliu.domain.Msg;
import com.maogousoft.wuliu.domain.User;

/**
 * @description 司机管理
 * @author shevliu
 * @email shevliu@gmail.com Jul 28, 2012 5:00:46 PM
 */
public class DriverController extends BaseController {
	private Log log = LogFactory.getLog(this.getClass());
	
	
	
	/**
	 * 
	 * @description 待审核列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 4:50:58 PM
	 */
	public void pendingAuditList(){
		render("pendingAuditList.ftl");
	}
	
	/**
	 * 
	 * @description 有效司机列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 4:51:19 PM
	 */
	public void validList(){
		render("validList.ftl");
	}
	
	/**
	 * 
	 * @description 审核未通过司机列表
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 6:14:41 PM
	 */
	public void invalidList(){
		render("invalidList.ftl");
	}
	
	/**
	 * 
	 * @description 获取待审核列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:01:12 PM
	 */
	public void queryPendingAuditList(){
		query(Driver.STATUS_PENDING_AUDIT);
	}
	
	/**
	 * 
	 * @description 获取审核未通过列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:01:12 PM
	 */
	public void queryInvalidList(){
		query(Driver.STATUS_INVALID);
	}
	
	/**
	 * 
	 * @description 获取有效司机列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:01:12 PM
	 */
	public void queryValidList(){
		try {
			query(Driver.STATUS_VALID);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @description 查询列表 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 15, 2013 11:56:59 PM
	 */
	private void query(int status){
		StringBuffer from = new StringBuffer();
		from.append("from logistics_driver a left join logistics_dict b on a.car_type = b.id  where a.status = " + status ) ;
		from.append(createAnd("a.phone:phone|a.name:name|a.id_card:id_card|a.plate_number:plate_number|a.car_type:car_type|a.recommender:recommender"));
		from.append(createOrder()) ;
		
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(),
				"select a.* , b.name as car_type_name ", from.toString() );
		Dict.fillDictToRecords(page.getList());
		for(Record r : page.getList()){
			String mainRoute = "";
			mainRoute += r.getStr("start_province_str") == null ? "" : r.getStr("start_province_str") ;
			mainRoute += r.getStr("start_city_str") == null ? "" : r.getStr("start_city_str") ;
			mainRoute += "--";
			mainRoute += r.getStr("end_province_str") == null ? "" : r.getStr("end_province_str") ;
			mainRoute += r.getStr("end_city_str") == null ? "" : r.getStr("end_city_str") ;
			r.set("main_route", mainRoute);
		}
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}
	
	/**
	 * 
	 * @description 根据推荐人账号查询 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * 2013年10月13日 上午12:15:33
	 */
	public void queryByRecommenderPage(){
		render("driver_recommender.ftl");
	}
	
	/**
	 * 
	 * @description 根据推荐人账号查询 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * 2013年10月13日 上午12:14:51
	 */
	public void queryByRecommender(){
		StringBuffer from = new StringBuffer();
		from.append("from logistics_driver a left join logistics_dict b on a.car_type = b.id  where a.status = " + Driver.STATUS_VALID + " and a.recommender='" + getUserPhone() + "' order by a.id desc") ;
		
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(),
				"select a.* , b.name as car_type_name ", from.toString() );
		Dict.fillDictToRecords(page.getList());
		for(Record r : page.getList()){
			String mainRoute = "";
			mainRoute += r.getStr("start_province_str") == null ? "" : r.getStr("start_province_str") ;
			mainRoute += r.getStr("start_city_str") == null ? "" : r.getStr("start_city_str") ;
			mainRoute += "--";
			mainRoute += r.getStr("end_province_str") == null ? "" : r.getStr("end_province_str") ;
			mainRoute += r.getStr("end_city_str") == null ? "" : r.getStr("end_city_str") ;
			r.set("main_route", mainRoute);
		}
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}
	
	/**
	 * 
	 * @description 修改记录状态 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:08:24 PM
	 * @param status
	 */
	private void changeStatus(int status){
		int id = getParaToInt();
		Db.update("update logistics_driver set status = ?  where id = ? " , status ,id);
	}
	
	
	/**
	 * 
	 * @description 删除 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:08:32 PM
	 */
	public void delete(){
		changeStatus(Driver.STATUS_DELETED);
	}
	
	/**
	 * 
	 * @description 审核通过 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:09:28 PM
	 */
	public void pass(){
		int id = getParaToInt();
		Driver driver = Driver.dao.findById(id);
		String recommender = driver.getStr("recommender");
		if(StringUtils.hasText(recommender)){
			//推荐人获得5个物流币
			double recommenderUserAward = 5;
			User recommenderUser = User.dao.findFirst("select * from logistics_user where phone = ?" , recommender);
			System.out.println("------->" + recommenderUser);
			if(recommenderUser != null ){
				GoldResult gr = recommenderUser.adjustGold(recommenderUserAward);
				Business.dao.addUserBusiness(recommenderUser.getInt("id"), Business.BUSINESS_TYPE_AWARD, recommenderUserAward, gr.getBeforeGold(), gr.getAfterGold());
				Msg.dao.addUserMsg(Msg.TYPE_BUSINIESS, "推荐朋友获得奖励", "您推荐的朋友" + driver.getStr("name") + "注册成为易运宝会员，您获得奖励：" + recommenderUserAward, recommenderUser.getInt("id"));
			}
			double recommenderDriverAward = 5;
			Driver recommenderDriver = Driver.dao.findFirst("select * from logistics_driver where phone = ?" , recommender);
			if(recommenderDriver != null ){
				GoldResult gr = recommenderDriver.adjustGold(recommenderDriverAward);
				Business.dao.addDriverBusiness(recommenderDriver.getInt("id"), Business.BUSINESS_TYPE_AWARD, recommenderDriverAward, gr.getBeforeGold(), gr.getAfterGold());
				Msg.dao.addDriverMsg(Msg.TYPE_BUSINIESS, "推荐朋友获得奖励", "您推荐的朋友" + driver.getStr("name") + "注册成为易运宝会员，您获得奖励：" + recommenderDriverAward, recommenderDriver.getInt("id"));
			}
		}
		changeStatus(Driver.STATUS_VALID);
	}
	
	/**
	 * 
	 * @description 拒绝通过
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 5:09:36 PM
	 */
	public void reject(){
		changeStatus(Driver.STATUS_INVALID);
	}

	/**
	 * 添加
	 */
	public void add(){
		render("driver_add.ftl");
	}
	
	public void save(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord() ;
			String password = MD5Util.MD5(record.getStr("password"));
			
			record.set("password", password);
			record.set("regist_time", new Date());
			record.set("score1", 5);//初始化评分
			record.set("score2", 5);//初始化评分
			record.set("score3", 5);//初始化评分
			record.set("score", 5);//初始化评分
			record.set("gold", 0);//物流币为0
			record.set("total_deal", 0);//历史交易总金额为0
			record.set("status", Driver.STATUS_VALID);//0 待审核,1-已审核,直接就审核通过
			record.set("modified", new Date());//线路修改时间

			String sql = "select count(1) as count_num from logistics_user where phone = ?" ;
			String sqlDriver = "select count(1) as count_num from logistics_driver where phone = ?" ;
			long count = User.dao.findFirst(sql, record.getStr("phone")).getLong("count_num") + Driver.dao.findFirst(sqlDriver , record.getStr("phone")).getLong("count_num");
			if(count > 0){
				renderJson(JSONUtils.toMsgJSONString("手机号已被注册了，请换一个号码", false));
				return;
			}
			else{
				Db.save("logistics_driver", record);

				//记录全局用户
				Record globalUser = new Record();
				globalUser.set("uid", "d" + record.getLong("id"));
				globalUser.set("password", password);
				globalUser.set("user_type", 1);//0-货主,1-司机
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
	
	public void exportExcel() throws IOException {
		StringBuffer from = new StringBuffer();
		from.append("from logistics_driver where status = 1 ") ;
		from.append(createOrder()) ;
		Page<Record> page = Db.paginate(getPageIndex(), 100000, "select * ", from.toString());
		List<Record> list = page.getList();
		Dict.fillDictToRecords(page.getList());

		String headers = "编号|注册电话|姓名|推荐人|车牌号|身份证号|车型|车长|载重|物流币|注册时间|随车手机|主营省（起）|主营市（起）|主营省（止）|主营市（止）";
		String attributes = "id|phone|name|recommender|plate_number|id_card|car_type_str|car_length|car_weight|gold|regist_time|car_phone|start_province_str|start_city_str|end_province_str|end_city_str";

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
		String filename = TimeUtil.format(new Date(), "'司机导出'yyyyMMdd_HHmmss'.xls'");
		resp.addHeader("Content-Disposition","attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));
		ServletOutputStream out = resp.getOutputStream();
		wb.write(out);
		out.close();
		renderNull();
	}
	
	/**
	 * 
	 * @description 导出待审核司机 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * 2013年8月6日 下午11:47:19
	 * @throws IOException
	 */
	public void exportPendingAudit() throws IOException {
		StringBuffer from = new StringBuffer();
		from.append("from logistics_driver where status = 0 ") ;
		from.append(createOrder()) ;
		Page<Record> page = Db.paginate(getPageIndex(), 100000, "select * ", from.toString());
		List<Record> list = page.getList();
		Dict.fillDictToRecords(page.getList());
		
		String headers = "编号|注册电话|姓名|推荐人|车牌号|身份证号|车型|车长|载重|物流币|注册时间|随车手机|主营省（起）|主营市（起）|主营省（止）|主营市（止）";
		String attributes = "id|phone|name|recommender|plate_number|id_card|car_type_str|car_length|car_weight|gold|regist_time|car_phone|start_province_str|start_city_str|end_province_str|end_city_str";
		
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
		String filename = TimeUtil.format(new Date(), "'待审核司机导出'yyyyMMdd_HHmmss'.xls'");
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
	
	
	public void edit(){
		int driver_id = getParaToInt();
		Record driver = Db.findById("logistics_driver", driver_id);
		setAttr("driver", driver);
		render("driver_edit.ftl");
	}
	
	public void update(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord();
			Db.update("logistics_driver", record);
			renderJson(JSONUtils.toMsgJSONString("保存成功", true));
		}catch(Exception re){
			re.printStackTrace();
			renderJson(JSONUtils.toMsgJSONString("保存失败，请稍候再试", false));
		}
	}
	
	/**
	 * 批量导入司机
	 */
	public void batchDriver(){
		render("uploadDriverFile.ftl");
	}
	
	/**
	 * 上传司机文件
	 */
	public void uploadDriverFile(){
		  String savePath=JFinal.me().getServletContext().getRealPath("/")+"driver_upload";
		  UploadFile  upFile = getFile("file", savePath);
		  String path = savePath.replace('\\', '/');
		  CookieUtil.addCookie(getResponse(),"filePath", path+"/"+upFile.getFileName());
		  render("uploadDriverFile.ftl");
	}
	public void batchImportDriver(){
		String path = CookieUtil.getCookie(getRequest(), "filePath");
		CookieUtil.clearCookie(getRequest(), getResponse(),"filePath");
		String message = batchSaveDriver(path);
		renderJson(message);
	}
	public String batchSaveDriver(String filePath){
		//读取出excel文件的数据
		List<String[]> field = new ExcelUtil().readXls(filePath);
		if(field.size() > 0){
			Driver driver;
			Area area;
			for (int i = 0;i<field.size();i++) {
				driver = new Driver();
				String[] str = field.get(i);
				long count  = 0;
				//获取地址对应的id
				initDriverArea(driver, "end_province", str[3]);
				if(str[4] != null && !"".equals(str[4].trim())){
					initDriverArea(driver, "end_city", str[4]);
				}
				initDriverArea(driver, "end_province2", str[14]);
				if(str[15] != null && !"".equals(str[15].trim())){
					initDriverArea(driver, "end_city2", str[15]);
				}
				initDriverArea(driver, "end_province3", str[18]);
				if(str[19] != null && !"".equals(str[19].trim())){
					initDriverArea(driver, "end_city3", str[19]);
				}
				initDriverArea(driver, "start_province", str[5]);
				if(str[6] != null && !"".equals(str[6].trim())){
					area = Area.getAreaByName(str[6]);
					initDriverArea(driver, "start_city", str[6]);
				}
				initDriverArea(driver, "start_province2", str[12]);
				if(str[13] != null && !"".equals(str[13].trim())){
					initDriverArea(driver, "start_city2", str[13]);
				}
				initDriverArea(driver, "start_province3", str[16]);
				if(str[17] != null && !"".equals(str[17].trim())){
					initDriverArea(driver, "start_city3", str[17]);
				}
				String sql;
				//判断电话号码是否重复
				if((str[2] != null && !"".equals(str[2])) || (str[20] != null && !"".equals(str[20]))){
					sql = "select count(1) as count_num from logistics_driver where phone in (?,?) or owner_phone in (?,?)";
					count += Db.findFirst(sql,str[2],str[20],str[2],str[20]).getLong("count_num");
					if(count > 0){
						 //JSONUtils.toMsgJSONString("手机号码或者随车号码已被注册！erro in "+(i+1)+"行", false);
						 continue;
					}
				}
				//身份证是否重复
				if(str[10] != null && !"".equals(str[10].trim())){
					sql = "select count(1) as count_num from logistics_driver where id_card = ?" ;
					count += Db.findFirst(sql,str[10]).getLong("count_num");
					if(count > 0){
						 //JSONUtils.toMsgJSONString("身份证号已经被使用！erro in "+(i+1)+"行", false);
						 continue;
					}
				}
				//车牌号码是否重复
				if(str[0] != null && !"".equals(str[0].trim())){
					sql = "select count(1) as count_num from logistics_driver where plate_number = ?" ;
					count += Db.findFirst(sql,str[0]).getLong("count_num");
					if(count > 0){
						 //JSONUtils.toMsgJSONString("车牌号码已经被使用！erro in "+(i+1)+"行", false);
						 continue;
					}
				}
				if(str[7] != null && !"".equals(str[7].trim())){
					sql = "select id from logistics_dict where name=? and dict_type = 'car_type'";
					Record r = Db.findFirst(sql, str[7]);
					long carTypeId = r != null ? r.getNumber("id").longValue():0;
					driver.set("car_type", carTypeId);
				}
				if(str[8] != null && !"".equals(str[8].trim())){
					String length = str[8];
					if(str[8].indexOf("米") != -1){
						length = str[8].replace("米", "");
					}
					driver.set("car_length",length);
				}
				if(str[9] != null && !"".equals(str[9].trim())){
					String weight = str[9];
					if(str[9].indexOf("吨") != -1){
						weight = str[9].replace("吨", "");
					}
					driver.set("car_weight",weight);
				}
				driver.set("plate_number", str[0]).
				set("name", str[1]).
				set("password",MD5Util.MD5("666666")).
				set("phone", str[2]).
				set("id_card", str[10]).
				set("license", str[11]).
				set("owner_phone", str[20]);
				driver.save();
			}
		}
		return JSONUtils.toMsgJSONString("司机导入成功！",true);
	}
	//查询路线的id
	public void initDriverArea(Driver driver,String where,String areaName){
		Area area;
		if(areaName != null && !"".equals(areaName.trim())){
			area = Area.getAreaByName(areaName);
			long id = area != null?area.getNumber("id").longValue():0;
			driver.set(where, id);
		}
	}
	
}
