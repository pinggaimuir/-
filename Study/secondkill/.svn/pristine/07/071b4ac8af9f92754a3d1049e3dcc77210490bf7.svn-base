package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;

/**
 * 美国禽肉、美国鸡蛋月度数据爬虫
 * @author bric_yangyulin
 * @date 2016-09-06
 * */
public class AmericanPoultryMeat {
	private static final Logger LOG = Logger.getLogger(AmericanPoultryMeat.class);
	private static String className = AmericanPoultryMeat.class.getName();
	private DAOUtils dao = new DAOUtils();
	private Map<String, String> numMonthMap = new HashMap<String, String>();//数字-月份英文映射
	private String[] urls = {
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Meat_statistics/Livestock_and_poultry_slaughter/SlaughterCountsFull.xls",//联邦检查屠宰量
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Meat_statistics/Livestock_and_poultry_live_and_dressed_weights/SlaughterWeightsFull.xls",//联邦检查屠宰活重
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Meat_statistics/Red_meat_and_poultry_beginning_cold_storage_stocks/ColdStorageFull.xls",//鸡肉冷库存量、冰蛋品冷库库存
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Quarterly_red_meat_poultry_and_egg_supply_and_disappearance_and_per_capita_disappearance/Other_chicken/WASDE_OtherChickenFull.xls",//其他鸡肉年度供需、其他鸡肉年度人均消费
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Quarterly_red_meat_poultry_and_egg_supply_and_disappearance_and_per_capita_disappearance/Broilers/WASDE_BroilerFull.xls",//肉鸡肉年度供需、肉鸡肉年度人均消费、肉鸡肉季度供需、肉鸡肉季度人均消费
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Quarterly_red_meat_poultry_and_egg_supply_and_disappearance_and_per_capita_disappearance/Turkeys/WASDE_TurkeyFull.xls",//火鸡肉年度供需、火鸡肉年度人均消费、火鸡肉季度供需、火鸡肉季度人均消费
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Quarterly_red_meat_poultry_and_egg_supply_and_disappearance_and_per_capita_disappearance/Total_poultry/WASDE_TotalPoultryFull.xls",//禽肉年度供需、禽肉年度人均消费、禽肉季度供需、禽肉季度人均消费
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Quarterly_red_meat_poultry_and_egg_supply_and_disappearance_and_per_capita_disappearance/Eggs_and_egg_products/WASDE_EggAndProductFull.xls",//年度供需平衡表、季度供需平衡表
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Broiler_turkey_and_egg_feed_costs/BTECOST.xlsx",//饲料价格及饲料成本指数、肉鸡饲料价格及饲料成本指数、火鸡饲料价格及饲料成本指数
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Production_Indicators_and_Estimated_Returns/ProductionIndicators.xls",//蛋鸡生产指标、肉鸡生产指标、火鸡生产指标
			"http://www.ers.usda.gov/datafiles/Livestock_Meat_Domestic_Data/Wholesale_Prices/WholesalePrices.xls"//鸡蛋批发价、肉鸡肉批发价、东北地区肉鸡批发价、东部地区火鸡批发价、东部地区火鸡肉批发价
	};
	{
		numMonthMap.put("01", "Jan");
		numMonthMap.put("02", "Feb");
		numMonthMap.put("03", "Mar");
		numMonthMap.put("04", "Apr");
		numMonthMap.put("05", "May");
		numMonthMap.put("06", "Jun");
		numMonthMap.put("07", "Jul");
		numMonthMap.put("08", "Aug");
		numMonthMap.put("09", "Sep");
		numMonthMap.put("10", "Oct");
		numMonthMap.put("11", "Nov");
		numMonthMap.put("12", "Dec");
	}
	
