package cn.futures.data.importor.crawler;


import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.Dbm3InterfaceDao;
import cn.futures.data.entity.WholesalePriceMonthly;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.RecordCrawlResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class WholesalePriceMonthlyCrawler {


	public static final String CLZ_TABLES = "CX_clzpro_wholeprice_index_month";
	public static final String AGRI_TABLES = "CX_agripro_wholeprice_index_month";
	public static final Log LOG = LogFactory.getLog(WholesalePriceMonthlyCrawler.class);
	private static final String className = WholesalePriceMonthlyCrawler.class.getName();
	private static final String URL = "http://pfscnew.agri.gov.cn/pfsc/jgcx/reports.html";
	private static final Pattern TIME_PATTERN = Pattern.compile("^\\d*年\\d*月$");
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+\\.?\\d*");

	private final Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();


	public List<WholesalePriceMonthly> fetch() throws Exception{
		double[] tempData = new double[6];
		Document doc = Jsoup.parse(new URL(URL).openStream(), "gb2312", URL);
		Elements elements = doc.getElementsMatchingOwnText(TIME_PATTERN);
		List<WholesalePriceMonthly> priceDataList = new ArrayList<WholesalePriceMonthly>();
		for (Element element:elements){
			Integer timeInt = Integer.parseInt(element.ownText().replaceAll("\\D", ""));
			Double price1 = .0, price2 = .0;
			Element dataElement = element;
			for (int i = 0;i < 6;++ i){
				dataElement= dataElement.nextElementSibling();
				tempData[i] = getDoubleFromElement(dataElement);
			}
			priceDataList.add(new WholesalePriceMonthly(timeInt, 
					tempData[3], tempData[4], tempData[5], 
					tempData[0], tempData[1], tempData[2]));
		}
		return priceDataList;
	}
	
	private List<WholesalePriceMonthly> filterByTimeInt(List<WholesalePriceMonthly> list){
		List<WholesalePriceMonthly> newList = new ArrayList<WholesalePriceMonthly>();
		int newestTimeInt = Math.min(DAOUtils.getNewestTimeInt(CLZ_TABLES),
				DAOUtils.getNewestTimeInt(AGRI_TABLES));
		for (WholesalePriceMonthly priceData:list)
			if (priceData.getTimeInt() > newestTimeInt)
				newList.add(priceData);
		return newList;
	}
	
	public Double getDoubleFromElement(Element element){
		String rawStr = element.ownText();
		Matcher numMatcher = NUMBER_PATTERN.matcher(rawStr);
		if (numMatcher.find())
			return Double.parseDouble(numMatcher.group());
		return null;
	}
	
	@Scheduled
	(cron = CrawlScheduler.CRON_WHOLESALE_MONTHLY)
	public void doFetchAndSave() throws Exception{
		try{
			List<WholesalePriceMonthly> priceDataList = fetch();
			LOG.info("Wholesale Price Index Fetched!");
			priceDataList = filterByTimeInt(priceDataList);
			LOG.info("Update " + priceDataList.size() + " new data!");
			for(WholesalePriceMonthly p:priceDataList){
				List list = new ArrayList<>();
				Map<String, String> map = new HashMap<>();
				Map data = new HashMap<>();
				map.put("timeint", p.getTimeInt() + "");
				map.put("Varid", p.getVarid() + "");
				map.put("定基指数", p.getClzFixedBase() + "");
				map.put("同比指数", p.getClzYearOnYear() + "");
				map.put("环比指数", p.getClzMonthOnMonth() + "");
				list.add(map);
				data.put("dbName",CLZ_TABLES);
				data.put("data", list);
				dbm3Dao.saveByDbName(data);
			}
			for(WholesalePriceMonthly p:priceDataList){
				List list = new ArrayList<>();
				Map<String, String> map = new HashMap<>();
				Map data = new HashMap<>();
				map.put("timeint", p.getTimeInt() + "");
				map.put("Varid", p.getVarid() + "");
				map.put("定基指数", p.getAgriFixedBase() + "");
				map.put("同比指数", p.getAgriYearOnYear() + "");
				map.put("环比指数", p.getAgriMonthOnMonth() + "");
				list.add(map);
				data.put("dbName",AGRI_TABLES);
				data.put("data", list);
				dbm3Dao.saveByDbName(data);
			}
			if(priceDataList.isEmpty()){
				RecordCrawlResult.recordFailData(className, null, null, "没有要更新的数据。");
			}
		} catch(Exception e) {
			LOG.error("发生未知异常。", e);
			RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
		}
	}
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println(11);
	}

}
