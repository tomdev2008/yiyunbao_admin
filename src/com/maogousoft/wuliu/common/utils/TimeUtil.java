package com.maogousoft.wuliu.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;


public class TimeUtil
{
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TimeUtil.class);
	
	
	public static Date parse(String str, String format, Date defaultValue)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date = sdf.parse(str);
			return date;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	public static String format(Date date, String format)
	{
		String str = DateFormatUtils.format(date, format);
		return str;
	}
}
