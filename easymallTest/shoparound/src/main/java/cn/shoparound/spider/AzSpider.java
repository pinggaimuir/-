package cn.shoparound.spider;

import cn.shoparound.pojo.Product;
import cn.shoparound.utils.ParseUtil;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 高健 on 2017/5/18.
 */
@Component
public class AzSpider implements Spider {
    private final String starturl="https://www.amazon.cn/s/ref=nb_sb_noss_2?field-keywords=";

//    @Resource(name="parseUtil")
//    private ParseUtil parser;
    private ParseUtil parser=new ParseUtil();


    public List<Product> getProductInfo(String keyword) throws Exception {
        JXDocument document=parser.getDocByJsoup(starturl+keyword);
        //解析页面
        List<Product> plist=parsePage(document);
        return plist;
    }

    public List<Product> nextPage(String keyword,int page) throws IOException {
        String nextUrl=starturl+keyword+"page="+page+"ie=UTF8";
        JXDocument document=parser.getDocByJsoup(nextUrl);
        List<Product> plist=parsePage(document);
        return plist;
    }

    /**
     * 解析页面
     * @param document
     * @return
     * @throws IOException
     */
    private List<Product> parsePage(JXDocument document) throws IOException {
        List<String> itemurls=parser.getInfoListByXpath(document,"//div[@class='s-item-container']/div/div/div/a/@href");
        if(itemurls==null){
            itemurls=parser.getInfoListByXpath(document,"//ul[@id='s-results-list-atf']/li/div[@class='s-item-container']/div/div/div/a/@href");
        }
        Set<String> itemset=new HashSet<String>(itemurls);

        //标题
        List<String> titleList=parser.getInfoListByXpath(document,"//div[@class='s-item-container']/div[2]/div/div/a/@title");
        //价格
        List<String> priceList=parser.getInfoListByXpath(document,"//div[@class='s-item-container']/div[3]/div/div/a/@title");

        List<Product> plist=new ArrayList<Product>();

        for (String itemurl:itemset){
            System.out.println(itemurl);
            //标题：
//
//            Product product=new Product();
//            product.setName(title);
//            System.out.println(price);
//            if(price!=""){
//                product.setPrice(Double.parseDouble(price));
//            }
//            product.setImageurl(imageurl);
//            product.setComment(comment);
//            product.setUrl(itemurl);
//
//            plist.add(product);
//            System.out.println(product);
        }
        return plist;
    }
    public static void main(String[] args) throws Exception {
        new AzSpider().getProductInfo("三星");
    }

}
