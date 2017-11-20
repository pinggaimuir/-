package cn.futures.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.FileStrIO;

/**
 * 统一单位和数值
 * @author bric_yangyulin
 * @date 2016-12-13
 * */
public class UniteUnitAndValue {
	
	private static Logger LOG = Logger.getLogger(UniteUnitAndValue.class);
	
	/**
	 * 修改数据
	 * @param table
	 * @param startRow 此次处理起始行
	 * @param endRow 此次处理截止行（含该行）
	 * */
	public void modifyValue(String[][] table, int startRow, int endRow){
		LOG.info("------------------------------开始修改数据...-------------------------------");
		//修改数据
		for(int rowId = startRow; rowId < table.length && rowId <= endRow; rowId++){
			LOG.info("数据：处理（" + (rowId+1) + "）  " + table[rowId][0] + "-" + table[rowId][1] + "-" + table[rowId][2] + "-" + table[rowId][3]);
			//查询逻辑表
			String querySql = "select meta.dbName, meta.varId, meta.headerId from CFG_TABLE_META_NEW meta "
					+ " LEFT JOIN CFT_TABLE_INDUSTRY ind ON meta.industryId = ind.id" 
					+ " LEFT JOIN CFT_TABLE_INDUSTRY_DETAIL_NEW detail ON meta.industryDetailId = detail.id"
					+ " LEFT JOIN CX_Variety_new var ON meta.varId = var.Id" 
					+ " where meta.status = 7 AND ind.name = ? AND detail.name = ? and var.VName = ? and meta.cnName=?";
			Map<String, Object> rsltMap = queryTable(querySql, new String[]{table[rowId][0], table[rowId][1], table[rowId][2], table[rowId][3]});
			String dbName = null;//物理表名称
			dbName = (String)rsltMap.get("dbName");
			int varId = -1;//品种id
			varId = (Integer)rsltMap.get("varId");
			int headerId = -1;//表头id
			headerId = (Integer)rsltMap.get("headerId");
			if(dbName != null && varId != -1 && headerId != -1){
				//修改数据
				String modifySql = getModifySql(dbName, varId, headerId, table[rowId][7], table[rowId][8], table[rowId][9], table[rowId][10]);
				if(modifySql != null){
					update(modifySql, null);
					String updateTimeSql = "UPDATE CFG_TABLE_META_NEW SET dataUpdateTime = getDate() FROM CFG_TABLE_META_NEW meta " 
							+ " LEFT JOIN CFT_TABLE_INDUSTRY ind ON meta.industryId = ind.id" 
							+ " LEFT JOIN CFT_TABLE_INDUSTRY_DETAIL_NEW detail ON meta.industryDetailId = detail.id"
							+ " LEFT JOIN CX_Variety_new var ON meta.varId = var.Id" 
							+ " where meta.status = 7 AND ind.name = ? AND detail.name = ? and var.VName = ? and meta.cnName=?";
					//修改相应表对应菜单项中的dataUpdateTime
					update(updateTimeSql, new String[]{table[rowId][0], table[rowId][1], table[rowId][2], table[rowId][3]});
				}
			} else {
				LOG.error("没有查找到对应逻辑表。");
			}
		}
		LOG.info("数据修改结束。");
	}
	
