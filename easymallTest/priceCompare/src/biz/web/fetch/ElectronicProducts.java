package biz.web.fetch;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import biz.entity.main.GoodsObject;

public class ElectronicProducts implements ObjectFormat {

	public List<GoodsObject> goodsObjectFormat(Document doc) {
		List<GoodsObject> list = new ArrayList<GoodsObject>();
		String imagePath = null;
		String goodsName = null;
		String price = null;
		String sellAmount = null;
		// <img
		// data-ks-lazyload="http://img.taobaocdn.com/bao/uploaded/i5/T1_UjsFoJkXXa68F2c_095646.jpg_180x180.jpg"alt=""
		// />
//		Element img = node.child(0).getElementsByTag("img").get(0);
//		imagePath = img.attr("data-ks-lazyload");
//
//		// <div class="title">
//		Element title = node.getElementsByClass("title").get(0);
//
//		// <b>Apple/苹果iPhone 4s</b>
//		goodsName = title.getElementsByTag("b").get(0).text();
//
//		// <em>2488</em>
//		price = title.getElementsByTag("em").get(0).text();
//		
//		//(周销量 32269件)
//		sellAmount = title.getElementsByTag("p").get(1).ownText();
		// log.info(i+": "+img.attr("data-ks-lazyload"));
		
		GoodsObject object = new GoodsObject();
		object.setGoodsName(goodsName);
		object.setPicUrl(imagePath);
		object.setPrice(getPrice(price));
		object.setSellAmount(getAmount(sellAmount));
		object.setType(getGoodsType());
		
		list.add(object);
		
		return list;

	}

	public int getGoodsType() {
		return ObjectFormatManager.Electronic_Products;
	}
	public float getPrice(String price){
		float ret = 0f;
		try {
			ret = Float.valueOf(price);
		} catch (Exception e) {
		}
		return ret;
	}
	private int getAmount(String str){
		String ret = "0";
		if(str != null){
			try {
				ret = str.substring(4, str.length()-2).trim();
			} catch (Exception e) {
			}
		}
		return Integer.valueOf(ret);
	}

	public int totalGoodsAmount(Document doc) {
		return 0;
	}

//	public static void main(String[] args) {
//		String html = "<p><font>约</font><span>&yen;<em>2488</em></span>(周销量 32269件)</p>";
//		Document doc = Jsoup.parse(html);
//		Element link = doc.select("p").first();
//
//		System.out.println(link.text());
//		System.out.println(link.outerHtml());
//		System.out.println(link.ownText());
//	}
}
