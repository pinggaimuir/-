package cn.futures.data.importor.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import cn.futures.data.util.RegexUtil;

/**
 * 油料油脂-加拿大油菜籽-加拿大国内菜籽报价
 * @author ctm
 *
 */
public class CANInnerRapeseedPriceFetch {
	private static final String className = CANInnerRapeseedPriceFetch.class.getName();
	private static final Log logger = LogFactory.getLog(CANInnerRapeseedPriceFetch.class);
	private static final Map<String, List<String>> CAN_RAPESEED_PRICE_MAP = new HashMap<String, List<String>>();
	private static final Map<String, String> enByMonth = new HashMap<String, String>();//月份-月份英文
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String cnName = "加拿大国内菜籽报价";
	static{
		List<String> urlList = new ArrayList<String>();
		urlList.add("http://dashboard.albertacanola.com/advanced_reports/cash-prices?utf8=%E2%9C%93&metric=Tonne&from=#startDate&to=#endDate&commodity_id=10");
		urlList.add("http://fxtop.com/en/historical-exchange-rates.php?A=1&C1=USD&C2=CAD&DD1=&MM1=&YYYY1=&B=1&P=&I=1&DD2=%day%&MM2=%month%&YYYY2=%year%&btnOK=Go!");
		CAN_RAPESEED_PRICE_MAP.put("加拿大油菜籽", urlList);
		
		enByMonth.put("01", "January");
		enByMonth.put("02", "February");
		enByMonth.put("03", "March");
		enByMonth.put("04", "April");
		enByMonth.put("05", "May");
		enByMonth.put("06", "June");
		enByMonth.put("07", "July");
		enByMonth.put("08", "August");
		enByMonth.put("09", "September");
		enByMonth.put("10", "October");
		enByMonth.put("11", "November");
		enByMonth.put("12", "December");
	}
	@Scheduled
	(cron=CrawlScheduler.CRON_CAN_RAPESEED_PRICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("加拿大国内菜籽报价", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到加拿大国内菜籽报价在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -1);
				fetchData(date);
			}else{
				logger.info("抓取加拿大国内菜籽报价的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String encoding = "gbk";
		for(String varName:CAN_RAPESEED_PRICE_MAP.keySet()){
			Map<String, String> dataMap = new HashMap<String, String>();
			//抓温哥华港口1号菜籽现货（加元/吨）
			String url = CAN_RAPESEED_PRICE_MAP.get(varName).get(0);
			logger.info("start fetch " + varName + "-" + cnName + "温哥华港口1号菜籽现货（加元/吨）@" + url);
			String[] params = {"table","class","prices_table"};//通过params过滤table
			String[] rowColChoose = {"", "11"};//选择哪几行哪几列
			String startDate = DateTimeUtil.formatDate(DateTimeUtil.addDay(date, -1), DateTimeUtil.YYYY_MM_DD);//查询起始日期(待抓取数据时间序列的前一天)
			String endDate = DateTimeUtil.formatDate(date, DateTimeUtil.YYYY_MM_DD);//查询截止日期（待抓取数据的时间序列）
			String contents = dataFetchUtil.getPrimaryContent(0, url.replace("#startDate", startDate).replace("#endDate", endDate), encoding, varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"温哥华港口1号菜籽现货（加元/吨）所需要的数据为空");
			}else{
				logger.info("分析温哥华港口1号菜籽现货（加元/吨）数据");
				String dayRegexPart = timeInt.substring(6, 8);
				if(dayRegexPart.startsWith("0")){
					dayRegexPart = "0?" + dayRegexPart.substring(1);
				}
				String priceRegex = enByMonth.get(timeInt.substring(4, 6)) + " " + dayRegexPart + " " + timeInt.substring(0, 4) + ",\\$(\\d+(\\.\\d+)?)";
				List<String> prices = RegexUtil.getMatchStr(contents, priceRegex, new int[]{1});
				if(prices != null && prices.get(0) != null && prices.get(0).matches("\\d+(\\.\\d+)?")){
					dataMap.put("温哥华港口1号菜籽现货(加元/吨)", prices.get(0));
				} else {
					logger.info(timeInt+"温哥华港口1号菜籽现货（加元/吨）数据网页尚未更新");
					RecordCrawlResult.recordFailData(className, varName, varName, "温哥华港口1号菜籽现货（加元/吨）数据网页尚未更新");
				}
			}
			//抓美元加元汇率
			url = CAN_RAPESEED_PRICE_MAP.get(varName).get(1).replace("%day%", timeInt.substring(6, 8))
				.replace("%month%", timeInt.substring(4, 6)).replace("%year%", timeInt.substring(0, 4));
			logger.info("start fetch " + varName + "-" + cnName + "美元加元汇率@" + url);
			String[] params1 = {"body", "table"};//通过params过滤table
			contents = dataFetchUtil.getPrimaryContent(0, url, encoding, varName, params1, null, 0);
			if(contents == null){
				logger.error("抓取"+varName+"美元加元汇率所需要的数据为空");
			}else{
				logger.info("分析美元加元汇率数据");
				String rateRegex = "<td[^>]*>1 USD-United States \\[US dollar / \\$\\]=[\\d\\.]* CAD-Canada \\[Canadian dollar / \\$ CA\\]</td><td[^>]*>1 USD=(\\d+(\\.\\d+)?) CAD</td>";
				List<String> rates = RegexUtil.getMatchStr(contents.replace("@", "#").replace("</tbody>", "@"), rateRegex, new int[]{1});
				if(rates != null && rates.get(0) != null && rates.get(0).matches("\\d+(\\.\\d+)?")){
					String rate = rates.get(0);
					dataMap.put("美元加元汇率", rate);
				} else {
					logger.warn("未抓到美元加元汇率");
					RecordCrawlResult.recordFailData(className, varName, varName, "未抓到美元加元汇率");
				}
			}
			//Winniberg菜籽近月收盘（加元/吨）：直接取“期货行情数据-WCE油菜籽-收盘价”的nearby值
			String nearbyLast = dao.getFieldByTable(Variety.getVaridByName("WCE油菜籽"), "收盘价", timeInt);
			if(nearbyLast.equals(""))
				nearbyLast="0";
			dataMap.put("Winniberg菜籽近月收盘（加元/吨）", nearbyLast);
			logger.info("保存加拿大国内菜籽报价数据:"+timeInt);
//			dao.saveByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
		}
	}
	
	public static void main(String []args){
		CANInnerRapeseedPriceFetch cani = new CANInnerRapeseedPriceFetch();
//		cani.start();
		/*补指定日期的历史数据*/
		Date fetchDate = DateTimeUtil.parseDateTime("20161007", DateTimeUtil.YYYYMMDD);//待补数据日期
		cani.fetchData(fetchDate);
	}
}
