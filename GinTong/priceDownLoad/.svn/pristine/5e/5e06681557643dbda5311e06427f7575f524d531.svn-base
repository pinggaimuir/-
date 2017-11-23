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
import com.bric.intoDB.model.DLCCModel;
import com.bric.jdbc.JdbcRunner;
import com.bric.util.Constants;

public class DLCCDao {
	Logger logger = Logger.getLogger(DLCCDao.class);
	
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
				List<DLCCModel> DLCCModelList = new LinkedList<DLCCModel>();
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
								DLCCModel model = new DLCCModel();
								String[] array = str.split(",");
								model.setExchangeId(Constants.DL_EXCHANGE_ID);
								model.setVarId(VarId);
								model.setTimeInt(TimeInt);
								model.setContractNum(ContractNum);
								try {
									if(array[0]==null || array[0].trim().length()<1 ){
										model.setDealRank(row-1);
									}else{											
										model.setDealRank(Integer.valueOf(array[0]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[1]==null || array[1].trim().length()<1 ){
										model.setDealComp(null);
									}else{
										model.setDealComp(array[1]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[2]==null || array[2].trim().length()<1 ){									
										model.setDealAmount(0);
									}else{
										model.setDealAmount(Integer.valueOf(array[2]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								try {
									if(array[3]==null || array[3].trim().length()<1 ){
										model.setDealChange(0);
									}else{
										model.setDealChange(Integer.valueOf(array[3]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									if(array[4]==null || array[4].trim().length()<1 ){
										model.setBuyRank(row-1);
									}else{
										model.setBuyRank(Integer.valueOf(array[4]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[5]==null || array[5].trim().length()<1 ){
										model.setBuyComp(null);
									}else{
										model.setBuyComp(array[5]);
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								
								try {
									if(array[6]==null || array[6].trim().length()<1 ){
										model.setBuyAmount(0);
									}else{
										model.setBuyAmount(Integer.valueOf(array[6]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[7]==null || array[7].trim().length()<1 ){
										model.setBuyChange(0);
									}else{
										model.setBuyChange(Integer.valueOf(array[7]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[8]==null || array[8].trim().length()<1 ){
										model.setSaleRank(row-1);
									}else{
										model.setSaleRank(Integer.valueOf(array[8]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}
								try {
									if(array[9]==null || array[9].trim().length()<1 ){
										model.setSaleComp(null);
									}else{
										model.setSaleComp((array[9]));
									}
								} catch (ArrayIndexOutOfBoundsException e) {
									
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}							
								try {
									if(array[10]==null || array[10].trim().length()<1 ){
										model.setSaleAmount(0);
									}else{
										model.setSaleAmount(Integer.valueOf(array[10]));
									}
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行");
								}							
								try {
									if(array[11]==null || array[11].trim().length()<1 ){
										model.setSaleChange(0);
									}else{
										model.setSaleChange(Integer.valueOf(array[11]));
									}
								} catch (ArrayIndexOutOfBoundsException e) {
//									System.out.println("出错文件："+varName+"-"+ContractNum+"-"+TimeInt);
								} catch (NumberFormatException e) {
									logger.error("出错文件："+varName+"-"+ContractNum+"-"+TimeInt+"第"+row+"行", e);
								}
								DLCCModelList.add(model);
							}
							
						}
						reader.close();
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException:", e);
					} catch (IOException e) {
						logger.error("IOException:", e);
					}
					
					save(DLCCModelList, tableName);
					logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
				}
				
			}
		}
		logger.info("******all finished******");
	}
	
	private void save(List<DLCCModel> list, String table) {
		String sql = "insert into " + table + " (EditTime, ExchangeId,VarId, TimeInt,ContractNum, 成交名次,成交会员, 成交量, 成交增减, 持买名次, 持买会员, "+
	"持买单量, 持买增减, 持卖名次,持卖会员,持卖单量,持卖增减) values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		Object[][] vals = new Object[list.size()][16];
		for (int i = 0; i < list.size(); i++){
			DLCCModel p = list.get(i);
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
			} finally {
				jdbc.release();
			}
		} finally {
			if (jdbc!=null) {
				jdbc.release();			
			}

		}
	}
	
}
