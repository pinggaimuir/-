package cn.futures.data.util;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 记录爬取结果。（成功或者失败，失败的原因等，而非抓到的数据）
 * @author bric_yangyulin
 * @date 2016-08-10
 * */
public class RecordCrawlResult {
	
	public static Logger logger = Logger.getLogger(RecordCrawlResult.class);
	
	/**
	 * 记录爬虫的失败信息。
	 * @param crawlerName 爬虫名称
	 * @param varName 品种名称
	 * @param cnName 中文名称
	 * @param failReason 失败原因
	 * @author bric_yangyulin
	 * @date 2016-08-11
	 * */
	public static void recordFailData(String crawlerName, String varName, String cnName, String failReason){
		try {
			failReason = failReason.replace("\"", "\"\"");//转义failReason中的双引号
			Date currentDate = new Date();
			String currentDay = DateTimeUtil.formatDate(currentDate, DateTimeUtil.YYYYMMDD);//当前日期 
			String editTime = DateTimeUtil.formatDate(currentDate, DateTimeUtil.YYYY_MM_DD_HH_MM_SS);//记录的添加时间
			String failRecord = String.format("%s,%s,%s,\"%s\",%s", crawlerName, varName, cnName, failReason, editTime);//双引号内的内容视为整体，防止excel打开时将其中的逗号视为分隔符
			String dirStr = Constants.CRAWL_RESULT + currentDay + Constants.FILE_SEPARATOR;
			
			FileStrIO.appendStringToFile(failRecord, dirStr, Constants.CRAWL_FAIL_RECORD_NAME, Constants.ENCODE_GB2312);
		} catch (IOException e) {
			logger.error(String.format("保存失败的爬虫失败记录：%s,%s,%s,%s", crawlerName, varName, cnName, failReason));
		}
	}
	
	public static void main(String[] args) {
		RecordCrawlResult.recordFailData("testCrawler", "白糖", "郑糖", "没有新数据");
	}
}
