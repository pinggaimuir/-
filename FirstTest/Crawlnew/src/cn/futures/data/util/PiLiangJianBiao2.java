package cn.futures.data.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.futures.data.jdbc.JdbcRunner;

public class PiLiangJianBiao2 {
	//static private String line=" insert into  CX_Variety values(getdate(),'伊春市（总）',10),";
	//  \t\t(\S+)\tcx_xt_(\S+)\t(\d+)\t(\S+)\r\n  
	//	\t\t"$1",\t"cx_var_$2",\t"$3",\t"$4",\r\n   
	
//	static String sql_def3="insert into CFG_TABLE_META(industryId,industryDetailId,varId,cnName,dbName,"
//			+ "headerId,unit,updatefreq,timeType,creatorId,dataSource,"
//			+ "status,createTime,updateTime, lastOperatorId,sort) "
//			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',1,1,28,"
//			+ "'统计局年鉴',0,getDate(),getDate(),28,#sort)";
	
	private static final Logger LOG = Logger.getLogger(PiLiangJianBiao2.class);
	
	static String sql_def="insert into CFG_TABLE_META_NEW(industryId,industryDetailId,varId,cnName,dbName,"
			+ "headerId,unit,updatefreq,timeType,dutyId,creatorId,dataSource,"
			+ "status,createTime,updateTime, lastOperatorId,sort,quality,crawlerId, dataExplain) "
			+ "VALUES(#hangye,#zihangye,#var,'#cnname','#dbname',#headerid,'#unit',#updatefreq,#timetype,#dutyId,#creatorid,"
			+ "'#datasource',0,getDate(),getDate(),#creatorid,#sort,0,#crawlerId,'#dataExplain')";
	
	public static JdbcRunner jdbc ;
	public static void run() throws Exception{
		jdbc = new JdbcRunner();
		HSSFWorkbook book = null;
		try {
			InputStream input = new FileInputStream("data/新增数据20170614-杨三.xls");
			book = new HSSFWorkbook(input);
//			HSSFSheet sheet = book.getSheetAt(0);
			HSSFSheet sheet = book.getSheet("人工整理数据清单");
			
			int start=1;
			int i;
			for(i=1;i<=sheet.getLastRowNum();i++){				
				if( sheet.getRow(i)==null || sheet.getRow(i).getCell(0)==null){
					continue;
				}
				Process(sheet,i);		
			}
		} catch (IOException e) {
			LOG.error(e);
		}
	}
	
