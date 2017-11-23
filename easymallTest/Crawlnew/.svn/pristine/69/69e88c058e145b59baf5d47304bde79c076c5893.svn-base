package cn.futures.data.importor.crawler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.MapInit;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 大连商品交易所
 * @author ctm
 *
 */
public class DCEDataFetch {
	private static final String className = DCEDataFetch.class.getName();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private Log logger = LogFactory.getLog(DCEDataFetch.class);
	private String cronUrl = "http://www.dce.com.cn/dalianshangpin/sspz/ym/gjdt/index.html";
	private DAOUtils dao = new DAOUtils();
	private static String encoding = "utf-8";
	private static Map<String, String> cid_map = new HashMap<String, String>();
	
	static{
		cid_map.put("玉米-进口完税价", "http://www.dce.com.cn/dalianshangpin/sspz/ym/gjdt/index.html");
//		cid_map.put("棕榈油-国际豆棕FOB价差", "1261730308126");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_DCE_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("棕榈油玉米资讯、持仓排名、仓单日报", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到棕榈油玉米资讯、持仓排名、仓单日报在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到棕榈油玉米资讯、持仓排名、仓单日报在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				try{
					cronFetch(date);//抓取玉米进口完税价
				} catch(Exception e) {
					logger.error("抓取玉米进口完税价发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, "玉米", "进口完税价", "发生未知异常。");
				}
				try{
					fetchDayTransPositions(date);	
				} catch(Exception e) {
					logger.error("抓取日成交持仓排名发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "日成交持仓排名发生未知异常。");
				}
				try{
					fetchDatawhSheet(date);
				} catch(Exception e) {
					logger.error("抓取仓单日报发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, "仓单日报", "仓单日报发生未知异常。");
				}
				
			}else{
				logger.info("抓取棕榈油玉米资讯、持仓排名、仓单日报的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取棕榈油玉米资讯、持仓排名、仓单日报的定时器已关闭");
			}
		}
	}
	
