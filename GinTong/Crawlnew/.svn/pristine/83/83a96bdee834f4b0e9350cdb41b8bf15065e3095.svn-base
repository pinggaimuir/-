package cn.futures.data.importor.crawler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 美国动物油脂数据（对应爬虫需求文档《数据抓取需求文档-美国动物油脂-陈郁.docx》）
 * @author bric_yangyulin
 * @date 2016-10-10
 * */
public class USDAFatsAndOils {
	private static String url = "http://usda.mannlib.cornell.edu/MannUsda/viewDocumentInfo.do?documentID=1902";
	private static String className = USDAFatsAndOils.class.getName();
	private static String encoding = "utf-8";
	private static String varName = "美国动物油脂";
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(USDAFatsAndOils.class);
	private static Map<String, String> cnNameByStr = new HashMap<String, String>();//标识英文字符串-中文名
	
	static{
		cnNameByStr.put("Tallow, inedible", "不可食用牛羊油脂产消库存");
		cnNameByStr.put("Tallow, technical", "工业用牛羊油脂产消库存");
		cnNameByStr.put("Yellow grease", "黄油脂产消库存");
		cnNameByStr.put("Choice white grease", "精选白油脂产消及库存");
		cnNameByStr.put("Tallow, edible", "可食用牛羊油脂产消库存");
		cnNameByStr.put("Other grease", "其他动物油脂产消库存");
		cnNameByStr.put("Poultry fats", "禽类脂肪产消库存");
		cnNameByStr.put("Lard", "猪油产消及库存");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_USDAFATSANSOILS)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("美国动物油脂数据1", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到美国动物油脂数据1爬虫的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到美国动物油脂数据1爬虫的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					String curUrl = this.fetchUrl();
					this.fetchData(curUrl);
				} catch(Exception e) {
					LOG.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, varName, null, "发生未知异常");
				}
			}else{
				LOG.info("抓取美国动物油脂数据1的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取美国动物油脂数据1的定时器已关闭");
			}
		}
	}
	
	/**
	 * 获取最新发布的信息的链接。
	 * */
	public String fetchUrl(){
		String curUrl = null;
		String[] filters = {"div", "id", "latest"};
		String divStr = fetchUtil.getPrimaryContent(0, url, encoding, varName, filters, null, 0);
		String compStr = "<a href=\"(http://usda.mannlib.cornell.edu/usda/current/FatsOils/FatsOils-(\\d{2})-(\\d{2})-(\\d{4}).txt)\">";
		List<String> rslt = RegexUtil.getMatchStr(divStr, compStr, new int[]{1, 2, 3, 4});
		curUrl = rslt.get(0);
		return curUrl;
	}
	
	/**
	 * 抓取并解析数据。
	 * @param curUrl 指定的url(可用于补历史数据)
	 * */
	public void fetchData(String curUrl){
		String content = fetchUtil.getCompleteContent(0, curUrl, encoding, varName + "_detail");
		int timeInt = 0;//时间序列
		String[] fileLines = content.split("\n");//记录文件每行的内容
		int startLineId = -1;//记录标识所找表格起始位置的行id。
		int columnNeedId = -1;//记录所需数据所在列id。
		for(int lineId = 0; lineId < fileLines.length; lineId++){
			if(startLineId == -1){
				if(fileLines[lineId].contains("Animal Fats and Oils Production, Consumption and Stocks - United States:")){
					//找到对应表格起始部位， 开始逐行解析
					startLineId = lineId;
					String[] columnNames = fileLines[lineId + 2].split(":");
					columnNeedId = columnNames.length-1;//记录所需列号
					List<String> timeNeed = RegexUtil.getMatchStr(columnNames[columnNeedId], "(\\b[a-zA-z]+\\b)\\s+(\\d+)", new int[]{0, 1, 2});
					DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
			        try {
						Date date = dateFormat.parse(timeNeed.get(1) + " " + timeNeed.get(2));
						String timeIntStr = DateTimeUtil.formatDate(date, DateTimeUtil.YYYYMM);
						timeInt = Integer.parseInt(timeIntStr);
					} catch (ParseException e) {
						LOG.error("解析时间序列是发生异常。", e);
						RecordCrawlResult.recordFailData(className, varName, null, "解析时间序列是发生异常。");
						break;
					}
					lineId += 3;//跳过列名栏三行
				}
			} else if(fileLines[lineId].matches("-+")) {
				break;//表格解析完成，跳出。
			} else {
				//待解析表格内数据
				if(cnNameByStr.containsKey(fileLines[lineId].replaceAll("\\s*:\\s*", ""))){
					System.out.println(fileLines[lineId].replaceAll("\\s*:\\s*", ""));
					//是所需中文名，解析其数据
					String compStr = "(\\b[0-9,]+\\b)|(\\(D\\))|(\\(S\\))";
					Map<String, String> dataMap = new HashMap<String, String>();
					List<String> productionList = null;
					List<String> processingList = null;
					List<String> sotckList = null;
					String specialColName = null;//有一个数据对应列名不定，该项记录对应列名
					if(fileLines[lineId+1].contains("Production...") 
							&& fileLines[lineId+2].contains("Removed for inedible use")
							&& fileLines[lineId+3].contains("Stocks on hand end of month...")){
						productionList = RegexUtil.getMatchStr(fileLines[lineId+1], compStr);
						processingList = RegexUtil.getMatchStr(fileLines[lineId+2], compStr);
						sotckList = RegexUtil.getMatchStr(fileLines[lineId+3], compStr);
						specialColName = "非食用加工量";
					} else if(fileLines[lineId+1].contains("Production...") 
							&& fileLines[lineId+2].contains("Removed for use in processing...")
							&& fileLines[lineId+5].contains("Stocks on hand end of month...")) {
						productionList = RegexUtil.getMatchStr(fileLines[lineId+1], compStr);
						processingList = RegexUtil.getMatchStr(fileLines[lineId+2], compStr);
						sotckList = RegexUtil.getMatchStr(fileLines[lineId+5], compStr);
						specialColName = "加工量";
					} else {
						continue;
					}
					if(productionList.get(columnNeedId-1) != null 
							&& !productionList.get(columnNeedId-1).equals("(D)") 
							&& !productionList.get(columnNeedId-1).equals("(S)")){
						productionList.get(columnNeedId-1);
						dataMap.put("产量", productionList.get(columnNeedId-1).replace(",", ""));
					}
					if(processingList.get(columnNeedId-1) != null 
							&& !processingList.get(columnNeedId-1).equals("(D)") 
							&& !processingList.get(columnNeedId-1).equals("(S)")){
						dataMap.put(specialColName, processingList.get(columnNeedId-1).replace(",", ""));
					}
					if(sotckList.get(columnNeedId-1) != null 
							&& !sotckList.get(columnNeedId-1).equals("(D)") 
							&& !sotckList.get(columnNeedId-1).equals("(S)")){
						dataMap.put("月末库存", sotckList.get(columnNeedId-1).replace(",", ""));
					}
					if(timeInt != 0 && dataMap.size() > 0){
						System.out.println(dataMap.toString());
						dao.saveOrUpdateByDataMap(varName, cnNameByStr.get(fileLines[lineId].replaceAll("\\s*:\\s*", "")), timeInt, dataMap);
					} else {
						RecordCrawlResult.recordFailData(className, varName, cnNameByStr.get(fileLines[lineId].replaceAll("\\s*:\\s*", "")), "数据为空，timeInt为" + timeInt);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		USDAFatsAndOils u = new USDAFatsAndOils();
		
		//补当月数据应抓取数据
		u.start();
		
		//补历史数据
//		String url = "http://usda.mannlib.cornell.edu/usda/nass/FatsOils//2010s/2016/FatsOils-09-01-2016.txt";
//		u.fetchData(url);
        
	}
}
