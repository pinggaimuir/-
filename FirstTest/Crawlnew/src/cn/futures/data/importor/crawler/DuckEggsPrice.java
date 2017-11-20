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
 * 肉禽禽蛋周度数据爬虫1
 * 肉禽：鸭种蛋价格、淘汰肉种鸭价格。    禽蛋：鸭蛋出厂价。
 * @author bric_yangyulin
 * @date 2016-09-12
 * */
public class DuckEggsPrice {
	private static String className = DuckEggsPrice.class.getName();
	private String encoding = "utf-8";
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(DuckEggsPrice.class);
	
	//周度，每周五上午九点抓取当天数据。
	@Scheduled(cron = CrawlScheduler.CRON_DUCKEGGS_PRICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("肉禽禽蛋周度数据1", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到肉禽禽蛋周度数据1爬虫的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到肉禽禽蛋周度数据1爬虫的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date today = new Date();
				String timeIntStr = DateTimeUtil.formatDate(today, DateTimeUtil.YYYYMMDD);
				fetchData(timeIntStr);
			}else{
				LOG.info("抓取肉禽禽蛋周度数据1的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取肉禽禽蛋周度数据1的定时器已关闭");
			}
		}
	}
	
	//禽肉：白条鸭批发市场报价    周度、每周五上午10点，抓取当天数据
	@Scheduled(cron = CrawlScheduler.CRON_WHITEBARSDUCK_PRICE)
	public void start2(){
		String switchFlag = new CrawlerManager().selectCrawler("肉禽禽蛋周度数据2", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到肉禽禽蛋周度数据2爬虫的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到肉禽禽蛋周度数据2爬虫的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date today = new Date();
				String timeIntStr = DateTimeUtil.formatDate(today, DateTimeUtil.YYYYMMDD);
				try{
					LOG.info("--------开始抓取禽肉-白条鸭批发市场报价--------");
					parseWhitebarsduck(timeIntStr);
				} catch(Exception e){
					LOG.error(e);
					RecordCrawlResult.recordFailData(className, "禽肉", "白条鸭批发市场报价", "发生未知异常");
				}
			}else{
				LOG.info("抓取肉禽禽蛋周度数据2的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取肉禽禽蛋周度数据2的定时器已关闭");
			}
		}
	}
	
	/**
	 * 饲料养殖-禽肉-白条鸭批发市场报价
	 * */
	public void parseWhitebarsduck(String timeIntStr){
		int timeInt = Integer.parseInt(timeIntStr);
		StringBuilder expectStr = new StringBuilder(timeIntStr);
		expectStr.insert(6, '月');
		expectStr.insert(4, '年');
		if('0' == expectStr.charAt(5)){
			expectStr.deleteCharAt(5);
		}
		String varName = "禽肉";
		String cnName = "白条鸭批发市场报价";
		String listPageUrl = "http://www.intedc.com/market/list-219.html";//列表页url
		String[] listFilters = {"div", "class", "catlist"};
		String content = dataFetchUtil.getPrimaryContent(0, listPageUrl, encoding, varName, listFilters, null, 0);//行情列表页内容
		String compStr = "<li[^\\n]*href=\"(\\S+)\"[^\\n]*title=\"" + expectStr + "日白条鸭批发市场报价\"[^\\n]*";
		int[] index = {1};
		List<String> catlist = RegexUtil.getMatchStr(content, compStr, index);
		if(catlist != null && !catlist.isEmpty()){
			String[] filters = {"table", "summary", "鸭子网白条鸭价格表"};
			String[] rowColChoose = {"", "10100"};
			content = dataFetchUtil.getPrimaryContent(0, catlist.get(0), encoding, varName, filters, rowColChoose, 0);

			Map<String, String> priceByProv = new HashMap<String, String>();
			Map<String, Float> sumByProv = new HashMap<String, Float>();//省-价格和
			Map<String, Integer> countByProv = new HashMap<String, Integer>();//省-价格个数
			String[] priceInfos = content.split("\n");
			float sum = 0;//用于计算全国平均值
			int count = 0;//用于计算全国平均值
			for(int i = 1; i < priceInfos.length; i++){
				String[] provPrice = priceInfos[i].split(",");
				String prov = provPrice[0].substring(0, provPrice[0].length()-1);
				if(provPrice[1].matches("\\d+(\\.\\d+)?")){
					if(sumByProv.containsKey(prov)){
						sumByProv.put(prov, sumByProv.get(prov) + Float.parseFloat(provPrice[1]));
						countByProv.put(prov, countByProv.get(prov) + 1);
					} else {
						sumByProv.put(prov, Float.parseFloat(provPrice[1]));
						countByProv.put(prov, 1);
					}
				}
				sum += Float.parseFloat(provPrice[1]);
				count++;
			}
			for(String prov: sumByProv.keySet()){
				float aveProv = sumByProv.get(prov) / countByProv.get(prov);
				priceByProv.put(prov, String.valueOf(aveProv));
			}
			priceByProv.put("全国", String.valueOf(sum / count));
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, priceByProv);
		}
	}
	
	public void fetchData(String timeIntStr){
		int timeInt = Integer.parseInt(timeIntStr);
		StringBuilder expectStr = new StringBuilder(timeIntStr);
		expectStr.insert(6, '月');
		expectStr.insert(4, '年');
		if('0' == expectStr.charAt(5)){
			expectStr.deleteCharAt(5);
		}
		try{
			LOG.info("------------开始抓取肉禽-鸭种蛋价格------------");
			this.fetchDuckEggs(timeInt, expectStr.toString());
		} catch(Exception e) {
			LOG.error(e);
			RecordCrawlResult.recordFailData(className, "肉禽", "鸭种蛋价格", "发生未知异常");
		}
		try{
			LOG.info("------------开始抓取肉禽-淘汰肉种鸭价格------------");
			this.parseOutMeatDuck(timeInt, expectStr.toString());
		} catch(Exception e) {
			LOG.error(e);
			RecordCrawlResult.recordFailData(className, "肉禽", "淘汰肉种鸭价格", "发生未知异常");
		}
		try{
			LOG.info("------------开始抓取肉禽-淘汰肉种鸭价格------------");
			this.parseDuckFarmgate(timeInt, expectStr.toString());
		} catch(Exception e) {
			LOG.error(e);
			RecordCrawlResult.recordFailData(className, "禽蛋", "鸭蛋出场价", "发生未知异常");
		}
	}
	
	/**
	 * 饲料养殖-肉禽-鸭种蛋价格
	 * */
	public void fetchDuckEggs(int timeInt, String expectStr){
		String varName = "肉禽";
		String cnName = "鸭种蛋价格";
		String listPageUrl = "http://www.intedc.com/market/list-176.html";//列表页url
		String[] listFilters = {"div", "class", "catlist"};
		String content = dataFetchUtil.getPrimaryContent(0, listPageUrl, encoding, varName, listFilters, null, 0);//行情列表页内容
		String compStr = "<li[^\\n]*href=\"(\\S+)\"[^\\n]*title=\"" + expectStr + "日全国鸭种蛋价格行情\"[^\\n]*";
		int[] index = {1};
		List<String> catlist = RegexUtil.getMatchStr(content, compStr, index);
		if(catlist != null && !catlist.isEmpty()){
			String[] filters = {"table", "summary", "鸭子网[www.intedc.com]全国鸭种蛋价格行情表"};
			String[] rowColChoose = {"", "110"};
			content = dataFetchUtil.getPrimaryContent(0, catlist.get(0), encoding, varName, filters, rowColChoose, 0);
			Map<String, String> priceByProv = duckEggsPrice(content);
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, priceByProv);
		}
	}
	public Map<String, String> duckEggsPrice(String content){
		Map<String, Float> priceByProv = new HashMap<String, Float>();
		String[] rowLine = content.split("\n");
		float priceSum = 0; //全国参考价均值
		int priceNum = 0;//参考价个数
		for(int rowI = 1; rowI < rowLine.length; rowI++){
			String[] tempArray = rowLine[rowI].split(",");
			if(tempArray.length == 2 && tempArray[1].matches("\\d+(\\.\\d+)?")){
				String prov = null;
				if(tempArray[0].contains("-")){
					prov = tempArray[0].substring(0, tempArray[0].indexOf("-"));
				} else {
					prov = tempArray[0];
				}
				if(priceByProv.containsKey(prov)){
					priceSum = priceSum + Float.parseFloat(tempArray[1]);
					priceNum++;
					priceByProv.put(prov, (priceByProv.get(prov) + Float.parseFloat(tempArray[1])) / 2);
				} else {
					priceSum = priceSum + Float.parseFloat(tempArray[1]);
					priceNum++;
					priceByProv.put(prov, Float.parseFloat(tempArray[1]));
				}
			}
		}
		if(!priceByProv.isEmpty()){
			priceByProv.put("全国", priceSum/priceNum);
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String prov: priceByProv.keySet()){
			dataMap.put(prov, String.valueOf(priceByProv.get(prov)));
		}
		return dataMap;
	}
	
	/**
	 * 饲料养殖-肉禽-淘汰肉种鸭价格
	 * */
	public void parseOutMeatDuck(int timeInt, String expectStr){
		String varName = "肉禽";
		String cnName = "淘汰肉种鸭价格";
		String listPageUrl = "http://www.intedc.com/market/list-220.html";
		String[] listFilters = {"div", "class", "catlist"};
		String content = dataFetchUtil.getPrimaryContent(0, listPageUrl, encoding, varName, listFilters, null, 0);//行情列表页内容
		String compStr = "<li[^\\n]*href=\"(\\S+)\"[^\\n]*title=\"" + expectStr + "日淘汰肉种鸭参考报价\"[^\\n]*";
		int[] index = {1};
		List<String> catlist = RegexUtil.getMatchStr(content, compStr, index);
		if(catlist != null && !catlist.isEmpty()){
			String[] filters = {"div", "id", "article"};
			content = dataFetchUtil.getPrimaryContent(0, catlist.get(0), encoding, varName, filters, null, 0);
			Map<String, String> priceByProv = new HashMap<String, String>();
			List<String> prices = RegexUtil.getMatchStr(content, "四川(\\d+(\\.\\d+)?)元/斤.*北方(\\d+(\\.\\d+)?)元/斤", new int[]{1, 3});
			priceByProv.put("四川", prices.get(0));
			priceByProv.put("北方", prices.get(1));
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, priceByProv);
		}
	}
	
	/**
	 * 饲料养殖-禽蛋-鸭蛋出场价
	 * */
	public void parseDuckFarmgate(int timeInt, String expectStr){
		String varName = "禽蛋";
		String cnName = "鸭蛋出场价";
		String listPageUrl = "http://www.intedc.com/market/list-176.html";//列表页url
		String[] listFilters = {"div", "class", "catlist"};
		String content = dataFetchUtil.getPrimaryContent(0, listPageUrl, encoding, varName, listFilters, null, 0);//行情列表页内容
		String compStr = "<li[^\\n]*href=\"(\\S+)\"[^\\n]*title=\"" + expectStr + "日全国鸭蛋价格行情\"[^\\n]*";
		int[] index = {1};
		List<String> catlist = RegexUtil.getMatchStr(content, compStr, index);
		if(catlist != null && !catlist.isEmpty()){
			String[] filters = {"div", "id", "article"};
//			String[] filters = {"table", "thead"};
			content = dataFetchUtil.getPrimaryContent(0, catlist.get(0), encoding, varName, filters, null, 0);
			Map<String, String> priceByProv = priceDuckFarmgate(content);
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, priceByProv);
		}
	}
	public Map<String, String> priceDuckFarmgate(String content){
		Map<String, String> priceByProv = new HashMap<String, String>();//省-均价
		content = content.split("</?tbody>")[1];
		String[] trs = content.split("<tr>");
		String curProv = null;//当前处理省份
		int curCount = 0;//当前省份数据计数
		float curSum = 0f;//当前省份数据和
		String priceRegex = "\\d+(\\.\\d+)?";//价格数据
		String proveRegex = "[-\u4E00-\u9FA5]+";//省份数据
		for(String tr: trs){
			String[] tds = tr.split("<td[^>]*>");
			for(int i = 0; i < tds.length; i++){
				tds[i] = tds[i].replaceAll("\\s", "").replaceAll("<[^>]*>", "");
			}
			if(tds.length == 8){
				if(tds[1].matches(proveRegex) && tds[2].matches(priceRegex) && tds[3].matches(priceRegex) && curProv != null){
					curCount++;
					curSum += Float.parseFloat(tds[3]);
				} else {
					//不符合格式要求
					LOG.error("不符合格式要求8");
				}
			} else if(tds.length == 9) {
				if(tds[1].matches(proveRegex) && tds[2].matches(proveRegex) && tds[3].matches(priceRegex) && tds[4].matches(priceRegex)){
					//保存上一个省份的均值
					if(curProv != null){
						priceByProv.put(curProv, String.valueOf(curSum / curCount));
					}
					//记录新省份的数据
					curProv = tds[1];
					curCount = 1;//重置
					curSum = Float.parseFloat(tds[4]);
				} else {
					//不符合格式要求
					LOG.error("不符合格式要求9");
				}
			}
		}
		if(!priceByProv.containsKey(curProv)){
			priceByProv.put(curProv, String.valueOf(curSum / curCount));
		}
		if(priceByProv.containsKey("均价")){
			priceByProv.put("全国", priceByProv.get("均价"));
			priceByProv.remove("均价");
		}
		
		return priceByProv;
		
	}
	
	public static void main(String[] args) {
		DuckEggsPrice d = new DuckEggsPrice();
		d.start();
		d.start2();
//		d.fetchData("20160916");
//		d.parseWhitebarsduck("20160916");
	}
}
