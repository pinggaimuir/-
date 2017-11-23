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

/**
 * 统计局
 * 流通领域重要生产资料市场价格变动情况
 * 50个城市主要食品平均价格变动情况
 * @author ctm
 *
 */
public class StatDepDatas {
	private static final String className = StatDepDatas.class.getName();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static Log logger = LogFactory.getLog(StatDepDatas.class);
	private static String urlBase = "http://www.stats.gov.cn/tjsj/zxfb/";//统计局最新发布链接
	private DAOUtils dao = new DAOUtils();
	private static boolean updateCirc = false;
	private static boolean update50cities = false;
	private static Map<String, String> priceForCircMap = new HashMap<String, String>();
	private static Map<String, String> priceFor50countryMap = new HashMap<String, String>();
	static{
		priceForCircMap.put("花生-油料花生米价格","花生（油料花生米）");
		priceForCircMap.put("复合肥-复合肥市场价","复合肥（硫酸钾复合肥）");
		priceForCircMap.put("氮肥-尿素市场价","尿素（小颗料）");
		priceForCircMap.put("皮棉-棉花市场价","棉花（皮棉，白棉三级）");
		priceForCircMap.put("小麦-小麦市场价","小麦（国标三等）");
		priceForCircMap.put("玉米-玉米市场价","玉米（黄玉米二等）");
		priceForCircMap.put("除草剂-草甘膦市场价","农药(草甘膦，95%原药）");
		priceForCircMap.put("豆粕-豆粕市场价","豆粕（粗蛋白含量≥43%）");
		priceForCircMap.put("大豆-大豆市场价","大豆（黄豆）");
		priceForCircMap.put("生猪-生猪市场价","生猪（外三元）");
		priceForCircMap.put("大米-稻米市场价（粳稻米）","稻米（粳稻米）");
		
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-土豆","土豆");
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-豆角","豆角");
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-西红柿","西红柿");
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-黄瓜","黄瓜");
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-芹菜","芹菜");
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-油菜","油菜");
		priceFor50countryMap.put("统计局50个城市蔬菜平均价-大白菜","大白菜");
		priceFor50countryMap.put("统计局50个城市水果平均价-香蕉","香蕉国产");
		priceFor50countryMap.put("统计局50个城市水果平均价-苹果","苹果富士苹果");
		priceFor50countryMap.put("猪肉-50个城市猪后腿肉平均价","猪肉猪肉后臀尖(后腿肉)");
		priceFor50countryMap.put("大米-50个城市大米平均价格","大米粳米");
		priceFor50countryMap.put("居民食用消费-50个城市豆腐平均价","豆制品豆腐");
		priceFor50countryMap.put("面粉-50个城市面粉平均价（富强粉）","面粉富强粉");
		priceFor50countryMap.put("统计局50个城市水产品平均价-带鱼","带鱼");
		priceFor50countryMap.put("统计局50个城市水产品平均价-活草鱼","活草鱼");
		priceFor50countryMap.put("统计局50个城市水产品平均价-活鲤鱼","活鲤鱼");
		priceFor50countryMap.put("禽肉-50个城市白条鸭平均价","鸭白条鸭");
		priceFor50countryMap.put("禽肉-50个城市鸡胸肉平均价","鸡鸡胸肉");
		priceFor50countryMap.put("禽肉-50个城市白条鸡均价","鸡白条鸡");
		priceFor50countryMap.put("猪肉-50个城市猪五花肉平均价","猪肉五花肉");
		priceFor50countryMap.put("面粉-50个城市面粉平均价","面粉标准粉");
		priceFor50countryMap.put("羊肉-50个城市羊肉平均价","羊肉腿肉");
		priceFor50countryMap.put("牛肉-50个城市牛肉平均价","牛肉腿肉");
		priceFor50countryMap.put("居民食用消费-50个城市大豆油平均价","大豆油5L桶装");
		priceFor50countryMap.put("居民食用消费-50个城市菜籽油平均价","菜籽油一级散装");
		priceFor50countryMap.put("居民食用消费-50个城市花生油平均价","花生油压榨一级");
		priceFor50countryMap.put("鸡蛋-50个城市散装鲜鸡蛋平均价","鸡蛋散装鲜鸡蛋");
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_STATDEP_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("统计局流通领域、50个城市价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到统计局流通领域、50个城市价格的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				startFetch(new Date());
			}else{
				logger.info("抓取统计局流通领域、50个城市价格的定时器已关闭");
			}
		}
	}
	
	public void startFetch(Date date){
		logger.info("==========开始抓取国家统计局最新发布的链接=============");
		String[] params = {"ul","class","center_list_contlist"};
		String[] rowColChoose = null;
		String contents = dataFetchUtil.getPrimaryContent(0, urlBase, "utf-8", "国家统计局最新发布链接", params, rowColChoose, 0);
		if(contents != null && !contents.equals("")){
			String[] contentFields = contents.split("<li>");
			String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
			logger.info("开始匹配[流通领域重要生产资料市场价格变动情况]、[50个城市主要食品平均价格变动情况]");
			for(String field:contentFields){
				if(field.indexOf("流通领域重要生产资料市场价格变动情况") != -1 && !updateCirc){
					parserAndSaveCirc(field, timeInt);
				}else if(field.indexOf("50个城市主要食品平均价格变动情况") != -1 && !update50cities){
					parseAndSave50cities(field, timeInt);
				}
				if(update50cities && updateCirc){
					break;
				}
			}
			
		}else{
			logger.error("抓取国家统计局最新发布的链接为空");
		}
	}
	
	private void parseAndSave50cities(String field, String timeInt) {
		String comp ="href=\"(.+)html";
		int[] index={1};
		List<String> results = dataFetchUtil.getMatchStr(field, comp, index);
		if(results.size() > 0){
			if(results.get(0).indexOf(timeInt) != -1){
				String url2 = urlBase + results.get(0).substring(2) + "html";
				logger.info("开始抓取[50个城市主要食品平均价格变动情况]中的价格" +"@"+url2);
				String[] params1 = {"table","class","MsoNormalTable"};
				String[] rowColChoose1 = {"01111111111111111111111111110","110100"};
				String contents1 = dataFetchUtil.getPrimaryContent(0, url2, "utf-8", "国家统计局：50个城市主要食品平均价格变动情况", params1, rowColChoose1, 0);
				if(contents1 != null && !contents1.equals("")){
					String[] lines = contents1.split("\n");
					Map<String, String> contentMap = new HashMap<String, String>();
					for(String line:lines){
						String[] fields = line.split(",");
						//去掉空格、中文全角空格
						contentMap.put((fields[0]+fields[1]).replaceAll("&nbsp;", "").replaceAll("[\\s]*", "").replaceAll("[　]*", ""), fields[2]);
					}
					for(String varAndCnNames:priceFor50countryMap.keySet()){
						String[] tmps = varAndCnNames.split("-");
						String varName = tmps[0];
						int varId = Variety.getVaridByName(varName);
						String cnName = tmps[1];
						Map<String, String> dataMap = new HashMap<String, String>();
						if(contentMap.get(priceFor50countryMap.get(varAndCnNames)) != null){
							dataMap.put("全国", contentMap.get(priceFor50countryMap.get(varAndCnNames)));
							logger.info("保存"+varName+"("+varId+")"+cnName);
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
						}else{
							logger.info(varName+"没有数据需要保存");
						}
					}
					update50cities = true;
				}else{
					logger.warn("[50个城市主要食品平均价格变动情况]中的价格数据没有抓到");
				}
			}else{
				logger.warn("[50个城市主要食品平均价格变动情况]中的价格数据今天没有更新");
			}
		}else{
			logger.error("未匹配到[50个城市主要食品平均价格变动情况]的链接");
		}
	}

	private void parserAndSaveCirc(String field, String timeInt) {
		String comp ="href=\"(.+)html";
		int[] index={1};
		List<String> results = dataFetchUtil.getMatchStr(field, comp, index);
		if(results.size() > 0){
			if(results.get(0).indexOf(timeInt) != -1){
				String url1 = urlBase + results.get(0).substring(2) + "html";
				logger.info("开始抓取[流通领域重要生产资料市场价格变动情况]中的价格数据@"+url1);
				String[] params1 = {"table","class","MsoNormalTable"};
				//String[] rowColChoose1 = {"001111111111111111111111111111111111111111111111111111111111100","10100"};
				String[] rowColChoose1=null;
				String contents1 = dataFetchUtil.getPrimaryContent(0, url1, "utf-8", "国家统计局：流通领域重要生产资料市场价格变动情况", params1, rowColChoose1, 0);
				if(contents1 != null && !contents1.equals("")){
					Map<String, String> contentMap = new HashMap<String, String>();
					contents1 = contents1.replaceAll("<([^>]+)>", "").replaceAll("\\s+", ",");
					String[] fields = contents1.split(",");
					for(int i=1;i<fields.length;i++){
						if(fields[i].trim().equals("吨") || fields[i].trim().equals("千克")){
							contentMap.put(fields[i-1].trim(), fields[i+1].trim());
						}
					}
					for(String varAndCnNames:priceForCircMap.keySet()){
						String[] tmps = varAndCnNames.split("-");
						String varName = tmps[0];
						int varId = Variety.getVaridByName(varName);
						String cnName = tmps[1];
						Map<String, String> dataMap = new HashMap<String, String>();
						if(contentMap.get(priceForCircMap.get(varAndCnNames)) != null){
							dataMap.put("全国", contentMap.get(priceForCircMap.get(varAndCnNames)));
							logger.info("保存"+varName+"("+varId+")"+cnName);
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
						}else{
							logger.info(varName+"没有数据需要保存");
						}
					}
					updateCirc = true;
				}else{
					logger.warn("价格数据没有抓到");
				}
			}else{
				if(!updateCirc){
					logger.warn("[流通领域重要生产资料市场价格变动情况]中的价格数据今天没有更新");
				}
			}
		}else{
			logger.error("未匹配到[流通领域重要生产资料市场价格变动情况]的链接");
		}
	}

	public static void main(String[] args){
		new StatDepDatas().startFetch(DateTimeUtil.parseDateTime("20151014", "yyyyMMdd"));
	}
}
