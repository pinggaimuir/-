<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/6
  Time: 9:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    request.getSession().setAttribute("xxx","session去火星了");
    request.setAttribute("gao","jian");
    request.setAttribute("gao","dfgdsfgndsfjkg");
    request.removeAttribute("gao");
%>
</body>
</html>
