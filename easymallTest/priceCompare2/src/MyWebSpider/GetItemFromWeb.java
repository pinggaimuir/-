/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyWebSpider;

import InfoData.ItemInfoData;
import InfoData.PageInfoData;
import MyWebSpider.AmazonSpider.AZInfoGet;
import MyWebSpider.JDSpider.JDInfoGet;
import org.htmlparser.util.ParserException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author MZH
 */
public class GetItemFromWeb {

    
    HttpServletRequest request;
    public PageInfoData pagedata=new PageInfoData();
   
    public GetItemFromWeb(HttpServletRequest request1) throws UnsupportedEncodingException{
        
        request=request1;
        
        pagedata.page=StringtoInt(request.getParameter("page")) ;
        pagedata.key_utf8= request.getParameter("key");
        if(pagedata.key_utf8!=null)
            if(pagedata.key_utf8.length()==0)
                pagedata.key_utf8=null;
        System.out.println("当前页码："+pagedata.page);        
    }
   
    
    private ItemInfoData[] GetFromURL(String urljd,String urlaz) throws ParserException, IOException, InterruptedException{
    
            InfoGetRunnable jdthread=null;
            if(urljd!=null){
                    jdthread=new InfoGetRunnable(new JDInfoGet(pagedata,urljd));
                    new Thread(jdthread).start();
                    }
            
            InfoGetRunnable azthread=null;
            if(urljd!=null){
                    azthread=new InfoGetRunnable(new AZInfoGet(pagedata,urlaz));
                    new Thread(azthread).start();
                    }
            
           /* ItemInfoData[] itemlistaz=null;
            AZInfoGet azinfoget=null;
            if(urlaz!=null){
                     azinfoget=new  AZInfoGet();
                     azinfoget.SETpagedata(pagedata);
                     itemlistaz=azinfoget.GetInfo(urlaz);
                    }
           
           if(jdthread==null)
                    return itemlistaz;
                else 
                    if(azinfoget==null){
                            while(jdthread.over==false){
                                //System.out.println("睡眠一次");
                                Thread.sleep(500);
                            }
                             return jdthread.itemlist;
                            }
                    else{
                            while(jdthread.over==false){
                                //System.out.println("睡眠一次");
                                Thread.sleep(1000);
                            }
                            ItemInfoData[] result=new ItemInfoData[52];
                            for(int i=0,j=0;i<36;i++){
                                if(jdthread.itemlist!=null&&jdthread.itemlist[i]!=null){
                                    result[j]=jdthread.itemlist[i];
                                    j++;
                                }
                                if(i<16&&itemlistaz!=null&&itemlistaz[i]!=null){
                                    result[j]=itemlistaz[i];
                                    j++;
                                }
                             }
                            return result;
                    }*/
            
            if(jdthread==null){
             while(azthread.over==false){
                                //System.out.println("睡眠一次");
                                Thread.sleep(1000);
                            }
             BubbleSortSort(azthread.itemlist);
             return azthread.itemlist;
             }
            else if(azthread==null){
                while(jdthread.over==false){
                                //System.out.println("睡眠一次");
                                Thread.sleep(1000);
                            }
                BubbleSortSort(jdthread.itemlist);
                return jdthread.itemlist;
            }
                else{
                while(jdthread.over==false||azthread.over==false)
                     Thread.sleep(1000);
                    ItemInfoData[] result=new ItemInfoData[52];
                            for(int i=0,j=0;i<36;i++){
                                if(jdthread.itemlist!=null&&jdthread.itemlist[i]!=null){
                                    result[j]=jdthread.itemlist[i];
                                    j++;
                                }
                                if(i<16&&azthread.itemlist!=null&&azthread.itemlist[i]!=null){
                                    result[j]=azthread.itemlist[i];
                                    j++;
                                } 
                            }
                System.out.println("爬取结果"+result);
                            BubbleSortSort(result);
                            return result;
            }
            
     } 
    
