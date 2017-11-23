package cn.futures.data.importor.crawler.futuresMarket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.CrawlerUtil;
import cn.futures.data.util.DateTimeUtil;

public class DLPriceFetch {

	/**
	 * The URL to fetch DCE data, here we use the parameter 'all' for all DCE
	 * varieties. If detailed varieties needed, a loop is necessary which
	 * contains lines as follow:
	 */
	private static String urlBase = "http://www.dce.com.cn/PublicWeb/MainServlet?action=Pu00011_result"
			+ "&Pu00011_Input.trade_date=%timeint%&Pu00011_Input.variety=%variety%"
			+ "&Pu00011_Input.trade_type=0&Submit=%B2%E9+%D1%AF";
	private static String urlZZBase = "http://www.czce.com.cn/portal/exchange/%year%/datadaily/%timeint%%variety%.htm";
	private static int MAX_RETRY = 3;
	public static final Logger LOG = Logger.getLogger(DLPriceFetch.class);

	static String[][] tablesdou = {
			{ null, "CX_MarketData_1",// 指数连续
					"CX_MarketData_2"// 主力连续是表
			},
			{
					null, // N0
					"CX_MarketData_3", // N1
					null, // N2
					"CX_MarketData_4", // N3
					null, // N4
					"CX_MarketData_5", // N5
					null, // .....
					"CX_MarketData_6", null, 
					"CX_MarketData_7", null,
					"CX_MarketData_8", null },
			{
					null, // (N+1)0
					"CX_MarketData_9", // (N+1)1
					null, // (N+1)2
					"CX_MarketData_10", // (N+1)3
					null, // .....
					"CX_MarketData_11", null, "CX_MarketData_12", null,
					"CX_MarketData_13", null, "CX_MarketData_14", null },
			{ null, // (N+2)0
					null, // (N+2)1
					null, null, null } };

	static String[][] tablesjidan = {
			{ null, "CX_MarketData_1",// 指数连续
					"CX_MarketData_2"// 主力连续是表
			},
			{
					null, // N0
					"CX_MarketData_3",
					"CX_MarketData_4",
					"CX_MarketData_5",
					"CX_MarketData_6",
					"CX_MarketData_7",
					"CX_MarketData_8",
					"CX_MarketData_9",
					"CX_MarketData_10",
					"CX_MarketData_11",
					"CX_MarketData_12",
					"CX_MarketData_13",
					"CX_MarketData_14"
			},
			{
					null, // (N+1)0,备用
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null
			} };
	static String[][] tablesdianfen = {
		{ null, "CX_MarketData_1",// 指数连续
				"CX_MarketData_2"// 主力连续是表
		},
		{
				null, // N0
				"CX_MarketData_6",
				null,
				"CX_MarketData_3",
				null,
				"CX_MarketData_4",
				null,
				"CX_MarketData_7",
				null,
				"CX_MarketData_5",
				null,
				"CX_MarketData_8",
				null
		},
		{
				null, // (N+1)0
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null
		} };
	
	public class VandT {
		String name;
		String variety;
		String table[][];
	}

	public static VandT douzi, yumi, dianfen, jidan, doupo, zonglvyou, douyou;

	DLPriceFetch() {
		douzi = new VandT();
		douzi.name = "豆一";
		douzi.variety = "144";
		douzi.table = tablesdou;

		yumi = new VandT();
		yumi.name = "玉米";
		yumi.variety = "148";
		yumi.table = tablesdou;

		dianfen = new VandT();
		dianfen.name = "玉米淀粉";
		dianfen.variety = "534";
		dianfen.table = tablesdianfen;

		jidan = new VandT();
		jidan.name = "鸡蛋";
		jidan.variety = "303";
		jidan.table = tablesjidan;

		doupo = new VandT();
		doupo.name = "豆粕";
		doupo.variety = "145";
		doupo.table = tablesdou;

		zonglvyou = new VandT();
		zonglvyou.name = "棕榈油";
		zonglvyou.variety = "147";
		zonglvyou.table = tablesjidan;

		douyou = new VandT();
		douyou.name = "豆油";
		douyou.variety = "146";
		douyou.table = tablesdou;
		
		vMap.put("豆一", douzi);
		vMap.put("玉米", yumi);
		vMap.put("玉米淀粉", dianfen);
		vMap.put("鸡蛋", jidan);
		vMap.put("豆粕", doupo);
		vMap.put("棕榈油", zonglvyou);
		vMap.put("豆油", douyou);		
	}
	public static HashMap<String, VandT> vMap = new HashMap<String, VandT>();

