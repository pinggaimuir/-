<%@ page import="August_27.Bean1" %>
<%@ page import="java.util.*"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/27
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" buffer="8kb" autoFlush="true"%>
<%@ page errorPage="iserrorPage.jsp"%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
//    HttpSession session=request.getSession();
//    session.getAttribute("");
    String username="gaojian";
//    pageContext.getPage();
//    pageContext.getException();
//    pageContext.getResponse();
//    pageContext.getServletContext();
//    pageContext.setAttribute("myname","gaojian",PageContext.APPLICATION_SCOPE);
//    pageContext.setAttribute("myname","gaochao",PageContext.SESSION_SCOPE);
   // pageContext.setAttribute("myname","huzhengze",PageContext.REQUEST_SCOPE);
   // pageContext.setAttribute("myname","fenghaoyu",PageContext.PAGE_SCOPE);

%>

<%=pageContext.getAttribute("myname",3)%><br/>
<%=pageContext.findAttribute("myname")%>

<%out.print("你是不是大傻逼！sdgdfgrdsgfsdhfgdhdddddddddddddddddddddddddddddg<br/>");
    out.write("hello");
    response.getWriter().write("你牵着马");
    out.write("我牵着你");
    pageContext.setAttribute("zhangfei ","飞飞");
    String names[]={"晴雪","杨幂","一封"};
    pageContext.setAttribute("names",names);
    List<String> list=new ArrayList();
    list.add("擎天柱");
    list.add("黑寡妇");
    list.add("大黄蜂");
    pageContext.setAttribute("list",list);
    Map<String,String> map=new HashMap();
    map.put("abc","西湖");
    map.put("asd","大明湖");
    map.put("sdf","雷峰塔");
    map.put("aaa","abc");
    map.put("aaa.abc","asd");
    pageContext.setAttribute("map",map,PageContext.SESSION_SCOPE);
    pageContext.setAttribute("aaa","asd",PageContext.SESSION_SCOPE);
    Bean1 b=new Bean1();
    b.setName("xiaoqing");
    b.setAge(15);
    b.setAddr("商人");
    pageContext.setAttribute("bean",b);
    String str=null;
    pageContext.setAttribute("str","wo de ");
    List list2=new ArrayList();
    request.setAttribute("name","高健");
    pageContext.setAttribute("name","高超");
    request.getParameter("username");
%>

<br/>
${names[1]}
${list[1]}

${bean.name}${bean.age}${bean.addr}
${4+3/0}
${3 gt "2"}
${empty list2}
${4>3?"我大":"你大就你大"}
${requestScope.name}
<br/>
${param.username}
${paramValues.like[0]}
${paramValues.like[1]}
${paramValues.like[2]}
${map[abc]}
<%--<jsp:forward page="sale.jsp">--%>
    <%--<jsp:param value="gao" name="jian"></jsp:param>--%>
<%--</jsp:forward>--%>
<br/>
<br/>
<br/>
<br/>
<br/>
<%
    Cookie cookie=new Cookie("time",new Date().toLocaleString()+"");
    cookie.setPath(request.getContextPath()+"/");
    cookie.setMaxAge(60*10);
    response.addCookie(cookie);
%>
<%--${headerValues["Content-Encoding"]}--%>
<br/>
<%--<c:out value="30" escapeXml="true" default="0"></c:out>--%>
<!--html注释-->
</body>
</html>
