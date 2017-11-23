package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 饲料行业信息网 牛肉羊肉猪肉菜籽油现货价格
 * @author ctm
 * 修改历史：
 * 1. 2016-09-09 yyl  程序中原访问链接不可用，采用原文档中所给链接。
 */
public class FeedTradeData {
	private static final String className = FeedTradeData.class.getName();
	private Log logger = LogFactory.getLog(FeedTradeData.class);
	private static final String encoding = "utf-8";
	private static final String baseUrl = "http://hangqing.ymt.com/chandi_#varCode_0_#cityCode";
	private DataFetchUtil fetchUtil = new DataFetchUtil(true);
	private DAOUtils dao = new DAOUtils();
	
	/**
	 * 初始化常量。应该没有必要弄成静态的。
	 * @author bric_yangyulin
	 * @date 2019-09-09
	 * */
	public void initMap(Map<String, String> var2CodeMap, Map<String, String> city2CodeMap){
		{
			var2CodeMap.put("牛肉", "8173");
			var2CodeMap.put("羊肉", "8501");
			
			city2CodeMap.put("全国", "-1");
			city2CodeMap.put("北京", "1");
			city2CodeMap.put("天津", "2");
			city2CodeMap.put("上海", "3");
			city2CodeMap.put("重庆", "4");
			city2CodeMap.put("河北", "5");
			city2CodeMap.put("山西", "6");
			city2CodeMap.put("内蒙古", "7");
			city2CodeMap.put("辽宁", "8");
			city2CodeMap.put("吉林", "9");
			city2CodeMap.put("黑龙江", "10");
			city2CodeMap.put("江苏", "11");
			city2CodeMap.put("浙江", "12");
			city2CodeMap.put("安徽", "13");
			city2CodeMap.put("福建", "14");
			city2CodeMap.put("江西", "15");
			city2CodeMap.put("山东", "16");
			city2CodeMap.put("河南", "17");
			city2CodeMap.put("湖北", "18");
			city2CodeMap.put("湖南", "19");
			city2CodeMap.put("广东", "20");
			city2CodeMap.put("广西", "21");
			city2CodeMap.put("海南", "22");
			city2CodeMap.put("四川", "23");
			city2CodeMap.put("贵州", "24");
			city2CodeMap.put("云南", "25");
			city2CodeMap.put("西藏", "26");
			city2CodeMap.put("陕西", "27");
			city2CodeMap.put("甘肃", "28");
			city2CodeMap.put("青海", "29");
			city2CodeMap.put("宁夏", "30");
			city2CodeMap.put("新疆", "31");
		}
	}
	
	public void fetchBeefAMutton(Date date){
		String timeIntStr = DateTimeUtil.formatDate(date, "yyyyMMdd");
		int timeInt = Integer.parseInt(timeIntStr);
		logger.info("=====抓取牛肉羊肉"+timeInt+"批发价======");
		String cnName = "批发市场价格";
		Map<String, String> var2CodeMap = new HashMap<String, String>();//品种-编码 映射
		Map<String, String> city2CodeMap = new LinkedHashMap<String, String>();//城市-编码 映射
		this.initMap(var2CodeMap, city2CodeMap);
		String[] filters = {"span", "class", "c_detail_price"};
		for(String varName:var2CodeMap.keySet()){
			Map<String, String> dataMap = new HashMap<String, String>();//保存表数据
			for(String city: city2CodeMap.keySet()){
				try{
					String detailPrice = fetchUtil.getPrimaryContent(0, baseUrl.replace("#varCode", var2CodeMap.get(varName)).replace("#cityCode", city2CodeMap.get(city)), encoding, varName, 
							filters, null, 0);//今日均价
					if(detailPrice != null && RegexUtil.match(detailPrice, "\\d+(\\.\\d+)?")){
						dataMap.put(city, detailPrice);
					}
					try {
						Thread.sleep((int)(Math.random() * 3000));
					} catch (InterruptedException e1) {
						logger.error(e1);
					}
				} catch(Exception e) {
					logger.error(e);
				}
			}
			if(!dataMap.isEmpty()){
				dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
			} else {
				RecordCrawlResult.recordFailData(className, varName, cnName, "数据为空。");
			}
		}
	}
	@Scheduled
	(cron=CrawlScheduler.CRON_BEEFAMUTTON)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("全国各省羊肉牛肉批发市场价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到全国各省羊肉牛肉价格在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到全国各省羊肉牛肉价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = new Date();
					fetchBeefAMutton(date);
				} catch(Exception e) {
					logger.error("发生未知异常。", e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取全国各省羊肉牛肉价格的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取全国各省羊肉牛肉价格的定时器已关闭");
			}
		}
	}
	public static void main(String[] args) {
		new FeedTradeData().start();
	}

}
