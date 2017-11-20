package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 商水天气数据：24小时实况数据
 * */
public class ShangShuiWeather {
	
	private static final String url = "http://www.weather.com.cn/weather/101181406.shtml";//数据源网址
	private static String className =  ShangShuiWeather.class.getName();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(ShangShuiWeather.class);
	
	@Scheduled(cron=CrawlScheduler.CRON_SHANGSHUIWEATHER)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("商水天气24小时实况数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有商水天气24小时实况数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到商水天气24小时实况数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchObserve24hData();
			}else{
				LOG.info("抓取商水天气24小时实况数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取商水天气24小时实况数据的定时器已关闭");
			}
		}
	}
	/**
	 * 抓取24小时观察数据
	 * */
	public void fetchObserve24hData(){
		String html = fetchUtil.getCompleteContent(0, url, Constants.ENCODE_UTF8, "商水县24小时天气.txt");
		String varName = "商水气象要素";
		String cnName = "整点实况天气数据";
		Date now = new Date();
		String nowStr = DateTimeUtil.formatDate(now, "yyyyMMddhh");
		try {
			FileStrIO.saveStringToFile(html, Constants.DATABAK_TXT + "商水天气24小时实况数据" + Constants.FILE_SEPARATOR, nowStr + ".txt", Constants.ENCODE_UTF8);
		} catch (IOException e) {
			LOG.error(e);
		}
		
		String dataRegex = "<script>[^<]*var observe24h_data = (\\{[^<]*\\});[^<]*</script>";//"<script>[^<]*var observe24h_data = ({[^<]*});[^<]*</script>";
		List<String> dataList = RegexUtil.getMatchStr(html, dataRegex, new int[]{1});
		if(dataList != null){
			String dataJsonStr = dataList.get(0);
			JSONObject dataJson = JSONObject.fromObject(dataJsonStr);
			JSONObject dataJsonOd = dataJson.getJSONObject("od");
			String time = dataJsonOd.getString("od0");//数据时间
			Date curDate = DateTimeUtil.parseDateTime(time.substring(0, 8), DateTimeUtil.YYYYMMDD);//提取出该曲线图的对应时间
			Date yesDate = DateTimeUtil.addDay(curDate, -1);//计算相应的前一天的时间
			int curTimeStr = Integer.parseInt(DateTimeUtil.formatDate(curDate, DateTimeUtil.YYYYMMDD));
			int yesTimeStr = Integer.parseInt(DateTimeUtil.formatDate(yesDate, DateTimeUtil.YYYYMMDD));
			JSONArray hourDataJson = dataJsonOd.getJSONArray("od2");//24小时实况数据数组
			Iterator<JSONObject> hourDataJsonI = hourDataJson.iterator();
			boolean isYes = false;//是否为前一天的时刻
			int count = 0;//计录提取出的时刻数
			while(hourDataJsonI.hasNext()){//时刻倒序
				JSONObject oneHour = hourDataJsonI.next();//某一时刻的数据
				int hour = oneHour.getInt("od21");//时刻
				int timeInt = 0;//时间序列（精确到小时）
				if(hour == 23 && count != 0){//23不是最新数据时刻时23可作为前一天数据的标记
					isYes = true;
				}
				if(isYes){
					timeInt = yesTimeStr * 100 + hour;
				} else {
					timeInt = curTimeStr * 100 + hour;
				}
				String temperature = oneHour.getString("od22");//温度
				String windDirection = oneHour.getString("od24");//风向
				String windForce = oneHour.getString("od25");//风力等级
				String rainfall = oneHour.getString("od26");//降雨量
				String humidity = oneHour.getString("od27");//相对湿度
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("温度", temperature);
				dataMap.put("风向", windDirection);
				dataMap.put("风力等级", windForce);
				dataMap.put("降雨量", rainfall);
				dataMap.put("相对湿度", humidity);
				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
				count++;
				if(count >= 2){
					break;
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		ShangShuiWeather s = new ShangShuiWeather();
		s.start();
	}
}
