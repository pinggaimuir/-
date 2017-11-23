package com.bric.intoDB;

import com.bric.analyser.wholePriceAnalyse;
import com.bric.crawler.MapInit;
import com.bric.jdbc.JdbcRunner;
import com.bric.util.Constants;
import com.bric.util.formatData;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Price21foodDao {
	private Logger logger = Logger.getLogger(Price21foodDao.class);

    private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();

    public void save21foodPriceData(File file,String today){

        JdbcRunner jdbc=new JdbcRunner();

        try {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    save21foodPriceData(file2, today);
                } else {
                    List list=new ArrayList<>();
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

                                String Area = formatData.getAreaByMarket(array[1]);
                                String Province = formatData.getProvinceByArea(Area);

                                Map<String,String> map=new HashMap<>();
                                map.put("KindId",KindId+"");
                                map.put("VarId",VarId+"");
                                map.put("TimeInt",array[5]);
                                map.put("Province",Province);
                                map.put("Area",Area);
                                map.put("产品",array[0]);
                                map.put("市场",array[1]);
                                //此属性在目标网址更新后没有了  所以注释掉
//                                  map.put("产地规格",VarId);
                                map.put("最高价",array[2]);
                                map.put("最低价",array[3]);
                                map.put("平均价",array[4]);
                                list.add(map);


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
                        Map data=new HashMap<>();
                        data.put("dbName",tableName);
                        data.put("data", list);
                        data.put("condition", "TimeInt,VarId,市场");
                        dbm3Dao.saveByDbName(data);
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
}
