package cn.futures.data.util;

import cn.futures.data.DAO.ProxyInfoDaoImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProxyManagerUtil {
	
	private static List<String> proxies = new ArrayList<String>();
	private static int proxyNum,currProxy = -1 ;
	private static String proxyStr = "";
	
	static{
		selectFromDB();
	}
	
	/**
	 * 代理初始化
	 */
	private static void selectFromDB(){
		try{
			ProxyInfoDaoImp pidi = new ProxyInfoDaoImp();
			List<Object[]> sockets = null;
			Iterator<Object[]> iter = null;

			String sql = "select  proxy from ProxyInfo where useful = 1 and lately_status = '1' order by success_rate desc, lately_status_times desc limit " + Constants.proxyFetchNum + ";";
			
			sockets = pidi.queryBySql(sql, 1);
			iter = sockets.iterator();
			while(iter.hasNext()){
				Object[] socket = iter.next();
				String tempStr = socket[0].toString().replace(":", ",");
				proxies.add(tempStr);
				System.out.println(tempStr);
			}
			
			proxyNum = proxies.size();				
			System.out.println("当前取出 "+proxyNum+" 个可用代理。");
			useProxy(0);
			
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 使用指定位置的代理
	 * */
	private static void useProxy(int x){	
		if(proxyNum < 1){
			currProxy = -1 ;
			proxyStr = "";
			System.out.println("无可用代理ip ...");
		} else {
			currProxy = x;
			proxyStr = proxies.get(currProxy%proxyNum);
			System.out.println("切换到代理："+proxyStr);	
		}
	}
	
	/**
	 * 返回当前代理
	 * @return proxyStr 当前代理
	 * */
	public static String getProxyStr(){
		return proxyStr;
	}
	
	/**
	 * 删除当前使用的代理ip,并换用一个新的，若无新的可用，则从数据库中选择一批优质代理。
	 * */
	public static void changeProxy(){
		if(currProxy >= 0){
			proxies.remove(currProxy);
		}
		proxyNum--;
		if(proxyNum < 1){
			freshProxies();
		} else {
			currProxy %= proxyNum;
			useProxy(currProxy);
		}
	}		
	
	/**
	 * 重新从数据库中筛选一批优质代理
	 * */
	public static void freshProxies(){
		selectFromDB();
	}
	
	/**
	 * 代理ip列表是否为空
	 * */
	public static boolean isEmpty(){
		if(proxies.isEmpty()){
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception{
//		selectFromDB();
		String proxy = ProxyManagerUtil.getProxyStr();
		System.out.println("当前proxy：" + proxy);
//		while(true){
//			ProxyManagerUtil.changeProxy();
//		}
	}
}

