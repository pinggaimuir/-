package cn.futures.data.importor.crawler;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.*;

/**
 * 国外期货数据：芝加哥商品交易所、纽约商品交易所、ICE交易所数据
 * */
public class CMEDataFetch {
	
	private static final String className = CMEDataFetch.class.getName();
	private static final Logger LOG = Logger.getLogger(CMEDataFetch.class);
	private MyHttpClient myHttpClient = new MyHttpClient();
	private DAOUtils dao = new DAOUtils();
	private static final String encoding = "utf-8";
	//基础url	（这个quandl网站提供的json格式接口）
	private static final String baseUrl = "https://www.quandl.com/api/v3/datasets/#dataset_code.json?api_key=RrVfiYnoFaCvWiMCVwji&start_date=#start_date&end_date=#end_date&order=desc";
	private static Map<String, String> mapVarCode = new LinkedHashMap<String, String>();//品种-cnName 与 Quandl code的映射
	static {
		mapVarCode.put("芝加哥美豆-1号连续", "CHRIS/CME_S1");
		mapVarCode.put("芝加哥美豆-2号连续", "CHRIS/CME_S2");
		mapVarCode.put("芝加哥美豆-3号连续", "CHRIS/CME_S3");
		mapVarCode.put("芝加哥美豆-4号连续", "CHRIS/CME_S4");
		mapVarCode.put("芝加哥美豆-5号连续", "CHRIS/CME_S5");
		mapVarCode.put("芝加哥美豆-6号连续", "CHRIS/CME_S6");
		mapVarCode.put("芝加哥美豆-7号连续", "CHRIS/CME_S7");
		mapVarCode.put("芝加哥美豆-8号连续", "CHRIS/CME_S8");
		mapVarCode.put("芝加哥美豆-9号连续", "CHRIS/CME_S9");
		mapVarCode.put("芝加哥美豆-10号连续", "CHRIS/CME_S10");
		
		mapVarCode.put("芝加哥美豆粕-1号连续", "CHRIS/CME_SM1");
		mapVarCode.put("芝加哥美豆粕-2号连续", "CHRIS/CME_SM2");
		mapVarCode.put("芝加哥美豆粕-3号连续", "CHRIS/CME_SM3");
		mapVarCode.put("芝加哥美豆粕-4号连续", "CHRIS/CME_SM4");
		mapVarCode.put("芝加哥美豆粕-5号连续", "CHRIS/CME_SM5");
		mapVarCode.put("芝加哥美豆粕-6号连续", "CHRIS/CME_SM6");
		mapVarCode.put("芝加哥美豆粕-7号连续", "CHRIS/CME_SM7");
		mapVarCode.put("芝加哥美豆粕-8号连续", "CHRIS/CME_SM8");
		mapVarCode.put("芝加哥美豆粕-9号连续", "CHRIS/CME_SM9");
		mapVarCode.put("芝加哥美豆粕-10号连续", "CHRIS/CME_SM10");
		mapVarCode.put("芝加哥美豆粕-11号连续", "CHRIS/CME_SM11");
		
		mapVarCode.put("芝加哥美豆油-1号连续", "CHRIS/CME_BO1");
		mapVarCode.put("芝加哥美豆油-2号连续", "CHRIS/CME_BO2");
		mapVarCode.put("芝加哥美豆油-3号连续", "CHRIS/CME_BO3");
		mapVarCode.put("芝加哥美豆油-4号连续", "CHRIS/CME_BO4");
		mapVarCode.put("芝加哥美豆油-5号连续", "CHRIS/CME_BO5");
		mapVarCode.put("芝加哥美豆油-6号连续", "CHRIS/CME_BO6");
		mapVarCode.put("芝加哥美豆油-7号连续", "CHRIS/CME_BO7");
		mapVarCode.put("芝加哥美豆油-8号连续", "CHRIS/CME_BO8");
		mapVarCode.put("芝加哥美豆油-9号连续", "CHRIS/CME_BO9");
		mapVarCode.put("芝加哥美豆油-10号连续", "CHRIS/CME_BO10");
		
		mapVarCode.put("芝加哥美玉米-1号连续", "CHRIS/CME_C1");
		mapVarCode.put("芝加哥美玉米-2号连续", "CHRIS/CME_C2");
		mapVarCode.put("芝加哥美玉米-3号连续", "CHRIS/CME_C3");
		mapVarCode.put("芝加哥美玉米-4号连续", "CHRIS/CME_C4");
		mapVarCode.put("芝加哥美玉米-5号连续", "CHRIS/CME_C5");
		mapVarCode.put("芝加哥美玉米-6号连续", "CHRIS/CME_C6");
		mapVarCode.put("芝加哥美玉米-7号连续", "CHRIS/CME_C7");
		mapVarCode.put("芝加哥美玉米-8号连续", "CHRIS/CME_C8");
		mapVarCode.put("芝加哥美玉米-9号连续", "CHRIS/CME_C9");
		mapVarCode.put("芝加哥美玉米-10号连续", "CHRIS/CME_C10");
		
		mapVarCode.put("芝加哥美麦-1号连续", "CHRIS/CME_W1");
		mapVarCode.put("芝加哥美麦-2号连续", "CHRIS/CME_W2");
		mapVarCode.put("芝加哥美麦-3号连续", "CHRIS/CME_W3");
		mapVarCode.put("芝加哥美麦-4号连续", "CHRIS/CME_W4");
		mapVarCode.put("芝加哥美麦-5号连续", "CHRIS/CME_W5");
		mapVarCode.put("芝加哥美麦-6号连续", "CHRIS/CME_W6");
		mapVarCode.put("芝加哥美麦-7号连续", "CHRIS/CME_W7");
		

		mapVarCode.put("芝加哥美稻米-1号连续", "CHRIS/CME_RR1");
		mapVarCode.put("芝加哥美稻米-2号连续", "CHRIS/CME_RR2");
		mapVarCode.put("芝加哥美稻米-3号连续", "CHRIS/CME_RR3");
		
		mapVarCode.put("澳大利亚高粱-1号连续", "CHRIS/ASX_US1");
		
		mapVarCode.put("澳大利亚饲用大麦-1号连续", "CHRIS/ASX_UB1");
		
		mapVarCode.put("育肥牛期货-1号连续", "CHRIS/CME_FC1");
		mapVarCode.put("育肥牛期货-2号连续", "CHRIS/CME_FC2");
		mapVarCode.put("育肥牛期货-3号连续", "CHRIS/CME_FC3");
		mapVarCode.put("育肥牛期货-4号连续", "CHRIS/CME_FC4");
		mapVarCode.put("育肥牛期货-5号连续", "CHRIS/CME_FC5");
		mapVarCode.put("育肥牛期货-6号连续", "CHRIS/CME_FC6");
		mapVarCode.put("育肥牛期货-7号连续", "CHRIS/CME_FC7");
		
		mapVarCode.put("活牛期货-1号连续", "CHRIS/CME_LC1");
		mapVarCode.put("活牛期货-2号连续", "CHRIS/CME_LC2");
		mapVarCode.put("活牛期货-3号连续", "CHRIS/CME_LC3");
		mapVarCode.put("活牛期货-4号连续", "CHRIS/CME_LC4");
		mapVarCode.put("活牛期货-5号连续", "CHRIS/CME_LC5");
		mapVarCode.put("活牛期货-6号连续", "CHRIS/CME_LC6");
		mapVarCode.put("活牛期货-7号连续", "CHRIS/CME_LC7");
		
		mapVarCode.put("瘦肉猪期货-1号连续", "CHRIS/CME_LN1");
		mapVarCode.put("瘦肉猪期货-2号连续", "CHRIS/CME_LN2");
		mapVarCode.put("瘦肉猪期货-3号连续", "CHRIS/CME_LN3");
		mapVarCode.put("瘦肉猪期货-4号连续", "CHRIS/CME_LN4");
		mapVarCode.put("瘦肉猪期货-5号连续", "CHRIS/CME_LN5");
		mapVarCode.put("瘦肉猪期货-6号连续", "CHRIS/CME_LN6");
		mapVarCode.put("瘦肉猪期货-7号连续", "CHRIS/CME_LN7");
		mapVarCode.put("瘦肉猪期货-8号连续", "CHRIS/CME_LN8");
		mapVarCode.put("瘦肉猪期货-9号连续", "CHRIS/CME_LN9");
		mapVarCode.put("瘦肉猪期货-10号连续", "CHRIS/CME_LN10");
		
		mapVarCode.put("三级牛奶期货-1号连续", "CHRIS/CME_DA1");
		mapVarCode.put("三级牛奶期货-2号连续", "CHRIS/CME_DA2");
		mapVarCode.put("三级牛奶期货-3号连续", "CHRIS/CME_DA3");
		mapVarCode.put("三级牛奶期货-4号连续", "CHRIS/CME_DA4");
		mapVarCode.put("三级牛奶期货-5号连续", "CHRIS/CME_DA5");
		mapVarCode.put("三级牛奶期货-6号连续", "CHRIS/CME_DA6");
		mapVarCode.put("三级牛奶期货-7号连续", "CHRIS/CME_DA7");
		mapVarCode.put("三级牛奶期货-8号连续", "CHRIS/CME_DA8");
		mapVarCode.put("三级牛奶期货-9号连续", "CHRIS/CME_DA9");
		mapVarCode.put("三级牛奶期货-10号连续", "CHRIS/CME_DA10");
		
		mapVarCode.put("乙醇期货-1号连续", "CHRIS/CME_EH1");
		mapVarCode.put("乙醇期货-2号连续", "CHRIS/CME_EH2");
		mapVarCode.put("乙醇期货-3号连续", "CHRIS/CME_EH3");
		mapVarCode.put("乙醇期货-4号连续", "CHRIS/CME_EH4");
		
		/*纽约商品交易所*///未补完，字段不一，暂时无法批量存储
		mapVarCode.put("乙醇（普氏）期货-1号连续", "CHRIS/CME_CU1");
		mapVarCode.put("乙醇（普氏）期货-2号连续", "CHRIS/CME_CU2");
		mapVarCode.put("乙醇（普氏）期货-3号连续", "CHRIS/CME_CU3");
		mapVarCode.put("乙醇（普氏）期货-4号连续", "CHRIS/CME_CU4");
		mapVarCode.put("乙醇（普氏）期货-5号连续", "CHRIS/CME_CU5");
		mapVarCode.put("乙醇（普氏）期货-6号连续", "CHRIS/CME_CU6");
		mapVarCode.put("乙醇（普氏）期货-7号连续", "CHRIS/CME_CU7");
		mapVarCode.put("乙醇（普氏）期货-8号连续", "CHRIS/CME_CU8");
		mapVarCode.put("乙醇（普氏）期货-9号连续", "CHRIS/CME_CU9");
		mapVarCode.put("乙醇（普氏）期货-10号连续", "CHRIS/CME_CU10");
		mapVarCode.put("乙醇（普氏）期货-11号连续", "CHRIS/CME_CU11");
		mapVarCode.put("乙醇（普氏）期货-12号连续", "CHRIS/CME_CU12");
		mapVarCode.put("乙醇（普氏）期货-13号连续", "CHRIS/CME_CU13");
		
		/*纽约商品交易所*/
		mapVarCode.put("黄金期货-1号连续", "CHRIS/CME_GC1");
		mapVarCode.put("黄金期货-2号连续", "CHRIS/CME_GC2");
		mapVarCode.put("黄金期货-3号连续", "CHRIS/CME_GC3");
		mapVarCode.put("黄金期货-4号连续", "CHRIS/CME_GC4");
		mapVarCode.put("黄金期货-5号连续", "CHRIS/CME_GC5");
		mapVarCode.put("黄金期货-6号连续", "CHRIS/CME_GC6");
		mapVarCode.put("黄金期货-7号连续", "CHRIS/CME_GC7");
		mapVarCode.put("黄金期货-8号连续", "CHRIS/CME_GC8");
		mapVarCode.put("黄金期货-9号连续", "CHRIS/CME_GC9");
		mapVarCode.put("黄金期货-10号连续", "CHRIS/CME_GC10");
		mapVarCode.put("黄金期货-11号连续", "CHRIS/CME_GC11");
		mapVarCode.put("黄金期货-12号连续", "CHRIS/CME_GC12");
		mapVarCode.put("黄金期货-13号连续", "CHRIS/CME_GC13");
		
		/*纽约商品交易所*/
		mapVarCode.put("纽约原油-1号连续", "CHRIS/CME_CL1");
		mapVarCode.put("纽约原油-2号连续", "CHRIS/CME_CL2");
		mapVarCode.put("纽约原油-3号连续", "CHRIS/CME_CL3");
		mapVarCode.put("纽约原油-4号连续", "CHRIS/CME_CL4");
		mapVarCode.put("纽约原油-5号连续", "CHRIS/CME_CL5");
		mapVarCode.put("纽约原油-6号连续", "CHRIS/CME_CL6");
		mapVarCode.put("纽约原油-7号连续", "CHRIS/CME_CL7");
		mapVarCode.put("纽约原油-8号连续", "CHRIS/CME_CL8");
		mapVarCode.put("纽约原油-9号连续", "CHRIS/CME_CL9");
		mapVarCode.put("纽约原油-10号连续", "CHRIS/CME_CL10");
		mapVarCode.put("纽约原油-11号连续", "CHRIS/CME_CL11");
		mapVarCode.put("纽约原油-12号连续", "CHRIS/CME_CL12");
		mapVarCode.put("纽约原油-13号连续", "CHRIS/CME_CL13");
		mapVarCode.put("纽约原油-14号连续", "CHRIS/CME_CL14");
		mapVarCode.put("纽约原油-15号连续", "CHRIS/CME_CL15");
		mapVarCode.put("纽约原油-16号连续", "CHRIS/CME_CL16");
		mapVarCode.put("纽约原油-17号连续", "CHRIS/CME_CL17");
		mapVarCode.put("纽约原油-18号连续", "CHRIS/CME_CL18");
		mapVarCode.put("纽约原油-19号连续", "CHRIS/CME_CL19");
		mapVarCode.put("纽约原油-20号连续", "CHRIS/CME_CL20");
		mapVarCode.put("纽约原油-21号连续", "CHRIS/CME_CL21");
		mapVarCode.put("纽约原油-22号连续", "CHRIS/CME_CL22");
		mapVarCode.put("纽约原油-23号连续", "CHRIS/CME_CL23");
		mapVarCode.put("纽约原油-24号连续", "CHRIS/CME_CL24");
		
		/*ICE交易所*///只补了一号的，第一天的会有问题。
		mapVarCode.put("布伦特原油-1号连续", "CHRIS/ICE_B1");
		mapVarCode.put("布伦特原油-2号连续", "CHRIS/ICE_B2");
		mapVarCode.put("布伦特原油-3号连续", "CHRIS/ICE_B3");
		mapVarCode.put("布伦特原油-4号连续", "CHRIS/ICE_B4");
		mapVarCode.put("布伦特原油-5号连续", "CHRIS/ICE_B5");
		mapVarCode.put("布伦特原油-6号连续", "CHRIS/ICE_B6");
		mapVarCode.put("布伦特原油-7号连续", "CHRIS/ICE_B7");
		mapVarCode.put("布伦特原油-8号连续", "CHRIS/ICE_B8");
		mapVarCode.put("布伦特原油-9号连续", "CHRIS/ICE_B9");
		mapVarCode.put("布伦特原油-10号连续", "CHRIS/ICE_B10");
		mapVarCode.put("布伦特原油-11号连续", "CHRIS/ICE_B11");
		mapVarCode.put("布伦特原油-12号连续", "CHRIS/ICE_B12");
		mapVarCode.put("布伦特原油-13号连续", "CHRIS/ICE_B13");
		mapVarCode.put("布伦特原油-14号连续", "CHRIS/ICE_B14");
		mapVarCode.put("布伦特原油-15号连续", "CHRIS/ICE_B15");
		mapVarCode.put("布伦特原油-16号连续", "CHRIS/ICE_B16");
		mapVarCode.put("布伦特原油-17号连续", "CHRIS/ICE_B17");
		mapVarCode.put("布伦特原油-18号连续", "CHRIS/ICE_B18");
		mapVarCode.put("布伦特原油-19号连续", "CHRIS/ICE_B19");
		mapVarCode.put("布伦特原油-20号连续", "CHRIS/ICE_B20");
		mapVarCode.put("布伦特原油-21号连续", "CHRIS/ICE_B21");
		mapVarCode.put("布伦特原油-22号连续", "CHRIS/ICE_B22");
		mapVarCode.put("布伦特原油-23号连续", "CHRIS/ICE_B23");
		mapVarCode.put("布伦特原油-24号连续", "CHRIS/ICE_B24");
		
		/*ICE交易所*/
		mapVarCode.put("美元指数期货-1号连续", "CHRIS/ICE_DX1");
		mapVarCode.put("美元指数期货-2号连续", "CHRIS/ICE_DX2");
		
		/*ICE交易所*/
		mapVarCode.put("ICE原糖-1号连续", "CHRIS/ICE_SB1");
		mapVarCode.put("ICE原糖-2号连续", "CHRIS/ICE_SB2");
		mapVarCode.put("ICE原糖-3号连续", "CHRIS/ICE_SB3");
		mapVarCode.put("ICE原糖-4号连续", "CHRIS/ICE_SB4");
		mapVarCode.put("ICE原糖-5号连续", "CHRIS/ICE_SB5");
		mapVarCode.put("ICE原糖-6号连续", "CHRIS/ICE_SB6");
		
		/*ICE交易所*/
		mapVarCode.put("ICE棉花-1号连续", "CHRIS/ICE_CT1");
		mapVarCode.put("ICE棉花-2号连续", "CHRIS/ICE_CT2");
		mapVarCode.put("ICE棉花-3号连续", "CHRIS/ICE_CT3");
		mapVarCode.put("ICE棉花-4号连续", "CHRIS/ICE_CT4");
		mapVarCode.put("ICE棉花-5号连续", "CHRIS/ICE_CT5");
		mapVarCode.put("ICE棉花-6号连续", "CHRIS/ICE_CT6");
		
	}
	
