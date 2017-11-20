package cn.futures.data.importor.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;

import cn.futures.data.DAO.FvFirDao;
import cn.futures.data.entity.FvFirModel;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.DateTimeUtil;

public class DataOfFvFir  {
	private static final String className = DataOfFvFir.class.getName();
	// 果蔬 数据抓取
	private static final Log logger = LogFactory.getLog(DataOfFvFir.class);
	
	private FvFirDao fvFirDao = new FvFirDao();
	private boolean onlyCurr = false;// 是否仅 获取今天
	private Date date; // 当天日期
	private int emptyFlag = 0; // 空数据
	private int maxFaildTimes = 50;// 最大失败次数
	private String url = "http://schq.bjscxh.cn/jshqlist.php"; // 提交网址
	private static Map<Integer, String> varieties;
	private List<FvFirModel> fvFirModels;
	static {
		varieties = new HashMap<Integer, String>();
		varieties.put(1, "蔬菜");
		varieties.put(2, "肉蛋禽");
		varieties.put(3, "粮油");
		varieties.put(4, "水产");
		varieties.put(5, "水果");
	}
	
	@Scheduled
	(cron = CrawlScheduler.CRON_BJ_MARKET)
	public void start(){
		String switchFlag = new CrawlerManager().selectCrawler("北京果蔬", className.substring(className.lastIndexOf(".")+1));
		if(switchFlag == null){
			logger.info("没有获取到水果、蔬菜在数据库中的定时器配置");
		}else{
			if(switchFlag.equals("1")){
				run();
			}else{
				logger.info("抓取水果、蔬菜的定时器已关闭");
			}
		}
	}
	
	public void run() {
		try {
			try {
				loadFvFirModels();
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("loadFvFirModels Error",e);
			}
			
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
			getData();
		} catch (Exception e) {
			logger.error("run", e);
		}
	}
	
	private void loadFvFirModels() throws Exception{
		fvFirModels = fvFirDao.getTableList("286", "299");
		if (fvFirModels==null) {
			logger.info("fvFirModels is null");
		}
	}	
	private String getDbTableByKind(String kind){
		String tableName = "";
		for(FvFirModel fvFirModel:fvFirModels){
			String cnName = fvFirModel.getCnName();
			String dbName = fvFirModel.getDbName();
			if (kind.equals(cnName)) {
				tableName = dbName;
				break;
			}
		}
		return tableName;
	}
	private void saveData(String cnName,String bigClass,int timeInt,Double minPrice,Double maxPrice,
			Double avgPrice,Double dealPrice,Double dealNum,Double localNum,Double outNum) {	
		String tableName = getDbTableByKind(cnName.trim());
		if (tableName==null || tableName.isEmpty() || tableName.trim().length()<1) {
			logger.error("can not find dbTableName by "+cnName);
			return;
		}
		if (bigClass.equals("蔬菜")) {
			fvFirDao.save(tableName, 286, timeInt,minPrice,maxPrice,avgPrice,dealPrice,dealNum,localNum,outNum);
		}else if(bigClass.equals("水果")) {
			fvFirDao.save(tableName, 299, timeInt,minPrice,maxPrice,avgPrice,dealPrice,dealNum,localNum,outNum);
		}
	}

	public void getData() {
		String scid = "0";
		if(date==null){
			date = new Date();
			date = DateTimeUtil.addDay(date, -1);
		}
		if(fvFirDao==null){	
			fvFirDao = new FvFirDao();
		}
		if(fvFirModels == null){
			try{
				fvFirModels = fvFirDao.getTableList("286", "299");
			}catch(Exception e){
				logger.error("fvfirdao gettablelist error:",e);
			}
		}
		if (fvFirModels==null) {
			logger.info("fvFirModels is null");
		}

		int cid = 1;
		logger.info("果蔬价格数据抓取开始!");
		// do {
		while (cid < 6) {
			if(cid>1&&cid<5){
				cid ++;
				continue;
			}
			logger.info("cid:" + cid + "    date:" + DateTimeUtil.formatDate(date, "yyyy-MM-dd"));
			Document doc = null;
			int count = 0;
			while(true){
				try {
					Thread.sleep(3000*(count++));
					doc = Jsoup.connect(url).data("cid", "" + cid).data("rq", DateTimeUtil.formatDate(date, "yyyy-MM-dd")).data("scid", scid).timeout(10000).post();
					//parseDocIntoXLS(doc, DateTimeUtil.formatDate(date, "yyyyMMdd"), cid);			
				} catch(Exception e){
					logger.error(e);
					continue;
				}
				break;
			}
			parseDoc(doc, DateTimeUtil.formatDate(date, "yyyyMMdd"),cid);
			emptyFlag = 0;
			cid++;
		}

		// cid = 1; // cid分类
		// date = DateTimeUtil.addDay(date, -1);// 时间倒退一天
		// } while (emptyFlag < maxFaildTimes && !onlyCurr);
	}

