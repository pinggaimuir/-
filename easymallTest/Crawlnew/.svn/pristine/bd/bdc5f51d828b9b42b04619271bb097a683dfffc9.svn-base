package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
 * 棉花纺织-棉纱-Cotlook棉纱指数
 * @author ctm
 *
 */
public class CotlookYarnIndexFetch{
	private static final String className = CotlookYarnIndexFetch.class.getName();
	private static final Log logger = LogFactory.getLog(CotlookYarnIndexFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String cnName = "Cotlook棉纱指数";
	private static final Map<String, String> COTLOOK_YARN_INDEX_MAP = new HashMap<String, String>();
	static{
		COTLOOK_YARN_INDEX_MAP.put("棉纱", "http://www.cottonchina.org/newprice/index.php");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_COTLOOK_YARN_INDEX)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("Cotlook棉纱指数", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到"+cnName+"在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchData(date);
			}else{
				logger.info("抓取"+cnName+"的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
		}
		String encoding = "gbk";
		for(String varName:COTLOOK_YARN_INDEX_MAP.keySet()){
			String url = COTLOOK_YARN_INDEX_MAP.get(varName);
			int varId = Variety.getVaridByName(varName);
			logger.info("start fetch " + varName + varId +"-"+cnName+ "@" + url);
			String[] params = {"table"};//通过params过滤table
			String[] rowColChoose = {"00",""};//选择哪几行哪几列
			int elementAtIndex = 30;
			String contents = dataFetchUtil.getPrimaryContent(0, url, encoding, varName, params, rowColChoose, elementAtIndex);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
			}else{
				logger.info("开始分析数据");
				String[] fields = contents.split("\n")[0].split(",");
				//截取年月日分析是否是上个星期数据
				Date d = null;
				try {
					d = new SimpleDateFormat("yyyy-MM-dd").parse(fields[0]);
				} catch (ParseException e) {
					logger.error("网页格式有变动");
				}
				if(!DateTimeUtil.isLastDateTime(d, date)){
					logger.warn("网页中的Cotlook棉纱指数数据不是上个星期数据");
				}
				String timeInt = DateTimeUtil.formatDate(d, "yyyyMMdd");
				Map<String, String> dataMap = new HashMap<String, String>();
				dataMap.put("指数", fields[1]);
				logger.info("timeInt： "+timeInt);
				dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
			}
		}
	}
	
	public static void main(String[] args){
		new CotlookYarnIndexFetch().start();
	}
}
