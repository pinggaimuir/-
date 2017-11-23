package com.bric.analyser;

import java.sql.SQLException;

import com.bric.crawler.MapInit;
import com.bric.jdbc.JdbcRunner;

public class wholePriceAnalyse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new wholePriceAnalyse().analyseWholePrice("20140808","22001","CX_21FoodPrice_Beans","CX_fruits_wholes_greenbean","303");
	}
	
	/*
	 * 对抓取下来的批发市场的数据进行汇总分析，计算全国各省的平均价后存入一个新表，依赖于一个存储过程（保存在数据库服务器上）
	 */
	public int analyseWholePrice(String timeint,String var,String table1,String table2,String varDB)
	{
		System.out.println(timeint+","+var+","+table1+","+table2);
		if(timeint==null)
			return 0;
		if(var == null)
			return 0;
		if(table1==null)
			return 0;
		if(table2==null)
			return 0;
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.execProc("cal_fruits_wholes_greenbean","'"+timeint+"'","'"+var+"'","'"+table1+"'","'"+table2+"'","'"+varDB+"'");
			jdbc.endTransaction();
			return 1;
		} catch (Exception e){
			System.out.println("analyse greenbean whole price error");
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
		return 0;
	}

}
