package myWebSpider.taobaoSpider;

import infoData.ItemInfo;
import infoData.PageInfoData;
import myUtils.SimilarityUtil;
import myWebSpider.ItemInfoListInterface;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sammy on 14-5-13.
 */
public class TaobaoItemInfoList implements ItemInfoListInterface {

    PageInfoData pagedata;
    /**
     * 当前需要解析的页面URL
     */
    String pageurl;

    public TaobaoItemInfoList(PageInfoData pagedata, String url) {
        this.pagedata = pagedata;
        pageurl = url;
    }


    @Override
    public List<ItemInfo> getItemInfoList()
            throws IOException, InterruptedException {
        /**
         * 通过URL获得Document对象
         */
        Document doc = Jsoup.connect(this.pageurl).get();
        pagedata.tbpage = this.getPageNum(doc);
        List<ItemInfo> prodInfoList = new ArrayList<ItemInfo>();
        Elements itemInfoListDivs = null;
        Elements itemInfoListDivs1 = null;
        Element itemInfoListDiv = doc.select("div.tb-content").first();
        Element itemInfoListDiv1 = doc.select("div.product-combo-bd").first();

        if (itemInfoListDiv1 != null) {
            itemInfoListDivs1 = itemInfoListDiv1.select("div.col");
            for (int i = 0; i < itemInfoListDivs1.size(); i++) {
                Element itemInfoDiv = itemInfoListDivs1.get(i);
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.itemUrl = "http://s.taobao.com" + itemInfoDiv.select("div.photo a").first().attributes().get("href");
                itemInfo.itemImage = itemInfoDiv.select("div.photo img").first().attributes().get("src");
                Element summary = itemInfoDiv.select("h5 a").first();
                if (summary != null) {
                    itemInfo.itemIntro = "<font class=\"skcolor_ljg\">淘宝：</font>" + summary.text();
                } else {
                    continue;
                }
                Element price = itemInfoDiv.select("span.price").first();
                if (price != null) {
                    itemInfo.itemPrice = price.text().substring(1);
                }
//            System.out.println("key:" + SimilarityUtil.sim(this.pagedata.key, summary.text()));
//            System.out.println("keyutf8:"+ SimilarityUtil.sim(this.pagedata.key_utf8, summary.text()));
//            System.out.println("keygbk:"+SimilarityUtil.sim(this.pagedata.key_gbk,summary.text()));
//                if (SimilarityUtil.sim(this.pagedata.key_utf8, summary.text()) > 0.01) {
                prodInfoList.add(itemInfo);
//                }
            }
        } else {
            System.out.println("找不到淘宝页中的类product-combo-bd");
//            return prodInfoList;
        }
        if (itemInfoListDiv != null) {
            itemInfoListDivs = itemInfoListDiv.select("div.item-box");
            for (int i = 0; i < itemInfoListDivs.size(); i++) {
                Element itemInfoDiv = itemInfoListDivs.get(i);
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.itemUrl = itemInfoDiv.select("p.pic-box a").first().attributes().get("href");
                itemInfo.itemImage = itemInfoDiv.select("p.pic-box img").first().attributes().get("data-ks-lazyload");
                Element summary = itemInfoDiv.select("h3.summary").first();
                if (summary != null) {
                    itemInfo.itemIntro = "<font class=\"skcolor_ljg\">淘宝：</font>" + summary.text();
                } else {
                    continue;
                }
                Element price = itemInfoDiv.select("div.price").first();
                if (price != null) {
                    itemInfo.itemPrice = price.text().substring(1);
                }
//            System.out.println("key:" + SimilarityUtil.sim(this.pagedata.key, summary.text()));
//            System.out.println("keyutf8:"+ SimilarityUtil.sim(this.pagedata.key_utf8, summary.text()));
//            System.out.println("keygbk:"+SimilarityUtil.sim(this.pagedata.key_gbk,summary.text()));
                if (SimilarityUtil.sim(this.pagedata.key_utf8, summary.text()) > 0.03) {
                    prodInfoList.add(itemInfo);
                }
            }
        } else {
            System.out.println("找不到淘宝页中的类tb-content");
//            return prodInfoList;
        }
        return prodInfoList;
    }

    /**
     * 获取商品总页数
     *
     * @param doc
     * @return
     */
    public int getPageNum(Document doc) {
        int totalPage = 0;
        String pageInfoText = null;//pageInfoText="1/100"
        Element pageinfo = doc.select("div.pagination span.page-info").first();
        if (pageinfo != null) {
            pageInfoText = pageinfo.text();
            totalPage = Integer.parseInt(pageInfoText.substring(pageInfoText.indexOf("/") + 1));
        }
        return totalPage;
    }
}
