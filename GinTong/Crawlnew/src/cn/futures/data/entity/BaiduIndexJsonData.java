package cn.futures.data.entity;

import java.io.Serializable;

public class BaiduIndexJsonData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -311154189193151036L;
	private String keyword;
	private String week_all_index;
	private String week_mobile_index;
	private String mouth_all_index;
	private String mouth_mobile_index;
	@Override
	public String toString() {
		return "BaiduIndexJsonData [keyword=" + keyword + ", week_all_index=" + week_all_index + ", week_mobile_index="
				+ week_mobile_index + ", mouth_all_index=" + mouth_all_index + ", mouth_mobile_index="
				+ mouth_mobile_index + "]";
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getWeek_all_index() {
		return week_all_index;
	}
	public void setWeek_all_index(String week_all_index) {
		this.week_all_index = week_all_index;
	}
	public String getWeek_mobile_index() {
		return week_mobile_index;
	}
	public void setWeek_mobile_index(String week_mobile_index) {
		this.week_mobile_index = week_mobile_index;
	}
	public String getMouth_all_index() {
		return mouth_all_index;
	}
	public void setMouth_all_index(String mouth_all_index) {
		this.mouth_all_index = mouth_all_index;
	}
	public String getMouth_mobile_index() {
		return mouth_mobile_index;
	}
	public void setMouth_mobile_index(String mouth_mobile_index) {
		this.mouth_mobile_index = mouth_mobile_index;
	}
	public BaiduIndexJsonData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BaiduIndexJsonData(String keyword, String week_all_index, String week_mobile_index, String mouth_all_index,
			String mouth_mobile_index) {
		super();
		this.keyword = keyword;
		this.week_all_index = week_all_index;
		this.week_mobile_index = week_mobile_index;
		this.mouth_all_index = mouth_all_index;
		this.mouth_mobile_index = mouth_mobile_index;
	}
}
