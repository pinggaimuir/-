package cn.futures.data.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.futures.data.DAO.DAOUtils;

public class WareHouse {
	
	private static DAOUtils dao = new DAOUtils();
	private static final Logger log = Logger.getLogger(WareHouse.class);
	
	/**
	 * 将单行表数据保存到xls文件中。
	 * @param varName 品种名
	 * @param cnName 中文名
	 * @param timeInt 时间序列
	 * @param dataMap 单行数据映射（列名-值）
	 * @param filePathSuffix 文件路径前缀（需以文件分隔符结尾）
	 * @author bric_yangyulin
	 * @date 2016-04-27
	 * */
	public static void saveToExcel(String varName, String cnName, int timeInt, Map<String, String> dataMap, String filePathSuffix){
		filePathSuffix = String.format("%s%s%s%s%s", filePathSuffix,timeInt,Constants.FILE_SEPARATOR,varName,Constants.FILE_SEPARATOR);
		String fileName = cnName + ".xls";
		try {
			WareHouse.saveToXls(filePathSuffix, fileName, cnName, dataMap);
		} catch (FileNotFoundException e) {
			log.error("写成xls时发生异常：" + e.getMessage());
		} catch (IOException e) {
			log.error("写成xls时发生异常：" + e.getMessage());
		}
	}
	
