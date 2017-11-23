<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/30
  Time: 19:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! int amont=10;%>
<html>
<head>
    <!---我的就是你的-->
    <title>Title</title>
</head>
<body>
<%amont++;%>

<%=amont++%>
<%--<%--通过url传递JSESSIONID的值给服务器%>
<%--//    HttpSession session=request.getSession();--%>
   <%--String url1= response.encodeURL(request.getContextPath()+"/BuyServlet?prop=阿迪王皮鞋");--%>
   <%--String url2= response.encodeURL(request.getContextPath()+"/BuyServlet?prop=海尔洗衣机");--%>
   <%--String url3= response.encodeURL(request.getContextPath()+"/PayServlet");--%>
<%--%>--%>
<%--<br/>--%>

    <%--<a href=<%=url1%>>阿迪王皮鞋</a><br/><br/>--%>
    <%--<a href=<%=url2%>>海尔洗衣机</a><br/><br/>--%>
    <%--<a href=<%=url3%>>支付</a><br/><br/>--%>
<%=request.getParameter("jian")%>
    <a href="<%=request.getContextPath()%>/BuyServlet">阿迪王皮鞋</a><br/><br/>
    <a href="<%=request.getContextPath()%>/BuyServlet">海尔洗衣机</a><br/><br/>
    <a href="<%=request.getContextPath()%>/PayServlet">支付</a><br/><br/>
<% out.write(session.getAttribute("seg")+",");%>
<%--<%session.invalidate();--%>
    <%--out.print(session.getAttribute("seg"));--%>
<%--%>--%>
<%=request.getParameter("nfgsd")%>
<%session.setAttribute("gao","jian");%>
</body>
</html>