	// 解析内容 存入数据库
	public synchronized void parseDoc(Document doc, String timeInt, Integer cid) {
		Element table = doc.getElementById("table1");
		if (table != null && table.childNodes() != null && table.childNodes().size() >= 1) { // tbody
			Element tbody = table.child(0);
			if (tbody != null && tbody.childNodes() != null && tbody.childNodes().size() >= 2) {// 跳过title
				int itemLen = tbody.children().size();
				Double minPrice,maxPrice,avgPrice,dealPrice,dealNum,localNum,outNum;
				for (int i = 2; i < itemLen; i++) {
					try {
						List<Element> tds = tbody.child(i).children();	
						String cnName = tds.get(2).text().trim();
						cnName = cnName.replace("哈蜜瓜", "哈密瓜");
						cnName = cnName.replace("粟子", "栗子");
						cnName = cnName.replace("其他", "其他"+varieties.get(cid));
						try {
							minPrice = Double.parseDouble(tds.get(4).text());
						} catch (Exception e) {
							minPrice = null;
						}
						try {
							maxPrice = Double.parseDouble(tds.get(6).text());
						} catch (Exception e) {
							maxPrice = null;
						}
						try {
							avgPrice = Double.parseDouble(tds.get(8).text());
						} catch (Exception e) {
							avgPrice = null;
						}
						try {
							dealPrice = Double.parseDouble(tds.get(12).text());
						} catch (Exception e) {
							dealPrice = null;
						}
						try {
							dealNum = Double.parseDouble(tds.get(14).text())*10;
						} catch (Exception e) {
							dealNum = null;
						}
						try {
							localNum = Double.parseDouble(tds.get(16).text())*10;
						} catch (Exception e) {
							localNum = null;
						}
						try {
							outNum = Double.parseDouble(tds.get(18).text())*10;
						} catch (Exception e) {
							outNum = null;
						}
						String ccid = varieties.get(cid);
						
						saveData(cnName, ccid, Integer.valueOf(timeInt), minPrice,
								maxPrice, avgPrice, dealPrice, dealNum, localNum, outNum);					
					
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("parseDoc", e);
					}
				}
			}
		}
	}

