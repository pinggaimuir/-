<%@ page import="August_27.Bean1" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/2
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <c:if test="${3>2}" var="flag" scope="page">yes</c:if>
    <c:if test="${flag}"> no</c:if>
    <%--<c:if test="${user.visitCount}">欢迎第一次造访</c:if>--%>
<%--choose相当与IF()-else if()- esle--%>
    <c:set var="weekday" value="8" scope="page"/>
<c:choose>
    <c:when test="${weekday==1}">星期一</c:when>
    <c:when test="${weekday==2}">星期二</c:when>
    <c:when test="${weekday==3}">星期三</c:when>
    <c:when test="${weekday==4}">星期四</c:when>
    <c:when test="${weekday==5}">星期无</c:when>
    <c:otherwise >
        想起日
    </c:otherwise>
</c:choose>
<%--<c:forEach var="" items="" varStatus="" begin="" end='"" step="">--%>
<%String names[]={"唐僧","猪八戒","小白龙","白骨精"};
    pageContext.setAttribute("names",names);
    List<String> list=new ArrayList();
    list.add("xiaobai");
    list.add("xiaohei");
    list.add("xiaohong");
    pageContext.setAttribute("list",list);
%>
<c:forEach var="name" items="${names}" >${name}</c:forEach>
<c:forEach var="li" items="${list}" >${li}<br/></c:forEach>

<%--循环对象--%>
<%
    List<Bean1> list1=new ArrayList();
    list1.add(new Bean1("gaojian1",23,"吕梁学院"));
    list1.add(new Bean1("gaojian2",24,"吕梁小院"));
    list1.add(new Bean1("gaojian3",25,"皇家吕梁学院"));
    pageContext.setAttribute("list1",list1);
%>
    <table>
<c:forEach var="bean" items="${list1}" varStatus="stat">
    <tr><td>${bean.name}</td><td>${bean.age}</td><td>${bean.addr}</td><td>${stat.count}</td></tr>
</c:forEach>
    </table>
<%--遍历Map--%>
<%
    Map map2=new HashMap();
    map2.put("1",new Bean1("gaojian",23,"临汾"));
    map2.put("2",new Bean1("lining",22,"古县"));
    map2.put("3",new Bean1("panle",22,"阳泉"));
    pageContext.setAttribute("map2",map2,3);
%>
<c:forEach items="${map2}" var="entry" >
    ${entry.key}===========${entry.value.age}==${entry.value.age}==${entry.value.addr}&nbsp;&nbsp;&nbsp;&nbsp;
</c:forEach>
    <br/>
<c:forEach var="i" begin="10" end="99" step="1" varStatus="stat">
    <c:if test="${stat.count%3==0}" var="flag">
      <span style="color:red"> ${i}</span>
    </c:if>
    <c:if test="${!flag}">
        ${i}
    </c:if>
</c:forEach>
<%--forTokens--%><br/>
<%%>
<c:forTokens var="token" items="www.baidu.com" delims=".">${token}dfghsdfgdfgdf--------</c:forTokens>
</body>
</html>
