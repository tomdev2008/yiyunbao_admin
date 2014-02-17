/**
 * @filename StatisticsService.java
 */
package com.maogousoft.wuliu.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.domain.Order;
import com.maogousoft.wuliu.domain.User;

/**
 * @description 货主数据统计
 * @author shevliu
 * @email shevliu@gmail.com
 * May 3, 2013 7:53:57 PM
 */
public class UserStatisticsService {
	
	private static final Log log = LogFactory.getLog(UserStatisticsService.class);

	/**
	 * 
	 * @description 统计货主相关数据，并计算排名，更新 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 7:55:19 PM
	 */
	public void userStatistics(){
		List<Record> userList = Db.find("select id,phone from logistics_user where status = 0") ;
		for(Record record : userList){
			int userId = record.getInt("id");
			String phone = record.getStr("phone");
			long countInsurance = User.dao.countInsurance(userId);
			//成交订单量已在司机输入回单密码时累加，这里无需再统计
//			long orderCount = User.dao.countFinishedOrder(userId);
			long countRecommend = User.dao.countRecommend(phone);
			long countFleet = User.dao.countFleet(userId);
			long countVerify = User.dao.countVerify(userId);
			String sql = "update logistics_user set insurance_count = ? , recommender_count = ? , fleet_count = ? ,verify_count = ? where id = ? ";
			log.info(userId + "  countRecommend:" + countRecommend + ",countFleet:" + countFleet);
			Db.update(sql , countInsurance  , countRecommend , countFleet , countVerify , userId);
			//信誉评价
			userReply(userId);
		}
		
		//以下为重新排名
		insuranceRank();
		orderRank();
		recommenderRank();
		fleetRank();
		verifyRank();
	}
	
	/**
	 * 
	 * @description 货主保险排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:51:22 PM
	 */
	private void insuranceRank(){
		List<Record> list = Db.find("select id,insurance_count from logistics_user where status = 0 order by insurance_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("insurance_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_user set insurance_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 成交单数排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:52:15 PM
	 */
	private void orderRank(){
		List<Record> list = Db.find("select id,order_count from logistics_user where status = 0 order by order_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("order_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_user set order_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 推荐人数排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:53:13 PM
	 */
	private void recommenderRank(){
		List<Record> list = Db.find("select id,recommender_count from logistics_user where status = 0 order by recommender_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("recommender_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_user set recommender_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 我的车队排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:53:49 PM
	 */
	private void fleetRank(){
		List<Record> list = Db.find("select id,fleet_count from logistics_user where status = 0 order by fleet_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("fleet_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_user set fleet_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 验证数量排名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:54:25 PM
	 */
	private void verifyRank(){
		List<Record> list = Db.find("select id,verify_count from logistics_user where status = 0 order by verify_count desc") ;
		for(int i = 0 ; i<list.size() ; i++){
			Record record = list.get(i);
			int userId = record.getInt("id");
			int insurance_count = record.getInt("verify_count");
			int rank = i + 1 ;
			if(insurance_count == 0){ //如果计数为0，则排名为0，否则修改排名
				rank = 0 ;
			}
			String sql = "update logistics_user set verify_count_rank = ? where id = ?" ;
			Db.update(sql , rank , userId) ;
		}
	}
	
	/**
	 * 
	 * @description 货主信誉评价 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 10:42:48 PM
	 */
	public void userReply(int userId){
		String sql = "select avg(score1) as score1,avg(score2) as score2 ,avg(score3) as score3 from logistics_user_reply where user_id = ? and status = 0";
		Record record = Db.findFirst(sql, userId);
		log.info("score1:" + record.getBigDecimal("score1"));
		double score1 = record.getBigDecimal("score1") == null ? 0 : record.getBigDecimal("score1").doubleValue() ;
		double score2 = record.getBigDecimal("score2") == null ? 0 : record.getBigDecimal("score2").doubleValue() ;
		double score3 = record.getBigDecimal("score3") == null ? 0 : record.getBigDecimal("score3").doubleValue() ;
		double score = (score1 + score2 + score3)/3 ;
		String updateSql = "update logistics_user set score1 = ? ,score2 = ? , score3 = ? , score= ?  where id = ?" ;
		Db.update(updateSql , score1 , score2 , score3 ,score , userId) ;
	}
}
