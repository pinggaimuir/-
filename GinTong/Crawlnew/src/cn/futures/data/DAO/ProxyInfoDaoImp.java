package cn.futures.data.DAO;

import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.futures.data.entity.ProxyInfo;
import cn.futures.data.jdbc.JdbcRunner;

public class ProxyInfoDaoImp implements ProxyInfoDao{
	private Logger log = Logger.getLogger(ProxyInfoDaoImp.class);
	
	/**
	 * 从文件中提取的代理ip保存即初次保存该ip时使用。
	 * */
	public boolean saveProxyInfo(ProxyInfo proxyInfo){
		boolean flag = false;
		
		JdbcRunner jdbc = null;
		
		String sql = "insert into ProxyInfo values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[][] vals = new Object[1][11];

		vals[0][0] = proxyInfo.getProxy();
		vals[0][1] = proxyInfo.getSuccessTimes();
		vals[0][2] = proxyInfo.getFailTimes();
		vals[0][3] = proxyInfo.getFindTime();
		vals[0][4] = proxyInfo.getLatestSuccessTime();
		vals[0][5] = proxyInfo.getLatestFailTime();
		vals[0][6] = proxyInfo.getUseful();
		vals[0][7] = proxyInfo.getLatelyStatus();
		vals[0][8] = proxyInfo.getLatelyStatusTimes();
		vals[0][9] = proxyInfo.getSuccessRate();
		vals[0][10] = proxyInfo.getProxySource();
		try {
			jdbc = new JdbcRunner();//new该对象时会获取数据库连接，所以要将该语句放在try块中，以确保一定会执行下面释放连接的语句。
			jdbc.beginTransaction();
			try{
				jdbc.batchUpdate2(sql, vals);
			} catch(BatchUpdateException e) {
				log.warn(e.getMessage());
			}
			jdbc.endTransaction();
		} catch (Exception e) {
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				log.error(e1.getMessage());	
			}
			log.error(e.getMessage());
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}
		flag = true;
		
		return flag;
	}
	
	/**
	 * 批量存储ProxyInfo对象（未使用，因常会因某个重复代理导致该次批量导入失败）。
	 * */
	public boolean saveBatch(List<ProxyInfo> proxyInfos){
		boolean flag = false;
		
		JdbcRunner jdbc = null;
		
		String sql = "insert into ProxyInfo values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[][] vals = new Object[proxyInfos.size()][11];
		int i = 0;
		for(ProxyInfo proxyInfo: proxyInfos){

			vals[i][0] = proxyInfo.getProxy();
			vals[i][1] = proxyInfo.getSuccessTimes();
			vals[i][2] = proxyInfo.getFailTimes();
			vals[i][3] = proxyInfo.getFindTime();
			vals[i][4] = proxyInfo.getLatestSuccessTime();
			vals[i][5] = proxyInfo.getLatestFailTime();
			vals[i][6] = proxyInfo.getUseful();
			vals[i][7] = proxyInfo.getLatelyStatus();
			vals[i][8] = proxyInfo.getLatelyStatusTimes();
			vals[i][9] = proxyInfo.getSuccessRate();
			vals[i][10] = proxyInfo.getProxySource();
			i++;
		}
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
		} catch (Exception e) {
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				log.error(e1.getMessage());	
			}
			log.error(e.getMessage());
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}
		flag = true;
		
		return flag;
	}
	
	/**
	 * 批量更新数据库中存储的ProxyInfo数据，对数据库中代理ip进行定时检测时使用。
	 * */
	public boolean updateBatch(List<ProxyInfo> proxyInfos){
		boolean flag = false;
		
		JdbcRunner jdbc = null;
		
		String sql = "update ProxyInfo set success_times = ?, fail_times = ?, latest_success_time = ?, latest_fail_time = ?, "
				+ "lately_status = ?, lately_status_times = ?, success_rate = ?  "
				+ "where proxy = ?";
		Object[][] vals = new Object[proxyInfos.size()][8];
		int i = 0;
		for(ProxyInfo proxyInfo: proxyInfos){
			vals[i][0] = proxyInfo.getSuccessTimes();
			vals[i][1] = proxyInfo.getFailTimes();
			vals[i][2] = proxyInfo.getLatestSuccessTime();
			vals[i][3] = proxyInfo.getLatestFailTime();
			vals[i][4] = proxyInfo.getLatelyStatus();
			vals[i][5] = proxyInfo.getLatelyStatusTimes();
			vals[i][6] = proxyInfo.getSuccessRate();
			vals[i][7] = proxyInfo.getProxy();
			i++;
		}
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
		} catch (Exception e) {
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				log.error(e1.getMessage());	
			}
			log.error(e.getMessage());
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
		}
		flag = true;
		
		return flag;
	}
	
	/**
	 * 查询时使用
	 * */
	public List<ProxyInfo> queryAllProxyInfo(){
		List<ProxyInfo> proxyInfos = new LinkedList<ProxyInfo>();//存放查到的代理ip实体。
		JdbcRunner jdbc = null;
		String sql = "select * from ProxyInfo";
		ResultSet rs = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			rs = jdbc.query(sql);
			jdbc.endTransaction();
			while(rs.next()){
				//注意结果集的index是从1开始的。
				ProxyInfo proxyInfo = new ProxyInfo();
				proxyInfo.setProxy(rs.getString(2));
				proxyInfo.setSuccessTimes(rs.getInt(3));
				proxyInfo.setFailTimes(rs.getInt(4));
				proxyInfo.setFindTime(rs.getString(5));
				proxyInfo.setLatestSuccessTime(rs.getString(6));
				proxyInfo.setLatestFailTime(rs.getString(7));
				proxyInfo.setUseful(rs.getString(8));
				proxyInfo.setLatelyStatus(rs.getString(9));
				proxyInfo.setLatelyStatusTimes(rs.getInt(10));
				proxyInfo.setSuccessRate(rs.getFloat(11));
				proxyInfo.setProxySource(rs.getString(12));
				
				proxyInfos.add(proxyInfo);
			}
			log.error("dao:proxyInfos:" + proxyInfos.size());
		} catch (Exception e) {
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				log.error(e1.getMessage());	
			}
			log.error(e.getMessage());
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
			try {
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return proxyInfos;
	}
	
	public List<Object[]> queryBySql(String sql, int columnSize){
		List<Object[]> result = new ArrayList<Object[]>();
		JdbcRunner jdbc = null;
		ResultSet rs = null;
		try {
			jdbc = new JdbcRunner();
			rs = jdbc.query(sql.toString());
			while(rs.next()){
				//注意结果集的index是从1开始的。
				Object[] column = new Object[columnSize];
				for(int j = 1; j <= columnSize; j++){
					column[j - 1] = rs.getObject(j);
				}
				result.add(column);
			}
		} catch (Exception e){
			log.error("select error ",e);
		} finally {
			if(jdbc!=null){
				jdbc.release();
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
		return result;
	}
	
}
