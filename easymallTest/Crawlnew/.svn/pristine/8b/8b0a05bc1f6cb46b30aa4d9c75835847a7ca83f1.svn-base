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
	* @description	大蒜主产区主销区相关价格数据
	* @author 		xjlong 
    * @date 		2016年8月27日  
*/
public class GarlicMonthDataFetch {
	private static final String className = GarlicMonthDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "大蒜";
	private Log logger = LogFactory.getLog(GarlicMonthDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static Map<String,String> exportPrice_Map = new HashMap<String, String>();
	private static Map<String,String> mainSalIndex_Map = new HashMap<String, String>();
	private static Map<String,String> mainProduceIndex_Map = new HashMap<String, String>();
	private static Map<String,String> mainProdeceBuy_Map = new HashMap<String, String>();
	private static Map<String,String> mainWholesaleMarket_Map = new HashMap<String, String>();
	static{
		//出口价格弹性
		exportPrice_Map.put("全国", "http://zs.51garlic.com/dszs/list.php?catid=28");
		//主销区价格指数
		mainSalIndex_Map.put("定基指数", "http://zs.51garlic.com/dszs/list.php?catid=21&page=");
		mainSalIndex_Map.put("环比指数", "http://zs.51garlic.com/dszs/list.php?catid=23&page=");
		mainSalIndex_Map.put("同比指数", "http://zs.51garlic.com/dszs/list.php?catid=44&page=");
		//主产区价格指数
		mainProduceIndex_Map.put("定基指数", "http://zs.51garlic.com/dszs/list.php?catid=7");
		mainProduceIndex_Map.put("环比指数", "http://zs.51garlic.com/dszs/list.php?catid=9");
		mainProduceIndex_Map.put("同比指数", "http://zs.51garlic.com/dszs/list.php?catid=42");
		//主产区收购价格
		mainProdeceBuy_Map.put("主产区均价", "http://www.51garlic.com/jiage/kwsj1/");
		//主销区批发市场价格
		mainWholesaleMarket_Map.put("北京新发地农副产品批发市场信息中心", "http://www.xinfadi.com.cn/marketanalysis/1/list/");
		/*mainWholesaleMarket_Map.put("天津金钟蔬菜批发市场", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160BFF6936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("无锡朝阳股份有限公司农产品批发市场", "http://www.99sj.com/Market/wxcy1026.htm");
		mainWholesaleMarket_Map.put("杭州良渚蔬菜批发交易市场", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160C0C0936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("包头", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160C031936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("成都农产品", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160C149936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("山东青岛李沧区沧口蔬菜副食品", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160C07D936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("浙江金华农产品批发市场", "http://www.jhncp.com/Price.aspx");
		mainWholesaleMarket_Map.put("河北保定", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160C003936CE040A8C020017257&newsearc=&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("河北石家庄桥西蔬菜中心批发市场", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=76E4F160BFFF936CE040A8C020017257&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");
		mainWholesaleMarket_Map.put("寿光市场", "http://jgsb.agri.gov.cn/controller?reportDate=&marketUuid=8493E07B04544142E0430A0207154142&SERVICE_ID=KAS_PFSC_TODAY_MARKETS_PRICES_SEARCH_SERVICE&recordperpage=15&newsearch=true&login_result_sign=nologin");*/
	}
	//抓取主销区价格指数
	private void fetchMainSalData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "主销区价格指数";
		String contents = "";
		String pageUrl = "";
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header : mainSalIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			System.out.println(header);
			try {
				pageUrl = mainSalIndex_Map.get(header);
				contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filters, rowColChoose,1);
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
					}else{
						timeInt = "20"+timeInt.substring(0, 2)+timeInt.substring(2);
					}
					dataMap.put(header,b[0].split(",")[1]);
					if(!dataMap.isEmpty()){
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
		}
	}
	//抓取主产区价格指数
	private void fetchMainProduceData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "主产区价格指数";
		String contents = "";
		String pageUrl = "";
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header : mainProduceIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			try {
				pageUrl = mainProduceIndex_Map.get(header);
				contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filters, rowColChoose,1);
				if(contents != null && !contents.equals("")){
					String b[]=(contents.split("\n"));
					timeInt = b[0].split(",")[0];
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
		}
	}
	
	//主销区批发市场价格数据抓取
	private void fetchMainWholesaleMarketData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "主销区批发市场价格";
		String contents = "";
		String pageUrl = "";
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header : mainWholesaleMarket_Map.keySet()){
			logger.info("正在抓取"+header+"的数据");
			if("北京新发地农副产品批发市场信息中心".equals(header)){
				String[] filters = {"table","tr"};
				String[] rowColChoose = {"001","111"};
				for(int i=3;i<30;i++){
					pageUrl = mainWholesaleMarket_Map.get(header)+i+".shtml";
					contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
					if(contents != null && !contents.equals("")){
						String b[]=(contents.split("\n"));
						for(int j = 0;j<b.length;j++){
							if(b[j].split(",")[0].equals(varName)){
								timeInt = b[j].split(",")[6];
								dataMap.put(header,b[j].split(",")[2]);
							}
							if(!dataMap.isEmpty()){
								dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
								dataMap.clear();
							}else {
								RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
							}
						}
					}
				}
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
	/*
	 * 定时器：每月十号十点更新数据
	 * */
	@Scheduled
	(cron=CrawlScheduler.CRON_CINDER_MONTH)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("抓取大蒜主产区主销区相关数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到大蒜主产区主销区数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到大蒜主产区主销区数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchMainProduceData(date);
					fetchMainSalData(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取大蒜主产区主销区数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取大蒜主产区主销区数据的定时器已关闭");
			}
		}
	}
	public static void main(String[] args) {
		GarlicMonthDataFetch gm = new GarlicMonthDataFetch();
		gm.fetchMainProduceData(new Date());
		//gm.fetchMainSalData(new Date());
		//gm.fetchMainWholesaleMarketData(new Date());
	}
}
