package com.bric.intoDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bric.analyser.wholePriceAnalyse;
import com.bric.crawler.MapInit;
import com.bric.intoDB.model.MofcomModel;
import com.bric.intoDB.model.Price21foodModel;
import com.bric.jdbc.JdbcRunner;
import com.bric.util.Constants;
import com.bric.util.formatData;

public class MofcomDao {
	private Logger logger = Logger.getLogger(MofcomDao.class);

	public static void main(String[] a){
		new MofcomDao().saveMofcomPriceData(new File(Constants.MOFCOM_ROOT), 2014);
	}
	public void saveMofcomPriceData(File file,int year1){
		String year = String.valueOf(year1);
		int curMonth = formatData.getMonth(0);
		for(File file2:file.listFiles()){			
			if (file2.isDirectory()) {
				saveMofcomPriceData(file2,year1);
			}else {
				List<MofcomModel> MofcomModelList = new LinkedList<MofcomModel>();
				String Product = file2.getParentFile().getParentFile().getParentFile().getName();
				String KindName = file2.getParentFile().getParentFile().getParentFile().getParentFile().getName();
				String tableName = MapInit.marketPriceMofcomTableMap.get(KindName);
				String market = file2.getParentFile().getName();
				String TimeInt = file2.getName().split("\\.")[0];
				int KindId = MapInit.mofcomMarketPriceMap.get(KindName);
				Integer VarId = 0;
				try {
					VarId = MapInit.mofcomKindReflectMap.get(KindName).get(Product);
				} catch (NullPointerException e) {
					logger.error("error:"+KindName+"--"+Product+"--"+market);
					return;
				}
//				Integer VarId = MapInit.mofcomKindReflectMap.get(KindName).get(Product);
				if (TimeInt.equals(year)) {
					InputStreamReader inputStreamReader;
					try {
						inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.FILE_ENCODING);
						BufferedReader reader = new BufferedReader(inputStreamReader);
						String str = "";
						while((str=reader.readLine())!=null){
							MofcomModel model = new MofcomModel();
							String[] array = str.split(",");							
							if (!array[0].equals("0")) {
								if (curMonth == Integer.valueOf(array[1])) {//只取最近一个月数据
									String month = Integer.valueOf(array[1])<10?"0"+array[1]:array[1];
									String Area = formatData.getAreaByMarket(market);
									String Province = formatData.getProvinceByArea(Area);
									
									model.setKindId(KindId);
									model.setVarId(VarId);
									model.setTimeInt(Integer.valueOf(year+month));
									
									model.setProvince(Province);
									model.setArea(Area);//Area
									
									model.setProduct(Product);
									model.setMarket(market);
									model.setPrice(Float.valueOf(array[0]));
									MofcomModelList.add(model);
								}								
							}
							
							
						}
						reader.close();
					} catch (UnsupportedEncodingException e) {
						logger.error("UnsupportedEncodingException", e);
					} catch (FileNotFoundException e) {
						logger.error("FileNotFoundException", e);
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("IOException", e);
					}
					if(MofcomModelList!=null){
						if (MofcomModelList.size()>0) {
							save(MofcomModelList, tableName);
							System.out.println(Product);
							logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
						}
					}					
				}
			}
		}
	}
	private void save(List<MofcomModel> list, String table) {
		String sql = "insert into " + table + " (EditTime, KindId,VarId, MarketId,TimeInt,Province,Area,产品, 市场,价格)"+
	" values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[][] vals = new Object[list.size()][9];
		for (int i = 0; i < list.size(); i++){
			MofcomModel p = list.get(i);
			vals[i][0] = p.getKindId();
			vals[i][1] = p.getVarId();
			vals[i][2] = p.getMarketId();
			vals[i][3] = p.getTimeInt();
			vals[i][4] = p.getProvince();
			vals[i][5] = p.getArea();
			vals[i][6] = p.getProduct();
			vals[i][7] = p.getMarket();
			vals[i][8] = p.getPrice();
					
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
