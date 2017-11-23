package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;

/**
 * 美元兑换
 * @author ctm
 *
 */
@Service
public class USDollarExgData {
	private static final String className = USDollarExgData.class.getName();
	private DAOUtils dao = new DAOUtils();
	private Log logger = LogFactory.getLog(USDollarExgData.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private String url = "http://www.123cha.com/hl/?from=USD&to=%to%&q=100";
	private static Map<String, String> header2codeMap = new HashMap<String, String>();
	static{
		header2codeMap.put("欧元", "EUR");
		header2codeMap.put("英镑", "GBP");
		header2codeMap.put("日元", "JPY");
		header2codeMap.put("澳元", "AUD");
		header2codeMap.put("新西兰元", "NZD");
		header2codeMap.put("加元", "CAD");
		header2codeMap.put("港元", "HKD");
		header2codeMap.put("人民币", "CNY");
		header2codeMap.put("雷亚尔", "BRL");
		header2codeMap.put("阿根廷比索", "ARS");
		header2codeMap.put("马来西亚令吉", "MYR");
		header2codeMap.put("印度卢比", "INR");
		header2codeMap.put("韩元", "KRW");
		header2codeMap.put("墨西哥比索", "MXN");
		header2codeMap.put("泰铢", "THB");
		header2codeMap.put("秘鲁索尔", "PEN");
		header2codeMap.put("卢布", "RUB");
		header2codeMap.put("印尼盾", "IDR");
		header2codeMap.put("智利比索", "CLP");
		header2codeMap.put("南非兰特", "ZAR");
		header2codeMap.put("新台币", "TWD");
		header2codeMap.put("瑞士法郎", "CHF");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_USDOLLOR_EXG_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("美元兑主要货币", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到美元兑主要货币的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到美元兑主要货币的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();				
				try{
					fetch(date);
				}catch(Exception e){
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取美元兑主要货币的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取美元兑主要货币的定时器已关闭");
			}
		}
	}
	
	public void fetch(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String varName = "全球利率和汇率";
		int varId = Variety.getVaridByName(varName);
		String cnName = "美元兑主要货币";
		String contents = "";
		String pageUrl = "";
		String[] filters = {"table", "class", "tb"};
		String[] rowColChoose = {"001", "010"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header:header2codeMap.keySet()){
			logger.info("抓美元兑换"+header+"数据");
			try {
				logger.info("sleep 5 seconds ...");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("InterruptedException:", e);
			}
			pageUrl = url.replaceAll("%to%", header2codeMap.get(header));
			contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose, 0);
			if(contents != null && !contents.equals("")){
				dataMap.put(header, contents.split("\n")[0]);
			}else{
				logger.info("没有抓取到美元兑换"+header+"数据");
				RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到美元兑换"+header+"数据");
			}
		}
		if(!dataMap.isEmpty()){
			dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
		} else {
			RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到美元兑主要货币数据");
		}
	}
	
	public static void main(String[] args){
		//new USDollarExgData().fetch(DateTimeUtil.parseDateTime("20151108","yyyyMMdd"));
		//new USDollarExgData().fetch(DateTimeUtil.parseDateTime("20151109","yyyyMMdd"));
		//new USDollarExgData().fetch(DateTimeUtil.parseDateTime("20151109","yyyyMMdd"));
		//new USDollarExgData().fetch(DateTimeUtil.parseDateTime("20151109","yyyyMMdd"));
		new USDollarExgData().fetch(new Date());
	}
}
