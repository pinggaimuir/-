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
 * ���ܣ�DAO���ֱ�Ӳ�������
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

			/** ������ */
			/*
			 * Class.forName("sun.jdbc.odbc.JdbcOdbcDriver") ; }
			 * catch(ClassNotFoundException e) {
			 * System.out.println("������������ʱ����!") ; e.printStackTrace() ; } try {
			 * conn = DriverManager.getConnection("jdbc:odbc:accfutures") ;
			 */
			/** ������ﶼ **/
		} catch (Exception e) {
			System.out.println("ϵͳ���ܻ������");
			logger.error("Can not get a connection from DB");
			release();
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return conn;
	}

	/**
	 * �������ݿ⣬ֱ����SQL���
	 * 
	 * @param sqlClause
	 *            SQL���
	 * @throws SQLException
	 *             ��������쳣���׳�
	 * @return ���µļ�¼��
	 */
	public int update(String sqlClause) throws Exception {
		try {
			if (stmt == null)
				stmt = conn.createStatement();
			return Constants.updateAllowed ? stmt.executeUpdate(sqlClause) : 1;
		} catch (Exception e) {
			System.out.println("�����쳣��" + sqlClause);
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
			System.out.println("batch update �쳣");
			logger.error("batch update �쳣", e);
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
			System.out.println("batch update �쳣");
			logger.error("batch update �쳣", e);
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
			System.out.println("batch update �쳣");
			logger.error("batch update �쳣", e);
			e.printStackTrace();
			release();
			throw e;
		}
	}

	/**
	 * ��ѯ���ݿ�
	 * 
	 * @param sqlClause
	 *            ���
	 * @return ResultSet ����ɹ�����ResultSet
	 * @throws SQLException
	 *             ��������쳣���׳�
	 */
	public ResultSet query(String sqlClause) throws Exception {
		try {
			if (stmt == null)
				stmt = conn.createStatement(1004, 1008);
			rs = stmt.executeQuery(sqlClause);
			return rs;
		} catch (Exception e) {
			System.out.println("��ѯ�쳣" + sqlClause);
			logger.error("��ѯ�쳣" + sqlClause, e);
			e.printStackTrace();
			release();
			throw e;
		}
	}

	/**
	 * ��PreparedStatement ��ѯ���ݿ�
	 * 
	 * @param sqlClause
	 *            ���
	 * @param vals
	 *            ���鸳ֵ
	 * @return ResultSet ���
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
			System.out.println("��ѯ�쳣" + sqlClause);
			logger.error("��ѯ�쳣" + sqlClause, e);
			e.printStackTrace();
			release();
			throw e;
		}
	}

	/**
	 * ��PreparedStatement ��ѯ���ݿ�
	 * 
	 * @param sqlClause
	 *            sql���
	 * @param vals
	 *            ���鸳ֵ
	 * @param valNumber
	 *            ����Ԫ�صĸ���
	 * @return ResultSet ���
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
	 * ���ܣ��������ݿ⣬��PreparedStatement��䣬�������鸳����ֵ
	 * 
	 * @param sqlClause
	 *            ���
	 * @param vals
	 *            ���鸳ֵ
	 * @return int ���µļ�¼��
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
			System.out.println("update �쳣");
			logger.error("update �쳣", e);
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
	 * ���ܣ��������ݿ⣬��PreparedStatement��䣬�������鸳����ֵ
	 * 
	 * @param sqlClause
	 *            ���
	 * @param vals
	 *            ���鸳ֵ
	 * @param valNumber
	 *            ����Ԫ�صĸ���
	 * @return int ���µļ�¼��
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
			logger.error("update �쳣", e);
			e.printStackTrace();
			release();
			throw e;
		}

	}

	/**
	 * �������浽���ݿ���
	 * 
	 * @param sql
	 *            �ŵ���һ����sql������䡣sql.get(i)='insert into(username,password)
	 *            values('����','123456')'; return 0 ��ʾ��ȷִ��û�д��� return -1
	 *            ��ʾ��û�в���ɹ���ִ�������ݻع�
	 * */
	public int save(List<String> sql) {
		int flag = 0;
		if(Constants.updateAllowed){
			return flag;
		}
		try {
			// ��������
			conn.setAutoCommit(false);
			// SQL����
			try {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (int i = 0; i < sql.size(); i++) {
					stmt.execute(sql.get(i));
				}
				// stmt.executeUpdate(sql);

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("��������ʱ���쳣���ʱ������");
				logger.error("��������ʱ���쳣", e);

				return -1;
			}

			// �ύ����
			conn.commit();
		} catch (SQLException e) {

			try {
				conn.rollback();
				System.out.println("�ع������");
			} catch (SQLException e1) {
				System.out.println("�ع��쳣��");
				logger.error("�ع��쳣��", e);
				e1.printStackTrace();
			}
		}
		return flag;

	}

	/* ����DatabaseMetaData���� */
	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	/**
	 * ��ʼһ������
	 * 
	 * @throws SQLException
	 *             ����ʼʧ�ܷ����쳣
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
	 * ����һ������
	 * 
	 * @throws SQLException
	 *             �����ύʧ�ܷ����쳣
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
	 * �ͷ����е���Դ,ʹ�������󣬱�����ô˷������ͷ����е���Դ��
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