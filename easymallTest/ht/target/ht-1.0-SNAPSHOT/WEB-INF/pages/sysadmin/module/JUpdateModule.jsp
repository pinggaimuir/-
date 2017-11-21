<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>新增模块</title>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>

	<li id="new"><a href="#" onclick="formSubmit('updateModule','_self');this.blur();">保存</a></li>
	<li id="new"><a href="#" onclick="window.history.go(-1)">返回</a></li>
</ul>
  </div>
</div>
</div>
</div>

  <div class="textbox-title">
	<img src="../../staticfile/skin/default/images/icon/currency_yen.png"/>
	  新增模块
      <c:if test="${message!=null||message!=''}">
            <span style="color:red;">&nbsp;&nbsp;&nbsp;${message}</span>
      </c:if>
  </div>
<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >
	<input type="hidden" value="${module.moduleId}" name="moduleId"/>
	<tbody class="tableBody" >
		<tr class="odd">
			<td>模块标识</td>
			<td><input type="text" value="${module.name}" name="name"/></td>
		</tr>
		<tr class="odd">
			<td>父级模块</td>
			<td>
                <select name="parent.moduleId">
                    <option value="">--请选择--</option>
                    <c:forEach items="${list}" var="m" >
                        <option value="${m.moduleId}"
							<c:if test="${m.moduleId==module.moduleId}">selected="selected"</c:if>
						>${m.name}</option>
                    </c:forEach>
                </select>
            </td>
		</tr>
		<tr class="odd">
			<td>类型</td>
			<td>
                <select name="ctype">
                    <option value="1" <c:if test="${module.ctype==1}">selected="selected"</c:if>>主菜单</option>
                    <option value="2" <c:if test="${module.ctype==2}">selected="selected"</c:if>>左侧菜单</option>
                    <option value="3" <c:if test="${module.ctype==3}">selected="selected"</c:if>>按钮</option>
                </select>
            </td>
		</tr>
		<tr class="odd">
			<td>排序号</td>
			<td><input type="text" value="${module.orderNo}" name="orderNo"/></td>
		</tr>
		<tr class="odd">
			<td>备注信息</td>
			<td><textarea name="remark" style="height:120px;">${module.remark}</textarea></td>
		</tr>
		<tr class="odd">
			<td>状态</td>
			<td>
                <input type="radio" value="1" name="state" <c:if test="${module.state==1}">checked="checked"</c:if>/>启用
                <input type="radio" value="0" name="state" <c:if test="${module.state==0}">checked="checked"</c:if>/>停用
            </td>
		</tr>
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

