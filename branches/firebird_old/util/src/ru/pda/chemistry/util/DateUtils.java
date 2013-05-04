/*
 Copyright 2008 SEC "Knowledge Genesis", Ltd.
 http://www.kg.ru, http://www.knowledgegenesis.com
 $HeadURL$
 $Author$ pda
 $Revision$
 $Date: 28.09.2009 14:37:07$
*/
package ru.pda.chemistry.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

/**
 * @author pda (28.09.2009 14:37:07)
 * @version $Revision$
 */
public class DateUtils
{
	public static DateFormat getDayMonthYearFormat() {
		return new SimpleDateFormat("dd.MM.yyyy");
	}
	public static DateFormat getYearMonthDayFormat() {
		return new SimpleDateFormat("yyyy.MM.dd");
	}
	public static DateFormat getHourMinuteSecondFormat() {
		return new SimpleDateFormat("HH:mm:ss");
	}
	public static DateFormat getDayMonthYearHourMinuteFormat() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}
	public static DateFormat getDayMonthYearHourMinuteSecondFormat() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	}
	public static DateFormat getYearMonthDayHourMinuteSecondMillisecondFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}
	public static DateFormat getTimestampDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @param calendar календарь, используемый для преобразования.
	 * @param date исходная дата.
	 * @return дата с выставленным временем 00:00:00:000.
	 */
	public static Date getDayMonthYearDate(Calendar calendar, Date date) {
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * @param date исходная дата.
	 * @return дата с выставленным временем 03:00:00.
	 */
	public static Date getDate3AM(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 3);
		return calendar.getTime();
	}

	public static Date getDate(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day); // month is 0-based
		return calendar.getTime();
	}
	public static Date getLastDayOfMonthDate(int month, int year) {
		Calendar calendar = Calendar.getInstance();

		// get 1st day of next month
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, month); // month is 0-based, this sets next month
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// subtract one day from 1st day of next month -> get last 
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return calendar.getTime();
	}

	public static Timestamp getTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	public static Date getDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
}