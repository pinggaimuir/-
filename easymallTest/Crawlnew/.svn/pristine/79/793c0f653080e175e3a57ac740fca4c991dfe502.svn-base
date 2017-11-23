package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 中国汇易网数据
 * @date 2016-12-05
 * @author bric_yangyulin
 * */
public class ChinajciDataFetch {
	private static final String detailUrlBase = "http://www.chinajci.com/pro/news_jgrb.aspx?re_date=#re_date&info_no=#info_no&pro_name=#pro_name&zw=#zw";//详情页链接模板
	private static final String logUrl = "http://www.chinajci.com/member/login.aspx?info_no=#info_no";//登录链接模板
	private String className = ChinajciDataFetch.class.getName();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(ChinajciDataFetch.class);
	
	@Scheduled(cron = CrawlScheduler.CRON_CHINAJCIDATA_1)
	public void start1(){//周度，每周四下午13:30抓取当天数据
		String switchFlag = new CrawlerManager().selectCrawler("中国汇易网数据_1", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到中国汇易网数据_1在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date curDate = new Date();
				String curDateStr = DateTimeUtil.formatDate(curDate, DateTimeUtil.YYYY_MM_DD);//当天的时间序列
				//米糠油-价格
				fetchRiceOilPrice(curDateStr);
				//米糠油-油糠价格 
				fetchBranOilPrice(curDateStr);
				//玉米油-玉米胚芽粕
				fetchCornGermMeal(curDateStr);
				//花生-现货
				fetchPeanutSpotPrice(curDateStr);
				//葵花籽-收购价格
				fetchSunflowlerSeedPrice(curDateStr);
				//葵花籽油-价格
				fetchSunflowlerOil(curDateStr);
				//芝麻-贸易商报价
				fetchSesamePrice(curDateStr);
				//芝麻油-贸易商价格
				fetchSesameOilPrice(curDateStr);
			}else{
				LOG.info("抓取中国汇易网数据_1的定时器已关闭");
			}
		}
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_CHINAJCIDATA_2)
	public void start2(){//周度，每周一下午15:30更新上周五的数据
		String switchFlag = new CrawlerManager().selectCrawler("中国汇易网数据_2", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到中国汇易网数据_2在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date curDate = new Date();
				Date lastFriday = DateTimeUtil.addDay(curDate, -3);//上周五
				String lastFridayStr = DateTimeUtil.formatDate(lastFriday, DateTimeUtil.YYYY_MM_DD);//当天的时间序列
				//油料油脂-豆油-油厂库存
				fetchSoyoilsPortStocks(lastFridayStr);
				//油料油脂-大豆-大豆港口库存
				fetchSoybeanPortStocks(lastFridayStr);
			}else{
				LOG.info("抓取中国汇易网数据_2的定时器已关闭");
			}
		}
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_CHINAJCIDATA_3)
	public void start3(){//日度，每天下午15:00抓取当天数据
		String switchFlag = new CrawlerManager().selectCrawler("中国汇易网数据_3", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到中国汇易网数据_3在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date curDate = new Date();
				String curDateStr = DateTimeUtil.formatDate(curDate, DateTimeUtil.YYYY_MM_DD);//当天的时间序列
				//豆油-四级豆油现货价格
				fetchSoyoilsPrice(curDateStr);
				//豆油-四级豆油成交均价
				fetchSoyoilsHammerPrice(curDateStr);
				//豆油-一级豆油成交均价
				fetchFirstOrderSoyoilsPrice(curDateStr);
			}else{
				LOG.info("抓取中国汇易网数据_3的定时器已关闭");
			}
		}
	}
	
	/**
	 * 获取消息的info_no(该网站一篇报告的唯一标识)
	 * @param curDateStr 当前日期
	 * @param listUrl 列表页地址
	 * @param signWord 标识词汇（用于抓取详情页链接）
	 * */
	public String fetchInfoNo(String curDateStr, String listUrl, String signWord, String encoding){
		String infoNo = null;//报告标识
		try{
			String html = fetchUtil.getCompleteContent(0, listUrl, encoding, signWord + "_list.txt");
			Document doc = Jsoup.parse(html);
			Element hrefSpan = doc.getElementById("labxxxx");
			if(hrefSpan != null){
				String infoNoRegex = "<a href=\"/article/a([0-9a-zA-Z]+).html\" target=\"_blank\">" 
						+ curDateStr.substring(5, 7) + "月" + curDateStr.substring(8, 10) + signWord + "</a>";
				List<String> infoNoList = RegexUtil.getMatchStr(hrefSpan.html(), infoNoRegex, new int[]{1});
				if(infoNoList != null && !infoNoList.isEmpty()){
					infoNo = infoNoList.get(0);
				}
			}
		} catch(Exception e) {
			LOG.error(signWord + "获取infoNo时发生异常。");
		}
		return infoNo;
	}
	/**
	 * 获取过滤掉html标签的表格内容
	 * @param curDateStr 当前时间
	 * @param listUrl 列表页地址
	 * @param signWord 标识词汇（用于抓取详情页链接）
	 * @param varName 品种名
	 * @param cnName 中文名
	 * */
	public String fetchTableStr(String curDateStr, String listUrl, String signWord, String proName, String zw, String varName, String cnName, String encoding){
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			LOG.error(e1);
		}
		LOG.info("开始处理:" + varName + "-" + cnName);
		String infoNo = fetchInfoNo(curDateStr, listUrl, signWord, encoding);//获取报告标识
		LOG.info("infoNo：" + infoNo);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			LOG.error(e1);
		}
		String pureStr = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "sky1");//用户名
		params.put("password", "football");//密码
		try{
			String detailUrl = detailUrlBase.replace("#info_no", infoNo).replace("#re_date", curDateStr)
					.replace("#pro_name", proName).replace("#zw", zw);
			String htmlContent = fetchUtil.getCompleteContent(logUrl.replace("#info_no", infoNo), detailUrl, 
					varName + "_" + cnName + ".txt", Constants.ENCODE_UTF8, params);
			if(htmlContent != null && !htmlContent.isEmpty()){
				Document doc = Jsoup.parse(htmlContent);
				Elements divTable = doc.select("div:has(table)");//表格所在div
				if(divTable != null){
					pureStr = divTable.html().replace("&lt;", "<").replace("&gt;", ">").replaceAll("<[^>]*>", "");
				}
			}
		} catch(Exception e) {
			LOG.error("获取数据时发生异常", e);
		}
		return pureStr;
	}
	
	/**
	 * 抓取 米糠油-价格 数据
	 * @param curDateStr 当前日期
	 * */
	public void fetchRiceOilPrice(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/Oils/Rice-oil.html";//列表页
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "米糠油";
		String cnName = "价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区米糠油价格行情日报", "%e7%b1%b3%e7%b3%a0%e6%b2%b9", "%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"辽宁", "黑龙江", "江苏", "安徽", "江西", "山东", "湖北"}, 
				{"辽宁营口", "黑龙江佳木斯-黑龙江哈尔滨", "江苏徐州", "安徽淮南", ".*西南昌", "山东寿光", "湖北仙桃"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	/**
	 * 抓取 米糠油-油糠价格 数据
	 * @param curDateStr 当前日期
	 * */
	public void fetchBranOilPrice(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/Oils/Paddy/Rice-bran.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "米糠油";
		String cnName = "油糠价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区米糠价格行情日报", "%e7%b1%b3%e7%b3%a0", "%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"辽宁", "黑龙江", "江苏", "安徽", "江西", "湖北", "湖南"}, 
				{"辽宁营口", "黑龙江佳木斯", "江苏徐州", "安徽淮南", "江西吉水", "湖北仙桃", "湖南岳阳"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-玉米油-玉米胚芽粕价格
	 * */
	public void fetchCornGermMeal(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/Feed/Rapemeal/germ-meal.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "玉米油";
		String cnName = "玉米胚芽粕价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区玉米胚芽粕价格行情日报", "%e7%8e%89%e7%b1%b3%e8%83%9a%e8%8a%bd%e7%b2%95", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"吉林", "河北", "山东"}, 
				{"吉林黄龙-吉林长春", "河北秦皇.*", "山东滨州"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-花生-现货价格
	 * */
	public void fetchPeanutSpotPrice(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/Oils/Goober/";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "花生";
		String cnName = "现货价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区油料花生米价格行情日报", "%e6%b2%b9%e6%96%99%e8%8a%b1%e7%94%9f%e7%b1%b3", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_GB2312);
		String[][] colNames = {{"河南", "山东", "河北", "江西", "湖北", "江苏", "安徽", "广东", "广西"}, 
				{"河南南阳-河南安阳", "山东临沂-山东青岛-山东济宁", "河北邯郸", "江西鹰潭", "湖北宜城", "江苏新沂", "安徽固镇", "广东肇庆", "广西玉林"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-葵花籽-收购价格
	 * */
	public void fetchSunflowlerSeedPrice(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/sunflower-seed.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "葵花籽";
		String cnName = "收购价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区葵花籽价格行情日报（早报）", "%e8%91%b5%e8%8a%b1%e7%b1%bd", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"内蒙古", "新疆"}, 
				{"内蒙赤峰", "新疆伊犁"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-葵花籽油-价格
	 * */
	public void fetchSunflowlerOil(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/Oils/Sunflower-oil.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "葵花籽油";
		String cnName = "价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区葵花油价格行情日报", "%e8%91%b5%e8%8a%b1%e6%b2%b9", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"内蒙古", "河北"}, 
				{"内蒙赤峰", "河北保定"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-芝麻-贸易商报价
	 * */
	public void fetchSesamePrice(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/sesame.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "芝麻";
		String cnName = "贸易商报价";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区芝麻价格行情日报", "%e8%8a%9d%e9%ba%bb", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"天津", "山东", "江西", "湖北", "广西"}, 
				{"天津", "山东黄岛", "江西乐平", "湖北潜江", "广西桂平"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-芝麻油-贸易商价格
	 * */
	public void fetchSesameOilPrice(String curDateStr){
		String listUrl = "http://www.chinajci.com/Breed/Oils/Sesame-oil.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "芝麻油";
		String cnName = "贸易商价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区芝麻油价格行情日报", "%e8%8a%9d%e9%ba%bb%e6%b2%b9", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"河北", "山西", "湖北", "广西"}, 
				{"河北廊坊", "山西运城", "湖北天门", "广西北海"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-豆油-四级豆油现货价格
	 * */
	public void fetchSoyoilsPrice(String curDateStr){
		String listUrl = "http://new.chinajci.com/Breed/Oils/Soyoils/Prices-report.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "豆油";
		String cnName = "四级豆油现货价格";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区豆油价格行情日报（午报）", "%e5%9b%9b%e7%ba%a7%e8%b1%86%e6%b2%b9", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"黑龙江", "吉林", "辽宁", "天津", "山东", "河北", "河南", "湖北", "江苏", "安徽", "上海", "浙江", "福建", "广东", "广西", "四川"}, 
				{"哈尔滨", "长春", "沈阳-大连", "天津", "青岛", "石家庄", "郑州", "武汉", "连云港-张家港-南京", "合肥", "上海", "宁波", "福州", "黄埔", "南宁", "成都"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-豆油-四级豆油成交均价
	 * */
	public void fetchSoyoilsHammerPrice(String curDateStr){
		String listUrl = "http://new.chinajci.com/Breed/Oils/Soyoils/Prices-report.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "豆油";
		String cnName = "四级豆油成交均价";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区豆油价格行情日报（午报）", "%e5%9b%9b%e7%ba%a7%e8%b1%86%e6%b2%b9", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"京津地区", "山东地区", "华东地区", "两广地区", "东北地区"}, 
				{"天津", "青岛", "连云港-张家港-合肥-南京-上海-宁波", "黄埔-南宁", "哈尔滨-长春-沈阳-大连"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-豆油-一级豆油成交均价
	 * */
	public void fetchFirstOrderSoyoilsPrice(String curDateStr){
		String listUrl = "http://new.chinajci.com/Breed/Oils/Soyoils/Prices-report.html";
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "豆油";
		String cnName = "一级豆油成交均价";
		String pureStr = fetchTableStr(curDateStr, listUrl, "日国内主要地区一级豆油价格行情日报（午报）", "%e4%b8%80%e7%ba%a7%e8%b1%86%e6%b2%b9", 
				"%e5%8d%88%e6%8a%a5", varName, cnName, Constants.ENCODE_UTF8);
		String[][] colNames = {{"京津地区", "山东地区", "华东地区", "两广地区", "东北地区"}, 
				{"天津", "日照", "张家港-宁波", "黄埔", "大连"}};//物理表列名及网页中对应名称
		saveWithAverage(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 计算省内平均值并保存
	 * @param colNames 列名与对应城市
	 * @param pureStr 过滤后的表格内容
	 * @param timeIntStr 时间序列
	 * @param varName 品种名
	 * @param cnName 中文名
	 * */
	public boolean saveWithAverage(String[][] colNames, String pureStr, String timeIntStr, String varName, String cnName){
		boolean isSaved = false;//是否保存成功
		Map<String, String> dataMap = new HashMap<String, String>();
		try{
			for(int i = 0; i < colNames[0].length; i++){
				if(colNames[1][i].contains("-")){//说明为多地求平均值
					String[] citys = colNames[1][i].split("-");
					double sum = 0.0d;//和
					int num = 0;//有数据的城市个数
					for(String city: citys){
						String priceRegex = city + "\\s*(\\d+)\\s*";
						List<String> priceList = RegexUtil.getMatchStr(pureStr, priceRegex, new int[]{1});
						if(priceList != null){
							String price = priceList.get(0);
							if(!price.isEmpty()){
								sum += Integer.parseInt(price);
								num++;
							}
						}
					}
					if(varName.equals("葵花籽") && cnName.equals("收购价格")){
						dataMap.put(colNames[0][i], String.valueOf(sum/num/2000));
					} else {
						dataMap.put(colNames[0][i], String.valueOf(sum/num));
					}
				} else {
					String priceRegex = colNames[1][i] + "\\s*(\\d+)\\s*";
					List<String> priceList = RegexUtil.getMatchStr(pureStr, priceRegex, new int[]{1});
					if(priceList != null){
						String price = priceList.get(0);
						if(!price.isEmpty()){
							if(varName.equals("葵花籽") && cnName.equals("收购价格")){
								dataMap.put(colNames[0][i], String.valueOf(Double.parseDouble(price)/2000));
							} else {
								dataMap.put(colNames[0][i], price);
							}
						}
					}
				}
			}
			String avePriceRegex = "平均价格:\\s*((\\d+)(\\.\\d+)?)\\s*";//平均价格的正则式
			List<String> aveList = RegexUtil.getMatchStr(pureStr, avePriceRegex, new int[]{1});
			if(aveList != null && !aveList.get(0).isEmpty()){
				if(varName.equals("葵花籽") && cnName.equals("收购价格")){
					dataMap.put("全国", String.valueOf(Double.parseDouble(aveList.get(0))/2000));
				} else {
					dataMap.put("全国", aveList.get(0));
				}
			}
		} catch(Exception e) {
			LOG.error("解析数据时发生异常。", e);
		}
		int timeInt = Integer.parseInt(timeIntStr);
		if(dataMap.size() == colNames[0].length + 1){
			LOG.info(dataMap);
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			isSaved = true;
		} else if(dataMap.isEmpty()){
			LOG.info("数据为空。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "数据为空。");
		} else {
			LOG.info("数据不完整。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "数据不完整。");
			LOG.info(dataMap);
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			isSaved = true;
		}
		return isSaved;
	}
	
	/**
	 * 计算地区内的和并保存
	 * @param colNames 列名与对应城市
	 * @param pureStr 过滤后的表格内容
	 * @param timeIntStr 时间序列
	 * @param varName 品种名
	 * @param cnName 中文名
	 * */
	public boolean saveWithSum(String[][] colNames, String pureStr, String timeIntStr, String varName, String cnName){
		boolean isSaved = false;//是否保存成功
		Map<String, String> dataMap = new HashMap<String, String>();
		try{
			for(int i = 0; i < colNames[0].length; i++){
				if(colNames[1][i].contains("-")){//说明为多地求平均值
					String[] citys = colNames[1][i].split("-");
					double sum = 0.0d;//和
					for(String city: citys){
						String priceRegex = city + "\\s*(\\d+(\\.\\d+)?)\\s*";
						List<String> priceList = RegexUtil.getMatchStr(pureStr, priceRegex, new int[]{1});
						if(priceList != null){
							String price = priceList.get(0);
							if(!price.isEmpty()){
								sum += Double.parseDouble(price);								
							}
						}
					}
					dataMap.put(colNames[0][i], String.valueOf(sum));
				} else {
					String priceRegex = colNames[1][i] + "\\s*(\\d+(\\.\\d+)?)\\s*";
					List<String> priceList = RegexUtil.getMatchStr(pureStr, priceRegex, new int[]{1});
					if(priceList != null){
						String price = priceList.get(0);
						if(!price.isEmpty()){
							dataMap.put(colNames[0][i], price);
						}
					}
				}
			}
		} catch(Exception e) {
			LOG.error("解析数据时发生异常。", e);
		}
		int timeInt = Integer.parseInt(timeIntStr);
		if(dataMap.size() == colNames[0].length){
			LOG.info(dataMap);
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			isSaved = true;
		} else if(dataMap.isEmpty()){
			LOG.info("数据为空。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "数据为空。");
		} else {
			LOG.info("数据不完整。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "数据不完整。");
			LOG.info(dataMap);
			dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			isSaved = true;
		}
		return isSaved;
	}
	
	/**
	 * 油料油脂-豆油-油厂库存
	 * */
	public void fetchSoyoilsPortStocks(String curDateStr){
		String timeIntStr = curDateStr.replace("-", "");
		String varName = "豆油";
		String cnName = "油厂库存";
		LOG.info("开始处理：" + varName + "-" + cnName);
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "sky1");//用户名
		params.put("password", "football");//密码
		String pureStr = null;
		try{
			String detailUrl = "http://www.chinajci.com/pro/soyoils_import.aspx?re_date=" + curDateStr + "&zw=Leon";
			String htmlContent = fetchUtil.getCompleteContent(logUrl, detailUrl, 
					varName + "_" + cnName + ".txt", Constants.ENCODE_UTF8, params);
			if(htmlContent != null && !htmlContent.isEmpty()){
				Document doc = Jsoup.parse(htmlContent);
				Element divTable = doc.getElementById("GridView1");//表格所在div
				if(divTable != null){
					pureStr = divTable.html().replace("&lt;", "<").replace("&gt;", ">").replaceAll("<[^>]*>", "");
				}
			}
		} catch(Exception e) {
			LOG.error("解析数据时发生未知异常");
			RecordCrawlResult.recordFailData(className, varName, cnName, "解析数据时发生未知异常");
		}
		String[][] colNames = {{"京津地区", "山东地区", "华东地区", "两广地区", "东北地区", "全国"}, 
				{"天津", "山东", "华东", "广西-广州", "东北三省", "全国"}};//物理表列名及网页中对应名称
		saveWithSum(colNames, pureStr, timeIntStr, varName, cnName);
	}
	
	/**
	 * 油料油脂-大豆-大豆港口库存
	 * */
	public void fetchSoybeanPortStocks(String curDateStr){
		String varName = "大豆";
		String cnName = "大豆港口库存";
		LOG.info("开始处理：" + varName + "-" + cnName);
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "sky1");//用户名
		params.put("password", "football");//密码
		try{
			String detailUrl = "http://www.chinajci.com/pro/stock.aspx?re_date=" + curDateStr + "&zw=Leon";
			String htmlContent = fetchUtil.getCompleteContent(logUrl, detailUrl, 
					varName + "_" + cnName + ".txt", Constants.ENCODE_UTF8, params);
			if(htmlContent != null && !htmlContent.isEmpty()){
				Document doc = Jsoup.parse(htmlContent);
				Element stock = doc.getElementById("T_STOCK1");//表格所在div
				if(stock != null && stock.text().matches("[0-9\\.]+")){
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("全国", stock.text());
					int timeInt = Integer.parseInt(curDateStr.replace("-", ""));
					dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
				} else {
					LOG.info("库存列为空。");
					RecordCrawlResult.recordFailData(className, varName, cnName, "库存列为空。");
				}
			} else {
				LOG.warn("获取的内容为空。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "获取的内容为空。");
			}
		} catch(Exception e) {
			LOG.error("解析数据时发生异常。", e);
			RecordCrawlResult.recordFailData(className, varName, cnName, "解析数据时发生异常。");
		}
	}
	
	public static void main(String[] args) {
		ChinajciDataFetch c = new ChinajciDataFetch();
//		c.start1();
//		c.start2();
//		c.start3();
		//米糠油-价格
//		c.fetchRiceOilPrice("2016-12-05");
		//米糠油-油糠价格 
//		c.fetchBranOilPrice("2016-12-05");
		//玉米油-玉米胚芽粕
//		c.fetchCornGermMeal("2016-12-05");
		//花生-现货
//		c.fetchPeanutSpotPrice("2016-12-05");
		//葵花籽-收购价格
//		c.fetchSunflowlerSeedPrice("2016-12-05");
		//葵花籽油-价格
//		c.fetchSunflowlerOil("2016-12-05");
		//芝麻-贸易商报价
//		c.fetchSesamePrice("2016-12-05");
		//芝麻油-贸易商价格
//		c.fetchSesameOilPrice("2016-12-05");
		//豆油-四级豆油现货价格
//		c.fetchSoyoilsPrice("2016-12-05");
		//豆油-四级豆油成交均价
//		c.fetchSoyoilsHammerPrice("2016-12-05");
		//豆油-一级豆油成交均价
//		c.fetchFirstOrderSoyoilsPrice("2016-12-05");
		
		//油料油脂-豆油-油厂库存
//		c.fetchSoyoilsPortStocks("2016-12-07");
		//油料油脂-大豆-大豆港口库存
		c.fetchSoybeanPortStocks("2016-12-23");
	}
}
