package cn.futures.test;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.FileStrIO;

public class ImportCoopMapData {
	private static final Logger LOG = Logger.getLogger(ImportCoopMapData.class);
	
	private void importData(String filePath, String sheetName, int colNum){
		String[][] table = FileStrIO.readXls(filePath, sheetName);
		for(int rowId = 2; rowId < table.length; rowId++){//逐行进行处理，一行为一条合作社信息
			String insSql = "INSERT INTO cooperative_map ([cooperative], [province], [city], "
					+ "[county], [small_town], [address], [address_detail], [latitude], [longitude], "
					+ "[coop_type], [operate_type], [legal_repre], [phone], [fammer_num], "
					+ "[operate_income], [land_scale], [cir_land_area], [registered_capital], "
					+ "[remark]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			for(int colId = 0; colId < colNum; colId++){//取用到的colNum列
				if(table[rowId][12] != null){
					table[rowId][12] = table[rowId][12].replace(".", "").replace("E10", "");//手机号在文件中是数字格式的，得到的为科学技术法形式的字符串，需作处理。
					if(table[rowId][12].length() > 13){
						LOG.warn(table[rowId][11] + "联系方式位数不对。");
						table[rowId][12] = null;
					}
				}
				if(table[rowId][colId] != null){
					table[rowId][colId] = table[rowId][colId].replaceAll("\\s*", "");//去除所有空白符
					if(table[rowId][colId].isEmpty()){
						table[rowId][colId] = null;//无值则设为空
					}
				}
			}
			Object[] params = {table[rowId][0], table[rowId][1], table[rowId][2], table[rowId][3], 
					table[rowId][4], table[rowId][5], table[rowId][6], table[rowId][7], table[rowId][8], 
					table[rowId][9], table[rowId][10], table[rowId][11], table[rowId][12], table[rowId][13], 
					table[rowId][14], table[rowId][15], table[rowId][16], table[rowId][17], table[rowId][18]};
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
		ImportCoopMapData im = new ImportCoopMapData();
		im.importData("C:\\Users\\bric_yangyulin\\Desktop\\临时文件\\新建文件夹\\合作社信息-20170405(2).xlsx", "汇总版", 19);
	}
}
