package com.bric.intoDB.model;

public class Price21foodModel {

	private int KindId;
	private int VarId;
	private int MarketId;
	private int TimeInt;
	private String Province;
	private String Area;
	private String Product;
	private String Market;
	private String SourceAndStd;
	private float MaxPrice;
	private float MinPrice;
	private float AveragePrice;
	
	public void setKindId(int KindId){
		this.KindId = KindId;
	}
	public int getKindId(){
		return this.KindId;
	}
	
	public void setVarId(int VarId){
		this.VarId = VarId;
	}
	public int getVarId(){
		return this.VarId;
	}
	
	public void setTimeInt(int TimeInt){
		this.TimeInt = TimeInt;
	}
	public int getTimeInt(){
		return this.TimeInt;
	}
	
	public void setMarketId(int MarketId){
		this.MarketId = MarketId;
	}
	public int getMarketId(){
		return this.MarketId;
	}
	
	public void setProduct(String Product){
		this.Product = Product;
	}
	public String getProduct(){
		return this.Product;
	}
	
	public void setProvince(String Province){
		this.Province = Province;
	}
	public String getProvince(){
		return this.Province;
	}
	
	public void setArea(String Area){
		this.Area = Area;
	}
	public String getArea(){
		return this.Area;
	}
	
	public void setSourceAndStd(String SourceAndStd){
		this.SourceAndStd = SourceAndStd;
	}
	public String getSourceAndStd(){
		return this.SourceAndStd;
	}
	
	public void setMarket(String Market){
		this.Market = Market;
	}
	public String getMarket(){
		return this.Market;
	}
	
	public void setMaxPrice(float MaxPrice){
		this.MaxPrice = MaxPrice;
	}
	public float getMaxPrice(){
		return this.MaxPrice;
	}
	
	public void setMinPrice(float MinPrice){
		this.MinPrice = MinPrice;
	}
	public float getMinPrice(){
		return this.MinPrice;
	}
	
	public void setAveragePrice(float AveragePrice){
		this.AveragePrice = AveragePrice;
	}
	public float getAveragePrice(){
		return this.AveragePrice;
	}
}
