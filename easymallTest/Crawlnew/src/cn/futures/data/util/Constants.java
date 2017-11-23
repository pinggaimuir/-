package cn.futures.data.util;


public class Constants {

	public static int DL_EXCHANGE_ID = 100;
	
	public static String FILE_SEPARATOR = System.getProperty("file.separator");
	
// For linux
	public static String DLCCDATA_ROOT = "/mnt/logs/crawlers/data/DLCCdata";
	public static String MARKETPRICEDATA_21FOOD_ROOT = "/mnt/logs/crawlers/data/MarketPriceData21Food";
	public static String TEMP_QUERY_21FOOD_ROOT = "/mnt/logs/crawlers/data/TempQueryData21Food";
	public static String ZZCCDATA_ROOT = "/mnt/logs/crawlers/data/CZCE";
	public static String MOFCOM_ROOT = "/mnt/logs/crawlers/data/MOFCOM";
	public static String YMTDATA_ROOT ="/mnt/logs/crawlers/data/YMTData";
	public static String YMTCURDATA_ROOT ="/mnt/logs/crawlers/data/YMTCURData";
	public static String YMTPHONE_ROOT = "/mnt/logs/crawlers/data/YMTPHONE";
	public static String YMTSUPPLY_ROOT = "/mnt/logs/crawlers/data/YMTSUPPLY";
	public static String SAVEDHTML_ROOT = "/mnt/logs/crawlers/data/HTML";
	public static String WEATHER_ROOT = "/mnt/logs/crawlers/data/WEATHER";//专用于保存中国天气数据（放了其它数据会对中国天气数据入库产生影响）
	public static String OTHER_WEATHER_ROOT = "/mnt/logs/Crawlers/data/OtherWeather";//新加天气爬虫数据保存目录，如泰国天气数据
	public static String FUTURES_ROOT = "/mnt/logs/crawlers/data/FUTURES";
	public static String INDIA_SUNRISE_ROOT = "/mnt/logs/crawlers/data/INDIA_SUNRISE";
	public static String DATABAK_TXT = "/mnt/logs/crawlers/data/DATABAK/";
	/*爬虫 爬取结果记录文件存放目录*/
	public static final String CRAWL_RESULT = "/mnt/logs/crawlers/cata/CrawlResult/";

/*	//for windows
	public static String DLCCDATA_ROOT = "D:\\Crawlers\\Data\\DLCCdata";//windows
	public static String MARKETPRICEDATA_21FOOD_ROOT = "D:\\Crawlers\\Data\\MarketPriceData21Food";//windows
	public static String TEMP_QUERY_21FOOD_ROOT = "D:\\Crawlers\\Data\\TempQueryData21Food";//windows
	public static String ZZCCDATA_ROOT = "D:\\Crawlers\\Data\\CZCE";//windows
	public static String MOFCOM_ROOT = "D:\\Crawlers\\Data\\NCMOFCOM";//windows
	public static String YMTDATA_ROOT ="D:\\Crawlers\\Data\\YMTData";
	public static String YMTCURDATA_ROOT ="D:\\Crawlers\\Data\\YMTCURData";
	public static String YMTPHONE_ROOT = "D:\\Crawlers\\Data\\YMTPHONE";
	public static String YMTSUPPLY_ROOT = "D:\\Crawlers\\Data\\YMTSUPPLY";
	public static String SAVEDHTML_ROOT = "D:\\Crawlers\\Data\\HTML";
	public static String WEATHER_ROOT = "D:\\Crawlers\\Data\\WEATHER";//专用于保存中国天气数据（放了其它数据会对中国天气数据入库产生影响）
	public static String OTHER_WEATHER_ROOT = "D:\\Crawlers\\Data\\OtherWeather";//新加天气爬虫数据保存目录，如泰国天气数据
	public static String FUTURES_ROOT = "D:\\Crawlers\\Data\\FUTURES";
	public static String INDIA_SUNRISE_ROOT = "D:\\Crawlers\\Data\\INDIA_SUNRISE";
	public static String DATABAK_TXT = "D:\\Crawlers\\Data\\DATABAK\\";
//	爬虫 爬取结果记录文件存放目录
	public static final String CRAWL_RESULT = "D:\\Crawlers\\Data\\CrawlResult\\";*/

	public static final String CRAWL_FAIL_RECORD_NAME = "CrawlResultRecord.csv";
	
	public static boolean updateAllowed = true;	//是否允许更新数据库，主要在测试时使用，false不允许，true为允许	
	public static int proxyFetchNum = 20;			//从数据库中一次取出的数量。
	
// For linux
/*	public static String DLCCDATA_ROOT = "/mnt/data/ProgramData/Crawl/DLCCdata";
	public static String YMTDATA_ROOT ="/mnt/data/ProgramData/Crawl/YMTData";
	public static String YMTCURDATA_ROOT ="/mnt/data/ProgramData/Crawl/YMTData";
	public static String MOFCOM_ROOT = "/mnt/data/ProgramData/MOFCOM";//windows	
	public static String MARKETPRICEDATA_21FOOD_ROOT = "/mnt/data/ProgramData/Crawl/MarketPriceData21Food";
	public static String TEMP_QUERY_21FOOD_ROOT = "/mnt/data/ProgramData/Crawl/TempQueryData21Food";
	public static String ZZCCDATA_ROOT = "/mnt/data/ProgramData/Crawl/CZCE";*/

	public static final String ENCODE_GBK = "GBK";
	public static final String ENCODE_GB2312 = "GB2312";
	public static final String ENCODE_UTF8 = "UTF-8";
//	public static String FILE_ENCODING = "utf-8";
	
}
