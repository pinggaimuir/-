package cn.futures.data.importor.crawler;

import java.util.Date;

import java.util.HashMap;
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
 * 中国纺织经济信息网：纱线、坯布
 * @author ctm
 *
 */
public class CTEICNFetch {
	private static final String className = CTEICNFetch.class.getName();
	private DAOUtils dao = new DAOUtils();
	private Log logger = LogFactory.getLog(CTEICNFetch.class);
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private String url = "http://www.ctei.cn/";
	private Map<String, String> varName2cnNameMap = new HashMap<String, String>(){
		{
			put("棉布-show_zhishu_pb","坯布价格");
			put("棉纱-show_zhishu_sx","纱线价格");
		}
	};
	private Map<String, String> cnName2htmlMap = new HashMap<String, String>(){
		{
			put("10支纯棉气流纱","10支纯棉气流纺");
			put("32支纯棉普梳纱","32支纯棉普梳");
			put("40支纯棉精梳纱","40支纯棉精梳");
			put("45支涤棉混纺纱","45支棉混纺");
			put("32支纯涤纶纱","32支纯涤沦");
			put("30支纯粘胶纱","30支纯粘胶");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_CTEICN_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("纱线、坯布价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到纱线、坯布价格的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetch(DateTimeUtil.addDay(date, -1));
			}else{
				logger.info("抓取纱线、坯布价格的定时器已关闭");
			}
		}
	}
	
	private void fetch(Date date) {
		logger.info("=====开始抓取中国纺织经济信息网：纱布、坯布价格=====");
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		for(String varNameACode:varName2cnNameMap.keySet()){
			String cnName = varName2cnNameMap.get(varNameACode);
			String varName = varNameACode.split("-")[0];
			int varId = Variety.getVaridByName(varName);
			String id = varNameACode.split("-")[1];
			String[] filters = {"span", "id", id};
			String contents = fetchUtil.getPrimaryContent(0, url, "utf-8", varName, filters, null, 0);
			if(contents != null && !contents.equals("")){
				contents = contents.replaceAll("<([^>]+)>", "").replaceAll("(\\s+)", ",");
				String[] fields = contents.split(",");
				if(!fields[fields.length-1].substring(3).equals(timeInt)){
					logger.info("当天的数据网页没有更新");
					timeInt = fields[fields.length-1].substring(3);
				}
				Map<String, String> dataMap = new HashMap<String, String>();
				for(int i=1;i<fields.length;i++){
					if(fetchUtil.isNumeric(fields[i])&& !fetchUtil.isNumeric(fields[i-1])){
						String key = fields[i-1];
						if(cnName2htmlMap.get(key)!=null){
							key = cnName2htmlMap.get(key);
						}
						dataMap.put(key, fields[i]);
					}
				}
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}
		}
	}
	public static void main(String[] args){
		new CTEICNFetch().start();
	}
}
