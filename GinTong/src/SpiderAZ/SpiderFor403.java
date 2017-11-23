package SpiderAZ;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * Created by gao on 2017/2/22.
 */
public class SpiderFor403 {
    private static final ObjectMapper MAPPER=new ObjectMapper();
    public static void main(String[] args) throws IOException, XpathSyntaxErrorException {
        Document document= Jsoup.connect("http://www.tianyancha.com/company/2358815206.json")
                .header("Referer","http://www.tianyancha.com/company/2358815206#/company/2358815206")
                .header("Tyc-From","normal")
                .header("CheckError","check")
//                .header("ContentType","application/json, text/javascript")
                .ignoreContentType(true)
//                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .header("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko")
                .header("Host","www.tianyancha.com")
//                .header("Accept-Encoding", "deflate")
//                .header("Accept-Language", "application/json, text/plain, */*")
//                .header("Accept", "application/json, text/javascript")
                .header("Accept","*/*")
                .header("Connection", "Keep-Alive")
                .cookie("aliyungf_tc","AQAAAI41nHjn2QAAxnhBfMhYonDYdWqQ")
                .cookie("token","9c6a01e379e444bb8eaf2f4617130d0e")
                .cookie("_utm","f48a9c6e496147c99a72cbcd18891881")
                .cookie("_pk_id.1.e431","7cf90dbbf98f1b48.1487763412.2.1487765893.1487765893.")
                .cookie("_pk_ses.1.e431","*")
                .cookie("TYCID","ff7b290a147248b4b92fabe8f559c2aa")
                .cookie("tnet","124.65.120.198")
                .cookie("RTYCID","5c219de5ba4343748279923ae6e23095")
                .cookie("Hm_lvt_e92c8d65d92d534b0fc290df538b4758","1487763417")
                .cookie("Hm_lpvt_e92c8d65d92d534b0fc290df538b4758","1487765892")
                .get();
//        String json=document.text();

//        System.out.println(json);
//        String str=MAPPER.readTree(json).get("data").get("baseInfo").get("bondNum").asText();
//        System.out.println(str);
    }
}
