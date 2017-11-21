package rabbit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 爬取网页
 * Created by gao on 2016/11/25.
 */
public class CnblogCrawler extends Thread{

    //未爬取的url队列
    private NotCrawlUrlQueue queue=new NotCrawlUrlQueue();
    public void run(){
        //从队列中获取url
        String url=queue.getUrl();
        try {
            //爬取页面
            Document document=Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)").timeout(2000).get();
            //从新爬取的页面中获取新的url
            Elements elements=document.select("div post_item div div.post_item_body h3 a");
            for(Element element:elements){
                String newUrl=elements.attr("href");
                //添加到队列当中
                queue.addUrl(newUrl);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
