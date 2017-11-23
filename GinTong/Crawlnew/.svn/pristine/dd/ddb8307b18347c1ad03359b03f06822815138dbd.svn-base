package cn.futures.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import cn.futures.data.util.FileStrIO;

/**
 * 解析新注册账号
 * */
public class ParseNewAccount {
	
	//返回文件列表
	private String[] getFileList(String tarDirPath){
		File tarDir = new File(tarDirPath);
		String[] tarFiles = tarDir.list();
		String parentPath = tarDir.getPath();
		for(int i = 0; i < tarFiles.length; i++){
			tarFiles[i] = parentPath + File.separator + tarFiles[i];
		}
		return tarFiles;
	}
	
	private void parseFiles(String[] files){
		for(String file: files){
			FileInputStream fileStream = null;
			BufferedReader reader = null;
			try {
				fileStream = new FileInputStream(file);
				reader = new BufferedReader( new InputStreamReader(fileStream, "GBK"));
				String tempString = null;
				while ((tempString = reader.readLine()) != null){
//					System.out.println(tempString);
					if(tempString.contains("entity=[联系人],act=[新建]")){
						if(tempString.contains("13716150213") && tempString.contains("15501527156") 
								&& tempString.contains("18120043569")){
							continue;//测试手机号
						}
						System.out.println(tempString);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(reader != null){
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			
		}
	}
	
	public static void main(String[] args) {
		ParseNewAccount p = new ParseNewAccount();
		String[] files = p.getFileList("C:\\Users\\bric_yangyulin\\Desktop\\临时文件\\新建文件夹");
		p.parseFiles(files);
	}
}
