/**
 * DateUtils.java
 * 
 * @date 2011-12-22
 * 
 * Copyright @2010 BeiJing Pingtech Co. Ltd.
 * 
 * All right reserved.
 */
package com.seeyon.v3x.dee.common.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @description 日期工具类
 * 
 * @author liuls
 */
@SuppressWarnings("unused")
public class DateUtils {
	private final static Log log = LogFactory.getLog(DateUtils.class);
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;

	private static final long ONE_HOUR = 60 * 60 * 1000;

	private static final long ONE_MIN = 60 * 1000;

	static public final String DATE_FMT_1 = "yyyy-MM-dd";
	
	// ORA标准时间格式
	 
	private static final SimpleDateFormat ORA_DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	// 带时分秒的ORA标准时间格式
	 
	private static final SimpleDateFormat ORA_DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	/**
	 * @description 将日期转化为字符串,字符串格式("YYYY-MM-DD")，小时、分、秒被忽略
	 * @date 2011-12-22
	 * @author liuls
	 * @param date 日期
	 * @return 带格式的字符串
	 */
	public static String DateToString(Date date) {
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(
			"yyyy-MM-dd");
		String strDateTime = formater.format(date);
		return strDateTime;
	}

	/**
	 * @description 将日期转化为字符串,字符串格式("YYYY年-mm月-dd日")，小时、分、秒被忽略
	 * @date 2011-12-22
	 * @author liuls
	 * @param date  日期
	 * @return 带格式的字符串
	 */
	public static String DateToStringText(Date date) {
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(
			"yyyy年MM月dd日");
		String strDateTime = formater.format(date);
		return strDateTime;
	}

	/**
 	 * @description	将日期转化为字符串,字符串格式自定义
 	 * @date 2011-12-22
	 * @author liuls
	 * @param Date 日期
	 * @param pattern 日期格式
	 * @return String 类型
	 */
	public static String DateToString(Date date, String pattern) throws
			Exception {
		String strDateTime = null;
		try {
			java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(pattern);
			strDateTime = formater.format(date);
		}
		catch (Exception ex) {
			throw ex;
		}
		return strDateTime;
	}

