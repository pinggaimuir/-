package cn.futures.data.service.impl;

import java.util.Map;

import cn.futures.data.importor.crawler.FAOSTAT.ParamsMap;
import cn.futures.data.service.FAOSTATService;

public class FAOSTATServiceImpl implements FAOSTATService {

	@Override
	public StringBuffer getCountryCodeOfJson(Map<String, String> countryMap) {
		StringBuffer countryCodes = new StringBuffer();
		countryCodes.append("(");
		for(String courtry:countryMap.keySet()){
			String countryCode = ParamsMap.contries_map.get(courtry);
			if(countryCode == null) 
				continue;
			countryCodes.append("''"+ParamsMap.contries_map.get(courtry)+"'',");
		}
		countryCodes.setLength(countryCodes.length()-1);
		countryCodes.append(")");
		return countryCodes;
	}

}