	/**
	 * download the target website and get data from the inner table.
	 * 
	 * @param timeint
	 * @param variety
	 *            , see the follow Maps <option value="a">豆一</option> 144
	 *            <option value="b">豆二</option> <option value="bb">胶合板</option>
	 *            <option value="c">玉米</option> 148 <option
	 *            value="cs">玉米淀粉</option>534 <option value="fb">纤维板</option>
	 *            <option value="i">铁矿石</option> <option value="j">焦炭</option>
	 *            <option value="jd">鸡蛋</option>303 <option
	 *            value="jm">焦煤</option> <option value="l">聚乙烯</option> <option
	 *            value="m">豆粕</option>145 <option value="p">棕榈油</option>147
	 *            <option value="pp">聚丙烯</option> <option
	 *            value="v">聚氯乙烯</option> <option value="y">豆油</option>146
	 *            <option value="s">大豆</option>
	 * @return
	 */
	public List<List<String>> fetchDataDL(int timeint, String variety) {
		String url = urlBase.replace("%timeint%", String.valueOf(timeint))
				.replace("%variety%", variety);
		// get target website
		Document page = CrawlerUtil.httpGetDoc(url);
		System.out.println("GET URL finished:" + url);

		// get target table
		Elements table = page.getElementsByAttributeValueContaining("class","table");
		if (table == null || table.size() < 1) {
			System.out.println("Can not find the formatted table.");
			return null;
		}
		Element e = table.first();

		// get all rows in table
		Elements trs = e.getElementsByTag("tr");
		if (trs == null || trs.size() < 1) {
			System.out.println("Table Empty.");
			return null;
		}

		// get table headers
		List<String> headers = new ArrayList<String>();
		Elements ths = trs.first().getElementsByTag("td");
		if (ths == null || ths.size() < 1) {
			System.out.println("Row error <no td flag> with: " + ths.html());
			return null;
		}
		for (Element th : ths) {
			headers.add(th.text().trim());
		}
		List<List<String>> datas = new ArrayList<List<String>>();
		datas.add(headers);

		// get all data
		for (Element tr : trs) {
			Elements tds = tr.getElementsByTag("td");
			if (tds == null || tds.size() < 1) {
				System.out.println("Row error <no td flag> with: " + tr.html());
				continue;
			}
			if (!vMap.containsKey(tds.first().text().trim())) {
				//System.out.println("无效行");
				continue;
			}
			List<String> rowdata = new ArrayList<String>();
			for (Element td : tds) {
				rowdata.add(td.text().replaceAll(",| ", ""));
			}
			datas.add(rowdata);
		}
		System.out.println("datas size(): "+datas.size());
		return datas;
	}

