package com.bric.crawler.download;

import java.io.File;
import java.io.IOException;

import com.bric.crawler.MapInit;
import com.bric.intoDB.CZCEDao;
import com.bric.util.Constants;
import com.bric.util.FileStrIO;
import com.bric.util.HtmlNodeListUtil;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class CZCEDataFetch {
	private String rawUrl = "http://www.czce.com.cn/portal/exchange/YYYY/datatradeholding/YYYYMMDD.htm";
	private String encoding = "GB2312";
	private Logger logger = Logger.getLogger(CZCEDataFetch.class);
	
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
	
	public static void main(String[] args) {
		new CZCEDataFetch().fetchData("20140815");
		System.out.println("fetch succeed");
//		new CZCEDao().saveCZCEData(new File("D:\\CZCE"), "20140805");
	}
}

