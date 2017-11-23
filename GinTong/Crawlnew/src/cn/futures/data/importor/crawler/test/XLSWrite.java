package cn.futures.data.importor.crawler.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.futures.data.importor.MapInit;
import cn.futures.data.util.DateTimeUtil;
/**
* 利用POI实现向excel中写入内容
*/
public class XLSWrite {

	public static String fileToWrite = "d:/test111.xls";
	public List<String> readXls() throws IOException {
		InputStream is = new FileInputStream("e:/ccorn.xls");
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<String> list = new ArrayList<String>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					HSSFCell no = hssfRow.getCell(0);
					System.out.println(getValue(no));
					HSSFCell name = hssfRow.getCell(1);
					System.out.println(getValue(name));
					HSSFCell age = hssfRow.getCell(2);
					System.out.println(getValue(age));
					HSSFCell score = hssfRow.getCell(3);
					System.out.println(getValue(score));
				}
			}
		}
		return list;
	}
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BLANK) {
			return "";
		}else{
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	public void writeXls() throws IOException {
		HSSFWorkbook wb = null;
		if(new File(fileToWrite).exists()){
			// 新建一输出文件流
			FileInputStream fs=new FileInputStream(fileToWrite);  //获取d://test.xls  
	        POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
	        wb=new HSSFWorkbook(ps); 
			HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
			HSSFRow row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据  
		    row.createCell(0).setCellValue("leilei"); //设置第一个（从0开始）单元格的数据  
		    row.createCell(1).setCellValue(24); //设置第二个（从0开始）单元格的数据  
		    System.out.println("文件追加..." + fileToWrite);
		}else{
			// 创建新的Excel 工作簿
			  wb = new HSSFWorkbook();
			  // 在Excel工作簿中建一工作表，其名为缺省值
			  HSSFSheet sheet = wb.createSheet("第一页");
			  //HSSFSheet sheet = workbook.createSheet();
			  // 在指定的索引处创建一行
			  HSSFRow row = sheet.createRow((short) 0);
			  //在指定索引处创建单元格
			  HSSFCell id = row.createCell((short) 0);
			  // 定义单元格为字符串类型
			  id.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			  // 在单元格中输入一些内容,HSSFRichTextString可以解决乱码问题
			  HSSFRichTextString idContent = new HSSFRichTextString("QQ群");
			  id.setCellValue(idContent);
			  HSSFCell name = row.createCell((short) 1);
			  name.setCellType(HSSFCell.CELL_TYPE_STRING);
			  HSSFRichTextString nameContent = new HSSFRichTextString("用户名");
			  name.setCellValue(nameContent);
			  HSSFCell password = row.createCell((short) 2);
			  password.setCellType(HSSFCell.CELL_TYPE_STRING);
			  HSSFRichTextString passwordContent = new HSSFRichTextString("用户密码");
			  password.setCellValue(passwordContent);
			  System.out.println("文件生成..." + fileToWrite);
		}
		FileOutputStream out = new FileOutputStream(fileToWrite);
		out.flush();
		// 把相应的Excel 工作簿存盘
		wb.write(out);
		// 操作结束，关闭文件
		out.close();
		
		/*FileInputStream fs=new FileInputStream("d://test.xls");  //获取d://test.xls  
        POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
        HSSFWorkbook wb=new HSSFWorkbook(ps);    
        HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表  
        HSSFRow row=sheet.getRow(0);  //获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值  
        System.out.println(sheet.getLastRowNum()+" "+row.getLastCellNum());  //分别得到最后一行的行号，和一条记录的最后一个单元格  
          
        FileOutputStream out=new FileOutputStream("d://test.xls");  //向d://test.xls中写数据  
        row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据  
        row.createCell(0).setCellValue("leilei"); //设置第一个（从0开始）单元格的数据  
        row.createCell(1).setCellValue(24); //设置第二个（从0开始）单元格的数据  
  
          
        out.flush();  
        wb.write(out);    
        out.close();    
        System.out.println(row.getPhysicalNumberOfCells()+" "+row.getLastCellNum());  */	
	}
	public void writeXls(String[][] data) throws IOException {
		HSSFWorkbook wb = null;
		if(new File(fileToWrite).exists()){
			// 新建一输出文件流
			FileInputStream fs=new FileInputStream(fileToWrite);  //获取d://test.xls  
	        POIFSFileSystem ps=new POIFSFileSystem(fs);  //使用POI提供的方法得到excel的信息  
	        wb=new HSSFWorkbook(ps); 
			HSSFSheet sheet=wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表
			HSSFRow row=sheet.createRow((short)(sheet.getLastRowNum()+1)); //在现有行号后追加数据  
		    row.createCell(0).setCellValue("leilei"); //设置第一个（从0开始）单元格的数据  
		    row.createCell(1).setCellValue(24); //设置第二个（从0开始）单元格的数据  
		    System.out.println("文件追加..." + fileToWrite);
		}else{
			// 创建新的Excel 工作簿
			  wb = new HSSFWorkbook();
			  // 在Excel工作簿中建一工作表，其名为缺省值
			  HSSFSheet sheet = wb.createSheet("第一页");
			  //HSSFSheet sheet = workbook.createSheet();
			  // 在指定的索引处创建一行
			  Set<String> provSet = MapInit.weather_prov2city_map.keySet();
			  int j=0;
			  for(String provTmp:provSet){
				  HSSFRow row = sheet.createRow((short) (j++));
				//在指定索引处创建单元格
				  HSSFCell prov = row.createCell((short) 0);
				  // 定义单元格为字符串类型
				  prov.setCellType(HSSFCell.CELL_TYPE_STRING);
				  // 在单元格中输入一些内容,HSSFRichTextString可以解决乱码问题
				  HSSFRichTextString idContent = new HSSFRichTextString(provTmp.split("-")[0]);
				  prov.setCellValue(idContent);
				  Map<String, String> cityMap = MapInit.weather_prov2city_map.get(provTmp);
				  int i=1;
				  for(String city:cityMap.keySet()){
					  HSSFCell cityCell = row.createCell((short) (i++));
					  cityCell.setCellType(HSSFCell.CELL_TYPE_STRING);
					  HSSFRichTextString nameContent = new HSSFRichTextString(city);
					  cityCell.setCellValue(nameContent);
				  }
			  }
			  
			  System.out.println("文件生成..." + fileToWrite);
		}
		FileOutputStream out = new FileOutputStream(fileToWrite);
		out.flush();
		// 把相应的Excel 工作簿存盘
		wb.write(out);
		// 操作结束，关闭文件
		out.close();
	}
	public static void main(String[] args) throws Exception {
		new XLSWrite().writeXls(null);
	}
}