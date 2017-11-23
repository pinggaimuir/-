  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/7
  Time: 16:01
  To change this template use File | Settings | File Templates.
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
    <link href="${app}css/prodList.css" rel="stylesheet" type="text/css">
    <style type='text/css'>
    #fy_div{
        margin-top: 10%;
    text-align: center;
    }

    #fy_div input{
    width:20px;
    border:solid 1px #CCCCCC;
    }

    #fy_div a{
    text-decoration: none;
    border: solid 1px #CCCCCC;
    padding: 5px;
    margin: 3px;
    color:#333
    }

    #fy_div a:hover{
    color:white;
    background-color: red;
    }
    </style>
    <script type="text/javascript" src="script/XMLHttpRequest.js"></script>
    <script type="text/javascript" >
        function changePage(obj){
            var newPage=obj.value;
            if(!newPage.match(/^[1-9][0-9]*$/)){
                alert("请输入正确的页面值！");
                obj.value="${page.thispage}";
                return;
            }
            /*修改隐藏域中的thispage页码，然后提交*/
            document.getElementById("thispage").value=newPage;
            document.getElementById("searchForm").submit();
        }
        function changePageA(tp){
            /*修改隐藏域中的thispage页码，然后提交*/
            document.getElementById("thispage").value=tp;
            document.getElementById("searchForm").submit();
        }
        function changePageB(){
            /*修改隐藏域中的thispage页码为1，然后提交*/
            document.getElementById("thispage").value=1;
            document.getElementById("searchForm").submit();
        }
        function editnum(id){
            var xmlHttp=ajaxFunction();
            xmlHttp.open("GET","${app}/AddCartServlet?id="+id+"&num=1",true);
            xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            xmlHttp.send(null);
            xmlHttp.onreadystatechange=function(e){
                if(xmlHttp.readyState==4&&(xmlHttp.status==200||xmlHttp.status==304)){
                    var data=xmlHttp.responseText;
                    if(data=="true"){
                        alert("加入购物车成功！");
                    }else{
                        alert("加入购物车失败！");
                    }
                }
            }
        }
    </script>
</head>
<body>
<%@include file="_head.jsp"%>
<div id="content">
    <div id="search_div">
        <form id="searchForm" method="post" action="${app}/ProdPageServlet">
            <input type="hidden" id="thispage"name="thispage" value="${page.thispage}"/>
            <input type="hidden" name="rowperpage" value="10"/>
            <span class="input_span">商品名：<input type="text" name="name" value="${name}"/></span>
            <span class="input_span">商品种类：<input type="text" name="category" value="${category}"/></span>
            <span class="input_span">商品价格区间：<input type="text" name="minprice" value="${minprice}"/> - <input type="text" name="maxprice" value="${maxprice}"/></span>
            <input type="button" value="查询" onclick="changePageB()">
        </form>
    </div>
    <div id="prod_content">
        <c:forEach items="${page.list}" var="prod">
            <div id="prod_div">
                <a href="${app}/ProdInfoServlet?id=${prod.id}"><img src="${app}/ProdImgServlet?imgurl=${prod.imgurl}"/></a>
                <div id="prod_name_div">
                    ${prod.name}
                </div>
                <div id="prod_price_div">
                    ￥${prod.price}元
                </div>
                <div>
                    <div id="gotocart_div">
                        <a href="javascript:void(0)" onclick="editnum('${prod.id}')">加入购物车</a>
                    </div>
                    <div id="say_div">
                        库存：${prod.pnum}
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div style="clear: both"></div>
</div>
<div id="fy_div">
    共${page.countrow}条记录 共${page.countpage}页
    <a href="javascript:void(0)" onclick="changePageA(1)">首页</a>
    <a href="javascript:void(0)" onclick="changePageA(${page.prepage})">上一页</a>
    <%-- 分页逻辑开始 --%>
    <c:set var="begin" value="0" scope="page" />
    <c:set var="end" value="0" scope="page" />
    <c:if test="${page.countpage<=5}">
        <c:set var="begin" value="1" scope="page"/>
        <c:set var="end" value="${page.countpage}" scope="page"/>
    </c:if>
        <c:if test="${page.countpage>5}">
            <c:choose>
                <c:when test="${page.thispage<=3}">
                    <c:set var="begin" value="1" scope="page"/>
                    <c:set var="end" value="5" scope="page"/>
                </c:when>
                <c:when test="${page.thispage>=page.countpage-2}">
                    <c:set var="begin" value="${page.countpage-4}" scope="page"/>
                    <c:set var="end" value="${page.countpage}" scope="page"/>
                </c:when>
                <c:otherwise>
                    <c:set var="begin" value="${page.thispage-2}" scope="page"/>
                    <c:set var="end" value="${page.thispage+2}" scope="page"/>
                </c:otherwise>
            </c:choose>
        </c:if>
        <%--循环遍历输出页码--%>
        <c:forEach begin="${begin}" end="${end}" step="1" var="i">
            <c:if test="${page.thispage==i}">${i}</c:if>
            <c:if test="${page.thispage!=i}"><a href="javascript:void(0)" onclick="changePageA(${i})">${i}</c:if>
        </c:forEach>
    <%-- 分页逻辑结束 --%>
    <a href="javascript:void(0)" onclick="changePageA(${page.nextpage})">下一页</a>
    <a href="javascript:void(0)" onclick="changePageA(${page.countpage})">尾页</a>
    跳转到<input type="text" value="${page.thispage }" onblur="changePage(this)"/>页
</div>
<%@include file="_foot.jsp"%>
</body>
</html>

