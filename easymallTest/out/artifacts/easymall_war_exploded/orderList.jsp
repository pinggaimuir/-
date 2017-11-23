<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/9
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
    <link href="${app}/css/orderList.css" rel="stylesheet" type="text/css">
</head>
<%@include file="_head.jsp"%>
<body style="text-align:center;">
<c:forEach items="${orderList}" var="order">
<dl class="Order_information">
    <dt>
    <h3>订单信息</h3>
    </dt>
    <dd>
        订单编号：${order.id}<br />
        下单时间：<fmt:formatDate value="${order.ordertime}" pattern="yyyy-MM-dd HH-mm-ss"/><br />
        订单金额：${order.money}<br />
        支付状态：
        <c:choose>
            <c:when test="${order.paystate==0}">
        <font color="red">未支付</font>&nbsp;&nbsp;&nbsp;
        <a href="/MyOrderServlet?method=deleteOrder&order_id=${order.id }"><img src="${app}/img/orderList/sc.jpg" width="69" height="19"></a>
        <a href="/pay.jsp?order_id=${order.id }&money=${order.money}"> <img src="${app}/img/orderList/zx.jpg" width="69" height="19"></a><br />
        </c:when>
            <c:otherwise>
            <font color="blue">已支付</font>
            </c:otherwise>
        </c:choose>
        所属用户：${order.user_id}<br/>
        收货地址：${order.receiverinfo}<br/>
        支付方式：在线支付
    </dd>
</dl>

<table width="1200" border="0" cellpadding="0"
       cellspacing="1" style="background:#d8d8d8;color:#333333">

    <tr>
        <th width="276" height="30" align="center" valign="middle" bgcolor="#f3f3f3">商品图片</th>
        <th width="247" align="center" valign="middle" bgcolor="#f3f3f3">商品名称</th>
        <th width="231" align="center" valign="middle" bgcolor="#f3f3f3">商品单价</th>
        <th width="214" align="center" valign="middle" bgcolor="#f3f3f3">购买数量</th>
        <th width="232" align="center" valign="middle" bgcolor="#f3f3f3">总价</th>
    </tr>
    <c:set var="money" value="0" scope="page"/>
    <c:forEach items="${order.maplist}" var="entry">
        <tr>
            <td align="center" valign="middle" bgcolor="#FFFFFF">
                <img src="${app}/ProdImgServlet?imgurl=${entry.imgurl}" width="90" height="105">
            </td>
            <td align="center" valign="middle" bgcolor="#FFFFFF">${entry.name}</td>
            <td align="center" valign="middle" bgcolor="#FFFFFF">${entry.price}元</td>
            <td align="center" valign="middle" bgcolor="#FFFFFF">${entry.buynum}件</td>
            <td align="center" valign="middle" bgcolor="#FFFFFF">${entry.price*entry.buynum}元</td>
            <c:set var="money" value="${money+entry.price*entry.buynum}" scope="page"/>
        </tr>
    </c:forEach>
</table>

<div class="Order_price">${money}</div>
</c:forEach>
<%@include file="_foot.jsp"%>
</body>
</html>
