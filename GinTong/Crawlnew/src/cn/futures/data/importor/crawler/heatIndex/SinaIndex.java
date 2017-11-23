package cn.futures.data.importor.crawler.heatIndex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.futures.data.entity.JsonData01;
import cn.futures.data.entity.JsonSent01;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.WebServiceTool;

/**
 * 新浪微舆情 热度指数 爬虫 {"大蒜","生姜","苹果","白砂糖","绿豆"}
 * */
public class SinaIndex {
	//http://wyq.sina.com/view/hotSearch/goSearch.action//数据源
	private static String className = SinaIndex.class.getName();
	private static final String baseUrl = "http://wyq.sina.com/dwr/exec/EChartsDwr.getHotTableAndLine.dwr";//数据源de异步请求
	private static final String[] keywords ={"大蒜","生姜","苹果","白砂糖","绿豆"};//要爬取的种类名称
	private static final Logger LOG = Logger.getLogger(SinaIndex.class);
	
	/**
	 * 定时任务，定时爬取新浪微舆情大蒜热度指数
	 * update bric_crawler_table set name = '新浪微舆情热度值数据',detail='品种：大蒜，生姜，白砂糖，苹果，绿豆'，remark='来源：新浪微舆情' where ID = 57;
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_HEADLINEINDEXGARLIC)
	public void start(){
		/*//新增数据抓取的记录，开启开关
		CrawlerManager manager = new CrawlerManager();
		String[] param = {"爬取大蒜每日热度值数据",className.substring(className.lastIndexOf(".")+1),"新浪微舆情、今日头条","0 0 1/3 * * ?","1","0","0",baseUrl};
		manager.insertCrawler(param);*/
		String switchFlag = new CrawlerManager().selectCrawler("新浪微舆情每日热度值数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到新浪微舆情每日热度值数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到新浪微舆情每日热度值数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				for (int j = 0; j < keywords.length; j++) {
					fetch(date,keywords[j]);
				}
			}else{
				LOG.info("新浪微舆情每日热度值数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "新浪微舆情每日热度值数据的定时器已关闭");
			}
		}
	}
	
	/**
	 * 数据抓取,定时任务
	 * @param date
	 */
	public void fetch(Date date,String keyword){
		LOG.info("------开始抓取"+keyword+"每日热度值数据------");
		String timeInt = DateTimeUtil.formatDate(date, DateTimeUtil.YYYYMMDDHH);
		//获取爬取网页的post请求参数
		Map<String, String> params = getParams(date,keyword);
		String content1 = doPost(baseUrl,Constants.ENCODE_UTF8,params);
		//对字符串进行截取，获取有效数据
		int beginindex = content1.indexOf(":")+1;
		int endindex = content1.indexOf(",");
		String sinawyqdata = content1.substring(beginindex, endindex);
		LOG.info("------抓取到"+keyword+timeInt+"热度值数据------"+sinawyqdata);
		//上传到数据库,拼接参数
		JsonData01 jsonData01 = new JsonData01(timeInt,keyword,"sina",sinawyqdata);
		ArrayList<JsonData01> datalist = new ArrayList<JsonData01>();
		datalist.add(jsonData01);
		JsonSent01 jsonSent01 = new JsonSent01("liww","weiwei","varName,TimeInt,index_source","sentiment_tracking",true,datalist);
		Gson gson = new GsonBuilder().create();
		String datagson = gson.toJson(jsonSent01);
		//上传到数据库，发送post请求
		String url = "http://dbm3.ncpqh.com/warehouse/byDbName";
		String postparam = "data="+datagson;
		String re = WebServiceTool.postRequest(url,postparam);
		LOG.info(re);
	}
	
	/**
	 * post请求获取页面，返回含有关键 信息的字符串
	 * @param url
	 * @param encode
	 * @param param
	 * @return
	 */
	public static String doPost(String url, String encode,Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), encode);
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				LOG.error(e);
			}
		}
		return resultString;
	}
	

	/**
	 * 拼接、处理post请求所需的参数
	 * @param date
	 * @return
	 */
	public static final Map<String, String> getParams(Date date,String keyword){
		//异步所需的参数拼接
		Map<String, String> params = new HashMap<String, String>();
		//decodetime = URLDecoder.decode("%2021%3A43%3A32", "UTF-8");//解析参数中时分秒格式
		//System.out.println(decodetime);
		String begintime = DateTimeUtil.formatDate(date, DateTimeUtil.YYYY_MM_DD);//起始日期（即要查的数据的日期），精确到秒，时分秒要编码
		Date endDate = DateTimeUtil.addDay(date, 1);
		String endtime = DateTimeUtil.formatDate(endDate, DateTimeUtil.YYYY_MM_DD);//截止日期（要查数据的时间的后一天）
		StringBuffer buffer = new StringBuffer();
		//拼接 HH:MM:SS
		buffer.append(" "+date.getHours()+":").append(date.getMinutes()+":").append(date.getSeconds());
		String newKeyword = null;
		String subtime = null;
		try {
			//对 关键字 以及 时间的时分秒 进行编码
			newKeyword = URLEncoder.encode(keyword, "utf-8");
			subtime = URLEncoder.encode(buffer.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error(e);
		}
		params.put("callCount", "1");
		params.put("c0-scriptName", "EChartsDwr");
		params.put("c0-methodName", "getHotTableAndLine");
		params.put("c0-id", "8345_1496583815305");
		//params.put("c0-param0", "string:%E5%A4%A7%E8%92%9C");
		params.put("c0-param0", "string:"+newKeyword);
		params.put("c0-param1", "string:");
		params.put("c0-param2", "string:24");
		params.put("c0-param3", "string:"+newKeyword);
		params.put("c0-param4", "string:");
		params.put("c0-param5", "string:"+begintime+subtime);//开始时间点，时分秒进行编码
		params.put("c0-param6", "string:"+endtime+subtime);
		params.put("c0-param7", "number:8");
		params.put("xml", "true");
		return params;
	}
	
	
	public static void main(String[] args) {
		SinaIndex x = new SinaIndex();
		x.start();
		
	}
	
}
