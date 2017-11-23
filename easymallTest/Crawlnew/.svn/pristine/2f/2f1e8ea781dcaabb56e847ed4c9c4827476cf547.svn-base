package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 全球大宗商品价格月度数据
 * @author bric_yangyulin
 * @date 2016-10-13
 * */
public class GlobalCommodityPricesMonth {
	private static String className = GlobalCommodityPricesMonth.class.getName();
	private static String url = "http://www.worldbank.org/en/research/commodity-markets";
	private static final String encoding = "utf-8";
	private static final String varName = "全球大宗商品价格";
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(GlobalCommodityPricesMonth.class);
	private static Map<String, String> colNameByMarkForIndices = new HashMap<String, String>();//indices工作表中标记字符串-列名
	private static Map<String, Map<String, String>> colsByCnName = new HashMap<String, Map<String, String>>();//中文名-列名表 （price工作表）
	private static Map<String, String> colNameByMarkGrain = new HashMap<String, String>();//谷物及油脂油料价格（月） 列名映射
	private static Map<String, String> colNameByMarkCrude = new HashMap<String, String>();//原油价格（月） 列名映射
	private static Map<String, String> colNameByMarkCoal = new HashMap<String, String>();//煤炭价格（月） 列名映射
	private static Map<String, String> colNameByMarkGas = new HashMap<String, String>();//天然气价格（月） 列名映射
	private static Map<String, String> colNameByMarkChemicalMetal = new HashMap<String, String>();//化工金属价格（月） 列名映射
	private static Map<String, String> colNameByMarkCotton = new HashMap<String, String>();//棉糖等软商品价格（月） 列名映射
	private static Map<String, String> colNameByMarkTobacco = new HashMap<String, String>();//烟草木材及制品价格（月） 列名映射
	private static Map<String, String> colNameByMarkNobleMetals = new HashMap<String, String>();//贵金属价格（月） 列名映射
	static {
		colNameByMarkForIndices.put("iENERGY", "能源价格指数");
		colNameByMarkForIndices.put("iNONFUEL", "非能源价格指数");
		colNameByMarkForIndices.put("iAGRICULTURE", "农产品价格指数");
		colNameByMarkForIndices.put("iBEVERAGES", "饮料价格指数");
		colNameByMarkForIndices.put("iFOOD", "食物价格指数");
		colNameByMarkForIndices.put("iFATS_OILS", "油粕价格指数");
		colNameByMarkForIndices.put("iGRAINS", "谷物价格指数");
		colNameByMarkForIndices.put("iOTHERFOOD", "其他食物价格指数");
		colNameByMarkForIndices.put("iRAW_MATERIAL", "原料价格指数");
		colNameByMarkForIndices.put("iTIMBER", "木材价格指数");
		colNameByMarkForIndices.put("iOTHERRAWMAT", "其他原料价格指数");
		colNameByMarkForIndices.put("iFERTILIZERS", "化肥价格指数");
		colNameByMarkForIndices.put("iMETMIN", "金属及矿产价格指数");
		colNameByMarkForIndices.put("iBASEMET", "基础金属价格指数");
		colNameByMarkForIndices.put("iPRECIOUSMET", "贵金属价格指数");
		
		colsByCnName.put("谷物及油脂油料价格（月）", colNameByMarkGrain);
		colsByCnName.put("原油价格（月）", colNameByMarkCrude);
		colsByCnName.put("煤炭价格（月）", colNameByMarkCoal);
		colsByCnName.put("天然气价格（月）", colNameByMarkGas);
		colsByCnName.put("化工金属价格（月）", colNameByMarkChemicalMetal);
		colsByCnName.put("棉糖等软商品价格（月）", colNameByMarkCotton);
		colsByCnName.put("烟草木材及制品价格（月）", colNameByMarkTobacco);
		colsByCnName.put("贵金属价格（月）", colNameByMarkNobleMetals);
		
		colNameByMarkGrain.put("COCONUT_OIL", "椰子油");
		colNameByMarkGrain.put("COPRA", "椰肉");
		colNameByMarkGrain.put("GRNUT", "花生");
		colNameByMarkGrain.put("FISH_MEAL", "鱼粉");
		colNameByMarkGrain.put("GRNUT_OIL", "花生油");
		colNameByMarkGrain.put("PALM_OIL", "棕榈油");
		colNameByMarkGrain.put("PLMKRNL_OIL", "棕榈仁油");
		colNameByMarkGrain.put("SOYBEANS", "大豆");
		colNameByMarkGrain.put("SOYBEAN_OIL", "豆油");
		colNameByMarkGrain.put("SOYBEAN_MEAL", "豆粕");
		colNameByMarkGrain.put("RAPESEED_OIL", "菜籽油");
		colNameByMarkGrain.put("SUNFLOWER_OIL", "葵花籽油");
		colNameByMarkGrain.put("BARLEY", "大麦");
		colNameByMarkGrain.put("MAIZE", "玉米");
		colNameByMarkGrain.put("SORGHUM", "高粱");
		colNameByMarkGrain.put("RICE_05", "泰国大米（碎米率5%）");
		colNameByMarkGrain.put("RICE_25", "泰国大米（碎米率25%）");
		colNameByMarkGrain.put("RICE_A1", "泰国大米（碎米率100%）");
		colNameByMarkGrain.put("RICE_05_VNM", "越南大米（碎米率5％）");
		colNameByMarkGrain.put("WHEAT_US_SRW", "小麦（美国软红冬麦）");
		colNameByMarkGrain.put("WHEAT_US_HRW", "小麦（美国硬红冬小麦）");
		
		colNameByMarkCrude.put("CRUDE_PETRO", "原油（平均）");
		colNameByMarkCrude.put("CRUDE_BRENT", "原油（布伦特）");
		colNameByMarkCrude.put("CRUDE_DUBAI", "原油（迪拜）");
		colNameByMarkCrude.put("CRUDE_WTI", "西德克萨斯轻质原油");
		
		colNameByMarkCoal.put("COAL_AUS", "煤炭（澳大利亚）");
		colNameByMarkCoal.put("COAL_COL", "煤炭（哥伦比亚）");
		colNameByMarkCoal.put("COAL_SAFRICA", "煤炭（南非）");
		
		colNameByMarkGas.put("NGAS_US", "天然气（美国）");
		colNameByMarkGas.put("NGAS_EUR", "天然气（欧洲）");
		colNameByMarkGas.put("NGAS_JP", "液化天然气（日本）");
		colNameByMarkGas.put("iNATGAS", "天然气指数");
		
		colNameByMarkChemicalMetal.put("PHOSROCK", "磷矿石");
		colNameByMarkChemicalMetal.put("DAP", "磷酸二铵（DAP）");
		colNameByMarkChemicalMetal.put("TSP", "重过磷酸钙（TSP）");
		colNameByMarkChemicalMetal.put("UREA_EE_BULK", "尿素");
		colNameByMarkChemicalMetal.put("POTASH", "氯化钾");
		colNameByMarkChemicalMetal.put("ALUMINUM", "铝");
		colNameByMarkChemicalMetal.put("IRON_ORE", "铁矿石（CFR现货)");
		colNameByMarkChemicalMetal.put("COPPER", "铜");
		colNameByMarkChemicalMetal.put("LEAD", "铅");
		colNameByMarkChemicalMetal.put("Tin", "锡");
		colNameByMarkChemicalMetal.put("NICKEL", "镍");
		colNameByMarkChemicalMetal.put("Zinc", "锌");
		
		colNameByMarkCotton.put("COCOA", "可可");
		colNameByMarkCotton.put("COFFEE_ARABIC", "咖啡（阿拉比卡）");
		colNameByMarkCotton.put("COFFEE_ROBUS", "咖啡（罗布斯塔）");
		colNameByMarkCotton.put("TEA_AVG", "茶（平均拍卖价）");
		colNameByMarkCotton.put("TEA_COLOMBO", "茶（科伦坡）");
		colNameByMarkCotton.put("TEA_KOLKATA", "茶（加尔各答）");
		colNameByMarkCotton.put("TEA_MOMBASA", "茶（蒙巴萨）");
		colNameByMarkCotton.put("BANANA_EU", "香蕉（欧洲）");
		colNameByMarkCotton.put("BANANA_US", "香蕉（美国）");
		colNameByMarkCotton.put("ORANGE", "橙子");
		colNameByMarkCotton.put("BEEF", "牛肉");
		colNameByMarkCotton.put("CHICKEN", "鸡肉");
		colNameByMarkCotton.put("LAMB", "羊肉");
		colNameByMarkCotton.put("SHRIMP_MEX", "虾（墨西哥）");
		colNameByMarkCotton.put("SUGAR_EU", "糖（欧盟）");
		colNameByMarkCotton.put("SUGAR_US", "糖（美国）");
		colNameByMarkCotton.put("SUGAR_WLD", "糖（世界）");
		colNameByMarkCotton.put("COTTON_A_INDX", "棉花（CotlookA）");
		colNameByMarkCotton.put("RUBBER_TSR20", "橡胶（TSR20）");
		colNameByMarkCotton.put("RUBBER1_MYSG", "橡胶（SGP_MYS）");
		
		colNameByMarkTobacco.put("TOBAC_US", "未加工烟草（美国进口CIF价格）");
		colNameByMarkTobacco.put("LOGS_CMR", "原木（喀麦隆）");
		colNameByMarkTobacco.put("LOGS_MYS", "原木（马来西亚）");
		colNameByMarkTobacco.put("SAWNWD_CMR", "锯木（喀麦隆）");
		colNameByMarkTobacco.put("SAWNWD_MYS", "锯木（马来西亚）");
		colNameByMarkTobacco.put("PLYWOOD", "合板");
		colNameByMarkTobacco.put("WOODPULP", "木浆");
		
		colNameByMarkNobleMetals.put("GOLD", "金");
		colNameByMarkNobleMetals.put("PLATINUM", "铂");
		colNameByMarkNobleMetals.put("SILVER", "银");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_GLOBALCOMMODITYPRICESMONTH)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("全球大宗商品价格月度数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到全球大宗商品价格月度数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, varName, null, "没有获取到全球大宗商品价格月度数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					String fileUrl = fetchFileUrl();
					String filePath = downFile(fileUrl);
					parseFileIndices(filePath, 16, 9, 2);
					parseFilePrices(filePath, 86, 6, 2);
				} catch(Exception e) {
					LOG.info("发生未知错误", e);
					RecordCrawlResult.recordFailData(className, varName, null, "发生未知错误");
				}
			}else{
				LOG.info("抓取全球大宗商品价格月度数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, varName, null, "抓取全球大宗商品价格月度数据的定时器已关闭");
			}
		}
	}
	
	/**
	 * 获取数据文件url
	 * */
	public String fetchFileUrl(){
		String fileUrl = null;
		String[] filters = {"div", "class", "tab-content"};
		String div = fetchUtil.getPrimaryContent(0, url, encoding, varName, filters, null, 0);
		System.out.println(div);
		String fileUrlRegex = "<a href=\"([^\"]+)\">Monthly prices</a>";
		List<String> urlList = RegexUtil.getMatchStr(div, fileUrlRegex, new int[]{1});
		if(urlList != null && urlList.size() == 1){
			fileUrl = urlList.get(0);
		} else {
			LOG.warn("未找到文件链接。");
			RecordCrawlResult.recordFailData(className, varName, null, "未找到文件链接");
		}
		return fileUrl;
	}
	
	/**
	 * 下载文件
	 * */
	public String downFile(String fileUrl){
		String timestamp = DateTimeUtil.formatDate(new Date(), DateTimeUtil.YYYYMMDD);
		String filePath = Constants.DATABAK_TXT + timestamp + Constants.FILE_SEPARATOR + fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
		MyHttpClient.fetchAndSave(fileUrl, filePath);
		return filePath;
	}
	
	/**
	 * 解析工作表Monthly Prices
	 * */
	public void parseFilePrices(String filePath, int maxColumns, int markRowId, int num){
		LOG.info("---------开始处理价格数据---------");
		String[][] table = FileStrIO.readXls(filePath, "Monthly Prices", maxColumns);
		String timeRegex = "(\\d{4})M(\\d{2})";//匹配此文件中的时间字符串
		String priceRegex = "\\d+(.\\d+)?";//匹配价格
		int count = 0;//计数（处理了最近几条数据）
		for(int rowId = table.length - 1; rowId >= 0; rowId--){
			if(table[rowId][0] != null && table[rowId][0].matches(timeRegex)){
				//匹配到最新一条数据，提取，入库，跳出
				String timeIntStr = table[rowId][0].replace("M", "");
				int timeInt = Integer.parseInt(timeIntStr);
				for(String cnName: colsByCnName.keySet()){//针对每张表分别遍历这条数据取出相应字段并存储
					LOG.info("处理cnName=" + cnName + ",timeInt=" + timeInt);
					Map<String, String> cnByEn = colsByCnName.get(cnName);//列名映射
					Map<String, String> dataMap = new HashMap<String, String>();
					for(int colId = 1; colId < table[markRowId].length; colId++){
						if(cnByEn.containsKey(table[markRowId][colId]) 
								&& table[rowId][colId] != null && table[rowId][colId].matches(priceRegex)){
							dataMap.put(cnByEn.get(table[markRowId][colId]), table[rowId][colId]);
						}
					}
					if(dataMap.size() > 0){
						//有数据，保存
						dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
					} else {
						//数据未获取，报警
						LOG.warn("获取数据为空");
						RecordCrawlResult.recordFailData(className, varName, cnName, "获取数据为空");
					}
				}
				count++;
				if(count >= num){
					break;
				}
			}
		}
	}
	
	/**
	 * 解析工作表Monthly Indices（只取最新两条数据）
	 * @param filePath 文件存储路径
	 * @param maxColumns 读取列数
	 * @param markRowId 标记行的id（标记行即对应列名的那一行）
	 * @param num 处理的最近数据的条数
	 * */
	public void parseFileIndices(String filePath, int maxColumns, int markRowId, int num){
		LOG.info("---------开始处理指数数据---------");
		String cnName = "大宗品类价格指数（月）";
		String[][] table = FileStrIO.readXls(filePath, "Monthly Indices", maxColumns);
		String timeRegex = "(\\d{4})M(\\d{2})";//匹配此文件中的时间字符串
		String priceRegex = "\\d+(.\\d+)?";//匹配价格
		int count = 0;//计数（处理了最近几条数据）
		for(int rowId = table.length - 1; rowId >= 0; rowId--){
			if(table[rowId][0] != null && table[rowId][0].matches(timeRegex)){
				//匹配到最新一条数据，提取，入库，跳出
				String timeIntStr = table[rowId][0].replace("M", "");
				int timeInt = Integer.parseInt(timeIntStr);
				Map<String, String> dataMap = new HashMap<String, String>();
				for(int colId = 1; colId < table[markRowId].length; colId++){
					if(table[rowId][colId] != null && table[rowId][colId].matches(priceRegex) 
							&& colNameByMarkForIndices.containsKey(table[markRowId][colId])){
						dataMap.put(colNameByMarkForIndices.get(table[markRowId][colId]), table[rowId][colId]);
					}
				}
				if(dataMap.size() == colNameByMarkForIndices.size()){
					//数据完整，保存
					dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
				} else if(dataMap.size() > 0) {
					//数据不完整，报警，保存
					LOG.warn("数据不完整");
					dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
					RecordCrawlResult.recordFailData(className, varName, cnName, "获取的数据不完整");
				} else {
					//数据未获取，报警
					LOG.warn("获取数据为空");
					RecordCrawlResult.recordFailData(className, varName, cnName, "获取数据为空");
				}
				count++;
				if(count >= num){
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		GlobalCommodityPricesMonth global = new GlobalCommodityPricesMonth();
		global.start();
//		global.parseFileIndices("D:\\Test\\CMO-Historical-Data-Monthly.xlsx", 16, 9, 2);
//		global.parseFilePrices("D:\\Test\\CMO-Historical-Data-Monthly.xlsx", 86, 6, 2);
	}
}
