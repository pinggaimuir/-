package cn.futures.data.importor.crawler.futuresMarket;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.ZipFileManager;

public class CFTCFuturesData {
	private static final String className = CFTCFuturesData.class.getName();
	private Log logger = LogFactory.getLog(CFTCFuturesData.class);
	private DAOUtils dao = new DAOUtils();
	private Map<String, String> cnName2urlMap = new HashMap<String, String>(){
		{
			put("CFTC期货持仓", "http://www.cftc.gov/files/dea/history/deacot%year%.zip");//annual.txt
			put("CFTC期货分类持仓", "http://www.cftc.gov/files/dea/history/fut_disagg_txt_%year%.zip");//f_year.txt
			put("CFTC期货期权持仓", "http://www.cftc.gov/files/dea/history/deahistfo%year%.zip");//annualof.txt
			put("CFTC期货期权分类持仓", "http://www.cftc.gov/files/dea/history/com_disagg_txt_%year%.zip");
			put("CFTC指数交易持仓", "http://www.cftc.gov/files/dea/history/dea_cit_txt_%year%.zip");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_CTFC_FUTURES)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("CFTC期货数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到CFTC期货数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchFutures(new Date());
			}else{
				logger.info("抓取CFTC期货数据的定时器已关闭");
			}
		}
	}
	
	private void fetchFutures(Date date) {
		String year = DateTimeUtil.getCurrentYear()+"";
		logger.info("=====开始抓取CFTC期货数据=====");
		for(String cnName:cnName2urlMap.keySet()){
			String url = cnName2urlMap.get(cnName).replaceAll("%year%", year);
			String zipName = url.substring(url.lastIndexOf("/")+1);
			logger.info("下载"+cnName+zipName+"@"+url);
			String path = "d:/tmp/" + zipName;
			FileStrIO.downloadFile(path, url);
			logger.info("解压"+zipName);
			String fileName = "";
			try {
				fileName = ZipFileManager.unZipFiles(new File(path), "D:/tmp/").split(",")[0];
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("读取并分析"+fileName);
			String contents = FileStrIO.getTxtContent(fileName);
			if(!contents.equals("")){
				parserAndSave(contents, cnName);
			}
		}
	}
	
	private void parserAndSave(String contents, String cnName){
		int timeIndex = 2;//时间列
		String[] lines = contents.split("\n");
		String[] titles = lines[0].split(",");
		Map<String, String> headers = MapInit.cftc_cnName2header_map.get(cnName);
		Map<String, Integer> header2index = new HashMap<String, Integer>();
		for(int i=0;i<titles.length;i++){
			String title = StringUtils.substringBetween(titles[i], "\"", "\"").trim();
			if(title.indexOf("YYYY-MM-DD") != -1 || title.indexOf("MM_DD_YYYY") != -1){
				timeIndex = i;
				continue;
			}
			for(String header:headers.keySet()){
				if(title.equals(headers.get(header))){
					header2index.put(header, i);
				}
			}
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		String beforeVarName="";
		for(int i=1;i<lines.length;i++){
			String[] fields = lines[i].split(",");
			if(fields[1].indexOf("\"") == -1){
				if(fields[0].equals(beforeVarName))
					continue;
				else{
					String varName = MapInit.cftc_code2varName_map.get(fields[0]);
					beforeVarName = fields[0];
					if(varName != null){
						int varId = Variety.getVaridByName(varName);
						dataMap.clear();
						String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(fields[timeIndex].trim(), "yyyy-MM-dd"), "yyyyMMdd");
						for(String header:header2index.keySet()){
							dataMap.put(header, fields[header2index.get(header)].trim());
						}
						for(String header:headers.keySet()){
							String val = headers.get(header);
							if(val.startsWith("calc")){
								String[] tmps = val.split(",")[1].split("-");
								dataMap.put(header, Double.parseDouble(dataMap.get(tmps[0]))-Double.parseDouble(dataMap.get(tmps[1]))+"");
							}
						}
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
					}
				}
			}else{
				if((fields[0]+fields[1]).equals(beforeVarName))
					continue;
				else{
					if(beforeVarName.equals(fields[0]+fields[1])) continue;
					if((fields[0]+fields[1]).indexOf("CRUDE OIL LIGHT SWEET - NEW YORK MERCANTILE EXCHANGE")!=-1){
						int varId = Variety.getVaridByName("纽约原油");
						dataMap.clear();
						String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(fields[timeIndex+1].trim(), "yyyy-MM-dd"), "yyyyMMdd");
						for(String header:header2index.keySet()){
							dataMap.put(header, fields[header2index.get(header)+1].trim());
						}
						for(String header:headers.keySet()){
							String val = headers.get(header);
							if(val.startsWith("calc")){
								String[] tmps = val.split(",")[1].split("-");
								dataMap.put(header, Double.parseDouble(dataMap.get(tmps[0]))-Double.parseDouble(dataMap.get(tmps[1]))+"");
							}
						}
						dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
					}
					beforeVarName = fields[0]+fields[1];
				}
			}
		}
	}
	public static void main(String[] args){
		new CFTCFuturesData().start();
	}
}
