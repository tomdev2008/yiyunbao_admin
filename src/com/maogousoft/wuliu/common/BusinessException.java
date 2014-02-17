package com.maogousoft.wuliu.common;


/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public BusinessException(String msg){
		super(msg);
	}

	public BusinessException(String msg , Throwable t) {
		super(msg ,t);
	}
}
