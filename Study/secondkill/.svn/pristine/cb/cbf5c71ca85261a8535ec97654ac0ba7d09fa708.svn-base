package cn.futures.data.DAO;

import cn.futures.data.entity.FvFirModel;
import cn.futures.data.jdbc.JdbcRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("fvFirDao")
public class FvFirDao {
	private static final Log logger = LogFactory.getLog(FvFirDao.class);
	public List<FvFirModel> getTableList(String varId1,String varId2) throws Exception{		
		String sql = "select cnName,dbName from CFG_TABLE_META_NEW where VarId in ("+varId1+","+varId2+")";
		logger.info(sql);	
		List<FvFirModel> list = new ArrayList<FvFirModel>();
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			ResultSet rs = jdbc.query(sql);
			while (rs.next()){
				FvFirModel p = new FvFirModel();
				p.setCnName(rs.getString("cnName"));
				p.setDbName(rs.getString("dbName"));				
				list.add(p);
			}
				
		} catch (Exception e){
			logger.error("",e);
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}	
		return list;
	}

	
	public void save(String tableName,int varId,int timeInt,Double minPrice,Double maxPrice,
			Double avgPrice,Double dealPrice,Double dealNum,Double localNum,Double outNum) {
		String sql = "insert into "+tableName+" (EditTime,VarId,TimeInt,最低批发价,最高批发价,平均批发价,成交额,"
				+ "成交量,本市量,外埠量) values (sysdate(),?,?,?,?,?,?,?,?,?)";
		logger.info(sql);
		Object[][] vals = new Object[1][9];
		for (int i = 0; i < 1; i++){
			vals[0][0] = varId;
			vals[0][1] = timeInt;
			vals[0][2] = minPrice;
			vals[0][3] = maxPrice;
			vals[0][4] = avgPrice;
			vals[0][5] = dealPrice;
			vals[0][6] = dealNum;
			vals[0][7] = localNum;
			vals[0][8] = outNum;
		}
		
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
			sql = "update CFG_TABLE_META_NEW set dataUpdateTime=sysdate() where dbName like '"+tableName+"' and varid="+varId;
			jdbc.update(sql);
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
		logger.info( " prices saved ");		
		
	}
	
}
