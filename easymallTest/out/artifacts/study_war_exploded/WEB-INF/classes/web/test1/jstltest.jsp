<%@ page import="August_27.Bean1" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/2
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <c:set var="fruit" value="西瓜刀" scope="page"/>
    <%--<c:remove var="fruit" scope="page"></c:remove>--%>
    <%--//如果与对象中存在该属性则覆盖--%>
    <c:set var="fruit" value="大砍刀" scope="page"/>
    <c:out value="${fruit}" default="ming" escapeXml="false"/>
    <%--//向Map中添加值--%>
    <% Map<String,String> map=new HashMap();
            pageContext.setAttribute("map",map);
        Bean1 bean=new Bean1();
        pageContext.setAttribute("bean",bean);
    %>
<c:set target="${map}" property="name1" value="张无忌"/>
<c:set target="${map}" property="name2" value="赵敏"/>
<c:set target="${bean}" property="age" >15</c:set>
    <c:remove var="bean" scope="page"/>
<c:out value="${bean.age}"/>
<%--删除 入不指定scope 删除所有--%>
<c:set var="hero" value="盖伦" scope="page"/>
<c:set var="hero" value="德邦" scope="request"/>
<c:set var="hero" value="狐狸" scope="session"/>
<c:set var="hero" value="骚" scope="application"/>
<c:remove var="hero" scope="page"></c:remove>
<c:remove var="hero"></c:remove>
<c:out value="${hero}"/>
</body>
</html>
