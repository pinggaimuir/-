package cn.futures.data.importor;

import cn.futures.data.jdbc.JdbcRunner;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Variety {
	
	private static Logger logger = Logger.getLogger(Variety.class);
	public static Map<String, Integer> variety;
	public static Map<Integer, String> varietyId;
	
	static {
		synchronized(Variety.class){
			loadVariety();
		}		
	}
	
	public static int getVaridByName(String name) {
		if (variety.containsKey(name)) {
			return variety.get(name);
		} else {
			return -1;
		}
	}
	
	public static String getVarNameById(int id){
		if (varietyId.containsKey(id)) {
			return varietyId.get(id);
		} else {
			return "";
		}		
	}

	/**
	 */
	
	public static void loadVariety() {
		variety = new HashMap<String, Integer>();
		varietyId = new HashMap<Integer, String>();
		
		String sql = "select id, vname from CX_Variety_new";
		JdbcRunner db = null;
		try {
			db = new JdbcRunner();
			ResultSet rs = db.query(sql);
			while (rs.next()){
				Integer varid = rs.getInt("id");
				String vname = rs.getString("vname");
//				System.out.println(varid + " - " + vname);
				
				variety.put(vname, varid);
				varietyId.put(varid, vname);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error happens when load Variety");
		} finally {
			db.release();
		}
	}

}
