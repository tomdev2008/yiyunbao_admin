package com.maogousoft.wuliu.common.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import flexjson.JSONDeserializer;

/**
 * Json操作基础类
 *
 * @author 刘鹏飞
 * @创建日期: Aug 11, 2008 10:31:17 AM
 *
 */
public class JSONUtils {

	public final static Log log = LogFactory.getLog(JSONUtils.class);

	/**
	 * 分页查询组装JSON对象
	 *
	 * @param result
	 * @param attributes
	 * @return String
	 * @throws JSONException
	 * @author 刘鹏飞
	 * @创建日期 Aug 11, 2008 10:32:50 AM
	 */
	@SuppressWarnings("unchecked")
	public static String toPagedGridJSONString(Page<? extends Model> page,
			String attributes) {
		JSONObject oJson = new JSONObject();
		try {
			List<String> attrList = WuliuStringUtils.parseVertical(attributes);

			JSONArray arrayValue = new JSONArray();

			for (Model model : page.getList()) {
				JSONObject obj = new JSONObject();
				for (String attr : attrList) {
					obj.put(attr, model.get(attr));
				}
				arrayValue.put(obj);
			}
			oJson.put("total", page.getTotalRow());
			oJson.put("data", arrayValue);
		} catch (JSONException e) {
			log.error("转换json失败", e);
			throw new RuntimeException("转换json失败", e);
		}
		return oJson.toString();
	}

	/**
	 *
	 * @description 分页查询组装JSON对象
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:42:13 PM
	 * @param page record
	 * @param attributes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String toPagedGridJSONStringUsingRecord(Page<Record> page,
			String attributes) {
		JSONObject oJson = new JSONObject();
		try {
			List<String> attrList = WuliuStringUtils.parseVertical(attributes);
			JSONArray arrayValue = new JSONArray();

			for (Record record : page.getList()) {
				JSONObject obj = new JSONObject();
				for (String attr : attrList) {
					obj.put(attr, record.get(attr));
				}
				arrayValue.put(obj);
			}
			oJson.put("total", page.getTotalRow());
			oJson.put("data", arrayValue);
		} catch (JSONException e) {
			log.error("转换json失败", e);
			throw new RuntimeException("转换json失败", e);
		}
		return oJson.toString();
	}

	public static String toPagedGridJSONStringUsingRecord(Page<Record> page) {
		JSONObject oJson = new JSONObject();
		try {
			JSONArray arrayValue = new JSONArray();

			for (Record record : page.getList()) {
				JSONObject obj = new JSONObject();
				for(Iterator it = record.getColumns().keySet().iterator(); it.hasNext(); ) {
					String key = (String) it.next();
					obj.put(key, record.get(key));
				}
				arrayValue.put(obj);
			}
			oJson.put("total", page.getTotalRow());
			oJson.put("data", arrayValue);
		} catch (JSONException e) {
			log.error("转换json失败", e);
			throw new RuntimeException("转换json失败", e);
		}
		return oJson.toString();
	}

	/**
	 *
	 * @description list转换JSON String
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 15, 2013 9:32:47 PM
	 * @param list
	 * @param attributes
	 */
	public static String toJSONString(List<? extends Model> list, String attributes){
		try {
			List<String> attrList = WuliuStringUtils.parseVertical(attributes);

			JSONArray arrayValue = new JSONArray();

			for (Model model : list) {
				JSONObject obj = new JSONObject();
				for (String attr : attrList) {
					obj.put(attr, model.get(attr));
				}
				arrayValue.put(obj);
			}
			return arrayValue.toString();
		} catch (JSONException e) {
			log.error("转换json失败", e);
			throw new RuntimeException("转换json失败", e);
		}
	}

	public static String toJSONStringUsingRecord(List<Record> list, String attributes){
		try {
			List<String> attrList = WuliuStringUtils.parseVertical(attributes);

			JSONArray arrayValue = new JSONArray();
			for (Record record : list) {
				JSONObject obj = new JSONObject();
				for (String attr : attrList) {
					obj.put(attr, record.get(attr));
				}
				arrayValue.put(obj);
			}
			return arrayValue.toString();
		} catch (JSONException e) {
			log.error("转换json失败", e);
			throw new RuntimeException("转换json失败", e);
		}
	}

	/**
	 *
	 * @description list转换JSONArray
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * @param list
	 * @param attributes
	 * @return JSONArray
	 * @throws JSONException
	 */
	public static JSONArray toJSONArray(List<?> list, String attributes)
			throws JSONException {
		JSONArray arrayValue = new JSONArray();
		if (list == null || list.size() == 0) {
			return arrayValue;
		}
		List<?> attList = WuliuStringUtils.parseVertical(attributes);
		JSONObject oJson;
		String attValue;
		int x = 0;
		for (Iterator<?> i = list.iterator(); i.hasNext();) {
			Object o = i.next();
			oJson = new JSONObject();
			for (int j = 0; j < attList.size(); j++) {
				String attName = attList.get(j).toString();
				attValue = attName;
				if (attName.indexOf(":") > 0) {
					attValue = attName.substring(attName.indexOf(":") + 1);
					attName = attName.substring(0, attName.indexOf(":"));
				}
				Object value = null;
				try {
					value = org.apache.commons.beanutils.BeanUtils.getProperty(
							o, attValue);
				} catch (IllegalArgumentException e) {
					log.warn(e);
				} catch (Exception e) {
					e.printStackTrace();
				}
				oJson.put(attName, value);
			}
			arrayValue.put(x, oJson);
			x++;
		}
		return arrayValue;
	}

	public static Object Decode(String json) {
		try {
			if (StringUtils.isBlank(json))
				return "";
			JSONDeserializer deserializer = new JSONDeserializer();
			// deserializer.use(String.class, new
			// DateTransformer("yyyy-MM-dd'T'HH:mm:ss"));
			Object obj = deserializer.deserialize(json);
			if (obj != null && obj.getClass() == String.class) {
				return Decode(obj.toString());
			}
			return obj;
		} catch (Exception e) {
			log.error("json转换object失败", e);
			throw new RuntimeException("json转换object失败", e);
		}
	}

	/**
	 * 返回消息
	 * @param messageString
	 * @param success
	 * @return String
	 * @throws JSONException
	 * @author 刘鹏飞
	 * @创建日期 Aug 11, 2008 10:32:21 AM
	 */
	public static String toMsgJSONString(String messageString, boolean success){
		JSONObject oJson = new JSONObject();
		try {
			oJson.put("data", messageString);
			oJson.put("success", success);
		} catch (JSONException e) {
			log.error("转换json失败：" ,e);
		}
		return oJson.toString();
	}

	/**
	 * 将model转换为json字符串
	 * @param model
	 * @return
	 */
	public static String toJSONString(Model model) {
		if(model == null) {
			return null;
		}
		JSONObject json = new JSONObject();
		String[] columnNames = model.getAttrNames();
		Arrays.sort(columnNames);
		for (String columnName : columnNames) {
			Object obj = model.get(columnName);
			try {
				if(obj instanceof Date) {
					obj = ((Date)obj).getTime();
				}
				json.put(columnName, obj);
			} catch (JSONException e) {
				log.error(e.getMessage(),e);
			}
		}
		return json.toString();
	}
}
