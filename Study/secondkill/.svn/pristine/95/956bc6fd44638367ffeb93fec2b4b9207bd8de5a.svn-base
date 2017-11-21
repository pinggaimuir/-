package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.MyHttpClient;

/**
 * 农业部畜产品和饲料集贸市场价格情况
 * A.饲料养殖-羊肉-集贸市场均价
 * B.饲料养殖-牛肉-平均价
 *　C.饲料养殖-奶畜-生鲜乳集贸市场均价
　* D.饲料养殖-蛋禽-商品代蛋鸡雏价格（周度）
 * E．饲料养殖-蛋禽-蛋鸡配合料价格（农业部）
 * F.饲料养殖-鸡蛋-鸡蛋集市价格（农业部）
 * G.饲料养殖-鸡蛋-主产省鸡蛋平均价格（农业部）
 * @author ctm
 *
 */

public class MOAAnimalFeedMarketPrice {
	private static final String className = MOAAnimalFeedMarketPrice.class.getName();
	private static final Log logger = LogFactory.getLog(MOAAnimalFeedMarketPrice.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private MyHttpClient httpClient = new MyHttpClient();
	private DAOUtils dao = new DAOUtils();
	private static final String urlBase = "http://www.moa.gov.cn/ztzl/nybrl/";
	
	private static Map<String, String> cnName2VarName = new HashMap<String, String>();
	static{
		cnName2VarName.put("集贸市场均价", "羊肉");
		cnName2VarName.put("平均价", "牛肉");
		cnName2VarName.put("生鲜乳集贸市场均价", "奶畜");
		cnName2VarName.put("商品代蛋鸡雏价格（周度）", "蛋禽");
		cnName2VarName.put("蛋鸡配合料价格（农业部）", "蛋禽");
		cnName2VarName.put("鸡蛋集市价格（农业部）", "鸡蛋");
		cnName2VarName.put("主产省鸡蛋平均价格（农业部）", "鸡蛋");
		cnName2VarName.put("仔猪集贸市场价格（农业部）", "生猪");
		cnName2VarName.put("育肥猪配合料集贸市场价格（农业部）", "配合料");
		cnName2VarName.put("肉鸡配合料集贸市场价格（农业部）", "配合料");
		cnName2VarName.put("商品代肉鸡雏集贸市场价格（农业部）", "肉禽");
		cnName2VarName.put("鱼粉集贸市场价格（农业部）", "鱼粉");
		cnName2VarName.put("豆粕集贸市场价格（农业部）", "豆粕");
		cnName2VarName.put("玉米集贸市场价格（农业部）", "玉米");
		cnName2VarName.put("白条鸡集贸市场价格（农业部）", "禽肉");
		cnName2VarName.put("活鸡集贸市场价格（农业部）", "肉禽");
		cnName2VarName.put("猪肉集贸市场价格（农业部）", "猪肉");
		cnName2VarName.put("活猪集贸市场价格（农业部）", "生猪");
	}
	
	private static Map<String, String> html2cnName = new HashMap<String, String>();
	static{
		html2cnName.put("羊肉", "集贸市场均价");
		html2cnName.put("牛肉", "平均价");
		html2cnName.put("主产省生鲜乳", "生鲜乳集贸市场均价");
		html2cnName.put("商品代蛋雏鸡", "商品代蛋鸡雏价格（周度）");
		html2cnName.put("蛋鸡配合饲料", "蛋鸡配合料价格（农业部）");
		html2cnName.put("鸡蛋", "鸡蛋集市价格（农业部）");
		html2cnName.put("主产省鸡蛋", "主产省鸡蛋平均价格（农业部）");
		html2cnName.put("仔猪", "仔猪集贸市场价格（农业部）");
		html2cnName.put("育肥猪配合饲料", "育肥猪配合料集贸市场价格（农业部）");
		html2cnName.put("肉鸡配合饲料", "肉鸡配合料集贸市场价格（农业部）");
		html2cnName.put("商品代肉雏鸡", "商品代肉鸡雏集贸市场价格（农业部）");
		html2cnName.put("进口鱼粉", "鱼粉集贸市场价格（农业部）");
		html2cnName.put("豆粕", "豆粕集贸市场价格（农业部）");
		html2cnName.put("玉米", "玉米集贸市场价格（农业部）");
		html2cnName.put("白条鸡", "白条鸡集贸市场价格（农业部）");
		html2cnName.put("活鸡", "活鸡集贸市场价格（农业部）");
		html2cnName.put("猪肉", "猪肉集贸市场价格（农业部）");
		html2cnName.put("活猪", "活猪集贸市场价格（农业部）");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_MOA_PRICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("农业部畜产品和饲料集贸市场价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到农业部畜产品和饲料集贸市场价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				startFetch(new Date());
			}else{
				logger.info("抓取农业部畜产品和饲料集贸市场价格的定时器已关闭");
			}
		}
	}
	
