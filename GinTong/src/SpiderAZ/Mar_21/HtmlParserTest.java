package SpiderAZ.Mar_21;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Created by 高健 on 2017/3/21.
 */
public class HtmlParserTest {
    public static void main(String[] args) throws Exception {
        //用jsoup链接网页
        Document document=Jsoup.connect("http://price.21food.cn/product/504.html").timeout(5000).get();
        String content=document.html();
//        System.out.println(content);
        //创建htmlParser
        Parser parser= Parser.createParser(content,"utf-8");

        /*Price21foodDao修改前的保留版本
            TagNameFilter divFilter=new TagNameFilter("div");
            HasAttributeFilter classFilter=new HasAttributeFilter("class","sjs_top_cent_erv");
            AndFilter filter=new AndFilter(divFilter,classFilter);
            NodeList nodes=parser.extractAllNodesThatMatch(filter).elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("table"),true);
            if(nodes.size() != 0){
//				tableList = nodes.elementAt(0).getChildren();
			}else{
				logger.error("未找到过滤节点");
				return null;
			}
			return HtmlNodeListUtil.table2Str_SpecifyRowsCols(nodes, "0", "11111100");*/
        String html=parser.parse(new TagNameFilter("html")).toHtml();
        Document document1=Jsoup.parse(html);
        JXDocument doc=new JXDocument(document1);
        List list=doc.sel("//div[@class='gs_top_t2_left']/div[2]/div[2]/ul/li/table/tbody/tr/allText()");
        for(Object o:list){
            System.out.println(o.toString().replaceAll("\\s",","));
        }
//        List list=doc.sel("//div[@class='sjs_top_cent_erv']/ul/li/table/tbody/tr//text()");
//        for(Object o:list){
//            String[] strs=o.toString().split("茄子");
//            for (String s:strs){
//                System.out.println(s);
//            }
//        }





    }
}
