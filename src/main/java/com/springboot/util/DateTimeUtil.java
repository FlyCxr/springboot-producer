package com.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 操作日期时间工具类
 */
@SuppressWarnings("all")
public class DateTimeUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);

//	private static String format = "yyyy-MM-dd HH:mm:ss";//默认使用
//	private static String format2 = "yyyy年MM月dd日 HH时mm分ss秒";
//	private static String format3 = "yyyy-MM-dd";

	/**
	 * 根据指定格式返回Date时间字符串
	 * @param format
	 * @return
	 */
	public static String format(Date date,String format){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * 根据指定指定格式解析字符串时间返回Date时间
	 * @param str 字符串
	 * @param format 格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String str,String format) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.parse(str);
	}

	/**
	 * 根据指定指定格式解析字符串时间返回Calendar时间
	 * @param str
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Calendar parseCalendar(String str,String format) throws ParseException{
		GregorianCalendar calendar = new GregorianCalendar();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(str);
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 根据指定格式返回Calendar时间制字符串
	 * @param format
	 * @return
	 */
	public static String formatCalendar(String format,Calendar calendar){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * 相对当前日期，增加或减少天数
	 * @param date
	 * @param day
	 * @return
	 */
	public static String addDay(Date date, int day,String format) {
		return new SimpleDateFormat(format).format(new Date(date.getTime() + 1000l * 24 * 60 * 60 * day));
	}

	/**
	 * 相对当前日期，增加或减少天数
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDayToDate(Date date, int day) {
		return new Date(date.getTime() + 1000l * 24 * 60 * 60 * day);
	}


	/**
	 * 返回两个时间的相差天数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long getTimeDiff(Date startTime, Date endTime) {
		Long days = null;
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		long l_s = c.getTimeInMillis();
		c.setTime(endTime);
		long l_e = c.getTimeInMillis();
		days = (l_e - l_s) / 86400000;
		return days;
	}

	/**
	 * 返回两个时间的相差分钟数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long getMinuteDiff(Date startTime, Date endTime) {
		Long minutes = null;
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		long l_s = c.getTimeInMillis();
		c.setTime(endTime);
		long l_e = c.getTimeInMillis();
		minutes = (l_e - l_s) / (1000l * 60);
		return minutes;
	}

	/**
	 * 返回两个时间的相差秒数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static Long getSecondDiff(Date startTime, Date endTime) {
		return (endTime.getTime() - startTime.getTime()) / 1000;
	}

}
