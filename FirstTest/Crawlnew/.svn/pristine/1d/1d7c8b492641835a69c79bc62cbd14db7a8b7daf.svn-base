package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 油料油脂-大豆-港口分销价
 * 日度，每天下午15:00抓取当天数据
 * 由于网站格式变动较频繁，改为人工更新（2017-02-16）
 * */
public class SoybeanPortDistributionPrices {
	
	private static String className = SoybeanPortDistributionPrices.class.getName();
	private static final String url = "http://www.feedtrade.com.cn/soybean/soybean_port/";//列表页链接
	private static final String encoding = "gb2312";
	private static final String varName = "大豆";
	private static final String cnName = "港口分销价";
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(SoybeanPortDistributionPrices.class);
	
//	@Scheduled(cron = CrawlScheduler.CRON_SOYBEANPORTDISTPRICE)//CRON_SOYBEANPORTDISTPRICE = "0 0 15 * * ?"
	public void start(){

		String switchFlag = new CrawlerManager().selectCrawler("大豆港口分销价", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到大豆港口分销价在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date curDay = new Date();
					String timeIntStr = DateTimeUtil.formatDate(curDay, DateTimeUtil.YYYYMMDD);
					String month = timeIntStr.substring(4, 6);
					String day = timeIntStr.substring(6, 8);
					if(month.startsWith("0")){
						month = month.substring(1);
					}
					if(day.startsWith("0")){
						day = day.substring(1);
					}
					String detailUrl = fetchUrl(month, day);
					fetchData(detailUrl, timeIntStr);
				} catch(Exception e){
					LOG.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, "美国大豆", "现货理论榨利", "发生未知异常。");
				}
			}else{
				LOG.info("抓取大豆港口分销价的定时器已关闭");
			}
		}
	}
	
	/**
	 * 从列表页获取详情页链接
	 * */
	public String fetchUrl(String month, String day){
		String detailUrl = null;//详情页链接
		String content = fetchUtil.getCompleteContent(0, url, encoding, varName + cnName + "列表页.txt");
		String urlRegex = "<a href=\"([^\"]+)\" title=\"" + month + "月" + day + "日进口大豆港口分销价格数据表\">";
		List<String> urlList = RegexUtil.getMatchStr(content, urlRegex, new int[]{1});
		if(urlList != null && !urlList.isEmpty()){
			detailUrl = urlList.get(0);
		}
		return detailUrl;
	}
	
	public void fetchData(String detailUrl, String timeIntStr){
		String[] filters = {"div", "class", "ReplyContent"};
		String table = fetchUtil.getPrimaryContent(0, detailUrl, encoding, varName, filters, null, 0);
		String colRegex = "<td[^>]*>港口/省市</td>\\s*<td[^>]*>品质/等级</td>\\s*<td[^>]*>当日价格</td>";//匹配列名行
		String lianRegex = "<td[^>]*>连云港</td>\\s*<td[^>]*>美湾豆</td>\\s*<td[^>]*>([\\d,]+)</td>";//连云港-美湾豆 正则式
		String qingRegex = "<td[^>]*>青岛市</td>\\s*<td[^>]*>美湾豆</td>\\s*<td[^>]*>([\\d,]+)</td>";//青岛市-美湾豆 正则式
		String riRegex = "<td[^>]*>日照市</td>\\s*<td[^>]*>美西豆</td>\\s*<td[^>]*>([\\d,]+)</td>";//日照市-美西豆 正则式
		String priceRegex = "[\\d,]+";//匹配价格数据
		
		List<String> matchList = RegexUtil.getMatchStr(table, colRegex);
		if(matchList != null && !matchList.isEmpty() && !matchList.get(0).isEmpty()){
			Map<String, String> dataMap = new HashMap<String, String>();
			int[] index = {1};
			matchList.clear();
			matchList = RegexUtil.getMatchStr(table, lianRegex, index);
			if(!matchList.isEmpty() && matchList.get(0).matches(priceRegex)){
				dataMap.put("连云港", matchList.get(0).replace(",", ""));
			}
			matchList.clear();
			matchList = RegexUtil.getMatchStr(table, qingRegex, index);
			if(!matchList.isEmpty() && matchList.get(0).matches(priceRegex)){
				dataMap.put("青岛", matchList.get(0).replace(",", ""));
			}
			matchList.clear();
			matchList = RegexUtil.getMatchStr(table, riRegex, index);
			if(!matchList.isEmpty() && matchList.get(0).matches(priceRegex)){
				dataMap.put("日照", matchList.get(0).replace(",", ""));
			}
			matchList.clear();
			if(!dataMap.isEmpty()){
				dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), dataMap);
			} else {
				LOG.info("数据为空。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "数据为空");
			}
		} else {
			LOG.info("数据源格式发生变化。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "数据源格式发生变化。");
		}
	}
	
	public static void main(String[] args) {
		SoybeanPortDistributionPrices s = new SoybeanPortDistributionPrices();
		Date startDay = DateTimeUtil.parseDateTime("2016-12-27", DateTimeUtil.YYYY_MM_DD);
		while(startDay.before(new Date())){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String timeIntStr = DateTimeUtil.formatDate(startDay, DateTimeUtil.YYYYMMDD);
			String month = timeIntStr.substring(4, 6);
			String day = timeIntStr.substring(6, 8);
			if(month.startsWith("0")){
				month = month.substring(1);
			}
			if(day.startsWith("0")){
				day = day.substring(1);
			}
			String detailUrl = s.fetchUrl(month, day);
			s.fetchData(detailUrl, timeIntStr);
			startDay = DateTimeUtil.addDay(startDay, 1);
		}
		//补指定日期数据
//		String timeIntStr = "20170210";
//		String month = timeIntStr.substring(4, 6);
//		String day = timeIntStr.substring(6, 8);
//		if(month.startsWith("0")){
//			month = month.substring(1);
//		}
//		if(day.startsWith("0")){
//			day = day.substring(1);
//		}
//		String detailUrl = s.fetchUrl(month, day);
//		s.fetchData(detailUrl, timeIntStr);
	}
}
