/**
 * @filename Dict.java
 */
package com.maogousoft.wuliu.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.maogousoft.wuliu.WuliuConstants;
import com.maogousoft.wuliu.common.BusinessException;

/**
 * @description 字典
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 15, 2013 9:02:30 PM
 */
public class Dict extends Model<Dict>{

	private static final Logger log = LoggerFactory.getLogger(Dict.class);

	private static final long serialVersionUID = 2230573683269292447L;

	/**
	 * 车型
	 */
	public static final String TYPE_CAR = "car_type" ;

	/**
	 * 货物类型
	 */
	public static final String TYPE_CARGO = "cargo_type" ;

	/**
	 * 上下车方式
	 */
	public static final String TYPE_ADISBURDEN = "disburden" ;

	/**
	 * 运输方式
	 */
	public static final String TYPE_SHIP_TYPE = "ship_type" ;

	public static final Dict dao = new Dict();

	public static final Map<String, String> dicts = new LinkedHashMap<String, String>();
	public static final Map<Integer, String> CARGO_UNIT_NAME = new LinkedHashMap<Integer, String>();

	static {
		dicts.put("car_type", "车型");
		dicts.put("cargo_type", "货物类型");
		dicts.put("disburden", "装卸方式");
		dicts.put("ship_type", "运输方式");
		dicts.put("vender_category", "联盟商家类别");

		CARGO_UNIT_NAME.put(1, "车");
		CARGO_UNIT_NAME.put(2, "吨");
		CARGO_UNIT_NAME.put(3, "方");
	}

	/**
	 * 获取字典类型名称
	 * @param dictType
	 * @return
	 */
	public static String getDictTypeName(String dictType) {
		return dicts.get(dictType);
	}

	/**
	 * 判断是否是有效的字典类型
	 * @param dictType
	 * @return
	 */
	public static boolean isValidDictType(String dictType) {
		return dicts.containsKey(dictType);
	}

	/**
	 * 验证是否是有效的字典类型
	 * @param dictType
	 */
	public static void verifyDictType(String dictType) {
		if(dictType == null) {
			throw new IllegalArgumentException("dictType can't be null.");
		}
		if(!isValidDictType(dictType)) {
			throw new BusinessException(dictType + "不是有效的字典类型.");
		}
	}

	/**
	 * 根据ID与字典类型获取属性(带缓存)
	 * @param dictType
	 * @param id
	 * @return
	 */
	public static Dict getDictByTypeAndIdWithCache(final String dictType, final Integer id) {
		Dict.verifyDictType(dictType);
		String cacheKey = "dictType_" + id;
		Dict dict = CacheKit.get(WuliuConstants.CACHE_DICT, cacheKey, new IDataLoader() {

			@Override
			public Object load() {
				Dict dict = Dict.dao.findFirst("select id,name,dict_type,dict_desc,status from logistics_dict where status=0 and dict_type=? and id=?", dictType, id);
				return dict;
			}
		});
		return dict;
	}

	/**
	 * 验证字典数据
	 * @param dictType
	 * @param dictId
	 */
	public static void verifyDict(final String dictType, final Integer dictId) {
		if(dictId == null) {
			return;
		}
		Dict dict = Dict.getDictByTypeAndIdWithCache(dictType, dictId);
		if(dict == null) {
			String dictTypeName = Dict.getDictTypeName(dictType);
			throw new BusinessException(dictId + "不是有效的" + dictTypeName + "数据");
		}
	}

	public static void fillAreaById(Record record, String idColumn, String strColumn) {
		final Integer areaId = record.getInt(idColumn);
		if (areaId != null) {
			Area area = Area.getAreaByIdWithCache(areaId);
			if (area != null) {
				String strValue = area.getStr("short_name");
				record.set(strColumn, strValue);
			}else {
				record.set(strColumn, null);
			}
		}
	}

