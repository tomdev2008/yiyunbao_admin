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

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.BusinessException;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.common.utils.TimeUtil;
import com.maogousoft.wuliu.common.utils.WuliuStringUtils;
import com.maogousoft.wuliu.domain.Dict;

/**
 * @author yangfan(kenny0x00@gmail.com) 2013-6-16 下午9:34:15
 */
public class InsuranceController extends BaseController {

	public void index() {
		render("insurance_index.ftl");
	}

	public void list(){
		String status = getPara("status");
		String from = "from logistics_insure a left join logistics_dict b on a.package_type = b.id left join insurance_cargo_type c on a.cargo_type1 = c.id  left join insurance_cargo_type d on a.cargo_type2 = d.id where 1=1  " ;
		if(StringUtils.isNotBlank(status)){
			from += " and a.status = " + status ;
		}
		from += " order by id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), "select a.*,b.name as package_name , c.name as cargo_name1, d.name as cargo_name2 ", from);
//		Dict.fillDictToRecords(page.getList());
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page, "id|insurer_name|insured_name|insurer_phone|insured_phone|shiping_number|cargo_desc|packet_number|ship_type|ship_tool|plate_number|start_area|end_area|start_date|insurance_type|amount_covered|sign_time|insurance_charge|create_time|create_user|package_name|cargo_name1|cargo_name2|receipt_title|ratio|status"));
	}

	public void viewInsure() {
		int id = getParaToInt(0, -1);
		if(id == -1) {
			throw new BusinessException("保单号不能为空.");
		}
		Record record = Db.findById("logistics_insure", id);
		Dict.fillDictToRecord(record);
		setAttr("insure", record);
		render("viewInsure.ftl");
	}
	
	/**
	 * 
	 * @description 标记为已处理 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * 2013年9月7日 下午10:47:04
	 */
	public void deal() {
		int id = getParaToInt(0, -1);
		if(id == -1) {
			throw new BusinessException("保单号不能为空.");
		}
		Db.update("update logistics_insure set status = 1 where id = ?" , id) ;
	}

	public void exportExcel() throws IOException {
		String from = "from logistics_insure a left join logistics_dict b on a.package_type = b.id left join insurance_cargo_type c on a.cargo_type1 = c.id  left join insurance_cargo_type d on a.cargo_type2 = d.id   " ;
		from += " order by id desc";
		Page<Record> page = Db.paginate(getPageIndex(), 100000, "select a.*,b.name as package_name , c.name as cargo_name1, d.name as cargo_name2 ", from);
		List<Record> list = page.getList();
		Dict.fillDictToRecords(page.getList());
		for(Record record : list){
			if(record.getInt("status") == 0){
				record.set("status_str", "未处理");
			}
			else{
				record.set("status_str", "已处理");
			}
		}
		String headers = "状态|编号|投保人|投保电话|被保险人|被保险人电话|运单号|货物名称|件数/重量|运输方式|运输工具|车牌号|起运地|目的地|起运日期|险种|保险金额|签单日期|保险费|保险费率|提交时间|提交人|包装类型|货物类型（大类）|货物类型（小类）|发票抬头";
		String attributes = "status_str|id|insurer_name|insured_name|insurer_phone|insured_phone|shiping_number|cargo_desc|packet_number|ship_type|ship_tool|plate_number|start_area|end_area|start_date|insurance_type|amount_covered|sign_time|insurance_charge|ratio|create_time|create_user|package_name|cargo_name1|cargo_name2|receipt_title";

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
		String filename = TimeUtil.format(new Date(), "'保单导出'yyyyMMdd_HHmmss'.xls'");
		resp.addHeader("Content-Disposition","attachment;filename="+ new String(filename.getBytes("GBK"), "ISO-8859-1"));
		ServletOutputStream out = resp.getOutputStream();
		wb.write(out);
		out.close();
		renderNull();
	}

	private Object getValue(Record record, String attr) {
		Object value = record.get(attr, "");
		if(attr.equals("insurance_type")) {
			final int insurance_type = NumberUtils.toInt(String.valueOf(value), 1);
			if(insurance_type == 1) {
				value = "基本险";
			}else if(insurance_type == 2) {
				value = "综合险";
			}else if(insurance_type == 3) {
				value = "综合险附加被盗险";
			}else {
				value = "未知险种:" + value;
			}
		}
		return value;
	}

}
