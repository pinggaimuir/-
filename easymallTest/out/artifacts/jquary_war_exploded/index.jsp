<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/25
  Time: 11:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script type="text/javascript" src="script/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="script/usernameVarify.js"></script>
      <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}css/window.css"/>
  </head>
  <body>
  <h5>请输入用户名</h5>
  <input type="text" id="username"/>
  <span id="tip"></span>
  <input type="button" value="显示窗口" id="btn1">
  <div id="win">
      <div id="title">点击显示内容<span id="close">x</span></div>
      <div id="context">我时要显示的内容</div>
  </div>
  <ul id="menu1">
      <a href="#">姓名</a>
      <li><a href="#"id="tableEdit.jsp">高健</a></li>
      <li>高争锋</li>
      <li>旁海</li>
  </ul>
  <div id="content"></div>
  </body>
</html>
