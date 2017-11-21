package cn.futures.data.importor.crawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 中国价格信息网：生猪-猪料比（发改委）
 * @date 20160921
 * @author bric_yangyulin
 * */
public class ChinaPriceInfo {
	private static final Logger LOG = Logger.getLogger(ChinaPriceInfo.class);
	private static String className = ChinaPriceInfo.class.getName();
	private static final String encoding = "gb2312";
	private static DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static DAOUtils dao = new DAOUtils();
	
	@Scheduled(cron = CrawlScheduler.CRON_CN_PRICE_INFO)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("中国价格信息网数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到中国价格信息网数据爬虫的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到中国价格信息网数据爬虫的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					fetchData();
				} catch(Exception e){
					LOG.error("发生未知异常：", e);
					RecordCrawlResult.recordFailData(className, null, null, "发生未知异常");
				}
			}else{
				LOG.info("抓取中国价格信息网数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取中国价格信息网数据的定时器已关闭");
			}
		}
	}
	/**
	 * 抓最近一次发布的数据，数据库中已有则更新。
	 * */
	public void fetchData(){
		String varName = "生猪";//品种名
		String cnName = "猪料比（发改委）";//中文名
		String[] filters = {"table", "class", "B14"};
		String listUrl = "http://www.chinaprice.com.cn/fgw/chinaprice/free/yaobao/zhuliao_0_0_0.htm";//列表页链接
		String listContent = dataFetchUtil.getPrimaryContent(1, listUrl, encoding, varName, filters, null, 0);//列表页内容

		String compStr = "<a[^>]*href=\"([^\"]+)\"[^>]*>\\s*\\d月第\\d周猪料、鸡料、蛋料比价\\s*</a>";//用于匹配详情页链接
		List<String> detail = RegexUtil.getMatchStr(listContent, compStr, new int[]{0, 1});
		String detailHref = "http://www.chinaprice.com.cn" + detail.get(1);
		String[] detailFilter = {"div", "id", "div"};
		String detailContent = dataFetchUtil.getPrimaryContent(0, detailHref, encoding, "生猪", detailFilter, null, 0);
		String timeRegex = "<div>[^#]*生猪[^#]*(\\d{4})年(\\d{2})月(\\d{2})日[^#]*#";//匹配数据详情对应的时间序列
		List<String> timeList = RegexUtil.getMatchStr(detailContent.replace("</div>", "#"), timeRegex, new int[]{0, 1, 2, 3});
		String timeIntStr = timeList.get(1) + timeList.get(2) + timeList.get(3);//数据详情的时间序列
		String priceRegex = "<tbody>[^#]*生猪价格[^#]*(<tr>[^@]*本周[^@]*@)[^#]*#";
		List<String> priceList = RegexUtil.getMatchStr(detailContent.replace("</tbody>", "#").replace("</tr>", "@"), priceRegex, new int[]{1});
		String thisweek = priceList.get(0);//本周信息块
		if(thisweek != null){
			thisweek = thisweek.replaceAll("<[^>]+>", "");
			priceRegex = "\\d+(.\\d+)?";
			List<String> prices = RegexUtil.getMatchStr(thisweek, priceRegex);//价格数据
			if(prices.size() == 5){
				Map<String, String> priceByColumn = new HashMap<String, String>();
				String[] columns = {"生猪价格", "饲料价格", "猪料比价", "猪料比价平衡点", "预期盈利"};
				for(int i = 0; i < columns.length; i++){
					priceByColumn.put(columns[i], prices.get(i));
				}
				dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), priceByColumn);
			} else {
				RecordCrawlResult.recordFailData(className, varName, cnName, "\"价格数据不是5个,格式可能发生了变化。\"");
			}
		}
	}
	public static void main(String[] args) {
		ChinaPriceInfo cpi = new ChinaPriceInfo();
		cpi.start();
	}
}
