package spider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pojo.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gao on 2016/11/20.
 */
public class JdSpider implements SpiderInterface{
    private static ObjectMapper MAPPER=new ObjectMapper();


    /**
     * 通过关键字获取京东商品列表
     * @return 京东商品列表
     */
    public List<Item> getItemList(String url){
        //用来存储获取到的商品列表
        List<Item> itemList=new ArrayList();

        Document document= null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取商品列表的总页数
        int totalPage=this.getTotalPage(document);
        //获取当前页面商品的url
        List<String> itmUrlList=this.getUrlList(document);


        return itemList;
    }

    /**
     * 获取jd商品页面的总页数
     * @param document
     * @return
     */
    public Integer getTotalPage(Document document){
        String stringTotal=document.select("div#J_topPage span.fp-text i").text();
        return Integer.parseInt(stringTotal);
    }

    /**
     * 获取jd当前页面上所有商品的url链接 eg：//item.jd.com/10515839833.html
     * @param document
     * @return
     */
    public List<String> getUrlList(Document document){
        //存储所有商品url的list
        List<String> itemUrlList=new ArrayList<String>();
        //获取当前页面的商品url
        Elements elements=document.select("li.gl-item div.gl-i-wrap div.p-img a");
        for(Element element:elements){
            itemUrlList.add(element.attr("href"));
        }
        return itemUrlList;
    }
    //将item信息写入数据库
    public static Item save(String itemUrl) throws IOException {
        Document document=Jsoup.connect(itemUrl).get();
        Item item=new Item();

        String itemId=itemUrl.replace("http://item.jd.com/", "").replace(".html", "");
        item.setId(Long.parseLong(itemId));
        //获取商品 标题
        String title = document.select(".sku-name").text();
        if(StringUtils.isEmpty(title)){	//再次尝试
            title = document.select("div#itemInfo div#name h1").text();
        }
        item.setTitle(title);

        item.setImage(getImage(document));
        item.setPrice(getPrice(itemId));
        return item;
    }
    //获取图片
    public static String getImage(Document doc){
        String image = "";
        Elements eles = doc.select("ul.lh li img");
        for(Element ele : eles){
            image += "http:" + ele.attr("src")+",";
        }
        if(StringUtils.isNotEmpty(image)){
            image = image.substring(0,image.length()-1);
        }
        return image;
    }

    //获取商品价格
    public static String getPrice(String itemId) throws IOException {
        String itemPriceUrl = "http://p.3.cn/prices/mgets?skuIds=J_"+itemId;
        Document document=Jsoup.connect(itemPriceUrl).ignoreContentType(true).get();
        String jsonPrice=document.text();
        JsonNode priceNode=MAPPER.readTree(jsonPrice).get(0).get("p");
        return priceNode.asText();
    }
    //获取商品标题
    public String getTitle(String itemId) throws IOException {
        String titleUrl="https://c.3.cn/recommend?methods=accessories%2Csuitv2&sku="+itemId+"&cat=12218%2C13591%2C13594&area=1_72_2799_0&_=1480160080529";
        //获得jsonp数据
        String jsonP=Jsoup.connect(titleUrl).ignoreContentType(true).execute().body();
        //截取jsonp中的json数据
        String jsonData=jsonP.substring(14,jsonP.length()-1);
        String title=MAPPER.readTree(jsonData).get("accessories").get("data").get("wName").asText();
        return title;
    }
    //获取商品详情
    public static String getItemDesc(String itemId) throws IOException {
        String itemUrl="http://d.3.cn/desc/"+itemId;
        String itemDesc=null;
        try {
            String jsonP = Jsoup.connect(itemUrl).ignoreContentType(true).execute().body();
            String jsonData = jsonP.substring(0, jsonP.length() - 1).replace("showdesc(", "");
            itemDesc = MAPPER.readTree(jsonData).get("content").asText();
        }catch(Exception e){
            return null;
        }
        return itemDesc;
    }
    //获取商品介绍
    public static String getItemIntro(String itemId) throws IOException {
        String itemUrl="https://cd.jd.com/promotion/v2?&skuId="+itemId+"&area=1_72_2799_0&cat=12218%2C13591%2C13594";
        String intro=null;
        try {
            byte[] jsonData = Jsoup.connect(itemUrl).ignoreContentType(true).get().text().getBytes("ISO8859_1");
            intro = MAPPER.readTree(jsonData).get("ads").get(0).get("ad").asText();
//            intro=new String(intro.getBytes("utf-8"),"gbk");
        }catch(Exception e){
            return null;
        }
        return intro;
    }
}
