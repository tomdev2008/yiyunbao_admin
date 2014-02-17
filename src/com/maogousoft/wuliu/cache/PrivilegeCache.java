/**
 * @filename PrivilegeCache.java
 */
package com.maogousoft.wuliu.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @description 权限缓存
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 6, 2013 1:12:19 PM
 */
public class PrivilegeCache {
	
	private static final Log log = LogFactory.getLog(PrivilegeCache.class);

	private static Map<String, Record> map = null;
	
	/**
	 * 
	 * @description 根据请求路径获取权限信息
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 3:04:13 PM
	 * @param uri
	 * @return
	 */
	public static Record getByUri(String uri){
		if(map == null){
			map = new HashMap<String, Record>();
			List<Record> list = Db.find("select * from system_privilege where status = 0");
			for(Record record : list ){
				String url = record.getStr("url");
				if(StringUtils.isNotBlank(url) ){
					map.put(url, record);
				}
			}
		}
		log.info("uri:" + uri);
		log.info("map:" + map);
		return map.get(uri) ;
	}
	
	/**
	 * 
	 * @description 据请求路径获取权限ID
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 6, 2013 3:19:56 PM
	 * @param uri
	 * @return
	 */
	public static int getPrivilegeIdByUri(String uri){
		Record record = getByUri(uri) ;
		if(record == null){
			return 0; 
		}else{
			return record.getInt("id");
		}
	}
}
