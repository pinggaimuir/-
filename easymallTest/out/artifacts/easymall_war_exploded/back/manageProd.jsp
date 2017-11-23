<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<style type="text/css">
body {
	background: #F5F5F5;
	text-align: center;
}

table {
	text-align: center;
	margin: 0px auto;
}

th {
	background-color: silver;
}
</style>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/XMLHttpRequest.js"></script>
	<script type="text/javascript">
		function changePnum(id,obj,oldNum){
			var pnum=document.getElementById("pnum").value;
			if(isNaN(pnum)){
				alert("您输入的不是数字！");
				obj.value=oldNum;
			}else if(pnum<0){
					if(confirm("亲，您输入了小于0的数字，执行了删除操作，确定要删除么？")){
                        location.href("${app}/BackDeleteProdServlet?id="+id);
					}else{
                        obj.value=oldNum;
					}
            }else if(pnum!=oldNum){
                    editPnum(id,pnum);
            }
		}
        function editPnum(id,num){
            var xmlHttp=ajaxFunction();

            xmlHttp.open("GET","${app}/AjaxChangePnumServlet?id="+id+"&pnum="+num,true);
            xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            xmlHttp.send(null);
            xmlHttp.onreadystatechange=function(e){
                if(xmlHttp.readyState==4&&(xmlHttp.status==200||xmlHttp.status==304)){
                    var data=xmlHttp.responseText;
                    if(data=="true"){
                        alert("修改成功！");
                    }else{
                        alert("修改失败！");
                    }
                }
            }
        }
	</script>
</head>
<body>
	<h1>商品管理</h1>
	<a href="${pageContext.request.contextPath}/back/manageAddProd.jsp">添加商品</a>
	<hr>
	<table bordercolor="black" border="1" width="95%" cellspacing="0px" cellpadding="5px">
		<tr>
			<th>商品图片</th>
			<th>商品id</th>
			<th>商品名称</th>
			<th>商品种类</th>
			<th>商品单价</th>
			<th>库存数量</th>
			<th>描述信息</th>
            <th>操作</th>
		</tr>
		<c:forEach items="${list}" var="prod">
			<tr>
				<td><img width="120px" height="120px" src="${pageContext.request.contextPath}/ProdImgServlet?imgurl=${prod.imgurl}"/></td>
				<td>${prod.id }</td>
				<td>${prod.name}</td>
				<td>${prod.category}</td>
				<td>${prod.price }</td>
				<td><input type="text" id="pnum" value="${prod.pnum}" style="width: 50px" onblur="changePnum('${prod.id}',this,${prod.pnum})"/></td>
				<td>${prod.description }</td>
                <td><a href="${app}/back/BackDeleteProdServlet?id=${prod.id}">删除</a>
                        <a href="${app}/back/BackProdEditPreServlet?id=${prod.id}">修改</a>
                </td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
