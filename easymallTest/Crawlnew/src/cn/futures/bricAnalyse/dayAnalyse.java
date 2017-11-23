package cn.futures.bricAnalyse;

import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.importor.crawler.futuresMarket.MarketCrawler;
import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;
import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.CrawlerUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class dayAnalyse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new dayAnalyse().run();
	}
	
	public static final Map<Integer,Integer> qixianduizhao = new HashMap<Integer,Integer>();
	static {
		qixianduizhao.put(139, 4);	//郑州白糖 139 ，白砂糖 4
		qixianduizhao.put(140, 126);	//郑州棉花 140,  皮棉126
		qixianduizhao.put(141, 77);	//141菜籽油， 菜籽油77
		//qixianduizhao.put(142, 56);	//142早籼稻，早籼稻56
		//qixianduizhao.put(143, 31);		//143郑州强麦，小麦31
		qixianduizhao.put(144, 30);		//144大连豆一，大豆30
		qixianduizhao.put(145, 32);		//145大连豆粕，豆粕32
		qixianduizhao.put(146, 76);		//146豆油，豆油76
		qixianduizhao.put(147, 78);		//147棕榈油，棕榈油78
		qixianduizhao.put(148, 29);		//148玉米，玉米29
		//qixianduizhao.put(297, 74);	//297菜籽，油菜籽 74
		qixianduizhao.put(298, 34);	//298菜粕，菜粕34
		//qixianduizhao.put(303, 47);	//303鸡蛋，鸡蛋47
		//qixianduizhao.put(304, 23);	//304粳稻，稻米23
	}

	public static final Map<Integer,String> saveTable = new HashMap<Integer,String>();
	static {
		saveTable.put(139, "CX_Price");	//郑州白糖 139 ，白砂糖 4
		saveTable.put(140, "CX_Price");	//郑州棉花 140，  皮棉126
		saveTable.put(141, "CX_Price1");	//141菜籽油， 菜籽油77
		saveTable.put(142, "CX_Price");	//142早籼稻，早籼稻56
		saveTable.put(143, "CX_Price");		//143郑州强麦，小麦31
		saveTable.put(144, "CX_Price1");		//144大连豆一，大豆30
		saveTable.put(145, "CX_Price");		//145大连豆粕，豆粕32
		saveTable.put(146, "CX_Price2");		//146豆油，豆油76
		saveTable.put(147, "CX_Price1");		//147棕榈油，棕榈油78
		saveTable.put(148, "CX_Price");		//148玉米，玉米29
		saveTable.put(297, "CX_Price1");	//297菜籽，油菜籽 74
		saveTable.put(298, "CX_Price");	//298菜粕，菜粕34
		saveTable.put(303, "CX_Price");	//
	}
	
	@Scheduled
	(cron = CrawlScheduler.CRON_Calculator)
	public void run(){
		//System.out.println("22");
		Object[] vars = qixianduizhao.keySet().toArray();// {139,140,141,142,143,144,145,146,147,148,297,298};
		List<BricAnalyseData> dataList = new ArrayList<BricAnalyseData>();
		for(int i=0;i<vars.length;i++){
			BricAnalyseData P = calculator((Integer)vars[i]);
			if(P!=null)
				dataList.add(P);
		}
		calGuanzhudu(dataList);		
		for(int i=0;i<dataList.size();i++){
			saveData(dataList.get(i));
		}
		
		//调用计算糖类数据的存储过程
		calculatorSuger();
	}
	
	public void calculatorSuger(){

		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.execProc("dayAnalyse_sugar");
			jdbc.endTransaction();
		} catch (Exception e){
			System.out.println("analyse sugar data error");
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				System.out.println("DB Transaction rollback error");
			} finally {
				jdbc.release();
			}
		} finally {
			if (jdbc!=null) {
				jdbc.release();			
			}

		}
	}
	
	public void calGuanzhudu(List<BricAnalyseData> list){		
			
		//获取关注度的各项权重度
		String sqlGetWeight = " select * from CX_MarketData_DayAnalyse_Weight";
		System.out.println(sqlGetWeight);
		double qijiaWeight=0,xianhuoWeight=0,holdWeight=0,moneyWeight=0;
		JdbcRunner jdbc = null;
		try {

			jdbc = new JdbcRunner();		
			ResultSet rs = jdbc.query(sqlGetWeight);
			if(rs.next()){
				qijiaWeight = rs.getDouble("期价权重")/100.00;
				xianhuoWeight = rs.getDouble("现货权重")/100.00;
				holdWeight =  rs.getDouble("持仓权重")/100.00;
				moneyWeight = rs.getDouble("资金流向权重")/100.00;
			}else{
				System.out.println("没有权重配置数据");
				//return ;
			}		
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			if(jdbc!=null){
				jdbc.release();
			}
		}
		
		double Sqijiabiandong = 0;//主力期价变动绝对值的平方和
		for(int i=0;i<list.size();i++){
			Sqijiabiandong += Math.pow(list.get(i).zhuliqijiabiandong,2);
		}
		for(int i=0;i<list.size();i++){
			if(Sqijiabiandong == 0){
				list.get(i).guanzhudu=0;
			}else{
				list.get(i).guanzhudu += Math.pow(list.get(i).zhuliqijiabiandong,2)/Sqijiabiandong*qijiaWeight;
			}
		}		
	}
	
	public void saveData(BricAnalyseData p){
		//保存p
		String sqlsave = "insert into CX_MarketData_DayAnalyse" +
				"(edittime,timeint,varid,主力期价,主力期价变动,现货均价,现货均价变动,基差" +
				",基差变动,持仓总量,持仓变动,成交总量,成交变动,关注度,mainCode,资金流向)" +
				"values" +
				"(sysdate(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] vals = new Object[15];
		DecimalFormat df = new DecimalFormat("0.000000"); 
		
			vals[0] = p.Timeint;
			vals[1] = p.VarId;
			vals[2] = p.zhuliqijia;
			vals[3] = Float.valueOf(df.format(p.zhuliqijiabiandong));
			vals[4] = Float.valueOf(df.format( p.xianhuojunjia));
			vals[5] = Float.valueOf(df.format( p.xianhuojunjiabiandong));
			vals[6] = Float.valueOf(df.format( p.jicha));
			vals[7] = Float.valueOf(df.format( p.jichabiandong));
			vals[8] = p.chicangzongliang;
			vals[9] = Float.valueOf(df.format( p.chicangbiandong));
			vals[10] = p.chengjiaozongliang;
			vals[11] = Float.valueOf(df.format( p.chengjiaobiandong));
			vals[12] = Float.valueOf(df.format( p.guanzhudu));
			vals[13] = p.mainCode;
			vals[14] = p.zijinliuxiang;
		
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.update2(sqlsave,vals);
			jdbc.endTransaction();
			String sql = "update CFG_TABLE_META_NEW  set dataUpdateTime=sysdate() where dbName like 'CX_MarketData_DayAnalyse' and varid="+p.VarId;
			jdbc.update(sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}
		
	}
	
	public  BricAnalyseData calculator(int varid){				
		//读取昨日数据,timeint不能单纯-1，有可能是跨越周末的数据,可以获取时间早于今天的最新数据
		int timeintStart = CrawlerUtil.todayTimeStartHourint();
		
		
		
		
		String sqlLast = "select  * from CX_MarketData_DayAnalyse where varId = "
		+varid+" and timeint < "+timeintStart+" order by timeint desc,edittime desc limit 1";
		//获取当天数据，也有可能当天有很多条数据，以edittime取最新数据.持仓量和成交量成交额取指数连续，其他取主力连续
		int timeintHour = CrawlerUtil.nowTimeintHour();
		String sqlCurrent = "select  a.timeint,a.varid,a.code,a.edittime,a.开盘价,a.最高价,a.最低价,a.收盘价,a.结算价,b.持仓量,b.成交量,b.成交额 "
				+" from CX_MarketData_2FH a,CX_MarketData_1FH b where a.timeint=b.timeint and a.varId=b.varId "
				+" and a.varid = "+varid+"  and a.timeint <= "+timeintHour+" order by a.timeint desc,a.edittime desc limit 1";
		//获取当天现货均价，当天可能没有数据，就提取最新的报价数据
		String sqlXianhuo = "select * from "+saveTable.get(varid)+" where varId = "
		+qixianduizhao.get(varid)+" and timeint<="+(timeintHour/100)+" order by timeint desc,edittime desc limit 1";
		
		MarketPrice pCurrent = new MarketPrice();
		BricAnalyseData pLast = new BricAnalyseData();
		BricAnalyseData p = new BricAnalyseData();
		
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();			
			//提取今天的最新数据
			System.out.println(sqlCurrent);
			ResultSet rs = jdbc.query(sqlCurrent);
			if(rs.next()){
				pCurrent.setTimeint(rs.getInt("timeint"));
				pCurrent.setVarid(rs.getInt("varid"));
				pCurrent.setCode(rs.getString("code"));
				pCurrent.setEditTime(rs.getTimestamp("edittime"));
				pCurrent.setOpen(rs.getFloat("开盘价"));
				pCurrent.setHigh(rs.getFloat("最高价"));
				pCurrent.setLow(rs.getFloat("最低价"));
				pCurrent.setLast(rs.getFloat("收盘价"));
				pCurrent.setSettle(rs.getFloat("结算价"));
				pCurrent.setPosition(rs.getFloat("持仓量"));
				pCurrent.setVolume(rs.getFloat("成交量"));
				pCurrent.setTurnover(rs.getFloat("成交额"));	
			}else{//无结过
				System.out.println("无法获取到当天的数据2,今天尚无期货数据");
				//进行对应的异常处理
				return null;
			}
			//提取之前的统计分析数据
			System.out.println(sqlLast);
			rs = jdbc.query(sqlLast);	
			if(rs.next()){
				pLast.id = rs.getInt("id");
				pLast.Timeint = rs.getInt("timeint");
				if(pLast.Timeint<timeintStart-300){
					System.out.println("好像很久没有计算数据了：");
					//异常处理
				}
				pLast.VarId = rs.getInt("varid");
				pLast.mainCode = rs.getString("mainCode");
				pLast.EditTime =rs.getTimestamp("edittime");
				pLast.zhuliqijia = rs.getFloat("主力期价");
				pLast.zhuliqijiabiandong = rs.getFloat("主力期价变动");
				pLast.xianhuojunjia = rs.getFloat("现货均价");
				pLast.xianhuojunjiabiandong = rs.getFloat("现货均价变动");
				pLast.jicha = rs.getFloat("基差");
				pLast.jichabiandong = rs.getFloat("基差变动");
				pLast.chicangzongliang = rs.getFloat("持仓总量");
				pLast.chicangbiandong = rs.getFloat("持仓变动");
				pLast.chengjiaozongliang = rs.getFloat("成交总量");
				pLast.chengjiaobiandong = rs.getFloat("成交变动");
				pLast.zijinliuxiang = rs.getFloat("资金流向");
				pLast.guanzhudu = rs.getFloat("关注度");	
			}else{//无结果
				System.out.println("无法获取到之前的分析数据1");
				//需要进行对应的异常处理
				return null;
			}
			
			//提取现货均价
			System.out.println(sqlXianhuo);
			rs = jdbc.query(sqlXianhuo);
			double xianhuojunjia = 0;
			if(rs.next()){
				int time = rs.getInt("timeint");
				if( time<timeintStart/100 ){
					//今天还没有报价数据
					System.out.println("今天还没有报价数据");
				}else if(time<timeintStart-100){
					//昨天也没有报价数据
					System.out.println("没有这两天的报价数据");
				}
				xianhuojunjia = rs.getDouble("全国");
			}else{
				System.out.println("没有今天的现货均价");
				//异常处理
				return null;
			}
			
			//计算今天的统计分析数据
			p.Timeint = timeintHour;
			p.VarId = varid;
			p.mainCode = pCurrent.getCode();
			p.zhuliqijia = pCurrent.getLast();
			if( p.zhuliqijia == 0 ){
				System.out.println("数据异常");				
			}
			if( pLast.zhuliqijia == 0 ){
				p.zhuliqijiabiandong = 999;				
			}
			else
				p.zhuliqijiabiandong = (p.zhuliqijia-pLast.zhuliqijia)/pLast.zhuliqijia*100.0f;
			p.xianhuojunjia = xianhuojunjia;
			if( p.xianhuojunjia == 0 ){
				System.out.println("数据异常");
			}
			if( pLast.xianhuojunjia == 0 ){
				p.xianhuojunjiabiandong = 999;
			}
			else
				p.xianhuojunjiabiandong = (p.xianhuojunjia - pLast.xianhuojunjia)/pLast.xianhuojunjia*100.0f;
			p.jicha = p.xianhuojunjia - p.zhuliqijia;
			if( p.jicha == 0 ){
				System.out.println("数据异常");
			}
			if( pLast.jicha == 0)
				p.jichabiandong = 999;
			else
				p.jichabiandong = (p.jicha - pLast.jicha)/pLast.jicha*100.0f;
			p.chicangzongliang = pCurrent.getPosition();
			if( p.chicangzongliang == 0 ){
				System.out.println("数据异常");
			}
			if( pLast.chicangzongliang == 0 ){
				p.chicangbiandong =999;
			}
			else
				p.chicangbiandong = (p.chicangzongliang-pLast.chicangzongliang)/pLast.chicangzongliang*100.0f;
			p.chengjiaozongliang = pCurrent.getVolume();
			if( p.chengjiaozongliang == 0 ){
				System.out.println("数据异常");				
			}
			if( pLast.chengjiaozongliang == 0 ){
				p.chengjiaobiandong = 999;				
			}
			else
				p.chengjiaobiandong = (p.chengjiaozongliang-pLast.chengjiaozongliang)/pLast.chengjiaozongliang*100.0f;
			//计算资金流向:资金流向计算：持仓总量x变化x主力期价x合约数量单位,以百万元为单位			
			p.zijinliuxiang = p.chicangzongliang*p.chicangbiandong/100.0*p.zhuliqijia*MarketCrawler.TURNOVER_FACTOR.get(varid)/1000000.0;
		
				
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}
		return p;	
	}

}