	/**
	 * save data, translate to SQLs the first row is headers, the rest rows
	 * contains data
	 * 
	 * @param datas
	 */
	public List<String> transSQLDL(int timeint, List<List<String>> datas) {
		if (datas == null || datas.size() < 2)
			return null;
		// 商品名称 交割月份 开盘价 最高价 最低价 收盘价 前结算价 结算价 涨跌 涨跌1 成交量 持仓量 持仓量变化 成交额
		List<String> sqls = new ArrayList<String>();
		String lastVariety = null;
		String temp, code;
		List<MarketPrice> list = new ArrayList<MarketPrice>();
		for (int i = 1; i < datas.size(); i++) {
			MarketPrice p = new MarketPrice();
			temp = datas.get(i).get(0);// @VarietyName
			code = datas.get(i).get(1);
			if (temp == null || !vMap.containsKey(temp) || code == null
					|| temp.length() < 1 || code.length() < 1) {
				continue;
			}
			p.setVarid(Integer.valueOf(vMap.get(temp).variety));
			p.setTimeint(timeint);
			if (isDouble(datas.get(i).get(2)))
				p.setOpen(Double.parseDouble(datas.get(i).get(2)));
			if (isDouble(datas.get(i).get(3)))
				p.setHigh(Double.parseDouble(datas.get(i).get(3)));
			if (isDouble(datas.get(i).get(4)))
				p.setLow(Double.parseDouble(datas.get(i).get(4)));
			if (isDouble(datas.get(i).get(5)))
				p.setClose(Double.parseDouble(datas.get(i).get(5)));
			if (isDouble(datas.get(i).get(7)))
				p.setPrice(Double.parseDouble(datas.get(i).get(7)));
			if (isDouble(datas.get(i).get(10)))
				p.setTotalAmount(Double.parseDouble(datas.get(i).get(10)));
			if (isDouble(datas.get(i).get(11)))
				p.setHold(Double.parseDouble(datas.get(i).get(11)));
			if (isDouble(datas.get(i).get(13)))
				p.setTotalMoney(Double.parseDouble(datas.get(i).get(13))/100.0f);//布瑞克数据库中以百万元为成交额的单位
			p.setCode(datas.get(i).get(1));

			if (lastVariety != null && !lastVariety.equals(datas.get(i).get(0))) {
				sqls.addAll(processWithVar(timeint, vMap.get(lastVariety), list));
				list.clear();
				/*
				 * //处理majorPrice和indexPrice pindex.setOpen(pindex.volume == 0 ?
				 * 0: pindex.open/pindex.volume); pindex.setHigh(pindex.volume
				 * == 0 ? 0: pindex.high/pindex.volume);
				 * pindex.setLow(pindex.volume == 0 ? 0:
				 * pindex.low/pindex.volume); pindex.setLast(pindex.volume == 0
				 * ? 0: pindex.last/pindex.volume);
				 * pindex.setSettle(pindex.volume == 0 ? 0:
				 * pindex.settle/pindex.volume);
				 * 
				 * //generate sql sqls.add(pindex.toSql(tablename,
				 * vMap.get(lastVariety), timeint));
				 * sqls.add(majorPrice.toSql(tablename, vMap.get(lastVariety),
				 * timeint)); majorPrice.reset(); pindex.reset();
				 */
			}
			lastVariety = datas.get(i).get(0);
			list.add(p);

			/*
			 * //统计主力连续 if(majorIndex<0 || majorPrice==null ||
			 * majorPrice.getPosition()<p.getPosition()){ majorPrice =
			 * p.clone(); } //统计指数连续 //持仓量，成交量，成交额 直接累加 pindex.volume +=
			 * p.volume; pindex.position += p.position; pindex.turnover +=
			 * p.turnover; // 价格按成交量加权 pindex.open += p.open*p.volume;
			 * pindex.high += p.high*p.volume; pindex.low += p.low*p.volume;
			 * pindex.last += p.last*p.volume; pindex.settle +=
			 * p.settle*p.volume;
			 * 
			 * sqls.add(p.toSql(tablename, vMap.get(lastVariety), timeint));
			 */
		}
		sqls.addAll(processWithVar(timeint, vMap.get(lastVariety), list));
		return sqls;
	}

