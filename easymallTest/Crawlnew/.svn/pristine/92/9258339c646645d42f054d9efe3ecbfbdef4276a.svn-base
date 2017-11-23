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
 * 期货行情数据—ICE棉花—合约库存
 * @author ctm
 *
 */
public class CottonStockFetch {
	private static final String className = CottonStockFetch.class.getName();
	private String cnName = "合约库存";
	private static final Log logger = LogFactory.getLog(CottonStockFetch.class);
	private static final Map<String, String> COTTON_STOCK_MAP = new HashMap<String, String>();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	
	static{
		COTTON_STOCK_MAP.put("ICE棉花", "https://www.theice.com/marketdata/reports/icefuturesus/CertifiedCottonReports.shtml");
	}
	public static final Map<String, String> AREA_MAP = new HashMap<String, String>();
	static{
		AREA_MAP.put("总计","sum");
		AREA_MAP.put("加尔维斯顿（德克萨斯州）","GALVESTON");
		AREA_MAP.put("格林威尔（南卡罗莱纳州）","GREENVILLE");
		AREA_MAP.put("休斯敦（德克萨斯州）","HOUSTON");
		AREA_MAP.put("孟斐斯（田纳西州）","MEMPHIS");
		}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_COTTON_STOCK)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("ICE棉花合约库存", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到"+cnName+"在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -1);
				fetchData(date);
			}else{
				logger.info("抓取"+cnName+"的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		String encoding = "gbk";
		for(String varName:COTTON_STOCK_MAP.keySet()){
			String url = COTTON_STOCK_MAP.get(varName);
			logger.info("start fetch " + varName +"-"+cnName + "@" + url);
			String[] params = {"table","class","table table-responsive table-data"};//通过params过滤table
			String[] rowColChoose = {"", ""};//选择哪几行哪几列
			String contents = dataFetchUtil.getPrimaryContent(0, url, encoding, varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
			}else{
				parseAndSave(contents, varName, DateTimeUtil.formatDate(date, "yyyyMMdd"));
			}
		}
	}
	private void parseAndSave(String contents, String varName, String timeInt) {
		      contents=contents.replaceAll("\\s{2}","");
		String[] lines = contents.split("\n");
		Map<String, String> areaMap = new HashMap<String, String>();
		String[] titles = lines[0].split(",");
		int currentStockIndex = 0;
		for(int i=0;i<titles.length;i++){
			if(titles[i].startsWith("Current Stock")){
				currentStockIndex = i;
				break;
			}
		}
		int lineSize = lines.length;
		for(int i=1;i<lineSize-1;i++){
			String[] tmp = lines[i].split(",");
			String area = tmp[0].split(" ")[0];
			if(!areaMap.containsKey(area)){
				areaMap.put(area, tmp[currentStockIndex]);
			}
		}
		areaMap.put("sum", lines[lineSize-1].split(",")[currentStockIndex]);
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String area:AREA_MAP.keySet()){
			dataMap.put(area, areaMap.get(AREA_MAP.get(area)));
		}
		logger.info("timeInt： "+timeInt);
		dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
	}
	

	
	public static void main(String []args){
		//new CottonStockFetch().start();
		Date date = DateTimeUtil.addDay(new Date(), -1);
		new CottonStockFetch().	fetchData(date);
	}
}
