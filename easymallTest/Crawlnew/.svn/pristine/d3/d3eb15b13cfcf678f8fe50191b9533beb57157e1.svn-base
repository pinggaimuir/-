package cn.futures.data.importor.crawler.ymtCrawler;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;

/**
 * 一亩田价格行情:只能抓当天数据
 * @author ctm
 *
 */
public class YMTDataFetch {
	private static final String className = YMTDataFetch.class.getName();
	private Log logger = LogFactory.getLog(YMTDataFetch.class);
	private static String url = "http://hangqing.ymt.com/jiage/";
	private DataFetchUtil fetchUtil = new DataFetchUtil(true);
	
	@Scheduled
	(cron=CrawlScheduler.CRON_YMT_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("一亩田价格行情", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到一亩田价格行情在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchData();
			}else{
				logger.info("抓取一亩田价格行情的定时器已关闭");
			}
		}
	}
	
	public void fetchData(){
		logger.info("======一亩田价格行情=====");
		Date now = new Date();
		String time = DateTimeUtil.formatDate(now, "yyyy-MM-dd");
		String[] filters = {"div", "class", "jg_area_left"};
		for(String kind:ParamsMap.ymtcomKindReflectMap.keySet()){
			logger.info("===="+kind+" start====");
			Map<String, String> varNameMap = ParamsMap.ymtcomKindReflectMap.get(kind);
			for(String varName:varNameMap.keySet()){
				String code = varNameMap.get(varName);
				String contentSave = "";
				for(int page=0;page<100;page++){
					String pageUrl = url+code+"/"+page*20;
					String contents = fetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, filters, null, 0);
					if(contents != null && !contents.equals("")){
						logger.info("分析第 "+(page+1)+" 页数据");
						String keyContents = StringUtils.substringBetween(contents, "class=\"price_list\">", "</ul>");
						if(keyContents == null) break;
						keyContents = keyContents.replaceAll("<[^>]*>", "").replaceAll("(\\s+)", ",").replaceAll("([,]+)", ",");
						if(keyContents.indexOf(time) == -1) break;
						contentSave += fetchUtil.getLineContent(keyContents.substring(keyContents.indexOf("报价时间,")+5), 4, time);
					}else{
						logger.info(kind+varName+"第 "+(page+1)+" 页没有抓取到内容");
						break;
					}
				}
				if(contentSave.equals("")){
					logger.info(varName+"没有需要保存的内容");
					continue;
				}
				logger.info("保存到本地文本文件");
				try {
					String timeInt = DateTimeUtil.formatDate(now, "yyyyMMdd");
					String dirString = Constants.YMTCURDATA_ROOT + Constants.FILE_SEPARATOR + kind + Constants.FILE_SEPARATOR + varName + Constants.FILE_SEPARATOR;
					if (new File(dirString + timeInt + ".txt").exists())
						logger.warn("Overwrite: "+ varName);
					FileStrIO.saveStringToFile(contentSave, dirString,	timeInt + ".txt");
					logger.info("data saved: " + varName);
				} catch (IOException e) {
					logger.error("IOException while saving "+varName+" data:", e);
				}
			}
			logger.info("===="+kind+" finished====");
		}
	}
	
	public static void main(String[] args){
		String[] filters = {"div", "class", "jg_area_left"};
		//String contents = new DataFetchUtil(true).getPrimaryContent(0, "http://www.ymt.com/", "utf-8", "玉米", filters, null, 0);
			
		new YMTDataFetch().start();
	}
}
