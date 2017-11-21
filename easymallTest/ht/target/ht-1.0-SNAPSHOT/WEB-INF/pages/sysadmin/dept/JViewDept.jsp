<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>新增部门</title>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
	<li id="new"><a href="#" onclick="window.history.go(-1) ">返回</a></li>
</ul>
  </div>
</div>
</div>
</div>
   
  <div class="textbox-title">
	<img src="../../staticfile/skin/default/images/icon/currency_yen.png"/>
    部门列表
  </div> 
  
<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >
	<tbody class="tableBody" >
		<tr class="odd">
			<td>部门ID</td>
			<td>${dept.deptId}</td>
		</tr>
		<tr class="odd">
			<td>父级部门</td>
			<td>
                ${dept.parentDept.deptName}
            </td>
		</tr>
        <tr class="odd">
            <td>状态</td>
            <td>
                <c:if test="${dept.state==1}"><font color="#7fffd4">启用</c:if>
                <c:if test="${dept.state==0}"><font color="#dc143c">停用</c:if>
            </td>
        </tr>
        <tr class="odd">
            <td>创建人</td>
            <td>${dept.createBy}</td>
        </tr>
        <tr class="odd">
            <td>创建部门</td>
            <td>${dept.createDept}</td>
        </tr>
        <tr class="odd">
            <td>创建时间</td>
            <td>${dept.createTime}</td>
        </tr>
        <tr class="odd">
            <td>修改人</td>
            <td>${dept.updateBy}</td>
        </tr>
        <tr class="odd">
            <td>修改时间</td>
            <td>${dept.updateTime}</td>
        </tr>
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

