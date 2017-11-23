<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/27
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<link rel="stylesheet" href="${app}/css/head.css"/>
<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />

<div id="common_head">
    <div id="line1">
        <div id="content">
            <c:if test="${sessionScope.user!=null}" var="isLogin" scope="page">
            欢迎回来，${user.username}!&nbsp;&nbsp;
                <a href="${app}/back/manage.jsp" style="text-decoration:underline;">后台管理</a>
            <a href="${app}/LoginServlet?method=logout" style="text-decoration:underline;">注销</a>
            </c:if>
            <c:if test="${!isLogin}">
            <a href="${app}/login.jsp">登录</a>&nbsp;&nbsp;|&nbsp;&nbsp;
            <a href="${app}/regist.jsp">注册</a>
            </c:if>
        </div>
    </div>
    <div id="line2">
        <a href="${app}/index.jsp"><img id="logo" src="img/head/logo.jpg"/></a>
        <input type="text" name=""/>
        <input type="button" value="搜索"/>
		<span id="goto">
			<a id="goto_order" href="${app}/MyOrderServlet?method=loadOrderList">我的订单</a>
			<a id="goto_cart" href="${app}/cart.jsp">我的购物车</a>
		</span>
        <img id="erwm" src="img/head/qr.jpg"/>
    </div>
    <div id="line3">
        <div id="content">
            <ul>
                <li><a href="#">首页</a></li>
                <li><a href="${app}/ProdPageServlet?rowperpage=10&thispage=1">全部商品</a></li>
                <li><a href="#">手机数码</a></li>
                <li><a href="#">电脑平板</a></li>
                <li><a href="#">家用电器</a></li>
                <li><a href="#">汽车用品</a></li>
                <li><a href="#">食品饮料</a></li>
                <li><a href="#">图书杂志</a></li>
                <li><a href="#">服装服饰</a></li>
                <li><a href="#">理财产品</a></li>
            </ul>
        </div>
    </div>
</div>