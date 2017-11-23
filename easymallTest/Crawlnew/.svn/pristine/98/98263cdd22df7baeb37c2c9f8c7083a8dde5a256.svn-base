package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
 * 大宗农产品进口数据
 * @author ctm
 *
 */
public class AgriImportsData {
	private static final String className = AgriImportsData.class.getName();
	private Log logger = LogFactory.getLog(AgriImportsData.class);
	private Log formatErrLog = LogFactory.getLog("format_err");
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String url = "http://wms.mofcom.gov.cn/article/wb/";
	private Map<String, String> cnNameMap = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;

		{
			put("odd", "进口装船,本期进口装船（装运港）");
			put("even", "进口到港,本期进口到港（分海关）");
			put("last", "本期进口到港（口岸）");//有的进口信息中有，有的进口信息中没有
		}
	};
	private Map<String, String> varNameMap = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;

		{
			put("鲜奶","乳品");
			put("奶粉","乳品");
			put("牛肉及其副产品","牛肉");
			put("羊肉及其副产品","羊肉");
			put("猪肉及其副产品","猪肉");
			put("玉米酒糟","DDGS");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_MOF_IMPORT_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("大宗农产品进口数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到大宗农产品进口数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				startFetch();
			}else{
				logger.info("抓取大宗农产品进口数据的定时器已关闭");
			}
		}
	}
	
	/**
	 * 今天抓昨天更新的数据(数据半月更新一次)
	 */
	public void startFetch(){
		logger.info("=====开始抓取大宗农产品进口数据=====");
		String[] hrefFilters = {"div","class","alist"};
		Set<String> varieties = new HashSet<String>();//记录本次任务处理过的类别。保证每次任务中每个类别的信息只更新一个最新的。
		String hrefContents = fetchUtil.getPrimaryContent(0, url+"?", "utf-8", "大宗农产品进口信息", hrefFilters, null, 0);
		if(hrefContents != null && !hrefContents.equals("")){
			String[] lies = StringUtils.substringsBetween(hrefContents, "<li>", "</li>");
			String comp = "href=\"/article/wb/([^\"]+)\">大宗农产品进口信息发布(.+)发布日期：([^<]+)<(.+)";
			int[] index={1,2,3};
			String[] filters = {"div", "id", "zoom"};
			for(String li:lies){
				List<String> results = fetchUtil.getMatchStr(li, comp, index);
				if(results.size() > 0 && !varieties.contains(results.get(1))){
					varieties.add(results.get(1));
					String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(results.get(2), "yyyy年MM月dd日"),"yyyyMMdd");
					String contents = fetchUtil.getPrimaryContent(0, url + results.get(0), "utf-8", "大宗农产品进口信息", filters, null, 0);
					if(contents != null && !contents.equals("")){
						parseAndSave(contents, timeInt);
					}else{
						logger.error("没有抓取到"+timeInt + "大宗农产品进口信息数据");
					}
				}
			}
		}else{
			logger.error("没有抓取到大宗农产品进口信息的链接数据");
		}
	}
	
	/**
	 * 用于补指定日期发布的数据（显示的发布日期，虽然更新到网站上的日期并不一定是）
	 */
	public void repairFetch(Date date){
		logger.info("=====开始抓取大宗农产品进口数据=====");
		String asignDay = DateTimeUtil.formatDate(date, "yyyy-MM-dd");
		String[] hrefFilters = {"div","class","alist"};
		boolean update = false;
		String hrefContents = fetchUtil.getPrimaryContent(0, url+"?", "utf-8", "大宗农产品进口信息", hrefFilters, null, 0);
		if(hrefContents != null && !hrefContents.equals("")){
			String[] lies = StringUtils.substringsBetween(hrefContents, "<li>", "</li>");
			String comp = "href=\"/article/wb/([^\"]+)\">大宗农产品进口信息发布(.+)发布日期：([^<]+)<(.+)"+asignDay;
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
	}
	
	private void parseAndSave(String contents, String timeInt){
		Map<String, String> header2dataMap = new HashMap<String, String>();
		Map<String, String> dataMap = new HashMap<String,String>();
		Map<Integer, String> varNameIndexMap = new HashMap<Integer, String>();//某品种在某次进口信息中的序号，标识其数据表格的位序。
		String varName = "";
		String[] tables = StringUtils.substringsBetween(contents, "<TABLE", "</TABLE>");
		String[] tmps = ("<"+tables[0]).replaceAll("(<[^>]+>)", ",").replaceAll("([,]+)", ",").split(",");
		
		//从第一个表格中提取品种对应位序。
		for(int i=tmps.length-1;i>=1;i=i-2){
			if(!fetchUtil.isNumeric(tmps[i-1].substring(0,1)) && !tmps[i-1].substring(0,1).equals("&")) break;
			if(tmps[i-1].substring(0,1).equals("&")) continue;
			varNameIndexMap.put(Integer.parseInt(tmps[i-1].substring(0,1)), tmps[i].replaceAll("菜子油", "菜籽油"));
		}
		if(varNameIndexMap.size()*2+1 == tables.length) {//不存在分地区进口到港情况表。
			//依次处理每个品种对应的装船情况表和进口到港情况表
			for(int tableIndex=1;tableIndex<tables.length;tableIndex++){
				extractTable(header2dataMap, varNameIndexMap, dataMap, timeInt, tables[tableIndex], tableIndex );
			}
		}else if(varNameIndexMap.size()*2+2 == tables.length){//存在分地区进口到港情况表。
			//依次处理每个品种对应的装船情况表和进口到港情况表
			for(int tableIndex=1;tableIndex<tables.length-1;tableIndex++){
				extractTable(header2dataMap, varNameIndexMap, dataMap, timeInt, tables[tableIndex], tableIndex );
			}
			
			//下面处理分地区进口到港情况表（只有一个）
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
		}else{
			formatErrLog.info("大宗农产品进口数据格式变化[AgriImportsData]");
			logger.info("网页格式有调整！！！");
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
			if(header.equals("香港") && header2dataMap.get("中国香港") != null){
				dataMap.put(header, header2dataMap.get("中国香港"));
			}
			if(header.equals("台湾") && header2dataMap.get("中国台湾") != null){
				dataMap.put(header, header2dataMap.get("中国台湾"));
			}
			if(header.indexOf("确认")!=-1 || header.indexOf("确定")!=-1){//处理“未确认”“待确认”“待确认及其他”不一致的问题
				for(String headerTmp:header2dataMap.keySet()){
					if(headerTmp.indexOf("确认")!=-1 || headerTmp.indexOf("确定")!=-1){
						dataMap.put(header, header2dataMap.get(headerTmp));
						break;
					}
				}
			}
		}
		return dataMap;
	}
	
	/**
	 * 提取品种对应的装船情况表和进口到港情况表。
	 * @param header2dataMap 表格字段名-值
	 * @param varNameIndexMap 品种-位序
	 * @param dataMap 提取结果
	 * @param timeInt 数据的时间序列
	 * @param tableStr 待提取表格的字符串
	 * @param tableIndex 待提取表的位序
	 * */
	private void extractTable(Map<String, String> header2dataMap, Map<Integer, String> varNameIndexMap, Map<String, String> dataMap, String timeInt, String tableStr, int tableIndex ){
		
		header2dataMap.clear();
		String tmp = ("<"+tableStr).replaceAll("(<[^>]+>)", ",").replaceAll("([,]+)", ",").replaceAll("&nbsp;", "");
		String[] fields = tmp.split(",");
		int sumIndex = 0;
		for(int i=1;i<fields.length;i++){	//只提取本期情况
			if(!fields[i].equals("") && fetchUtil.isNumeric(fields[i]) && !fetchUtil.isNumeric(fields[i-1])){
				String value = fields[i];
				if(value.indexOf(".") == -1){//为什么要判断有无小数点儿？可能是格式问题，防止将一个数值分成几部分的情况
					value += fields[i+1];
				}
				if(value.indexOf(".") == -1){
					value += fields[i+2];
				}
				header2dataMap.put(fields[i-1], value);
			}
			if(fields[i].equals("合计")){
				String value = fields[i+1];
				if(value.indexOf(".") == -1){
					value += fields[i+2];
				}
				if(value.indexOf(".") == -1){
					value += fields[i+3];
				}
				header2dataMap.put("合计", value);
				sumIndex = i;
				break;
			}
		}
		if(sumIndex>0){//说明正常提取到合计装船数据
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
		String varName = varNameIndexMap.get((int)Math.floor((tableIndex+1)/2));//品种名，根据两个表为一个品种的规则而得。
		String[] cnNames = null;
		if(tableIndex%2 == 1){//每组表格中第一个为装船情况，第二个为到港情况。
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
			List<String> headers = dao.getHeaders(varName, cnName);//有找不到的情况
			dataMap = getDataByHeader(headers, header2dataMap);
			boolean havaData = false;
			//判断有无有效数据
			for(String data:dataMap.keySet()){
				if(!dataMap.get(data).equals("0.00") && !dataMap.get(data).equals("0"))
					havaData = true;
			}
			if(havaData){
				dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
			}else{
				logger.info(varName+cnName+"没有数据需要保存");
				dao.deleteByTimeInt(varId, cnName, Integer.parseInt(timeInt));
			}
		}
	}
	
	public static void main(String[] args) {
		AgriImportsData aid = new AgriImportsData();
//		aid.start();
		
		//补指定日期的历史数据
		Date date = DateTimeUtil.parseDateTime("2016-07-12", "yyyy-MM-dd");
		aid.repairFetch(date);
	}

}