	// 解析内容 存入Excel表
/*	public synchronized void parseDocIntoXLS(Document doc, String timeInt, Integer cid) {
		Element table = doc.getElementById("table1");
		if (table != null && table.childNodes() != null && table.childNodes().size() >= 1) { // tbody
			Element tbody = table.child(0);
			if (tbody != null && tbody.childNodes() != null && tbody.childNodes().size() >= 2) {// 跳过title
				int itemLen = tbody.children().size();
				for (int rowIndex = 0; rowIndex < itemLen; rowIndex++) {
					try {
						if (rowIndex == 0) { // 第一行 获取标题
							xls.createRow(rowIndex); // 创建行
							List<Element> firTitle = tbody.child(0).children();
							List<Element> secTitle = tbody.child(1).children();
							xls.setCell(0, "TimeInt");// 时间
							xls.setCell(1, "分类名");// 分类名
							xls.setCell(2, firTitle.get(2).text() + "(" + secTitle.get(2).text() + ")");// 品种名
							xls.setCell(3, firTitle.get(4).text() + "(" + secTitle.get(4).text() + ")");// 最低批发价（元/公斤）
							xls.setCell(4, firTitle.get(6).text() + "(" + secTitle.get(6).text() + ")");// 最高批发价（元/公斤）
							xls.setCell(5, firTitle.get(8).text() + "(" + secTitle.get(8).text() + ")");// 批发价（元/公斤）
							xls.setCell(6, firTitle.get(10).text() + "(" + secTitle.get(10).text() + ")");// 零售平均价（元/公斤）
							xls.setCell(7, firTitle.get(12).text() + "(" + secTitle.get(12).text() + ")");// 成交额（万元）
							xls.setCell(8, firTitle.get(14).text() + "(" + secTitle.get(14).text() + ")");// 成交量（万公斤）
							xls.setCell(9, firTitle.get(16).text() + "(" + secTitle.get(16).text() + ")");// 本市（万公斤）
							xls.setCell(10, firTitle.get(18).text() + "(" + secTitle.get(18).text() + ")");// 外阜（万公斤）
							xls.setCell(11, "写入时间");// 写入时间
						}
						xls.createRow(rowIndex); // 创建行
						List<Element> tds = tbody.child(rowIndex).children();
						xls.setCell(0, timeInt);// 时间
						xls.setCell(1, varieties.get(cid));// 分类名
						xls.setCell(2, tds.get(2).text());// 品种名
						xls.setCell(3, tds.get(4).text());// 最低批发价（元/公斤）
						xls.setCell(4, tds.get(6).text());// 最高批发价（元/公斤）
						xls.setCell(5, tds.get(8).text());// 批发价（元/公斤）
						xls.setCell(6, tds.get(10).text());// 零售平均价（元/公斤）
						xls.setCell(7, tds.get(12).text());// 成交额（万元）
						xls.setCell(8, tds.get(14).text());// 成交量（万公斤）
						xls.setCell(9, tds.get(16).text());// 本市（万公斤）
						xls.setCell(10, tds.get(18).text());// 外阜（万公斤）
						xls.setCell(11, DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));// 写入时间
						xls.exportXLS();
						//add for vagetables and fruits
//						minPrice,maxPrice,avgPrice,dealPrice,dealNum,localNum,outNum
						Double minPrice,maxPrice,avgPrice,dealPrice,dealNum,localNum,outNum;
						try {
							minPrice = Double.parseDouble(tds.get(4).text());
						} catch (Exception e) {
							minPrice = null;
						}
						try {
							maxPrice = Double.parseDouble(tds.get(6).text());
						} catch (Exception e) {
							maxPrice = null;
						}
						try {
							avgPrice = Double.parseDouble(tds.get(8).text());
						} catch (Exception e) {
							avgPrice = null;
						}
						try {
							dealPrice = Double.parseDouble(tds.get(12).text());
						} catch (Exception e) {
							dealPrice = null;
						}
						try {
							dealNum = Double.parseDouble(tds.get(14).text())*10;
						} catch (Exception e) {
							dealNum = null;
						}
						try {
							localNum = Double.parseDouble(tds.get(16).text())*10;
						} catch (Exception e) {
							localNum = null;
						}
						try {
							outNum = Double.parseDouble(tds.get(18).text())*10;
						} catch (Exception e) {
							outNum = null;
						}
						
						saveData(tds.get(2).text(), varieties.get(cid), Integer.valueOf(timeInt), minPrice,
								maxPrice, avgPrice, dealPrice, dealNum, localNum, outNum);					
					} catch (Exception e) {
						String sql = "select * from FruitsAndVegetables f where f.cid=? and f.timeInt=? ";
						try {
							FruitsAndVegetables fav = new FruitsAndVegetables();
							FruitsAndVegetables f = this.fruitsAndVegetablesManager.findForObject(sql, cid, timeInt);
							if (f == null) {
								fav.setTimeInt(Integer.parseInt(timeInt));
								fav.setCid(cid);// 分类 对应着请求的网站的分类 ：蔬菜、水果等
								fav.setVname(StringUtils.trim(vname));// 品种
								this.fruitsAndVegetablesManager.add(fav);
							}
						} catch (Exception e1) {
							logger.error("parseDocIntoXLS catch", e1);
						}
						logger.error("parseDocIntoXLS", e);
					}
				}
			}
		}
	}*/

	public String getResult(String value) {
		if (value==null || value.trim().length()<1) {
			return "";
		}
		return value;
	}

	public Double getDouble(String doubleString) {
		if (doubleString != null && doubleString.trim().length()>0) {
			return Double.parseDouble(doubleString);
		}
		return null;
	}

	public String getStringTrim(Element item) {
		if (item != null) {
			return item.text().trim();
		}
		return "";
	}

	public static void main(String[] args) {
		//(new DataOfFvFir()).run();
		DataOfFvFir t = new DataOfFvFir();
		//t.start();
		Date startdate = DateTimeUtil.parseDateTime("2016-02-18","yyyy-MM-dd");
		Date enddate = DateTimeUtil.parseDateTime("2016-02-23","yyyy-MM-dd");		
		while(startdate.compareTo(enddate)<=0){
			t.date = startdate;
			t.getData();
			startdate = DateTimeUtil.addDay(startdate,1);
		}
		t.run();
	}
}
