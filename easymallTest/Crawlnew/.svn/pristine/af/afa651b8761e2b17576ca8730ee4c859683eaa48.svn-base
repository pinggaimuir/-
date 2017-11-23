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
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 上海商品交易所期货行情数据
 * @author ctm
 *
 */
public class SHFEFuturesMarket {
	private static final String className = SHFEFuturesMarket.class.getName();
	private FuturesMarketService futuresMarketService = new FuturesMarketServiceImpl();
	private MyHttpClient httpClient = new MyHttpClient();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private Log logger = LogFactory.getLog(DCEDataFetch.class);
	private DAOUtils dao = new DAOUtils();
	public static MarketPriceDAO priceDao = new MarketPriceDAO();
	private PriceCalc calc = new PriceCalc();
	private String url = "http://www.shfe.com.cn/data/dailydata/kx/kx%date%.dat";
	public static Map<String, String> shfe_varName_Map = new HashMap<String, String>(){
		{
			put("cu_f", "上海铜");
			put("au_f", "上海黄金");
			put("ru_f", "上海橡胶");
		}
	};
	@Scheduled
	(cron=CrawlScheduler.CRON_SHFE_FUTURES)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("上海商品交易所期货行情数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到上海商品交易所期货行情数据的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchFutures(date);
			}else{
				logger.info("抓取上海商品交易所期货行情数据的定时器已关闭");
			}
		}
	}
	
	private void fetchFutures(Date date) {
		String timeInt = DateTimeUtil.formatDate(date, "yyyyMMdd");
		logger.info("=========开始抓取上海交易所：期货行情数据"+timeInt+"============");
		String pageUrl = url.replaceAll("%date%", timeInt);
		String contents = httpClient.getHtmlByHttpClient(pageUrl, "utf-8", "");
		contents = contents.substring(0,contents.indexOf("总计"));
		if(contents != null && !contents.equals("")){
			contents = contents.replaceAll("([\"|}]+)", "").replaceAll(":", ",");
			String bak = fetchUtil.getLineContent(contents.substring(contents.indexOf(",")+1), 32);
			String dirString = Constants.FUTURES_ROOT+Constants.FILE_SEPARATOR+"上海交易所"+Constants.FILE_SEPARATOR;
			if (new File(dirString + timeInt + ".txt").exists())
				logger.warn("Overwrite: 上海交易所-期货行情数据");
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
				while(i<fields.length){
					String tmp = fields[i].trim();
					if(fetchUtil.isNumeric(tmp) || tmp.length() != 4){//cu_f、au_f、ru_f均为4位
						i++;
						continue;
					}
					if(shfe_varName_Map.keySet().contains(tmp) && (i+6)<fields.length){
						String monthTmp = fields[i+6].trim();
						String month = monthTmp.substring(monthTmp.length()-2, monthTmp.length());
						if(!monthList.contains(month)){
							monthList.add(month);
							varName = shfe_varName_Map.get(tmp);
							varId = Variety.getVaridByName(varName);
							MarketPrice price = new MarketPrice();
							String tableName = dao.getTableName(varId, Integer.parseInt(month)+"月连续");
							price.setTable(tableName);
							price.setVarid(varId);
							price.setTimeint(Integer.parseInt(timeInt));
							String openPrice = fields[i+10].trim();
							if(!openPrice.equals("")){
								price.setOpen(Double.parseDouble(openPrice));
							}
							String highPrice = fields[i+12].trim();
							if(!highPrice.equals("")){
								price.setHigh(Double.parseDouble(highPrice));
							}
							String lowPrice = fields[i+14].trim();
							if(!lowPrice.equals("")){
								price.setLow(Double.parseDouble(lowPrice));
							}
							String lastPrice = fields[i+16].trim();
							if(!lastPrice.equals("")){
								price.setLast(Double.parseDouble(lastPrice));
							}
							String settlePrice = fields[i+18].trim();
							if(!settlePrice.equals("")){
								price.setSettle(Double.parseDouble(settlePrice));//结算价
							}
							String positionPrice = fields[i+26].trim();
							if(!positionPrice.equals("")){
								price.setPosition(Double.parseDouble(positionPrice));//持仓量
							}
							String volumePrice = fields[i+24].trim();
							if(!volumePrice.equals("")){
								price.setVolume(Double.parseDouble(volumePrice));//成交量
							}
							//成交额settle * volume / 1e4; 单位是万元
							price.setTurnover(price.getSettle()*price.getVolume()/1e4);//成交额
							price.setCode(monthTmp);
							prices.add(price);
							i = i+30;
						}else{
							logger.info("**********异常数据*********");
						}
					}
					i++;
				}
				if(prices.size()>0){
					if(varId != -1){
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
						prices.removeAll(removePrices);
						priceDao.dbm3SaveByDbName(prices);
					}
				}
			}
		}else{
			logger.info(timeInt+"没有抓取到网页数据");
		}
	}
	
	public static void main(String[] args){
		SHFEFuturesMarket futures = new SHFEFuturesMarket();
		futures.start();
//		futures.fetchFutures(DateTimeUtil.parseDateTime("20150722", "yyyyMMdd"));
//		Date date = DateTimeUtil.parseDateTime("20150723", "yyyyMMdd");
//		futures.fetchFutures(date);
		//Date date = new Date();
		/*while(DateTimeUtil.parseDateTime("20110225", "yyyyMMdd").before(date)){
			futures.fetchFutures(date);
			date = DateTimeUtil.addDay(date, -1);
		}*/
	}
}
