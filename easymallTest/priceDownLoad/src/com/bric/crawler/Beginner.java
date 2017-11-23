package com.bric.crawler;

import com.bric.crawler.download.CZCEDataFetch;
import com.bric.crawler.download.DLCCDataFetch;
import com.bric.crawler.download.MofcomDataFetch;
import com.bric.crawler.download.Price21foodDataFetch;
import com.bric.intoDB.CZCEDao;
import com.bric.intoDB.DLCCDao;
import com.bric.intoDB.MofcomDao;
import com.bric.intoDB.Price21foodDao;
import com.bric.temporary.Query21foodPrice;
import com.bric.util.Constants;
import com.bric.util.formatData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Beginner {
	
	public static void main(String[] a){
		//new Beginner().fetchAndSaveMofcomData();
		new MofcomDao().saveMofcomPriceData(new File(Constants.MOFCOM_ROOT), 2015);
	}
	/**
	 * 抓取大连交易所持仓数据 先缓存 后入库
	 * */
	public void fetchAndSaveDLCCData(){
		Date today = formatData.getDate(0);	
		String todayStr = this.formatDate(today, "yyyyMMdd");
		new DLCCDataFetch().fetchData(todayStr);
		new DLCCDao().saveDLCCData(new File(Constants.DLCCDATA_ROOT), todayStr);
		
	}	

	public void fetchAndSaveCZCEData(){
		Date today = formatData.getDate(0);	
		String todayStr = this.formatDate(today, "yyyyMMdd");
		new CZCEDataFetch().fetchData(todayStr);
		new CZCEDao().saveCZCEData(new File(Constants.ZZCCDATA_ROOT), todayStr);
	}
	
	/**
	 * 抓取21food食品商务网价格行情数据 先缓存 后入库
	 * */
	public void fetchAndSave21foodData(){
			Date today = formatData.getDate(0);	
			String todayStr = this.formatDate(today, "yyyyMMdd");
			//today
			new Price21foodDataFetch().fetchData(today);
			new Price21foodDao().save21foodPriceData(new File(Constants.MARKETPRICEDATA_21FOOD_ROOT), todayStr);
			Query21foodPrice.loadAreaAndPrice(todayStr);
		
	}
	public static String formatDate(Date date, String format) {
		if (date == null)
			return null;
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
		String dateString = bartDateFormat.format(date);
		return dateString;
	}

	
//	public void fetchAndSaveMofcomData(){//北京 历史数据已经入库
//		for(int i=2006;i<2015;i++){
//			new MofcomDataFetch().fetchData(i);
//			new MofcomDao().saveMofcomPriceData(new File(Constants.MOFCOM_ROOT), i);
//		}
//	}
	/**
	 * 抓取Mofcom每月价格行情数据 先缓存 后入库
	 */
	public void fetchAndSaveMofcomData(){
		int year = formatData.getCurYear();
		new MofcomDataFetch().fetchData(year);
		new MofcomDao().saveMofcomPriceData(new File(Constants.MOFCOM_ROOT), year);
	}
}
