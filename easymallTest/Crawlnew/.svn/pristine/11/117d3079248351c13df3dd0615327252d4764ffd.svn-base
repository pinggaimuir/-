package cn.futures.data.importor;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class ExcelDataUtil {
	
	private static final String DEFAULT_VALUE = "0";


	public static String getTimeint(HSSFCell cell) {
		if (cell == null) return DEFAULT_VALUE;
		
		int type = cell.getCellType();
		int timeint = 0;
		
		if (type == HSSFCell.CELL_TYPE_STRING) {
			String stime = cell.getStringCellValue();
			timeint = parseTimeInt(stime);
		} else if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			Double dtime = cell.getNumericCellValue();
			timeint = (int)Math.floor(dtime);
		}
		
		return String.valueOf(timeint);
		
	}
	
	
	
	public static String getCellValue(HSSFCell cell) {
		if (cell == null) return DEFAULT_VALUE;
		
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			double d = cell.getNumericCellValue();
			return Double.toString(d);
		} 
		return DEFAULT_VALUE;
	}
	
	
	/**
	 * 将stime转为timeint
	 * 支持yyyy-m-d, yyyy-m, yyyy
	 * 分别转为yyyymmdd, yyyymm, yyyy
	 * @param stime
	 * @return
	 */
	public static int parseTimeInt(String stime) {
		int year, month, day = 0;
		
		
		String[] ymd = stime.split("[-|/]");
		if (ymd.length == 3) {	//y-m-d
			year = Integer.parseInt(ymd[0]);
			month = Integer.parseInt(ymd[1]);
			day = Integer.parseInt(ymd[2]);
			return year*10000 + month*100 + day;
		} else if (ymd.length == 2) {	//y-m
			year = Integer.parseInt(ymd[0]);
			month = Integer.parseInt(ymd[1]);
			return year*100 + month;
		} else if (ymd.length == 1) {	//y
			year = Integer.parseInt(ymd[0]);			
			return year;
		} else {
			return 0;
		}
		
	}
	

}
