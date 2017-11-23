package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	* @description	大蒜日产区日批发价格指数数据
	* @author 		xjlong 
    * @date 		2016年8月27日  
*/
public class GarlicDayDataFetch {
	private static final String className = GarlicDayDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "大蒜";
	private Log logger = LogFactory.getLog(GarlicDayDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static Map<String,String> dayProduceIndex_Map = new HashMap<String, String>();
	private static Map<String,String> dayWholesaleIndex_Map = new HashMap<String, String>();
	static{
		//日产去价格指数映射
		dayProduceIndex_Map.put("定基指数", "http://zs.51garlic.com/dszs/list.php?catid=5");
		//日批发价格指数映射
		dayWholesaleIndex_Map.put("定基指数", "http://zs.51garlic.com/dszs/list.php?catid=19");
	}
	//抓取日产区价格指数
	private void fetchDayData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "日产区价格指数";
		String contents = "";
		String pageUrl = "";
		String[] str=null;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header:dayProduceIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			pageUrl = dayProduceIndex_Map.get(header);
			contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filters, rowColChoose,1);
			if(contents != null && !contents.equals("")){
				String b[]=(contents.split("\n"));
					timeInt = b[0].split(",")[0];
					timeInt = timeInt.replace("/","");
					dataMap.put(header,b[0].split(",")[1]);
					if(!dataMap.isEmpty()){
						logger.info("正在保存--"+header+"--"+timeInt+"--的数据");
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
	
	//抓取日批发价格指数
	private void fetchDayWholesaleData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "日批发价格指数";
		String contents = "";
		String pageUrl = "";
		String[] str=null;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header:dayWholesaleIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			pageUrl = dayWholesaleIndex_Map.get(header);
			contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filters, rowColChoose,1);
			if(contents != null && !contents.equals("")){
				String b[]=(contents.split("\n"));
				timeInt = b[0].split(",")[0];
				timeInt = timeInt.replace("/","");
				dataMap.put(header,b[0].split(",")[1]);
				if(!dataMap.isEmpty()){
					logger.info("正在保存--"+header+"--"+timeInt+"--的数据");
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
	/*
	 * 定时器：每天十一点半更新数据
	 * */
	@Scheduled
	(cron=CrawlScheduler.CRON_CINDER_DAY)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("抓取大蒜日产去及日批发价格指数", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到大蒜日产区及日批发价格指数在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到大蒜日产区及日批发价格指数在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchDayData(date);
					fetchDayWholesaleData(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取大蒜日产区及日批发价格指数的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取大蒜日产区及日批发价格指数的定时器已关闭");
			}
		}
	}
	public static void main(String[] args) {
		GarlicDayDataFetch garlic = new GarlicDayDataFetch();
		//garlic.fetchDayData(new Date());
		garlic.fetchDayWholesaleData(new Date());
	}
}
