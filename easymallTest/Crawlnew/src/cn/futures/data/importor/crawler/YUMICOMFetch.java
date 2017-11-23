package cn.futures.data.importor.crawler;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.ProvAndCityFormatData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中国玉米网：淀粉、酒精
 * @author ctm
 * 该爬虫的数据源已经过期！！！！！
 *
 */
public class YUMICOMFetch {
	private static final String className = YUMICOMFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private Log logger = LogFactory.getLog(YUMICOMFetch.class);
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private String baseUrl = "http://www.yumi.com.cn";
	private String url = "http://www.yumi.com.cn/yumijiage/pricelist/third_id/%code%.html";
	private Map<String, String> varName2cnNameMap = new HashMap<String, String>(){
		{
			put("玉米-17-淀粉价格","玉米淀粉价格");
			put("酒精-18-酒精价格","价格");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_YUMICOM_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("中国玉米网：淀粉、酒精价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到中国玉米网：淀粉、酒精价格的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetch(date);
			}else{
				logger.info("抓取中国玉米网：淀粉、酒精价格的定时器已关闭");
			}
		}
	}
	
	private void fetch(Date date) {
		logger.info("=====开始抓取中国玉米网：玉米淀粉与酒精价格=====");
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String[] priceFilters = {"div", "class", "con1Text"};
		for(String varNameACode:varName2cnNameMap.keySet()){
			String cnName = varName2cnNameMap.get(varNameACode);
			String varName = varNameACode.split("-")[0];
			int varId = Variety.getVaridByName(varName);
			String code = varNameACode.split("-")[1];
			String tips = varNameACode.split("-")[2];
			String[] filters = {"ul", "id", "priceRstShow"};
			String contents = fetchUtil.getPrimaryContent(0, url.replaceAll("%code%", code), "utf-8", varName, filters, null, 0);
			if(contents != null && !contents.equals("")){
				Map<String, String> dataMap = new HashMap<String, String>();
				Map<String, Integer> areaNumMap = new HashMap<String, Integer>();
				double sum = 0;
				String[] lis = contents.split("<li>");
				for(int i=1;i<lis.length;i++){
					String comp = "href=\"([^\"]+)([^>]+)>(.{11})([^<]+)"+tips;
					int[] index={1,3,4};
					List<String> results = fetchUtil.getMatchStr(lis[i], comp, index);
					if(results.size()>0 && results.get(1).equals(DateTimeUtil.formatDate(date, "yyyy年MM月dd日"))){
						String area = ProvAndCityFormatData.getAreaByMarket(results.get(2));
						String prov = ProvAndCityFormatData.getProvinceByArea(area);
						//酒精价格抓需要抓取吉林、黑龙江、河南三个省份的价格
						if(varName.equals("酒精") && !(prov.equals("吉林")||prov.equals("黑龙江")||prov.equals("河南")))
							continue;
						String priceContents = fetchUtil.getPrimaryContent(0, baseUrl+results.get(0), "utf-8", varName, priceFilters, null, 0);
						comp = "市场主流为:(.+)元/吨";
						List<String> prices = fetchUtil.getMatchStr(priceContents, comp, null);
						if(prices.size() > 0 && !prov.equals("")){
							if(dataMap.get(prov) != null){
								dataMap.put(prov, Double.parseDouble(prices.get(0))+Double.parseDouble(dataMap.get(area))+"");
								areaNumMap.put(prov, areaNumMap.get(prov)+1);
							}else{
								dataMap.put(prov, prices.get(0));
								areaNumMap.put(prov, 1);
							}
						}else{
							logger.info("");
						}
					}
				}
				for(String prov:dataMap.keySet()){
					double areaData = Double.parseDouble(dataMap.get(prov))/areaNumMap.get(prov);
					dataMap.put(prov, areaData+"");
					sum += areaData;
				}
				dataMap.put("全国", sum/dataMap.size()+"");
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}
		}
	}
	public static void main(String[] args){
		new YUMICOMFetch().start();
	}
}
