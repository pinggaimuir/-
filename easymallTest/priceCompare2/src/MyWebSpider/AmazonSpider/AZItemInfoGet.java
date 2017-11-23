/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider.AmazonSpider;

import MyWebSpider.GetItemFromWeb;
import InfoData.ItemInfoData;
import MyWebSpider.ItemInfoGetInteface;
import MyFilter.MyFilterEND_1times;
import MyFilter.MyFilterSTART_1times;
import InfoData.PageInfoData;
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
public class AZItemInfoGet implements ItemInfoGetInteface{
    
    
    PageInfoData pagedata;

    public   AZItemInfoGet(PageInfoData pagedata1){
    
        pagedata=pagedata1;
    
    }

    @Override
    public ItemInfoData[] GetArrayFromList(NodeList pagItemList) throws ParserException, IOException {
       
        if(pagItemList!=null){
            if(pagedata.page==1)
               GetPageNum(pagItemList.elementAt(0));             //获取商品页数
            
            
            
             ItemInfoData[] itemlist = new ItemInfoData[16];
             int num=pagItemList.size()-1;
             for(int i=0,j=1;j<=num;i++,j++){
                 ItemInfoData iteminfo=OneItemInfoGet(pagItemList.elementAt(j));
                 if(iteminfo==null){
                     i--;   
                     continue;
                 }
                    
                 itemlist[i]=iteminfo;
                 //System.out.println("搞定"+i);
                 }
          
             return itemlist;
         }
        else
            return null;
        
        
    }


    private ItemInfoData OneItemInfoGet(Node node) throws ParserException, IOException {
    
         Parser itemInfoParser=new Parser(node.toHtml());
         NodeFilter[] filter=new NodeFilter[3];
         filter[0]=new MyFilterSTART_1times("img",true);
         filter[1]=new MyFilterEND_1times("productTitle\"",true);
         filter[2]=new MyFilterEND_1times("newPrice\"",true);
         NodeList infolist=itemInfoParser.extractAllNodesThatMatch(new OrFilter(filter));
          
         ItemInfoData iteminfo=new ItemInfoData();
         
         iteminfo.itemImage=((ImageTag)infolist.elementAt(0)).getImageURL();                                 //获取图片链接     
         
          if(infolist.elementAt(1)==null){
            
             return null;
         }
         Node itemurlandintro=infolist.elementAt(1).getFirstChild();                                                    //获取商品链接
         while(!itemurlandintro.getText().startsWith("a"))
              itemurlandintro= itemurlandintro.getNextSibling();
          iteminfo.itemUrl= ((LinkTag)itemurlandintro).getLink();                                                       
          
                                                             
         
          if( itemurlandintro.getFirstChild()==null)                                                                             //获取商品简介
              iteminfo.itemIntro="<font class=\"skcolor_ljg\">亚马逊：</font>"+ itemurlandintro.getNextSibling().getText();
         else
               iteminfo.itemIntro="<font class=\"skcolor_ljg\">亚马逊：</font>"+itemurlandintro.getFirstChild().getText();
            
            if(infolist.elementAt(2)==null){
            
                iteminfo.itemPrice="无货";
                return iteminfo;
            }
            itemurlandintro=infolist.elementAt(2).getFirstChild();                                                                               //获取商品价格
            //System.out.println(itemurlandintro.getText());
            while(itemurlandintro.getText()!=null&&!itemurlandintro.getText().startsWith("sp"))
                itemurlandintro=itemurlandintro.getNextSibling();
            String pricetemp;
            if(itemurlandintro.getFirstChild()==null)
                pricetemp=itemurlandintro.getNextSibling().getText();
            else
                pricetemp=itemurlandintro.getFirstChild().getText();
            StringBuffer pricetmp=new StringBuffer();
            for(int i=0;i<pricetemp.length();i++)
                if((47<pricetemp.charAt(i)&&pricetemp.charAt(i)<57)||pricetemp.charAt(i)==46)
                    pricetmp=pricetmp.append(pricetemp.charAt(i));
            iteminfo.itemPrice=pricetmp.toString();
            //System.out.println("价格："+iteminfo.itemPrice);
             
            return iteminfo;
                
    }


    private void GetPageNum(Node node) {
  
        Node numnode;
        if(node.getFirstChild()==null)
            numnode=node.getNextSibling();
        else
           numnode=node.getFirstChild();
        String numinfo=numnode.getText();
        
        int st=0,en=0;
        //numinfo=numinfo.replaceFirst("共", "&");
        st=numinfo.indexOf("共")+1;
        en=numinfo.length()-1;
        //System.out.println(st);
        //System.out.println(en);
        //System.out.println(numinfo);
       // System.out.println(numinfo.substring(st, en));
        int itemnum=GetItemFromWeb.StringtoInt(numinfo.substring(st, en));
        if(itemnum%16==0)
            pagedata.azpage=itemnum/16;
        else
            pagedata.azpage=itemnum/16+1;
        System.out.println("亚马逊商品页数："+pagedata.azpage);
        
     }
    
}
