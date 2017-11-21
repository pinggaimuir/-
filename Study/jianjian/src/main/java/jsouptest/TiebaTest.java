package jsouptest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gao on 2016/11/18.
 */
public class TiebaTest {
    //title\s*=[^>]+\s+target\s*=[^>]+
    public static void main(String[] args) throws IOException {
        Document document=Jsoup.connect("http://tieba.baidu.com/f?kw=%C2%C0%C1%BA%D1%A7%D4%BA").get();
        String re="[^\\s]((<\\s*[aA]\\s+([hH][rR][eE][fF]\\s*=[^>]+\\s*)\\s+[^>]+\\s+([c][l][a][s][s]\\s*=\"[j][_][t][h][_][t][i][t] \")\\s*>)(.*)</[aA]>)";
        Elements elements=document.select("div:matchesOwn("+re+")");
        for(Element element:elements){
            System.out.println(element.text());
        }
//        Pattern pattern=Pattern.compile(re);
//        Matcher m=pattern.matcher(page);
//        while (m.find()){
//            System.out.println(m.group(1));
//        }
    }
}
