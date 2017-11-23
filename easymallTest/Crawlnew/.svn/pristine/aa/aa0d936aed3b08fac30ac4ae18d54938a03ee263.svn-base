package cn.futures.data.importor.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 饲料养殖-玉米-深加工企业收购价
 * @author bric_yangyulin
 * @date 2016-11-03
 * */
public class CornPurchasePrice {
	private static String className = CornPurchasePrice.class.getName();
	private static final String varName = "玉米";
	private static final String cnName = "深加工企业收购价";
	private static final Set<String> colSet = new HashSet();//表中所含列名
	private static final String listUrl = "http://www.yumi.com.cn/yumijiage/alist/code_id/4319.html";//列表页链接
	private static final String encoding = "utf-8";
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(CornPurchasePrice.class);
	
	static {
		colSet.add("中粮生化（龙江）");
		colSet.add("青冈龙凤淀粉厂");
		colSet.add("嘉吉生化");
		colSet.add("吉林燃料乙醇");
		colSet.add("黄龙食品");
		colSet.add("四平天成");
		colSet.add("中粮生化（公主岭）");
		colSet.add("中粮生化（榆树）");
		colSet.add("长春大成");
		colSet.add("潍坊英轩实业");
		colSet.add("寿光金玉米");
		colSet.add("新丰淀粉");
		colSet.add("诸城兴贸");
		colSet.add("德州福洋");
		colSet.add("盛泰药业");
		colSet.add("滨州西王");
		colSet.add("沂水大地");
		colSet.add("临清金玉米");
		colSet.add("恒仁工贸");
		colSet.add("丰原集团");
		colSet.add("骊骅淀粉");
		colSet.add("宁晋玉锋");
		colSet.add("德瑞淀粉");
		colSet.add("榆树生化");
		colSet.add("西安国维");
		colSet.add("德州中谷淀粉");
		colSet.add("吉林梅河口");
		colSet.add("肇东中粮");
		colSet.add("蚌埠中粮生化");
		colSet.add("祥瑞药业");
		colSet.add("华辰淀粉");
		colSet.add("全国");
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_CORNPURCHASEPRICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("玉米深加工企业收购价", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到玉米深加工企业收购价在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date curDay = new Date();
					String timeIntStr = DateTimeUtil.formatDate(curDay, DateTimeUtil.YYYYMMDD);
					String detailUrl = fetchDetailUrl(timeIntStr);
					if(detailUrl != null){
						fetchData(detailUrl, timeIntStr);
					}
				} catch(Exception e){
					RecordCrawlResult.recordFailData(className, varName, cnName, "发生未知异常。");
				}
			}else{
				LOG.info("抓取玉米深加工企业收购价的定时器已关闭");
			}
		}
	}
	
	/**
	 * 获取详情页链接
	 * */
	public String fetchDetailUrl(String timeIntStr){
		String year = timeIntStr.substring(0, 4);
		String month = timeIntStr.substring(4, 6);
		String day = timeIntStr.substring(6, 8);
		if(month.startsWith("0")){
			month = month.substring(1);
		}
		if(day.startsWith("0")){
			day = day.substring(1);
		}
		String hrefRegex = "<a href=\"([^\"]+)\"[^>]*title=\"" + year + "年" + month + "月" + day + "日国内玉米深加工企业即时报价\"\\s>";
		String content = fetchUtil.getCompleteContent(0, listUrl, encoding, "玉米_深加工企业收购价list.txt");
		List<String> urlList = RegexUtil.getMatchStr(content, hrefRegex, new int[]{1});
		String detailUrl = null;
		if(urlList != null && !urlList.isEmpty() && !urlList.get(0).isEmpty()){
			detailUrl = "http://www.yumi.com.cn/" + urlList.get(0);
		} else {
			LOG.warn("没有获取到详情页链接。");
			RecordCrawlResult.recordFailData(className, varName, cnName, "没有获取到详情页链接。");
		}
		return detailUrl;
	}
	
	/**
	 * 获取数据
	 * */
	public void fetchData(String detailUrl, String timeIntStr){
		List<String> emptyCompany = new ArrayList<String>();//记录出现公司名而没有相应价格数据的公司名（这样的取最近一条数据来补充）
		String[] filters = {"table", "class", "MsoNormalTable ke-zeroborder"};
		String[] rowColChoose = {"00", "11000"};
		String content = fetchUtil.getPrimaryContent(0, detailUrl, encoding, varName + "_" + cnName, filters, rowColChoose, 0);
		if(content != null && !content.isEmpty()){
			Map<String, String> dataMap = new HashMap<String, String>();
			String[] rows = content.split("\n");
			double sumPrice = 0;//总价
			int num = 0;//个数
			for(String row: rows){
				String[] columns = row.split(",");
				if(columns.length == 2 && columns[0] != null){
					if(columns[0].equals("中粮生化（龙江）公司")){
						columns[0] = "中粮生化（龙江）";
					}
					if(colSet.contains(columns[0])){
						if(columns[1] != null && columns[1].matches("\\d+(.\\d+)?")){
							dataMap.put(columns[0], columns[1]);
							sumPrice += Double.parseDouble(columns[1]);
							num++;
						} else {
							emptyCompany.add(columns[0]);
						}
					} else {
						LOG.warn("该行数据不需要。" + row);
					}
				} else {
					LOG.warn("该行数据不符合期望格式。" + row);
				}
			}
			//对于价格列为空值的公司，取最近一条数据补充
			int varId = Variety.getVaridByName(varName);
			List<String> newestData = dao.getNewestListValues(varId, cnName, emptyCompany);
			for(int index = 0; index < emptyCompany.size(); index++){
				String tmp = newestData.get(index);
				if(tmp != null && tmp.matches("\\d+(.\\d+)?")){
					dataMap.put(emptyCompany.get(index), newestData.get(index));
					sumPrice += Double.parseDouble(newestData.get(index));
					num++;
				}
			}
			//入库
			if(!dataMap.isEmpty()){
				double average = sumPrice / num;
				dataMap.put("全国", String.valueOf(average));
				dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), dataMap);
			} else {
				LOG.warn("未提取到有效数据。");
				RecordCrawlResult.recordFailData(className, varName, cnName, "未提取到有效数据。");
			}
		}
	}
	
	public static void main(String[] args) {
		CornPurchasePrice c = new CornPurchasePrice();
		c.start();
		
		//补指定日期的历史数据
//		String targetTime = "20161104";
//		String detailUrl = c.fetchDetailUrl(targetTime);
//		if(detailUrl != null){
//			c.fetchData(detailUrl, targetTime);
//		}
	}
}
