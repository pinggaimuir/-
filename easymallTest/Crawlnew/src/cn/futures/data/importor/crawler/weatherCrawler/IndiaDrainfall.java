package cn.futures.data.importor.crawler.weatherCrawler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.MapInit;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.MyHttpClient;

public class IndiaDrainfall {
	
	private MyHttpClient httpClient = new MyHttpClient();
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private Log logger = LogFactory.getLog(IndiaDrainfall.class);
	private DAOUtils dao = new DAOUtils();
	private String url="http://www.imd.gov.in/section/hydro/distrainfall/webrain/%state%/%city%.txt";
	
	public void start(){
		fetchDrain();
	}
	
	private void fetchDrain(){
		logger.info("=====开始抓取印度月度降水量=====");
		for(String stateTmp:MapInit.india_drainfall_state_map.keySet()){
			Map<String, String> cityMap = MapInit.india_drainfall_state_map.get(stateTmp);
			String state = stateTmp.split("-")[0];
			String varName = stateTmp.split("-")[1];
			int varId = Variety.getVaridByName(varName);
			String stateCode = MapInit.india_state2html_map.get(state);
			for(String cityTmp:cityMap.keySet()){
				logger.info(state+"-"+cityTmp);
				String cnName = cityTmp;
				String pageUrl = url.replaceAll("%state%", stateCode).replaceAll("%city%", cityMap.get(cityTmp));
				//String response  = CrawlerUtil.httpGetBody(pageUrl);
				String response = httpClient.getResponseBody(pageUrl);
				if(response!=null && !response.equals("")){
					String[] lines = response.split("\n");
					int[] drainIndexs = new int[12];//标记降雨量位置
					int[] biasIndexs = new int[12];//标记偏离值
					int k1=0,k2=0;
					int i=0;
					for(i=0;i<lines.length;i++){
						String line = lines[i];
						if(line.indexOf("R/F %DEP.")!=-1){
							for(int j=0;j<line.length();j++){
								if(line.charAt(j)=='F')
									drainIndexs[k1++]=j;
								if(line.charAt(j)=='.')
									biasIndexs[k2++]=j;
							}
							break;
						}
					}
					Map<String, String> dataMap = new HashMap<String, String>();
					for(int ii=i+2;ii<lines.length;ii++){
						String line = lines[ii];
						String comp = "^([0-9]{4}\\s)";
						List<String> r = fetchUtil.getMatchStr(line, comp, null);
						if(r.size()>0){
							String year = r.get(0).trim();
							char[] chars = line.toCharArray();
							for(int index=0;index<drainIndexs.length;index++){//降雨量
								StringBuffer val = new StringBuffer();
								for(int jj=drainIndexs[index];jj>0;jj--){
									if((chars[jj]<='9' && chars[jj]>='0')||chars[jj]=='.'||chars[jj]=='-')
										val.insert(0,chars[jj]);
									else {
										if(!val.toString().equals("")){
											dataMap.clear();
											String timeInt = year+((index+1+"").length()==1?("0"+(index+1)):(index+1));
											dataMap.put("降雨量", val.toString());
											dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
										}
										break;
									}
								}
							}
							for(int index=0;index<biasIndexs.length;index++){//偏离值
								StringBuffer val = new StringBuffer();
								for(int jj=biasIndexs[index];jj>0;jj--){
									if(jj>chars.length-1) break;
									if((chars[jj]<='9' && chars[jj]>='0')||chars[jj]=='.'||chars[jj]=='-')
										val.insert(0,chars[jj]);
									else {
										if(!val.toString().equals("")){
											dataMap.clear();
											String timeInt = year+((index+1+"").length()==1?("0"+(index+1)):(index+1));
											dataMap.put("累计降雨比历史正常值", val.toString());
											dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new IndiaDrainfall().start();
	}  
}
