package cn.futures.data.importor.crawler.marketPrice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.Constants;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DataFetchUtil;
import cn.futures.data.util.DateTimeUtil;
import cn.futures.data.util.RecordCrawlResult;
import cn.futures.data.util.RegexUtil;

/**
 * 新农村商网中的批发市场价格数据
 * */
public class MofcomMarketPrice {
	private static String className = MofcomMarketPrice.class.getName();
	private DataFetchUtil dataFetchUtil = new DataFetchUtil();
	private DAOUtils dao = new DAOUtils();
	private static final Logger LOG = Logger.getLogger(MofcomMarketPrice.class);
	private static final String baseUrl = "http://nc.mofcom.gov.cn/channel/gxdj/jghq/jg_detail.shtml?id=#id&startTime=#startTime&endTime=#endTime&page=";
	//示例：http://nc.mofcom.gov.cn/channel/gxdj/jghq/jg_detail.shtml?id=2576578&startTime=2017-01-04&endTime=2017-01-04&page=2
	private static final Map<String, String> proNameBySign = new HashMap<String, String>();//产品名映射（只包含与表中存储不一致的）
	static {
		proNameBySign.put("血橙/二月红/中华红", "血橙_二月红_中华红");
		proNameBySign.put("富士（纸袋Φ70-84）", "富士（纸袋70_84）");
		proNameBySign.put("无籽小号-大号", "无籽小号_大号");
		proNameBySign.put("赣南脐橙Ф65-Ф95", "赣南脐橙65_95");
		proNameBySign.put("南美、菲律宾香蕉", "南美_菲律宾香蕉");
		proNameBySign.put("富士（纸袋Φ85以上）", "富士（纸袋85以上）");
		proNameBySign.put("花牛(Φ70-85)", "花牛(70_85)");
		proNameBySign.put("小兰.特小凤", "小兰_特小凤");
		proNameBySign.put("鄂、湘、川脐橙", "鄂_湘_川脐橙");
	}
	private static final String[][] markets = {
			{"北京昌平区水屯农副产品批发市场", "2576578"}, 
			{"北京顺义区顺鑫石门农产品批发市场", "20551"}, 
			{"北京市日上综合商品批发市场", "20548"}, 
			{"北京市通州八里桥农产品中心批发市场", "20536"}, 
			{"北京市锦绣大地玉泉路粮油批发市场", "20546"}, 
			{"北京市华垦岳各庄批发市场", "20532"}, 
			{"北京朝阳区大洋路农副产品批发市场", "20540"}, 
			{"北京市锦绣大地农副产品批发市场", "20543"},
			{"上海农产品中心批发市场有限公司", "20547"},
			{"上海市江桥批发市场", "20542"},
			{"云南通海金山蔬菜批发市场", "21081"},
			{"云南省呈贡县龙城蔬菜批发市场", "45195"},
			{"内蒙古包头市友谊蔬菜批发市场", "2543558"},
			{"东瓦窑农副产品批发市场", "20779"},
			{"内蒙古美通食品批发市场", "2576564"},
			{"内蒙古赤峰西城蔬菜批发市场", "20801"},
			{"吉林辽源市物流园区仙城水果批发市场", "20909"},
			{"吉林松原市三井子杂粮杂豆产地批发市场", "45646"},
			{"四川凉山州西昌市广平农副土特产品市场", "15002168"},
			{"四川绵阳市高水蔬菜批发市场", "21019"},
			{"四川省成都市农产品批发中心", "45233"},
			{"四川南充北川农产品批发市场", "2549825"},
			{"四川凉山州会东县堵格乡牲畜交易市场", "21036"},
			{"四川省江油仔猪批发市场", "8757171"},
			{"四川成都龙泉聚和果蔬菜交易中心", "21010"},
			{"四川汉源县九襄农产品批发市场", "45218"},
			{"四川泸州仔猪批发市场", "21014"},
			{"四川广安邻水县农产品交易中心", "8757132"},
			{"四川成都西部禽蛋批发市场", "21006"},
			{"天津市东丽区金钟蔬菜市场", "20556"},
			{"天津市西青区红旗农贸批发市场", "20570"},
			{"天津市武清区大沙河蔬菜批发市场", "20555"},
			{"天津何庄子批发市场", "20553"},
			{"天津市西青区当城蔬菜批发市场", "20571"},
			{"天津市韩家墅农产品批发市场", "8755761"},
			{"吴忠市鑫鲜农副产品市场有限公司", "21045"},
			{"宁夏银川市北环批发市场", "21051"},
			{"安徽合肥周谷堆农产品批发市场", "20859"},
			{"安徽省阜阳农产品中心批发市场", "20840"},
			{"安徽砀山农产品中心惠丰批发市场", "9060946"},
			{"安徽安庆市龙狮桥蔬菜批发市场", "20843"},
			{"安徽亳州蔬菜批发市场", "20825"},
			{"安徽马鞍山安民农副产品批发交易市场", "20849"},
			{"安徽蚌埠蔬菜批发市场", "20857"},
			{"安徽濉溪县中瑞农副产品批发市场", "45482"},
			{"安徽和县皖江蔬菜批发大市场", "45559"},
			{"安徽六安裕安区紫竹林农产品批发市场", "45544"},
			{"安徽舒城蔬菜大市场", "20822"},
			{"山东章丘刁镇蔬菜批发市场", "20749"},
			{"青岛黄河路农产品批发市场", "22346051"},
			{"济南七里堡蔬菜综合批发市场", "20756"},
			{"青岛抚顺路蔬菜副食品批发市场", "20747"},
			{"山东青岛莱西东庄头蔬菜批发", "20743"},
			{"山东临邑县临南蔬菜大市场", "20633"},
			{"山东省威海市水产品批发市场", "20663"},
			{"山东龙口果蔬批发市场", "20700"},
			{"山东宁津县东崔蔬菜市场合作社", "20662"},
			{"山东青岛市沧口蔬菜副食品批发市场", "20740"},
			{"山东青岛城阳蔬菜水产品批发市场", "20734"},
			{"山东省淄博市鲁中果品批发市场", "20722"},
			{"山东青岛平度市南村蔬菜批发市场", "20732"},
			{"山东寿光农产品物流园", "20696"},
			{"山东滨州市滨城区(六街)鲁北无公害蔬菜批发", "20652"},
			{"晋城绿盛农业技术开发有限公司", "2550347"},
			{"山西晋城绿欣农产品批发市场", "20712"},
			{"太原市河西农副产品市场", "2550411"},
			{"山西长治市金鑫瓜果批发市场", "45065"},
			{"山西省临汾市尧丰农副产品批发市场", "20762"},
			{"山西汾阳市晋阳农副产品批发市场", "20741"},
			{"山西运城市蔬菜批发市场", "20726"},
			{"山西长治市紫坊农副产品综合交易市场", "20707"},
			{"山西省大同市南郊区振华蔬菜批发市场", "20697"},
			{"山西太原市城东利民果菜批发市场", "20693"},
			{"晋新绛蔬菜批发市场", "2576593"},
			{"山西省吕梁离石马茂庄农贸批发市场", "8763884"},
			{"山西省临汾市襄汾县农副产品批发市场", "20766"},
			{"山西阳泉蔬菜瓜果批发市场", "20702"},
			{"晋运城果品中心市场", "2576584"},
			{"广东省广州市江南农副产品市场", "20742"},
			{"广东汕头农副产品批发中心", "20754"},
			{"广东江门市新会区水果食品批发市场有限公司", "20776"},
			{"广东江门市水产冻品副食批发市场", "45296"},
			{"广东东莞大京九农副产品中心批发市场", "45271"},
			{"广西南宁五里亭蔬菜批发市场", "2576518"},
			{"广西田阳县果蔬菜批发市场", "20992"},
			{"新疆乌鲁木齐北园春批发市场", "21035"},
			{"新疆石河子西部绿珠果蔬菜批发市场", "20985"},
			{"新疆乌鲁木齐市凌庆蔬菜果品有限公司", "21033"},
			{"新疆维吾尔自治区克拉玛依农副产品批发市场", "2576419"},
			{"新疆乌尔禾蔬菜批发市场", "8763855"},
			{"新疆米泉通汇农产品批发市场", "45088"},
			{"新疆博乐市农五师三和市场", "21015"},
			{"新疆焉耆县光明农副产品综合批发市场", "45078"},
			{"新疆伊犁哈萨克族自治州霍城县界梁子66团农贸市场", "20995"},
			{"新疆兵团农二师库尔勒市孔雀农副产品综合批发市场", "9348188"},
			{"江苏凌家塘市场发展有限公司", "20959"},
			{"南京农副产品物流中心", "22346058"},
			{"江苏省无锡市天鹏集团公司", "2576431"},
			{"江苏扬州联谊农副产品批发市场", "45612"},
			{"江苏无锡朝阳市场", "20969"},
			{"江苏徐州七里沟农副产品中心", "20965"},
			{"苏州市南环桥农副产品批发市场", "20956"},
			{"江苏常州宣塘桥水产品交易市场", "20958"},
			{"江苏建湖水产批发市场", "45591"},
			{"江苏丰县南关农贸市场", "20962"},
			{"江苏宜兴蔬菜副食品批发市场", "20968"},
			{"江西南昌农产品中心批发市场", "20792"},
			{"南方粮食交易市场", "15060112"},
			{"江西乐平市蔬菜开发总公司", "20784"},
			{"江西永丰县蔬菜中心批发市场", "45527"},
			{"江西赣州南北蔬菜大市场", "20777"},
			{"荷花坑批发市场", "9348019"},
			{"河北省怀来县京西果菜批发市场", "45328"},
			{"河北省邯郸市（馆陶）金凤禽蛋农贸批发市场", "45276"},
			{"河北饶阳县瓜菜果品交易市场", "20660"},
			{"河北乐亭县冀东果蔬批发市场", "20599"},
			{"石家庄桥西蔬菜中心批发市场", "20576"},
			{"河北秦皇岛（昌黎）农副产品批发市场", "20613"},
			{"河北沧州市红枣批发市场", "45060"},
			{"河北秦皇岛海阳农副产品批发市场", "20610"},
			{"河北邯郸（魏县）天仙果品农贸批发交易市场", "20615"},
			{"浙江嘉兴蔬菜批发交易市场", "20884"},
			{"浙江省金华农产品批发市场", "45578"},
			{"杭州农副产品物流中心", "45569"},
			{"嘉兴水果市场", "20885"},
			{"浙江宁波市蔬菜副食品批发交易市场", "45586"},
			{"浙江温州菜篮子集团", "20895"},
			{"浙江义乌农贸城", "20871"},
			{"浙江绍兴蔬菜果品批发交易中心", "20877"},
			{"浙江省杭州笕桥蔬菜批发交易市场", "20907"},
			{"浙江嘉善浙北果蔬菜批发交易", "20880"},
			{"湖北武汉白沙洲农副产品大市场", "20661"},
			{"湖北宜昌金桥蔬菜果品批发市场", "20670"},
			{"湖北孝感市南大批发市场", "2550029"},
			{"鄂襄樊蔬菜批发市场", "2547955"},
			{"湖北荆州两湖平原农产品交易物流中心", "8756123"},
			{"湖北省黄石市农副产品批发交易公司", "20664"},
			{"武汉市皇经堂批发市场", "20653"},
			{"湖北十堰市堰中蔬菜批发市场", "20668"},
			{"湖北浠水市城北农产品批发大市场", "20690"},
			{"湖北洪湖市农贸市场", "20687"},
			{"湖北省鄂州市蟠龙蔬菜批发交易市场", "20673"},
			{"湖南长沙马王堆蔬菜批发市场", "2543746"},
			{"湖南长沙红星农副产品大市场", "20704"},
			{"湖南衡阳西园蔬菜批发市场", "20716"},
			{"湖南岳阳花板桥批发市场", "20723"},
			{"湖南常德甘露寺蔬菜批发市场", "20728"},
			{"湖南邵阳市江北农产品大市场", "20719"},
			{"湖南益阳市团洲蔬菜批发市场", "20733"},
			{"湖南省吉首市蔬菜果品批发大市场", "20736"},
			{"甘肃靖远县瓜果蔬菜批发市场", "21124"},
			{"甘肃酒泉春光农产品市场", "15001931"},
			{"甘肃腾胜农产品集团", "2550066"},
			{"兰州大青山蔬菜瓜果批发市场", "21129"},
			{"甘肃天水瀛池果菜批发市场", "21121"},
			{"甘肃省陇西县首阳镇蔬菜果品批发市场", "45682"},
			{"甘肃省武山县洛门蔬菜批发市场", "45682"},
			{"福建同安闽南果蔬批发市场", "45539"},
			{"福建福鼎闽浙边界农贸中心市场", "20799"},
			{"海峡农副产品批发物流中心", "20819"},
			{"贵州铜仁地区玉屏畜禽产地批发市场", "21063"},
			{"辽宁省朝阳果菜批发市场", "2576573"},
			{"辽宁鞍山宁远农产品批发市场", "20876"},
			{"大连双兴批发市场", "20869"},
			{"重庆观农贸批发市场", "45217"},
			{"陕西泾阳县云阳蔬菜批发市场", "21048"},
			{"西部欣桥农产品物流中心市场", "32902062"},
			{"陕西咸阳市新阳光农副产品批发市场", "21053"},
			{"青海省西宁市仁杰粮油批发市场", "45117"},
			{"黑龙江鹤岗万圃源蔬菜批发市场", "2604441"},
			{"黑龙江哈尔滨哈达果菜批发市场有限公司", "20926"},
			{"河南省濮阳市王助蔬菜瓜果批发市场", "20614"},
			{"河南商丘市农产品中心批发市场", "20632"},
			{"河南安阳豫北蔬菜批发市场", "20595"},
			{"河南郑州市农产品物流配送中心", "20584"},
			{"河南郑州毛庄蔬菜批发市场", "20582"},
			{"豫南阳果品批发交易中心", "2547980"},
			{"河南三门峡西原店蔬菜批发市场", "20619"},
			{"河南信阳市平桥区豫信花生制品有限公司", "2547851"},
			{"河南新野县蔬菜批发交易市场", "20628"}
};
	
