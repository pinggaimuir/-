/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider.JDSpider;

import MyWebSpider.InfoGetInterface;
import InfoData.ItemInfoData;
import InfoData.PageInfoData;
import MyFilter.MyFilter2StrEND_1times;
import java.io.IOException;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 *
 * @author mazhenhao
 */
public class JDInfoGet implements InfoGetInterface{
    //private String pageurl;
    /*public JDItemInfoListGet(String key) throws ParserException
    {
        pageurl="http://search.jd.com/Search?keyword="+key+"&enc=utf-8&area=17";
    }*/
    PageInfoData pagedata;
    String pageurl;

    public JDInfoGet(PageInfoData pagedata1,String url){
    
        pagedata=pagedata1;
        pageurl=url;
    }
    
    private  NodeList GetItemInfoList() 
            throws ParserException, IOException, InterruptedException
    {
        //String pageurl;
        //pageurl="http://search.jd.com/Search?keyword="+key+"&enc=utf-8&area=17";
        //System.out.println(pageurl);
        //long t1=System.currentTimeMillis();
       
        //Parser pageItemParser=new Parser(pageurl);
        //System.out.println(pageItemParser.getURL());
        //pageItemParser.setEncoding("gbk");
        /*ListGetRunnable run1=new ListGetRunnable(pageurl,"li s");
        ListGetRunnable run2=new ListGetRunnable(pageurl,"li class=\"i");
        new Thread(run1).start();
        * 
        new Thread(run2).start();
        //int sleeptimes=0;
       while((run1.list==null&&run2.list==null)||(run1.over=false||run2.over==false))
       {
           //System.out.println(run1.list.size());
           //sleeptimes++;
           //if(sleeptimes>10)
               //return null;
           System.out.println("休眠2秒钟 ");
           Thread.sleep(2000);
       }
           
        
        long t2=System.currentTimeMillis();
        System.out.println("获取页面商品列表耗时");
        System.out.println(t2-t1);
        if(run1.list!=null)
            return run1.list;
        else
            return run2.list;*/
        //System.out.println("开始加载网页");
        //System.out.println(new String(new MyHtmlParser(pageurl).GetHtmlText()));
      
        Parser pageItemParser=new Parser(pageurl);
        pageItemParser.setEncoding("GBK");
       // NodeFilter[] filter=new NodeFilter[2];
       // filter[0]=new MyFilterSTART("li s",true);
       // filter[1]= new MyFilterSTART("li class=\"i",true);
        //filter[0]=new MyFilterSTART_1times("head",true);
       // filter[0]= new MyFilterEND_1times("top_pagi\"",true);
       // filter[1]= new MyFilterEND_1times("fix\"",true);
       // filter[3]= new MyFilterEND("class=\"prev-disabled\"",true);
        
       System.out.println("开始获取京东list");
       // NodeList resultlist=pageItemParser.parse(new OrFilter(filter));
        NodeList resultlist=pageItemParser.parse(new MyFilter2StrEND_1times("top_pagi\"","fix\"",true));
        
        System.out.println("已经获取京东list");
        //long t2=System.currentTimeMillis();
        //System.out.println("获取页面商品列表耗时");
        //System.out.println(t2-t1);
        //System.out.println(resultlist.toHtml());
        
       /* ArrayList<String> linklist=new ArrayList<String>();                     //获取CSS链接
        NodeList headlist=resultlist.elementAt(0).getChildren();
       for(int sonindex=headlist.size()-1;sonindex>=0;sonindex--){
           if(headlist.elementAt(sonindex).getText().startsWith("li")){
                linklist.add(headlist.elementAt(sonindex).toHtml());*/
                //System.out.println(headlist.elementAt(sonindex).toHtml());
           
       
      // pagedata.csslink=new String[linklist.size()];
       //pagedata.csslink=linklist.toArray(pagedata.csslink);
        
        
        
       
        if(resultlist.size()==2){
        
            NodeList itemlist=new NodeList();                               //获取页面商品list
            itemlist.add(resultlist.elementAt(1).getChildren());
            //itemlist=itemlist.extractAllNodesThatMatch(new MyFilterSTART("l",true));
            itemlist.add(resultlist.elementAt(0));
            //System.out.println(itemlist.toHtml());
        
         return itemlist;
        }
           
        else
           //System.out.println("京东商品为空！");
            return null;
    
    }
    @Override
    public ItemInfoData[] GetInfo() 
            throws ParserException, IOException, InterruptedException{
        
        return new JDItemInfoGet(pagedata).GetArrayFromList(GetItemInfoList());
    
    }
}