	/**
	 * @description 将传入的年月日转化为Date类型
	 * @date 2011-12-22
	 * @author liuls
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return Date类型
	 */
	public static Date YmdToDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.getTime();
	}


	/**
	 *
	 * @description 将日期转化为字符串
	 * @date 2011-12-22
	 * @author liuls
	 * @param date 日期
	 * @return 字符串格式("MM/dd HH:mm:ss")
	 */
	public static String communityDateToString(Date date) {
		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat(
			"MM/dd HH:mm:ss");
		String strDateTime = formater.format(date);
		return strDateTime;
	}

	/**
	 * @description 得到某一天的所在周的第一天和最后一天（周一（） 周日()）和这一天所在的为这一年的第几周
	 * @date 2011-12-22
	 * @author liuls
	 * @param str  日期格式的字符串
	 * @param pattern 日期格式
	 * @param type 类型：周日为一周开始 为 0，周一为一周的开始为1
	 * @return  数组：某一天的所在周的第一天和最后一天（周一（） 周日()）和这一天所在的为这一年的第几周
	 */
	public static String[] getWeekParams(String str,String pattern,int type)
	{
		String[] three = new String[3];
		if (pattern==null||pattern.equals(""))
		{
			pattern = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat formater= null;
		try
		{
			formater = new SimpleDateFormat(pattern);
		}catch(Exception e)
		{
			formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		Date date = new Date();
		try
		{
			date = formater.parse(str);
		} catch (ParseException e)
		{
			log.error("Parse date Error in DateUtils");
		}
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(type+1);
		c.setTime(date);
		c.get(Calendar.WEEK_OF_YEAR);
		c.set(Calendar.DAY_OF_WEEK,2);
		three[0] = formater.format(c.getTime());
		c.set(Calendar.DAY_OF_WEEK, 8);
		three[1] = formater.format(c.getTime());
		three[2] = String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
		return three;
	}
	/**
	 *
	 * @description
	 * @date 2011-12-22
	 * @author liuls
	 * @param date
	 * @param weekday
	 * @param type
	 * @throws Exception
	 */
	public static Date getDateWeekDays(Date date,int weekday,int type) throws Exception {
		String format ="yyyy-MM-dd HH:mm:ss";
		int weekdayIs = dateToWeekDay(date, type);
		String[] day =getWeekParams(DateToString(date, format),format,type);

//
		if(type==0){
			if(weekday>weekdayIs){
				date.setTime(date.getTime()+(weekday-weekdayIs)*24 * 60 * 60 * 1000);
				return date;
			}else if(weekday==weekdayIs){
				return date;
			}else if(weekday<weekdayIs){
				date.setTime(date.getTime()+(7-weekdayIs+weekday)*24*60*60*1000);
				return date;
			}
		}else{
			if(weekday>weekdayIs){
				date.setTime(date.getTime()+(weekday-weekdayIs)*24 * 60 * 60 * 1000);
				log.debug( dateToWeekDay(date, type));
				return date;
			}else if(weekday==weekdayIs){
				log.debug( dateToWeekDay(date, type));
				return date;
			}else if(weekday<weekdayIs){
				date.setTime(date.getTime()+7*24*60*60*1000);
				log.debug( dateToWeekDay(date, type));
				return date;
			}
		}
		return date;
	}



	/**
	 * @description 将字符串转化为日期。
	 * @date 2011-12-22
	 * @author liuls
	 * @param 字符串格式("yyyy-MM-dd HH:mm")。
	 */
	@SuppressWarnings("finally")
	public static Date StringToDate(String str) {
		Date dateTime = null;
		try {
			if (! (str == null || str.equals(""))) {
				java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
				dateTime = formater.parse(str);
			}
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		finally {
			return dateTime;
		}
	}

	/**
	 * @description 日期时间带时分秒的Timestamp表示,
	 * @date 2011-12-22
	 * @author liuls
	 * @param 日期字符串 必须符合 格式 例如2010-09-02 11:12:12.022111111
	 * @return Timestamp 时间戳
	 */
	public static Timestamp StringToDateHMS(String str) throws Exception {
		Timestamp time = null;
		try {
			time = java.sql.Timestamp.valueOf(str);
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return time;

	}

	/**
	 * @description 得一个date对象对应的日期的0点0分0秒时刻的Date对象。
	 * @date 2011-12-22
	 * @author liuls
	 * @param date 一个日期
	 * @return Date对象。
	 */
	public static Date getMinDateOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));

		return calendar.getTime();
	}




	/**
	 * @description 取得一个date对象对应的日期的23点59分59秒时刻的Date对象。
	 * @date 2011-12-22
	 * @author liuls
	 * @param date 一个日期
	 * @return Date对象。
	 */
	public static Date getMaxDateOfDay(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,
					 calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND,
					 calendar.getActualMaximum(Calendar.MILLISECOND));

		return calendar.getTime();
	}

	/**
	 * @description 字符串按照格式转化日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param dateString 日期字符串
	 * @param DataFormat 日期格式
	 * @return 转换后的日期
	 */
	public static Date parseDate(String dateString, String DataFormat) {
		if(DataFormat==null)DataFormat="yyyy-MM-dd";
		SimpleDateFormat fordate = new SimpleDateFormat(DataFormat);
		if (dateString == null || dateString.equals(""))
			return null;
		try {

			return fordate.parse(dateString);
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * @description 字符串按照格式转化java.sql.Date 默认格式 "yyyy-MM-dd"
	 * @date 2011-12-22
	 * @author liuls
	 * @param dateString 日期字符串
	 * @return 转换后的日期
	 */
	public static java.sql.Date parseSQLDate(String dateString){
		SimpleDateFormat fordate = new SimpleDateFormat("yyyy-MM-dd");
		if (dateString == null || dateString.equals(""))
			return null;
		try{
		Date d= fordate.parse(dateString);
		java.sql.Date sd=new java.sql.Date(d.getTime());
		return sd;
		}
		catch (ParseException e){
			log.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * @description 将两个格式为HH:MM:SS的时间字符串相加，例如：00:59:06 + 01:00:59 返回 02:00:05。
	 * @date 2011-12-22
	 * @author liuls
	 * @param time1 要累计的时间字符串
	 * @param time2 要累计的时间字符串
	 * @return 累计后的时间字符串
	 */
	public static String addTwoTimeStr(String time1, String time2) {

		String returnStr = "00:00:00";
		if (time1 != null && !time1.equalsIgnoreCase("") && time2 != null
				&& !time2.equalsIgnoreCase("")) {
			String[] time1Array = time1.split(":");
			String[] time2Array = time2.split(":");
			int hour1 = (new Integer(time1Array[0])).intValue();
			int hour2 = (new Integer(time2Array[0])).intValue();
			int min1 = (new Integer(time1Array[1])).intValue();
			int min2 = (new Integer(time2Array[1])).intValue();
			int sec1 = (new Integer(time1Array[2])).intValue();
			int sec2 = (new Integer(time2Array[2])).intValue();

			String lastSec, lastMin, lastHour;

			int totalSec = sec1 + sec2;
			if (totalSec / 60 > 0) {
				min1 = min1 + totalSec / 60;
			}
			if (totalSec % 60 > 9) {
				lastSec = new Integer(totalSec % 60).toString();
			}
			else {
				lastSec = new String("0"
						+ new Integer(totalSec % 60).toString());
			}

			int totalMin = min1 + min2;
			if (totalMin / 60 > 0) {
				hour1 = hour1 + totalMin / 60;
			}
			if (totalMin % 60 > 9) {
				lastMin = new Integer(totalMin % 60).toString();
			}
			else {
				lastMin = new String("0"
						+ new Integer(totalMin % 60).toString());
			}

			int totalHour = hour1 + hour2;
			if (totalHour % 24 > 9) {
				lastHour = new Integer(totalHour % 24).toString();
			}
			else {
				lastHour = new String("0"
						+ new Integer(totalHour % 24).toString());
			}

			returnStr = lastHour + ":" + lastMin + ":" + lastSec;
		}
		else if (time1 != null && !time1.equalsIgnoreCase("")) {
			returnStr = time1.substring(0, 8);
		}
		else if (time2 != null && !time2.equalsIgnoreCase("")) {
			returnStr = time2.substring(0, 8);
		}
		else {
			returnStr = "00:00:00";
		}

		return returnStr;
	}

	/**
	 * @description 创建一个标准ORA时间格式的克隆
	 * @date 2011-12-22
	 * @author liuls
	 * @return 标准ORA时间格式的克隆
	 */
	private static synchronized DateFormat getOraDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_FORMAT
				.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * @description 创建一个带分秒的ORA时间格式的克隆
	 * @date 2011-12-22
	 * @author liuls
	 * @return 标准ORA时间格式的克隆
	 */
	private static synchronized DateFormat getOraExtendDateTimeFormat() {
		SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_EXTENDED_FORMAT
				.clone();
		theDateTimeFormat.setLenient(false);
		return theDateTimeFormat;
	}

	/**
	 * @description 得到系统当前的日期 格式为YYYY-MM-DD
	 * @date 2011-12-22
	 * @author liuls
	 * @return 系统当前的日期 格式为YYYY-MM-DD
	 */
	public static String getSystemCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return doTransform(calendar.get(Calendar.YEAR), calendar
				.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}
	/**
	 * @description 返回格式为YYYY-MM-DD
	 * @date 2011-12-22
	 * @author liuls
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return YYYY-MM-DD格式的字符串
	 */
	private static String doTransform(int year, int month, int day) {
		StringBuffer result = new StringBuffer();
		result.append(String.valueOf(year)).append("-").append(
				month < 10 ? "0" + String.valueOf(month) : String
						.valueOf(month)).append("-").append(
				day < 10 ? "0" + String.valueOf(day) : String.valueOf(day));
		return result.toString();
	}

	/**
	 * @description 获得昨天的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @return 指定日期的上一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayBeforeToday() {
		Date date = new Date(System.currentTimeMillis());
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, -1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * @description 获得指定日期的上一天的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param dateStr  指定的日期 格式:YYYY-MM-DD
	 * @return
	 */
	public static synchronized String getDayBeforeToday(String dateStr) {
		Date date = toDayStartDate(dateStr);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, -1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * @description 获得明天的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @return 指定日期的下一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayAfterToday() {
		Date date = new Date(System.currentTimeMillis());
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * @description 获得指定日期的下一天的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param dateStr 指定的日期 格式:YYYY-MM-DD
	 * @return 指定日期的下一天 格式:YYYY-MM-DD
	 */
	public static synchronized String getDayAfterToday(String dateStr) {
		Date date = toDayStartDate(dateStr);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.DATE, 1);
		return doTransform(toString(gc.getTime(), getOraExtendDateTimeFormat()))
				.substring(0, 10);
	}

	/**
	 * @description 以当前日期为准，获得以后几个月的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param months 月
	 * @return Date类型的日期
	 */
	public static synchronized Date getDayAfterMonth(int months) {
		Date date = new Date(System.currentTimeMillis());
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		gc.add(Calendar.MONTH, months);
		return gc.getTime();
	}

	/**
	 * @description 将输入格式为2004-8-13,2004-10-8类型的字符串转换为标准的Date类型,这种Date类型 对应的日期格式为YYYY-MM-DD
	 * 				00:00:00,代表一天的开始时刻
	 * @date 2011-12-22
	 * @author liuls
	 * @param dateStr 要转换的字符串
	 * @return 转换后的Date对象
	 */
	public static synchronized Date toDayStartDate(String dateStr) {
		String[] list = dateStr.split("-");
		int year = Integer.parseInt(list[0]);
		int month = Integer.parseInt(list[1]);
		int day = Integer.parseInt(list[2]);
		Calendar cale = Calendar.getInstance();
		cale.set(year, month - 1, day, 0, 0, 0);
		return cale.getTime();

	}

	/**
	 * @description 将两个scorm时间相加
	 * @date 2011-12-22
	 * @author liuls
	 * @param scormTime1 scorm时间,格式为00:00:00(1..2).0(1..3)
	 * @param scormTime2 scorm时间,格式为00:00:00(1..2).0(1..3)
	 * @return 两个scorm时间相加的结果
	 */
	public static synchronized String addTwoScormTime(String scormTime1,
			String scormTime2) {
		int dotIndex1 = scormTime1.indexOf(".");
		int hh1 = Integer.parseInt(scormTime1.substring(0, 2));
		int mm1 = Integer.parseInt(scormTime1.substring(3, 5));
		int ss1 = 0;
		if (dotIndex1 != -1) {
			ss1 = Integer.parseInt(scormTime1.substring(6, dotIndex1));
		}
		else {
			ss1 = Integer
					.parseInt(scormTime1.substring(6, scormTime1.length()));
		}
		int ms1 = 0;
		if (dotIndex1 != -1) {
			ms1 = Integer.parseInt(scormTime1.substring(dotIndex1 + 1,
					scormTime1.length()));
		}

		int dotIndex2 = scormTime2.indexOf(".");
		int hh2 = Integer.parseInt(scormTime2.substring(0, 2));
		int mm2 = Integer.parseInt(scormTime2.substring(3, 5));
		int ss2 = 0;
		if (dotIndex2 != -1) {
			ss2 = Integer.parseInt(scormTime2.substring(6, dotIndex2));
		}
		else {
			ss2 = Integer
					.parseInt(scormTime2.substring(6, scormTime2.length()));
		}
		int ms2 = 0;
		if (dotIndex2 != -1) {
			ms2 = Integer.parseInt(scormTime2.substring(dotIndex2 + 1,
					scormTime2.length()));
		}

		int hh = 0;
		int mm = 0;
		int ss = 0;
		int ms = 0;

		if (ms1 + ms2 >= 1000) {
			ss = 1;
			ms = ms1 + ms2 - 1000;
		}
		else {
			ms = ms1 + ms2;
		}
		if (ss1 + ss2 + ss >= 60) {
			mm = 1;
			ss = ss1 + ss2 + ss - 60;
		}
		else {
			ss = ss1 + ss2 + ss;
		}
		if (mm1 + mm2 + mm >= 60) {
			hh = 1;
			mm = mm1 + mm2 + mm - 60;
		}
		else {
			mm = mm1 + mm2 + mm;
		}
		hh = hh + hh1 + hh2;

		StringBuffer sb = new StringBuffer();
		if (hh < 10) {
			sb.append("0").append(hh);
		}
		else {
			sb.append(hh);
		}
		sb.append(":");
		if (mm < 10) {
			sb.append("0").append(mm);
		}
		else {
			sb.append(mm);
		}
		sb.append(":");
		if (ss < 10) {
			sb.append("0").append(ss);
		}
		else {
			sb.append(ss);
		}
		sb.append(".");
		if (ms < 10) {
			sb.append(ms).append("00");
		}
		else if (ms < 100) {
			sb.append(ms).append("0");
		}
		else {
			sb.append(ms);
		}
		return sb.toString();
	}

	/**
	 * @description 根据timeType返回当前日期与传入日期的差值（当前日期减传入日期） 当要求返回月份的时候，date的日期必须和当前的日期相等，
	 * 				否则返回0（例如：2003-2-23 和 2004-6-12由于23号和12号不是同一天，固返回0， 2003-2-23 和 2005-6-23
	 * 				则需计算相差的月份，包括年，此例应返回28（个月）。 2003-2-23 和 2001-6-23
	 * 				也需计算相差的月份，包括年，此例应返回-20（个月））
	 * @date 2011-12-22
	 * @author liuls
	 * @param date 要与当前日期比较的日期
	 * @param timeType 0代表返回两个日期相差月数，1代表返回两个日期相差天数
	 * @return 根据timeType返回当前日期与传入日期的差值
	 */
	public static int CompareDateWithNow(Date date, int timeType) {
		Date now = Calendar.getInstance().getTime();

		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(now);
		calendarNow.set(Calendar.HOUR, 0);
		calendarNow.set(Calendar.MINUTE, 0);
		calendarNow.set(Calendar.SECOND, 0);

		Calendar calendarPara = Calendar.getInstance();
		calendarPara.setTime(date);
		calendarPara.set(Calendar.HOUR, 0);
		calendarPara.set(Calendar.MINUTE, 0);
		calendarPara.set(Calendar.SECOND, 0);

		float nowTime = now.getTime();
		float dateTime = date.getTime();

		if (timeType == 0) {
			if (calendarNow.get(Calendar.DAY_OF_YEAR) == calendarPara
					.get(Calendar.DAY_OF_YEAR))
				return 0;
			return (calendarNow.get(Calendar.YEAR) - calendarPara
					.get(Calendar.YEAR))
					* 12
					+ calendarNow.get(Calendar.MONTH)
					- calendarPara.get(Calendar.MONTH);
		}
		else {
			float result = nowTime - dateTime;
			float day = 24 * 60 * 60 * 1000;
			result = result / day;
			Float resultFloat = new Float(result);
			float fraction = result - resultFloat.intValue();
			if (fraction > 0.5) {
				return resultFloat.intValue() + 1;
			}
			else if (fraction < -0.5) {
				return resultFloat.intValue() - 1;
			}
			else {
				return resultFloat.intValue();
			}
		}
	}

	/**
	 * @description 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串对象.
	 * @date 2011-12-22
	 * @author liuls
	 * @param theDate  要转换的日期对象
	 * @param theDateFormat 返回的日期字符串的格式
	 * @return 转换为制定格式的日期
	 */
	public static synchronized String toString(Date theDate,
			DateFormat theDateFormat) {
		if (theDate == null) {
			return "";
		}
		else {
			return theDateFormat.format(theDate);
		}
	}

	/**
	 * @description 返回格式为YYYY-MM-DD hh:mm:ss
	 * @date 2011-12-22
	 * @author liuls
	 * @param date  输入格式为ORA标准时间格式
	 * @return  格式为YYYY-MM-DD hh:mm:ss 的时间串
	 */
	private static String doTransform(String date) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(date.substring(0, 4));
		buffer.append("-");
		buffer.append(date.substring(4, 6));
		buffer.append("-");
		buffer.append(date.substring(6, 8));
		buffer.append(" ");
		buffer.append(date.substring(8, 10));
		buffer.append(":");
		buffer.append(date.substring(10, 12));
		buffer.append(":");
		buffer.append(date.substring(12, 14));

		return buffer.toString();
	}
	/**
	 * @description 获得日期是一个星期的第几天
	 * @date 2011-12-22
	 * @author liuls
	 * @param date 转换的日期
	 * @param type 周日为一周开始 为 0，周一为一天开始的为1
	 * @return  日期是一个星期的第几天
	 */
	public static int dateToWeekDay(Date date,int type){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(type==0){
			return calendar.get(Calendar.DAY_OF_WEEK);
		}
		else if (type==1) {
			if(calendar.get(Calendar.DAY_OF_WEEK)==1){
				return 7;
			}else{
				return calendar.get(Calendar.DAY_OF_WEEK)-1;
			}
		}
		else{
			return -1;
		}
	}
	/**
	 * @description 获得当前星期开始日期
	 * @date 2011-12-22
	 * @author liuls
	 * @return 当前星期开始日期
	 */
	public static Date getWeekStartDate() {
		Calendar calendar = Calendar.getInstance();
		Date firstDateOfWeek;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - 1));
		firstDateOfWeek = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		return firstDateOfWeek;
	}

	/**
	 * @description 获得当前星期结束日期
	 * @date 2011-12-22
	 * @author liuls
	 * @return 当前星期结束日期
	 */
	public static Date getWeekEndDate() {
		Calendar calendar = Calendar.getInstance();
		Date lastDateOfWeek;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - 1));
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		lastDateOfWeek = calendar.getTime();
		return lastDateOfWeek;
	}

	/**
	 * @description 获得当前月份的第一天
	 * @date 2011-12-22
	 * @author liuls
	 * @return 当前月份的第一天
	 */
	public static Date getMonthStartDate() {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date(System.currentTimeMillis()));
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return toDayStartDate(df.format(gc.getTime()));
	}

	/**
	 * @description 获得当前月份的最后一天
	 * @date 2011-12-22
	 * @author liuls
	 * @return Date 前月份的最后一天
	 */
	public static Date getMonthEndDate() {
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
//		java.text.SimpleDateFormat dff = new java.text.SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.DATE, -1);
		return toDayEndDate(df.format(cal.getTime()));
	}
	/**
	 * @description 将输入格式为2004-8-13,2004-10-8类型的字符串转换为标准的Date类型,这种Date类型 对应的日期格式为YYYY-MM-DD
	 * 				23:59:59,代表一天的结束时刻
	 * @date 2011-12-22
	 * @author liuls
	 * @param dateStr 输入格式:2004-8-13,2004-10-8
	 * @return 转换后的Date对象 ，格式为YYYY-MM-DD 23:59:59
	 */
	public static synchronized Date toDayEndDate(String dateStr) {
		String[] list = dateStr.split("-");
		int year = new Integer(list[0]).intValue();
		int month = new Integer(list[1]).intValue();
		int day = new Integer(list[2]).intValue();
		Calendar cale = Calendar.getInstance();
		cale.set(year, month - 1, day, 23, 59, 59);
		return cale.getTime();

	}
	/**
	 * @description 得到几天前的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param d 标准日期
	 * @param day 第几天
	 * @return 几天前的日期
	 */
	public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);   
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();   
    }
	/**
	 * @description 得到几年前的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param d  标准日期
	 * @param day 第几年
	 * @return 几年前的日期
	 */
	public static Date getYearBefore(Date d, int year) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);   
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) - year);
        return now.getTime();   
    }
	/**
	 * @description 得到几月前的日期
	 * @date 2011-12-22
	 * @author liuls
	 * @param d  标准日期
	 * @param day  第几月
	 * @return  几月前的日期
	 */
	public static Date getMonthBefore(Date d, int month) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);   
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) - month);
        return now.getTime();   
    }
	
	
	
	/**
	 * @description 将毫秒字符串转换为Date的方法
	 * @date 2011-12-22
	 * @author liuls
	 * @param str 毫秒字符串
	 * @return 转换后的Date
	 */
	public static Date parseDateByMilliSecondString(String str){
		Long millisecond = Long.parseLong(str);
		return new Date(millisecond);
	}
}
