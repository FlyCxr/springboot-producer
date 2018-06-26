package com.springboot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 操作日期时间工具类
 */
@SuppressWarnings("all")
public class DateTimeUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);
	
	/**
	 * HH24小时制,hh12小时制
	 */
	private static String dateFormat9 = null;
	private static String dateFormat1 = null;
	private static String dateFormat2 = null;
	private static String dateFormat3 = null;
	private static String dateFormat4 = null;
	private static String dateFormat5 = null;
	private static String dateFormat6 = null;
	private static String dateFormat7 = null;
	private static String dateFormat8 = null;
	private static SimpleDateFormat simpleDateFormat = null;
	static {
		dateFormat9 = "yyyy-MM-dd HH:mm:ss";//默认使用
		dateFormat1 = "yyyy年MM月dd日 HH时mm分ss秒";
		dateFormat2 = "yyyy年MM月dd日 HH时mm分";
		dateFormat3 = "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒";
		dateFormat4 = "yyyy/MM/dd HH:mm:ss";
		dateFormat5 = "yyyy/MM/dd HH:mm:ss.SSS";
		dateFormat6 = "yyyy-MM-dd HH:mm:ss.SSS";
		dateFormat7 = "yyyy-MM-dd";
		dateFormat8 = "yyyy-MM-dd HH:mm";
		simpleDateFormat = new SimpleDateFormat(dateFormat9);
	}
	
	/**
	 * 返回当前时间24小时制。默认格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String calendarToString(){
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 根据指定格式返回当前时间24小时制字符串
	 * @param format
	 * @return
	 */
	public static String calendarToString(String format){
		simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 返回当前时间24小时制。默认格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateToString(){
		return simpleDateFormat.format(new Date());
	}
	
	/**
	 * 根据指定格式返回当前时间24小时制字符串 
	 * @return
	 */
	public static String dateToString(String format){
		simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date());
	}
	
	/**
	 * 根据指定字符串返回默认24小时制Date时间 默认格式yyyy-MM-dd HH:mm:ss
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String str) throws ParseException{
		return simpleDateFormat.parse(str);//提取格式中的日期
	}
	
	/**
	 * 根据指定字符串,指定格式返回默认24小时制Date时间
	 * @param str 字符串
	 * @param format 格式
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String str,String format) throws ParseException{
		simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.parse(str);//提取格式中的日期
	}
	
	/**
	 * 根据指定字符串返回默认24小时制Calendar时间.默认格式yyyy-MM-dd HH:mm:ss
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String str) throws ParseException{
		GregorianCalendar calendar = new GregorianCalendar();
		Date date = simpleDateFormat.parse(str);
		calendar.setTime(date); 
		return calendar;
	}
	
	/**
	 * 根据指定字符串返回默认24小时制Calendar时间
	 * @param str 字符串
	 * @param format 格式
	 * @return
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String str,String format) throws ParseException{
		GregorianCalendar calendar = new GregorianCalendar();
		simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(str);
		calendar.setTime(date); 
		return calendar;
	}
	
	/**
	 * 
	 * <p>把java日期转换为XML格式日期</p>
	 * @param date
	 * @return
	 * @autho 董杨炀
	 * @time 2017-4-10 下午2:53:28
	 */
	public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
		if (date != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			XMLGregorianCalendar gc = null;
			try {
				gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			} catch (Exception e) {
				LOGGER.error("XML日期类型转换错误：", e);
				throw new DateTimeUtilsException("Date of XML type conversion error");
			}
			return gc;
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * <p>xml格式日期转换</p>
	 * @param cal
	 * @return
	 * @autho 董杨炀
	 * @time 2017-4-10 下午2:53:41
	 */
	public static Date converToJavaDate(XMLGregorianCalendar cal){
		if (cal != null) {
			GregorianCalendar ca = cal.toGregorianCalendar();
			return ca.getTime();
		} else {
			return null;
		}
	}
	
	/**
	 * 相对当前日期，增加或减少天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String addDay(Date date, int day) {
		return simpleDateFormat.format(new Date(date.getTime() + 1000l * 24 * 60 * 60 * day));
	}

	/**
	 * 相对当前日期，增加或减少天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDayToDate(Date date, int day) {
		return new Date(date.getTime() + 1000l * 24 * 60 * 60 * day);
	}

	/**
	 * 返回两个时间的相差天数
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差天数
	 */

	public static Long getTimeDiff(String startTime, String endTime) {
		Long days = null;
		Date startDate = null;
		Date endDate = null;
		try {
			if (startTime.length() == 10 && endTime.length() == 10) {
				startDate = new SimpleDateFormat(dateFormat7).parse(startTime);
				endDate = new SimpleDateFormat(dateFormat7).parse(endTime);
			} else {
				simpleDateFormat.parse(startTime);
				simpleDateFormat.parse(endTime);
			}
			days = getTimeDiff(startDate, endDate);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			throw new DateTimeUtilsException("Date/time parsing errors");
		}
		return days;
	}

	/**
	 * 返回两个时间的相差天数
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差天数
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
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差分钟数
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
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差秒数
	 */
	public static Long getSecondDiff(Date startTime, Date endTime) {
		return (endTime.getTime() - startTime.getTime()) / 1000;
	}

	
	static class DateTimeUtilsException extends RuntimeException{
		
		private static final long serialVersionUID = 1L;
		
		public DateTimeUtilsException(String s) {
			super(s);
		}

		public DateTimeUtilsException(String s, Throwable e) {
			super(s, e);
		}

		public DateTimeUtilsException(Throwable e) {
			super(e);
		}
		
	}
}
