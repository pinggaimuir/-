package cn.futures.data.importor.crawler.heatIndex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import com.google.gson.Gson;

import cn.futures.data.entity.BaiduIndexJson;
import cn.futures.data.entity.JsonData01;
import cn.futures.data.entity.JsonSent01;
import cn.futures.data.importor.CrawlScheduler;
import cn.futures.data.util.CrawlerManager;
import cn.futures.data.util.WebServiceTool;

//百度指数爬虫
public class BaiduIndex {

	private static final Logger logger = Logger.getLogger(BaiduIndex.class);
	private static final String className = BaiduIndex.class.getName();
	static String[] keywords = { "大蒜", "生姜", "苹果", "白砂糖", "绿豆" };

	// 开始爬取数据
	@Scheduled(cron = CrawlScheduler.CRON_HEADLINEINDEXGARLIC)
	public void start() {
		// 获取爬虫定时器开关配置
		String switchFlag = new CrawlerManager().selectCrawler("百度指数数据",
				className.substring(className.lastIndexOf(".") + 1));
		if (switchFlag == null) {
			logger.info("没有获取到百度指数数据在数据库中的定时器配置");
		} else {
			if (switchFlag.equals("1")) {
				// 执行爬虫爬取工作
				this.getBaiduIndex(keywords);
			} else {
				logger.info("抓取百度指数数据的定时器已关闭");
			}
		}
	}

	// 爬虫代码
	public void getBaiduIndex(String[] keywords) {
		URL url = null;
		int responsecode = 0;
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String line = "";
		BaiduIndexJson baidu = null;
		InputStreamReader streamReader = null;
		// 获取昨天特定格式的日期
		long t1 = System.currentTimeMillis() - 3600000 * 24;
		Date date = new Date(t1);
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		// String time = formatter.format(date);
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHH");
		String time1 = formatter1.format(date);
		for (String keyword : keywords) {
			String string = null;
			// 向头条服务器发送请求，获取数据
			try {
				String str = "http://api.shenjianshou.cn/?appid=a97611a497d912032402d1fc66e7ee72&keyword=" + keyword;
				url = new URL(str);
				urlConnection = (HttpURLConnection) url.openConnection();
				responsecode = urlConnection.getResponseCode();
				if (responsecode == 200) {
					streamReader = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
					reader = new BufferedReader(streamReader);
					StringBuffer s1 = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						s1.append(line);
					}
					// 为避免浪费，神箭手爬虫百度数据保存到log日志里面
					logger.info("神箭手爬虫爬取" + keyword + "的百度指数数据：" + s1);
					// 将数据转换成json格式
					Gson gson = new Gson();
					baidu = gson.fromJson(s1.toString(), BaiduIndexJson.class);
					String index = baidu.getData().getWeek_all_index();
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
			if (baidu.getError_code()!= null) {
				// 如果返回值是0，代表成功
				if (baidu.getError_code().equals("0")) {
					// 开始上传数据
					// json生成
					JsonSent01 j = new JsonSent01();
					j.setOperator("shiq");
					j.setBric("shiqiang123");
					j.setCondition("varName,TimeInt,index_source");
					j.setDbName("sentiment_tracking");
					j.setIsCovered(true);
					JsonData01 jd = new JsonData01(time1, keyword, "baidu", string);
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
							logger.info(keyword + "的头条指数数据上传:" + string);
						} else {
							logger.info(keyword + "的头条指数数据上传出现异常");
						}
					} else {
						logger.info(keyword + "的头条指数数据上传失败");
					}

				} else {
					logger.info("调用神箭手接口获取数据失败，失败原因：" + baidu.getReason());
				}
			}
		}
	}

	public static void main(String[] args) {
		new BaiduIndex().getBaiduIndex(keywords);
	}
}
