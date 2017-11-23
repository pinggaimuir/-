<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/26
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>application</title>
</head>
<body>
<%=application.getServerInfo()%><br/>
<%=application.getServletContextName()%><br/>
<%
    Object obj=application.getAttribute("counter");
    if(obj==null){
        application.setAttribute("counter",new Integer(1));
        out.println("页面被访问了1次<br/>");
    }else{
        int counterValues=Integer.parseInt(obj.toString());
        counterValues++;
        out.println("页面被访问了"+counterValues+"次<br/>");
        application.setAttribute("counter",counterValues);
    }
%>
</body>
</html>
