package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.WareHouse;

/**
 * 泰国天气爬虫，抓前一天的数据数据
 * @author bric_yangyulin
 * @date 2016-04-25
 * */
public class ThailandWeather {
	private static final String className = ThailandWeather.class.getName();
	private final static String url = "http://www.tmd.go.th/en/climate.php?FileID=1";
	private final static String encoding = "tis-620";//"tis-620"
	private static DAOUtils dao = new DAOUtils();
	private static MyHttpClient myHttpClient = new MyHttpClient();
	private static Map<String, String> varNameMap = new HashMap<String, String>();//网页中的地区名与varName的映射
	private final static Logger LOG = Logger.getLogger(ThailandWeather.class);

	static{
		varNameMap.put("Northern Part", "北部");
		varNameMap.put("Northeastern Part", "东北部");
		varNameMap.put("Central Part", "中部");
		varNameMap.put("Eastern Part", "东部");
		varNameMap.put("Southern Part (East Coast)", "南部（东海岸）");
		varNameMap.put("Southern Part (West Coast)", "南部（西海岸）");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_THAILAND_WEATHER)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("泰国天气数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到泰国天气数据在数据库中的定时器配置");
		}else{
			try{
				Date date = DateTimeUtil.addDay(new Date(), -1);
				String time = DateTimeUtil.formatDate(date, DateTimeUtil.YYYY_MM_DD);
				String[] timeArr = time.split("-");
				fetchData(timeArr[0] + "-" + timeArr[1], timeArr[2]);
			} catch(Exception e) {
				LOG.error("数据抓取失败。");
				RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
			}
		}
	}
	
	public void fetchData(String ddlMonth, String ddlDay){
		LOG.info("----------开始抓取泰国天气数据。----------");
		String time = null;
		String htmlContent = null;
		time = ddlMonth.replace("-", "") + ddlDay;
		int timeInt = Integer.parseInt(time);
		String htmlPath = Constants.SAVEDHTML_ROOT + Constants.FILE_SEPARATOR + time + Constants.FILE_SEPARATOR;
		String htmlFileName = "泰国天气" + ".html";
		Map<String, String> params = new HashMap<String, String>();//post请求参数
		Map<String, String> dataMap = new HashMap<String, String>();
		//抓取html并保存
		params.put("ddlDay", ddlDay);//"24"
		params.put("ddlMonth", ddlMonth);//"2016-04"
//		htmlContent = myHttpClient.getPostHtmlByHttpClient(url, params, encoding);
		htmlContent = myHttpClient.getHtmlByHttpClient(url, "utf-8", "");
		try {
			FileStrIO.saveStringToFile(htmlContent, htmlPath, htmlFileName, encoding);
		} catch (IOException e1) {
			LOG.error("存储html文件时发生异常：" + e1.getMessage());
		}
		
		//解析html（采用Jsoup解析，因为用htmlparser解析时有不明原因的问题：有些数据好像被无视了一样比如数据所在的table）
		Document doc = Jsoup.parse(htmlContent);
		Elements elements = doc.select("table[width=100%]");
		Element tableEle = elements.get(0);
		Elements trs = tableEle.select("tr[class=RH2], tr[class=RDS], tr[class=RADS]");
		Iterator<Element> iterTr = trs.iterator();
		String varName = null;//品种名
		while(iterTr.hasNext()){
			Element tr = iterTr.next();
			String attrClass = tr.attr("class");
			if(attrClass != null && attrClass.equals("RH2")){//标识地区（如：Northern Part）的行
				Elements b = tr.select("b");
				if(b != null && !b.isEmpty()){
					String areaName = b.get(0).text();//区域名（在表中未品种名）
					varName = varNameMap.get(areaName);
				} else {
					LOG.info("表格格式可能发生了变化，数据抓取中断,请查看后重新抓取。");
					RecordCrawlResult.recordFailData(className, null, null, "\"表格格式可能发生了变化，数据抓取中断,请查看后重新抓取。\"");
					return;
				}
			} else if(attrClass != null && attrClass.equals("RADS") || attrClass.equals("RDS")) {//标识城市天气数据的行。
				String[] oneRow = new String[8];//存储一行中的8列数据
				Elements tds = tr.select("td");
				Iterator<Element> iterTd = tds.iterator();
				int columnId = 0;//标识列的位序（因为有些连续列为空时会合并为一列）
				while(iterTd.hasNext()){
					Element td = iterTd.next();//某列数据
					String colspan = td.attr("colspan");
					if(colspan != null && !colspan.isEmpty()){
						try{
							int colspanInt = Integer.parseInt(colspan);
							if(colspanInt == 1){
								if((td.text()).equals("-") || (td.text()).equals("Trace")){//比如日降雨量有时为"-"或"Trace"
									oneRow[columnId] = null;
								} else {
									oneRow[columnId] = td.text();
								}
								columnId++;
							} else {
								for(int i = 0; i < colspanInt; i++){//多列合并的为连续列无数据为"- N/A -"
									oneRow[columnId] = null;
									columnId++;
								}
							}
						} catch(Exception e) {
							LOG.error(td + "---colspan转换为整型时发生异常：" + e.getMessage());
						}
					} else {
						if((td.text()).equals("-") || (td.text()).equals("Trace")){//比如日降雨量有时为"-"或"Trace"
							oneRow[columnId] = null;
						} else {
							oneRow[columnId] = td.text();
						}
						
						columnId++;
					}
				}
				String cnName = null;//Location列为中文名
				if(oneRow[0] != null && !oneRow[0].isEmpty()){
					cnName = oneRow[0].replaceAll("\\.|\\*", "");
					cnName = ThailandWeather.initialUppercaseOneWord(cnName);
//					System.out.println("cnName: " + cnName);
				} else {
					LOG.info("出现了Location为空的情况。---" + tr.html());
					RecordCrawlResult.recordFailData(className, varName, cnName, "出现了Location列（中文名）为空的情况");
					continue;
				}
				Double max = null;//最高气温
				Double min = null;//最低气温
				Double ave = null;//平均气温
				String speed = null;//风速
				String dayRainfall = null;//日降雨量（24 hr.）
				String cumulativeRainfall = null;//累计降雨量（Year-to-Date）
				
				if(oneRow[1] != null && !oneRow[1].isEmpty()){
					try{
						max = Double.parseDouble(oneRow[1]);
					} catch(Exception e) {
						LOG.error(tr.html() + "---类型转换时发生异常：" + e.getMessage());
					}
				}
				if(oneRow[2] != null && !oneRow[2].isEmpty()){
					try{
						min = Double.parseDouble(oneRow[2]);
					} catch(Exception e) {
						LOG.error(tr.html() + "---类型转换时发生异常：" + e.getMessage());
					}
				}
				if(max != null && min != null){
					ave = (max + min) / 2;
				}
				speed = oneRow[4];
				dayRainfall = oneRow[6];
				cumulativeRainfall = oneRow[7];
				if(max != null){
					dataMap.put("最高气温", max.toString());
				} else {
					dataMap.put("最高气温", null);
					RecordCrawlResult.recordFailData(className, varName, cnName, "最高气温为空");
				}
				if(min != null){
					dataMap.put("最低气温", min.toString());
				} else {
					dataMap.put("最低气温", null);
					RecordCrawlResult.recordFailData(className, varName, cnName, "最低气温为空");
				}
				if(ave != null){
					dataMap.put("平均气温", ave.toString());
				} else {
					dataMap.put("平均气温", null);
					RecordCrawlResult.recordFailData(className, varName, cnName, "平均气温为空");
				}
				//当值大于1000后会变成1,046.7，需去除逗号。
				if(dayRainfall != null){
					dayRainfall = dayRainfall.replace(",", "");
				}
				if(cumulativeRainfall != null){
					cumulativeRainfall = cumulativeRainfall.replace(",", "");
				}
				dataMap.put("风速", speed);
				dataMap.put("日降雨量", dayRainfall);
				dataMap.put("累计降雨量", cumulativeRainfall);
				
				//保存数据到excel文件
				String filePathSuffix = Constants.OTHER_WEATHER_ROOT + Constants.FILE_SEPARATOR + "ThailandWeather" + Constants.FILE_SEPARATOR;
				WareHouse.saveToExcel(varName, cnName, timeInt, dataMap, filePathSuffix);
				//存到数据库
				int varId = Variety.getVaridByName(varName);
				dao.saveOrUpdateByDataMap(varId, cnName, timeInt, dataMap);
				//或者也可以从xls文件导入数据库
//				WareHouse.xlsToDb(varName, cnName, timeInt, filePathSuffix);
				dataMap.clear();
			}
		}
		
	}
	
	/**
	 * 将字符串中单词改为首字母大写的形式
	 * */
	public static String initialUppercaseOneWord(String str){
		char[] chars = str.toCharArray();
		for(int i = (chars.length - 1); i > 0; i--){
			if((chars[i] >= 65 && chars[i] <= 90) && (chars[i-1] >= 65 && chars[i-1] <= 90)){
				chars[i] += 32;
			}
		}
		return String.valueOf(chars);
	}
	
	public static void main(String[] args) {
		ThailandWeather tw = new ThailandWeather();
		//补历史数据（指定日期，非前一天）,当天不可以补当天数据，因为网站上当天无有效数据
		//目前不能补历史数据了，没有调好。
//		tw.fetchData("2016-04", "24");
		//补当天定时任务的数据（即前一天的数据）
		tw.start();
		
		//批量补历史数据(仅可补近三个月的数据)
/*
		{
			int dayStart = 1;	//补数据的初始日期
			int dayEnd = 1;	//补数据的结束日期
			String dllMonth = "2016-06";
			for(; dayStart <= dayEnd; dayStart++){
				try{
					if(dayStart < 10){
						LOG.info(String.format("----------开始抓取%s-0%s泰国天气数据。----------", dllMonth, dayStart));
						tw.fetchData(dllMonth, "0" + dayStart);
					} else {
						LOG.info(String.format("----------开始抓取%s-%s泰国天气数据。----------", dllMonth, dayStart));
						tw.fetchData(dllMonth, dayStart+"");
					}
					try {
						Thread.sleep((int)(Math.random() * 5000));
					} catch (InterruptedException e) {
						LOG.error("睡眠意外中断：", e);
					}
				} catch(Exception e) {
					LOG.error("数据抓取失败。day: " + dayStart);
				}
				
			}
		}
*/
	}

}
