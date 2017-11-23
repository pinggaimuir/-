package cn.shoparound.spider;

import cn.shoparound.pojo.Product;
import cn.shoparound.utils.ParseUtil;
import cn.wanghaomiao.xpath.model.JXDocument;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 高健 on 2017/5/18.
 */

public class AzSpiderBak implements Spider {
    private final String starturl="https://www.amazon.cn/s/ref=nb_sb_noss_2?field-keywords=";

    @Resource(name="parseUtil")
    private ParseUtil parser;
//    private ParseUtil parser=new ParseUtil();


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

        List<Product> plist=new ArrayList<Product>();

        for (String itemurl:itemset){
            System.out.println(itemurl);
            JXDocument doc=parser.getDocByJsoup(itemurl);
            String title=parser.getInfoByXpath(doc,"//span[@id='productTitle']/text()");
            if(title==null||title==""){
                //图书的标题
                title=parser.getInfoByXpath(doc,"//h1[@class='a-size-large a-spacing-none']/allText()");
            }

            String commentStr=parser.getInfoByXpath(doc,"//span[@id='acrCustomerReviewText']/text()");
            String comment=parser.getStrByReg(".*?(\\d*).*?",commentStr);

            String priceStr=parser.getInfoByXpath(doc,"//span[@id='priceblock_ourprice']/text()");
            if(priceStr==null||priceStr==""){
                //促销价
                priceStr=parser.getInfoByXpath(doc,"//span[@id='priceblock_saleprice']/text()");
                if(priceStr==null||priceStr==""){
                    //非全新品，售价从起
                    priceStr=parser.getInfoByXpath(doc,"//span[@class='olp-padding-right']/span/text()");
                    if(priceStr==null||priceStr==""){
                        //书的价格
                        priceStr=parser.getInfoByXpath(doc,"//span[@class='a-size-base a-color-secondary']/text()");
                    }
                }
            }
            String price=priceStr.replace("￥","").replace(",","");

            String imageurl=parser.getInfoByXpath(doc,"//img[@id='landingImage']/@src");
            if(imageurl==null||imageurl==""){
                imageurl=parser.getInfoByXpath(doc,"//img[@class='a-dynamic-image frontImage']/@src");
            }

            Product product=new Product();
            product.setName(title);
            System.out.println(price);
            if(price!=""){
                product.setPrice(Double.parseDouble(price));
            }
            product.setImageurl(imageurl);
            product.setComment(comment);
            product.setUrl(itemurl);

            plist.add(product);
            System.out.println(product);
        }
        return plist;
    }
    public static void main(String[] args) throws Exception {
        new AzSpiderBak().getProductInfo("三星");
    }

}
