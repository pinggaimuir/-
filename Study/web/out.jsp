<%--
  Created by IntelliJ IDEA.
  User: gao
  Date: 2016/8/25
  Time: 20:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page buffer="10kb" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        out.println("当前缓冲区大小："+out.getBufferSize());
        out.println("当前缓冲区的剩余字节数："+out.getRemaining());
        out.println(session.getId());
    %>
</body>
</html>
