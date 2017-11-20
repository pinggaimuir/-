package cn.futures.data.importor.crawler.futuresMarket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.Variety;
import cn.futures.data.importor.crawler.DCEDataFetch;
import cn.futures.data.service.FuturesMarketService;
import cn.futures.data.service.impl.FuturesMarketServiceImpl;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;

/**
 * 郑州商品交易所期货行情数据
 * @author ctm
 *
 */
public class CZCEFuturesMarket {
	private static final String className = CZCEFuturesMarket.class.getName();
	private FuturesMarketService futuresMarketService = new FuturesMarketServiceImpl();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private MyHttpClient httpClient = new MyHttpClient();
	private Log logger = LogFactory.getLog(DCEDataFetch.class);
	private DAOUtils dao = new DAOUtils();
	public static MarketPriceDAO priceDao = new MarketPriceDAO();
	private PriceCalc calc = new PriceCalc();
	private String urlcurr2 = "http://www.czce.com.cn/portal/exchange/%year%/datadaily/%date%.txt";
	private String urlold = "http://www.czce.com.cn/portal/exchange/jyxx/hq/hq%date%.html";
	private String urlcurr = "http://www.czce.com.cn/portal/DFSStaticFiles/Future/%year%/%date%/FutureDataDaily.txt";
	public static Map<String, String> czce_varName_Map = new HashMap<String, String>(){
		{
			put("SR", "郑州白糖");
			put("CF", "郑州棉花");
			put("OI", "郑州菜籽油");
			put("RI", "郑州早籼稻");
			put("WH", "郑州强麦");
			put("RO", "郑州菜籽油");
			put("ER", "郑州早籼稻");
			put("WS", "郑州强麦");
			put("RM", "郑州菜粕");
			put("RS", "郑州菜籽");
		}
	};
	
