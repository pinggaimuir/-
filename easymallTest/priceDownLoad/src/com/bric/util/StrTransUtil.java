//package com.bric.util;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.HashMap;
//import java.util.Set;
//
//public class StrTransUtil {
//
//	private StrTransUtil(){}
//
//	public static String map2Str(Map<String, Integer> map){
//		Set<String> keySet = map.keySet();
//		StringBuffer mapStr = new StringBuffer();
//		String newLine = System.getProperty("line.separator");
//		for (String key:keySet)
//			if (!key.trim().equals("")){
//				mapStr.append(key);
//				mapStr.append(",");
//				mapStr.append(map.get(key));
//				mapStr.append(newLine);
//			}
//		return mapStr.toString();
//	}
//
//	public static Map<String, Integer> str2Map(String str){
//		Map<String, Integer> strMap = new HashMap<String, Integer>();
//		String[] strLines = str.split("\n");
//		for (int line = 0;line < strLines.length;++ line){
//			String[] itemStrings = strLines[line].split(",", 3);
//			if (itemStrings.length >= 2 && !itemStrings[0].trim().equals("")){
//				int valueID = Integer.valueOf(itemStrings[1].trim());
//				strMap.put(itemStrings[0].trim(), valueID);
//			}
//		}
//		return strMap;
//	}
//
//	public static String collection2Str(Collection<String> collection){
//		StringBuffer collectionStr = new StringBuffer();
//		String newLine = System.getProperty("line.separator");
//		for (String item:collection){
//			collectionStr.append(item);
//			collectionStr.append(newLine);
//		}
//		return collectionStr.toString();
//	}
//
//	public static String set2Str(Set<String> set){
//		return collection2Str(set);
//	}
//
//	public static String list2Str(List<String> list){
//		return collection2Str(list);
//	}
//
//	public static void extendCollectionWithStr(String str, Collection<String> collection){
//		String[] strLines = str.split("\n");
//		for (int line = 0;line < strLines.length;++ line)
//			collection.add(strLines[line]);
//	}
//
//	public static Collection<String> str2Collection(String str, Class<? extends Collection<String>> collectionClass)
//			throws InstantiationException, IllegalAccessException{
//		Collection<String> strCollection = collectionClass.newInstance();
//		extendCollectionWithStr(str, strCollection);
//		return strCollection;
//	}
//
//	public static Set<String> str2Set(String str){
//		Set<String> strSet = new HashSet<String>();
//		extendCollectionWithStr(str, strSet);
//		return strSet;
//	}
//
//	public static List<String> str2List(String str){
//		List<String> strList = new LinkedList<String>();
//		extendCollectionWithStr(str, strList);
//		return strList;
//	}
//}