	@Scheduled(cron = CrawlScheduler.CRON_MofcomMarketPrice)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("新农村商网批发市场数据", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			LOG.info("没有获取到新农村商网批发市场数据在数据库中的定时器配置");
			RecordCrawlResult.recordFailData(className, null, null, "没有获取到新农村商网批发市场数据在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				Date date = new Date();
				fetchData(date);
			}else{
				LOG.info("抓取新农村商网批发市场数据的定时器已关闭");
				RecordCrawlResult.recordFailData(className, null, null, "抓取新农村商网批发市场数据的定时器已关闭");
			}
		}
	}
	
	public void fetchData(Date date){
		String startTime = DateTimeUtil.formatDate(date, DateTimeUtil.YYYY_MM_DD);
		int timeInt = Integer.parseInt(DateTimeUtil.formatDate(date, DateTimeUtil.YYYYMMDD));
		for(int i = 0; i < markets.length; i++){
			LOG.info("开始抓取" + markets[i][0]);
			try{
				fetchOneMarket(markets[i][0] , markets[i][1], startTime, timeInt);
			} catch(Exception e) {
				LOG.error("发生未知异常。", e);
			}
		}
	}
	
	public void fetchOneMarket(String marketName , String id, String startTime, int timeInt){
		String curUrl = baseUrl.replace("#id", id).replace("#startTime", startTime).replace("#endTime", startTime);
		int pageCount = 0;//数据总页数
		//访问第一页解析出总页数
		String tempHtml = dataFetchUtil.getCompleteContent(0, curUrl, Constants.ENCODE_GBK, marketName + "_pageCount");
		if(tempHtml != null){
			List<String> pageList = RegexUtil.getMatchStr(tempHtml, "var v_PageCount = (\\d+);", new int[]{1});
			if(!pageList.get(0).isEmpty()){
				pageCount = Integer.parseInt(pageList.get(0));
			}
		}
		LOG.info("总页数:" + pageCount);
		String[] filter = {"table", "class", "s_table03"};
		String[] rowColChoose = {"0", "111000"};
		//依次访问每一页（含第一页），解析保存
		for(int pageNo = 1; pageNo < pageCount; pageNo++){
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				LOG.error(e);
			}
			LOG.info("开始解析第" + pageNo + "页");
			String table = dataFetchUtil.getPrimaryContent(0, curUrl + pageNo, Constants.ENCODE_GBK, marketName, filter, rowColChoose , 0);
			String[] rows = table.split("\n");
			for(int rowId = 0; rowId < rows.length; rowId++){
				String[] cols = rows[rowId].split(",");
				Map<String, String> priceInfo = new HashMap<String, String>();
				priceInfo.put("min_price", null);
				priceInfo.put("max_price", null);
				priceInfo.put("ave_price", cols[1]);
				priceInfo.put("produce_area", null);
				priceInfo.put("specification", null);
				String productName = null;
				if(proNameBySign.containsKey(cols[0])){
					productName = proNameBySign.get(cols[0]);
				} else {
					productName = cols[0];
				}
				dao.saveOrUpdateForMarketPrice(productName, marketName, timeInt, priceInfo);
			}
		}
	}
	
	public static void main(String[] args) {
		MofcomMarketPrice m = new MofcomMarketPrice();
//		m.start();
		
		//补指定日期历史数据
		Date tarDate = DateTimeUtil.parseDateTime("20170216", DateTimeUtil.YYYYMMDD);
		m.fetchData(tarDate);
	}
}
