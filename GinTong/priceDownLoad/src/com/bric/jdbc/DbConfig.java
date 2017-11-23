package com.bric.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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
	private static Logger logger = Logger.getLogger(DbConfig.class);
	
	private DbConfig(){		
	}

	static {
		try {
			Properties prop = new Properties();
			InputStream is = DbConfig.class.getClassLoader()
					.getResourceAsStream("com/bric/jdbc/dbcpBricData.properties");
			prop.load(is);
			ds = BasicDataSourceFactory.createDataSource(prop);
		} catch (Exception e) {
			logger.error("Error While Initting DbConfig", e);
		}
	}

	public static void printDataSourceStats(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
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