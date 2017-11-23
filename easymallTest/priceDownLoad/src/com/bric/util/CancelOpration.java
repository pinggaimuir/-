/*
package com.bric.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.bric.crawler.MapInit;

public class CancelOpration {
	private static String[] placeArray = {"全国","北京","天津","上海","重庆","河北","山西","内蒙古","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建",""
			+ "江西","山东","河南","湖北","湖南","广东","广西","海南","四川","贵州","云南","西藏","陕西","甘肃",""
					+ "青海","宁夏","新疆","香港","澳门"};
	public static void main(String[] a){
		deleteScriptAndNull();
//		deleteRepeatData();
	}
	
	private static void deleteRepeatData(){
		System.out.println("canceling... ");
		for(String name:MapInit.nameMap.keySet()){
			Map<Integer, String> innerMap = MapInit.nameMap.get(name);
			for(Integer id:innerMap.keySet()){
				for (int i = 0; i < placeArray.length; i++) {
					String dir = Constants.YMTDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"+placeArray[i]+".txt";
					File file = new File(dir);
					String curStr = "";
					String preStr = "";
					StringBuffer stringBuffer = new StringBuffer();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(file));
						preStr = reader.readLine();
							while((curStr=reader.readLine())!=null){
								
								if (curStr.split(",")[0].equals(preStr.split(",")[0])) {
									
								}else {
									stringBuffer.append(curStr+"\r\n");
								}
								preStr = curStr;
							}
							String result = stringBuffer.toString();
							FileWriter writer = new FileWriter(file);
							writer.write(result);
							writer.close();
							reader.close();
//							writer.
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(innerMap.get(id)+"cancel finished ,please wait...");
			}
		}
		System.out.println("Finish all cancel!");
	
	}
	
	private static void deleteScriptAndNull(){
		System.out.println("canceling... ");
		for(String name:MapInit.nameMap.keySet()){
			Map<Integer, String> innerMap = MapInit.nameMap.get(name);
			for(Integer id:innerMap.keySet()){
				for (int i = 0; i < placeArray.length; i++) {
					String dir = Constants.YMTDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"+placeArray[i]+".txt";
					File file = new File(dir);
					String string = "";
					StringBuffer stringBuffer = new StringBuffer();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(file));
						
							while((string=reader.readLine())!=null){
								
								if (string.contains("<script>") || string.equals("null")) {
									break;
								}else {
									stringBuffer.append(string+"\r\n");
								}								
							}
							String result = stringBuffer.toString();
							FileWriter writer = new FileWriter(file);
							writer.write(result);
							writer.close();
							reader.close();
//							writer.
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(innerMap.get(id)+"cancel finished ,please wait...");
			}
		}
		System.out.println("Finish all cancel!");
	}
}
*/
