package com.bric.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.bric.util.Constants;

/**
 * 
 * 功能：DAO层的直接操作对象
 * 
 */
public class JdbcRunner {

	private Connection conn = null;

	private Statement stmt = null;

	private PreparedStatement ps = null;

	private ResultSet rs = null;

	private static Logger logger = Logger.getLogger(JdbcRunner.class);

	/**
	 * 
	 * @throws SQLException
	 */
	public JdbcRunner() {
		try {

			// chengsheng
			conn = DbConfig.getConnection();

			/** 到这里 */
			/*
			 * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver") ; }
			 * catch(ClassNotFoundException e) {
			 * System.out.println("加载驱动程序时出错!") ; e.printStackTrace() ; } try {
			 * conn = DriverManager.getConnection("jdbc:odbc:accfutures") ;
			 */
			/** 到这个里都 **/
		} catch (Exception e) {
			System.out.println("系统不能获得连接");
			logger.error("Can not get a connection from DB");
			release();
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return conn;
	}

	/**
	 * 更新数据库，直接用SQL语句
	 * 
	 * @param sqlClause
	 *            SQL语句
	 * @throws SQLException
	 *             如果发生异常，抛出
	 * @return 更新的记录数
	 */
	public int update(String sqlClause) throws Exception {
		try {
			if (stmt == null)
				stmt = conn.createStatement();
			return Constants.updateAllowed ? stmt.executeUpdate(sqlClause) : 1;
		} catch (Exception e) {
			System.out.println("更新异常：" + sqlClause);
			e.printStackTrace();
			release();
			throw e;
		}
		// release it in dao
		// finally {
		// release();
		// }
	}

	public int execProc(String procName) throws Exception {

		try {
			CallableStatement c = conn.prepareCall("{call " + procName + "}");
			if(Constants.updateAllowed){
				c.execute();	
			}
			c.close();
			return 1;
		} catch (Exception e) {
			System.out.println("batch update 异常");
			logger.error("batch update 异常", e);
			e.printStackTrace();
			release();
			throw e;
		}

	}

	public int execProc(String procName, String...params) throws Exception {

		try {
			String sql = "{call "+procName+" ";			
			for (int i=0;i<params.length;i++) {	
				if(i==0)
					sql+=params[i];
				else
					sql+=","+params[i];
			}
			sql+=" }";
			System.out.println(sql);
			CallableStatement c = conn.prepareCall(sql);
			if(Constants.updateAllowed){
				c.execute();	
			}
			c.close();
			return 1;
		} catch (Exception e) {
			System.out.println("batch update 异常");
			logger.error("batch update 异常", e);
			e.printStackTrace();
			release();
			throw e;
		}

	}

	public int batchUpdate(String sqlClause, Object[][] vals) throws Exception {
		try {
			this.ps = this.conn.prepareStatement(sqlClause);
			if (vals != null) {
				for (Object[] row : vals) {
					for (int i = 0; i < row.length; i++) {
						if (row[i] != null)
							this.ps.setObject(i + 1, row[i]);
						else {
							this.ps.setNull(i + 1, 0);
						}
						// System.out.println("for DB test1"+row[i]);
//						logger.error("for DB test1" + row[i]);
					}
					this.ps.addBatch();
				}
			}
			if(Constants.updateAllowed){
				this.ps.executeBatch();	
			}
			return 1;
		} catch (Exception e) {
			System.out.println("batch update 异常");
			logger.error("batch update 异常", e);
			e.printStackTrace();
			release();
			throw e;
		}
	}

	/**
	 * 查询数据库
	 * 
	 * @param sqlClause
	 *            语句
	 * @return ResultSet 如果成功返回ResultSet
	 * @throws SQLException
	 *             如果发生异常，抛出
	 */
	public ResultSet query(String sqlClause) throws Exception {
		try {
			if (stmt == null)
				stmt = conn.createStatement(1004, 1008);
			rs = stmt.executeQuery(sqlClause);
			return rs;
		} catch (Exception e) {
			System.out.println("查询异常" + sqlClause);
			logger.error("查询异常" + sqlClause, e);
			e.printStackTrace();
			release();
			throw e;
		}
	}

	/**
	 * 用PreparedStatement 查询数据库
	 * 
	 * @param sqlClause
	 *            语句
	 * @param vals
	 *            数组赋值
	 * @return ResultSet 结果
	 * @throws SQLException
	 */
	public ResultSet query(String sqlClause, Object[] vals) throws Exception {
		try {
			ps = conn.prepareStatement(sqlClause);
			if (vals != null) {
				int size = vals.length;
				for (int i = 0; i < size; i++) {
					if (vals[i] != null) {
						ps.setObject((i + 1), vals[i]);
					} else {
						ps.setNull((i + 1), Types.NULL);

					}
				}
			}
			rs = ps.executeQuery();
			return rs;
		} catch (Exception e) {
			System.out.println("查询异常" + sqlClause);
			logger.error("查询异常" + sqlClause, e);
			e.printStackTrace();
			release();
			throw e;
		}
	}

	/**
	 * 用PreparedStatement 查询数据库
	 * 
	 * @param sqlClause
	 *            sql语句
	 * @param vals
	 *            数组赋值
	 * @param valNumber
	 *            数组元素的个数
	 * @return ResultSet 结果
	 * @throws SQLException
	 */
	public ResultSet query(String sqlClause, Object[] vals, int valNumber)
			throws SQLException {
		try {
			if (ps != null)
				ps.clearParameters();
			ps = conn.prepareStatement(sqlClause);
			if (vals != null) {
				int size = vals.length;
				for (int i = 0; i < size && i < valNumber; i++) {
					if (vals[i] != null) {
						ps.setObject((i + 1), vals[i]);
					} else {
						ps.setNull((i + 1), Types.NULL);
					}
				}
			}

			try {
				rs = ps.executeQuery();
			} catch (SQLException sqle) {
				release();
				throw sqle;
			}
			return rs;
		} catch (SQLException e) {
			release();
			throw e;
		}
	}

	/**
	 * 功能：更新数据库，用PreparedStatement语句，利用数组赋参数值
	 * 
	 * @param sqlClause
	 *            语句
	 * @param vals
	 *            数组赋值
	 * @return int 更新的记录数
	 * @throws SQLException
	 */
	public int update(String sqlClause, Object[] vals) throws Exception {
		try {
			ps = conn.prepareStatement(sqlClause);
			if (vals != null) {
				int size = vals.length;
				for (int i = 0; i < size; i++) {
					if (vals[i] != null) {
						ps.setObject((i + 1), vals[i]);
					} else {
						ps.setNull((i + 1), Types.NULL);
					}
				}
			}
			int rslt = 0;
			if(Constants.updateAllowed){
				rslt =  ps.executeUpdate();	
			}
			return rslt;
		} catch (Exception e) {
			System.out.println("update 异常");
			logger.error("update 异常", e);
			e.printStackTrace();
			release();
			throw e;
		}
		// release it in dao
		// finally {
		// release();
		// }

	}

	/**
	 * 功能：更新数据库，用PreparedStatement语句，利用数组赋参数值
	 * 
	 * @param sqlClause
	 *            语句
	 * @param vals
	 *            数组赋值
	 * @param valNumber
	 *            数组元素的个数
	 * @return int 更新的记录数
	 * @throws SQLException
	 */
	public int update(String sqlClause, Object[] vals, int valNumber)
			throws Exception {
		try {
			ps = conn.prepareStatement(sqlClause);

			if (vals != null) {
				int size = vals.length;
				for (int i = 0; i < size && i < valNumber; i++) {
					if (vals[i] != null) {
						ps.setString((i + 1), (String) vals[i]);
					} else {
						ps.setNull((i + 1), Types.NULL);
					}
				}
			}
			int rslt = 0;
			if(Constants.updateAllowed){
				rslt = ps.executeUpdate();
			}
			return rslt;
		} catch (Exception e) {
			logger.error("update 异常", e);
			e.printStackTrace();
			release();
			throw e;
		}

	}

	/**
	 * 批量保存到数据库中
	 * 
	 * @param sql
	 *            放的是一个个sql插入语句。sql.get(i)='insert into(username,password)
	 *            values('程悦','123456')'; return 0 表示正确执行没有错误 return -1
	 *            表示的没有插入成功，执行了数据回滚
	 * */
	public int save(List<String> sql) {
		int flag = 0;
		if(Constants.updateAllowed){
			return flag;
		}
		try {
			// 启动事务
			conn.setAutoCommit(false);
			// SQL操作
			try {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (int i = 0; i < sql.size(); i++) {
					stmt.execute(sql.get(i));
				}
				// stmt.executeUpdate(sql);

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("插入数据时候异常这个时候我们");
				logger.error("插入数据时候异常", e);

				return -1;
			}

			// 提交事务
			conn.commit();
		} catch (SQLException e) {

			try {
				conn.rollback();
				System.out.println("回滚了事物！");
			} catch (SQLException e1) {
				System.out.println("回滚异常！");
				logger.error("回滚异常！", e);
				e1.printStackTrace();
			}
		}
		return flag;

	}

	/* 返回DatabaseMetaData对象 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	/**
	 * 开始一个事务
	 * 
	 * @throws SQLException
	 *             事务开始失败返回异常
	 */
	public void beginTransaction() throws SQLException {
		// try {
		// conn.setAutoCommit(false);
		// } catch (SQLException e) {
		// release();
		// throw e;
		// }
	}

	/**
	 * 结束一个事务
	 * 
	 * @throws SQLException
	 *             事务提交失败返回异常
	 */
	public void endTransaction() throws SQLException {
		// try {
		// conn.commit();
		// conn.setAutoCommit(true);
		// } catch (SQLException e) {
		// release();
		// throw e;
		// }
	}

	public void rollTransaction() throws SQLException {
		// try {
		// conn.rollback();
		// conn.setAutoCommit(true);
		// } catch (SQLException e) {
		// release();
		// throw e;
		// }
	}

	/**
	 * 释放所有的资源,使用完此类后，必须调用此方法，释放所有的资源。
	 */
	public void release() {
		try {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.ps != null) {
					this.ps.close();
					this.ps = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (this.stmt != null) {
						this.stmt.close();
						this.stmt = null;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (this.conn != null) {
							this.conn.close();
							this.conn = null;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}