/**
 * @filename OrderController.java
 */
package com.maogousoft.wuliu.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.maogousoft.wuliu.common.BaseController;
import com.maogousoft.wuliu.common.BusinessException;
import com.maogousoft.wuliu.common.utils.ExcelUtil;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Area;
import com.maogousoft.wuliu.domain.Business;
import com.maogousoft.wuliu.domain.Dict;
import com.maogousoft.wuliu.domain.Driver;
import com.maogousoft.wuliu.domain.GoldResult;
import com.maogousoft.wuliu.domain.Msg;
import com.maogousoft.wuliu.domain.Order;
import com.maogousoft.wuliu.domain.OrderLog;
import com.maogousoft.wuliu.domain.OrderVie;
import com.maogousoft.wuliu.domain.User;
import com.maogousoft.wuliu.service.PushService2;

/**
 * @description 订单
 * @author shevliu
 * @email shevliu@gmail.com Mar 17, 2013 7:03:19 PM
 */
public class OrderController extends BaseController {

	private static final Log log = LogFactory.getLog(OrderController.class);

	/**
	 *
	 * @description 已创建列表
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 18, 2013 10:25:54 PM
	 */
	public void pendingAuditList() {
		render("pendingAuditList.ftl");
	}

	/**
	 * 审核通过列表
	 *
	 * @author cdliupengfei
	 * @date 2013-3-21
	 */
	public void passList() {
		render("passList.ftl");
	}
	
	/**
	 * 快速导入货源（Exce）
	 */
	public void impFile(){
		render("impFile.ftl");
	}
	
