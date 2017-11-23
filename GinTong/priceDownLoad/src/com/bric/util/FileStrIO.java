package com.bric.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class FileStrIO {
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
		saveStringToFile(dataString, fullFilePath);
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
	 */
	public static void saveStringToFile(String dataString, String fullFilePath)
			throws IOException{
		if (!"\n".equals(newLine))
			dataString = dataString.replaceAll("\n", newLine);
		File file = new File(fullFilePath);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(dataString.getBytes(Constants.FILE_ENCODING));
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
	 * @return String
	 * 		文件中的换行在串中用\n表示
	 * @throws IOException
	 */
	public static String loadStringFromFile(String fileFullPath)
			throws IOException{
		StringBuffer fileContentStr = new StringBuffer();
		try{
			FileInputStream fileStream = new FileInputStream(fileFullPath);
			BufferedReader reader = new BufferedReader( new InputStreamReader(fileStream, "GB2312"));
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
}
