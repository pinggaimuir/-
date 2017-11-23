<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/28
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-1.9.1.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#btn2").click(function(){
                var url="/spring/addUser3.do?";
                var inputs=$("input[type='text']");
                $.each(inputs,function(i){
                    var input=$(this);
                    url+=input.attr("name")+"="+input.val();
                    url+="&";
                });
                var radioNode=$(":checked");
                url+=radioNode.attr("name")+"="+radioNode.val();
                url=encodeURI(encodeURI(url));
                $.get(url,null,function(){});
            });
        });
    </script>
    <style type="text/css">
        #t1{
            border-collapse: collapse;
            position:absolute;
            left:500px;
        }
        #t1 td{
            border:1px black solid;
            padding:5px;
        }
    </style>
</head>
<body>
<form action="${pageContext.request.contextPath}/addUser3.do" method="post">
    <table id="t1">
        <tr>
            <td>用户名：</td>
            <td><input type="text" name="username"/></td>
        </tr>
        <tr>
            <td>密码：</td>
            <td><input type="text" name="password"/></td>
        </tr>
        <tr>
            <td>性别：</td>
            <td>
                男<input type="radio" value="男"name="userInfo.sex">
                女<input type="radio" value="女"name="userInfo.sex">
            </td>
        </tr>
        <tr>
            <td>姓名：</td>
            <td><input type="text" name="userInfo.name"/></td>
        </tr>
        <tr>
            <td>年龄：</td>
            <td><input type="text" name="userInfo.age"/></td>
        </tr>
        <tr>
            <td>生日：</td>
            <td><input type="text" name="userInfo.birthday"/></td>
        </tr>
        <tr>

          <%--  <td colspan="2" align="center" id="btn">
                <button id="btn2">提交</button>
            </td>--%>
            <td colspan="2" align="center" id="btn">
                <input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
<%--</form>--%>
</body>
</html>
