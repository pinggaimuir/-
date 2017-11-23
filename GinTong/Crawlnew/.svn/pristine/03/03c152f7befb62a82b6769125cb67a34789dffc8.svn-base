package cn.futures.data.importor.crawler;

import java.util.ArrayList;
import java.util.Arrays;
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
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;

/**
 * 棉花纺织-纱布-纱价格
 * 棉花纺织-化纤-粘胶短纤价格
 * 棉花纺织-化纤-涤纶短纤价格
 * @author ctm
 *
 */
public class CottonTextilePriceFetch {
	private static final String className = CottonTextilePriceFetch.class.getName();
	private static final String url = "http://dc.cncotton.com/dc/data/viewTextileIndex.action";
	private static final String sfUrl = "http://www.sfmianhua.com/news/list-9.html";
	private static final Log logger = LogFactory.getLog(CottonTextilePriceFetch.class);
	private DAOUtils dao = new DAOUtils();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	public static final Map<String, String> VNAME_MAP = new HashMap<String, String>();//<指标名,品种名>
	static{
		VNAME_MAP.put("纱价格","纱布");
		VNAME_MAP.put("粘胶短纤价格","化纤");
		VNAME_MAP.put("涤纶短纤价格","化纤");
		}
	private static Map<String, String> sfvarName2cnNameMap = new HashMap<String, String>(){
		{
			put("棉壳", "出厂价");
			put("棉短绒", "出厂价");
			put("棉油", "出厂价");
			put("棉籽", "油厂到厂价");
		}
	};
	private static List<String> sfheaders = Arrays.asList("山东德州","山东菏泽","安徽安庆","河北衡水","河南安阳","湖北潜江","湖南岳阳","江苏盐城","山西运城","陕西渭南","新疆阿克苏");
	
	@Scheduled
	(cron=CrawlScheduler.CRON_COTTON_TEXTILE_PRICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("纱布、粘胶短纤、涤纶短纤价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到纱布、粘胶短纤、涤纶短纤价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchData(date);
			}else{
				logger.info("抓取纱布、粘胶短纤、涤纶短纤价格的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
		}
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		Map<String, Integer> varityMap = new HashMap<String, Integer>();//<品种名，品种ID>
		String tips = "";
		for(String cnName:VNAME_MAP.keySet()){
			String varName = VNAME_MAP.get(cnName);
			varityMap.put(varName, Variety.getVaridByName(varName));
			tips += varName + "-"+cnName+" ";
		}
		logger.info("start fetch " + tips+ "@" + url);
		String contents = CrawlerUtil.httpGetBody(url);
		if (contents == null) {
			logger.error("抓取"+tips+"所需要的数据为空");
		}else{
			logger.info("开始分析数据");
			String[] fields = contents.split("\":\"");
			String[]  tmp = fields[2].substring(0,5).split("-");
			String dateHtml = tmp[0]+tmp[1];
			String fdate = DateTimeUtil.formatDate(date, "MMdd");
			if(Integer.parseInt(dateHtml)<=Integer.parseInt(fdate)){
				String timeIntSave = timeInt.substring(0,4) + dateHtml;
				Map<String, String> dataMap = new HashMap<String, String>();
				logger.info("保存纱价格");
				String cnName = "纱价格";
				dataMap.put("全国", fields[3].split("\",\"")[0].trim());
				dao.saveOrUpdateByDataMap(varityMap.get(VNAME_MAP.get(cnName)), cnName, Integer.parseInt(timeIntSave), dataMap);
				logger.info("保存粘胶短纤价格");
				cnName = "粘胶短纤价格";
				dataMap.put("全国", fields[13].split("\",\"")[0].trim());
				dao.saveOrUpdateByDataMap(varityMap.get(VNAME_MAP.get(cnName)), cnName, Integer.parseInt(timeIntSave), dataMap);
				logger.info("保存涤纶短纤价格");
				cnName = "涤纶短纤价格";
				dataMap.put("全国", fields[11].split("\",\"")[0].trim());
				dao.saveOrUpdateByDataMap(varityMap.get(VNAME_MAP.get(cnName)), cnName, Integer.parseInt(timeIntSave), dataMap);
			}else{
				logger.warn("当天数据没有更新");
			}
		}
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_COTTON_SFMIANHUA)
	public void sfstart(){
		String switchFlag = new CrawlerManager().selectCrawler("棉壳、棉短绒、棉油棉籽价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到棉壳、棉短绒、棉油棉籽价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchSFMianHua(date);
			}else{
				logger.info("抓取棉壳、棉短绒、棉油棉籽价格的定时器已关闭");
			}
		}
	}
	//顺丰棉花网
	private void fetchSFMianHua(Date date){
		logger.info("=====开始抓取顺丰棉花网：棉壳、棉短绒、棉油棉籽=====");
		boolean update = false;
		String monthTime = DateTimeUtil.formatDate(date, "M月d日");
		int year = DateTimeUtil.getCurrentYear();
		String[] filters = {"div", "class", "catlist"}; 
		String[] priceFilter = {"table","tbody"};
		String[] rowColChoose = {"0","10100"};
		String contents = fetchUtil.getPrimaryContent(0, sfUrl, "gbk", "顺丰棉花网棉副资讯", filters, null, 0);
		if(contents != null && !contents.equals("")){
			String[] lis = contents.split("<li class=\"catlist_li\">");
			for(String li:lis){
				String comp = "href=\"([^\"]+)\"([^>]+)>国内(.+)最新价格([^<]+)<";
				int[] index={1,3,4};//href="([^"]+)"([^>]+)>国内(.+)最新价格([^<]+)<
				List<String> results = fetchUtil.getMatchStr(li, comp, index);
				if(results.size() > 0 && sfvarName2cnNameMap.keySet().contains(results.get(1))){
					//if(results.get(2).equals(monthTime)){
					String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(year+"年"+results.get(2), "yyyy年MM月dd日"), "yyyyMMdd");
						String varName = results.get(1);
						int varId = Variety.getVaridByName(varName);
						String cnName = sfvarName2cnNameMap.get(varName);
						update = true;
						String priceContents = fetchUtil.getPrimaryContent(0, results.get(0), "gbk", results.get(1), priceFilter, rowColChoose, 0);
						if(priceContents != null && !priceContents.equals("")){	
							Map<String, String> dataMap = new HashMap<String, String>();
							String[] lines = priceContents.split("\n");
							List<String> noDataHeaders = new ArrayList<String>();
							for(String line:lines){
								String[] fields = line.split(",");
								if(sfheaders.contains(fields[0])){
									dataMap.put(fields[0], fields[1]);
								}
							}
							//没有的字段数据取数据库中最近的对应字段数据
							for(String header:sfheaders){
								if(dataMap.get(header) == null){
									noDataHeaders.add(header);
								}
							}
							List<String> values = dao.getNewestListValues(varId, cnName, noDataHeaders);
							for(int i=0;i<noDataHeaders.size();i++){
								dataMap.put(noDataHeaders.get(i), values.get(i));
							}
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
						}
					//}
				}
			}
			if(!update){
				logger.info("网页尚未更新！");
			}
		}
	}
	
	public static void main(String[] args){
		new CottonTextilePriceFetch().sfstart();
		new CottonTextilePriceFetch().start();
	}
}
