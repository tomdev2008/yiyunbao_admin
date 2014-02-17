/**
 * @filename MiniData.java
 */
package com.maogousoft.wuliu.common.domain;

import com.jfinal.plugin.activerecord.Record;

/**
 * @description mini ui post数据封装，包括是否编辑、post数据等。
 * @author shevliu
 * @email shevliu@gmail.com
 * Mar 12, 2013 11:11:35 PM
 */
public class MiniData {
	
	public static final String ADD = "added";
	
	public static final String UPDATE = "modified" ;

	private String pageState ;
	
	private Record record ;


	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public String getPageState() {
		return pageState;
	}

	public void setPageState(String pageState) {
		this.pageState = pageState;
	}
}
