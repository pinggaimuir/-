package qichezhijiaSpider;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 高健 on 2017/11/14.
 */
public class CarSpider {

    public static final  ObjectMapper MAPPER=new ObjectMapper();

    public static void main(String[] args) {
        String startUrl="https://www.autohome.com.cn/beijing/";
        Document document=null;
        try {
            document= Jsoup.connect(startUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //转换为可以由xpath解析
        JXDocument docx=new JXDocument(document);
        try {
            //获取最新的10条文章的地址
            List alist=docx.sel("//div[@id='tab-41']/div[1]/div[1]/ul/li/div/h3/a/@href");
            //获取最新的10条视频的地址
            List vlist=docx.sel("//dl[@class='carvideo-list']/dd/div[1]/a/@href");

            for (Object o:alist){
                String articleUrl="https://www.autohome.com.cn"+o.toString();
                document = Jsoup.connect(articleUrl).get();
                JXDocument docx2=new JXDocument(document);
                //获取文章的文本内容
                String articleList=docx2.sel("//*[@id='articleContent']//text()").get(0).toString().trim();
//                articleList=articleList.substring(articleList.indexOf("]")+2,articleList.indexOf("查看同类文章"));
                System.out.println(articleList);
            }

            for (Object o:vlist){
                String articleUrl="https:"+o.toString();
                document = Jsoup.connect(articleUrl).get();
                JXDocument docx2=new JXDocument(document);
                //从网页源码中获取mid
                String html=document.outerHtml();
//                System.out.println(html);
                Pattern p=Pattern.compile("//p-vp.autohome.com.cn/api/player\\?mid=(\\w+?)&amp;container");
                Matcher matcher=p.matcher(html);
                String mid="";
                if(matcher.find()){
                    mid=matcher.group(1);
                }
                //根据mid构造url
                String preurl="https://p-vp.autohome.com.cn/api/pi?mid="+mid;
                String vUrl=getVideoUrl(preurl);
                System.out.println(vUrl);

                String filename=mid+".mp4";
                String savePath="D:\\英雄时刻\\";
                DownloadFile.downLoadFromUrl(vUrl,filename,savePath);
            }
        } catch (XpathSyntaxErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *   通过请求url并且解析获得的json得到视频的真实url
     * @param preUrl
     * @return  视频的真实url
     * @throws IOException
     */
    public static String getVideoUrl(String preUrl) throws IOException {
        Document document = Jsoup.connect(preUrl).ignoreContentType(true).get();
        String json=document.text();
        String vUrl=MAPPER.readTree(json).get("media").get("qualities").get(0).get("copies").get(0).asText();
        return vUrl;
    }
}
