package cn.futures.data.importor.crawler;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**	
	* @description	大蒜出口量及出口额相关数据
	* @author 		xjlong 
    * @date 		2016年8月27日  
*/
public class GarlicExportDataFetch {
	private static final String className = GarlicExportDataFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "大蒜";
	private Log logger = LogFactory.getLog(GarlicExportDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	
	/**
 	*鲜或冷藏的大蒜出口量（分省）&& 鲜或冷藏的大蒜出口额（分省）
 	*鲜或冷藏的大蒜出口量（分海关）&& 鲜或冷藏的大蒜出口额（分海关）
 	*鲜或冷藏的大蒜出口量（分国别）&& 鲜或冷藏的大蒜出口额（分国别）
 */
	private void fetchFreshOrFrozenExportData(){
		int totalPage = 0;
		String timeInt = null;
		String cnName_Weight = null;
		String cnName_Price = null;
		String[] filters = {"ul","li"};
		String[] rowColChoose = null;
		int varId = Variety.getVaridByName(varName);
		List<String> list = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://www.51garlic.com/shuju/cksj/";
		String contents = dataFetchUtil.getPrimaryContent(0, baseUrl, "gb2312", varName, filters, rowColChoose,0);
		if(contents != null && !contents.equals("")){
			String regLi = "(?<=<li>)[\\s\\S]*?(?=</li>)";//正则-提取<li>标签中的内容
			list = RegexUtil.getMatchStr(contents, regLi);
			for(int j = 0;j<10;j++){
				String pageUrl = "http://www.51garlic.com"+match(list.get(j), "A", "href");
				String producePlace = match(list.get(j), "A", "title");
				try {
					timeInt = this.matchDate(producePlace);
					if(Integer.parseInt(timeInt.substring(4)) < 10){
						timeInt = timeInt.substring(0, 4)+"0"+timeInt.substring(4);
					}
				} catch (ParseException e) {
					logger.error("从标题提取报价日期失败",e);
				}
				String[] filterTags = {"table","tr"};
				String[] rowCol = {"0","111"};
				if(producePlace.contains("大蒜出口数据（按省市）") || producePlace.contains("大蒜出口数据（按海关）")
						|| producePlace.contains("鲜或冷藏的大蒜出口数据（按国家）")){
					String tableContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,2);
					String fetchContents = null;
					try {
						fetchContents = (tableContents).substring(48);
					} catch (Exception e) {
						logger.error("截取鲜或冷藏的大蒜数据失败。。。",e);
					}
					String fetchAmount = null;
					if(producePlace.contains("大蒜出口数据（按省市）")){
						fetchAmount = fetchContents.substring(fetchContents.indexOf("37"), fetchContents.indexOf("干大蒜"));//截取鲜或冷藏大蒜数据
						cnName_Weight = "鲜或冷藏的大蒜出口量（分省）";
						cnName_Price = "鲜或冷藏的大蒜出口额（分省）";
					}else if(producePlace.contains("大蒜出口数据（按海关）")){
						fetchAmount = fetchContents.substring(fetchContents.indexOf("42"), fetchContents.indexOf("干大蒜"));//截取鲜或冷藏大蒜数据
						cnName_Weight = "鲜或冷藏的大蒜出口量（分海关）";
						cnName_Price = "鲜或冷藏的大蒜出口额（分海关）";
					}else{
						fetchAmount = (tableContents).substring(35);
						cnName_Weight = "鲜或冷藏的大蒜出口量（分国别）";
						cnName_Price = "鲜或冷藏的大蒜出口额（分国别）";
					}
					String b[]=(fetchAmount.split("\n"));
					for(int i =0; i<b.length;i++){
						String pName = "";
						if(producePlace.contains("鲜或冷藏的大蒜出口数据（按国家）")){
							pName = b[i].split(",")[0].substring(4);//国家
						}else{
							pName = b[i].split(",")[0].substring(3);//省份,海关
						}
						String totalAmount = b[i].split(",")[1];//出口量
						String totalExports = b[i].split(",")[2];//出口额
						dataMap.put(pName, totalAmount);
						if(!dataMap.isEmpty()){
							//保存鲜或冷藏出口量数据
							logger.info("正在保存--"+pName+"--鲜或冷藏大蒜出口量数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Weight , Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Weight, "没有抓取到大蒜相关数据");
						}
						dataMap.put(pName, totalExports);
						if(!dataMap.isEmpty()){
							//保存鲜或冷藏出口额数据
							logger.info("正在保存--"+pName+"--鲜或冷藏大蒜出口额数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Price, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Price, "没有抓取到大蒜相关数据");
						}
					}
				}
			}
		}
	}
	/**
	  	干大蒜出口量（分省）&& 干大蒜出口额（分省）
		干大蒜出口量（分海关）&& 干大蒜出口额（分海关）
		干大蒜出口量（分国别）&& 干大蒜出口额（分国别）
	 */
	private void fetchDryGarlicExportData(){
		int totalPage = 0;
		String timeInt = null;
		String cnName_Weight = null;
		String cnName_Price = null;
		String[] filters = {"ul","li"};
		String[] rowColChoose = null;
		int varId = Variety.getVaridByName(varName);
		List<String> list = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://www.51garlic.com/shuju/cksj/";
		String contents = dataFetchUtil.getPrimaryContent(0, baseUrl, "gb2312", varName, filters, rowColChoose,0);
		if(contents != null && !contents.equals("")){
			String regLi = "(?<=<li>)[\\s\\S]*?(?=</li>)";//正则-提取<li>标签中的内容
			list = RegexUtil.getMatchStr(contents, regLi);
			for(int j = 0;j<10;j++){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String pageUrl = "http://www.51garlic.com"+match(list.get(j), "A", "href");
				String producePlace = match(list.get(j), "A", "title");
				try {
					timeInt = this.matchDate(producePlace);
					if(Integer.parseInt(timeInt.substring(4)) < 10){
						timeInt = timeInt.substring(0, 4)+"0"+timeInt.substring(4);
					}
				} catch (ParseException e) {
					logger.error("从标题提取报价日期失败",e);
				}
				String[] filterTags = {"table","tr"};
				String[] rowCol = {"0","111"};
				if(producePlace.contains("大蒜出口数据（按省市）") || producePlace.contains("大蒜出口数据（按海关）")
						|| producePlace.contains("其它大蒜出口数据（按国家）")){
					String tableContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,2);
					String fetchContents = null;
					try {
						fetchContents = (tableContents).substring(tableContents.indexOf("干大蒜"),tableContents.indexOf("盐水大蒜"))
								.substring(4);
					} catch (Exception e) {
						logger.error("截取干大蒜数据失败。。。",e);
					}
					
					String fetchAmount = null;
					if(producePlace.contains("大蒜出口数据（按省市）")){
						cnName_Weight = "干大蒜出口量（分省）";
						cnName_Price = "干大蒜出口额（分省）";
					}else if(producePlace.contains("大蒜出口数据（按海关）")){
						cnName_Weight = "干大蒜出口量（分海关）";
						cnName_Price = "干大蒜出口额（分海关）";
					}else{
						cnName_Weight = "干大蒜出口量（分国别）";
						cnName_Price = "干大蒜出口额（分国别）";
					}
					String b[]=(fetchContents.split("\n"));
					for(int i =0; i<b.length;i++){
						String pName = "";
						if(producePlace.contains("其它大蒜出口数据（按国家）")){
							pName = b[i].split(",")[0].substring(4);//国家
						}if(producePlace.contains("大蒜出口数据（按海关）")){
							pName = b[i].split(",")[0].substring(3);//省份,海关
						}if(producePlace.contains("大蒜出口数据（按省市）")){
							pName = b[i].split(",")[0].substring(3).substring(0, 2);//省份,海关
						}
						String totalAmount = b[i].split(",")[1];//出口量
						String totalExports = b[i].split(",")[2];//出口额
						dataMap.put(pName, totalAmount);
						if(!dataMap.isEmpty()){
							//干大蒜出口量数据
							logger.info("正在保存--"+pName+"--干大蒜出口量数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Weight , Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Weight, "没有抓取到大蒜相关数据");
						}
						dataMap.put(pName, totalExports);
						if(!dataMap.isEmpty()){
							//干大蒜出口额数据
							logger.info("正在保存--"+pName+"--干大蒜出口额数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Price, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Price, "没有抓取到大蒜相关数据");
						}
					}
				}
			}
		}
	}
	/**
	   	盐水大蒜出口量（分省） && 盐水大蒜出口额（分省）
		盐水大蒜出口量（分海关）&& 盐水大蒜出口额（分海关）	
		盐水大蒜出口量（分国别）&& 盐水大蒜出口额（分国别）
	 * @throws InterruptedException 
	 */
	private void fetchSaltWaterExportData(){
		int totalPage = 0;
		String timeInt = null;
		String cnName_Weight = null;
		String cnName_Price = null;
		String[] filters = {"ul","li"};
		String[] rowColChoose = null;
		int varId = Variety.getVaridByName(varName);
		List<String> list = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://www.51garlic.com/shuju/cksj/";
		String contents = dataFetchUtil.getPrimaryContent(0, baseUrl, "gb2312", varName, filters, rowColChoose,0);
		if(contents != null && !contents.equals("")){
			String regLi = "(?<=<li>)[\\s\\S]*?(?=</li>)";//正则-提取<li>标签中的内容
			list = RegexUtil.getMatchStr(contents, regLi);
			for(int j = 0;j < 10;j++){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String pageUrl = "http://www.51garlic.com"+match(list.get(j), "A", "href");
				String producePlace = match(list.get(j), "A", "title");
				try {
					timeInt = this.matchDate(producePlace);
					if(Integer.parseInt(timeInt.substring(4)) < 10){
						timeInt = timeInt.substring(0, 4)+"0"+timeInt.substring(4);
					}
				} catch (ParseException e) {
					logger.error("从标题提取报价日期失败",e);
				}
				String[] filterTags = {"table","tr"};
				String[] rowCol = {"0","111"};
				if(producePlace.contains("大蒜出口数据（按省市）") || producePlace.contains("大蒜出口数据（按海关）")
						|| producePlace.contains("其它大蒜出口数据（按国家）")){
					String tableContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,2);
					String fetchContents = null;
					try {
						fetchContents = (tableContents).substring(tableContents.indexOf("盐水大蒜"),tableContents.indexOf("用醋或醋酸制作或保藏的大蒜"))
												.substring(5);
					} catch (Exception e) {
						logger.error("截取盐水大蒜数据失败。。。",e);
					}
					String fetchAmount = null;
					if(producePlace.contains("大蒜出口数据（按省市）")){
						cnName_Weight = "盐水大蒜出口量（分省）";
						cnName_Price = "盐水大蒜出口额（分省）";
					}else if(producePlace.contains("大蒜出口数据（按海关）")){
						cnName_Weight = "盐水大蒜出口量（分海关）";
						cnName_Price = "盐水大蒜出口额（分海关）";
					}else{
						cnName_Weight = "盐水大蒜出口量（分国别）";
						cnName_Price = "盐水大蒜出口额（分国别）";
					}
					String b[]=(fetchContents.split("\n"));
					for(int i =0; i<b.length;i++){
						String pName = "";
						if(producePlace.contains("其它大蒜出口数据（按国家）")){
							pName = b[i].split(",")[0].substring(4);//国家
						}if(producePlace.contains("大蒜出口数据（按海关）")){
							pName = b[i].split(",")[0].substring(3);//海关
						}if(producePlace.contains("大蒜出口数据（分省）")){
							pName = b[i].split(",")[0].substring(3).substring(0, 2);//省份
						}
						String totalAmount = b[i].split(",")[1];//出口量
						String totalExports = b[i].split(",")[2];//出口额
						dataMap.put(pName, totalAmount);
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+pName+"--盐水大蒜出口量数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Weight , Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Weight, "没有抓取到大蒜相关数据");
						}
						dataMap.put(pName, totalExports);
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+pName+"--盐水大蒜出口额数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Price, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Price, "没有抓取到大蒜相关数据");
						}
					}
				}
			}
		}
	}
	
