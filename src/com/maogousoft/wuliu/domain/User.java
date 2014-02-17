/**
 * @filename Dict.java
 */
package com.maogousoft.wuliu.domain;

import com.jfinal.plugin.activerecord.Db;
import com.maogousoft.wuliu.common.BusinessException;

/**
 * @description 货主
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 15, 2013 9:02:30 PM
 */
public class User extends BaseModel<User>{

	private static final long serialVersionUID = 2230573683269292447L;

	public static final User dao = new User();

	/**
	 *
	 * @description 获取保险购买量
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:37:47 PM
	 * @param userId
	 * @return
	 */
	public long countInsurance(int userId){
		return 0 ;
	}

	/**
	 *
	 * @description 获取证件验证数量
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:38:38 PM
	 * @param userId
	 * @return
	 */
	public long countVerify(int userId){
		return 0 ;
	}

	/**
	 *
	 * @description 获取成交单数
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 8:03:48 PM
	 * @return 成交单数
	 */
	public long countFinishedOrder(int userId){
		String sql = "select count(1) as count_num from logistics_order where user_id = ? and status = ?" ;
		long count = Db.queryFirst(sql, userId , Order.STATUS_FINISH);
		return count ;
	}

	/**
	 *
	 * @description 获取推荐人数
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:17:54 PM
	 * @param phone
	 * @return
	 */
	public long countRecommend(String phone){
		String sqlUser = "select count(1) as count_num from logistics_user where recommender = ? " ;
		String sqlDriver = "select count(1) as count_num from logistics_driver where recommender = ? " ;
		long countUser = Db.queryFirst(sqlUser, phone);
		long countDriver = Db.queryFirst(sqlDriver, phone);
		return countUser + countDriver ;
	}

	/**
	 *
	 * @description 统计我的车队
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * May 3, 2013 9:21:29 PM
	 * @param userId
	 * @return
	 */
	public long countFleet(int userId){
		String sql = "select count(1) as count_num from logistics_fleet where user_id = ? " ;
		long count = Db.queryFirst(sql, userId);
		return count ;
	}

	/**
	 * 根据ID载入货主信息，如果不存在，则抛出异常
	 * @param driver_id
	 * @return
	 */
	public User loadUserById(int user_id) {
		User user = dao.findById(user_id);
		if(user == null) {
			throw new BusinessException("货主不存在[" + user_id + "]");
		}
		return user;
	}

	/**
	 * 调整货主物流币
	 * @param amount
	 * @return 调整前与调整后的金额
	 */
	public GoldResult adjustGold(double amount) {
		GoldResult result = new GoldResult();
		double gold = this.asDoubleValue("gold", 0);
		result.setBeforeGold(gold);

		gold = gold + amount;
		this.set("gold", gold);
		this.update();

		result.setAfterGold(gold);

		return result;
	}
}
