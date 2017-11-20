package cn.futures.data.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.net.URI;
import java.net.URL;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileStrIO {
	
	private static Log logger = LogFactory.getLog(FileStrIO.class);
	static final String newLine = System.getProperty("line.separator");
	private FileStrIO(){}
	
	/**
	 * 将字符串存入指定目录下指定名称的文件，若目录或文件不存在则将被创建
	 * @param dataString
	 * 		待存入字符串，字符串中的所有\n将被替换为系统定义的换行符
	 * @param dirStr
	 * 		待存入目录路径
	 * @param fileStr
	 * 		待存入文件名
	 * @throws IOException
	 */
	public static void saveStringToFile(String dataString, String dirStr, String fileStr)
			throws IOException{
		String fullFilePath = dirStr + fileStr;
		File idDir = new File(dirStr);
		if (!idDir.exists()) {
			idDir.mkdirs();
		}
		saveStringToFile2(dataString, fullFilePath, Constants.ENCODE_GB2312);
	}
	
	
	/**
	 * 将字符串存入指定目录下指定名称的文件，若目录或文件不存在则将被创建
	 * @param dataString
	 * 		待存入字符串，字符串中的所有\n将被替换为系统定义的换行符
	 * @param dirStr
	 * 		待存入目录路径
	 * @param fileStr
	 * 		待存入文件名
	 * @param encoding
	 * 		编码
	 * @throws IOException
	 * @author bric_yangyulin
	 * @date 2016-04-29
	 */
	public static void saveStringToFile(String dataString, String dirStr, String fileStr, String encoding)
			throws IOException{
		String fullFilePath = dirStr + fileStr;
		File idDir = new File(dirStr);
		if (!idDir.exists()) {
			idDir.mkdirs();
		}
		saveStringToFile2(dataString, fullFilePath, encoding);
	}
	
	/**
	 * 将字符串存入指定目录下指定名称的文件，若文件不存在则将被创建
	 * @param dataString
	 * 		待存入字符串，字符串中的所有\n将被替换为系统定义的换行符
	 * @param dirStr
	 * 		待存入目录路径
	 * @param fileStr
	 * 		待存入文件名
	 * @throws IOException
	 * 		目录不存在或发生其它存取错误时将抛出该异常
	 * @author bric_yangyulin
	 * @date 2016-04-29
	 */
	public static void saveStringToFile2(String dataString, String fullFilePath, String encoding)
			throws IOException{
		if (!"\n".equals(newLine))
			dataString = dataString.replaceAll("\n", newLine);
		File file = new File(fullFilePath);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(dataString.getBytes(encoding));
			fileOutputStream.close();
		}
		catch (FileNotFoundException e){
			String errorMsg = "Error While Saving " + fullFilePath + ", Folder Does Not Exist\n" + e.getMessage();
			throw new IOException(errorMsg, e);			
		}
		catch (IOException e) {
			String errorMsg = "Error While Saving " + fullFilePath + "\n" + e.getMessage();
			throw new IOException(errorMsg, e);
		}
	}
	
	/**
	 * 从文件中读取字符串，若文件不存在则输出提示并返回空串
	 * @param fileFullPath
	 * @Param encoding 编码
	 * @return String
	 * 		文件中的换行在串中用\n表示
	 * @throws IOException
	 */
	public static String loadStringFromFile(String fileFullPath, String encoding)
			throws IOException{
		StringBuffer fileContentStr = new StringBuffer();
		try{
			FileInputStream fileStream = new FileInputStream(fileFullPath);
			BufferedReader reader = new BufferedReader( new InputStreamReader(fileStream, encoding));
			String tempString = null;
			while ((tempString = reader.readLine()) != null){
				fileContentStr.append(tempString);
				fileContentStr.append("\n");
			}
			reader.close();
		}
		catch (FileNotFoundException e){
			System.out.println("File Not Exist:" + fileFullPath);
		}
		catch(IOException e){
			String errorMsg = "Error While Loading: " + fileFullPath + "\n" + e.getMessage();
			throw new IOException(errorMsg, e);
		}
		return fileContentStr.toString();
	}
	
	public static void appendStringToFile(String dataString, String dirStr, String fileStr, String encoding)
			throws IOException{
		String fullFilePath = dirStr + fileStr;
		File idDir = new File(dirStr);
		if (!idDir.exists()) {
			idDir.mkdirs();
		}
		appendStringToFile(dataString, fullFilePath, encoding);
	}
	
	public static void appendStringToFile(String dataString, String fullFilePath, String encoding)
			throws IOException{
		BufferedWriter out = null;
		try {
			if(encoding == null){
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(fullFilePath, true)));
			} else {
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(fullFilePath, true), encoding));
			}
			out.write(dataString+"\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
     * 下载文件保存到本地 
     *  
     * @param path 
     *            文件保存位置 
     * @param url 
     *            文件地址 
     * @throws IOException 
     */  
    public static void downloadFile(String path, String strUrl){  
		HttpClient client = null;  
        try {  
        	URL url = new URL(strUrl);
        	URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        	client  = new DefaultHttpClient();
        	HttpGet httpget = new HttpGet(uri);
            // 发送请求获得返回结果  
            HttpResponse response = client.execute(httpget);  
            // 如果成功  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {  
                byte[] result = EntityUtils.toByteArray(response.getEntity());  
                BufferedOutputStream bw = null;  
                try {  
                    // 创建文件对象  
                    File f = new File(path);  
                    // 创建文件路径  
                    if (!f.getParentFile().exists())  
                        f.getParentFile().mkdirs();  
                    // 写入文件  
                    bw = new BufferedOutputStream(new FileOutputStream(path));  
                    bw.write(result);  
                } catch (Exception e) { 
                	logger.error("文件下载：关闭输出流时异常");
                } finally {  
                    try {  
                        if (bw != null)  
                            bw.close();  
                    } catch (Exception e) { 
                    	logger.error("文件下载：关闭输出流时异常");
                    }  
                }  
            }else {  
            	logger.error("文件下载：不能成功链接到服务器");
            }  
        } catch (Exception e) {  
            logger.error("文件下载时异常");
        } finally {  
            try {  
                client.getConnectionManager().shutdown();  
            } catch (Exception e) {
            	logger.error("关闭链接时异常");
            }  
        }  
    }  
    
    public static Workbook create(InputStream inp) throws IOException,InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }
    /**
     * 读xls表格，如果存在sheetName找对应工作簿的表格，为空的话找第一个不为空的工作薄
     * @param path 文件全路径
     * @param sheetName 工作表名
     * @param maxColumns 最大列数
     * @param startRowIndex 起始行号
     * @return
     */
    public static String[][] readXls(String path, String sheetName, int maxColumns, int startRowIndex){
    	String[][] datas = null;
		InputStream is;
		Workbook hssfWorkbook;
		try {
			is = new FileInputStream(path);
			hssfWorkbook = create(is);
			int sheetNums = hssfWorkbook.getNumberOfSheets();
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < sheetNums; numSheet++) {
				Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				if(sheetName == null
						|| (sheetName != null && sheetName.equals(hssfSheet.getSheetName()))){
					// 循环行Row
					int rowNums = hssfSheet.getLastRowNum();
					datas = new String[rowNums+1][maxColumns];
					for (int rowNum = startRowIndex; rowNum <= rowNums; rowNum++) {
						Row hssfRow = hssfSheet.getRow(rowNum);
						if (hssfRow != null) {
							for(int colNum=0;colNum<maxColumns;colNum++){
								Cell cell = hssfRow.getCell(colNum);
								if(cell != null){
									datas[rowNum][colNum] = getValue(cell);
								}else{
									continue;
								}
							}
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读xls异常");
		}
		return datas;
	}
    
    /**
     * 读xls表格，如果存在sheetName找对应工作簿的表格，为空的话找第一个不为空的工作薄（最大列数取默认值30，起始行号取取默认值1）
     * @param path
     * @param sheetName
     * @return
     */
    public static String[][] readXls(String path, String sheetName){
		return readXls(path, sheetName, 30);
    }
    
    /**
     * 读xls表格，如果存在sheetName找对应工作簿的表格，为空的话找第一个不为空的工作薄（起始行号取取默认值1）
     * @param path
     * @param sheetName
     * @param maxColumns 最大列数
     * @return
     */
    public static String[][] readXls(String path, String sheetName, int maxColumns){
		return readXls(path, sheetName, maxColumns, 1);
    }
    
	private static String getValue(Cell hssfCell) {
		if(hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		}else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC || hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		}else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BLANK) {
			return "";
		}else{
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	
	public static String getTxtContent(String path){
		return getTxtContent(new File(path));
	}
	
	public static String getTxtContent(File file){
		StringBuilder contents = new StringBuilder();//运用String的“+”操作容易造成内存溢出情况
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(reader);
			String line;
			while((line=br.readLine())!=null){
				contents.append(line+"\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contents.toString();
	}
	
	/**
	 * 一个全大写的单词转为首字母大写
	 * */
	public static String initialUppercaseOneWord(String word){
		char[] chars = word.toCharArray();
		for(int i = 1; i < chars.length; i++){
			chars[i] += 32;
		}
		return String.valueOf(chars);
		
	}
	
	/**
	 * 
	 * */
}