	/**
	 * 定时抓取前一天的数据
	 * */
	@Scheduled(cron = CrawlScheduler.CRON_CMEData)
	public void start(){
		try{
			String switchFlag = new CrawlerManager().selectCrawler("国外期货数据", className.substring(className.lastIndexOf(".")+1));
			if(switchFlag == null){
				LOG.info("没有获取到芝加哥交易所期货数据爬虫的定时器配置");
				RecordCrawlResult.recordFailData(className, null, null, "没有获取到芝加哥交易所期货数据爬虫的定时器配置");
			}else{
				if(switchFlag.equals("1")){
					Date today = new Date();
					Date yestoday = DateTimeUtil.addDay(today, -1);
					String timeStr = DateTimeUtil.formatDate(yestoday, DateTimeUtil.YYYY_MM_DD);
					fetchData(timeStr);
				}else{
					LOG.info("抓取芝加哥交易所期货数据的定时器已关闭");
					RecordCrawlResult.recordFailData(className, null, null, "抓取芝加哥交易所期货数据的定时器已关闭");
				}
			}
		} catch(Exception e) {
			LOG.error("发生未知异常。", e);
			RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
		}
	}
	
	/**
	 * 抓取指定时间序列所有品种的数据
	 * @param timeStr 时间序列
	 * */
	public void fetchData(String timeStr){
		for(String key: mapVarCode.keySet()){
			String[] varCn = key.split("-");
			String varName = varCn[0];
			String cnName = varCn[1];
			LOG.info("开始抓取：" + key);
			try{
				fetchOne(timeStr, varName, cnName, mapVarCode.get(key));
				Thread.sleep((int)(Math.random() * 5000));
			} catch(Exception e) {
				LOG.error(key + ": 出现未知错误。", e);
				RecordCrawlResult.recordFailData(className, varName, cnName, "\"发生异常。" + e.getMessage() + "\"");
			}
		}
	}
	
