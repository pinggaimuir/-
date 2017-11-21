package cn.futures.data.importor.crawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 油料油脂-美国大豆-现货理论榨利(该数据源无历史数据)
 * @date 2016-12-08
 * @author bric_yangyulin
 * */
public class USBeansMargin {
	
	private static final String url = "https://www.ams.usda.gov/mnreports/gx_gr211.txt";
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static String className = USBeansMargin.class.getName();
	private static final Logger LOG = Logger.getLogger(USBeansMargin.class);
	private static final Map<String, String> numByMon = new HashMap<String, String>();
	{
		numByMon.put("Jan", "01");
		numByMon.put("Feb", "02");
		numByMon.put("Mar", "03");
		numByMon.put("Apr", "04");
		numByMon.put("May", "05");
		numByMon.put("Jun", "06");
		numByMon.put("Jul", "07");
		numByMon.put("Aug", "08");
		numByMon.put("Sep", "09");
		numByMon.put("Oct", "10");
		numByMon.put("Nov", "11");
		numByMon.put("Dec", "12");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_USBEANSMARGIN)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("美国大豆现货理论榨利", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到美国大豆现货理论榨利在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					fetchData();
				} catch(Exception e){
					LOG.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, "美国大豆", "现货理论榨利", "发生未知异常。");
				}
			}else{
				LOG.info("抓取美国大豆现货理论榨利的定时器已关闭");
			}
		}
	}
	
	public void fetchData(){
		String varName = "美国大豆";//品种名
		String cnName = "现货理论榨利";//中文名
		int timeInt = 0;//时间序列
		String cont = fetchUtil.getCompleteContent(0, url, Constants.ENCODE_UTF8, "美国大豆_现货理论榨利.txt");
		String timeRegex = "This week\\s+Last week\\s+Last year\\s+Unit\\s+([a-zA-Z]{3,4}) (\\d{1,2}), (\\d{4})";//用于解析这周时间序列的正则式
		List<String> timeList = RegexUtil.getMatchStr(cont, timeRegex, new int[]{1, 2, 3});
		if(timeList != null && timeList.size() == 3){
			String month = timeList.get(0);
			month = numByMon.get(month);//月份
			String day = timeList.get(1);
			if(day != null && day.matches("\\d")) {
				day = '0' + day;
			} else if(day == null || !day.matches("\\d\\d")) {
				LOG.info("格式发生变动，抓取失败。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "格式发生变动，抓取失败。");
				return;
			}
			String year = timeList.get(2);
			if(!year.matches("\\d{4}")){
				LOG.info("格式发生变动，抓取失败。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "格式发生变动，抓取失败。");
				return;
			}
			timeInt = Integer.parseInt(year + month + day);
			String[][] colnames = new String[][]{{"中伊利诺伊毛豆油价格美分每磅", "豆油出率磅每蒲式耳", 
				"豆油价值美元", "中伊利诺伊48%豆粕散货价美元每吨", "豆粕出率磅每蒲式耳", "豆粕价值美元", 
				"中伊利诺伊1号黄豆卡车价美元每蒲", "压榨总价值-大豆成本美元每蒲式耳"}, 
				{"Soybean oil, crude\\s*tank cars & trucks\\s*Central IL.", 
					"Oil yield per\\s*bushel crushed", "Value from bushel\\s*of soybeans", 
					"48% Soybean Meal\\s*unrestricted, bulk\\s*Central IL.", 
					"Meal yield per\\s*bushel crushed", "Meal yield per\\s*bushel crushed.*\\s*Value from bushel\\s*of soybeans", 
					"No. 1 Yellow Soybeans\\s*truck price Central\\s*IL. points", 
					"Difference between\\s*soybean price & value\\s*of oil & meal"}};
			Map<String, String> dataMap = new HashMap<String, String>();
			for(int i = 0; i < colnames[0].length; i++){
				String valRegex = colnames[1][i] + "\\s+[^\\s]+\\s+(\\d+(\\.\\d+)?)";
				List<String> valList = RegexUtil.getMatchStr(cont, valRegex, new int[]{1});
				if(valList != null && !valList.isEmpty() && !valList.get(0).isEmpty()){
					String val = valList.get(0);
					dataMap.put(colnames[0][i], val);
				}
			}
			LOG.info(dataMap);
			if(dataMap.size() == colnames[0].length){
				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			} else if(dataMap.isEmpty()) {
				LOG.info("数据为空。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "数据为空。");
			} else {
				LOG.info("数据不完整。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "数据不完整。");
				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			}
			
		} else {
			LOG.info("格式发生变动，抓取失败。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "格式发生变动，抓取失败。");
		}
	}
	
	public static void main(String[] args) {
		USBeansMargin u = new USBeansMargin();
//		u.fetchData();
		u.start();
	}
}
