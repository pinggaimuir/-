package com.bric.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class MapConfigureIO {
	private static String DIR_STR = "D:\\Crawlers\\Data\\21food\\";
	private static String SUFFIX = ".txt";

	public Map<String, Integer> getFullMap(List<String> list, String kindStr){
		String configureStr = "";
		Map<String, Integer> configureMap;
		try{
			configureStr = loadStringFromFile(DIR_STR + kindStr	+ SUFFIX);
			configureMap = Str2Map(configureStr);
			boolean mapRenewed = renewMapByList(configureMap, list);
			if (mapRenewed) {
				String newConfigureStr = Map2Str(configureMap);
				saveStringToFile(newConfigureStr, DIR_STR, kindStr + SUFFIX);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
		return configureMap;
	}
	
	public static String getDIR_STR() {
		return DIR_STR;
	}

	public static void setDIR_STR(String dIR_STR) {
		DIR_STR = dIR_STR;
	}

	public static String getSUFFIX() {
		return SUFFIX;
	}

	public static void setSUFFIX(String sUFFIX) {
		SUFFIX = sUFFIX;
	}
	
	private void saveStringToFile(String dataString, String dirStr, String fileStr)
			throws IOException{
		String fullFilePath = dirStr + fileStr;
		File idDir = new File(dirStr);
		if (!idDir.exists()) {
			idDir.mkdirs();
		}
		File file = new File(fullFilePath);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(dataString.getBytes("GB2312"));
			fileOutputStream.close();
		}
		catch (IOException e) {
			String errorMsg = "Error While Saving " + fullFilePath + "\n" + e.getMessage();
			throw new IOException(errorMsg, e);
		}
	}
		
	private String loadStringFromFile(String fileFullPath)
			throws IOException{
		StringBuffer fileContentStr = new StringBuffer();
		String newLine = System.getProperty("line.separator");
		try{
			FileInputStream fileStream = new FileInputStream(fileFullPath);
			BufferedReader reader = new BufferedReader( new InputStreamReader(fileStream, "GB2312"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null)
				fileContentStr.append(tempString + newLine);
			reader.close();
		}
		catch (FileNotFoundException e){
			System.out.println("File Not Exist");
		}
		catch(IOException e){
			String errorMsg = "Error While Loading: " + fileFullPath + "\n" + e.getMessage();
			throw new IOException(errorMsg, e);
		}
		return fileContentStr.toString();
	}
	
	private String Map2Str(Map<String, Integer> map){
		Set<String> keySet = map.keySet();
		String mapStr = "";
		String newLine = System.getProperty("line.separator");
		for (String key:keySet){
			if (!key.trim().equals("")){
				int value = map.get(key);
				mapStr += key + "," + value + newLine;
			}
		}
		return mapStr;
	}
	
	private Map<String, Integer> Str2Map(String str){
		Map<String, Integer> strMap = new HashMap<String, Integer>();
		String[] strLines = str.split("\n");
		for (int line = 0;line < strLines.length;++ line){
			String[] itemStrings = strLines[line].split(",", 3);
			if (itemStrings.length >= 2 && !itemStrings[0].trim().equals(""))
				try{
					int valueID = Integer.parseInt(itemStrings[1].trim());
					strMap.put(itemStrings[0].trim(), valueID);
				} catch (NumberFormatException e) {
					continue;
				}
		}
		return strMap;
	}
	
	private int getMaxValue(Map<String, Integer> map){
		Collection<Integer> valueSet = map.values();
		int maxValue = -1;
		for (Integer value : valueSet)
			if (value > maxValue)
				maxValue = value;
		return maxValue;			
	}
	
	private boolean renewMapByList(Map<String, Integer> map, List<String> list){
		int maxValue = getMaxValue(map);
		int maxID = maxValue;
		for (String element:list)
			if (!map.containsKey(element))
				map.put(element, ++ maxID);
		return (maxValue != maxID);
	}
}
