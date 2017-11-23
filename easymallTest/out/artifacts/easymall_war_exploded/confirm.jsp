<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/12
  Time: 17:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${app}/css/confirm.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@include file="_head.jsp"%>
<!-- 确认支付form -->
<div id="warp">
    <div>
        <h3>
            订单号：${p2_Order}
            <br>
            付款金额 ：${p3_Amt }
        </h3>
        <form action="https://www.yeepay.com/app-merchant-proxy/node" method="post">
            <input type="hidden" name="pd_FrpId" value="${pd_FrpId }" />
            <input type="hidden" name="p0_Cmd" value="${p0_Cmd }" />
            <input type="hidden" name="p1_MerId" value="${p1_MerId }" />
            <input type="hidden" name="p2_Order" value="${p2_Order }" />
            <input type="hidden" name="p3_Amt" value="${p3_Amt }" />
            <input type="hidden" name="p4_Cur" value="${p4_Cur }" />
            <input type="hidden" name="p5_Pid" value="${p5_Pid }" />
            <input type="hidden" name="p6_Pcat" value="${p6_Pcat }" />
            <input type="hidden" name="p7_Pdesc" value="${p7_Pdesc }" />
            <input type="hidden" name="p8_Url" value="${p8_Url }" />
            <input type="hidden" name="p9_SAF" value="${p9_SAF }" />
            <input type="hidden" name="pa_MP" value="${pa_MP }" />
            <input type="hidden" name="pr_NeedResponse" value="${pr_NeedResponse }" />
            <input type="hidden" name="hmac" value="${hmac }" />
            <input type="submit" value="确认支付" />
        </form>
    </div>
</div>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
<%@include file="_foot.jsp"%>
</body>
</html>
