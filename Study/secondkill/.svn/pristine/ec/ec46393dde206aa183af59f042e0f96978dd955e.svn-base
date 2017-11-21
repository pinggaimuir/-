package cn.futures.data.importor.crawler.price21food;

import cn.futures.data.DAO.Dbm3InterfaceDao;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.crawler.price21food.assist.MapInit;
import cn.futures.data.importor.crawler.price21food.assist.formatData;
import cn.futures.data.util.Constants;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.HtmlNodeListUtil;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.util.*;

public class DLCCDataFetch {

	private static String contractUrl = "http://www.dce.com.cn/PublicWeb/MainServlet?action=Pu00021_contract&"
			+ "Pu00021_Input.content=0,1,2&Pu00021_Input.trade_type=0"
			+ "&Pu00021_Input.trade_date=";//&Pu00021_Input.variety=a";
	
	private static String CCUrl = "http://www.dce.com.cn/PublicWeb/MainServlet?action=Pu00021_result&"
			+ "Pu00021_Input.content=0,1,2&Pu00021_Input.trade_type=0&"
			+ "Pu00021_Input.trade_date=";//Pu00021_Input.contract_id=a1411
	
	private Logger logger = Logger.getLogger(DLCCDataFetch.class);

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();


	/**
	 * 定时任务
	 */
	@Scheduled(cron= CrawlScheduler.CRON_DLCC)
	public void start(){
		logger.info("开始抓取DLCCDataFetch大连持仓量排名数据");
		fetchAndSaveDLCCData();
		logger.info("DLCCDataFetch大连持仓量排名数据爬虫执行完毕");
	}

	/**
	 * 主方法
	 */
	public void fetchAndSaveDLCCData(){
		Date today = formatData.getDate(0);
		String todayStr = DateTimeUtil.formatDate(today, "yyyyMMdd");
		DLCCDataFetch fetch=new DLCCDataFetch();
		fetch.fetchData(todayStr);
		fetch.saveDLCCData(new File(Constants.DLCCDATA_ROOT), todayStr);

	}

	private List<String> getConIdList(String url)
			throws ParserException {
		Parser parser = new Parser();
		parser.setURL(url);
		TagNameFilter tagNameFilter = new TagNameFilter("table");
		NodeList nodeList = parser.extractAllNodesThatMatch(tagNameFilter);
		NodeList trNodeList = nodeList.elementAt(1).getChildren();
		return HtmlNodeListUtil.col2ListExcludeTitleRow(trNodeList, 2);
	}
	
	private String getCCdataAsStr(String url)
			throws ParserException {
		NodeList contractTableList;
		String CCstr = "";
		Parser parser = new Parser();
		parser.setURL(url);
		TagNameFilter tagNameFilter = new TagNameFilter("table");
		contractTableList = parser.extractAllNodesThatMatch(tagNameFilter);
		for(int i=2;i<contractTableList.size();i++)
			CCstr += HtmlNodeListUtil.table2Str(contractTableList.elementAt(i).getChildren());
		return CCstr;
	}
	
	/**
	 * 获取 today 大连交易所持仓数据并缓存
	 * @param dateStr
	 * */
	public void fetchData(String dateStr){
		logger.info("");
		logger.info("******fetch start, date:"+dateStr+"******");
		for(String productName: MapInit.DLCCmapMap.keySet()){
			String ContractUrl = contractUrl+dateStr+"&Pu00021_Input.variety="+MapInit.DLCCmapMap.get(productName);
			List<String> conIdList;
			try {
				conIdList = getConIdList(ContractUrl);
			} catch (ParserException e) {
				logger.error("ParserException while get conIdList of "+productName+":",e);
				continue;
			}
			
			for(String contractID:conIdList){
				String CCURL = CCUrl+dateStr+"&Pu00021_Input.contract_id="+contractID;
				String CCdataString;
				try {
					CCdataString = getCCdataAsStr(CCURL);
				} catch (ParserException e) {
					logger.error("ParserException while fetching "+productName+contractID+":", e);
					continue;
				}
//				String dirString = Constants.DLCCDATA_ROOT+"\\"+productName+"\\"+contractID + "\\";//windows
				String dirString = Constants.DLCCDATA_ROOT+Constants.FILE_SEPARATOR+productName+Constants.FILE_SEPARATOR+contractID + Constants.FILE_SEPARATOR;//linux
				try {
					if (new File(dirString + dateStr + ".txt").exists())
						logger.warn("Overwrite: "+ productName + contractID);
					FileStrIO.saveStringToFile(CCdataString, dirString, dateStr + ".txt");
					//目前本程序只做到下载持仓数据，对于下载下来的数据还未进行处理。
					logger.info("data saved: " + productName + contractID);
				} catch (IOException e) {
					logger.error("IOException while saving "+productName+contractID+" data:", e);
				}
			}
			logger.info(productName + " fetched");
		}
		logger.info("******fetch succeed******");
	}

