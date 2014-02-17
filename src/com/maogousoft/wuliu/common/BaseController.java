/**
 * @filename BaseController.java
 */
package com.maogousoft.wuliu.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.maogousoft.wuliu.common.domain.MiniData;
import com.maogousoft.wuliu.common.utils.CookieUtil;
import com.maogousoft.wuliu.common.utils.DesCipher;
import com.maogousoft.wuliu.common.utils.JSONUtils;
import com.maogousoft.wuliu.domain.Constant;

/**
 * @description 
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 12, 2013 11:09:33 PM
 */
public class BaseController extends Controller{
	
	private static final Log log = LogFactory.getLog(BaseController.class);
	
	/**
	 * 默认分页大小
	 */
	public static final int DEFAULT_PAGESIZE = 20 ;

	protected MiniData getMiniData(){
		MiniData data = new MiniData() ;
		List<Map<String, Object>> list = (List<Map<String, Object>>) JSONUtils.Decode(getPara("data"));
		Map<String, Object> map = list.get(0);
		data.setPageState(map.get("_state")+"");
		map.remove("_state");
		map.remove("_uid");
		map.remove("_index");
		Record record = new Record();
		record.setColumns(map);
		data.setRecord(record);
		return data ;
	}
	
	/**
	 * 
	 * @description sql拼接and条件 ,varchar
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:49:07 PM
	 * @param allField
	 * @return
	 */
	public String createAnd(String allField){
		String sql = "" ;
		Assert.hasText(allField) ;
		String[] fields = allField.split("\\|");
		for(String fieldName : fields){
			String[] fieldNames = fieldName.split(":");
			String field = getPara(fieldNames[fieldNames.length -1]);
			if(StringUtils.isNotBlank(field)){
				sql += " and " + fieldNames[0] + " = '" + field + "' " ;
			}
		}
		log.info("sql:" + sql);
		return sql ;
	}
	
	/**
	 * sql拼接and条件  , int型
	 * @param allField
	 * @return
	 * @author cdliupengfei
	 * @date 2013-3-21
	 */
	public String createAndUsingInt(String allField){
        String sql = "" ;
        Assert.hasText(allField) ;
        String[] fields = allField.split("\\|");
        for(String fieldName : fields){
            String[] fieldNames = fieldName.split(":");
            String field = getPara(fieldNames[fieldNames.length -1]);
            if(StringUtils.isNotBlank(field)){
                sql += " and " + fieldNames[0] + " = " + field + " " ;
            }
        }
        log.info("sql:" + sql);
        return sql ;
    }
	
	
	/**
	 * 
	 * @description sql拼接order语句 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:49:22 PM
	 * @return
	 */
	public String createOrder(String tableName){
		String sortField = getPara("sortField") ;
		String sortOrder = getPara("sortOrder") ;
		String s = " order by " ;
		if(StringUtils.isNotBlank(tableName)){
			s += tableName + "." ;
		}
		if(StringUtils.isNotBlank(sortField)){
			s += sortField;
		}
		else{
			s += "id" ;
		}
		
		if(StringUtils.isNotBlank(sortOrder)){
			s += " " + sortOrder + " ";
		}
		else{
			s += " desc " ;
		}
		return s ;
	}
	
	/**
	 * 
	 * @description sql拼接order语句 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:49:22 PM
	 * @return
	 */
	public String createOrder(){
		return createOrder(null);
	}
	
	/**
	 * 
	 * @description 获取页码 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:48:10 PM
	 * @return
	 */
	public int getPageIndex(){
		return getParaToInt("pageIndex" , 0) + 1 ;
	}
	
	/**
	 * 
	 * @description 获取分页大小 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Mar 17, 2013 3:48:59 PM
	 * @return
	 */
	public int getPageSize(){
		return getParaToInt("pageSize" , DEFAULT_PAGESIZE);
	}
	
	/**
	 * 
	 * @description 获取登录用户ID 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:17:19 PM
	 * @return
	 */
	public int getUserId(){
		DesCipher desCipher = new DesCipher(Constant.COOKIE_DES_KEY);
		String id = desCipher.decrypt(CookieUtil.getCookie(getRequest(), Constant.COOKIE_ID));
		return NumberUtils.toInt(id);
	}
	
	/**
	 * 
	 * @description 获取登录用户姓名 
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:17:56 PM
	 * @return
	 */
	public String getUserName(){
		DesCipher desCipher = new DesCipher(Constant.COOKIE_DES_KEY);
		String userName = desCipher.decrypt(CookieUtil.getCookie(getRequest(), Constant.COOKIE_USER_NAME));
		return userName;
	}
	
	/**
	 * 
	 * @description 获取登录用户手机号
	 * @author shevliu
	 * @email shevliu@gmail.com
	 * Apr 5, 2013 11:18:18 PM
	 * @return
	 */
	public String getUserPhone(){
		DesCipher desCipher = new DesCipher(Constant.COOKIE_DES_KEY);
		String account = desCipher.decrypt(CookieUtil.getCookie(getRequest(), Constant.COOKIE_ACCOUNT));
		return account;
	}
}
