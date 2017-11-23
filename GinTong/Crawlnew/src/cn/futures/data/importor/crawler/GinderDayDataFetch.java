package cn.futures.data.importor.crawler;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**	
	* @description	生姜日价格指数及日更新相关数据
	* @author 		xjlong 
    * @date 		2016年8月30日  
*/
public class GinderDayDataFetch {
	private static final String className = GinderDayDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "生姜";
	private Log logger = LogFactory.getLog(GinderDayDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static Map<String,String> dayPriceIndex_Map = new HashMap<String, String>();
	private static Map<String,String> mainWholesalePrice_Map = new HashMap<String, String>();
	static{
		//日价格指数映射
		dayPriceIndex_Map.put("定基指数","http://zs.jiang7.com/zhishu/list.php?catid=5&page=");
		//主销区生姜批发价格
		//mainWholesalePrice_Map.put("苏州","http://www.nhqnm.com/PriceQuery.aspx?ItemName=%e7%94%9f%e5%a7%9c&ItemSpec=%e8%8b%8f%e5%b7%9e");
		mainWholesalePrice_Map.put("北京","http://www.xinfadi.com.cn/marketanalysis/1/list/");
	}
	//抓取日价格指数数据
	public void fetchDayPriceIndexData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "日价格指数";
		String contents = "";
		String pageUrl = "";
		int totalPage = 0;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"0","011"};
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header:dayPriceIndex_Map.keySet()){
			logger.info("抓"+cnName+"--"+header+"数据");
			pageUrl = dayPriceIndex_Map.get(header);
			contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
			if(contents != null && !contents.equals("")){
				String b[]=(contents.split("\n"));
				for(int j=0;j<b.length;j++){
					timeInt = b[j].split(",")[0];
					timeInt = timeInt.replace("/","");
					dataMap.put(header,b[j].split(",")[1]);
					if(!dataMap.isEmpty()){
						logger.info("正在保存--"+header+"--"+timeInt+"--的数据。。。");
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
						dataMap.clear();
					}else {
						RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
					}
				}
			}else{
				logger.info("没有抓取到"+header+"数据");
				RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到"+header+"数据");
			}
		}
	}
	
	//主销区生姜批发价格抓取
	public void fetchMainWholesalePriceData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int varId = Variety.getVaridByName(varName);
		String cnName = "主销区生姜批发价格";
		String contents = "";
		String pageUrl = "";
		int totalPage = 0;//总页数
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header : mainWholesalePrice_Map.keySet()){
			logger.info("正在抓取"+header+"的数据");
			if("北京".equals(header)){
				totalPage = 20;
				String[] filters = {"table","tr"};
				String[] rowColChoose = {"0","111"};
				pageUrl = mainWholesalePrice_Map.get(header)+".shtml?prodname=%E5%A7%9C";
				contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,1);
				if(contents != null && !contents.equals("")){
					String b[]=(contents.split("\n"));
					for(int j = 0;j < 2;j++){
						if(b[j].split(",")[0].equals("姜")){
							timeInt = b[j].split(",")[6];
							timeInt = timeInt.replace("-", "");
							dataMap.put(header,b[j].split(",")[2]);
						}
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+header+"--"+timeInt+"--的数据。。。");
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
						}
					}
				}else{
					logger.info("没有抓取到"+cnName+"数据");
					RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到"+header+"数据");
				}
			}else{
				String[] filters = {"table","tr"};
				String[] rowColChoose = {"0","111"};
				pageUrl = mainWholesalePrice_Map.get(header);
				contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, rowColChoose,39);
				if(contents != null && !contents.equals("")){
					String b[]=(contents.split("\n"));
					for(int j = 0;j<b.length;j++){
						if(b[j].split(",")[0].equals(varName)){
							timeInt = b[j].split(",")[6];
							timeInt = timeInt.replace("-", "");
							dataMap.put(header,b[j].split(",")[2]);
						}
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+header+"--"+timeInt+"--的数据。。。");
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
						}
					}
				}else{
					logger.info("没有抓取到"+cnName+"数据");
					RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到"+header+"数据");
				}
			}
		}
	}
	//抓取山东主产区生姜批发价格
	public void fetchMainProduceWholesaleData(Date date){
		String baseUrl = "http://www.jiang7.com/News/C02/";
		int varId = Variety.getVaridByName(varName);
		String cnName = "山东主产区生姜批发价格";
		String contents = "";
		String pageUrl = "";
		int totalPage = 0;
		String timeInt = null;
		String[] filters = {"ul","li"};
		String[] rowColChoose = null;
		Map<String, String> dataMap = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();
		contents = dataFetchUtil.getPrimaryContent(0, baseUrl, "gb2312", varName, filters, rowColChoose,3);
		if(contents != null && !contents.equals("")){
			String reg = "<A(?:\\s+.+?)*?\\s+href=\"([^\"]*?)\".+>(.*?)</A>";//正则-提取<a>标签href属性的值
			String regLi = "(?<=<li>)[\\s\\S]*?(?=</li>)";//正则-提取<li>标签中的内容
			String regSpan = "[^>]+(?=</span>)";//正则-提取<span>标签中的内容
			list = RegexUtil.getMatchStr(contents, regLi);
			for(int i=0;i<8;i++){
				String[] filterTags = {"div","class","nei-text"};
				String[] rowCol = null;
				String regNumber="[^0-9]";
				pageUrl = "http://www.jiang7.com"+match(list.get(i), "A", "href");
				String producePlace = match(list.get(i), "A", "title");
				Date timeStr = new Date(RegexUtil.getMatchStr(list.get(i),regSpan).get(0));
				timeInt = DateTimeUtil.formatDate(timeStr,"yyyyMMdd");
				String regGinder = null;
				String placeName = null;
				Double totalPrice = 0d; //所在区所有地方生姜价总和
				Double avgPrice	= 0d; //所在区生姜平均价格
				int countPlace = 0;//生姜报价地方总数
				//只抓取山东安丘、莱芜、昌邑三个区相关数据
				if(producePlace.contains("安丘") || producePlace.contains("莱芜")){
					String fetchContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,0).replaceAll("<BR>", "\n");
					if(producePlace.contains("安丘")){
						regGinder = "泥姜：[^\\s]*（优质）";
						placeName = "潍坊安丘（好泥姜）";
					}
					if(producePlace.contains("莱芜")){
						regGinder = "水洗姜：[^\\s]*（一般）";
						placeName = "莱芜（水洗姜）";
					}
					List<String> listFont = RegexUtil.getMatchStr(fetchContents, regGinder);
					for(String fontTxt : listFont){
						Double price = 0d;
						try {
							if(producePlace.contains("莱芜")){
								price = Double.parseDouble(fontTxt.substring(4,8)); 
							}else{
								price = Double.parseDouble(fontTxt.substring(3,7)); 
							}
							totalPrice += price;
							countPlace ++;
						} catch (Exception e) {
							logger.error("String转Double类型转换异常。", e);
						}
					}
					avgPrice =scaleNumber((totalPrice/countPlace));
					dataMap.put(placeName,avgPrice.toString());
				}
				if(producePlace.contains("昌邑")){
					String fetchContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,0).replaceAll("<BR>", "\n");
					Double zlTotalPrice = 0d;
					Double zlAvgPrice = 0d;
					regGinder = "洗姜：[^\\s]*（优质）";
					String regZhuLiu = "洗姜：[^\\s]*（主流）";
					List<String> listFont = RegexUtil.getMatchStr(fetchContents, regGinder);
					List<String> zhuliu = RegexUtil.getMatchStr(fetchContents, regZhuLiu);
					for(String fontTxt : listFont){
						try {
							Double price = Double.parseDouble(fontTxt.substring(3,7)); 
							totalPrice += price;
							countPlace ++;
						} catch (Exception e) {
							logger.error("String转Double类型转换异常。", e);
						}
					}
					countPlace = 0;
					for(String zlTxt : zhuliu){
						try {
							double zlPrice = Double.parseDouble(zlTxt.substring(3,7)); 
							zlTotalPrice += zlPrice;
							countPlace ++;
						} catch (Exception e) {
							logger.error("String转Double类型转换异常。", e);
						}
					}
					avgPrice = scaleNumber((totalPrice/countPlace));
					zlAvgPrice = scaleNumber(zlTotalPrice/countPlace);
					dataMap.put("潍坊昌邑（好水洗姜）",avgPrice.toString());
					dataMap.put("潍坊昌邑（一般水洗姜）", zlAvgPrice.toString());
				}
				if(!dataMap.isEmpty()){
					dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
					dataMap.clear();
				}else {
					RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到大蒜相关数据");
				}
			}
			logger.info("山东主产区生姜批发价格数据保存完成。。。");
		}else{
			logger.info("没有抓取到"+cnName+"数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到数据");
		}
	}
	/**
 	* @description	从一个字符串中提取出一个标签属性的值
	* @param  		source,字符串 element，标签名 attr,属性名
	* @return		result，属性值
	*/
	public  String match(String source, String element, String attr) {  
	    String result = null;  
	    String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";  
	    Matcher m = Pattern.compile(reg).matcher(source);  
	    while (m.find()) {  
	        result =  m.group(1);
	    }  
	    return result;  
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
	//四舍五入，保留两位小数点
	public double scaleNumber(double data){
		BigDecimal b = new BigDecimal(data); 
		return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	/*
	 *定时器：每天上午11:30更新数据 
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_CINDER_DAY)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("生姜---日价格相关数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到生姜---日价格相关数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到生姜---日价格相关数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchDayPriceIndexData(date);
					fetchMainWholesalePriceData(date);
					fetchMainProduceWholesaleData(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取生姜---日价格相关数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取生姜---日价格相关数据的定时器已关闭");
			}
		}
	}
	public static void main(String[] args) {
		GinderDayDataFetch gd = new GinderDayDataFetch();
		gd.start();
//		gd.fetchMainProduceWholesaleData(new Date());
		//gd.fetchDayPriceIndexData(new Date());
		//gd.fetchMainWholesalePriceData(new Date());
	}
}
