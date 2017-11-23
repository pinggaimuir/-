<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/6
  Time: 9:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    out.print(request.getSession().getAttribute("xxx"));

%>
</body>
</html>
