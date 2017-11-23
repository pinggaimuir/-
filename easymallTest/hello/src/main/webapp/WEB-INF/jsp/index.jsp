<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/10/6
  Time: 14:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>首页</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
    <script type="text/javascript">
      $(document).ready(function(){
        $("#dianji").click(function(){
          $.get("${pageContext.request.contextPath}/item/itemlist/1/5",null,function(data){
            alert(data);
          });
        });
      });
    </script>
  </head>
  <body>
    <button id="dianji">点击</button>
  </body>
</html>
