/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myWebSpider.amazonSpider;

import infoData.ItemInfo;
import infoData.PageInfoData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import myUtils.SimilarityUtil;
import myWebSpider.ItemInfoListInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author mazhenhao
 */
public class AZItemInfoList implements ItemInfoListInterface {

    /**
     * 用于存储分页信息的对象
     */
    PageInfoData pagedata;
    /**
     * 页面请求URL
     */
    String pageurl;

    public AZItemInfoList(PageInfoData pagedata, String url) {
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
         * 获取页数信息
         */
//        if (pagedata.page == 1) {
        pagedata.azpage = this.getPageNum(doc);
//            System.out.println("亚马逊商品页数：" + pagedata.azpage);
//        }
        /**
         * 获取商品项信息列表
         */
        List<ItemInfo> itemInfoList = new ArrayList<ItemInfo>();
        Elements listViews = doc.select("div.listView");//listView div 的数量为2
        for (int i = 0; i < listViews.size(); i++) {
            Element listView = listViews.get(i);
            Elements itemInfos = listView.select("div.result");//itemInfo div列表
            for (int j = 0; j < itemInfos.size(); j++) {
                ItemInfo itemInfo = new ItemInfo();
                Element itemInfoDiv = itemInfos.get(j);//itemInfo div
                Element itemUrlNode = itemInfoDiv.select("div.productImage a").first();
                Element itemImageNode = itemInfoDiv.select("div.productImage>a>img").first();
                Element itemIntroNode = itemInfoDiv.select("div.productTitle>a").first();
                Element itemPriceNode = itemInfoDiv.select("div.newPrice>span").first();
                if (itemPriceNode != null && itemPriceNode.text() != "" && itemPriceNode.text() != null) {
                    if (itemUrlNode != null) {
                        itemInfo.itemUrl = itemUrlNode.attributes().get("href");
                    }
                    if (itemImageNode != null) {
                        itemInfo.itemImage = itemImageNode.attributes().get("src");
                    }
                    if (itemIntroNode != null) {
                        itemInfo.itemIntro = "<font class=\"skcolor_ljg\">亚马逊：</font>" + itemIntroNode.text();
                    }
                    String itemPrice = itemPriceNode.text().substring(1);
                    if (itemPrice.contains("-")) {
                        itemInfo.itemPrice = itemPrice.substring(0, itemPrice.indexOf("-") - 1);
                    } else {
                        itemInfo.itemPrice = itemPrice;
                    }
//                    System.out.println("key:"+SimilarityUtil.sim(this.pagedata.key,itemIntroNode.text()));
//                    System.out.println("keyutf8:"+ SimilarityUtil.sim(this.pagedata.key_utf8, itemIntroNode.text()));
//                    System.out.println("keygbk:"+SimilarityUtil.sim(this.pagedata.key_gbk,itemIntroNode.text()));
                    if (SimilarityUtil.sim(this.pagedata.key_utf8,itemIntroNode.text())>0.03) {
                        itemInfoList.add(itemInfo);
                    }
                }
            }
        }
        return itemInfoList;
    }

    /**
     * 获取商品总页数
     *
     * @param doc
     * @return
     */
    private int getPageNum(Document doc) {
        String rscText = doc.select("div.resultCount").text();//rscText="显示： 1-16条， 共21,612条"
        String itemCnts = "";
        if(rscText.contains(",")){
            String[] strArr = rscText.substring(rscText.indexOf("共") + 1, rscText.lastIndexOf("条")).split(",");//strArr=["21","612"]
            for(int i=0;i<strArr.length;i++)
                itemCnts += strArr[i];//itemCnts="21612"
        }else if(rscText.contains("共")){
            itemCnts = rscText.substring(rscText.indexOf("共") + 1, rscText.lastIndexOf("条"));
        }
        int itemnum = 0;
        if (itemCnts != "") {
            itemnum = Integer.parseInt(itemCnts);
        }
        if (itemnum % 16 == 0)
            return itemnum / 16;
        else
            return itemnum / 16 + 1;
    }
}
