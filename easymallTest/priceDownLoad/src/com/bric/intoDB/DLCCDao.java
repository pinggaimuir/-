package com.bric.intoDB;

import com.bric.crawler.MapInit;
import com.bric.util.Constants;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DLCCDao {
	Logger logger = Logger.getLogger(DLCCDao.class);

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();
	/**
	 * 保存 today 大连持仓数据至数据库
	 * @param file
	 * @param today
	 * */	
	public void saveDLCCData(File file,String today){//递归遍历文件目录
		logger.info("******saving DLCCData******");
		for(File file2:file.listFiles()){			
			if (file2.isDirectory()) {
				saveDLCCData(file2,today);
			}else {
				List list=new ArrayList<>();
				String varName = file2.getParentFile().getParentFile().getName();
				String tableName = MapInit.DLCCTableMap.get(varName);
				String ContractNum = file2.getParentFile().getName();
				int VarId = MapInit.DLCCVarIdMap.get(varName);
				int TimeInt = Integer.valueOf(file2.getName().split("\\.")[0]);
				if(TimeInt == Integer.valueOf(today)){//只匹配当天数据
					try {
						InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.FILE_ENCODING);
						BufferedReader reader = new BufferedReader(inputStreamReader);
						String str = reader.readLine();
						int row = 1;
						while((str=reader.readLine())!=null){
							row++;							
							if (str.contains("总计")) {
								break;
							}else {
								Map<String,String> map=new HashMap<>();
								String[] array = str.split(",");
								map.put("ExchangeId",Constants.DL_EXCHANGE_ID+"");
								map.put("VarId",VarId+"");
								map.put("TimeInt",TimeInt+"");
								map.put("ContractNum",ContractNum);
								try {
									if(array[0]==null || array[0].trim().length()<1 ){
										map.put("成交名次",(row-1)+"");
									}else{											
										map.put("成交名次",array[0]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[1]==null || array[1].trim().length()<1 ){
										map.put("成交会员",null);
									}else{
										map.put("成交会员",array[1]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[2]==null || array[2].trim().length()<1 ){									
										map.put("成交量",0+"");
									}else{
										map.put("成交量",array[2]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[3]==null || array[3].trim().length()<1 ){
										map.put("成交增减",0+"");
									}else{
										map.put("成交增减",array[3]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									if(array[4]==null || array[4].trim().length()<1 ){
										map.put("持买名次",(row-1)+"");
									}else{
										map.put("持买名次",array[4]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[5]==null || array[5].trim().length()<1 ){
										map.put("持买会员",null);
									}else{
										map.put("持买会员",array[5]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								
								try {
									if(array[6]==null || array[6].trim().length()<1 ){
										map.put("持买单量","0");
									}else{
										map.put("持买单量",array[6]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[7]==null || array[7].trim().length()<1 ){
										map.put("持买增减","0");
									}else{
										map.put("持买增减",array[7]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[8]==null || array[8].trim().length()<1 ){
										map.put("持卖名次",(row-1)+"");
									}else{
										map.put("持卖名次",array[8]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[9]==null || array[9].trim().length()<1 ){
										map.put("持卖会员",null);
									}else{
										map.put("持卖会员",array[9]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									if(array[10]==null || array[10].trim().length()<1 ){
										map.put("持卖单量","0");
									}else{
										map.put("持卖单量",array[10]);
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}							
								try {
									if(array[11]==null || array[11].trim().length()<1 ){
										map.put("持卖增减","0");
									}else{
										map.put("持卖增减",array[11]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								list.add(map);
							}
							
						}
						reader.close();
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException:", e);
					} catch (IOException e) {
						logger.error("IOException:", e);
					}
					Map data=new HashMap<>();
					data.put("dbName",tableName);
					data.put("data", list);
					dbm3Dao.saveByDbName(data);
					logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
				}
				
			}
		}
		logger.info("******all finished******");
	}
}
