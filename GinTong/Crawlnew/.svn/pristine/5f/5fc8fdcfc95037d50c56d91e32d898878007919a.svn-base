package cn.futures.chart;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.futures.data.jdbc.JdbcRunner;

public class PriceExcel2Db {
	
	public static Logger LOG = Logger.getLogger(PriceExcel2Db.class);

	public static final String AREA_PROVINCE_FILE = "cn/futures/chart/网站数据价格数据对应.xls";
	public static final String[] PROVINCES = new String[] { "全国", "北京", "天津", "河北", "山西", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆"};

	public static HashMap<String, String> area2provinceMap = null;

	public PriceExcel2Db() {
		if (area2provinceMap == null) {
			synchronized(this) {
				if (area2provinceMap == null) {
					area2provinceMap = parseArea2ProvinceMap();
				}
			}
		}
	}
	
	public void mychart2bricdata(int classid, List<MyChartVo> chartList ) {
		LOG.info("input excel, classid="+classid+" rows="+chartList.size());
		int varid = getVaridByClassid(classid);
		if (varid == -1) {
			LOG.warn("no such classid:" + classid);
			return;
		}
		String table = getPriceTableByVarid(varid);

		// 将excle中的数据按timeint聚合
		HashMap<Integer, HashMap<String, Double>> priceTable = new HashMap<Integer, HashMap<String, Double>>();
		for (MyChartVo c : chartList) {
			String province = null;			
			if (c.getArea() == null || 
					(province = area2provinceMap.get(c.getArea().toUpperCase())) == null){
				LOG.warn("skip area: " + c.getArea() + "-" + province );
				continue;		//跳过找不到对应省份的数据点
			}
			
			int timeInt = parseTimeInt(c.getStime());
			if (timeInt == 0) {
				LOG.warn("skip timeint: " + c.getStime());
				continue;	//跳过日期转换出错的记录
			}
			HashMap<String, Double> priceRow = priceTable.get(timeInt);
			if (priceRow == null) {
				priceRow = new HashMap<String, Double>();
			}

			Double price = c.getPrice();
			// 同一天同一地区多个报价，以后来者为准
			priceRow.put(province, price);
			priceTable.put(timeInt, priceRow);
		}
		
		// 对每个timeint执行一次插入或更新操作
		for (int timeInt : priceTable.keySet()){
			int priceCnt = getPriceCount(table, varid, timeInt);
			if (priceCnt <= 0){
				CxPriceInsert(table, varid, timeInt);
			}
			
			CxPriceUpdate(table, varid, timeInt, priceTable.get(timeInt));
		}
		
		
	}
	
	/**
	 * 将stime转为timeint
	 * 支持yyyy-m-d, yyyy-m, yyyy
	 * 分别转为yyyymmdd, yyyymm, yyyy
	 * @param stime
	 * @return
	 */
	private int parseTimeInt(String stime) {
		int year, month, day = 0;
		
		
		String[] ymd = stime.split("[-|/]");
		if (ymd.length == 3) {	//y-m-d
			year = Integer.parseInt(ymd[0]);
			month = Integer.parseInt(ymd[1]);
			day = Integer.parseInt(ymd[2]);
			return year*10000 + month*100 + day;
		} else if (ymd.length == 2) {	//y-m
			year = Integer.parseInt(ymd[0]);
			month = Integer.parseInt(ymd[1]);
			return year*100 + month;
		} else if (ymd.length == 1) {	//y
			year = Integer.parseInt(ymd[0]);			
			return year;
		} else {
			return 0;
		}
		
	}

	private int getPriceCount(String table, int varid, int timeInt) {
		int count = 0;
		String sql = "select count(1) from " + table + " where VarId=" + varid + " and TimeInt=" + timeInt;
		LOG.info(sql);
		JdbcRunner bricdata = null;
		ResultSet rs = null;
		try {
			bricdata = new JdbcRunner();
			rs = bricdata.query(sql);
			if (rs.next()){
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bricdata.release();
		}
		
		return count;
	}
	
	private void CxPriceInsert(String table, int varid, int timeInt) {
		String sql = "insert into " + table +" (EditTime, VarId, TimeInt) values (getDate()," + varid +","+timeInt+")";
		
		JdbcRunner bricdata = null;
		try {
			bricdata = new JdbcRunner();
			LOG.info(sql);
			bricdata.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bricdata.release();
		}
	}
	
	private void CxPriceUpdate(String table, int varid, int timeInt, HashMap<String, Double> priceRow) {
			
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("update ").append(table).append(" set EditTime=getDate(), ");
			for (Iterator<String> iter = priceRow.keySet().iterator(); iter.hasNext();){
				
				String province = iter.next();
				double price = priceRow.get(province);
				sqlBuilder.append(province).append("=").append(price);
				if (iter.hasNext()){
					sqlBuilder.append(",");
				}
			}
			sqlBuilder.append(" where TimeInt=").append(timeInt).append(" and VarId=").append(varid);

		String sql = sqlBuilder.toString();
		JdbcRunner bricdata = null;
		try {
			bricdata = new JdbcRunner();
			LOG.info(sql);
			bricdata.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bricdata.release();
		}
	}
	
	/**
	 * 从城市转为省份
	 * 自动加载自 网站数据价格数据对应.xls
	 * @return
	 */
	private HashMap<String, String> parseArea2ProvinceMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		InputStream input = PriceExcel2Db.class.getClassLoader().
			getResourceAsStream(AREA_PROVINCE_FILE);
		
		HSSFWorkbook book = null;
		try {
			book = new HSSFWorkbook(input);
		}  catch (IOException e) {
			e.printStackTrace();
		}
		
		HSSFSheet sheet = book.getSheetAt(0);
		int totalRow = sheet.getLastRowNum();
		
		//  标题行
		HSSFRow rowTitle = sheet.getRow(0);
		// 省份-省份的映射
		for (short k = 0; k<rowTitle.getLastCellNum(); k++){
			HSSFCell cell = rowTitle.getCell(k);
			if (cell != null){
				String v = cell.getStringCellValue();
				map.put(v, v);
			}
		}
		// 从第二行开始，城市-省份的映射
		for(int i=1; i<totalRow; i++){
			HSSFRow row = sheet.getRow(i);
			if (row == null) continue;
			int totalCol = row.getLastCellNum();
			//跳过品种列，从第二列开始
			for (short j=1; j<totalCol; j++){
				HSSFCell cell = row.getCell(j);
				if (cell == null) continue;
				String key = cell.getStringCellValue().toUpperCase();
				String value = rowTitle.getCell(j).getStringCellValue();
				System.out.println(key + "-" + value);
				map.put(key, value);
			}
		}
		
		return map;
	}
	
	/**
	 * 从classid转为varid
	 * 来自 填表.xls	 * 
	 * @param classid
	 * @return
	 */
	private int getVaridByClassid (int classid){
		switch (classid) {
			case 5:				//白糖
				return 4;
			case 1:				//玉米
				return 29;
			case 2:				//大豆
				return 30;
			case 3:				//小麦
				return 31;
			case 21:			// 饲料养殖?
				return 41;
			case 7:				//豆油
				return 76;
			case 8:				//菜油
				return 77;
			case 11:			//棕榈油
				return 78;
			case 15:			//豆粕
				return 32;
			default:
				return -1;
		}
	}
	
	/**
	 * 从varid获得对应的价格表
	 * 来自 填表.xls
	 * @param varid
	 * @return
	 */
	private String getPriceTableByVarid(int varid){
		switch (varid){
		case 4:
		case 29:
		case 31:
		case 32:
			return "cx_price";
		case 30:
		case 76:
		case 77:
		case 78:
			return "cx_price1";
		case 41:
			return "cx_price4";
		default:
			return null;
		}
	}


	/**
	 * 曲线手动输入页面（无stime）的适配器
	 * 
	 * @param parseInt
	 * @param stime
	 * @param mychartvos
	 */
	public void mychart2bricdata(int parseInt, String stime,
			List<MyChartVo> mychartvos) {
		LOG.info("input excel, classid="+parseInt+" stime="+stime + " rows="+mychartvos.size());
		for (MyChartVo c : mychartvos){
			c.setStime(stime);
			LOG.info(c.getChartid() + " " + c.getClassid() + " "
					+ c.getStime() + " " + c.getArea() + " " + c.getPrice());
		}
		mychart2bricdata(parseInt, mychartvos);
	}

}
