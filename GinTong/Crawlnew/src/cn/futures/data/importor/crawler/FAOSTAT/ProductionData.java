package cn.futures.data.importor.crawler.FAOSTAT;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.Variety;
import cn.futures.data.service.FAOSTATService;
import cn.futures.data.service.impl.FAOSTATServiceImpl;
import cn.futures.data.util.MyHttpClient;


public class ProductionData {
	private FAOSTATService faostatService = new FAOSTATServiceImpl();
	private String url="http://faostat3.fao.org/wds/rest/table/json";
	//http://faostat3.fao.org/download/Q/QC/E
	private MyHttpClient myHttpClient = new MyHttpClient();
	private Log logger = LogFactory.getLog(FAOSTATData.class);
	private DAOUtils dao = new DAOUtils();
	private static final Map<String, String> postParams = new HashMap<String, String>(){
		{
			put("cssFilename", "");
			put("datasource", "faostatdb");
			put("decimalNumbers", "2");
			put("decimalSeparator", ".");
			put("json", "{\"limit\":null,\"query\":\"EXECUTE Warehouse.dbo.usp_GetDataTESTP  @DomainCode = 'QC',   @lang = 'E',   @List1Codes = '(''-1'')',   @List2Codes = '(''2312'',''2413'',''2510'')',   @List3Codes = '(''667'')',   @List4Codes = '(''-1'')',    @List5Codes = '',     @List6Codes = '',     @List7Codes = '',    @NullValues = 0,     @Thousand = '',     @Decimal = '.',     @DecPlaces = 2 ,   @Limit =2000000\",\"frequency\":\"NONE\"}");
			//list1:countries;list2:elements(收获面积、单产等);list3:items(品种);list4:years
			put("thousandSeparator", ",");
			put("valueIndex", "5");
		}
	};
	
	public void start(){
		fetchProductionByContinent();
	}
	
	/**
	 * 生产数据，有收获面积、单产、产量
	 * @DomainCode = 'QC'
	 * 数据库存储方式：按洲划分
	 */
	private void fetchProductionByContinent(){
		logger.info("======开始抓取收获面积、单产、产量等生产数据=====");
		String comp = "'\\(([0-9|,|'|-]*)\\)',";
		Map<String, String> dataMap = new HashMap<String, String>();
		Map<String, String> headerMap = new HashMap<String, String>();
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'QC',"));
		for(String varName:ParamsMap.faostat_production_varnames_map.keySet()){
			int varId = Variety.getVaridByName(varName);
			String engVarName = ParamsMap.faostat_production_varnames_map.get(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(engVarName)+"'')',"));
			for(String continent:ParamsMap.faostat_production_headers_map.keySet()){
				headerMap.clear();
				headerMap = ParamsMap.faostat_production_headers_map.get(continent);
				//countries
				postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
				for(String element:ParamsMap.faostat_production_elements_map.keySet()){
					String cnName = continent + ParamsMap.faostat_production_elements_map.get(element);
					//elements
					postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(element)+"'')',"));
					String contents = myHttpClient.getPostHtmlByHttpClient(url, postParams, "utf-8");
					if(contents != null && !contents.equals("")){
						String[] lines = contents.split("\\],\\[");
						for(String line:lines){
							dataMap.clear();
							String[] fields = line.split("\",\"");
							String value = fields[8];
							if(value.equals(""))
								continue;
							String timeInt = fields[6];
							dataMap.put(headerMap.get(fields[1]), value);
							logger.info("保存"+varName+"  "+fields[1]+"  "+value);
							dao.saveOrUpdateByDataMap(varId, cnName, Integer.parseInt(timeInt), dataMap);
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		new ProductionData().start();
	}
}
