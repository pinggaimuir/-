package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;

public class ChinaWeather_rp5 {
	public static String CHINA_HISWEATHER_ROOT = "D:\\Crawlers\\Data\\CHINA_HISWEATHER\\";
	private MyHttpClient httpClient = new MyHttpClient();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private Log logger = LogFactory.getLog(ChinaWeather_rp5.class);
	private String url="http://rp5.ru/%city%_";
	private String filePath = "http://rp5.ru/objects";
	private String postUrl = "http://rp5.ru/inc/f_archive.php";
	private Map<String, String> params = new HashMap<String, String>(){
		{
			put("a_date1","01.01.2005");
			put("a_date2","07.08.2015");
			put("f_ed3","8");
			put("f_ed4","8");
			put("f_ed5","7");
			put("f_pe","1");
			put("f_pe1","1");
			put("lng_id","8");
			put("type","xls");
			put("wmo_id","54534");
		}
	};
	public String getDownloadUrl(String code){
		params.put("wmo_id",code);
		String html = httpClient.getPostHtmlByHttpClient(postUrl, params, "utf-8");
		String comp = "../../objects([^>]+)>";
		List<String> r = fetchUtil.getMatchStr(html, comp, null);
		return r.get(0);
	}
	
	public void fetch(){
		String comp = "document.fwmo.wmo_id.value(\\s)=(\\s)'([^']+)';";
		int[] index = {1,2,3};
		for(String city:WeatherParamsMap.China_his_weather_cities){
			logger.info("********"+city+"********");
			try {
				String pageUrl = url.replaceAll("%city%", URLEncoder.encode(city,"utf-8"));
				String contents = httpClient.getResponseBody(pageUrl);
				if(contents != null && !contents.equals("")){
					List<String> r = fetchUtil.getMatchStr(contents, comp, index);
					if(r.size()>0){
						String code = r.get(2);
						logger.info("选择到文件gz(历史)");
						String path = filePath+getDownloadUrl(code);;
						String zipName = path.substring(path.lastIndexOf("/")+1);
						logger.info(city+"下载");
						FileStrIO.downloadFile(CHINA_HISWEATHER_ROOT+city+zipName, path);
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args){
		new ChinaWeather_rp5().fetch();
	}
}
