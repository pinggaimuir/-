<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/18
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
    <script type="text/javascript">
      $(document).ready(function(){
        $("#addUser").click(function(){
//          $("div").load("/spring/accessable.do",null,function(){});
            location.href="/spring/accessable.do";
        });
      });
    </script>
  </head>
  <body>
  <button id="addUser">添加用户</button>
  <div></div>
  </body>
</html>
