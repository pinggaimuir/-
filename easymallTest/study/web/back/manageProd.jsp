<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<style type="text/css">
body {
	background: #F5F5F5;
	text-align: center;
}

table {
	text-align: center;
	margin: 0px auto;
}

th {
	background-color: silver;
}
</style>
</head>
<body>
	<h1>商品管理</h1>
	<a href="${pageContext.request.contextPath}/back/manageAddProd.jsp">添加商品</a>
	<hr>
	<table bordercolor="black" border="1" width="95%" cellspacing="0px" cellpadding="5px">
		<tr>
			<th>商品图片</th>
			<th>商品id</th>
			<th>商品名称</th>
			<th>商品种类</th>
			<th>商品单价</th>
			<th>库存数量</th>
			<th>描述信息</th>
		</tr>
		<c:forEach items="${list}" var="prod">
			<tr>
				<td><img width="120px" height="120px" src="${pageContext.request.contextPath}/ProdImgServlet?imgurl=${prod.imgurl}"/></td>
				<td>${prod.id }</td>
				<td>${prod.name}</td>
				<td>${prod.category}</td>
				<td>${prod.price }</td>
				<td><input type="text" value="" style="width: 50px" onblur=""/></td>
				<td>${prod.description }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
