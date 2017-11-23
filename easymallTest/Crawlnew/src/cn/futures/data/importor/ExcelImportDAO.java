package cn.futures.data.importor;

import cn.futures.data.jdbc.JdbcRunner;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ExcelImportDAO {

	public static Logger LOG = Logger.getLogger(ExcelImportDAO.class);

	public int count(String table, int timeint, int varid) {
		String sql = "select count(1) from " + table + " where timeint="
				+ timeint + " and varid=" + varid;
		LOG.debug(sql);

		int count = 0;
		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		} finally {
			db.release();
		}
		return count;
	}

	public void insert(String table, int timeint, int varid,
			List<String> fields, String[] values) {

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("INSERT INTO ").append(table)
				.append(" (EditTime, varid ");
		for (String field : fields) {
			sqlBuilder.append(", ").append(field);
		}
		sqlBuilder.append(") VALUES ( sysdate(), ").append(varid);
		for (String field : fields) {
			sqlBuilder.append(", ?");
		}
		sqlBuilder.append(")");

		String sql = sqlBuilder.toString();
		LOG.debug(sql);

		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			db.update2(sql, values);
			sql = "update CFG_TABLE_META_NEW  set dataUpdateTime=sysdate() where dbName like '"+table+"' and varid="+varid;
			db.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		} finally {
			db.release();
		}

	}

	public void update(String table, int timeint, int varid,
			List<String> fields, String[] values) {

		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("UPDATE ").append(table)
				.append(" SET EditTime=sysdate() ");
		for (String field : fields) {
			sqlBuilder.append(", ").append(field).append("=?");
		}
		sqlBuilder.append(" where timeint=").append(timeint)
				.append(" and varid=").append(varid);

		String sql = sqlBuilder.toString();
		LOG.debug(sql);

		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			db.update2(sql, values);
			sql = "update CFG_TABLE_META_NEW  set dataUpdateTime=sysdate() where dbName like '"+table+"' and varid="+varid;
			db.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		} finally {
			db.release();
		}

	}

	/**
	 * 查询数据库中，指定数据表和品种的最新数据
	 * 
	 * @param table
	 * @param varid
	 * @return
	 */
	public ExcelDataset findRecentData(String table, int varid) {
		ExcelDataset data = null;
		int topnum = 10;
		String sql = "select * from " + table + " where varid=" + varid
				+ " order by EditTime desc, id desc limit " + topnum ;
		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			ResultSet rs = db.query(sql);
			
			
			// 获得列名
			ResultSetMetaData meta = rs.getMetaData();
			int columnCount = meta.getColumnCount();
			List<String> fields = new ArrayList<String>(columnCount);
			for (int i = 0; i < columnCount; i++){
				fields.add(meta.getColumnName(i+1));
			}
			//System.out.println(fields);
			
			// 获得值
			List<String[]> values = new ArrayList<String[]>(topnum);
			while (rs.next()){
				String[] row = new String[columnCount];
				for (int j = 0; j < columnCount; j++){
					String v = rs.getString(j+1);
					row[j] = (v != null) ? v : "";
				}
				//System.out.println(Arrays.asList(row));
				values.add(row);
			}
			
			data = new ExcelDataset();
			data.setFields(fields);
			data.setValues(values);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		} finally {
			db.release();
		}
		return data;

	}
	
}
