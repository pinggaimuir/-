<%--
  Created by IntelliJ IDEA.
  User: gao
  Date: 2017/5/15
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <style>
        body{
            margin:0;padding:0;
            width:100%;
            height:100%;
        }
        p,a,li,ul,form,input{
            margin:0;padding:0;list-style:none;
        }
        a{
            text-decoration:none;color:#333;
        }
        ul{
            padding-left:5px;
            padding-top:5px;
        }
        .ware{
            width:100%;
            display:block;
        }
        .ware:before,.ware:after{
            content:"";
            display:block;
        }
        .ware:after{
            clear:both;
        }
        .ware .image{
            display:block;
            float:left;
            width:54%;
        }
        .ware .image img{
            width:100%;
        }
        .texts{
            width:40%;
            height:210px;
            float:right;
            position:relative;
            padding:0 10px;
        }
        .texts .line1 a{
            color:#ff4400;
            font-weight:600;
        }
        .texts .line2 .sell{
            color:#999;
            font-size:12px;
        }
        .texts .subtitle{
            padding:10px 0;
            color:#666;
            font-size:12px;
        }
        .texts .line3{
            position:absolute;
            bottom:20px;
            font-size:12px;
        }
        .texts .line3 a{
            padding-right:12px;
            color:#666;
        }
    </style>
</head>
<body>
<ul>
<c:forEach items="${azlist}" var="product">
    <li class="ware">
        <a href="" class="image">
            <img src="${product.imageurl}" alt="">
        </a>
        <div class="texts">
            <div class="line1">
                <a href="" class="price" title="${product.name}">
                    <span>￥</span>
                    <em>${product.price}</em>
                </a>
            </div>
            <div class="line2">
                <a href="" class="new" title="新品"></a>
                <a href="" class="sell">月销
                <em>${product.salecount}</em>
                     笔</a>
            </div>
            <div class="subtitle">
                <a href="">${product.name}</a>
            </div>
            <div class="line3">
                <a href="" class="comment">评价
                <em>${product.comment}</em>
                 </a>
            <a href="" class="collect">
                收藏
                <em>3331</em>
            </a>
                <%--<input type="radio" class="parity" name="ware" form="warepartiy"/>--%>
            </div>
        </div>
    </li>
    </c:forEach>
    </ul>
</body>
</html>
