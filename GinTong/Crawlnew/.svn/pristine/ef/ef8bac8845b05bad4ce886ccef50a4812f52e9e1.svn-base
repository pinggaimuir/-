package cn.futures.data.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;

/**
 * 
 * 功能：获得数据库的基本资源 使用：在JdbcRunner中使用
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
		//每次获取数据库连接时都输出当前连接池状态: 已用连接数 - 空闲连接数（相对于初始化连接数而非最大连接数）
//		LOG.info("DataSource status: " + bds.getNumActive() + " - " + bds.getNumIdle());
	}

	/**
	 * 获得一个数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		printDataSourceStats(ds);
		return ds.getConnection();
	}

}