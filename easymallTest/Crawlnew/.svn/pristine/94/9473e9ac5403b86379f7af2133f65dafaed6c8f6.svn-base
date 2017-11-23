package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;

/**
 * 棉花纺织-皮棉-Cotlook棉价指数
 * @author ctm
 *
 */
public class CotlookAIndexFetch {
	private static final String className = CotlookAIndexFetch.class.getName();
	private static final Log logger = LogFactory.getLog(CotlookAIndexFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String cnName = "Cotlook棉价指数";
	private static final Map<String, String> COTLOOKA_INDEX_MAP = new HashMap<String, String>();
	static{
		COTLOOKA_INDEX_MAP.put("皮棉", "https://www.cotlook.com/");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_COTLOOKA_INDEX)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("Cotlook棉价指数", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到"+cnName+"在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				date = DateTimeUtil.addDay(date, -1);
				fetchData(date);
			}else{
				logger.info("抓取"+cnName+"的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String[] dateStr = date.toString().split(" ");
		if(dateStr[2].startsWith("0")){
			dateStr[2] = Integer.parseInt(dateStr[2])+"";
		}
		String encoding = "gbk";
		for(String varName:COTLOOKA_INDEX_MAP.keySet()){
			String url = COTLOOKA_INDEX_MAP.get(varName);
			int varId = Variety.getVaridByName(varName);
			logger.info("start fetch " + varName + varId +"-"+cnName+ "@" + url);
			String[] params = {"table","tbody"};//通过params过滤table
			String[] rowColChoose = {"100","0101"};//选择哪几行哪几列
			String contents = dataFetchUtil.getPrimaryContent(2, url, encoding, varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
			}else{
				logger.info("开始分析数据");
				String[] fields = contents.split("\n")[0].split(",");
				//截取年月日分析是否是前一天数据  取哪天数据抓哪个数据
				int len = fields[1].length();
				String dateTmp = fields[1].substring(len - 13, len - 11) + fields[1].substring(len - 9);
				Date d = null;
				try {
					d = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH).parse(dateTmp);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.error("网页中日期格式有改动");
				}
				String timeIntSave = DateTimeUtil.formatDate(d, "yyyyMMdd");
//				if(DateTimeUtil.formatDate(d, "yyyyMMdd").equals(timeInt)){
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("指数", fields[0]);
					logger.info("timeInt： "+timeIntSave);
					dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeIntSave), dataMap);
//				}else{
//					logger.warn("网页中的Cotlook棉价指数数据不是"+timeInt+"数据");
//				}
			}
		}
	}
	
	public static void main(String[] args){
		new CotlookAIndexFetch().start();
	}
}
