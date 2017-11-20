package cn.futures.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.seleniumhq.jetty7.util.log.Log;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.FileStrIO;

/**
 * 批发市场数据采集-处理文件保存基本数据
 * */
public class WholesaleMarketData {

	private static final Logger LOG = Logger.getLogger(WholesaleMarketData.class);
	
	/**
	 * 根据xls文件保存批发市场数据中的品种
	 * @param filePath 文件路径
	 * @param sheetName 工作簿名
	 * @param productColId 产品列列号
	 * @param unitColId 单位列列号
	 * */
	public void saveProduct(String filePath, String sheetName, int productColId, int unitColId){
		String[][] table = FileStrIO.readXls(filePath, sheetName);
		String querySql = "select count(id) count from market_product where name = ?";
		String updateSql = "INSERT INTO market_product ([name], [unit]) VALUES (?, ?)";
		for(int rowId = 0; rowId < table.length; rowId++){//产品字段以及单位字段均不为空时保存
			if(table[rowId][productColId] != null && !table[rowId][productColId].isEmpty()
					&& table[rowId][unitColId] != null && !table[rowId][unitColId].isEmpty()){
				
				int count = queryCount(querySql, new Object[]{table[rowId][productColId]});
				if(count == 0){
					update(updateSql, new Object[]{table[rowId][productColId], table[rowId][unitColId]});
				} else {
					LOG.info("第" + (rowId + 1) + "行数据（" + table[rowId][productColId] + "）已存在。");
				}
			} else {
				LOG.info("第" + (rowId + 1) + "行数据（" + table[rowId][productColId] + "）为空。");
			}
		}
	}
	
