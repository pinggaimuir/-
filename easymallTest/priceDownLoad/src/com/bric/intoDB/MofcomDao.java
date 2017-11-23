package com.bric.intoDB;

import com.bric.crawler.MapInit;
import com.bric.util.Constants;
import com.bric.util.formatData;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MofcomDao {
	private Logger logger = Logger.getLogger(MofcomDao.class);

	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();

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
				List list=new ArrayList<>();
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
							Map<String,String> map=new HashMap<>();
							String[] array = str.split(",");							
							if (!array[0].equals("0")) {
								if (curMonth == Integer.valueOf(array[1])) {//只取最近一个月数据
									String month = Integer.valueOf(array[1])<10?"0"+array[1]:array[1];
									String Area = formatData.getAreaByMarket(market);
									String Province = formatData.getProvinceByArea(Area);

									map.put("KindId",KindId+"");
									map.put("VarId",VarId+"");
									map.put("TimeInt",year+month);
									map.put("Province",Province);
									map.put("Area",Area);
									map.put("产品",Product);
									map.put("市场",market);
									map.put("价格",KindId+"");
									map.put("KindId",array[0]);
									list.add(map);
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
					if(list!=null){
						if (list.size()>0) {
							//数据通过dbm3接口进行存储
							Map data=new HashMap<>();
							data.put("dbName",tableName);
							data.put("data", list);
							dbm3Dao.saveByDbName(data);
							System.out.println(Product);
							logger.info("insert "+file2.getParentFile().getName()+"-"+file2.getName()+" data into table "+tableName);
						}
					}					
				}
			}
		}
	}
}
