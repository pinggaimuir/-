package cn.futures.data.importor.crawler.usdaCrawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RegexUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 美国农业部数据
 * @author ctm
 *
 */
public class USDAOnlineData {
	private static final String className = USDAOnlineData.class.getName();
	private static final Log logger = LogFactory.getLog(USDAOnlineData.class);
	private static final String url = "https://apps.fas.usda.gov/PSDOnlineApi/api/query/RunQuery";//查询数据接口（浏览器访问为：https://apps.fas.usda.gov/psdonline/app/index.html#/app/advQuery）
	//USDA全球数据json参数基本格式,形如："{\"queryId\":0,\"commodityGroupCode\":null,\"commodities\":[\"0011000\"],\"attributes\":[25],\"countries\":[\"R00\", \"ALL\"],\"marketYears\":[2017,2016,2015,2014],\"chkCommoditySummary\":false,\"chkAttribSummary\":false,\"chkCountrySummary\":false,\"commoditySummaryText\":\"\",\"attribSummaryText\":\"\",\"countrySummaryText\":\"\",\"optionColumn\":\"year\",\"chkTopCountry\":false,\"topCountryCount\":\"\",\"chkfileFormat\":false,\"chkPrevMonth\":false,\"chkMonthChange\":false,\"chkCodes\":false,\"chkYearChange\":false,\"queryName\":\"\",\"sortOrder\":\"Commodity/Attribute/Country\",\"topCountryState\":false}";
	private static final String postJsonBase = "{\"queryId\":0,\"commodityGroupCode\":null,\"commodities\":[#commodities],\"attributes\":[#attributes],\"countries\":[#countries],\"marketYears\":[#marketYears],\"chkCommoditySummary\":false,\"chkAttribSummary\":false,\"chkCountrySummary\":false,\"commoditySummaryText\":\"\",\"attribSummaryText\":\"\",\"countrySummaryText\":\"\",\"optionColumn\":\"year\",\"chkTopCountry\":false,\"topCountryCount\":\"\",\"chkfileFormat\":false,\"chkPrevMonth\":false,\"chkMonthChange\":false,\"chkCodes\":false,\"chkYearChange\":false,\"queryName\":\"\",\"sortOrder\":\"Commodity/Attribute/Country\",\"topCountryState\":false}";
	private static final String weeklyExportUrl = "https://apps.fas.usda.gov/EsrQuery/esrq.aspx";
	private static final String ersUrl = "http://www.ers.usda.gov/datafiles/Commodity_Costs_and_Returns/Data/Current_Costs_and_Returns_All_commodities/";
	private static final String ersMilkUrl = "http://www.ers.usda.gov/datafiles/Milk_Cost_of_Production_Estimates/Milk_CostofProduction_Estimates2010_Base/Annual/";
	private static final String ersMonthlyUrl = "http://www.ers.usda.gov/datafiles/Milk_Cost_of_Production_Estimates/Milk_CostofProduction_Estimates2010_Base/Monthly/";
	private static final String ersForecastsUrl = "http://www.ers.usda.gov/datafiles/Commodity_Costs_and_Returns/Data/CostofProduction_Forecasts/";
	private static final Map<String, String> weeklyExportParams = new HashMap<String, String>();
	private static final Map<String, String> params = new HashMap<String, String>();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static DAOUtils dao = new DAOUtils();
	private static final Integer[] yearList = new Integer[3];
	private static final Integer[] yearListSugar = new Integer[3];//专用于全球食糖,主要是因为该网站食糖的数据跟其它有点儿不同，lstDate=2015时查到的是2014/2015,实际为2014年的数据；而对于其它比如稻米，lstDate=2015查的是2015/2016即2015年的数据。
	private static String yearStr = null;
	private static String yearStrSugar = null;
	
	private static List<String> varNameCalYield = new ArrayList<String>();
	private static List<String> cnNames = new ArrayList<String>();
	private static int yearNow = Integer.parseInt(DateTimeUtil.formatDate(new Date(), "yyyy"));
	static{
		yearList[0] = yearNow;
		yearList[1] = yearNow-1;
		yearList[2] = yearNow-2;
		yearStr = yearList[0] + "," + yearList[1] + "," + yearList[2];
		yearListSugar[0] = yearNow + 1;
		yearListSugar[1] = yearNow;
		yearListSugar[2] = yearNow - 1;
		yearStrSugar = yearListSugar[0] + "," + yearListSugar[1] + "," + yearListSugar[2];
		
		cnNames.add("周出口（分国别）");
		cnNames.add("年度累计出口");
		cnNames.add("本年度未执行");
		cnNames.add("本年度销售");
		cnNames.add("本年度净销售");
		cnNames.add("总装船量");
		cnNames.add("下年度未执行合同");
		cnNames.add("下年度净销售");
		
		varNameCalYield.add("全球花生");
		varNameCalYield.add("全球油菜籽");
		varNameCalYield.add("全球大豆");
		varNameCalYield.add("全球葵花籽");
		params.put("lstAttribute", "4");
		params.put("lstColumn", "Year");
		params.put("lstCommodity", "0422110");
		params.put("lstCountry", "**,99");
		params.put("lstDate", yearNow+"");
		params.put("visited", "1");
		params.put("lstGroup", "all");
		params.put("lstOrder", "Commodity/Attribute/Country");
		params.put("hidColumnIndex", "0");
		params.put("hidQueryID", "");
		params.put("hidLoadQuery", "");
		params.put("hidOrderIndex", "0");
		params.put("hidQuerySave", "");
		params.put("hidQuerySaveMode", "0");
		params.put("hidLoadCommodities", "");
		params.put("hidLoadAttributes", "");
		params.put("hidSplitYear", "0577400^1,0011000^0,0013000^0,0574000^1,0430000^1,0711100^2,0440000^1,2631000^1,0230000^0,0240000^0,0224400^0,0223000^0,0224200^0,0577500^1,0579305^1,0579309^1,0572220^1,0575100^1,0572120^1,0813700^1,0813300^1,0814200^1,0813800^1,0813200^1,0813600^1,0813100^1,0813101^1,0813500^1,0111000^0,0113000^0,0459100^1,0459900^1,0452000^1,4242000^1,4233000^1,4235000^1,4243000^1,4244000^1,4234000^1,4239100^1,4232000^1,4232001^1,4236000^1,2231000^1,2223000^1,2232000^1,2221000^1,2226000^1,2222000^1,2222001^1,2224000^1,0585100^1,0571120^1,0579220^1,0577907^1,0114200^0,0114300^0,0575200^1,0422110^1,0451000^1,0459200^1,0612000^2,0571220^1,0577901^1,0410000^1");
		params.put("hidShowFooter", "0");
		params.put("hidShowRiceFooter", "0");
		params.put("hidShowOilSoybeanFooter", "0");
		
		weeklyExportParams.put("__VIEWSTATEGENERATOR", "41AA5B91");
		weeklyExportParams.put("ctl00$MainContent$btnSubmit", "Submit");
		weeklyExportParams.put("ctl00$MainContent$ddlReportFormat", "10");
		weeklyExportParams.put("ctl00$MainContent$lbCommodity", "401");
		weeklyExportParams.put("ctl00$MainContent$lbCountry", "0:0");
		weeklyExportParams.put("ctl00$MainContent$rblColumnSelection", "regular");
		weeklyExportParams.put("ctl00$MainContent$rblOutputType", "2");//0，浏览器页面显示；1，pdf;2，excel
		weeklyExportParams.put("ctl00$MainContent$tbStartDate", "01/01/2017");
		weeklyExportParams.put("ctl00$MainContent$tbEndDate", "01/12/2017");
		weeklyExportParams.put("ctl00$MainContent$ibtnStart", "01/01/2017");
		weeklyExportParams.put("ctl00$MainContent$ibtnEnd", "01/12/2017");
		weeklyExportParams.put("__EVENTARGUMENT", "");
		weeklyExportParams.put("__EVENTTARGET", "");
		weeklyExportParams.put("__LASTFOCUS", "");
	}

