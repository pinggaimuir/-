package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.set.SynchronizedSortedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;

/**	
	* @description	生姜周价格指数数据
	* @author 		xjlong 
    * @date 		2016年8周30日  
*/
public class GinderWeekDataFetch {
	private static final String className = GinderWeekDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "生姜";
	private Log logger = LogFactory.getLog(GinderWeekDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static Map<String,String> weekPriceIndex_Map =  new HashMap<String, String>();
	static{
		//周价格指数映射
		weekPriceIndex_Map.put("定基指数","http://zs.jiang7.com/zhishu/list.php?catid=6&page=");
		weekPriceIndex_Map.put("环比指数","http://zs.jiang7.com/zhishu/list.php?catid=8&page=");
		weekPriceIndex_Map.put("同比指数","http://zs.jiang7.com/zhishu/list.php?catid=10&page=");
	}
	//抓取周价格指数数据
	public void fetchWeekPriceIndexData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "周价格指数";
		String contents = "";
		String pageUrl = "";
		int totalPage = 0;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header : weekPriceIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			System.out.println(header);
			if(header.equals("同比指数")){
				try {
					pageUrl = weekPriceIndex_Map.get(header);
					contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
					if(contents != null && !contents.equals("")){
						String b[]=(contents.split("\n"));
						timeInt = matchDate(b[0].split(",")[0]);
						int year = Integer.parseInt(timeInt.substring(0, 2));
						int week = Integer.parseInt(timeInt.substring(2));
						String timeStr = getDateByWeek(year,week,6);
						timeInt = DateTimeUtil.formatDate(strToDate(timeStr), "yyyyMMdd");
						dataMap.put(header,b[0].split(",")[1]);
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+header+"--"+timeInt+"--的数据。。。");
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
						}
					}else{
						logger.info("没有抓取到"+header+"数据");
						RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到"+header+"数据");
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("数据遍历出现异常...");
				}
				
			}else{
				pageUrl = weekPriceIndex_Map.get(header);
				contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
				if(contents != null && !contents.equals("")){
					String b[]=(contents.split("\n"));
					try {
						timeInt = matchDate(b[0].split(",")[0]);
						int year = Integer.parseInt(timeInt.substring(0, 2));
						int week = Integer.parseInt(timeInt.substring(2));
						String timeStr = getDateByWeek(year,week,6);
						timeInt = DateTimeUtil.formatDate(strToDate(timeStr), "yyyyMMdd");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					dataMap.put(header,b[0].split(",")[1]);
					if(!dataMap.isEmpty()){
						logger.info("正在保存--"+header+"--"+timeInt+"--的数据。。。");
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
						dataMap.clear();
					}else {
						RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
					}
				}else{
					logger.info("没有抓取到"+header+"数据");
					RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到"+header+"数据");
				}
			}
		}	
	}
	/*
	 *定时器：每周六上午十点更新数据 
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_GINDER_WEEK)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("生姜---周价格指数", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到生姜---周价格指数在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到生姜---周价格指数在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchWeekPriceIndexData(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取生姜---周价格指数的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取生姜---周价格指数的定时器已关闭");
			}
		}
	}
	/**
 	* @description	从字符串中提取出所有数字
	* @param  		contents,字符串
	* @throws ParseException 
	 */
	public String matchDate(String contents) throws ParseException{
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(contents); 
		String timeint = m.replaceAll("").trim();
		return m.replaceAll("").trim();
	}
	public Date strToDate(String timeStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(timeStr);
	}
	/**
	 	* @description	根据年份、周数、星期几获取对应日期
		* @param  year,年份 week，周数 day,星期几
		* @return 日期
	 */
	public String getDateByWeek(int year,int week,int day){
		  Date date = new Date(year,0,0);
		  long dayMS   	=  24*60*60*1000;  
		  long firstDay =   (7-date.getDay())*dayMS;  
		  long weekMS   =   (week-2)*7*dayMS;  
		  long result   =   date.getTime()+firstDay+weekMS+day*dayMS;  
		  date.setTime(result);
		  String timeStr = "20"+date.toLocaleString().substring(2);
		  return timeStr;
	}
	public static void main(String[] args) {
		GinderWeekDataFetch gw = new GinderWeekDataFetch();
		gw.fetchWeekPriceIndexData(new Date());
	}
}
