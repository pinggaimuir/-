package cn.futures.data.importor.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.ProvAndCityFormatData;

import java.util.Date;

/**
 * 新农村商网价格行情数据
 * @author ctm
 *
 */
public class MOFCOMDataFetch {
	private static final String className = MOFCOMDataFetch.class.getName();
	private Logger logger = Logger.getLogger(MOFCOMDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil(true);
	private DAOUtils dao = new DAOUtils();
	private static String FetchURL = "http://nc.mofcom.gov.cn/channel/gxdj/jghq/jg_list.shtml?par_craft_index=%marketId%&craft_index=%varId%&startTime=%startTime%&endTime=%endTime%&page=";
	private String beforeContent = "";

	private String getMarketPriceAsStr(Date date, int marketId, String varId, String varName){
		String dateStr = DateTimeUtil.formatDate(date, "yyyy-MM-dd");
		String strBuffer = "";
		String[] filters = {"table", "class", "s_table03"};
		String[] rowColChoose = {"0","111100"};
		String url = FetchURL.replaceAll("%marketId%", marketId+"").replaceAll("%varId%", varId+"")
			.replaceAll("%startTime%", dateStr).replaceAll("%endTime%", dateStr);
		for(int i=1;i<100;i++){
			String contents = dataFetchUtil.getPrimaryContent(0, url+i, "GBK", varName, filters, rowColChoose, 0);
			if(contents == null || beforeContent.equals(contents)){
				break;
			}else{
				strBuffer += contents;
				beforeContent = contents;
			}
		}
		return strBuffer;
	}

	public void fetchData(Date date){
		logger.info("**********fetch start, date "+DateTimeUtil.formatDate(date, "yyyyMMdd")+"*********");
		for(String bigClass:MapInit.mofcomMarketPriceMap.keySet()){
			int marketId = MapInit.mofcomMarketPriceMap.get(bigClass);
			logger.info("start fetch "+ bigClass);
			Map<String, String> varIdMap = MapInit.mofcomKindReflectMap.get(bigClass);
			for(String varName:varIdMap.keySet()){
				String varId = varIdMap.get(varName);
				String content = getMarketPriceAsStr(date, marketId, varId, varName);
				String dirString = Constants.MOFCOM_ROOT + Constants.FILE_SEPARATOR + bigClass + Constants.FILE_SEPARATOR + varName + Constants.FILE_SEPARATOR;
				//String dirString = Constants.MOFCOM_ROOT + Constants.FILE_SEPARATOR + bigClass + Constants.FILE_SEPARATOR;
				try {
					String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
					if (new File(dirString + timeInt + ".txt").exists())
						logger.warn("Overwrite: "+ varName);
					FileStrIO.saveStringToFile(content, dirString,	timeInt + ".txt");
					//FileStrIO.appendStringToFile(content, dirString, varName + ".txt");
					logger.info("data saved: " + varName);
				} catch (IOException e) {
					logger.error("IOException while saving "+varName+" data:", e);
				}
				logger.info("====== "+ varName + " fetched ======");
			}
		}
	}
	
	public void saveMOFDataToDb(File file, String date){
		for(File file2:file.listFiles()){			
			if (file2.isDirectory()) {
				saveMOFDataToDb(file2,date);
			}else {
				String cnName = file2.getParentFile().getName();
				String varName = file2.getParentFile().getParentFile().getName();
				int varId = Variety.getVaridByName(varName);
				int TimeInt = Integer.valueOf(file2.getName().split("\\.")[0]);
				if (TimeInt==Integer.valueOf(date)) {
					Map<String, String> dataMap = new HashMap<String, String>();
					Map<String, Integer> provNumArea = new HashMap<String, Integer>();
					InputStreamReader inputStreamReader;
					try {
						inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.ENCODE_GB2312);
						BufferedReader reader = new BufferedReader(inputStreamReader);
						String str = "";
						while((str=reader.readLine())!=null){
							String[] array = str.split(",");
							String area = ProvAndCityFormatData.getAreaByMarket(array[2]);
							String prov = ProvAndCityFormatData.getProvinceByArea(area);
							if(prov.equals("")){
								continue;
							}
							if(dataMap.get(prov) != null){
								dataMap.put(prov, Double.parseDouble(dataMap.get(prov))+Double.parseDouble(array[1])+"");
								provNumArea.put(prov, provNumArea.get(prov)+1);
							}else{
								dataMap.put(prov, array[1]);
								provNumArea.put(prov, 1);
							}
						}
						double sumVal = 0;
						for(String prov:dataMap.keySet()){
							double value = Double.parseDouble(dataMap.get(prov))/provNumArea.get(prov);
							sumVal += value;
							dataMap.put(prov, value +"");
						}
						if(sumVal > 0){
							dataMap.put("全国", sumVal/dataMap.keySet().size()+"");
						}
						reader.close();
					} catch (UnsupportedEncodingException e) {
						logger.error("UnsupportedEncodingException", e);
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException", e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("IOException", e);
					}
					if(dataMap.size()>0){
						logger.info("开始保存"+varName+cnName);
						Integer timeInt = Integer.parseInt(date);
						dao.saveOrUpdateByDataMap(varId, cnName, timeInt, dataMap);
					}else{
						logger.info(varName+cnName+TimeInt+"没有需要保存的数据");
					}
				}
			}
		}
	}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_MOFCOM_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("新农村商网价格行情数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到新农村商网价格行情数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -2);
				fetchData(date);
				saveMOFDataToDb(new File(Constants.MOFCOM_ROOT), DateTimeUtil.formatDate(date, "yyyyMMdd"));
			}else{
				logger.info("抓取新农村商网价格行情数据的定时器已关闭");
			}
		}
	}
	
	public static void main(String[] a){
		MOFCOMDataFetch mof = new MOFCOMDataFetch();

		/*String[] vars = "大白菜、洋白菜、土豆、西红柿、菜花、芹菜、青椒、生菜、韭菜、油菜、菠菜、黄瓜、茄子、白萝卜、豇豆".split("、");
		String[] cities = "苏州、无锡、常州、上海".split("、");
		String[] markets_sz = "苏州市南环桥农副产品批发市场".split("、");
		String[] markets_wx = "江苏无锡天鹏集团公司、江苏无锡朝阳市场、江苏宜兴蔬菜副食品批发市场".split("、");
		String[] markets_cz = "江苏凌家塘农副产品批发市场、江苏常州宣塘桥水产品交易市场".split("、");
		String[] markets_sh = "上海农产品中心批发市场有限公司、上海市江桥批发市场".split("、");
		String time_start="20150101";
		String time_end = "20160113";
		*/
				
		
		//mof.start();
//		Date date = DateTimeUtil.parseDateTime("20160315", "yyyyMMdd");
//		while(date.before(new Date())){
//			mof.fetchData(date);
//			mof.saveMOFDataToDb(new File(Constants.MOFCOM_ROOT), DateTimeUtil.formatDate(date, "yyyyMMdd"));
//			date = DateTimeUtil.addDay(date, 1);
//		}
		String[] times = {"20161013"};
		for(String time: times){
			Date date = DateTimeUtil.parseDateTime(time, "yyyyMMdd");
			mof.fetchData(date);
			mof.saveMOFDataToDb(new File(Constants.MOFCOM_ROOT), DateTimeUtil.formatDate(date, "yyyyMMdd"));
		}
	}
}

