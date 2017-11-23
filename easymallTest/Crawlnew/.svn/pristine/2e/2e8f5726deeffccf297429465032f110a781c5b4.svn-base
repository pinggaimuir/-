package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.set.SynchronizedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.bricAnalyse.dayAnalyse;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;

/**	
	* @description	饲料养殖-羊肉-白条羊批发价格 && 饲料养殖-牛肉-白条牛批发价格
	* @author 		xjlong 
    * @date 		2016年9月20日  
*/
public class WhiteSheepAndCowDataFetch {
	private static final String className = WhiteSheepAndCowDataFetch.class.getName();
	private Log logger = LogFactory.getLog(WhiteSheepAndCowDataFetch.class);
	private DAOUtils dao = new DAOUtils();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	/*
	 *抓取饲料养殖-羊肉-白条羊批发价格 
	 */
	private void fetchWhiteSheepwholesalePrice(Date date){
		String timeIntStr = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String timeInt = null;
		String varName = "羊肉";
		int varId = Variety.getVaridByName(varName);
		String cnName = "白条羊批发价格";
		String contents = null;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"111","111"};
		logger.info("抓取饲料养殖-羊肉-白条羊"+timeIntStr+"批发价。。。");
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://cif.mofcom.gov.cn/site/queryReport.do?rptData.rptCategory.id=11450850&newCifHome=true";
		contents = dataFetchUtil.getPrimaryContent(1, baseUrl, "gbk", varName, filters, rowColChoose,4);
		if(contents != null && !contents.equals("")){
			String wholeSalePrice = null;
			String[] b = contents.split("\n");
			timeInt = (b[0].split(",")[8]).replace("/","");
			wholeSalePrice = b[1].split(",")[8];
			dataMap.put("全国",wholeSalePrice);
			if(!dataMap.isEmpty()){
				logger.info("正在保存--"+timeInt+"--的白条羊批发价格。。。");
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}else {
				RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到白条羊相关数据");
			}
		}else{
			logger.info("没有抓取到--白条羊--的数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到白条羊相关数据");
		}
		
	}
	/*
	 	饲料养殖-牛肉-白条牛批发价格
	 */
	private void fetchWhiteCowholesalePrice(Date date){
		String timeIntStr = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String timeInt = null;
		String varName = "牛肉";
		int varId = Variety.getVaridByName(varName);
		String cnName = "白条牛批发价格";
		String contents = null;
		String[] filters = {"table","tr"};
		String[] rowColChoose = {"111","111"};
		logger.info("饲料养殖-牛肉-白条牛批发价格"+timeIntStr+"批发价。。。");
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://cif.mofcom.gov.cn//site/queryReport.do?rptData.rptCategory.id=11450707&newCifHome=true";
		contents = dataFetchUtil.getPrimaryContent(1, baseUrl, "gbk", varName, filters, rowColChoose,4);
		if(contents != null && !contents.equals("")){
			String wholeSalePrice = null;
			String[] b = contents.split("\n");
			timeInt = (b[0].split(",")[8]).replace("/","");
			wholeSalePrice = b[1].split(",")[8];
			dataMap.put("全国",wholeSalePrice);
			if(!dataMap.isEmpty()){
				logger.info("正在保存--"+timeInt+"--的白条牛批发价格。。。");
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}else {
				RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到白条牛相关数据");
			}
		}else{
			logger.info("没有抓取到--白条牛--的数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "没有抓取到白条牛相关数据");
		}
	}
	/*
	 *定时器：每周五下午三点更新数据 
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_AM_WHITESHEEPANDCOW_WEEK)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("饲料养殖白条牛--白条羊批发价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到饲料养殖白条牛--白条羊批发价格在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到饲料养殖白条牛--白条羊批发价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchWhiteCowholesalePrice(date);
					fetchWhiteSheepwholesalePrice(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取饲料养殖白条牛--白条羊批发价格的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取饲料养殖白条牛--白条羊批发价格的定时器已关闭");
			}
		}
	}
	public static void main(String[] args) {
		WhiteSheepAndCowDataFetch wp = new WhiteSheepAndCowDataFetch();
		//wp.fetchWhiteSheepwholesalePrice(new Date());
		wp.fetchWhiteCowholesalePrice(new Date());
	}
}
