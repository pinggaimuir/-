package com.bric.crawler.download;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.bric.crawler.MapInit;
import com.bric.intoDB.DLCCDao;
import com.bric.util.Constants;
import com.bric.util.FileStrIO;
import com.bric.util.HtmlNodeListUtil;
import com.bric.util.StrTransUtil;

public class DLCCDataFetch {

	private static String contractUrl = "http://www.dce.com.cn/PublicWeb/MainServlet?action=Pu00021_contract&"
			+ "Pu00021_Input.content=0,1,2&Pu00021_Input.trade_type=0"
			+ "&Pu00021_Input.trade_date=";//&Pu00021_Input.variety=a";
	
	private static String CCUrl = "http://www.dce.com.cn/PublicWeb/MainServlet?action=Pu00021_result&"
			+ "Pu00021_Input.content=0,1,2&Pu00021_Input.trade_type=0&"
			+ "Pu00021_Input.trade_date=";//Pu00021_Input.contract_id=a1411
	
	private Logger logger = Logger.getLogger(DLCCDataFetch.class);
	
	private List<String> getConIdList(String url)
			throws ParserException{
		Parser parser = new Parser();
		parser.setURL(url);
		TagNameFilter tagNameFilter = new TagNameFilter("table");
		NodeList nodeList = parser.extractAllNodesThatMatch(tagNameFilter);
		NodeList trNodeList = nodeList.elementAt(1).getChildren();
		return HtmlNodeListUtil.col2ListExcludeTitleRow(trNodeList, 2);
	}
	
	private String getCCdataAsStr(String url)
			throws ParserException{
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
		for(String productName:MapInit.DLCCmapMap.keySet()){
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
	
	public static void main(String[] args) {
		String date = "20160315";
		new DLCCDataFetch().fetchData(date);
		new DLCCDao().saveDLCCData(new File(Constants.DLCCDATA_ROOT), date);
		System.out.println("fetch succeed");
	}
	
}
