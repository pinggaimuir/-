package com.bric.intoDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bric.jdbc.JdbcRunner;
import com.bric.util.Constants;
import com.bric.util.formatData;
import com.bric.crawler.Beginner;
import com.bric.crawler.MapInit;
import com.bric.crawler.download.YmtDataFetch;
import com.bric.intoDB.model.YMTPriceModel;

public class PriceDao {

	public static String[] methodArray = {"setCountry","setBeiJing","setTianJin","setShangHai","setChongQing",
		"setHeBei","setShanXi1","setNeiMengGu","setLiaoNing","setJiLin","setHeiLongJiang","setJiangSu","setZheJiang","setAnHui","setFuJian",
		"setJiangXi","setShanDong","setHeNan","setHuBei","setHuNan","setGuangDong","setGuangXi","setHaiNan",
		"setSiChuan","setGuiZhou","setYunNan","setXiZang","setShanXi3","setGanSu","setQingHai","setNingXia",
		"setXinJiang","setXiangGang","setAoMen"};
	
	public void savePriceData(){
		System.out.println("begin...");
		for(String name:MapInit.nameMap.keySet()){//大类别：蔬菜、水产...
			Map<Integer, String> innerMap = MapInit.nameMap.get(name);
			for(int id:innerMap.keySet()){//小类别如蔬菜中的洋白菜、豆角...
				List<YMTPriceModel> priceModelList = new LinkedList<YMTPriceModel>();
				for (int i = 0; i < YmtDataFetch.placeArray.length; i++) {//省份
					System.out.println("reading "+innerMap.get(id)+" "+YmtDataFetch.placeArray[i]);
					
					String pointDir = Constants.YMTDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"
							+YmtDataFetch.placeArray[i]+".txt";
					File file = new File(pointDir);
					try {
						BufferedReader bufferedReader = new BufferedReader(
								new FileReader(file));
						String str = "";
						if (i==0) {//每行创建一个YMTPriceModel对象，对应存储以后省份的对应行数据，bug:没有考虑“全国”没有数据的情况
							while((str=bufferedReader.readLine())!=""){
								String[] array = {};
								try {
									array = str.split(",");
								} catch (Exception e) {
									// TODO: handle exception
									break;
//									e.printStackTrace();
								}
								
								
								YMTPriceModel priceModel = new YMTPriceModel();
								priceModel.setKindId(MapInit.nameKindIdMap.get(name));
								priceModel.setVarId(id);
								priceModel.setTimeInt(formatData.getTimeInt(array[0]));
								priceModel.setCountry(Float.parseFloat(array[1]));
								priceModelList.add(priceModel);
							}
							bufferedReader.close();
						}else {
							int index=0;
							int size = priceModelList.size();
							while((str=bufferedReader.readLine())!="" && str != null && index < size){
								YMTPriceModel priceModel = priceModelList.get(index);
								try {//动态调用省份对应的set方法
									Method method = priceModel.getClass().getMethod(methodArray[i], new Class[]{float.class});									
									method.invoke(priceModel, new Object[]{Float.parseFloat(str.split(",")[1])});
									index+=1;
								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NullPointerException e) {
									// TODO: handle exception
									System.out.println(str);
								}
								
							}
							bufferedReader.close();
						}
						
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}
				
				save(priceModelList, "CX_MarketPrice");
				System.out.println(innerMap.get(id)+" finished");
			}
		}
	}
	
	private void save(List<YMTPriceModel> list, String table) {
		String sql = "insert into " + table + " (EditTime, KindId,VarId, TimeInt, 全国,北京, 天津, 上海, 重庆, 河北, 山西, 内蒙, 辽宁,吉林,黑龙江,"+
	"江苏,浙江,安徽,福建,江西,山东,河南,湖北,湖南,广东,广西,海南,四川,贵州,云南,西藏,陕西,甘肃,青海,宁夏,新疆,香港,澳门) " +
				" values (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[][] vals = new Object[list.size()][37];
		for (int i = 0; i < list.size(); i++){
			YMTPriceModel p = list.get(i);
			vals[i][0] = p.getKindId();
			vals[i][1] = p.getVarId();
			vals[i][2] = p.getTimeInt();
			vals[i][3] = p.getCountry();
			vals[i][4] = p.getBeiJing();
			vals[i][5] = p.getTianJin();
			vals[i][6] = p.getShangHai();
			vals[i][7] = p.getChongQing();
			vals[i][8] = p.getHeBei();
			vals[i][9] = p.getShanXi1();
			vals[i][10] = p.getNeiMengGu();
			vals[i][11] = p.getLiaoNing();
			vals[i][12] = p.getJiLin();
			vals[i][13] = p.getHeiLongJiang();
			vals[i][14] = p.getJiangSu();
			vals[i][15] = p.getZheJiang();
			vals[i][16] = p.getAnHui();
			vals[i][17] = p.getFuJian();
			vals[i][18] = p.getJiangXi();
			vals[i][19] = p.getShanDong();
			vals[i][20] = p.getHeNan();
			vals[i][21] = p.getHuBei();
			vals[i][22] = p.getHuNan();
			vals[i][23] = p.getGuangDong();
			vals[i][24] = p.getGuangXi();
			vals[i][25] = p.getHaiNan();
			vals[i][26] = p.getSiChuan();
			vals[i][27] = p.getGuiZhou();
			vals[i][28] = p.getYunNan();
			vals[i][29] = p.getXiZang();
			vals[i][30] = p.getShanXi3();
			vals[i][31] = p.getGanSu();
			vals[i][32] = p.getQingHai();
			vals[i][33] = p.getNingXia();
			vals[i][34] = p.getXinJiang();
			vals[i][35] = p.getXiangGang();
			vals[i][36] = p.getAoMen();
		}
		
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate(sql, vals);
			jdbc.endTransaction();
		} catch (Exception e){
//			LOG.error("insert data into DB error",e);
			System.out.println("insert data into DB error");
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
//				LOG.error("DB Transaction rollback error",e1);
				System.out.println("DB Transaction rollback error");
			} finally {
				jdbc.release();
			}
		} finally {
			if (jdbc!=null) {
				jdbc.release();			
			}

		}
//		LOG.info(table + ":" +list.size() + " prices saved ");
	}
}
