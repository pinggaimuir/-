package cn.futures.data.entity;

import java.util.List;

import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;

public class PriceCalc {
	
	public static final String INDEX_TALBE = "CX_MarketData_1";
	public static final String MAJOR_TALBE = "CX_MarketData_2";
	
	/**
	 * 计算指数连续
	 * @param list
	 */
	public MarketPrice calcIndex(List<MarketPrice> list){
		if (list == null || list.size()  <= 0){
			return null;
		}
		
		// 指数连续
		MarketPrice index = new MarketPrice();
		index.setTable(getIndexTable());
		index.setVarid(list.get(0).getVarid());
		index.setTimeint(list.get(0).getTimeint());
		double weightSum = 0;
		
		for (int i = 0; i < list.size(); i++){
			MarketPrice p = list.get(i);
			// 不计算重复项
			if (p.isDuplicated()){
				continue;
			}

			// 价格按成交量加权
			//double weight = p.getVolume();
			//改为 价格按持仓量加权
			double weight = p.getPosition();
			weightSum += weight;
			index.setOpen(index.getOpen() + p.getOpen() * weight);
			index.setHigh(index.getHigh() + p.getHigh() * weight);
			index.setLow(index.getLow() + p.getLow() * weight);
			index.setLast(index.getLast() + p.getLast() * weight);
			index.setSettle(index.getSettle() + p.getSettle() * weight);
			
			// 持仓量，成交量，成交额 直接累加
			index.setPosition(index.getPosition() + p.getPosition());
			index.setVolume(index.getVolume() + p.getVolume());
			index.setTurnover(index.getTurnover() + p.getTurnover());
		}
		// 加权后平均
		index.setOpen(weightSum==0?0:index.getOpen()/weightSum);
		index.setHigh(weightSum==0?0:index.getHigh()/weightSum);
		index.setLow(weightSum==0?0:index.getLow()/weightSum);
		index.setLast(weightSum==0?0:index.getLast()/weightSum);
		index.setSettle(weightSum==0?0:index.getSettle()/weightSum);
		
		return index;
	}
	
	/**
	 * 计算主力合约
	 * @param list
	 * @return
	 */
	public MarketPrice calcMajor(List<MarketPrice> list){
		if (list == null || list.size()  <= 0){
			return null;
		}
		
		// 最大持仓量
		int maxI = 0;
		double maxV = 0;

		for (int i = 0; i < list.size(); i++){
			MarketPrice p = list.get(i);
			if (p.getPosition() > maxV){
				maxV = p.getPosition();
				maxI = i;
			}
		}
		
		// 主力合约
		MarketPrice major = (MarketPrice)list.get(maxI).clone();
		major.setTable(getMajorTable());
		return major;
	}
	
	private String getMajorTable(){
		return MAJOR_TALBE;
	}
	private String getIndexTable(){
		return INDEX_TALBE;
	}
	
}

