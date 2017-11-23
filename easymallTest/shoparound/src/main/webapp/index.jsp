<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>主页</title>
    <link rel="stylesheet" href="/css/head.css"/>
    <script src="/script/shop.js"></script>
    <script src="/script/jquery-1.9.1.js"></script>
</head>
<body>
    <div>
        <input type="text" id="keyword" name="" value="${keyword}"/>
        <input type="button" id="searchBtn" value="搜索"/>
    </div>
</body>
<script type="text/javascript">
    $(function(){
        shop.search();
    });
</script>
</html>
