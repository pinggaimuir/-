package cn.futures.data.importor.crawler.futuresMarket;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 期货行情数据—伦敦白糖—1-5号连续
 * @author ctm
 *
 */
@Component
public class LondonSugarFetch {
	private static final String className = LondonSugarFetch.class.getName();
	private static final Log logger = LogFactory.getLog(LondonSugarFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private MarketPriceDAO dao = new MarketPriceDAO();
	private static final int SERIES_CONSTANT = 5;//表示1-5号连续
	private static final Map<String, String> LONDON_SUGAR_MAP = new HashMap<String, String>();
	
	static{
		LONDON_SUGAR_MAP.put("伦敦白糖", "http://old.barchart.com/commodityfutures/White_Sugar_5_Futures/SW?mode=D&view=");
	}
	public static final Map<String, String> DBTABLE_MAP = new HashMap<String, String>();
	static{
		DBTABLE_MAP.put("1号连续","CX_LIFFE1");
		DBTABLE_MAP.put("2号连续","CX_LIFFE2");
		DBTABLE_MAP.put("3号连续","CX_LIFFE3");
		DBTABLE_MAP.put("4号连续","CX_LIFFE4");
		DBTABLE_MAP.put("5号连续","CX_LIFFE5");
		}
	
	@Scheduled
	(cron=CrawlScheduler.CRON_LONDON_SUGAR)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("伦敦白糖连续数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到伦敦白糖连续数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到伦敦白糖连续数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				try{
					Date date = DateTimeUtil.addDay(new Date(), -1);
					fetchData(date);
				} catch(Exception e) {
					logger.error(e);
					RecordCrawlResult.recordFailData(className, null, null, "\"发生未知异常。" + e.getMessage() + "\"");
				}
			}else{
				logger.info("抓取伦敦白糖连续数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取伦敦白糖连续数据的定时器已关闭");
			}
		}
	}
	
	private void fetchData(Date date){
		List<MarketPrice> priceList = new ArrayList<MarketPrice>();
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		logger.info("start!");
		String encoding = "gbk";
		for(String varName:LONDON_SUGAR_MAP.keySet()){
			String url = LONDON_SUGAR_MAP.get(varName);
			int varid = Variety.getVaridByName(varName);
			logger.info("fetch " + varName + varid + "@" + url);
			String[] params = {"table","id","dt1"};//通过params过滤table
			String[] rowColChoose = {"0", "010111110"};//选择哪几行哪几列
			String contents = dataFetchUtil.getPrimaryContent(0, url, encoding, varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
				RecordCrawlResult.recordFailData(className, varName, null, "抓取"+varName+"所需要的数据为空");
			}else{
				priceList = getListByContents(contents, varid);
			}
			dao.saveNoCode(priceList);//不包括code字段的价格数据保存
			//备份至txt
			DAOUtils.bak2txt(priceList);
		}
	}
	
	private List<MarketPrice> getListByContents(String contents, int varid) {
		List<MarketPrice> prices = new ArrayList<MarketPrice>();
		String[] lineContents = contents.split("\n");
		for(int i=0; i<SERIES_CONSTANT; i++){
			String line = lineContents[i];
			String key = (i+1) + "号连续";
			logger.info("read[" + key + "]["+varid+"]:"+line);
			dataFetchUtil.addList(prices, parse(line, DBTABLE_MAP.get(key), varid));
		}
		return prices;
	}
	
	/**
	 * 对抓获的数据解析成MarketPrice数据
	 * @param line
	 * @param dbTable
	 * @param varid
	 * @return
	 */
	private MarketPrice parse(String line, String dbTable, int varid){
		MarketPrice p = new MarketPrice(dbTable, varid);
		try {
			String[] splits = line.split(",");
			for(int i=0; i<splits.length; i++){
				String s = splits[i];
				if(!dataFetchUtil.isNumeric(s)){
					splits[i] = dataFetchUtil.getDigitByStr(s);
				}
			}
			if(!splits[1].equals("")){
				double open = Double.parseDouble(splits[1]);
				p.setOpen(open);
			}
			if(!splits[2].equals("")){
				double high = Double.parseDouble(splits[2]);
				p.setHigh(high);
			}
			if(!splits[3].equals("")){
				double low = Double.parseDouble(splits[3]);
				p.setLow(low);
			}
			if(!splits[0].equals("")){
				double last = Double.parseDouble(splits[0]);
				p.setLast(last);
			}
			if(!splits[4].equals("")){
				double volume = Double.parseDouble(splits[4]);
				p.setVolume(volume);
			}
			if(!splits[5].equals("")){
				double position = Double.parseDouble(splits[5]);
				p.setPosition(position);
			}
//			double turnover = settle * volume / 1e6;
			
			int timeint = CrawlerUtil.yesterdayTimeint();
			//String code = splits[0];
			//p.setCode(code);
//			p.setTurnover(turnover);
			p.setTimeint(timeint);
			
		} catch (Exception e){
			logger.warn("数据行解析异常"+line);
			logger.error(e);
			return null;
		}
		
		logger.info("parsed: " + p);
		return p; 
	}
	
	public static void main(String []args){
		new LondonSugarFetch().start();
	}
}
