package cn.futures.data.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class RegexUtil {
	private static final Logger lOG = Logger.getLogger(RegexUtil.class);
	
	/**
	 * 通过正则表达式匹配需要的字符串
	 * @param contents 被查找的字符串
	 * @param compStr  正则表达式
	 * @param index    返回的字符串序号
	 * @return
	 */
	public static List<String> getMatchStr(String contents, String compStr, int[] index){
		List<String> results = new ArrayList<String>();
		Pattern pattern = Pattern.compile(compStr);
		Matcher matcher = pattern.matcher(contents);
		if(matcher.find()){
			if(index == null)
				results.add(matcher.group());
			else{
				for(Integer i:index){
					results.add(matcher.group(i));
//					System.out.println(matcher.group(i));
				}
			}
		}else{
			lOG.error(" 通过正则表达式未匹配到需要的字符串");  
		}
		//做了下面这个处理，为了使用时不用再判空
		if(results.isEmpty()){
			results.add("");
		}
		return results;
	}
	
	/**
	 * 匹配多个同模式的字符串。
	 * */
	public static List<String> getMatchStr(String contents, String compStr){

		List<String> results = new ArrayList<String>();
		Pattern pattern = Pattern.compile(compStr);
		Matcher matcher = pattern.matcher(contents);
		while(matcher.find()){
			results.add(matcher.group());
		}
		
		if(results == null || results.isEmpty()){
			lOG.error(" 通过正则表达式未匹配到需要的字符串"); 
		}
		return results;
	
	}
	
	/**
	 * 用于验证数据格式，比如价格。
	 * @author bric_yangyulin
	 * @date 2016-09-09
	 * */
	public static boolean match(String contents, String compStr){
		Pattern pattern = Pattern.compile(compStr);
		Matcher matcher = pattern.matcher(contents);
		if(matcher.matches()){
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		boolean isMatch = RegexUtil.match("52.35", "\\d+(\\.\\d+)?");
		System.out.println(isMatch);
	}
}
