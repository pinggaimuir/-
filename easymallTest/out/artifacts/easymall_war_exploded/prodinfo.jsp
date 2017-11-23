<%--
  Created by IntelliJ IDEA.
  User: tarena
  Date: 2016/9/7
  Time: 18:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="css/prodInfo.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="script/XMLHttpRequest.js"></script>
    <script type="text/javascript">
        var buyNumInp=document.getElementById("buyNumInp");
        function add(){
            var buyNumInp=document.getElementById("buyNumInp");
            var buyNum=parseInt(buyNumInp.value);
                buyNumInp.value=buyNum+1;
        }
        function del(){
            var buyNumInp=document.getElementById("buyNumInp");
            var buyNum=parseInt(buyNumInp.value);
            if(buyNum>0){
                buyNumInp.value=buyNum-1;
            }
        }

        function addCart(id){
            var buyNumInp=document.getElementById("buyNumInp");
            editnum(id,buyNumInp.value)
        }
        function editnum(id,num){
            var xmlHttp=ajaxFunction();

            xmlHttp.open("GET","${app}/AddCartServlet?id="+id+"&num="+num,true);
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
<div id="warp">
    <div id="left">
        <div id="left_top">
            <img src="${app}/ProdImgServlet?imgurl=${prod.imgurl}"/>
        </div>
        <div id="left_bottom">
            <img id="lf_img" src="img/prodInfo/lf.jpg"/>
            <img id="mid_img" src="${app}/ProdImgServlet?imgurl=${prod.imgurl}" width="60px" height="60px"/>
            <img id="rt_img" src="img/prodInfo/rt.jpg"/>
        </div>
    </div>
    <div id="right">
        <div id="right_top">
            <span id="prod_name">${prod.name} <br/></span>
            <br>
            <span id="prod_desc">${prod.description}<br/></span>
        </div>
        <div id="right_middle">
				<span id="right_middle_span">
						EasyMall 价：<span class="price_red">￥${prod.price }<br/>
			            运     费：满 100 免运费<br />
			            服     务：由EasyMall负责发货，并提供售后服务<br />
			            购买数量：
	            <a href="javascript:void(0);" id="delNum" onclick="del();">-</a>
	            <input id="buyNumInp" name="buyNum" type="text" value="1" onblur=""/>
		        <a href="javascript:void(0);" id="addNum" onclick="add();">+</a>
        </div>
        <div id="right_bottom">
            <input class="add_cart_but" onclick="addCart('${prod.id}');" type="button"/>
        </div>
    </div>
</div>
<%@include file="_foot.jsp"%>
</body>
</html>
