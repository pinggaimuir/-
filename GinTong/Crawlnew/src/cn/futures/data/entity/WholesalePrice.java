package cn.futures.data.entity;

public class WholesalePrice {
	private static final Integer varId = 329;
	private Integer timeInt;
	private Double clzPriceIndex;
	private Double agriPriceIndex;
	
	public WholesalePrice(Integer timeInt, 
			Double clzPriceIndex, 
			Double argiPriceIndex){
		this.timeInt = timeInt;
		this.clzPriceIndex = clzPriceIndex;
		this.agriPriceIndex = argiPriceIndex;
	}

	public static Integer getVarid() {
		return varId;
	}
	
	public Integer getTimeInt() {
		return timeInt;
	}

	public void setTimeInt(Integer timeInt) {
		this.timeInt = timeInt;
	}

	public Double getClzPriceIndex() {
		return clzPriceIndex;
	}

	public void setClzPriceIndex(Double clzPriceIndex) {
		this.clzPriceIndex = clzPriceIndex;
	}

	public Double getAgriPriceIndex() {
		return agriPriceIndex;
	}

	public void setAgriPriceIndex(Double agriPriceIndex) {
		this.agriPriceIndex = agriPriceIndex;
	}
}
