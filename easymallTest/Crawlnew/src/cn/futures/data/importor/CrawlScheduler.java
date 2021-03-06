package cn.futures.data.importor;

public class CrawlScheduler {	
	
	public static final String CRON_SEND_MAIL ="0 0 0 * * ?";
	public static final String CRON_USDA = "0 15 6 10,11,12,13,14 * ?";// ÿ��10,11,12,13,14�ռ����ҳ����Ƿ���£�û�и��µ�ֱ�Ӹ���
	//public static final String CRON_USDA = "0 15 6 12 * ?";
	//public static final String CRON_CN   = "00 31 16 ? * MON-FRI";//��һ����������16��ץȡ�������
	public static final String CRON_US   = "10 0 3 ? * TUE-SAT";//美国商品交易所的数据
	//public static final String CRON_ASIA = "10 1 23 ? * MON-FRI";//��һ����������23��ץȡ�������
	public static final String CRON_MYOIL_QUOTE = "0 50 7 ? * TUE-SAT";//��������������ڻ��������
	public static final String CRON_Calculator = "00 05 8-17 ? * MON-FRI";//��һ�����壬�°����㵱��Ĳ����ָ�����
	
	public static final String CRON_CN_FH = "00 00 8-17 ? * MON-FRI";
	public static final String CRON_US_FH = "00 10 0-23 ? * TUE-SAT";
	public static final String CRON_ASIA_FH = "00 20 0/1 ? * MON-FRI";
	public static final String CRON_WHOLESALE = "30 38 10 ? * MON-SAT";
	public static final String CRON_WHOLESALE_MONTHLY = "00 38 10 ? * MON-SAT";
	public static final String CRON_BJ_MARKET = "0 20 05 * * ?";
	
