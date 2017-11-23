package cn.futures.data.importor.crawler.weatherCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.util.RegexUtil;

/**	
	* @description	各国主要城市气象数据
	* @author 		xjlong 
    * @date 		2016年8月23日  
*/
public class MainCityWeatherDataFetch {
	private static final String className = MainCityWeatherDataFetch.class.getName();
	private static final Logger LOG = Logger.getLogger(MainCityWeatherDataFetch.class);
	private DAOUtils dao = new DAOUtils();
	private RegexUtil reg = new RegexUtil();
	private static Map<String, String> countryName_Map = new LinkedHashMap<String, String>();//国家名中英文名映射
	private static Map<String, String> cityName_Map = new LinkedHashMap<String, String>();//城市中英文名映射
	private static Map<String, String> tableHeader_Map = new LinkedHashMap<String, String>();//表头信息中英文映射
	//初始化参数值
	static{
		//国家：对国家中英文名进行映射
		countryName_Map.put("ARGENT", "阿根廷");
		countryName_Map.put("AUSTRA", "澳大利亚");
		countryName_Map.put("BRAZIL", "巴西");
		countryName_Map.put("CANADA", "加拿大");
		countryName_Map.put("CHILE", "智利");
		countryName_Map.put("CHINA", "中国");
		countryName_Map.put("COTED", "科特迪瓦");
		countryName_Map.put("CUBA", "古巴");
		countryName_Map.put("EGYPT", "埃及");
		countryName_Map.put("ETHIOP", "埃塞俄比亚");
		countryName_Map.put("FRANCE", "法国");
		countryName_Map.put("GERMAN", "德国");
		countryName_Map.put("GREECE", "希腊");
		countryName_Map.put("INDIA", "印度");
		countryName_Map.put("INDONE", "印度尼西亚");
		countryName_Map.put("IRELAN", "爱尔兰");
		countryName_Map.put("ITALY", "意大利");
		countryName_Map.put("JAPAN", "日本");
		countryName_Map.put("KAZAKH", "哈萨克斯坦");
		countryName_Map.put("MALAYS", "马来西亚");
		countryName_Map.put("MEXICO", "墨西哥");
		countryName_Map.put("NKORE", "朝鲜");
		countryName_Map.put("NZEALA", "新西兰");
		countryName_Map.put("PAKIST", "巴基斯坦");
		countryName_Map.put("PERU", "秘鲁");
		countryName_Map.put("PHILIP", "菲律宾");
		countryName_Map.put("POLAND", "波兰");
		countryName_Map.put("PORTUG", "葡萄牙");
		countryName_Map.put("ROMANI", "罗马尼亚");
		countryName_Map.put("RUSSIA", "俄国");
		countryName_Map.put("SAFRI", "南非");
		countryName_Map.put("SKORE", "韩国");
		countryName_Map.put("SPAIN", "西班牙");
		countryName_Map.put("SWITZE", "瑞士");
		countryName_Map.put("SYRIA", "叙利亚");
		countryName_Map.put("THAILA", "泰国");
		countryName_Map.put("TURKEY", "土耳其");
		countryName_Map.put("TURKME", "土库曼斯坦");
		countryName_Map.put("UKINGD", "英国");
		countryName_Map.put("UKRAIN", "乌克兰");
		countryName_Map.put("UZBEKI", "乌兹别克斯坦");
		countryName_Map.put("ZAMBIA", "赞比亚");
		
		//城市：对城市中英文名进行映射
		cityName_Map.put("IGUAZU","伊瓜苏");
		cityName_Map.put("FORMOSA","福莫萨");
		cityName_Map.put("CERES","塞里斯");
		cityName_Map.put("CORDOBA","科尔多瓦");
		cityName_Map.put("RIO CUARTO","里奥夸尔托");
		cityName_Map.put("ROSARIO","罗萨里奥");
		cityName_Map.put("BUENOS AIRES","布宜诺斯艾利斯");
		cityName_Map.put("SANTA ROSA","圣罗莎");
		cityName_Map.put("TRES ARROYOS","特雷斯阿罗约斯");
		cityName_Map.put("DARWIN","达尔文");
		cityName_Map.put("BRISBANE","布里斯班");
		cityName_Map.put("PERTH","珀斯");
		cityName_Map.put("CEDUNA","塞杜纳");
		cityName_Map.put("ADELAIDE","阿德莱德");
		cityName_Map.put("MELBOURNE","墨尔本");
		cityName_Map.put("WAGGA","瓦格");
		cityName_Map.put("CANBERRA","堪培拉");
		cityName_Map.put("FORTALEZA","福塔雷萨");
		cityName_Map.put("RECIFE","累西腓市");
		cityName_Map.put("CAMPO GRANDE","大坎普");
		cityName_Map.put("FRANCA","弗朗卡");
		cityName_Map.put("RIO DE JANEIRO","里约热内卢");
		cityName_Map.put("LONDRINA","隆德里纳");
		cityName_Map.put("SANTA MARIA","圣玛丽亚");
		cityName_Map.put("TORRES","托雷斯");
		cityName_Map.put("TORONTO","多伦多");
		cityName_Map.put("MONTREAL","蒙特利尔");
		cityName_Map.put("WINNIPEG","温尼伯");
		cityName_Map.put("REGINA","里贾纳");
		cityName_Map.put("SASKATOON","萨斯卡通");
		cityName_Map.put("LETHBRIDGE","莱斯布里奇");
		cityName_Map.put("CALGARY","卡尔加里");
		cityName_Map.put("VANCOUVER","温哥华");
		cityName_Map.put("SANTIAGO","圣地亚哥");
		cityName_Map.put("HARBIN","哈尔滨");
		cityName_Map.put("HAMI","哈密市");
		cityName_Map.put("BEIJING","北京");
		cityName_Map.put("TIENTSIN","天津");
		cityName_Map.put("LHASA","拉萨");
		cityName_Map.put("KUNMING","昆明");
		cityName_Map.put("CHENGCHOW","郑州");
		cityName_Map.put("YEHCHANG","张掖");
		cityName_Map.put("HANKOW","汉口");
		cityName_Map.put("CHUNGKING","重庆");
		cityName_Map.put("CHIHKIANG","芷江");
		cityName_Map.put("WU HU","芜湖");
		cityName_Map.put("SHANGHAI","上海");
		cityName_Map.put("NANCHANG","南昌");
		cityName_Map.put("TAIPEI","台北");
		cityName_Map.put("CANTON","广州");
		cityName_Map.put("NANNING","南宁");
		cityName_Map.put("ABIDJAN","阿比让");
		cityName_Map.put("HAVANA","哈瓦那");
		cityName_Map.put("CAIRO","开罗");
		cityName_Map.put("ASWAN","阿斯旺");
		cityName_Map.put("ADDIS ABABA","亚的斯亚贝巴");
		cityName_Map.put("PARIS/ORLY","巴黎/奥利");
		cityName_Map.put("STRASBOURG","斯特拉斯堡");
		cityName_Map.put("BOURGES","布尔日");
		cityName_Map.put("BORDEAUX","波尔多");
		cityName_Map.put("TOULOUSE","图卢兹");
		cityName_Map.put("MARSEILLE","马赛");
		cityName_Map.put("HAMBURG","汉堡");
		cityName_Map.put("BERLIN","柏林");
		cityName_Map.put("DUSSELDORF","杜塞尔多夫");
		cityName_Map.put("LEIPZIG","莱比锡");
		cityName_Map.put("DRESDEN","德累斯顿");
		cityName_Map.put("STUTTGART","斯图加特");
		cityName_Map.put("NURNBERG","纽伦堡");
		cityName_Map.put("AUGSBURG","奥格斯堡");
		cityName_Map.put("THESSALONIKA","塞萨洛尼基");
		cityName_Map.put("LARISSA","拉里萨");
		cityName_Map.put("ATHENS","雅典");
		cityName_Map.put("AMRITSAR","阿姆利则");
		cityName_Map.put("NEW DELHI","新德里");
		cityName_Map.put("AHMEDABAD","艾哈迈达巴德");
		cityName_Map.put("INDORE","印多尔");
		cityName_Map.put("CALCUTTA","加尔各答");
		cityName_Map.put("VERAVAL","韦拉瓦尔");
		cityName_Map.put("BOMBAY","孟买");
		cityName_Map.put("POONA","浦那");
		cityName_Map.put("BEGAMPET","比甘姆比特");
		cityName_Map.put("VISHAKHAPATNAM","维萨卡帕特南");
		cityName_Map.put("MADRAS","马德拉斯");
		cityName_Map.put("MANGALORE","芒格洛尔");
		cityName_Map.put("SERANG","塞朗");
		cityName_Map.put("DUBLIN","都柏林");
		cityName_Map.put("MILAN","米兰");
		cityName_Map.put("VENICE","威尼斯");
		cityName_Map.put("GENOA","热那亚");
		cityName_Map.put("ROME","罗马");
		cityName_Map.put("NAPLES","那不勒斯");
		cityName_Map.put("SAPPORO","札幌市");
		cityName_Map.put("NAGOYA","名古屋");
		cityName_Map.put("TOKYO","东京");
		cityName_Map.put("YOKOHAMA","横滨");
		cityName_Map.put("KYOTO","京都");
		cityName_Map.put("OSAKA","大阪");
		cityName_Map.put("KUSTANAY","库斯塔纳");
		cityName_Map.put("TSELINOGRAD","亚斯塔纳");
		cityName_Map.put("KARAGANDA","卡拉干达");
		cityName_Map.put("KUALA LUMPUR","吉隆坡");
		cityName_Map.put("GUADALAJARA","瓜达拉哈拉");
		cityName_Map.put("TLAXCALA","特拉斯卡拉");
		cityName_Map.put("ORIZABA","奥里萨巴");
		cityName_Map.put("PYONGYANG","平壤");
		cityName_Map.put("AUCKLAND","奥克兰");
		cityName_Map.put("WELLINGTON","惠灵顿");
		cityName_Map.put("KARACHI","卡拉奇");
		cityName_Map.put("LIMA","利马");
		cityName_Map.put("MANILA","马尼拉");
		cityName_Map.put("WARSAW","华沙");
		cityName_Map.put("LODZ","罗兹");
		cityName_Map.put("KATOWICE","卡托维兹");
		cityName_Map.put("LISBON","里斯本");
		cityName_Map.put("BUCHAREST","布加勒斯特");
		cityName_Map.put("ST.PETERSBURG","圣彼得堡");
		cityName_Map.put("KAZAN","喀山");
		cityName_Map.put("MOSCOW","莫斯科");
		cityName_Map.put("YEKATERINBURG","叶卡捷琳堡");
		cityName_Map.put("OMSK","鄂木斯克");
		cityName_Map.put("BARNAUL","巴尔瑙尔");
		cityName_Map.put("KHABAROVSK","哈巴罗夫斯克");
		cityName_Map.put("VLADIVOSTOK","符拉迪沃斯托克");
		cityName_Map.put("VOLGOGRAD","伏尔加格勒");
		cityName_Map.put("ASTRAKHAN","阿斯特拉罕");
		cityName_Map.put("ORENBURG","奥伦堡");
		cityName_Map.put("JOHANNESBURG","约翰内斯堡");
		cityName_Map.put("DURBAN","德班");
		cityName_Map.put("CAPE TOWN","开普敦");
		cityName_Map.put("SEOUL","汉城");
		cityName_Map.put("VALLADOLID","巴利亚多利德");
		cityName_Map.put("MADRID","马德里");
		cityName_Map.put("SEVILLE","塞维利亚");
		cityName_Map.put("ZURICH","苏黎世");
		cityName_Map.put("GENEVA","日内瓦");
		cityName_Map.put("DAMASCUS","大马士革");
		cityName_Map.put("PHITSANULOK","彭世洛");
		cityName_Map.put("BANGKOK","曼谷");
		cityName_Map.put("ISTANBUL","伊斯坦布尔");
		cityName_Map.put("ANKARA","安卡拉");
		cityName_Map.put("ASHKHABAD","阿什哈巴德");
		cityName_Map.put("ABERDEEN","阿伯丁");
		cityName_Map.put("LONDON","伦敦");
		cityName_Map.put("KIEV","基辅");
		cityName_Map.put("LVOV","利沃夫");
		cityName_Map.put("KIROVOGRAD","基洛沃格勒");
		cityName_Map.put("ODESSA","敖德萨");
		cityName_Map.put("KHARKOV","哈尔科夫");
		cityName_Map.put("TASHKENT","塔什干");
		cityName_Map.put("LUSAKA","卢萨卡");
		
		//表头：对表头中英文名进行映射
		/*tableHeader_Map.put("AVG_MAX","月平均最高气温");
		tableHeader_Map.put("AVG_MIN", "月平均最低气温");
		tableHeader_Map.put("HI_MAX", "月最高气温");
		tableHeader_Map.put("LO_MIN", "月最低气温");
		tableHeader_Map.put("AVG", "月平均气温");
		tableHeader_Map.put("TDEP_NRM", "月平均气温偏离正常值");
		tableHeader_Map.put("TOT", "月累计降水量");
		tableHeader_Map.put("DEP_NRM", "月累计降水量偏离正常值");*/
		
		//表头：对表头中英文名进行映射
		tableHeader_Map.put("weatherData_1","月平均最高气温");
		tableHeader_Map.put("weatherData_2", "月平均最低气温");
		tableHeader_Map.put("weatherData_3", "月最高气温");
		tableHeader_Map.put("weatherData_4", "月最低气温");
		tableHeader_Map.put("weatherData_5", "月平均气温");
		tableHeader_Map.put("weatherData_6", "月平均气温偏离正常值");
		tableHeader_Map.put("weatherData_7", "月累计降水量");
		tableHeader_Map.put("weatherData_8", "月累计降水量偏离正常值");
		
		
	}
	
