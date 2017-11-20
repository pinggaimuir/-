<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/24
  Time: 18:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆成功提示页面</title>
</head>
<body>
    登陆成功!<br>
    您提交的信息为：<br/>
   用户名： <%=request.getParameter("uname")%><br/>
    密码： <%=request.getParameter("upwd")%><br/>
    <a href="Login.jsp">返回登陆页面</a>
</body>
</html>
