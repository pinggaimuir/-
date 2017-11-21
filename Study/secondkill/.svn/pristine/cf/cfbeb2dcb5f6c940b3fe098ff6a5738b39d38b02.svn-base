package cn.futures.data.importor.crawler.usdaCrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.MyHttpClient;

/**
 * 美农作物生长数据
 * @author ctm
 *
 */
public class MannUsdaData {
	private static final String className = MannUsdaData.class.getName();
	private Log logger = LogFactory.getLog(MannUsdaData.class);
	private MyHttpClient httpClient = new MyHttpClient();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private String url = "http://usda.mannlib.cornell.edu/MannUsda/viewDocumentInfo.do?documentID=1048";
	
	@Scheduled
	(cron=CrawlScheduler.CRON_MANN_USDA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("美农作物生长数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到美农作物生长数据的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				//date = DateTimeUtil.parseDateTime("20151011", "yyyyMMdd");
				fetchMann(date);
			}else{
				logger.info("抓取美农作物生长数据的定时器已关闭");
			}
		}
	}
	
	private void fetchMann(Date date){
		logger.info("=====开始抓取美农作物生长数据=====");
		String response  = httpClient.getResponseBody(url);
		if(response != null && !response.equals("")){
			String comp = "<a href=\"http://usda.mannlib.cornell.edu/usda/current/CropProg/([^>]+).txt\">";
			List<String> results = fetchUtil.getMatchStr(response, comp, null);
			if(results.size() > 0){
				String timeStr = results.get(0);
				fetchOne(timeStr, false);
			}
		}
	}
	
	private void fetchOne(String timeStr, boolean isHistory){
		String tarUrl = "http://usda.mannlib.cornell.edu/usda/current/CropProg/"+timeStr+".txt";
		if(isHistory){
			tarUrl = "http://usda.mannlib.cornell.edu/usda/nass/CropProg//2010s/2017/"+timeStr+".txt";
		}
		String contents = httpClient.getResponseBody(tarUrl);
		if(contents != null && !contents.equals("")){
			String timeInt = DateTimeUtil.formatDate(DateTimeUtil.getLastWeekEndDay(DateTimeUtil.parseDateTime(timeStr.substring(timeStr.indexOf("-")+1), "MM-dd-yyyy")),"yyyyMMdd");
			String[] lines = contents.split("\n");
			for(int i=0;i<lines.length;i++){
				String line = lines[i].trim();
				String cnNameStr = null;
				boolean isCondition = false;//是否为优良率
				String lineT = line.replaceAll("([\\s]+)", ""); 
				if(lineT.startsWith("State:Verypoor:")){
					cnNameStr= lines[i-4].trim();
					isCondition = true;
				}else if(lineT.startsWith("State:")){
					cnNameStr = lines[i-5].trim();
				}
				String varName = null;
				String cnName = null;
				Map<String, String> headers = new HashMap<String, String>();
				if(cnNameStr != null){
					for(String tmp:MapInit.mannUsda_code2varNameAcnName_map.keySet()){
						if(cnNameStr.replaceAll("(\\s+)", "").startsWith(tmp+"-")){
							String[] varNameAcnName = MapInit.mannUsda_code2varNameAcnName_map.get(tmp).split("-");
							varName = varNameAcnName[0];
							cnName = varNameAcnName[1];
							headers = MapInit.mannUsda_varName2header_map.get(varName);
							break;
						}
					}
				}
				//有用的品种
				Map<String, String> dataTmpMap = new HashMap<String, String>();
				Map<String, String> dataMap = new HashMap<String, String>();
				if(varName != null){
					int startIndex;
					if(isCondition) startIndex = i+4;
					else startIndex = i+5;
					for(int j=startIndex;j<lines.length;j++){
						String lineTmp = lines[j].trim();
						if(lineTmp.replaceAll("([-]+)", "").equals("")) break;
						String[] fields = lineTmp.split(":");
						if(fields.length == 0) continue;
						String[] values = fields[1].replaceAll("(\\s+)", ",").replaceAll("-", "0").split(",");
						String key = fields[0].replaceAll("([.]+)", "").trim();
						if(key.matches("([0-9]+\\s)States")){
							key = "All States";
						}
						if(isCondition){
							if(values[4].equals("(NA)")) continue;
							Double val = Double.parseDouble(values[4]) + Double.parseDouble(values[5]);
							dataTmpMap.put(key, val+"");
						}else{
							if(values[3].equals("(NA)")) continue;
							dataTmpMap.put(key, values[3]);
						}
					}
					for(String header:headers.keySet()){
						if(dataTmpMap.get(headers.get(header))!=null){
							dataMap.put(header, dataTmpMap.get(headers.get(header)));
						}
					}
					int varId = Variety.getVaridByName(varName);
					dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
				}
			}
		}
	}
	
	private void fetchHisMann(Date date) {
		logger.info("=====开始抓取美农作物生长（历史）数据=====");
		String response  = httpClient.getResponseBody(url);
		if(response != null && !response.equals("")){
			System.out.println(response);
			//抓历史数据
			String[] divs = response.split("<div id=\"CropProg-");
			for(String div:divs){
				String comp = "^([^\"]{10}).txt\"(.+)href=\"([^\"]+)\"";
				int[] index={1,3};
				List<String> results = fetchUtil.getMatchStr(div, comp, index);
				if(results.size() > 0){
					String timeStr = results.get(0);
					if(DateTimeUtil.parseDateTime(timeStr, "MM-dd-yyyy").after(DateTimeUtil.parseDateTime("4-20-1999", "MM-dd-yyyy"))){
						continue;
					}
					String contents = httpClient.getResponseBody(results.get(1));
					if(contents != null && !contents.equals("")){
						String timeInt = DateTimeUtil.formatDate(DateTimeUtil.getLastWeekEndDay(DateTimeUtil.parseDateTime(timeStr, "MM-dd-yyyy")),"yyyyMMdd");
						//if(Integer.parseInt(timeInt)>20100628) continue;
						if(Integer.parseInt(timeInt)>20100628){
							String[] lines = contents.split("\n");
							for(int i=0;i<lines.length;i++){
								String line = lines[i].trim();
								String cnNameStr = null;
								boolean isCondition = false;//是否为优良率
								String lineT = line.replaceAll("([\\s]+)", "");
								if(lineT.startsWith("State:Verypoor:") || lineT.startsWith("State:VP:")){
									cnNameStr= lines[i-4].trim();
									isCondition = true;
								}else if(lineT.startsWith("State:")){
									cnNameStr = lines[i-5].trim();
								}
								String varName = null;
								String cnName = null;
								Map<String, String> headers = new HashMap<String, String>();
								if(cnNameStr != null){
									for(String tmp:MapInit.mannUsda_code2varNameAcnName_map.keySet()){
										if(cnNameStr.replaceAll("(\\s+)", "").startsWith(tmp.replaceAll("(\\s+)", "")+"-")){
											String[] varNameAcnName = MapInit.mannUsda_code2varNameAcnName_map.get(tmp).split("-");
											varName = varNameAcnName[0];
											cnName = varNameAcnName[1];
											headers = MapInit.mannUsda_varName2header_map.get(varName);
											break;
										}
									}
								}
								//有用的品种
								Map<String, String> dataTmpMap = new HashMap<String, String>();
								Map<String, String> dataMap = new HashMap<String, String>();
								if(varName != null){
									int startIndex;
									if(isCondition) startIndex = i+4;
									else startIndex = i+5;
									for(int j=startIndex;j<lines.length;j++){
										String lineTmp = lines[j].trim();
										if(lineTmp.replaceAll("([-]+)", "").equals("")) break;
										String[] fields = lineTmp.split(":");
										if(fields.length == 0) continue;
										String[] values = fields[1].replaceAll("(\\s+)", ",").replaceAll("-", "0").split(",");
										String key = fields[0].replaceAll("([.]+)", "").trim();
										if(key.matches("([0-9]+\\s)States")){
											key = "All States";
										}
										if(isCondition){
											if(values[4].equals("(NA)")) continue;
											Double val = Double.parseDouble(values[4]) + Double.parseDouble(values[5]);
											dataTmpMap.put(key, val+"");
										}else{
											if(values[3].equals("(NA)")) continue;
											dataTmpMap.put(key, values[3]);
										}
									}
									for(String header:headers.keySet()){
										if(dataTmpMap.get(headers.get(header))!=null){
											dataMap.put(header, dataTmpMap.get(headers.get(header)));
										}
									}
									int varId = Variety.getVaridByName(varName);
									dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
								}
							}
						}else{
							String[] lines = contents.split("\n");
							for(int i=0;i<lines.length;i++){
								String line = lines[i].trim();
								String cnNameStr = null;
								boolean isCondition = false;//是否为优良率
								String lineT = line.replaceAll("(\\s+)", "");
								if(lineT.startsWith("State:Verypoor:") || lineT.startsWith("State:VP:")){
									cnNameStr= lines[i-4].trim();
									isCondition = true;
								}else if(lineT.startsWith("State:")){
									cnNameStr = lines[i-5].trim();
								}
								String varName = null;
								String cnName = null;
								Map<String, String> headers = new HashMap<String, String>();
								if(cnNameStr != null){
									for(String tmp:MapInit.mannUsda_code2varNameAcnName_map_old.keySet()){
										if(cnNameStr.replaceAll("(\\s+)", "").startsWith(tmp.replaceAll("(\\s+)", ""))){
											String[] varNameAcnName = MapInit.mannUsda_code2varNameAcnName_map_old.get(tmp).split("-");
											varName = varNameAcnName[0];
											cnName = varNameAcnName[1];
											headers = MapInit.mannUsda_varName2header_map_old.get(varName);
											break;
										}
									}
								}
								//有用的品种
								Map<String, String> dataTmpMap = new HashMap<String, String>();
								Map<String, String> dataMap = new HashMap<String, String>();
								if(varName != null){
									int startIndex;
									if(isCondition) startIndex = i+4;
									else startIndex = i+5;
									for(int j=startIndex;j<lines.length;j++){
										String lineTmp = lines[j].trim();
										if(lineTmp.replaceAll("([-]+)", "").equals("")) break;
										String[] fields = lineTmp.split(":");
										if(fields.length == 0) continue;
										String[] values = fields[1].replaceAll("(\\s+)", ",").replaceAll("-", "0").split(",");
										String key = fields[0].replaceAll("([.]+)", "").trim();
										if(key.matches("([0-9]+\\s)Sts")){
											key = "All States";
										}
										if(isCondition){
											if(values[4].equals("(NA)") || values[4].equals("NA")) continue;
											Double val = Double.parseDouble(values[4]) + Double.parseDouble(values[5]);
											dataTmpMap.put(key, val+"");
										}else{
											if(values[1].equals("(NA)") || values[1].equals("NA")) continue;
											dataTmpMap.put(key, values[1]);
										}
									}
									for(String header:headers.keySet()){
										if(dataTmpMap.get(headers.get(header))!=null){
											dataMap.put(header, dataTmpMap.get(headers.get(header)));
										}
									}
									int varId = Variety.getVaridByName(varName);
									dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
								}
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args){
//		new MannUsdaData().start();
		new MannUsdaData().fetchOne("CropProg-05-30-2017", true);
	}
}
