<%--
  Created by IntelliJ IDEA.
  User: gao
  Date: 2016/11/21
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp"%>
</head>
<body>
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">
                <h1>${seckill.name}</h1>
            </div>
        </div>
        <div class="panel-body text-center">
            <h2 class="text-danger">
                <!-- 显示time图标 -->
                <span class="glyphicon glyphicon-time"></span>
                <!-- 显示倒计时 -->
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
    <!-- 登录弹出层，输入电话号 -->
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone"></span>秒杀电话：
                    </h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey"
                                   placeholder="填手机号^o^" class="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <!-- 验证信息 -->
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-phone"></span>
                        Submit
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>

<%--<script src="<%=request.getContextPath() %>//webjars/jquery/3.1.1/dist/jquery.js"></script>--%>
<%--<script src="<%=request.getContextPath() %>/WEB-INF/bootstrap/js/bootstrap.min.js"></script>--%>
<%--<script src="<%=request.getContextPath() %>/webjars/jquery.countdown/2.1.0/dist/jquery.countdown.min.js"></script>--%>
<%--<script src="<%=request.getContextPath() %>/webjars/jquery.cookie/1.4.1/jquery.cookie.js"></script>--%>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- Jquery cookie 插件 -->
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<!-- jQuery countDown倒计时插件 -->
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<!-- 交互逻辑 -->
<script type="text/javascript" src="/resources/script/seckill.js"></script>
<script type="text/javascript">
    $(function(){
       seckill.detail.init({
           seckillId:${seckill.seckillId},
           startTime:${seckill.startTime.time},
           endTime:${seckill.endTime.time}
       });
    });
</script>
</html>