	/**
	 * 大体检查数据格式是否符合要求
	 * @param table
	 * @param startRow 此次处理起始行
	 * @param endRow 此次处理截止行（含该行）
	 * */
	public boolean check(String[][] table, int startRow, int endRow){
		LOG.info("-------------------------开始数据检查...-----------------------");
		boolean isRight = true;
		for(int rowId = startRow; rowId < table.length && rowId <= endRow; rowId++){
			//检查各列是否有空值
			for(int colId = 0; colId < table[rowId].length; colId++){
				if(table[rowId][colId] == null || table[rowId][colId].isEmpty() || table[rowId][colId].equals("null")){
					isRight = false;
					LOG.error("第" + (rowId+1) + "行， 第" + colId + "列为空。");
				}
			}
			//检查表是否存在（即目录有没有写错）
			
			/*String querySql = "select meta.dbName, meta.varId, meta.headerId from CFG_TABLE_META_NEW meta "
					+ " LEFT JOIN CFT_TABLE_INDUSTRY ind ON meta.industryId = ind.id" 
					+ " LEFT JOIN CFT_TABLE_INDUSTRY_DETAIL_NEW detail ON meta.industryDetailId = detail.id"
					+ " LEFT JOIN CX_Variety_new var ON meta.varId = var.Id" 
					+ " where meta.status = 7 AND ind.name = ? AND detail.name = ? and var.VName = ? and meta.cnName=?";
			Map<String, Object> rsltMap = queryTable(querySql, new String[]{table[rowId][0], table[rowId][1], table[rowId][2], table[rowId][3]});
			Object dbName = null;//物理表名称
			dbName = rsltMap.get("dbName");
			if(dbName == null || dbName.equals("")){
				isRight = false;
				LOG.error("第" + (rowId+1) + "行，目录未匹配到表。" + table[rowId][0] + "-" + table[rowId][1] + "-" + table[rowId][2] + "-" + table[rowId][3]);
			}*/
			
			if("数值不变".equals(table[rowId][7]) && "数值不变".equals(table[rowId][8])
					&& "数值不变".equals(table[rowId][9]) && "数值不变".equals(table[rowId][10])){
				continue;
			}
			//检查“需变更列说明”字段
			if(table[rowId][7] == null 
					|| !(table[rowId][7].equals("所有") || table[rowId][7].matches("(\\[[^\\]]+\\],)*\\[[^\\]]+\\]"))){
				isRight = false;
				LOG.error("第" + (rowId+1) + "行，‘需变更列说明’列不合格式要求。");
			}
			//检查“需变更时间序列”字段
			if(table[rowId][8] == null 
					|| !(table[rowId][8].equals("所有") || table[rowId][8].matches("除\\[[-;0-9]*\\](所有)?")
							|| table[rowId][8].matches("\\[[-;0-9]*\\]"))){
				isRight = false;
				LOG.error("第" + (rowId+1) + "行，‘时间序列变更列说明’列不合格式要求。");
			}
			//检查“变更操作”字段
			if(table[rowId][9] == null || !(table[rowId][9].matches("(乘以)|(除以)"))){
				isRight = false;
				LOG.error("第" + (rowId+1) + "行，‘变更操作’列不合格式要求。");
			}
			//检查“数值”字段
			if(table[rowId][10] == null || !(table[rowId][10].matches("(\\d+)(\\.\\d+)?"))){
				isRight = false;
				LOG.error("第" + (rowId+1) + "行，‘数值’列不合格式要求。");
			}
		}
		LOG.info("数据检查结果：" + isRight);
		return isRight;
	}
	
	/**
	 * 修改单位
	 * @param table
	 * @param startRow 此次处理起始行
	 * @param endRow 此次处理截止行（含该行）
	 * */
	public void modifyUnit(String[][] table, int startRow, int endRow){
		LOG.info("-------------------开始修改单位...-----------------------");
		String unitSql = "UPDATE CFG_TABLE_META_NEW SET unit = ? FROM CFG_TABLE_META_NEW meta " 
				+ " LEFT JOIN CFT_TABLE_INDUSTRY ind ON meta.industryId = ind.id" 
				+ " LEFT JOIN CFT_TABLE_INDUSTRY_DETAIL_NEW detail ON meta.industryDetailId = detail.id"
				+ " LEFT JOIN CX_Variety_new var ON meta.varId = var.Id" 
				+ " where meta.status = 7 AND ind.name = ? AND detail.name = ? and var.VName = ? and meta.cnName=?";
		for(int rowId = startRow; rowId < table.length && rowId <= endRow; rowId++){
			LOG.info("单位：处理（" + (rowId+1) + "）  " + table[rowId][0] + "-" + table[rowId][1] + "-" + table[rowId][2] + "-" + table[rowId][3]);
			if(table[rowId][5].equals("不变")){
				continue;
			}
			String[] params = new String[]{table[rowId][5], table[rowId][0], table[rowId][1], table[rowId][2], table[rowId][3]};
			update(unitSql, params);
		}
		LOG.info("单位修改完成。");
	}
	
