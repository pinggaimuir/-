<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/27
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<link rel="stylesheet" href="css/foot.css"/>
<div id="common_foot">
    <p>

     <h1 style="text-align: center">错误页面！</h1>
            <h4 style="text-align: center"><%=exception.getMessage()%></h4>

    </p>
</div>
</body>
</html>