	/**
	 * 保存 today 大连持仓数据至数据库
	 * @param file
	 * @param today
	 * */
	public void saveDLCCData(File file,String today){//递归遍历文件目录
		logger.info("******saving DLCCData******");
		for(File file2:file.listFiles()){
			if (file2.isDirectory()) {
				saveDLCCData(file2,today);
			}else {
				List list=new ArrayList<>();
				String varName = file2.getParentFile().getParentFile().getName();
				String tableName = MapInit.DLCCTableMap.get(varName);
				String ContractNum = file2.getParentFile().getName();
				int VarId = MapInit.DLCCVarIdMap.get(varName);
				int TimeInt = Integer.valueOf(file2.getName().split("\\.")[0]);
				if(TimeInt == Integer.valueOf(today)){//只匹配当天数据
					try {
						InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.ENCODE_GB2312);
						BufferedReader reader = new BufferedReader(inputStreamReader);
						String str = reader.readLine();
						int row = 1;
						while((str=reader.readLine())!=null){
							row++;
							if (str.contains("总计")) {
								break;
							}else {
								Map<String,String> map=new HashMap<>();
								String[] array = str.split(",");
								map.put("ExchangeId",Constants.DL_EXCHANGE_ID+"");
								map.put("VarId",VarId+"");
								map.put("TimeInt",TimeInt+"");
								map.put("ContractNum",ContractNum);
								try {
									if(array[0]==null || array[0].trim().length()<1 ){
										map.put("成交名次",(row-1)+"");
									}else{
										map.put("成交名次",array[0]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[1]==null || array[1].trim().length()<1 ){
										map.put("成交会员",null);
									}else{
										map.put("成交会员",array[1]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[2]==null || array[2].trim().length()<1 ){
										map.put("成交量",0+"");
									}else{
										map.put("成交量",array[2]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[3]==null || array[3].trim().length()<1 ){
										map.put("成交增减",0+"");
									}else{
										map.put("成交增减",array[3]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[4]==null || array[4].trim().length()<1 ){
										map.put("持买名次",(row-1)+"");
									}else{
										map.put("持买名次",array[4]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[5]==null || array[5].trim().length()<1 ){
										map.put("持买会员",null);
									}else{
										map.put("持买会员",array[5]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {

								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}

								try {
									if(array[6]==null || array[6].trim().length()<1 ){
										map.put("持买单量","0");
									}else{
										map.put("持买单量",array[6]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[7]==null || array[7].trim().length()<1 ){
										map.put("持买增减","0");
									}else{
										map.put("持买增减",array[7]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[8]==null || array[8].trim().length()<1 ){
										map.put("持卖名次",(row-1)+"");
									}else{
										map.put("持卖名次",array[8]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[9]==null || array[9].trim().length()<1 ){
										map.put("持卖会员",null);
									}else{
										map.put("持卖会员",array[9]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {

								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[10]==null || array[10].trim().length()<1 ){
										map.put("持卖单量","0");
									}else{
										map.put("持卖单量",array[10]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[11]==null || array[11].trim().length()<1 ){
										map.put("持卖增减","0");
									}else{
										map.put("持卖增减",array[11]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								list.add(map);
							}

						}
						reader.close();
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException:", e);
					} catch (IOException e) {
						logger.error("IOException:", e);
					}
					Map data=new HashMap<>();
					data.put("dbName",tableName);
					data.put("data", list);
					dbm3Dao.saveByDbName(data);
					logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
				}

			}
		}
		logger.info("******all finished******");
	}
	public static void main(String[] args) {
		String date = "20160315";
		DLCCDataFetch fetch=new DLCCDataFetch();
		fetch.fetchData(date);
		fetch.saveDLCCData(new File(Constants.DLCCDATA_ROOT), date);
		System.out.println("fetch succeed");
	}
	
}
