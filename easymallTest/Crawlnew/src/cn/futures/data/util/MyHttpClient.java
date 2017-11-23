package cn.futures.data.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.format.datetime.joda.MillisecondInstantPrinter;

public class MyHttpClient {
	
	private static Logger logger = Logger.getLogger(MyHttpClient.class);
	
	/**
	 * 下载文件
	 * @param url 下载链接
	 * @param fileDir 文件保存路径及文件名
	 * */
	public static void fetchAndSave(String url, String fileDir) {
		FileOutputStream fileOutputStream = null;
		String dirPath = fileDir.substring(0, fileDir.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
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
			logger.info("文件保存成功@" + url);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
	}
	
	/**
	 * 下载文件
	 * @param url 下载链接
	 * @param fileDir 文件保存路径及文件名
	 * */
	public static void fetchAndSave(String url, String fileDir, Map<String, String> params) {
		FileOutputStream fileOutputStream = null;
		String dirPath = fileDir.substring(0, fileDir.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
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
		PostMethod postMethod = new PostMethod(url);
		// getMethod.getParams().setIntParameter(HttpMethodParams., value);
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		postMethod.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		if(params != null){
			for(String key: params.keySet()){
				postMethod.addParameter(key, params.get(key));
			}
		}
		try {
			int statusCode = client.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ postMethod.getStatusLine());
			}
			InputStream inputStream = postMethod.getResponseBodyAsStream();
			byte[] bytes = new byte[500];
			int count = 0;
			while ((count = inputStream.read(bytes)) != -1) {
				fileOutputStream.write(bytes, 0, count);

			}
			inputStream.close();
			fileOutputStream.close();
			logger.info("文件保存成功@" + url);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
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
	 * @param url  网页链接
	 * @param encoding 网页编码方式
	 * @param proxyStr 代理服务器信息
	 * @return
	 * 返回值   0：访问页面不存在；1：访问页面其他异常；2:html内容不完整；3：其他异常
	 * 连接超时时间延用之前定的一分钟
	 */
	public String getHtmlByHttpClient(String url, String encoding, String proxyStr) {
		String rslt = this.getHtmlByHttpClient(url, encoding, proxyStr, 60000);
		return rslt;
	}
	
	/**
	 * 通过HttpClient得到网页html内容
	 * @param url  网页链接
	 * @param encoding 网页编码方式
	 * @param proxyStr 代理服务器信息,不使用代理时写""或null
	 * @param timeout 超时时间
	 * @return
	 * 返回值   0：访问页面不存在；1：访问页面其他异常；2:html内容不完整；3：其他异常
	 */
	public String getHtmlByHttpClient(String url, String encoding, String proxyStr, int timeout) { 
        String searchHtml = "";  
        HttpClient httpClient = new HttpClient();  
        if(proxyStr == null){
        	proxyStr = "";
        }

		//因为代理已不可用，所以注释掉这一段
        //设置代理服务器的ip地址和端口  使用抢先认证
//		if(!proxyStr.equals("")){
//			String[] proxyTmp = proxyStr.split(",");
//	        httpClient.getHostConfiguration().setProxy(proxyTmp[0], Integer.parseInt(proxyTmp[1]));
//	        httpClient.getParams().setAuthenticationPreemptive(true);
//	       // Credentials upcreds = new UsernamePasswordCredentials("username", "password");
//	       // httpClient.getState().setProxyCredentials(AuthScope.ANY, upcreds);
//		}
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);  
        GetMethod getMethod = new GetMethod(url);  
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);  
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,  
                new DefaultHttpMethodRetryHandler());  
        getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        getMethod.setRequestHeader("Connection", "close");
        InputStream bodyIs = null; 
        BufferedReader br = null;
        try {  
            int statusCode = httpClient.executeMethod(getMethod);  //这儿也存在卡死现象。
            if (statusCode != HttpStatus.SC_OK) {   
            	if(statusCode == HttpStatus.SC_NOT_FOUND){
            		if( searchHtml.contains("proxy") ){
            			logger.error("代理服务器错误");
            			return "1";
            		}
            		logger.warn("访问页面不存在");
//    				return "0";//改，因可能是代理的问题
            		return "1";//改
            	}else{
            		logger.warn("网页服务器内部错误");
            		return "1";
            	}
            }   
            byte[] responseBody = getMethod.getResponseBody();
            searchHtml = new String(responseBody,encoding);  
            //访问网页异常
//          对于代理服务器来说，存在需要网关验证的情况 ，此时抓到的HTML网页是网关验证的页面
            if(!proxyStr.equals("") && !searchHtml.startsWith("<!DOCTYPE") && searchHtml.startsWith("<html")){
            	logger.warn("html内容不完整");
            	return "2";
            }
            return searchHtml;  
        }catch(IOException e){
        	e.printStackTrace();
        	logger.warn("通过HttpClient获得网页html内容时异常");
        	return "3";
        }catch (Exception e) { 
        	e.printStackTrace();
        	logger.warn("通过HttpClient获得网页html内容时异常");
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
				logger.warn("在通过HttpClient获得网页html内容过程中，字符流关闭异常");
				return "3";
			}
        }  
    } 
	
	/**
	 * Post提交
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 */
	public String getPostHtmlByHttpClient(String url, Map<String, String> params, String encoding ){
		String postHtml = "";
		try{
			//1 得到浏览器
			@SuppressWarnings("deprecation")
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    //2 指定请求方式
		    HttpPost httpPost = new HttpPost(url);
		    //3构建请求实体的数据
		    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		    for(String key:params.keySet()){
			    parameters.add(new BasicNameValuePair(key, params.get(key)));
		    }
		    //4 构建实体
		    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, encoding);
		    //5 把实体数据设置到请求对象
		    httpPost.setEntity(entity);
		    //6 执行请求
		    HttpResponse httpResponse = httpClient.execute(httpPost);
		    //7 判断请求是否成功
		    if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			    //获取服务器返回页面的值
			    HttpEntity en=httpResponse.getEntity();
			    postHtml = EntityUtils.toString(en,encoding).trim();
			    httpPost.abort();
		    }else{
			  
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println(postHtml);
		return postHtml;
	}
	
	public String getResponseBody(String url){
		String responseBody = "";
		@SuppressWarnings("deprecation")
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通过httpClient获取响应信息时异常");
		} 
		return responseBody;
	}
	

	/**
	 * get请求访问https链接
	 * @param url
	 * @return
	 * @author bric_yangyulin
	 * @date 2016-08-15
	 */
	public static String getStringByHttps(String url, String encoding) {
		String result = "";
		Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", https);
		GetMethod get = new GetMethod(url);
		HttpClient client = new HttpClient();
		try {
			client.executeMethod(get);
			result = get.getResponseBodyAsString();
			result = new String(result.getBytes("ISO-8859-1"), encoding);
			Protocol.unregisterProtocol("https");
			return result;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "error";
	}

	/**
	 * post请求访问https链接
	 * @param url
	 * @param body
	 * @param contentType
	 * @author bric_yangyulin
	 * @date 2016-08-15
	 * */
	public static String postByHttps(String url, String contentType, Map<String, String> params) {
		String result = "";
		Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", https);
		PostMethod post = new PostMethod(url);
		HttpClient client = new HttpClient();
		try {
			post.setRequestHeader("Content-Type", contentType);
			org.apache.commons.httpclient.NameValuePair[] paramBody = new org.apache.commons.httpclient.NameValuePair[params.size()];
			int i = 0;
			for(String key: params.keySet()){
				org.apache.commons.httpclient.NameValuePair pair = new org.apache.commons.httpclient.NameValuePair(key, params.get(key));
				paramBody[i] = pair;
				i++;
			}
			post.setRequestBody(paramBody);
			client.executeMethod(post);
			result = post.getResponseBodyAsString();
			Protocol.unregisterProtocol("https");
			return result;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return "error";
	}

	/**
	 * 访问以一个url获取cookie，之后带cookie去访问第二个url.
	 * @param urlBef 首先访问的url（如登录页）
	 * @param urlAfter 之后访问的url
	 * @param encoding 编码格式
	 * @param params 访问urlBef时post提交的参数（如用户名、密码）
	 * */
	public static String getResoucesByLoginCookies(String urlBef, String urlAfter, String encoding, Map<String, String> params) {
		StringBuilder responseContent = new StringBuilder();
		@SuppressWarnings("resource")
		DefaultHttpClient client = new DefaultHttpClient(new PoolingClientConnectionManager());
		/**
		 * 第一次请求登录页 获得cookie
		 * 相当于在登录页面点击登录，此处在URL�? 构造参数，
		 * 如果参数列表相当多的话可以使用HttpClient的方式构造参数
		 */
		HttpPost post = new HttpPost(urlBef);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	    for(String key:params.keySet()){
		    parameters.add(new BasicNameValuePair(key, params.get(key)));
	    }
		UrlEncodedFormEntity logEntity = null;
		try {
			logEntity = new UrlEncodedFormEntity(parameters, encoding);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		post.setEntity(logEntity);
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			response = client.execute(post);
			entity = response.getEntity();
			CookieStore cookieStore = client.getCookieStore();
			client.setCookieStore(cookieStore);
			
			//带着登录过的cookie请求下一个页面，可以是需要登录才能下载的url
			HttpGet get = new HttpGet(urlAfter);
			get.addHeader("Content-Type", "text/html;charset=" + encoding);
			response = client.execute(get);
			entity = response.getEntity();
			try {
				InputStream is = entity.getContent();
				byte[] byteA = new byte[1000];
				while(is.read(byteA, 0, 1000) != -1){
					responseContent.append(new String(byteA, encoding));
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		return responseContent.toString();
	}
	
	/**
	 * https协议  post请求  下载文件
	 * */
	public static void fetchAndSaveHttps(String url, String fileDir, Map<String, String> params){
		FileOutputStream fileOutputStream = null;
		String dirPath = fileDir.substring(0, fileDir.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
		File dir = new File(dirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
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
		Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", https);
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("referer", url);
		post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		org.apache.commons.httpclient.NameValuePair[] paramBody = new org.apache.commons.httpclient.NameValuePair[params.size()];
		int i = 0;
		for(String key: params.keySet()){
			org.apache.commons.httpclient.NameValuePair pair = new org.apache.commons.httpclient.NameValuePair(key, params.get(key));
			paramBody[i] = pair;
			i++;
		}
		post.setRequestBody(paramBody);
		InputStream is = null;
		try {
			client.executeMethod(post);
			is = post.getResponseBodyAsStream();
			byte[] byteA = new byte[1000];
			int count = 0;
			while((count = is.read(byteA, 0, 1000)) != -1){
				fileOutputStream.write(byteA, 0, count);
			}
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Protocol.unregisterProtocol("https");
	}
	
	/**
	 * @param tarUrl 目标地址
	 * @param postJson json格式参数
	 * @param contentType 如："application/json;charset=utf-8"
	 * @param encoding 编码
	 * */
	public static String httpsPostJson(String tarUrl, String postJson, String contentType, String encoding){
		String html = null;
		Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", https);
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(tarUrl);
		post.setRequestHeader("referer", "https://apps.fas.usda.gov/psdonline/app/index.html#/app/advQuery");
		post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
		post.addRequestHeader("Content-Type", contentType);
		RequestEntity requestEntity = null;
		try {
			requestEntity = new StringRequestEntity(postJson, contentType, encoding);
			post.setRequestEntity(requestEntity);
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1);
		}
		InputStream is = null;
		try {
			int status = client.executeMethod(post);
			if(status == 200){
//				is = post.getResponseBodyAsStream();
//				byte[] tempByte = new byte[1000];
//				int count = 0;
//				while((count = is.read(tempByte, 0, 1000)) != -1){
//					html.append(new String(tempByte, "UTF-8"));
//				}
				html = post.getResponseBodyAsString();
			} else {
				logger.info(status);
			}
		} catch (HttpException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		Protocol.unregisterProtocol("https");
		return html;
	}
	
	public static void main(String[] args) {
		String url = "https://apps.fas.usda.gov/PSDOnlineApi/api/query/RunQuery";
		String postJson = "{\"queryId\":0,\"commodityGroupCode\":null,\"commodities\":[\"0011000\"],\"attributes\":[25],\"countries\":[\"ALL\"],\"marketYears\":[2017,2016,2015,2014],\"chkCommoditySummary\":false,\"chkAttribSummary\":false,\"chkCountrySummary\":false,\"commoditySummaryText\":\"\",\"attribSummaryText\":\"\",\"countrySummaryText\":\"\",\"optionColumn\":\"year\",\"chkTopCountry\":false,\"topCountryCount\":\"\",\"chkfileFormat\":false,\"chkPrevMonth\":false,\"chkMonthChange\":false,\"chkCodes\":false,\"chkYearChange\":false,\"queryName\":\"\",\"sortOrder\":\"Commodity/Attribute/Country\",\"topCountryState\":false}";
		String html = MyHttpClient.httpsPostJson(url, postJson, "application/json;charset=utf-8", "utf-8");
		try {
			FileStrIO.saveStringToFile2(html, "D:\\Test\\USDA_TEST.txt", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
