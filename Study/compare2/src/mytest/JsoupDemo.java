package mytest;

import infoData.ItemInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Sammy on 14-5-10.
 */
public class JsoupDemo {
    public  static  void  main(String [] args) {
        String url = "http://search.getFromJd.com/search?keyword=520&qr=&qrst=UNEXPAND&et=&rt=1&area=17&page=10";
        try {
            Document doc = Jsoup.connect(url).get();

//            String prodCnts = doc.select("div.total strong").text();
//            Element prodPageCountsEle = doc.getElementById("top_pagi");
//            String currPage = prodPageCountsEle.select("i").text();
//           String totalPageHtmlText =  prodPageCountsEle.select("span.text").text();
//            String totalPage = totalPageHtmlText.substring(totalPageHtmlText.indexOf("/")+1);
//            print(prodPageCountsEle.html());
//            print("==========================");
//            print(prodPageCountsEle.text());
//            print("==========================");
//            print(currPage);
//            print("==========================");
//            print("totalPage:"+totalPage);
//            print("==========================");
//            print("prodCnts:"+prodCnts);

//            print(doc.html());

            Elements prodLis = doc.select("ul.list-h").first().select("li");
//           print( prodLis.html());
//            prodLis.
            for(int i = 0;i<prodLis.size();i++){
                Element prod = prodLis.get(i);
                print(prod.html());
                ItemInfo prodInfo = new ItemInfo();
                prodInfo.jdid = prod.attributes().get("sku");
                prodInfo.itemUrl = prod.select("div.p-img > a").first().attributes().get("href");
                prodInfo.itemImage =  prod.select("div.p-img > a > img").first().attributes().get("data-lazyload");
                prodInfo.itemIntro = "<font class=\"skcolor_ljg\">京东：</font>" + prod.select("div.p-name").first().html();
                prodInfo.itemPrice = null;
                print(prod.attributes().get("sku"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  static void print(String str){
        System.out.println(str);
    }
}