	public static void Process(HSSFSheet sheet,int start) throws Exception{
		
		LOG.info("处理文件第" + start + "行");
		String indust = sheet.getRow(start).getCell(0).getStringCellValue().trim();
		//String indust = "8";//String.valueOf(Double.valueOf(sheet.getRow(start).getCell(0).getNumericCellValue()).intValue());
		String indust_detail = sheet.getRow(start).getCell(1).getStringCellValue().trim();
		String var = sheet.getRow(start).getCell(2).getStringCellValue().trim();		
		
		String sql;
		sql = "select id from CFT_TABLE_INDUSTRY where name like '"+indust+"'";
//		System.out.println(sql);
		ResultSet rs,rs2;
		rs = jdbc.query(sql);
		if(rs.next()){
			indust = rs.getString(1);
		}else{
			//注意：行业表中主键非标识列，所以该插入语句一定会抛出异常。
			sql = "insert into CFT_TABLE_INDUSTRY values ('"+indust+"','"+indust+"','"+indust+"')";
			LOG.info("添加行业：" + sql);//先建行业
			try{
				jdbc.update(sql);
				sql = "select id from CFT_TABLE_INDUSTRY where name like '"+indust+"'";
//				System.out.println(sql);
				rs2 = jdbc.query(sql);
				if(rs2.next()){
					indust = rs2.getString(1);
				}
				rs2.close();
			}catch(Exception e){
				LOG.error("插入行业出错",e);
			}
		}
		rs.close();
		
		sql = "select id from cft_table_industry_detail_new where name like '"+indust_detail+"' and industryId = " + indust;
		LOG.info("子行业查询：" + sql);
		rs = jdbc.query(sql);
		if(rs.next()){
			indust_detail = rs.getString(1);
		}else{
//			sql = "insert into cft_table_industry_detail_new values ('"+indust_detail+"')";
			sql = "INSERT INTO cft_table_industry_detail_new ([name], [industryId], [olddetail]) VALUES ('"+indust_detail+"', '"+indust+"', NULL);";
			LOG.info(sql);//建子行业
			try{
				jdbc.update(sql);
				sql = "select id from cft_table_industry_detail_new where name like '"+indust_detail+"' and industryId = " + indust;
				LOG.info("子行业查询：" + sql);
				rs2 = jdbc.query(sql);
				if(rs2.next()){
					indust_detail = rs2.getString(1);
				}
				rs2.close();
			}catch(Exception e){
				LOG.error("插入子行业出错", e);
			}
		}
		rs.close();

		sql = "select id from cx_variety_new where vname like '"+var+"'";
//		LOG.info("品种查询" + sql);
		rs = jdbc.query(sql);
		if(rs.next()){
			var = rs.getString(1);
		}else{
//			String sql2 = "insert into CX_Variety(VName,industryId) values('"+var+"',"+indust+");";
			String sql2 = "INSERT INTO cx_variety_new ([EditTime], [VName], [industryDetailId], [oldvar]) VALUES (GETDATE(), '"+var+"', '" + indust_detail + "', NULL);";
			LOG.info(sql2);
			jdbc.update(sql2);	
			sql = "select id from cx_variety_new where vname like '"+var+"'";
//			LOG.info("品种查询" + sql);
			rs2 = jdbc.query(sql);
			if(rs2.next()){
				var = rs2.getString(1);
			}else{
				LOG.error("fatal error!");
				System.exit(0);
			}
			rs2.close();
		}
		rs.close();

		String cnname = sheet.getRow(start).getCell(3).getStringCellValue().trim();//中文名
		String dbname = sheet.getRow(start).getCell(4).getStringCellValue().trim();//物理表名
		String header =  String.valueOf(Double.valueOf(sheet.getRow(start).getCell(5).getNumericCellValue()).intValue());//表头id
		String unit = sheet.getRow(start).getCell(6).getStringCellValue().trim();//单位
		String datasource = sheet.getRow(start).getCell(7).getStringCellValue().trim();//数据来源
		String datatype = sheet.getRow(start).getCell(8).getStringCellValue().trim();//时间类型
		if(datatype.contains("年")){
			datatype = "1";
		}else if(datatype.contains("月")){
			datatype = "2";
		}else if(datatype.contains("周")){
			datatype = "3";
		}else if(datatype.contains("日")){
			datatype = "4";
		}else{
			LOG.error("Fatal error!");
			System.exit(0);
		}

		String freq = sheet.getRow(start).getCell(9).getStringCellValue().trim();//更新频次
		if(freq.contains("年")){
			freq = "1";
		}else if(freq.contains("月")){
			freq = "2";
		}else if(freq.contains("周")){
			freq = "3";
		}else if(freq.contains("日")){
			freq = "4";
		}else if(freq.contains("季")){
			freq = "5";
		}else{
			LOG.error("Fatal error!");
			System.exit(0);
		}
		//创建者id，采用58（数据中心），59为技术部, 36为李喜明,
		String creator =  "36";//String.valueOf(Double.valueOf(sheet.getRow(start).getCell(10).getNumericCellValue()).intValue());
		String creatorName = sheet.getRow(start).getCell(10).getStringCellValue().trim();
		if(creatorName.contains("杨三")){
			creator = "63";
		} else if(creatorName.contains("李喜明")) {
			creator = "36";
		}
		//负责人id, 59为技术部,64为仇华
		String dutyName = sheet.getRow(start).getCell(11).getStringCellValue().trim();
		String dutyId = "58";
		if(dutyName.contains("技术部")){
			dutyId = "59";
		} else if(dutyName.contains("杨三")) {
			dutyId = "63";
		} else if(dutyName.contains("陈李超")){
			dutyId = "94";
		}
		
		//所属爬虫id
		String crawlerId = "NULL";
		String dataExplain = "NULL";//说明/备注;
		if(sheet.getRow(start).getCell(12) != null){
			dataExplain = sheet.getRow(start).getCell(12).getStringCellValue().trim();
		}
		String sql1=sql_def;
		sql1=sql1.replaceAll("#hangye", indust);
		sql1=sql1.replaceAll("#zihangye", indust_detail);
		sql1=sql1.replaceAll("#var", var );
		sql1=sql1.replaceAll("#cnname",cnname);
		sql1=sql1.replaceAll("#dbname", dbname);
		sql1=sql1.replaceAll("#headerid", header);
		sql1=sql1.replaceAll("#unit", unit);
		sql1=sql1.replaceAll("#updatefreq", freq);
		sql1=sql1.replaceAll("#timetype", datatype);
		sql1=sql1.replaceAll("#dutyId", dutyId);
		sql1=sql1.replaceAll("#creatorid", creator);
		sql1=sql1.replaceAll("#datasource", datasource);
		sql1=sql1.replaceAll("#crawlerId", crawlerId);
		sql1=sql1.replaceAll("#dataExplain", dataExplain);
		
//		sql = "select * from CFG_TABLE_META_NEW where varid="+var+" and dbName='"+dbname+"' and cnName='" + cnname + "'";
		sql = "select * from CFG_TABLE_META_NEW where varid="+var+" and cnName='" + cnname + "'";
//		System.out.println(sql);
		rs = jdbc.query(sql);
		if(rs.next()){
			LOG.info("已经存在同一品种同一中文名的表定义");
			System.exit(1);
		}
		
		sql = "select max(sort)+1 from CFG_TABLE_META_NEW where industryId="+indust+" and industryDetailId="
				+indust_detail+" and varId="+var;
		LOG.info(sql);
		rs = jdbc.query(sql);
		String sort="1";
		if(rs.next()){
			sort = rs.getString(1);
			if(sort == null)
				sort = "1";
		}else{
			sort = "1";
		}
		rs.close();
		
		sql1=sql1.replaceAll("#sort", sort);

		LOG.info(sql1);
		try{
			jdbc.update(sql1);
		}catch(Exception e){
			LOG.error("出错行："+sql1, e);	
		}			
			
	}
	/**
	 * 初步检查有没有目录一样的情况。
	 * */
	public static void check(){
		String[][] table = FileStrIO.readXls("data/新增数据20170614-杨三.xls", "人工整理数据清单", 15, 1);
		Set<String> treeNames = new HashSet<String>();
		for(int rowId = 1; rowId < table.length; rowId++){
			String treeName = table[rowId][0] + "-" + table[rowId][1] + "-" + table[rowId][2] + "-" + table[rowId][3];
			if(!treeNames.add(treeName)){
				LOG.info("行号 = " + (rowId + 1) + ", 目录 = " + treeName);
			}
		}
	}
	public static void main(String[] args) {
		try{
			run();
//			check();
		}catch(Exception e){
			LOG.error(e);
		}
	}
}

