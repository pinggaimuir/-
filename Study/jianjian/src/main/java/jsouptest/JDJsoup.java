package jsouptest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gao on 2016/11/17.
 */
public class JDJsoup {
    private static ObjectMapper MAPPER=new ObjectMapper();
    public static void main(String[] args) throws Exception {
        List<String> levelUrlList=getLevel3Url();
        for(String level3Url:levelUrlList) {
            //获取3及分类的所有url
            Document document = Jsoup.connect("http:"+level3Url).get();
            //获取商品列表页总页数
            int total = getTotalPage(document);
            //存储所有商品项的页面url
            List<String> itemUrlList = new ArrayList<String>();
            for (int i = 1; i <= total; i++) {
                List<String> itemUrls = getItemUrls("http:"+level3Url+"&page=" + i);
                itemUrlList.addAll(itemUrls);
            }
            System.out.println(itemUrlList.size());
        }
    }
    //获取京东商品3及分类url
    public static List<String> getLevel3Url() throws IOException {
        String url="http://www.jd.com/allSort.aspx";
        Document document= null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            while (document==null) {
                document = Jsoup.connect(url).get();
            }
        }
        Elements elements=document.select("dl.clearfix dd a");
        List<String> list=new ArrayList();
        for(Element element :elements){
            String level3Url=element.attr("href");
            if(level3Url.startsWith("//list.jd.com/list.html?cat=")){
                list.add(level3Url);
            }
        }
        return list;
    }
    //获取商品列表的总页数
    public static int getTotalPage(Document document)throws Exception{
//        Document document=Jsoup.connect(level3Url).get();
        String totalPage=document.select("div.f-pager span.fp-text i").text();
        int total=Integer.parseInt(totalPage);
        return total;
    }
    //获取商品项的url
    public static List<String> getItemUrls(String itemListUrl) throws IOException {
        Document document= null;
        try {
            document = Jsoup.connect(itemListUrl).get();
        } catch (IOException e) {
            document = Jsoup.connect(itemListUrl).get();
        }
        Elements elements=document.select(".gl-i-wrap").select(".j-sku-item div.p-img a");
        List<String> itemUrlList=new ArrayList<String>();
        for(Element element:elements){
            //获取商品页面url
            String itemUrl=element.attr("href");
            System.out.println(itemUrl);
            //获取商品id
//            Long itemId=Long.parseLong(itemUrl.replace("//item.jd.com/","").replace(".html",""));
            itemUrlList.add(itemUrl);
        }
        return itemUrlList;
    }
    //获取商品价格
    public static Long getPrice(String itemId) throws IOException {
        String itemPriceUrl = "http://p.3.cn/prices/mgets?skuIds=J_"+itemId;
        Document document=Jsoup.connect(itemPriceUrl).ignoreContentType(true).get();
        String jsonPrice=document.text();
        JsonNode priceNode=MAPPER.readTree(jsonPrice).get(0).get("p");
        long price=Long.parseLong(priceNode.asText());
        return price;
    }
    //获取商品卖点
    public static String getSellPoint(String itemId) throws IOException {
        String itemUrl="http://ad.3.cn/ads/mgets?skuids=AD_"+itemId;
        Document document=Jsoup.connect(itemUrl).ignoreContentType(true).get();
        String jsonSellPoint=document.text();
        JsonNode sellPointNode=MAPPER.readTree(jsonSellPoint).get(0).get("ad");
        System.out.println(sellPointNode.asText());
        return sellPointNode.asText();
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
        item.setItemDesc(getItemDesc(itemId));
        item.setPrice(getPrice(itemId));
        item.setSellPoint(getSellPoint(itemId));
        return item;
    }
}
