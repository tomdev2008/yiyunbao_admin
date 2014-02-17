/**
 * @filename SystemUser.java
 */
package com.maogousoft.wuliu.domain.sys;

import com.jfinal.plugin.activerecord.Model;

/**
 * @description 系统用户
 * @author shevliu
 * @email shevliu@gmail.com
 * Apr 4, 2013 3:34:21 PM
 */
public class SystemUser extends Model<SystemUser>{
	
	private static final long serialVersionUID = 2669262178079263638L;
	
	public static final SystemUser dao = new SystemUser();
}
