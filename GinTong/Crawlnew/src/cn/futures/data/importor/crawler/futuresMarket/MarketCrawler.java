package cn.futures.data.importor.crawler.futuresMarket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.futures.IdenExceptions.JobCNException;
import cn.futures.data.DAO.MarketPriceDAO;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.ExcelSchema;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.MyHttpClient;

@Component
public class MarketCrawler {
	private static final String className = MarketCrawler.class.getName();
	public static final Log LOG = LogFactory.getLog(MarketCrawler.class);
	private MyHttpClient httpClient = new MyHttpClient();
	public static MarketPriceDAO dao = new MarketPriceDAO();
	public static final Map<String,String> URL_FH_MAP = new HashMap<String,String>();
	static {
		URL_FH_MAP.put("郑州白糖", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=sr&market=3");	//139
		URL_FH_MAP.put("郑州棉花", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=cf&market=3");	//140 
		//URL_MAP.put("郑州菜籽油", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=ro&market=3");	//141
		URL_FH_MAP.put("郑州菜籽油", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=OI&market=3");	//141
		//URL_MAP.put("郑州早籼稻", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=er&market=3");	//142
		URL_FH_MAP.put("郑州早籼稻", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=RI&market=3");	//142
		//URL_MAP.put("郑州强麦", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=ws&market=3");		//143
		URL_FH_MAP.put("郑州强麦", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=wh&market=3");		//143
		URL_FH_MAP.put("大连豆一", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=a&market=2");		//144
		URL_FH_MAP.put("大连豆粕", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=m&market=2");		//145
		URL_FH_MAP.put("大连豆油", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=y&market=2");		//146
		URL_FH_MAP.put("大连玉米", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=c&market=2");		//148
		URL_FH_MAP.put("大连棕榈油", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=p&market=2");		//147
		URL_FH_MAP.put("上海橡胶","http://quote.futures.hexun.com//hqzxrestquote.aspx?type=ru&market=1");		//149
		URL_FH_MAP.put("上海铜", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=cu&market=1");		//150
		URL_FH_MAP.put("上海黄金", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=au&market=1");		//151
		//URL_MAP.put("郑州菜籽", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=rs&market=3");	//297
		URL_FH_MAP.put("郑州菜籽", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=RS&market=3");	//297
		//URL_MAP.put("郑州菜粕", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=rm&market=3");	//298
		URL_FH_MAP.put("郑州菜粕", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=RM&market=3");	//298
		URL_FH_MAP.put("大连鸡蛋", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=jd&market=2"); //303
		URL_FH_MAP.put("郑州粳稻", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=JR&market=3");	//304
		URL_FH_MAP.put("大连玉米淀粉", "http://quote.futures.hexun.com/hqzx/restquote.aspx?type=CS&market=2");	//534
	}
	public static final List<String>  URL123 = Arrays.asList(
			new String[]{"大连棕榈油", "上海橡胶", "上海铜", "上海黄金", "大连鸡蛋","郑州粳稻","郑州菜籽","郑州菜粕","大连玉米淀粉"});
	
	//下面定义了期货交易的交割单位，比如鸡蛋的一手是5吨，而白糖一手为10吨
	public static final Map<Integer, Float>  TURNOVER_FACTOR = new HashMap<Integer, Float>();
	static {
		TURNOVER_FACTOR.put(139, 10.0f);
		TURNOVER_FACTOR.put(140, 5.0f);
		TURNOVER_FACTOR.put(141, 5.0f);
		TURNOVER_FACTOR.put(142, 10.0f);
		TURNOVER_FACTOR.put(143, 10.0f);
		TURNOVER_FACTOR.put(144, 10.0f);
		TURNOVER_FACTOR.put(145, 10.0f);
		TURNOVER_FACTOR.put(146, 10.0f);
		TURNOVER_FACTOR.put(147, 10.0f);//大连棕榈油
		TURNOVER_FACTOR.put(148, 10.0f);
		TURNOVER_FACTOR.put(149, 5.0f);
		TURNOVER_FACTOR.put(150, 5.0f);
		TURNOVER_FACTOR.put(151, 1000.0f);
		TURNOVER_FACTOR.put(297, 10.0f);
		TURNOVER_FACTOR.put(298, 10.0f);
		TURNOVER_FACTOR.put(303, 5.0f);//大连鸡蛋的交易单位为5吨/手
		TURNOVER_FACTOR.put(304, 20.0f);//粳稻,20吨/手
		TURNOVER_FACTOR.put(534, 10.0f);//粳稻,20吨/手
		
	}
	
	//上面这个对应应该是无效了，实际上是在数据库的cx_variety表里面保存了对应关系，例如：
	// 大连棕榈油和大连玉米正好反了序号
	//Id	EditTime	VName
	//	139	2011-07-11 06:29:55.780	郑州白糖
	//	140	2011-07-11 06:29:55.780	郑州棉花
	//	141	2011-07-11 06:29:55.780	郑州菜籽油
	//	142	2011-07-11 06:29:55.780	郑州早籼稻
	//	143	2011-07-11 06:29:55.780	郑州强麦
	//	144	2011-07-11 06:29:55.780	大连豆一
	//	145	2011-07-11 06:29:55.793	大连豆粕
	//	146	2011-07-11 06:29:55.793	大连豆油
	//	147	2011-07-11 06:29:55.793	大连棕榈油
	//	148	2011-07-11 06:29:55.793	大连玉米
	//	149	2011-07-11 06:29:55.793	上海橡胶
	//	150	2011-07-11 06:29:55.793	上海铜
	//	151	2011-07-11 06:29:55.793	上海黄金
	//	152	2011-07-11 06:29:55.793	ICE原糖
	//	153	2011-07-11 06:29:55.793	ICE棉花
	//	154	2011-07-11 06:29:55.793	纽约原油
	//Id	EditTime	VName	industryId
	//139	2011-07-11 06:29:55.780	郑州白糖	5
	//140	2011-07-11 06:29:55.780	郑州棉花	5
	//141	2011-07-11 06:29:55.780	郑州菜籽油	5
	//142	2011-07-11 06:29:55.780	郑州早籼稻	5
	//143	2011-07-11 06:29:55.780	郑州强麦	5
	//297	2013-04-19 08:54:47.357	郑州菜籽	5
	//298	2013-04-19 08:55:07.250	郑州菜粕	5
	
	public static final String TABLE_MONTH_FORMAT = "期货行情数据-%s-%s%d月连续";
	
	private int timeint;
	
	public MarketCrawler(int timeint){
		this.timeint = timeint;
	}
	
	public MarketCrawler(){
		this.timeint = CrawlerUtil.todayTimeint();
	}
	public List<MarketPrice> fetchFH (String varname) {
		String url = URL_FH_MAP.get(varname);
		int varid = Variety.getVaridByName(varname);
		LOG.info("fetch " + varname + "@" + url);
		
		//String body = CrawlerUtil.httpGetBody(url);
		String body = httpClient.getResponseBody(url);
		if (body == null) {
			LOG.error("无法获取行情");
			return null;
		}
		// 每个行情代码为一行
		String[] lines = CrawlerUtil.findStrBetween(body, "[[", "]]").split("\\],\\[");
		int lineCount = lines.length;
		
		int[] codes = new int[lineCount];
		for (int i = 0; i < lineCount; i++){
			//'p1403','棕榈油1403',0,0,0,0,0,0,0,0,6066,0,0,0,0
			try {//新加捕捉异常语句，避免因为没有数据返回时导致的StringIndexOutOfBoundsException异常
				String codestr = lines[i].substring(0,lines[i].indexOf(","));
				codes[i] = Integer.parseInt(codestr.substring(codestr.length()-5,codestr.length()-1));//获取编码1403，太粗暴了应该用正则表达式获取
			} catch (StringIndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
				continue;
			}
		}
		List<MarketPrice> prices = new ArrayList<MarketPrice>();
		// 根据代码匹配表名
		for (int i = 0; i < lineCount; i++){
			if( varname.equals("大连棕榈油") &&  ! lines[i].contains("棕榈油")){
				LOG.info("Data error with no estimated strings ' " + varname + "' @" + url);
				continue;
			}
			int suffix = codes[i] % 100;//1403---->03
			String tableN = String.format(TABLE_MONTH_FORMAT, varname, "N", suffix);
			String tableN1 = String.format(TABLE_MONTH_FORMAT, varname, "(N+1)", suffix);
			String tableF = String.format(TABLE_MONTH_FORMAT, varname, "", suffix);
			boolean sameSuffix = false;
			// 1, 2, 3 .. 12 形式，不考虑N+1
			if (URL123.contains(varname)){
				addList(prices, parse(lines[i], tableF, varid));
				continue;
			} else {
				// 1,3,5 .. 11形式，考虑N+1
				for (int j = 0; j < lineCount; j++){
					// 两年的相同月份合约同时存在
					if (i != j && codes[j]%100 == suffix){
						sameSuffix = true;
						if (codes[i] < codes[j]){
							// 小年份的写在N表
							addList(prices, parse(lines[i], tableN, varid));
						} else {
							// 大年份的写在N+1表
							addList(prices, parse(lines[i], tableN1, varid));
						}
						
						break;
					}
				}
				// 一个月份的合约只存在一份，同时写入N和N+1表
				// N+1表的duplidated属性设为true， 防止计算指数、主力时的重复
				// Why?
				if (!sameSuffix){
					addList(prices, parse(lines[i], tableN, varid), false);
					addList(prices, parse(lines[i], tableN1, varid), true);
				}
			}
			
		}
		
		return prices;
	}
	private void addList(List<MarketPrice> list, MarketPrice p) {
		addList(list, p , false);
	}
	
	private void addList(List<MarketPrice> list, MarketPrice p, boolean isDuplicated){
		if (p == null) {
			return;
		}
		if (p.getOpen() ==0 && p.getHigh() == 0 && p.getLow() == 0 && p.getLast() == 0 && p.getSettle() == 0) {
			return;
		}
		p.setDuplicated(isDuplicated);
		list.add(p);
	}
	
	private MarketPrice parse(String line, String table, int varid){
		String dbTable = ExcelSchema.getTableByName(table);
		LOG.info("read[" + table + "]["+varid+"]:"+line);
		MarketPrice p = new MarketPrice(dbTable, varid);
		try {
			String[] splits = line.split(",");
			
			//line='RM1307','菜籽粕1307',2680,9,2675,5,2680,1,10,2671,2671,2680,2671,36,-4
			String code = splits[0]; // RM1307
			
			double open = Double.parseDouble(splits[9]); //2671
			double high = Double.parseDouble(splits[11]); //2680
			double low = Double.parseDouble(splits[12]); //2671
			double last = Double.parseDouble(splits[2]); //2680
			double settle = Double.parseDouble(splits[10]); //2671
			double position = Double.parseDouble(splits[13]); //36
			double volume = Double.parseDouble(splits[8]); //10
			double turnover = settle * volume / 1e6; //  2671*10/1000000
			
			// turnover factor
			float tnfactor = TURNOVER_FACTOR.get(varid);
			if (tnfactor > 0){
				turnover *= tnfactor;
			}
			
			int timeint = getTimeint();
			
			p.setOpen(open);
			p.setHigh(high);
			p.setLast(last);
			p.setLow(low);
			p.setSettle(settle);
			p.setPosition(position);
			p.setVolume(volume);
			p.setTurnover(turnover);
			p.setTimeint(timeint);
			p.setCode(code);
			
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
		return CrawlerUtil.todayTimeint();
//		return CrawlerUtil.parseTimeint("2011-12-09", "yyyy-MM-dd");
	}	

	@Scheduled
	(cron = CrawlScheduler.CRON_CN_FH)
	public void startHour() throws JobCNException{
		String switchFlag = new CrawlerManager().selectCrawler("中国各品种期货数据(每小时一次)", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到中国各品种期货数据(每小时一次)在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				doFetchAndSaveForHour();
			}else{
				LOG.info("抓取中国各品种期货数据(每小时一次)的定时器已关闭");
			}
		}
	}
	public void doFetchAndSaveForHour() throws JobCNException{
		PriceCalc calc = new PriceCalc();
		List<MarketPrice> all = new ArrayList<MarketPrice>(0);
		for (String varname : URL_FH_MAP.keySet()){
			try {
				List<MarketPrice> piece = fetchFH(varname);
				MarketPrice priceIndex = calc.calcIndex(piece);
				MarketPrice priceMajor = calc.calcMajor(piece);
				piece.add(priceIndex);
				piece.add(priceMajor);
				all.addAll(piece);
			} catch (Exception e){
				LOG.error("market crawler error", e);
				throw new JobCNException("JobCNForHour runtime error");
			}
			
		}
		dao.saveForHour(all);
	}
	public static void main(String argv[]){
		try{
			new MarketCrawler().startHour();
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			AMF3Serializer l = new AMF3Serializer(bos); 
//			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
}
