/**
 * @filename StatisticsService.java
 */
package com.maogousoft.wuliu.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @description 司机数据统计
 * @author shevliu
 * @email shevliu@gmail.com
 * May 3, 2013 7:53:57 PM
 */
public class VenderStatisticsService {
	
	private static final Log log = LogFactory.getLog(VenderStatisticsService.class);

	/**
	 * 
	 * @description 统计商家相关数据，并计算排名，更新 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 7:55:19 PM
	 */
	public void venderStatistics(){
		List<Record> venderList = Db.find("select id,vender_name from logistics_vender") ;
		for(Record record : venderList){
			int venderId = record.getInt("id");
			//信誉评价
			venderReply(venderId);
		}
	}
	
	/**
	 * 
	 * @description 商家信誉评价 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 10:42:48 PM
	 */
	public void venderReply(int venderId){
		String sql = "select avg(score1) as score1,avg(score2) as score2 ,avg(score3) as score3 from logistics_vender_reply where vender_id = ? ";
		Record record = Db.findFirst(sql, venderId);
		log.info("score1:" + record.getBigDecimal("score1"));
		double score1 = record.getBigDecimal("score1") == null ? 0 : record.getBigDecimal("score1").doubleValue() ;
		double score2 = record.getBigDecimal("score2") == null ? 0 : record.getBigDecimal("score2").doubleValue() ;
		double score3 = record.getBigDecimal("score3") == null ? 0 : record.getBigDecimal("score3").doubleValue() ;
		double score = (score1 + score2 + score3)/3 ;
		String updateSql = "update logistics_vender set score1 = ? ,score2 = ? , score3 = ? , score= ?  where id = ?" ;
		Db.update(updateSql , score1 , score2 , score3 ,score , venderId) ;
	}
}
