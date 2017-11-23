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
	<h1>销售榜单</h1>
	<a href="${app}/back/DownLoadSalesServlet?method=downLoad">销售榜单下载</a>
	<hr>

	<table bordercolor="black" border="1" width="95%" cellspacing="0px" cellpadding="5px">
		<tr>
			<th>商品id</th>
			<th>商品名称</th>
			<th>销售总量</th>
		</tr>
		<c:forEach items="${sales}" var="entry">
			<tr>
				<td>${entry.id}</td>
				<td>${entry.name}</td>
				<td>${entry.sale_num}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