	/**
	 * 抓取指定时间段所有品种的数据
	 * @param startTime 开始时间序列
	 * @param endTime 结束时间序列
	 * */
	public void fetchData(String startTime, String endTime){
		for(String key: mapVarCode.keySet()){
			String[] varCn = key.split("-");
			String varName = varCn[0];
			String cnName = varCn[1];
			LOG.info("开始抓取：" + key);
			try{
				this.fetchOne(startTime, endTime, varName, cnName, mapVarCode.get(key));
				Thread.sleep((int)(Math.random() * 5000));
			} catch(Exception e) {
				LOG.error(key + ": 出现未知错误。", e);
				RecordCrawlResult.recordFailData(className, varName, cnName, "\"发生异常。" + e.getMessage() + "\"");
			}
		}
	}
	
	/**
	 * 抓指定数据
	 * @param timeStr 时间序列
	 * @param varName 品种名
	 * @param cnName 中文名
	 * @param datasetCode 数据集编码
	 * */
	public void fetchOne(String timeStr, String varName, String cnName, String datasetCode){
		String pageUrl = baseUrl.replace("#dataset_code", datasetCode);
		pageUrl = pageUrl.replace("#start_date", timeStr);
		pageUrl = pageUrl.replace("#end_date", timeStr);
		LOG.info("url:@" + pageUrl);
		String contents = myHttpClient.getHtmlByHttpClient(pageUrl, encoding, null);
		if(contents == null || contents.isEmpty() || "023".contains(contents)){
			LOG.info("未抓到数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "未抓到数据");
			return;
		}else if(contents.equals("1")){
			LOG.info("未抓到数据，可能是没有该时间序列的数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "\"未抓到数据，可能是没有该时间序列的数据\"");
			return;
		}
		try {
			FileStrIO.saveStringToFile(contents, Constants.FUTURES_ROOT + Constants.FILE_SEPARATOR + "芝加哥交易所" + Constants.FILE_SEPARATOR + timeStr.replace("-", "") + Constants.FILE_SEPARATOR, varName + cnName + ".txt");
		} catch (IOException e) {
			LOG.error("保存内容时发生异常。", e);
			RecordCrawlResult.recordFailData(className, varName, cnName, "保存内容时发生异常。");
		}
		Map<String, String> newestData = this.analyse(contents);
		parseAndSave(newestData, varName, cnName);
		return;
	}
	
	/**
	 * 根据json串解析出最新可用时间序列的数据
	 * @param jsonStr 待解析json串
	 * */
	public Map<String, String> analyse(String jsonStr){
		Map<String, String> dataMap = null;
		JSONTokener jsonTokener = new JSONTokener(jsonStr);
		JSONObject rslt = (JSONObject) jsonTokener.nextValue();
		JSONObject dataset = rslt.getJSONObject("dataset");
		dataMap = new HashMap<String, String>();
		JSONArray column_names = dataset.getJSONArray("column_names");//列名
		JSONArray data = dataset.getJSONArray("data");//数据集
		JSONArray assignData = null;//最新可用时间序列的数据
		//找到最新可用时间序列的数据
		if(data != null && data.size() > 0){
			assignData = data.getJSONArray(0);
			//构造 列名-值 映射
			for(int i = column_names.size()-1; i >= 0; i--){
				dataMap.put(column_names.getString(i), assignData.getString(i));
			}
		}
		
		return dataMap;
	}
	
	/**
	 * 根据json串解析出全部可用时间序列的数据
	 * @param jsonStr 待解析json串
	 * */
	public List<Map<String, String>> analyseAllDate(String jsonStr){
		List<Map<String, String>> allDataList = new LinkedList<Map<String, String>>();
		JSONTokener jsonTokener = new JSONTokener(jsonStr);
		JSONObject rslt = (JSONObject) jsonTokener.nextValue();
		JSONObject dataset = rslt.getJSONObject("dataset");
		JSONArray column_names = dataset.getJSONArray("column_names");//列名
		JSONArray data = dataset.getJSONArray("data");//数据集
		JSONArray assignData = null;//最新可用时间序列的数据
		//找到最新可用时间序列的数据
		if(data != null && data.size() > 0){
			for(int dateIndex = 0; dateIndex < data.size(); dateIndex++){
				Map<String, String> dataMap = new HashMap<String, String>();
				assignData = data.getJSONArray(dateIndex);
				//构造 列名-值 映射
				for(int i = column_names.size()-1; i >= 0; i--){
					dataMap.put(column_names.getString(i), assignData.getString(i));
				}
				allDataList.add(dataMap);
			}
		}
		return allDataList;
	}
	
	/**
	 * 解析某日的数据，并保存
	 * */
	public void parseAndSave(Map<String, String> newestData, String varName, String cnName){
		Map<String, String> dataMap = new HashMap<String, String>();
		if(newestData != null){
			boolean isSaveSuccess = false;//标识该品种的数据是否保存成功。
			if(newestData.get("Open") != null && !newestData.get("Open").equalsIgnoreCase("null")){
				dataMap.put("开盘价", newestData.get("Open"));
			}
			if(newestData.get("High") != null && !newestData.get("High").equalsIgnoreCase("null")){
				dataMap.put("最高价", newestData.get("High"));
			}
			if(newestData.get("Low") != null && !newestData.get("Low").equalsIgnoreCase("null")){
				dataMap.put("最低价", newestData.get("Low"));
			}
			if(newestData.get("Last") != null && !newestData.get("Last").equalsIgnoreCase("null")){
				dataMap.put("收盘价", newestData.get("Last"));
			}
			if(newestData.get("Settle") != null && !newestData.get("Settle").equalsIgnoreCase("null")){
				dataMap.put("结算价", newestData.get("Settle"));
			}
			if(newestData.get("Volume") != null && !newestData.get("Volume").equalsIgnoreCase("null")){
				dataMap.put("成交量", newestData.get("Volume"));
			}
			if(newestData.get("Open Interest") != null && !newestData.get("Open Interest").equalsIgnoreCase("null")){
				dataMap.put("持仓量", newestData.get("Open Interest"));
			}
			if(!dataMap.isEmpty()){
				int timeInt = Integer.parseInt(newestData.get("Date").replace("-", ""));//时间序列
				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
				isSaveSuccess = true;
			}
			if(newestData.get("Previous Settlement") != null && !newestData.get("Previous Settlement").equalsIgnoreCase("null")){
				/*保存前一天的结算价*/
				Map<String, String> PreviousSettlement = new HashMap<String, String>();
				PreviousSettlement.put("结算价", newestData.get("Previous Settlement"));
				String curDataDate = newestData.get("Date");//当前时间序列
				int previousTimeInt = 0;
				Calendar c = Calendar.getInstance();
				c.setTime(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD));
				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){//周一的话上一个结算日应该是上周五。
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -3);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				} else {
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -1);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				}
				dao.saveOrUpdateByDataMap(varName, cnName, previousTimeInt, PreviousSettlement);
				isSaveSuccess = true;
			}
			if(newestData.get("Prev. Day Open Interest") != null && !newestData.get("Prev. Day Open Interest").equalsIgnoreCase("null")){
				/*保存前一天的持仓量*/
				Map<String, String> prevDayOpenInterest = new HashMap<String, String>();
				prevDayOpenInterest.put("持仓量", newestData.get("Prev. Day Open Interest"));
				String curDataDate = newestData.get("Date");//当前时间序列
				int previousTimeInt = 0;
				Calendar c = Calendar.getInstance();
				c.setTime(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD));
				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){//周一的话上一个结算日应该是上周五。
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -3);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				} else {
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -1);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				}
				dao.saveOrUpdateByDataMap(varName, cnName, previousTimeInt, prevDayOpenInterest);
				isSaveSuccess = true;
			}
			if(!isSaveSuccess){
				RecordCrawlResult.recordFailData(className, varName, cnName, "无新数据入库。");
			}
		} else {
			LOG.info("未发现最新数据。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "未发现最新数据。");
		}
	}
	
	/**
	 * 解析某日的数据，暂不保存，待解析完之后批量保存。
	 * */
	public void parseAndSave(Map<String, String> newestData, String varName, String cnName, Map<Integer, Map<String, String>> butchData){
		Map<String, String> dataMap = new HashMap<String, String>();
		if(newestData != null){
			boolean isSaveSuccess = false;//标识该品种的数据是否保存成功。
			if(newestData.get("Open") != null){
				if(!newestData.get("Open").equalsIgnoreCase("null")){
					dataMap.put("开盘价", newestData.get("Open"));
				} else {
					dataMap.put("开盘价", null);
				}
			}
			if(newestData.get("High") != null){
				if(!newestData.get("High").equalsIgnoreCase("null")){
					dataMap.put("最高价", newestData.get("High"));
				} else {
					dataMap.put("最高价", null);
				}
			}
			if(newestData.get("Low") != null){
				if(!newestData.get("Low").equalsIgnoreCase("null")){
					dataMap.put("最低价", newestData.get("Low"));
				} else {
					dataMap.put("最低价", null);
				}
			}
			if(newestData.get("Last") != null){
				if(!newestData.get("Last").equalsIgnoreCase("null")){
					dataMap.put("收盘价", newestData.get("Last"));
				} else {
					dataMap.put("收盘价", null);
				}
			}
			if(newestData.get("Settle") != null){
				if(!newestData.get("Settle").equalsIgnoreCase("null")){
					dataMap.put("结算价", newestData.get("Settle"));
				} else {
					dataMap.put("结算价", null);
				}
			}
			if(newestData.get("Volume") != null){
				if(!newestData.get("Volume").equalsIgnoreCase("null")){
					dataMap.put("成交量", newestData.get("Volume"));
				} else {
					dataMap.put("成交量", null);
				}
			}
			if(newestData.get("Open Interest") != null){
				if(!newestData.get("Open Interest").equalsIgnoreCase("null")){
					dataMap.put("持仓量", newestData.get("Open Interest"));
				} else {
					dataMap.put("持仓量", null);
				}
			}
			if(!dataMap.isEmpty()){
				int timeInt = Integer.parseInt(newestData.get("Date").replace("-", ""));//时间序列
//				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
				if(!butchData.containsKey(timeInt)){
					butchData.put(timeInt, dataMap);//批量保存
				} else {
					butchData.get(timeInt).putAll(dataMap);
					LOG.info("已有该日数据：" + timeInt);
				}
				isSaveSuccess = true;
			}
			if(newestData.get("Previous Settlement") != null){
				/*保存前一天的结算价*/
				Map<String, String> PreviousSettlement = new HashMap<String, String>();
				if(!newestData.get("Previous Settlement").equalsIgnoreCase("null")){
					PreviousSettlement.put("结算价", newestData.get("Previous Settlement"));
				} else {
					PreviousSettlement.put("结算价", null);
				}
				String curDataDate = newestData.get("Date");//当前时间序列
				int previousTimeInt = 0;
				Calendar c = Calendar.getInstance();
				c.setTime(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD));
				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){//周一的话上一个结算日应该是上周五。
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -3);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				} else {
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -1);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				}
//				dao.saveOrUpdateByDataMap(varName, cnName, previousTimeInt, PreviousSettlement);
				if(!butchData.containsKey(previousTimeInt)){
					butchData.put(previousTimeInt, PreviousSettlement);//批量保存
				} else {
					butchData.get(previousTimeInt).putAll(PreviousSettlement);
					LOG.info("已有前一日结算价：" + previousTimeInt);
				}
				isSaveSuccess = true;
			}
			if(newestData.get("Prev. Day Open Interest") != null){
				/*保存前一天的持仓量*/
				Map<String, String> prevDayOpenInterest = new HashMap<String, String>();
				if(!newestData.get("Prev. Day Open Interest").equalsIgnoreCase("null")){
					prevDayOpenInterest.put("持仓量", newestData.get("Prev. Day Open Interest"));
				} else {
					prevDayOpenInterest.put("持仓量", null);
				}
				String curDataDate = newestData.get("Date");//当前时间序列
				int previousTimeInt = 0;
				Calendar c = Calendar.getInstance();
				c.setTime(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD));
				if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){//周一的话上一个结算日应该是上周五。
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -3);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				} else {
					Date previousDate = DateTimeUtil.addDay(DateTimeUtil.parseDateTime(curDataDate, DateTimeUtil.YYYY_MM_DD), -1);
					previousTimeInt = Integer.parseInt(DateTimeUtil.formatDate(previousDate, DateTimeUtil.YYYYMMDD));
				}
