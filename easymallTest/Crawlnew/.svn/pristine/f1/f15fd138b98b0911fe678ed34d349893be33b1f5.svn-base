package cn.futures.data.importor.crawler;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.Dbm3InterfaceDao;
import cn.futures.data.entity.WholesalePrice;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.RecordCrawlResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
public class WholesalePriceCrawler {

	public static final String CLZ_TABLES = "CX_clz_prod_wholesale_price_index";
	public static final String AGRI_TABLES = "CX_agri_prod_wholesale_prices_totalindex";
	public static final Log LOG = LogFactory.getLog(WholesalePriceCrawler.class);
	private static final String className = WholesalePriceCrawler.class.getName();
	private static final String URL = "http://pfscnew.agri.gov.cn/pfsc/jgcx/reports.html";
	private static final Pattern TIME_PATTERN = Pattern.compile("^\\d*年\\d*月\\d*日$");
	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+\\.?\\d*");

	private final Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();


	public List<WholesalePrice> fetch() throws Exception{
		Document doc = Jsoup.parse(new URL(URL).openStream(), "gb2312", URL);
		Elements elements = doc.getElementsMatchingOwnText(TIME_PATTERN);
		List<WholesalePrice> priceDataList = new ArrayList<WholesalePrice>();
		for (Element element:elements){
			Integer timeInt = Integer.parseInt(element.ownText().replaceAll("\\D", ""));
			Element subling1= element.nextElementSibling();
			Element subling2= subling1.nextElementSibling();
			priceDataList.add(new WholesalePrice(timeInt, 
					getDoubleFromElement(subling1), 
					getDoubleFromElement(subling2)));
		}
		return priceDataList;
	}
	
	private List<WholesalePrice> filterByTimeInt(List<WholesalePrice> list){
		List<WholesalePrice> newList = new ArrayList<WholesalePrice>();
		int newestTimeInt = Math.min(DAOUtils.getNewestTimeInt(CLZ_TABLES),
				DAOUtils.getNewestTimeInt(AGRI_TABLES));
		for (WholesalePrice priceData:list)
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
	(cron = CrawlScheduler.CRON_WHOLESALE)
	public void doFetchAndSave() throws Exception{
		try{
			List<WholesalePrice> priceDataList = fetch();
			LOG.info("Wholesale Price Index Fetched!");
			priceDataList = filterByTimeInt(priceDataList);
			LOG.info("Update " + priceDataList.size() + " new data!");
			for(WholesalePrice p:priceDataList){
				List list = new ArrayList<>();
				Map<String, String> map = new HashMap<>();
				Map data = new HashMap<>();
				map.put("timeint", p.getTimeInt() + "");
				map.put("Varid", p.getVarid() + "");
				map.put("全国", p.getClzPriceIndex() + "");
				list.add(map);
				data.put("dbName",CLZ_TABLES);
				data.put("data", list);
				dbm3Dao.saveByDbName(data);
			}
			for(WholesalePrice p:priceDataList){
				List list = new ArrayList<>();
				Map<String, String> map = new HashMap<>();
				Map data = new HashMap<>();
				map.put("timeint", p.getTimeInt() + "");
				map.put("Varid", p.getVarid() + "");
				map.put("全国", p.getAgriPriceIndex() + "");
				list.add(map);
				data.put("dbName",AGRI_TABLES);
				data.put("data", list);
				dbm3Dao.saveByDbName(data);
			}
			if(priceDataList.isEmpty()){
				RecordCrawlResult.recordFailData(className, null, null, "没有要更新的数据");
			}
		} catch(Exception e) {
			LOG.error("发生未知异常。", e);
			RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
		}
	}
		
	public static void main(String[] args) throws Exception {
		new WholesalePriceCrawler().doFetchAndSave();
	}

}
