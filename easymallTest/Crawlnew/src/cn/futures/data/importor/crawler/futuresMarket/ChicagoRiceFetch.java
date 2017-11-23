package cn.futures.data.importor.crawler.futuresMarket;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 期货行情数据-芝加哥美稻米-指数连续、主力连续、1-5号连续
 * @author ctm
 *
 */
@Component
public class ChicagoRiceFetch {
	private static final String className = ChicagoRiceFetch.class.getName();
	private static final Log logger = LogFactory.getLog(ChicagoRiceFetch.class);
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private MarketPriceDAO dao = new MarketPriceDAO();
	private static final int SERIES_CONSTANT = 5;//表示1-5号连续
	private static final Map<String, String> CHICAGO_RICE_MAP = new HashMap<String, String>();
	
	static{
		CHICAGO_RICE_MAP.put("芝加哥美稻米", "http://www.cmegroup.com/trading/agricultural/grain-and-oilseed/rough-rice_quotes_settlements_futures.html");
	}
	public static final Map<String, String> DBTABLE_MAP = new HashMap<String, String>();
	static{
		DBTABLE_MAP.put("1号连续","CX_MarketData_3");
		DBTABLE_MAP.put("2号连续","CX_MarketData_4");
		DBTABLE_MAP.put("3号连续","CX_MarketData_5");
		DBTABLE_MAP.put("4号连续","CX_MarketData_6");
		DBTABLE_MAP.put("5号连续","CX_MarketData_7");
		DBTABLE_MAP.put("指数连续","CX_MarketData_1");
		DBTABLE_MAP.put("主力连续","CX_MarketData_2");
		}
	 
	@Scheduled
	(cron=CrawlScheduler.CRON_CHICAGO_RICE)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("芝加哥美稻米连续数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到芝加哥美稻米连续数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = DateTimeUtil.addDay(new Date(), -1);
				fetchData(date);
			}else{
				logger.info("抓取芝加哥美稻米连续数据的定时器已关闭");
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
		for(String varName:CHICAGO_RICE_MAP.keySet()){
			String url = CHICAGO_RICE_MAP.get(varName);
			int varid = Variety.getVaridByName(varName);
			logger.info("fetch " + varName + varid + "@" + url);
			String[] params = {"table"};//通过params过滤table
			String[] rowColChoose = {"00", "011110"};//选择哪几行哪几列
			String contents = dataFetchUtil.getPrimaryContent(0, url, encoding, varName, params, rowColChoose, 0);
			if(contents == null){
				logger.error("抓取"+varName+"所需要的数据为空");
			}else{
				priceList = getListByContents(contents, varid);
			}
			logger.info("开始保存"+varName+"数据");
			dao.dbm3SaveByDbName(priceList);
			//备份至txt
			DAOUtils.bak2txt(priceList);
		}
	}
	
	private List<MarketPrice> getListByContents(String contents, int varid) {
		PriceCalc calc = new PriceCalc();
		List<MarketPrice> prices = new ArrayList<MarketPrice>();
		String[] lineContents = contents.split("\n");
		List<MarketPrice> noAttendCalPrices = new ArrayList<MarketPrice>();//不参与指数连续与主力连续的价格
		for(int i=0; i<SERIES_CONSTANT; i++){
			String line = lineContents[i];
			String key = (i+1) + "号连续";
			logger.info("read[" + key + "]["+varid+"]:"+line);
			MarketPrice price = parse(line, DBTABLE_MAP.get(key), varid);
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
			if(!splits[0].equals("")){
				double open = Double.parseDouble(splits[0]);
				p.setOpen(open);
			}
			if(!splits[1].equals("")){
				double high = Double.parseDouble(splits[1]);
				p.setHigh(high);
			}
			if(!splits[2].equals("")){
				double low = Double.parseDouble(splits[2]);
				p.setLow(low);
			}
			if(!splits[3].equals("")){
				double last = Double.parseDouble(splits[3]);
				p.setLast(last);
			}
			if(!splits[4].equals("")){
				double settle = Double.parseDouble(splits[4]);
				p.setSettle(settle);
			}
			if(!splits[5].equals("")){
				double volume = Double.parseDouble(splits[5]);
				p.setVolume(volume);
			}
			if(!splits[6].equals("")){
				double position = Double.parseDouble(splits[6]);
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
		new ChicagoRiceFetch().start();
	}
}
