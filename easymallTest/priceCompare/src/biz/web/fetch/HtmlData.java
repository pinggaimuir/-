package biz.web.fetch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.filter.WL;

import biz.entity.main.GoodsObject;

/**
 * 获取html内容,并解析为java对象,入库
 * 
 * 联系方式: QQ 879347604; Email:javwork@foxmail.com
 * 
 */
public class HtmlData {
	private static Logger log = LoggerFactory.getLogger(HtmlData.class);
	private static final String charset = "UTF-8";
	private int totalCount = 1;
	private static final int MAX_PAGE = 2; // 采集的最大页数
	private List<GoodsObject> tblist = new ArrayList<GoodsObject>();
	private List<GoodsObject> jdlist = new ArrayList<GoodsObject>();

	public static final String From_taobao = "淘宝";
	public static final String From_jd = "京东";

	public List<GoodsObject> getTblist() {
		return tblist;
	}

	public List<GoodsObject> getJdlist() {
		return jdlist;
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		// HtmlData crawlHuxiu = new HtmlData();
		//
		// // 创建 HttpClient 的多线程实例
		// PoolingHttpClientConnectionManager connectionManager = new
		// PoolingHttpClientConnectionManager();
		// HttpClient httpClient =
		// HttpClientBuilder.create().setConnectionManager(connectionManager).build();
		// int type = ObjectFormatManager.ALL;
		// String key = "test12";
		// crawlHuxiu.execute(httpClient, type, key);
		// log.info("end");
		//

		// //////// 以上是淘宝测试

		// //////// 以下是奶茶婊测试

		String naiurl = "http://search.jd.com/Search?keyword=金士顿&enc=utf-8&page=5";
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).build();
		int type = ObjectFormatManager.ALL;
		HttpResponse response = null;
		response = httpClient.execute(new HttpGet(naiurl));
		int status = response.getStatusLine().getStatusCode();
		Document doc = null;
		try {
			doc = Jsoup.parse(response.getEntity().getContent(), charset, "");
			//System.out.println(doc.html());
			Elements list = doc.getElementsByClass("gl-item");
			for (Element item : list) {
				Element nametag = item.getElementsByClass("p-name").get(0);
				String name = nametag.text();
				System.out.println(name);

				Element imgtag = item.getElementsByClass("p-img").get(0);
				imgtag = imgtag.getElementsByTag("a").get(0);
				//System.out.println(imgtag.html());
				String goodDetailPath = imgtag.attr("href");
				System.out.println(goodDetailPath);

				imgtag = imgtag.getElementsByTag("img").get(0);
				String goodImgPath = imgtag.attr("src");
				if (StringUtils.isEmpty(goodImgPath)) {
					goodImgPath = imgtag.attr("data-lazy-img");
				}
				System.out.println(goodImgPath);

				Element pricetag = item.getElementsByClass("p-price").get(0);
				pricetag = pricetag.getElementsByTag("strong").get(0);
				String price = pricetag.attr("data-price");
				System.out.println(price);
				System.out.println("----------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 线程池执行的任务，抓取网页
	 */
	class ThreadPoolTask /* implements Runnable */{
		HttpClient httpClient = null;
		HttpGet getMethod = null;
		int i = 0;
		int type;
		String key;

		public ThreadPoolTask(HttpGet getMethod, int i) {
		}

		public ThreadPoolTask(HttpClient httpClient, HttpGet getMethod, int i, int type, String key) {
			this.httpClient = httpClient;
			this.getMethod = getMethod;
			this.i = i;
			this.type = type;
			this.key = key;
		}

		@SuppressWarnings("unchecked")
		public void run() {
			// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
			try {
				// 执行getMethod
				HttpResponse response = null;
				response = httpClient.execute(getMethod);
				int status = response.getStatusLine().getStatusCode();

				// 连接返回的状态码
				if (HttpStatus.SC_OK == status) {

					// 获取到的内容
					Document doc = null;
					try {
						doc = Jsoup.parse(response.getEntity().getContent(), charset, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					String htmlString = doc.html();
					int findex = htmlString.indexOf("g_page_config");
					if (findex > 0) {
						System.out.println("---------------------------------");
						htmlString = htmlString.substring(findex + "g_page_config =".length());
						int eindex = htmlString.indexOf("};");
						htmlString = htmlString.substring(0, eindex + 1);
						System.out.println(htmlString);
						Map<String, Object> tempMap = parseJSON2Map(htmlString);
						tempMap = (Map<String, Object>) tempMap.get("mods");
						tempMap = (Map<String, Object>) tempMap.get("itemlist");
						// System.out.println("mods: "+tempMap.get("itemlist"));
						// auctions
						tempMap = (Map<String, Object>) tempMap.get("data");
						JSONArray array = (JSONArray) tempMap.get("auctions");
						//System.out.println("array: " + array);
						//System.out.println(array.size());
						//System.out.println("---------------------------------");
						GoodsObject goods = null;

						String goodsName = null; // 名称
						String picUrl = null;// 图片
						float price = 0;// 价格
						int sellAmount = 0;// 销量
						int comments = 0;// 好评数量
						float commentPercent = 0;// 好评率
						String goodsUrl = null;
						for (int i = 0; i < array.size(); i++) {
							Map<String, Object> jsonbean = (Map<String, Object>) array.get(i);
							goodsName = (String) jsonbean.get("raw_title");
							picUrl = (String) jsonbean.get("pic_url");
							price = Float.valueOf((String) jsonbean.get("view_price"));

							String temp = (String) jsonbean.get("view_sales");
							sellAmount = Integer.valueOf(temp.substring(0, temp.indexOf("人付款")));
							try {
								comments = Integer.valueOf((String) jsonbean.get("comment_count"));
							} catch (Exception e) {
								comments = 0;
							}
							goodsUrl = (String) jsonbean.get("detail_url");
							goods = new GoodsObject();
							goods.setCommentPercent(commentPercent);
							goods.setComments(comments);
							goods.setGoodsName(goodsName);
							goods.setGoodsUrl(goodsUrl);
							goods.setPicUrl(picUrl);
							goods.setPrice(price);
							goods.setSellAmount(sellAmount);
							goods.setType(ObjectFormatManager.ALL);
							goods.setGoodsFrom(HtmlData.From_taobao);
							goods.setId(totalCount++);
							//tblist.add(goods);
							goods.setKeyword(key);
							WL.bizService.addGoods(goods);
						}
					}

					// toObject(doc, i, type);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
	}

	class ThreadPoolTaskNaicha /* implements Runnable */{
		HttpClient httpClient = null;
		HttpGet getMethod = null;
		int i = 0;
		int type;
		String key;

		public ThreadPoolTaskNaicha(HttpGet getMethod, int i) {
		}

		public ThreadPoolTaskNaicha(HttpClient httpClient, HttpGet getMethod, int i, int type, String key) {
			this.httpClient = httpClient;
			this.getMethod = getMethod;
			this.i = i;
			this.type = type;
			this.key = key;
		}

		public void run() {
			// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
			try {
				// 执行getMethod
				HttpResponse response = null;
				response = httpClient.execute(getMethod);
				int status = response.getStatusLine().getStatusCode();

				// 连接返回的状态码
				Document doc = null;
				if (HttpStatus.SC_OK == status) {

					doc = Jsoup.parse(response.getEntity().getContent(), charset, "");
					Elements list = doc.getElementsByClass("gl-item");
					for (Element item : list) {
						Element nametag = item.getElementsByClass("p-name").get(0);
						String name = nametag.text();
						//System.out.println(name);

						Element imgtag = item.getElementsByClass("p-img").get(0);
						imgtag = imgtag.getElementsByTag("a").get(0);
						//System.out.println(imgtag.html());
						String goodDetailPath = imgtag.attr("href");
						//System.out.println(goodDetailPath);

						imgtag = imgtag.getElementsByTag("img").get(0);
						String goodImgPath = imgtag.attr("src");
						if (StringUtils.isEmpty(goodImgPath)) {
							goodImgPath = imgtag.attr("data-lazy-img");
						}
						//System.out.println(goodImgPath);

						Element pricetag = item.getElementsByClass("p-price").get(0);
						pricetag = pricetag.getElementsByTag("strong").get(0);
						String price = pricetag.attr("data-price");
						//System.out.println(price);

						GoodsObject goods = new GoodsObject();
						// goods.setCommentPercent(commentPercent);
						// goods.setComments(comments);
						goods.setGoodsName(name);
						goods.setGoodsUrl(goodDetailPath);
						goods.setPicUrl(goodImgPath);
						goods.setPrice(Float.parseFloat(price));
						// goods.setSellAmount(sellAmount);
						goods.setType(ObjectFormatManager.ALL);
						goods.setGoodsFrom(HtmlData.From_jd);
						goods.setId(totalCount++);
						//						jdlist.add(goods);
						goods.setKeyword(key);
						WL.bizService.addGoods(goods);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
	}

	private int totalPage = 0;

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		ListOrderedMap map = new ListOrderedMap();
		// 最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(parseJSON2Map(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

	public boolean execute(HttpClient httpClient, int type, String key) {
		this.key = key;
		HttpGet getMethod = null;
		// 京东真实页码为 (页码-1)*2 +1;
		int i = 1;
		while (true) {
			String url = GetUtil.getUrl(type, i, key);
			log.info(url);
			log.info("page: {}, key: {}", i, key);
			getMethod = new HttpGet(url);
			new ThreadPoolTask(httpClient, getMethod, i, type, key).run();

			int jdpage = (i - 1) * 2 + 1;
			String naiurl = "http://search.jd.com/Search?keyword=" + key + "&enc=utf-8&page=" + jdpage;
			getMethod = new HttpGet(naiurl);
			new ThreadPoolTaskNaicha(httpClient, getMethod, i, type, key).run();

			i++;
			if (i > MAX_PAGE) {
				break;
			}
		}

		return true;
	}

	private String key;

}