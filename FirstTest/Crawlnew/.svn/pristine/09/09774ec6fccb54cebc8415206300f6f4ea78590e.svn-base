package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.File;
import cn.futures.data.importor.crawler.weatherCrawler.WeatherParamsMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.Variety;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.FileStrIO;

/**
 * usda 天气数据
 * 需要手动转换pdf为xls格式，技术读取xls中的天气数据
 * @author ctm
 *
 */
public class USDAWeatherFetch {
	
	private DataFetchUtil fetchUtil = new DataFetchUtil();
	private Log logger = LogFactory.getLog(USDAWeatherFetch.class);
	private DAOUtils dao = new DAOUtils();
	private static String path = "E:\\data\\weatherdata";
	
	public void cycFile(File file){
		if(file.listFiles() == null){
			logger.info("没有搜索到E:\\data\\weatherdata文件夹下的文件");
		}else{
			boolean haveXlsxFile = false;
			logger.info("=====start fetch usda weather data=====");
			for(File file2:file.listFiles()){			
				if (file2.isDirectory()) {
					cycFile(file2);
				}else {
					String xlsName = file2.getName().split("\\.")[0];
					if(xlsName.length() != 25) {
						haveXlsxFile = true;
						logger.info(xlsName+".xlsx的文件命名不符合条件！");
						continue;
					}
					String timeInt = DateTimeUtil.formatDate(DateTimeUtil.parseDateTime(xlsName.substring(15,25), "MM-dd-yyyy"),"yyyyMMdd");
					if(file2.getName().split("\\.")[1].equals("xlsx")){
						haveXlsxFile = true;
						startFetch(file2.toString(), timeInt);
					}
				}
			}
			if(!haveXlsxFile){
				logger.info("没有搜索到E:\\data\\weatherdata文件夹下.xlsx的文件");
			}
		}
	}
	
