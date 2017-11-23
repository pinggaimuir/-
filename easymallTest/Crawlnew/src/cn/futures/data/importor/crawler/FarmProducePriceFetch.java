package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.ListInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.MyHttpClient;

/**
 * 农副产品价格
 * @author ctm
 *
 */
public class FarmProducePriceFetch {
	private static final String className = FarmProducePriceFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final Log logger = LogFactory.getLog(FarmProducePriceFetch.class);
	private static final MyHttpClient httpClient = new MyHttpClient();
	//http://cif.mofcom.gov.cn/cif/html/second/index.html
	private static final String urlBase = "http://cif.mofcom.gov.cn/site/queryReport.do?rptData.rptCategory.id=";//224053&startDate=2015-04-03&endDate=2015-04-24
	
	@Scheduled
	(cron=CrawlScheduler.CRON_FARM_PRODUCE_PRICE)
	public void start(){
		//商务部水果批发价西瓜监测是否有更新
		int timeInt = DAOUtils.getNewestTimeInt("CX_watermelon");
		if(DateTimeUtil.isLastDateTime(DateTimeUtil.parseDateTime(timeInt+"", "yyyyMMdd"), new Date())){
			logger.info("全国农副产品批发价格上周数据已经更新");
		}else{
			String switchFlag = new CrawlerManager().selectCrawler("全国农副产品批发价格", className.substring(className.lastIndexOf(".")+1));
			if(switchFlag == null){
				logger.info("没有获取到全国农副产品批发价格在数据库中的定时器配置");
			}else{
				if(switchFlag.equals("1")){
					fetchData();
				}else{
					logger.info("抓取全国农副产品批发价格的定时器已关闭");
				}
			}
		}
	}
	
	public void fetchData(){
		String[] dates = null;
		String[] datas = null;
		for(String info:ListInit.cifmofcomList){
        	String[] tmp = info.split(",");
        	String varName = tmp[2];
        	String cnName = tmp[0];
        	if(tmp.length == 5){
        		//需要抓取历史数据的备注
        		String url = urlBase + tmp[3]+"&startDate=2006-01-06&endDate=2015-05-01";
        		logger.info("start fetch " + varName + tmp[0] + "@" + url);
        		String responseBody = httpClient.getResponseBody(url);
    			Pattern pattern = Pattern.compile("[^/]javaArray(.+)(0)(.+)new Array(.+);");
    			Matcher matcher = pattern.matcher(responseBody);
    			if(matcher.find()){
    			    System.out.println(matcher.group(4));
    			    String dateStr = matcher.group(4);
    			    dates = dateStr.substring(2,dateStr.length()-2).split("','");
    			}else{
    				logger.error("未匹配到"+tmp[2]+tmp[0]+"日期数据");
    			}
    			pattern = Pattern.compile("javaArray(.+)(1)(.+)new Array(.+);");
    			matcher = pattern.matcher(responseBody);
    			if(matcher.find()){
    			    System.out.println(matcher.group(4));
    			    String dataStr = matcher.group(4);
    			    datas = dataStr.substring(2,dataStr.length()-2).split("','");
    			}else{
    				logger.error("未匹配到"+tmp[2]+tmp[0]+"数据");
    			}
    			for(int i=0;i<dates.length;i++){
    				Date d = null;
					try {
						d = new SimpleDateFormat("yy/MM/dd").parse(dates[i]);
					} catch (ParseException e) {
						e.printStackTrace();
						logger.error("日期格式转换错误");
					}
    				int timeInt = Integer.parseInt(DateTimeUtil.formatDate(d, "yyyyMMdd"));
    				Map<String, String> dataMap = new HashMap<String, String>();
    				dataMap.put("全国", datas[i]);
    				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
    			}
        	}else{
        		String url = urlBase + tmp[3];
            	logger.info("start fetch " + varName + tmp[0] + "@" + url);
    			String responseBody = httpClient.getResponseBody(url);
    			Pattern pattern = Pattern.compile("[^/]javaArray(.+)(0)(.+)new Array(.+);");
    			Matcher matcher = pattern.matcher(responseBody);
    			if(matcher.find()){
    			    System.out.println(matcher.group(4));
    			    String dateStr = matcher.group(4);
    			    dates = dateStr.substring(2,dateStr.length()-2).split("','");
    			    Date d = null;
					try {
						d = new SimpleDateFormat("yy/MM/dd").parse(dates[dates.length-1]);
					} catch (ParseException e) {
						e.printStackTrace();
						logger.error("日期格式转换错误");
					}
    			    if(DateTimeUtil.isLastDateTime(d, new Date())){
    			    	pattern = Pattern.compile("javaArray(.+)(1)(.+)new Array(.+);");
		    			matcher = pattern.matcher(responseBody);
		    			if(matcher.find()){
		    			    System.out.println(matcher.group(4));
		    			    String dataStr = matcher.group(4);
		    			    datas = dataStr.substring(2,dataStr.length()-2).split("','");
		    				int timeInt = Integer.parseInt(DateTimeUtil.formatDate(d, "yyyyMMdd"));
		    				Map<String, String> dataMap = new HashMap<String, String>();
		    				dataMap.put("全国", datas[datas.length-1]);
		    				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
		    			}else{
		    				logger.error("未匹配到"+tmp[2]+tmp[0]+"数据");
		    			}
    			    }else{
    			    	logger.error("抓取"+tmp[2]+tmp[0]+"数据不是上个星期的数据");
    			    }
    			}else{
    				logger.error("未匹配到"+tmp[2]+tmp[0]+"日期数据");
    			}
        	}
		}
	}
	
	public static void main(String[] args){
		new FarmProducePriceFetch().start();
	}
}
