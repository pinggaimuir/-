<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/1
  Time: 12:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="me" uri="http://mycompany.com"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%=session.getAttribute("gao" + "" +"")%>
<c:out value="15" default="0"/>
    <%pageContext.setAttribute("myname","gaojian");
           application.setAttribute("myname1","ning");

    %>
    <%=pageContext.getAttribute("myname1",4)%>
    <%=pageContext.getServletContext().getInitParameter("param1")%><br/>
<%--${sessionScope.myname1}--%>
${cookie.JSESSIONID.value}
    <%--//获取配置参数--%>
    ${initParam.param1}
    ${fn:toUpperCase("dfgfdsgf")}

${header.host}20
    <a href="http://www.tmooc.cn">TMOOC</a>
<c:out  value="<a href='http://www.tmooc.cn'>TMOOC</a>" escapeXml="true"></c:out>
<c:out value="${myname}" default=""></c:out>
</body>
</html>
