package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;

/**
 * CAAA肉禽蛋禽价格
 * 饲料养殖-肉禽-白羽肉鸡苗价格（CAAA）
 * 饲料养殖-肉禽-黄羽肉鸡苗价格（CAAA）
 * 饲料养殖-肉禽-黄羽肉毛鸡价格（CAAA）
 * 饲料养殖-肉禽-鸭苗价格（CAAA）
 * 饲料养殖-肉禽-毛鸭价格（CAAA）
 * 饲料养殖-蛋禽-淘汰鸡批发价（CAAA）
 * @author ctm
 *
 */
public class CAAAMeatPoultryPrice {
	private static final String className = CAAAMeatPoultryPrice.class.getName();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Log logger = LogFactory.getLog(CAAAMeatPoultryPrice.class);
	private static final String urlCAAA = "http://www.caaa.cn/market/index.php?class=2381";
	private static final String urlReport = "http://www.caaa.cn/show/newsarticle.php?id=";
	private static final String urlFairPrice = "http://www.caaa.cn/market/trend/local/xml/1-%province%-%kind%.xml";//集市价格链接
	private static final String urlFairPriceWorld = "http://www.caaa.cn/market/zs/xml/1/%zsid%.xml";//全国价格链接
	
	@Scheduled
	(cron=CrawlScheduler.CRON_CAAA_FAIR_PRICE)
	public void start1(){
		String switchFlag = new CrawlerManager().selectCrawler("CAAA饲料养殖集市价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到CAAA饲料养殖集市价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchCAAAFairPrice();
			}else{
				logger.info("抓取CAAA饲料养殖集市价格的定时器已关闭");
			}
		}
	}
	
	public void fetchCAAAFairPrice(){
		//根据数据库里面最新的日期确定该抓哪个月份的日期,以鸡蛋集市价格（CAAA）为准
		int newestTime = DAOUtils.getNewestTimeInt("CX_MonthlyPricesofeggs_caaa");
		logger.info("===========开始抓取CAAA饲料养殖集市价格==============");
		Date date = DateTimeUtil.parseDateTime(newestTime+"", "yyyyMM");
		String timeIntStr = DateTimeUtil.formatDate(DateTimeUtil.addMonth(date, 1), "yyyyMM");
		Integer timeInt = Integer.parseInt(timeIntStr);
 		for(String cnName:MapInit.caaa_cnName_map.keySet()){
			Map<Integer, Map<String, String>> time2dataMap = new HashMap<Integer, Map<String, String>>();
			String varName = MapInit.caaa_cnName2varName_map.get(cnName);
			int varId = Variety.getVaridByName(varName);
			for(String province:MapInit.caaa_province_map.keySet()){
				String fetchUrl = urlFairPrice.replace("%province%", MapInit.caaa_province_map.get(province))
					.replace("%kind%", MapInit.caaa_cnName_map.get(cnName).split("-")[0]);
				logger.info("开始抓取" + province+varName + "@" + fetchUrl);
				String body = CrawlerUtil.httpGetBody(fetchUrl);
				int index = body.indexOf("<series name=\""+DateTimeUtil.formatDate(date, "yyyy")+"年\">");
				if (body != null && index != -1) {
					body = body.substring(index);
					String tmp = body.substring(body.indexOf("<point"), body.indexOf("</series>"));
					String[] points = tmp.split("/>\n");
					for(String point:points){
						String[] fields = point.split("\"");
						//只抓上个月数据
						String lastMonth = Integer.parseInt(timeIntStr.substring(4,6))+"月";
						if(!fields[1].equals(lastMonth)){
							continue;
						}
						Map<String, String> dataMapTmp = time2dataMap.get(timeInt);
						if(dataMapTmp == null){
							Map<String, String> dateMap = new HashMap<String, String>();
							for(String prov:MapInit.caaa_province_map.keySet()){
								dateMap.put(prov, "0");
							}
							dateMap.put(province, fields[3].equals("missing")?"0":fields[3]);
							time2dataMap.put(timeInt, dateMap);
						}else{
							dataMapTmp.put(province, fields[3].equals("missing")?"0":fields[3]);
							time2dataMap.put(timeInt, dataMapTmp);
						}
					}
				}else{
					logger.error("无法获取" + province + "-" + varName + "-" + cnName + "数据");
				}
			}
			logger.info("计算全国的价格");
			List<Integer> removeKey = new ArrayList<Integer>();
			for(int tmp:time2dataMap.keySet()){
				double sum = 0;
				int provinceNum = 0;
				Map<String, String> dataMap = time2dataMap.get(tmp);
				for(String province:dataMap.keySet()){
					String data = dataMap.get(province);
					if(data != null && !data.equals("0")){
						sum += Double.parseDouble(data);
						provinceNum ++;
					}
				}
				if(provinceNum == 0){
					removeKey.add(timeInt);
				}else{
					dataMap.put("全国", sum/provinceNum +"");
					time2dataMap.put(timeInt, dataMap);
				}
			}
			for(Integer key:removeKey){
				time2dataMap.remove(key);
			}
			if(time2dataMap.size() > 0){
				logger.info("批量保存"+varName+"-"+cnName);
				dao.batchSaveByDataMap(varId, cnName, time2dataMap, null);
			}else{
				logger.info("没有需要保存的数据");
			}
		}
 		fetchCAAAAllCountry(timeIntStr);
	}
	//更新存在全国价格的数据
	public void fetchCAAAAllCountry(String timeIntStr){
		logger.info("======开始更新存在全国价格的数据=======");
		Date date = new Date();
		String lastMonth = Integer.parseInt(timeIntStr.substring(4,6))+"月";
 		for(String cnName:MapInit.caaa_cnName_map.keySet()){
 			String[] codes = MapInit.caaa_cnName_map.get(cnName).split("-");
 			String zsid = codes[1];
			String varName = MapInit.caaa_cnName2varName_map.get(cnName);
			int varId = Variety.getVaridByName(varName);
			if(zsid.equals("0")) continue;
			String fetchUrl = urlFairPriceWorld.replace("%zsid%", zsid);
			logger.info("开始抓取"+varName + cnName + "全国价格@" + fetchUrl);
			String body = CrawlerUtil.httpGetBody(fetchUrl);
			int index = body.indexOf("<series name=\""+DateTimeUtil.formatDate(date, "yyyy")+"年\">");
			if (body != null && index != -1) {
				body = body.substring(index);
				String tmp = body.substring(body.indexOf("<point"), body.indexOf("</series>"));
				String[] points = tmp.split("/>\n");
				for(String point:points){
					String[] fields = point.split("\"");
					if(!fields[1].equals(lastMonth)){
						continue;
					}
					if(!fields[3].equals("missing")){
						Map<String, String> dataMap = new HashMap<String, String>();
						dataMap.put("全国", fields[3]);
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeIntStr), dataMap);
					}else{
						logger.error(varName + "-" + cnName + timeIntStr+"全国价格数据不存在");
					}
				}
			}else{
				logger.error("无法获取" + varName + "-" + cnName + "全国价格数据");
			}
		}
	}
	
	@Scheduled
	(cron = CrawlScheduler.CRON_CAAA_POULTRY_PRICE)
	public void start2(){
		String switchFlag = new CrawlerManager().selectCrawler("CAAA肉禽蛋禽", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到CAAA肉禽蛋禽在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				startFetch();
			}else{
				logger.info("抓取CAAA肉禽蛋禽的定时器已关闭");
			}
		}
	}
	
	public void startFetch(){
		String[] params = {"table"};
		String[] rowColChoose = null;
		logger.info("start fetch CAAA主页中的市场周报@" + urlCAAA);
		String contents = dataFetchUtil.getPrimaryContent(0, urlCAAA, "GBK", "CAAA肉禽蛋禽", params, rowColChoose, 13);
		if(contents == null){
			logger.error("未找到CAAA中的市场周报");
		}else{
			Pattern pattern = Pattern.compile("CAAA(.+)<BR>");
			Matcher matcher = pattern.matcher(contents);
			if(matcher.find()){
			  String str = matcher.group(1).split("BR")[0];//实例：(360253) >2015年禽及相关产品第16周周报</a> <font color=red><i>15-04-28</i></font><
			  String id = str.substring(str.indexOf("(")+1, str.indexOf(")"));
			  String reportDate = str.substring(str.indexOf("<i>")+3, str.indexOf("</i>"));
			  Date d = null;
			  try {
				  Date now = new Date();
				  //Date now = DateTimeUtil.parseDateTime("20150608", "yyyyMMdd");
				  d = new SimpleDateFormat("yy-MM-dd").parse(reportDate);
				  if(DateTimeUtil.isSameDay(d, now)){
					  int timeInt = Integer.parseInt(DateTimeUtil.formatDate(d, "yyyyMMdd"));
					  String[] params1 = {"td","class","font_14"};
					  String[] rowColChoose1 = null;
					  logger.info("start fetch 市场周报中的肉禽蛋禽价格@" + urlCAAA);
					  contents = dataFetchUtil.getPrimaryContent(0, urlReport+id, "GBK", "CAAA肉禽蛋禽", params1, rowColChoose1, 0);
					  if(contents != null && !contents.equals("")){
						  //饲料养殖-肉禽-白羽肉鸡苗价格（CAAA）
						  logger.info("开始分析白羽肉鸡苗价格");
						  String varName = "肉禽";
						  String cnName = "白羽肉鸡苗价格(CAAA）";
						  String compStr = "白羽肉鸡：鸡苗([0-9|/.]*)元";
						  int[] index = {1};
						  List<String> whiteChicken = dataFetchUtil.getMatchStr(contents, compStr, index);
						  if(whiteChicken.size() > 0){
							  Map<String, String> dataMap = new HashMap<String, String>();
							  dataMap.put("全国", whiteChicken.get(0));
							  logger.info("开始保存白羽肉鸡苗价格");
							  dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						  }else{
							  logger.error("未匹配到白羽肉鸡苗价格数据");  
						  }
						  //饲料养殖-肉禽-黄羽肉鸡苗价格（CAAA）
						  logger.info("开始分析黄羽肉鸡苗价格");
						  cnName = "黄羽肉鸡苗价格(CAAA）";
						  compStr = "黄羽肉鸡(.+)快速型鸡苗([0-9|/.]*)元(.+)中速型鸡苗([0-9|/.]*)元(.+)慢速型鸡苗([0-9|/.]*)元";
						  int[] index1 = {2,4,6};
						  List<String> yellowChicken = dataFetchUtil.getMatchStr(contents, compStr, index1);
						  if(yellowChicken.size() > 0){
							  Map<String, String> dataMap = new HashMap<String, String>();
							  dataMap.put("快速型", yellowChicken.get(0));
							  dataMap.put("中速型", yellowChicken.get(1));
							  dataMap.put("慢速型", yellowChicken.get(2));
							  logger.info("开始保存黄羽肉鸡苗价格");
							  dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						  }else{
							  logger.error("未匹配到黄羽肉鸡苗价格数据");  
						  }
						  //饲料养殖-肉禽-黄羽肉毛鸡价格（CAAA）
						  logger.info("开始分析黄羽肉毛鸡价格");
						  cnName = "黄羽肉毛鸡价格(CAAA）";
						  compStr = "黄羽肉鸡(.+)快速型鸡苗(.+)毛鸡([0-9|/.]*)元(.+)中速型鸡苗(.+)毛鸡([0-9|/.]*)元(.+)慢速型鸡苗(.+)毛鸡([0-9|/.]*)元";
						  int[] index3 = {3,6,9};
						  List<String> yellowGrossChicken = dataFetchUtil.getMatchStr(contents, compStr, index3);
						  if(yellowGrossChicken.size() > 0){
							  Map<String, String> dataMap = new HashMap<String, String>();
							  dataMap.put("快速型", yellowGrossChicken.get(0));
							  dataMap.put("中速型", yellowGrossChicken.get(1));
							  dataMap.put("慢速型", yellowGrossChicken.get(2));
							  logger.info("开始保存黄羽肉毛鸡价格");
							  dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						  }else{
							  logger.error("未匹配到黄羽肉毛鸡价格数据");  
						  }
						  //饲料养殖-肉禽-鸭苗价格（CAAA）
						  logger.info("开始分析鸭苗价格");
						  cnName = "鸭苗价格(CAAA）";
						  compStr = "鸭(.+)鸭苗([0-9|/.]*)元";
						  int[] index4 = {2};
						  List<String> duck = dataFetchUtil.getMatchStr(contents, compStr, index4);
						  if(duck.size() > 0){
							  Map<String, String> dataMap = new HashMap<String, String>();
							  dataMap.put("全国", duck.get(0));
							  logger.info("开始保存鸭苗价格");
							  dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						  }else{
							  logger.error("未匹配到鸭苗价格数据");  
						  }
						  //饲料养殖-肉禽-毛鸭价格（CAAA）
						  logger.info("开始分析毛鸭价格");
						  cnName = "毛鸭价格(CAAA）";
						  compStr = "毛鸭（山东）([0-9|/.]*)元";
						  int[] index5 = {1};
						  List<String> grossDuck = dataFetchUtil.getMatchStr(contents, compStr, index5);
						  if(grossDuck.size() > 0){
							  Map<String, String> dataMap = new HashMap<String, String>();
							  dataMap.put("山东", grossDuck.get(0));
							  logger.info("开始保存毛鸭价格");
							  dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						  }else{
							  logger.error("未匹配到毛鸭价格数据");  
						  }
						  //饲料养殖-蛋禽-淘汰鸡批发价（CAAA）
						  varName = "蛋禽";
						  logger.info("开始分析淘汰鸡批发价格");
						  cnName = "淘汰鸡批发价(CAAA）";
						  compStr = "淘汰鸡批发价([0-9|/.]*)元";
						  int[] index6 = {1};
						  List<String> outChicken = dataFetchUtil.getMatchStr(contents, compStr, index6);
						  if(outChicken.size() > 0){
							  Map<String, String> dataMap = new HashMap<String, String>();
							  dataMap.put("全国", outChicken.get(0));
							  logger.info("开始保存淘汰鸡批发价格");
							  dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
						  }else{
							  logger.error("未匹配到淘汰鸡批发价格数据");  
						  }
					  }else{
						  logger.error("未找到CAAA中肉禽蛋禽数据"); 
					  }
				  }else{
					  logger.warn("CAAA市场周报上周的数据没有在今天更新");
				  }
			  } catch (ParseException e) {
				  logger.error("网页格式有变动");
			  }
			}else{
				logger.error("未找到CAAA中的市场周报");
			}
		}
	}
	
	public static void main(String[] args){
		new CAAAMeatPoultryPrice().start1();
	}
}
