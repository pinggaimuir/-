package cn.futures.data.importor.crawler.futuresMarket;

import java.util.Date;

public class MarketPrice implements Cloneable {
	
	private String name;
	private String cnName;
	
	private int id;
	private Date editTime;
	private String varName;
	private int timeint;
	private String sqlTemplate = "insert into %table% (Edittime, varId,timeint,开盘价, 最高价, 最低价, 收盘价, 结算价, 持仓量, 成交额, 成交量, code) " +
			"values(getdate(),%variety%,%timeint%,%open%,%high%,%low%,%close%,%price%,%hold%,%totalmoney%,%totalamount%,%code%)";

	
	private boolean isDuplicated = false;

	public MarketPrice(){}
	
	public MarketPrice(String cnName, String varName) {
		super();
		this.cnName = cnName;
		this.varName = varName;
	}
	

	/**
	 * 代码
	 */
	String code;

	/**
	 * 开盘价
	 */
	double open;
	
	/**
	 * 最高价
	 */
	double high;
	
	/**
	 * 最低价
	 */
	double low;
	
	/**
	 * 收盘价
	 */
	double last;
	
	/**
	 * 结算价
	 */
	double settle;
	
	/**
	 * 持仓量
	 */
	double position;
	
	/**
	 * 成交量
	 */
	double volume;
	
	/**
	 * 成交额=结算价*成交量
	 */
	double turnover;
	
	public void reset(){
		open = high = low = last = settle = position = volume = turnover = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public int getTimeint() {
		return timeint;
	}

	public void setTimeint(int timeint) {
		this.timeint = timeint;
	}

	/**
	 * 开盘价
	 * @return
	 */
	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 * 最高价
	 * @return
	 */
	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 * 最低价
	 * @return
	 */
	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	/**
	 * 收盘价
	 * @return
	 */
	public double getLast() {
		return last;
	}

	public void setLast(double last) {
		this.last = last;
	}
	public double getClose() {
		return last;
	}

	public void setClose(double last) {
		this.last = last;
	}

	/**
	 * 结算价
	 * @return
	 */
	public double getSettle() {
		return settle;
	}

	public void setSettle(double settle) {
		this.settle = settle;
	}
	public double getPrice() {
		return settle;
	}

	public void setPrice(double settle) {
		this.settle = settle;
	}

	/**
	 * 持仓量
	 * @return
	 */
	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public double getHold() {
		return position;
	}

	public void setHold(double position) {
		this.position = position;
	}

	/**
	 * 成交量
	 * @return
	 */
	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getTotalAmount() {
		return volume;
	}

	public void setTotalAmount(double volume) {
		this.volume = volume;
	}

	/**
	 * 成交额
	 * @return
	 */
	public double getTurnover() {
		return turnover;
	}

	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}

	public double getTotalMoney() {
		return turnover;
	}

	public void setTotalMoney(double turnover) {
		this.turnover = turnover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isDuplicated() {
		return isDuplicated;
	}

	public void setDuplicated(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}

	@Override
	public String toString() {
		return "MarketPrice [cnName=" + cnName + ", varName=" + varName +  ", timeint="
				+ timeint + ", open=" + open + ", high=" + high + ", low="
				+ low + ", last=" + last + ", settle=" + settle + ", position="
				+ position + ", volume=" + volume + ", turnover=" + turnover
				+ ", editTime=" + editTime + ", code=" + code + ", id=" + id +  "]";
	}
	
	public String toSql(String tablename,String variety,String timeint) {
		//sqlTemplate = "insert into %table% (Edittime, variety,timeint,开盘价, 最高价, 最低价, 收盘价, 结算价, 持仓量, 成交额, 成交量, code, 交割月份) " +
		//"values(getdate(),%variety%,%timeint%,%open%,%high%,%low%,%close%,%price%,%hold%,%totalamount%,%totalmoney%,%code%,%month%)";
		if(tablename == null || variety==null || timeint== null)
			return null;
		String sql = sqlTemplate.toString();
		sql = sql.replace("%table%", tablename)
				.replace("%variety%", variety)
				.replace("%timeint%", timeint)
				.replace("%open%", String.valueOf(open))
				.replace("%high%", String.valueOf(high))
				.replace("%low%", String.valueOf(low))
				.replace("%close%", String.valueOf(last))
				.replace("%price%", String.valueOf(settle))
				.replace("%hold%", String.valueOf(position))
				.replace("%totalamount%", String.valueOf(volume))
				.replace("%totalmoney%", String.valueOf(turnover))
				.replace("%code%", String.valueOf(code));	
		//System.out.println(sql);
		return sql;
	}
	
	
	public MarketPrice clone() {
		MarketPrice o = new MarketPrice();
		try {
			o = (MarketPrice)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	
	
}
