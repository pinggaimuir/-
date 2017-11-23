<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/6
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/DiskFileServlet" method="post" enctype="multipart/form-data">
    用户名：<input type="text" name="username1"/>
    密码：<input type="text" name="password1"/>
    附件1：<input type="file" name="file1"/>
    附件2：<input type="file" name="file2"/>
    <input type="submit" value="提交"/>
</form>
</body>
</html>
