<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/24
  Time: 18:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆时被提示页面</title>
</head>
<body>
登陆失败!<br>
错误提示:
<%
    Object obj=request.getAttribute("msg");
    if(obj!=null){
        System.out.println(obj.toString());
    }else{
        System.out.println("无");
    }
%>
您提交的信息为：<br/>
用户名： <%=request.getParameter("uname")%><br/>
密码： <%=request.getParameter("upwd")%><br/>
<a href="Login.jsp">返回登陆页面</a>

</body>
</html>
