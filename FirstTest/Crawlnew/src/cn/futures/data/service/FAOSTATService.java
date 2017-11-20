package cn.futures.data.service;

import java.util.Map;

public interface FAOSTATService {
	/**
	 * 取需要的国家的代码
	 * @param countryMap
	 * @return
	 */
	public StringBuffer getCountryCodeOfJson(Map<String, String> countryMap);
}
