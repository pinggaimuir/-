/*
 * 全国农产品批发价格指数
 * 吴宪国
 * 20141110
 * 这个用于抓取指数数据，但是宣成已经完成数据的抓取，所以这个就暂时不用
 */
package cn.futures.data.importor.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import cn.futures.data.importor.Variety;
import cn.futures.data.importor.crawler.AgriProWholesPriceIndex;
import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.CrawlerUtil;

public class AgriProWholesPriceIndex {

	public static final Logger LOG = Logger.getLogger(AgriProWholesPriceIndex.class);
	public int maxTotal , maxClz ;
	
	
	class Price{
		String timeint;
		String price1;
		String price2;
	}
	
	public void run(){
		//需要先判断
		String url = "http://pfscnew.agri.gov.cn/pfsc/jgcx/reports.html";
		int varid = Variety.getVaridByName("农产品价格指数");
		LOG.info("fetch   农产品价格指数  @" + url);
		maxTotal=getTimeintMaxTotal();
		maxClz = getTimeintMaxCLZ();
		
		String html = getHTML(url,"gb2312");
		String txtcontent = CrawlerUtil.findStrBetween(html, "农业部“全国农产品批发价格指数”――日度指数", "近期“菜篮子”产品批发价格指数日度走势图");
		txtcontent  = txtcontent.replaceAll("</?[^>]+>", ""); //剔出<html>的标签  
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>|&nbsp;", "");//去除字符串中的空格,回车,换行符,制表符  
        txtcontent = txtcontent.replaceAll("\n*\n", "\n");//去除字符串中的空格,回车,换行符,制表符  
        //System.out.println(txtcontent);    
        
        String[] lines=txtcontent.split("\n");
        Price p = new Price();
        int match=0;
        for(String line:lines){
        	if(line.matches("\\d{4}年\\d{2}月\\d{2}日")){
        		p.timeint = line.replaceAll("年|月|日", "");
        		//System.out.println(p.timeint);
        		match = 1;
        	}
        	if(line.matches("\\d+(\\.\\d+)?")){
        		if(match==1){
        			p.price1 = line;
        			match = 2;
        		}else if(match ==2){//此处原先有个bug，需要带else
        			p.price2 = line;
        			save(p,varid);
        		}         		
        	}
        }     
	}
	public int getTimeintMaxTotal(){		
		String sql = " select max(timeint) timeint from CX_agri_prod_wholesale_prices_totalindex";
		int result = 0;
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			ResultSet rs = jdbc.query(sql);
			if(rs.next()){
				result = rs.getInt("timeint");
			}
			jdbc.release();
		} catch (Exception e){
			//LOG.error("select error ",e);
			e.printStackTrace();
			jdbc.release();
		} finally {
			jdbc.release();
		}
		return result;
	}
	public int getTimeintMaxCLZ(){
		String sql = "select max(timeint) timeint from CX_clz_prod_wholesale_price_index";
		int result = 0;
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			ResultSet rs = jdbc.query(sql);
			if(rs.next()){
				result = rs.getInt("timeint");
			}
			jdbc.release();
		} catch (Exception e){
			//LOG.error("select error ",e);
			e.printStackTrace();
			jdbc.release();
		} finally {
			jdbc.release();
		}
		return result;
	}
	public void save(Price p,int varid){
		
		String sql1 = "insert into CX_agri_prod_wholesale_prices_totalindex (edittime, varid, timeint, 全国) " +
				" values (getDate(), ? , ? , ? )";
		String sql2 = "insert into CX_clz_prod_wholesale_price_index (edittime, varid, timeint, 全国) " +
				" values (getDate(), ? , ? , ? )";		
		
		Object[][] vals = new Object[1][3];
		
		JdbcRunner jdbc = null;
		try {
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			if(Integer.valueOf(p.timeint)>maxTotal){	
				vals[0][0] = varid;
				vals[0][1] = p.timeint;
				vals[0][2] = p.price1;		
				jdbc.batchUpdate2(sql1, vals);
				String sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like 'CX_agri_prod_wholesale_prices_totalindex' and varid="+varid;
				jdbc.update(sql);
			}
			if(Integer.valueOf(p.timeint)>maxClz){	
				vals[0][0] = varid;
				vals[0][1] = p.timeint;
				vals[0][2] = p.price2;		
				jdbc.batchUpdate2(sql2, vals);
				String sql = "update CFG_TABLE_META_NEW set dataUpdateTime=getDate() where dbName like 'CX_clz_prod_wholesale_price_index' and varid="+varid;
				jdbc.update(sql);
			}
			jdbc.endTransaction();
		} catch (Exception e){
			LOG.error("insert data into DB error",e);
			try {
				jdbc.rollTransaction();
			} catch (SQLException e1) {
				LOG.error("DB Transaction rollback error",e1);
			} finally {
				jdbc.release();
			}
		} finally {
			jdbc.release();
		}
	}

	 public static String getHTML(String pageURL, String encoding) { 
	        StringBuilder pageHTML = new StringBuilder(); 
	        try { 
	            URL url = new URL(pageURL); 
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
	            connection.setRequestProperty("User-Agent", "MSIE 7.0"); 
	            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding)); 
	            String line = null; 
	            while ((line = br.readLine()) != null) { 
	                pageHTML.append(line); 
	                pageHTML.append("\n"); 
	            } 
	            connection.disconnect(); 
	        }
	        catch(ConnectException e){
	        	try {
	        		System.out.println("Connection timeout,will try again ten seconds later...");
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        	getHTML(pageURL, encoding);
	        }
	        catch (Exception e) { 
	            e.printStackTrace(); 
	        } 
	        return pageHTML.toString(); 
	    } 
	 
	 /**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			(new AgriProWholesPriceIndex()).run();	
		}
}
