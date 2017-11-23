package com.bric.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class ProxyManagerUtil {

	//private static  List<String[]> proxyContents = null;
	private static List<String> proxies = null;
	private static int proxyNum,currProxy = -1 ;
	private static String proxyStr = "";
	private static String proxyfile = "D:/crawlers/bin/proxynew.txt";

	static{
		initListFromFile();
	}

	/**
	 * 代理初始化
	 */

	private static void initListFromFile(){
		try{
			System.out.println("从文件初始化代理列表");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proxyfile)));
			String line = "";
			proxies= new ArrayList<String>();
			while((line=br.readLine())!=null){
				proxies.add(line);
			}
			br.close();
			proxies = removeInvalidProxy(proxies);

			proxyNum = proxies.size();
			System.out.println("当前总共有 "+proxyNum+" 个可用代理。");
			useProxy(0);

		}catch(Exception e){

		}
	}

	private static void initListFromWeb2(){
		try{
			System.out.println("获取代理列表");
			String proxyfile = "D:/crawlers/bin/proxynew.txt";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proxyfile)));
			String line = "";
			List<String> oldlist= new ArrayList<String>();
			while((line=br.readLine())!=null){
				oldlist.add(line);
			}
			br.close();
			OutputStream os = new FileOutputStream(new File(proxyfile),true);
			String baseUrl="http://www.kuaidaili.com/proxylist/";
			for(int x=1;x<=10;x++){
				String webUrl = baseUrl+x+"/";
				String[] filters = {"table", "class", "table table-bordered table-striped"};
				String contents = "";//(new DataFetchUtil(true)).getPrimaryContent(0, webUrl, "utf-8", "代理列表网页", filters, null, 0);

				contents = contents.substring(contents.indexOf("<tbody>"),contents.indexOf("</tbody>"));
				String[] lines=contents.split("<tr>|<tr class=\"odd\">");
				for(int i=1;i<lines.length && i<20;i++){
					String[] ss = lines[i].split("<td>");
					Pattern p = Pattern.compile("((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)");
		            Matcher match = p.matcher(ss[1]);
		            String ip,port;
		            if (match.find()){//匹配IP地址
		                ip = match.group();
		            }else{
		            	continue;
		            }
		            p = Pattern.compile("\\d+");
		            match = p.matcher(ss[2]);
		            if(match.find()){
		            	port = match.group();
		            }else{
		            	continue;
		            }
		            //System.out.println("解析代理："+ip+":"+port);
		            String proxy2 = ip+","+port;
		            if(!oldlist.contains(proxy2)){
		            	oldlist.add(proxy2);
		            	os.write((proxy2+"\n").getBytes());
		            	os.flush();
		            }
		            if(proxies == null)
		            	proxies = oldlist;
		            if(!proxies.contains(proxy2)){
		            	proxies.add(proxy2);
		            }
				}
			}
			os.close();
			proxies = removeInvalidProxy(proxies);
			proxyNum = proxies.size();
			System.out.println("当前总共有 "+proxyNum+" 个可用代理。");
			if(proxyNum==0){
				System.out.println("代理都不能用了啊，搞毛,重新从文件里面load吧");
				//Thread.sleep(60000);
				initListFromFile();
			}
			if(proxyNum == 0){
				System.out.println("还是不行啊");
				proxies.add("");
				proxyNum = 1;
				currProxy = 0;
			}
			useProxy(0);
		}catch(Exception e){
			System.out.println("下载代理目录出问题了");
			e.printStackTrace();
		}
	}

	private static void useProxy(int x){
		currProxy = x;
		proxyStr = proxies.get(currProxy%proxyNum);
		System.out.println("切换到代理："+proxyStr);
	}

	/**
	 * 剔除当前不可用代理,使用线程池
	 * @param proxyList
	 */
	private static List<String> removeInvalidProxy(List<String> list){
		if(list == null)
			return null;
		//定义一个可缓存线程池
		System.out.println("测试代理可用性");
		ExecutorService exe = Executors.newFixedThreadPool(40);
		final List<String> tmp = new ArrayList<String>();
		final List<Integer> delete = new ArrayList<Integer>();//声明一个存储不可用ip的list
		//long start = System.currentTimeMillis();
		for(int i=list.size()-1;i>=0;i--){
			final int index = i;

			final String proxyStr = list.get(i);
			String[] strs = proxyStr.split(",");
			if(strs == null || strs.length < 2)
				continue;
			final String ip = strs[0];
			final int port = Integer.parseInt(strs[1]);

			try{
				final URL url = new URL("http://www.baidu.com");
				final String url2 = "http://www.baidu.com";
				//执行线程
				exe.execute(new Runnable() {

	    			@Override
	    			public void run() {
	    				int state;
	    				Proxy proxyTmp;
	    				URLConnection conn;
	    				proxyTmp = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,port));
	    				try {
    					  	HttpClient httpClient = new HttpClient();
    				        //设置代理服务器的ip地址和端口  使用抢先认证
    					    httpClient.getHostConfiguration().setProxy(ip, port);
    					    httpClient.getParams().setAuthenticationPreemptive(true);
    				        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
    				        GetMethod getMethod = new GetMethod(url2);
    				        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
    				        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
    				        getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
    				        getMethod.setRequestHeader("Connection", "close");
    				        int statusCode = httpClient.executeMethod(getMethod);
	    				    System.out.println(ip+":"+port+"  statusCode:"+statusCode);
	    					if(statusCode != HttpStatus.SC_OK){
	    						delete.add(index);
	    					}else{
	    						String searchHtml = getMethod.getResponseBodyAsString();
	    						if(!searchHtml.contains("百度")){
	    							delete.add(index);
	    						}
	    						System.out.println("【当前可用代理】proxy："+proxyStr);
	    						if(!tmp.contains(proxyStr)){
	    							tmp.add(proxyStr);
	    						}
		    					/*conn = url.openConnection(proxyTmp);
		    					conn.setConnectTimeout(5000);
		    					conn.setReadTimeout(5000);
		    					state = ((HttpURLConnection)conn).getResponseCode();
		    					if(state != 200) {
		    						delete.add(index);//添加不可用ip到delete
		    						//System.out.println("【当前不可用代理】proxy："+ip+",port:"+port);
		    					}else{
		    						InputStream in = conn.getInputStream();
		    						String s = IOUtils.toString(in);
		    						if( !s.contains("百度")){
		    							delete.add(index);
		    							System.out.println(s);
		    						}
		    						System.out.println("【当前可用代理】proxy："+ip+",port:"+port);
		    					}*/
	    					}
	    				} catch (IOException e) {
	    					delete.add(index);//添加不可用ip到delete
	    					//System.out.println("【当前不可用代理】proxy："+ip+",port:"+port);
	    				} catch(Exception e){
	    					delete.add(index);
	    				}
	    			}
	    		});
			}catch(Exception e){
				System.out.println("百度的格式或者路径错误，不能测试出不可用代理");
				continue;
			}
		}

		exe.shutdown(); //关闭线程池
	    while (true) {
	    	//判断线程是否全部执行完毕
	      if (exe.isTerminated()) {
	    	 /* //long end = System.currentTimeMillis();
	  			//System.out.println("线程池运行时间为:"+(end-start));
	  			//System.out.println("总代理数为："+list.size());
	  			//System.out.println("不可用代理数为："+delete.size());
	  			if(delete != null && delete.size()>0){
	  				Collections.sort(delete);//对delete进行排序
		  			for(int i=delete.size()-1;i>=0;i--){
		  				System.out.println("remove proxy: "+list.get(delete.get(i)));
		  				int index = (Integer) delete.get(i);
		  				list.remove(index);//从list中删除不可用ip
		  			}
	  			}*/
	        break;
	      }
	      try {
			Thread.sleep(1000);
	      } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	      }
	    }
	    return tmp;
	}

	public static String getProxyStr(){
		return proxyStr;
	}

	public static void changeProxy(){
		currProxy ++;
		currProxy %= proxyNum;
		useProxy(currProxy);
	}
	public static void main(String[] args) throws Exception{
		//new ProxyManagerUtil();
		initListFromFile();
	}
}
