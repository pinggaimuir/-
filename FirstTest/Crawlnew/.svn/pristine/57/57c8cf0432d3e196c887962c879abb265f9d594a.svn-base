package cn.futures.data.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class WebServiceTool {

	private static final Logger log = Logger.getLogger(WebServiceTool.class);
	
	/**
	 * 调用crm接口工具方法
	 * 参数说明：访问webService地址、给此地址传递的参数
	 * 返回值说明：访问成功（200） 失败（>=400）
	 * 丁鹏
	 * 2015.7.17
	 */
	public String webSercviceTool(String regUrl, String urlParas){
		String restr = null;
		StringBuffer contentBuffer = new StringBuffer();
		int responseCode = -1;
		HttpURLConnection con = null;
		try {
			URL url = new URL(regUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			con.setRequestMethod("post");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(urlParas);
			responseCode = con.getResponseCode();
			InputStream inStr = con.getInputStream();
			InputStreamReader istreamReader = new InputStreamReader(inStr, "utf-8");
			BufferedReader buffStr = new BufferedReader(istreamReader);
			int line_num = 0;
			String str = null;
			while (line_num < 50 && (str = buffStr.readLine()) != null){
				contentBuffer.append(str);
				line_num++;
			}
			if (responseCode == -1) {
				log.error(url.toString() + " : connection is failure...");
				con.disconnect();
			}
			if (responseCode >= 400){//请求失败
				log.error("请求失败 或IP异常:get response code: " + responseCode);
				con.disconnect();
			}
			if(responseCode == 200){
				log.info("接口访问成功，接口地址：" + regUrl + "; 接口参数：" + urlParas);
				restr = "200";
			}else{
				log.error("接口访问失败，接口地址：" + regUrl + "; 接口参数：" + urlParas);
			}
			out.flush();
			out.close();
			inStr.close();
			//System.out.println("contentBuffer : " + contentBuffer);
		} catch (IOException e) {
			contentBuffer = null;
			log.error("error: 注册接口访问出错！",e);
		} finally {
			con.disconnect();
		}
		return contentBuffer != null ? contentBuffer.toString() : "";
	}
	


	/**
	 * @param httpUrl
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
//	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
//	        connection.setRequestProperty("apikey",  "7e56792222e0d986a2a57d497e3753aa");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	log.error(e);
	    }
	    return result;
	}
	
	/**
	 * post请求调用跨域接口(未获取返回值)
	 * @author bric_yangyulin
	 * @date 2016-07-27
	 * */
	public static String postRequest(String httpUrl, String httpArg) {
	    String result = null;
	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("POST");
	        // 填入apikey到HTTP header
	        connection.setConnectTimeout(10000);//连接超时 单位毫秒
	        connection.setReadTimeout(10000);//读取超时 单位毫秒
	        connection.setDoOutput(true);// 是否输入参数
	        // 表单参数与get形式一样
	        byte[] bypes = httpArg.getBytes("utf-8");
	        connection.getOutputStream().write(bypes);// 输入参数
	        int responseCode = connection.getResponseCode();
	        result = String.valueOf(responseCode);
	    } catch (Exception e) {
	    	log.error(e);
	    }
	    return result;
	}

}
