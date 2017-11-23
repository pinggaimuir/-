package com.bric.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class MyHttpClient {

	public static void fetchAndSave(String url, String fileDir) {
		FileOutputStream fileOutputStream = null;
		File file = new File(fileDir);

		try {
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);

		} catch (IOException e) {

			e.printStackTrace();
		}
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		// getMethod.getParams().setIntParameter(HttpMethodParams., value);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		getMethod.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);

		try {
			int statusCode = client.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			byte[] bytes = new byte[500];
			int count = 0;
			while ((count = inputStream.read(bytes)) != -1) {
				fileOutputStream.write(bytes, 0, count);

			}
			inputStream.close();
			fileOutputStream.close();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();

		}
	}

	public static byte[] get(String url) {

		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			int statusCode = client.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			byte[] bytes = new byte[1024 * 2000];
			int index = 0;
			int count = inputStream.read(bytes, index, 500);
			while (count != -1) {
				index += count;
				count = inputStream.read(bytes, index, 1);
			}
			// byte[] bytes2 = bytes.;
			return bytes;
		} catch (HttpException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			getMethod.releaseConnection();

		}
	}

	public static void saveFile(byte[] content, String fileUrl) {
		File file = new File(fileUrl);
		if (content == null || content.equals("")) {
			System.out.println("There is no content");
		} else {
			try {
				if (file.exists()) {
					file.delete();
					file.createNewFile();
				}
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(content);
				fileOutputStream.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 通过HttpClient得到网页html内容
	 * @param url
	 * @param encoding
	 * @param proxyStr
	 * @return
	 * 返回值   0：访问页面不存在；1：访问页面其他异常；2:html内容不完整；3：其他异常
	 */
	public String getHtmlByHttpClient(String url, String encoding, String proxyStr) {  
        String searchHtml = "";  
        HttpClient httpClient = new HttpClient();  
        //设置代理服务器的ip地址和端口  使用抢先认证
		if(!proxyStr.equals("")){
			String[] proxyTmp = proxyStr.split(",");
	        httpClient.getHostConfiguration().setProxy(proxyTmp[0], Integer.parseInt(proxyTmp[1]));
	        httpClient.getParams().setAuthenticationPreemptive(true);
		}
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);  
        GetMethod getMethod = new GetMethod(url);  
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);  
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,  
                new DefaultHttpMethodRetryHandler());  
        InputStream bodyIs = null; 
        BufferedReader br = null;
        try {  
            int statusCode = httpClient.executeMethod(getMethod);  
            //访问网页异常
            if (statusCode != HttpStatus.SC_OK) { 
            	if(statusCode == HttpStatus.SC_NOT_FOUND){
            		System.out.println("访问页面不存在");
    				return "0";
            	}else{
            		System.out.println("服务器禁止访问");
            		return "1";
            	}
            } 
            bodyIs = getMethod.getResponseBodyAsStream();//  
            br = new BufferedReader(new InputStreamReader(bodyIs, encoding));  
            StringBuffer sb = new StringBuffer();  
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line);  
            }  
            searchHtml = sb.toString();
            if(!searchHtml.startsWith("<!DOCTYPE")){
            	System.out.println("html内容不完整");
            	return "2";
            }
            return searchHtml;  
        } catch (Exception e) { 
        	e.printStackTrace();
        	System.out.println("通过HttpClient得到网页html内容时异常");
        	return "3";
        } finally {
        	getMethod.releaseConnection(); 
        	try {
        		if(br != null){
					br.close();
        		}
        		if(bodyIs != null){
        			bodyIs.close();
        		}
			} catch (IOException e) {
				System.out.println("流关闭时异常");
				return "3";
			}
        }  
    } 
}
