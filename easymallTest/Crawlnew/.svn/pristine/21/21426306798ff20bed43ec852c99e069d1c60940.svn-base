package cn.futures.data.util;

import cn.futures.data.jdbc.JdbcRunner;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PiLiangJianBiao3 {
	//static private String line=" insert into  CX_Variety values(getdate(),'伊春市（总）',10),";
	//  \t\t(\S+)\tcx_xt_(\S+)\t(\d+)\t(\S+)\r\n  
	//	\t\t"$1",\t"cx_var_$2",\t"$3",\t"$4",\r\n   
	
//	static String sql_def3="insert into CFG_TABLE_META(industryId,industryDetailId,varId,cnName,dbName,"
//			+ "headerId,unit,updatefreq,timeType,creatorId,dataSource,"
//			+ "status,createTime,updateTime, lastOperatorId,sort) "
//			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',1,1,28,"
//			+ "'统计局年鉴',0,getDate(),getDate(),28,#sort)";
	
	static String sql_def="insert into CFG_TABLE_META(industryId,industryDetailId,varId,cnName,dbName,"
			+ "headerId,unit,updatefreq,timeType,creatorId,dataSource,"
			+ "status,createTime,updateTime, lastOperatorId,sort) "
			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',#updatefreq,#timetype,#creatorid,"
			+ "'#datasource',0,sysdate(),sysdate(),#creatorid,#sort)";
	
	static String sql_temp = "update CFG_TABLE_META set memo='#memo',dataExplain='#memo' " +
			" where varid=(select id from cx_variety where vname like '#varName') " +
			" and cnName like '#cnName'  and dbName like '#dbName' ";
	
	
	static int varCol=2,cnCol=3,dbCol=4,freqCol=5,noteCol=5;
			
	public static JdbcRunner jdbc ;
	public static void run() throws Exception{
		//jdbc = new JdbcRunner();
		HSSFWorkbook book = null;
		try {
			InputStream input = new FileInputStream("data/月度数据提醒结果20160321.xls");
			book = new HSSFWorkbook(input);
			HSSFSheet sheet = book.getSheetAt(1);			
			int start=1;
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
	
	public static void Process(HSSFSheet sheet,int start) throws Exception{

		//String id = String.valueOf(Double.valueOf(sheet.getRow(start).getCell(0).getNumericCellValue()).intValue());
		
		String varName = sheet.getRow(start).getCell(varCol).getStringCellValue();
		String cnName = sheet.getRow(start).getCell(cnCol).getStringCellValue();
		String dbName = sheet.getRow(start).getCell(dbCol).getStringCellValue();
		String memo = sheet.getRow(start).getCell(noteCol).getStringCellValue();
		

		//String note = sheet.getRow(start).getCell(noteCol).getStringCellValue();
				

		String sql1=sql_temp;

		//sql1=sql1.replaceAll("#dataExplain", noteCol);
		//sql1=sql1.replaceAll("#memo", dataExplain);
		//sql1=sql1.replaceAll("#updatetimememo", updatetimememo);
		sql1=sql1.replaceAll("#varName", varName );
		sql1=sql1.replaceAll("#cnName", cnName );
		sql1=sql1.replaceAll("#dbName", dbName );
		sql1=sql1.replaceAll("#memo", memo );

		System.out.println( sql1);
//		try{
//			jdbc.update(sql1);
//		}catch(Exception e){
//			System.out.println("出错行："+sql1);	
//			e.printStackTrace();
//		}			
			
	}
	
	public static void main(String[] args) {
		try{
			run();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

