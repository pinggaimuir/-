package cn.futures.data.DAO;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;
import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.Constants;
import cn.futures.data.util.DataLog;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;

public class DAOUtils {
	private static final DataLog dataLog = DataLog.getInstance();
	private static final Logger LOG = Logger.getLogger(DAOUtils.class);
	//取最近的主力连续code
	public String getNewestMajorCode(int varId){
		String query = "select code from CX_MarketData_2  where varId = "+varId+" order by timeint desc";
		JdbcRunner jdbc = null;
		String code = "";
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			if (rs.next())
				code = rs.getString(1);
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return code;
	}
	
	public static int getNewestTimeInt(String tableName){
		String query = "select top 1 timeint from TABLE order by timeint desc";
		JdbcRunner jdbc = null;
		int timeInt = 0;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query.replace("TABLE", tableName));
			if (rs.next())
				timeInt = rs.getInt(1);
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return timeInt;
	}
	
	public String getNewestDataByProv(int varId, String cnName, String prov ){
		String tableName = getTableName(varId, cnName);
		String query = "select top 1 "+prov+" from " + tableName + " where varid=" + varId + 
			" order by timeInt desc";
		return execSearch(query);
	}
	
	public List<String> getHeaders(String varName, String cnName){
		int varId = Variety.getVaridByName(varName);
		String query = "select colName from cfg_table_columns where headerId=" +
				"(select headerId from cfg_table_meta_new where cnName='"+cnName+"' and varId="+varId+")";
		return retColListByExecSearch(query);
	}
	
	public List<String> getHeaderById(int headerId){
		String query = "select colName from cfg_table_columns where headerId="+headerId;
		return retColListByExecSearch(query);
	}
	
	public List<Double> getUsdaRecByTimeInt(int varId, String cnName, String timeInt){
		List<Double> records = new ArrayList<Double>();
		String table = getTableName(varId, cnName);
		if(table.equals("")){
			return records;
		}
		String countries = "";
		int courtryNum = 0;
		List<String> countryList = new ArrayList<String>();
		countryList.addAll(MapInit.usda_country2ch_map.values());
		for(String country:countryList){
			countries += ","+country;
			courtryNum ++;
		}
		String query = "select "+countries.substring(1)+" from "+table+
				" where timeInt="+timeInt+" and varId = "+varId;
		JdbcRunner jdbc = null;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			if (rs.next()){
				for(int i=1;i<=courtryNum;i++){
					records.add(rs.getDouble(i));
				}
			}
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return records;
	}
	/**
	 * 通过字段列表查找出对应的字段值
	 * @param varId
	 * @param cnName
	 * @param timeInt
	 * @return
	 */
	public List<String> getListValues(int varId, String cnName, String timeInt, List<String> fieldNames){
		String tableName = getTableName(varId, cnName);
		String fieldName = "";
		for(String tmp:fieldNames){
			fieldName+=","+tmp;
		}
		String query = "select " + fieldName.substring(1) + " from " + tableName + " where varid=" + varId + 
			" and timeInt=" + timeInt + "";
		return retRowListByExecSearch(query, fieldNames.size());
	}
	/**
	 * 通过字段列表查找出对应的字段值(时间是最新的)
	 * @param varId
	 * @param cnName
	 * @return
	 */
	public List<String> getNewestListValues(int varId, String cnName, List<String> fieldNames){
		String tableName = getTableName(varId, cnName);
		String fieldName = "";
		for(String tmp:fieldNames){
			fieldName+=",["+tmp+"]";
		}
		String query = "select top 1 " + fieldName.substring(1) + " from " + tableName + " where varid=" + varId + 
			" order by timeInt desc";
		return retRowListByExecSearch(query, fieldNames.size());
	}
	/**
	 * 通过单个字段查找出对应的单个值
	 * @param varId
	 * @param cnName
	 * @param timeInt
	 * @param fieldName
	 * @return
	 */
	public String getSingleValue(int varId, String cnName, String timeInt, String fieldName){
		String tableName = getTableName(varId, cnName);
		String query = "select "+fieldName+" from " + tableName + " where varid=" + varId + 
			" and timeInt=" + timeInt + "";
		return execSearch(query);
	}
	/**
	 * 通过单个字段查找出对应的最新的单个值
	 * @param varId
	 * @param cnName
	 * @param fieldName
	 * @return
	 */
	public String getNewestSingleValue(int varId, String cnName, String fieldName){
		String tableName = getTableName(varId, cnName);
		String query = "select top 1 "+fieldName+" from " + tableName + " where varid=" + varId + 
			" order by timeInt desc";
		return execSearch(query);
	}
	/**
	 * 取期货行情数据-WCE油菜籽-收盘价”的nearby值
	 * @return
	 */
	public String getFieldByTable(int varId, String cnName, String timeInt){
		String tableName = getTableName(varId, cnName);
		String query = "select nearby from " + tableName + " where varid=" + varId + 
			" and timeInt=" + timeInt + "";
		return execSearch(query);
	}
	/**
	 * 通过品种与指标查询表名
	 * @param varId
	 * @param cnName
	 * @return
	 */
	public String getTableName(int varId, String cnName){
		String query = "select dbName from cfg_table_meta_new where status = 7 and varid=" + varId + " and cnName='" + cnName + "'";
		String tableName = execSearch(query);
		return tableName;
	}

	/**
	 * 通过查找到的表名、字段及字段对应的数据向表更新数据
	 * @param varId   品种
	 * @param cnName  指标
	 * @param timeInt 数据时间
	 * @param dataMap 字段及对应数据
	 */
	public void saveOrUpdateByDataMap(int varId, String cnName, int timeInt, Map<String, String> dataMap){
		LOG.info("start to save or update data:"+timeInt);
		String tableName = getTableName(varId, cnName);
		LOG.info(String.format("varId: %s --- cnName: %s --- timeInt:%s --- tableName: %s", varId, cnName, timeInt, tableName));
		if(tableName.equals("")){
			LOG.error("未找到对应表");
		}else{
			updateOrSave(null, varId, cnName, tableName, timeInt, dataMap, null);		
		}
	}
	/**
	 * 通过查找到的表名、字段及字段对应的数据向表更新数据
	 * @param varId   品种
	 * @param cnName  指标
	 * @param timeInt 数据时间
	 * @param dataMap 字段及对应数据
	 */
	public void saveOrUpdateByDataMap(int varId, String cnName, int timeInt, Map<String, String> dataMap, Integer crawlerId){
		LOG.info("start to save or update data:"+timeInt);
		String tableName = getTableName(varId, cnName);
		LOG.info(String.format("varId: %s --- cnName: %s --- timeInt:%s --- tableName: %s", varId, cnName, timeInt, tableName));
		if(tableName.equals("")){
			LOG.error("未找到对应表");
		}else{
			updateOrSave(null, varId, cnName, tableName, timeInt, dataMap, crawlerId);		
		}
	}
	/**
	 * 通过查找到的表名、字段及字段对应的数据向表更新数据
	 * @param varName 品种
	 * @param cnName  指标
	 * @param timeInt 数据时间
	 * @param dataMap 字段及对应数据
	 */
	public void saveOrUpdateByDataMap(String varName, String cnName, int timeInt, Map<String, String> dataMap){
		int varId = Variety.getVaridByName(varName);
		boolean haveTable = false;
		String tableName = getTableName(varId, cnName);
		if(tableName.equals("")){
			varId = Variety.getVaridByName(varName+"(续表)");
			tableName = getTableName(varId, cnName);
			if(!tableName.equals("")) haveTable = true;
		}else
			haveTable = true;
		if(haveTable){
			LOG.info(String.format("varName:%s---cnName:%s---varId:%s---tableName:%s---timeInt:%s---", varName, cnName, varId, tableName, timeInt));
			updateOrSave(varName, varId, cnName, tableName, timeInt, dataMap, null);
		}else{
			LOG.error("未找到对应表");
		}
	}
	/**
	 * 通过查找到的表名、字段及字段对应的数据向表更新数据
	 * @param varName 品种
	 * @param cnName  指标
	 * @param timeInt 数据时间
	 * @param dataMap 字段及对应数据
	 */
	public void saveOrUpdateByDataMap(String varName, String cnName, int timeInt, Map<String, String> dataMap, Integer crawlerId){
		int varId = Variety.getVaridByName(varName);
		boolean haveTable = false;
		String tableName = getTableName(varId, cnName);
		if(tableName.equals("")){
			varId = Variety.getVaridByName(varName+"(续表)");
			tableName = getTableName(varId, cnName);
			if(!tableName.equals("")) haveTable = true;
		}else
			haveTable = true;
		if(haveTable){
			LOG.info(String.format("varName:%s---cnName:%s---varId:%s---tableName:%s---timeInt:%s---", varName, cnName, varId, tableName, timeInt));
			updateOrSave(varName, varId, cnName, tableName, timeInt, dataMap, crawlerId);
		}else{
			LOG.error("未找到对应表");
		}
	}
	
	private void updateOrSave(String varName, int varId, String cnName, String tableName, int timeInt, Map<String, String> dataMap, Integer crawlerId){
		LOG.info("find table：" + tableName);
		bak2txt(varId, tableName, timeInt, dataMap);
		String sql = "";
		//查找对应timeInt的记录是否存在
		String query = "select timeInt from " + tableName + " where timeInt=" + timeInt+" and varId = "+varId;
		if(execSearch(query).equals("")){
			LOG.info("not find record where timeInt=" + timeInt +"-insert");
			Object[][] vals = new Object[1][dataMap.keySet().size() + 2];
			vals[0][0] = varId;
			vals[0][1] = timeInt;
			String fields = "";
			String questionMark = "";
			int i = 2;
			for(String data:dataMap.keySet()){
				fields += ",[" + data+"]";
				questionMark += ",?";
				vals[0][i++] = dataMap.get(data);
			}
			sql = "insert into "+tableName+" (EditTime,VarId,TimeInt"+fields
			+") values (getDate(),?,?"+questionMark+")";
			LOG.info(sql);
			execInsert(sql,vals,tableName,varId,crawlerId);
			logPrint(varName, varId, cnName, tableName, "a", timeInt, dataMap);//记录数据更新日志
		}else{
			LOG.info("find record where timeInt=" + timeInt +" and varId = " + varId + "-update");
			Object[][] vals = new Object[1][dataMap.keySet().size()];
			String fields = "";
			int col = 0;
			for(String data:dataMap.keySet()){
				fields += ",[" + data+"]=?";
				vals[0][col++] = dataMap.get(data);
			}
			//update CX_LIFFE5 set timeInt=20150430 where id=789
			sql = "update "+tableName+" set editTime=getDate()" + fields + " where timeInt=" + timeInt+" and varId = "+varId;
			LOG.info(sql);
			execInsert(sql,vals,tableName,varId,crawlerId);
			logPrint(varName, varId, cnName, tableName, "u", timeInt, dataMap);//记录数据更新日志
		}
		LOG.info( " saved success ");
	}
	
	/**
	 * 记录数据更新日志
	 * @param varName 品种名
	 * @param varId 品种id
	 * @param cnName 中文名
	 * @param dbName 物理表名
	 * @param operation 操作（u 更新，a 插入）
	 * @param timeInt 时间序列
	 * @param dataMap 数据
	 * */
	private void logPrint(String varName, int varId, String cnName, String dbName, String operation, int timeInt, Map<String, String> dataMap){
		String datastr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")).format(new Date())+",crawlnew," + operation + ",table_meta_id,--" + varName + "-" + cnName + ","+ dbName + "," + varId + "," + timeInt;
		for(String col: dataMap.keySet()){
			datastr += ","+col+":"+dataMap.get(col);
		}
		try {
			dataLog.println(datastr);
		} catch (Exception e) {
			LOG.error("记录数据更新记录时异常", e);
		}
	}
	
	public void deleteByTimeInt(int varId, String cnName, int timeInt){
		LOG.info("start to save or update data:"+timeInt);
		String tableName = getTableName(varId, cnName);
		if(tableName.equals("")){
			LOG.error("未找到对应表");
		}else{
			LOG.info("find table：" + tableName);
			String sql = "";
			//查找对应timeInt的记录是否存在
			String query = "select timeInt from " + tableName + " where timeInt=" + timeInt+" and varId = "+varId;
			if(execSearch(query).equals("")){
				LOG.info("not find record where timeInt=" + timeInt +"-insert");
			}else{
				LOG.info("find record where timeInt=" + timeInt +" and varId = " + varId + "-update");
				sql = "delete from "+tableName+" where timeInt=" + timeInt+" and varId = "+varId;
				LOG.info(sql);
				JdbcRunner jdbc = null;
				try {
					jdbc = new JdbcRunner();
					jdbc.beginTransaction();
					jdbc.update(sql);
					jdbc.endTransaction();
				} catch (Exception e){
					LOG.error("insert data into DB error",e);
					try {
						jdbc.rollTransaction();
					} catch (SQLException e1) {
						LOG.error("DB Transaction rollback error",e1);
					} finally {
						jdbc.release();
					}
				} finally {
					jdbc.release();
				}	
			}
			LOG.info( " delete success ");		
		}
	}
	
	/**
	 * 批量保存
	 * @param varId
	 * @param cnName
	 * @param time2dataMap
	 */
	public void batchSaveByDataMap(int varId, String cnName, Map<Integer, Map<String, String>> time2dataMap, Integer crawlerId){
		String tableName = getTableName(varId, cnName);
		if(tableName.equals("")){
			LOG.error("未找到对应表");
		}else{
			LOG.info("find table：" + tableName);
			int fieldNum = 0;
			for(int timeInt:time2dataMap.keySet()){
				fieldNum = time2dataMap.get(timeInt).keySet().size();
				break;
			}
			Object[][] vals = new Object[time2dataMap.keySet().size()][fieldNum + 2];
			String fields = "";
			String questionMark = "";
			for(int i=0;i<fieldNum;i++){
				questionMark += ",?";
			}
			int row = 0;
			int i = 0;
			for(int timeInt:time2dataMap.keySet()){
				Map<String, String> dataMap = time2dataMap.get(timeInt);
				int col = 2;
				for(String data:dataMap.keySet()){
					if(i == 0){
						fields += ",[" + data+"]";
					}
					vals[row][0] = varId;
					vals[row][1] = timeInt;
					vals[row][col++] = dataMap.get(data);
				}
				i ++;
				row ++;
			}
			String sql = "insert into "+tableName+" (EditTime,VarId,TimeInt"+fields
				+") values (getDate(),?,?"+questionMark+")";
			LOG.info(sql);
			
			execInsert(sql,vals,tableName,varId,crawlerId);
			LOG.info( " saved success ");	
		}
	}
	
	/**
	 * 通过查找到的表名、字段及字段对应的数据向表插入数据
	 * @param varId   品种
	 * @param cnName  指标
	 * @param timeInt 数据时间
	 * @param dataMap 字段及对应数据
	 
	public void saveByDataMap(int varId, String cnName, int timeInt, Map<String, String> dataMap){
		LOG.info("start to save data:"+timeInt);
		String tableName = getTableName(varId, cnName);
		if(tableName.equals("")){
			LOG.error("未找到对应表");
		}
		LOG.info(String.format("找到对应表，tableName = %s, varId = %s, cnName = %s, timeInt = %s", tableName, varId, cnName, timeInt));//方便查看
		Object[][] vals = new Object[1][dataMap.keySet().size() + 2];
		String fields = "";
		String questionMark = "";
		int i = 2;
		for(String data:dataMap.keySet()){
			fields += ",[" + data+"]";
			questionMark += ",?";
			vals[0][i++] = dataMap.get(data);
		}
		String sql = "insert into "+tableName+" (EditTime,VarId,TimeInt"+fields
			+") values (getDate(),?,?"+questionMark+")";
		LOG.info(sql);
		
		vals[0][0] = varId;
		vals[0][1] = timeInt;
		
		execInsert(sql,vals,tableName,varId);
		LOG.info( " saved success ");		
	}
	*/
	
	private void execInsert(String sql, Object[][] vals,String table, int varId, Integer crawlerId){
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
			if(crawlerId == null){
				sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName = '"+table+"' and varid="+varId;
				jdbc.update(sql);
			} else {
				sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate(), crawlerId = ? where dbName = ? and varid=?";
				Object[] params = {crawlerId, table, varId};
				jdbc.update2(sql, params);
			}
		} catch (Exception e){
			LOG.error("insert data into DB error",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			} finally {
				jdbc.release();
			}
		} finally {
			jdbc.release();
		}	
	}
	
	private String execSearch(String query){
		JdbcRunner jdbc = null;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			if (rs.next())
				return rs.getString(1);
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return "";
	}
	
	private List<String> retRowListByExecSearch(String query, int resNum){
		List<String> res = new ArrayList<String>();
		JdbcRunner jdbc = null;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			if (rs.next()){
				for(int i=1;i<=resNum;i++){
					res.add(rs.getString(i));
				}
			}
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return res;
	}
	
	private List<String> retColListByExecSearch(String query){
		List<String> res = new ArrayList<String>();
		JdbcRunner jdbc = null;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			while (rs.next()){
				res.add(rs.getString(1));
			}
		} catch (Exception e){
			LOG.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return res;
	}
	
	/**
	 * 数据备份
	 */
	private void bak2txt(int varId, String tableName, int timeInt, Map<String, String> dataMap){
		StringBuffer saveContent = new StringBuffer();
		String editTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd hh-mm-dd");
		saveContent.append("editTime:"+editTime+";"+"varId:"+varId+";"+"timeInt:"+timeInt+";");
		for(String data:dataMap.keySet()){
			saveContent.append(data+":"+dataMap.get(data)+";");
		}
		try {
			FileStrIO.appendStringToFile(saveContent.toString(), Constants.DATABAK_TXT, tableName + ".txt", null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 更新爬虫字段
	 * @param cnName 中文名
	 * @param varId 品种id
	 * @param crawlerId 爬虫id
	 * */
	public void setTestTable(String cnName, int varId,int crawlerId){
		String sql = "update cfg_table_meta_new set crawlerId=? where cnName=? and varId = ?";
		Object[] params = {crawlerId, cnName, varId};
		LOG.info("更新：" + sql);
		JdbcRunner jdbc = null;
		try{
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			int updateRslt = jdbc.update2(sql, params);
			jdbc.endTransaction();
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
	 * 数据备份
	 */
	public static void bak2txt(List<MarketPrice> prices){
		StringBuffer saveContent = new StringBuffer();
		Date date = new Date();
		String editTime = DateTimeUtil.formatDate(date, "yyyy-MM-dd hh-mm-dd");
		for(MarketPrice price:prices){
			saveContent.delete(0, saveContent.length());
			saveContent.append("editTime:"+editTime+";"+"varId:"+price.getVarid()+";"+"timeInt:"+price.getTimeint()+";");
			saveContent.append("开盘价:"+price.getOpen()+";");
			saveContent.append("最高价:"+price.getHigh()+";");
			saveContent.append("最低价:"+price.getLow()+";");
			saveContent.append("收盘价:"+price.getLast()+";");
			saveContent.append("结算价:"+price.getSettle()+";");
			saveContent.append("持仓量:"+price.getPosition()+";");
			saveContent.append("成交额:"+price.getTurnover()+";");
			saveContent.append("成交量:"+price.getVolume()+";");
			try {
				FileStrIO.appendStringToFile(saveContent.toString(), Constants.DATABAK_TXT, price.getTable() + ".txt", null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存或者更新指定时间序列指定批发市场的价格数据(专用于批发市场价格数据入库)
	 * @param productName 产品名称
	 * @param marketName 批发市场名称
	 * @param timeInt 数据的时间序列
	 * @param priceInfo 价格信息（含最低价、最高价、平均价、产地、规格）
	 * */
	public void saveOrUpdateForMarketPrice(String productName, String marketName, Integer timeInt, Map<String, String> priceInfo){
		String querySql = "SELECT COUNT(id) count FROM market_price WHERE product_id = ? AND market_id = ? AND TimeInt = ? AND specification = ?";
		String queryProIdSql = "select id from market_product where name = '" + productName + "'";
		String queryMarIdSql = "select id from wholesale_market where name = '" + marketName + "'";
		String proId = execSearch(queryProIdSql);//产品id
		if(proId == null || proId.isEmpty()){
			LOG.info("未找到对应产品：" + productName);
			return;
		}
		String marId = execSearch(queryMarIdSql);//批发市场id
		if(marId == null || marId.isEmpty()){
			LOG.info("未找到对应批发市场：" + marketName);
			return;
		}
		int count = queryCount(querySql, new Object[]{proId, marId, timeInt, priceInfo.get("specification")});
		if(count > 0){//已有，更新
			String updateSql = "UPDATE market_price SET EditTime = getDate(), min_price = ?, max_price = ?, ave_price = ?, produce_area = ?, specification = ? WHERE product_id = ? AND market_id = ? AND TimeInt = ? AND specification = ?";
			update(updateSql, new Object[]{priceInfo.get("min_price"), priceInfo.get("max_price"), 
					priceInfo.get("ave_price"), priceInfo.get("produce_area"), priceInfo.get("specification"), proId, marId, timeInt, priceInfo.get("specification")});
			String datastr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")).format(new Date())+",crawlnew_market,u," + marketName + "-" + productName + "," + timeInt;
			for(String col: priceInfo.keySet()){
				datastr += ","+col+":"+priceInfo.get(col);
			}
			try {
				dataLog.println(datastr);
			} catch (Exception e) {
				LOG.error("记录数据更新记录时异常", e);
			}
		} else {//没有，插入
			String insertSql = "INSERT INTO market_price ([market_id], [product_id], [TimeInt], [min_price], [max_price], [ave_price], [produce_area], [specification]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			update(insertSql, new Object[]{marId, proId,timeInt, priceInfo.get("min_price"), priceInfo.get("max_price"), 
					priceInfo.get("ave_price"), priceInfo.get("produce_area"), priceInfo.get("specification"),});
			String datastr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")).format(new Date())+",crawlnew_market,a," + marketName + "-" + productName + "," + timeInt;
			for(String col: priceInfo.keySet()){
				datastr += ","+col+":"+priceInfo.get(col);
			}
			try {
				dataLog.println(datastr);
			} catch (Exception e) {
				LOG.error("记录数据更新记录时异常", e);
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
	
}
