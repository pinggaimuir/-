<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/12
  Time: 17:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <link href="${app}/css/pay.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />

</head>

<body>
<%@include file="_head.jsp"%>
<%--<form action="https://www.yeepay.com/app-merchant-proxy/node" method="post">--%>
<form action="${app}/PayServlet?method=Pay" method="post">
    <dl class="payment_page">
        <dt>
            <strong>订单号：</strong>
            <input class="idinp" name="order_id" readonly="readonly" type="text" value="${param.order_id}">
            <strong>支付金额：</strong>
            <input class="moneyinp" name="money" type="text" readonly="readonly" type="text" value="${param.money}">元
        </dt>
        <dd class="payment_page_name">
            <strong>请您选择在线支付银行 :</strong>
        </dd>
        <dd class="banks">
            <ul>
                <li>
                    <input name="pd_FrpId" type="radio" value="ICBC-NET-B2C">
                    <img src="${app}/img/pay/01gs.jpg" width="130" height="52">
                </li>
                <li>
                    <input name="pd_FrpId" type="radio" value="CMBCHINA-NET-B2C">
                    <img src="${app}/img/pay/02zs.jpg" width="130" height="52">
                </li>
                <li>
                    <input name="pd_FrpId" type="radio" value="CCB-NET-B2C">
                    <img src="${app}/img/pay/03js.jpg" width="130" height="52">
                </li>
                <li>
                    <input name="pd_FrpId" type="radio" value="ABC-NET-B2C">
                    <img src="${app}/img/pay/04ny.jpg" width="130" height="52">
                </li>
                <li>
                    <input name="pd_FrpId" type="radio" value="BOC-NET-B2C ">
                    <img src="${app}/img/pay/05zg.jpg" width="130" height="52">
                </li>
                <li>
                    <input name="pd_FrpId" type="radio" value="BOCO-NET-B2C">
                    <img src="${app}/img/pay/06jt.jpg" width="130" height="52">
                </li>
                <li>
                    <input name="pd_FrpId" type="radio" value="HXB-NET-B2C">
                    <img src="${app}/img/pay/07hx.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="CIB-NET-B2C">
                    <img src="${app}/img/pay/08xy.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/09gd.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/10sz.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/11ms.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/12sh.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/13zx.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/14gd.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/15cq.jpg" width="130" height="52">
                </li>

                <li>
                    <input name="pd_FrpId" type="radio" value="">
                    <img src="${app}/img/pay/16bh.jpg" width="130" height="52">
                </li>
                <%--<input type="hidden" name="p0_Cmd" value="${p0_Cmd }" />--%>
                <%--<input type="hidden" name="p1_MerId" value="${p1_MerId }" />--%>
                <%--<input type="hidden" name="p2_Order" value="${p2_Order }" />--%>
                <%--<input type="hidden" name="p3_Amt" value="${p3_Amt }" />--%>
                <%--<input type="hidden" name="p4_Cur" value="${p4_Cur }" />--%>
                <%--<input type="hidden" name="p5_Pid" value="${p5_Pid }" />--%>
                <%--<input type="hidden" name="p6_Pcat" value="${p6_Pcat }" />--%>
                <%--<input type="hidden" name="p7_Pdesc" value="${p7_Pdesc }" />--%>
                <%--<input type="hidden" name="p8_Url" value="${p8_Url }" />--%>
                <%--<input type="hidden" name="p9_SAF" value="${p9_SAF }" />--%>
                <%--<input type="hidden" name="pa_MP" value="${pa_MP }" />--%>
                <%--<input type="hidden" name="pr_NeedResponse" value="${pr_NeedResponse }" />--%>
                <%--<input type="hidden" name="hmac" value="${hmac }" />--%>
            </ul>
        </dd>
        <div style="clear: both;"></div>
        <dd class="ok_dd">
            <input  type="submit" class="ok_pay" value="确认支付">
        </dd>
    </dl>
</form>
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
<br/>
<%@include file="_foot.jsp"%>
</body>
</html>

