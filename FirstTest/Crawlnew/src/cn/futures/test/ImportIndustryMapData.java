package cn.futures.test;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.FileStrIO;

public class ImportIndustryMapData {
	
	private static final Logger LOG = Logger.getLogger(ImportIndustryMapData.class);
	private void importData(String filePath, String sheetName){
		String[][] table = FileStrIO.readXls(filePath, sheetName);
		for(int rowId = 2; rowId < table.length; rowId++){//逐行进行处理，一行为一条工厂信息
			String insSql = "INSERT INTO ind_map ([factory], [enterprise], [province], [city], "
					+ "[county], [coordinate], [fac_type], [ind_chain], [material], [product], "
					+ "[daily_capacity], [yearly_capacity], [rec_year], [unit]) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			for(int colId = 0; colId <= 13; colId++){//取用到的14列
				if(table[rowId][colId] != null){
					table[rowId][colId] = table[rowId][colId].replaceAll("\\s*", "").replaceAll("[，、]", ",");//去除所有空白符
					if(table[rowId][colId].isEmpty()){
						table[rowId][colId] = null;//无值则设为空
					}
				}
			}
			//统一为"经度,纬度"格式
			if(table[rowId][12] != null && !table[rowId][12].isEmpty()){
				String[] tempArr = table[rowId][12].split(",");
				if(Double.parseDouble(tempArr[0]) < Double.parseDouble(tempArr[1])){
					table[rowId][12] = tempArr[1] + "," + tempArr[0];
				}
			}
			table[rowId][6] = table[rowId][6] == null ? null : table[rowId][6].replaceAll("\\.\\d+", "");
			Object[] params = {table[rowId][7], table[rowId][8], table[rowId][9], table[rowId][10], 
					table[rowId][11], table[rowId][12], table[rowId][0], table[rowId][1], table[rowId][2], 
					table[rowId][3], table[rowId][4], table[rowId][5], table[rowId][6], table[rowId][13]};
			update(insSql, params);
		}
	} 
	
	/**
	 * 更新数据
	 * */
	private void update(String sql, Object[] params){
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
	
	public static void main(String[] args) {
		ImportIndustryMapData imp = new ImportIndustryMapData();
		String filePath = "C:\\Users\\bric_yangyulin\\Desktop\\临时文件\\新建文件夹\\工厂地址林国发调整-2.xlsx";
		String[][] files = {{filePath, "谷物"}, {filePath, "食糖"}, 
				{filePath, "饲料"}, {filePath, "生猪"}, {filePath, "油脂"}};
		for(String[] file: files){
			imp.importData(file[0], file[1]);
		}
	}
}
