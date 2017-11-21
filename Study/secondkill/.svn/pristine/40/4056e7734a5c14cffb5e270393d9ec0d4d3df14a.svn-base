package cn.futures.data.service;

import java.util.List;
import java.util.Map;

import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;

public interface FuturesMarketService {
	
	/**
	 * 一个月份的合约只存在一份，同时写入N和N+1表
	 * 两年的相同月份合约同时存在:小年份的写在N表大年份的写在N+1表
	 * @param monthList
	 * @return
	 */
	public Map<String, String> getMonthMap(List<String> monthList);
	
	/**
	 * 取最近年份的月份合约
	 * @param monthList
	 * @return
	 */
	public Map<String, String> getNearlyMonthMap(List<String> monthList);
	
	/**
	 * 单纯的取持仓量最大的主力连续会出现月份跳跃，现在要求主力合约的月份不可逆
	 * @param list
	 * @param varId
	 * @return
	 */
	public MarketPrice getMajorPrice(List<MarketPrice> list, int varId);
}