	public static final String CRON_CHICAGO_RICE = "0 15 7 ? * TUE-SAT";
	public static final String CRON_LONDON_SUGAR = "0 20 7 ? * TUE-SAT";
	public static final String CRON_RAPESEED_LAST = "0 45 3 ? * TUE-SAT";
	public static final String CRON_COTTON_STOCK = "0 50 3 ? * TUE-SAT";
	public static final String CRON_BALTIC_DRY_INDEX = "0 20 4,16 * * ?";
	public static final String CRON_MYOIL_INDEX = "0 50 16 * * ?";
	public static final String CRON_COTTON_TEXTILE_PRICE = "0 55 16 * * ?";
	public static final String CRON_COTLOOKA_INDEX = "0 17 13 * * ?";
	public static final String CRON_COTLOOK_YARN_INDEX = "0 20 16 ? * MON";
	public static final String CRON_CAN_RAPESEED_PRICE = "0 30 9 * * TUE-SAT";
	public static final String CRON_CAAA_POULTRY_PRICE = "0 30 18 ? * MON-FRI";
	public static final String CRON_FARM_PRODUCE_PRICE = "0 44 16 ? * WED-THU";
	public static final String CRON_STATDEP_DATA = "0 35 16 ? * MON-FRI";
	public static final String CRON_MOA_PRICE = "0 05 19 ? * MON-FRI";
	public static final String CRON_SC_PIG = "0 05 16 ? * FRI";
	public static final String CRON_CAAA_FAIR_PRICE = "0 15 20 5,15,25 * ?";//������ֳ���м۸�-�й�����ҵ��Ϣ��ÿ��15��ץȡ
	public static final String CRON_DCE_DATA = "0 17 16 ? * MON-FRI";
	public static final String CRON_USDA_WEEKLY_EXPORT = "0 02 8 ? * FRI";
	public static final String CRON_USDA_ERS_MONTHLY = "0 15 6 26 * ?";
	public static final String CRON_CZCE_DATA = "0 11 16 ? * MON-FRI";//持仓数据
	public static final String CRON_MOFCOM_DATA = "0 33 3 * * ?";
	public static final String CRON_USDOLLOR_EXG_DATA = "0 0 16 ? * MON-FRI";
	public static final String CRON_CHINA_DATA = "0 33 16,20 * * ?";//各省区鸡蛋价格
	public static final String CRON_WEATHER_DATA = "0 45 5,17 * * ?";
	public static final String CRON_WEATHER_WINDS = "0 01 0 * * ?";//中国天气风速数据
	public static final String CRON_BOYARCN_DATA = "0 17 18 * * ?";
	public static final String CRON_BEEFAMUTTON = "0 17 15 * * ?";
	public static final String CRON_YMT_DATA = "0 17 21 * * ?";
	public static final String CRON_YMT_SUPPLY_DATA = "0 27 1 * * ?";
	public static final String CRON_MOF_IMPORT_DATA = "0 44 1 * * ?";//
	public static final String CRON_CTFC_FUTURES = "0 7 10 ? * TUE";//CFTC期货持仓等数据
	public static final String CRON_YUMICOM_DATA = "0 7 16 * * ?";
	public static final String CRON_CTEICN_DATA = "0 2 10 ? * MON-FRI";
	public static final String CRON_COTTON_SFMIANHUA = "0 42 16 * * ?";
	public static final String CRON_MANN_USDA = "0 4 5 ? * TUE";
	public static final String CRON_DCE_FUTURES = "00 46 15 ? * MON-FRI";//大商所期货数据
	public static final String CRON_CZCE_FUTURES = "00 49 19 ? * MON-FRI";//֣郑商所期货数据
	public static final String CRON_SHFE_FUTURES = "00 52 15 ? * MON-FRI";//�Ϻ�����
	public static final String CRON_CUSTOM_IMPOREXP = "0 22 17 20-25 * ?";//��������ÿ��20-25����Ƿ����
	public static final String CRON_INDIA_WEATHER = "0 42 7 * * ?";
	public static final String PROXY_INIT_SCHEDULER = "0 59 * * * ?";
	public static final String CRON_THAILAND_WEATHER = "0 50 11 * * ?";//泰国天气数据
	public static final String CRON_CMEData = "0 55 10 * * ?";//国外期货数据爬虫（quandl网站上的期货数据）
	public static final String CRON_AM_POULTEYMEAT_EGGS = "0 30 4 ? * THU";//美国禽肉-肉鸡种蛋入孵量、肉鸡雏订购量爬虫
	public static final String CRON_AM_POULTEYMEAT_MONTH = "0 33 4 3 * ?";//美国肉禽和鸡蛋月更新数据爬虫
	public static final String CRON_DUCKEGGS_PRICE = "0 0 09 ? * FRI";//肉禽禽蛋周度数据爬虫1
	public static final String CRON_WHITEBARSDUCK_PRICE = "0 0 10 ? * FRI";//禽肉：白条鸭批发市场报价
	public static final String CRON_AM_GARLICDATA_DAY = "0 30 09 * * ?";//大蒜日产区及日批发价格指数数据
	public static final String CRON_AM_WHITESHEEPANDCOW_WEEK = "0 0 15 ? * FRI";//饲料养殖--白条羊&&白条牛批发价格
	public static final String CRON_CN_PRICE_INFO = "0 2 21 ? * WED";//中国价格信息网爬虫
	public static final String CRON_GINDER_WEEK = "0 0 10 ? * SAT";//生姜---周价格指数
	public static final String CRON_CINDER_DAY = "0 30 11 * * ?";//生姜--日价格相关数据
	public static final String CRON_CINDER_MONTH = "0 30 10 10 * ?";//生姜--月价格指数
	public static final String CRON_USDAFATSANSOILS = "0 0 9 3 * ?";//美国动物油脂数据1
	public static final String CRON_GLOBALCOMMODITYPRICESMONTH = "5 0 5 20 * ?";//全球大宗商品价格月度数据
	public static final String CRON_CORNSTARCHPRICE = "0 25 15 ? * MON-FRI";//饲料养殖-淀粉-主销区玉米淀粉成交价、主要区域玉米淀粉市场价格
	public static final String CRON_CORNPURCHASEPRICE = "0 23 15 ? * MON-FRI";//饲料养殖-玉米-深加工企业收购价
	public static final String CRON_SHANGSHUIWEATHER = "07 07 * ? * *";//商水天气数据：24小时实况数据
	public static final String CRON_CHINAJCIDATA_1 = "0 30 13 ? * THU";//中国汇易网数据-1
	public static final String CRON_CHINAJCIDATA_2 = "0 30 15 ? * MON";//中国汇易网数据-2
	public static final String CRON_CHINAJCIDATA_3 = "0 0 15 * * ?";//中国汇易网数据-3
	public static final String CRON_USBEANSMARGIN = "0 30 14 ? * MON";//油料油脂-美国大豆-现货理论榨利
	public static final String CRON_XINFADIMARKETPRICE = "0 15 17 * * ?";//北京新发地市场数据
	public static final String CRON_MofcomMarketPrice = "0 20 19 * * ?";//新农村商网中的批发市场价格数据
	public static final String CRON_FATSANDOILSCOUNTRIES = "0 6 9 5 * ?";//油脂油料——分国别
	public static final String CRON_HEADLINEINDEXGARLIC = "0 0 1/3 * * ?";//新浪大蒜指数每三个小时执行一次

	//price21food相关
	public static final String CRON_DLCC   = "0 40 16 * * ?";//大连持仓量排名
	public static final String CRON_21FOOD = "0 30 17 * * ?";//食品商务网价格行情
	public static final String CRON_CZCE = "20 32 17 * * ?";//大连持仓量排名
	public static final String CRON_MOFCOM = "0 00 20 1 * ?";//Mofcom 每月价格行情
}