//				dao.saveOrUpdateByDataMap(varName, cnName, previousTimeInt, prevDayOpenInterest);
				if(!butchData.containsKey(previousTimeInt)){
					butchData.put(previousTimeInt, prevDayOpenInterest);
				} else {
					butchData.get(previousTimeInt).putAll(prevDayOpenInterest);
					LOG.info("已有前一日持仓量：" + previousTimeInt);
				}
				isSaveSuccess = true;
			}
			if(!isSaveSuccess){
				RecordCrawlResult.recordFailData(className, varName, cnName, "无新数据入库。");
			}
		} else {
			LOG.info("未发现最新数据。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "未发现最新数据。");
		}
	}
	
	/**
	 * 抓某一个品种指定时间段的历史数据
	 * */
	public void fetchOne(String startTime, String endTime, String varName, String cnName, String datasetCode){

		String pageUrl = baseUrl.replace("#dataset_code", datasetCode);
		pageUrl = pageUrl.replace("#start_date", startTime);
		pageUrl = pageUrl.replace("#end_date", endTime);
		LOG.info("url:@" + pageUrl);
		String contents = myHttpClient.getHtmlByHttpClient(pageUrl, encoding, null);
		if(contents == null || contents.isEmpty() || "023".contains(contents)){
			LOG.info("未抓到数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "未抓到数据");
			return;
		}else if(contents.equals("1")){
			LOG.info("未抓到数据，可能是没有该时间序列的数据");
			RecordCrawlResult.recordFailData(className, varName, cnName, "\"未抓到数据，可能是没有该时间序列的数据\"");
			return;
		}
		try {
			FileStrIO.saveStringToFile(contents, Constants.FUTURES_ROOT + Constants.FILE_SEPARATOR + "芝加哥交易所" + Constants.FILE_SEPARATOR + startTime.replace("-", "") + endTime.replace("-", "") + Constants.FILE_SEPARATOR, varName + cnName + ".txt");
		} catch (IOException e) {
			LOG.error("保存内容时发生异常。", e);
			RecordCrawlResult.recordFailData(className, varName, cnName, "保存内容时发生异常。");
		}
		List<Map<String, String>> allDataList = analyseAllDate(contents);
		Map<Integer, Map<String, String>> butchData = new HashMap<Integer, Map<String, String>>();//批量保存时所用的批量数据
		for(Map<String, String> dataOneDate: allDataList){
			this.parseAndSave(dataOneDate, varName, cnName, butchData);
		}
		int varId = Variety.getVaridByName(varName);
		if(butchData.containsKey(20160817)){
			butchData.remove(20160817);
		}
		dao.batchSaveByDataMap(varId, cnName, butchData, null);
		return;
	}
	
	public static void main(String[] args) {
		CMEDataFetch cme = new CMEDataFetch();
		cme.start();
		
		/**
		 * 该网站限定每天匿名访问次数为50次，超出会返回错误信息如下，
		 * {"quandl_error":{"code": "QELx01","message": "You have exceeded the anonymous user limit of 50 calls per day. To make more calls today, please register for a free Quandl account and then include your API key with your requests."}}
		 * */
		/*补指定时间序列的历史数据*/
//		cme.fetchData("2017-06-28");
		
		/*补指定时间段内的数据*/
//		String startTime = "1959-07-01";
//		String startTime = "2017-06-28";
//		String endTime = "2016-06-28";
//		cme.fetchData(startTime, endTime);
		
	}
}