	public void startFetch(String filePath, String timeInt){
		String[][] contents = FileStrIO.readXls(filePath, null);
		if(contents != null){
			List<Integer> rowsOfData = new ArrayList<Integer>();
			for(int row=0;row<contents.length;row++){
				if(contents[row][1]!=null && 
						(contents[row][1].trim().equals("AND STATIONS")||contents[row][1].trim().equals("STATIONS")||contents[row][1].trim().equals("STATES AND STATIONS"))){
					rowsOfData.add(row);
				}
				if(contents[row][0]!=null ){
					if(contents[row][0].trim().equals("STATES AND STATIONS")){
						rowsOfData.add(row+2);
					}else if(contents[row][0].trim().equals("AND STATIONS")){
						rowsOfData.add(row);
					}
				}
			}
			String contentSave = "";
			if(rowsOfData.size() != 3){
				logger.info(filePath+"内容有错，没有查到三次[AND STATIONS]");
			}else{
				for(Integer rowOfData:rowsOfData){
					for(int row=rowOfData+1;row<contents.length;row++){
						if(contents[row][1]==null) break;
						for(int col=0;col<30;col++){
							if(contents[row][col] == null) break;
							contentSave += contents[row][col]+",";
						}
						contentSave += "\n";
					}
				}
				//System.out.print(contentSave);
//				parseStr(contentSave, timeInt);
				parseStr1(contentSave, timeInt);
			}
		}
		logger.info("==========="+filePath+"天气数据已完成插入=========");
	}
	private void parseStr1(String contentSave, String timeInt){
		logger.info("解析"+timeInt+"美农数据");
		String[] lines = contentSave.split("\n");
		int varId = 0;
		String stateCode = "";
		String state = "";
		String city = "";
		Map<String, String> dataMap = new HashMap<String, String>();
		if(!fetchUtil.isNumeric(lines[0].split(",")[1])){
			for(String line:lines){
				dataMap.clear();
				String[] fields = line.split(",");
				if(fields.length < 2) continue;
				if(!fields[0].equals("")){
					stateCode = fields[0];
					if(stateCode.equals("Ml") || stateCode.equals("Hl") || stateCode.equals("Rl") || stateCode.equals("Wl")){
						stateCode = stateCode.replaceAll("l", "I");
					}else if(stateCode.equals("VW")){
						stateCode = "WV";
					}
					state = WeatherParamsMap.code2stateMap.get(stateCode);
					if(state == null)
						continue;
					varId = Variety.getVaridByName(state);
					if(varId==-1)
						continue;
				}
				String cityTmp = fields[1].trim().replaceAll("巳", "B").replaceAll("[^A-Z]","" );
				city = WeatherParamsMap.city_map.get(stateCode+"-"+cityTmp);
				if(city == null){
					logger.info(timeInt+"-"+stateCode+"-"+cityTmp+"未识别");
					continue;
				}
				List<String> fieldList = new ArrayList<String>();
				if(fields.length<21){
					for(String field:fields){
						fieldList.add(field);
					}
					for(int i=fields.length;i<21;i++){
						fieldList.add("0");
					}
				}else{
					fieldList = Arrays.asList(fields);
				}
				if(fetchUtil.isNumeric(fieldList.get(2)) && !fieldList.get(2).equals("-")){
					dataMap.put("平均最高气温", fieldList.get(2));
				}
				if(fetchUtil.isNumeric(fieldList.get(3)) && !fieldList.get(3).equals("-")){
					dataMap.put("平均最低气温", fieldList.get(3));
				}
				if(fetchUtil.isNumeric(fieldList.get(4)) && !fieldList.get(4).equals("-")){
					dataMap.put("历史最高气温", fieldList.get(4));
				}
				if(fetchUtil.isNumeric(fieldList.get(5)) && !fieldList.get(5).equals("-")){
					dataMap.put("历史最低气温", fieldList.get(5));
				}
				if(fetchUtil.isNumeric(fieldList.get(6)) && !fieldList.get(6).equals("-")){
					dataMap.put("周平均气温", fieldList.get(6));
				}
				if(fetchUtil.isNumeric(fieldList.get(7)) && !fieldList.get(7).equals("-")){
					dataMap.put("偏离正常气温", fieldList.get(7));
				}
				if(fetchUtil.isNumeric(fieldList.get(8)) && !fieldList.get(8).equals("-")){
					dataMap.put("周累计降水量", fieldList.get(8));
				}
				if(fetchUtil.isNumeric(fieldList.get(9)) && !fieldList.get(9).equals("-")){
					dataMap.put("偏离正常降水量", fieldList.get(9));
				}
				if(fetchUtil.isNumeric(fieldList.get(10)) && !fieldList.get(10).equals("-")){
					dataMap.put("24小时最大降雨量", fieldList.get(10));
				}
				if(fetchUtil.isNumeric(fieldList.get(11)) && !fieldList.get(11).equals("-")){
					dataMap.put("当月累计降雨量", fieldList.get(11));
				}
				if(fetchUtil.isNumeric(fieldList.get(12)) && !fieldList.get(12).equals("-")){
					dataMap.put("当月累计降雨比历史正常值", fieldList.get(12));
				}
				if(fetchUtil.isNumeric(fieldList.get(13)) && !fieldList.get(13).equals("-")){
					dataMap.put("当年累计降雨量", fieldList.get(13));
				}
				if(fetchUtil.isNumeric(fieldList.get(14)) && !fieldList.get(14).equals("-")){
					dataMap.put("当年累计降雨比历史正常值", fieldList.get(14));
				}
				if(fetchUtil.isNumeric(fieldList.get(15)) && !fieldList.get(15).equals("-")){
					dataMap.put("相对湿度平均最大值", fieldList.get(15));
				}
				if(fetchUtil.isNumeric(fieldList.get(16)) && !fieldList.get(16).equals("-")){
					dataMap.put("相对湿度平均最小值", fieldList.get(16));
				}
				if(fetchUtil.isNumeric(fieldList.get(17)) && !fieldList.get(17).equals("-")){
					dataMap.put("气温大于等于90度的天数", fieldList.get(17));
				}
				if(fetchUtil.isNumeric(fieldList.get(18)) && !fieldList.get(18).equals("-")){
					dataMap.put("气温小于等于32度的天数", fieldList.get(18));
				}
				if(fetchUtil.isNumeric(fieldList.get(19)) && !fieldList.get(19).equals("-")){
					dataMap.put("降水大于.01英寸的天数", fieldList.get(19));
				}
				if(fetchUtil.isNumeric(fieldList.get(20)) && !fieldList.get(20).equals("-")){
					dataMap.put("降水大于.50英寸的天数", fieldList.get(20));
				}
				if(dataMap.size()>0){
					dao.saveOrUpdateByDataMap(varId, city, Integer.parseInt(timeInt), dataMap);
				}
			}
		}else{
			String stateCodeBefore = "";
			for(String line:lines){
				dataMap.clear();
				String[] fields = line.split(",");
				if(fields.length < 2) continue;
				if(fields[0].trim().indexOf(" ")!=-1){
					stateCode = fields[0].trim().split(" ")[0];
					if(stateCode.equals("Ml") || stateCode.equals("Hl") || stateCode.equals("Rl") || stateCode.equals("Wl")){
						stateCode = stateCode.replaceAll("l", "I");
					}else if(stateCode.equals("VW")){
						stateCode = "WV";
					}
					state = WeatherParamsMap.code2stateMap.get(stateCode);
					if(state == null){
						String cityTmp = fields[0].trim().replaceAll("巳", "B").replaceAll("[^A-Z]","" );
						city = WeatherParamsMap.city_map.get(stateCodeBefore+"-"+cityTmp);
					}else{
						stateCodeBefore = stateCode;
						varId = Variety.getVaridByName(state);
						if(varId==-1)
							continue;
						String cityTmp = fields[0].trim().split(" ")[1].trim().replaceAll("巳", "B").replaceAll("[^A-Z]","" );
						city = WeatherParamsMap.city_map.get(stateCode+"-"+cityTmp);
					}
				}else{
					String cityTmp = fields[0].trim().replaceAll("巳", "B").replaceAll("[^A-Z]","" );
					city = WeatherParamsMap.city_map.get(stateCode+"-"+cityTmp);
				}
				if(city == null){
					//logger.info(timeInt+"-"+stateCode+"-"+cityTmp+"未识别");
					continue;
				}
				List<String> fieldList = new ArrayList<String>();
				if(fields.length<20){
					for(String field:fields){
						fieldList.add(field);
					}
					for(int i=fields.length;i<20;i++){
						fieldList.add("0");
					}
				}else{
					fieldList = Arrays.asList(fields);
				}
				if(fetchUtil.isNumeric(fieldList.get(1)) && !fieldList.get(1).equals("-")){
					dataMap.put("平均最高气温", fieldList.get(1));
				}
				if(fetchUtil.isNumeric(fieldList.get(2)) && !fieldList.get(2).equals("-")){
					dataMap.put("平均最低气温", fieldList.get(2));
				}
				if(fetchUtil.isNumeric(fieldList.get(3)) && !fieldList.get(3).equals("-")){
					dataMap.put("历史最高气温", fieldList.get(3));
				}
				if(fetchUtil.isNumeric(fieldList.get(4)) && !fieldList.get(4).equals("-")){
					dataMap.put("历史最低气温", fieldList.get(4));
				}
				if(fetchUtil.isNumeric(fieldList.get(5)) && !fieldList.get(5).equals("-")){
					dataMap.put("周平均气温", fieldList.get(5));
				}
				if(fetchUtil.isNumeric(fieldList.get(6)) && !fieldList.get(6).equals("-")){
					dataMap.put("偏离正常气温", fieldList.get(6));
				}
				if(fetchUtil.isNumeric(fieldList.get(7)) && !fieldList.get(7).equals("-")){
					dataMap.put("周累计降水量", fieldList.get(7));
				}
				if(fetchUtil.isNumeric(fieldList.get(8)) && !fieldList.get(8).equals("-")){
					dataMap.put("偏离正常降水量", fieldList.get(8));
				}
				if(fetchUtil.isNumeric(fieldList.get(9)) && !fieldList.get(9).equals("-")){
					dataMap.put("24小时最大降雨量", fieldList.get(9));
				}
				if(fetchUtil.isNumeric(fieldList.get(10)) && !fieldList.get(10).equals("-")){
					dataMap.put("当月累计降雨量", fieldList.get(10));
				}
				if(fetchUtil.isNumeric(fieldList.get(11)) && !fieldList.get(11).equals("-")){
					dataMap.put("当月累计降雨比历史正常值", fieldList.get(11));
				}
				if(fetchUtil.isNumeric(fieldList.get(12)) && !fieldList.get(12).equals("-")){
					dataMap.put("当年累计降雨量", fieldList.get(12));
				}
				if(fetchUtil.isNumeric(fieldList.get(13)) && !fieldList.get(13).equals("-")){
					dataMap.put("当年累计降雨比历史正常值", fieldList.get(13));
				}
				if(fetchUtil.isNumeric(fieldList.get(14)) && !fieldList.get(14).equals("-")){
					dataMap.put("相对湿度平均最大值", fieldList.get(14));
				}
				if(fetchUtil.isNumeric(fieldList.get(15)) && !fieldList.get(15).equals("-")){
					dataMap.put("相对湿度平均最小值", fieldList.get(15));
				}
				if(fetchUtil.isNumeric(fieldList.get(16)) && !fieldList.get(16).equals("-")){
					dataMap.put("气温大于等于90度的天数", fieldList.get(16));
				}
				if(fetchUtil.isNumeric(fieldList.get(17)) && !fieldList.get(17).equals("-")){
					dataMap.put("气温小于等于32度的天数", fieldList.get(17));
				}
				if(fetchUtil.isNumeric(fieldList.get(18)) && !fieldList.get(18).equals("-")){
					dataMap.put("降水大于.01英寸的天数", fieldList.get(18));
				}
				if(fetchUtil.isNumeric(fieldList.get(19)) && !fieldList.get(19).equals("-")){
					dataMap.put("降水大于.50英寸的天数", fieldList.get(19));
				}
				if(dataMap.size()>0){
					dao.saveOrUpdateByDataMap(varId, city, Integer.parseInt(timeInt), dataMap);
				}
			}
		}
	}
	private void parseStr(String contentSave, String timeInt){
		logger.info("解析"+timeInt+"美农数据");
		String[] lines = contentSave.split("\n");
		int varId = 0;
		String stateCode = "";
		String state = "";
		String city = "";
		Map<String, String> dataMap = new HashMap<String, String>();
		for(String line:lines){
			dataMap.clear();
			String[] fields = line.split(",");
			if(fields.length == 0) continue;
			if(!fields[0].equals("")){
				stateCode = fields[0];
				if(stateCode.equals("Ml") || stateCode.equals("Hl") || stateCode.equals("Rl") || stateCode.equals("Wl")){
					stateCode = stateCode.replaceAll("l", "I");
				}else if(stateCode.equals("VW")){
					stateCode = "WV";
				}
				state = WeatherParamsMap.code2stateMap.get(stateCode);
				if(state == null)
					continue;
				varId = Variety.getVaridByName(state);
				if(varId==-1)
					continue;
			}
			String cityTmp = fields[1].trim().replaceAll("巳", "B").replaceAll("[^A-Z]","" );
			city = WeatherParamsMap.city_map.get(stateCode+"-"+cityTmp);
			if(city == null){
				logger.info(timeInt+"-"+stateCode+"-"+cityTmp+"未识别");
				continue;
			}
			List<String> fieldList = new ArrayList<String>();
			if(fields.length<21){
				for(String field:fields){
					fieldList.add(field);
				}
				for(int i=fields.length;i<21;i++){
					fieldList.add("0");
				}
			}else{
				fieldList = Arrays.asList(fields);
			}
			if(fetchUtil.isNumeric(fieldList.get(2)) && !fieldList.get(2).equals("-")){
				dataMap.put("平均最高气温", fieldList.get(2));
			}
			if(fetchUtil.isNumeric(fieldList.get(3)) && !fieldList.get(3).equals("-")){
				dataMap.put("平均最低气温", fieldList.get(3));
			}
			if(fetchUtil.isNumeric(fieldList.get(4)) && !fieldList.get(4).equals("-")){
				dataMap.put("历史最高气温", fieldList.get(4));
			}
			if(fetchUtil.isNumeric(fieldList.get(5)) && !fieldList.get(5).equals("-")){
				dataMap.put("历史最低气温", fieldList.get(5));
			}
			if(fetchUtil.isNumeric(fieldList.get(6)) && !fieldList.get(6).equals("-")){
				dataMap.put("周平均气温", fieldList.get(6));
			}
			if(fetchUtil.isNumeric(fieldList.get(7)) && !fieldList.get(7).equals("-")){
				dataMap.put("偏离正常气温", fieldList.get(7));
			}
			if(fetchUtil.isNumeric(fieldList.get(8)) && !fieldList.get(8).equals("-")){
				dataMap.put("周累计降水量", fieldList.get(8));
			}
			if(fetchUtil.isNumeric(fieldList.get(9)) && !fieldList.get(9).equals("-")){
				dataMap.put("偏离正常降水量", fieldList.get(9));
			}
			if(fetchUtil.isNumeric(fieldList.get(10)) && !fieldList.get(10).equals("-")){
				dataMap.put("24小时最大降雨量", fieldList.get(10));
			}
			if(fetchUtil.isNumeric(fieldList.get(11)) && !fieldList.get(11).equals("-")){
				dataMap.put("当月累计降雨量", fieldList.get(11));
			}
			if(fetchUtil.isNumeric(fieldList.get(12)) && !fieldList.get(12).equals("-")){
				dataMap.put("当月累计降雨比历史正常值", fieldList.get(12));
			}
			if(fetchUtil.isNumeric(fieldList.get(13)) && !fieldList.get(13).equals("-")){
				dataMap.put("当年累计降雨量", fieldList.get(13));
			}
			if(fetchUtil.isNumeric(fieldList.get(14)) && !fieldList.get(14).equals("-")){
				dataMap.put("当年累计降雨比历史正常值", fieldList.get(14));
			}
			if(fetchUtil.isNumeric(fieldList.get(15)) && !fieldList.get(15).equals("-")){
				dataMap.put("相对湿度平均最大值", fieldList.get(15));
			}
			if(fetchUtil.isNumeric(fieldList.get(16)) && !fieldList.get(16).equals("-")){
				dataMap.put("相对湿度平均最小值", fieldList.get(16));
			}
			if(fetchUtil.isNumeric(fieldList.get(17)) && !fieldList.get(17).equals("-")){
				dataMap.put("气温大于等于90度的天数", fieldList.get(17));
			}
			if(fetchUtil.isNumeric(fieldList.get(18)) && !fieldList.get(18).equals("-")){
				dataMap.put("气温小于等于32度的天数", fieldList.get(18));
			}
			if(fetchUtil.isNumeric(fieldList.get(19)) && !fieldList.get(19).equals("-")){
				dataMap.put("降水大于.01英寸的天数", fieldList.get(19));
			}
			if(fetchUtil.isNumeric(fieldList.get(20)) && !fieldList.get(20).equals("-")){
				dataMap.put("降水大于.50英寸的天数", fieldList.get(20));
			}
			if(dataMap.size()>0){
				dao.saveOrUpdateByDataMap(varId, city, Integer.parseInt(timeInt), dataMap);
			}
		}
	}
	
	public static void main(String[] args){
//		Map<String, String> cityMap = MapInit.usda_weather_prov2city_map.get("AL");
		new USDAWeatherFetch().cycFile(new File(path));
		/*for(String key:ParamsMap.state_code2cnNameMap.keySet()){
			String[] fields = key.split("-");
			System.out.println("put(\""+fields[0]+"\",\""+fields[1]+"\");");
		}*/
	}
}
