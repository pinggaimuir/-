package cn.futures.data.importor.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.MyHttpClient;

/**
 * 郑州商品交易所
 * @author ctm
 *
 */
public class CZCEDataFetch {
	private static final String className = CZCEDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private Log logger = LogFactory.getLog(CZCEDataFetch.class);
	private static String url2 = "http://www.czce.com.cn/portal/exchange/%year%/%kind%/%date%.txt";
	private static String url = "http://www.czce.com.cn/portal/DFSStaticFiles/Future/%year%/%date%/%kind%.txt";
	private static Map<String, String> sheetVarNameMap= new HashMap<String, String>();
	private static Map<String, String> holdingVarNameMap= new HashMap<String, String>();
	private static Map<String, String> dealClassMap = new HashMap<String, String>();
	static{
		dealClassMap.put("持仓排名", "FutureDataHolding");
		dealClassMap.put("仓单日报", "FutureDataWhsheet");
		//dealClassMap.put("持仓排名", "datatradeholding");
		//dealClassMap.put("仓单日报", "datawhsheet");
		
		sheetVarNameMap.put("菜籽油OI", "郑州菜籽油");
		sheetVarNameMap.put("早籼稻RI", "郑州早籼稻");
		sheetVarNameMap.put("菜粕RM", "郑州菜粕");
		sheetVarNameMap.put("菜籽RS", "郑州菜籽");
		sheetVarNameMap.put("强筋小麦WH", "郑州强麦");
		sheetVarNameMap.put("白糖SR", "郑州白糖");
		sheetVarNameMap.put("一号棉CF", "郑州棉花");
		
		holdingVarNameMap.put("菜籽RS", "郑州菜籽");
		holdingVarNameMap.put("强麦WH", "郑州强麦");
		holdingVarNameMap.put("早籼RI", "郑州早籼稻");
		holdingVarNameMap.put("菜油OI", "郑州菜籽油");
		holdingVarNameMap.put("菜粕RM", "郑州菜粕");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_CZCE_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("持仓排名、仓单日报", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到持仓排名、仓单日报在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchData(date);
			}else{
				logger.info("抓取持仓排名、仓单日报的定时器已关闭");
			}
		}
	}
	
	public void fetchData(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		for(String dealClass:dealClassMap.keySet()){
			logger.info("==========开始抓取郑州商品交易所:"+dealClass+"=============");
			MyHttpClient httpClient = new MyHttpClient();
			String pageUrl = url.replaceAll("%year%", timeInt.substring(0,4))
				.replaceAll("%kind%", dealClassMap.get(dealClass))
				.replaceAll("%date%", timeInt);
			logger.info("抓取"+dealClass+"@"+pageUrl);
			String body = httpClient.getHtmlByHttpClient(pageUrl, "GBK", "");
			body = body.replaceAll(",", "").replaceAll("[|]", ",").replaceAll("名次([^\\n]+)\\n", "").replaceAll("仓库编号([^\\n]+)\\n", "").replaceAll(" *", "").replaceAll("\n\r", "");
			if(dealClass.equals("仓单日报")){
				datawhsheetAnalysis(body, dealClass, timeInt);
			}else if(dealClass.equals("持仓排名")){
				tradeHoldingAnalysis(body, dealClass, timeInt);
			}
		}
	}
	
	private void datawhsheetAnalysis(String body, String dealClass,String timeInt){
		if(body != null && !body.equals("")){
			logger.info("开始分析"+dealClass+timeInt+"数据");
			String cnName = dealClass;
			String[] kinds = body.split("品种：");
			for(String kind:kinds){
				if(kind.indexOf("单位") == -1) continue;
				String varTmp = kind.substring(0, kind.indexOf("单位")).trim();
				if(sheetVarNameMap.keySet().contains(varTmp)){
					String varName = sheetVarNameMap.get(varTmp);
					String[] fields = kind.substring(kind.indexOf("总计")).split(",");
					Map<String, String> dataMap = new HashMap<String, String>();
					//如果仓单日报数据抓取出现问题极有可能是此处的问题，如果此处对应数据表格的格式发生了改变，主要是“仓单数量”和“有效预报”两个字段位置的变动（不同品种不完全相同）会影响数据抓取。
					if(varTmp.equals("一号棉CF") || varTmp.equals("白糖SR")){
						dataMap.put("仓单数量", fields[5].trim());
						dataMap.put("有效预报", fields[7].trim());
					}else if(varTmp.equals("菜籽油OI")){
						dataMap.put("仓单数量", fields[4].trim());
						dataMap.put("有效预报", fields[6].trim());
					}else if(varTmp.equals("菜粕RM")){
						dataMap.put("仓单数量", fields[3].trim());
						dataMap.put("有效预报", fields[5].trim());
					}else if(varTmp.equals("菜籽RS")){
						dataMap.put("仓单数量", fields[2].trim());
						dataMap.put("有效预报", fields[4].trim());
					}else{// if(varTmp.equals("早籼稻RI")||varTmp.equals("强筋小麦WH")){
						dataMap.put("仓单数量", fields[4].trim());
						dataMap.put("有效预报", fields[6].trim());
					}
//					boolean haveData = false;
//					for(String tmp:dataMap.keySet()){
//						if(!dataMap.get(tmp).equals("0")) haveData = true;
//					}
//					if(haveData){
						int varId = Variety.getVaridByName(varName);
						logger.info("保存"+varName+":"+cnName+"("+timeInt+")"+"数据");
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
//					}else{
//						logger.info(timeInt + varName+cnName+"没有需要保存的数据");
//					}
				}
			}
		}else{
			logger.info("没有抓取到"+dealClass+timeInt+"数据");
		}
	}
	private void tradeHoldingAnalysis(String body, String dealClass,String timeInt){
		if(body != null && !body.equals("")){
			logger.info("开始分析"+dealClass+timeInt+"数据");
			List<String> headers = new ArrayList<String>();
			String[] kinds = body.split("品种：");
			for(String kind:kinds){
				if(kind.indexOf("日期") == -1) continue;
				String varTmp = kind.substring(0, kind.indexOf("日期")).trim();
				if(varTmp.equals("棉花CF") || varTmp.equals("白糖SR")){
					String varName = "郑州"+varTmp.substring(0,2);
					logger.info("处理"+varName+"主力多头持仓、主力空头持仓");
					int varId = Variety.getVaridByName(varName);
					headers.clear();
					headers.addAll(dao.getHeaders(varName,"主力多头持仓"));
					String[] datas = new String[21];
					datas[0] = kind.substring(0,kind.indexOf("\n2,"));
					for(int i=1;i<19;i++){
						datas[i] = kind.substring(kind.indexOf("\n"+(i+1)+","),kind.indexOf("\n"+(i+2)+","));
					}
					datas[19] = kind.substring(kind.indexOf("20,"),kind.indexOf("合计,"));
					datas[20] = kind.substring(kind.indexOf("合计,"));
					Map<String, String> dataBuyMap = new HashMap<String, String>();
					Map<String, String> dataSellMap = new HashMap<String, String>();
					for(int i=0;i<20;i++){
						String[] fields = datas[i].split(",");
						if(fields[4].equals("永安期货") && varName.equals("郑州白糖")){
							dataBuyMap.put("浙江永安", fields[5]);
						}
						if(fields[7].equals("永安期货") && varName.equals("郑州白糖")){
							dataSellMap.put("浙江永安", fields[8]);
						}
						if(headers.contains(fields[4])){
							dataBuyMap.put(fields[4], fields[5]);
						}
						if(headers.contains(fields[7])){
							dataSellMap.put(fields[7], fields[8]);
						}
					}
					String[] fields = datas[20].split(",");
					dataBuyMap.put("主力持仓", fields[5]);
					dataSellMap.put("主力持仓", fields[8]);
					dao.saveOrUpdateByDataMap(varId, "主力多头持仓", Integer.parseInt(timeInt), dataBuyMap);
					dao.saveOrUpdateByDataMap(varId, "主力空头持仓", Integer.parseInt(timeInt), dataSellMap);
				}else if(holdingVarNameMap.keySet().contains(varTmp)){
					String varName = holdingVarNameMap.get(varTmp);
					logger.info("处理"+varName+"主力持仓");
					int varId = Variety.getVaridByName(varName);
					String data = kind.substring(kind.indexOf("合计,"));
					String cnName = "主力持仓";
					String[] fields = data.split(",");
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("持买单量", fields[5]);
					dataMap.put("持卖单量", fields[8]);
					dataMap.put("净多单量", Double.parseDouble(fields[5])-Double.parseDouble(fields[8])+"");
					logger.info("保存"+varName+":"+cnName+"("+timeInt+")"+"数据");
					dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
				}
			}
		}else{
			logger.info("没有抓取到"+dealClass+timeInt+"数据");
		}
	}
	public static void main(String[] args){
		CZCEDataFetch fetch = new CZCEDataFetch();
//		fetch.start();
		Date date = DateTimeUtil.parseDateTime("20160316", "yyyyMMdd");
		fetch.fetchData(date);
//		fetch.fetchDatawhSheet(date);
//		Date dateBefore = DateTimeUtil.parseDateTime("20151209", "yyyyMMdd");
//		while(date.after(dateBefore)){
//			fetch.fetchData(date);
//			date = DateTimeUtil.addDay(date, -1);
//		}
	}
}
