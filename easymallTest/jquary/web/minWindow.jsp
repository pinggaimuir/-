<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/27
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="script/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="script/minWindow.js"></script>
    <style type="text/css">
        #window{
            width:110px;
            height:60px;
            z-index:90;
            background-color: #64BDF9;
            display: none;
        }
    </style>
</head>
<body>
    <div id="s1001"><a href="#">中石油:</a><span></span></div>
    <div id="s1002"><a href="#">浦发银行:</a><span></span></div>
    <div id="window">
        <div id="yesterday">昨日：<span></span></div>
        <div id="today">今日：<span></span></div>
        <div id="now">现在：<span></span></div>
    </div>
</body>
</html>
