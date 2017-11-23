package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;
import cn.futures.data.util.WareHouse;

/**
 * @description 新的印度天气爬虫（针对新的数据来源）
 * @author bric_yangyulin
 * @date 2016-04-27
 * */
public class IndiaWeather {
	
	private static final String className = IndiaWeather.class.getName();
	private static String baseUrl = "http://www.accuweather.com/en/in/#name/#code/daily-weather-forecast/#code";  
	private static MyHttpClient myHttpClient = new MyHttpClient();
	private static DAOUtils dao = new DAOUtils();
	private static int retryTimes = 3;//访问网址失败时重试的次数。 
	private static final String encoding = "UTF-8";
	private static final Logger LOG = Logger.getLogger(IndiaWeather.class); 
	
	@Scheduled
	(cron=CrawlScheduler.CRON_INDIA_WEATHER)
	public void start(){
		try{
			String switchFlag = new CrawlerManager().selectCrawler("印度气象局数据", className.substring(className.lastIndexOf(".")+1));
			if(switchFlag == null){
				LOG.info("没有获取到印度气象局数据的定时器配置");
				RecordCrawlResult.recordFailData(className, null, null, "没有获取到印度气象局数据的定时器配置");
			}else{
				if(switchFlag.equals("1")){
					fetchWeather();
				}else{
					LOG.info("抓取印度气象局数据的定时器已关闭");
					RecordCrawlResult.recordFailData(className, null, null, "抓取印度气象局数据的定时器已关闭");
				}
			}
		} catch(Exception e) {
			LOG.error("发生未知异常。" + e.getMessage());
			RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
		}
	}
	
