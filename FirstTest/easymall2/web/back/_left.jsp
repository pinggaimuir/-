<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
  	<style type="text/css">
  		body{
  			background: #6495ED;
  			text-align: center;
  			font-size: 25px;
  		}
  		a{
  			text-decoration: none;
  		}
  	</style>
  </head>
  <body>
	<a target="_right" href="${pageContext.request.contextPath}/BackProdListServlet">商品管理</a><br>
	<a target="_right" href="#">用户管理</a><br>
	<a target="_right" href="#">权限管理</a><br>
	<a target="_right" href="#">销售榜单</a><br>
  	<a target="_parent" href="${app}/index.jsp">返回首页</a><br>
  </body>
</html>
