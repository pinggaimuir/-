package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.DateTimeUtil;

/**
 * 饲料养殖-国际运费-波罗的海运价指数
 * @author ctm
 *
 */
@Component
public class BalticDryIndexFetch {
	private static final String className = BalticDryIndexFetch.class.getName();
	private String cnName = "波罗的海运价指数";
	private static final Log logger = LogFactory.getLog(BalticDryIndexFetch.class);
	private static final Map<String, String> BALTIC_DRY_INDEX_MAP = new HashMap<String, String>();
	static{
		BALTIC_DRY_INDEX_MAP.put("国际运费", "http://www.cnss.com.cn/caches/task/exponent/%kind%/month.json?v=");
	}
	public static final Map<String, String> KIND_MAP = new HashMap<String, String>();
	static{
		KIND_MAP.put("BDI干散货运价指数","bdi");
		KIND_MAP.put("BCI海岬型船运价指数","bci");
		KIND_MAP.put("BPI巴拿马型船运价指数","bpi");
		KIND_MAP.put("BSI超灵便型船运价指数","bsi");
		KIND_MAP.put("BHSI灵便型船运价指数","bhsi");
		}
	private DAOUtils dao = new DAOUtils();
	
	@Scheduled
	(cron=CrawlScheduler.CRON_BALTIC_DRY_INDEX)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("波罗的海运价指数", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到"+cnName+"在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -1);
				fetchData(date);
			}else{
				logger.info("抓取"+cnName+"的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = DateTimeUtil.addDay(new Date(), -1);
		}
		for(String varName:BALTIC_DRY_INDEX_MAP.keySet()){
			String url = BALTIC_DRY_INDEX_MAP.get(varName);
			int varId = Variety.getVaridByName(varName);
			Map<String, Map<String, String>> dateKindDataMap = new HashMap<String, Map<String, String>>();//指数 时间 数据
			Set<String> dateSave = new HashSet<String>();
			int newestTimeInt = DAOUtils.getNewestTimeInt(dao.getTableName(varId, cnName));
			Date newestDate = DateTimeUtil.parseDateTime(newestTimeInt+"", "yyyyMMdd");
			for(String kind:KIND_MAP.keySet()){
				String fetchUrl = url.replace("%kind%", KIND_MAP.get(kind))+new Date().getTime();
				logger.info("start fetch " + varName +"-"+ cnName + "-" + kind + "@" + fetchUrl);
				String body = CrawlerUtil.httpGetBody(fetchUrl);
				if (body == null) {
					logger.error("无法获取"+varName+kind+"数据");
				}
				/*示例：[{"index":"435","date":"2015-03-23"},{"index":"448","date":"2015-03-24"},
				 * {"index":"455","date":"2015-03-25"},{"index":"456","date":"2015-03-26"},
				 * {"index":"456","date":"2015-03-27"},{"index":"460","date":"2015-03-30"},
				 * {"index":"475","date":"2015-03-31"},{"index":"463","date":"2015-04-01"},
				 * {"index":"454","date":"2015-04-02"},{"index":"448","date":"2015-04-07"},
				 * {"index":"450","date":"2015-04-08"},{"index":"461","date":"2015-04-09"},
				 * {"index":"466","date":"2015-04-10"},{"index":"460","date":"2015-04-13"},
				 * {"index":"470","date":"2015-04-14"},{"index":"494","date":"2015-04-15"},
				 * {"index":"521","date":"2015-04-16"},{"index":"532","date":"2015-04-17"}] */
				String[] lines = body.split("},");
				Map<String, String> datas = new HashMap<String, String>();
				for(String line:lines){
					String[] contents = line.split("\"");
					if(contents.length<4) continue;
					datas.put(contents[7], contents[3]);
				}
				Map<String, String> date2data = new HashMap<String, String>();//时间 数据
				for(String dateTmp:datas.keySet()){
					Date d = DateTimeUtil.parseDateTime(dateTmp, "yyyy-MM-dd");
					if(d.before(DateTimeUtil.addDay(date, 1)) && d.after(newestDate)){
						dateSave.add(dateTmp);
						date2data.put(dateTmp, datas.get(dateTmp));
					}
				}
				dateKindDataMap.put(kind, date2data);
			}
			//当天没有数据,不需要保存
			if(dateSave.size() > 0){
				for(String timeTmp:dateSave){
					Map<String, String> kindDatas = new HashMap<String, String>();
					for(String kind:dateKindDataMap.keySet()){
						Map<String, String> date2data = dateKindDataMap.get(kind);
						String dataTmp = date2data.get(timeTmp);
						if(dataTmp == null){
							kindDatas.put(kind, "0");
						}else{
							kindDatas.put(kind, dataTmp);
						}
					}
					String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(timeTmp, "yyyy-MM-dd"), "yyyyMMdd");
					dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeInt), kindDatas);
				}
			}else{
				logger.info("没有需要保存的数据");
			}
		}
	}
	
	public static void main(String []args){
		new BalticDryIndexFetch().start();
	}
}
