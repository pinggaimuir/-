package com.bric.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMM = "yyyyMM";
	public static final String YYYY = "yyyy";

	public static long HOUR_VARIABLE = 60 * 1000;
	private static final Calendar calendar = Calendar.getInstance(Locale.CHINA);
	static {
		calendar.setFirstDayOfWeek(Calendar.MONDAY);// 中国周一是一周的第一天
	}

	/**
	 * 根据时间和小时间隔得到一个新的时间
	 * 
	 * @return
	 */
	public static Date addDate(Date date, double hour) {
		if (date == null)
			return null;
		int integer = 0;
		int remainder = 0;

		integer = (int) Math.floor(hour);
		remainder = (int) (hour * 10) % 10;

		Calendar calendar = setCalendarDate(date);
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, integer);
		calendar.add(Calendar.MINUTE, remainder * 6);

		return calendar.getTime();
	}

	public static Date addDay(Date date, Integer nDay) {
		if (date == null || date.equals("null") || date.equals("") 
				|| nDay == null || nDay.equals("null") || nDay.equals("")) {
			return null;
		}
		Calendar calendar = setCalendarDate(date);
		calendar.setTime(date);
		calendar.add(Calendar.DATE, nDay);
		Date result = calendar.getTime();
		return result;
	}

	public static Date addHours(Date date, int nhour) {
		Calendar calendar = setCalendarDate(date);
		calendar.add(Calendar.HOUR, nhour);
		Date result = calendar.getTime();
		return result;
	}

	public static Date addMonth(Date date, int nMonth) {
		Calendar calendar = setCalendarDate(date);
		calendar.add(Calendar.MONTH, nMonth);
		Date result = calendar.getTime();
		return result;
	}

	public static Date addYear(Date date, int nyear) {
		Calendar calendar = setCalendarDate(date);
		calendar.add(Calendar.YEAR, nyear);
		Date result = calendar.getTime();
		return result;
	}

	/**
	 * 返回某天的开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date begin(Date date) {
		if (null == date) {
			return null;
		}
		Calendar calendar = setCalendarDate(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	/**
	 * 相差 时 分 秒
	 * 
	 * @param dateFir
	 * @param dateSec
	 * @return
	 */
	public static String cha(Date dateFir, Date dateSec) {
		StringBuffer str = new StringBuffer();
		if (null != dateFir && null != dateSec && dateSec.getTime() > dateFir.getTime()) {
			long cha = (dateSec.getTime() - dateFir.getTime()) / 1000;
			int hor = (int) cha / 3600;
			if (0 != hor) {
				str.append(hor).append("小时");
			}
			int secd = (int) cha % 3600 / 60;
			if (0 != secd) {
				str.append(secd).append("分");
			}
			int miao = (int) cha % 60;
			if (0 != miao) {
				str.append(miao).append("秒");
			}
		}
		return str.toString();
	}

	/**
	 * 返回某天的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date end(Date date) {
		if (null == date) {
			return null;
		}
		Calendar calendar = setCalendarDate(date);
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		return calendar.getTime();
	}

	public static String formatDate(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
		String dateString = bartDateFormat.format(date);
		return dateString;
	}

	/**
	 * 取2个日期的间隔月数
	 * 
	 * @param dateFir
	 * @param dateSec
	 * @return
	 */
	public static int getBetweenMonth(Date dateFir, Date dateSec) {
		if (null == dateFir || null == dateSec) {
			return 0;
		}
		if (dateFir.after(dateSec)) {
			Date swap = dateFir;
			dateFir = dateSec;
			dateSec = swap;
		}
		Calendar temp = Calendar.getInstance();
		temp.setTime(dateSec);
		int total = temp.get(Calendar.YEAR) * 12 + temp.get(Calendar.MONTH);
		temp.setTime(dateFir);
		total = total - (temp.get(Calendar.YEAR) * 12 + temp.get(Calendar.MONTH));
		return total;
	}

	public static int getBetweenYear(Date dateFir, Date dateSec) {
		if (null == dateFir || null == dateSec) {
			return 0;
		}
		if (dateFir.after(dateSec)) {
			Date swap = dateFir;
			dateFir = dateSec;
			dateSec = swap;
		}
		Calendar temp = Calendar.getInstance();
		temp.setTime(dateSec);
		int total = temp.get(Calendar.YEAR);
		temp.setTime(dateFir);
		total = total - temp.get(Calendar.YEAR);
		return total;
	}

	private static Calendar getCalendar() {
		return setCalendarDate(new Date());
	}

	public static String getChineseWeek(Calendar calendar) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	public static String getChineseWeek(Date date) {
		Calendar calendar = setCalendarDate(date);
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	/**
	 * 返回当前月YYYYMM
	 * 
	 * @return
	 */
	public static int getCurrentMonthInt() {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMM);
		return Integer.parseInt(sdf.format(new Date()));
	}

	public static int getCurrentYear() {
		return getCalendar().get(Calendar.YEAR);
	}

	public static int getDateHour(Date date) {
		return setCalendarDate(date).get(Calendar.HOUR_OF_DAY);
	}

	public static int getDateMinute(Date date) {
		return setCalendarDate(date).get(Calendar.MINUTE);
	}

	/**
	 * 获得2个日期间隔时间
	 * 
	 * @param calendarFir
	 * @param calendarSec
	 * @return
	 */
	public static int getDaysBetween(Calendar calendarFir, Calendar calendarSec) {
		if (calendarFir.after(calendarSec)) {
			Calendar swap = calendarFir;
			calendarFir = calendarSec;
			calendarSec = swap;
		}
		int days = calendarSec.get(Calendar.DAY_OF_YEAR) - calendarFir.get(Calendar.DAY_OF_YEAR);

		int yearFir = calendarFir.get(Calendar.YEAR);
		int yearSec = calendarSec.get(Calendar.YEAR);

		if (yearFir != yearSec) {
			calendarFir = (Calendar) calendarFir.clone();
			do {
				days += calendarFir.getActualMaximum(Calendar.DAY_OF_YEAR);
				calendarFir.add(Calendar.YEAR, 1);
			} while (calendarFir.get(Calendar.YEAR) != yearSec);
		}
		return days;
	}

	/**
	 * 取得日期所在月包含的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {
		return setCalendarDate(date).getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar calendar = setCalendarDate(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * @param calendarFir
	 * @param calendarSec
	 * @return
	 */
	public static int getHolidays(Calendar calendarFir, Calendar calendarSec) {
		return getDaysBetween(calendarFir, calendarSec) - getWorkingDay(calendarFir, calendarSec);
	}

	/**
	 * 取两时间间隔小时/分钟
	 * 
	 * @return
	 */
	public static double getHourInt(Date dateFir, Date dateSec) {
		double hour = 0.0;
		double time = 0.0;

		if (dateFir != null && dateSec != null) {
			time = (dateFir.getTime() - dateSec.getTime()) / HOUR_VARIABLE;
		}

		if (time > 0) {
			hour = time / 60;
			hour = new Double(hour).doubleValue();
			BigDecimal initHour = new BigDecimal(hour);
			BigDecimal ho = initHour.setScale(1, BigDecimal.ROUND_HALF_UP);
			hour = ho.doubleValue();
		}
		return hour;
	}

	/**
	 * 取得月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar calendar = setCalendarDate(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		return calendar.getTime();
	}

	public static Date getLastWeekBeginDay(Date date) {
		Calendar calendar = setCalendarDate(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_YEAR, -(day - 2 + 7));
		return calendar.getTime();
	}

	public static Date getLastWeekEndDay(Date date) {
		Calendar calendar = setCalendarDate(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DAY_OF_YEAR, -(day - 1));
		return calendar.getTime();
	}

	/**
	 * 取两时间间隔分钟
	 * 
	 * @return
	 */
	public static double getMinuteInt(Date dateFir, Date dateSec) {
		double minute = 0.0;
		if (dateFir != null && dateSec != null) {
			minute = (dateFir.getTime() - dateSec.getTime()) / HOUR_VARIABLE;
		}
		return minute;
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * 取两时间间隔秒
	 * 
	 * @return
	 */
	public static double getSecondInt(Date dateFir, Date dateSec) {
		double minute = 0.0;
		if (dateFir != null && dateSec != null) {
			minute = (dateFir.getTime() - dateSec.getTime()) / 1000;
		}
		return minute;
	}

	public static Date getThisWeekDay(int weekday) {
		Calendar calendar = getCalendar();
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			if (weekday != Calendar.SUNDAY) {
				calendar.add(Calendar.DAY_OF_YEAR, -7);
			} else {
				calendar.set(Calendar.DAY_OF_WEEK, weekday);
			}
		} else {
			if (weekday == Calendar.SUNDAY) {
				calendar.add(Calendar.DAY_OF_YEAR, 7);
			} else {
				calendar.set(Calendar.DAY_OF_WEEK, weekday);
			}
		}
		calendar.set(Calendar.DAY_OF_WEEK, weekday);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获得当天日期，不含时分秒
	 * 
	 * @return
	 */
	public static Date getToday() {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}


	/**
	 * 获得指定日期后一天日期，不含时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTomorrow(Date date) {
		Calendar calendar = setCalendarDate(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, +1);
		return calendar.getTime();
	}

	/**
	 * 获得2个日期间工作日天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getWokingDays(Date beginDate, Date endDate) {
		if (beginDate == null) {
			beginDate = new Date();
		}
		if (endDate == null) {
			endDate = new Date();
		}
		Calendar calendarFir = Calendar.getInstance();
		calendarFir.setTimeInMillis(beginDate.getTime());
		Calendar calendarSec = Calendar.getInstance();
		calendarSec.setTimeInMillis(endDate.getTime());
		return getWorkingDay(calendarFir, calendarSec);
	}

	/**
	 * 计算2个日期之间的相隔天数
	 * 
	 * @param calendarFir
	 * @param calendarSec
	 * @return
	 */
	public static int getWorkingDay(Calendar calendarFir, Calendar calendarSec) {
		int result = -1;
		if (calendarFir.after(calendarSec)) { // 日期比较大小
			Calendar swap = calendarFir;
			calendarFir = calendarSec;
			calendarSec = swap;
		}

		int charge_start_date = 0;// 开始日期的日期偏移量
		int charge_end_date = 0;// 结束日期的日期偏移量
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - calendarFir.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - calendarSec.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			charge_end_date = etmp - 1;
		}
		result = (getDaysBetween(getNextMonday(calendarFir), getNextMonday(calendarSec)) / 7) * 5 + charge_start_date - charge_end_date;
		return result;
	}

	/**
	 * 获得指定日期前一天日期，不含时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYesterday(Date date) {
		Calendar calendar = setCalendarDate(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.add(Calendar.DATE, -1);
		return new Date(calendar.getTimeInMillis());
	}

	public static boolean isDate(String dateStr) {
		String regex = "^[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}$";
		return dateStr.matches(regex);
	}

	public static boolean isDateTime(String dateTime) {
		String regex = "^[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2} [0-9]{2}:[0-9]{1,2}$";
		return dateTime.matches(regex);
	}

	/**
	 * 是不是同一天
	 * 
	 * @param dateFir
	 * @param dateSec
	 * @return
	 */
	public static boolean isSameDay(Date dateFir, Date dateSec) {
		if (null == dateFir || null == dateSec) {
			return false;
		}
		String d1Str = formatDate(dateFir, "yyyy-MM-dd");
		return d1Str.equals(formatDate(dateSec, "yyyy-MM-dd"));
	}

	public static boolean isTime(String timeStr) {
		String regex = "^[0-9]{2}:[0-9]{1,2}:[0-9]{1,2}$";
		return timeStr.matches(regex);
	}

	public static void main(String[] args) {
		System.out.println(formatDate(getThisWeekDay(Calendar.MONDAY), YYYYMMDD));
		System.out.println(formatDate(getThisWeekDay(Calendar.SUNDAY), YYYYMMDD));
	}

	/**
	 * 数字转换日期
	 * 
	 * @param lDate
	 *            long/1000的数据
	 * @return Date
	 */
	public static Date numberToDate(Long lDate) {
		if (null == lDate) {
			return null;
		}
		return new Date(lDate.longValue() * ((long) 1000));
	}

	public static Date parseDateTime(String dateString, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		if (dateString == null || dateString.trim().equals("")) {
			return null;
		}
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException ignored) {
			ignored.printStackTrace();
		}
		return date;
	}

	private static Calendar setCalendarDate(Date date) {
		calendar.setTime(date);
		return calendar;
	}


}
