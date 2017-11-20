package cn.futures.data.importor;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.futures.data.importor.ExcelDataset;
import cn.futures.data.jdbc.JdbcRunner;

public class ExcelDataChecker {
	
	private static final Logger log = Logger.getLogger(ExcelDataChecker.class);
	
	public String checkSize(ExcelDataset data){
		List<String> fields = data.getFields();
		if (fields == null || fields.size() == 0) {
			return "未找到标题行";
		}
		List<String[]> values = data.getValues();
		if (values == null || values.size() == 0){
			return "未找到数据行";
		}
		int col = fields.size();
		for (int i = 0; i < values.size(); i++){
			String[] row = values.get(i);
			if (row.length != col){
				return "第" + (i+1) + "行数据与标题行不对齐";
			}
		}
		return null;
	}
	
	public String checkTimeint(ExcelDataset data){
		List<String> fields = data.getFields();
		String timeintField = fields.get(0);
		if (!timeintField.toLowerCase().equals("timeint")){
			return "第一列不是时间列(TimeInt)";
		}
		
		List<String[]> values = data.getValues();
		Pattern p = Pattern.compile("^\\d{8}|\\d{6}|\\d{4}$");
		for (String[] row : values){
			String timeint = row[0];
			if (!p.matcher(timeint).matches()){
				return "时间列数值不合法：[" + timeint + "]";
			}
		}
		return null;
	}
	
	public String checkTable(ExcelDataset data) {
		String sql = "select count(1) from sysobjects where xtype='u' and name='"+data.getTable()+"'";
		int count = 0;
		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			ResultSet rs = db.query(sql);
			if (rs.next()){
				count = rs.getInt(1);
			}
		} catch (Exception e){
			log.error(e);
		} finally {
			db.release();
		}
		
		if (count == 0) {
			return "找不到对应的数据表：[" + data.getTable() + "]";
		}
		
		return null;
	}
	
	public String checkFields(ExcelDataset data){
		String sql = "select name from syscolumns where id=OBJECT_ID('"+data.getTable()+"')";
		List<String> list = new ArrayList<String>();
		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			ResultSet rs = db.query(sql);
			while (rs.next()){
				list.add(rs.getString("name"));
			}
		} catch (Exception e){
			log.error(e);
		} finally {
			db.release();
		}
		
		List<String> fields = data.getFields();
		StringBuilder sb = new StringBuilder();
		for (String field : fields ){
			if (!list.contains(field)){
				sb.append("列名["+field+"]在数据库表["+data.getTable()+"]中不存在<br/>"); 
			}
		}
		if (sb.length() > 0){
			sb.append("数据库表["+data.getTable()+"]中的实际列为：" + list);
		}
		return (sb.length() > 0)?sb.toString():null;
	}
	

}