	/**
	 * 抓周出口全球、周出口中国数据
	 * @param date
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_USDA_WEEKLY_EXPORT)
	public void start1(){
		String switchFlag = new CrawlerManager().selectCrawler("美农周出口数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到美农周出口数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date now = new Date();
				fetchWeeklyExportDatas(now);
				fetchWeeklyExportToOtherCountry(now);
			}else{
				logger.info("抓取美农周出口数据的定时器已关闭");
			}
		}
	}
	
	/**
	 * 抓取周出口数据
	 * */
	private void fetchWeeklyExportDatas(Date now){
		logger.info("========开始抓取周出口数据========");
		Map<String, Map<String, String>> time2exportWeeklyWorld = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> time2exportWeeklyChina = new HashMap<String, Map<String, String>>();
		//取上周四的日期
		String date = DateTimeUtil.formatDate(DateTimeUtil.addDay(DateTimeUtil.getLastWeekEndDay(now),-3), "yyyyMMdd");
		String fetchDate = DateTimeUtil.formatDate(DateTimeUtil.addDay(DateTimeUtil.getLastWeekEndDay(now),-3), "MM/dd/yyyy");
		weeklyExportParams.put("ctl00$MainContent$tbStartDate", fetchDate);
		weeklyExportParams.put("ctl00$MainContent$tbEndDate", fetchDate);
		weeklyExportParams.put("ctl00$MainContent$ibtnStart", fetchDate);
		weeklyExportParams.put("ctl00$MainContent$ibtnEnd", fetchDate);
		for(String varName:MapInit.usda_exportWeekly_varName2code.keySet()){
			String varCodes = MapInit.usda_exportWeekly_varName2code.get(varName);
			String cnNamePre = "";
			if(varName.equals("美国小麦制品")){
				varName = "美国小麦";
				cnNamePre = "美国小麦制品";
			}
			int varId = Variety.getVaridByName(varName);
			logger.info("开始抓取"+date+varName+"("+varId+")数据");
			List<String> varCodeList = new ArrayList<String>();
			if(varCodes.indexOf(",") != -1){
				varCodeList = Arrays.asList(varCodes.split(","));
			}else{
				varCodeList.add(varCodes);
			}
			time2exportWeeklyWorld.clear();
			time2exportWeeklyChina.clear();
			for(String varCode:varCodeList){
				weeklyExportParams.put("ctl00$MainContent$lbCommodity", varCode);
				String timeInt = date;//数据的时间序列
				String[][] table = getWeeklyExportTable(varName, varCode, date);
				int colId = 4;//第4列为国家名称列
				Map<String, String> worldTmpMap = new HashMap<String, String>();//周出口全球数据
				Map<String, String> chinaTmpMap = new HashMap<String, String>();//周出口中国数据
				for(int rowId = 0; rowId < table.length; rowId++){
					if("GRAND TOTAL".equals(table[rowId][colId])){//周出口全球数据
						for(int j=0;j<8;j++){
							worldTmpMap.put(MapInit.usda_exportWeekly_header.get(j+""), table[rowId][colId+j+1]);
						}
					} else if("CHINA, PEOPLES REPUBLIC OF".equals(table[rowId][colId])) {//周出口中国数据
						for(int j=0;j<8;j++){
							chinaTmpMap.put(MapInit.usda_exportWeekly_header.get(j+""), table[rowId][colId+j+1]);
						}
					}
				}
				if(worldTmpMap.size() > 0){
					if(time2exportWeeklyWorld.get(timeInt) != null){
						Map<String, String> tmpWorld = time2exportWeeklyWorld.get(timeInt);
						for(String header:tmpWorld.keySet()){
							tmpWorld.put(header, Double.parseDouble(tmpWorld.get(header))+Double.parseDouble(worldTmpMap.get(header))+"");
						}
						time2exportWeeklyWorld.put(timeInt, tmpWorld);
					}else{
						time2exportWeeklyWorld.put(timeInt, worldTmpMap);
					}
				}else{
					logger.info(timeInt+cnNamePre+"周出口全球数据不存在");
				}
				if(chinaTmpMap.size() > 0){
					if(time2exportWeeklyChina.get(timeInt) != null){
						Map<String, String> tmpChina = time2exportWeeklyChina.get(timeInt);
						for(String header:tmpChina.keySet()){
							tmpChina.put(header, Double.parseDouble(tmpChina.get(header))+Double.parseDouble(chinaTmpMap.get(header))+"");
						}
						time2exportWeeklyChina.put(timeInt, tmpChina);
					}else{
						time2exportWeeklyChina.put(timeInt, chinaTmpMap);
					}
				}else{
					logger.info(cnNamePre+"周出口中国"+timeInt+"数据不存在");
				}
				logger.info(varName+timeInt+"分析完毕");
			}
			for(String timeInt:time2exportWeeklyWorld.keySet()){
				logger.info("保存"+varName+"("+varId+")"+cnNamePre+"周出口全球"+timeInt+"数据");
				dao.saveOrUpdateByDataMap(varId, cnNamePre+"周出口全球", Integer.parseInt(timeInt), time2exportWeeklyWorld.get(timeInt));
			}
			for(String timeInt:time2exportWeeklyChina.keySet()){
				logger.info("保存"+varName+"("+varId+")"+cnNamePre+"周出口中国"+timeInt+"数据");
				dao.saveOrUpdateByDataMap(varId, cnNamePre+"周出口中国", Integer.parseInt(timeInt), time2exportWeeklyChina.get(timeInt));
			}
		}
	}
	
	/**
	 * 获取周出口数据
	 * @author bric_yangyulin
	 * @date 2017-02-07
	 * */
	public String[][] getWeeklyExportTable(String varName, String varCode, String date){
		String[][] table = null;
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		String content = dataFetchUtil.getCompleteContent(2, weeklyExportUrl, Constants.ENCODE_UTF8, null);
		String viewRegex = "id=\"__VIEWSTATE\" value=\"([^\"]*)\"";
		String view = RegexUtil.getMatchStr(content, viewRegex, new int[]{1}).get(0);
		String eventRegex = "id=\"__EVENTVALIDATION\" value=\"([^\"]*)\"";
		String event = RegexUtil.getMatchStr(content, eventRegex, new int[]{1}).get(0);
		weeklyExportParams.put("__VIEWSTATE", view);
		weeklyExportParams.put("__EVENTVALIDATION", event);
		String fileDir = Constants.DATABAK_TXT + "USDA周出口数据" + Constants.FILE_SEPARATOR + varName + varCode + date + ".xls";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		MyHttpClient.fetchAndSaveHttps(weeklyExportUrl, fileDir, weeklyExportParams);
		table = FileStrIO.readXls(fileDir, "Sheet1", 15);
		return table;
	}
	
