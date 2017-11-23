package com.bric.intoDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bric.crawler.MapInit;
import com.bric.intoDB.model.CZCEModel;
import com.bric.jdbc.JdbcRunner;
import com.bric.util.Constants;

public class CZCEDao {
	private Logger logger = Logger.getLogger(CZCEDao.class);
	
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
				List<CZCEModel> CZCEModelList = new LinkedList<CZCEModel>();
				String varName = file2.getParentFile().getParentFile().getName();
				String tableName = MapInit.CZCEtableMap.get(varName);
				String ContractNum = file2.getParentFile().getName();
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
								CZCEModel model = new CZCEModel();
								String[] array = str.split(",");
								model.setExchangeId(101);
								model.setVarId(VarId);
								model.setTimeInt(TimeInt);
								model.setContractNum(ContractNum);
								try {
									model.setDealComp(array[1]);
								} catch (ArrayIndexOutOfBoundsException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									model.setDealAmount(Integer.valueOf(array[2]));
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									model.setDealChange(Integer.valueOf(array[3]));
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									model.setBuyComp(array[4]);
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								
								try {
									model.setBuyAmount(Integer.valueOf(array[5]));
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									model.setBuyChange(Integer.valueOf(array[6]));
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									model.setSaleComp(array[7]);
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									model.setSaleAmount(Integer.valueOf(array[8]));
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}							
								try {
									model.setSaleChange(Integer.valueOf(array[9]));
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									int rank = Integer.valueOf(array[0]);
									if (!"".equals(model.getDealComp()))
										model.setDealRank(rank);
									if (!"".equals(model.getBuyComp()))
										model.setBuyRank(rank);
									if (!"".equals(model.getSaleComp()))
										model.setSaleRank(rank);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								CZCEModelList.add(model);
							}
							
						}
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException:", e);
					} catch (IOException e) {
						logger.error("IOException:", e);
					}
					
					save(CZCEModelList, tableName);
					logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
				}
				
			}
		}
		logger.info("******all finished******");
	}
	
	private void save(List<CZCEModel> list, String table) {
		String sql = "insert into " + table + " (EditTime, ExchangeId,VarId, TimeInt,ContractNum, 成交名次,成交会员, 成交量, 成交增减, 持买名次, 持买会员, "+
	"持买单量, 持买增减, 持卖名次,持卖会员,持卖单量,持卖增减) values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		Object[][] vals = new Object[list.size()][16];
		for (int i = 0; i < list.size(); i++){
			CZCEModel p = list.get(i);
			vals[i][0] = p.getExchangeId();
			vals[i][1] = p.getVarId();
			vals[i][2] = p.getTimeInt();
			vals[i][3] = p.getContractNum();
			vals[i][4] = p.getDealRank();
			vals[i][5] = p.getDealComp();
			vals[i][6] = p.getDealAmount();
			vals[i][7] = p.getDealChange();
			vals[i][8] = p.getBuyRank();
			vals[i][9] = p.getBuyComp();
			vals[i][10] = p.getBuyAmount();
			vals[i][11] = p.getBuyChange();
			vals[i][12] = p.getSaleRank();
			vals[i][13] = p.getSaleComp();
			vals[i][14] = p.getSaleAmount();
			vals[i][15] = p.getSaleChange();
		}
		
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate(sql, vals);
			jdbc.endTransaction();
		} catch (Exception e){
			logger.error("insert data into DB error",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				logger.error("DB Transaction rollback error",e1);
			}
		} finally {
			if (jdbc!=null) {
				jdbc.release();			
			}

		}
	}
	
}