	/**
	 * 本周数据保存时间为上周周三，因为它统计的是上周数据
	 * @param date
	 */
	public void startFetch(Date date){
		if(date == null){
			date = new Date();
		}
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMM");
		logger.info("开始抓取农业经济信息发布日历@" + urlBase);
		String contents = httpClient.getHtmlByHttpClient(urlBase,"utf-8","");
		//System.out.println(contents);
		if(contents != null && !contents.equals("")){
			logger.info("开始匹配农业经济信息发布日历中的畜产品和饲料集贸市场价格情况");
			String comp = "畜产品和饲料集贸市场价格情况([^\"]+)./rlxx([^\"]+)" + timeInt + "([^\"]+).htm";
			int[] index = {2,3};
			List<String> results = dataFetchUtil.getMatchStr(contents, comp, index);
			if(results.size() > 0){
				String url = urlBase + "rlxx"+ results.get(0) + timeInt + results.get(1)+".htm";
				String[] params1 = {"table"};
				String[] rowColChoose1 = {"00011111111111111111100","110000"};//选择哪几行哪几列
				logger.info("开始抓取农业部畜产品和饲料集贸市场价格情况@" + url);
				String contents1 = dataFetchUtil.getPrimaryContent(0, url, "utf-8", "农业部畜产品和饲料集贸市场价格情况", params1, rowColChoose1, 5);
				if(contents1 != null && !contents1.equals("")){
					String[] lines = contents1.split("\n");
					String saveTimeInt = DateTimeUtil.formatDate(DateTimeUtil.addDay(DateTimeUtil.getLastWeekBeginDay(date), 2), "yyyyMMdd");
					Map<String, String> dataMap = new HashMap<String, String>();
					for(String line:lines){
						String[] fields = line.split(",");
						if(html2cnName.keySet().contains(fields[0])){
							String cnName = html2cnName.get(fields[0]);
							String varName = cnName2VarName.get(cnName);
							int varId = Variety.getVaridByName(varName);
							dataMap.clear();
							dataMap.put("全国", fields[1]);
							logger.info("开始保存"+varName+"("+varId+")"+cnName);
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(saveTimeInt), dataMap);
						}
					}
				}else{
					logger.error("没有抓取到农业部畜产品和饲料集贸市场价格情况");
				}
			}else{
				logger.error("没有匹配到"+timeInt+"农业经济信息发布日历中的畜产品和饲料集贸市场价格情况");
			}
		}else{
			logger.error("没有抓取到农业经济信息发布日历");
		}
	}
	
	/**
	 * 抓2012.08之前的数据
	 */
	public void startFetch1(){
		logger.info("开始抓取农业经济信息发布日历@" + urlBase);
		for(int page=35;page<=35;page++){
		String url1 = "http://search.agri.gov.cn/agrisearch/search.jsp?page="+page+"&searchword=%27%E7%95%9C%E4%BA%A7%E5%93%81%E5%92%8C%E9%A5%B2%E6%96%99%E9%9B%86%E8%B4%B8%E5%B8%82%E5%9C%BA%E4%BB%B7%E6%A0%BC%27&sortfield=-%E6%97%A5%E6%9C%9F&channelid=75077&prepage=10";
		String[] filters = {"div","id","searchresult"};
		String contents = dataFetchUtil.getPrimaryContent(0, url1, "utf-8", "", filters, null, 0);
		System.out.println(contents);
		if(contents != null && !contents.equals("")){
			String[] divs = contents.split("<div class=\"tong\">");
			for(int i=1;i<divs.length;i++){
				String div = divs[i];
				logger.info("开始匹配农业经济信息发布日历中的畜产品和饲料集贸市场价格情况");
				String comp = "href=\"([^\"]+)\"(.+)采集日为([0-9]+)月([0-9]+)日";
				int[] index = {1,3,4};
				List<String> results = dataFetchUtil.getMatchStr(div, comp, index);
				if(results.size() > 0){
					String tmp = results.get(0); 
					String timeInt = tmp.substring(tmp.lastIndexOf("/t")+2,tmp.indexOf("_"));
					if(Integer.parseInt(timeInt)>20120308){
						continue;
					}
					String url = tmp;
					String[] params1 = {"div","id","TRS_AUTOADD"};
					logger.info("开始抓取农业部畜产品和饲料集贸市场价格情况@" + url);
					String contents1 = dataFetchUtil.getPrimaryContent(0, url, "utf-8", "农业部畜产品和饲料集贸市场价格情况", params1, null, 0);
					if(contents1 != null && !contents1.equals("")){
						contents1 = contents1.replaceAll("<([^>]+)>", "");
						String saveTimeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(timeInt.substring(0,4)+"年"+results.get(1)+"月"+results.get(2)+"日", "yyyy年MM月dd日"), "yyyyMMdd");
						Map<String, String> dataMap = new HashMap<String, String>();
						for(String html:html2cnName.keySet()){
							String data = "";
							String compTmp = "全国"+html+"平均价格([为]{0,1})([0-9|.]+)";
							int[] indexx = {2};
							List<String> r = dataFetchUtil.getMatchStr(contents1, compTmp, indexx);
							if(r.size()>0){
								data = r.get(0);
								String cnName = html2cnName.get(html);
								String varName = cnName2VarName.get(cnName);
								int varId = Variety.getVaridByName(varName);
								dataMap.put("全国", data);
								logger.info("开始保存"+varName+"("+varId+")"+cnName);
								dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(saveTimeInt), dataMap);
							}else{
								String compTmp1 = html+"平均价格([为]{0,1})([0-9|.]+)";
								List<String> r1 = dataFetchUtil.getMatchStr(contents1, compTmp1, indexx);
								if(r1.size()>0){
									data = r1.get(0);
									String cnName = html2cnName.get(html);
									String varName = cnName2VarName.get(cnName);
									int varId = Variety.getVaridByName(varName);
									dataMap.put("全国", data);
									logger.info("开始保存"+varName+"("+varId+")"+cnName);
									dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(saveTimeInt), dataMap);
								}else{
									String compTmp2 = "。(.{6,7})、(.{6,7})和(.{6,7})平均价格分别为([0-9|.]+)元/公斤、([0-9|.]+)元/公斤和([0-9|.]+)元/公斤";
									int[] index1 = {1,2,3,4,5,6};
									List<String> r2 = dataFetchUtil.getMatchStr(contents1, compTmp2, index1);
									if(r2.size()>0){
										for(int ii=0;ii<3;ii++){
											data = r2.get(ii+3);
											String cnName = html2cnName.get(r2.get(ii));
											String varName = cnName2VarName.get(cnName);
											int varId = Variety.getVaridByName(varName);
											dataMap.put("全国", data);
											logger.info("开始保存"+varName+"("+varId+")"+cnName);
											dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(saveTimeInt), dataMap);
										}
									}else{
										String compTmp3 = "([；|。])(.{6,7})和(.{6,7})平均价格分别为([0-9|.]+)元/公斤([和]{0,1})([0-9|.]+)元/公斤";
										int[] index3 = {2,3,4,6};
										List<String> r3 = dataFetchUtil.getMatchStr(contents1, compTmp3, index3);
										if(r3.size()>0){
											for(int ii=0;ii<2;ii++){
												data = r3.get(ii+2);
												String cnName = html2cnName.get(r3.get(ii));
												String varName = cnName2VarName.get(cnName);
												int varId = Variety.getVaridByName(varName);
												dataMap.put("全国", data);
												logger.info("开始保存"+varName+"("+varId+")"+cnName);
												dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(saveTimeInt), dataMap);
											}
										}else{
											logger.info(html+"没有匹配到数据");
										}
									}
								}
							}
						}
//						String[] lines = contents1.split("\n");
//						String saveTimeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(timeInt.substring(0,4)+"年"+results.get(1)+"月"+results.get(2)+"日", "yyyy年MM月dd日"), "yyyyMMdd");
//						Map<String, String> dataMap = new HashMap<String, String>();
//						for(String line:lines){
//							String[] fields = line.split(",");
//							if(html2cnName.keySet().contains(fields[0])){
//								String cnName = html2cnName.get(fields[0]);
//								String varName = cnName2VarName.get(cnName);
//								int varId = Variety.getVaridByName(varName);
//								dataMap.clear();
//								dataMap.put("全国", fields[1]);
//								logger.info("开始保存"+varName+"("+varId+")"+cnName);
//								dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(saveTimeInt), dataMap);
//							}
//						}
					}else{
						logger.error("没有抓取到农业部畜产品和饲料集贸市场价格情况");
					}
				}else{
					logger.error("没有匹配到农业经济信息发布日历中的畜产品和饲料集贸市场价格情况");
				}
			}
		}else{
			logger.error("没有抓取到农业经济信息发布日历");
		}
		}
	}
	public static void main(String[] args){
		MOAAnimalFeedMarketPrice m = new MOAAnimalFeedMarketPrice();
		m.start();
	}
}
