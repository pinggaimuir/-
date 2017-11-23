package cn.futures.data.importor.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RegexUtil;

/**
 * 油脂油料——分国别 Fats and Oils: Oilseed Crushings, Production, Consumption and Stocks
 * @author bric_yangyulin
 * @date 2017-02-09
 * */
public class FatsAndOilsCountries {

	//数据源：http://usda.mannlib.cornell.edu/MannUsda/viewDocumentInfo.do?documentID=1902
	private static final String listUrl = "http://usda.mannlib.cornell.edu/MannUsda/viewDocumentInfo.do?documentID=1902";//列表页（其实只有最新发布的）
	private String className = FatsAndOilsCountries.class.getName();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(FatsAndOilsCountries.class);
	private Map<String, String[]> soybeanUS = new HashMap<String, String[]>();//存储表格Soybean Crushing, Production, Consumption and Stocks - United States
	private Map<String, String[]> soybeanRegional = new HashMap<String, String[]>();//存储表格Soybean Crushing - Regional
	private Map<String, String[]> selectedOilseed = new HashMap<String, String[]>();//存储表格Selected Oilseed Crushing, Production, Consumption and Stocks - United States
	private Map<String, String[]> animal = new HashMap<String, String[]>();//存储表格Animal Fats and Oils Production, Consumption and Stocks - United States
	
	private static List<String[][]> tableColumns = new LinkedList<String[][]>();//存储表及其列名，n行两列的一个二维数据对应一张表，第一行存品种名和表名，之后存列名和对应标记字符串
	
