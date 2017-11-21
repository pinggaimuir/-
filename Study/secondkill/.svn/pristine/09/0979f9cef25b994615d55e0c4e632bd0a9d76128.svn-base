package cn.futures.data.DAO;

import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;
import cn.futures.data.jdbc.JdbcRunner;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketPriceDAO {
	
	public static final Logger LOG = Logger.getLogger(MarketPriceDAO.class);

	private final Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();

	public static final String[] TABLES = {"CX_MarketData_1", "CX_MarketData_2", "CX_MarketData_3", "CX_MarketData_4", 
		"CX_MarketData_5", "CX_MarketData_6", "CX_MarketData_7", "CX_MarketData_8", 
		"CX_MarketData_9", "CX_MarketData_10", "CX_MarketData_11", "CX_MarketData_12", 
		"CX_MarketData_13", "CX_MarketData_14"};
	public static final String[] TABLE_NAMES = {
		"指数连续", "主力连续", "N1月连续", "N3月连续", "N5月连续"
		, "N7月连续", "N9月连续", "N11月连续", "(N+1)1月连续"
		, "(N+1)3月连续", "(N+1)5月连续", "(N+1)7月连续"
		, "(N+1)9月连续", "(N+1)11月连续" 
	};

	/**
	 * 请求dbm3接口，存储数据
	 * @param priceList
	 */
	public void dbm3SaveByDbName(List<MarketPrice> priceList){
		for(MarketPrice mp:priceList) {
			List list = new ArrayList<>();
			Map<String, String> map = new HashMap<>();
			Map data = new HashMap<>();
			map.put("TimeInt", mp.getTimeint() + "");
			map.put("开盘价", mp.getOpen() + "");
			map.put("最高价", mp.getHigh() + "");
			map.put("最低价", mp.getLow() + "");
			map.put("收盘价", mp.getLast() + "");
			map.put("结算价", mp.getSettle() + "");
			map.put("持仓量", mp.getPosition() + "");
			map.put("成交额", mp.getTurnover() + "");
			map.put("成交量", mp.getVolume() + "");
			if(null!=mp.getCode()) {
				map.put("code", mp.getCode() + "");
			}
			map.put("VarId",mp.getVarid()+"");
			list.add(map);
			data.put("dbName",mp.getTable());
			data.put("data", list);
			dbm3Dao.saveByDbName(data);
			LOG.info("完成向表："+mp.getTable()+"   varId="+mp.getVarid()+"  内存入数据");
		}
	}
	

	public List<MarketPrice> list(String table){
		
		String sql = "select * from " + table
				+ " order by edittime desc, timeint desc, varid asc limit 50";
		List<MarketPrice> list = new ArrayList<MarketPrice>();
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			ResultSet rs = jdbc.query(sql);
			while (rs.next()){
				MarketPrice p = new MarketPrice();
				p.setTimeint(rs.getInt("timeint"));
				p.setVarid(rs.getInt("varid"));
				p.setEditTime(rs.getTimestamp("edittime"));
				p.setOpen(rs.getFloat("开盘价"));
				p.setHigh(rs.getFloat("最高价"));
				p.setLow(rs.getFloat("最低价"));
				p.setLast(rs.getFloat("收盘价"));
				p.setSettle(rs.getFloat("结算价"));
				p.setPosition(rs.getFloat("持仓量"));
				p.setVolume(rs.getFloat("成交量"));
				p.setTurnover(rs.getFloat("成交额"));
				
				list.add(p);
			}
				
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}
		
		return list;
	}

//	public void save(List<MarketPrice> list){
//		// aggregate by table
//		Map<String, List<MarketPrice>> tableMap = new HashMap<String, List<MarketPrice>>();
//		for (MarketPrice p : list){
//			if (p == null || p.getTable() == null) continue;
//
//			List<MarketPrice> slice = tableMap.get(p.getTable());
//			if (slice == null){
//				slice = new ArrayList<MarketPrice>();
//			}
//			slice.add(p);
//			tableMap.put(p.getTable(), slice);
//		}
//		// do batch
//		for (String table : tableMap.keySet()){
//			save(tableMap.get(table), table);
//			LOG.info("完成向表："+table+" 内存入数据");
//		}
//	}