	/**
	 * 主要国家：
	 * 周出口量
	 * 年度累计出口
	 * 本年度未执行合同
	 * 本年度销售
	 * 本年度净销售
	 * 总装船量
	 * 下年度未执行合同
	 * 下年度净销售
	 */
	private void fetchWeeklyExportToOtherCountry(Date date){
		logger.info("========开始抓取主要国家：周出口、年度累计出口、本年度未执行合同、本年度销售、本年度净销售、总装船量、下年度未执行合同、下年度净销售数据========");
		Map<String, String> tmpMap = new HashMap<String, String>();
		String timeIntStr = DateTimeUtil.formatDate(DateTimeUtil.addDay(DateTimeUtil.getLastWeekEndDay(date),-3), "yyyyMMdd");
		for(String varName:MapInit.usda_exportWeekly_varName2code.keySet()){
			String varCodes = MapInit.usda_exportWeekly_varName2code.get(varName);
			logger.info("开始抓取"+timeIntStr+varName+"数据");
			List<String> varCodeList = new ArrayList<String>();
			if(varCodes.indexOf(",") != -1){
				varCodeList = Arrays.asList(varCodes.split(","));
			}else{
				varCodeList.add(varCodes);
			}
			for(String varCode:varCodeList){
				//读取文件中的周出口数据（fetchWeeklyExportDatas（）方法中下载的）
				String fileDir = null;
				if(varName.equals("美国小麦制品")){
					fileDir = Constants.DATABAK_TXT + "USDA周出口数据" + Constants.FILE_SEPARATOR + "美国小麦" + varCode + timeIntStr + ".xls";
				} else {
					fileDir = Constants.DATABAK_TXT + "USDA周出口数据" + Constants.FILE_SEPARATOR + varName + varCode + timeIntStr + ".xls";
				}
				String[][] table = FileStrIO.readXls(fileDir, "Sheet1", 15);
				int colId = 4;//第4列为国家名称列
				String varNameCopy = varName;
				for(int cnNameIndex=0;cnNameIndex<cnNames.size();cnNameIndex++){
					String cnNameTmp = cnNames.get(cnNameIndex);
					String cnName = cnNameTmp;
					if(varNameCopy.equals("美国小麦制品")){
						if(cnNameTmp.equals("周出口（分国别）")){
							cnName = "美国小麦制品" + cnNameTmp;
						}else{
							cnName = cnNameTmp + "（小麦制品）";
						}
						varName = "美国小麦";
					}
					if(varCode.equals("1404")){//美国棉花（特殊）
						if(cnNameTmp.equals("周出口（分国别）")){
							cnName = "陆地棉"+cnNameTmp;
						}else{
							cnName = cnNameTmp + "（陆地棉）";
						}
					}else if(varCode.equals("1301")){//美国棉花（特殊）
						if(cnNameTmp.equals("周出口（分国别）")){
							cnName = "皮马棉"+cnNameTmp;
						}else{
							cnName = cnNameTmp + "（皮马棉）";
						}
					}
					int varId = Variety.getVaridByName(varName);
					logger.info("开始分析"+timeIntStr+cnName+"数据");
					tmpMap.clear();
					for(int rowId = 0; rowId < table.length; rowId++){
						if(MapInit.usda_exportWeekly_countryHeader.containsKey(table[rowId][colId])){
							tmpMap.put(MapInit.usda_exportWeekly_countryHeader.get(table[rowId][colId]), table[rowId][colId+cnNameIndex+1]);
						}
					}
					logger.info(varName+timeIntStr+"分析完毕");
					if(tmpMap.size() > 0){
						logger.info("保存"+varName+"("+varId+")"+cnName+timeIntStr+"数据");
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeIntStr), tmpMap);
					}else{
						logger.info(varName+"("+varId+")"+cnName+timeIntStr+"没有需要保存的数据");
					}
				}
			}
		}
	}
	
	/**
	 * 抓全球数据，如全球生猪、全球玉米等
	 * @param date
	 */
	private void fetchAllCountryDatas(){
		logger.info("=========开始抓取全球数据============");
//		params.put("lstCountry", "**,99");//"**"对应"ALL COUNTRIES"	"99"对应"WORLD TOTAL"
		logger.info("抓取全球牛的肉牛期初存栏");
		String varNameCattle = "全球牛";
		String cnNameCattle = "肉牛期初存栏";
		String now = DateTimeUtil.formatDate(new Date(), DateTimeUtil.YYYYMMDD);
		String fileDir = null;
//		String postJson = null;/*
		String postJson = postJsonBase.replace("#commodities", "\"0011000\"");//Animal Numbers, Cattle 全球牛
		postJson = postJson.replace("#attributes", "25");//Beef Cows Beg. Stocks 肉牛期初存栏
		postJson = postJson.replace("#countries", "\"R00\", \"ALL\"");//"ROO" 为 "World Total", "ALL" 为 "All Countries"
		postJson = postJson.replace("#marketYears", yearStr);
		String contents = dataFetchUtil.httpsPostJson(url, postJson, "application/json;charset=utf-8", Constants.ENCODE_UTF8, varNameCattle + cnNameCattle);
		fileDir = Constants.DATABAK_TXT + "USDA周出口数据" + Constants.FILE_SEPARATOR + varNameCattle + cnNameCattle + now + ".txt";
		try {
			FileStrIO.saveStringToFile2(contents, fileDir, Constants.ENCODE_UTF8);//保存返回结果
		} catch (IOException e) {
			logger.error(e);
		}
		//解析结果
		if(contents != null && !contents.equals("")){
			JSONObject rsltJson = JSONObject.fromObject(contents);
			Map<String, String> dataMap = new HashMap<String, String>();
			for(int year: yearList){
				for(String courtryCh : MapInit.usda_world_cattle_header_map.values()){
					dataMap.put(courtryCh, "0");//保证没有数据的补0
				}
				JSONArray queryRslt = rsltJson.getJSONArray("queryResult");//查询结果
				for(int i = 0; i < queryRslt.size(); i++){
					JSONObject oneCountry = queryRslt.getJSONObject(i);
					String country = oneCountry.getString("country");
					String price = oneCountry.get(String.valueOf(year)).toString();
					if(!"null".equals(price) && MapInit.usda_world_cattle_header_map.containsKey(country)) {
						dataMap.put(MapInit.usda_world_cattle_header_map.get(country), price);
					}
				}
				dao.saveOrUpdateByDataMap(varNameCattle, cnNameCattle, year, dataMap);//保存数据
				dataMap.clear();
			}
		}else{
			logger.error("没有抓取到"+varNameCattle+"("+ cnNameCattle +")数据");
		}
//		*/
		for(String varName:MapInit.allCountry_map.keySet()){
			Integer[] tempYearList = null;
			if(varName.equals("全球食糖")){
				tempYearList = yearListSugar;
			} else {
				tempYearList = yearList;
			}
			Map<String, String> cnNameMap = MapInit.allCountry_map.get(varName);
			for(String cnName:cnNameMap.keySet()){
				postJson = postJsonBase.replace("#commodities", "\"" + MapInit.usda_commodity_map.get(MapInit.varName2commodity_map.get(varName)) + "\"")
				.replace("#attributes", cnNameMap.get(cnName)).replace("#countries", "\"R00\", \"ALL\"").replace("#marketYears", yearStr);
				logger.info("开始抓取" + varName + cnName + "数据");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error(e);
				}
				String contents2 = dataFetchUtil.httpsPostJson(url, postJson, "application/json;charset=utf-8", Constants.ENCODE_UTF8, varName + cnName);
				fileDir = Constants.DATABAK_TXT + "USDA周出口数据" + Constants.FILE_SEPARATOR + varName + cnName + now + ".txt";
				try {
					FileStrIO.saveStringToFile2(contents2,  fileDir, Constants.ENCODE_UTF8);//保存返回结果
				} catch (IOException e) {
					logger.error(e);
				}
				JSONObject rsltJson = JSONObject.fromObject(contents2);
				Map<String, String> dataMap = new HashMap<String, String>();
				for(int year: tempYearList){
					for(String courtryCh : MapInit.usda_country2ch_map.values()){
						dataMap.put(courtryCh, "0");
					}
					JSONArray queryRslt = rsltJson.getJSONArray("queryResult");//查询结果
					for(int i = 0; i < queryRslt.size(); i++){
						JSONObject oneCountry = queryRslt.getJSONObject(i);
						String country = oneCountry.getString("country");
						Object priceObj = oneCountry.get(year+"/"+(year+1));
						if(priceObj != null){
							String price = oneCountry.get(year+"/"+(year+1)).toString();
							if(!"null".equals(price) && MapInit.usda_country2ch_map.containsKey(country)) {
								dataMap.put(MapInit.usda_country2ch_map.get(country), price);
							}
						} else if(null!=oneCountry.get(year+"")){
                            String price = oneCountry.get(year+"").toString();
                            if(!"null".equals(price) && MapInit.usda_country2ch_map.containsKey(country)) {
                                dataMap.put(MapInit.usda_country2ch_map.get(country), price);
                            }
						}else{
                            logger.info("未找到数据：" + country + year);
                        }
					}
					dao.saveOrUpdateByDataMap(varName, cnName, year, dataMap);//保存数据
					dataMap.clear();
				}
			}
		}
		//需要通过计算得到的单产（全球花生、全球油菜籽、全球大豆、全球葵花籽）
		for(int year:yearList){
			for(String varName:varNameCalYield){
				int varId = Variety.getVaridByName(varName);
				logger.info("开始计算"+varName+ year+"年"+"单产");
				List<Double> sumPro = dao.getUsdaRecByTimeInt(varId, "总产量", year+"");
				List<Double> area = dao.getUsdaRecByTimeInt(varId, "播种面积", year+"");
				List<String> yield = new ArrayList<String>();
				if(sumPro.size() > 0 && area.size() > 0){
					for(int i=0;i<sumPro.size();i++){
						if(area.get(i)==0){
							yield.add("0");
						}else{
							yield.add(sumPro.get(i)/area.get(i)+"");
						}
					}
				}
				if(yield.size() > 0){
					Map<String, String> dataMap = new HashMap<String, String>();
					int i =0 ;
					List<String> countryList = new ArrayList<String>();
					countryList.addAll(MapInit.usda_country2ch_map.values());
					for(String country:countryList){
						dataMap.put(country, yield.get(i++));
					}
					logger.info("开始保存"+varName + year+"年"+"单产");
					dao.saveOrUpdateByDataMap(varName, "单产", year, dataMap);//表CX_PerOutput_international varid:89\87\83\51
				}else{
					logger.info(varName + year+"年"+"单产"+"没有数据保存");
				}
			}
		}
	}
	
	/**
	 * 抓取平衡表数据
	 */
	public void fetchBalanceTable(){
		logger.info("=========开始抓取平衡表数据============");
		String fileDir = null;
		String now = DateTimeUtil.formatDate(new Date(), DateTimeUtil.YYYYMMDD);
		String tempYearStr = yearStr;
		Integer[] tempYearList = yearList;
		String postJson = null;
		for(String varNameStr:MapInit.balanceTableMap.keySet()){
			String[] varNames = varNameStr.split(",");//全球，玉米，平衡表
			String varName = varNames[0] + varNames[1];
			if(varName.equals("中国葵花籽")||varName.equals("中国葵花籽油")||varName.equals("中国葵花籽粕")){
				varName = varName.substring(2);
			}
			if(varNames[1].equals("食糖")){
				tempYearStr = yearStrSugar;//由于对于食糖数据，年份前置了一年，所以需单独处理其year参数
				tempYearList = yearListSugar;
			}
			String cnName = varNames[2];
			Map<String, String> headerMap = MapInit.balanceTableMap.get(varNameStr);
			String cnCodes = "";
			Map<String, String> cnNameMap = new HashMap<String, String>();//中文名-网站中属性名映射
			Map<String, String> needCalMap = new HashMap<String, String>();//需要计算的内容
			for(String cnNames : headerMap.keySet()){
				String cnCode = headerMap.get(cnNames);
				if(!cnCode.startsWith("calc")){
					cnCodes += "," + cnCode;
					String[] tmp = cnNames.split("-");
					cnNameMap.put(tmp[0], tmp[1]);
				}else{
					needCalMap.put(cnNames, headerMap.get(cnNames));
				}
			}
			postJson = postJsonBase.replace("#commodities", "\"" + MapInit.usda_commodity_map.get(varNames[1]) + "\"")
					.replace("#attributes", cnCodes.substring(1))
					.replace("#countries", "\"" + MapInit.usda_country2code_map.get(varNames[0]) + "\"")
					.replace("#marketYears", tempYearStr);
			logger.info("开始抓取"+varName+cnName+"数据");
			logger.info(String.format("%s-%s", MapInit.usda_commodity_map.get(varNames[1]), MapInit.usda_country2code_map.get(varNames[0])));
//			/*
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				logger.error(e1);
			}
			String contents = dataFetchUtil.httpsPostJson(url, postJson, "application/json;charset=utf-8", Constants.ENCODE_UTF8, varName + cnName);
			if(contents != null){
				fileDir = Constants.DATABAK_TXT + "USDA周出口数据" + Constants.FILE_SEPARATOR + varName + cnName + now + ".txt";
				try {
					FileStrIO.saveStringToFile2(contents,  fileDir, Constants.ENCODE_UTF8);//保存返回结果
				} catch (IOException e) {
					logger.error(e);
				}
				logger.info("开始分析");
				JSONObject rsltJson = JSONObject.fromObject(contents);
				JSONArray queryRslt = rsltJson.getJSONArray("queryResult");//查询结果
				for(int year: tempYearList){
					Map<String, String> dataMap = new HashMap<String, String>();
					Map<String, String> tmpMap = new HashMap<String, String>();
					for(int i = 0; i < queryRslt.size(); i++){
						JSONObject oneCountry = queryRslt.getJSONObject(i);
						String attribute = oneCountry.getString("attribute");
						
						Object priceObj = oneCountry.get(year + "/" + (year + 1));
						if(priceObj != null){
							String price = oneCountry.get(year + "/" + (year + 1)).toString();
							if(!"null".equals(price)) {
								tmpMap.put(attribute, price);//属性-对应年份数值
							}
						} else {
							logger.info("未找到数据：" + attribute + year);
						}
					}
					for(String cnNameTmp:cnNameMap.keySet()){//各属性赋值
						String value = tmpMap.get(cnNameMap.get(cnNameTmp));
						if(value != null){
							dataMap.put(cnNameTmp, value);
						}else{
							dataMap.put(cnNameTmp, "0");
						}
					}
					boolean needSave = false;
					for(String tmp:dataMap.values()){//判断是否有数据，只要有一条数据即需要保存。
						if(!tmp.equals("0")){
							needSave = true;
							break;
						}
					}
					if(!needSave){
						logger.info(varName+varNames[2]+ year+"年没有数据需要保存");
						continue;
					}
					if(needCalMap.size() == 1){//有且仅有一个数据（总消费）需要计算得来
						for(String cnNameTmp:needCalMap.keySet()){
							logger.info("开始计算"+cnNameTmp);
							String[] tmp= needCalMap.get(cnNameTmp).split(",");
							double sum = 0;
							for(int i=1;i<tmp.length;i++){
								sum += Double.parseDouble(dataMap.get(tmp[i]));
							}
							dataMap.put(cnNameTmp, sum+"");
						}
					}
					logger.info("开始计算库存消费比");
					if(dataMap.get("期末库存") != null && (dataMap.get("总消费") != null||dataMap.get("国内总消费") != null)){
						double sumCons = 0;
						if(dataMap.get("总消费") != null){
							sumCons = Double.parseDouble(dataMap.get("总消费"));
						}else{
							sumCons = Double.parseDouble(dataMap.get("国内总消费"));
						}
						
						if(sumCons > 0){
							dataMap.put("库存消费比", Double.parseDouble(dataMap.get("期末库存"))/sumCons*100+"");
						}else{
							dataMap.put("库存消费比","0");
						}
					}else{
						dataMap.put("库存消费比", "0");
					}
					dao.saveOrUpdateByDataMap(varName, cnName, year, dataMap);
					//如果是大豆顺便计算单产并更新单产表
					if(varName.equals("美国大豆")||varName.equals("阿根廷大豆")||varName.equals("巴西大豆")){
						String product = dataMap.get("产量");
						String area = dataMap.get("收获面积");
						double yield = 0;
						if(product != null && area != null){
							yield = Double.parseDouble(product)/Double.parseDouble(area);
						}
						Map<String, String> dataYieldMap = new HashMap<String, String>();
						dataYieldMap.put("全国", yield+"");
						dao.saveOrUpdateByDataMap(varName, "单产", year, dataYieldMap);
					}
				}
			} else {
				logger.info(varName + "-" + cnName + "未抓到数据。");
			}
//			*/
		}
	}
	
	/**
	 * 更新预测数据，如播种面积预测、产量预测、单产预测
	 */
	public void updatePredictDatas(){
		String dataStr = DateTimeUtil.formatDate(new Date(), "yyyyMMdd");
		int monthInt = Integer.parseInt(dataStr.substring(4,6));
		String year = "";
		String month = monthInt +"";
		logger.info("==========开始更新预测数据==========");//玉米从9月开始取下一年度，棉花是从8月开始取下一年度数据
		for(String varNameTmp:MapInit.usda_predict_map.keySet()){
			String[] tmp = varNameTmp.split("-");
			String varName = tmp[0];
			if(varName.indexOf("玉米")!=-1){
				if(monthInt < 9){
					year = Integer.parseInt(dataStr.substring(0,4))-1+"";
				}else{
					year = dataStr.substring(0,4);
				}
			}else if(varName.indexOf("棉花")!=-1){
				if(monthInt < 8){
					year = Integer.parseInt(dataStr.substring(0,4))-1+"";
				}else{
					year = dataStr.substring(0,4);
				}
			}
			int varId = Variety.getVaridByName(varName);
			String cnNameFrom = tmp[1];
			Map<String, String> preCnNameMap = MapInit.usda_predict_map.get(varNameTmp);
			logger.info("取"+varName+"("+varId+")的预测数据");
			List<String> fieldNames = new ArrayList<String>();
			List<String> cnNameList = new ArrayList<String>();
			boolean needUpYield = false;
			for(String preCnName:preCnNameMap.keySet()){
				if(!preCnName.equals("USDA单产预测")){
					fieldNames.add(preCnNameMap.get(preCnName));
					cnNameList.add(preCnName);
				}else{
					needUpYield = true;
				}
			}
			List<String> values = dao.getListValues(varId, cnNameFrom, year, fieldNames);
			if(needUpYield){
				String yieldFrom = preCnNameMap.get("USDA单产预测");
				cnNameList.add("USDA单产预测");
				String[] tmp1 = yieldFrom.split(",");
				values.add(dao.getSingleValue(Variety.getVaridByName(tmp1[0]), tmp1[1], year, tmp1[2]));
			}
			logger.info("更新"+varName+"("+year+"-"+month+")");
			for(int i=0;i<cnNameList.size();i++){
				String cnNameTo = cnNameList.get(i);
				Map<String, String> dataMap = new HashMap<String, String>();
				if(!cnNameTo.equals("USDA单产预测")){
					dataMap.put(month+"月", Double.parseDouble(values.get(i))/1000+"");
				}else{
					dataMap.put(month+"月", values.get(i));
				}
				dao.saveOrUpdateByDataMap(varId, cnNameTo, Integer.parseInt(year), dataMap);
			}
		}
	}
	
	/**
	 * 更新全球植物油与全球油料（前提是全球数据已经更新完）
	 */
	public void updateOilBearAndVege(){
		logger.info("==========开始更更新全球植物油与全球油料==========");
		String years = "";
		for(int year:yearList){
			years += ","+year;
		}
		logger.info("更新年份有"+years.substring(1));
		for(int year:yearList){
			for(String varName:MapInit.usda_oilBear_vege_cnName_map.keySet()){
				int varId = Variety.getVaridByName(varName);
				String[] cnNames = MapInit.usda_oilBear_vege_cnName_map.get(varName);
				for(String cnName:cnNames){
					String[] headers = MapInit.usda_oilBear_vege_header_map.get(varName);
					Map<String, String> dataMap = new HashMap<String, String>();
					double sum = 0;
					logger.info("更新"+year+varName+"-"+cnName+"数据");
					for(String header:headers){
						String varNameFrom = "全球"+(header.equals("大豆油")?"豆油":header);
						String cnNameFrom = cnName.equals("收获面积")?"播种面积":cnName;
						if((varNameFrom+cnNameFrom).equals("全球椰子仁播种面积")||(varNameFrom+cnNameFrom).equals("全球棕榈仁播种面积")){
							continue;
						}
						if((varNameFrom+cnNameFrom).equals("全球棉籽播种面积")){
							cnNameFrom = "收获面积";
						}
						if(MapInit.usda_oilBear_vege_special_map.keySet().contains(varName+cnName+header)){
							String tmpFrom = MapInit.usda_oilBear_vege_special_map.get(varName+cnName+header);
							String[] tmps = tmpFrom.split("-");
							varNameFrom = tmps[0];
							cnNameFrom = tmps[1];
						}
						int varIdFrom = Variety.getVaridByName(varNameFrom);
						logger.info("取"+header+cnNameFrom);
						String value = dao.getSingleValue(varIdFrom, cnNameFrom, year+"", "全球");
						if(!value.equals("")){
							sum += Double.parseDouble(value);
							dataMap.put(header, value);
						}else{
							dataMap.put(header, "0");
						}
					}
					if(sum > 0){
						dataMap.put("总计", sum+"");
						logger.info("开始保存"+varName+"-"+cnName);
						dao.saveOrUpdateByDataMap(varId, cnName, year, dataMap);
					}else{
						logger.info(year+varName+"-"+cnName+"没有数据更新");
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void fetchOtherUsda(){
		String years = "";
		for(int year:yearList){
			years += ","+year;
		}
		logger.info("抓取的年份有"+years.substring(1));
		params.put("lstDate", years.substring(1));
		String[] filters = {"table","id","gridReport"};
		String[] rowColChoose = {"", "0"};
		for(String varNames:MapInit.usda_other_map.keySet()){
			String[] arrs = varNames.split(",");//生猪,美国,美国养殖
			params.put("lstCommodity",MapInit.usda_commodity_map.get(arrs[0]));
			if(arrs[1].equals("欧洲")){
				params.put("lstCountry", MapInit.usda_country2code_map.get("欧盟"));
			}else{
				params.put("lstCountry", MapInit.usda_country2code_map.get(arrs[1]));
			}
			String varName = arrs[2];
			int varId = Variety.getVaridByName(varName);
			Map<String, String> cnNameMap = MapInit.usda_other_map.get(varNames);
			for(String cnName:cnNameMap.keySet()){
				params.put("lstAttribute", cnNameMap.get(cnName));
				logger.info("开始抓取"+varName+"("+varId+")"+cnName+"数据");
				String contents = dataFetchUtil.getPostPrimaryContent(0, url, "utf-8", varName, filters, rowColChoose, 0, params);
				if(contents != null && !contents.equals("")){
					logger.info("开始分析");
					String[] lines = contents.split("\n");
					String[] titles = lines[0].split(",");
					String[] cons = lines[1].split(",");
					for(int yearIndex=2;yearIndex<titles.length;yearIndex++){
						String year = titles[yearIndex].substring(0,4);
						Map<String, String> dataMap = new HashMap<String, String>();
						if(!cons[yearIndex].equals("0")){
							if(varName.equals("欧洲养殖") || varName.equals("欧盟玉米")){//欧盟玉米也是“全联盟”
								dataMap.put("全联盟", cons[yearIndex]);
							}else{
								dataMap.put("全国", cons[yearIndex]);
							}
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(year), dataMap);
						}else{
							logger.info(varName+cnName+ year+"年没有数据需要保存");
						}
					}
				}
			}
		}
	}
	
	public List<String> SearchDate(int year){//日期查询
		List<String> dateList = new ArrayList<String>();
		 Calendar calendar = new GregorianCalendar();//定义一个日历，变量作为年初
		 Calendar calendarEnd = new GregorianCalendar();//定义一个日历，变量作为年末
		 calendar.set(Calendar.YEAR, year);
		 calendar.set(Calendar.MONTH, 0);
		 calendar.set(Calendar.DAY_OF_MONTH, 1);//设置年初的日期为1月1日
		 calendarEnd.set(Calendar.YEAR, year);
		 calendarEnd.set(Calendar.MONTH, 11);
		 calendarEnd.set(Calendar.DAY_OF_MONTH, 31);//设置年末的日期为12月31日
		 SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
		 while(calendar.getTime().getTime()<=calendarEnd.getTime().getTime()){//用一整年的日期循环
		  if(calendar.get(Calendar.DAY_OF_WEEK)==5){//判断如果为星期四时，打印
			  dateList.add(sf.format(calendar.getTime()));
		  }
		  calendar.add(Calendar.DAY_OF_MONTH, 1);//日期+1
		 }
		 return dateList;
	}
	
	/**
	 * 美国农业部成本收益数据
	 * 半年抓一次
	 */
	
	public void fetchERSData(){
		//先抓2015、2016年预测数据
		fetchForecastData();
		//xls表格格式统一的统一处理
		for(String varName:MapInit.usda_ers_varName2url.keySet()){
			String xlsName = MapInit.usda_ers_varName2url.get(varName);
			String path = "d:/tmp/" + xlsName;
			logger.info("下载"+xlsName+"@"+ersUrl + xlsName);
			FileStrIO.downloadFile(path, ersUrl + xlsName);
			String varNameTrans = varName;
			if(varName.indexOf("-")!=-1){
				varNameTrans = varName.split("-")[0];
			}
			int varId = Variety.getVaridByName(varNameTrans);
			String[][] contents = FileStrIO.readXls(path, null);
			Map<String, String> indexOfCountryMap = new HashMap<String, String>();
			Map<String, String> header2dataMapTmp = new HashMap<String, String>();
			Map<String, String> dataMap = new HashMap<String, String>();
			if(contents != null){
				logger.info("分析"+varName+"@"+path);
				indexOfCountryMap = getIndexOfCountry(contents);
				//开始遍历行，找表头对应的数据
				Map<String, String> cnNameMap = MapInit.usda_ers_varName2cnName.get(varName);
				for(String cnName:cnNameMap.keySet()){
					logger.info("分析"+varName+":"+cnName);
					String indexOfCountry = indexOfCountryMap.get(cnNameMap.get(cnName));
					if(indexOfCountry == null){
						logger.error("没有找到"+cnName+"对应的列");
						continue;
					}
					int rowOfCountry = Integer.parseInt(indexOfCountry.split(",")[0]);
					int colOfCountry = Integer.parseInt(indexOfCountry.split(",")[1]);
					for(int i=0;i<2;i++){
						String timeInt = contents[rowOfCountry+1][colOfCountry+i];
						header2dataMapTmp.clear();
						dataMap.clear();
						for(int row=rowOfCountry+1; row<contents.length; row++){
							if(contents[row][0] != null && !contents[row][0].trim().equals("")
									&& contents[row][colOfCountry+i] != null && !contents[row][colOfCountry+i].equals("")){
								String contentTmp = contents[row][0].trim();
								contentTmp = contentTmp.replaceAll(",", "").replaceAll("\\s+"," ");
								if(header2dataMapTmp.get(contentTmp) != null){
									//处理棉花与棉籽的收购价格同名且在同一个xls中
									if(header2dataMapTmp.get("d"+contentTmp+"d") != null) continue;
									header2dataMapTmp.put("d"+contentTmp+"d", contents[row][colOfCountry+i]);
									continue;
								}
								header2dataMapTmp.put(contentTmp, contents[row][colOfCountry+i]);
							}
						}
						Map<String, String> headerMap = MapInit.usda_ers_varName2header_map.get(varName);
						for(String header:headerMap.keySet()){
							String engHeader = headerMap.get(header);
							if(header.equals("单产")){
								if(varName.equals("美国棉花")){
									engHeader = "Cotton yield";
								}else if(varName.equals("美国高粱")){
									engHeader = "Sorghum Yield";
								}else if(varName.equals("美国花生")){
									engHeader = "Peanut yield";
								}
							}
							dataMap.put(header, "0");
							//生猪的育肥猪购买成本、保育猪购买成本特殊处理
							if(header.equals("育肥猪购买成本") || header.equals("保育猪购买成本")){
								dataMap.put(header, header2dataMapTmp.get("d"+engHeader+"d"));
								continue;
							}
							for(String tmp:header2dataMapTmp.keySet()){
								if(tmp.startsWith(engHeader)){
									dataMap.put(header, header2dataMapTmp.get(tmp));
									break;
								}
							}
						}
						//处理其他成本需要计算的情况
						String calcFor = headerMap.get("其他成本");
						if(calcFor != null && calcFor.startsWith("calc")){
							String[] eles = calcFor.split(",");
							double sumTmp = 0;
							for(int index=1;index<eles.length;index++){
								sumTmp += Double.parseDouble(dataMap.get(eles[index]));
							}
							dataMap.put("其他成本", sumTmp+"");
						}
						logger.info("开始保存"+varName+"("+varId+")"+cnName +timeInt.substring(0,4)+"数据");
						for(String key:dataMap.keySet()){
							System.out.println(key+":"+dataMap.get(key));
						}
						//dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt.substring(0,4)), dataMap);
					}
				}
				if(varName.equals("美国棉花")){
					//美国棉籽和美国棉花在同一个xls中，美国棉籽直接处理一下
					logger.info("分析美国棉籽");
					varName = "美国棉籽";
					varId = Variety.getVaridByName(varName);
					int cottonseedRow = 0;
					for(int row=0; row<contents.length; row++){
						if(contents[row][0] != null && 
								contents[row][0].trim().equals("Cottonseed yield: pounds per planted acre")){
							cottonseedRow = row;
						}
					}
					dataMap.clear();
					for(int i=0;i<2;i++){
						String cnName = "美国主要农业区棉籽单产";
						String timeInt = "";
						for(String header:MapInit.usda_ers_headers_cottonseed_map.keySet()){
							dataMap.put(header, "0");
							String indexOfCountry = indexOfCountryMap.get(MapInit.usda_ers_headers_cottonseed_map.get(header));
							int rowOfCountry = Integer.parseInt(indexOfCountry.split(",")[0]);
							int colOfCountry = Integer.parseInt(indexOfCountry.split(",")[1]);
							timeInt = contents[rowOfCountry+1][colOfCountry+i];
							dataMap.put(header, contents[cottonseedRow][colOfCountry+i]);
						}
						logger.info("开始保存"+varName+"("+varId+")"+cnName +timeInt.substring(0,4)+"数据");
						//dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt.substring(0,4)), dataMap);
					}
					for(int i=0;i<2;i++){
						String cnName = "美国主要农业区棉籽收购价";
						String timeInt = "";
						for(String header:MapInit.usda_ers_headers_cottonseed_map.keySet()){
							dataMap.put(header, "0");
							String indexOfCountry = indexOfCountryMap.get(MapInit.usda_ers_headers_cottonseed_map.get(header));
							int rowOfCountry = Integer.parseInt(indexOfCountry.split(",")[0]);
							int colOfCountry = Integer.parseInt(indexOfCountry.split(",")[1]);
							timeInt = contents[rowOfCountry+1][colOfCountry+i];
							dataMap.put(header, contents[cottonseedRow+1][colOfCountry+i]);
						}
						logger.info("开始保存"+varName+"("+varId+")"+cnName +timeInt.substring(0,4)+"数据");
						//dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt.substring(0,4)), dataMap);
					}
				}
			}else{
				logger.error("没有读取到"+path+"表格数据");
			}
		}
		fetchUsdaErsOthers();
	}
	
	/**
	 * 生猪与牛奶其他模式处理
	 */
	public void fetchUsdaErsOthers(){
		Map<String, String> indexOfCountryMap = new HashMap<String, String>();
		Map<String, String> header2dataMapTmp = new HashMap<String, String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		List<String> years = new ArrayList<String>(){
			{
				add("2013");
				add("2014");
			}
		};
		for(String varNameAXls:MapInit.usda_ers_varNameAXls2cnName_map.keySet()){
			String[] varNameXls = varNameAXls.split("-");
			String varName = varNameXls[0];
			String xlsName = varNameXls[1];
			String downloadUrl = ersUrl + xlsName;
			if(varName.equals("美国牛奶")){
				downloadUrl = ersMilkUrl + xlsName;
				String path = "d:/tmp/" + xlsName;
				logger.info("下载"+xlsName+"@"+downloadUrl);
				FileStrIO.downloadFile(path, downloadUrl);
				int varId = Variety.getVaridByName(varName);
				for(String year:years){
					String timeInt = year;
					String[][] contents = FileStrIO.readXls(path, year);
					if(contents != null){
						logger.info("分析"+varName+"@"+path);
						indexOfCountryMap = getIndexOfCountry(contents);
						//开始遍历行，找指标对应的行
						Map<String, Integer> rowOfCnNameMap = new HashMap<String, Integer>();
						for(int row=1; row<contents.length; row++){
							if(contents[row][0] != null && !contents[row][0].trim().equals("")){
								String contentTmp = contents[row][0].trim();
								contentTmp = contentTmp.replaceAll(",", "").replaceAll("\\s+"," ");
								rowOfCnNameMap.put(contentTmp, row);
							}
						}
						Map<String, String> cnNameMap = MapInit.usda_ers_varNameAXls2cnName_map.get(varNameAXls);
						for(String cnName:cnNameMap.keySet()){
							logger.info("分析"+varName+":"+cnName);
							dataMap.clear();
							int rowCnName = 0;
							for(String tmp:rowOfCnNameMap.keySet()){
								if(tmp.startsWith(cnNameMap.get(cnName))){
									rowCnName = rowOfCnNameMap.get(tmp);
									break;
								}
							}
							if(rowCnName == 0){
								logger.info("没有找到"+cnName+"所在行");
								continue;
							}
							Map<String, String> headerMap = MapInit.usda_ers_varNameAXls2header_map.get(varNameAXls);
							for(String header:headerMap.keySet()){
								String tmp = indexOfCountryMap.get(headerMap.get(header));
								if(tmp == null){
									continue;
								}
								int col = Integer.parseInt(tmp.split(",")[1]);
								dataMap.put(header, contents[rowCnName][col]);
							}
							logger.info("开始保存"+varName+"("+varId+")"+cnName +timeInt.substring(0,4)+"数据");
							for(String key:dataMap.keySet()){
								System.out.println(key+":"+dataMap.get(key));
							}
							//dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt.substring(0,4)), dataMap);
						}
					}
				}
			}else{
				String path = "d:/tmp/" + xlsName;
				logger.info("下载"+xlsName+"@"+downloadUrl);
				FileStrIO.downloadFile(path, downloadUrl);
				int varId = Variety.getVaridByName(varName);
				String[][] contents = FileStrIO.readXls(path, null);
				if(contents != null){
					logger.info("分析"+varName+"@"+path);
					indexOfCountryMap = getIndexOfCountry(contents);
					//开始遍历行，找表头对应的数据
					Map<String, String> cnNameMap = MapInit.usda_ers_varNameAXls2cnName_map.get(varNameAXls);
					for(String cnName:cnNameMap.keySet()){
						logger.info("分析"+varName+":"+cnName);
						String indexOfCountry = indexOfCountryMap.get(cnNameMap.get(cnName));
						if(indexOfCountry == null){
							logger.error("没有找到"+cnName+"对应的列");
							continue;
						}
						int rowOfCountry = Integer.parseInt(indexOfCountry.split(",")[0]);
						int colOfCountry = Integer.parseInt(indexOfCountry.split(",")[1]);
						for(int i=0;i<2;i++){
							String timeInt = contents[rowOfCountry+1][colOfCountry+i];
							header2dataMapTmp.clear();
							dataMap.clear();
							for(int row=rowOfCountry+1; row<contents.length; row++){
								if(contents[row][0] != null && !contents[row][0].trim().equals("")
										&& contents[row][colOfCountry+i] != null && !contents[row][colOfCountry+i].equals("")){
									String contentTmp = contents[row][0].trim();
									contentTmp = contentTmp.replaceAll(",", "").replaceAll("\\s+"," ");
									if(header2dataMapTmp.get(contentTmp) != null){
										//生猪中保育猪与育肥猪相关指标同名
										header2dataMapTmp.put("d"+contentTmp+"d", contents[row][colOfCountry+i]);
										continue;
									}
									header2dataMapTmp.put(contentTmp, contents[row][colOfCountry+i]);
								}
							}
							Map<String, String> headerMap = MapInit.usda_ers_varNameAXls2header_map.get(varNameAXls);
							for(String header:headerMap.keySet()){
								String engHeader = headerMap.get(header);
								dataMap.put(header, "0");
								//生猪的育肥猪购买成本、保育猪购买成本特殊处理
								if(header.equals("育肥猪数量") || header.equals("保育猪数量") || header.equals("商品猪数量")){
									dataMap.put(header, header2dataMapTmp.get("d"+engHeader+"d"));
									continue;
								}
								for(String tmp:header2dataMapTmp.keySet()){
									if(tmp.startsWith(engHeader)){
										dataMap.put(header, header2dataMapTmp.get(tmp));
										break;
									}
								}
							}
							logger.info("开始保存"+varName+"("+varId+")"+cnName +timeInt.substring(0,4)+"数据");
							for(String key:dataMap.keySet()){
								System.out.println(key+":"+dataMap.get(key));
							}
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt.substring(0,4)), dataMap);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 美国牛奶月度数据
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_USDA_ERS_MONTHLY)
	public void start2(){
		String switchFlag = new CrawlerManager().selectCrawler("美国牛奶月度数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到美国牛奶月度数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchUsdaErsMonthly();
			}else{
				logger.info("抓取美国牛奶月度数据的定时器已关闭");
			}
		}
	}
	public void fetchUsdaErsMonthly(){
		Date now = new Date();
		String timeInt = DateTimeUtil.formatDate(DateTimeUtil.addMonth(now, -3), "yyyyMM");
		String month = DateTimeUtil.addMonth(now, -3).toGMTString().split(" ")[1];
		String xlsName = "nationalmonthlymilkcop2010base";
		String downloadUrl = ersMonthlyUrl + "nationalmonthlymilkcop2010base.xls";
		String path = "d:/tmp/" + xlsName;
		logger.info("下载"+xlsName+"@"+downloadUrl);
		FileStrIO.downloadFile(path, downloadUrl);
		String[][] contents = FileStrIO.readXls(path, timeInt.substring(0,4));
		if(contents != null){
			Map<String, String> header2dataMapTmp = new HashMap<String, String>();
			Map<String, String> dataMap = new HashMap<String, String>();
			String varName = "美国牛奶";
			int varId = Variety.getVaridByName(varName);
			logger.info("分析"+varName+"@"+path);
			//月份所在列
			int colMonth = 0;
			for(int row=0;row<contents.length;row++){
				String colContents = "";
				for(int col=0;col<contents[row].length;col++){
					colContents += contents[row][col];
				}
				if(colContents.contains(month)){
					for(int col=0;col<contents[row].length;col++){
						if(contents[row][col] != null && contents[row][col].equals(month)) {
							colMonth = col;
							break;
						}
					}
					break;
				}
			}
			String cnName = "美国月度牛奶生产成本";
			logger.info("分析"+varName+":"+cnName);
			if(colMonth == 0){
				logger.error("没有找到"+month+"对应的列");
			}else{
				for(int row=1; row<contents.length; row++){
					if(contents[row][0] != null && !contents[row][0].trim().equals("")
							&& contents[row][colMonth] != null && !contents[row][colMonth].equals("")){
						String contentTmp = contents[row][0].trim();
						contentTmp = contentTmp.replaceAll(",", "").replaceAll("\\s+"," ");
						header2dataMapTmp.put(contentTmp, contents[row][colMonth]);
					}
				}
				for(String header:MapInit.usda_ers_headers_monthmilk_map.keySet()){
					String engHeader = MapInit.usda_ers_headers_monthmilk_map.get(header);
					dataMap.put(header, "0");
					for(String tmp:header2dataMapTmp.keySet()){
						if(tmp.startsWith(engHeader)){
							dataMap.put(header, header2dataMapTmp.get(tmp));
							break;
						}
					}
				}
				//计算其他成本
				String specialheader = "其他成本";
				String[] addends = MapInit.usda_ers_headers_monthmilk_map.get(specialheader).split(",");
				double sum = 0;
				for(int i=1;i<addends.length;i++){
					sum += Double.parseDouble(dataMap.get(addends[i]));
				}
				dataMap.put(specialheader, sum+"");
				logger.info("开始保存"+varName+"("+varId+")"+cnName +timeInt+"数据");
				for(String key:dataMap.keySet()){
					System.out.println(key+":"+dataMap.get(key));
				}
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}
		}
	}
	
	/**
	 * 美农成本收益
	 * 预测数据
	 */
	public void fetchForecastData(){
		String xlsName = "cop_forecast.xls";
		String path = "d:/tmp/" + xlsName;
		String downloadUrl = ersForecastsUrl + xlsName;
		logger.info("下载"+xlsName+"@"+ downloadUrl);
		FileStrIO.downloadFile(path, downloadUrl);
		String[][] contents = FileStrIO.readXls(path, null);
		Map<String, String> colOfVarNameMap = new HashMap<String, String>();
		Map<String, String> header2dataMapTmp = new HashMap<String, String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		if(contents != null){
			for(int row=0;row<contents.length;row++){
				String colContents = "";
				for(int col=0;col<contents[row].length;col++){
					colContents += contents[row][col];
				}
				if(colContents.contains("Corn")){
					for(int col=0;col<contents[row].length;col++){
						if(contents[row][col] == null) break;
						colOfVarNameMap.put(contents[row][col].trim(), row+","+col);
					}
					break;
				}
			}
			for(String varName:MapInit.usda_ers_fore_varName_map.keySet()){
				logger.info("分析"+varName+"@"+path);
				String cnName = varName+"成本收益";
				int varId = Variety.getVaridByName(varName);
				String index = colOfVarNameMap.get(MapInit.usda_ers_fore_varName_map.get(varName));
				int rowTmp = Integer.parseInt(index.split(",")[0]);
				int colTmp = Integer.parseInt(index.split(",")[1]);
				//开始遍历行，找表头对应的数据
				for(int i=1;i<3;i++){
					String timeInt = contents[rowTmp+1][colTmp+i];
					header2dataMapTmp.clear();
					dataMap.clear();
					for(int row=rowTmp+1; row<contents.length; row++){
						if(contents[row][0] != null && !contents[row][0].trim().equals("")
								&& contents[row][colTmp+i] != null && !contents[row][colTmp+i].equals("")){
							String contentTmp = contents[row][0].trim();
							contentTmp = contentTmp.replaceAll(",", "").replaceAll("\\s+"," ");
							header2dataMapTmp.put(contentTmp, contents[row][colTmp+i]);
						}
					}
					Map<String, String> headerMap = MapInit.usda_ers_varName2header_map.get(varName);
					for(String header:headerMap.keySet()){
						String engHeader = headerMap.get(header);
						dataMap.put(header, "0");
						if(header.equals("土地机会成本")){
							dataMap.put(header, header2dataMapTmp.get("Land"));
							continue;
						}else if(header.equals("间接费用")){
							dataMap.put(header, header2dataMapTmp.get("Total allocated costs"));
							continue;
						}else if(header.equals("家庭劳动机会成本")){
							dataMap.put(header, header2dataMapTmp.get("Unpaid labor"));
							continue;
						} 
						for(String tmp:header2dataMapTmp.keySet()){
							if(tmp.startsWith(engHeader)){
								dataMap.put(header, header2dataMapTmp.get(tmp));
								break;
							}
						}
					}
					logger.info("开始保存"
							+varName+"("+varId+")"
							+cnName 
							+timeInt.substring(0,4)
							+"数据");
					for(String key:dataMap.keySet()){
						System.out.println(key+":"+dataMap.get(key));
					}
					dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt.substring(0,4)), dataMap);
				}
			}
		}else{
			logger.error("没有读取到"+path+"表格数据");
		}
	}
	
	private Map<String, String> getIndexOfCountry(String[][] contents){
		//找出国家对应哪一列
		Map<String, String> indexOfCountryMap = new HashMap<String, String>();
		for(int row=0;row<contents.length;row++){
			String colContents = "";
			for(int col=0;col<contents[row].length;col++){
				colContents += contents[row][col];
			}
			if(colContents.contains("United States") || colContents.contains("States") || colContents.contains("Farrow-to-Weanling") || colContents.contains("Fewer than")){
				for(int col=0;col<contents[row].length;col++){
					if(contents[row][col] == null) break;
					indexOfCountryMap.put(contents[row][col].trim(), row+","+col);
				}
				break;
			}
		}
		return indexOfCountryMap;
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_USDA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("美农全球数据、平衡表数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到美农全球数据、平衡表数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				if(update()){
					fetchAllCountryDatas();//抓全球数据
					fetchBalanceTable();//抓平衡表数据
					updatePredictDatas();//更新预测数据数据
					updateOilBearAndVege();//更新全球植物油与全球油料
					fetchOtherUsda();//抓USDA其他数据
				}
			}else{
				logger.info("抓取美农全球数据、平衡表数据的定时器已关闭");
			}
		}
	}
	
	//监测全球稻米的收获面积是否更新（以其反应其它品种数据有没有更新，概率并非百分之百，牺牲准确率以保证效率）
	private boolean update(){
		int varId = Variety.getVaridByName("全球稻米");
		params.put("lstCommodity", "0422110");
		params.put("lstCountry", "99");	//post参数，“99”代表 “World Total”。
		for(int i = 0; i < 2; i++){
			int timePoint = yearList[i];	//选取的探测时间点，用于探测数据有没有更新。
			params.put("lstDate", timePoint +"");
			String[] filters = {"table","id","gridReport"};
			String[] rowColChoose = {"0", "0001"};
			for(String tmp:MapInit.usda_world_riceMilled_dataType_map.keySet()){
				params.put("lstAttribute", MapInit.usda_world_riceMilled_dataType_map.get(tmp));
				String contents = dataFetchUtil.getPostPrimaryContent(0, url, "utf-8", "全球稻米的"+tmp+"更新情况", filters, rowColChoose, 0, params);
				if(contents != null && !contents.equals("")){
					List<String> fields = Arrays.asList("EditTime","全球");
					List<String> results = dao.getListValues(varId, tmp, timePoint +"", fields);
					if(results.size()>0){
						if(!results.get(0).startsWith(timePoint+"-"+(DateTimeUtil.getCurrentMonthInt()+"").substring(4,6))
								&& !(Float.parseFloat(contents.split("\n")[0])+"").equals(results.get(1))){
							return true;
						}else{
							logger.info("美农全球稻米"+tmp+"当前月的本地数据库数据已经更新或者当前月网页数据尚未更新");
						}
					}else{
						logger.error("没有查找到全球稻米"+timePoint+"的记录");
					}
				}else{
					logger.error("请检查美农网页链接是否正常，当前不能够抓取全球稻米的收获面积以检查更新情况(也可能是没有符合筛选条件的数据)");
				}	
			}
		}

		return false;
	}
	
	public static void main(String[] args){
		USDAOnlineData data = new USDAOnlineData();
		//data.updatePredictDatas();
		//data.fetchUsdaErsMonthly();
		//data.fetchForecastData();
		//data.fetchUsdaErsMonthly();
		//data.fetchERSData();//美国农业部成本收益数据
		//data.fetchUsdaErsOthers();//美国农业部成本收益数据
		
		//data.fetchWeeklyExportDatas();//周出口全球、周出口中国数据
//		String[] dateArr = {"2016-11-25", "2016-12-02", "2016-12-09", "2016-12-16", "2016-12-23", "2016-12-30", "2017-01-06", 
//				"2017-01-13", "2017-01-20", "2017-01-27", "2017-02-03"};
//		for(String dateStr: dateArr){
//			Date date = DateTimeUtil.parseDateTime(dateStr, DateTimeUtil.YYYY_MM_DD);
//			data.fetchWeeklyExportDatas(date);//周出口全球、周出口中国数据
//			data.fetchWeeklyExportToOtherCountry(date);//主要国家的周出口量、年度累计出口等
//		}
		
//		String tarUrl = "https://apps.fas.usda.gov/PSDOnlineApi/api/query/RunQuery";
//		String postJson = "{\"queryId\":0,\"commodityGroupCode\":null,\"commodities\":[\"0011000\"],\"attributes\":[25],\"countries\":[\"ALL\"],\"marketYears\":[2017,2016,2015,2014],\"chkCommoditySummary\":false,\"chkAttribSummary\":false,\"chkCountrySummary\":false,\"commoditySummaryText\":\"\",\"attribSummaryText\":\"\",\"countrySummaryText\":\"\",\"optionColumn\":\"year\",\"chkTopCountry\":false,\"topCountryCount\":\"\",\"chkfileFormat\":false,\"chkPrevMonth\":false,\"chkMonthChange\":false,\"chkCodes\":false,\"chkYearChange\":false,\"queryName\":\"\",\"sortOrder\":\"Commodity/Attribute/Country\",\"topCountryState\":false}";
//		String content = data.dataFetchUtil.httpsPostJson(tarUrl, postJson, "application/json;charset=utf-8", "utf-8", "USDA全球数据");
//		System.out.println(content);
		
//		data.fetchAllCountryDatas();//全球数据
		data.fetchBalanceTable();//平衡表
	}
	
}
