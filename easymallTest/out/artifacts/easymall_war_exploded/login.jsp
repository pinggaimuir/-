<%@ page language="java" import="java.net.URLDecoder" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="me" uri="http://www.pinggai.com"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="${app}/css/login.css"/>
		<title>EasyMall欢迎您登陆</title>
	</head>
	<body>
		<form action="${app}/LoginServlet?method=doPost" method="POST">
			<table>
                <tr>
                    <td colspan="2" style="text-align:center;font-size: 12px;color:red">
                        ${requestScope.msg}
                    </td>
                </tr>
				<tr>
                    <h1>欢迎登陆EasyMall</h1>
					<td class="tdx">用户名:</td>

					<td><input type="text" name="username" value=" <me:URLDecode name='username'/>"/></td>
				</tr>
				<tr>
					<td class="tdx">密码:</td>
					<td><input type="password" name="password"/></td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="checkbox" name="remname" value="true"
                       <c:if test="${cookie.username!=null}">checked="checked"</c:if>/>记住用户名
						<input type="checkbox" name="autologin" value="true"/>30天内自动登陆
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="登陆"/>
						<font color="red"></font>
					</td>
				</tr>
			</table>
		</form>		
	</body>
</html>
