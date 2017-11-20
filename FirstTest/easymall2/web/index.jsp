<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/8/27
  Time: 12:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="${app}/css/index.css"/>
    <title>欢迎光临EasyMall</title>
</head>
<body>

<%@include file="/_head.jsp"%>
<div id="index">
    <div id="line1">
        <img src="img/index/banner_big.jpg"/>
    </div>
    <div id="line2">
        <img id="line2_1" src="img/index/adv1.jpg"/>
        <img id="line2_2" src="img/index/adv2.jpg"/>
        <img id="line2_3" src="img/index/adv_l1.jpg"/>
    </div>
    <div id="line3">
        <img id="line3_1" src="img/index/adv3.jpg"/>
        <img id="line3_2" src="img/index/adv4.jpg"/>
        <div id="line3_right">
            <img id="line3_3" src="img/index/adv_l2.jpg"/>
            <img id="line3_4" src="img/index/adv_l3.jpg"/>
        </div>
    </div>
    <div id="line4">
        <img src="img/index/217.jpg"/>
    </div>
    <div id="line5">
				<span id="line5_1">
					<img src="img/index/icon_g1.png"/>&nbsp;&nbsp;500强企业 品质保证
				</span>
				<span id="line5_2">
					<img src="img/index/icon_g2.png"/>&nbsp;&nbsp;7天退货 15天换货
				</span>
				<span id="line5_3">
					<img src="img/index/icon_g3.png"/>&nbsp;&nbsp;100元起免运费
				</span>
				<span id="line5_4">
					<img src="img/index/icon_g4.png"/>&nbsp;&nbsp;448家维修网点 全国联保
				</span>
    </div>
</div>

<%@include file="/_foot.jsp"%>
</body>
</html>