	/**
	 * 获取玉米进口完税价
	 * */
	public void cronFetch(Date date){
		logger.info("==========开始抓取大连交易所的玉米进口完税价=============");
		String varName = "玉米";//品种名
		String cnName = "进口完税价";//中文名
		String listCont = dataFetchUtil.getCompleteContent(0, cronUrl, encoding, varName + cnName + "_list.txt");
		String curDateStr = DateTimeUtil.formatDate(date, DateTimeUtil.YYYY_MM_DD);
		String regex = "<li><span>" + curDateStr + "</span><a href=\"([^\"]*)\"[^>]*title=\"国际大豆、玉米升贴水报价及进口成本估算\">国际大豆、玉米升贴水报价及进口成本估算</a></li>";
		List<String> list = RegexUtil.getMatchStr(listCont, regex, new int[]{1});
		String detailUrl = "http://www.dce.com.cn" + list.get(0);//详情页链接
		
		//抓取详情页
		String detailCont = dataFetchUtil.getCompleteContent(0, detailUrl, encoding, varName + cnName + "_detail.txt");
		Document detailPage = Jsoup.parse(detailCont);//详情页
		Elements zoomDiv = detailPage.select("div[id=zoom]");//找到指定div(表格所在div)
		Elements table = zoomDiv.tagName("table");//找到指定表格
		//获取时间序列
		String timeRegex = "报告日期：.*(\\d{4}-\\d{1,2}-\\d{1,2})";
		List<String> timeList = RegexUtil.getMatchStr(table.html(), timeRegex, new int[]{1});
		String timeIntStr = null;
		if(timeList != null && !timeList.isEmpty() && timeList.get(0) != null){
			timeIntStr = timeList.get(0);
			if(timeIntStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")){
				String year = timeIntStr.substring(0, 4);
				String month = timeIntStr.substring(timeIntStr.indexOf('-') + 1, timeIntStr.lastIndexOf('-'));
				String day = timeIntStr.substring(timeIntStr.lastIndexOf('-') + 1);
				if(month.length() == 1){
					month = "0" + month;
				}
				if(day.length() == 1){
					day = "0" + day;
				}
				timeIntStr = year + month + day;
			} else {
				timeIntStr = null;
			}
		}
		//获取数据
		boolean isSaveSuc = false;//标识是否保存成功
		Elements trs = table.select("tr:has(td)");//"tr:has(td:not([colspan]))"
		Iterator<Element> trIter = trs.iterator();//遍历表格中所有行
		while(trIter.hasNext()){
			Element curTr = trIter.next();
			if(curTr.html().contains("美国玉米")){//定位到美国玉米那一行，标识作用，其后第三行为所需数据行
				trIter.next(); 
				trIter.next(); 
				curTr = trIter.next();//玉米进口完税价为：（取最近一个月份的进口成本整数）
				Elements tds = curTr.select("td[nowrap]");//滤掉最后那个看不见的td标签
				Element targetTd = tds.last();//所需数据列为过滤后的最后一列
				String targetStr = targetTd.html();
				String price = targetStr.replaceAll("<[^>]*/?>", "").replaceAll("\\s", "");
				if(price.matches("\\d+(.\\d+)?")){
					//保存数据
					if(timeIntStr != null){
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("全国", price);
						dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), dataMap);
						isSaveSuc = true;
					}
				}
			}
		}
		if(!isSaveSuc){
			RecordCrawlResult.recordFailData(className, varName, cnName, "未保存成功。");
		}
	}

	/**
	 * 仓单日报
	 */
	public void fetchDatawhSheet(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String cnName = "仓单日报";
		String url = "http://www.dce.com.cn/publicweb/quotesdata/wbillWeeklyQuotes.html";
		logger.info("=========开始抓取大连交易所：仓单日报"+timeInt+"============");
		Map<String, String> paramsDatawhSheet = new HashMap<String, String>(); 
		paramsDatawhSheet.put("wbillWeeklyQuotes.variety","all");
		paramsDatawhSheet.put("year", timeInt.substring(0, 4));
		paramsDatawhSheet.put("month", String.valueOf(Integer.parseInt(timeInt.substring(4, 6)) - 1));
		paramsDatawhSheet.put("day", String.valueOf(Integer.parseInt(timeInt.substring(6, 8))));
		paramsDatawhSheet.put("curdate", timeInt);
		logger.info("抓取所有品种的"+cnName);
		String content = dataFetchUtil.getCompleteContent(0, url, encoding, "大连商品交易所仓单日报.txt", paramsDatawhSheet);
		logger.info("开始处理"+cnName);
		Document document = Jsoup.parse(content);
		Elements table = document.select("div.dataArea > table");
		Elements trs = table.select("tr:contains(小计)");
		Set<String> codeHasDeal = new HashSet<String>();//已被处理过的品种
		for(Element tr: trs){
			String code = tr.child(0).text().replace("小计", "");//品种（与数据库中品种名不完全相同）
			if(MapInit.dce_code2varName_Map.containsKey(code)){
				Map<String, String> dataMap = new HashMap<String, String>();
				String yesRegistWR = tr.child(2).text().replaceAll("\\s|,", "");//昨日注册仓单量
//				String todayRegist = tr.child(3).text().replaceAll("\\s|,", "");//今日新注册量
//				String todayCancel = tr.child(4).text().replaceAll("\\s|,", "");//今日新注销量
				String todayRegistWR = tr.child(3).text().replaceAll("\\s|,", "");//今日注册仓单量
				String changeWR = tr.child(4).text().replaceAll("\\s|,", "");//仓单变动量
				dataMap.put("昨日注册仓单量", yesRegistWR);
//				dataMap.put("今日新注册量", todayRegist);
//				dataMap.put("今日新注销量", todayCancel);
				dataMap.put("今日注册仓单量", todayRegistWR);
				dataMap.put("仓单变动量", changeWR);
				String varName = MapInit.dce_code2varName_Map.get(code);//品种名
				if(!dataMap.isEmpty()){
					logger.info("开始保存"+varName+cnName);
					dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
					codeHasDeal.add(code);
				}
			}
		}
		for(String key: MapInit.dce_code2varName_Map.keySet()){
			if(!codeHasDeal.contains(key)){
				Map<String, String> dataMap = new HashMap<String, String>();
				String varName = MapInit.dce_code2varName_Map.get(key);//品种名
				logger.info(varName+cnName+"的仓单量未找到，赋值为0");
				RecordCrawlResult.recordFailData(className, varName, cnName, "仓单量为0");
				dataMap.put("昨日注册仓单量", "0");
				dataMap.put("今日新注册量", "0");
				dataMap.put("今日新注销量", "0");
				dataMap.put("今日注册仓单量", "0");
				dataMap.put("仓单变动量", "0");
				logger.info("开始保存"+varName+cnName);
				dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
			}
		}
	}
	
	/**
	 * 指定日期的日成交持仓排名
	 * @param date 指定的日期
	 */
	public void fetchDayTransPositions(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String contractUrl = "http://www.dce.com.cn/publicweb/quotesdata/memberDealPosiQuotes.html";//合约参数链接
		String fileUrl = "http://www.dce.com.cn/publicweb/quotesdata/exportMemberDealPosiQuotesData.html";//导出文件链接
		logger.info("=========开始抓取大连交易所：日成交持仓排名"+timeInt+"============");
		for(String varName:MapInit.dce_varName_Map.keySet()){
			String code = MapInit.dce_varName2Code_Map.get(varName);
			Map<String, String> paramsForContract = new HashMap<String, String>();
			paramsForContract.put("memberDealPosiQuotes.variety", code);//品种
			paramsForContract.put("memberDealPosiQuotes.trade_type", "");//类型： 0 期货； 1 期权。
			paramsForContract.put("contract.contract_id", "all");//合约
			paramsForContract.put("contract.variety_id", code);//合约所属品种
			paramsForContract.put("currDate", timeInt);//当前日期
			paramsForContract.put("year", timeInt.substring(0, 4));
			paramsForContract.put("month", String.valueOf(Integer.parseInt(timeInt.substring(4, 6)) - 1));
			paramsForContract.put("day", String.valueOf(Integer.parseInt(timeInt.substring(6, 8))));
			Map<String, String> paramsForFile = new HashMap<String, String>(paramsForContract);//下载文件时的post参数
			paramsForFile.put("exportFlag", "txt");//导出文件的格式
			logger.info("抓取"+varName+timeInt+"合约参数");
			Map<String, String> contractMap = new HashMap<String, String>();
			String contractsCont = dataFetchUtil.getCompleteContent(0, contractUrl, encoding, varName + timeInt + "合约参数", paramsForContract);
			String contractRegex = "(?<=javascript:setContract_id\\(')([a-z]+[0-9]+)";
			List<String> contractParamList = RegexUtil.getMatchStr(contractsCont, contractRegex);//合约参数列表
			if(contractParamList == null || contractParamList.isEmpty()){
				logger.error("没有抓取到"+varName + timeInt+"合约参数");
				RecordCrawlResult.recordFailData(className, varName, null, "没有抓取到" + timeInt + "合约参数");
				continue;
			}else{
				for(String contractParam:contractParamList){
					String cnName = Integer.parseInt(contractParam.substring(contractParam.length()-2,contractParam.length()))+"月合约主力持仓";
					if(contractMap.get(cnName) != null) continue;//对于大连豆一那种出现月份合约重复的情形选用日期年份较近的。（较近的排在前边）
					contractMap.put(cnName, contractParam);
				}
				contractMap.put("主力持仓", "all");
			}
			if(contractMap.size() == 0) {
				logger.error("抓取到的"+varName + timeInt+"合约参数为空");
				RecordCrawlResult.recordFailData(className, varName, null, "抓取到的" + timeInt + "合约参数为空");
				continue;
			}
			Map<String, String> cnNamesMap = MapInit.dce_varName_Map.get(varName);
			for(String cnName:cnNamesMap.keySet()){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error(e);
				}
				logger.info("抓取"+cnName);
				String contractId = contractMap.get(cnName);
				if(contractId == null){
					logger.info("没有获取到"+varName+cnName+"合约参数");
					RecordCrawlResult.recordFailData(className, varName, cnName, "没有获取到合约参数。");
					continue;
				}
				paramsForFile.put("contract.contract_id", contractId);//挑选指定合约
				String fileName = Constants.DATABAK_TXT + timeInt + Constants.FILE_SEPARATOR + timeInt + "_" + code + "_" + contractId + ".txt";
				MyHttpClient.fetchAndSave(fileUrl, fileName, paramsForFile);
				parseDayTransPositions(varName, cnName, timeInt, fileName);
			}
		}
	}
	
	/**
	 * 解析日成交持仓排名
	 * */
	public void parseDayTransPositions(String varName, String cnName, String timeInt, String fileName){
		boolean isSaveSuccess = false;//标识是否成功保存新数据
		String failInfo = null;//失败信息
		String content = null;
		try {
			content = FileStrIO.loadStringFromFile(fileName, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(content != null && !content.equals("")){
			logger.info("开始处理"+varName+cnName);
			String[] lines = content.split("\n");
			double buySum = 0, sellSum = 0;//持买单量前20名合计，持卖单量前20名合计
			int len = 20;
			for(int lineId = 0; lineId < lines.length; lineId++){
				if(lines[lineId].contains("会员简称")){
					if(lines[lineId].contains("持买单量")){
						for(int i = 1; i <= len; i++){
							if(lines[lineId+i].startsWith("总计")){
								break;
							}
							String[] fields = lines[lineId+i].split("\t+");
							if(fields.length == 4 && fields[2] != null){
								String price = fields[2].replace(",", "");
								if(price.matches("\\d+(.\\d+)?")){
									buySum += Double.parseDouble(price);
								}
							}
						}
					} else if(lines[lineId].contains("持卖单量")) {
						for(int i = 1; i <= len; i++){
							if(lines[lineId+i].startsWith("总计")){
								break;
							}
							String[] fields = lines[lineId+i].split("\t+");
							if(fields.length == 4 && fields[2] != null){
								String price = fields[2].replace(",", "");
								if(price.matches("\\d+(.\\d+)?")){
									sellSum += Double.parseDouble(price);
								}
							}
						}
					}
				}
			}
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("持买单量", buySum + "");
			dataMap.put("持卖单量", sellSum + "");
			dataMap.put("净多单量", buySum - sellSum + "");
			dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), dataMap);
			if(buySum == 0 || sellSum == 0){
				failInfo = "持买单量或者持卖单量为0.";
			} else {
				isSaveSuccess = true;
			}
		}else{
			logger.info("没有抓取到"+varName+cnName);
			failInfo = "没有解析到数据。";
		}
		if(!isSaveSuccess){
			RecordCrawlResult.recordFailData(className, varName, cnName, failInfo);
		}
		
	}
	
	public static void main(String[] args){
		DCEDataFetch fetch = new DCEDataFetch();
//		Date dateBefore = DateTimeUtil.parseDateTime("20170523", "yyyyMMdd");
//		fetch.start();
		
//		fetch.cronFetch(dateBefore);//抓取玉米进口完税价
//		fetch.fetchDayTransPositions(dateBefore);
//		fetch.fetchDatawhSheet(dateBefore);
		
		//补历史玉米进口完税价
		fetch.cronUrl = "http://www.dce.com.cn/dalianshangpin/sspz/ym/gjdt/12894-1.html";
		String[] dateArr = {"20170512","20170515","20170516","20170517","20170518","20170519","20170522","20170523","20170524"};
		for(String dateStr: dateArr){
			Date dateTar = DateTimeUtil.parseDateTime(dateStr, "yyyyMMdd");
			fetch.cronFetch(dateTar);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}