//package com.bric.intoDB;
//
//import com.bric.crawler.MapInit;
//import com.bric.crawler.download.YmtDataFetch;
//import com.bric.util.Constants;
//import com.bric.util.formatData;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class PriceDao {
//
//	private Dbm3InterfaceDao dbm3Dao=new Dbm3InterfaceDao();
//
//	public String[] citys = {"全国","北京","天津","上海,","重庆","河北","山西","内蒙","辽宁","吉林","黑龙江",
//			"江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","四川",
//			"贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","香港","澳门"};
//
//	public void savePriceData(){
//		System.out.println("begin...");
//		for(String name:MapInit.nameMap.keySet()){//大类别：蔬菜、水产...
//			Map<Integer, String> innerMap = MapInit.nameMap.get(name);
//			for(int id:innerMap.keySet()){//小类别如蔬菜中的洋白菜、豆角...
//				List<Map<String,String>> list=new ArrayList<>();
//				for (int i = 0; i < YmtDataFetch.placeArray.length; i++) {//省份
//					System.out.println("reading "+innerMap.get(id)+" "+YmtDataFetch.placeArray[i]);
//
//					String pointDir = Constants.YMTDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"
//							+YmtDataFetch.placeArray[i]+".txt";
//					File file = new File(pointDir);
//					try {
//						BufferedReader bufferedReader = new BufferedReader(
//								new FileReader(file));
//						String str = "";
//						if (i==0) {//每行创建一个YMTPriceModel对象，对应存储以后省份的对应行数据，bug:没有考虑“全国”没有数据的情况
//							while((str=bufferedReader.readLine())!=""){
//								String[] array = {};
//								try {
//									array = str.split(",");
//								} catch (Exception e) {
//									// TODO: handle exception
//									break;
////									e.printStackTrace();
//								}
//								Map<String,String> map=new HashMap<>();
//								map.put("KindId",MapInit.nameKindIdMap.get(name)+"");
//								map.put("VarId",id+"");
//								map.put("TimeInt",formatData.getTimeInt(array[0])+"");
//								map.put("全国",Float.parseFloat(array[1])+"");
//								list.add(map);
//							}
//							bufferedReader.close();
//						}else {
//							int index=0;
//							int size = list.size();
//							while((str=bufferedReader.readLine())!="" && str != null && index < size){
//								Map<String,String> map = list.get(index);
//								try {
//									map.put(citys[i],Float.parseFloat(str.split(",")[1])+"");
//									index+=1;
//								} catch (NumberFormatException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (IllegalArgumentException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (SecurityException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (NullPointerException e) {
//									// TODO: handle exception
//									System.out.println(str);
//								}
//
//							}
//							bufferedReader.close();
//						}
//
//
//					} catch (FileNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO: handle exception
//						e.printStackTrace();
//					}
//
//				}
//				Map data=new HashMap<>();
//				data.put("dbName","CX_MarketPrice");
//				data.put("data", list);
//				dbm3Dao.saveByDbName(data);
//				System.out.println(innerMap.get(id)+" finished");
//			}
//		}
//	}
//
//}
