package spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pojo.Item;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gao on 2016/11/26.
 */
public class JdSpider2Test {

    @org.junit.Test
    public void testGetItemList() throws Exception {
        JdSpider spider=new JdSpider();
        Document document= null;
        try {
            document = Jsoup.connect("https://search.jd.com/Search?keyword=油条&enc=utf-8").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取商品列表的总页数
//        int totalPage=spider2.getTotalPage(document);
        //获取当前页面商品的url
        List<String> itemUrlList=spider.getUrlList(document);
//        System.out.println(itmUrlList);
        for(String url:itemUrlList){
            String itemId=url.replace("//item.jd.com/", "").replace(".html", "");
            String intro=spider.getItemIntro(itemId);
            System.out.println(intro);
        }
    }

    @org.junit.Test
    public void testGetUrlList() throws Exception {

    }

    @org.junit.Test
    public void testGetItemIntro() throws Exception {

    }
}