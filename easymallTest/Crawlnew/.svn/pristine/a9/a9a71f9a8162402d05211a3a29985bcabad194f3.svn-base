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
 * 油料油脂-马来西亚棕榈油-涛动指数日值
 * @author ctm
 *
 */
public class MYPalmOilAltIndex {
	private static final String className = MYPalmOilAltIndex.class.getName();
	private static final Log logger = LogFactory.getLog(MYPalmOilAltIndex.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String cnName = "涛动指数日值";
	private static final Map<String, String> MYOIL_INDEX_MAP = new HashMap<String, String>();
	
	static{
		MYOIL_INDEX_MAP.put("马来西亚棕榈油", "https://www.longpaddock.qld.gov.au/seasonalclimateoutlook/southernoscillationindex/30daysoivalues/");
	}
	public static final Map<String, String> FIELD_MAP = new HashMap<String, String>();
	static{
		FIELD_MAP.put("塔希提","1");//Tahiti
		FIELD_MAP.put("达尔文","2");//Darwin
		FIELD_MAP.put("日线","3");//Daily**
		FIELD_MAP.put("日线（30MA）","4");//30 day avg SOI
		FIELD_MAP.put("日线（90MA）","5");//90 day avg SOI
		}
	@Scheduled
	(cron=CrawlScheduler.CRON_MYOIL_INDEX)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("马来西亚棕榈油涛动指数日值", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到马来西亚棕榈油涛动指数日值在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchData(date);
			}else{
				logger.info("抓取马来西亚棕榈油涛动指数日值的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
		}
		for(String varName:MYOIL_INDEX_MAP.keySet()){
			String url = MYOIL_INDEX_MAP.get(varName);
			int varId = Variety.getVaridByName(varName);
			logger.info("start fetch " + varName + varId +"-"+cnName+ "@" + url);
			String[] params = {"table", "class", "data"};//通过params过滤table
			String[] rowColChoose = {"0", ""};//选择哪几行哪几列
			String contents = dataFetchUtil.getPrimaryContent(0, url, "gbk", varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
			}else{
				Map<String, String> fieldDataMap = new HashMap<String, String>();
				logger.info("开始分析数据");
				int newestTimeInt = DAOUtils.getNewestTimeInt(dao.getTableName(varId, cnName));
				String[] lineContents = contents.split("\n");
				boolean havaData = false;
				for(String line:lineContents){
					String[] fieldDate = line.split(",");
					try {
						Date d = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH).parse(fieldDate[0]);
						if(d.before(date) && d.after(DateTimeUtil.parseDateTime(newestTimeInt+"", "yyyyMMdd"))){
							havaData = true;
							String timeIntSave = DateTimeUtil.formatDate(d, "yyyyMMdd");
							logger.info("存在" + timeIntSave + "数据");
							for(String field:FIELD_MAP.keySet()){
								fieldDataMap.put(field, fieldDate[Integer.parseInt(FIELD_MAP.get(field))]);
							}
							dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntSave), fieldDataMap);
						}
					} catch (ParseException e) {
						e.printStackTrace();
						logger.error("网页中日期格式有改动");
					}
				}
				if(!havaData){
					logger.info("没有数据需要保存");
				}
			}
		}
	}
	
	public static void main(String[] args){
		new MYPalmOilAltIndex().start();
	}
}
