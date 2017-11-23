package com.bric.crawler;

public abstract class Worker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private String url;
	private String cronTime;
	private String name;
	
	public int fetchPage(){
		return 0;
	}
	
	public int getText(){
		return 1;
	}
	
	public int getContent(){
		return 1;
	}
	
	public  int getSql(){
		return 1;
	}
	
	public int saveSql(){
		return 1;
	}	
}
