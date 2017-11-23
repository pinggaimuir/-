package cn.futures.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.FileStrIO;

/**
 * 批量更新负责人
 * @author bric_yangyulin
 * @date 2017-05-31
 * */
public class UpdateDuty {
	private static final Logger LOG = Logger.getLogger(UpdateDuty.class);
	private Map<String, Integer> dbm3User = null;
	private List<Object[]> upParams = null;
	private String updateSql = null;
	private String checkSql = null;//用来检查表是否存在的sql
	private UpdateDuty(){
		dbm3User = new HashMap<String, Integer>();
		upParams = new LinkedList<Object[]>();
		updateSql = "UPDATE CFG_TABLE_META_NEW SET dutyId = ? FROM CFG_TABLE_META_NEW meta "
				+ "LEFT JOIN CFT_TABLE_INDUSTRY ind on meta.industryId = ind.id "
				+ "LEFT JOIN CFT_TABLE_INDUSTRY_DETAIL_NEW detail ON meta.industryDetailId = detail.id "
				+ "LEFT JOIN CX_Variety_new variety on meta.varId = variety.id "
				+ "WHERE ind.name = ? AND detail.name = ? AND variety.VName = ? AND cnName = ?";
		String querySql = "SELECT id, name from CFG_security_user_new";
		dbm3User = queryUser(querySql);
		checkSql = "select count(meta.id) count FROM CFG_TABLE_META_NEW meta "
				+ "LEFT JOIN CFT_TABLE_INDUSTRY ind on meta.industryId = ind.id "
				+ "LEFT JOIN CFT_TABLE_INDUSTRY_DETAIL_NEW detail ON meta.industryDetailId = detail.id "
				+ "LEFT JOIN CX_Variety_new variety on meta.varId = variety.id "
				+ "WHERE ind.name = ? AND detail.name = ? AND variety.VName = ? AND cnName = ?";
	}
	/**
	 * 解析数据
	 * */
	private void parseData(String filePath, String sheetName){
		String[][] table = FileStrIO.readXls(filePath, sheetName);
		int[] slcCols = {6, 0, 1, 2, 3};
		for(int rowId = 1; rowId < table.length; rowId++){
			if(table[rowId][slcCols[0]] == null || table[rowId][slcCols[1]] == null || 
			table[rowId][slcCols[2]] == null || table[rowId][slcCols[3]] == null ||
			table[rowId][slcCols[4]] == null){
				LOG.info("第" + (rowId + 1) + "行有空值。");
				continue;
			}
			if(dbm3User.get(table[rowId][slcCols[0]]) == null){
				LOG.info("第" + (rowId + 1) + "行有负责人名称与dbm3用户姓名不一致。");
				continue;
			}
			Object[] param = {dbm3User.get(table[rowId][slcCols[0]].trim()), table[rowId][slcCols[1]], 
					table[rowId][slcCols[2]], table[rowId][slcCols[3]], 
					table[rowId][slcCols[4]]};
			upParams.add(param);
		}
	}
	/**
	 * 更新负责人
	 * */
	private void updateDuty(){
		if(!upParams.isEmpty()){
			for(Object[] param: upParams){
				update(updateSql, param);
			}
		} else {
			LOG.info("无需更新内容。");
		}
	}
	private void check(){
		LOG.info("------------------------------");
		if(!upParams.isEmpty()){
			for(Object[] param: upParams){
				int count = queryCount(checkSql, new Object[]{param[1], param[2], param[3], param[4]});
				if(count == 0){
					LOG.info("内容无效：" + param[1] + "-" + param[2] + "-" + param[3] + "-" + param[4]);
				}
			}
		} else {
			LOG.info("无需更新内容。");
		}
		LOG.info("------------------------------");
	}
	/**
	 * 更新数据
	 * */
	private void update(String sql, Object[] params){
//		LOG.info("更新：" + sql);
		if(params != null){
			String parStr = "";
			for(Object param: params){
				parStr += param;
				parStr += "-";
			}
			LOG.info(parStr);
		}
		JdbcRunner jdbc = null;
		try{
			jdbc = new JdbcRunner();
			int updateRslt = jdbc.update2(sql, params);
			LOG.info("updateRslt:" + updateRslt);
		} catch(Exception e) {
			LOG.error("更新数据时异常。",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			}
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
	}
	/**
	 * 查询总条数
	 * */
	public int queryCount(String sql, Object[] params){
		JdbcRunner jdbc = null;
		ResultSet rs = null;
		int count = 0;
		try{
			jdbc = new JdbcRunner();
			rs = jdbc.query(sql, params);
			if(rs.next()){
				count = rs.getInt("count");
			}
		} catch(Exception e) {
			LOG.error("查询时发生异常。",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			}
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
		return count;
	}
	/**
	 * 查询一条
	 * @param sql prepared sql
	 * @param params 参数
	 * */
	private Map<String, Integer> queryUser(String sql){
		Map<String, Integer> rslt = new HashMap<String, Integer>();
		JdbcRunner jdbc = null;
		try{
			jdbc = new JdbcRunner();
			ResultSet rs = jdbc.query(sql);
			while(rs.next()){
				Integer dutyId = rs.getInt("id");
				String dutyName = rs.getString("name");
				rslt.put(dutyName, dutyId);
			}
		} catch(Exception e) {
			LOG.error(e);
		} finally {
			if(jdbc != null){
				jdbc.release();
			}
		}
		return rslt;
	}
	public static void main(String[] args) {
		UpdateDuty up = new UpdateDuty();
		LOG.info("--------------------------------------------");
		up.parseData("E:\\Space\\wugui\\数据表修改信息记录\\数据管理汇总表-20170616_李喜明.xlsx", "新增");
//		up.check();
		up.updateDuty();
	}
}
