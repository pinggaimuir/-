package cn.futures.data.importor.crawler.price21food;

import cn.futures.data.DAO.Dbm3InterfaceDao;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.crawler.price21food.assist.MapInit;
import cn.futures.data.importor.crawler.price21food.assist.formatData;
import cn.futures.data.util.Constants;
import cn.futures.data.util.FileStrIO;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MofcomDataFetch {

	private static String FetchURL = "http://nc.mofcom.gov.cn/nc/face/gxdj/jghq/get_date_curve.jsp?var=";

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();

	private Logger logger = Logger.getLogger(MofcomDataFetch.class);

	/**
	 * 定时任务
	 */
	@Scheduled(cron= CrawlScheduler.CRON_MOFCOM)
	public void start(){
		logger.info("开始抓取Mofcom 每月价格行情数据");
		fetchAndSaveMofcomData();
		logger.info("Mofcom 每月价格行情数据爬虫执行完毕");
	}

	public void fetchAndSaveMofcomData(){
		int year = formatData.getCurYear();
		MofcomDataFetch fetch=new MofcomDataFetch();
		fetch.fetchData(year);
		fetch.saveMofcomPriceData(new File(Constants.MOFCOM_ROOT), year);
	}

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
		for(String province: MapInit.mofcomMarketReflectMap.keySet()){//省份
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



	public void saveMofcomPriceData(File file,int year1){
		String year = String.valueOf(year1);
		int curMonth = formatData.getMonth(0);
		for(File file2:file.listFiles()){
			if (file2.isDirectory()) {
				saveMofcomPriceData(file2,year1);
			}else {
				List list=new ArrayList<>();
				String Product = file2.getParentFile().getParentFile().getParentFile().getName();
				String KindName = file2.getParentFile().getParentFile().getParentFile().getParentFile().getName();
				String tableName = MapInit.marketPriceMofcomTableMap.get(KindName);
				String market = file2.getParentFile().getName();
				String TimeInt = file2.getName().split("\\.")[0];
				int KindId = MapInit.mofcomMarketPriceMap.get(KindName);
				Integer VarId = 0;
				try {
					VarId = MapInit.mofcomKindReflectMap.get(KindName).get(Product);
				} catch (NullPointerException e) {
					logger.error("error:"+KindName+"--"+Product+"--"+market);
					return;
				}
//				Integer VarId = MapInit.mofcomKindReflectMap.get(KindName).get(Product);
				if (TimeInt.equals(year)) {
					InputStreamReader inputStreamReader;
					try {
						inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.ENCODE_GB2312);
						BufferedReader reader = new BufferedReader(inputStreamReader);
						String str = "";
						while((str=reader.readLine())!=null){
							Map<String,String> map=new HashMap<>();
							String[] array = str.split(",");
							if (!array[0].equals("0")) {
								if (curMonth == Integer.valueOf(array[1])) {//只取最近一个月数据
									String month = Integer.valueOf(array[1])<10?"0"+array[1]:array[1];
									String Area = formatData.getAreaByMarket(market);
									String Province = formatData.getProvinceByArea(Area);

									map.put("KindId",KindId+"");
									map.put("VarId",VarId+"");
									map.put("TimeInt",year+month);
									map.put("Province",Province);
									map.put("Area",Area);
									map.put("产品",Product);
									map.put("市场",market);
									map.put("价格",KindId+"");
									map.put("KindId",array[0]);
									list.add(map);
								}
							}


						}
						reader.close();
					} catch (UnsupportedEncodingException e) {
						logger.error("UnsupportedEncodingException", e);
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException", e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("IOException", e);
					}
					if(list!=null){
						if (list.size()>0) {
							//数据通过dbm3接口进行存储
							Map data=new HashMap<>();
							data.put("dbName",tableName);
							data.put("data", list);
							dbm3Dao.saveByDbName(data);
							System.out.println(Product);
							logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
						}
					}
				}
			}
		}
	}

	public static void main(String[] a){
		new MofcomDataFetch().fetchData(2015);					
	}
}
