package util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取系统时间
 * 
 * @version 1.0 2007-11-30
 * @author puzg
 */
public class DateUtil {
	/* 日志对象 */
	// private static Logger logger = Logger.getLogger(SystemUtil.class);
	/* 获取年份 */
	public static final int YEAR = 1;
	/* 获取年月 */
	public static final int YEARMONTH = 2;
	/* 获取年月日 */
	public static final int YEARMONTHDAY = 3;
	/* 获取年月日，小时 */
	public static final int YMD_HOUR = 4;
	/* 获取年月日，小时，分钟 */
	public static final int YMD_HOURMINUTE = 5;
	/* 获取年月日，时分秒 */
	public static final int FULL = 6;
	/* 获取年月日时分秒 格式：yyyyMMddHHmmss */
	public static final int UTILTIME = 7;
	public static Date addDays(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + days);
		return calendar.getTime();
	}

	/**
	 * 根据指定时间格式类型得到当前时间
	 * 
	 * @param type
	 *            时间类型
	 * @return String 字符串时间
	 */
	public static synchronized String getCurrentTime(int type) {
		String format = getFormat(type);
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		Date date = new Date();
		return timeformat.format(date);
	}

	/**
	 * 返回当前系统时间的年月日
	 * 
	 * @return
	 */
	public static synchronized String getCurrentTime() {
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return timeformat.format(date);
	}

	/**
	 * 根据参数格式，格式化当前日期
	 * 
	 * @param format
	 * @return
	 */
	public static synchronized String getDateString(String format) {
		SimpleDateFormat timeformat = new SimpleDateFormat(format);
		Date date = new Date();
		return timeformat.format(date);
	}

	/**
	 * 根据指定时间格式类型，格式化时间格式
	 * 
	 * @param type
	 *            时间格式类型
	 * @return
	 */
	private static String getFormat(int type) {
		String format = "";
		if (type == 1) {
			format = "yyyy";
		} else if (type == 2) {
			format = "yyyy-MM";
		} else if (type == 3) {
			format = "yyyy-MM-dd";
		} else if (type == 4) {
			format = "yyyy-MM-dd HH";
		} else if (type == 5) {
			format = "yyyy-MM-dd HH:mm";
		} else if (type == 6) {
			format = "yyyy-MM-dd HH:mm:ss";
		} else if (type == 7) {
			format = "yyyyMMddHHmmss";
		} else {
			throw new RuntimeException("日期格式参数错误");
		}
		return format;
	}

	public static int getYear(String dateString) {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dd.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.YEAR);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static int getMonth(String dateString) {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dd.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.MONTH) + 1;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static int getDay(String dateString) {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dd.parse(dateString);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Date StringToDate(String dateStr, String formatStr) {
		SimpleDateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 当前日期和参数日期距离的小时数 日期格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static double getHours(String date) {
		SimpleDateFormat timeformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			Date d = new Date();
			Date d1 = timeformat.parse(date);

			long temp = d.getTime() - d1.getTime();
			double f = temp / 3600000d;
			BigDecimal b = new BigDecimal(f);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static int getAge(String birthDayString)  {
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd");
		Date birthDay = null;
		try {
			birthDay = timeformat.parse(birthDayString);
		} catch (ParseException e) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			return 0;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		} else {
			// monthNow<monthBirth
			// donothing
		}

		return age;
	}

	public static void main(String a[]) {
		try {
			int aa = getYear("2012-01-08");
			System.out.println(aa);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
