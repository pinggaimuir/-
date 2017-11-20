package cn.futures.data.entity;

import java.io.Serializable;
import java.util.Map;
//
public class GarlicHeadLineIndex implements Serializable{
	private Map<String,String> trends;//趋势图
	
	private Map<String,String> trends_increase;//累计图
	
	private Map<String,String> trends_range;//日期
	public GarlicHeadLineIndex(Map<String,String> trends, Map<String,String> trends_increase, Map<String,String> trends_range) {
		super();
		this.trends = trends;
		this.trends_increase = trends_increase;
		this.trends_range = trends_range;
	}
	public GarlicHeadLineIndex() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Map<String,String> getTrends() {
		return trends;
	}
	public void setTrends(Map<String,String> trends) {
		this.trends = trends;
	}
	public Map<String,String> getTrends_increase() {
		return trends_increase;
	}
	public void setTrends_increase(Map<String,String> trends_increase) {
		this.trends_increase = trends_increase;
	}
	public Map<String,String> getTrends_range() {
		return trends_range;
	}
	public void setTrends_range(Map<String,String> trends_range) {
		this.trends_range = trends_range;
	}
	@Override
	public String toString() {
		return "Garlic [trends=" + trends + ", trends_increase=" + trends_increase + ", trends_range=" + trends_range
				+ "]";
	}
	
}