	/**
	 * 将单行表数据保存到xls文件中。
	 * @param filePathSuffix 文件路径前缀（需以文件分隔符结尾）
	 * @param fileName 文件名
	 * @param sheetName 工作簿
	 * @param dataMap 单行数据映射（列名-值），现在假设只有String型数据
	 * @author bric_yangyulin
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @date 2014-04-27
	 * */
	public static void saveToXls(String filePathSuffix, String fileName,String sheetName, Map<String, String> dataMap) throws FileNotFoundException, IOException{
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		HSSFRow columnNameRow = null;
		ArrayList<String> columnNames = new ArrayList<String>();//存储列名行
		
		String fullFilePath = filePathSuffix + fileName;
		if((new File(fullFilePath)).exists()){
			//excel已文件存在，则读取
			
			workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(fullFilePath)));
			if((sheet = workbook.getSheet(sheetName)) == null){
				sheet = workbook.createSheet(sheetName);
				//写入列名行
				columnNameRow = sheet.createRow(0);
				int columnId = 0;
				for(String columnName: dataMap.keySet()){
					HSSFCell cell = null;
					cell = columnNameRow.createCell(columnId);
					//暂时假定所有列数据均为String型。
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(columnName);
					columnNames.add(columnName);
					columnId++;
				}
			} else {//工作表存在，需检测是否有列名行，（锁定若有则在第0行）
				columnNameRow = sheet.getRow(0);
				if(columnNameRow != null){
					HSSFCell cell = null;
					cell = columnNameRow.getCell(0);
					if(cell != null){
						String column0 = cell.getStringCellValue();
						if(column0 != null && dataMap.containsKey(column0)){//说明含列名行，读列名行（为了记录其出现顺序）
							for(int columnId = 0;columnId < columnNameRow.getLastCellNum(); columnId++){
								cell = columnNameRow.getCell(columnId);
								String column = cell.getStringCellValue();
								if(column != null && !column.isEmpty()){
									columnNames.add(column);
								} else {
									log.warn("列名行出现空列，注意查看文件：" + filePathSuffix + fileName);
								}
							}
						} else {//说明不含列名行，写入列名行
							int columnId = 0;
							for(String columnName: dataMap.keySet()){
								cell = columnNameRow.createCell(columnId);
								//暂时假定所有列数据均为String型。
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(columnName);
								columnNames.add(columnName);
								columnId++;
							}
						}
					}
				} else {
					//写入列名行
					columnNameRow = sheet.createRow(0);
					int columnId = 0;
					for(String columnName: dataMap.keySet()){
						HSSFCell cell = null;
						cell = columnNameRow.createCell(columnId);
						//暂时假定所有列数据均为String型。
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(columnName);
						columnNames.add(columnName);
						columnId++;
					}
				}
			}
		} else {
			//excel文件不存在，则创建
			
			workbook = new HSSFWorkbook();
			sheet = workbook.createSheet(sheetName);
			//写入列名行
			columnNameRow = sheet.createRow(0);
			int columnId = 0;
			for(String columnName: dataMap.keySet()){
				HSSFCell cell = null;
				cell = columnNameRow.createCell(columnId);
				//暂时假定所有列数据均为String型。
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(columnName);
				columnNames.add(columnName);
				columnId++;
			}
		}
		
		//写入数据
		int lastRowIndex = sheet.getLastRowNum();
		//定位到最后一行
		HSSFRow row = sheet.createRow(++lastRowIndex);
		for(int columnId = (dataMap.size() - 1); columnId >= 0; columnId--){
			HSSFCell cell = null;
			cell = row.createCell(columnId);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(dataMap.get(columnNames.get(columnId)));
		}
		
		// 新建一输出文件流
		File file = new File(filePathSuffix);
		if(!file.exists()){
			file.mkdirs();
		}
        FileOutputStream fOut = new FileOutputStream(fullFilePath);
        // 把相应的Excel 工作簿存盘
        workbook.write(fOut);
        fOut.flush();
        // 操作结束，关闭文件
        fOut.close();
        log.info(fullFilePath + "文件生成...");
	}
	
	/**
	 * 将xls中的数据表导入数据库中(假设表数据均为String型)
	 * @param varName 品种名
	 * @param cnName 中文名
	 * @param timeInt 数据的时间序列
	 * @param filePathSuffix 文件路径前缀
	 * @author bric_yangyulin
	 * @date 2016-04-28
	 * */
	public static void xlsToDb(String varName, String cnName, int timeInt, String filePathSuffix){
		filePathSuffix = String.format("%s%s%s%s%s", filePathSuffix,timeInt,Constants.FILE_SEPARATOR,varName,Constants.FILE_SEPARATOR);
		String fileName = cnName + ".xls";
		String fullFilePath = filePathSuffix + fileName;
		WareHouse.xlsToDb(fullFilePath, cnName);
	}
	
	/**
	 * 将xls中的数据表导入数据库中(假设表数据均为String型)
	 * @param fullFilePath xls文件全路径
	 * @param sheet 工作表名（任意，现采用cnName）
	 * @author bric_yangyulin
	 * @date 2016-04-27 
	 * */
	public static void xlsToDb(String fullFilePath, String sheetName){
		try{
			HSSFWorkbook workbook = null;
			HSSFSheet sheet = null;
			
			//根据文件全路径得到varName cnName timeInt
			String[] parts = null;
			if(Constants.FILE_SEPARATOR.equals("\\")){
				parts = fullFilePath.split("\\\\");//需两次转义，一次java编译器，一次正则式。
			} else {
				parts = fullFilePath.split(Constants.FILE_SEPARATOR);
			}
			
			String cnName = parts[parts.length - 1].split("\\.")[0];//文件名（不含扩展名）为cnName,正则式中'.'为特殊字符需转义。
			String varName = parts[parts.length - 2];//文件所在文件夹的名称为varName
			int timeInt = Integer.parseInt(parts[parts.length -3]);//文件所在文件夹的父文件夹名称为时间序列
			
			if((new File(fullFilePath)).exists()){
				//excel已文件存在，则读取
				
				workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(fullFilePath)));
				if((sheet = workbook.getSheet(sheetName)) != null){
					Object[] columnNames = null;
					//工作表存在
					
					columnNames = readOneRow(sheet, 0);//列名行
					if(columnNames == null || columnNames.length == 0){
						log.error("列名行为空，请检查文件：" + fullFilePath + sheetName);
						return;
					}
					
					//记录列名行
					Object[] dataRow = null;//行数据
					int lastRowNum = sheet.getLastRowNum();	//最后一行的行号
					for(int rowCur = 1; rowCur <= lastRowNum; rowCur++){
						dataRow = WareHouse.readOneRow(sheet, rowCur);
						//存到数据库
						//to do ...
						Map<String, String> dataMap = new HashMap<String, String>();
						for(int columnId = 0; columnId < columnNames.length; columnId++){
							if(columnNames[columnId] != null){
								if(dataRow[columnId].equals("")){
									dataMap.put(columnNames[columnId].toString(), null);
								} else {
									dataMap.put(columnNames[columnId].toString(), dataRow[columnId].toString());
								}
							}
						}
						dao.saveOrUpdateByDataMap(varName, cnName, timeInt, dataMap);
					}
				} else {
					log.error("工作表不存在：" + sheetName);
				}
			} else {
				log.error("xls文件不存在：" + fullFilePath);
			}
        } catch(Exception e) {
			log.error("数据导入过程中发生异常：" + e.getMessage(),e);
		}
	}
	
	/**
	 * 读工作表中的指定行
	 * @param sheet 指定工作表
	 * @param rowCur 指定行
	 * @return Object[] 指定行的数据
	 * @author bric_yangyulin
	 * @date 2016-04-27
	 * */
	public static Object[] readOneRow(HSSFSheet sheet, int rowCur){
		HSSFRow row = null;
		if((row = sheet.getRow(rowCur)) != null){
			//当前行不为空
			int lastCellNum = row.getLastCellNum();//最大列号
			HSSFCell cell = null;
			Object[] dataRow = new Object[lastCellNum+1];//存储一行数据；
			for(int i = 0; i <= lastCellNum; i++){
				cell = row.getCell(i);
				if(cell != null){
					  switch (cell.getCellType()) {     
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字     
                        	dataRow[i] = cell.getNumericCellValue(); 
                            break;     
                        case HSSFCell.CELL_TYPE_STRING: // 字符串     
                        	dataRow[i] = cell.getStringCellValue();
                            break;     
                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean     
                        	dataRow[i] = cell.getBooleanCellValue();
                            break;     
                        case HSSFCell.CELL_TYPE_FORMULA: // 公式     
                        	dataRow[i] = cell.getCellFormula();
                            break;     
                        case HSSFCell.CELL_TYPE_BLANK: // 空值     
                        	dataRow[i] = "";     
                            break;     
                        case HSSFCell.CELL_TYPE_ERROR: // 故障     
                        	log.error("故障。 ");     
                            break;     
                        default:     
                            log.error("未知类型   ");     
                            break;     
                        }
					
				} else {
					dataRow[i] = null;
				}
			}
			return dataRow;
		}
		return null;
	}
}
