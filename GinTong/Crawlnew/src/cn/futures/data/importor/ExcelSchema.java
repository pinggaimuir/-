package cn.futures.data.importor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.futures.chart.PriceExcel2Db;

public class ExcelSchema {

	public static final String SCHEMA_FILE = "cn/futures/data/importor/ExcelSchema.xls";

	public static Map<String, String> schema;
	

	public static String getTableByName(String filename) {
		if (schema == null) {
			synchronized (ExcelSchema.class){
				if (schema == null) {
					schema = loadSchema();
				}
			}
		}
		return schema.get(filename);
	}

	public static Map<String, String> loadSchema() {

		HashMap<String, String> map = new LinkedHashMap<String, String>();

		HSSFWorkbook book = null;
		try {
			InputStream input = PriceExcel2Db.class.getClassLoader()
				.getResourceAsStream(SCHEMA_FILE);
			book = new HSSFWorkbook(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int sheetIndex = 0; sheetIndex < book.getNumberOfSheets(); sheetIndex++){
			HSSFSheet sheet = book.getSheetAt(sheetIndex);
			String sheetName = book.getSheetName(sheetIndex);
//			System.out.println(sheetName);
			if (sheetName == null || sheetName.startsWith("Sheet")) continue;
			
			int totalRow = sheet.getLastRowNum();
			for(int i=0; i<totalRow; i++){
				HSSFRow row = sheet.getRow(i);
				if (row == null) continue;
				
				String varname = getValue(row.getCell((short)1));
				String table_cn = getValue(row.getCell((short)2));
				String table_en = getValue(row.getCell((short)3));
				
				if (varname != null && table_cn != null && table_en != null) {
					map.put(sheetName+"-"+varname+"-"+table_cn, table_en);
//					System.out.println(sheetName+"-"+varname+"-"+table_cn +" # "+ table_en);
				}
			}
		}
		
		return map;
	}

	private static String getValue(final HSSFCell cell){
		if (cell == null) return null;
		String s = cell.getStringCellValue();
		if (s == null || s.trim().length() == 0) return null;
		return s.trim();
	}
	
	

}
