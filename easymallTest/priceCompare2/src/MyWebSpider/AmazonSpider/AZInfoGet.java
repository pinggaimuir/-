/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider.AmazonSpider;

import MyWebSpider.InfoGetInterface;
import InfoData.ItemInfoData;
import MyFilter.MyFilterEND_1times;
import MyFilter.MyFilterSTART;
import InfoData.PageInfoData;
import java.io.IOException;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 *
 * @author mazhenhao
 */
public class AZInfoGet implements InfoGetInterface{
   
    
    PageInfoData pagedata;
    String pageurl;
    
   public AZInfoGet( PageInfoData pagedata1,String url){
   
       pagedata=pagedata1;
       pageurl=url;
       
   }
    
    private  NodeList GetItemInfoList() throws ParserException
    {
        //System.out.println("开始解析链接："+pageurl);
        Parser pageItemParser=new Parser(pageurl);
        pageItemParser.setEncoding("utf-8");
        //System.out.println(pageItemParser.parse(null).toHtml(true));
        NodeFilter[] filter=new NodeFilter[2];
        filter[0]= new MyFilterSTART("div class=\"result",true);
        filter[1]=new MyFilterEND_1times("Count\"",true);
        NodeList itemlist=pageItemParser.parse(new OrFilter( filter ));
        //int num=itemlist.size()-1;
        //System.out.println("亚马逊商品数目："+num);
        //System.out.println(itemlist.elementAt(0).getText());
        if(itemlist.size()>0)
            return itemlist;
        else
            System.out.println("亚马逊商品列表为空");
            return null;
    }
    
    @Override
    public ItemInfoData[] GetInfo() 
            throws ParserException, IOException, InterruptedException{
        
        return new AZItemInfoGet(pagedata).GetArrayFromList(GetItemInfoList());
    
    }
}
