<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/2
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@page import="java.util.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>fmt</title>
</head>
<body>
<%Date date=new Date();
    pageContext.setAttribute("date",date,4);
%>
<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate><br/>
<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"></fmt:formatDate>
<fmt:formatNumber value="121242545" pattern=""></fmt:formatNumber>

</body>
</html>