	/**
	 * 生成修改数据的sql语句
	 * */
	public String getModifySql(String dbName, int varId, int headerId, String columnModify, String timeIntModify, String operator, String multiple){
		if(columnModify.equals("数值不变")){
			LOG.info("数值不变");
			return null;	
		}
		StringBuilder sql = new StringBuilder("update ");
		sql.append(dbName);
		Object[] columns = null;//需修改的列名
		String oper = null;//操作符
		String where = " where varId = " + varId;//where 条件
		if(columnModify.equals("所有")){//所有列都需要修改
			//查询出所有数据列（排除id、EditTime、TimeInt、varId列）
			String columnSql = "SELECT colName from CFG_TABLE_COLUMNS WHERE headerId = ?";
			columns = queryColumns(columnSql, new Object[]{headerId});
		} else {//指定列需要修改
			//提取出指定列
			columns = columnModify.split(",");
		}
		if(!timeIntModify.equals("所有")){//指定时间序列的数据需要修改
			if(timeIntModify.startsWith("除")) {
				timeIntModify = timeIntModify.replace("所有", "");//文件中出现了"除[...]"、"除[...]所有"两种格式
				String[] timeSpans = timeIntModify.substring(2, timeIntModify.length()-1).split(";");
				for(int i = 0; i < timeSpans.length; i++){
					if(timeSpans[i].contains("-")){
						String[] timeInts = timeSpans[i].split("-");
						if(timeInts[0].compareTo(timeInts[1]) <= 0){
							where += " and timeInt not between " + timeInts[0] + " and " + timeInts[1];
						} else {
							where += " and timeInt not between " + timeInts[1] + " and " + timeInts[0];
						}
					} else {
						where += " and timeInt != " + timeSpans[i];
					}
				}
			} else {
				String[] timeSpans = timeIntModify.substring(1, timeIntModify.length()-1).split(";");
				for(int i = 0; i < timeSpans.length; i++){
					if(timeSpans[i].contains("-")){
						String[] timeInts = timeSpans[i].split("-");
						if(timeInts[0].compareTo(timeInts[1]) <= 0){
							if(i == 0){
								where += " and timeInt between " + timeInts[0] + " and " + timeInts[1];
							} else {
								where += " or timeInt between " + timeInts[0] + " and " + timeInts[1];
							}
						} else {
							if(i == 0){
								where += " and timeInt between " + timeInts[1] + " and " + timeInts[0];
							} else {
								where += " or timeInt between " + timeInts[1] + " and " + timeInts[0];
							}
						}
					} else {
						if(i == 0){
							where += " and timeInt = " + timeSpans[i];
						} else {
							where += " or timeInt = " + timeSpans[i];
						}
					}
				}
			}
		}
		if(operator.equals("乘以")){
			oper = "*";
		} else if(operator.equals("除以")) {
			oper = "/";
		} else {
			return null;
		}
		if(columns != null){
			if(columns.length > 0){
				sql.append(" set EditTime = getDate() ");//修改数据时需要修改相应的EditTime
			}
			for(int i = 0; i < columns.length; i++){
				sql.append(" ,");
				sql.append((String)columns[i]);
				sql.append(" = ");
				sql.append((String)columns[i]);
				sql.append(oper);
				sql.append(multiple);
			}
			sql.append(where);
		} else {
			return null;
		}
		
		return sql.toString();
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
	 * 查询表中所有列名
	 * */
	public Object[] queryColumns(String sql, Object[] params){
		JdbcRunner jdbc = null;
		ResultSet rs = null;
		Object[] columns = null;
		try{
			jdbc = new JdbcRunner();
			rs = jdbc.query(sql, params);
			List<String> colList = new LinkedList<String>();
			try {
				while(rs.next()){
					colList.add("[" + rs.getString("colName") + "]");
				}
				columns = colList.toArray();
			} catch (SQLException e) {
				LOG.error(e);
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
		return columns;
	}
	
	/**
	 * 查询表的表名、品种id、表头id
	 * */
	public Map<String, Object> queryTable(String sql, Object[] params){
		JdbcRunner jdbc = null;
		ResultSet rs = null;
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		try{
			jdbc = new JdbcRunner();
			rs = jdbc.query(sql, params);
			if(rs.next()){
				rsltMap.put("dbName", rs.getString("dbName"));
				rsltMap.put("varId", rs.getInt("varId"));
				rsltMap.put("headerId", rs.getInt("headerId"));
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
		return rsltMap;
	}
	
	public static void main(String[] args) {
		UniteUnitAndValue u = new UniteUnitAndValue();
//		String[][] table = FileStrIO.readXls("D:\\Test\\县域数据需要修改情况汇总-20170331(1).xlsx", "Sheet1", 11, 0);
//		int startRow = 1;//此次处理起始行
//		int endRow = 16;//此次处理截止行（含该行）
		
//		String[][] table = FileStrIO.readXls("D:\\Test\\县域数据需要修改情况汇总-20170331(1).xlsx", "泉州市", 11, 0);
//		int startRow = 1;//此次处理起始行
//		int endRow = 51;//此次处理截止行（含该行）
		
		String[][] table = FileStrIO.readXls("D:\\Test\\县域数据需要修改情况汇总-20170331(1).xlsx", "其他市", 11, 0);
		int startRow = 5;//此次处理起始行
		int endRow = 29;//此次处理截止行（含该行）
		
		//先检查文件格式
		boolean isRight = u.check(table, startRow, endRow);
		if(!isRight){
			LOG.error("格式不符合要求。");
		}
		//u.modifyUnit(table, startRow, endRow);
		u.modifyValue(table, startRow, endRow);
	}
}
