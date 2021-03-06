package cn.futures.data.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.futures.data.jdbc.JdbcRunner;

public class PiLiangJianBiao {
	//static private String line=" insert into  CX_Variety values(getdate(),'伊春市（总）',10),";
	//  \t\t(\S+)\tcx_xt_(\S+)\t(\d+)\t(\S+)\r\n  
	//	\t\t"$1",\t"cx_var_$2",\t"$3",\t"$4",\r\n   
	
	static String sql_def="insert into CFG_TABLE_META_NEW(industryId,industryDetailId,varId,cnName,dbName,"
			+ "headerId,unit,updatefreq,timeType,creatorId,dataSource,"
			+ "status,createTime,updateTime, lastOperatorId,sort,quality) "
			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',1,1,28,"
			+ "'统计局年鉴',0,getDate(),getDate(),28,#sort,0)";
	
	static String sql_def2="insert into CFG_TABLE_META_NEW(industryId,industryDetailId,varId,cnName,dbName,"
			+ "headerId,unit,updatefreq,timeType,creatorId,dataSource,"
			+ "status,createTime,updateTime, lastOperatorId,sort,quality) "
			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',#updatefreq,#timetype,#creatorid,"
			+ "'#datasource',0,getDate(),getDate(),#creatorid,#sort,0)";
	
	public static JdbcRunner jdbc ;
	private static final Logger log = Logger.getLogger(PiLiangJianBiao.class);
	
	public static void run() throws Exception{
		jdbc = new JdbcRunner();
		HSSFWorkbook book = null;
		try {
			InputStream input = new FileInputStream("data/数据库建表（辽宁省）(1).xls");
			book = new HSSFWorkbook(input);
//			HSSFSheet sheet = book.getSheetAt(0);
			HSSFSheet sheet = book.getSheet("辽宁县域数据建表");
			
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
			log.error(e.getMessage());
		}
	}
	
	public static void Process(HSSFSheet sheet,int start,int end) throws Exception{
		log.info("start: " + start + " --- end: " + end);
		String province = sheet.getRow(start).getCell(0).getStringCellValue().trim();
		String city = sheet.getRow(start).getCell(1).getStringCellValue().trim();
		String hangye="";//行业ID
		String zihangye="";//子行业ID
		
		String sql;
		sql = "select id from CFT_TABLE_INDUSTRY where name like '"+province+"'";
//		System.out.println(sql);
		ResultSet rs,rs2;
		rs = jdbc.query(sql);
		if(rs.next()){
			hangye = rs.getString(1);
			log.info("行业：" + province + "，已存在。");
		}else{
			sql = "insert into CFT_TABLE_INDUSTRY values ('"+province+"','"+province+"','"+province+"')";
			log.info("添加行业：" + sql);//先建行业
			try{
				jdbc.update(sql);
				sql = "select id from CFT_TABLE_INDUSTRY where name like '"+province+"'";
//				System.out.println(sql);
				rs2 = jdbc.query(sql);
				if(rs2.next()){
					hangye = rs2.getString(1);
				}else{
					log.error("Fatal error!");
					System.exit(0);
				}
				rs2.close();
			}catch(Exception e){
				log.error("插入行业出错",e);
			}
		}
		rs.close();
		
		sql = "select id from CFT_TABLE_INDUSTRY_DETAIL_NEW where name like '"+city+"' and industryId = " + hangye;
		log.info("子行业查询：" + sql);
		rs = jdbc.query(sql);
		if(rs.next()){
			zihangye = rs.getString(1);
			log.info("子行业：" + city + "，已存在。");
		}else{
			sql = "insert into CFT_TABLE_INDUSTRY_DETAIL_NEW ([name], [industryId]) values ('"+city+"', " + hangye + ")";
			log.info("添加子行业：" + sql);//建子行业
			try{
				jdbc.update(sql);
				sql = "select id from CFT_TABLE_INDUSTRY_DETAIL_NEW where name like '"+city+"' and industryId = " + hangye;
				log.info("子行业查询：" + sql);
				rs2 = jdbc.query(sql);
				if(rs2.next()){
					zihangye = rs2.getString(1);
				}else{
					log.error("Fatal error!");
					System.exit(0);
				}
				rs2.close();
			}catch(Exception e){
				log.error("插入子行业出错",e);
			}
		}
		rs.close();
		
		String citystr = sheet.getRow(start).getCell(2).getStringCellValue().trim();
		
		for(int i=start;i<=end;i++){
			HSSFRow row = sheet.getRow(i);
			if(row ==null){
				log.error("fatal error!");
				System.exit(0);
			}
			HSSFCell cell = row.getCell(3);
			if(cell==null)
				continue;
			String region = cell.getStringCellValue().trim();
			if(region==null || region.length()<1)
				continue;
			
			String varid="";//品种ID
			sql = "select id from CX_Variety_new where vname like '"+region+"'";
			log.info("品种查询：" + sql);
			rs = jdbc.query(sql);
			if(rs.next()){
				varid = rs.getString(1);
				log.info("品种：" + region + "，已存在。");
			}else{
				String sql2 = "insert into CX_Variety_new ([EditTime], [VName], [industryDetailId]) values (getdate(),'"+region+"',"+zihangye+")";
				log.info("添加品种：" + sql2);
				jdbc.update(sql2);	
				sql = "select id from CX_Variety_new where vname like '"+region+"'";
				log.info("品种查询：" + sql);
				rs2 = jdbc.query(sql);
				if(rs2.next()){
					varid = rs2.getString(1);
				}else{
					log.error("fatal error!");
					System.exit(0);
				}
				rs2.close();
			}
			rs.close();
			
			String regionstr = row.getCell(4).getStringCellValue().trim();
			
			for(int j=start;j<=end;j++){
				if(sheet.getRow(j).getCell(6) ==null || sheet.getRow(j).getCell(6).getStringCellValue().trim().isEmpty())
					continue;
				String cnName = sheet.getRow(j).getCell(6).getStringCellValue().trim();
				
				sql = "select top 1 id from cfg_table_meta_new where varId = " + varid + " and cnName = '" + cnName +"'";
				rs = jdbc.query(sql);
				if(rs.next()){//行业、品种、中文名唯一确定一个逻辑表，若该逻辑表在cfg_table_meta_new中已有信息，则不再插入。由于很多地方用的品种-中文名确定唯一逻辑表，故对此进行监视。
					log.info("品种：" + region + "--中文名：" + cnName + "，已存在不再插入cfg_table_meta_new。");
					continue;
				}
				
				String enName = sheet.getRow(j).getCell(7).getStringCellValue().trim();
				Integer header = Double.valueOf(sheet.getRow(j).getCell(8).getNumericCellValue()).intValue();
				String unit   = sheet.getRow(j).getCell(9).getStringCellValue().trim();	
				
				//查找有没有同样的表头,表复用
				String dbName;
				sql = "select top 1 dbname from cfg_table_meta_new as t1 where headerID="+header+" and "+varid+" not in (select distinct varid from cfg_table_meta_new as t2 where t2.dbname=t1.dbname) and industryid>8";
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

				log.info(cnName+",");
				log.info(sql1);	
				try{
					jdbc.update(sql1);
				}catch(SQLException e){
					log.info("出错行："+sql1,e);	
				}
				
			}
			log.info("\n");
		}		
	}
	
	public static void main(String[] args) {
		try{
			run();
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
}

