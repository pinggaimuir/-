package cn.futures.data.util;

import cn.futures.data.jdbc.JdbcRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrawlerManager {
	
	private Log logger = LogFactory.getLog(CrawlerManager.class);
	
	public void insertCrawler(Object[] param){
		int len = param.length;
		if(len != 8) {
			logger.info("参数个数不对");
		}else{
			Object[][] vals = new Object[1][len];
			String questionMark = "";
			for(int i=0;i<len;i++){
				vals[0][i] = param[i];
				questionMark += ",?";
			}
			String sql = "insert into bric_crawler_table (name,actionName,detail,time,switch,errNum,lastErrNum,remark) values ("+questionMark.substring(1)+")";
			logger.info(sql);
			execInsert(sql,vals);
		}
	}
	
	public List<String[]> selectCrawler(){
		List<String[]> results = new ArrayList<String[]>();
		String table = "bric_crawler_table";
		String query = "select * from "+table;
		JdbcRunner jdbc = null;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			while (rs.next()){
				int len = 9;
				String[] tmp = new String[len];
				tmp[0] = rs.getInt(1)+"";
				tmp[1] = rs.getString(2);
				tmp[2] = rs.getString(3);
				tmp[3] = rs.getString(4);
				tmp[4] = rs.getString(5);
				tmp[5] = rs.getString(6);
				tmp[6] = rs.getInt(7)+"";
				tmp[7] = rs.getInt(8)+"";
				tmp[8] = rs.getString(9);
				results.add(tmp);
			}
		} catch (Exception e){
			logger.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return results;
	}
	
	public String selectCrawler(String name, String actionName){
		String table = "bric_crawler_table";
		String query = "select switch from "+table+" where name='"+name+"' and actionName='"+actionName+"' limit 1";
		JdbcRunner jdbc = null;
		try{
			ResultSet rs;
			jdbc = new JdbcRunner();
			rs = jdbc.query(query);
			if (rs.next()){
				return rs.getString(1);
			}
		} catch (Exception e){
			logger.error("",e);
		} finally {
			if(jdbc!=null)
				jdbc.release();
		}
		return null;
	}
	
	private void execInsert(String sql, Object[][] vals){
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
		} catch (Exception e){
			logger.error("insert data into DB error",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				logger.error("DB Transaction rollback error",e1);
			} finally {
				jdbc.release();
			}
		} finally {
			jdbc.release();
		}	
	}
	
	public void xxx(){
		String file = "E:\\workspace\\Crawlnew\\data\\工作簿1.xls";
		//JdbcRunner jdbc = new JdbcRunner();
		HSSFWorkbook book = null;
		try {
			InputStream input = new FileInputStream(file);
			book = new HSSFWorkbook(input);
			HSSFSheet sheet = book.getSheetAt(0);
			
			
			for(int i=0;i<sheet.getLastRowNum();i++){				
				if( sheet.getRow(i)==null || sheet.getRow(i).getCell(0)==null){
					continue;
				}
				String id = String.valueOf(Double.valueOf(sheet.getRow(i).getCell(0).getNumericCellValue()).intValue());
				String time = sheet.getRow(i).getCell(4).getStringCellValue();
				String sql ="update bric_crawler_table set [time]='"+time+"' where id="+id;
				System.out.println(sql);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		
//		Object [][] params = {
//				{"Cotlook棉纱指数", "CotlookYarnIndexFetch", "棉花纺织-棉纱-Cotlook棉纱指数", "0 20 16 ? * MON", "1", 0, 0, "来源：中国棉花信息网"},
//				{"大宗农产品进口数据", "AgriImportsData", "进口装船、进口到港数据", "0 44 1 * * ?", "1", 0, 0, "来源：商务部"},
//				{"波罗的海运价指数", "BalticDryIndexFetch", "饲料养殖-国际运费-波罗的海运价指数", "0 20 4,16 * * ?", "1", 0, 0, "来源：中国海事服务网"},
//				{"毛鸭鸭苗猪价格", "BOYARCNDataFetch", "毛鸭、鸭苗、生猪、仔猪", "0 17 18 * * ?", "1", 0, 0, "来源：博亚和讯网"},
//				{"CAAA饲料养殖集市价格", "CAAAMeatPoultryPrice", "CAAA饲料养殖集市价格", "0 15 20 15 * ?", "1", 0, 0, "来源：中国畜牧业信息网"},
//				{"CAAA肉禽蛋禽", "CAAAMeatPoultryPrice", "CAAA肉禽蛋禽", "0 30 18 ? * MON-FRI", "1", 0, 0, "来源：中国畜牧业信息网"},
//				{"加拿大国内菜籽报价", "CANInnerRapeseedPriceFetch", "油料油脂-加拿大油菜籽-加拿大国内菜籽报价", "0 30 9 * * TUE-SAT", "1", 0, 0, "来源：加拿大奥博塔农业生产委员会"},
//				{"全国各省鸡蛋与淘汰鸡价格", "ChinaDataFetch", "全国各省鸡蛋与淘汰鸡价格", "0 33 20 * * ?", "1", 0, 0, "来源：芝华数据蛋鸡数据"},
//				{"Cotlook棉价指数", "CotlookAIndexFetch", "棉花纺织-皮棉-Cotlook棉价指数", "0 17 13 * * ?", "1", 0, 0, "来源：www.cotlook.com"},
//				{"ICE棉花合约库存", "CottonStockFetch", "期货行情数据—ICE棉花—合约库存", "0 50 3 ? * TUE-SAT", "1", 0, 0, "来源：交易所"},
//				{"纱布、粘胶短纤、涤纶短纤价格", "CottonTextilePriceFetch", "纱布、粘胶短纤、涤纶短纤价格", "0 55 16 * * ?", "1", 0, 0, "来源：中国棉花信息网"},
//				{"持仓排名、仓单日报", "CZCEDataFetch", "郑商所持仓排名、仓单日报", "0 11 16 ? * MON-FRI", "1", 0, 0, "来源：郑州商品交易所"},
//				{"北京果蔬", "DataOfFvFir", "北京果蔬", "0 30 16 * * ?", "1", 0, 0, "来源：北京市场协会"},
//				{"棕榈油玉米资讯、持仓排名、仓单日报", "DCEDataFetch", "大商所棕榈油玉米资讯、持仓排名、仓单日报", "0 17 16 ? * MON-FRI", "1", 0, 0, "来源：大连商品交易所"},
//				{"全国农副产品批发价格", "FarmProducePriceFetch", "全国农副产品批发价格", "0 44 19 ? * THU", "1", 0, 0, "来源：商务部"},
//				{"全国各省羊肉牛肉批发市场价格", "FeedTradeData", "全国各省羊肉牛肉批发市场价格", "0 17 15 * * ?", "1", 0, 0, "来源：一亩田"},
//				{"农业部畜产品和饲料集贸市场价格", "MOAAnimalFeedMarketPrice", "农业部畜产品和饲料集贸市场价格", "0 05 19 ? * MON-FRI", "1", 0, 0, "来源：中国农业信息网"},
//				{"新农村商网价格行情数据", "MOFCOMDataFetch", "新农村商网价格行情数据", "0 33 3 * * ?", "1", 0, 0, "来源：商务部"},
//				{"马来西亚棕榈油涛动指数日值", "MYPalmOilAltIndex", "油料油脂-马来西亚棕榈油-涛动指数日值", "0 50 16 * * ?", "1", 0, 0, "来源：澳大利亚气象台"},
//				{"生猪", "PigDataFetch", "全国规模以上生猪定点屠宰企业白条肉平均出厂价格、四川生猪价格预警", "0 05 16 ? * FRI", "1", 0, 0, "来源：国务院数据、四川省农业厅"},
//				{"WCE油菜籽收盘价", "RapeseedLastFetch", "期货行情数据-WCE油菜籽-收盘价", "0 45 3 ? * TUE-SAT", "1", 0, 0, "来源：加拿大奥博塔农业生产委员会"},
//				{"统计局流通领域、50个城市价格", "StatDepDatas", "统计局流通领域、50个城市价格", "0 35 16 ? * MON-FRI", "1", 0, 0, "来源：统计局"},
//				{"美元兑主要货币", "USDollarExgData", "美元兑主要货币", "0 11 12 ? * MON-FRI", "1", 0, 0, "来源：www.123cha.com"},
//				{"芝加哥美稻米连续数据", "ChicagoRiceFetch", "期货行情数据-芝加哥美稻米-指数连续、主力连续、1-5号连续", "0 15 7 ? * TUE-SAT", "1", 0, 0, "来源：芝加哥商品交易所"},
//				{"伦敦白糖连续数据", "LondonSugarFetch", "期货行情数据—伦敦白糖—1-5号连续", "0 20 7 ? * TUE-SAT", "1", 0, 0, "来源：交易所"},
//				{"中国各品种期货数据", "MarketCrawler", "郑州白糖、郑州棉花等期货数据", "00 31 16 ? * MON-FRI", "1", 0, 0, "来源：和讯网"},
//				{"中国各品种期货数据(每小时一次)", "MarketCrawler", "郑州白糖、郑州棉花等期货数据", "00 00 8-17 ? * MON-FRI", "1", 0, 0, "来源：和讯网"},
//				{"美国各品种期货数据(每小时一次)", "MarketCrawlerForeign", "ICE原糖、ICE棉花等期货数据", "00 10 0-23 ? * TUE-SAT", "1", 0, 0, "来源：和讯网"},
//				{"美国各品种期货数据", "MarketCrawlerForeign", "ICE原糖、ICE棉花等期货数据", "10 0 3 ? * TUE-SAT", "1", 0, 0, "来源：和讯网"},
//				{"马来西亚棕榈油连续数据", "MYPalmOilQuotFetch", "期货行情数据—马来西亚棕榈油—指数连续、主力连续、1-9号连续", "0 50 7 ? * TUE-SAT", "1", 0, 0, "来源：马来西亚交易所"},
//				{"美农周出口数据", "USDAOnlineData", "周出口、年度累计出口、本年度未执行、本年度销售等", "0 02 8 ? * FRI", "1", 0, 0, "来源：美国农业部"},
//				{"美国牛奶月度数据", "USDAOnlineData", "美国牛奶月度数据", "0 15 6 26 * ?", "1", 0, 0, "来源：美国农业部"},
//				{"美农全球数据、平衡表数据", "USDAOnlineData", "美农全球数据、平衡表数据", "0 15 6 10,11,12,13,14 * ?", "1", 0, 0, "来源：美国农业部"},
//				{"一亩田价格行情", "YMTDataFetch", "一亩田价格行情数据", "0 17 21 * * ?", "1", 0, 0, "来源：一亩田"},
//		        {"一亩田供应数据", "YMTSupplyDataFetch", "一亩田供应数据", "0 27 1 * * ?", "1", 0, 0, "来源：一亩田"},
//				{"CFTC期货数据", "CFTCFuturesData", "CFTC期货持仓等数据", "0 33 15 ? * MON", "1", 0, 0, "来源：商品期货交易委员会（CFTC）"},
//				{"棉壳、棉短绒、棉油棉籽价格", "CottonTextilePriceFetch", "顺丰棉花网：棉壳、棉短绒、棉油棉籽出厂价", "0 42 16 * * ?", "1", 0, 0, "来源：顺丰棉花网"},
//				{"纱线、坯布价格", "CTEICNFetch", "中国纺织经济信息网：纱线、坯布价格", "0 2 10 ? * MON-FRI", "1", 0, 0, "来源：中国纺织经济信息网"},
//				{"海关总署：进出口数据", "CustomsIOData", "海关总署：进出口数据", "0 22 17 20-25 * ?", "1", 0, 0, "来源：海关总署"},
//				{"中国玉米网：淀粉、酒精价格", "YUMICOMFetch", "中国玉米网：淀粉、酒精价格", "0 7 16 * * ?", "1", 0, 0, "来源：中国玉米网"},
//				{"上海商品交易所期货行情数据", "SHFEFuturesMarket", "上海商品交易所期货行情数据", "0 52 15 ? * MON-FRI", "1", 0, 0, "来源：上海商品交易所"},
//				{"大连商品交易所期货行情数据", "DCEFuturesMarket", "大连商品交易所期货行情数据", "0 46 15 ? * MON-FRI", "1", 0, 0, "来源：大连商品交易所"},
//				{"郑州商品交易所期货行情数据", "CZCEFuturesMarket", "郑州商品交易所期货行情数据", "0 49 15 ? * MON-FRI", "1", 0, 0, "来源：郑州商品交易所"},
//				{"美农作物生长数据", "MannUsdaData", "美农作物生长数据", "0 4 5 ? * TUE", "1", 0, 0, "来源：美国农业部"},
//				{"印度气象局数据", "IndiaWeather", "印度气象局数据", "0 42 7 * * ?", "1", 0, 0, "来源：印度气象局"},
//				{"中央气象台数据", "ChinaWeatherData", "中央气象台数据", "0 45 5,17 * * ?", "1", 0, 0, "来源：中央气象台"},
//				{"中央气象台日照时长", "ChinaWeatherData", "中央气象台日照时长", "0 7 21 * * ?", "1", 0, 0, "来源：中央气象台"},
		//		};
		Object[] param = {"中央气象台数据", "ChinaWeatherData", "中央气象台数据", "0 45 5,17 * * ?", "1", 0, 0, "来源：中央气象台"};
		//new CrawlerManager().insertCrawler(param);
		new CrawlerManager().xxx();
	}
}
