<%-- 
    Document   : index
    Created on : 2013-5-12, 19:33:49
    Author     : mazhenhao
--%>

<%@page import="InfoData.ItemInfoData"%>
<%@page import="MyWebSpider.GetItemFromWeb"%>
<%@page contentType="text/html" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html" charset="utf-8"/>
        <link rel="stylesheet" type="text/css" href="jdbase.css" media="all" />
        <link rel="stylesheet" type="text/css" href="jdpsearch20130409.css" media="all">
        <link rel="stylesheet" type="text/css" href="jdpop_compare.css" media="all" />
        
        
        <style type="text/css">
        .skcolor_ljg {color:#FF0000;}
        #plist .reputation{display:inline;}
        </style>
        
        
        
        
        
        <title>商品搜索结果</title>
    </head>
    <body class="root61">
         
        <div id="o-header-2013">
            <div class="w" id="header-2013">
                <div id="search-2013">
                    <div class="i-search ld">
                        <ul id="shelper" class="hide"></ul>
                        <div class="form">
                    <form action="search.jsp" accept-charset="utf-8">
                            <input  class="text" type="text" name="key" />
                            <input class="button" type=submit value="搜索" >
                    </form>
         
            </div></div></div></div></div>
        
        
        
            <div class="w main">
     
        <%
      
       GetItemFromWeb getinfo=new GetItemFromWeb(request);
        if(getinfo.pagedata.key_utf8!=null)
       {
            ItemInfoData[] itemlist=getinfo.GetInfo();
            if(itemlist!=null&&itemlist[0]!=null)
            {
                 int size=itemlist.length;
                 
                 
                 out.println("<div class=\"m psearch\" id=\"plist\">");
                 out.println("<ul class=\"list-h clearfix\">");
                 for(int i=0;i<size;i++)
                 {
               //System.out.println(i);
                    if(itemlist[i]!=null)
                    {
                        
                        /*out.println("</br><a target=\"_blank\" href=\""+itemlist[i].itemUrl+"\">");
                        out.println("<img width=\"160\" height=\"160\" data-img=\"1\" src=\""+itemlist[i].itemImage+"\" />");
                        out.println("</a>");
                        out.println("</br>"+itemlist[i].itemPrice);
                        out.println("</br>"+itemlist[i].itemIntro+"</br>");*/
                        out.println("<li>");
                        out.println("<div class=\"p-img\">");
                        out.println("<a target=\"_blank\"href=\""+itemlist[i].itemUrl+"\">");
                        out.println("<img width=\"160\" height=\"160\" src=\""+itemlist[i].itemImage+" \"/>");
                        out.println("</a></div><div class=\"p-name\">");
                        out.println("<a target=\"_blank\" href=\""+itemlist[i].itemUrl+"\">");
                        out.println(itemlist[i].itemIntro);
                        out.println("<font style='color:#ff0000;margin-left:5px;' class='adwords' ></font>");
                        out.println("</a></div><div class=\"p-price\" ><em></em><strong>"+itemlist[i].itemPrice+"</strong></div></li>");
                    }
                    else
                        break;
                   }
                    
                 out.println("</ul>");
                 out.println("</div>");
                   
                    
                    int nextpagenum=0;
                    int prepagenum=0;
                    int totalnum=0;
                    String nextpage=null;
                    String prepage=null;
                     if(getinfo.pagedata.jdpage>getinfo.pagedata.azpage)
                            totalnum=getinfo.pagedata.jdpage;
                     else
                         totalnum=getinfo.pagedata.azpage;
                     if(getinfo.pagedata.page>1)
                         prepagenum=getinfo.pagedata.page-1;
                     if(getinfo.pagedata.page<totalnum)
                         nextpagenum=getinfo.pagedata.page+1;
                     
                     
                         
                        out.println("<div class=\"m clearfix\" id=\"bottom_pager\">");
                        out.println("<div id=\"pagin-btm\" class=\"pagin fr\" clstag=\"search|keycount|search|pre-page2\">");
                    
                        if(prepagenum>0){
                               
                            prepage="search.jsp?key="+getinfo.pagedata.key_utf8+"&page="+prepagenum+"&JD="+getinfo.pagedata.jdpage+"&AZ="+getinfo.pagedata.azpage;
                            out.println("<a class=\"prev\" charset=\"utf-8\"  href=\""+prepage+"\" target=\"_self\" rel=\"nofollow\">上一页<b></b></a>");
                         }
                            
                        if(nextpagenum>0){
                            
                            nextpage="search.jsp?key="+getinfo.pagedata.key_utf8+"&page="+nextpagenum+"&JD="+getinfo.pagedata.jdpage+"&AZ="+getinfo.pagedata.azpage;
                            out.println("<a class=\"next\" charset=\"utf-8\"  href=\""+nextpage+"\" target=\"_self\" rel=\"nofollow\">下一页<b></b></a>");
                        }
                        
                        
                        out.println("<span class=\"page-skip\"><em>&nbsp;&nbsp;共"+totalnum+"页&nbsp;&nbsp;&nbsp;&nbsp;</em>");
                        out.println("<em>&nbsp;&nbsp;当前第"+getinfo.pagedata.page+"页</em>");
                        out.println("</span></div></div>");
             }
            else
              out.println("没有搜索到商品！");
            
            out.println("<div id=\"re-search\" class=\"m\">");
            out.println("<dl><dt>重新搜索</dt><dd>");
            out.println("<form action=\"search.jsp\" accept-charset=\"utf-8\">");
            out.println("<input type=\"text\" class=\"text\" name=\"key\" />");
            out.println("<input type=submit  class=\"button\" value=\"搜索\" >");
            out.println("</form>");
            out.println("</dd>");
       }
       else
            out.println("请输入关键字查询");
       
           
       
      %>
      
           </div> 
            
    </body>
</html>
