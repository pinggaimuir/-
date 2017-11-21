/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myWebSpider.jdSpider;

import infoData.ItemInfo;
import infoData.PageInfoData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import myUtils.SimilarityUtil;
import myWebSpider.ItemInfoListInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Sammy
 */
public class JDItemInfoList implements ItemInfoListInterface {

    PageInfoData pagedata;
    /**
     * 当前需要解析的页面URL
     */
    String pageurl;

    public JDItemInfoList(PageInfoData pagedata, String url) {
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
        /**
         * 获取商品总页数
         */
        pagedata.jdpage = this.getPageNum(doc);
        System.out.println("京东商品总页数：" + pagedata.jdpage);
//        }
        /**
         * 获取商品项信息列表
         */
        List<ItemInfo> prodInfoList = new ArrayList<ItemInfo>();
        Element ul = doc.select("ul.list-h").first();
        Elements liList = null;
        if (ul != null) {
            liList = ul.select("li");
        }else {
            ul = doc.select("ul.clearfix").first();
            if (ul != null) {
                liList = ul.select("li");
            }
        }
        if(liList == null) return prodInfoList;
        for (int i = 0; i < liList.size(); i++) {
            Element prodEle = liList.get(i);
            ItemInfo itemInfo = new ItemInfo();
            String jdid = prodEle.attributes().get("sku");
            String bookid = prodEle.attributes().get("bookid");
//            if (bookid == ""){
//                String str = prodEle.select("div.p-price strong").first().attributes().get("class");
//                bookid = str.substring(str.indexOf("_")+1);
//            }
            Element itemUrlDiv = null;
            Element itemImageDiv = null;
            Element itemIntrolDiv = null;
            if (jdid != "") {
                itemInfo.jdid = jdid;
                itemUrlDiv = prodEle.select("div.p-img a").first();
                itemImageDiv = prodEle.select("div.p-img img").first();
                itemIntrolDiv = prodEle.select("div.p-name").first();
                if (itemUrlDiv != null)
                    itemInfo.itemUrl = itemUrlDiv.attributes().get("href");
                if (itemImageDiv != null)
                    itemInfo.itemImage = itemImageDiv.attributes().get("data-lazyload");
                itemInfo.itemPrice = null;
                if (itemIntrolDiv != null) {
                    itemInfo.itemIntro = "<font class=\"skcolor_ljg\">京东：</font>" + itemIntrolDiv.text();
//                    System.out.println("key:"+SimilarityUtil.sim(this.pagedata.key,itemIntrolDiv.text()));
//                    System.out.println("keyutf8:"+SimilarityUtil.sim(this.pagedata.key_utf8,itemIntrolDiv.text()));
//                    System.out.println("keygbk:"+SimilarityUtil.sim(this.pagedata.key_gbk,itemIntrolDiv.text()));
                    if (SimilarityUtil.sim(this.pagedata.key_utf8,itemIntrolDiv.text())>0.03) {
                        prodInfoList.add(itemInfo);
                    }
                }
            }else if (bookid != "") {
                itemInfo.jdid = bookid;
                itemUrlDiv = prodEle.select("div.p-img a").first();
                itemImageDiv = prodEle.select("div.p-img img").first();
                itemIntrolDiv = prodEle.select("div.p-name").first();
                if (itemUrlDiv != null)
                    itemInfo.itemUrl = itemUrlDiv.attributes().get("href");
                if (itemImageDiv != null)
                    itemInfo.itemImage = itemImageDiv.attributes().get("data-lazyload");
                if (itemIntrolDiv != null)
                    itemInfo.itemIntro = "<font class=\"skcolor_ljg\">京东：</font>" + itemIntrolDiv.text();
                itemInfo.itemPrice = null;
                prodInfoList.add(itemInfo);
            }

        }
        /**
         * 更新一下价格
         */
        if (prodInfoList.size()>0 && prodInfoList.get(0) != null && prodInfoList.get(0).itemPrice == null ) {
            System.out.println("京东价格获取失败，开始更新价格");
            this.UpdatePrice(prodInfoList);
        }

        return prodInfoList;

    }
    /**
     * 获取商品总页数
     *
     * @param doc
     * @return
     */
    private int getPageNum(Document doc) {
        /**
         * 获取页数信息
         */
//        String prodCnts = doc.select("div.total strong").text();//产品总数量
        Element prodPageCountsEle = doc.getElementById("top_pagi");
//        String currPage = prodPageCountsEle.select("i").text();//当前页页码
        String totalPageHtmlText = null;
        if (prodPageCountsEle != null) {
            totalPageHtmlText = prodPageCountsEle.select("span.text").text();
        }
        int totalPage = 0;
        if ( totalPageHtmlText != null) {
            totalPage = Integer.parseInt(totalPageHtmlText.substring(totalPageHtmlText.indexOf("/") + 1));
        }
        return totalPage;
    }
    /**
     * 更新京东价格
     *
     * @param itemlist
     * @throws IOException
     */
    private void UpdatePrice(List<ItemInfo> itemlist) throws IOException {
        int maxindex = itemlist.size() - 1;
        StringBuffer priceurl = new StringBuffer("http://p.3.cn/prices/mgets?skuids=");
        for (int i = 0; i <= maxindex; i++) {
            if (itemlist.get(i) != null) {
                priceurl = priceurl.append("J_").append(itemlist.get(i).jdid).append(",");
            } else {
                break;
            }
        }
        priceurl.append("&type=1");
        JSONArray jsonArr = this.getPriceJSONArray(priceurl.toString());
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                itemlist.get(i).itemPrice = (String) jsonArr.getJSONObject(i).get("p");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 到得价格JSON对象
     *
     * @param url
     * @return JSONArray对象，对象中属性格式如： [{"id":"J_1024062417","p":"129.00","m":"259.00"},{"id":"J_1022922287","p":"48.00","m":"256.00"},...]
     * @throws IOException
     */
    private JSONArray getPriceJSONArray(String url) throws IOException {
        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
        String jsonObjsStr = doc.text();
        JSONArray ja = null;
        try {
            ja = new JSONArray(jsonObjsStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ja;
    }
}
