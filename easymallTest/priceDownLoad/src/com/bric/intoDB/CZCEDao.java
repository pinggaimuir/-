package com.bric.intoDB;

import com.bric.crawler.MapInit;
import com.bric.util.Constants;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CZCEDao {
	private Logger logger = Logger.getLogger(CZCEDao.class);

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();
	
	/**
	 * 保存 today 大连持仓数据至数据库
	 * @param file
	 * @param today
	 * */	
	public void saveCZCEData(File file,String today){//递归遍历文件目录
		logger.info("******saving CZCEData******");
		for(File file2:file.listFiles()){
			if (file2.isDirectory()) {
				System.out.println("directory");
				saveCZCEData(file2,today);
			}else {
				System.out.println("Saving " + file2.getAbsolutePath());
				String varName = file2.getParentFile().getParentFile().getName();
				String tableName = MapInit.CZCEtableMap.get(varName);
				String ContractNum = file2.getParentFile().getName();
				List list=new ArrayList<>();
				int VarId = MapInit.CZCEvarIDMap.get(varName);
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
								map.put("ExchangeId","101");
								map.put("VarId",VarId+"");
								map.put("TimeInt",TimeInt+"");
								map.put("ContractNum",ContractNum);
								try {
									map.put("成交会员",array[1]);
								} catch (ArrayIndexOutOfBoundsException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									map.put("成交量",array[2]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									map.put("成交增减",array[3]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									map.put("持买会员",array[4]);
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								
								try {
									map.put("持买单量",array[5]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									map.put("持买增减",array[6]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									map.put("持卖会员",array[7]);
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									map.put("成交量",array[8]);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}							
								try {
									map.put("持卖增减",array[9]);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									String rank = array[0];
									if (!"".equals(map.get("成交会员")))
										map.put("成交名次",rank);
									if (!"".equals(map.get("持买会员")))
										map.put("持买名次",rank);
									if (!"".equals(map.get("持卖会员")))
										map.put("持卖名次",rank);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								list.add(map);
							}
							
						}
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
