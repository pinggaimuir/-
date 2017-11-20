package cn.futures.data.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import cn.futures.data.DAO.WholesalePriceMonthlyDAO;
import cn.futures.data.entity.WholesalePriceMonthly;
import cn.futures.data.jdbc.JdbcRunner;

public class WholesalePriceMonthlyDAO {
	public static final Logger LOG = Logger.getLogger(WholesalePriceMonthlyDAO.class);
	
	public static final String CLZ_TABLES = "CX_clzpro_wholeprice_index_month";
	public static final String AGRI_TABLES = "CX_agripro_wholeprice_index_month";
	
	private boolean saveJDBC(String sql, Object[][] vals,String table){
		System.out.println(sql);
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
			sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like '"+table+"' and varid="+vals[0][0];
			jdbc.update(sql);
			return true;
		} catch (Exception e){
			LOG.error("insert data into DB error",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			} finally {
				jdbc.release();
			}
		} finally {
			jdbc.release();
		}
		return false;
	}
	
	public void save(List<WholesalePriceMonthly> list){
		if (null==list || list.size()<=0)
			return;
		String sql = "insert into TABLE (edittime, varid, timeint, 定基指数, 同比指数, 环比指数) "
				+ "VALUES(getDate(), ?, ?, ?, ?, ?)";
		Object[][] vals = new Object[list.size()][5];
		WholesalePriceMonthly priceData;
		for (int i=0 ; i<list.size() ; ++i){
			priceData = list.get(i);
			vals[i][0] = priceData.getVarid();
			vals[i][1] = priceData.getTimeInt();
			vals[i][2] = priceData.getClzFixedBase();
			vals[i][3] = priceData.getClzYearOnYear();
			vals[i][4] = priceData.getClzMonthOnMonth();
		}
		if (saveJDBC(sql.replace("TABLE", CLZ_TABLES), vals,CLZ_TABLES))
			LOG.info(CLZ_TABLES + ":" +list.size() + " prices saved ");
		
		for (int i=0 ; i<list.size() ; ++i){
			priceData = list.get(i);
			vals[i][2] = priceData.getAgriFixedBase();
			vals[i][3] = priceData.getAgriYearOnYear();
			vals[i][4] = priceData.getAgriMonthOnMonth();
		}
		if (saveJDBC(sql.replace("TABLE", AGRI_TABLES), vals, AGRI_TABLES))
			LOG.info(AGRI_TABLES + ":" +list.size() + " prices saved ");
	}
	
	public static void main(String[] args) {
//		System.out.println(DAOUtils.getNewestTimeInt(CLZ_TABLES));
	}
}
