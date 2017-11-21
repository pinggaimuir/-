package cn.futures.data.importor.crawler;

import java.util.ArrayList;
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

/**
 * 
 * 博亚和讯网：毛鸭、鸭苗、猪价格
 * @author ctm
 *
 */
public class BOYARCNDataFetch {
	private static final String className = BOYARCNDataFetch.class.getName();
	private Log logger = LogFactory.getLog(BOYARCNDataFetch.class);
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private static DAOUtils dao = new DAOUtils();
	private static Map<String, String>  BOYAR_CN_MAP = new HashMap<String, String>();
	private static Map<String, String> params = new HashMap<String, String>();
	private static List<String> allAreaList = new ArrayList<String>();
	static{
		BOYAR_CN_MAP.put("肉禽", "http://www.boyar.cn/column/136/");
		BOYAR_CN_MAP.put("生猪", "http://www.boyar.cn/column/135/");
		
		params.put("column_id","67");
		params.put("page","1");
		
		allAreaList.addAll(dao.getHeaderById(0));//利用表头0来获取港澳台除外的所有省份的名称
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_BOYARCN_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("毛鸭鸭苗猪价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到博亚和讯网：毛鸭、鸭苗、猪价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchData();
			}else{
				logger.info("抓取博亚和讯网：毛鸭、鸭苗、猪价格的定时器已关闭");
			}
		}
	}
	
	public void fetchData(){
		logger.info("=====开始抓取博亚和讯网的毛鸭、鸭苗、生猪、仔猪价格数据=====");
		String[] filters = {"div", "class", "liebiao-left5"};
		for(String varName:BOYAR_CN_MAP.keySet()){
			String pageUrl = BOYAR_CN_MAP.get(varName);
			String contents = fetchUtil.getPostPrimaryContent(0, pageUrl, "GBK", "博亚和讯首页-禽 价格", filters, null, 0, params);
			if(contents != null && !contents.equals("")){
				if(varName.equals("肉禽")){
					fetchDuck(contents, varName);
				}else if(varName.equals("生猪")){
					fetchPig(contents, varName);
				}
			}
		}
	}
	
	private void fetchDuck(String contents, String varName){
		int[] index={1,4};
		List<String> res = new ArrayList<String>();
		String[] hrefs = contents.split("href=");
		String[] filters1 = {"div", "id", "ctn"};
		for(String href:hrefs){
			String comp = "\"([^\"]+)\"(.+)博亚和讯毛鸭、鸭苗价格{0,1}监测(.+)([0-9]{4}-[0-9]{2}-[0-9]{2})";
			res = fetchUtil.getMatchStr(href, comp, index);
			if(res.size() > 0){
				String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(res.get(1), "yyyy-MM-dd"),"yyyyMMdd");
				//补数据时使用
//				if(DateTimeUtil.parseDateTime(timeInt, DateTimeUtil.YYYYMMDD).before(DateTimeUtil.parseDateTime("20160316", DateTimeUtil.YYYYMMDD))){
//					break;
//				}
				int varId = Variety.getVaridByName(varName);
				String cnName = null;
				String urlDuck = res.get(0);
				logger.info("抓取肉毛鸭和肉鸭苗价格@"+urlDuck);
				String contentDuck = fetchUtil.getPrimaryContent(0, urlDuck, "gbk", varName, filters1, null, 0);
					
				if(contentDuck != null && !contentDuck.equals("")){
					contentDuck = contentDuck.replaceAll("<[^>]*>", "").replaceAll("(\\s+)", ",").replaceAll("([,]+)", ",");
					
					String[] contentDuckPart = contentDuck.split("博亚和讯监测");
					//有时仅有一个不确定是肉毛鸭还是鸭苗
					if(contentDuckPart.length != 3){
						break;//补数据时改为continue
					}
					for(int j = 1; j < 3; j++){
						if(j == 1){
							cnName = "肉毛鸭价格";
						} else {
							cnName = "肉鸭苗价格";
						}
						contentDuck = contentDuckPart[j];
						String[] fields = contentDuck.split(",");
						for(String field:fields){
							if(allAreaList.contains(field)){
								contentDuck = contentDuck.replaceAll(field, "prov:"+field);
							}
						}
						String[] provs = contentDuck.split("prov:");
						Map<String, String> dataMap = new HashMap<String, String>();
						double worldSum = 0;
						int provNum = 0;
						for(int i=1;i<provs.length;i++){
							String[] tmps = provs[i].split(",");
							String prov = tmps[0];
							if(!allAreaList.contains(prov)){
								continue;
							}
							double sum = 0;
							int num = 0;
							boolean firstNum = true;
							for(String tmp:tmps){
								if(fetchUtil.isNumeric(tmp) || tmp.trim().startsWith("-")){
									if(firstNum){
										if(tmp.trim().equals("-")){
											sum += 0;
										}else{
											sum += Double.parseDouble(tmp);
										}
										num ++;
										firstNum = false;
									}else{
										firstNum = true;
									}
								}
							}
							worldSum += sum/num;
							provNum ++;
							dataMap.put(prov, sum/num+"");
						}
						dataMap.put("全国", worldSum/provNum+"");
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
					}
				}else{
					logger.error("没有抓取到肉毛鸭和肉鸭苗价格@"+urlDuck);
				}
				break;//补数据时注释掉
			}
		}
	}
	
	private void fetchPig(String contents, String varName){
		int varId = Variety.getVaridByName(varName);
		int[] index={1,3,5};
		List<String> res = new ArrayList<String>();
		String[] hrefs = contents.split("href=");
		String[] filters1 = {"div", "id", "ctn"};
		for(String href:hrefs){
			String comp = "\"([^\"]+)\"(.+)全国部分地区生猪及仔猪价格【([0-9]{1,2}\\.[0-9]{1,2})】(.+)([0-9]{4}-[0-9]{2}-[0-9]{2})";
			res = fetchUtil.getMatchStr(href, comp, index);
			if(res.size() > 0){
//				String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(res.get(2), "yyyy-MM-dd"),"yyyyMMdd");
				String publishDate = res.get(2);
				String[] timeIntMonthDay = res.get(1).split("\\.");
				if(timeIntMonthDay[0].length() == 1){
					timeIntMonthDay[0] = 0 + timeIntMonthDay[0];
				}
				if(timeIntMonthDay[1].length() == 1){
					timeIntMonthDay[1] = 0 + timeIntMonthDay[1];
				}
				String timeInt = publishDate.substring(0, publishDate.indexOf('-')) + timeIntMonthDay[0] + timeIntMonthDay[1];
				/*补指定时间序列之前所有的数据时使用*/
//				if(DateTimeUtil.parseDateTime(timeInt, DateTimeUtil.YYYYMMDD).before(DateTimeUtil.parseDateTime("20161222", DateTimeUtil.YYYYMMDD))){
//					break;
//				}
//				try {
//					Thread.sleep(1500L);
//				} catch (InterruptedException e) {
//					logger.error(e);
//				}
				/*补指定时间序列的数据时使用*/
//				if(!timeInt.equals("20161202")){
//					continue;
//				}
				String urlDuck = res.get(0);
				logger.info("抓取生猪、仔猪"+"@"+urlDuck);
				String contentPig = fetchUtil.getPrimaryContent(0, urlDuck, "gbk", varName, filters1, null, 0);
				if(contentPig != null && !contentPig.equals("")){
					contentPig = contentPig.replaceAll("<[^>]*>", "").replaceAll("(\\s+)", ",").replaceAll("([,]+)", ",");
					if(contentPig.indexOf("合计") != -1){
						contentPig = contentPig.substring(0, contentPig.indexOf("合计"));
					}
					String[] fields = contentPig.split(",");
					for(String field:fields){
						if(allAreaList.contains(field)){
							contentPig = contentPig.replaceAll(field, "prov:"+field);
						}
					}
					String[] provs = contentPig.split("prov:");
					Map<String, String> pigDataMap = new HashMap<String, String>();//生猪价格（外三元）
					Map<String, String> pigletDataMap = new HashMap<String, String>();//仔猪价格（外三元）
					double worldSumPig = 0;//生猪价格（外三元）  ---  全国
					int provNum = 0;//仔猪价格（外三元）  ---  全国
					double worldSumPiglet = 0;
					for(int i=1;i<provs.length;i++){//逐个省份去处理
						String[] oneProvData = provs[i].split(",");//该省所有列数据，0位记录省份名称，后边依序为“县市区”，“外三元.生猪（元/kg）”，“涨跌”，“外三元.仔猪（元/kg.20kg体重）”，“涨跌”，“外三元.仔猪（元/kg.15kg体重）”，“涨跌”的循环。
						String prov = oneProvData[0];
						double sumPig = 0;//该省价格和
						int num = 0;//该省价格数
						double sumPiglet = 0;
						for(int j=1;j<oneProvData.length-1;j++){
							if(j % 7 == 1 && oneProvData[j].matches("[\u4E00-\u9FFF]+")){//定位“县市区”列
								sumPig += Double.parseDouble(oneProvData[j + 1]);
								sumPiglet += Double.parseDouble(oneProvData[j + 3]);
								num++;
							} else if(j % 7 == 1){
								logger.error("格式有变化：" + prov);
							}
						}
						provNum ++;
						worldSumPig += sumPig/num;
						worldSumPiglet += sumPiglet/num;
						pigDataMap.put(prov, sumPig/num+"");
						pigletDataMap.put(prov, sumPiglet/num+"");
					}
					pigDataMap.put("全国", worldSumPig/provNum+"");
					pigletDataMap.put("全国", worldSumPiglet/provNum+"");
					dao.saveOrUpdateByDataMap(varId, "生猪价格（外三元）", Integer.parseInt(timeInt), pigDataMap);
					dao.saveOrUpdateByDataMap(varId, "仔猪价格（外三元）", Integer.parseInt(timeInt), pigletDataMap);
				}else{
					logger.error("没有抓取到生猪、仔猪"+"@"+urlDuck);
				}
				break;//补数据时注释掉
			}
		}
	}
	public static void main(String[] args){
		BOYARCNDataFetch bo = new BOYARCNDataFetch();
		//测试
		bo.start();
		//补历史数据需将上面的break注释掉，并根据timeInt确定何时终止，不要覆盖数据。
//		for(int page = 1; page <= 1; page++){
//			params.put("page",String.valueOf(page));
//			bo.start();
//		}
	}
}
