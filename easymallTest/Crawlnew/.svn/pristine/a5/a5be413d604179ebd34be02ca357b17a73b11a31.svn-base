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

/**
 * 茶叶、日本稻米、韩国稻米
 * @author ctm
 *
 */
public class FAOSTATData {
	
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
		fetchProduction();
		fetchTrade();
		fetchProducerPrice();
		getSingleCountryData();
	}
	
	/**
	 * 生产数据，有收获面积、单产、产量
	 * @DomainCode = 'QC'
	 */
	private void fetchProduction(){
		logger.info("======开始抓取收获面积、单产、产量等生产数据=====");
		String comp = "'\\(([0-9|,|'|-]*)\\)',";
		Map<String, String> dataMap = new HashMap<String, String>();
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'QC',"));
		for(String varNameTmp:ParamsMap.faostat_production_varname2headers_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[1])+"'')',"));
			Map<String, String> headerMap = ParamsMap.faostat_production_varname2headers_map.get(varNameTmp);
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_production_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
	}
	
	/**
	 * 贸易数据，有进出口数据
	 * @DomainCode = 'TP'
	 */
	private void fetchTrade(){
		logger.info("======开始抓取进出口等贸易数据=====");
		String comp = "'\\(([0-9|,|'|-]*)\\)',";
		Map<String, String> dataMap = new HashMap<String, String>();
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'TP',"));
		logger.info("*****进口*****");
		for(String varNameTmp:ParamsMap.faostat_trade_import_varname2headers_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			String pre = "";
			if(varName.equals("茶叶副产品")){
				varName = "全球茶叶";
				pre = "茶叶副产品";
			}
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[1])+"'')',"));
			Map<String, String> headerMap = ParamsMap.faostat_trade_import_varname2headers_map.get(varNameTmp);
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_trade_import_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						logger.info("保存"+varName+pre+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, pre+cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		logger.info("*****出口*****");
		for(String varNameTmp:ParamsMap.faostat_trade_export_varname2headers_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			String pre = "";
			if(varName.equals("茶叶副产品")){
				varName = "全球茶叶";
				pre = "茶叶副产品";
			}
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[1])+"'')',"));
			Map<String, String> headerMap = ParamsMap.faostat_trade_export_varname2headers_map.get(varNameTmp);
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_trade_export_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						logger.info("保存"+varName+pre+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, pre+cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
	}
	
	/**
	 * 生产者价格数据
	 * 生产者价格（年）@DomainCode = 'PP'
	 * 生产者价格（月）@DomainCode = 'PM'
	 * 生产者价格指数    @DomainCode = 'PI'
	 * 
	 */
	private void fetchProducerPrice(){
		logger.info("======开始抓取生产者价格数据=====");
		String comp = "'\\(([0-9|,|'|-]*)\\)',";
		Map<String, String> dataMap = new HashMap<String, String>();
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'PP',"));
		//生产者价格（年）
		for(String varNameTmp:ParamsMap.faostat_producer_price_year_varname2headers_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[1])+"'')',"));
			Map<String, String> headerMap = ParamsMap.faostat_producer_price_year_varname2headers_map.get(varNameTmp);
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_producer_price_year_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		//生产者价格（月）（特殊）
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'PM',"));
		for(String varNameTmp:ParamsMap.faostat_producer_price_month_varname2headers_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[1])+"'')',"));
			Map<String, String> headerMap = ParamsMap.faostat_producer_price_month_varname2headers_map.get(varNameTmp);
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_producer_price_month_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
				String contents = myHttpClient.getPostHtmlByHttpClient(url, postParams, "utf-8");
				if(contents != null && !contents.equals("")){
					String[] lines = contents.split("\\],\\["); 
					for(String line:lines){
						dataMap.clear();
						String[] fields = line.split("\",\"");
						String value = fields[8];
						if(value.equals(""))
							continue;
						String timeInt = fields[6]+ParamsMap.producer_price_month_elements_map.get(cnNameTmp);
						dataMap.put(headerMap.get(fields[1]), value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, "生产者价格（月）", Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		
		//生产者价格指数
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'PI',"));
		for(String varNameTmp:ParamsMap.faostat_producer_price_index_varname2headers_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[1])+"'')',"));
			Map<String, String> headerMap = ParamsMap.faostat_producer_price_index_varname2headers_map.get(varNameTmp);
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '"+faostatService.getCountryCodeOfJson(headerMap)+"',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_producer_price_index_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						if(fields[1].startsWith("Lao People")){//老挝特殊
							dataMap.put("老挝", value);
						}else{
							dataMap.put(headerMap.get(fields[1]), value);
						}
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
	}
	
	/**
	 * 单个国家数据抓取，表头均为“全国”
	 */
	public void getSingleCountryData(){
		logger.info("======开始抓取单个国家收获面积、单产、产量等生产数据=====");
		String comp = "'\\(([0-9|,|'|-]*)\\)',";
		Map<String, String> dataMap = new HashMap<String, String>();
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'QC',"));
		for(String varNameTmp:ParamsMap.faostat_production_single_varname2cnnames_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[2])+"'')',"));
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '(''"+ParamsMap.contries_map.get(varNames[1])+"'')',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_production_single_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dataMap.put("全国", value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		logger.info("======开始抓取单个国家进出口等贸易数据=====");
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'TP',"));
		logger.info("*****进口*****");
		for(String varNameTmp:ParamsMap.faostat_trade_import_single_varname2cnnames_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[2])+"'')',"));
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '(''"+ParamsMap.contries_map.get(varNames[1])+"'')',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_trade_import_single_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dataMap.put("全国", value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		logger.info("*****出口*****");
		for(String varNameTmp:ParamsMap.faostat_trade_export_single_varname2cnnames_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[2])+"'')',"));
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '(''"+ParamsMap.contries_map.get(varNames[1])+"'')',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_trade_export_single_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dataMap.put("全国", value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		logger.info("======开始抓取单个国家生产者价格数据=====");
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'PP',"));
		//生产者价格（年）
		for(String varNameTmp:ParamsMap.faostat_producer_price_year_single_varname2cnnames_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[2])+"'')',"));
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '(''"+ParamsMap.contries_map.get(varNames[1])+"'')',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_producer_price_year_single_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dataMap.put("全国", value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		//生产者价格（月）（特殊）
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'PM',"));
		for(String varNameTmp:ParamsMap.faostat_producer_price_month_single_varname2cnnames_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[2])+"'')',"));
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '(''"+ParamsMap.contries_map.get(varNames[1])+"'')',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_producer_price_month_single_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
				String contents = myHttpClient.getPostHtmlByHttpClient(url, postParams, "utf-8");
				if(contents != null && !contents.equals("")){
					String[] lines = contents.split("\\],\\[");
					for(String line:lines){
						dataMap.clear();
						String[] fields = line.split("\",\"");
						String value = fields[8];
						if(value.equals(""))
							continue;
						String timeInt = fields[6]+ParamsMap.producer_price_month_elements_map.get(cnNameTmp);
						dataMap.put("全国", value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, "生产者价格（月）", Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
		
		//生产者价格指数
		postParams.put("json", postParams.get("json").replaceAll("@DomainCode = '([^']+)',", "@DomainCode = 'PI',"));
		for(String varNameTmp:ParamsMap.faostat_producer_price_index_single_varname2cnnames_map.keySet()){
			String[] varNames = varNameTmp.split("	");
			String varName = varNames[0];
			int varId = Variety.getVaridByName(varName);
			//items
			postParams.put("json", postParams.get("json").replaceAll("@List3Codes = "+comp, "@List3Codes = '(''"+ParamsMap.items_map.get(varNames[2])+"'')',"));
			//countries
			postParams.put("json", postParams.get("json").replaceAll("@List1Codes = "+comp, "@List1Codes = '(''"+ParamsMap.contries_map.get(varNames[1])+"'')',"));
			//elements
			Map<String, String> cnNameMap = ParamsMap.faostat_producer_price_index_single_varname2cnnames_map.get(varNameTmp);
			for(String cnNameTmp:cnNameMap.keySet()){
				postParams.put("json", postParams.get("json").replaceAll("@List2Codes = "+comp, "@List2Codes = '(''"+ParamsMap.elements_map.get(cnNameTmp)+"'')',"));
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
						dataMap.put("全国", value);
						logger.info("保存"+varName+"  "+fields[1]+"  "+value);
						dao.saveOrUpdateByDataMap(varId, cnNameMap.get(cnNameTmp), Integer.parseInt(timeInt), dataMap);
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		new FAOSTATData().getSingleCountryData();
	}
}
