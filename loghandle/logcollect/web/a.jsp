<%--
  Created by IntelliJ IDEA.
  User: gao
  Date: 2017/1/8
  Time: 22:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>页面A</title>
    <script type="application/javascript" src="<%=request.getContextPath()%>/js/tongji.js"></script>
</head>
<body>
        <span>页面A</span>
        AAAAAAAAAAAAAAAAAA
        <a href="${pageContext.request.contextPath}/b.jsp">BBB</a>
</body>
</html>