	/**
	 * 查询总条数
	 * */
	public int queryCount(String sql, Object[] params){
		JdbcRunner jdbc = null;
		ResultSet rs = null;
		int count = 0;
		try{
			jdbc = new JdbcRunner();
			rs = jdbc.query(sql, params);
			if(rs.next()){
				count = rs.getInt("count");
			}
		} catch(Exception e) {
			LOG.error("查询时发生异常。",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			}
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
		return count;
	}
	
	/**
	 * 更新数据
	 * */
	public void update(String sql, Object[] params){
		LOG.info("更新：" + sql);
		if(params != null){
			String parStr = "";
			for(Object param: params){
				parStr += param;
				parStr += "-";
			}
			LOG.info(parStr);
		}
		JdbcRunner jdbc = null;
		try{
			jdbc = new JdbcRunner();
			int updateRslt = jdbc.update2(sql, params);
			LOG.info("updateRslt:" + updateRslt);
		} catch(Exception e) {
			LOG.error("更新数据时异常。",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			}
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
	}
	
	/**
	 * 查询一条
	 * @param sql prepared sql
	 * @param params 参数
	 * */
	private void query(String sql, String[] params, Map<String, Object> rsltMap){
		JdbcRunner jdbc = null;
		try{
			jdbc = new JdbcRunner();
			ResultSet rs = jdbc.query(sql, params);
			if(rs.next()){
				for(String key: rsltMap.keySet()){
					rsltMap.put(key, rs.getObject(key));
				}
			}
		} catch(Exception e) {
			LOG.error(e);
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
	}
	
	//返回文件列表
	private String[] getFileList(String tarDirPath){
		File tarDir = new File(tarDirPath);
		String[] tarFiles = tarDir.list();
		String parentPath = tarDir.getPath();
		for(int i = 0; i < tarFiles.length; i++){
			tarFiles[i] = parentPath + File.separator + tarFiles[i];
		}
		return tarFiles;
	}
	
	//处理文件获取所有批发市场信息
	private void dealFiles(String[] tarFiles, String sheetName, int startRow){
		for(String tarFile: tarFiles){
			String[][] table = FileStrIO.readXls(tarFile, sheetName);
			for(int rowId = startRow; rowId < table.length; rowId++){
				String[] row = table[rowId];
				String marketName = row[0];//批发市场名称
				String prov = row[1] != null ? row[1].trim() : null;//省
				String city = row[2] != null ? row[2].trim() : null;//市
				String country = row[3] != null ? row[3].trim() : null;
				String marketType = null;
				if(row[4] != null && row[4].trim().equals("产地")){
					marketType = "0";
				} else if(row[4] != null && row[4].trim().equals("销地")) {
					marketType = "1";
				}
				String address = row[5] != null ? row[5].trim() : null;//地址
				String contact = row[6] != null ? row[6].trim() : null;//联系方式（邮箱）
				String telephone = row[7] != null ? row[7].trim() : null;//电话
				String coordinate = row[8] != null ? row[8].trim() : null;//经纬度
				String self_url = row[9] != null ? row[9].trim() : null;//自身网址
				String platform_url = row[10] != null ? row[10].trim() : null;//平台网址
//				System.out.println(String.format("{\"%s\", \"%s\"},", marketName, platform_url));//可用于提取爬虫配置信息。
				//查询是否已有该批发市场数据
				String queryMarSql = null;
				String[] marketParam = null;
				String[] areaParam = null;
				String acIdSql = null;
				String insProv = null;
				Map<String, Object> marRsltMap = new HashMap<String, Object>();
				marRsltMap.put("count", null);
				if(prov != null && city != null && country != null){
					queryMarSql = "SELECT COUNT(market.id) count FROM wholesale_market market LEFT JOIN area_country area ON market.AC_id = area.id "
							+ "WHERE market.name = ? AND area.province_name = ? AND area.city_name = ? AND area.countryside_name = ?";
					acIdSql = "SELECT id FROM area_country WHERE province_name = ? AND city_name = ? AND countryside_name = ?";
					insProv = "INSERT INTO area_country ([province_name], [city_name], [countryside_name]) VALUES (?, ?, ?)";
					String[] tempArr1 = {marketName, prov, city, country};
					String[] tempArr2 = {prov, city, country};
					marketParam = tempArr1;
					areaParam = tempArr2;
				} else if(prov != null && city != null && country == null) {
					queryMarSql = "SELECT COUNT(market.id) count FROM wholesale_market market LEFT JOIN area_country area ON market.AC_id = area.id "
							+ "WHERE market.name = ? AND area.province_name = ? AND area.city_name = ? AND area.countryside_name is null";
					acIdSql = "SELECT id FROM area_country WHERE province_name = ? AND city_name = ? AND countryside_name is null";
					insProv = "INSERT INTO area_country ([province_name], [city_name]) VALUES (?, ?)";
					String[] tempArr1 = {marketName, prov, city};
					String[] tempArr2 = {prov, city};
					marketParam = tempArr1;
					areaParam = tempArr2;
				} else if(prov != null && city == null && country == null) {
					queryMarSql = "SELECT COUNT(market.id) count FROM wholesale_market market LEFT JOIN area_country area ON market.AC_id = area.id "
							+ "WHERE market.name = ? AND area.province_name = ? AND city_name is null AND countryside_name is null";
					acIdSql = "SELECT id FROM area_country WHERE province_name = ? AND city_name is null AND countryside_name is null";
					insProv = "INSERT INTO area_country ([province_name]) VALUES (?)";
					String[] tempArr1 = {marketName, prov};
					String[] tempArr2 = {prov};
					marketParam = tempArr1;
					areaParam = tempArr2;
				} else {
					LOG.info("批发市场已存在：" + prov + city + country + marketName);
				}
				this.query(queryMarSql, marketParam, marRsltMap);
				if((Integer)marRsltMap.get("count") > 0){
					LOG.info("批发市场已存在：" + prov + city + country + marketName);
					continue;
				}
				//查询省市区（县）
				Map<String, Object> rsltMap = new HashMap<String, Object>();
				rsltMap.put("id", null);
				this.query(acIdSql, areaParam, rsltMap);
				if(rsltMap.get("id") == null){//保存省市区（县）信息
					this.update(insProv, areaParam);
					this.query(acIdSql, areaParam, rsltMap);//插入后再次查询
				}
				//保存批发市场信息
				//查询AC_id
				String acId = rsltMap.get("id").toString();
				//保存批发市场信息
				String insMarket = "INSERT INTO wholesale_market ([name], [AC_id], [market_type], [address], "
						+ "[contact], [telephone], [coordinate], [self_url], [platform_url]) VALUES "
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				this.update(insMarket, new String[]{marketName, acId, marketType, address, contact, 
						telephone, coordinate, self_url, platform_url});
				
			}
		}
	}
	
	//提取产品
	private void parseProduct(String[] tarFiles, int prodectColId, int unitColId){
		for(String tarFile: tarFiles){
			LOG.info("-------------开始解析文件：" + tarFile + "---------------");
			String[] sheetNames = parseSheetName(tarFile);
			for(String sheetName: sheetNames){
				if(!"信息".equals(sheetName)){
					this.saveProduct(tarFile, sheetName, prodectColId, unitColId);	
				}
			}
		}
	}
	
	//提取文件中所有sheetName
	private String[] parseSheetName(String tarFile){
		InputStream is = null;
		Workbook hssfWorkbook = null;
		try {
			is = new FileInputStream(tarFile);
			hssfWorkbook = FileStrIO.create(is);
			int sheetNums = hssfWorkbook.getNumberOfSheets();
			String[] sheetNames = new String[sheetNums];
			for(int i = 0; i < sheetNums; i++){
				sheetNames[i] = hssfWorkbook.getSheetName(i);
			}
			return sheetNames;
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (InvalidFormatException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
		return null;
	}
	
	/**
	 * @param tarFiles 
	 * @param prodectColId 产品列
	 * @param proSignColId 产品标识列
	 * */
	private void parseDifferCol(String[] tarFiles, int prodectColId, int proSignColId){
		Map<String, String> proNameBySign = new HashMap<String, String>();//产品名映射（只包含与表中存储不一致的）
		for(String tarFile: tarFiles){
			LOG.info("-------------开始解析文件：" + tarFile + "---------------");
			String[] sheetNames = parseSheetName(tarFile);
			for(String sheetName: sheetNames){
				if(!"信息".equals(sheetName)){
					LOG.info("-------------开始解析批发市场：" + sheetName + "---------------");
					String[][] table = FileStrIO.readXls(tarFile, sheetName);
					for(int rowId = 0; rowId < table.length; rowId++){
						if(table[rowId][prodectColId] != null && !table[rowId][prodectColId].matches("\\s*") &&
								table[rowId][proSignColId] != null && !table[rowId][proSignColId].matches("\\s*")){
							proNameBySign.put(table[rowId][proSignColId], table[rowId][prodectColId]);
							LOG.info(String.format("proNameBySign.put(\"%s\", \"%s\");", table[rowId][proSignColId], table[rowId][prodectColId]));
						}
					}
				}
			}
		}
		LOG.info("----------------------differ product-----------------------");
		for(String sign: proNameBySign.keySet()){
			LOG.info(String.format("proNameBySign.put(\"%s\", \"%s\");", sign, proNameBySign.get(sign)));
		}
	}
	
	public static void main(String[] args) {
		WholesaleMarketData wmd = new WholesaleMarketData();
//		wmd.saveProduct("D:\\Test\\北京农产品批发市场数据采集方案-20170104.xlsx", "北京新发地市场", 3, 6);
//		wmd.saveProduct("D:\\Test\\北京农产品批发市场数据采集方案-20170104.xlsx", "北京昌平水屯农副产品批发市场", 3, 6);
		System.out.println("start");
		String[] tarFiles = wmd.getFileList("C:\\Users\\bric_yangyulin\\Desktop\\农产品批发市场数据\\");
		
		//提取批发市场信息。
//		wmd.dealFiles(tarFiles, "信息", 1);
		
		//提取品种
		//wmd.parseProduct(tarFiles, 1, 2);
		
		//打印有所区别的品种
		wmd.parseDifferCol(tarFiles, 1, 3);
	}
}
