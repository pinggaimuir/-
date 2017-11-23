package cn.futures.data.entity;

import cn.futures.data.importor.crawler.futuresMarket.MarketPrice;

import java.util.List;

public class PriceCalc {
	
	public static final String INDEX_TALBE = "指数连续";
	public static final String MAJOR_TALBE = "主力连续";
	
	/**
	 * ����ָ������
	 * @param list
	 */
	public MarketPrice calcIndex(List<MarketPrice> list){
		if (list == null || list.size()  <= 0){
			return null;
		}
		
		// ָ������
		MarketPrice index = new MarketPrice();
		index.setCnName(getIndexTable());
		index.setVarName(list.get(0).getVarName());
		index.setTimeint(list.get(0).getTimeint());
		double weightSum = 0;
		
		for (int i = 0; i < list.size(); i++){
			MarketPrice p = list.get(i);
			// �������ظ���
			if (p.isDuplicated()){
				continue;
			}

			// �۸񰴳ɽ�����Ȩ
			//double weight = p.getVolume();
			//��Ϊ �۸񰴳ֲ�����Ȩ
			double weight = p.getPosition();
			weightSum += weight;
			index.setOpen(index.getOpen() + p.getOpen() * weight);
			index.setHigh(index.getHigh() + p.getHigh() * weight);
			index.setLow(index.getLow() + p.getLow() * weight);
			index.setLast(index.getLast() + p.getLast() * weight);
			index.setSettle(index.getSettle() + p.getSettle() * weight);
			
			// �ֲ������ɽ������ɽ��� ֱ���ۼ�
			index.setPosition(index.getPosition() + p.getPosition());
			index.setVolume(index.getVolume() + p.getVolume());
			index.setTurnover(index.getTurnover() + p.getTurnover());
		}
		// ��Ȩ��ƽ��
		index.setOpen(weightSum==0?0:index.getOpen()/weightSum);
		index.setHigh(weightSum==0?0:index.getHigh()/weightSum);
		index.setLow(weightSum==0?0:index.getLow()/weightSum);
		index.setLast(weightSum==0?0:index.getLast()/weightSum);
		index.setSettle(weightSum==0?0:index.getSettle()/weightSum);
		
		return index;
	}
	
	/**
	 * ����������Լ
	 * @param list
	 * @return
	 */
	public MarketPrice calcMajor(List<MarketPrice> list){
		if (list == null || list.size()  <= 0){
			return null;
		}
		
		// ���ֲ���
		int maxI = 0;
		double maxV = 0;

		for (int i = 0; i < list.size(); i++){
			MarketPrice p = list.get(i);
			if (p.getPosition() > maxV){
				maxV = p.getPosition();
				maxI = i;
			}
		}
		
		// ������Լ
		MarketPrice major = (MarketPrice)list.get(maxI).clone();
		major.setCnName(getMajorTable());
		return major;
	}
	
	private String getMajorTable(){
		return MAJOR_TALBE;
	}
	private String getIndexTable(){
		return INDEX_TALBE;
	}
	
}

