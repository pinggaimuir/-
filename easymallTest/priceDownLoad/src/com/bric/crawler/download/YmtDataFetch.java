package com.bric.crawler.download;

import java.util.Calendar;
import java.util.Map;

import com.bric.crawler.MapInit;
import com.bric.util.Constants;
import com.bric.util.MyHttpClient;
import com.bric.util.formatData;

public class YmtDataFetch {

	public static String[] placeArray = {"全国","北京","天津","上海","重庆","河北","山西","内蒙古","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建",""
			+ "江西","山东","河南","湖北","湖南","广东","广西","海南","四川","贵州","云南","西藏","陕西","甘肃",""
					+ "青海","宁夏","新疆","香港","澳门"};
	//获取一亩田价格行情历史数据
	private void excute(){

		String url = "http://www.ymt360.com/data/new_ajax_line/";

		System.out.println("downloading...");
		for(String name:MapInit.nameMap.keySet()){
			Map<Integer, String> innerMap = MapInit.nameMap.get(name);
			for(Integer id:innerMap.keySet()){

				for (int i = 0; i < placeArray.length; i++) {

					String dir = Constants.YMTDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"+placeArray[i]+".txt";

					MyHttpClient.fetchAndSave(url+"/"+String.valueOf(id)+"/"+String.valueOf(i)+"/"
							+"/"+"2010-05-01"+"/"+"2014-06-09",dir);
					formatData.filtData(dir);
					formatData.transMilToDate(dir);
				}
				System.out.println(innerMap.get(id)+"download finished ,please wait...");
			}
		}
		System.out.println("Finish all download!");
	}
	
	/**
	 * 获取当天（一亩田）价格行情数据*/
	public void getAndAppendCurrentData(){

		String url = "http://www.ymt360.com/data/new_ajax_line/";
		Calendar calendar = Calendar.getInstance();
		String today = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+
				"-"+calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String yesterday =  calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+
				"-"+calendar.get(Calendar.DAY_OF_MONTH);

		System.out.println("downloading...");
		for(String name:MapInit.nameMap.keySet()){
			Map<Integer, String> innerMap = MapInit.nameMap.get(name);
			for(Integer id:innerMap.keySet()){

				for (int i = 0; i < placeArray.length; i++) {

					String pointDir = Constants.YMTDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"+placeArray[i]+".txt";
					String dir = Constants.YMTCURDATA_ROOT+"\\"+name+"\\"+innerMap.get(id)+"\\"+placeArray[i]+".txt";

					MyHttpClient.fetchAndSave(url+"/"+String.valueOf(id)+"/"+String.valueOf(i)+"/"
							+"/"+yesterday+"/"+today,dir);
					formatData.filtData(dir);//过滤数据
					formatData.transMilToDate(dir);//将毫秒转换为日期
					formatData.appendFile(dir, pointDir);//将当天数据追加到历史数据中
				}
				System.out.println(innerMap.get(id)+"download finished ,please wait...");

			}
		}
		System.out.println("Finish all download!");
	}
	
	public static void main(String[] args) {
		new YmtDataFetch().getAndAppendCurrentData();
	}
}
