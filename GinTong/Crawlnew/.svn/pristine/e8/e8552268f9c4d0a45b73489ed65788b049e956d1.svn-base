package cn.futures.data.importor.crawler.mofcomCrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;

/**
 * 大宗农产品进口数据
 * @author ctm
 *
 */
public class AgriImportsData {
	
	private Log logger = LogFactory.getLog(AgriImportsData.class);
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String url = "http://wms.mofcom.gov.cn/article/wb/";
	private Map<String, String> cnNameMap = new HashMap<String, String>(){
		{
			put("odd", "进口装船,本期进口装船（装运港）");
			put("even", "进口到港,本期进口到港（分海关）");
			put("last", "本期进口到港（口岸）");
		}
	};
	private Map<String, String> varNameMap = new HashMap<String, String>(){
		{
			put("鲜奶","乳品");
			put("奶粉","乳品");
			put("牛肉及其副产品","牛肉");
			put("羊肉及其副产品","羊肉");
			put("猪肉及其副产品","猪肉");
			put("玉米酒糟","DDGS");
		}
	};
	
	/**
	 * 今天抓昨天更新的数据
	 */
	@Scheduled
	(cron=CrawlScheduler.CRON_MOF_IMPORT_DATA)
	public void startFetch(){
		logger.info("=====开始抓取大宗农产品进口数据=====");
		String yesterday = DateTimeUtil.formatDate(DateTimeUtil.addDay(new Date(), -1), "yyyy-MM-dd");
		//String yesterday = "2015-06-19";
		String[] hrefFilters = {"div","class","alist"};
		boolean update = false;
		//for(int page=19;page<31;page++){
			String pageUrl = url;
		//	if(page == 1) pageUrl += "?";
		//	else pageUrl += "?"+page;
			String hrefContents = fetchUtil.getPrimaryContent(0, pageUrl, "utf-8", "大宗农产品进口信息", hrefFilters, null, 0);
			if(hrefContents != null && !hrefContents.equals("")){
				String[] lies = StringUtils.substringsBetween(hrefContents, "<li>", "</li>");
				String comp = "href=\"/article/wb/([^\"]+)\">大宗农产品进口信息发布(.+)发布日期：([^<]+)<(.+)"+yesterday;
				int[] index={1,3};
				String[] filters = {"div", "id", "zoom"};
				for(String li:lies){
					List<String> results = fetchUtil.getMatchStr(li, comp, index);
					if(results.size() > 0){
						update = true;
						String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(results.get(1), "yyyy年MM月dd日"),"yyyyMMdd");
						String contents = fetchUtil.getPrimaryContent(0, url + results.get(0), "utf-8", "大宗农产品进口信息", filters, null, 0);
						if(contents != null && !contents.equals("")){
							parseAndSave(contents, timeInt);
						}else{
							logger.error("没有抓取到"+timeInt + "大宗农产品进口信息数据");
						}
					}
				}
				if(!update){
					logger.error("大宗农产品进口信息没有最新更新");
				}
			}else{
				logger.error("没有抓取到大宗农产品进口信息的链接数据");
			}
		//}
	}
	
	private void parseAndSave(String contents, String timeInt){
		Map<String, String> header2dataMap = new HashMap<String, String>();
		Map<String, String> dataMap = new HashMap<String,String>();
		Map<Integer, String> varNameIndexMap = new HashMap<Integer, String>();
		String varName = "";
		String[] tables = StringUtils.substringsBetween(contents, "<TABLE", "</TABLE>");
		String titles = contents.substring(0, contents.indexOf("单位：吨"));
		System.out.println(titles);
		String[] tmps = (titles+">").replaceAll("(<[^>]+>)", ",").replaceAll("([,]+)", ",").split(",");
//		List<String> st = Arrays.asList("1","2","3","4","5","6"); 
		for(int i=tmps.length-1;i>=1;i--){
			if(!fetchUtil.isNumeric(tmps[i-1].substring(0,1)) && !tmps[i-1].substring(0,1).equals("&")) break;
			if(tmps[i-1].substring(0,1).equals("&")) continue;
			varNameIndexMap.put(Integer.parseInt(tmps[i-1].substring(0,1)), tmps[i]);
//			if(st.contains(tmps[i].trim())){
//				varNameIndexMap.put(Integer.parseInt(tmps[i]), tmps[i+1]);
//			}
		}
		if(varNameIndexMap.size()*2+2 == tables.length) {
		for(int tableIndex=1;tableIndex<tables.length-1;tableIndex++){
			header2dataMap.clear();
			String tmp = ("<"+tables[tableIndex]).replaceAll("(<[^>]+>)", ",").replaceAll("([,]+)", ",").replaceAll("&nbsp;", "");
			String[] fields = tmp.split(",");
			int sumIndex = 0;
			for(int i=1;i<fields.length;i++){
				if(!fields[i].equals("") && fetchUtil.isNumeric(fields[i]) && !fetchUtil.isNumeric(fields[i-1])){
					header2dataMap.put(fields[i-1], fields[i]);
				}
				if(fields[i].equals("合计")){
					sumIndex = i;
					break;
				}
			}
			if(sumIndex>0){
				if(fields.length-sumIndex==4){
					header2dataMap.put("本期装船量", fields[sumIndex+1]);
					header2dataMap.put("预报本月装船量", fields[sumIndex+2]);
					header2dataMap.put("预报下月装船量", fields[sumIndex+3]);
				}else if(fields.length-sumIndex>=5){
					header2dataMap.put("本期装船量", fields[sumIndex+1]);
					header2dataMap.put("预报本月装船量", fields[sumIndex+2]);
					header2dataMap.put("预报下月装船量", fields[sumIndex+3]);
					header2dataMap.put("本期到港量", fields[sumIndex+1]);
					header2dataMap.put("预报下期到港量", fields[sumIndex+2]);
					header2dataMap.put("预报本月到港量", fields[sumIndex+3]);
					header2dataMap.put("预报下月到港量", fields[sumIndex+4]);
				}
			}
			varName = varNameIndexMap.get((int)Math.floor((tableIndex+1)/2));
			String[] cnNames = null;
			if(tableIndex%2 == 1){
				logger.info("处理本期进口"+varName+"装船情况表");
				cnNames = cnNameMap.get("odd").split(",");
			}else{
				logger.info("处理本期分地区"+varName+"进口到港情况表");
				cnNames = cnNameMap.get("even").split(",");
			}
			String varNameCopy = varName;
			if(varNameMap.get(varName) != null){
				varName = varNameMap.get(varName);
			}
			int varId = Variety.getVaridByName(varName);
			for(String cnName:cnNames){
				if(varNameMap.keySet().contains(varNameCopy) && !varNameCopy.equals("玉米酒糟")){
					cnName = varNameCopy.replaceAll("及其", "及")+cnName;
				}
				dataMap.clear();
				List<String> headers = dao.getHeaders(varName, cnName);
				dataMap = getDataByHeader(headers, header2dataMap);
				if(dataMap.size()>0){
					dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
				}else{
					logger.info(varName+cnName+"没有数据需要保存");
				}
			}
		}
		logger.info("处理本期分地区进口到港情况表");
		dataMap.clear();
		header2dataMap.clear();
		String tmp = ("<"+tables[tables.length-1]).replaceAll("(<[^>]+>)", ",").replaceAll("([,]+)", ",").replaceAll("&nbsp;", "");
		if(tmp.indexOf("单位：吨")!=-1){
			tmp = tmp.substring(tmp.indexOf("单位：吨")+4);
		}
		String[] lines = fetchUtil.getLineContent(tmp.substring(1), varNameIndexMap.size()+1).split("\n");
		String[] fields = lines[0].split(",");
		for(int i=1;i<fields.length;i++){
			String cnName = "本期进口到港（口岸）";
			varName = fields[i];
			String varNameCopy = varName;
			if(varNameMap.get(varName) != null){
				varName = varNameMap.get(varName);
			}
			int varId = Variety.getVaridByName(varName);
			for(int j=1;j<lines.length;j++){
				String[] fieldTmps = lines[j].split(",");
				header2dataMap.put(fieldTmps[0], fieldTmps[i]);
			}
			if(varNameMap.keySet().contains(varNameCopy) && !varNameCopy.equals("玉米酒糟")){
				cnName = varNameCopy.replaceAll("及其", "及")+cnName;
			}
			List<String> headers = dao.getHeaders(varName, cnName);
			dataMap = getDataByHeader(headers, header2dataMap);
			if(dataMap.size()>0){
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}else{
				logger.info(varName+cnName+"没有数据需要保存");
			}
		}
		}
		else{
			logger.info("**************");
		}
	}
	private Map<String, String> getDataByHeader(List<String> headers, Map<String, String> header2dataMap){
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String header:headers){
			dataMap.put(header, "0");
		}
		for(String header:headers){
			if(header2dataMap.get(header)!=null){
				dataMap.put(header, header2dataMap.get(header));
				continue;
			}
			if(header.contains("确认")){//处理“未确认”“待确认”“待确认及其他”不一致的问题
				for(String headerTmp:header2dataMap.keySet()){
					if(headerTmp.contains("确认")){
						dataMap.put(header, header2dataMap.get(headerTmp));
						break;
					}
				}
			}
		}
		return dataMap;
	}
	public static void main(String[] args) {
		new AgriImportsData().startFetch();
	}

}
