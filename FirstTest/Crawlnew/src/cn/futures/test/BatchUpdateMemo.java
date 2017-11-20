package cn.futures.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.futures.data.jdbc.JdbcRunner;

public class BatchUpdateMemo {

	
	static String sql_update = "UPDATE CFG_TABLE_META SET memo = '#memo',dataExplain = '#dataExplain' where varId = (select id from CX_Variety where VName = '#VName') and dbName = '#dbName'";
	static String sql_query = "select memo,dataExplain from CFG_TABLE_META,CX_Variety where CFG_TABLE_META.varId = CX_Variety.Id and dbName = '#dbName' and CX_Variety.VName = '#VName'";
	
	public static JdbcRunner jdbc ;
	public static void run(String filePath) throws Exception{
		jdbc = new JdbcRunner();
		Workbook book = null;
		try {
			InputStream input = new FileInputStream(filePath);
			if(filePath.endsWith("xls")){
				book = new HSSFWorkbook(input);
			} else if(filePath.endsWith("xlsx")) {
				book = new XSSFWorkbook(input);
			} else {
				System.out.println("文件格式不合要求");
			}
			Sheet sheet = book.getSheetAt(0);
			int i;
			for(i=1;i<=sheet.getLastRowNum();i++){				
				if( sheet.getRow(i)==null || sheet.getRow(i).getCell(0)==null){
					continue;
				}
				Process(sheet,i);		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Process(Sheet sheet,int start) throws Exception{

		String dbName = sheet.getRow(start).getCell(1).getStringCellValue();//表名
		String memo = null;
		String dataExplain = null;
		String varName = sheet.getRow(start).getCell(3).getStringCellValue();//品种名
		String sqlUpdate=sql_update;
		
		String sqlQuery = sql_query;
		sqlQuery = sqlQuery.replace("#dbName", dbName);
		sqlQuery = sqlQuery.replace("#VName", varName);
		ResultSet rs = null; 
		try{
			rs = jdbc.query(sqlQuery);
			while(rs.next()){
				memo = rs.getString(1);
				dataExplain = rs.getString(2);
			}
		}catch(Exception e){
			System.out.println("查询时出错行："+sqlQuery);	
			e.printStackTrace();
		}finally{
			rs.close();
		}
		if(memo == null || memo.equals("")){
			memo = "农业部停止发布数据";
		} else {
			memo += "。农业部停止发布数据";
		}
		if(dataExplain == null || dataExplain.equals("")){
			dataExplain = "农业部停止发布数据";
		} else {
			dataExplain += "。农业部停止发布数据";
		}
		System.out.println(String.format("dbName:%s  VName:%s  memo:%s  dataExplain:%s", dbName, varName, memo, dataExplain));
		
		sqlUpdate=sqlUpdate.replace("#dbName", dbName);
		sqlUpdate=sqlUpdate.replace("#memo", memo);
		sqlUpdate=sqlUpdate.replace("#dataExplain", dataExplain);
		sqlUpdate=sqlUpdate.replace("#VName", varName);
		System.out.println(dbName + ":" + sqlUpdate);
		try{
			jdbc.update(sqlUpdate);
		}catch(Exception e){
			System.out.println("更新时出错行："+sqlUpdate);	
			e.printStackTrace();
		}			
			
	}
	
	public static void main(String[] args) {
		try{
			run("D:\\蔬菜面积产量备注20160222.xlsx");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
