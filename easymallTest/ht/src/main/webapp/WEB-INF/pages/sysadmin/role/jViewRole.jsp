<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>查看角色</title>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
	<li id="new"><a href="#" onclick="window.history.go(-1)">返回</a></li>

</ul>
  </div>
</div>
</div>
</div>

  <div class="textbox-title">
	<img src="../../staticfile/skin/default/images/icon/currency_yen.png"/>
	 	查看角色
  </div>

<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >

	<tbody class="tableBody" >
		<tr class="odd">
			<td>角色名称</td>
			<td>${role.name}</td>
		</tr>
		<tr class="odd">
			<td>角色说明</td>
			<td>${role.remark}</td>
		</tr>
		<tr class="odd">
			<td>排序号</td>
			<td>${role.orderNo}</td>
		</tr>
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

