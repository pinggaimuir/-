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
 * 饲料养殖-生猪-全国规模以上生猪定点屠宰企业白条肉平均出厂价格
 * 饲料养殖-生猪-四川生猪价格预警
 * @author ctm
 *
 */
public class PigDataFetch {
	private static final String className = PigDataFetch.class.getName();
	private static final Log logger = LogFactory.getLog(PigDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final String varName = "生猪";
	private static final Map<String, String> CARCASS_MEAT_MAP = new HashMap<String, String>();
	private static final Map<String, String> SCPIG_MAP = new HashMap<String, String>();
	static{
		CARCASS_MEAT_MAP.put("全国规模以上生猪定点屠宰企业白条肉平均出厂价格", "http://www.gov.cn/zhuanti/node_18299.htm");
		SCPIG_MAP.put("四川生猪价格预警", "http://www.scxmsp.gov.cn/nysc/szyc/qsjc/");
	}
	
	public void carcassMeatStart(){
		fetchCarcassMeat(new Date());
	}
	
	private void fetchCarcassMeat(Date date){
		if(date == null){
			date = new Date();
		}
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		for(String cnName:CARCASS_MEAT_MAP.keySet()){
			String urlBase = CARCASS_MEAT_MAP.get(cnName);
			String[] params = {"div","class","pannel-inner pubListBox01"};
			String[] rowColChoose = null;
			String contents = dataFetchUtil.getPrimaryContent(0, urlBase, "utf-8", "白条肉价格列表", params, rowColChoose, 0);
			if(contents != null && !contents.equals("")){
				String tmp = timeInt.substring(0,4)+"-"+timeInt.substring(4,6)+"/"+timeInt.substring(6,8)+"/content_";
				String comp = tmp + "([0-9]*).htm(.+)生猪定点屠宰企业白条肉平均出厂价格";
				int[] index={1};
				List<String> results = dataFetchUtil.getMatchStr(contents, comp, index);
				if(results.size() > 0){
					//String url = "http://www.gov.cn/xinwen/" + tmp + results.get(0)+".htm";
				}else{
					logger.error("");
				}
			}else{
				logger.error("");
			}
		}
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_SC_PIG)
	public void SCPigStart(){
		String switchFlag = new CrawlerManager().selectCrawler("生猪", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到生猪的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchSCPig(new Date());
			}else{
				logger.info("抓取生猪的定时器已关闭");
			}
		}
	}
	
	private void fetchSCPig(Date date){
		if(date == null){
			date = new Date();
		}
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		String timeYestoday = DateTimeUtil.formatDate(DateTimeUtil.addDay(date, -1), "yyyyMMdd");
		for(String cnName:SCPIG_MAP.keySet()){
			String urlBase = SCPIG_MAP.get(cnName);
			logger.info("开始抓取四川省农业厅全省监测@" + urlBase);
			String[] params = {"div","class","p_list"};
			String[] rowColChoose = null;
			String contents = dataFetchUtil.getPrimaryContent(0, urlBase, "utf-8", "四川省农业厅全省监测", params, rowColChoose, 0);
			if(contents != null && !contents.equals("")){
				logger.info("开始匹配全省监测中的每周生猪监测报告");
				Map<String, String> dataMap = new HashMap<String, String>();
				String[] liStr = contents.split("<li>");
				String needStr = "";
				for(String li:liStr){
					if(li.indexOf("周生猪监测") != -1){
						needStr = li;
						break;
					}
				}
				String tmp = "("+timeInt+"|"+timeYestoday+")";
				String comp = tmp + "_([0-9]*).html";
				int[] index={1,2};
				List<String> results = dataFetchUtil.getMatchStr(needStr, comp, index);
				if(results.size() > 0){
					String timeIntSave = results.get(0);
					String url = urlBase + timeIntSave.substring(0,6)+"/t"+ timeIntSave + "_"+results.get(1) + ".html";
					logger.info("开始抓取四川生猪价格预警-"+varName+"@"+url);
					String[] params1 = {"table","class","MsoNormalTable"};
					String[] rowColChoose1 = {"01000000", "011111111"};
					String contents1 = dataFetchUtil.getPrimaryContent(0, url, "utf-8", varName, params1, rowColChoose1, 0);
					if(contents1 != null && !contents1.equals("")){
						String[] fields = contents1.split(",");
						dataMap.put("出栏肉猪平均价", fields[0]);
						dataMap.put("仔猪平均价", fields[1]);
						dataMap.put("后备母猪平均价", fields[2]);
						dataMap.put("猪肉平均价", fields[3]);
						dataMap.put("玉米平均价", fields[4]);
						dataMap.put("豆粕平均价", "0");
						dataMap.put("小麦麸平均价", fields[5]);
						dataMap.put("育肥猪配合料价格", fields[6]);
						dataMap.put("猪粮比", fields[7].substring(0,fields[7].length()-3));
					}else{
						logger.error("没有抓取到表格中的生猪价格");
					}
					String[] params2 = {"div","class","TRS_Editor"};
					String contents2 = dataFetchUtil.getPrimaryContent(0, url, "utf-8", varName, params2, null, 0);
					if(contents2 != null && !contents2.equals("")){
						String comp2 = "猪料比(.+)(到|至|和|与)([0-9|/.]*)比1";
						int[] index2 = {3};
						List<String> res = dataFetchUtil.getMatchStr(contents2, comp2, index2);
						if(res.size() > 0){
							dataMap.put("猪料比", res.get(0));
						}else{
							logger.error("没有匹配到猪料比");
						}
					}else{
						logger.error("没有抓取到段落中的生猪价格");
					}
					if(dataMap.size() > 0){
						logger.info("开始保存"+varName+"-"+cnName);
						dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntSave), dataMap);
					}else{
						logger.error("没有生猪价格数据需要保存");
					}
				}else{
					logger.error("没有监测到每周的生猪监测报告");
				}
			}else{
				logger.error("没有抓取到四川省农业厅全省监测");
			}
		}
	}
	
	public static void main(String[] args){
		Date date = DateTimeUtil.parseDateTime("20150501", "yyyyMMdd");
		new PigDataFetch().SCPigStart();
	}
}
