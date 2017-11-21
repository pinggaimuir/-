package cn.futures.data.importor.crawler.futuresMarket;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.importor.crawler.DCEDataFetch;
import cn.futures.data.service.FuturesMarketService;
import cn.futures.data.service.impl.FuturesMarketServiceImpl;
import cn.futures.data.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 大连商品交易所期货行情数据
 * @author ctm
 *
 */
public class DCEFuturesMarket {
	private static final String className = DCEFuturesMarket.class.getName();
	private static final String encoding = "utf-8";
	private FuturesMarketService futuresMarketService = new FuturesMarketServiceImpl();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private Log logger = LogFactory.getLog(DCEDataFetch.class);
	private DAOUtils dao = new DAOUtils();
	public static MarketPriceDAO priceDao = new MarketPriceDAO();
	private PriceCalc calc = new PriceCalc();
	private String url = "http://www.dce.com.cn/publicweb/quotesdata/dayQuotesCh.html";
	public static Map<String, String> dce_varName_Map = new HashMap<String, String>(){
		{
			put("豆粕", "大连豆粕");
			put("鸡蛋", "大连鸡蛋");
			put("玉米淀粉", "大连玉米淀粉");
			put("玉米", "大连玉米");
			put("棕榈油", "大连棕榈油");
			put("豆油", "大连豆油");
			put("豆一", "大连豆一");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_DCE_FUTURES)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("大连商品交易所期货行情数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到大连商品交易所期货行情数据的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchFutures(date);
			}else{
				logger.info("抓取大连商品交易所期货行情数据的定时器已关闭");
			}
		}
	}
	
	public void fetchFutures(Date date){
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		logger.info("=========开始抓取大连交易所：期货行情数据"+timeInt+"============");
		Map<String, String> postParams = new HashMap<String, String>();//post请求提交的参数
		postParams.put("dayQuotes.variety", "all");
		postParams.put("dayQuotes.trade_type", "0");
		postParams.put("year", timeInt.substring(0, 4));
		postParams.put("month", String.valueOf(Integer.parseInt(timeInt.substring(4, 6)) - 1));
		postParams.put("day", String.valueOf(Integer.parseInt(timeInt.substring(6, 8))));
		postParams.put("currDate", timeInt);
		String contents = dataFetchUtil.getCompleteContent(0, url, encoding, "大连交易所期货行情.txt", postParams);
		String[] filters = {"table","cellpadding", "0"};
		String[] rowColChoose = {"", ""};
		Document document = Jsoup.parse(contents);
		contents = dataFetchUtil.parseContent(document.html(), contents, filters, 0, rowColChoose);
		if(contents != null && !contents.equals("")){
			contents = contents.replace("&nbsp;", "");
			contents = contents.replaceAll("\r\n", ",").replaceAll("\n", ",");
			//期货数据备份
			String bak = dataFetchUtil.getLineContent(contents, 14);
			String dirString = Constants.FUTURES_ROOT+Constants.FILE_SEPARATOR+"大连交易所"+Constants.FILE_SEPARATOR;
			if (new File(dirString + timeInt + ".txt").exists())
				logger.warn("Overwrite: 大连交易所-期货行情数据");
			try {
				FileStrIO.saveStringToFile(bak, dirString,	timeInt + ".txt");
			} catch (IOException e) {
				logger.info("数据备份异常");
				e.printStackTrace();
			}
			String[] subtotals = contents.split("小计");
			for(String subtotal:subtotals){
				String varName = "";
				int varId = -1;
				List<String> monthList = new ArrayList<String>();
				String[] fields = subtotal.split(",");
				List<MarketPrice> prices = new ArrayList<MarketPrice>();
				int i=0;
				while(i<fields.length-10){
					if(dce_varName_Map.keySet().contains(fields[i])){
						String monthTmp = fields[i+1];
						if(!monthList.contains(monthTmp)){
							monthList.add(monthTmp);
							varName = dce_varName_Map.get(fields[i]);
							varId = Variety.getVaridByName(varName);
							MarketPrice price = new MarketPrice();
							//一开始表名存的是交割月份，后期需要处理： 两年的相同月份合约同时存在、一个月份的合约只存在一份两种情况
							price.setTable(monthTmp);
							price.setVarid(varId);
							price.setTimeint(Integer.parseInt(timeInt));
							if(!fields[i+2].equals("")){
								price.setOpen(Double.parseDouble(fields[i+2]));
							}
							if(!fields[i+3].equals("")){
								price.setHigh(Double.parseDouble(fields[i+3]));
							}
							if(!fields[i+4].equals("")){
								price.setLow(Double.parseDouble(fields[i+4]));
							}
							if(!fields[i+5].equals("")){
								price.setLast(Double.parseDouble(fields[i+5]));
							}
							if(!fields[i+7].equals("")){
								price.setSettle(Double.parseDouble(fields[i+7]));//结算价
							}
							if(!fields[i+11].equals("")){
								price.setPosition(Double.parseDouble(fields[i+11]));//持仓量
							}
							if(!fields[i+10].equals("")){
								price.setVolume(Double.parseDouble(fields[i+10]));//成交量
							}
							if(!fields[i+13].equals("")){
								price.setTurnover(Double.parseDouble(fields[i+13]));//成交额
							}
							price.setCode(monthTmp);
							prices.add(price);
							i = i+13;
						}else{
							logger.info("**********异常数据*********");
						}
					}
					i++;
				}
				if(prices.size()>0){
					if(varId != -1){
						if(varName.equals("大连玉米淀粉") ||varName.equals("大连棕榈油") ||varName.equals("大连鸡蛋")){
							Map<String, String> monthMap = futuresMarketService.getNearlyMonthMap(monthList);
							for(MarketPrice price:prices){
								String cnNameTmp = monthMap.get(price.getTable());
								price.setTable(dao.getTableName(varId, cnNameTmp));
							}
						}else{
							Map<String, String> monthMap = futuresMarketService.getMonthMap(monthList);
							List<MarketPrice> dupPrices = new ArrayList<MarketPrice>();
							for(MarketPrice price:prices){
								String cnNameTmp = monthMap.get(price.getTable());
								if(cnNameTmp.startsWith("(N+1)") || cnNameTmp.startsWith("N")){
									price.setTable(dao.getTableName(varId, cnNameTmp));
								}else{
									price.setTable(dao.getTableName(varId, "N"+cnNameTmp));
									MarketPrice dupPrice = new MarketPrice();
									dupPrice = price.clone();
									dupPrice.setTable(dao.getTableName(varId, "(N+1)"+cnNameTmp));
									dupPrice.setDuplicated(true);
									dupPrices.add(dupPrice);
								}
							}
							prices.addAll(dupPrices);
						}
						MarketPrice priceIndex = calc.calcIndex(prices);//计算指数连续
						prices.add(futuresMarketService.getMajorPrice(prices, varId));//计算主力连续
						prices.add(priceIndex);
						//删除没有数据表的合约
						List<MarketPrice> removePrices = new ArrayList<MarketPrice>();
						for(MarketPrice price:prices){
							if(price.getTable().equals("")){
								removePrices.add(price);
							}
						}
						if(removePrices.size()>0){
							prices.removeAll(removePrices);
						}
						priceDao.dbm3SaveByDbName(prices);
					}
				}
			}
		}else{
			logger.info(timeInt+"没有抓取到网页数据");
		}
	}
	
	public static void main(String[] args){
		DCEFuturesMarket futures = new DCEFuturesMarket();
		futures.start();
//		futures.fetchFutures(new Date());
//		Date targetDate = DateTimeUtil.parseDateTime("20161118", "yyyyMMdd");
//		futures.fetchFutures(targetDate);
//		while(DateTimeUtil.parseDateTime("20160205", "yyyyMMdd").before(date)){
//			futures.fetchFutures(date);
//			date = DateTimeUtil.addDay(date, -1);
//		}
	}
}
