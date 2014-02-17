/**
 * @filename StatisticsService.java
 */
package com.maogousoft.wuliu.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.domain.Driver;

/**
 * @description 司机数据统计
 * @author shevliu
 * @email shevliu@gmail.com
 * May 3, 2013 7:53:57 PM
 */
public class DriverStatisticsService {
	
	private static final Log log = LogFactory.getLog(DriverStatisticsService.class);

	/**
	 * 
	 * @description 统计司机相关数据，并计算排名，更新 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 7:55:19 PM
	 */
	public void driverStatistics(){
		List<Record> driverList = Db.find("select id,phone from logistics_driver where status = 1") ;
		for(Record record : driverList){
			int driverId = record.getInt("id");
			String phone = record.getStr("phone");
			long countOnlineTime = Driver.dao.countOnlineTime(driverId);
//			long orderCount = Driver.dao.countFinishedOrder(driverId);
			long countRecommend = Driver.dao.countRecommend(phone);
			String sql = "update logistics_driver set online_time = ? , recommender_count = ?  where id = ? ";
			Db.update(sql , countOnlineTime  , countRecommend , driverId);
			//信誉评价
			driverReply(driverId);
		}
		
		//以下为重新排名
		onlineTimeRank();
		orderRank();
		recommenderRank();
	}
	
	/**
	 * 
	 * @description 在线排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 10:12:25 PM
	 */
	private void onlineTimeRank(){
		List<Record> list = Db.find("select id,online_time from logistics_driver where status = 1 order by online_time desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("online_time");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_driver set online_time_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 成交单数排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 10:11:43 PM
	 */
	private void orderRank(){
		List<Record> list = Db.find("select id,order_count from logistics_driver where status = 1 order by order_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("order_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_driver set order_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 推荐人数排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 10:12:58 PM
	 */
	private void recommenderRank(){
		List<Record> list = Db.find("select id,recommender_count from logistics_driver where status = 1 order by recommender_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("recommender_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_driver set recommender_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 司机信誉评价 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 10:42:48 PM
	 */
	public void driverReply(int driverId){
		String sql = "select avg(score1) as score1,avg(score2) as score2 ,avg(score3) as score3 from logistics_driver_reply where driver_id = ? and status = 0";
		Record record = Db.findFirst(sql, driverId);
		log.info("score1:" + record.getBigDecimal("score1"));
		double score1 = record.getBigDecimal("score1") == null ? 0 : record.getBigDecimal("score1").doubleValue() ;
		double score2 = record.getBigDecimal("score2") == null ? 0 : record.getBigDecimal("score2").doubleValue() ;
		double score3 = record.getBigDecimal("score3") == null ? 0 : record.getBigDecimal("score3").doubleValue() ;
		double score = (score1 + score2 + score3)/3 ;
		String updateSql = "update logistics_driver set score1 = ? ,score2 = ? , score3 = ? , score= ?  where id = ?" ;
		Db.update(updateSql , score1 , score2 , score3 ,score , driverId) ;
	}
}