	public static void fillAreaById(Model<?> model, String idColumn, String strColumn) {
		final Integer areaId = model.getInt(idColumn);
		if (areaId != null) {
			Area area = Area.getAreaByIdWithCache(areaId);
			if (area != null) {
				String strValue = area.getStr("short_name");
				model.put(strColumn, strValue);
			}else {
				model.put(strColumn, null);
			}
		}
	}

	public static void fillDict(Record record, final String dictType, String idColumn, String strColumn) {
		final Integer id;
		try {
			id = record.getInt(idColumn);
		}catch(Throwable t) {
			log.error("Can't fillDict '" + dictType + "' to record, columnName:" + idColumn + ",cause:" + t.getMessage());
			return;
		}
		if(id != null) {
			Dict dict = Dict.getDictByTypeAndIdWithCache(dictType, id);
			if(dict != null) {
				String strValue = dict.getStr("name");
				record.set(strColumn, strValue);
			}else {
				record.set(strColumn, null);
			}
		}
	}

	public static void fillDict(Model<?> model, final String dictType, String idColumn, String strColumn) {
		final Integer id = model.getInt(idColumn);
		if(id != null) {
			Dict dict = Dict.getDictByTypeAndIdWithCache(dictType, id);
			if(dict != null) {
				String strValue = dict.getStr("name");
				model.put(strColumn, strValue);
			}else {
				model.put(strColumn, null);
			}
		}
	}

	/**
	 * 向记录中填充各种字典、日期数据
	 * @param list
	 */
	public static void fillDictToRecords(List<Record> list) {
		if(list.isEmpty()) {
			return;
		}
		for (Record record : list) {
			fillDictToRecord(record);
		}
	}
	public static void fillDictToRecords(List<Record> list, final String dictType, String idColumn, String strColumn) {
		for (Record record : list) {
			fillDict(record, dictType, idColumn, strColumn);
		}
	}
	public static void fillDictToRecord(Record record) {
		Dict.fillAreaById(record, "start_province", "start_province_str");
		Dict.fillAreaById(record, "start_city", "start_city_str");
		Dict.fillAreaById(record, "start_district", "start_district_str");
		Dict.fillAreaById(record, "end_province", "end_province_str");
		Dict.fillAreaById(record, "end_city", "end_city_str");
		Dict.fillAreaById(record, "end_district", "end_district_str");

		Dict.fillAreaById(record, "vender_province", "vender_province_name");
		Dict.fillAreaById(record, "vender_city", "vender_city_name");
		Dict.fillAreaById(record, "vender_district", "vender_district_name");

		Dict.fillDict(record, "cargo_type", "cargo_type", "cargo_type_str");
		Dict.fillDict(record, "car_type", "car_type", "car_type_str");
		Dict.fillDict(record, "ship_type", "ship_type", "ship_type_str");
		if(record.getInt("cargo_unit") != null) {
			Integer cargo_unit = record.getInt("cargo_unit");
			String cargo_unit_name = CARGO_UNIT_NAME.get(cargo_unit);
			record.set("cargo_unit_name", cargo_unit_name);
		}
	}

	public static void fillDictToModel(Model<?> model) {
		Dict.fillAreaById(model, "start_province", "start_province_str");
		Dict.fillAreaById(model, "start_city", "start_city_str");
		Dict.fillAreaById(model, "start_district", "start_district_str");
		Dict.fillAreaById(model, "end_province", "end_province_str");
		Dict.fillAreaById(model, "end_city", "end_city_str");
		Dict.fillAreaById(model, "end_district", "end_district_str");
		Dict.fillDict(model, "cargo_type", "cargo_type", "cargo_type_str");
		Dict.fillDict(model, "car_type", "car_type", "car_type_str");
		Dict.fillDict(model, "ship_type", "ship_type", "ship_type_str");
		if(model.getInt("cargo_unit") != null) {
			Integer cargo_unit = model.getInt("cargo_unit");
			String cargo_unit_name = CARGO_UNIT_NAME.get(cargo_unit);
			model.put("cargo_unit_name", cargo_unit_name);
		}
	}
}
