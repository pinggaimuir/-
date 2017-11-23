package cn.shoparound.spider;

import cn.shoparound.pojo.Product;
import cn.shoparound.utils.ParseUtil;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高健 on 2017/5/17.
 */

public class JdSpiderBak implements Spider{
    private final String searchurl="https://search.jd.com/Search?keyword={keyword}&enc=utf-8";

//    private WebDriverUtil webDriverUtil=new WebDriverUtil();
//    private WebDriver driver= webDriverUtil.getDriver();

    @Resource(name="parseUtil")
    private ParseUtil parser;


    private static ObjectMapper MAPPER=new ObjectMapper();
    /**
     * 根据关键词爬取商品信息列表
     * @param keyword
     * @return
     */
    public List<Product> getProductInfo(String keyword) throws Exception {
        JXDocument document=parser.getDocByJsoup(searchurl.replace("{keyword}",keyword));
        List<Product> plist=parsePage(document);
        return plist;
    }

    public List<Product> nextPage(String keyword,int page) throws IOException {
        page=page*2-1;
        String s=(page-2)*60+58+"";
        String nextUrl=searchurl.replace("{keyword}",keyword)+"&page="+page+"&s="+s;
        JXDocument document=parser.getDocByJsoup(nextUrl);
        List<Product> plist=parsePage(document);
        return plist;
    }

    private List<Product> parsePage(JXDocument document) throws IOException {
        String hrefxpath="//ul[@class='gl-warp clearfix']/li/div/div[@class='p-img']/a/@href";
        List<String> detailurls=parser.getInfoListByXpath(document,hrefxpath);



        List<Product> plist=new ArrayList<Product>();
        for(String detailurl:detailurls){
            Product product=new Product();
//            String productId=detailurl.substring(detailurl.lastIndexOf(".")-6,detailurl.lastIndexOf("."));
            String productId=parser.getStrByReg(".*?(\\d{3,8}).*?",detailurl);
            JXDocument doc=parser.getDocByJsoup("https:"+detailurl);
            //标题
            String title=parser.getInfoByXpath(doc,"//div[@class='sku-name']/text()");
            //价格
            Double price=this.getPrice(productId);
            //图片
            String imageurl=parser.getInfoByXpath(doc,"//img[@id='spec-img']/@data-origin");

            product.setUrl("https:"+detailurl);
            product.setName(title);
            product.setPrice(price);
            product.setImageurl("http:"+imageurl);
            System.out.println(product);
            plist.add(product);
        }
        return plist;
    }

    /**
     * 获取价格信息
     * @param productId 商品Id
     * @return
     * @throws IOException
     */
    private Double getPrice(String productId) throws IOException {
        String jsonPrice=parser.getJsonByJsoup("http://p.3.cn/prices/mgets?skuIds=J_"+productId);
        JsonNode priceNode=MAPPER.readTree(jsonPrice).get(0).get("p");
        return Double.parseDouble(priceNode.asText());
    }


    public static void main(String[] args) throws Exception {
        JdSpiderBak spider=new JdSpiderBak();
        spider.getProductInfo("三星");
    }
}
