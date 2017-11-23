package cn.futures.data.importor.crawler;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 饲料养殖-淀粉-主销区玉米淀粉成交价、主要区域玉米淀粉市场价格
 * */
public class CornstarchPrice {

	private static String className = CornstarchPrice.class.getName();
	private static final String varName = "淀粉";
	private static final Map<String, String> signByCN = new LinkedHashMap<String, String>();
	private static final String[] hammerCols = {"上海", "泉州", "广州", "成都", "重庆"};//主销区玉米淀粉成交价 所含列
	private static final String[] marketCols = {"哈尔滨", "长春", "沈阳", "济宁", "苏州", "上海", "杭州", "广州", "泉州"};//主要区域玉米淀粉市场价格 所含列
	private static final String listUrl = "http://www.siacn.org/index.php?optionid=1078";//列表页链接
	private static final String encoding = "utf-8";
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(CornstarchPrice.class);
	
	static{
		signByCN.put("主销区玉米淀粉成交价", "销区淀粉价格");
		signByCN.put("主要区域玉米淀粉市场价格", "淀粉价格信息");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_CORNSTARCHPRICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("玉米淀粉成交价市场价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到玉米淀粉成交价市场价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date curDay = new Date();
				Date lastWeekDay = null;
				if(DateTimeUtil.getChineseWeek(curDay).equals("星期一")){
					lastWeekDay = DateTimeUtil.addDay(curDay, -3);
				} else {
					lastWeekDay = DateTimeUtil.addDay(curDay, -1);
				}
				//重新抓取上一次的数据（因为有时会有延迟）
				try{
					fetch(lastWeekDay);
				} catch(Exception e) {
					LOG.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, varName, null, "抓取上一天数据发生未知异常。");
				}
				//抓取当天数据
				try{
					fetch(curDay);
				} catch(Exception e) {
					LOG.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, varName, null, "抓取当天数据发生未知异常。");
				}
			}else{
				LOG.info("抓取玉米淀粉成交价市场价格的定时器已关闭");
			}
		}
	}
	
	public void fetch(Date date){
		Map<String, String> detailByCN = fetchDetailUrl(date);
		String timeIntStr = DateTimeUtil.formatDate(date, DateTimeUtil.YYYYMMDD);
		Map<String, String> hammerData = null;//成交价
		Map<String, String> marketData = null;//市场价
		String hammerName = "主销区玉米淀粉成交价";
		String marketName = "主要区域玉米淀粉市场价格";
		if(detailByCN.get(hammerName) != null){//有新数据用新数据
			hammerData = fetchPrice(detailByCN.get(hammerName), hammerName, hammerCols);
			if(hammerData == null){
				int varId = Variety.getVaridByName(varName);
				List<String> citys = Arrays.asList(hammerCols);
				List<String> newestData = dao.getNewestListValues(varId, hammerName, Arrays.asList(hammerCols));
				hammerData = new HashMap<String, String>();
				//计算平均价
				double sum = 0;
				int num = 0;
				for(int index = 0; index < citys.size(); index++){
					hammerData.put(citys.get(index), newestData.get(index));
					sum += Double.parseDouble(newestData.get(index));
					num++;
				}
				hammerData.put("平均价", String.valueOf(sum / num));
			} else {
				//计算平均价
				double sum = 0;
				int num = 0;
				for(String city: hammerData.keySet()){
					sum += Double.parseDouble(hammerData.get(city));
					num++;
				}
				hammerData.put("平均价", String.valueOf(sum / num));
			}
		} else {//无新数据沿用旧数据
			int varId = Variety.getVaridByName(varName);
			List<String> citys = Arrays.asList(hammerCols);
			List<String> newestData = dao.getNewestListValues(varId, hammerName, Arrays.asList(hammerCols));
			hammerData = new HashMap<String, String>();
			//计算平均价
			double sum = 0;
			int num = 0;
			for(int index = 0; index < citys.size(); index++){
				hammerData.put(citys.get(index), newestData.get(index));
				sum += Double.parseDouble(newestData.get(index));
				num++;
			}
			hammerData.put("平均价", String.valueOf(sum / num));
		}
		if(detailByCN.get(marketName) != null){//有新数据用新数据
			marketData = fetchPrice(detailByCN.get(marketName), marketName, marketCols);
			if(marketData == null){
				int varId = Variety.getVaridByName(varName);
				List<String> citys = Arrays.asList(marketCols);
				List<String> newestData = dao.getNewestListValues(varId, marketName, Arrays.asList(marketCols));
				marketData = new HashMap<String, String>();
				for(int index = 0; index < citys.size(); index++){
					marketData.put(citys.get(index), newestData.get(index));
				}
			}
		} else {//无新数据沿用旧数据
			int varId = Variety.getVaridByName(varName);
			List<String> citys = Arrays.asList(marketCols);
			List<String> newestData = dao.getNewestListValues(varId, marketName, Arrays.asList(marketCols));
			marketData = new HashMap<String, String>();
			for(int index = 0; index < citys.size(); index++){
				marketData.put(citys.get(index), newestData.get(index));
			}
		}
		//成都、重庆两市的市场价使用成交价
		marketData.put("成都", hammerData.get("成都"));
		marketData.put("重庆", hammerData.get("重庆"));
		//计算成交价平均价
		double sumMarket = 0;
		int numMaket = 0;
		for(String city: marketData.keySet()){
			sumMarket += Double.parseDouble(marketData.get(city));
			numMaket++;
		}
		marketData.put("平均价", String.valueOf(sumMarket / numMaket));
		//入库
		int timeInt = Integer.parseInt(timeIntStr);
		dao.saveOrUpdateByDataMap(varName, hammerName, timeInt, hammerData);
		dao.saveOrUpdateByDataMap(varName, marketName, timeInt, marketData);
	}
	
	/**
	 * 获取详情页链接
	 * */
	public Map<String, String> fetchDetailUrl(Date date){
		Map<String, String> detailByCN = new LinkedHashMap<String, String>();
		String timeStr = DateTimeUtil.formatDate(date, "yyyy.MM.dd");
		timeStr = timeStr.replace(".0", ".");
		String content = fetchUtil.getCompleteContent(0, listUrl, encoding, varName + "list.txt");
		if(content != null){
			for(String cnName: signByCN.keySet()){
				String hrefRegex = "<a href=\"([^\"]+)\" title=\"" + signByCN.get(cnName) + "（" + timeStr + "）\"[^>]*>";
				List<String> urlList = RegexUtil.getMatchStr(content, hrefRegex, new int[]{1});
				if(urlList != null && !urlList.isEmpty() && !urlList.get(0).isEmpty()){
					detailByCN.put(cnName, urlList.get(0));
				} else {
					LOG.warn("没有获取到详情页链接。");
					RecordCrawlResult.recordFailData(className, varName, cnName, "没有获取到详情页链接。");
				}
			}
		} else {
			LOG.warn("没有获取到详情页链接。");
			RecordCrawlResult.recordFailData(className, varName, null, "抓取列表页失败。");
		}
		return detailByCN;
	}
	
	/**
	 * 获取数据
	 * */
	public Map<String, String> fetchPrice(String detailUrl, String cnName, String[] colNames){
		String[] filters = {"div", "class", "content"};
		String content = fetchUtil.getPrimaryContent(0, detailUrl, encoding, varName + "_" + cnName, filters, null, 0);
		content = content.replaceAll("<[^>]*>"," ").replaceAll("\\s", " ").replace("&nbsp;", " ");
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String colName: colNames){
			List<String> priceList = RegexUtil.getMatchStr(content, colName + "\\s*([\\d,]+)[-－]([\\d,]+)", new int[]{1, 2});
			if(priceList.size() == 2 && priceList.get(0) != null && priceList.get(1) != null){
				int lowPrice = Integer.parseInt(priceList.get(0).replace(",", ""));//较低价格
				int highPrice = Integer.parseInt(priceList.get(1).replace(",", ""));//较高价格
				double average = (lowPrice + highPrice) / 2.0;//平均价格
				dataMap.put(colName, String.valueOf(average));
			}
		}
		if(dataMap.isEmpty()){
			dataMap = null;
			LOG.warn(cnName + "获取的新数据为空。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "获取的新数据为空。");
		} else if(dataMap.size() != colNames.length) {
			LOG.warn(cnName + "获取的新数据不完整。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "获取的新数据不完整。");
		}
		return dataMap;
	}
	
	public static void main(String[] args) {
		CornstarchPrice c = new CornstarchPrice();
//		c.start();
		
		//补指定日期的历史数据
		String targetTime = "20161107";
		c.fetch(DateTimeUtil.parseDateTime(targetTime, "yyyyMMdd"));
	}
}
