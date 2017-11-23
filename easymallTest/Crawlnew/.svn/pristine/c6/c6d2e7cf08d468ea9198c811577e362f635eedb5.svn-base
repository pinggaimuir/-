package cn.futures.data.importor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelDataParser {
	
	private static final Logger log = Logger.getLogger(ExcelDataParser.class);

	public ExcelDataset read (File file) {
		ExcelDataset dataset = readMeta(file);
		
		HSSFSheet sheet = getSheet(file);
		
		dataset.setFields(getFieldsRow(sheet));
		dataset.setValues(getValueRows(sheet));
		
		return dataset;
	}
	
	public List<String[]> getValueRows(HSSFSheet sheet) {
		// 表数据
		int totalRow = sheet.getLastRowNum();
		
		List<String[]> values = new ArrayList<String[]>(totalRow-1);
		
		for (short j = 1; j <= totalRow; j++) {
			HSSFRow row = sheet.getRow(j);
			if (row == null) continue;
			
			String[] valueRow = new String[row.getLastCellNum()];
			for (short k = 0; k < row.getLastCellNum(); k++){
				HSSFCell cell = row.getCell(k);
				if (k == 0){ // 默认第一列为时间列
					valueRow[k] = ExcelDataUtil.getTimeint(cell);
				} else {					// 数据列
					valueRow[k] = ExcelDataUtil.getCellValue(cell);
				}
			}
			values.add(valueRow);
		}
		
		return values;
	}

	public List<String> getFieldsRow(HSSFSheet sheet) {
		//  表头
		HSSFRow rowTitle = sheet.getRow(0);
		short totalCol = rowTitle.getLastCellNum();
    	List<String> fields = new ArrayList<String>(totalCol);
		for (short i = 0; i < totalCol; i++) {
			HSSFCell cell = rowTitle.getCell(i);
			if (cell != null) {
				fields.add(cell.getStringCellValue());
			}
		}
		return fields;
	}

	private HSSFSheet getSheet(File file) {
		HSSFWorkbook book = null;
		try {
			InputStream input = new FileInputStream(file);
			book = new HSSFWorkbook(input);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
		
		// 第一张sheet
		HSSFSheet sheet = book.getSheetAt(0);
		
		return sheet;
		
	}

	public ExcelDataset readMeta(File file) {
		
		ExcelDataset dataset = new ExcelDataset();
		
		String filename = file.getName().split("\\.")[0];
		
		// 文件名
		dataset.setFilename(filename);
		
		String[] filenameArray = filename.split("-");
		
		if (filenameArray.length == 4) {
			String varName = filenameArray[2];
			// 品种名
			dataset.setVarname(varName);
			// 品种ID
			dataset.setVarid(Variety.getVaridByName(varName));
			// 对应的数据库表名
			dataset.setTable(ExcelSchema.getTableByName(filenameArray[1] + "-"
					+ filenameArray[2] + "-" + filenameArray[3]));
		} else if (filenameArray.length == 3) {
			String varName = filenameArray[1];
			// 品种名
			dataset.setVarname(varName);
			// 品种ID
			dataset.setVarid(Variety.getVaridByName(varName));
			// 对应的数据库表名
			dataset.setTable(ExcelSchema.getTableByName(filename));
		}
		return dataset;
		
	}
	


}
