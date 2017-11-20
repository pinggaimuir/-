package cn.futures.test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.futures.data.jdbc.JdbcRunner;
import cn.futures.data.util.FileStrIO;

/**
 * 用于更新cfg_table_meta_new中的dataExplain字段
 * @author bric_yangyulin
 * @date 2016-09-30
 * */
public class UpdateDataExplainByName {
	
	private static final Logger LOG = Logger.getLogger(UpdateDataExplainByName.class);
	
	/**
	 * @param butchSize 一次批处理的条数
	 * */
	public void update(String oldPath, String newPath, String sheetName,String sheetName2, int maxColumns, int startRowIndex, int butchSize){
		int idIndex = 0;//id所在位序
		int explainIndex = maxColumns - 1;//说明列所在位序
		String sql = "UPDATE CFG_TABLE_META_NEW SET dataExplain = ? "
				+ "FROM CFG_TABLE_META_NEW meta LEFT JOIN CFT_TABLE_INDUSTRY ind ON meta.industryId = ind.id "
				+ "LEFT JOIN CX_Variety_new var ON meta.varId = var.Id "
				+ "where meta.status = 7 and meta.cnName=? and var.VName = ?";
		String[][] metaInfosOld1 = FileStrIO.readXls(oldPath, sheetName, maxColumns, startRowIndex);//旧表文件数据part1
		String[][] metaInfosOld2 = FileStrIO.readXls(oldPath, sheetName2, maxColumns, startRowIndex);//旧表文件数据part2
		
		Map<String, String> dataExplainByNameOld = new HashMap<String, String>();//旧：id-说明
		LOG.info("------------------开始解析旧文件part1。-----------------");
		for(int rowI = 0; rowI < metaInfosOld1.length; rowI++){
			if(metaInfosOld1[rowI][idIndex] == null || !metaInfosOld1[rowI][idIndex].matches("\\d+(.0)?")) {
				LOG.info("id" + metaInfosOld1[rowI][idIndex] + "不合要求，跳过，请查看文件第" + (startRowIndex + rowI + 1) + "行。");
				continue;
			}
			if(metaInfosOld1[rowI][explainIndex] == null || metaInfosOld1[rowI][explainIndex].equalsIgnoreCase("null")){
				//LOG.info("id" + metaInfosOld1[rowI][idIndex] + "对应说明为空。");
				metaInfosOld1[rowI][explainIndex] = "";
			}
			dataExplainByNameOld.put(String.format("%s-%s", metaInfosOld1[rowI][idIndex+3], metaInfosOld1[rowI][idIndex+4]), metaInfosOld1[rowI][explainIndex]);
		}
		metaInfosOld1 = null;
		LOG.info("------------------开始解析旧文件part2。-----------------");
		for(int rowI = 0; rowI < metaInfosOld2.length; rowI++){
			if(metaInfosOld2[rowI][idIndex] == null || !metaInfosOld2[rowI][idIndex].matches("\\d+(.0)?")) {
				//LOG.info("id" + metaInfosOld2[rowI][idIndex] + "不合要求，跳过，请查看文件第" + (startRowIndex + rowI + 1) + "行。");
				continue;
			}
			if(metaInfosOld2[rowI][explainIndex] == null || metaInfosOld2[rowI][explainIndex].equalsIgnoreCase("null")){
				//LOG.info("id" + metaInfosOld2[rowI][idIndex] + "对应说明为空。");
				metaInfosOld2[rowI][explainIndex] = "";
			}
			dataExplainByNameOld.put(String.format("%s-%s", metaInfosOld2[rowI][idIndex+3], metaInfosOld2[rowI][idIndex+4]), metaInfosOld2[rowI][explainIndex]);
		}
		metaInfosOld2 = null;
		LOG.info("旧文件中共有" + dataExplainByNameOld.size() + "条说明字段符合要求");
		LOG.info("------------------开始解析新文件。-----------------");
		String[][] metaInfosNew = FileStrIO.readXls(newPath, sheetName, maxColumns, startRowIndex);//新表文件数据
		LinkedList<String[]> updateList = new LinkedList<String[]>();//待更新列表
//		Map<String, List<String>> idsByDataExplain = new HashMap<String, List<String>>();//新：说明-id列表
		for(int rowI = 0; rowI < metaInfosNew.length; rowI++){
			if(metaInfosNew[rowI][idIndex] == null || !metaInfosNew[rowI][idIndex].matches("\\d+(.0)?")) {
				LOG.info("id" + metaInfosNew[rowI][idIndex] + "不合要求，跳过，请查看文件第" + (startRowIndex + rowI + 1) + "行。");
				continue;
			}
			if(metaInfosNew[rowI][explainIndex] == null || metaInfosNew[rowI][explainIndex].equalsIgnoreCase("null")){
				//LOG.info("id" + metaInfosNew[rowI][idIndex] + "对应说明为空。");
				metaInfosNew[rowI][explainIndex] = "";
			}
			String name = String.format("%s-%s", metaInfosNew[rowI][idIndex+3], metaInfosNew[rowI][idIndex+4]);
			if(metaInfosNew[rowI][explainIndex].equals(dataExplainByNameOld.get(name))){
				LOG.info("id" + metaInfosNew[rowI][idIndex] + "对应说明未发生改变，不更新。");
				continue;
			} else {
				LOG.info("name" + name + "加入更新列表。");
				LOG.info("原dataExplain：" + dataExplainByNameOld.get(name));
				LOG.info("新dataExplain：" + metaInfosNew[rowI][explainIndex]);
				String[] nameArray = name.split("-");
				updateList.add(new String[]{metaInfosNew[rowI][explainIndex], nameArray[1], nameArray[0]});
			}
		}
		metaInfosNew = null;
		dataExplainByNameOld.clear();
		
		int updateCount = updateList.size();//待更新总数
		LOG.info("-----------待更新列表共" + updateCount + "条------------");
		String[][] updateParams = new String[updateCount][3];
		int index = 0;
		for(String[] oneUp: updateList){
			updateParams[index][0] = oneUp[0];
			updateParams[index][1] = oneUp[1];
			updateParams[index][2] = oneUp[2];
			index++;
		}
		updateList.clear();
		LOG.info("-----------开始更新,共" + updateParams.length + "条------------");
		int from = 0;
		int to = butchSize;
		for(; from < updateCount; from += butchSize, to = from + butchSize){
			LOG.info("-----------更新" + from + "-" + to + "------------");
			if(to > updateCount){
				to = updateCount;
			}
			String[][] butchParams = Arrays.copyOfRange(updateParams, from, to);
			long startTime = new Date().getTime();
			this.butchSave(sql, butchParams);
			long endTime = new Date().getTime();
			long execTime = endTime - startTime;//本次执行时间
			LOG.info("本次执行时间（ms）为" + execTime);
			try {
				Thread.sleep(execTime * 2);
			} catch (InterruptedException e) {
				LOG.error("睡眠时被中断。" + e.getMessage());
			}
		}
	}
	
	public void butchSave(String sql, String[][] vals){
		JdbcRunner jdbc = null;
		try{
			jdbc = new JdbcRunner();
			jdbc.beginTransaction();
			jdbc.batchUpdate2(sql, vals);
			jdbc.endTransaction();
		} catch(Exception e) {
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
	
	public static void main(String[] args) {
		LOG.info("开始更新说明...");
		UpdateDataExplainByName u = new UpdateDataExplainByName();
		u.update("D:\\Test\\表_说明.xls", "D:\\Test\\表说明修改汇总.xls", "Sheet1", "Sheet2", 6, 1, 100);
		LOG.info("更新说明结束。");
	}
}
