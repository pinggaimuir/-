package cn.futures.data.importor.crawler.heatIndex;

import cn.futures.data.entity.GarlicHeadLineIndex;
import cn.futures.data.entity.JsonData01;
import cn.futures.data.entity.JsonSent01;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.WebServiceTool;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//头条指数爬虫
//该爬虫被python爬虫取代
public class HeadLineIndex {

	private static final Logger logger = Logger.getLogger(HeadLineIndex.class);
	private static final String className = HeadLineIndex.class.getName();
	static String[] keywords = { "大蒜", "生姜", "苹果", "白砂糖", "绿豆" };

	// 开始爬取数据
//	@Scheduled(cron = CrawlScheduler.CRON_HEADLINEINDEXGARLIC)
	public void start() {
		// 获取爬虫定时器开关配置
		String switchFlag = new CrawlerManager().selectCrawler("头条指数数据",
				className.substring(className.lastIndexOf(".") + 1));
		if (switchFlag == null) {
			logger.info("没有获取到头条指数数据在数据库中的定时器配置");
		} else {
			if (switchFlag.equals("1")) {
				// 执行爬虫爬取工作
				this.getHeadLineIndex(keywords);
			} else {
				logger.info("抓取头条指数数据的定时器已关闭");
			}
		}
	}

	// 爬虫代码
	public void getHeadLineIndex(String[] keywords) {
		URL url = null;
		int responsecode = 0;
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String line = "";
		GarlicHeadLineIndex garlic = null;
		InputStreamReader streamReader = null;
		// 获取昨天特定格式的日期
		long t1 = new Date().getTime() - 3600000 * 24;
		Date date = new Date(t1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String time = formatter.format(date);
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHH");
		String time1 = formatter1.format(date);
		for (String keyword : keywords) {
			String string = null;
			// 向头条服务器发送请求，获取数据
			try {
				String str = "https://index.toutiao.com/api/keyword/trends?region=0&category=0&keyword=" + keyword
						+ "&start=" + time + "&end=" + time + "&is_hourly=0";
				url = new URL(str);
				logger.info(url.toString());
				urlConnection = (HttpURLConnection) url.openConnection();
				responsecode = urlConnection.getResponseCode();
				if (responsecode == 200) {
					streamReader = new InputStreamReader(urlConnection.getInputStream(), "GBK");
					reader = new BufferedReader(streamReader);
					StringBuffer s1 = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						s1.append(line);
					}
					// 将数据转换成json格式
					Gson gson = new Gson();
					garlic = gson.fromJson(s1.toString(), GarlicHeadLineIndex.class);
					String index = garlic.getTrends().get(keyword);
					int i = index.indexOf(".");
					if (i == -1) {
						string = index;
					} else {
						string = index.substring(0, i);
					}
				} else {
					logger.info("爬虫获取数据访问网站异常响应码是：" + responsecode);
				}
			} catch (Exception e) {
				logger.info("出现异常：" + e);

			} finally {
				// 关流
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.info("关流出现异常：" + e);
					}
				}
				// 关流
				if (streamReader != null) {
					try {
						streamReader.close();
					} catch (IOException e) {
						logger.info("关流出现异常：" + e);
					}
				}

			}
			if (responsecode == 200) {
				// 开始上传数据
				// json生成
				JsonSent01 j = new JsonSent01();
				j.setOperator("shiq");
				j.setBric("shiqiang123");
				j.setCondition("varName,TimeInt,index_source");
				j.setDbName("sentiment_tracking");
				j.setIsCovered(true);
				JsonData01 jd = new JsonData01(time1, keyword, "toutiao", string);
				ArrayList<JsonData01> list = new ArrayList<JsonData01>();
				list.add(jd);
				j.setData(list);
				Gson gson = new Gson();
				String jo = gson.toJson(j);
				String params = "data=" + jo;
				// 访问数据上传接口，上传数据
				String postRequest = WebServiceTool.postRequest("http://dbm3.ncpqh.com/warehouse/byDbName", params);

				if (postRequest != null) {
					if (postRequest.equals("200")) {
						logger.info(keyword + "的头条指数数据上传成功:" + string);
					} else {
						logger.info(keyword + "的头条指数数据上传出现异常：" + postRequest);
					}
				} else {
					logger.info(keyword + "的头条指数数据上传失败");
				}

			}
		}
	}

	public static void main(String[] args) {
		new HeadLineIndex().getHeadLineIndex(keywords);
	}
}
