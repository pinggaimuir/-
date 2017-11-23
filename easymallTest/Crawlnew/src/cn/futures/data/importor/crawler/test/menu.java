package cn.futures.data.importor.crawler.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.Variety;
import cn.futures.data.importor.crawler.weatherCrawler.WeatherParamsMap;
import cn.futures.data.util.Constants;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.FileStrIO;
import cn.futures.data.util.MyHttpClient;

public class menu {
	
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private MyHttpClient httpClient = new MyHttpClient();
	private DAOUtils dao = new DAOUtils();
	private Log logger = LogFactory.getLog(menu.class);
	public void getMenu(){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File("e://menu.txt")),Constants.ENCODE_GB2312);
			BufferedReader reader = new BufferedReader(isr);
			String line="";
			StringBuffer buffer = new StringBuffer();
			while((line=reader.readLine())!=null){
				if(line.indexOf("##")!=-1){
					buffer.append(line.substring(17, line.length()-4)).append("\n");
				}else{
					String val = line.substring(34, line.length()-9);
					buffer.append("put(\"").append(val.substring(val.indexOf(">")+1))
						.append("\",\"").append(val.substring(0,val.indexOf("\"")))
						.append("\");").append("\n");
				}
			}
			System.out.println(buffer);
		} catch (Exception e) {
			
		}finally{
			
		}
	}
	
	public void test(){
		Set<String> hisContents = new HashSet<String>();
		String url="http://rp5.ru/%prov%_";
		String[] filters11={"span","class","verticalCenter"};
		String[] filters={"table","class","countryMap"};
		int[] index={1,2};
		for(String prov:WeatherParamsMap.rp5_China_his_weather_prov.keySet()){
			logger.info("************"+prov+"************");
			hisContents.clear();
			try {
				String pageUrl = url.replaceAll("%prov%", URLEncoder.encode(prov+"天气","utf-8"));
				String contents = fetchUtil.getPrimaryContent(0, pageUrl, "utf-8", prov+"城市", filters, null, 0);
				if(contents != null && !contents.equals("")){
					String[] divs = contents.split("<div class=RajTemp>");
					for(int i=1;i<divs.length;i++){
						String div = divs[i];
//						String comp="href=\"/([^\"]+)_\"(\\s)title=\"([^\"]+)\"><span(\\s)class=\"ToWeather\">>>>";
						String comp="href=\"/([^\"]+)_\">([^<]+)</a>";
						List<String> r = fetchUtil.getMatchStr(div, comp, index);
						if(r.size()>0){
							contents = fetchUtil.getPrimaryContent(0, url.replaceAll("%prov%", URLEncoder.encode(r.get(0),"utf-8")), "utf-8", r.get(1), filters, null, 0);
							if(contents != null && !contents.equals("")){
								String[] citys = contents.split("<div class=\"city_link\">");
								for(int j=1;j<citys.length;j++){
									comp="href=\"/([^\"]+)_\">";
									List<String> r1 = fetchUtil.getMatchStr(citys[j], comp, null);
									if(r1.size()>0){
										contents = fetchUtil.getPrimaryContent(0, url.replaceAll("%prov%", URLEncoder.encode(r1.get(0),"utf-8")), "utf-8", r1.get(0), filters11, null, 0);
										if(contents != null && !contents.equals("")){
											comp="href=\"http://rp5.ru/([^\"]+)_\"";
											List<String> r2 = fetchUtil.getMatchStr(contents, comp, null);
											if(r2.size()>0){
												hisContents.add(r2.get(0));
											}
										}
									}
								}
							}
						}
					}
				}else{
					pageUrl = url.replaceAll("%prov%", URLEncoder.encode(prov+"天气(地区)","utf-8"));
					contents = fetchUtil.getPrimaryContent(0, pageUrl, "utf-8", prov+"城市", filters, null, 0);
					if(contents != null && !contents.equals("")){
						String[] divs = contents.split("<div class=RajTemp>");
						for(int i=1;i<divs.length;i++){
							String div = divs[i];
							String comp="href=\"/([^\"]+)_\"(\\s)title=\"([^\"]+)\"><span(\\s)class=\"ToWeather\">>>>";
							List<String> r = fetchUtil.getMatchStr(div, comp, index);
							if(r.size()>0){
								contents = fetchUtil.getPrimaryContent(0, url.replaceAll("%prov%", URLEncoder.encode(r.get(0),"utf-8")), "utf-8", r.get(2), filters, null, 0);
								if(contents != null && !contents.equals("")){
									String[] citys = contents.split("<div class=\"city_link\">");
									for(int j=1;j<citys.length;j++){
										comp="href=\"/([^\"]+)_\">";
										List<String> r1 = fetchUtil.getMatchStr(citys[j], comp, null);
										if(r1.size()>0){
											contents = fetchUtil.getPrimaryContent(0, url.replaceAll("%prov%", URLEncoder.encode(r1.get(0),"utf-8")), "utf-8", r1.get(0), filters11, null, 0);
											if(contents != null && !contents.equals("")){
												comp="href=\"http://rp5.ru/([^\"]+)_\"";
												List<String> r2 = fetchUtil.getMatchStr(contents, comp, null);
												if(r2.size()>0){
													hisContents.add(r2.get(0));
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String tt:hisContents){
				logger.info(tt+"\n");
			}
		}
	}
	
	public void matchStr(){
		String path = "d:\\aaa.txt";
		String comp = "data-label=\"([^\"]+)\"\\sdata-faostat=\"([^\"]+)\"";
		int[] index={1,2};
		//读文件
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader r = new BufferedReader(isr);
			String line;
			while((line=r.readLine())!=null){
				List<String> rr = fetchUtil.getMatchStr(line, comp, index);
				if(rr.size()>0){
					System.out.println("put(\""+rr.get(0)+"\",\""+rr.get(1)+"\");");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setCrawId(){
		String[][] rr = FileStrIO.readXls("d:\\crawId.xlsx", "Sheet2");
		for(int i=0;i<rr.length;i++){
			String[] lines = rr[i];
			int varId = Variety.getVaridByName(lines[1]);
			if(varId == -1){
				logger.info(lines[1]+" 品种名不正确");
				continue;
			}
			dao.setTestTable(lines[0], varId, (int)Double.parseDouble(lines[2]));
		}
	}
	
	public static void main(String[] args){
		new menu().setCrawId();
	}
}
