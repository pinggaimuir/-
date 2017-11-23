package biz.web.fetch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.entity.main.GoodsObject;

public class AllGoods implements ObjectFormat {
	private static Logger log = LoggerFactory.getLogger(AllGoods.class);

	public List<GoodsObject> goodsObjectFormat(Document doc) {
		// 44个class = item-box
		List<GoodsObject> list = new ArrayList<GoodsObject>();
		Elements items = doc.getElementsByClass("item-box");
		if (items == null || items.isEmpty()) {
			return list;
		}
		String goodsName = null; // 名称
		String picUrl = null;// 图片
		float price = 0;// 价格
		int sellAmount = 0;// 销量
		int comments = 0;// 好评数量
		float commentPercent = 0;// 好评率
		String goodsUrl = null;
		GoodsObject goods = null;
		for (Element itemEl : items) {
			Element title = itemEl.getElementsByClass("summary").first();
			title = title.getElementsByTag("a").first();
			goodsName = title.attr("title");
			
			Element picbox = itemEl.getElementsByClass("pic-box").first();
			picbox = picbox.getElementsByTag("a").first();

			goodsUrl = picbox.attr("href");

			picUrl = picbox.getElementsByTag("img").first()
					.attr("data-ks-lazyload");

			Element priceEl = itemEl.getElementsByClass("price").first();
			String priceString = priceEl.text();
			price = getPrice(priceString);

			Element sellEl = itemEl.getElementsByClass("dealing").first();
			sellAmount = getSellAmount(sellEl.text());

			goods = new GoodsObject();
			goods.setCommentPercent(commentPercent);
			goods.setComments(comments);
			goods.setGoodsName(goodsName);
			goods.setGoodsUrl(goodsUrl);
			goods.setPicUrl(picUrl);
			goods.setPrice(price);
			goods.setSellAmount(sellAmount);
			goods.setType(getGoodsType());

			log.info(goods.toString());
			list.add(goods);
		}
		return list;
	}

	public int getGoodsType() {
		return ObjectFormatManager.ALL;
	}

	public int totalGoodsAmount(Document doc) {
		Element totalEle = doc.getElementsByClass("nav-topbar-content").first();

		String totalString = totalEle.getElementsByClass("h").first().text();
		return getTotalNumber(totalString);
	}

	private int getSellAmount(String str) {
		int ret = 0;
		if (StringUtils.isNotBlank(str)) {
			try {
				str = str.trim();
				str = str.substring(0, str.length() - 3);
				ret = Integer.valueOf(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 单价
	 * 
	 * @param str
	 * @return
	 */
	private float getPrice(String str) {
		float ret = 0f;

		if (StringUtils.isNotBlank(str)) {
			try {
				str = str.trim();
				str = str.substring(1, str.length());
				ret = Float.valueOf(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * 总数
	 * 
	 * @param str
	 * @return
	 */
	private int getTotalNumber(String str) {
		int ret = 0;
		if (StringUtils.isNotBlank(str)) {
			str = str.trim();
			try {
				String numstring = null;
				int end = 1;
				if (str.endsWith("万")) {
					numstring = str.substring(0, str.length() - 1);
					end = 10000;
				} else {
					numstring = str;
				}
				Float f = Float.valueOf(numstring);
				f = f * end;
				ret = f.intValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

}