	private void fetchWeather(){
		String timeStr = DateTimeUtil.formatDate(new Date(), DateTimeUtil.YYYYMMDD);
		int timeInt = Integer.parseInt(timeStr);//时间序列
		Map<String, String> dataMap = new HashMap<String, String>();//"列名-列值"映射
		
		LOG.info("=====开始抓取印度日度天气数据"+timeInt+"=====");
		//针对每个城市(中文名称)进行循环。
		for(String state:MapInit.india_day_weather_state_map.keySet()){//state即为VarName
			Map<String, String> cityMap = MapInit.india_day_weather_state_map.get(state);
			for(String city:cityMap.keySet()){//city即为cnName
				try{
					String htmlContent = null;
					String name = this.transferCnName(city);
					String url = baseUrl.replace("#name", name).replace("#code", cityMap.get(city));
					LOG.info("抓取@" + url);
					try {
						Thread.sleep((int)(Math.random()) * 3000);
					} catch (InterruptedException e1) {
						LOG.error(e1);;
					}
					for(int i = 0; i < retryTimes && (htmlContent == null || "123".contains(htmlContent)); i++){
						htmlContent = myHttpClient.getHtmlByHttpClient(url, encoding, "", 10000);
					}
					if(htmlContent == null || "123".contains(htmlContent)){
						RecordCrawlResult.recordFailData(className, state, city, "抓取html失败");
						continue;
					}
					String htmlPath = Constants.SAVEDHTML_ROOT + Constants.FILE_SEPARATOR + timeStr + Constants.FILE_SEPARATOR;
					String fileName = city + ".html";
					try {
						FileStrIO.saveStringToFile(htmlContent, htmlPath, fileName, encoding);
					} catch (IOException e) {
						LOG.error("保存html文件时发生异常：" + e.getMessage());
					}
					
					//Jsoup解析
					Document doc = Jsoup.parse(htmlContent);
					//气温
					Element temperature = doc.getElementById("feature-history");
					//解析最高气温High行的today和normal项。
					Elements eles = temperature.getElementsByAttributeValue("class", "hi");
					Element ele = eles.get(0);
					eles = ele.getElementsByTag("td");
					ele = eles.get(0);
					String todayHigh = ele.text();//今日最高气温
					todayHigh = todayHigh.replace("°", "");
					if(todayHigh.isEmpty()){//防止其为空存入数据库时默认转化为0。
						todayHigh = null;
					}
					ele = eles.get(1);
					String normalHigh = ele.text();//正常最高气温
					normalHigh = normalHigh.replace("°", "");
					if(normalHigh.isEmpty()){
						normalHigh = null;
					}
					Float highDeviation = null;//最高气温偏离正常值
					if(todayHigh != null && normalHigh != null){
						highDeviation = Float.parseFloat(todayHigh) - Float.parseFloat(normalHigh);
					} else {
						RecordCrawlResult.recordFailData(className, state, city, "今日最高气温或者正常最高气温存在空值。");
					}
					
					//解析最低气温Low行的today和normal项。
					eles  = temperature.getElementsByAttributeValue("class", "last lo");
					ele = eles.get(0);
					eles = ele.getElementsByTag("td");
					ele = eles.get(0);
					String todayLow = ele.text();//今日最低气温
					todayLow = todayLow.replace("°", "");
					if(todayLow.isEmpty()){
						todayLow = null;
					}
					ele = eles.get(1);
					String normalLow = ele.text();//正常最低气温
					normalLow = normalLow.replace("°", "");
					if(normalLow.isEmpty()){
						normalLow = null;
					}
					Float lowDeviation = null;//最低气温偏离正常值
					if(todayLow != null && normalLow != null){
						lowDeviation = Float.parseFloat(todayLow) - Float.parseFloat(normalLow);
					} else {
						RecordCrawlResult.recordFailData(className, state, city, "今日最低气温或者正常最低气温存在空值。");
					}
					
					Float aveT = null;//平均气温
					if(todayHigh != null && todayLow != null){
						aveT = (Float.parseFloat(todayHigh) + Float.parseFloat(todayLow)) / 2;
					} else {
						RecordCrawlResult.recordFailData(className, state, city, "今日最高气温或今日最低气温存在空值。");
					}
					
					//解析降雨量
					Element rainE = doc.getElementById("detail-day-night");
					eles = rainE.select("ul[class=stats]");
					
					String dayRain = null;//白天降雨量
					ele = eles.get(0);
					Elements lis = ele.children();
					for(int i = lis.size()-1; i >= 0; i--){
						ele = lis.get(i);
						String text = ele.text();
						if(text.startsWith("Rain:")){
							dayRain = text.replaceAll("\\s", "");
							List list = RegexUtil.getMatchStr(dayRain, "(?<=Rain:).+(?=mm)", null);
							if(list != null && list.size() > 0){
								dayRain = list.get(0).toString();
							} else {
								dayRain = null;
								LOG.warn("未找到白天降雨量");
							}
							break;
						}
					}
					
					String nightRain = null;//夜晚降雨量
					ele = eles.get(1);
					lis = ele.children();
					for(int i = lis.size()-1; i >= 0; i--){
						ele = lis.get(i);
						String text = ele.text();
						if(text.startsWith("Rain:")){
							nightRain = text.replaceAll("\\s", "");
							List list = RegexUtil.getMatchStr(nightRain, "(?<=Rain:).+(?=mm)", null);
							if(list != null && list.size() > 0){
								nightRain = list.get(0).toString();
							} else {
								nightRain = null;
								LOG.warn("未找到夜晚降雨量");
							}
							break;
						}
					}
					Float rain = null;//今日降雨量
					if(dayRain != null && !dayRain.isEmpty() && 
							nightRain != null && !nightRain.isEmpty()){
						rain = Float.parseFloat(dayRain) + Float.parseFloat(nightRain);
					}
					
					//解析日照时间
					Element sunE = doc.getElementById("feature-sun");
					eles = sunE.select("li[class=duration]");
					ele = eles.get(0).select("span").get(0);
					String sunshine = ele.html().replace(":", ".").replace(" hr", "");
					if(sunshine.isEmpty()){
						sunshine = null;
						RecordCrawlResult.recordFailData(className, state, city, "日照时长为空。");
					}
					
					dataMap.put("最高气温", todayHigh);
					dataMap.put("最低气温", todayLow);
					dataMap.put("最高气温偏离正常值", String.valueOf(highDeviation));
					dataMap.put("最低气温偏离正常值", String.valueOf(lowDeviation));
					dataMap.put("平均气温", String.valueOf(aveT));
					dataMap.put("降雨量", String.valueOf(rain));
					dataMap.put("日照时长", sunshine);
					
//					String filePathSuffix = String.format("%s%s%s%s%s%s%s%s%s", "D:\\Data\\",timeInt,Constants.FILE_SEPARATOR,"IndiaWeatherNewSource",Constants.FILE_SEPARATOR,"农业气象数据", Constants.FILE_SEPARATOR,state,Constants.FILE_SEPARATOR);
					String filePathSuffix = Constants.OTHER_WEATHER_ROOT + Constants.FILE_SEPARATOR + className.substring(className.lastIndexOf(".")+1) + Constants.FILE_SEPARATOR;
					fileName = city + ".xls";
					try {
						WareHouse.saveToXls(filePathSuffix, fileName, city, dataMap);
					} catch (FileNotFoundException e) {
						LOG.error("写成xls时发生异常：" + e.getMessage());
						RecordCrawlResult.recordFailData(className, state, city, "\"写成xls时发生异常" + e.getMessage() + "\"");
					} catch (IOException e) {
						LOG.error("写成xls时发生异常：" + e.getMessage());
						RecordCrawlResult.recordFailData(className, state, city, "\"写成xls时发生异常" + e.getMessage() + "\"");
					};

					//存到数据库
					int varId = Variety.getVaridByName(state);
					dao.saveOrUpdateByDataMap(varId, city, timeInt, dataMap);
					dataMap.clear();
				} catch(Exception e) {
					LOG.info(city + "数据抓取时出现未知异常。", e);
					RecordCrawlResult.recordFailData(className, state, city, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}
		}
		
	}
	
	/**
	 * 将一串只含有字母和空格的字符串转化为字母全部小写空格用'-'代替的字符串。
	 * */
	private String transferCnName(String cnName){
		char[] chars = cnName.toCharArray();
		for(int i = (chars.length - 1); i >= 0; i--){
			if(chars[i] > 96 && chars[i] < 123 ){
				continue;
			} else if(chars[i] > 64 && chars[i] < 91){
				chars[i] += 32;
			} else if(chars[i] == 32){//若为空格则转为-
				chars[i] = 45;
			} else {
				LOG.warn("cnName包含未处理字符，请查看：" + cnName);
			}
		}
		
		return String.valueOf(chars);
		
	}
	
	public static void main(String[] args) {
		IndiaWeather iwns = new IndiaWeather();
//		iwns.fetchWeather();
		iwns.start();
	}
}
