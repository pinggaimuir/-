<%@ page import="java.util.Random" %><%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/27
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>欢迎注册EasyMall</title>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="${app}/css/regist.css"/>
    <script type="text/javascript" src="${app}/script/registCheck.js"></script>
    <script type="text/javascript" src="<c:url value='script/jquery-1.9.1.js'/>"></script>
    <script type="text/javascript">
        //更改访问验证码的路径，实现换验证码
        function changeImg(imgObj) {
            imgObj.src = "${app}/valiImageServlet?time=" + new Date().getTime();
        }
        //长治用户重复登陆
        function checkUserName() {
            var flag = checkNull("username", "用户名不能为空");
            var username = document.getElementsByName("username")[0].value;
            /*var xhr = ajaxFunction();
            //建立连接
            xhr.open("GET", "${app}/checkUserNameServlet?username="+username, true);
            //发送请求
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send(null);
            if (flag) {
                xhr.onreadystatechange = function (e) {
                    if (xhr.readyState == 4) {
                        if (xhr.status == 200 || xhr.status == 304) {
                            var data = xhr.responseText;
                            if (data == "true") {
                                setMsg("username", "用户名已经存在");
                                flag = false;
                            } else {
                                setMsg("username", "恭喜你,用户名可以用！");
                            }
                        }
                    }
                }
            }*/
            $.get("${app}/checkUserNameServlet?username="+username,null,function(data){
                if (data == "true") {
                    setMsg("username", "用户名已经存在");
                    flag = false;
                } else {
                    setMsg("username", "恭喜你,用户名可以用！");
                }
            });
        }
    </script>
</head>
<body>
<h1>欢迎注册EasyMall</h1>
<form action="${app}/registServlet" method="POST" onsubmit="return checkForm();">
    <table>
   <%--     <tr>
            <%
                Random random = new Random();
                String token = random.nextInt() + "";
                request.getSession().setAttribute("token", token);
            %>
            <td><input type="hidden" name="token" value="<%=token%>"/></td>
        </tr>
        <tr>--%>
            <td colspan="2" style="text-align: center;color:red;font-size: 12px;">
                ${requestScope.msg}
            </td>
        </tr>
        <tr>
            <td class="tds">用户名：</td>
            <td><input type="text" name="username"
                       value="${param.username}"
                       onblur="checkUserName()">
                <span id="username_msg"></span></td>
        </tr>
        <tr>
            <td class="tds">密码：</td>
            <td><input type="password" name="password"
                       value="${param.password}%>"
                       onblur="checkNull('password','密码不能为空')">
                <span id="password_msg"></span></td>
        </tr>
        <tr>
            <td class="tds">确认密码：</td>
            <td><input type="password" name="password2"
                       value="${param.password2}"
                       onblur="checkpassword('password','确认密码与密码不一致')">
                <span id="password2_msg"></span></td>
        </tr>
        <tr>
            <td class="tds">昵称：</td>
            <td><input type="text" name="nickname"
                       value="${param.nickname}"
                       onblur="checkNull('nickname','昵称不能为空')">
                <span id="nickname_msg"></span></td>
        </tr>
        <tr>
            <td class="tds">邮箱：</td>
            <td><input type="text" name="email"
                       value="${param.email}"
                       onblur="checkEmail('email','邮箱格式不正确')">
                <span id="email_msg"></span>
            </td>
        </tr>
        <tr>
            <td class="tds">验证码：</td>
            <td><input type="text" name="valistr" onblur="checkNull('valistr','验证码不能为空')">
                <img id="yzm_img" src="${app}/valiImageServlet" style="cursor: pointer"
                     onclick="changeImg(this)"/>
                <span id="valistr_msg"></span>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="注册用户"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
