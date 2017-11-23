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
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;

/**
 * 芝华数据蛋鸡数据
 * @author ctm
 *
 */
public class ChinaDataFetch {
	private static final String className = ChinaDataFetch.class.getName();
	private Log logger = LogFactory.getLog(ChinaDataFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private static DAOUtils dao = new DAOUtils();
	private String url = "http://58.49.94.60/chinaData/template/zhsjdjmh/list.jsp?categoryId=%category%&pageNum=";
	private String urlBase = "http://poultry.china-data.com.cn/chinaData/template/zhsjdjmh/content.jsp?contentId=";
	private Map<String, String> varNameMap = new HashMap<String, String>(){
		{
			put("蛋禽","1201308281158540004");
			put("鸡蛋","1201308281156470002");
		}
	};
	private static Map<String, List<String>> jdCnName2headerMap = new HashMap<String, List<String>>();
	private static List<String> ttjAreaList = new ArrayList<String>();
	private static List<String> ttjMainAreaList = new ArrayList<String>();
	private static List<String> allAreaList = new ArrayList<String>();
	private static List<String> mainProducingAreaList = new ArrayList<String>();
	private static List<String> mainSalesAreaList = new ArrayList<String>();
	private static List<String> diffOfPriceList = new ArrayList<String>();
	private static Map<String, String> abbr2prov = new HashMap<String, String>();
	static{
		allAreaList.addAll(dao.getHeaderById(0));
		ttjAreaList.addAll(allAreaList);
		ttjMainAreaList.addAll(Arrays.asList("河北","辽宁","江苏","山东","河南"));//淘汰鸡的全国价格是这五个主要产区价格的平均
		mainProducingAreaList.addAll(Arrays.asList("主产省均价","安徽","河北","河南","黑龙江","湖北","湖南","吉林","江苏","辽宁","山东","山西"));
		mainSalesAreaList.addAll(Arrays.asList("主销区均价","广东地区","沪浙闽地区","京津地区"));
		diffOfPriceList.addAll(Arrays.asList("北京-朝阳","广州-黄冈","上海-临沂"));
		
		jdCnName2headerMap.put("各省鸡蛋价格", allAreaList);
		jdCnName2headerMap.put("主产区鸡蛋价格", mainProducingAreaList);
		jdCnName2headerMap.put("三大主销区鸡蛋价格", mainSalesAreaList);
		jdCnName2headerMap.put("鸡蛋贸易价差", diffOfPriceList);
		
		abbr2prov.put("广东地区", "广东");
		abbr2prov.put("沪浙闽地区", "上海,浙江,福建");
		abbr2prov.put("京津地区", "北京,天津");
	}
	@Scheduled
	(cron=CrawlScheduler.CRON_CHINA_DATA)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("全国各省鸡蛋与淘汰鸡价格", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到全国各省鸡蛋与淘汰鸡价格在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				startFetch(new Date());
			}else{
				logger.info("抓取全国各省鸡蛋与淘汰鸡价格的定时器已关闭");
			}
		}
	}
	
	public void startFetch(Date date){
		logger.info("==========开始抓取芝华数据蛋鸡数据中的全国各省鸡蛋与淘汰鸡价格=============");
		String updateTime = DateTimeUtil.formatDate(date, "yyyy-MM-dd");
		for(String varName:varNameMap.keySet()){
			String pageNum = "1";
			String pageUrl = url.replaceAll("%category%", varNameMap.get(varName))+pageNum;
			String[] params = {"table"};
			String[] rowColChoose = null;
			String contents = dataFetchUtil.getPrimaryContent(0, pageUrl, "utf-8", varName, params, rowColChoose, 12);
			if(contents != null && !contents.equals("")){
				List<String> res = new ArrayList<String>();
				String tmp = "";
				if(varName.equals("蛋禽")){
					tmp = "淘汰鸡价格";
				}else if(varName.equals("鸡蛋")){
					tmp = "全国各省鸡蛋价格";//个别情况会变为“全国鸡蛋价格”,尚未处理该情况
				}
				String[] tables = contents.split("<tr");
				boolean update = false;
				for(int i=10;i<tables.length;i++){//当天数据位序变成了14
					String table = tables[i];
					//if(dataFetchUtil.getMatchStr(table, tmp, null).size()==0) continue;
					if(table.indexOf(tmp) == -1) continue;
					String comp = "contentId=([0-9]+)([\\s\\S]+)"+tmp+"([\\s\\S]+)(日[^\"])([\\s\\S]+)"+updateTime;
					int[] index = {1,3};
					res.clear();
					res.addAll(dataFetchUtil.getMatchStr(table, comp, index));
					if(res.size() > 0){
						update = true;
						String time = res.get(1);
						time = time.replace("\">", "");
						time = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(time.replaceAll("(\\s*)", ""), "yyyy年M月d"), "yyyyMMdd");
						if(time == null){
							break;
						}
						int timeInt = Integer.parseInt(time);
						if(varName.equals("蛋禽")){
							String[] params1 = {"table","tbody"};
							//String[] params1 = {"table","align","CENTER"};
							String[] rowColChoose1 = {"0","111000"};//现在第三列为当天数据第四列为前一天数据，取当天数据。
							String contents1 = dataFetchUtil.getPrimaryContent(0, urlBase+res.get(0), "utf-8", varName, params1, rowColChoose1, 2);
							if(contents1 != null && !contents1.equals("")){
								String[] lines = contents1.split("\n");
								logger.info("分析处理"+tmp+timeInt+"数据");
								parseAndSaveTtj(varName, timeInt,lines);
							}else{
								logger.error(tmp+"数据没有抓取到");
							}
						}else if(varName.equals("鸡蛋")){
							String[] params1 = {"table","align","CENTER"};
							String[] rowColChoose1 = {"0","110100"};//现在表格一行的格式为：省	报价地	产品分类	2016/3/25	2016/3/24	涨跌
							String contents1 = dataFetchUtil.getPrimaryContent(0, urlBase+res.get(0), "utf-8", varName, params1, rowColChoose1, 0);
							if(contents1 != null && !contents1.equals("")){
								String[] lines = contents1.split("\n");
								logger.info("分析处理"+tmp+timeInt+"数据");
								parseAndSaveEgg(varName, timeInt,lines);
							}else{
								logger.error(tmp+"数据没有抓取到");
							}
						}
						break;
					}else{
						continue;
					}
				}
				if(!update){
					logger.info(tmp+"的数据网页没有更新");
				}
			}else{
				logger.error("抓取芝华数据蛋鸡数据主页为空");
			}
		}
	}
	
	private void parseAndSaveEgg(String varName, int timeInt, String[] lines) {
		int varId = Variety.getVaridByName(varName);
		Map<String, String> prov2dataMap = new HashMap<String, String>();
		Map<String, String> area2dataMap = new HashMap<String, String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String prov = "";
		String city = "";
		double sumCityPrice = 0;
		double sumPrice = 0;
		int areaNum = 0;
		int kindNum = 0;
		String emptyValue = "";//表格中的空值，为省略的省、市名，开始为"",现需要变更为"　",为了便于修改，设此变量
		for(String line:lines){
			//过滤掉无用的字符，防止因页面小的修改（比如添加一些零宽字符）导致错误。
			line = line.replaceAll("([\u0000-\u002b])|([\u003a-\u4e00])|([\u9fc0-\uffff])", emptyValue);
//			logger.info(line);
			String[] fields = line.split(",");
			if(fields.length < 1) {	//期望数据类似于"上海,联盟,3.53",前两个可为空，第三个字段不能为空，但即便为空也需保存省市名称。
				logger.info("废弃的城市数据：" + line);
				continue;
			} else if(fields.length == 1 && fields[0] != null && !fields[0].equals(emptyValue)) {
				prov = fields[0];
			}
			Double priceOfCity = null;//对应某个城市的价格
			try{
				priceOfCity = Double.parseDouble(fields[2]);
			} catch(Exception e){
				logger.warn("价格数据有问题的城市：" + line);
				priceOfCity = null;
			}
			if(!fields[0].equals(emptyValue) && !fields[1].equals(emptyValue)){//省份与城市都不为空
				if(!city.equals("")){
					double cityPrice = sumPrice/kindNum;
					sumCityPrice += cityPrice;
					areaNum ++;
					area2dataMap.put(prov+city, cityPrice+"");
				}
				if(!prov.equals("")){
					prov2dataMap.put(prov, sumCityPrice/areaNum+"");
					sumCityPrice = 0;
					areaNum = 0;
				}
				prov = fields[0];
				city = fields[1];
				if(priceOfCity != null){
					sumPrice = priceOfCity;
					kindNum = 1;
				} else {
					continue;
				}
			}else if(fields[0].equals(emptyValue) && !fields[1].equals(emptyValue)){//省份为空
				if(!city.equals("")){
					double cityPrice = sumPrice/kindNum;
					sumCityPrice += cityPrice;
					areaNum ++;
					area2dataMap.put(prov+city, cityPrice+"");
				}
				city = fields[1];
				if(priceOfCity != null){
					sumPrice = priceOfCity;
					kindNum = 1;
				} else {
					continue;
				}
			}else if(fields[0].equals(emptyValue) && fields[1].equals(emptyValue)){//省份与城市都为空
				if(priceOfCity != null){
					sumPrice += priceOfCity;
					kindNum ++;
				} else {
					continue;
				}
			}
		}
		double cityPrice = sumPrice/kindNum;
		sumCityPrice += cityPrice;
		areaNum ++;
		area2dataMap.put(prov+city, cityPrice+"");
		prov2dataMap.put(prov, sumCityPrice/areaNum+"");
		//天津数据不存在取昨天的天津数据
		if(prov2dataMap.get("天津")==null){
			prov2dataMap.put("天津",dao.getNewestSingleValue(varId, "各省鸡蛋价格", "天津"));
		}
		//计算主销区相关价格
		double sum0 = 0;
		int num0 = 0;
		for(String abbr:abbr2prov.keySet()){
			String[] provs = abbr2prov.get(abbr).split(",");
			double sum1 = 0;
			for(String provTmp:provs){
				//没有今天数据的取最近数据
				if(prov2dataMap.get(provTmp)==null){
					prov2dataMap.put(provTmp,dao.getNewestSingleValue(varId, "各省鸡蛋价格", provTmp));
				}
				sum1 += Double.parseDouble(prov2dataMap.get(provTmp));
			}
			prov2dataMap.put(abbr, sum1/provs.length+"");
			sum0 += sum1;
			num0 += provs.length;
		}
		prov2dataMap.put("主销区均价", sum0/num0+"");
		//计算鸡蛋贸易价差价格
		String liaoningVal = area2dataMap.get("辽宁朝阳");
		if(liaoningVal == null){
			liaoningVal = area2dataMap.get("辽宁辽阳");
		}
		if(liaoningVal == null){
			liaoningVal = area2dataMap.get("辽宁抚顺");
		}
		prov2dataMap.put("北京-朝阳", Double.parseDouble(prov2dataMap.get("北京"))-Double.parseDouble(liaoningVal)+"");
		prov2dataMap.put("广州-黄冈", Double.parseDouble(area2dataMap.get("广东广州"))-Double.parseDouble(area2dataMap.get("湖北黄冈"))+"");
		if(area2dataMap.get("山东临沂")!=null){
			prov2dataMap.put("上海-临沂", Double.parseDouble(prov2dataMap.get("上海"))-Double.parseDouble(area2dataMap.get("山东临沂"))+"");
		}else{
			if(area2dataMap.get("山东沂南")!=null){
				prov2dataMap.put("上海-临沂", Double.parseDouble(prov2dataMap.get("上海"))-Double.parseDouble(area2dataMap.get("山东沂南"))+"");
			}else{
				prov2dataMap.put("上海-临沂", Double.parseDouble(prov2dataMap.get("上海"))-Double.parseDouble(area2dataMap.get("山东沂水"))+"");
			}
		}
		for(String cnName:jdCnName2headerMap.keySet()){
			dataMap.clear();
			double sumTmp = 0;
			int num = 0;
			List<String> tmpList = jdCnName2headerMap.get(cnName);
			for(String provTmp:tmpList){
				String dataTmp = prov2dataMap.get(provTmp);
				if(dataTmp == null){
					dataMap.put(provTmp, "0");
				}else{
					dataMap.put(provTmp, dataTmp);
					sumTmp += Double.parseDouble(dataTmp);
					num ++;
				}
			}
			double avg = sumTmp/num;
			if(tmpList.contains("全国")){
				dataMap.put("全国", avg + "");
			}else if(tmpList.contains("主产省均价")){
				dataMap.put("主产省均价", avg + "");
			}
			logger.info("开始保存"+varName+cnName);
			dao.saveOrUpdateByDataMap(varId, cnName, timeInt, dataMap);
		}
	}

	private void parseAndSaveTtj(String varName, int timeInt, String[] lines) {
		int varId = Variety.getVaridByName(varName);
		Map<String, String> prov2dataMap = new HashMap<String, String>();
		Map<String, String> area2dataMap = new HashMap<String, String>();
		Map<String, String> dataMap = new HashMap<String, String>();
		String prov = "";
		double sumPrice = 0;
		int areaNum = 0;
		for(String line:lines){
			String[] fields = line.split(",");
			if(!fields[0].equals("")){//如果省份名不为空
				if(!prov.equals("")){
					prov2dataMap.put(prov, sumPrice/areaNum+"");
				}
				prov = fields[0];
				sumPrice = 0;
				areaNum = 0;
			}
			sumPrice += Double.parseDouble(fields[2]);
			areaNum ++;
			area2dataMap.put(prov+fields[1], fields[2]);
		}
		prov2dataMap.put(prov, sumPrice/areaNum+"");
		String cnName = "淘汰鸡价格";
		double sumTmp = 0;
		int num = 0;
		for(String provTmp:ttjAreaList){//没有数据的省市补0
			String dataTmp = prov2dataMap.get(provTmp);
			if(dataTmp == null){
				dataMap.put(provTmp, "0");
			}else{
				dataMap.put(provTmp, dataTmp);
				sumTmp += Double.parseDouble(dataTmp);
				num ++;
			}
		}
		logger.info("开始处理淘汰鸡主产区的特殊情况");
		//淘汰鸡主产区特殊处理,如果主产区为0的话取最近一天的主产区数据，且全国数据取的主产区的平均
		double mainProvSum = 0;
	 	for(String mainProv:ttjMainAreaList){
			if(dataMap.get(mainProv).equals("0")){
				String value = dao.getNewestDataByProv(varId, cnName, mainProv);
				mainProvSum += Double.parseDouble(value);
				dataMap.put(mainProv, value);
			}else{
				mainProvSum += Double.parseDouble(dataMap.get(mainProv));
			}
		}
		dataMap.put("全国", mainProvSum/5+"");
		logger.info("开始保存"+varName+cnName);
		dao.saveOrUpdateByDataMap(varId, cnName, timeInt, dataMap);
	}

	public static void main(String[] args){
//		new ChinaDataFetch().startFetch(DateTimeUtil.parseDateTime("20160517", "yyyyMMdd"));//改成对应时间序列运行即可补对应日期的数据（只要数据在行情列表页第一页，否则需修改上边的页码值。）
//		new ChinaDataFetch().start();
		ChinaDataFetch chinaDataFetch = new ChinaDataFetch();
		Date fetchDataDate = DateTimeUtil.parseDateTime("2016-09-23", DateTimeUtil.YYYY_MM_DD); 
		chinaDataFetch.startFetch(fetchDataDate);
	}
}
