package cn.futures.data.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.futures.data.jdbc.JdbcRunner;

public class PiLiangJianBiao5_shangshigongsi {
	//static private String line=" insert into  CX_Variety values(getdate(),'伊春市（总）',10),";
	//  \t\t(\S+)\tcx_xt_(\S+)\t(\d+)\t(\S+)\r\n  
	//	\t\t"$1",\t"cx_var_$2",\t"$3",\t"$4",\r\n   
	
	static String sql_def="insert into CFG_TABLE_META(industryId,industryDetailId,varId,cnName,dbName,"
			+ "headerId,unit,creatorId,dataSource,"
			+ "status,createTime,updateTime, lastOperatorId,sort,timeType,updateFreq) "
			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',28,"
			+ "'统计局年鉴',0,getDate(),getDate(),28,#sort,#timeType,#updateFreq)";
	
		
	public static JdbcRunner jdbc ;
	public static void run() throws Exception{
		jdbc = new JdbcRunner();
		HSSFWorkbook book = null;
		try {
			InputStream input = new FileInputStream("data/待建表(1).xls");
			book = new HSSFWorkbook(input);
			HSSFSheet sheet = book.getSheetAt(0);
			
			int start=1;
			int i;
			for(i=1;i<=sheet.getLastRowNum();i++){				
				if( sheet.getRow(i)==null || sheet.getRow(i).getCell(0)==null
						|| sheet.getRow(i).getCell(0).getStringCellValue() ==null
						|| sheet.getRow(i).getCell(0).getStringCellValue().trim().length()<1 
				){	
					if(start != -1){
						Process(sheet,start,i-1);					
					}		
					start = -1;						
				}else{
					if(start==-1)
						start = i;
				}
			}
			if(start!=-1){
				Process(sheet,start,i-1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Process(HSSFSheet sheet,int start,int end) throws Exception{
		String province = sheet.getRow(start).getCell(0).getStringCellValue().trim();
		String city = sheet.getRow(start).getCell(1).getStringCellValue().trim();
		String sql = "insert into CFT_TABLE_INDUSTRY values ('"+province+"','"+province+"','"+province+"')";
		System.out.println(sql);//先建行业
		/*try{
			jdbc.update(sql);
		}catch(Exception e){
			System.out.println("插入行业出错");
			//e.printStackTrace();
		}*/
		/*sql = "insert into CFT_TABLE_INDUSTRY_DETAIL values ('"+city+"')";
		System.out.println(sql);//建子行业
		try{
			jdbc.update(sql);
		}catch(Exception e){
			System.out.println("插入子行业出错");
			//e.printStackTrace();
		}
*/
		String hangye="";//行业ID
		sql = "select id from CFT_TABLE_INDUSTRY where name like '"+province+"'";
		//System.out.println(sql);
		ResultSet rs = jdbc.query(sql);
		if(rs.next()){
			hangye = rs.getString(1);
		}else{
			System.out.println("Fatal error!");
			System.exit(0);
		}
		rs.close();
		
		String zihangye="";//子行业ID
		sql = "select id from CFT_TABLE_INDUSTRY_DETAIL where name like '"+city+"'";
		//System.out.println(sql);
		rs = jdbc.query(sql);
		if(rs.next()){
			zihangye = rs.getString(1);
		}else{
			System.out.println("Fatal error!");
			System.exit(0);
		}
		rs.close();
		
		String citystr = sheet.getRow(start).getCell(2).getStringCellValue().trim();
		
		for(int i=start;i<=end;i++){
			HSSFRow row = sheet.getRow(i);
			if(row ==null){
				System.out.println("fatal error!");
				System.exit(0);
			}
			HSSFCell cell = row.getCell(3);
			if(cell==null)
				continue;
			String region = cell.getStringCellValue().trim();
			if(region==null || region.length()<1)
				continue;
			sql = "insert into CX_Variety values (getdate(),'"+region+"',"+hangye+")";
			System.out.println(sql); //建品种
			try{
				jdbc.update(sql);
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1) ;
			}

			String varid="";//品种ID
			sql = "select id from CX_Variety where vname like '"+region+"'";
			//System.out.println(sql);
			rs = jdbc.query(sql);
			if(rs.next()){
				varid = rs.getString(1);
			}else{
				System.out.println("Fatal error!");
				System.exit(0);
			}
			rs.close();
			
			String regionstr = row.getCell(4).getStringCellValue().trim();
			
			for(int j=start;j<=end;j++){
				if(sheet.getRow(j).getCell(6) ==null || sheet.getRow(j).getCell(6).getStringCellValue().trim().isEmpty())
					continue;
				String cnName = sheet.getRow(j).getCell(6).getStringCellValue().trim();
				String enName = sheet.getRow(j).getCell(7).getStringCellValue().trim();
				Integer header = Double.valueOf(sheet.getRow(j).getCell(8).getNumericCellValue()).intValue();
				String unit   = sheet.getRow(j).getCell(9).getStringCellValue().trim();	
				String timeType = String.valueOf(sheet.getRow(j).getCell(10).getNumericCellValue());				
				String updatefreq = String.valueOf(sheet.getRow(j).getCell(11).getNumericCellValue());	
				
				//查找有没有同样的表头,表复用
				String dbName;
				sql = "select top 1 dbname from cfg_table_meta as t1 where headerID="+header+" and "+varid+" not in (select distinct varid from cfg_table_meta as t2 where t2.dbname=t1.dbname) ";
				rs = jdbc.query(sql);
				if(rs.next()){
					dbName = rs.getString(1);
				}else{
					dbName = "cx_"+citystr+"_"+regionstr+"_"+enName;
				}
				rs.close();
				
								
				String sql1=sql_def;
				sql1=sql1.replaceAll("#hangye", hangye);
				sql1=sql1.replaceAll("#zihangye", zihangye);
				sql1=sql1.replaceAll("#var", varid );
				sql1=sql1.replaceAll("#cnname",cnName);
				sql1=sql1.replaceAll("#dbname", dbName);
				sql1=sql1.replaceAll("#headerid", String.valueOf(header));
				sql1=sql1.replaceAll("#unit", unit);
				Integer paixu= j-start+1;
				sql1=sql1.replaceAll("#sort", paixu.toString());
				sql1=sql1.replaceAll("#timeType", timeType);
				sql1=sql1.replaceAll("#updateFreq", updatefreq);

				//System.out.print(cnName+",");
				System.out.println(sql1);	
				try{
					jdbc.update(sql1);
				}catch(SQLException e){
					System.out.println("出错行："+sql1);	
					e.printStackTrace();
				}
				
			}
			System.out.println();
		}		
	}
	
	public static void main(String[] args) {
		try{
			run();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

