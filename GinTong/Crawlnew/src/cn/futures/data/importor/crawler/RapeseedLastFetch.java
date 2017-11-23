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

/**
 * 期货行情数据-WCE油菜籽-收盘价
 * @author ctm
 *
 */
public class RapeseedLastFetch {
	private static final String className = RapeseedLastFetch.class.getName();
	private static final Log logger = LogFactory.getLog(RapeseedLastFetch.class);
	private static final Map<String, String> RAPESEED_LAST_MAP = new HashMap<String, String>();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String cnName = "收盘价";
	static{
		RAPESEED_LAST_MAP.put("WCE油菜籽", "http://dashboard.albertacanola.com/agricharts?page=quote&sym=RSK15&mode=i");
	}
	public static final Map<String, String> MONTH_MAP = new HashMap<String, String>();
	static{
		MONTH_MAP.put("1月","Jan");
		MONTH_MAP.put("3月","Mar");
		MONTH_MAP.put("5月","May");
		MONTH_MAP.put("7月","Jul");
		MONTH_MAP.put("11月","Nov");
		}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_RAPESEED_LAST)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("WCE油菜籽收盘价", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到WCE油菜籽收盘价的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -1);
				fetchData(date);
			}else{
				logger.info("抓取WCE油菜籽收盘价的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		String encoding = "gbk";
		for(String varName:RAPESEED_LAST_MAP.keySet()){
			String url = RAPESEED_LAST_MAP.get(varName);
			logger.info("start fetch " + varName + "-" + cnName + "@" + url);
			String[] params = {"table","id","dt1"};//通过params过滤table
			String[] rowColChoose = {"00", "1100000000"};//选择哪几行哪几列
			String contents = dataFetchUtil.getPrimaryContent(1, url, encoding, varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
			}else{
				logger.info("开始分析数据");
				parseAndSave(contents, varName, DateTimeUtil.formatDate(date, "yyyyMMdd"));
			}
		}
	}
	/**
	 * 从抓取的内容中解析出1月3月5月7月11月合约各对应的油菜籽收盘价
	 * @param contents
	 * @param varId
	 * @param timeInt
	 */
	private void parseAndSave(String contents, String varName, String timeInt) {
		String[] lines = contents.split("\n");
		Map<String, String> lastMap = new HashMap<String, String>();
		for(String line:lines){
			String[] tmp = line.split(",");
			if(!dataFetchUtil.isNumeric(tmp[1])){
				tmp[1] = dataFetchUtil.getDigitByStr(tmp[1]);
			}
			String month = tmp[0].substring(0, 3);
			if(!lastMap.containsKey(month)){
				lastMap.put(month, tmp[1]);
			}
		}
		String nearbyStr = lines[0].split(",")[1]; 
		if(!dataFetchUtil.isNumeric(nearbyStr)){
			nearbyStr = dataFetchUtil.getDigitByStr(nearbyStr);
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("Nearby", nearbyStr);//最近一个月份合约
		for(String month:MONTH_MAP.keySet()){
			dataMap.put(month, lastMap.get(MONTH_MAP.get(month)));
		}
		logger.info("timeInt： "+timeInt);
		dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
	}
	
	public static void main(String []args){
		new RapeseedLastFetch().start();
//		RapeseedLastFetch r = new RapeseedLastFetch();
//		Date dataFetchDate = DateTimeUtil.parseDateTime("2016-08-03", DateTimeUtil.YYYY_MM_DD);
//		r.fetchData(dataFetchDate);
	}
}
