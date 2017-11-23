package cn.futures.data.importor;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

public class ExcelDataImport {
	
	public static Logger LOG = Logger.getLogger(ExcelDataImport.class);
	
	public static ExcelImportDAO dao = new ExcelImportDAO();
	
	
	public void readDir(File dir){
		if (dir == null || !dir.exists() ) return;
		
		if (dir.isDirectory()){
			for (File f : dir.listFiles()) {
				readDir(f);
			}
		} else {
			if (dir.getName().endsWith(".xls")) {
				save(dir);
			}
		}
	}


	public boolean save(File f) {
		
		
		LOG.info("开始导入：" + f.getAbsolutePath());
		
		ExcelDataParser dp = new ExcelDataParser();
		ExcelDataChecker dc = new ExcelDataChecker();
		
		ExcelDataset dataset = null;
		try {
			dataset= dp.read(f);
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		
		
		String checkmsg = null;
		if ((checkmsg = dc.checkSize(dataset)) != null) {
			LOG.error(checkmsg);
			return false;
		}
		if ((checkmsg = dc.checkTimeint(dataset)) != null) {
			LOG.error(checkmsg);
			return false;
		}		
		if ((checkmsg = dc.checkTable(dataset)) != null) {
			LOG.error(checkmsg);
			return false;
		}
		if ((checkmsg = dc.checkFields(dataset)) != null) {
			LOG.error(checkmsg);
			return false;
		}	
		
		LOG.info("读取成功：" + f.getName());
		
		
		List<String[]> values = dataset.getValues();
		List<String> fields = dataset.getFields();
		String table = dataset.getTable();
		int varid = dataset.getVarid();
		
		try {
			for (String[] value : values) {
				int timeint = Integer.parseInt((String)value[0]);
				int cnt = dao.count(table, timeint, varid);
				if (cnt == 0){
					dao.insert(table, timeint, varid, fields, value);
				} else {
					dao.update(table, timeint, varid, fields, value);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
		LOG.info("入库成功：" + f.getName());
		return true;
	}
	
	public ExcelDataset findRecentData(String table, int varid){
		return dao.findRecentData(table, varid);
	}

	

}