	static {
		//爬虫文档《爬虫文档—油脂油料—美国大豆+美国豆油+美国葵花籽油+美国酸化植物油脚+美国椰子油.docx》
		tableColumns.add(new String[][] {{"美国大豆", "大豆压榨量",null},
			{"全国", "Crush_Soybeans crushedtons", "soybeanUS"}, 
			{"Illinois ", "Soybeans crushed_Illinois", "soybeanRegional"}, 
			{"Iowa        ", "Soybeans crushed_Iowa", "soybeanRegional"}, 
			{"NorthandEast    ", "Soybeans crushed_North and East 1/ ", "soybeanRegional"}, 
			{"NorthCentral ", "Soybeans crushed_North Central 2/ ", "soybeanRegional"}, 
			{"SouthWestandPacific ", "Soybeans crushed_South, West and Pacific 3/ ", "soybeanRegional"}, 
			{"WestCentral", "Soybeans crushed_West Central 4/ ", "soybeanRegional"}
		});
		tableColumns.add(new String[][] {{"美国豆油", "豆油消费量", null}, 
			{"原油非食用加工量", "Consumption_Crude oil removed for inedible use in processing1,000 pounds", "soybeanUS"}, 
			{"精炼油加工量", "Consumption_Once refined oil removed for use in processing1,000 pounds", "soybeanUS"}, 
			{"精炼油食用加工量", "Consumption_  Removed for edible use in processing1,000 pounds", "soybeanUS"}, 
			{"精炼油非食用加工量", "Consumption_  Removed for inedible use in processing1,000 pounds", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国豆油", "精炼油月末库存", null}, 
			{"全国", "Stocks_Once refined oil on hand end of month1,000 pounds", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国豆油", "压榨厂原油月末库存", null}, 
			{"全国", "Stocks_  Crusher1,000 pounds", "soybeanUS"},
			{"Illinois ", "Crude oil stocks at crusher_Illinois", "soybeanRegional"},
			{"Iowa        ", "Crude oil stocks at crusher_Iowa", "soybeanRegional"},
			{"NorthandEast    ", "Crude oil stocks at crusher_North and East 1/ ", "soybeanRegional"},
			{"NorthCentral ", "Crude oil stocks at crusher_North Central 2/ ", "soybeanRegional"},
			{"SouthWestandPacific ", "Crude oil stocks at crusher_South, West and Pacific 3/ ", "soybeanRegional"},
			{"WestCentral", "Crude oil stocks at crusher_West Central 4/ ", "soybeanRegional"}
		});
		tableColumns.add(new String[][] {{"美国豆油", "原油产量", null}, 
			{"全国", "Crush_Crude oil produced1,000 pounds", "soybeanUS"},
			{"Illinois ", "Crude oil produced_Illinois", "soybeanRegional"},
			{"Iowa        ", "Crude oil produced_Iowa", "soybeanRegional"},
			{"NorthandEast    ", "Crude oil produced_North and East 1/ ", "soybeanRegional"},
			{"NorthCentral ", "Crude oil produced_North Central 2/ ", "soybeanRegional"},
			{"SouthWestandPacific ", "Crude oil produced_South, West and Pacific 3/ ", "soybeanRegional"},
			{"WestCentral", "Crude oil produced_West Central 4/ ", "soybeanRegional"}
		});
		tableColumns.add(new String[][] {{"美国豆油", "原油精炼", null}, 
			{"原油精炼量", "Refine_Crude oil processed in refining1,000 pounds", "soybeanUS"},
			{"精炼油产量", "Refine_Once refined oil produced1,000 pounds", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国豆油", "原油月末库存", null}, 
			{"总计", "Stocks_Crude oil on hand end of month1,000 pounds", "soybeanUS"}, 
			{"压榨厂", "Stocks_  Crusher1,000 pounds", "soybeanUS"}, 
			{"精炼厂", "Stocks_  Refiner1,000 pounds", "soybeanUS"}, 
			{"厂外", "Stocks_  Offsite1,000 pounds", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国葵花籽油", "产量", null}, 
			{"原油精炼量", "Sunflower_Refine_  Crude oil processed in refining1,000 pounds", "selectedOilseed"},
			{"精炼油产量", "Sunflower_Refine_  Once refined oil produced1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国葵花籽油", "消费量", null}, 
			{"精炼油加工量", "Sunflower_Consumption_  Once refined oil removed for use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国葵花籽油", "月末库存", null}, 
			{"原油", "Sunflower_Stocks_  Crude oil on hand end of month1,000 pounds", "selectedOilseed"}, 
			{"精炼油", "Sunflower_Stocks_  Once refined oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国酸化植物油脚", "精炼产量", null}, 
			{"精炼油产量", "Vegetable foots, raw and acidulated_Refine_  Oil produced1,000 pounds", "selectedOilseed"}//近几个月都无数据
		});
		tableColumns.add(new String[][] {{"美国酸化植物油脚", "消费量", null}, 
			{"精炼油加工量", "Vegetable foots, raw and acidulated_Consumption_  Oil removed for inedible use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国酸化植物油脚", "月末库存", null}, 
			{"精炼油", "Vegetable foots, raw and acidulated_Stocks_  Oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国椰子油", "产量", null}, 
			{"原油精炼量", "Coconut_Refine_  Crude oil processed in refining1,000 pounds", "selectedOilseed"}, 
			{"精炼油产量", "Coconut_Refine_  Once refined oil produced1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国椰子油", "消费量", null}, 
			{"原油非食用加工量", "Coconut_Consumption_  Crude oil removed for inedible use in processing1,000 pounds", "selectedOilseed"}, 
			{"精炼油加工量", "Coconut_Consumption_  Once refined oil removed for use in processing1,000 pounds", "selectedOilseed"}, 
			{"精炼油食用加工量", "Coconut_Consumption_    Removed for edible use in processing1,000 pounds", "selectedOilseed"}, 
			{"精炼油非食用加工量", "Coconut_Consumption_    Removed for inedible use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国椰子油", "月末库存", null}, 
			{"原油", "Coconut_Stocks_  Crude oil on hand end of month1,000 pounds", "selectedOilseed"}, 
			{"精炼油", "Coconut_Stocks_  Once refined oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
		//数据需求文档—饲料养殖—美国菜粕+动物蛋白饲料+豆粕+棉粕—陈郁
		tableColumns.add(new String[][] {{"美国菜粕", "产量", null},
			{"全国", "Canola_Crush_  Cake and meal producedtons", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国菜粕", "库存", null},
			{"全国", "Canola_Stocks_  Cake and meal on hand end of monthtons", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国动物蛋白饲料", "鸡肉骨粉产消库存", null},//尚无
			{"产量", "Poultry by-product meals_  Production", "animal"},
			{"月末库存", "Poultry by-product meals_  Stocks on hand end of month", "animal"}
		});
		tableColumns.add(new String[][] {{"美国动物蛋白饲料", "肉骨粉产消库存", null},//尚无
			{"产量", "Meat meal, meat and bone meal, and dry rendered tankage_  Production", "animal"},
			{"月末库存", "Meat meal, meat and bone meal, and dry rendered tankage_  Stocks on hand end of month", "animal"}
		});
		tableColumns.add(new String[][] {{"美国动物蛋白饲料", "羽毛粉产消库存", null},//尚无
			{"产量", "Feather meal_  Production", "animal"},
			{"月末库存", "Feather meal_  Stocks on hand end of month", "animal"}
		});
		tableColumns.add(new String[][] {{"美国豆粕", "大豆磨制饲料产量", null},
			{"全国", "Crush_Millfeed producedtons", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国豆粕", "大豆磨制饲料月末库存", null},
			{"全国", "Stocks_Millfeed on hand end of monthtons", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国豆粕", "豆饼粕产量（分区域）", null},
			{"全国", "Crush_Cake and meal producedtons", "soybeanUS"},//to check ...
			{"Illinois ", "Cake and meal produced_Illinois", "soybeanRegional"}, 
			{"Iowa        ", "Cake and meal produced_Iowa", "soybeanRegional"}, 
			{"NorthandEast    ", "Cake and meal produced_North and East 1/ ", "soybeanRegional"}, 
			{"NorthCentral ", "Cake and meal produced_North Central 2/ ", "soybeanRegional"}, 
			{"SouthWestandPacific ", "Cake and meal produced_South, West and Pacific 3/ ", "soybeanRegional"}, 
			{"WestCentral", "Cake and meal produced_West Central 4/ ", "soybeanRegional"}
		});
		tableColumns.add(new String[][] {{"美国豆粕", "豆饼粕产量（分用途）", null},
			{"总计", "Crush_Cake and meal producedtons", "soybeanUS"}, 
			{"动物饲料", "Crush_  For animal feedtons", "soybeanUS"}, 
			{"食用蛋白产量", "Crush_  For edible protein productstons", "soybeanUS"}
		});
		tableColumns.add(new String[][] {{"美国豆粕", "豆饼粕库存", null},
			{"全国", "Stocks_Cake and meal on hand end of monthtons", "soybeanUS"},//to check ...
			{"Illinois ", "Cake and meal stocks_Illinois", "soybeanRegional"},
			{"Iowa        ", "Cake and meal stocks_Iowa", "soybeanRegional"},
			{"NorthandEast    ", "Cake and meal stocks_North and East 1/ ", "soybeanRegional"},
			{"NorthCentral ", "Cake and meal stocks_North Central 2/ ", "soybeanRegional"},
			{"SouthWestandPacific ", "Cake and meal stocks_South, West and Pacific 3/ ", "soybeanRegional"},
			{"WestCentral", "Cake and meal stocks_West Central 4/ ", "soybeanRegional"}
		});
		tableColumns.add(new String[][] {{"美国棉粕", "产量", null},
			{"全国", "Cottonseed_Crush_  Cake and meal producedtons", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棉粕", "库存", null},
			{"全国", "Cottonseed_Stocks_  Cake and meal on hand end of monthtons", "selectedOilseed"}
		});
		//《爬虫数据—油脂油料—美国菜籽油、棕榈油、棉油_20170111_黄斌强.doc》
		tableColumns.add(new String[][] {{"美国棉油", "棉籽入榨量", null},
			{"全国", "Cottonseed_Crush_  Seeds crushedtons", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棉油", "月末库存", null},
			{"精炼油", "Cottonseed_Stocks_  Once refined oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棉油", "消费量", null},
			{"精炼油加工量", "Cottonseed_Consumption_  Once refined oil removed for use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棉油", "棉油产量", null},
			{"原油产量", "Cottonseed_Crush_  Crude oil produced1,000 pounds", "selectedOilseed"},
			{"原油精炼量", "Cottonseed_Refine_  Crude oil processed in refining1,000 pounds", "selectedOilseed"},
			{"精炼油产量", "Cottonseed_Refine_  Once refined oil produced1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国菜籽油", "菜籽油产量", null},
			{"原油产量", "Canola_Crush_  Crude oil produced1,000 pounds", "selectedOilseed"},
			{"原油精炼量", "Canola_Refine_  Crude oil processed in refining1,000 pounds", "selectedOilseed"},
			{"精炼油产量", "Canola_Refine_  Once refined oil produced1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国菜籽油", "菜籽入榨量", null},
			{"全国", "Canola_Crush_  Seeds crushedtons", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国菜籽油", "消费量", null},
			{"精炼油加工量", "Canola_Consumption_  Once refined oil removed for use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国菜籽油", "月末库存", null},
			{"精炼油", "Canola_Stocks_  Once refined oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棕榈油", "棕榈油消费量", null},
			{"精炼油加工量", "Palm_Consumption_  Once refined oil removed for use in processing1,000 pounds", "selectedOilseed"},
			{"精炼油食用加工量", "Palm_Consumption_    Removed for edible use in processing1,000 pounds", "selectedOilseed"},
			{"精炼油非食用加工量", "Palm_Consumption_    Removed for inedible use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棕榈油", "棕榈仁油消费量", null},
			{"精炼油加工量", "Palm kernel_Consumption_  Once refined oil removed for use in processing1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棕榈油", "精炼棕榈油月末库存", null},
			{"精炼油", "Palm_Stocks_  Once refined oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
		tableColumns.add(new String[][] {{"美国棕榈油", "精炼棕榈仁油月末库存", null},
			{"精炼油", "Palm kernel_Stocks_  Once refined oil on hand end of month1,000 pounds", "selectedOilseed"}
		});
	}
	
	@Scheduled(cron = CrawlScheduler.CRON_FATSANDOILSCOUNTRIES)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("油脂油料分国别数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到油脂油料分国别数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				fetchHref();
			}else{
				LOG.info("抓取油脂油料分国别数据的定时器已关闭");
			}
		}
	}
	
	/**
	 * 抓取最近数据文件超链接并解析入库
	 * */
	private void fetchHref(){
		String hrefCont = dataFetchUtil.getCompleteContent(0, listUrl, Constants.ENCODE_UTF8, "油脂油料-分国别");
		String hrefRegex = "<a href=\"(http://usda.mannlib.cornell.edu/usda/current/FatsOils/FatsOils-(\\d{2})-(\\d{2})-(\\d{4}).txt)\"><span class=\"text-uppercase\">txt</span></a>";
		List<String> hrefList = RegexUtil.getMatchStr(hrefCont, hrefRegex, new int[]{1, 2, 3, 4});//
		if(hrefList.size() == 4){
			String href = hrefList.get(0);//最近数据的超链接
			fetchData(href);
			parseAndSave();
		} else {
			LOG.info("未找到最新数据的链接。");
		}
	}
	
	/**
	 * 抓取指定数据文件
	 * */
	private void fetchData(String href){
		String fileCont = dataFetchUtil.getCompleteContent(0, href, Constants.ENCODE_UTF8, "油脂油料-分国别");
		parseTable(fileCont);
	}
	
	private void parseTable(String fileCont){
		fileCont = fileCont.replaceAll("\r?\n\r?\n", "\n");
		String[] lines = fileCont.split("\r?\n");
		Map<String, String[]> tableMap = null;
		String prefix = null;//前缀（由类别组成，作为前缀以区分表格中所有内容）
		String prefix2 = null;//前缀第二部分（个别表格使用）
		for(int lineId = 0; lineId < lines.length; lineId++){
			if(lines[lineId].isEmpty()){
				continue;
			}
			if(lines[lineId].startsWith("Soybean Crushing, Production, Consumption and Stocks - United States:")){
				tableMap = soybeanUS;
				//获取时间
				lineId += 2;
				String[] timeArray = lines[lineId].split(":");
				tableMap.put("Time", timeArray);
				lineId++;
			} else if(lines[lineId].startsWith("Soybean Crushing - Regional:")){
				tableMap = soybeanRegional;
				//获取时间
				String[] timeArray = lines[lineId+4].split(":");
				tableMap.put("Time", timeArray);
//				lineId++;
			} else if(lines[lineId].startsWith("Selected Oilseed Crushing, Production, Consumption and Stocks - United States:")){
				tableMap = selectedOilseed;
				//获取时间
				lineId += 2;
				if(!lines[lineId].contains("Item")){
					lineId++;
				}
				String[] timeArray = lines[lineId].split(":");
				tableMap.put("Time", timeArray);
				lineId++;
			} else if(lines[lineId].startsWith("Animal Fats and Oils Production, Consumption and Stocks - United States:")) {
				tableMap = animal;
				//获取时间
				lineId += 2;
				String[] timeArray = lines[lineId].split(":");
				tableMap.put("Time", timeArray);
				lineId++;
			} else {//处理表格内容
				if(tableMap == null){//非表格内容，跳过
					continue;
				} else if(tableMap == soybeanUS) {
					if(lines[lineId].matches("-+")){//该表格结束标志
						tableMap = null;
					} else {//处理内容
						if(lines[lineId].matches("\\s+:\\s+") || lines[lineId].contains("Item")){
							continue;
						} else if(lines[lineId].matches("[a-zA-Z\\s,]+:\\s+")){
							//提取前缀
							prefix = lines[lineId].replaceAll("\\s*:\\s+", "");
						} else {
							//提取数据
							String[] tempArray = lines[lineId].split(":");
							String[] dataArray = tempArray[1].split("\\s+");
							tableMap.put(prefix + "_" + tempArray[0].replace(".", ""), dataArray);
						}
					}
				} else if(tableMap == soybeanRegional) {
					if(!lines[lineId].matches("-+") && !lines[lineId].contains(":")){//该表格结束标志
						tableMap = null;
					} else {//处理内容
						if(lines[lineId].matches("-+")){
							continue;
						} else if(lines[lineId].contains("Region")){
							prefix = lines[lineId-2].replace(":", "").trim();//产品名称行，作前缀
						} else {
							String[] tempArray = lines[lineId].split(":");
							if(tempArray[0].matches("\\s*")){//非内容行（可能为单位行，也可能能为产品名称行）
								continue;
							} else {//内容行
								String[] dataArray = tempArray[1].split("\\s+");
								tableMap.put(prefix + "_" + tempArray[0].replace(".", ""), dataArray);
							}
						}
					}
				} else if(tableMap == selectedOilseed) {
					if(lines[lineId].matches("-+")){//该表格结束标志
						tableMap = null;
					} else {//处理内容
						if(lines[lineId].matches(" +: +") || lines[lineId].contains("Item")){
							continue;
						} else if(lines[lineId].matches("[a-zA-Z\\s,]+:\\s+")){
							//提取前缀(注意此表格的前缀由两部分组成)
							if(lines[lineId+1].matches("[a-zA-Z\\s,]+:\\s+")){
								prefix = lines[lineId].replaceAll("\\s*:\\s+", "");
								prefix2 = lines[++lineId].replaceAll("\\s*:\\s+", "");
							} else {
								prefix2 = lines[lineId].replaceAll("\\s*:\\s+", "");
							}
						} else {
							//提取数据
							String[] tempArray = lines[lineId].split(":");
							String[] dataArray = tempArray[1].split("\\s+");
							tableMap.put(prefix + "_" + prefix2 + "_" + tempArray[0].replace(".", ""), dataArray);
						}
					}
				} else if(tableMap == animal) {
					if(lines[lineId].matches("-+")){//该表格结束标志
						tableMap = null;
					} else {//处理内容
						if(lines[lineId].matches("\\s+:\\s+") || lines[lineId].contains("Item")){
							continue;
						} else if(lines[lineId].matches("[-a-zA-Z\\s,]+:\\s+")){
							//提取前缀
							prefix = lines[lineId].replaceAll("\\s*:\\s+", "");
						} else {
							//提取数据
							String[] tempArray = lines[lineId].split(":");
							if(tempArray[0].matches("\\s*")){//非内容行（可能为单位行，也可能能为产品名称行）
								continue;
							} else {//内容行
								String[] dataArray = tempArray[1].split("\\s+");
								tableMap.put(prefix + "_" + tempArray[0].replace(".", ""), dataArray);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 从表中取出数据并入库
	 * */
	private void parseAndSave(){
		for(String[][] columns: tableColumns){
			String varName = columns[0][0];
			String cnName = columns[0][1];
			SimpleDateFormat format = new SimpleDateFormat("MMMMM yyyy", new Locale(Locale.ENGLISH.toString(), Locale.US.toString()));
			Date dataDate = null;
			try {
				dataDate = format.parse(soybeanUS.get("Time")[3].trim());//使用soybeanUS中的时间，认为其余几个也一样
			} catch (ParseException e) {
				LOG.error("解析数据日期失败。", e);
			}
			String timeIntStr = DateTimeUtil.formatDate(dataDate, DateTimeUtil.YYYYMM);
			LOG.info("timeInt: " + timeIntStr);
			Map<String, String> dataMap = new HashMap<String, String>();
			for(int columnId = 1; columnId < columns.length; columnId++){
				if(columns[columnId][2].equals("soybeanUS")){
					String price = soybeanUS.get(columns[columnId][1])[3].trim().replace(",", "");
					if(price.matches("\\d+")){
						dataMap.put(columns[columnId][0], price);
					} else if(price.equals("(D)")) {
						LOG.info(varName + "_" + cnName + "_" + columnId + "没有数据。");
					} else {
						LOG.error(varName + "_" + cnName + "_" + columnId + "数据错误。");
					}
				} else if(columns[columnId][2].equals("soybeanRegional")) {
					String price = soybeanRegional.get(columns[columnId][1])[3].trim().replace(",", "");
					if(price.matches("\\d+")){
						dataMap.put(columns[columnId][0], price);
					} else if(price.equals("(D)")) {
						LOG.info(varName + "_" + cnName + "_" + columnId + "没有数据。");
					} else {
						LOG.error(varName + "_" + cnName + "_" + columnId + "数据错误。");
					}
				} else if(columns[columnId][2].equals("selectedOilseed")) {
					String price = selectedOilseed.get(columns[columnId][1])[3].trim().replace(",", "");
					if(price.matches("\\d+")){
						dataMap.put(columns[columnId][0], price);
					} else if(price.equals("(D)")) {
						LOG.info(varName + "_" + cnName + "_" + columnId + "没有数据。");
					} else {
						LOG.error(varName + "_" + cnName + "_" + columnId + "数据错误。");
					}
				} else if(columns[columnId][2].equals("animal")) {
					String price = animal.get(columns[columnId][1])[3].trim().replace(",", "");
					if(price.matches("\\d+")){
						dataMap.put(columns[columnId][0], price);
					} else if(price.equals("(D)")) {
						LOG.info(varName + "_" + cnName + "_" + columnId + "没有数据。");
					} else {
						LOG.error(varName + "_" + cnName + "_" + columnId + "数据错误。");
					}
				} else {
					LOG.error(varName + "_" + cnName + "_" + columnId + "匹配文件中表格错误");
				}
			}
			LOG.info(varName + "_" + cnName + "_" + dataMap);
//			dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), dataMap, 84);//初始化菜单表中的crawlerId列
			dao.saveOrUpdateByDataMap(varName, cnName, Integer.parseInt(timeIntStr), dataMap);
		}
		//清空表中数据以防影响下次使用
		soybeanUS.clear();
		soybeanRegional.clear();
		selectedOilseed.clear();
		animal.clear();
	}
	
	public static void main(String[] args) {
		FatsAndOilsCountries fa = new FatsAndOilsCountries();
		//补最新数据
		fa.start();
		
		//补指定链接的数据
//		String targetHref = "http://usda.mannlib.cornell.edu/usda/nass/FatsOils//2010s/2016/FatsOils-10-03-2016.txt";
//		String targetHref = "http://usda.mannlib.cornell.edu/usda/nass/FatsOils//2010s/2016/FatsOils-11-01-2016.txt";
//		String targetHref = "http://usda.mannlib.cornell.edu/usda/nass/FatsOils//2010s/2016/FatsOils-12-01-2016.txt";
//		String targetHref = "http://usda.mannlib.cornell.edu/usda/nass/FatsOils//2010s/2017/FatsOils-01-03-2017.txt";
//		fa.fetchData(targetHref);
//		fa.parseAndSave();
	}
	
}
