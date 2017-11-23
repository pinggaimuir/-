package com.bric.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;



public class formatData {
	
	public static String getProvinceByArea(String Area){
		String province = "";
		if (Area == "" || Area == null) {
			return "";
		}
		Map<String, List<String>> provinceAndCityMap = ProvincesAndCity.getProvincesAndCityMap();
		for(String str:provinceAndCityMap.keySet()){
			if (Area.contains(str)) {
				province = str;
			}
		}
		return province;
	}
	
	public static String getAreaByMarket(String market){
		String place = "";
		Map<String, List<String>> provinceAndCityMap = ProvincesAndCity.getProvincesAndCityMap();
		boolean hasProvince = false;
		boolean hasCity = false;
		for(String province:provinceAndCityMap.keySet()){//
			if (market.contains(province)) {//has province
				hasProvince = true;
				place += province;
				for(String city:provinceAndCityMap.get(province)){
					if (market.contains(city)) {//has province and city
						hasCity = true;
						place += city;
						break;
					}
				}
				break;
			}
		}
		
		if (!hasCity) {//has no city
			if (!hasProvince) {//neither city nor province 
				for(String province:provinceAndCityMap.keySet()){
					for(String city:provinceAndCityMap.get(province)){
						if (market.contains(city)) {//find province ky city
							place = province + city;
						}
					}
				}
			}else {//has no city but has province
				
			}
		}
		return place;//this 'place' probably contains 'province' or 'province and city'  
	}
	
	public static Date getDate(int i){		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -i);
		return calendar.getTime();
//		String today = String.valueOf(calendar.get(Calendar.YEAR));
//		int month = calendar.get(Calendar.MONTH)+1;
//		int day = calendar.get(Calendar.DAY_OF_MONTH);
//		if(month<10){
//			today+="0"+String.valueOf(month);
//		}else {
//			today+=String.valueOf(month);
//		}
//		if(day<10){
//			today+="0"+String.valueOf(day);
//		}else {
//			today+=String.valueOf(day);
//		}
////		System.out.println(today);
//		return today;
	}
	
	public static int getCurYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonth(int i){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -i);
		return calendar.get(Calendar.MONTH)+1;
	}
	
	public static void filtData(String fileUrl){		
		File yunnanFile = new File(fileUrl);
		String result = "";
		StringBuffer stringBuffer = new StringBuffer();
		try {
			FileReader reader = new FileReader(yunnanFile);
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			String str = "";			
			while((str=bufferedReader.readLine())!=null){
					stringBuffer.append(str);
				}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = stringBuffer.toString();
		try {
			result = result.substring(result.indexOf("[[")+2, result.indexOf("]]"));
			result = result.replaceAll("\\],\\[", "\r\n");
		} catch (StringIndexOutOfBoundsException e) {
			// TODO: handle exception
			System.out.println("no avaliable data!");
			return;
		}
		
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(yunnanFile));
			bufferedWriter.write(result);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	public static void transMilToDate(String fileUrl){
		File yunnanFile = new File(fileUrl);
		StringBuffer stringBuffer2 = new StringBuffer();
		try {
			FileReader reader = new FileReader(yunnanFile);
			BufferedReader bufferedReader = new BufferedReader(reader);

			
			String str = "";		
			String temp0 = "";
			String temp1 ="";

			Calendar calendar = Calendar.getInstance();
			while((str=bufferedReader.readLine())!=null){
				try {
					temp0 = str.split(",")[0];
					temp1 = str.split(",")[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO: handle exception
					return;
				}
					
					calendar.setTimeInMillis(Long.parseLong(temp0));
					Date date = calendar.getTime();
					stringBuffer2.append(calendar.get(Calendar.YEAR)+"-"+(date.getMonth()+1)+"-"+date.getDate()+","+temp1+"\r\n");
				}
			bufferedReader.close();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(yunnanFile));
			bufferedWriter.write(stringBuffer2.toString());
			bufferedWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("finish one");
	}
	
	public static void appendFile(String fileUrl,String pointFileUrl){
		try {
			FileWriter fileWriter = new FileWriter(new File(pointFileUrl), true);
			FileReader fileReader = new FileReader(new File(fileUrl));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String string = "";
			int i = 0;
			while((string=bufferedReader.readLine())!=null){
				if (string.contains("<script>")) {
					break;
				}else if(i == 1){
					fileWriter.append(string+"\r\n");
				}
				++i;
			}
//			fileWriter.append(string+"\r\n");
			bufferedReader.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getTimeInt(String string){
		String[] array = string.split("-");
		StringBuffer buffer = new StringBuffer();
		buffer.append(array[0]);
		if (array[1].length()<2) {
			buffer.append(0+array[1]);
		}else {
			buffer.append(array[1]);
		}
		if (array[2].length()<2) {
			buffer.append(0+array[2]);
		}else {
			buffer.append(array[2]);
		}
		return Integer.valueOf(buffer.toString());
	}

}
