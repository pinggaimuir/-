package biz.web.fetch;

import java.util.List;

import org.jsoup.nodes.Document;

import biz.entity.main.GoodsObject;

public interface ObjectFormat {
	public List<GoodsObject> goodsObjectFormat(Document doc);
	
	public int getGoodsType();
	
	public int totalGoodsAmount(Document doc);
}
