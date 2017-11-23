<%--
  Created by IntelliJ IDEA.
  User: gao
  Date: 2016/11/21
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>主页</title>
    <%@include file="common/head.jsp"%>
    <link rel="stylesheet" href="/css/head.css"/>
    <script src="/script/legal.js"></script>

</head>
<body>
    <div class="header">
        <span id="logo">北大英华</span>
        <input type="text" id="keyword" name="" value="${keyWord}"/>
        <input type="button" id="searchBtn" value="搜索"/>
        <input type="hidden" value="${bean.page}" id="page"/>
        <input type="hidden" value="${bean.total}" id="total"/>
    </div>
    <!-- 页面显示部分 -->
    <c:forEach var="info" items="${bean.data}">
        <div class="list-group">
            <a href="#" class="list-group-item active">
                <h4 class="list-group-item-heading">
                    ${info.title}
                </h4>
            </a>
            <a href="#" class="list-group-item">
                <h4 class="list-group-item-heading">
                    关键词：
                    ${info.keyWord}
                </h4>
                <p class="list-group-item-text">
                    审理经过：
                    ${info.afterTheTrial}
                </p>
                <p class="list-group-item-text">
                    法院：
                    ${info.court}
                </p>
                <p class="list-group-item-text">
                    当事人信息：
                    ${info.litigant}
                </p>
                <p class="list-group-item-text">
                    裁判结果：
                    ${info.secondResult}
                </p>
                <p class="list-group-item-text">
                    审判日期：
                    ${info.refereeDate}
                </p>
            </a>
        </div>
    </c:forEach>
    <div >
        <ul class="pager">
            <li><a href="#" id="prepage">上一页</a></li>
            <li><a href="#" id="nextpage">下一页</a></li>
        </ul>
    </div>
</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function(){
        legal.search();
        legal.prepage();
        legal.nextpage();
    });
</script>
</html>
