package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	* @description	生姜月价格指数数据
	* @author 		xjlong 
    * @date 		2016年8月30日  
*/
public class GinderMonthDataFetch {
	private static final String className = GinderMonthDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "生姜";
	private Log logger = LogFactory.getLog(GinderMonthDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static Map<String,String> monthPriceIndex_Map = new HashMap<String, String>();
	static{
		//月价格指数映射
		monthPriceIndex_Map.put("定基指数","http://zs.jiang7.com/zhishu/list.php?catid=7&page=");
		monthPriceIndex_Map.put("环比指数","http://zs.jiang7.com/zhishu/list.php?catid=9&page=");
		monthPriceIndex_Map.put("同比指数","http://zs.jiang7.com/zhishu/list.php?catid=11&page=");
	}
	//抓取月价格指数数据
	public void fetchMonthPriceIndexData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "月价格指数";
		String contents = "";
		String pageUrl = "";
		int totalPage = 0;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header : monthPriceIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			System.out.println(header);
			if(header.equals("同比指数")){
				try {
					pageUrl = monthPriceIndex_Map.get(header);
					contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
					if(contents != null && !contents.equals("")){
						String b[]=(contents.split("\n"));
							timeInt = matchDate(b[0].split(",")[0]);
							int month = Integer.parseInt(timeInt.substring(2));
							if(month < 10){
								timeInt = "20"+timeInt.substring(0, 2)+"0"+timeInt.substring(2);
							}
							dataMap.put(header,b[0].split(",")[1]);
							if(!dataMap.isEmpty()){
								logger.info("正在保存--"+header+"--"+timeInt+"--的数据...");
								dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
								dataMap.clear();
							}else {
								RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到生姜相关数据");
							}
						}else{
							logger.info("没有抓取到"+header+"数据");
							RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到"+header+"数据");
					}
				}catch (Exception e) {
					e.printStackTrace();
					logger.info("数据遍历出现异常...");
				}
				
			}else{
				pageUrl = monthPriceIndex_Map.get(header);
				contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
				if(contents != null && !contents.equals("")){
					String b[]=(contents.split("\n"));
					try {
						timeInt = matchDate(b[0].split(",")[0]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					int month = Integer.parseInt(timeInt.substring(2));
					if(month < 10){
						timeInt = "20"+timeInt.substring(0, 2)+"0"+timeInt.substring(2);
					}
					dataMap.put(header,b[0].split(",")[1]);
					if(!dataMap.isEmpty()){
						logger.info("正在保存--"+header+"--"+timeInt+"--的数据...");
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
	 *定时器：每月10号的10:30更新数据
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_CINDER_MONTH)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("生姜---月价格指数", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到生姜---月价格指数在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到生姜---月价格指数在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchMonthPriceIndexData(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取生姜---月价格指数的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取生姜---月价格指数的定时器已关闭");
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
	public static void main(String[] args) {
		GinderMonthDataFetch gd = new GinderMonthDataFetch();
		gd.fetchMonthPriceIndexData(new Date());
	}
}
