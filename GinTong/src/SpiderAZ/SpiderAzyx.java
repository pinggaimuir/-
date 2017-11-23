package SpiderAZ;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 爬取安卓游戏页面，url：http://www.anzhi.com/sort_21_1_hot.html
 * Created by gao on 2017/2/20.
 */
public class SpiderAzyx {
    public static void main(String[] args) {
        //要抓取的起始页面
        String startUrl="http://www.anzhi.com/sort_21_1_hot.html";
        Document document= null;
        try {
            document = Jsoup.connect(startUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            //调用下面的方法获取详情页的url列表
            List<String> detailUrls = getDetailUrls(document);
            for (String detailUrl : detailUrls) {
                try {
                    Document doc = Jsoup.connect("http://www.anzhi.com" + detailUrl).get();
                    //解析详情页面并且存储进数据库
                    parsePage(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String next=null;
            //进入下一页
            try {
                next = "http://www.anzhi.com" + document.select("a.next").get(0).attr("href");
                document=Jsoup.connect(next).get();
            }catch(Exception e){
                break;
            }
        }
    }


    /**
     * 获取每页列表中的详情页的url
     */
    public static List<String> getDetailUrls(Document document){
        Elements elements=document.select("span.app_name a");
        //存储每页中列表上跳转到详情页的链接
        List<String> detailUrls=new ArrayList<>();
        for(Element element:elements){
            String detailUrl=element.attr("href");
            detailUrls.add(detailUrl);
        }
        return detailUrls;
    }

    /**
     * 解析页面获取相应的字段
     * @param document
     */
    public static void parsePage(Document document){
//        System.setProperty("webdriver.chrome.driver", "E:/aaaa/chrom/chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
        try {
            //图标
            String imageStr= "http://www.anzhi.com"+document.select("div.detail_icon img").get(0).attr("src");
            String image = Jsoup.connect(imageStr).ignoreContentType(true).get().location();
            System.out.println(image);
            //名称
            String name=document.select("div.detail_description div.detail_line h3").get(0).text();
            System.out.println(name);
            //版本
            String version=document.select("span.app_detail_version").get(0).text();
            //时间
            String timeStr=document.select("ul#detail_line_ul li:nth-child(3)").get(0).text().split("\\：")[1];
            Date time=timeFormat(timeStr);
            System.out.println(time);
            //大小
            String size=document.select("ul#detail_line_ul li:nth-child(4)").get(0).text().split("\\：")[1];
            System.out.println(size);
            //系统
            String sys=document.select("ul#detail_line_ul li:nth-child(5)").get(0).text().split("\\：")[1];
            System.out.println(sys);
            //资费
            String charges=document.select("ul#detail_line_ul li:nth-child(6)").get(0).text().split("\\：")[1];
            System.out.println(charges);
            //作者
            String author=document.select("ul#detail_line_ul li:nth-child(7)").get(0).text().split("\\：")[1];
            System.out.println(author);
            //简介
            String introduction=document.select("div.app_detail_infor p").get(0).text();
            System.out.println(introduction);
            Elements liEles=document.select("div.section-body div ul li img");
            //多个游戏截图用“,”分隔连接为一个StringBuffer
            StringBuffer screenShots=new StringBuffer();
            for(Element ele:liEles){
                String screenShotStr="http://www.anzhi.com"+liEles.attr("src");
                String screenShot=Jsoup.connect(screenShotStr).ignoreContentType(true).get().location();
                screenShots.append(screenShot+",");
            }
            System.out.println(screenShots);
            System.out.println("----------------------------------------------------------------------");
            //存入数据库
//            storeToDataBase(imageStr,name,version,time,size,
//                sys,charges,author,introduction,screenShots.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *将数据存入mysql数据库中
     */
    public static void storeToDataBase(String image,String name,String version,Date time, String size,
                                       String sys,String charges,String author,String introduction,String screenShot){

    }

    /**
     * 将时间字符串格式化为Date类型
     */
    public static Date timeFormat(String time) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd");
        Date date=format.parse(time);
        return date;
    }
}
