/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider.JDSpider;

import MyWebSpider.GetItemFromWeb;
import InfoData.ItemInfoData;
import MyWebSpider.ItemInfoGetInteface;
import InfoData.PageInfoData;
import MyFilter.MyFilterSTART;
import MyFilter.MyFilterSTART_1times;
import MyWebSpider.MyHtmlParser;
import java.io.IOException;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 *
 * @author mazhenhao
 */
public class JDItemInfoGet implements ItemInfoGetInteface {
   // private NodeList pagItemList;
    //private JspWriter out;
    
    PageInfoData pagedata;

    public   JDItemInfoGet(PageInfoData pagedata1){
    
        pagedata=pagedata1;
    
    }
    
    @Override
    @SuppressWarnings("empty-statement")
    public ItemInfoData[] GetArrayFromList(NodeList pagItemList) 
            throws ParserException, IOException
    {
        /*if(pagItemList.size()==0)
            System.out.println("页面商品列表为空！");
        int threadnum=pagItemList.size();
        //SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<num;i++)
        {
            OneItemInfoGet(pagItemList.elementAt(i));
            //System.out.println(sm.format(new Date()));
        }*/
       
       if(pagItemList!=null)
       {
           
       
           
           //int threadnum=pagItemList.size();
            if(pagedata.page==1)
               GetPageNum(pagItemList.elementAt(pagItemList.size()-1));             //获取商品页数
         
           ItemInfoData[] itemlist = new ItemInfoData[36];
           int num=pagItemList.size()-1;                                            //去掉页面信息所占结点数（为1）
           //System.out.println("京东商品数目："+num);
           /*int threadnum;
           if(num%36!=0)
               threadnum=num/36+1;
           else
               threadnum=num/36;
           Runnable itemgetthread=new ItemDataGetRunnable(threadnum,pagItemList
               ,itemlist,this, pagedata);
           for(int i=0;i<threadnum;i++)
               new Thread(itemgetthread).start();
          
           if(pagedata.page==1)
               GetPageNum(pagItemList.elementAt(0));             //获取商品页数
          
           long t1=System.currentTimeMillis();
          
           //int sleepnum=0;
           while(((ItemDataGetRunnable)itemgetthread).itemnum!=num)
               try {
               Thread.sleep(20);
               //sleepnum++;
              // System.out.println("卡出翔了！"+((ItemDataGetRunnable)itemgetthread).itemnum);
               //if(sleepnum>1)
                   //break;
           } catch (InterruptedException ex) {
              // Logger.getLogger(JDItemInfoGet.class.getName()).log(Level.SEVERE, null, ex);
           }
              //((ItemDataGetRunnable)itemgetthread).flage=false;
              long t2=System.currentTimeMillis();
              //System.out.println("商品基本信息解析耗时");
              //System.out.println(t2-t1);
              return itemlist;*/
           for(int i=0,j=0;j<num;i++,j++)
           {
               if(pagItemList.elementAt(j)!=null){
                   //System.out.println("开始解析商品"+j);
                    ItemInfoData iteminfo=OneItemInfoGet(pagItemList.elementAt(j));
                    if(iteminfo==null){
                         i--;
                        continue;
                    }
                        itemlist[i]=iteminfo;
                   }
               else
                   break;
            }
           
           
           if( itemlist[0].itemPrice==null){
                System.out.println("京东价格获取失败，开始更新价格");
               UpdatePrice(itemlist);
           
           }
           
           
           return itemlist;
       }
       else
           System.out.println("商品列表为空");
           return null;
           
    }
    

