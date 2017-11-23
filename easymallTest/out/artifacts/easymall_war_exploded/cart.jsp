<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/7
  Time: 18:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <link href="${app}css/cart.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="script/XMLHttpRequest.js"></script>
    <script type="text/javascript">
        /*购物车中商品的数量增加*/
        function add(id){
            var buyNumInp=document.getElementById(id);
            var buyNum=parseInt(buyNumInp.value)+1;
            location.href="${app}/EditCartBuyNumServlet?id="+id+"&cartNum="+buyNum;
        }
        /*购物车中商品的数量减少*/
        function del(id){
            var buyNumInp=document.getElementById(id);
            var buyNum=parseInt(buyNumInp.value)-1;
            if(buyNum>0){
                location.href="${app}/EditCartBuyNumServlet?id="+id+"&cartNum="+buyNum;
            }else if(buyNum==0){
                location.href="${app}/DeleteCartServlet?id="+id;
            }
        }
        /*复选框全选*/
        function selectAll(obj){
            var prodC=document.getElementsByName("prodC");
            var allC=document.getElementsByName("allC");
            if(obj.checked) {
                for (var i = 0; i < prodC.length; i++) {
                    if (prodC[i].type == "checkbox")prodC[i].checked =1;
                    allC[0].checked=1;allC[1].checked=1;
                }
            }else{
                for (var i = 0; i < prodC.length; i++) {
                    if (prodC[i].type == "checkbox")prodC[i].checked = 0;
                    allC[0].checked=0;allC[1].checked=0;
                }
            }
        }
        /*修改购物车中商品的数量*/
        function editCartNum(id,oldNum){
            var  cartNum=document.getElementById(id);
            var reg=/^[0-9]*$/;
            if(!reg.test(cartNum.value)){
                alert("请您输入正确的商品数量！");
                cartNum.value=oldNum;
            }else if(cartNum.value<1){
                if(confirm("你执行了删除操作，确定要删除么")){
                    location.href="${app}/DeleteCartServlet?id="+id;
                }else{
                    cartNum.value=oldNum;
                }
            }
            if(cartNum.value!=oldNum){
                    location.href="${app}/EditCartBuyNumServlet?id="+id+"&cartNum="+cartNum.value;
            }
        }
    </script>
</head>
<body>
<%@include file="_head.jsp"%>
<div class="warp">

    <!-- 标题信息 -->
    <div id="title">
        <input name="allC" type="checkbox" value="" checked="checked" onclick="selectAll(this);"/>
        <span id="title_checkall_text">全选</span>
        <span id="title_name">商品</span>
        <span id="title_price">单价（元）</span>
        <span id="title_buynum">数量</span>
        <span id="title_money">小计（元）</span>
        <span id="title_del">操作</span>
    </div>
    <!-- 购物信息 -->
    <c:set var="total" value="0" scope="page"/>
    <c:forEach items="${sessionScope.cart}" var="entry" varStatus="index">
        <div id="prods">
            <input name="prodC" type="checkbox" value="" checked="checked" onclick=""/>
            <img src="${app}/ProdImgServlet?imgurl=${entry.key.imgurl}" width="90" height="90" />
            <span id="prods_name">${entry.key.name}</span>
            <span id="prods_price" name="prods_price">${entry.key.price}</span>
                    <span id="prods_buynum">
                        <a href="javascript:void(0)" id="delNum" onclick="del('${entry.key.id}')" >-</a>
                        <input id="${entry.key.id}" type="text" name="cartNum" onblur="editCartNum('${entry.key.id}',${entry.value})" value="${entry.value}" size="4" >
                        <a href="javascript:void(0)" id="addNum"onclick="add('${entry.key.id}')" >+</a>
                    </span>
            <span id="prods_money">${entry.value*entry.key.price}</span>
            <c:set var="money" scope="page" value="${money+entry.key.price*entry.value}"/>
            <span id="prods_del"><a href="${app}/DeleteCartServlet?id=${entry.key.id}">删除</a></span>
        </div>
    </c:forEach>
    <!-- 总计条 -->
    <div id="total">
        <div id="total_1">
            <input name="allC" onclick="selectAll(this);" checked="checked" type="checkbox" value=""/>
            <span>全选</span>
            <a id="del_a" href="#">删除选中的商品</a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="color: red"><c:out value="${msg}"/></span>
            <span id="span_1">总价：</span>
            <span id="span_2">${money}</span>
        </div>
        <div id="total_2">
            <a id="goto_order" href="${app}/addOrder.jsp">去结算</a>
        </div>
    </div>
</div>
<%@include file="_foot.jsp"%>
</body>
</html>