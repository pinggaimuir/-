package cn.futures.data.util;

import java.util.HashMap;
import java.util.Map;

public class UnitConvUtil {
	public static Map<String, Integer> unitMap = new HashMap<String, Integer>(){
		{
			put("吨", 1);
			put("百吨", 100);
			put("千吨", 1000);
			put("万吨", 10000);
			put("百万吨", 1000000);
		}
	};
	
	/**
	 * 
	 * @param data  来源数据
	 * @param unit  来源单位
	 * @param dbUnit 数据库中的单位
	 * @return
	 */
	public static Double getConvData(Double data, String unit, String dbUnit){
		if(unitMap.get(unit) != null && unitMap.get(dbUnit)!= null){
			return data/unitMap.get(dbUnit)*unitMap.get(unit);
		}else{
			return null;
		}
	}
}
