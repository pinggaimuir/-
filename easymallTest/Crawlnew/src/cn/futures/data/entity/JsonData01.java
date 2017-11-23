package cn.futures.data.entity;

import java.io.Serializable;

public class JsonData01 implements Serializable{
	private String TimeInt;
	private String varName;
	private String index_source;
	private String index_value;
	public JsonData01() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JsonData01(String timeInt, String varName, String index_source, String index_value) {
		super();
		TimeInt = timeInt;
		this.varName = varName;
		this.index_source = index_source;
		this.index_value = index_value;
	}
	public String getTimeInt() {
		return TimeInt;
	}
	public void setTimeInt(String timeInt) {
		TimeInt = timeInt;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getIndex_source() {
		return index_source;
	}
	public void setIndex_source(String index_source) {
		this.index_source = index_source;
	}
	public String getIndex_value() {
		return index_value;
	}
	public void setIndex_value(String index_value) {
		this.index_value = index_value;
	}
	@Override
	public String toString() {
		return "JsonData01 [TimeInt=" + TimeInt + ", varName=" + varName + ", index_source=" + index_source
				+ ", index_value=" + index_value + "]";
	}
	
}
