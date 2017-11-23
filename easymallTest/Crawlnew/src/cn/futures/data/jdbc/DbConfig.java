package cn.futures.data.jdbc;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * ���ܣ�������ݿ�Ļ�����Դ ʹ�ã���JdbcRunner��ʹ��
 * 
 */

public class DbConfig {

	private static DataSource ds = null;
	private static DataSource dsForHour = null;
	private static final Logger LOG = Logger.getLogger(DbConfig.class);
	
	private DbConfig(){		
	}

	static {
		try {
			Properties prop = new Properties();
			InputStream is = DbConfig.class.getClassLoader()
					.getResourceAsStream("cn/futures/data/jdbc/dbcpBricData.properties");
			prop.load(is);
			ds = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	public static void printDataSourceStats(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		//ÿ�λ�ȡ���ݿ�����ʱ�������ǰ���ӳ�״̬: ���������� - ����������������ڳ�ʼ�����������������������
//		LOG.info("DataSource status: " + bds.getNumActive() + " - " + bds.getNumIdle());
	}

	/**
	 * ���һ�����ݿ�����
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		printDataSourceStats(ds);
		return ds.getConnection();
	}

}