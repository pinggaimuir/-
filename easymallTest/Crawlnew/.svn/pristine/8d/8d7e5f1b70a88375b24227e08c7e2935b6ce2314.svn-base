package cn.futures.data.importor.crawler.price21food.assist;


import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Query21foodPrice {
	
	public static void main(String[] a){
		Date today = formatData.getDate(0);
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
		String todayStr = bartDateFormat.format(today);
		loadAreaAndPrice(todayStr);
	}

	public static void loadAreaAndPrice(String today){
		int TimeInt = Integer.valueOf(today);
		for(String kind: MapInit.nameReflectMap21food.keySet()){
			Map<String, Integer> innerMap = MapInit.nameReflectMap21food.get(kind);
			String table = MapInit.marketPrice21foodTableMap.get(kind);
			for(String varName:innerMap.keySet()){
				int VarId = innerMap.get(varName);
				String content = query(table, VarId, TimeInt);
				String dir = Constants.TEMP_QUERY_21FOOD_ROOT+"\\" + kind + "\\" + varName;
				String fileString = dir + "\\" + today +".csv";
				File dirFile = new File(dir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				File file = new File(fileString);
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					if (content!=null) {
						fileOutputStream.write(content.getBytes("GB2312"));
					}						
					fileOutputStream.close();
					
				} catch (FileNotFoundException e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(varName+" data load finish...");
			}
		}
		System.out.println(today + " data load finish");
	}
	
	private static String query(String table,int VarId,int TimeInt){
		String content = "";
		String sql = "select 产品,Area,avg(平均价) from "+ table +" where VarId="+VarId
				+" and Area <> '' and TimeInt= "+TimeInt+" group by 产品,Area;";
		
		JdbcRunner jdbcRunner = null;
		ResultSet resultSet = null;
		try {
			jdbcRunner = new JdbcRunner();
			jdbcRunner.beginTransaction();
			resultSet = jdbcRunner.query(sql);
			
			while(resultSet.next()){
				String product = resultSet.getString(1);
				String area = resultSet.getString(2);
				float avg = resultSet.getFloat(3);
				content += product+ "," + area+ "," + avg + "\r\n";
			}
			
			jdbcRunner.release();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jdbcRunner.release();
		} catch (Exception e) {
			e.printStackTrace();
			jdbcRunner.release();
		}
		return content;
	}


}
