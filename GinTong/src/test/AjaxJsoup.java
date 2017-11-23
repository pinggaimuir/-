package test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 高健 on 2017/3/12.
 */
public class AjaxJsoup {
    public static void main(String[] args) throws IOException {
        String reg="";
        String str="<img src='//www.lgstatic.com/thumbnail_160x160/i/image/M00/B3/90/Cgp3O1i81EGABdDWAAAXGblOzaA917.jpg' alt='公司'/>";
        Pattern pattern=Pattern.compile(reg);
        Matcher m=pattern.matcher("<img src='((.*?)(\\.jpg|\\.png|\\.gif))'.*?/>");
//        m.
        while (m.find()){
            System.out.println(m.group());
        }
    }

    /**
     * 1
     * @throws IOException
     */
    public static void tianYanChaIndex() throws IOException{
//        Connection connection=Jsoup.connect("http://www.tianyancha.com/");
//        Connection.Response res=connection.execute();
//        System.out.println(res.cookies());
        //{tnet=39.155.186.238, TYCID=e6db723afdb547138010b45c8d18be2c, aliyungf_tc=AQAAAAgOFgJifQ0A7rqbJ/FX1BtyNWhy}

    }

    /**
     * 2
     * @throws IOException
     */
    public static void tianyancha2() throws IOException {
        Connection connection=Jsoup.connect("http://www.tianyancha.com/act/create.json");
        connection.header("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Referer","http://www.tianyancha.com/")
                .header("Tyc-From","normal")
                .header("CheckError","check")
                .header("DNT","1")
                .header("Connection","Keep-Alive")
                .ignoreContentType(true)
                .header("Accept","application/json, text/plain, */*")
                .header("Accept-Language","zh-CN")
                .header("Host","www.tianyancha.com");;
        Connection.Response res=connection.execute();
        System.out.println(res.cookies());
    }

    public static void tianYanCha() throws IOException {
        Connection connect=Jsoup.connect("http://www.tianyancha.com/v2/search/%E5%B1%B1%E4%B8%9C%E6%95%B0%E6%8D%AE.json?").ignoreContentType(true);

        connect.header("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Referer","http://www.tianyancha.com/search?key=%E5%B1%B1%E4%B8%9C%E6%95%B0%E6%8D%AE&checkFrom=searchBox")
                .header("Tyc-From","normal")
                .header("loop","null")
                .header("CheckError","check")

                .header("Accept","application/json, text/plain, */*")
                .header("Accept-Language","zh-CN")
                .header("Host","www.tianyancha.com");
        Connection.Response res=connect.execute();
        System.out.println(res.cookies());

//        Document document=Jsoup.connect("http://www.tianyancha.com/v2/company/441320836.json")
//                .header("Referer","http://www.tianyancha.com/company/441320836")
//                .header("Tyc-From","normal")
//                .header("CheckError","check")
//                .header("Accept","application/json, text/plain, */*")
//                .header("Accept-Language","zh-CN")
//                .header("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko")
//                .header("Host","www.tianyancha.com")
//                .header("DNT","1")
//                .header("Connection","Keep-Alive")
//                .header("Cookie","_pk_id.1.e431=7cf90dbbf98f1b48.1487763412.3.1489327986.1489327252.; aliyungf_tc=AQAAAK4+S08blAUA7LqbJz7GfT0S07AC; paaptp=370f98d275a1ef480b65ab552bd7ac6d966474d07f02ebeeec15ac2dd94b5; _pk_ref.1.e431=%5B%22%22%2C%22%22%2C1489327252%2C%22http%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DnUjJhHnXxZd0iIgRbHn69RTKCCcTQp2-nEWU63o1D-f25x2V7jJGEMk-ubKV80l5%26wd%3D%26eqid%3Dbbc21172000efbe00000000358c55489%22%5D; _pk_ses.1.e431=*; token=c8f5efbf3167412792839a52b4067fbe; _utm=42a334e0d5f0468a91dc59660716325b; TYCID=b80face9b68e440d814538e1d379db22; tnet=39.155.186.236; RTYCID=eed3f5750a78405883f0211761a49272; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1487763417,1489327246; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1489327985")
//                .ignoreContentType(true)
//                .timeout(2000)
//                .method(Connection.Method.GET)
////                .execute()
//                .get();
//        String json=document.text();
//        System.out.println(json);
    }

    public static void wusonganli ()throws IOException {
//        Document document=Jsoup.connect("http://www.itslaw.com/api/v1/caseFiles?startIndex=4000&countPerPage=20" +
//                "&sortType=1&conditions=keyword%2B29497%2B3%2B%E5%87%8F%E5%88%91")//
        Document document=Jsoup.connect("http://www.itslaw.com/api/v1/caseFiles?startIndex=20&countPerPage=20&sortType=1&conditions=searchWord"+ URLEncoder.encode("高健"))

                .ignoreContentType(true)
                .header("If-Modified-Since","Mon, 26 Jul 1997 05:00:00 GMT")
                .header("Accept","application/json, text/plain, */*")
                .header("Cache-Control","no-cache")
                .header("Pragma","no-cache")
                .header("Referer","http://www.itslaw.com/search?searchMode=judgements&sortType=1&conditions=keyword"+URLEncoder.encode("高健"))
                .header("Accept-Language","zh-CN")
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Host","www.itslaw.com")
                .header("DNT","1")
                .header("Connection","Keep-Alive")
                .timeout(2000)
            .get();
        String json=document.text();
        System.out.println(json);
        System.out.println("%2B%E9%AB%98%E5%81%A5%2B1%2B%E9%AB%98%E5%81%A5");
        System.out.println(URLEncoder.encode("高健"));
    }
}
