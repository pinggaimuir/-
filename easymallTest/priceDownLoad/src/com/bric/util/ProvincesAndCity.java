package com.bric.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 格式化省的名称，formatData中调用
 */
public class ProvincesAndCity {

	private static Map<String, List<String>> provincesAndCityMap;
	
	public static Map<String, List<String>> getProvincesAndCityMap(){
		return provincesAndCityMap;
	}
	
	static{
		provincesAndCityMap = new HashMap<String, List<String>>();
//		File file = new File("provincesAndCities.txt");
		InputStream inputStream = ProvincesAndCity.class.getClassLoader()
				.getResourceAsStream("provincesAndCities.txt");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			String str = "";
			String province = "";
			while((str=reader.readLine())!=null){
				if (str.contains("省:")) {
					province = str.split(":")[1];
					List<String> list = new ArrayList<String>();
					provincesAndCityMap.put(province, list);
				}else {
					provincesAndCityMap.get(province).add(str);
				}
			}
			reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] a){
		System.out.println();
	}
}
