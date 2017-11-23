package cn.futures.data.DAO;

import java.util.List;

import cn.futures.data.entity.ProxyInfo;

public interface ProxyInfoDao {
	public boolean saveProxyInfo(ProxyInfo proxyInfo);
	public boolean saveBatch(List<ProxyInfo> proxyInfos);
	public List<ProxyInfo> queryAllProxyInfo();
	public boolean updateBatch(List<ProxyInfo> proxyInfos);
	/**
	 * 按指定sql语句进行查询
	 * @param sql 指定的完整sql语句
	 * @param columnSize 需要映射的列数
	 * */
	public List<Object[]> queryBySql(String sql, int columnSize);
}