    private ItemInfoData OneItemInfoGet(Node node)                //参数node代表的标签是<li>
            throws ParserException, IOException 
    {
        
        if(!node.getText().startsWith("l")){
            //System.out.println("无法检测到商品信息！");
                return null;
        }
        
        char[] jdid=node.getText().toCharArray();                                               //获取京东商品ID
        int sta=0,end=jdid.length-1;
        while(jdid[end]!=34)
            end--;
        sta=end-1;
        while(jdid[sta]!=34)
            sta--;
        ItemInfoData iteminfo=new ItemInfoData();
        iteminfo.jdid=new String(jdid,sta+1,end-sta-1);
        //System.out.println(iteminfo.jdid);
        
        Parser itemInfoParser=new Parser(node.toHtml(true));
        //NodeFilter infoFilter= new MyFilter2Str("div class=\"p-img\"",          //过滤简介和图片标签及其子标签部分
              //  "div class=\"p-name\"",true);
        NodeFilter[] filter=new NodeFilter[3];
        filter[0]=new MyFilterSTART_1times("img",true);
        filter[1]=new MyFilterSTART("div class=\"p-n",true);
        filter[2]=new MyFilterSTART_1times("￥",true);
        
        //HasParentFilter infoFilter_1=new HasParentFilter(new OrFilter(filter),true); 
     //   for(int i=0;i<node.getChildren().size();i++)
          //  System.out.println(node.getChildren().elementAt(i).getText());
        NodeList infoList=itemInfoParser.extractAllNodesThatMatch(              //获取父标签是p-image和p-name的子标签列表
                new OrFilter(filter));
        
        //System.out.println(infoList.size());
        if(infoList.size()==0){
            //System.out.println("无法检测到商品信息！");
            return null;
        }
        
        
        
            
     
       /* int index=1;
        Node[] infoListarray=infoList.toNodeArray();
        while(!infoListarray[index].getText().startsWith("img"))            //获取imag标签node
            index++;
        ImageTag itemLink_1=new ImageTag();
        itemLink_1.setText(infoListarray[index].toHtml().                  //node的setText()方法的参数必须是toHtml()后的字符串
                replace("data-lazyload","src"));
        iteminfo.itemImage=itemLink_1.getImageURL();                              //获取商品图片url
       
        while(!infoListarray[index].getText().startsWith("a tar"))
            index++;
        LinkTag itemLink=(LinkTag)infoListarray[index];
        iteminfo.itemUrl=itemLink.getLink();                                     //获取商品页面url
        iteminfo.itemIntro="<font class=\"skcolor_ljg\">京东：</font>"+infoListarray[index].getChildren().toHtml();     //获取商品简介
         while(!infoListarray[index].getText().startsWith("￥"))
            index++;
        iteminfo.itemPrice=infoListarray[index].getText();                      //获取商品价格  
    */
        ImageTag itemLink_1=new ImageTag();
        itemLink_1.setText(infoList.elementAt(0).toHtml().                 //node的setText()方法的参数必须是toHtml()后的字符串
                replace("data-lazyload","src"));
        iteminfo.itemImage=itemLink_1.getImageURL();                              //获取商品图片url
        //System.out.println(infoList.elementAt(1).getText());
        Node pname=infoList.elementAt(1).getFirstChild();
        //System.out.println(pname.toHtml());
       // if(pname.getText()==null)
        while(!pname.getText().startsWith("a"))
            pname=pname.getNextSibling();
         
         iteminfo.itemUrl=((LinkTag)pname).getLink();
         iteminfo.itemIntro="<font class=\"skcolor_ljg\">京东：</font>"+pname.getChildren().toHtml();
        
        
        if(infoList.elementAt(2)!=null) 
             iteminfo.itemPrice=infoList.elementAt(2).getText();
        else
            iteminfo.itemPrice=null;
        
        return iteminfo;
        
    }
    
    /*private String GetItemPrice(Node node) throws ParserException{
    
        Parser itemPageParser= new Parser(node.toHtml());
        NodeFilter priceFilter= new MyFilterSTART_1times("strong",true);
        NodeList pricelist=itemPageParser.extractAllNodesThatMatch(priceFilter);
        Node price=pricelist.elementAt(0).getFirstChild();
        if(price!=null)
            return pricelist.elementAt(0).getFirstChild().getText();
        else
            return pricelist.elementAt(0).getNextSibling().getText();
        
    
    }*/
    

