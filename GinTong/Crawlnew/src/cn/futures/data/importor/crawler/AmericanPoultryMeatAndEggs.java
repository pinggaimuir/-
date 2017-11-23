package cn.futures.data.importor.crawler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.testng.log4testng.Logger;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 谷物及畜牧饲料行业-分国别-美国禽肉-肉鸡种蛋入孵量、肉鸡雏订购量
 * @author bric_yangyulin
 * @date 2016-08-19
 * */
public class AmericanPoultryMeatAndEggs {
	
	private static final Logger LOG = Logger.getLogger(AmericanPoultryMeatAndEggs.class);
	private static String className = AmericanPoultryMeatAndEggs.class.getName();
	private MyHttpClient myHttpClient = new MyHttpClient();
	private DAOUtils dao = new DAOUtils();
	private static final String encoding = "utf-8";
	private static String url = "http://usda.mannlib.cornell.edu/usda/nass/BroiHatc//2010s/2016/BroiHatc-#date.txt";//基础url #date字段形如“08-17-2016”
	private static Map<String, String> columnMap = new HashMap<String, String>();//城市英文名-列名 映射
	private static Map<String, String> monthNumMap = new HashMap<String, String>();//月份英文-数字映射
	static {
		columnMap.put("Alabama", "阿拉巴马州");
		columnMap.put("Arkansas", "阿肯色州");
		columnMap.put("Delaware", "特拉华州");
		columnMap.put("Florida", "佛罗里达州");
		columnMap.put("Georgia", "格鲁吉亚州");
		columnMap.put("Kentucky", "肯塔基州");
		columnMap.put("Louisiana", "路易斯安那州");
		columnMap.put("Maryland", "马里兰州");
		columnMap.put("Mississippi", "密西西比州");
		columnMap.put("Missouri", "密苏里州");
		columnMap.put("North Carolina", "北卡罗来纳州");
		columnMap.put("Oklahoma", "俄克拉何马州");
		columnMap.put("Pennsylvania", "宾夕法尼亚州");
		columnMap.put("South Carolina", "南卡罗来纳州");
		columnMap.put("Texas", "得克萨斯州");
		columnMap.put("Virginia", "弗吉尼亚州");
		columnMap.put("California, Tennessee, and West Virginia", "加利福尼亚州和田纳西州和西弗吉尼亚州");
		columnMap.put("Other States", "其他州");
		columnMap.put("United States", "全国");
		
		monthNumMap.put("January", "01");
		monthNumMap.put("February", "02");
		monthNumMap.put("March", "03");
		monthNumMap.put("April", "04");
		monthNumMap.put("May", "05");
		monthNumMap.put("June", "06");
		monthNumMap.put("July", "07");
		monthNumMap.put("August", "08");
		monthNumMap.put("September", "09");
		monthNumMap.put("October", "10");
		monthNumMap.put("November", "11");
		monthNumMap.put("December", "12");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_AM_POULTEYMEAT_EGGS)	//每周四的上午3:00-8:00之间抓取上上周六的数据
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("肉鸡种蛋入孵量与肉鸡雏订购量", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到肉鸡种蛋入孵量与肉鸡雏订购量爬虫的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到肉鸡种蛋入孵量与肉鸡雏订购量爬虫的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date curDate = new Date();//当前时间
					Date lastSaturday = DateTimeUtil.addDay(curDate, -12);
					String timeIntExcept = DateTimeUtil.formatDate(lastSaturday, DateTimeUtil.YYYYMMDD);//期望抓取数据的时间序列（即上上周六）
					String dateParam = DateTimeUtil.formatDate(DateTimeUtil.addDay(curDate, -8), "MM-dd-yyyy");//相应数据文件下载链接中的日期参数（一般情况为上周三）。
					fetchData(timeIntExcept, dateParam);
				} catch(Exception e){
					LOG.error("发生未知异常：", e);
					RecordCrawlResult.recordFailData(className, null, null, "发生未知异常");
				}
			}else{
				LOG.info("抓取肉鸡种蛋入孵量与肉鸡雏订购量的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取肉鸡种蛋入孵量与肉鸡雏订购量的定时器已关闭");
			}
		}
	}
	
	/**
	 * @param timeIntExcept 期望抓取数据的时间序列
	 * @param dateParam url中date参数
	 * */
	public void fetchData(String timeIntExcept, String dateParam){
		url = url.replace("#date", dateParam);
		String originalData = myHttpClient.getHtmlByHttpClient(url, encoding, null);
		String timeIntStr = null;//数据的时间序列
		String varName = "美国禽肉";//品种
		String cnName = null;//中文名
		boolean isInnerTable = false;//标记是否已进入一个表格，以确定是否按表格内容进行处理。
		Map<String, String> dataMap = new HashMap<String, String>();//存储待入库数据
		try {
			FileStrIO.saveStringToFile(originalData, Constants.OTHER_WEATHER_ROOT + Constants.FILE_SEPARATOR + className.substring(className.lastIndexOf(".")+1) + Constants.FILE_SEPARATOR, timeIntExcept + ".txt", encoding);
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		originalData = originalData.substring(originalData.indexOf("-----"), originalData.lastIndexOf("-----") + 5);
		originalData = originalData.replaceAll("\\n([\\s,:])*\\n", "\n");
		String[] dataLines = originalData.split("\\n");
		for(int lineIndex = 0; lineIndex < dataLines.length; lineIndex++){
			if(dataLines[lineIndex].contains("State") && dataLines[lineIndex-1].contains("Week ending")){
				/*将要开始一张表的解析，初始化相关变量*/
				dataMap.clear();
				timeIntStr = null;
				cnName = null;
				/*设置为进入表格解析范围*/
				isInnerTable = true;
				/*解析时间序列*/
				String[] monthDayStrs = dataLines[lineIndex+1].split(":");
				String[] yearStrs = dataLines[lineIndex+2].split(":");
				timeIntStr = parseTimeInt(monthDayStrs[monthDayStrs.length-1], yearStrs[yearStrs.length-1]);
				/*判断是哪张表*/
				if(dataLines[lineIndex+4].contains("eggs")){
					//说明是肉鸡种蛋入孵量
					cnName = "肉鸡种蛋入孵量";
				} else if(dataLines[lineIndex+4].contains("chicks")){
					//说明是肉鸡雏订购量
					cnName = "肉鸡雏订购量";
				}
				lineIndex += 4;
				continue;
			} else if(isInnerTable){
				if(dataLines[lineIndex].contains("---")){
					isInnerTable = false;//离开表格范围。
					Integer timeInt = null;
					try{
						timeInt = Integer.parseInt(timeIntStr);
					} catch(Exception e) {
						LOG.error("timeInt解析异常。");
					}
					if(timeInt != null){
						dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						if(timeInt.equals(timeIntExcept)){
							LOG.warn("时间序列非期望值，请注意查看。");	
						}
					} else {
						LOG.warn("时间序列获取失败。");
					}
					continue;
				} else {
					/*解析数据*/
					String[] oneCity = dataLines[lineIndex].split(":");//一个城市的数据
					if(oneCity[0].contains(".")){//城市英文名称：数据
						String cityName = oneCity[0].substring(0, oneCity[0].indexOf('.') - 1);
						String[] cityData = oneCity[1].split("(\\s)+");
						if(cityData.length == 7){
							String latestData = cityData[6];
							if(latestData != null){
								latestData = latestData.replace(",", "");
							}
							if(columnMap.get(cityName) != null && !columnMap.get(cityName).isEmpty()){
								dataMap.put(columnMap.get(cityName), latestData);
							}
						}
					} else {//城市名称较长，换行
						lineIndex++;
						String cityName = oneCity[0].substring(0, oneCity[0].lastIndexOf(',') + 1);
						oneCity = dataLines[lineIndex].split(":");
						cityName = cityName + oneCity[0].substring(0, oneCity[0].indexOf('.') - 1);
						if(oneCity.length == 2){//城市英文名称：数据
							String[] cityData = oneCity[1].split("(\\s)+");
							if(cityData.length == 7){
								String latestData = cityData[6];
								if(latestData != null){
									latestData = latestData.replace(",", "");
								}
								if(columnMap.get(cityName) != null && !columnMap.get(cityName).isEmpty()){
									dataMap.put(columnMap.get(cityName), latestData);
								}
							}
						}
					}
				}
			} else {
				continue;
			}
		}
	}
	
	/**
	 * 解析出时间序列
	 * @param monthDayStr 包含月日的字符串，如“ August 13,  ”
	 * @param yearStr 包含年的字符串，如：“    2016     ”
	 * */
	public String parseTimeInt(String monthDayStr, String yearStr){
		String timeIntStr = yearStr.replaceAll("\\s", "");
		int[] index = {1,3};
		List<String> list = RegexUtil.getMatchStr(monthDayStr, "([A-Za-z]+)(\\s*)(\\d+)", index);
		String monthStr = list.get(0);
		if(monthNumMap.get(monthStr) != null ){
			timeIntStr += monthNumMap.get(monthStr);
		}
		String dayStr = list.get(1);
		if(dayStr.length() == 1){
			dayStr = '0' + dayStr;
		}
		timeIntStr += dayStr;
		return timeIntStr;
	}
	
	public static void main(String[] args) {
		AmericanPoultryMeatAndEggs apmae = new AmericanPoultryMeatAndEggs();
//		apmae.start();
		/*补指定日期数据*/
		String timeIntExcept = "20160917";//期望数据的时间序列
		String dateParam = "09-21-2016";//url中的日期参数
		apmae.fetchData(timeIntExcept, dateParam);
	}
}