	/**
	 * 上传excel，读取excel内容，将excel内容插入数据库
	 * @throws Exception 
	 */
	public void fileUpload(){
		
		//1.上传文件到指定路径
		String saveDirectoryUrl = "/usr/src/uploadFiles/"; 
		UploadFile file = this.getFile("Fdata", saveDirectoryUrl, 200*1024*1024, "utf-8");
		
		//2.读取上传的文件内容，并保存数据库
		ExcelUtil util = new ExcelUtil();
		
		String fileName = file.getSaveDirectory()+file.getFileName();
		Order order = null;
		boolean isSuccess = false;
		
		List<String[]> lists = util.readXls(fileName);
		for(String[] strs : lists){
			order = new Order();
			for(int i = 0 ; i<strs.length ; i++)
			{
				String value = strs[i];
				//获取第2列：货主电话
				if(i==1){
					Record record = Db.findFirst("select * from logistics_user where phone = ? and status=0" , value );
					if(record == null){
						//break;
					}else{
						int userId = record.getInt("id");
						order.set("user_id", userId);
					}
				}
				//获取第3列：出发地（省）
				if(i==2){
					String sql = "select id,pid,name,short_name,deep,status from logistics_area where short_name =? and status=0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("start_province", null);
					}else{
						Integer start_province = record.getInt("id");
						order.set("start_province", start_province);
					}
				}
				//获取第4列：出发地（市）
				if(i==3){
					String sql = "select id,pid,name,short_name,deep,status from logistics_area where short_name =? and status=0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("start_city", -1);
					}else{
						Integer start_city = record.getInt("id");
						order.set("start_city", start_city);
					}
				}
				//获取第5列：出发地(区县)
				if(i==4){
					String sql = "select id,pid,name,short_name,deep,status from logistics_area where short_name =? and status=0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("start_district", -1);
					}else{
						Integer start_district = record.getInt("id");
						order.set("start_district", start_district);
					}
				}
				//获取第6列：目的地(省)
				if(i==5){
					String sql = "select id,pid,name,short_name,deep,status from logistics_area where short_name =? and status=0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("end_province", null);
					}else{
						Integer end_province = record.getInt("id");
						order.set("end_province", end_province);
					}
				}
				//获取第7列：目的地(市)
				if(i==6){
					String sql = "select id,pid,name,short_name,deep,status from logistics_area where short_name =? and status=0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("end_city", -1);
					}else{
						Integer end_city = record.getInt("id");
						order.set("end_city", end_city);
					}
				}
				//获取第8列：目的地(区县)
				if(i==7){
					String sql = "select id,pid,name,short_name,deep,status from logistics_area where short_name =? and status=0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("end_district", -1);
					}else{
						Integer end_district = record.getInt("id");
						order.set("end_district", end_district);
					}
				}
				//获取第9列：订单有效期
				if(i==8){
					if(null != value && !"".equals(value.trim())){						
						order.set("validate_time", value);
					}else{
						Date date = new Date();
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						c.add(Calendar.MINUTE, 10*60);//当前时间+10小时
						Date time = c.getTime();
						order.set("validate_time",time);
					}
				}
				//获取第10列：货物类型
				if(i==9){
					String sql = "select * from logistics_dict t where t.dict_type = 'cargo_type' and t.name = ? and t.status = 0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("cargo_type", 0);
					}else{
						Integer cargo_type = record.getInt("id");
						order.set("cargo_type", cargo_type);
					}
				}
				//获取第11列：货物数量
				if(i==10){
					//如果货物数量为空，则默认数量为[1]单位为[车]
					if("".equals(value.trim()) || null == value){
						order.set("cargo_number",0);//数量
						order.set("cargo_unit", 2);//单位
					}else{
						String[] values = value.split("/");
						String cargo_number = values[0];//数量
						String unit = values[1];//单位
						int cargo_unit = 2;
						if("车".equals(unit.trim())){
							cargo_unit = 1;
						}else if("吨".equals(unit.trim())){
							cargo_unit = 2;
						}else if("方".equals(unit.trim())){
							cargo_unit = 3;
						}
						order.set("cargo_number",cargo_number);
						order.set("cargo_unit", cargo_unit);
					}
				}
				//获取第12列：货物照片
				if(i == 11){
//					order.set("cargo_photo1", value);
				}
				//获取第13列：货物描述
				if(i == 12){
					order.set("cargo_desc", value);
				}
				//获取第14列：车型
				if(i == 13){
					String sql = "select * from logistics_dict t where t.dict_type = 'car_type' and t.name = ? and t.status = 0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("car_type", 0);
					}else{
						Integer car_type = record.getInt("id");
						order.set("car_type", car_type);
					}
				}
				//获取第15列：车长
				if(i == 14){
					if(null != value && !"".equals(value.trim())){
						order.set("car_length",value);
					}				
				}
				//获取第16列：上下车方式
				if(i == 15){
					String sql = "select * from logistics_dict t where t.dict_type = 'disburden' and t.name = ? and t.status = 0";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						order.set("disburden_type", 0);
					}else{
						Integer disburden_type = record.getInt("id");
						order.set("disburden_type", disburden_type);
					}
				}
				//获取第17列：预期装货时间
				if(i == 16){
					if(null != value && !"".equals(value.trim())){
						order.set("loading_time", value);
					}
				}
				//获取第18列：补充说明
				if(i == 17){
					order.set("cargo_remark", value);
				}
				//获取第19列：价格(运费总价)
				if(i == 18){
					if(null != value && !"".equals(value.trim())){
						order.set("price",value);
					}else{
						order.set("price",0);
					}
				}
				//获取第20列：货主保证金
				if(i == 19){
					if(null != value && !"".equals(value.trim())){
						order.set("user_bond",value);
					}
				}
				//获取第21列：司机保证金
				if(i == 20){
					if(null != value && !"".equals(value.trim())){
						order.set("driver_bond",value);
					}
				}
				//获取第22列：中标司机
				if(i == 21){
					String sql = "select * from logistics_driver t where t.id = ? ";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						//break;
					}else{
						Integer driver_id = record.getInt("id");
						order.set("driver_id", driver_id);
					}
				}
				//获取第23列：中标司机手机号码
				if(i == 22){
					String sql = "select * from logistics_driver t where t.phone = ? ";
					Record record = Db.findFirst(sql , value );
					if(record == null){
						//break;
					}else{
//						Integer driver_phone = record.getInt("id");
//						order.set("driver_phone", driver_phone);
					}
				}
				//获取第24列：发布时间
				if(i == 23){
					order.set("create_time", new Date());
				}
				//获取第25列：成交时间
				if(i == 24){
					//order.set("deal_time", value);
				}
				//获取第26列：联系人
				if(i == 25){
					if(null != value && !"".equals(value)){
						order.set("cargo_user_name", value);
					}
				}
				//获取第27列：联系人电话
				if(i == 26){
					if(null != value && !"".equals(value)){
						order.set("cargo_user_phone", value);
					}
				}
			}
			//回单密码默认为1
			order.set("receipt_password", 1);
			//发布货源状态为：0-已创建
			order.set("status", 0);
			
			isSuccess = order.save();
		}
		
		//3.删除上传成功的文件
		file.getFile().delete();
		
		if(isSuccess){
			renderJson(JSONUtils.toMsgJSONString("上传成功", true));			
			render("pendingAuditList.ftl");
		}else{
			renderJson(JSONUtils.toMsgJSONString("上传失败", false));			
			render("impFile.ftl");
		}
		
	}
	
	/**
	 * 审核未通过列表
	 *
	 * @author cdliupengfei
	 * @date 2013-3-21
	 */
	public void rejectList() {
		render("rejectList.ftl");
	}

	/**
	 * 进行中订单列表
	 *
	 * @author cdliupengfei
	 * @date 2013-3-21
	 */
	public void dealList() {
		render("dealList.ftl");
	}

	/**
	 *
	 * @description 已取消列表
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 9:07:39 PM
	 */
	public void cancelList() {
		render("cancelList.ftl");
	}

	/**
	 *
	 * @description 已完成列表
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 9:07:56 PM
	 */
	public void finishList() {
		render("finishList.ftl");
	}

	/**
	 *
	 * @description 已过期列表
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 29, 2013 11:34:29 PM
	 */
	public void expiredList() {
		render("expiredList.ftl");
	}

	public void query() {

		String beginDate = getPara("beginDate");
		String endDate = getPara("endDate");
		String startPoint = getPara("start_point");
		String endPoint = getPara("end_point");
		int status = getParaToInt("status");

		String select = "select a.* , b.name as cargo_type_name , c.name as car_type_name ,d.name as start_province_name , "
				+ "e.name as start_city_name , f.name as start_district_name,g.name as end_province_name , h.name as end_city_name , "
				+ "i.name as end_district_name ,j.name as disburden_type_name ,k.phone as driver_phone ,k.name as driver_name ,"
				+ "m.phone as user_phone, m.name as user_name";

		StringBuffer from = new StringBuffer();
		from.append(" from logistics_order a ");
		from.append(" left join logistics_dict b on a.cargo_type = b.id ");
		from.append(" left join logistics_dict c on a.car_type = c.id");
		from.append(" left join logistics_area d on a.start_province = d.id ");
		from.append(" left join logistics_area e on a.start_city = e.id ");
		from.append(" left join logistics_area f on a.start_district = f.id");
		from.append(" left join logistics_area g on a.end_province = g.id ");
		from.append(" left join logistics_area h on a.end_city = h.id ");
		from.append(" left join logistics_area i on a.end_district = i.id");
		from.append(" left join logistics_dict j on a.disburden_type = j.id ");
		from.append(" left join logistics_driver k on a.driver_id = k.id ");
		from.append(" left join logistics_user m on a.user_id = m.id ");
		from.append(" where a.status!=-1 ");
		from.append(createAndUsingInt("a.car_type:car_type"));
		from.append(createAndUsingInt("a.cargo_type:cargo_type"));
		from.append(createAnd("m.phone:user_phone"));
		from.append(createAnd("m.name:user_name"));
		if (StringUtils.isNotBlank(beginDate)) {
			from.append(" and a.create_time >= '" + beginDate + "'");
		}
		if (StringUtils.isNotBlank(endDate)) {
			endDate += " 23:59:59";
			from.append(" and a.create_time <= '" + endDate + "'");
		}
		if (StringUtils.isNotBlank(startPoint)) {
			from.append(" and (a.start_province = " + startPoint
					+ " or a.start_city = " + startPoint
					+ " or a.start_district = " + startPoint + ") ");
		}
		if (StringUtils.isNotBlank(endPoint)) {
			from.append(" and (a.end_province = " + endPoint
					+ " or a.end_city = " + endPoint + " or a.end_district = "
					+ endPoint + ") ");
		}
		// 查询已过期订单，特殊处理
		if (status == Order.STATUS_EXPIRED) {
			from.append(" and a.validate_time < now() and a.status  = "
					+ Order.STATUS_PASS);
		} else {
			from.append(" and a.status  = " + status);
		}
		// 如果查询状态为抢单中，则加入有效期条件，不能小于当前时间
		if (status == Order.STATUS_PASS) {
			from.append(" and a.validate_time > now() ");
		}
		from.append(createOrder("a"));

		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select,
				from.toString());
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * @description 获取所有订单状态
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 18, 2013 10:06:13 PM
	 */
	public void showStatus() {
		try {
			renderText(JSONUtils.toJSONArray(Order.getAllStatus(),
					"status|text").toString());
		} catch (JSONException e) {
			log.error(e);
		}
	}

	/**
	 *
	 * @description 修改记录状态
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 17, 2013 5:08:24 PM
	 * @param status
	 */
	private void changeStatus(int status) {
		String ids = getPara();
		String sql = "update logistics_order set status = "+status+" where id in("+ids+")";
		Db.update(sql);
	}

	/**
	 *
	 * @description 审核通过
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 8:53:07 PM
	 */
	@Before(Tx.class)
	public void pass() {
		changeStatus(Order.STATUS_PASS);

		// 推送消息给司机
		String ids = getPara();
		String[] orderIds = ids.split(",");
		for(int i=0;i<orderIds.length;i++){
			Order order = Order.dao.findById(orderIds[i]);
			int userId = order.getInt("user_id");
			User orderOwner = User.dao.loadUserById(userId);
			Dict.fillDictToModel(order);
			order.put("score1", orderOwner.getDouble("score1"));
			order.put("score2", orderOwner.getDouble("score2"));
			order.put("score3", orderOwner.getDouble("score3"));
			order.put("score", orderOwner.getDouble("score"));
			order.remove("status");
			
			JSONObject json = new JSONObject();
			try {
				json.put("msg_type", 5);//5-新货源消息
				json.put("id", order.get("id"));
				json.put("cargo_desc", order.get("cargo_desc"));
				json.put("cargo_number", order.get("cargo_number"));
				json.put("cargo_unit", order.get("cargo_unit"));
				json.put("cargo_unit_name", order.get("cargo_unit_name"));
				json.put("validate_time", order.get("validate_time"));
			} catch (JSONException e) {
				log.error(e.getMessage(),e);
			}
			int[] driverIds = getDriversForPush(order);
			log.info("推送新货单消息给以下司机:" + Arrays.toString(driverIds));
//			PushService.pushSysMsgByDriverId(driverIds, json, 5);//5-新货源消息
			PushService2.pushSysMsgByDriverId(driverIds, json.toString());

			Db.update("update logistics_order set push_drvier_count=? where id=?",
					driverIds.length, orderIds[i]);// 更新已推送人数

			// 记录订单变更情况
			OrderLog.logOrder(Integer.parseInt(orderIds[i]), "管理员", "订单审核通过,并自动推送给" + driverIds.length
					+ "位司机.");

			// 添加货主信息
			Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单审核通过", "订单审核通过，订单编号:" + orderIds[i],
					userId);

			renderJson(JSONUtils.toMsgJSONString("审核通过", true));
		}
	}

	private int[] getDriversForPush(Order order) {
		int start_province = order.getInt("start_province");
		int start_city = order.getInt("start_city");
		int end_province = order.getInt("end_province");
		int end_city = order.getInt("end_city");

		List<Integer> idList;
		// 不同省之间
//		if (start_province != end_province) {
			String start = getRelatedProvinces(start_province);
			String end = getRelatedProvinces(end_province);
			// 以省为单位进行信息推送
			String sql = "select id from logistics_driver where (start_province in("
					+ start + ") and end_province in (" + end + "))";
			sql += " or (start_province2 in(" + start
					+ ") and end_province2 in (" + end + "))";
			sql += " or (start_province3 in(" + start
					+ ") and end_province3 in (" + end + "))";
			sql += " or (start_province in(" + end + ") and end_province in ("
					+ start + "))";
			sql += " or (start_province2 in(" + end
					+ ") and end_province2 in (" + start + "))";
			sql += " or (start_province3 in(" + end
					+ ") and end_province3 in (" + start + "))";
			// sql += "order by RAND() limit 50";//随机挑选本省50条记录
			idList = Db.query(sql);
//		} else {// 省内的精确到城市
//			String sql = "select id from logistics_driver where (start_city=? and end_city=?) or (end_city=? and start_city=?)";
//			idList = Db.query(sql, start_city, end_city, start_city, end_city);
//		}
		return ArrayUtils.toPrimitive(idList.toArray(new Integer[0]));

		// if(start_city != -1 && end_city != -1) {
		// String sql = "select id from logistics_driver where start_city=? and
		// end_city=? order by RAND() limit 50";
		// idList = Db.query(sql, start_city, end_city);
		// }else {
		// String sql = "select id from logistics_driver where start_province=?
		// and end_province=? order by RAND() limit 50";//随机挑选本省50条记录
		// idList = Db.query(sql, start_province, end_province);
		// }
	}

	/**
	 * 地区进行分组，获取相关的地区 江苏、浙江、上海 北京、天津、河北
	 *
	 * @param province_id
	 * @return
	 */
	private String getRelatedProvinces(int province_id) {
		String start = province_id + "";
		if (province_id == Area.ID_Beijing || province_id == Area.ID_TianJin
				|| province_id == Area.ID_HeBei) {
			start = Area.ID_Beijing + "," + Area.ID_TianJin + ","
					+ Area.ID_HeBei;
		}
		if (province_id == Area.ID_ShangHai || province_id == Area.ID_JiangShu
				|| province_id == Area.ID_ZheJiang) {
			start = Area.ID_ShangHai + "," + Area.ID_JiangShu + ","
					+ Area.ID_ZheJiang;
		}
		return start;
	}

	/**
	 *
	 * @description 拒绝审核
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 8:54:08 PM
	 */
	public void reject() {
		changeStatus(Order.STATUS_REJECT);
	}

	/**
	 *
	 * @description 删除(暂未使用)
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 9:03:47 PM
	 */
	public void delete() {
		changeStatus(Order.STATUS_DELETED);
	}

	/**
	 * 取消货单
	 */
	@Before(Tx.class)
	public void cancel_order() {
		int orderId = getParaToInt("order_id");

		Order order = Order.dao.loadOrder(orderId);

		if (!order.isStauts(Order.STATUS_PASS)) {
			throw new BusinessException("订单" + orderId + "不在待定状态，无法取消.");
		}
		final double user_bond = order.getDouble("user_bond");
		final double fee = order.getFee();

		// 获取抢单司机
		List<OrderVie> vieList = OrderVie.dao.getActiveVieListByOrder(orderId);

		if (vieList.isEmpty()) {// 没有抢单，直接取消订单
			// 取消订单
			order.set("status", Order.STATUS_CANCEL);
			order.update();

			User user = User.dao.loadUserById(getUserId());
			GoldResult gr = user.adjustGold(user_bond);// 解冻保证金
			user.update();

			Business.dao.addUserBusiness(getUserId(),
					Business.BUSINESS_TYPE_DEPOSIT_RETURN, user_bond, gr
							.getBeforeGold(), gr.getAfterGold());
			Msg.dao.addUserMsg(Msg.TYPE_BUSINIESS, "订单保证金已返还", "订单" + orderId
					+ "已取消，无人抢单，返还保证金：" + user_bond, getUserId());
			Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单取消",
					"订单已经取消，订单编号：" + orderId, getUserId());

			// 记录订单变更
			OrderLog.logOrder(orderId, "管理员", "取消了货单");
		} else {
			if (user_bond <= 0) {// 如果有抢单，且保证金为0，那么由客服取消后,仅返回运费.
				// 取消订单
				order.set("status", Order.STATUS_CANCEL);
				order.update();

				Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单取消", "订单已经取消，订单编号："
						+ orderId, getUserId());
				OrderLog.logOrder(orderId, "管理员", "取消了货单");

				// 更新抢单司机之前的保证金
				for (OrderVie orderVie : vieList) {
					double driver_bond = orderVie.asDoubleValue("driver_bond",
							0);
					int driver_id = orderVie.getInt("driver_id");
					Driver driver = Driver.dao.loadDriverById(driver_id);
					GoldResult gr = driver.adjustGold(driver_bond);

					Business.dao.addDriverBusiness(driver_id,
							Business.BUSINESS_TYPE_DEPOSIT_RETURN, driver_bond,
							gr.getBeforeGold(), gr.getAfterGold());
					Msg.dao.addDriverMsg(Msg.TYPE_BUSINIESS, "保证金已返回",
							"所抢货单已被系统取消，货单编号：" + orderId + ",保证金："
									+ driver_bond + "已 返还", driver_id);
					Msg.dao.addDriverMsg(Msg.TYPE_ORDER, "抢单已取消",
							"所抢货单已被系统取消，货单编号：" + orderId, driver_id);
				}
			} else {// 赔付给司机
				int vieCount = vieList.size();
				final double paid = user_bond / vieCount;// 每位司机赔付的金额
				for (OrderVie orderVie : vieList) {
					// 将货主的赔付金额付给司机
					int driver_id = orderVie.getInt("driver_id");
					Driver driver = Driver.dao.loadDriverById(driver_id);
					GoldResult gr = driver.adjustGold(paid);

					// 返回司机保证金
					final double driver_bond = orderVie.asDoubleValue(
							"driver_bond", 0);
					GoldResult gr2 = driver.adjustGold(driver_bond);

					driver.update();

					Business.dao.addDriverBusiness(driver_id,
							Business.BUSINESS_TYPE_VIOLATE_RETURN, driver_bond,
							gr.getBeforeGold(), gr.getAfterGold());
					Business.dao.addDriverBusiness(driver_id,
							Business.BUSINESS_TYPE_VIOLATE_PAID, paid, gr2
									.getBeforeGold(), gr2.getAfterGold());
					Msg.dao
							.addDriverMsg(Msg.TYPE_BUSINIESS, "保证金奖励",
									"货主违约，奖励保证金:" + paid + ",货单号：" + orderId,
									driver_id);
					Msg.dao.addDriverMsg(Msg.TYPE_ORDER, "货单取消", "货单已经取消，货单号："
							+ orderId, driver_id);
				}
				// 取消订单
				order.set("status", Order.STATUS_CANCEL);
				order.update();

				Msg.dao.addUserMsg(Msg.TYPE_ORDER, "订单取消", "订单已经取消，订单编号："
						+ orderId, getUserId());
				OrderLog.logOrder(orderId, "管理员", "取消了货单");
			}
		}
	}

	/**
	 *
	 * @description 抢单记录页面
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 9:26:41 PM
	 */
	public void vieList() {
		setAttr("orderId", getParaToInt());
		render("order_vie.ftl");
	}

	/**
	 *
	 * @description 获取抢单记录
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 22, 2013 9:17:37 PM
	 */
	public void queryVie() {
		int orderId = getParaToInt();
		String select = "select a.*,b.name as driver_name , b.phone ";
		String from = " from logistics_order_vie a left join logistics_driver  b on a.driver_id = b.id where  a.order_id = "
				+ orderId + " order by a.id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select,
				from);
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * @description 货物状态页面
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 24, 2013 10:25:25 PM
	 */
	public void statusList() {
		setAttr("orderId", getParaToInt());
		render("order_status.ftl");
	}

	/**
	 *
	 * @description 查询货物状态
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 24, 2013 10:29:02 PM
	 */
	public void queryStatus() {
		int orderId = getParaToInt();
		String select = "select * ";
		String from = " from logistics_order_status where order_id = "
				+ orderId + " order by id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select,
				from);
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * @description 司机位置历史记录页面
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 24, 2013 10:39:59 PM
	 */
	public void locationList() {
		setAttr("orderId", getParaToInt());
		render("order_location.ftl");
	}

	/**
	 *
	 * @description
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 24, 2013 10:40:26 PM
	 */
	public void queryLocation() {
		int orderId = getParaToInt();
		String select = "select * ";
		String from = " from logistics_order_location where order_id = "
				+ orderId + " order by id desc";
		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select,
				from);
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * @description 货主订单列表 页面
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 31, 2013 5:52:33 PM
	 */
	public void userOrder() {
		setAttr("userId", getParaToInt());
		render("user_order.ftl");
	}

	/**
	 *
	 * @description 货主订单列表
	 * @author shevliu
	 * @email shevliu@gmail.com Mar 31, 2013 5:52:43 PM
	 */
	public void queryUserOrder() {
		int userId = getParaToInt();
		String select = "select a.* , b.name as cargo_type_name , c.name as car_type_name ,d.name as start_province_name , "
				+ "e.name as start_city_name , f.name as start_district_name,g.name as end_province_name , h.name as end_city_name , "
				+ "i.name as end_district_name ,j.name as disburden_type_name ,k.phone as driver_phone ,k.name as driver_name ,"
				+ "m.phone as user_phone, m.name as user_name";
		StringBuffer from = new StringBuffer();
		from.append(" from logistics_order a ");
		from.append(" left join logistics_dict b on a.cargo_type = b.id ");
		from.append(" left join logistics_dict c on a.car_type = c.id");
		from.append(" left join logistics_area d on a.start_province = d.id ");
		from.append(" left join logistics_area e on a.start_city = e.id ");
		from.append(" left join logistics_area f on a.start_district = f.id");
		from.append(" left join logistics_area g on a.end_province = g.id ");
		from.append(" left join logistics_area h on a.end_city = h.id ");
		from.append(" left join logistics_area i on a.end_district = i.id");
		from.append(" left join logistics_dict j on a.disburden_type = j.id ");
		from.append(" left join logistics_driver k on a.driver_id = k.id ");
		from.append(" left join logistics_user m on a.user_id = m.id ");
		from.append(" where a.status!=-1 and user_id= " + userId);
		from.append(" order by a.id desc");

		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select,
				from.toString());
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * @description 司机订单列表，页面
	 * @author shevliu
	 * @email shevliu@gmail.com Apr 1, 2013 7:52:32 PM
	 */
	public void driverOrder() {
		setAttr("driverId", getParaToInt());
		render("driver_order.ftl");
	}

	/**
	 *
	 * @description 司机订单列表
	 * @author shevliu
	 * @email shevliu@gmail.com Apr 1, 2013 7:49:40 PM
	 */
	public void queryDriverOrder() {
		int driverId = getParaToInt();
		String select = "select a.* , b.name as cargo_type_name , c.name as car_type_name ,d.name as start_province_name , "
				+ "e.name as start_city_name , f.name as start_district_name,g.name as end_province_name , h.name as end_city_name , "
				+ "i.name as end_district_name ,j.name as disburden_type_name ,k.phone as driver_phone ,k.name as driver_name ,"
				+ "m.phone as user_phone, m.name as user_name , n.status as vie_status ";
		StringBuffer from = new StringBuffer();
		from.append(" from logistics_order a ");
		from.append(" left join logistics_dict b on a.cargo_type = b.id ");
		from.append(" left join logistics_dict c on a.car_type = c.id");
		from.append(" left join logistics_area d on a.start_province = d.id ");
		from.append(" left join logistics_area e on a.start_city = e.id ");
		from.append(" left join logistics_area f on a.start_district = f.id");
		from.append(" left join logistics_area g on a.end_province = g.id ");
		from.append(" left join logistics_area h on a.end_city = h.id ");
		from.append(" left join logistics_area i on a.end_district = i.id");
		from.append(" left join logistics_dict j on a.disburden_type = j.id ");
		from.append(" left join logistics_driver k on a.driver_id = k.id ");
		from.append(" left join logistics_user m on a.user_id = m.id ");
		from.append(" left join logistics_order_vie n on a.id = n.order_id ");
		from.append(" where a.status!=-1 and n.driver_id = " + driverId);
		from.append(" order by a.id desc");

		Page<Record> page = Db.paginate(getPageIndex(), getPageSize(), select,
				from.toString());
		renderText(JSONUtils.toPagedGridJSONStringUsingRecord(page));
	}

	/**
	 *
	 * @description 修改描述
	 * @author shevliu
	 * @email shevliu@gmail.com Jun 26, 2013 10:06:24 PM
	 */
	public void editDesc() {
		int orderId = getParaToInt();
		setAttr("order", Order.dao.findById(orderId));
		render("edit_desc.ftl");
	}

	public void updateDesc() {
		try {
			int orderId = getParaToInt("order_id");
			String cargo_remark = getPara("cargo_remark");
			Order order = Order.dao.findById(orderId);
			order.set("cargo_remark", cargo_remark);
			order.update();
			renderJson(JSONUtils.toMsgJSONString("成功", true));
		} catch (RuntimeException re) {
			log.error(re);
			renderJson(JSONUtils.toMsgJSONString(re.getMessage(), false));
		}
	}

	/**
	 *
	 * @description 获取新订单数量
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Jun 28, 2013 7:52:38 PM
	 */
	public void getNewOrderCount(){
		String sql = "select count(0) from logistics_order where status = ?" ;
		long count = Db.queryLong(sql , Order.STATUS_CREATED);
		renderJson(JSONUtils.toMsgJSONString(count + "", true));
	}
}
