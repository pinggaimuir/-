<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/28
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <table>
        <tr>
            <th>姓名</th>
            <th>密码</th>
            <th>性别</th>
            <th>年龄</th>
            <th>生日</th>
        </tr>
        <c:forEach items="${userList}" var="user">
            <tr>
                <td>${user.username}</td>
                <td>${user.password}</td>
                <td>${user.userInfo.sex}</td>
                <td>${user.userInfo.age}</td>
                <td><fmt:formatDate value="${user.userInfo.birthday}"/></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
