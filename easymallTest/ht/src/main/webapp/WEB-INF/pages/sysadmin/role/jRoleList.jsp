<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>角色列表</title>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
	<shiro:hasPermission name="角色--查看">
		<li id="view"><a href="#" onclick="formSubmit('toview','_self');this.blur();">查看</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="角色--新增">
		<li id="new"><a href="#" onclick="formSubmit('tocreate','_self');this.blur();">新增</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="角色--修改">
		<li id="update"><a href="#" onclick="formSubmit('toupdate','_self');this.blur();">修改</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="角色--删除">
		<li id="delete"><a href="#" onclick="formSubmit('delete','_self');this.blur();">删除</a></li>
	</shiro:hasPermission>
	<li id="new"><a href="#" onclick="formSubmit('toRoleModile','_self');this.blur();">权限</a></li>

</ul>
  </div>
</div>
</div>
</div>

  <div class="textbox-title">
	<img src="../../staticfile/skin/default/images/icon/currency_yen.png"/>
	  角色列表
      <c:if test="${message!=null||message!=''}">
            <span style="color:red;">&nbsp;&nbsp;&nbsp;${message}</span>
      </c:if>
  </div>

<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >

	<thead>
	<tr>
		<td class="tableHeader"><input type="checkbox" name="selid" onclick="checkAll('roleId',this)"></td>
		<td class="tableHeader">序号</td>
		<td class="tableHeader">角色名称</td>
		<td class="tableHeader">备注信息</td>
		<td class="tableHeader">排序号</td>
		<td class="tableHeader">更新时间</td>
	</tr>
	</thead>
	<tbody class="tableBody" >

	<c:forEach items="${roleList}" var="r" varStatus="status">
	<tr class="odd" onmouseover="this.className='highlight'" onmouseout="this.className='odd'">
		<td><input type="checkbox" name="roleId" value="${r.roleId}"/></td>
		<td>${status.index+1}</td>
        <td>${r.name}</td>
		<td>${r.remark}</td>
		<td>${r.orderNo}</td>
		<td><fmt:formatDate value="${r.updateTime}"></fmt:formatDate></td>
	</tr>
	</c:forEach>
	
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