	public List<String> processWithVar(int timeint, VandT variety,
			List<MarketPrice> list) {
		if (timeint == 0 || variety == null || list == null || list.size() < 1)
			return null;
		// year %= 10;

		List<String> sqls = new ArrayList<String>();
		// list是本天本品种的所有合约的报价信息，需要处理
		// 还要计算指数连续和主力连续
		MarketPrice p = null;
		MarketPrice pindex = new MarketPrice();
		MarketPrice pmax = new MarketPrice();
		int maxI = -1;

		for (int i = 0; i < list.size(); i++) {
			p = list.get(i);
			int codei = Integer.valueOf(p.getCode());
			boolean hassame = false;
			for (int j = 0; j < list.size(); j++) {
				int codej = Integer.valueOf(list.get(j).getCode());
				if (i != j && codei % 100 == codej % 100) {
					if (codei / 100 < codej / 100) {// 把i的写到N表
						if(variety.table[1][codei % 100] != null){
							sqls.add(p.toSql(variety.table[1][codei % 100], variety.variety,
									String.valueOf(timeint)));
						}
					} else {// 把I的写到N+1表
						if(variety.table[2][codei % 100] != null ){
							sqls.add(p.toSql(variety.table[2][codei % 100], variety.variety,
									String.valueOf(timeint)));
						}
					}
					hassame = true;
					break;// 最多出现一个重复
				}
			}
			if (!hassame) {
				// 把I写到N和N+1表
				if(variety.table[1][codei % 100] !=null){
					sqls.add(p.toSql(variety.table[1][codei % 100], variety.variety,
							String.valueOf(timeint)));
				}
				if(variety.table[2][codei % 100] != null){
					sqls.add(p.toSql(variety.table[2][codei % 100], variety.variety,
							String.valueOf(timeint)));
				}
			}

			// 统计指数连续
			// 持仓量，成交量，成交额 直接累加
			pindex.volume += p.volume;
			pindex.position += p.position;
			pindex.turnover += p.turnover;
			// 价格按成交量加权
			pindex.open += p.open * p.volume;
			pindex.high += p.high * p.volume;
			pindex.low += p.low * p.volume;
			pindex.last += p.last * p.volume;
			pindex.settle += p.settle * p.volume;

			// 统计主力连续
			if (maxI < 0 || list.get(maxI).position < p.position) {
				maxI = i;
			}
		}
		// 计算指数连续
		pindex.open = pindex.volume == 0 ? 0 : pindex.open / pindex.volume;
		pindex.high = pindex.volume == 0 ? 0 : pindex.high / pindex.volume;
		pindex.low = pindex.volume == 0 ? 0 : pindex.low / pindex.volume;
		pindex.last = pindex.volume == 0 ? 0 : pindex.last / pindex.volume;
		pindex.settle = pindex.volume == 0 ? 0 : pindex.settle / pindex.volume;

		if(variety.table[0][1] != null){
			sqls.add(pindex.toSql(variety.table[0][1], variety.variety, String.valueOf(timeint)));
		}
		// 计算主力连续
		pmax = list.get(maxI);
		if( variety.table[0][2] !=null){
			sqls.add(pmax.toSql(variety.table[0][2], variety.variety, String.valueOf(timeint)));
		}
		return sqls;
	}

	boolean isDouble(String str) {
		if(str == null)
			return false;
		str = str.replace(",", "");
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ex) {
		}
		return false;
	}

	public static void saveSQL2(List<String> sqls) {
		if (sqls == null || sqls.size() < 1)
			return;
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.save2(sqls);
		} catch (Exception e) {
			LOG.error("Error when excuting sqls", e);
			try {
				jdbc.rollTransaction();
				LOG.error("Rollback sqls", e);
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error", e1);
			} finally {
				jdbc.release();
			}
		} finally {
			jdbc.release();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub  1124---0111
		DLPriceFetch dlp = new DLPriceFetch();
		Date dateStart = DateTimeUtil.parseDateTime("20160310", "yyyyMMdd");
		Date dateEnd = DateTimeUtil.parseDateTime("20160316", "yyyyMMdd");
        while(dateStart.compareTo(dateEnd) <= 0) {   	
        	int timeint = Integer.valueOf(DateTimeUtil.formatDate(dateStart, "yyyyMMdd"));//CrawlerUtil.todayTimeint();
			List<List<String>> datas = dlp.fetchDataDL(timeint, "all");
			if (datas != null) {
				List<String> sqls = dlp.transSQLDL(timeint, datas);
				saveSQL2(sqls);
			}
            dateStart = DateTimeUtil.addDay(dateStart, 1);
            try{
            	Thread.sleep(3000);
            }catch(Exception e){
            	
            }
        }
	}

}
