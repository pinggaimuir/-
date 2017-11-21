package cn.futures.data.importor.crawler.futuresMarket;

import cn.futures.IdenExceptions.JobASIAException;
import cn.futures.IdenExceptions.JobUSException;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.ExcelSchema;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.DataFetchUtil;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MarketCrawlerForeign {
	private static final String className = MarketCrawlerForeign.class.getName();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	public static final Logger LOG = Logger.getLogger(MarketCrawlerForeign.class);
	public static MarketPriceDAO dao = new MarketPriceDAO();
	public static final Map<String,String> URL_MAP = new HashMap<String,String>();
	static {
//		URL_MAP.put("ICE原糖", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=56&breed=SB");
//		URL_MAP.put("ICE棉花", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=56&breed=CT");
		URL_MAP.put("纽约原油", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=57&breed=CL");
//		URL_MAP.put("芝加哥美豆", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=50&breed=ZS");
//		URL_MAP.put("芝加哥美豆粕", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=50&breed=ZM");
//		URL_MAP.put("芝加哥美豆油", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=50&breed=ZL");
		URL_MAP.put("芝加哥美玉米", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=50&breed=ZC");
		URL_MAP.put("芝加哥美麦", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=50&breed=ZW");
	}
	public static final Map<String,String> URL_MAP_ASIA = new HashMap<String,String>();
	static {
		URL_MAP_ASIA.put("马来西亚棕榈油", "http://quote.waipan.futures.hexun.com/Other/Quote/Market.ashx?market=55&breed=FCPO");
	}
	
	public static final Map<String,String> MONTH_MAP = new HashMap<String,String>();
	static {
		MONTH_MAP.put("A", "主力");
		MONTH_MAP.put("F", "1");
		MONTH_MAP.put("G", "2");
		MONTH_MAP.put("H", "3");
		MONTH_MAP.put("J", "4");
		MONTH_MAP.put("K", "5");
		MONTH_MAP.put("M", "6");
		MONTH_MAP.put("N", "7");
		MONTH_MAP.put("Q", "8");
		MONTH_MAP.put("U", "9");
		MONTH_MAP.put("V", "10");
		MONTH_MAP.put("X", "11");
		MONTH_MAP.put("Z", "12");
	}
	
	public static final Map<String,int[]> VAR_MONTH_MAP = new HashMap<String,int[]>();
	static {
		//以下国外的期货品种，对于合约的编号是固定的，比如美玉米，1号合约就是指9月合约，2号就是指12月，依次类推
//		VAR_MONTH_MAP.put("ICE原糖", new int[]{9,10,1,3,5,7}); //152
//		VAR_MONTH_MAP.put("ICE棉花", new int[]{10,12,3,5,7});  //ICE棉花153
		VAR_MONTH_MAP.put("纽约原油", new int[]{8,9,10,11,12,1,2,3,4,5,6,7}); //纽约原油
//		VAR_MONTH_MAP.put("芝加哥美豆", new int[]{8,9,10,11,1,3,5,7});  //芝加哥美豆
//		VAR_MONTH_MAP.put("芝加哥美豆粕", new int[]{8,9,10,11,1,3,5,7});  //芝加哥美豆粕
//		VAR_MONTH_MAP.put("芝加哥美豆油", new int[]{8,9,10,1,3,5,7});  //芝加哥美豆油
		VAR_MONTH_MAP.put("芝加哥美玉米", new int[]{9,12,3,5,7});  //芝加哥美玉米
		VAR_MONTH_MAP.put("芝加哥美麦", new int[]{9,12,3,5,7});  //芝加哥美麦
		VAR_MONTH_MAP.put("马来西亚棕榈油", new int[]{9,11,1,2,3,4,5,6,7});  //马来西亚棕榈油
	}
	
	private int timeint;
	
	public MarketCrawlerForeign(){
		this.timeint = CrawlerUtil.yesterdayTimeint();
	}
	
	public  MarketCrawlerForeign(int timeint){
		this.timeint = timeint;
	}
	
	public List<MarketPrice> fetch (String varname, String url) {
		
		int varid = Variety.getVaridByName(varname);
		LOG.info("fetch " + varname +","+ varid + "@" + url);
		
		List<MarketPrice> prices = new ArrayList<MarketPrice>();
		String body = CrawlerUtil.httpGetBody(url);
		if (body == null || body.equals("")) {
			LOG.error("无法获取行情");
			return prices;
		}		
		fetchUtil.saveHtml(url, varname, body);
		if(varname.equals("ICE原糖---")){
			body = CrawlerUtil.findStrBetween(body, "Click for<br>Chart</div>", "Times indicate exchange local time.");
			String[] tmp = body.split("<tr class=\"qdata\">");
			if(tmp.length<1){
				System.out.println("ICE数据错误");
				return null;
			}
			String txtcontent;
			int n = 0;
			for(int i=1;i<tmp.length&&n<4;i++){
				txtcontent = tmp[i].replaceAll("</td>|&nbsp;", "\t");
				txtcontent = txtcontent.replaceAll("</?[^>]+>", ""); //剔出<html>的标签  
		        //txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>|&nbsp;", "");//去除字符串中的空格,回车,换行符,制表符  
		        txtcontent = txtcontent.replaceAll("\n*\n", "\n");//去除字符串中的空格,回车,换行符,制表符  
		        txtcontent = txtcontent.replaceAll("\t*\t", "\t").trim();
		        if(txtcontent.length()<10){
		        	continue;
		        }		        
		        //通过values[0]判断合约
		        String month;
		        if("Oct".equals(txtcontent.substring(0,3))){
		        	month = "10";
		        }else if("Mar".equals(txtcontent.substring(0,3))){
		        	month = "03";		        	
		        }else if("May".equals(txtcontent.substring(0,3))){
		        	month = "05";		        	
		        }else if("Jul".equals(txtcontent.substring(0,3))){
		        	month = "07";		        	
		        }else{
		        	continue;
		        }
		        String table = String.format(TABLE_MONTH_FORMAT, varname, month);
				if (table == null ) continue;
				addList(prices, parse(txtcontent, table, varid,2));
				n++;
			}
			return prices;
		}	
		
		String[] lines = CrawlerUtil.findStrBetween(body, "[[", "]]").split("\\],\\[");
		//A 主力连续    F1月连续  H3月连续  K5月连续   N7月连续  U9月连续  V10月连续		
		/*示例：[['NYSBA','\u7F8E11\u53F7\u539F\u7CD6\u8FDE\u7EED',17.74,-0.100000000000001,17.73,37,17.74
		,41,3281,17.84,17.84,17.85,17.73,0],['NYSBF','\u7F8E11\u53F7\u539F\u7CD61\u6708\u5408\u7EA6',
		0,0,0,0,0,0,0,0,0,0,0,0],['NYSBH','\u7F8E11\u53F7\u539F\u7CD63\u6708\u5408\u7EA6',19.12,
		-0.0999999999999979,19.11,10,19.16,24,963,19.21,19.22,19.22,19.12,0],['NYSBK','\u7F8E11\u53F7
		\u539F\u7CD65\u6708\u5408\u7EA6',19.25,0.0399999999999991,19.16,31,19.18,2,86,19.25,19.21,19.26,
		19.25,0],['NYSBN','\u7F8E11\u53F7\u539F\u7CD67\u6708\u5408\u7EA6',0,0,19.14,10,19.17,2,72,0,
		19.24,0,0,0],['NYSBU','\u7F8E11\u53F7\u539F\u7CD69\u6708\u5408\u7EA6',0,0,0,0,0,0,0,0,0,0,0,0],
		['NYSBV','\u7F8E11\u53F7\u539F\u7CD610\u6708\u5408\u7EA6',17.74,-0.100000000000001,17.73,37,
		17.74,41,3261,17.84,17.84,17.85,17.73,0]] */
		for (String line : lines){//新加if判断，为解决java.lang.StringIndexOutOfBoundsException
			//示例：'NYSBA','\u7F8E11\u53F7\u539F\u7CD6\u8FDE\u7EED',17.74,-0.100000000000001,17.73,37,17.74
			//,41,3281,17.84,17.84,17.85,17.73,0
			if (line.split("'?,'?")[0].length() > 1) {
				String code = line.split("'?,'?")[0].substring(1);//
				String table = getTableName(varname, code);
				//table 示例：期货行情数据-ICE棉花-主力连续, 主力连续不是我们计算的，而是对方直接给出来的
				if (table == null ) continue;
				
				addList(prices, parse(line, table, varid,1));
			}
		}
		
		
		return prices;
	}
	
	@Scheduled
	(cron = CrawlScheduler.CRON_US)
	//抓取美国的数据，包括ICE，纽交所，芝加哥的数据等
	public void start() throws JobUSException{
		String switchFlag = new CrawlerManager().selectCrawler("美国各品种期货数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到美国各品种期货数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				doFetchAndSaveUS();
			}else{
				LOG.info("抓取美国各品种期货数据的定时器已关闭");
			}
		}
	}
	public void doFetchAndSaveUS() throws JobUSException{
		PriceCalc calc = new PriceCalc();
		List<MarketPrice> all = new ArrayList<MarketPrice>(0);
		for (String varname : URL_MAP.keySet()){
			try {
				String url = URL_MAP.get(varname);//提取URL
				List<MarketPrice> piece = fetch(varname, url);//从URL获取原始数据
				if(piece.size() > 0){
					all.addAll(piece);
					MarketPrice priceIndex = calc.calcIndex(piece);//计算指数连续
					all.add(calc.calcMajor(piece));//计算主力连续
					all.add(priceIndex);
				}else{
					LOG.error(varname+"没有数据可以保存");
				}
			} catch (Exception e){
				LOG.error("market crawler error", e);
				throw new JobUSException("JobUS runtime error");
			}
			
		}
		dao.dbm3SaveByDbName(all);
	}
	
	//@Scheduled
	//(cron = CrawlScheduler.CRON_ASIA) 这个地方的抓取已经单独实现了，此处不再需要
	public void doFetchAndSaveAsia() throws JobASIAException{
		PriceCalc calc = new PriceCalc();
		List<MarketPrice> all = new ArrayList<MarketPrice>(0);
		for (String varname : URL_MAP_ASIA.keySet()){
			try {
				String url = URL_MAP_ASIA.get(varname);
				List<MarketPrice> piece = fetch(varname, url);
				if(piece.size() >0 ){
					piece.add(calc.calcIndex(piece));
					piece.add(calc.calcMajor(piece));
					all.addAll(piece);
				}
			} catch (Exception e) {
				throw new JobASIAException("JobASIA runtime error");
			}

		}
		dao.dbm3SaveByDbName(all);
	}

	@Scheduled
	(cron = CrawlScheduler.CRON_US_FH)
	public void startHour() throws JobUSException{
		String switchFlag = new CrawlerManager().selectCrawler("美国各品种期货数据(每小时一次)", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到美国各品种期货数据(每小时一次)在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				doFetchAndSaveUSForHour();
				LOG.info("抓取美国各品种期货数据爬虫执行完毕");
			}else{
				LOG.info("抓取美国各品种期货数据(每小时一次)的定时器已关闭");
			}
		}
	}
	public void doFetchAndSaveUSForHour() throws JobUSException{
		LOG.info("jobus for hour running");
		PriceCalc calc = new PriceCalc();
		List<MarketPrice> all = new ArrayList<MarketPrice>(0);
		for (String varname : URL_MAP.keySet()){
			try {
				String url = URL_MAP.get(varname);//提取URL
				List<MarketPrice> piece = fetch(varname, url);//从URL获取原始数据
				if(piece.size() > 0){
					piece.add(calc.calcIndex(piece));
					piece.add(calc.calcMajor(piece));
					all.addAll(piece);
				}
			} catch (Exception e){
				LOG.error("market crawler error", e);
				throw new JobUSException("JobUS runtime error");
			}
			
		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		for(MarketPrice marketPrice:all){
			marketPrice.setTable(marketPrice.getTable()+"FH");
			//精准到小时
			marketPrice.setTimeint(100*marketPrice.getTimeint()+hour);
		}
		//用dbm3向数据库中存入数据
		dao.dbm3SaveByDbName(all);
	}
	
//	@Scheduled
//	(cron = CrawlScheduler.CRON_ASIA_FH)
	public void doFetchAndSaveAsiaForHour() throws JobASIAException{
		PriceCalc calc = new PriceCalc();
		List<MarketPrice> all = new ArrayList<MarketPrice>(0);
		for (String varname : URL_MAP_ASIA.keySet()){
			try {
				String url = URL_MAP_ASIA.get(varname);
				List<MarketPrice> piece = fetch(varname, url);
				piece.add(calc.calcIndex(piece));
				all.addAll(piece);
			} catch (Exception e) {
				throw new JobASIAException("JobASIA runtime error");
			}

		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		for(MarketPrice marketPrice:all){
			marketPrice.setTable(marketPrice.getTable()+"FH");
			//精准到小时
			marketPrice.setTimeint(100*marketPrice.getTimeint()+hour);
		}
		dao.dbm3SaveByDbName(all);
	}
	/***********/
	public static final String TABLE_MONTH_FORMAT = "期货行情数据-%s-%s连续";
	private String getTableName(String varname, String code){
		String monthCode = code.substring(code.length()-1); 
		String month = MONTH_MAP.get(monthCode);
		String monthNumberStr = null;
		if ("主力".equals(month)){
			monthNumberStr = month;
		} else {
//			int monthInt = Integer.parseInt(month);
//			int monthNumber = matchTableNumber(monthInt, varname);//这个函数不再被用到
//			if (monthNumber == -1){
//				return null;
//			}
//			monthNumberStr = String.valueOf(monthNumber) + "号";
			monthNumberStr = month+"月";/* 在原先的计算方式中，N号连续的编号通过前面VAR_MONTH_MAP中的数组顺序来确定，因此后来直接改成N月连续，避免意义混乱*/
		}
		
		String cnname = String.format(TABLE_MONTH_FORMAT, varname, monthNumberStr);
		return cnname;
	}
	
	private MarketPrice parse(String line, String table, int varid,int type){
		String dbTable = ExcelSchema.getTableByName(table);
		LOG.info("read[" + table + "]["+varid+"]:"+line);
		MarketPrice p = new MarketPrice(dbTable, varid);
		double open,high,low,last,position,volume;
		int timeint;
		String[] splits ;
		try {
			if(1==type)	{
				splits = line.split(",");
				open = Double.parseDouble(splits[9]);
				high = Double.parseDouble(splits[11]);
				low = Double.parseDouble(splits[12]);
				last = Double.parseDouble(splits[2]);
				position = Double.parseDouble(splits[13]);
				volume = Double.parseDouble(splits[8]);
			}else{
				splits = line.split("\t");
				open = Double.parseDouble(splits[1]);
				high = Double.parseDouble(splits[2]);
				low = Double.parseDouble(splits[3]);
				last = Double.parseDouble(splits[4]);
				position = 0;
				volume = Double.parseDouble(splits[8]);							
			}
			timeint = getTimeint();
			String code = splits[0];
			p.setCode(code);
			p.setOpen(open);
			p.setHigh(high);
			p.setLast(last);
			p.setLow(low);
//			p.setSettle(settle);
			p.setPosition(position);
			p.setVolume(volume);
//			p.setTurnover(turnover);
			p.setTimeint(timeint);
			
		} catch (Exception e){
			LOG.warn("数据行解析异常"+line);
			LOG.error(e);
			return null;
		}
		
		LOG.info("parsed: " + p);
		return p;
	}
	
	private int getTimeint(){
		//return timeint;
		return  CrawlerUtil.yesterdayTimeint();
	}
	
	private void addList(List<MarketPrice> list, MarketPrice p){
		if (p == null) {
			return;
		}
		if (CrawlerUtil.isEmpty(p)){
			LOG.info("skip empty");
			return;
		}
		list.add(p);
	}
	
	/**
	 * N月合约->N号合约
	 * @param month
	 * @return
	 */
	private int matchTableNumber(int month, String varname) {
//		Calendar now = Calendar.getInstance();
//		Calendar today = new GregorianCalendar(now.get(Calendar.YEAR), 
//				now.get(Calendar.MONTH)+1, now.get(Calendar.DAY_OF_MONTH));
		int[] monthArray = VAR_MONTH_MAP.get(varname);
		int idx = -1;
		for (int i = 0; i < monthArray.length; i++){
			if (monthArray[i] == month){
				idx = i+1;
				break;
			} 
		}
		return idx;
	}
	
	public static void main(String[] argvs) throws Exception{
		new MarketCrawlerForeign().doFetchAndSaveUS();
//		new MarketCrawlerForeign().doFetchAndSaveAsia();
	}
	
	
}
