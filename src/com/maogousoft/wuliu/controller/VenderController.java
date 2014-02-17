package com.maogousoft.wuliu.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.TimeUtil;
import com.maogousoft.wuliu.common.utils.WuliuStringUtils;
import com.maogousoft.wuliu.domain.Dict;

/**
 * 商家
 */
public class VenderController extends BaseController{

	private static final Logger log = LoggerFactory.getLogger(VenderController.class);

	public void index(){
		render("vender_index.ftl");
	}

	/**
	 * 查询列表
	 */
	public void query(){
		StringBuffer from = new StringBuffer();
		from.append("from logistics_vender where status != -1 ") ;
		from.append(createAnd("category|vender_name|vender_province|vender_city|vender_district|contact|vender_mobile|vender_phone|goods_name|normal_price"));
		from.append(createOrder()) ;

		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), "select * ", from.toString());
		Dict.fillDictToRecords(page.getList());
		Dict.fillDictToRecords(page.getList(), "vender_category", "category", "category");
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 * 添加
	 */
	public void add(){
		render("vender_add.ftl");
	}

	public void save(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord() ;
			record.set("read_time", 0);
			record.set("status", 0);
			record.remove("id");
			Db.save("logistics_vender", record);
			renderJson(JSONUtils.toMsgJSONString("保存商户成功", true));
		}catch(Exception re){
			log.error("保存商户失败" , re);
			renderJson(JSONUtils.toMsgJSONString("保存商户失败，请稍候再试", false));
		}
	}

	/**
	 * 修改
	 */
	public void edit(){
		int vender_id = getParaToInt();
		Record vender = Db.findById("logistics_vender", vender_id);
		Dict.fillDictToRecord(vender);
		setAttr("vender", vender);
		render("vender_edit.ftl");
	}

	public void update(){
		try{
			MiniData data = getMiniData();
			Record record = data.getRecord();
			record.set("status", 0);
			Db.update("logistics_vender", record);
			renderJson(JSONUtils.toMsgJSONString("保存商户成功", true));
		}catch(Exception re){
			log.error("保存商户失败" , re);
			renderJson(JSONUtils.toMsgJSONString("保存商户失败，请稍候再试", false));
		}
	}

	/**
	 *
	 * 删除
	 */
	public void delete(){
		int id = getParaToInt();
		Db.update("update logistics_vender set status = -1 where id = ? " , id);
	}

	public void exportExcel() throws IOException {
		StringBuffer from = new StringBuffer();
		from.append("from logistics_vender where status != -1 ") ;
		from.append(createAnd("category|vender_name|vender_province|vender_city|vender_district|contact|vender_mobile|vender_phone|goods_name|normal_price"));
		from.append(createOrder()) ;
		Page<Record> page = Db.paginate(getPageIndex(), 100000, "select * ", from.toString());
		List<Record> list = page.getList();
		Dict.fillDictToRecords(page.getList());
		Dict.fillDictToRecords(list, "vender_category", "category", "category");

		String headers = "编号|商家名称|分类|省|市|区县|商家地址|联系人|联系手机|联系电话|商品名称|正常价格|会员特惠|其他|评分|照片1|照片2|照片3|照片4|照片5|经度|维度";
		String attributes = "id|vender_name|category|vender_province_str|vender_city_str|vender_district_str|vender_address|contact|vender_mobile|vender_phone|goods_name|normal_price|member_price|other|score|photo1|photo2|photo3|photo4|photo5|longitude|latitude";

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
		String filename = TimeUtil.format(new Date(), "'联盟商家导出'yyyyMMdd_HHmmss'.xls'");
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