	/**
	  	用醋或醋酸制作或保藏的大蒜出口量（分省） && 用醋或醋酸制作或保藏的大蒜出口额（分省）
		用醋或醋酸制作或保藏的大蒜出口量（分海关） && 用醋或醋酸制作或保藏的大蒜出口额（分海关）
		用醋或醋酸制作或保藏的大蒜出口量（分国别） && 用醋或醋酸制作或保藏的大蒜出口额（分国别）
	*/
	private void fetchMakeByVinegarExportData() {
		int totalPage = 0;
		String timeInt = null;
		String cnName_Weight = null;
		String cnName_Price = null;
		String[] filters = {"ul","li"};
		String[] rowColChoose = null;
		int varId = Variety.getVaridByName(varName);
		List<String> list = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://www.51garlic.com/shuju/cksj/";
		String contents = dataFetchUtil.getPrimaryContent(0, baseUrl, "gb2312", varName, filters, rowColChoose,0);
		if(contents != null && !contents.equals("")){
			String regLi = "(?<=<li>)[\\s\\S]*?(?=</li>)";//正则-提取<li>标签中的内容
			list = RegexUtil.getMatchStr(contents, regLi);
			for(int j = 0;j < 10;j++){
				String pageUrl = "http://www.51garlic.com"+match(list.get(j), "A", "href");
				String producePlace = match(list.get(j), "A", "title");
				try {
					timeInt = this.matchDate(producePlace);
					if(Integer.parseInt(timeInt.substring(4)) < 10){
						timeInt = timeInt.substring(0, 4)+"0"+timeInt.substring(4);
					}
				}catch (ParseException e) {
					logger.error("从标题提取报价日期失败",e);
				}
				String[] filterTags = {"table","tr"};
				String[] rowCol = {"0","111"};
				if(producePlace.contains("大蒜出口数据（按省市）") || producePlace.contains("大蒜出口数据（按海关）")
						|| producePlace.contains("其它大蒜出口数据（按国家）")){
					String tableContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,2);
					String fetchContents = null;
					try {
						fetchContents = (tableContents).substring(tableContents.indexOf("用醋或醋酸制作或保藏的大蒜"))
								.substring(14);
					} catch (Exception e) {
						logger.error("截取用醋或醋酸制作或保藏的大蒜数据失败。。。",e);
					}
					
					String fetchAmount = null;
					if(producePlace.contains("大蒜出口数据（按省市）")){
						cnName_Weight = "用醋或醋酸制作或保藏的大蒜出口量（分省）";
						cnName_Price = "用醋或醋酸制作或保藏的大蒜出口额（分省）";
					}else if(producePlace.contains("大蒜出口数据（按海关）")){
						cnName_Weight = "用醋或醋酸制作或保藏的大蒜出口量（分海关）";
						cnName_Price = "用醋或醋酸制作或保藏的大蒜出口额（分海关）";
					}else{
						cnName_Weight = "用醋或醋酸制作或保藏的大蒜出口量（分国别）";
						cnName_Price = "用醋或醋酸制作或保藏的大蒜出口额（分国别）";
					}
					String b[]=(fetchContents.split("\n"));
					for(int i =0; i<b.length;i++){
						String pName = "";
						if(producePlace.contains("其它大蒜出口数据（按国家）")){
							pName = b[i].split(",")[0].substring(4);//国家
						}if(producePlace.contains("大蒜出口数据（按省市）")){
							pName = b[i].split(",")[0].substring(3).replace("省","").replace("市","");//省份
							System.out.println(">>"+pName);
						}if(producePlace.contains("大蒜出口数据（按海关）")){
							pName = b[i].split(",")[0].substring(3);//海关
						}
						String totalAmount = b[i].split(",")[1];//出口量
						String totalExports = b[i].split(",")[2];//出口额
						dataMap.put(pName, totalAmount);
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+pName+"--保存用醋或醋酸制作或保藏的大蒜出口量数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Weight , Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							//RecordCrawlResult.recordFailData(className, varName, cnName_Weight, "没有抓取到大蒜相关数据");
						}
						dataMap.put(pName, totalExports);
						if(!dataMap.isEmpty()){
							logger.info("正在保存--"+pName+"--保存用醋或醋酸制作或保藏的大蒜出口额数据...");
							dao.saveOrUpdateByDataMap(varId, cnName_Price, Integer.parseInt(timeInt), dataMap);
							dataMap.clear();
						}else {
							RecordCrawlResult.recordFailData(className, varName, cnName_Price, "没有抓取到大蒜相关数据");
						}
					}
				}
			}
		}
	}
	//大蒜出口数据（按总量）
	public void fetchExportDataByCount(){
		int totalPage = 0;
		String timeInt = null;
		String cnName_Weight = null;
		String cnName_Price = null;
		String[] filters = {"ul","li"};
		String[] rowColChoose = null;
		int varId = Variety.getVaridByName(varName);
		List<String> list = new ArrayList<String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String baseUrl = "http://www.51garlic.com/shuju/cksj/";
		String contents = dataFetchUtil.getPrimaryContent(0, baseUrl, "gb2312", varName, filters, rowColChoose,0);
		if(contents != null && !contents.equals("")){
			String regLi = "(?<=<li>)[\\s\\S]*?(?=</li>)";//正则-提取<li>标签中的内容
			list = RegexUtil.getMatchStr(contents, regLi);
			for(int j = 0;j<10;j++){
				String pageUrl = "http://www.51garlic.com"+match(list.get(j), "A", "href");
				String producePlace = match(list.get(j), "A", "title");
				try {
					timeInt = this.matchDate(producePlace);
					if(Integer.parseInt(timeInt.substring(4)) < 10){
						timeInt = timeInt.substring(0, 4)+"0"+timeInt.substring(4);
					}
				} catch (ParseException e) {
					logger.error("从标题提取报价日期失败",e);
				}
				String[] filterTags = {"table","tr"};
				String[] rowCol = {"0","111"};
				if(producePlace.contains("大蒜出口数据（按总量）")){
					String tableContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "gb2312", varName, filterTags, rowCol,2);
					String fetchContents = null;
					try {
						fetchContents = (tableContents).substring(48);
					} catch (Exception e) {
						logger.error("截取鲜或冷藏的大蒜数据失败。。。",e);
					}
				}
			}
		}
	}
	/*
	 * 定时器：每月十号十点半更新数据
	 * */
	@Scheduled
	(cron=CrawlScheduler.CRON_CINDER_MONTH)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("抓取大蒜出口量出口额相关数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到大蒜出口量出口额相关数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到大蒜出口量出口额相关数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchDryGarlicExportData();
					fetchFreshOrFrozenExportData();
					fetchSaltWaterExportData();
					fetchMakeByVinegarExportData();
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取大蒜出口量出口额相关数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取大蒜出口量出口额相关数据的定时器已关闭");
			}
		}
	}
	
	/**
 	* @description	从一个字符串中提取出一个标签属性的值
	* @param  		source,字符串 element，标签名 attr,属性名
	* @return		result，属性值
	*/
	 private  String match(String source, String element, String attr) {  
	    String result = null;  
	    String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";  
	    Matcher m = Pattern.compile(reg).matcher(source);  
	    while (m.find()) {  
	        result =  m.group(1);
	    }  
	    return result;  
	}
	//四舍五入，保留两位小数点
	public double scaleNumber(double data){
		BigDecimal b = new BigDecimal(data); 
		return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	/**
	 	* @description	从字符串中提取出所有数字
		* @param  		contents,字符串
		* @throws ParseException 
	 */
	public String matchDate(String contents) throws ParseException{
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(contents); 
		String timeint = m.replaceAll("").trim();
		return m.replaceAll("").trim();
	}
	
	public static void main(String[] args) {
		GarlicExportDataFetch garlic = new GarlicExportDataFetch();
		garlic.fetchExportDataByCount();
		//garlic.fetchFreshOrFrozenExportData();
		//garlic.fetchSaltWaterExportData();
		//garlic.fetchDryGarlicExportData();
		//garlic.fetchMakeByVinegarExportData();
	}
}
