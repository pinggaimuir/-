package cn.futures.data.util;

import cn.futures.data.jdbc.JdbcRunner;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 针对指定格式创建表头
 * @author bric_yangyulin
 * @date 2017-04-24
 * */
public class BatchCreateHeader {
	
	private static final Logger LOG = Logger.getLogger(BatchCreateHeader.class);
	
	/**
	 * 检查参数最大列数设置是否合适
	 * */
	public void checkParam(String filePath, String sheetName, int maxColumns, int startRowIndex){
		String[][] table = FileStrIO.readXls(filePath, sheetName, maxColumns, startRowIndex);
		for(int rowId = startRowIndex; rowId < table.length; rowId++){
			boolean isEnd = false;
			Set<String> columnNames = new HashSet<String>();
			for(int colId = 2; colId < table[rowId].length; colId++){
				if(table[rowId][colId] == null){
					isEnd = true;
					break;
				} else {
					if(columnNames.contains(table[rowId][colId])){
						LOG.info("rowId = " + rowId + ",headerId = " + table[rowId][0] + "已包含列：" + table[rowId][colId]);
					} else {
						columnNames.add(table[rowId][colId]);
					}
				}
			}
			if(!isEnd){
				LOG.info("rowId = " + rowId + ",headerId = " + table[rowId][0]);
			}
		}
	}
	
	public void parseData(String filePath, String sheetName, int maxColumns, int startRowIndex, int colType){
		String[][] table = FileStrIO.readXls(filePath, sheetName, maxColumns, startRowIndex);
		for(int rowId = startRowIndex; rowId < table.length; rowId++){
			if(table[rowId][0] == null || table[rowId][0].isEmpty()){
				continue;
			}
			int headerId = ((Float)Float.parseFloat(table[rowId][0])).intValue();	//表头id
			String headerName = table[rowId][1];	//表头名称
			Set<String> colFilters = new HashSet<String>();//用于列名查重
			List<String> columnNames = new LinkedList<String>();//存储对应所有列名
			for(int colId = 2; colId < table[rowId].length; colId++){
				if(table[rowId][colId] == null){
					break;
				} else {
					if(colFilters.contains(table[rowId][colId])){
						LOG.warn("rowId = " + rowId + ",headerId = " + table[rowId][0] + ",已包含列：" + table[rowId][colId]);
					} else {
						colFilters.add(table[rowId][colId]);
						columnNames.add(table[rowId][colId]);
					}
				}
			}
			//添加表头
			addHeader(headerId, headerName);
			//添加列
			int colOrder = 1;//列序
			for(String colName: columnNames){
				addColumn(headerId, colName, colOrder, colType);
				colOrder++;
			}
		}
	} 
	
	//添加表头
	public void addHeader(int headerId, String headerName){
		JdbcRunner jdbc = null;
		jdbc = new JdbcRunner();
		String sql = "set identity_insert CFG_TABLE_HEADER ON;INSERT INTO CFG_TABLE_HEADER (id, headerName, status) VALUES ('" + headerId + "', '" + headerName + "', '0');set identity_insert CFG_TABLE_HEADER OFF;";
		LOG.info("添加表头：" + sql);
		try {
			int rslt = jdbc.update(sql);
			if(rslt <=0){
				LOG.info("fail: headerId = " + headerId);
			}
		} catch (Exception e) {
			LOG.info(e);
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
	}
	
	//添加列
	public void addColumn(int headerId, String colName, int colOrder, int colType){
		JdbcRunner jdbc = null;
		jdbc = new JdbcRunner();
		String sql = "INSERT INTO CFG_TABLE_COLUMNS (headerId, colName, oldColName, colOrder, colType, status, unit) VALUES ('"
		+ headerId + "', '" + colName + "', '" + colName + "', '" + colOrder + "', '" + colType + "', '0', '');";
		LOG.info("添加列：" + sql);
		try {
			int rslt = jdbc.update(sql);
			if(rslt <=0){
				LOG.info("fail: headerId = " + headerId + ", colName = " + colName);
			}
		} catch (Exception e) {
			LOG.info(e);
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
	}
	
	public static void main(String[] args) {
		BatchCreateHeader ba = new BatchCreateHeader();
//		ba.checkParam("C:\\Users\\bric_yangyulin\\Desktop\\建表\\商水县数据建表-20170420-技术建表.xlsx", "表头", 55, 1);
		ba.parseData("data/技术建表20170609李喜明.xls", "新建表头", 230, 1, 2);
	}
}
