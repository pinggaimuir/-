package com.bric.intoDB;

import com.bric.analyser.wholePriceAnalyse;
import com.bric.crawler.MapInit;
import com.bric.intoDB.model.Price21foodModel;
import com.bric.jdbc.JdbcRunner;
import com.bric.util.Constants;
import com.bric.util.formatData;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Price21foodDao {
	private Logger logger = Logger.getLogger(Price21foodDao.class);


    public void save21foodPriceData(File file,String today){

        JdbcRunner jdbc=new JdbcRunner();

        try {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    save21foodPriceData(file2, today);
                } else {
                    List<Price21foodModel> price21foodModelList = new LinkedList<Price21foodModel>();
                    String Product = file2.getParentFile().getName();
                    String KindName = file2.getParentFile().getParentFile().getName();
                    String tableName = MapInit.marketPrice21foodTableMap.get(KindName);
                    int TimeInt = Integer.valueOf(file2.getName().split("\\.")[0]);
                    int KindId = MapInit.nameKindIdMap21food.get(KindName);
                    Integer VarId = MapInit.nameReflectMap21food.get(KindName).get(Product);
                    if (TimeInt == Integer.valueOf(today)) {
                        InputStreamReader inputStreamReader;
                        try {
                            inputStreamReader = new InputStreamReader(new FileInputStream(file2), Constants.FILE_ENCODING);
                            BufferedReader reader = new BufferedReader(inputStreamReader);
                            String str = "";
                            while ((str = reader.readLine()) != null) {
                                String[] array = str.split(",");
                                //判断此记录在数据库中是否已经存在，不存在则插入
                                if (queryIsInsert(jdbc, tableName, VarId, Integer.parseInt(array[5]), array[1])== "") {
                                    String Area = formatData.getAreaByMarket(array[1]);
                                    String Province = formatData.getProvinceByArea(Area);
                                    Price21foodModel model = new Price21foodModel();
                                    model.setKindId(KindId);
                                    model.setVarId(VarId);
                                    model.setTimeInt(Integer.parseInt(array[5]));
                                    model.setProvince(Province);
                                    model.setArea(Area);//Area
                                    model.setProduct(array[0]);
                                    model.setMarket(array[1]);
                                    //此属性在目标网址更新后没有了
//							model.setSourceAndStd(array[2]);
                                    model.setMaxPrice(Float.parseFloat(array[2]));
                                    model.setMinPrice(Float.parseFloat(array[3]));
                                    model.setAveragePrice(Float.parseFloat(array[4]));

                                    price21foodModelList.add(model);
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

                        save(price21foodModelList, tableName);
                        System.out.println(Product);
                        new wholePriceAnalyse().analyseWholePrice(today, String.valueOf(VarId), tableName, MapInit.analyseDataMap.get(Product), MapInit.productToKindMap.get(Product));
                        logger.info("insert " + file2.getParentFile().getName() + "-" + file2.getName() + " data into table " + tableName);
                    }

                }
            }
        }finally{
            jdbc.release();
        }
	}

    /**
     * 查询数据库表中该VarId和TimeInt和market的数据是否已经插入
     * @param tableName 数据库表明
     * @param varId
     * @param timeInt
     * @return 返回查询出的字符串
     */
    private String queryIsInsert(JdbcRunner jdbc,String tableName,int varId,int timeInt,String market){
        String sql="select TimeInt from "+tableName+" where varId="+varId+" and TimeInt="+timeInt+" and 市场='"+market+"'";
        //数据库连接
        try{
            ResultSet rs;
            rs = jdbc.query(sql);
            if (rs.next())
                return rs.getString(1);
        } catch (Exception e){
            logger.error("",e);
        }
        return "";

    }
	private void save(List<Price21foodModel> list, String table) {
		String sql = "insert into " + table + " (EditTime, KindId,VarId, MarketId,TimeInt,Province,Area,产品, 市场,产地规格, 最高价, 最低价, 平均价)"+
	" values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
		Object[][] vals = new Object[list.size()][12];
		for (int i = 0; i < list.size(); i++){
			Price21foodModel p = list.get(i);
			vals[i][0] = p.getKindId();
			vals[i][1] = p.getVarId();
			vals[i][2] = p.getMarketId();
			vals[i][3] = p.getTimeInt();
			vals[i][4] = p.getProvince();
			vals[i][5] = p.getArea();
			vals[i][6] = p.getProduct();
			vals[i][7] = p.getMarket();
			vals[i][8] = p.getSourceAndStd();
			vals[i][9] = p.getMaxPrice();
			vals[i][10] = p.getMinPrice();
			vals[i][11] = p.getAveragePrice();			
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