     private void GetPageNum(Node node){                             //获取商品总页数
     
        //System.out.println("页面内容标签"+node.getText());
        Node pagenum=node.getFirstChild();
       //System.out.println(pagenum.getText());
       while(pagenum.getText()==null||!pagenum.getText().endsWith("text\""))
            pagenum=pagenum.getNextSibling();
        //System.out.println(pagenum.getFirstChild().getText());
      
        char[] temp=pagenum.getChildren().toHtml().toCharArray();
         //System.out.println(temp);
        int index=temp.length-1;
        while(temp[index]!=47)
            index--;
        String numstr=new String(temp,index+1,temp.length-index-1);
        //System.out.println(numstr);
       pagedata.jdpage=GetItemFromWeb.StringtoInt(numstr);
        System.out.println("京东商品页数："+pagedata.jdpage);
        
     }
    
   /* private String GetItemPrice(String itemurl) 
            throws ParserException, IOException
    {
        Parser itemPageParser= new Parser(itemurl);
        itemPageParser.setEncoding("gb2312");
        //System.out.println(itemurl);
       
        if(!itemurl.startsWith("http://book"))                                  //如果商品非图书
        {
            NodeList itemInfoList=itemPageParser.parse(
                new MyFilter_1time("function changeImgP",true));
            if(itemInfoList.elementAt(0)==null)                                  //非普通商品直接过滤掉
               return null;
           // System.out.println(script);
            String script=itemInfoList.elementAt(0).getText();
            char[] charscpt =script.toCharArray();
            //for(int i=0;i<charscpt.length;i++)
            //{
               // System.out.println(i);
               // System.out.println(charscpt[i]);
           // }
          
            int start=0,end=0;                                                         
            while((charscpt[start]!='h'||charscpt[start+1]!='t'||
               charscpt[start+2]!='t'||charscpt[start+3]!='p')&&
               start<charscpt.length)
            {
                start++;
               // System.out.println(start);
            }
            if(start==charscpt.length)
            {
                System.out.println("商品"+itemurl+"URL提取错误");
                return null;
            }
            else
            {
                end=start;
                while((charscpt[end]!='2'||charscpt[end+1]!='N'||
                        charscpt[end+2]!='u'||charscpt[end+3]!='m')&&
                        end<charscpt.length)
                end++;
                end=end+3;
            //System.out.println(end);
                String priceUrl= new String(charscpt,start,end-start+1);
           //System.out.println(priceUrl);
                return "￥"+GetPriceChar(priceUrl);
            } 
        }
        else                                                                    //商品是图书
        {
             NodeList itemInfoList=itemPageParser.extractAllNodesThatMatch(
                new MyFilter_1time("del",true));
             return itemInfoList.elementAt(0).getNextSibling().getText();
        }
            
        
   }*/
   /* private String GetPriceChar(String priceUrl) 
            throws ParserException, IOException
    {
        byte[] priceText=MyHtmlParser.GetHtmlText(priceUrl);
        int start=0,end=0,istwo=0;
        while(istwo<3)
        {
            if((char)priceText[start]=='"')
                istwo++;
            start++;
        }
        end=start;
        while((char)priceText[end]!='"')
            end++;
        return new String(priceText,start,end-start);
    }*/

    private void UpdatePrice(ItemInfoData[] itemlist) throws IOException {
    
        int maxindex=itemlist.length-1;
        StringBuffer priceurl=new StringBuffer("http://p.3.cn/prices/mgets?skuids=");
        for(int i=0;i<=maxindex;i++){
            if(itemlist[i]!=null){
                priceurl=priceurl.append("J_").append(itemlist[i].jdid).append(",");
            }
            else
                break;
        }
        priceurl.append("&type=1");
        int[] priceindex=new int[36];                                                                   //存储每件商品价格信息起始索引
        //System.out.println(priceurl.toString());
        byte[] mget=new MyHtmlParser(priceurl.toString()).GetHtmlText();
        //System.out.println(new String(mget));
        maxindex=mget.length-1;
        for(int i=0,j=0;i<=maxindex;i++){
            if(mget[i]==123){
                priceindex[j]=i+6;
                j++;
            }
        }
   
        int i=0;
        int sta=0;  
        while(i<36&&priceindex[i]!=0){                                              //获取每件商品的价格
           sta=priceindex[i];
            while(mget[sta]!=34)
                sta++;
            itemlist[i].itemPrice=new String(mget,priceindex[i],sta-priceindex[i]);
            i++;
        }
    }
}
