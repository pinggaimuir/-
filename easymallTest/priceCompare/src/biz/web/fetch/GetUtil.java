package biz.web.fetch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import util.DateUtil;

public class GetUtil {
	private static Map<Integer, String> urls = new HashMap<Integer, String>();
	private final static int PAGE_SIZE = 96;
	private final static int PAGE_SIZE_ALL = 40;
	private final static String COUNT = "MYPAGENUM";
	private final static String SEARCH_KEY = "MYKEY";
	private final static String CURRENTDATE = "CURRENTDATE";

	static {
		urls.put(
				ObjectFormatManager.Electronic_Products,
				"http://list.taobao.com/itemlist/shuma.htm?cat=1512%2C14%2C1201%2C1101%2C50047310%2C11%2C50040831%2C50019393%2C50007218%2C50008090%2C50018627%2C50041307%2C20%2C50076292&sd=1&viewIndex=1&as=0&atype=b&s=MYPAGENUM&style=list&q=MYKEY&same_info=1&tid=0&isnew=2&_input_charset=utf-8");
		urls.put(
				ObjectFormatManager.ALL,
				"http://s.taobao.com/search?initiative_id=staobaoz_CURRENTDATE&js=1MYKEY&stats_click=search_radio_all%3A1&tab=all&promote=0&bcoffset=-8&s=MYPAGENUM#J_relative");
	}

	public static int getPageSize(int type) {
		int size = 0;
		switch (type) {
		case ObjectFormatManager.ALL:
			size = PAGE_SIZE_ALL;
			break;

		default:
			break;
		}
		return size;
	}

	public static String getUrl(int type, int page, String searchKey) {
		String url = null;
		int countNum = 0;
		String keyword = "";
		if (StringUtils.isNotBlank(searchKey)) {
			keyword = searchKey;
		}
		switch (type) {
		case ObjectFormatManager.Electronic_Products:
			if (page > 0) {
				countNum = (page - 1) * PAGE_SIZE;
			}

			String tempurl = urls.get(ObjectFormatManager.Electronic_Products);
			tempurl = tempurl.replace(COUNT, String.valueOf(countNum));
			url = tempurl.replace(SEARCH_KEY, keyword);
			break;
		case ObjectFormatManager.ALL:
			if (page > 0) {
				countNum = (page - 1) * PAGE_SIZE_ALL;
			}
			if (StringUtils.isNotBlank(searchKey)) {
				keyword = "&q=" + searchKey;
			}
			String cdate = DateUtil.getDateString("yyyyMMdd");
			String tempurlall = urls.get(ObjectFormatManager.ALL);
			tempurlall = tempurlall.replace(COUNT, String.valueOf(countNum));
			tempurlall = tempurlall.replace(CURRENTDATE, cdate);
			url = tempurlall.replace(SEARCH_KEY, keyword);
			break;
		}

		return url;
	}
}