//
//	private void save(List<MarketPrice> list, String table) {
//		if(list == null || list.size() <1 )
//			return;
//		String sql = "insert into " + table + " (edittime, varid, timeint, 开盘价, 最高价, 最低价, 收盘价, 结算价, 持仓量, 成交额, 成交量, code) " +
//				" values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		Object[][] vals = new Object[list.size()][11];
//		for (int i = 0; i < list.size(); i++){
//			MarketPrice p = list.get(i);
//			vals[i][0] = p.getVarid();
//			vals[i][1] = p.getTimeint();
//			vals[i][2] = p.getOpen();
//			vals[i][3] = p.getHigh();
//			vals[i][4] = p.getLow();
//			vals[i][5] = p.getLast();
//			vals[i][6] = p.getSettle();
//			vals[i][7] = p.getPosition();
//			vals[i][8] = p.getTurnover();
//			vals[i][9] = p.getVolume();
//			vals[i][10] = p.getCode();
//		}
//
//		JdbcRunner jdbc = null;
//		try {
//			jdbc = new JdbcRunner();
//			jdbc.beginTransaction();
//			jdbc.batchUpdate2(sql, vals);
//			jdbc.endTransaction();
//			LOG.info("table:" + table + "---varId" + vals[0][0]);
//			sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like '"+table+"' and varid="+vals[0][0];
//			jdbc.update(sql);
//		} catch (Exception e){
//			LOG.error("insert data into DB error",e);
//			try {
//				jdbc.rollTransaction();
//			} catch (SQLException e1) {
//				LOG.error("DB Transaction rollback error",e1);
//			} finally {
//				jdbc.release();
//			}
//		} finally {
//			jdbc.release();
//		}
//
//		LOG.info(table + ":" +list.size() + " prices saved ");
//	}
//
//	public void saveForHour(List<MarketPrice> list){
//		// aggregate by table
//		Map<String, List<MarketPrice>> tableMap = new HashMap<String, List<MarketPrice>>();
//		for (MarketPrice p : list){
//			if (p == null || p.getTable() == null) continue;
//
//			List<MarketPrice> slice = tableMap.get(p.getTable());
//			if (slice == null){
//				slice = new ArrayList<MarketPrice>();
//			}
//			slice.add(p);
//			tableMap.put(p.getTable(), slice);
//		}
//		// do batch
//		for (String table : tableMap.keySet()){
//			saveForHour(tableMap.get(table), table+"FH");
//			LOG.info("完成向表："+table+"FH"+" 内存入数据");
//		}
//	}
//	/**
//	 * 每小时执行一次
//	 * */
//	private void saveForHour(List<MarketPrice> list, String table) {
//		if(list == null || list.size() <1 )
//			return;
//		String sql = "insert into " + table + " (edittime, varid, timeint, 开盘价, 最高价, 最低价, 收盘价, 结算价, 持仓量, 成交额, 成交量, code) " +
//				" values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		Object[][] vals = new Object[list.size()][11];
//		Calendar calendar = Calendar.getInstance();
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		for (int i = 0; i < list.size(); i++){
//			MarketPrice p = list.get(i);
//			vals[i][0] = p.getVarid();
////			vals[i][1] = p.getTimeint();
//			vals[i][1] = 100*p.getTimeint()+hour;//精确到小时
//			vals[i][2] = p.getOpen();
//			vals[i][3] = p.getHigh();
//			vals[i][4] = p.getLow();
//			vals[i][5] = p.getLast();
//			vals[i][6] = p.getSettle();
//			vals[i][7] = p.getPosition();
//			vals[i][8] = p.getTurnover();
//			vals[i][9] = p.getVolume();
//			vals[i][10] = p.getCode();
//		}
//
//		JdbcRunner jdbc = null;
//		try {
//			jdbc = new JdbcRunner();
//			jdbc.beginTransaction();
//			jdbc.batchUpdate2(sql, vals);
//			jdbc.endTransaction();
//			sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like '"+table+"' and varid="+vals[0][0];
//			System.out.println(sql);
//			jdbc.update(sql);
//		} catch (Exception e){
//			LOG.error("insert data into DB error",e);
//			try {
//				jdbc.rollTransaction();
//			} catch (SQLException e1) {
//				LOG.error("DB Transaction rollback error",e1);
//			} finally {
//				jdbc.release();
//			}
//		} finally {
//			jdbc.release();
//		}
//
//		LOG.info(table + ":" +list.size() + " prices saved ");
//	}
//
//	public void saveNoCode(List<MarketPrice> list){
//		// aggregate by table
//		Map<String, List<MarketPrice>> tableMap = new HashMap<String, List<MarketPrice>>();
//		for (MarketPrice p : list){
//			if (p == null || p.getTable() == null) continue;
//
//			List<MarketPrice> slice = tableMap.get(p.getTable());
//			if (slice == null){
//				slice = new ArrayList<MarketPrice>();
//			}
//			slice.add(p);
//			tableMap.put(p.getTable(), slice);
//		}
//		// do batch
//		for (String table : tableMap.keySet()){
//			saveNoCode(tableMap.get(table), table);
//			LOG.info("完成向表："+table+" 内存入数据");
//		}
//	}
//
//	private void saveNoCode(List<MarketPrice> list, String table) {
//
//		if(list == null || list.size() <1 )
//			return;
//		String sql = "insert into " + table + " (edittime, varid, timeint, 开盘价, 最高价, 最低价, 收盘价, 结算价, 持仓量, 成交额, 成交量) " +
//				" values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		Object[][] vals = new Object[list.size()][10];
//		for (int i = 0; i < list.size(); i++){
//			MarketPrice p = list.get(i);
//			vals[i][0] = p.getVarid();
//			vals[i][1] = p.getTimeint();
//			vals[i][2] = p.getOpen();
//			vals[i][3] = p.getHigh();
//			vals[i][4] = p.getLow();
//			vals[i][5] = p.getLast();
//			vals[i][6] = p.getSettle();
//			vals[i][7] = p.getPosition();
//			vals[i][8] = p.getTurnover();
//			vals[i][9] = p.getVolume();
//		}
//
//		JdbcRunner jdbc = null;
//		try {
//			jdbc = new JdbcRunner();
//			jdbc.beginTransaction();
//			jdbc.batchUpdate2(sql, vals);
//			jdbc.endTransaction();
//			sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like '"+table+"' and varid="+vals[0][0];
//			jdbc.update(sql);
//		} catch (Exception e){
//			LOG.error("insert data into DB error",e);
//			try {
//				jdbc.rollTransaction();
//			} catch (SQLException e1) {
//				LOG.error("DB Transaction rollback error",e1);
//			} finally {
//				jdbc.release();
//			}
//		} finally {
//			jdbc.release();
//		}
//
//		LOG.info(table + ":" +list.size() + " prices saved ");
//	}
}
