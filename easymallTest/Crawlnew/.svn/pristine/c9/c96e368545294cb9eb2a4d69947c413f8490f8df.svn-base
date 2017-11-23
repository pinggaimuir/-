package cn.futures.data.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;

public class CrawlerUtil {
	
	public static final Logger LOG = Logger.getLogger(CrawlerUtil.class);
	
	public static final int TIMEOUT = 3000;
	public static final int RETRY = 3;
	
	public static String httpGetBody (String url){
		return httpGetBody(url, url);
	}
	
	public static Document httpGetDoc (String url) {
		return httpGetDoc(url, url);
	}
	
	public static String httpGetBody (String url, String referrer)  {
		int retry = 0;
		Connection.Response response = null;
		while (retry < RETRY){
			try {
				response = Jsoup.connect(url)
						.referrer(referrer)//always means the url browsed before this url.
						.method(Method.GET)
						.ignoreContentType(true)						
						.timeout(TIMEOUT)
						.execute();
				break;
			} catch (IOException e) {
				LOG.warn("HTTP GET error, retry " + (++retry));
				LOG.warn(e);
			}
		}
		if (response != null) {
			return response.body();
		} else {
			return null;
		}
	}
	
	public static Document httpGetDoc (String url, String referrer)  {
		int retry = 0;
		Document doc = null;
		while (retry < RETRY){
			try {
				doc = Jsoup.connect(url)
						.referrer(referrer)
						.ignoreContentType(true)
						.timeout(TIMEOUT)
						.get();
				break;
			} catch (IOException e) {
				LOG.warn("HTTP GET error, retry " + (++retry));
				LOG.warn(e);
			}
		}
		return doc;
	}
	
	public static String findStrBetween(String in, String delim1, String delim2){
		int i = in.indexOf(delim1);
		int j = in.lastIndexOf(delim2);
		if (i == -1 || j == -1 || i == j){
			return "";
		}
		return in.substring(i+delim1.length(), j);
	}
	
	/**
	 * 通过传入的HTML片段，假设里面包含了一个table，将其转化为一个二维数组
	 * @param html
	 * @return
	 */
	public static String[][] getTable(String html){
		if( html == null )
			return null;
		return null;
	}

	public static int parseTimeint(String date, String format){
		
		SimpleDateFormat sdf1 = new SimpleDateFormat(format);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		
		try {
			String time = sdf2.format(sdf1.parse(date));
			int timeint = Integer.parseInt(time);
			return timeint;
		} catch (Exception e) {
			LOG.error(e);
		}
		return -1;
	}
	
	public static int todayTimeint(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String time = sdf.format(new Date());
		int timeint = Integer.parseInt(time);
		return timeint;
	}
	public static int todayTimeStartHourint(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd00");
		String time = sdf.format(new Date());
		int timeint = Integer.parseInt(time);
		return timeint;
	}
	public static int nowTimeintHour(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		String time = sdf.format(new Date());
		int timeint = Integer.parseInt(time);
		return timeint;
	}
	
	public static int yesterdayTimeint(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String time = sdf.format(cal.getTime());
		int timeint = Integer.parseInt(time);
		return timeint;
	}
	
	public static boolean isEmpty(MarketPrice p){
		return Math.round(p.getOpen()) == 0.0
				&& Math.round(p.getLast()) == 0.0;
	}	
}
