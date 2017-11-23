package com.bric.crawler.download;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.bric.analyser.wholePriceAnalyse;
import com.bric.crawler.MapInit;
import com.bric.intoDB.Dbm3InterfaceDao;
import com.bric.intoDB.Price21foodDao;
import com.bric.jdbc.JdbcRunner;
import com.bric.temporary.Query21foodPrice;
import com.bric.util.*;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Price21foodDataFetch {

	private static String MarketsUrl ="http://www.21food.cn/news/price.jsp?product=";
	private Logger logger = Logger.getLogger(Price21foodDataFetch.class);
	private ProxyManagerUtil proxyManagerUtil = new ProxyManagerUtil();
	private MyHttpClient myHttpClient = new MyHttpClient();

    private SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();

	/**
	 * 主方法
	 */
	public void fetchAndSave21foodData(){
		Date today = formatData.getDate(0);
		String todayStr = DateTimeUtil.formatDate(today, "yyyyMMdd");
		//today
		Price21foodDataFetch fetch=new Price21foodDataFetch();
		fetch.fetchData(today);
		fetch.save21foodPriceData(new File(Constants.MARKETPRICEDATA_21FOOD_ROOT), todayStr);
		Query21foodPrice.loadAreaAndPrice(todayStr);

	}

	public Parser getHTMLParser(String pageURL, String encoding) {
		System.out.println("尝试下载页面："+pageURL);
		Parser parser;
		int i=0;
		while(true){
			try{ 
				//利用HttpClient得到页面内容
				String proxyStr = proxyManagerUtil.getProxyStr();
				String htmlContent = myHttpClient.getHtmlByHttpClient(pageURL, encoding, proxyStr);
				if(htmlContent.equals("0")){
					return null;
				}
				if("123".contains(htmlContent)){
					proxyManagerUtil.changeProxy();
					continue;
				}
				parser = Parser.createParser(htmlContent, encoding); 
				System.out.println("下载完成");
				return parser;
			}catch(Exception e){
				i++;
				if(i>50){
					System.out.println("代理已经换了n个了，还是不行，退出");
					return null;
				}
				System.out.println("其他错误");
				e.printStackTrace();				
				proxyManagerUtil.changeProxy();
			}
		}
	}	
	
	private String getMarketPriceAsStr(String varName,Date date){

        Parser parser = new Parser();
		NodeList tableList;
		try {
			String url = MarketsUrl+URLEncoder.encode(varName, "gbk");
			System.out.println(url);
			parser = getHTMLParser(url,"utf-8");
			if(parser == null){
				logger.error("抓取 "+varName+" 时出错。");
				return null;
			}


            //将htmlparser的parser转换为Jsoup的document
            String html=parser.parse(new TagNameFilter("html")).toHtml();
            Document document= Jsoup.parse(html);
            //将doucument包装为JxDocument来解析
            JXDocument doc=new JXDocument(document);
            //获取页面表格每一行的信息
            List list=doc.sel("//div[@class='gs_top_t2_left']/div[2]/div[2]/ul/li/table/tbody/tr/allText()");
            StringBuffer buffer=new StringBuffer();
            for(Object o:list){
                String trStr=o.toString().replaceAll("\\s",",");
                //得到数据类似： （火龙果,北京顺鑫石门农副产品批发市场,11,8.6,9,2017-03-17） 替换最后的时间信息为20170317
                String timeStr=trStr.substring(trStr.lastIndexOf(",")+1);


                trStr=trStr.substring(0,trStr.lastIndexOf(","))+","+formatToInt(timeStr);
                buffer.append(trStr);
                buffer.append("\n");
            }
            return buffer.toString();
		} catch (ParserException e) {
			logger.error("ParserException while fetching "+varName+":", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException "+varName+":", e);
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.error("error kind:"+varName);
		}
        catch (XpathSyntaxErrorException e) {
            logger.error("XpathSyntaxErrorException");
        }
        return null;
	}


    /**
     * 转换时间字符串的方法
     * 将2017-03-17 转换为20170317
     */
    public String formatToInt(String time){
        try {
            return format.format(parser.parse(time));
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     *
     * @param date 该参数在目标网址更新后只用来作为保存文件的文件名
     */
	public void fetchData(Date date){
		String dateStr = DateTimeUtil.formatDate(date, "yyyyMMdd");
		logger.info("**********fetch start, date "+dateStr+"*********");
		for(String kind:MapInit.nameReflectMap21food.keySet()){
			Map<String, Integer> innerMap = MapInit.nameReflectMap21food.get(kind);
			
			for (String varName : innerMap.keySet()) {
				try {
					logger.info(varName+ " .sleep 10 seconds ...");
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					logger.error("InterruptedException:", e);
				}
				String content = getMarketPriceAsStr(varName,date);
				if(content == null){
					logger.error("跳过保存： "+varName);
					continue;
				}
//				String dirString = Constants.MARKETPRICEDATA_21FOOD_ROOT + "\\" + kind + "\\" + varName + "\\";//windows
				String dirString = Constants.MARKETPRICEDATA_21FOOD_ROOT 
						+ Constants.FILE_SEPARATOR + kind 
						+ Constants.FILE_SEPARATOR + varName 
						+ Constants.FILE_SEPARATOR;//linux
				try {
					if (new File(dirString + dateStr + ".txt").exists())
						logger.warn("Overwrite: "+ varName);
					FileStrIO.saveStringToFile(content, dirString,	dateStr + ".txt");
					logger.info("data saved: " + varName);
				} catch (IOException e) {
					logger.error("IOException while saving "+varName+" data:", e);
				}
			}
			logger.info("====== "+ kind + " fetched ======");
			
			try {
				logger.info("sleep one minute...");
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				logger.error("InterruptedException:", e);
			}
			logger.info("continue fetch...");
		}
		logger.info("******fetch succeed!******");
	}


	public void save21foodPriceData(File file,String today){

		JdbcRunner jdbc=new JdbcRunner();

		try {
			for (File file2 : file.listFiles()) {
				if (file2.isDirectory()) {
					save21foodPriceData(file2, today);
				} else {
					List list=new ArrayList<>();
					String Product = file2.getParentFile().getName();
					String KindName = file2.getParentFile().getParentFile().getName();
					String tableName = MapInit.marketPrice21foodTableMap.get(KindName);
					int TimeInt = Integer.valueOf(file2.getName().split("\\.")[0]);
					int KindId = MapInit.nameKindIdMap21food.get(KindName);
					Integer VarId = MapInit.nameReflectMap21food.get(KindName).get(Product);
					if (TimeInt == Integer.valueOf(today)) {
						InputStreamReader inputStreamReader;
						try {
							inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.FILE_ENCODING);
							BufferedReader reader = new BufferedReader(inputStreamReader);
							String str = "";
							while ((str = reader.readLine()) != null) {
								String[] array = str.split(",");

								String Area = formatData.getAreaByMarket(array[1]);
								String Province = formatData.getProvinceByArea(Area);

								Map<String,String> map=new HashMap<>();
								map.put("KindId",KindId+"");
								map.put("VarId",VarId+"");
								map.put("TimeInt",array[5]);
								map.put("Province",Province);
								map.put("Area",Area);
								map.put("产品",array[0]);
								map.put("市场",array[1]);
								//此属性在目标网址更新后没有了  所以注释掉
//                                  map.put("产地规格",VarId);
								map.put("最高价",array[2]);
								map.put("最低价",array[3]);
								map.put("平均价",array[4]);
								list.add(map);


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
						Map data=new HashMap<>();
						data.put("dbName",tableName);
						data.put("data", list);
						data.put("condition", "TimeInt,VarId,市场");
						dbm3Dao.saveByDbName(data);
						System.out.println(Product);
						new wholePriceAnalyse().analyseWholePrice(today, String.valueOf(VarId), tableName, MapInit.analyseDataMap.get(Product), MapInit.productToKindMap.get(Product));
						logger.info("insert " + file2.getParentFile().getName() + "-" + file2.getName() + " data into table " + tableName);
					}

				}
			}
		}finally{
			jdbc.release();
		}
	}

	/**
     * 测试用代码
     * @param a
     */
	public static void main(String[] a){		
		Price21foodDataFetch fetch = new Price21foodDataFetch();
		String[] dates = {"20170327","20151130","20151129","20151128","20151127","20151126","20151125"};
        for(String dateStr:dates){
            fetch.fetchData(fetch.parseDateTime(dateStr, "yyyyMMdd"));
            new Price21foodDao().save21foodPriceData(new File(Constants.MARKETPRICEDATA_21FOOD_ROOT), dateStr);
            Query21foodPrice.loadAreaAndPrice(dateStr);
        }
	}
	public Date parseDateTime(String dateString, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		if (dateString == null || dateString.trim().equals("")) {
			return null;
		}
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException ignored) {
			ignored.printStackTrace();
		}
		return date;
	}


}