	//传入数据读取文件，开始读取
	public void start(){
		String file = "E:\\data\\weather_weekly-03-2017.xlsx";
		readExcel(file);
	}
	//从excel表格中读取数据
	public void readExcel(String file){
		XSSFWorkbook xwb;
		try {
			Map<String, String> dataMap = new HashMap<String, String>();
			xwb = new XSSFWorkbook(file);
			XSSFSheet sheet = xwb.getSheetAt(0); 
			Map<Integer,String> map = new HashMap<Integer,String>();
			int timeInt = Integer.parseInt(sheet.getSheetName());
			XSSFRow row;  
			String cell;  
			for (int i = 3; i <sheet.getPhysicalNumberOfRows(); i++) {  
			    row = sheet.getRow(i); 
			    String regEx = "-?\\d+(\\.\\d+)?";
			    String city = row.getCell(1).toString();
			    String cnName=cityName_Map.get(city);
			    for(int j=0;j<8;j++){
			    	map.put(j, row.getCell(j+2).toString());
		    		if(reg.getMatchStr(map.get(j), regEx) == null || reg.getMatchStr(map.get(j), regEx).isEmpty()){
		    			map.put(j, "");
			    	}else{
			    		dataMap.put(tableHeader_Map.get("weatherData_"+(j+1)),map.get(j));
			    	}
			    }
			    if(null != cnName && !dataMap.isEmpty()){
			    	LOG.info("开始保存主要城市-"+cnName+"-的气象数据...");
			    	try {
			    		dao.saveOrUpdateByDataMap("主要城市气象数据", cnName, timeInt, dataMap);
					} catch (Exception e) {
						e.printStackTrace();;
						LOG.info("主要城市-"+city+"-的气象数据保存失败...");
					}
			    }else{
			    	LOG.info("参数为空，不能保存相关气象数据...");
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		LOG.info(file+"的气象数据已添加完成....");
	 }
	
	public static void main(String[] args){
		MainCityWeatherDataFetch mainWeather = new MainCityWeatherDataFetch();
		mainWeather.start();
	}
}
