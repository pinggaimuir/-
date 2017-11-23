package com.bric.crawler.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.bric.crawler.MapInit;
import com.bric.util.Constants;
import com.bric.util.FileStrIO;

public class MofcomDataFetch {

	private static String FetchURL = "http://nc.mofcom.gov.cn/nc/face/gxdj/jghq/get_date_curve.jsp?var=";
	
	private Logger logger = Logger.getLogger(MofcomDataFetch.class);

	public Parser getHTMLParser(String pageURL, String encoding) { 
		System.out.println("open:"+pageURL);
		Parser parser;
		int i = 0;
		while(i<3){
			try{ 
				parser = new Parser((HttpURLConnection)(new URL(pageURL)).openConnection());
				System.out.println("ok");
				return parser;
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("network error");
			}
			i++;
		}
		return null;
	}	
	 public static String getHTML(String pageURL, String encoding) { 
	        StringBuilder pageHTML = new StringBuilder(); 
	        try { 
	            URL url = new URL(pageURL); 
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
	            connection.setRequestProperty("User-Agent", "MSIE 7.0"); 
	            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding)); 
	            String line = null; 
	            while ((line = br.readLine()) != null) { 
	                pageHTML.append(line); 
	                pageHTML.append("\n"); 
	            } 
	            connection.disconnect(); 
	        }
	        catch(ConnectException e){
	        	try {
	        		System.out.println("Connection timeout,will try again ten seconds later...");
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        	getHTML(pageURL, encoding);
	        }
	        catch (Exception e) { 
	            e.printStackTrace(); 
	        } 
	        return pageHTML.toString(); 
	    } 
	private String getMarketPriceAsStr(int year,int marketId,int varId){
		Parser parser = new Parser();
		String url = FetchURL + marketId +"-"+ varId +"-"+ year +"-"+ year;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			String line = getHTML(url,"utf-8");
			if(line.contains("<statDatas")){
				line = line.substring(line.indexOf("<statDatas"));
			}else{
				return null;
			}
			String regEx="(?<=<LastPrice>).*?(?=</LastPrice>)";   
			Pattern p = Pattern.compile(regEx);   
			Matcher m = p.matcher(line);
			int i=0;
			while(m.find()){
				String price= m.group(0).toString();  
				stringBuffer.append(price+","+(i+1)+"\n");
				i++;
//				NodeList list2 = list.elementAt(i).getChildren();
//				if (list2!=null) {
//					String price = list2.elementAt(0).toPlainTextString().trim();
//					String month = list2.elementAt(2).toPlainTextString().trim();
//					stringBuffer.append(price + ","+month+"\n");
//				}				
			}
			return stringBuffer.toString();
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.error("error kind:"+marketId+":"+varId);
		}
		return null;
	}

	public void fetchData(int year){
		logger.info("**********fetch start, year "+year+"*********");
		for(String province:MapInit.mofcomMarketReflectMap.keySet()){//省份
			Map<String, Integer> provinceMap = MapInit.mofcomMarketReflectMap.get(province);			
			for (String marketName : provinceMap.keySet()) {//市场
				Integer marketId = provinceMap.get(marketName);
				for(String kind:MapInit.mofcomKindReflectMap.keySet()){//大分类
					Map<String, Integer> varIdMap = MapInit.mofcomKindReflectMap.get(kind);
					for(String varName:varIdMap.keySet()){
						Integer varId = varIdMap.get(varName);
						String content = getMarketPriceAsStr(year,marketId,varId);
//						String dirString = Constants.MARKETPRICEDATA_21FOOD_ROOT + "\\" + kind + "\\" + varName + "\\";//windows
						String dirString = Constants.MOFCOM_ROOT + Constants.FILE_SEPARATOR + kind + Constants.FILE_SEPARATOR + varName + Constants.FILE_SEPARATOR+
								province + Constants.FILE_SEPARATOR + marketName + Constants.FILE_SEPARATOR;//linux
						try {
							if (new File(dirString + year + ".txt").exists())
								logger.warn("Overwrite: "+ varName);
							FileStrIO.saveStringToFile(content, dirString,	year + ".txt");
							logger.info("data saved: " + varName);
						} catch (IOException e) {
							logger.error("IOException while saving "+varName+" data:", e);
						}
					
					}
					logger.info("====== "+ kind + " fetched ======");
				}
				logger.info("====== "+ marketName + " fetched ======");
				try {
					logger.info("sleep ten seconds...");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					logger.error("InterruptedException:", e);
				}
			}			
			logger.info("contune fetch...");
		}
		logger.info("******fetch succeed!******");
	}

	public static void main(String[] a){
		new MofcomDataFetch().fetchData(2015);					
	}
}
