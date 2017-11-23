//package cn.futures.data.DAO;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import cn.futures.data.DAO.WholesalePriceDAO;
//import cn.futures.data.entity.WholesalePrice;
//import cn.futures.data.jdbc.JdbcRunner;
//
//public class WholesalePriceDAO {
//	public static final Logger LOG = Logger.getLogger(WholesalePriceDAO.class);
//
//	public static final String CLZ_TABLES = "CX_clz_prod_wholesale_price_index";
//	public static final String AGRI_TABLES = "CX_agri_prod_wholesale_prices_totalindex";
//
//	private boolean saveJDBC(String sql, Object[][] vals,String table){
//		System.out.println(sql);
//		JdbcRunner jdbc = null;
//		try {
//			jdbc = new JdbcRunner();
//			jdbc.beginTransaction();
//			jdbc.batchUpdate2(sql, vals);
//			jdbc.endTransaction();
//			sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like '"+table+"' and varid="+vals[0][0];
//			jdbc.update(sql);
//			return true;
//		} catch (Exception e){
//			LOG.error("insert data into DB error",e);
//			try {
//				jdbc.rollTransaction();
//			} catch (SQLException e1) {
//				LOG.error("DB Transaction rollback error",e1);
//			} finally {
//				jdbc.release();
//			}
//		} finally {
//			jdbc.release();
//		}
//		return false;
//	}
//
//	public void save(List<WholesalePrice> list){
//		if (null==list || list.size()<=0)
//			return;
//		String sql = "insert into TABLE (edittime, varid, timeint, 全国) VALUES(getDate(), ?, ?, ?)";
//		Object[][] vals = new Object[list.size()][3];
//		for (int i=0 ; i<list.size() ; ++i){
//			WholesalePrice priceData = list.get(i);
//			vals[i][0] = priceData.getVarid();
//			vals[i][1] = priceData.getTimeInt();
//			vals[i][2] = priceData.getClzPriceIndex();
//		}
//		if (saveJDBC(sql.replace("TABLE", CLZ_TABLES), vals, CLZ_TABLES))
//			LOG.info(CLZ_TABLES + ":" +list.size() + " prices saved ");
//
//		for (int i=0 ; i<list.size() ; ++i)
//			vals[i][2] = list.get(i).getAgriPriceIndex();
//		if (saveJDBC(sql.replace("TABLE", AGRI_TABLES), vals, AGRI_TABLES))
//			LOG.info(AGRI_TABLES + ":" +list.size() + " prices saved ");
//	}
//
//	public static void main(String[] args) {
////		System.out.println(DAOUtils.getNewestTimeInt(CLZ_TABLES));
//	}
//}
