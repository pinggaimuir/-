<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/2
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>
<%@page import="java.util.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="me" uri="http://mycompany.com" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>自定义Tag</title>
</head>
<body>
    <me:sip/>
    <%Date date=new Date();
        pageContext.setAttribute("date",date,pageContext.REQUEST_SCOPE);
    %>
    <fmt:formatDate value="${date}" pattern="HH:mm:ss"/>
    <c:choose>
        <c:when test="${3>2}">
            ${"3da"}
        </c:when>
        <c:otherwise>
            ${"3budas"}
        </c:otherwise>

    </c:choose>
    <%
        Cookie cookie=new Cookie("JSESSIONID",session.getId());
        cookie.setPath(request.getContextPath()+"/");
        cookie.setMaxAge(60);
        response.addCookie(cookie);

    %>
    <c:out value="${cookie.JSESSIONID}" default="mei na dao" escapeXml="true"></c:out>
    <%--<c:set target="${cookie}" property="JSESSIONID" value="sessionid" ></c:set>--%>
    <c:out value="${cookie.JSESSIONID}" default="又没获取到" ></c:out>
</body>
</html>
