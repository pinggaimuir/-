package cn.futures.data.importor.crawler.futuresMarket;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 期货行情数据—马来西亚棕榈油—指数连续、主力连续、1-9号连续
 * @author ctm
 *
 */
@Component
public class MYPalmOilQuotFetch {
	private static final String className = MYPalmOilQuotFetch.class.getName();
	private static final Log logger = LogFactory.getLog(MYPalmOilQuotFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private MarketPriceDAO dao = new MarketPriceDAO();
	private static final int SERIES_CONSTANT = 9;//表示1-9号连续
	private static final Map<String, String> MYOIL_QUOTE_MAP = new HashMap<String, String>();
	
	static{
		MYOIL_QUOTE_MAP.put("马来西亚棕榈油", "http://ws.bursamalaysia.com/market/derivatives/prices/prices_f.html?_=1430282534089&callback=jQuery162046134310180860216_1430281784104&contract_code=FCPO&filter=BD01&page=1");
	}
	public static final Map<String, String> DBTABLE_MAP = new HashMap<String, String>();
	static{
		DBTABLE_MAP.put("1号连续","CX_MarketData_3");
		DBTABLE_MAP.put("2号连续","CX_MarketData_4");
		DBTABLE_MAP.put("3号连续","CX_MarketData_5");
		DBTABLE_MAP.put("4号连续","CX_MarketData_6");
		DBTABLE_MAP.put("5号连续","CX_MarketData_7");
		DBTABLE_MAP.put("6号连续","CX_MarketData_8");
		DBTABLE_MAP.put("7号连续","CX_MarketData_9");
		DBTABLE_MAP.put("8号连续","CX_MarketData_10");
		DBTABLE_MAP.put("9号连续","CX_MarketData_11");
		DBTABLE_MAP.put("指数连续","CX_MarketData_1");
		DBTABLE_MAP.put("主力连续","CX_MarketData_2");
		}
	 
	@Scheduled
	(cron=CrawlScheduler.CRON_MYOIL_QUOTE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("马来西亚棕榈油连续数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到马来西亚棕榈油连续数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到马来西亚棕榈油连续数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -1);
				fetchData(date);
			}else{
				logger.info("抓取马来西亚棕榈油连续数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取马来西亚棕榈油连续数据的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		for(String varName:MYOIL_QUOTE_MAP.keySet()){
			String url = MYOIL_QUOTE_MAP.get(varName);
			//int varid = Variety.getVaridByName(varName);
			int varId = 2035;
			logger.info("start fetch " + varName + varId + "@" + url);
			String body = CrawlerUtil.httpGetBody(url);
			if (body == null || body.equals("")) {
				logger.error("无法获取马来西亚棕榈油的期货行情数据");
			}else{
				String[] lineDatas = body.split("/tr");
				List<MarketPrice> noAttendCalPrices = new ArrayList<MarketPrice>();
				List<MarketPrice> prices = new ArrayList<MarketPrice>();
				PriceCalc calc = new PriceCalc();
				for(int i=1;i<=SERIES_CONSTANT;i++){
					String key = i + "号连续";
					String[] fieldDatas = lineDatas[i].split("/td");
					String datas = getKeyDatas(fieldDatas);
					logger.info("read[" + key + "]["+varId+"]:"+datas);
					MarketPrice price = parse(datas, DBTABLE_MAP.get(key), varId);
					if(price != null){
						if(CrawlerUtil.isEmpty(price)){
							noAttendCalPrices.add(price);
						}else{
							prices.add(price);
						}
					}
				}
				if(prices.size() > 0){
					MarketPrice priceIndex = calc.calcIndex(prices);//计算指数连续
					prices.add(calc.calcMajor(prices));//计算主力连续
					prices.add(priceIndex);
				}
				prices.addAll(noAttendCalPrices);
				logger.info("开始保存"+varName+"数据");
				dao.dbm3SaveByDbName(prices);
				//备份至txt
				DAOUtils.bak2txt(prices);
			}
		}
	}
		
	/**
	 * 对抓获的数据解析成MarketPrice数据
	 * @param dbTable
	 * @param varid
	 * @return
	 */
	private MarketPrice parse(String datas, String dbTable, int varid){
		MarketPrice p = new MarketPrice(dbTable, varid);
		try {
			String[] splits = datas.split(",");
			for(int i=0; i<splits.length; i++){
				String s = splits[i];
				if(!dataFetchUtil.isNumeric(s)){
					splits[i] = dataFetchUtil.getDigitByStr(s);
				}
			}
			if(splits[1] != null && !splits[1].equals("")&& !splits[1].equals("-")){
				double open = Double.parseDouble(splits[1]);
				p.setOpen(open);
			}
			if(splits[2] != null && !splits[2].equals("")&& !splits[2].equals("-")){
				double high = Double.parseDouble(splits[2]);
				p.setHigh(high);
			}
			if(splits[3] != null && !splits[3].equals("")&& !splits[3].equals("-")){
				double low = Double.parseDouble(splits[3]);
				p.setLow(low);
			}
			if(splits[6] != null && !splits[6].equals("")&& !splits[6].equals("-")){
				double last = Double.parseDouble(splits[6]);
				p.setLast(last);
			}
			if(splits[9] != null && !splits[9].equals("")&& !splits[9].equals("-")){
				double position = Double.parseDouble(splits[9]);
				p.setPosition(position);
			}
			if(splits[10] != null && !splits[10].equals("")&& !splits[10].equals("-")){
				double volume = Double.parseDouble(splits[10] + ((splits.length==12)?splits[11]:""));
				p.setVolume(volume);
			}
//			double turnover = settle * volume / 1e6;
			
			int timeint = CrawlerUtil.yesterdayTimeint();
			//String code = splits[0];
			//p.setCode(code);
//			p.setTurnover(turnover);
			p.setTimeint(timeint);
			
		} catch (Exception e){
			logger.warn("数据行解析异常"+datas);
			logger.error(e);
			return null;
		}
		
		logger.info("parsed: " + p);
		return p;
	}
	
	private String getKeyDatas(String[] fieldDatas){
		String datas = "";
		for(int j=2;j<fieldDatas.length-1;j++){
			if(j==11){
				String tmp = fieldDatas[j].split("003E")[3];
				datas = datas + tmp.substring(0,tmp.indexOf("\\u"))+",";
			}else{
				String tmp = fieldDatas[j].split("003E")[2];
				datas = datas + tmp.substring(0,tmp.indexOf("\\u"))+",";
			}
		}
		return datas;
	}
	
	public static void main(String []args){
		new MYPalmOilQuotFetch().start();
	}
}
