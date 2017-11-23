<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/24
  Time: 13:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>登陆页面</title>
<%--<script type="text/javascript">--%>
    <%--function check(){--%>
        <%--document.forms.Login.uname.value="";--%>
    <%--}--%>
<%--</script>--%>
  </head>
  <body>
  <form action="servlet/servletDispatcher" method="post" name="Login">
    username：<input type="text" name="uname" /><br/>
    password：<input type="password" name="upwd"/><br/><br/>
    <input type="submit" value="Login" />
    <input type="reset" value="Reset"/>
  </form>
  </body>
</html>
