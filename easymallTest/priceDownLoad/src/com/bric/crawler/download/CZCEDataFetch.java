package com.bric.crawler.download;

import com.bric.crawler.MapInit;
import com.bric.intoDB.Dbm3InterfaceDao;
import com.bric.util.*;
import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.*;
import java.util.*;

public class CZCEDataFetch {
	private String rawUrl = "http://www.czce.com.cn/portal/exchange/YYYY/datatradeholding/YYYYMMDD.htm";
	private String encoding = "GB2312";
	private Logger logger = Logger.getLogger(CZCEDataFetch.class);

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();

	/**
	 * 主方法
	 */
	public void fetchAndSaveCZCEData(){
		Date today = formatData.getDate(0);
		String todayStr = DateTimeUtil.formatDate(today, "yyyyMMdd");
		CZCEDataFetch czce=new CZCEDataFetch();
		czce.fetchData(todayStr);
		czce.saveCZCEData(new File(Constants.ZZCCDATA_ROOT), todayStr);
	}
	
	private String getUrl(String dateStr){
		String fetchUrl = rawUrl;
		fetchUrl = fetchUrl.replaceFirst("YYYY", dateStr.substring(0,4));
		fetchUrl = fetchUrl.replaceFirst("YYYYMMDD", dateStr);
		return fetchUrl;
	}
	
	private NodeFilter getFilter(){
		HasParentFilter hasParentFilter = new HasParentFilter(new TagNameFilter("td"));
		return new AndFilter(hasParentFilter, new TagNameFilter("table"));
	}
	
	private NodeList fetchRootTableList(String url)
			throws ParserException{
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setEncoding(encoding);
		return parser.extractAllNodesThatMatch(getFilter());
	}
	
	private String matchContractName(String tableTitle){
		if (null == tableTitle)
			return "";
		int contractIndex = tableTitle.indexOf("合约：");
		if (contractIndex > 0)
			return tableTitle.substring(contractIndex + 3).split("&nbsp")[0];
		return "";
	}


	
	public void fetchData(String dateStr){
		logger.info("");
		logger.info("******fetch start, date:"+dateStr+"******");
		NodeList rootTables;
		try {
			rootTables = fetchRootTableList(getUrl(dateStr));
		} catch (ParserException e) {
			logger.error("ParserException while fetch rootTableList:", e);
			logger.info("******exit fetch******");
			return;
		}
		if (rootTables.size() <= 0)
			logger.warn("Find no data!");
		for (int i = 0;i < rootTables.size();++ i){
			NodeList subTable = rootTables.elementAt(i).getChildren();
			String contractID = matchContractName(HtmlNodeListUtil.getTableFirstCellStr(subTable));
			String productName = MapInit.CZCEmapReverse.get(contractID.split("[0-9]+")[0]);
			if (null == productName){
				if (! "".equals(contractID))
					logger.warn("table discard, contract ID = " + contractID);
				continue;
			}
//			String dirString = Constants.ZZCCDATA_ROOT+"\\" + productName + "\\" + contractID + "\\";//windows
			String dirString = Constants.ZZCCDATA_ROOT+Constants.FILE_SEPARATOR + productName + Constants.FILE_SEPARATOR + contractID + Constants.FILE_SEPARATOR;//linux
			String tableStr = HtmlNodeListUtil.table2Str_SpecifyRows(subTable, "0");
			try {
				if (new File(dirString + dateStr + ".txt").exists())
					logger.warn("Overwrite: "+ productName + contractID);
				FileStrIO.saveStringToFile(tableStr, dirString, dateStr + ".txt");
				logger.info("data saved: " + productName + contractID);
			} catch (IOException e) {
				logger.error("IOException in saving "+productName+contractID+":", e);
			}
		}
		logger.info("******fetch succeed******");
	}

	/**
	 * 保存 today 大连持仓数据至数据库
	 * @param file
	 * @param today
	 * */
	public void saveCZCEData(File file,String today){//递归遍历文件目录
		logger.info("******saving CZCEData******");
		for(File file2:file.listFiles()){
			if (file2.isDirectory()) {
				System.out.println("directory");
				saveCZCEData(file2,today);
			}else {
				System.out.println("Saving " + file2.getAbsolutePath());
				String varName = file2.getParentFile().getParentFile().getName();
				String tableName = MapInit.CZCEtableMap.get(varName);
				String ContractNum = file2.getParentFile().getName();
				List list=new ArrayList<>();
				int VarId = MapInit.CZCEvarIDMap.get(varName);
				int TimeInt = Integer.valueOf(file2.getName().split("\\.")[0]);
				if(TimeInt == Integer.valueOf(today)){//只匹配当天数据
					try {
						InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.FILE_ENCODING);
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
								map.put("ExchangeId","101");
								map.put("VarId",VarId+"");
								map.put("TimeInt",TimeInt+"");
								map.put("ContractNum",ContractNum);
								try {
									map.put("成交会员",array[1]);
								} catch (ArrayIndexOutOfBoundsException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									map.put("成交量",array[2]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									map.put("成交增减",array[3]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									map.put("持买会员",array[4]);
								} catch (ArrayIndexOutOfBoundsException e) {

								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}

								try {
									map.put("持买单量",array[5]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									map.put("持买增减",array[6]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									map.put("持卖会员",array[7]);
								} catch (ArrayIndexOutOfBoundsException e) {

								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									map.put("成交量",array[8]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									map.put("持卖增减",array[9]);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									String rank = array[0];
									if (!"".equals(map.get("成交会员")))
										map.put("成交名次",rank);
									if (!"".equals(map.get("持买会员")))
										map.put("持买名次",rank);
									if (!"".equals(map.get("持卖会员")))
										map.put("持卖名次",rank);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								list.add(map);
							}

						}
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
		new CZCEDataFetch().fetchData("20140815");
		System.out.println("fetch succeed");
//		new CZCEDao().saveCZCEData(new File("D:\\CZCE"), "20140805");
	}
}