    private ItemInfoData[] GetFromSearch() 
            throws UnsupportedEncodingException, ParserException, IOException, InterruptedException{
    
        byte[] content= pagedata.key_utf8.getBytes("utf-8"); 
        pagedata.key_gbk = new String(content,"GBK");
        
        
        /*String pageurl="http://search.jd.com/Search?keyword="+pagedata.key_gbk+"&enc=utf-8&area=17";
        System.out.println("首页使用gbk："+pageurl);
         return new JDInfoGet(pagedata).GetInfo(pageurl);*/
       
        //System.out.println("首页使用gbk："+pageurl);
       
        String pageurljd="http://search.jd.com/Search?keyword="+pagedata.key_gbk+"&enc=utf-8&area=17";
        
        String pageurlaz="http://www.amazon.cn/s/ref=nb_sb_noss_1?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%"
                + "8A%E7%BD%91%E7%AB%99&url=search-alias%3Daps&field-keywords="+pagedata.key_utf8;
         
         return GetFromURL(pageurljd,pageurlaz);
        
    }
    
    
    private ItemInfoData [ ] GetFromNeighbor()
            throws ParserException, IOException, InterruptedException{
    
        pagedata.jdpage=StringtoInt(request.getParameter("JD"));
        pagedata.azpage=StringtoInt(request.getParameter("AZ"));
         /*System.out.println("使用utf-8");
        String pageurl="http://search.jd.com/search?keyword="+pagedata.key_utf8+"&qr=&qrst=UNEXPAND&et=&rt=1&area=17&page="+pagedata.page;
        ItemInfoData[] result =null;
                result =new JDInfoGet(pagedata).GetInfo(pageurl);
                if(result==null){
                    System.out.println("utf8行不通使用gbk");
                    pageurl="http://search.jd.com/search?keyword="+pagedata.key_gbk+"&qr=&qrst=UNEXPAND&et=&rt=1&area=17&page="+pagedata.page;
                    return new JDInfoGet(pagedata).GetInfo(pageurl);
                }
                else 
                    return result;*/
        //System.out.println("使用utf-8");
       // String pageurl="http://www.amazon.cn/s/ref=sr_pg_2?page="+pagedata.page+"&keywords="+pagedata.key_utf8+"&ie=UTF8";
       // ItemInfoData[] result =null;
                //result =new AZInfoGet(pagedata).GetInfo(pageurl);
               /* if(result==null){
                    System.out.println("utf8行不通使用gbk");
                    pageurl="http://www.amazon.cn/s/ref=sr_pg_2?page="+pagedata.page+"&keywords="+pagedata.key_gbk+"&ie=UTF8";
                    return new AZInfoGet(pagedata).GetInfo(pageurl);
                }
                else 
                    return result;*/
           // return result;
            String pageurljd=null;
            String pageurlaz=null;
             if(pagedata.page<=pagedata.jdpage)
                pageurljd="http://search.jd.com/search?keyword="+pagedata.key_utf8+"&qr=&qrst=UNEXPAND&et=&rt=1&area=17&page="+pagedata.page;
             if(pagedata.page<=pagedata.azpage) 
                pageurlaz="http://www.amazon.cn/s/ref=sr_pg_2?page="+pagedata.page+"&keywords="+pagedata.key_utf8+"&ie=UTF8";
            
             return GetFromURL(pageurljd,pageurlaz);
    }
    
    
    public ItemInfoData[] GetInfo() 
            throws UnsupportedEncodingException, ParserException, IOException, InterruptedException{
       // if(request.getParameter("Search").equals("1
            System.out.println("搜索关键词："+pagedata.key_utf8);
            if(pagedata.page==1)
                return GetFromSearch();
            else
                return GetFromNeighbor();
           }
    
   private void BubbleSortSort(ItemInfoData[] itemlist){
       
       System.out.println("开始排序！");
       int maxindex=itemlist.length-1;
       while(itemlist[maxindex]==null)
           maxindex--;
       
      for(int i=0;i<maxindex;i++){
          int time=0;
          for(int j=maxindex;j>i;j--){
              if(ComparetwoString(itemlist[j-1].itemPrice,itemlist[j].itemPrice)){
                  ItemInfoData mintemp=itemlist[j-1];
                  itemlist[j-1]=itemlist[j];
                  itemlist[j]=mintemp;
                  time++;
            }
          }
          if(time==0)break;
      }
   }
   private boolean ComparetwoString(String str1,String str2){
       int len1=str1.length();
       int len2=str2.length();
       if(len1>len2)
           return true;
       else 
           if(len2>len1)
               return false;
            else{
                boolean str1larger=true;
                for(int i=0;i<len1;i++){
                if(str1.charAt(len1-1-i)<str2.charAt(len1-1-i))
                    str1larger=false;
                 else
                    str1larger=true;
                }
                return str1larger;
           }
    }
    
    public static int StringtoInt(String num){
   
       if(num==null)
           return 1;
       else{
       
           int number=0;
           int rate=1;
            //System.out.println(num);
           for(int index=num.length()-1;index>=0;index--){
                //System.out.println (num.charAt(index));
               if(num.charAt(index)==44)
                   continue;
                number=number+(num.charAt(index)-48)*rate;
                rate=rate*10;
        }
      return number; 
           
       }
    } 
}