	@Scheduled
	(cron=CrawlScheduler.CRON_CZCE_FUTURES)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("郑州商品交易所期货行情数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到郑州商品交易所期货行情数据的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchFutures(date);
			}else{
				logger.info("抓取郑州商品交易所期货行情数据的定时器已关闭");
			}
		}
	}
	
	private void fetchFutures(Date date) {
		int timeInt = Integer.parseInt(DateTimeUtil.formatDate(date, "yyyyMMdd"));
		logger.info("==========开始抓取郑州商品交易所:期货行情数据"+timeInt+"=============");
		String pageUrl;
		if(timeInt>=20100825){
			pageUrl = urlcurr.replaceAll("%year%", (""+timeInt).substring(0,4))
				.replaceAll("%date%", timeInt+"");
			getCurrContent(pageUrl, timeInt);
		}else{
			pageUrl = urlold.replaceAll("%date%", timeInt+"");
			getOldContent(pageUrl, timeInt);
		}
	}
	
	//从20151112开始使用新的数据地址与格式
	//格式参见 http://www.czce.com.cn/portal/DFSStaticFiles/Future/2015/20151112/FutureDataDaily.txt
	private void getCurrContent(String pageUrl, int timeInt) {
		String body = httpClient.getHtmlByHttpClient(pageUrl, "GBK", "");
		String dirString = Constants.FUTURES_ROOT+Constants.FILE_SEPARATOR+"郑州交易所"+Constants.FILE_SEPARATOR;
		if (new File(dirString + timeInt + ".txt").exists())
			logger.warn("Overwrite: 郑州交易所-期货行情数据");
		try {
			FileStrIO.saveStringToFile(body, dirString,	timeInt + ".txt");
		} catch (IOException e) {
			logger.info("数据备份异常");
			e.printStackTrace();
		}
		if(body != null && !body.equals("")){
			String vardatas[] = body.split("小计([^\\n]+)\\n");
			for(String vars:vardatas){		
				String varName = "";
				int varId = -1;
				List<String> monthList = new ArrayList<String>();
				List<MarketPrice> prices = new ArrayList<MarketPrice>();	
				String tmp[] = vars.split("\n");
				for( String line:tmp ){
					line = line.trim();
					if(line.length() < 2||line.contains("郑州商品交易所期货每日行情表")||line.contains("小计")||line.contains("总计")||line.contains("品种月份")){
						continue;//制表信息不要
					}else{
						line = line.replaceAll(",", "").replaceAll("(\\s)+", "");//将 5,230.15 的逗号去掉
						String fields[] = line.split("[|]");
						int i=0;
						String code = fields[0].substring(fields[0].length()-5,fields[0].length()-3);
						if(czce_varName_Map.keySet().contains(code)){
							String month = fields[0].substring(fields[0].length()-3, fields[0].length());
							if(!monthList.contains(month)){
								monthList.add(month);
								varName = czce_varName_Map.get(code);
								varId = Variety.getVaridByName(varName);
								MarketPrice price = new MarketPrice();
								//一开始表名存的是交割月份，后期需要处理： 两年的相同月份合约同时存在、一个月份的合约只存在一份两种情况
								price.setTable(month);
								price.setVarid(varId);
								price.setTimeint(timeInt);
								if(!fields[2].equals("")){
									price.setOpen(Double.parseDouble(fields[2]));
								}
								if(!fields[3].equals("")){
									price.setHigh(Double.parseDouble(fields[3]));
								}
								if(!fields[4].equals("")){
									price.setLow(Double.parseDouble(fields[4]));
								}
								if(!fields[5].equals("")){
									price.setLast(Double.parseDouble(fields[5]));
								}
								if(!fields[6].equals("")){
									price.setSettle(Double.parseDouble(fields[6]));//结算价
								}
								if(!fields[10].equals("")){
									price.setPosition(Double.parseDouble(fields[10]));//持仓量
								}
								if(!fields[9].equals("")){
									price.setVolume(Double.parseDouble(fields[9]));//成交量
								}
								if(!fields[12].equals("")){
									price.setTurnover(Double.parseDouble(fields[12]));//成交额
								}
								if(Integer.parseInt((timeInt+"").substring(2,3)+month)<Integer.parseInt((timeInt+"").substring(2,6))){
									price.setCode(Integer.parseInt((timeInt+"").substring(2,3))+1+month);
								}else{
									price.setCode((timeInt+"").substring(2,3)+month);
								}
								prices.add(price);
							}else{
								logger.info("**********异常数据*********");
							}
						}
					}
				}
				if(prices.size()>0){
					if(varId != -1){
						if(varName.equals("郑州菜籽")){
							Map<String, String> monthMap = futuresMarketService.getNearlyMonthMap(monthList);
							for(MarketPrice price:prices){
								String cnNameTmp = "N"+monthMap.get(price.getTable());
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
						//prices.add(calc.calcMajor(prices));//计算主力连续
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
						priceDao.save(prices);
					}
				}
			}
		}
	}

	private void getCurrContent2(String pageUrl, int timeInt) {
		String body = httpClient.getHtmlByHttpClient(pageUrl, "GBK", "");
		if(body != null && !body.equals("")){
			body = body.replaceAll("(<[^>]+>)", "").replaceAll("\\s+", ",").replaceAll("&nbsp;", ",");
			String bak = dataFetchUtil.getLineContent(body.substring(1), 13);
			String dirString = Constants.FUTURES_ROOT+Constants.FILE_SEPARATOR+"郑州交易所"+Constants.FILE_SEPARATOR;
			if (new File(dirString + timeInt + ".txt").exists())
				logger.warn("Overwrite: 郑州交易所-期货行情数据");
			try {
				FileStrIO.saveStringToFile(bak, dirString,	timeInt + ".txt");
			} catch (IOException e) {
				logger.info("数据备份异常");
				e.printStackTrace();
			}
			String[] subtotals = body.split("小计");
			for(String subtotal:subtotals){
				String varName = "";
				int varId = -1;
				List<String> monthList = new ArrayList<String>();
				String[] fields = subtotal.split(",");
				List<MarketPrice> prices = new ArrayList<MarketPrice>();
				int i=0;
				while(i<fields.length){
					if(dataFetchUtil.isNumeric(fields[i]) || fields[i].length() < 5){//?
						i++;
						continue;
					}
					String code = fields[i].substring(fields[i].length()-5,fields[i].length()-3);
					if(czce_varName_Map.keySet().contains(code)){
						String month = fields[i].substring(fields[i].length()-3, fields[i].length());
						if(!monthList.contains(month)){
							monthList.add(month);
							varName = czce_varName_Map.get(code);
							varId = Variety.getVaridByName(varName);
							MarketPrice price = new MarketPrice();
							//一开始表名存的是交割月份，后期需要处理： 两年的相同月份合约同时存在、一个月份的合约只存在一份两种情况
							price.setTable(month);
							price.setVarid(varId);
							price.setTimeint(timeInt);
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
							if(!fields[i+6].equals("")){
								price.setSettle(Double.parseDouble(fields[i+6]));//结算价
							}
							if(!fields[i+10].equals("")){
								price.setPosition(Double.parseDouble(fields[i+10]));//持仓量
							}
							if(!fields[i+9].equals("")){
								price.setVolume(Double.parseDouble(fields[i+9]));//成交量
							}
							if(!fields[i+12].equals("")){
								price.setTurnover(Double.parseDouble(fields[i+12]));//成交额
							}
							if(Integer.parseInt((timeInt+"").substring(2,3)+month)<Integer.parseInt((timeInt+"").substring(2,6))){
								price.setCode(Integer.parseInt((timeInt+"").substring(2,3))+1+month);
							}else{
								price.setCode((timeInt+"").substring(2,3)+month);
							}
							prices.add(price);
							i = i+12;
						}else{
							logger.info("**********异常数据*********");
						}
					}
					i++;
				}
				if(prices.size()>0){
					if(varId != -1){
						if(varName.equals("郑州菜籽")){
							Map<String, String> monthMap = futuresMarketService.getNearlyMonthMap(monthList);
							for(MarketPrice price:prices){
								String cnNameTmp = "N"+monthMap.get(price.getTable());
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
						//prices.add(calc.calcMajor(prices));//计算主力连续
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
						priceDao.save(prices);
					}
				}
			}
		}
	}

	private void getOldContent(String pageUrl, int timeInt) {
		String body = httpClient.getHtmlByHttpClient(pageUrl, "GBK", "");
		if(body != null && !body.equals("")){
			body = body.replaceAll("(<[^>]+>)", ";").replaceAll("[;]+", ";").replaceAll("&nbsp;", "");
			String[] subtotals = body.split("小计");
			for(String subtotal:subtotals){
				String varName = "";
				int varId = -1;
				List<String> monthList = new ArrayList<String>();
				String[] fields = subtotal.split(";");
				List<MarketPrice> prices = new ArrayList<MarketPrice>();
				int i=0;
				while(i<fields.length){
					String tmp = fields[i].trim();
					if(dataFetchUtil.isNumeric(tmp) || tmp.length() < 5){
						i++;
						continue;
					}
					String code = tmp.substring(tmp.length()-5,tmp.length()-3);
					if(czce_varName_Map.keySet().contains(code)){
						String month = tmp.substring(tmp.length()-3, tmp.length());
						if(!monthList.contains(month)){
							monthList.add(month);
							varName = czce_varName_Map.get(code);
							varId = Variety.getVaridByName(varName);
							MarketPrice price = new MarketPrice();
							//一开始表名存的是交割月份，后期需要处理： 两年的相同月份合约同时存在、一个月份的合约只存在一份两种情况
							price.setTable(month);
							price.setVarid(varId);
							price.setTimeint(timeInt);
							String open = fields[i+2];
							if(!open.equals("")){
								price.setOpen(Double.parseDouble(open));
							}
							String high = fields[i+3];
							if(!high.equals("")){
								price.setHigh(Double.parseDouble(high));
							}
							String low = fields[i+4];
							if(!low.equals("")){
								price.setLow(Double.parseDouble(low));
							}
							String last = fields[i+5];
							if(!last.equals("")){
								price.setLast(Double.parseDouble(last));
							}
							String settle = fields[i+6];
							if(!settle.equals("")){
								price.setSettle(Double.parseDouble(settle.replaceAll(",", "")));//结算价
							}
							String position = fields[i+10];
							if(!position.equals("")){
								price.setPosition(Double.parseDouble(position.replaceAll(",", "")));//持仓量
							}
							String volume = fields[i+9];
							if(!volume.equals("")){
								price.setVolume(Double.parseDouble(volume.replaceAll(",", "")));//成交量
							}
							String turnover = fields[i+12];
							if(!turnover.equals("")){
								price.setTurnover(Double.parseDouble(turnover.replaceAll(",", "")));//成交额
							}
							prices.add(price);
							i = i+12;
						}else{
							logger.info("**********异常数据*********");
						}
					}
					i++;
				}
				if(prices.size()>0){
					if(varId != -1){
						if(varName.equals("郑州菜籽")){
							Map<String, String> monthMap = futuresMarketService.getNearlyMonthMap(monthList);
							for(MarketPrice price:prices){
								String cnNameTmp = "N"+monthMap.get(price.getTable());
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
						prices.add(calc.calcMajor(prices));//计算主力连续
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
						priceDao.save(prices);
					}
				}
			}
		}
		/*String[] filters = {"table","width","790"};
		String[] rowColChoose = {"",""};
		String oldContents = dataFetchUtil.getPrimaryContent(0, pageUrl, "GBK", "郑州交易所期货数据", filters, rowColChoose, 0);
		if(oldContents != null && !oldContents.equals("")){
			oldContents = oldContents.replaceAll("\n", ",").replaceAll("&nbsp;", "");
			String[] subtotals = oldContents.split("小计");
			for(String subtotal:subtotals){
				String varName = "";
				int varId = -1;
				List<String> monthList = new ArrayList<String>();
				String[] fields = subtotal.split(",");
				List<MarketPrice> prices = new ArrayList<MarketPrice>();
				int i=0;
				while(i<fields.length){
					if(dataFetchUtil.isNumeric(fields[i]) || fields[i].length() < 5){
						i++;
						continue;
					}
					String code = fields[i].substring(fields[i].length()-5,fields[i].length()-3);
					if(czce_varName_Map.keySet().contains(code)){
						String month = fields[i].substring(fields[i].length()-3, fields[i].length());
						if(!monthList.contains(month)){
							monthList.add(month);
							varName = czce_varName_Map.get(code);
							varId = Variety.getVaridByName(varName);
							MarketPrice price = new MarketPrice();
							//一开始表名存的是交割月份，后期需要处理： 两年的相同月份合约同时存在、一个月份的合约只存在一份两种情况
							price.setTable(month);
							price.setVarid(varId);
							price.setTimeint(timeInt);
							String open = fields[i+2];
							if(!open.equals("")){
								price.setOpen(Double.parseDouble(open));
							}
							String high = fields[i+3];
							if(!high.equals("")){
								price.setHigh(Double.parseDouble(high));
							}
							String low = fields[i+4];
							if(!low.equals("")){
								price.setLow(Double.parseDouble(low));
							}
							String last = fields[i+5];
							if(!last.equals("")){
								price.setLast(Double.parseDouble(last));
							}
							String settle = fields[i+6];
							if(!settle.equals("")){
								price.setSettle(Double.parseDouble(settle));//结算价
							}
							String position = fields[i+10];
							if(!position.equals("")){
								price.setPosition(Double.parseDouble(position));//持仓量
							}
							String volume = fields[i+9];
							if(!volume.equals("")){
								price.setVolume(Double.parseDouble(volume));//成交量
							}
							String turnover = fields[i+12];
							if(!turnover.equals("")){
								price.setTurnover(Double.parseDouble(turnover));//成交额
							}
							prices.add(price);
							i = i+11;
						}else{
							logger.info("**********异常数据*********");
						}
					}
					i++;
				}
				if(prices.size()>0){
					Map<String, String> monthMap = futuresMarketService.getMonthMap(monthList);
					if(varId != -1){
						if(varName.equals("郑州菜籽")){
							for(MarketPrice price:prices){
								String cnNameTmp = monthMap.get(price.getTable());
								price.setTable(dao.getTableName(varId, cnNameTmp));
							}
						}else{
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
						prices.add(calc.calcMajor(prices));//计算主力连续
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
						priceDao.save(prices);
					}
				}
			}
		}*/
	}

	public static void main(String[] args){
		CZCEFuturesMarket futures = new CZCEFuturesMarket();
		Date date = new Date();
		//futures.start();
		date = DateTimeUtil.parseDateTime("20160222", "yyyyMMdd");
		while(DateTimeUtil.parseDateTime("20160220", "yyyyMMdd").before(date)){
			futures.fetchFutures(date);
			date = DateTimeUtil.addDay(date, -1);
		}
	}
}
