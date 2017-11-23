package cn.shoparound.spider;

import cn.shoparound.pojo.Product;
import cn.shoparound.utils.ParseUtil;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高健 on 2017/5/18.
 */
@Component
public class TbSpider implements Spider {
    private String starturl="https://s.taobao.com/search?q=";

    @Resource(name="parseUtil")
    private ParseUtil parser;

    /**
     * 根据关键字搜索
     * @param keyword
     * @return
     * @throws Exception
     */
    public List<Product> getProductInfo(String keyword) throws Exception {

        String text= Jsoup.connect(starturl+keyword).get().outerHtml();
        List<Product> plist=this.parsePage(text);
        return plist;
    }

    /**
     * 获取下一页信息
     * @param keyword
     * @param page
     * @return
     */
    public List<Product> nextPage(String keyword,int page) throws IOException {
        String s=(page-1)*44+"";
        String nextUrl=starturl+keyword+"&p4ppushleft=1%2C48&s="+s;
        String text= Jsoup.connect(nextUrl).get().outerHtml();
        List<Product> plist=this.parsePage(text);
        return plist;
    }

    private List<Product> parsePage(String text) throws IOException {
        String patid="\"nid\":\"(.*?)\"";

        String patprice="\"view_price\":\"(.*?)\"";

        String patname="\"raw_title\":\"(.*?)\"";

        String pataddress="\"item_loc\":\"(.*?)\"";

        String patpic="\"pic_url\":\"(.*?)\"";

        List<String> allid=parser.getStrsByReg(patid,text);//商品id集合
        List<String> allprice=parser.getStrsByReg(patprice,text);//商品价格
        List<String> allname=parser.getStrsByReg(patname,text);//商品名称集合
        List<String> alladdress=parser.getStrsByReg(pataddress,text);//商户地址集合
        List<String> allpic=parser.getStrsByReg(patpic,text);

        List<Product> plist=new ArrayList<Product>();
        for(int i=0;i<allid.size();i++){
            String id=allid.get(i);
            String itemurl="https://item.taobao.com/item.htm?id="+id;
            itemurl=Jsoup.connect(itemurl).get().location();
            System.out.println(itemurl);
            String MonthlySalesurl=null;
            String referer=null;
            String patjsonp=null;
            String commenturl=null;
            String pat=null;
            if(itemurl.contains("tmall.com")){
                MonthlySalesurl = "https://mdskip.taobao.com/core/initItemDetail.htm?itemId="+id+"&callback=setMdskip";

                referer = "https://detail.tmall.com/item.htm";

                patjsonp = "\"sellCount\":(.*?),";

                commenturl = "https://dsr-rate.tmall.com/list_dsr_info.htm?itemId="+id;

                pat = "\"rateTotal\":(.*?),";
            }else{
                MonthlySalesurl = "https://detailskip.taobao.com/service/getData/1/p1/item/detail/sib.htm?itemId="+id+"&modules=dynStock,qrcode,viewer,price,contract,duty,xmpPromotion,delivery,activity,fqg,zjys,couponActivity,soldQuantity&callback=onSibRequestSuccess";
                referer = "https://item.taobao.com/item.htm";
                patjsonp = "\"confirmGoodsCount\":(.*?),";
                commenturl = "https://rate.taobao.com/detailCount.do?callback=jsonp100&itemId=" + id;
                pat = "\"count\":(.*?)";
            }
            String jsonp=Jsoup.connect(MonthlySalesurl)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                    .header("referer",referer)
                    .ignoreContentType(true)
                    .get()
                    .outerHtml();
            String sellCount=parser.getStrByReg(patjsonp,jsonp);
            String commentdata=Jsoup.connect(commenturl).ignoreContentType(true).get().outerHtml();
            String comment=parser.getStrByReg(pat,commentdata);

            Product product=new Product();
            product.setName(allname.get(i));
            product.setUrl(itemurl);
            product.setPrice(Double.parseDouble(allprice.get(i)));
            product.setSalecount(sellCount);
            product.setComment(comment);
            product.setAddress(alladdress.get(i));
            product.setImageurl("https:"+allpic.get(i));

            plist.add(product);
            System.out.println(product);

        }
        return plist;
    }

    public static void main(String[] args) throws Exception {
        Spider tbSpider=new TbSpider();
        tbSpider.getProductInfo("三星");
    }
}