	private Map<String, String> supplyColName = new HashMap<String, String>();//供需数据列名的英中文映射
	{
		supplyColName.put("Netready-to-cook(RTC)4/", "总产量");
		supplyColName.put("Beginningstocks", "期初库存");
		supplyColName.put("Imports", "进口");
		supplyColName.put("Totalsupply5/", "总供给");
		supplyColName.put("Exports", "出口");
		supplyColName.put("Endingstocks", "期末库存");
	}
	private Map<String, String> consumeColName = new HashMap<String, String>();//消费数据列名的英中文映射
	{
		consumeColName.put("Carcassweight", "人均胴体消费量");
		consumeColName.put("Retailweight", "人均零售消费量");
		consumeColName.put("Bonelessretailweight", "人均去骨零售消费量");
	}
	private Map<String, String> balanceColName = new HashMap<String, String>();//平衡表数据的英中文映射
	{
		balanceColName.put("Table", "食用鸡蛋产量");
		balanceColName.put("Hatching", "种蛋产量");
		balanceColName.put("Total", "总产量");
		balanceColName.put("Beginningstocks2/", "期初库存");
		balanceColName.put("Imports", "进口");
		balanceColName.put("Totalsupply3/", "总供给");
		balanceColName.put("Exports", "出口");
		balanceColName.put("Endingstocks2/", "期末库存");
		balanceColName.put("Hatchinguse", "孵化使用");
		balanceColName.put("Totaldis-appearance3/4/", "国内总消费");
		balanceColName.put("U.S.population5/(1,000persons)", "美国人口");
		balanceColName.put("Percapitadisappear-ance(shelleggequivalent,number)", "人均消费量");
		balanceColName.put("Federallyinspectedeggsbroken6/", "联邦检查破损个数");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_AM_POULTEYMEAT_MONTH)//每月3日更新上个月的
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("美国禽肉鸡蛋数据月更新", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到美国禽肉鸡蛋数据月更新在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到美国禽肉鸡蛋数据月更新在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					fetchData(new Date());
				} catch(Exception e) {
					LOG.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				LOG.info("抓取美国禽肉鸡蛋数据月更新的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "美国禽肉鸡蛋数据月更新的定时器已关闭");
			}
		}
	}
	
	public void fetchData(Date date){
		LOG.info("---------开始抓取美国肉禽及鸡蛋月度数据-----------");
		Date lastMonth = DateTimeUtil.addMonth(date, -1);//上月
		Date preLastMonth = DateTimeUtil.addMonth(date, -2);//上上月
		String lastMonthTime = DateTimeUtil.formatDate(lastMonth, DateTimeUtil.YYYYMM);
		String preLastMonthTime = DateTimeUtil.formatDate(preLastMonth, DateTimeUtil.YYYYMM);
		/*抓取相关文件*/
		String savePathPrefix = Constants.SAVEDHTML_ROOT + Constants.FILE_SEPARATOR 
				+ DateTimeUtil.formatDate(new Date(), "yyyyMMdd") + Constants.FILE_SEPARATOR 
				+ className.substring(className.lastIndexOf(".") + 1) + Constants.FILE_SEPARATOR;
//		String savePathPrefix = Constants.SAVEDHTML_ROOT + Constants.FILE_SEPARATOR 
//				+ "20160906" + Constants.FILE_SEPARATOR 
//				+ className.substring(className.lastIndexOf(".") + 1) + Constants.FILE_SEPARATOR;
		for(int urlIndex = 0; urlIndex < urls.length; urlIndex++){
			try{
				LOG.info("开始抓取并解析文件：" + urls[urlIndex]);
				String savePath = savePathPrefix + urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1);
				MyHttpClient.fetchAndSave(urls[urlIndex], savePath);
				/*解析文件*/
				if("WholesalePrices.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))){
					parseWholesalePrices(savePath, lastMonthTime);
				} else if("SlaughterCountsFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					parseSlaughterCountsFull(savePath, preLastMonthTime, "联邦检查屠宰量", "SlaughterCounts-Full");
				} else if("SlaughterWeightsFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					parseSlaughterCountsFull(savePath, preLastMonthTime, "联邦检查屠宰活重", "SlaughterWeights-Full");
				} else if("ColdStorageFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					parseColdStorageFull(savePath, lastMonthTime, "ColdStorage-Full");
				} else if("WASDE_OtherChickenFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
//					parseWASDEOtherChickenFull(savePath, timeInt.substring(0, 4), "WASDE_OtherChicken", "美国禽肉");
					Map<String, Map<String, String>> yearCnMap = new HashMap<String, Map<String, String>>();//年度数据表
					yearCnMap.put("其他鸡肉年度供需", supplyColName);
					yearCnMap.put("其他鸡肉年度人均消费", consumeColName);
					parseWASDEBroilerFull(savePath, lastMonthTime.substring(0, 4), "WASDE_OtherChicken", "美国禽肉", yearCnMap, null, null);
				} else if("WASDE_BroilerFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					Map<String, Map<String, String>> yearCnMap = new HashMap<String, Map<String, String>>();//年度数据表
					Map<String, Map<String, String>> quarCnMap = new HashMap<String, Map<String, String>>();//季度数据表
					yearCnMap.put("肉鸡肉年度供需", supplyColName);
					yearCnMap.put("肉鸡肉年度人均消费", consumeColName);
					quarCnMap.put("肉鸡肉季度供需", supplyColName);
					quarCnMap.put("肉鸡肉季度人均消费", consumeColName);
					parseWASDEBroilerFull(savePath, lastMonthTime.substring(0, 4), "WASDE_Broiler", "美国禽肉", yearCnMap, quarCnMap, null);
				} else if("WASDE_TurkeyFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					Map<String, Map<String, String>> yearCnMap = new HashMap<String, Map<String, String>>();//年度数据表
					Map<String, Map<String, String>> quarCnMap = new HashMap<String, Map<String, String>>();//季度数据表
					yearCnMap.put("火鸡肉年度供需", supplyColName);
					yearCnMap.put("火鸡肉年度人均消费", consumeColName);
					quarCnMap.put("火鸡肉季度供需", supplyColName);
					quarCnMap.put("火鸡肉季度人均消费", consumeColName);
					parseWASDEBroilerFull(savePath, lastMonthTime.substring(0, 4), "WASDE_Turkey", "美国禽肉", yearCnMap, quarCnMap, null);
				} else if("WASDE_TotalPoultryFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					Map<String, Map<String, String>> yearCnMap = new HashMap<String, Map<String, String>>();//年度数据表
					Map<String, Map<String, String>> quarCnMap = new HashMap<String, Map<String, String>>();//季度数据表
					yearCnMap.put("禽肉年度供需", supplyColName);
					yearCnMap.put("禽肉年度人均消费", consumeColName);
					quarCnMap.put("禽肉季度供需", supplyColName);
					quarCnMap.put("禽肉季度人均消费", consumeColName);
					parseWASDEBroilerFull(savePath, lastMonthTime.substring(0, 4), "WASDE_TotalPoultry", "美国禽肉", yearCnMap, quarCnMap, null);
				}  else if("WASDE_EggAndProductFull.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					Map<String, Map<String, String>> yearCnMap = new HashMap<String, Map<String, String>>();//年度数据表
					Map<String, Map<String, String>> quarCnMap = new HashMap<String, Map<String, String>>();//季度数据表
					Map<String, Integer> colNeedIds = new HashMap<String, Integer>();//所需列-列号映射
					yearCnMap.put("年度供需平衡表", balanceColName);
					quarCnMap.put("季度供需平衡表", balanceColName);
					colNeedIds.put("Table", null);
					colNeedIds.put("Hatching", null);
					colNeedIds.put("Total", null);
					colNeedIds.put("Beginningstocks2/", null);
					colNeedIds.put("Imports", null);
					colNeedIds.put("Totalsupply3/", null);
					colNeedIds.put("Exports", null);
					colNeedIds.put("Endingstocks2/", null);
					colNeedIds.put("Hatchinguse", null);
					colNeedIds.put("Totaldis-appearance3/4/", null);
					colNeedIds.put("U.S.population5/(1,000persons)", null);
					colNeedIds.put("Percapitadisappear-ance(shelleggequivalent,number)", null);
					colNeedIds.put("Federallyinspectedeggsbroken6/", null);
					parseWASDEBroilerFull(savePath, lastMonthTime.substring(0, 4), "WASDE_EggAndProduct", "美国鸡蛋", yearCnMap, quarCnMap, colNeedIds);
				} else if("BTECOST.xlsx".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					parseBTECOST(savePath, preLastMonthTime);
				} else if("ProductionIndicators.xls".equals(urls[urlIndex].substring(urls[urlIndex].lastIndexOf("/") + 1))) {
					parseProductionIndicators(savePath, preLastMonthTime);
				}
				try {
					Thread.sleep((int)(Math.random() * 5000));
				} catch (InterruptedException e1) {
					LOG.error(e1);
				}
			} catch(Exception e) {
				LOG.error("发生未知异常。", e);
				RecordCrawlResult.recordFailData(className, "", "", "抓取或解析失败：" + urls[urlIndex]);
			}
		}
	}
	
	/**
	 * 解析SlaughterCountsFull.xls 和 SlaughterWeightsFull.xls (该数据3号还没有上月数据)
	 * @param filePath 文件保存路径
	 * @param timeIntStr 待解析数据时间序列
	 * */
	public void parseSlaughterCountsFull(String filePath, String timeIntStr, String cnName, String sheetName){
		int maxColumn = 35;//最大列数
		String[][] fileRows = FileStrIO.readXls(filePath, sheetName, maxColumn);//正常情况文件中共含32列
		int broilersCol = -1;	//记录肉鸡数据所在列号。
		int otherCol = -1;		//其他鸡数据所在列号。
		int turkeysCol = -1;	//火鸡数据所在列号。
		for(int colNum = 0; colNum < maxColumn; colNum++){
			if(fileRows[2][colNum] != null && fileRows[2][colNum].equals("Broilers")){
				broilersCol = colNum;
				continue;
			}
			if(fileRows[2][colNum] != null && fileRows[2][colNum].equals("Other chickens")){
				otherCol = colNum; 
				continue;
			}
			if(fileRows[2][colNum] != null && fileRows[2][colNum].equals("Turkeys")){
				turkeysCol = colNum;
				continue;
			}
		}
		if(broilersCol != -1 && otherCol != -1 && turkeysCol != -1){
			String timeStr = numMonthMap.get(timeIntStr.substring(4)) + "-" + timeIntStr.substring(0, 4);
			Map<String, String> dataMap = new HashMap<String, String>();
			for(int i = 0; i < fileRows.length; i++){
				if(fileRows[i][0] != null && fileRows[i][0].equals(timeStr)){//待提取数据
					dataMap.put("肉鸡", fileRows[i][broilersCol]);
					dataMap.put("其他鸡", fileRows[i][otherCol]);
					dataMap.put("火鸡", fileRows[i][turkeysCol]);
					break;
				}
			}
			if(!dataMap.isEmpty()){
				int timeInt = Integer.parseInt(timeIntStr);
				dao.saveOrUpdateByDataMap("美国禽肉", cnName, timeInt, dataMap);
			} else {
				RecordCrawlResult.recordFailData(className, "美国禽肉", cnName, "数据为空。");
			}
		} else {
			RecordCrawlResult.recordFailData(className, "美国禽肉", cnName, "文件格式有变动。");
		}
	}
	
	/**
	 * 解析ColdStorageFull.xls
	 * @param filePath 文件保存路径
	 * @param timeIntStr 待解析数据时间序列
	 * */
	public void parseColdStorageFull(String filePath, String timeIntStr, String sheetName){
		int maxColumn = 15;//最大列数
		int titleRow = 1;//列名所在列号
		String[][] fileRows = FileStrIO.readXls(filePath, sheetName);
		int broilersCol = -1;	//记录肉鸡数据所在列号
		int otherCol = -1;		//其他鸡数据所在列号
		int turkeysCol = -1;	//火鸡数据所在列号
		int frozenEggsCol = -1; //冰蛋品数据所在列号
		for(int colNum = 0; colNum < maxColumn; colNum++){
			if(fileRows[titleRow][colNum] != null && fileRows[titleRow][colNum].equals("Broiler")){
				broilersCol = colNum;
				continue;
			}
			if(fileRows[titleRow][colNum] != null && fileRows[titleRow][colNum].equals("Other chicken")){
				otherCol = colNum; 
				continue;
			}
			if(fileRows[titleRow][colNum] != null && fileRows[titleRow][colNum].equals("Turkey")){
				turkeysCol = colNum;
				continue;
			}
			if(fileRows[titleRow][colNum] != null && fileRows[titleRow][colNum].equals("Frozen eggs")){
				frozenEggsCol = colNum;
				continue;
			}
		}
		if(broilersCol != -1 && otherCol != -1 && turkeysCol != -1){
			String timeStr = numMonthMap.get(timeIntStr.substring(4)) + "-" + timeIntStr.substring(0, 4);
			Map<String, String> dataMap = new HashMap<String, String>();
			for(int i = 0; i < fileRows.length; i++){
				if(fileRows[i][0] != null && fileRows[i][0].equals(timeStr)){//待提取数据
					dataMap.put("肉鸡", fileRows[i][broilersCol]);
					dataMap.put("其他鸡", fileRows[i][otherCol]);
					dataMap.put("火鸡", fileRows[i][turkeysCol]);
					break;
				}
			}
			if(!dataMap.isEmpty()){
				int timeInt = Integer.parseInt(timeIntStr);
				dao.saveOrUpdateByDataMap("美国禽肉", "鸡肉冷库存量", timeInt, dataMap);
			} else {
				RecordCrawlResult.recordFailData(className, "美国禽肉", "鸡肉冷库存量", "数据为空。");
			}
		} else {
			RecordCrawlResult.recordFailData(className, "美国禽肉", "鸡肉冷库存量", "文件格式有变动。");
		}
		if(frozenEggsCol != -1){
			String timeStr = numMonthMap.get(timeIntStr.substring(4)) + "-" + timeIntStr.substring(0, 4);
			Map<String, String> frozenEggsMap = new HashMap<String, String>();
			for(int i = 0; i < fileRows.length; i++){
				if(fileRows[i][0] != null && fileRows[i][0].equals(timeStr)){//待提取数据
					frozenEggsMap.put("全国", fileRows[i][frozenEggsCol]);
					break;
				}
			}
			if(!frozenEggsMap.isEmpty()){
				int timeInt = Integer.parseInt(timeIntStr);
				dao.saveOrUpdateByDataMap("美国鸡蛋", "冰蛋品冷库库存", timeInt, frozenEggsMap);
			} else {
				RecordCrawlResult.recordFailData(className, "美国鸡蛋", "冰蛋品冷库库存", "数据为空。");
			}
		}
	}
	
	/**
	 * 解析WASDE_OtherChickenFull.xls --- 其他鸡肉年度供需、其他鸡肉年度人均消费--- 年度数据-月更
	 * @param filePath 文件保存路径
	 * @param timeIntStr 待解析数据时间序列
	 * @param sheetName 工作表名
	 * @param varName 品种名 
	 * */
	/*
	public void parseWASDEOtherChickenFull(String filePath, String timeIntStr, String sheetName, String varName){
		int titleCol = 2;//列名所在列号
		int colNum = 20;//读取列数
		String[][] fileRows = FileStrIO.readXls(filePath, sheetName, colNum);
		Map<String, Integer> colNeedIds = new HashMap<String, Integer>();
		{
			colNeedIds.put("Netready-to-cook(RTC)4/", null);
			colNeedIds.put("Beginningstocks", null);
			colNeedIds.put("Imports", null);
			colNeedIds.put("Totalsupply5/", null);
			colNeedIds.put("Exports", null);
			colNeedIds.put("Endingstocks", null);
			colNeedIds.put("Carcassweight", null);
			colNeedIds.put("Retailweight", null);
			colNeedIds.put("Bonelessretailweight", null);
		}
		Map<String, String> supplyColName = new HashMap<String, String>();//供需数据列名的英中文映射
		{
			supplyColName.put("Netready-to-cook(RTC)4/", "总产量");
			supplyColName.put("Beginningstocks", "期初库存");
			supplyColName.put("Imports", "进口");
			supplyColName.put("Totalsupply5/", "总供给");
			supplyColName.put("Exports", "出口");
			supplyColName.put("Endingstocks", "期末库存");
		}
		Map<String, String> consumeColName = new HashMap<String, String>();//消费数据列名的英中文映射
		{
			consumeColName.put("Carcassweight", "人均胴体消费量");
			consumeColName.put("Retailweight", "人均零售消费量");
			consumeColName.put("Bonelessretailweight", "人均去骨零售消费量");
		}
		Map<String, Map<String, String>> cnNameMap = new HashMap<String, Map<String, String>>();//中文名-列名 表映射
		{
			cnNameMap.put("其他鸡肉年度供需", supplyColName);
			cnNameMap.put("其他鸡肉年度人均消费", consumeColName);
		}
		for(int colId = 0; colId < colNum; colId++){
			if(fileRows[titleCol][colId] != null && colNeedIds.containsKey(fileRows[titleCol][colId].replaceAll("\\s", ""))){
				colNeedIds.put(fileRows[titleCol][colId].replaceAll("\\s", ""), colId);
			}
		}
		//提取数据//
		boolean isDataFound = false;//标识是否找到相应时间序列的数据。 
		String yearStr = null;//文件中的年份（浮点型）
		for(int rowId = 0; rowId < fileRows.length; rowId++){
			if(fileRows[rowId][0] != null && !fileRows[rowId][0].isEmpty()){
				yearStr = fileRows[rowId][0];
			}
			if(yearStr != null && yearStr.startsWith(timeIntStr) && "Yr Jan-Dec".equals(fileRows[rowId][1])){//待抓取时间序列的数据
				isDataFound = true;
				for(String cnName: cnNameMap.keySet()){
					Map<String, String> colNameMap = cnNameMap.get(cnName);
					Map<String, String> dataMap = new HashMap<String, String>();
					for(String enStr: colNameMap.keySet()){
						if(colNeedIds.get(enStr) != null){
							String valueStr = fileRows[rowId][colNeedIds.get(enStr)];
							if(valueStr != null && !valueStr.isEmpty()){
								dataMap.put(colNameMap.get(enStr), valueStr);
							}
						}
					}
					if(!dataMap.isEmpty()){
						int timeInt = Integer.parseInt(timeIntStr);
						dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
					} else {
						RecordCrawlResult.recordFailData(className, varName, cnName, "数据为空：" + timeIntStr);
					}
				}
				break;
			}
		}
		if(!isDataFound){
			RecordCrawlResult.recordFailData(className, varName, null, "尚未更新数据：" + timeIntStr);
		}
	}
	*/
	
	/**
	 * 解析
	 * WASDE_OtherChickenFull.xls --- 其他鸡肉年度供需、其他鸡肉年度人均消费--- 年度数据-月更
	 * WASDE_BroilerFull.xls --- 肉鸡肉年度供需、肉鸡肉年度人均消费、肉鸡肉季度供需、肉鸡肉季度人均消费 --- 年度、季度数据-月更
	 * WASDE_TurkeyFull.xls --- 火鸡肉年度供需、火鸡肉年度人均消费、火鸡肉季度供需、火鸡肉季度人均消费 --- 年度、季度数据 月更
	 * WASDE_TotalPoultryFull.xls --- 禽肉年度供需、禽肉年度人均消费、禽肉季度供需、禽肉季度人均消费 --- 年度、季度数据 月更
	 * WASDE_EggAndProductFull.xls --- 年度供需平衡表、季度供需平衡表 --- 年度、季度数据 月更
	 * @param filePath 文件保存路径
	 * @param timeIntStr 待解析数据时间序列
	 * @param sheetName 工作表名
	 * @param varName 品种名 
	 * @param yearCnMap 年度数据中文名-列名表映射
	 * @param quarCnMap 季度数据中文名-列名表映射
	 * @param colNeedIds 所需列-列号 映射（为null时取默认值）
	 * */
	public void parseWASDEBroilerFull(String filePath, String timeIntStr, String sheetName, String varName, 
			Map<String, Map<String, String>> yearCnMap, Map<String, Map<String, String>> quarCnMap, Map<String, Integer> colNeedIds){
		int titleCol = 2;//列名所在列号
		int colNum = 20;//读取列数
		String[][] fileRows = FileStrIO.readXls(filePath, sheetName, colNum);
		if(colNeedIds == null){//默认值
			colNeedIds = new HashMap<String, Integer>();
			{
				colNeedIds.put("Netready-to-cook(RTC)4/", null);
				colNeedIds.put("Beginningstocks", null);
				colNeedIds.put("Imports", null);
				colNeedIds.put("Totalsupply5/", null);
				colNeedIds.put("Exports", null);
				colNeedIds.put("Endingstocks", null);
				colNeedIds.put("Carcassweight", null);
				colNeedIds.put("Retailweight", null);
				colNeedIds.put("Bonelessretailweight", null);
			}
		}
		for(int colId = 0; colId < colNum; colId++){
			if(fileRows[titleCol][colId] != null && colNeedIds.containsKey(fileRows[titleCol][colId].replaceAll("\\s", ""))){
				colNeedIds.put(fileRows[titleCol][colId].replaceAll("\\s", ""), colId);
			}
		}
		/*提取数据*/
		boolean isDataFound = false;//标识是否找到相应时间序列的数据。 
		String yearStr = null;//文件中的年份（浮点型）
		for(int rowId = 0; rowId < fileRows.length; rowId++){
			if(fileRows[rowId][0] != null && !fileRows[rowId][0].isEmpty()){
				yearStr = fileRows[rowId][0];
			}
			if(yearStr != null && yearStr.startsWith(timeIntStr)){//待抓取时间序列的数据
				isDataFound = true;
				if(yearCnMap != null && "Yr Jan-Dec".equals(fileRows[rowId+4][1])){//提取年度数据
					for(String cnName: yearCnMap.keySet()){
						Map<String, String> colNameMap = yearCnMap.get(cnName);
						Map<String, String> dataMap = new HashMap<String, String>();
						for(String enStr: colNameMap.keySet()){
							if(colNeedIds.get(enStr) != null){
								String valueStr = fileRows[rowId+4][colNeedIds.get(enStr)];
								if(valueStr != null && !valueStr.isEmpty()){
									dataMap.put(colNameMap.get(enStr), valueStr);
								}
							}
						}
						if(!dataMap.isEmpty()){
							int timeInt = Integer.parseInt(timeIntStr);
							dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						} else {
							RecordCrawlResult.recordFailData(className, varName, cnName, "年度数据为空：" + timeIntStr);
						}
					}
				} else if(yearCnMap != null) {
					RecordCrawlResult.recordFailData(className, varName, null, filePath.substring(filePath.lastIndexOf("//")) + "年度数据为空：" + timeIntStr);
				}
				if(quarCnMap != null){//提取季度数据
					String[] signStrs = {"Q1 Jan-Mar", "Q2 Apr-Jun", "Q3 Jul-Sep", "Q4 Oct-Dec"}; 
					for(int signIndex = 0; signIndex < signStrs.length; signIndex++){
						int rowNeedId = rowId+signIndex;//所需数据所在行号
						if(signStrs[signIndex].equals(fileRows[rowNeedId][1])){//一季度
							for(String cnName: quarCnMap.keySet()){
								Map<String, String> colNameMap = quarCnMap.get(cnName);
								Map<String, String> dataMap = new HashMap<String, String>();
								for(String enStr: colNameMap.keySet()){
									if(colNeedIds.get(enStr) != null){
										String valueStr = fileRows[rowNeedId][colNeedIds.get(enStr)];
										if(valueStr != null && !valueStr.isEmpty()){
											dataMap.put(colNameMap.get(enStr), valueStr);
										}
									}
								}
								if(!dataMap.isEmpty()){
									int timeInt = Integer.parseInt(timeIntStr);
									timeInt = timeInt * 100 + (signIndex + 1) * 3;
									dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
								} else {
									RecordCrawlResult.recordFailData(className, varName, cnName, (signIndex + 1) +"季度数据为空：" + timeIntStr);
								}
							}
						} else {
							RecordCrawlResult.recordFailData(className, varName, null, filePath.substring(filePath.lastIndexOf("//")) + (signIndex + 1) +"季度数据为空：" + timeIntStr);
						}
					}
				}
				break;
			}
		}
		if(!isDataFound){
			RecordCrawlResult.recordFailData(className, varName, null, filePath.substring(filePath.lastIndexOf(Constants.FILE_SEPARATOR)) + "尚未更新数据：" + timeIntStr);
		}
	}
	
	/**
	 * 解析BTECOST.xlsx -- 饲料价格及饲料成本指数、肉鸡饲料价格及饲料成本指数、火鸡饲料价格及饲料成本指数 -- 月度数据
	 * */
	public void parseBTECOST(String filePath, String timeIntStr){
		int colNum = 10;//读取列数
		int timeInt = Integer.parseInt(timeIntStr);
		String[][] fileRows = FileStrIO.readXls(filePath, "A", colNum, 0);
		Map<String, String> colName = new HashMap<String, String>();//列名映射
		colName.put("DECATURSOYBEANMEAL", "迪凯特豆粕价格");
		colName.put("CHICAGONo. 2CORN", "芝加哥2号玉米期货");
		colName.put("Feed costsLiveweight Basis", "饲料成本");
		colName.put("Market Price", "市场价格");
		colName.put("Market Price -Feed costs", "市场价格减饲料成本");
		Map<String, String> en2cnMap = new HashMap<String, String>();//英-中文名映射
		en2cnMap.put("BROILERS", "美国禽肉-肉鸡饲料价格及饲料成本指数");
		en2cnMap.put("TURKEYS", "美国禽肉-火鸡饲料价格及饲料成本指数");
		en2cnMap.put("EGGS", "美国鸡蛋-饲料价格及饲料成本指数");
		if("Broiler, turkey, and egg feed costs and market prices".equals(fileRows[0][0])){
			String[] enColStr = {"", "", "", "", ""};
			for(int i = 1; i < 6; i++){
				for(int j = 2; j < 5; j++){
					if(fileRows[j][i] != null && !fileRows[j][i].isEmpty()){
						enColStr[i-1] = enColStr[i-1] + fileRows[j][i];
					}
				}
			}
			String monthNeed = numMonthMap.get(timeIntStr.substring(4));
			String yearNeed = timeIntStr.substring(0, 4);
			String enVarStr = null;//标识当前处理的哪个品种的数据
			for(int rowId = 1; rowId < fileRows.length; rowId++){
				if(en2cnMap.containsKey(fileRows[rowId][0])){
					enVarStr = en2cnMap.get(fileRows[rowId][0]);
				} else if(enVarStr != null && fileRows[rowId][0] != null && fileRows[rowId][0].contains(monthNeed) && fileRows[rowId][0].contains(yearNeed)) {
					//去提取相应时间的数据
					Map<String, String> dataMap = new HashMap<String, String>();
					for(int k = 0; k < 5; k++){
						if(colName.containsKey(enColStr[k]) && fileRows[rowId][k+1] != null && !fileRows[rowId][k+1].isEmpty()){
							dataMap.put(colName.get(enColStr[k]), fileRows[rowId][k+1]);
						}
					}
					String[] varCn = enVarStr.split("-");
					if(!dataMap.isEmpty()){
						dao.saveOrUpdateByDataMap(varCn[0], varCn[1], timeInt, dataMap);
						enVarStr = null;
					} else {
						RecordCrawlResult.recordFailData(className, varCn[0], varCn[1], "BTECOST.xls格式变化。");
					}
				}
			}
		} else {
			RecordCrawlResult.recordFailData(className, null, null, "数据为空");
		}
	}
	
	/**
	 * 解析ProductionIndicators.xls -- 蛋鸡生产指标、肉鸡生产指标、火鸡生产指标 -- 月度数据
	 * */
	public void parseProductionIndicators(String filePath, String timeIntStr){
		int colNum = 10;//读取列数
		int timeInt = Integer.parseInt(timeIntStr);
		String[][] fileRows = FileStrIO.readXls(filePath, "A", colNum, 0);
		if(fileRows[3][2].contains(timeIntStr.substring(0, 4)) && "PRODUCTIONINDICATORS".equals(fileRows[0][0].replaceAll("\\s", ""))){
			int columnNumToFetch = -1;//待抓取月份数据所在列号。
			for(int index = 2; index < fileRows[4].length; index++){
				if(fileRows[4][index] != null && fileRows[4][index].contains(numMonthMap.get(timeIntStr.substring(4)))){
					columnNumToFetch = index;
					break;
				}
			}
			if(columnNumToFetch != -1){
				Map<String, String> dataMap = new HashMap<String, String>();//存放表数据
				Map<String, String> colMap = null;//列名表
				String enVarStr = null;//品种在文件中对应的字符串 
				Map<String, String> broilersCol = new HashMap<String, String>();//Broilers列名表  英-中文映射
				broilersCol.put("Eggsinincubators(000)/1", "肉种蛋入孵量");
				broilersCol.put("Chickshatched(000)/2", "肉鸡雏孵化量");
				broilersCol.put("Hatchingegglayers(000)/1", "蛋种鸡数量");
				broilersCol.put("Pulletsplaced(000)", "鸡雏订购量");
				broilersCol.put("Hvy-typehenslaughter/2", "HVY型母鸡屠宰量");
				Map<String, String> turkeysCol = new HashMap<String, String>();//Turkeys列名表  英-中文映射
				turkeysCol.put("Eggsinincubators(000)/1", "肉种蛋入孵量");
				turkeysCol.put("Poultsplaced(000)", "鸡雏订购量");
				Map<String, String> eggsCol = new HashMap<String, String>();//Eggs列名表  英-中文映射
				eggsCol.put("Tableeggprod.(mil.doz.)/2", "食用鸡蛋产量");
				eggsCol.put("Tableegglayers,(000)/1", "商品蛋鸡存栏");
				eggsCol.put("Tableeggs/100layers/1", "产蛋率");
				eggsCol.put("Chickshatched(000)/2", "鸡雏产量");
				eggsCol.put("Lt.-typehenslaughter/2", "淘汰鸡屠宰量");
				Map<String, Map<String, String>> cnDataMap = new HashMap<String, Map<String, String>>();//中文名-列名表映射
				cnDataMap.put("Broilers:", broilersCol);
				cnDataMap.put("Turkeys:", turkeysCol);
				cnDataMap.put("Eggs:", eggsCol);
				Map<String, String> en2cnMap = new HashMap<String, String>();//英-中文名映射
				en2cnMap.put("Broilers:", "美国禽肉-肉鸡生产指标");
				en2cnMap.put("Turkeys:", "美国禽肉-火鸡生产指标");
				en2cnMap.put("Eggs:", "美国鸡蛋-蛋鸡生产指标");
				for(int rowIndex = 5; rowIndex < fileRows.length; rowIndex++){
					if(fileRows[rowIndex][0] != null && fileRows[rowIndex][0].contains(":")){
						if(!dataMap.isEmpty()){
							String[] varCn = en2cnMap.get(enVarStr).split("-");
							dao.saveOrUpdateByDataMap(varCn[0], varCn[1], timeInt, dataMap);
						}
						dataMap.clear();
						colMap = null;
						enVarStr = null;
						if(cnDataMap.containsKey(fileRows[rowIndex][0])){
							enVarStr = fileRows[rowIndex][0];
							colMap = cnDataMap.get(fileRows[rowIndex][0]);
						}
					} else if(colMap != null && fileRows[rowIndex][0] != null && colMap.containsKey(fileRows[rowIndex][0].replaceAll("\\s", ""))) {
						dataMap.put(colMap.get(fileRows[rowIndex][0].replaceAll("\\s", "")), fileRows[rowIndex][columnNumToFetch]);
					}
				}
			} else {
				RecordCrawlResult.recordFailData(className, null, null, "ProductionIndicators.xls中未找到新数据。");
			}
		} else {
			RecordCrawlResult.recordFailData(className, null, null, "ProductionIndicators.xls格式发生变动。");
		}
	}
	
	/**
	 * 解析WholesalePrices.xls
	 * @param filePath 文件保存路径
	 * @param timeIntStr 待解析数据时间序列
	 * */
	public boolean parseWholesalePrices(String filePath, String timeIntStr){
		boolean hasUpdate = false;
		String[][] fileRows = FileStrIO.readXls(filePath, "current");
		if(fileRows[2][2].contains(timeIntStr.substring(0, 4))){
			int columnNumToFetch = -1;//待抓取月份数据所在列号。
			for(int index = 3; index < fileRows[3].length; index++){
				if(fileRows[3][index].contains(numMonthMap.get(timeIntStr.substring(4)))){
					columnNumToFetch = index;
					break;
				}
			}
			if(columnNumToFetch != -1){
				hasUpdate = true;
				Map<String, String> eastTurkeyMap = new HashMap<String, String>();//东部地区火鸡批发价
				Map<String, String> broilerMap = new HashMap<String, String>();//肉鸡批发价
				Map<String, String> eastBroilerMeatMap = new HashMap<String, String>();//东北部地区肉鸡肉批发价
				Map<String, String> eastTurkeyMeatMap = new HashMap<String, String>();//东部地区火鸡肉批发价
				Map<String, String> eggsMap = new HashMap<String, String>();//鸡蛋批发价
				/*文件符合格式，开始提取所需数据*/
				for(int i = 4; i < fileRows.length; i++){
					if(fileRows[i][0].contains("12 City Avg.")){
						broilerMap.put("12个城市平均", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Georgia dock")) {
						broilerMap.put("乔治亚州码头", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Breast, boneless")) {
						eastBroilerMeatMap.put("去骨鸡胸肉", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Breast, Ribs on")) {
						eastBroilerMeatMap.put("带骨鸡胸肉", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Legs, whole")) {
						eastBroilerMeatMap.put("鸡全腿", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Leg quarters")) {
						eastBroilerMeatMap.put("1/4鸡腿", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Toms, 16-24 lb")) {
						eastTurkeyMap.put("汤姆斯火鸡16至24磅", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Hens, 8-16 lb")) {
						eastTurkeyMap.put("母鸡8至16磅", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Breast, 4-8 lb")) {
						eastTurkeyMeatMap.put("鸡胸肉4至8磅", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Drumsticks")) {
						eastTurkeyMeatMap.put("鸡腿下段", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("Wings, full cut")) {
						eastTurkeyMeatMap.put("分割全鸡翅", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("12 City Metro")) {
						eggsMap.put("12个大城市", fileRows[i][columnNumToFetch]);
					} else if(fileRows[i][0].contains("New York")) {
						eggsMap.put("纽约", fileRows[i][columnNumToFetch]);
					}
				}
				int timeInt = Integer.parseInt(timeIntStr);
				if(!eastTurkeyMap.isEmpty()){
					dao.saveOrUpdateByDataMap("美国禽肉", "东部地区火鸡批发价", timeInt, eastTurkeyMap);
				} else {
					RecordCrawlResult.recordFailData(AmericanPoultryMeat.className, "美国禽肉", "东部地区火鸡批发价", "数据为空。");
				}
				if(!broilerMap.isEmpty()){
					dao.saveOrUpdateByDataMap("美国禽肉", "肉鸡批发价", timeInt, broilerMap);
				} else {
					RecordCrawlResult.recordFailData(AmericanPoultryMeat.className, "美国禽肉", "肉鸡批发价", "数据为空。");
				}
				if(!eastBroilerMeatMap.isEmpty()){
					dao.saveOrUpdateByDataMap("美国禽肉", "东北部地区肉鸡肉批发价", timeInt, eastBroilerMeatMap);
				} else {
					RecordCrawlResult.recordFailData(AmericanPoultryMeat.className, "美国禽肉", "东北部地区肉鸡肉批发价", "数据为空。");
				}
				if(!eastTurkeyMeatMap.isEmpty()){
					dao.saveOrUpdateByDataMap("美国禽肉", "东部地区火鸡肉批发价", timeInt, eastTurkeyMeatMap);
				} else {
					RecordCrawlResult.recordFailData(AmericanPoultryMeat.className, "美国禽肉", "东部地区火鸡肉批发价", "数据为空。");
				}
				if(!eggsMap.isEmpty()){
					dao.saveOrUpdateByDataMap("美国鸡蛋", "鸡蛋批发价", timeInt, eggsMap);
				} else {
					RecordCrawlResult.recordFailData(AmericanPoultryMeat.className, "美国鸡蛋", "鸡蛋批发价", "数据为空。");
				}
			} else {
				RecordCrawlResult.recordFailData(AmericanPoultryMeat.className, null, null, "WholesalePrices.xls中未找到新数据。");
			}
		}
		return hasUpdate;
	}
	
	public static void main(String[] args) {
		AmericanPoultryMeat apm = new AmericanPoultryMeat();
		apm.start();
//		apm.fetchData(DateTimeUtil.parseDateTime("20160701", DateTimeUtil.YYYYMMDD));
		
	}
}
