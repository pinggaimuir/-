package cn.futures.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.futures.data.DAO.DAOUtils;
import cn.futures.data.entity.PriceCalc;
import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;
import cn.futures.data.service.FuturesMarketService;

@Service
public class FuturesMarketServiceImpl implements FuturesMarketService{
	
	private PriceCalc calc = new PriceCalc();
	private DAOUtils dao = new DAOUtils();
	public Map<String, String> getMonthMap(List<String> monthList){
		Map<String, String> monthMap = new HashMap<String, String>();
		for(int j=0;j<monthList.size()-1;j++){
			boolean twoSameMonth = true;
			String month1 = monthList.get(j);
			if(monthMap.get(month1)!=null) continue;
			String tmpYear = month1.substring(0,month1.length()-2);
			String tmpMonth = month1.substring(month1.length()-2,month1.length());
			for(int k=j+1;k<monthList.size();k++){
				String month2 = monthList.get(k);
				String tmpYeark = month2.substring(0,month2.length()-2);
				String tmpMonthk = month2.substring(month2.length()-2,month2.length());
				if(tmpMonthk.equals(tmpMonth)){
					twoSameMonth = false;
					if(Integer.parseInt(tmpYear) > Integer.parseInt(tmpYeark)){
						monthMap.put(month1, "(N+1)"+Integer.parseInt(tmpMonth)+"月连续");
						monthMap.put(month2, "N"+Integer.parseInt(tmpMonth)+"月连续");
					}else{
						monthMap.put(month1, "N"+Integer.parseInt(tmpMonth)+"月连续");
						monthMap.put(month2, "(N+1)"+Integer.parseInt(tmpMonth)+"月连续");
					}
					break;
				}
			}
			if(twoSameMonth){
				monthMap.put(month1, Integer.parseInt(tmpMonth)+"月连续");
			}
		}
		//最后一个月份
		String month1 = monthList.get(monthList.size()-1);
		if(monthMap.get(month1)==null) {
			String tmpMonth = month1.substring(month1.length()-2,month1.length());
			monthMap.put(month1, Integer.parseInt(tmpMonth)+"月连续");
		}
		return monthMap;
	}

	public Map<String, String> getNearlyMonthMap(List<String> monthList) {
		Map<String, String> monthMap = new HashMap<String, String>();
		for(int j=0;j<monthList.size()-1;j++){
			boolean twoSameMonth = true;
			String month1 = monthList.get(j);
			if(monthMap.get(month1)!=null) continue;
			String tmpYear = month1.substring(0,month1.length()-2);
			String tmpMonth = month1.substring(month1.length()-2,month1.length());
			for(int k=j+1;k<monthList.size();k++){
				String month2 = monthList.get(k);
				String tmpYeark = month2.substring(0,month2.length()-2);
				String tmpMonthk = month2.substring(month2.length()-2,month2.length());
				if(tmpMonthk.equals(tmpMonth)){
					twoSameMonth = false;
					if(Integer.parseInt(tmpYear) > Integer.parseInt(tmpYeark)){
						monthMap.put(month1, "(N+1)"+Integer.parseInt(tmpMonth)+"月连续");
						monthMap.put(month2, Integer.parseInt(tmpMonth)+"月连续");
					}else{
						monthMap.put(month1, Integer.parseInt(tmpMonth)+"月连续");
						monthMap.put(month2, "(N+1)"+Integer.parseInt(tmpMonth)+"月连续");
					}
					break;
				}
			}
			if(twoSameMonth){
				monthMap.put(month1, Integer.parseInt(tmpMonth)+"月连续");
			}
		}
		//最后一个月份
		String month1 = monthList.get(monthList.size()-1);
		if(monthMap.get(month1)==null) {
			String tmpMonth = month1.substring(month1.length()-2,month1.length());
			monthMap.put(month1, Integer.parseInt(tmpMonth)+"月连续");
		}
		return monthMap;
	}

	@Override
	public MarketPrice getMajorPrice(List<MarketPrice> list, int varId) {
		MarketPrice priceMajor = calc.calcMajor(list);//计算主力连续
		//取昨天的主力连续code 对比今天的主力连续code
		String newestMajorCode = dao.getNewestMajorCode(varId);
		if(newestMajorCode==null || Integer.parseInt(priceMajor.getCode())>=Integer.parseInt(newestMajorCode)){
			return priceMajor;
		}else{
			MarketPrice major = null;
			for(MarketPrice price:list){
				if(price.getCode().equals(newestMajorCode)){
					major = price.clone();
					major.setTable("CX_MarketData_2");
				}
			}
			return major;
		}
	}
}
