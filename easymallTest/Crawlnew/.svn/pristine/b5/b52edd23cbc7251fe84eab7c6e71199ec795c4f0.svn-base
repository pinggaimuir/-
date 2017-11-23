package cn.futures.data.importor.crawler.marketPrice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 北京新发地市场数据爬虫
 * */
public class XinfadiMarketPrice {
	//http://www.xinfadi.com.cn/marketanalysis/1/list/1.shtml//数据源
	private static String className = XinfadiMarketPrice.class.getName();
	private static final String baseUrl = "http://www.xinfadi.com.cn/marketanalysis/#class/list/#pageNo.shtml?prodname=&begintime=#begintime&endtime=#endtime";//数据源
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final String marketName = "北京丰台区新发地农产品批发市场";//批发市场名称
	private static final Logger LOG = Logger.getLogger(XinfadiMarketPrice.class);
	private static final Map<String, String> proNameBySign = new HashMap<String, String>();//产品名映射（只包含与表中存储不一致的）
	static {
		proNameBySign.put("赣南脐橙Ф65-Ф95", "赣南脐橙65_95");
		proNameBySign.put("鄂、湘、川脐橙", "鄂_湘_川脐橙");
		proNameBySign.put("血橙/二月红/中华红", "血橙_二月红_中华红");
		proNameBySign.put("无籽小号-大号", "无籽小号_大号");
		proNameBySign.put("小兰.特小凤", "小兰_特小凤");
		proNameBySign.put("南美、菲律宾香蕉", "南美_菲律宾香蕉");
		proNameBySign.put("富士（纸袋Φ70-84）", "富士（纸袋70_84）");
		proNameBySign.put("富士（纸袋Φ85以上）", "富士（纸袋85以上）");
		proNameBySign.put("花牛(Φ70-85)", "花牛(70_85)");
	}
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("北京新发地市场数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到北京新发地市场数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到北京新发地市场数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetch(date);
			}else{
				LOG.info("抓取北京新发地市场数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取北京新发地市场数据的定时器已关闭");
			}
		}
	}
	
	public void fetch(Date date){
		LOG.info("------开始抓取北京新发地批发市场数据------");
		int timeInt = Integer.parseInt(DateTimeUtil.formatDate(date, DateTimeUtil.YYYYMMDD));
		String begintime = DateTimeUtil.formatDate(date, DateTimeUtil.YYYY_MM_DD);//起始日期（即要查的数据的日期）
		Date endDate = DateTimeUtil.addDay(date, 1);
		String endtime = DateTimeUtil.formatDate(endDate, DateTimeUtil.YYYY_MM_DD);//截止日期（要查数据的时间的后一天）
		for(int classId = 1; classId <= 5; classId++ ){
			LOG.info("--------------抓取class：" + classId + "--------------");
			String tempUrl = baseUrl.replace("#begintime", begintime).replace("#endtime", endtime).replace("#class", classId + "");
			String curUrl = tempUrl.replace("#pageNo", "1");
			String content1 = dataFetchUtil.getCompleteContent(0, curUrl, Constants.ENCODE_UTF8, "北京新发地市场数据");
			int count = 0;//查询总条数
			String countRegex = "符合搜索条件的结果共有 <em style=\"color:red;font-weight:bold;\">(\\d+)</em> 条";
			List<String> countList = RegexUtil.getMatchStr(content1, countRegex, new int[]{1});
			if(!countList.get(0).isEmpty()){
				count = Integer.parseInt(countList.get(0));
			}
			if(count > 0){
				int pageCount = (count % 20 == 0) ? (count / 20) : (count / 20 + 1);//总页数，根据总条数计算页数，一页20条
				for(int i = 1; i <= pageCount;i++){//循环查询所有页
					LOG.info("------------抓取第" + i + "页--------------");
					curUrl = tempUrl.replace("#pageNo", i + "");
					String contentI = dataFetchUtil.getCompleteContent(0, curUrl, Constants.ENCODE_UTF8, "北京新发地市场数据");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						LOG.error(e);
					}
					String tableCont = dataFetchUtil.parseContent(contentI, Constants.ENCODE_UTF8, 
							new String[]{"table", "class", "hq_table"}, 0, new String[]{"0", "1111110"});
					String[] lines = tableCont.split("\n");
					for(int lineId = 0; lineId < lines.length; lineId++){
						String[] cols = lines[lineId].split(",");
						Map<String, String> priceInfo = new HashMap<String, String>();
						if(cols[5].equals("斤")){//因为保存时按公斤保存的
							priceInfo.put("min_price", String.valueOf(Float.parseFloat(cols[1]) * 2));
							priceInfo.put("max_price", String.valueOf(Float.parseFloat(cols[3]) * 2));
							priceInfo.put("ave_price", String.valueOf(Float.parseFloat(cols[2]) * 2));
						} else {
							priceInfo.put("min_price", cols[1]);
							priceInfo.put("max_price", cols[3]);
							priceInfo.put("ave_price", cols[2]);
						}
						priceInfo.put("produce_area", null);
						priceInfo.put("specification", cols[4]);
						String productName = null;
						if(proNameBySign.containsKey(cols[0])){
							productName = proNameBySign.get(cols[0]);
						} else {
							productName = cols[0];
						}
						dao.saveOrUpdateForMarketPrice(productName, marketName, timeInt, priceInfo);
					}
				}
			} else {
				LOG.info("查询无结果");
			}
		}
	}
	
	public static void main(String[] args) {
		XinfadiMarketPrice x = new XinfadiMarketPrice();
		x.start();
		
		//补指定日期的历史数据
//		Date tarDate = DateTimeUtil.parseDateTime("20170104", DateTimeUtil.YYYYMMDD);
//		x.fetch(tarDate);
	}
	
}
