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
	<li id="new"><a href="#" onclick="formSubmit('updateDept','_self');this.blur();">保存</a></li>
	<li id="new"><a href="#" onclick="window.history.go(-1)">返回</a></li>
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
			<td><input type="text" name="deptId" value="${dept.deptId}" readonly/></td>
		</tr>
		<tr class="odd">
			<td>部门名称</td>
			<td><input type="text" name="deptName" value="${dept.deptName}"/></td>
		</tr>
		<tr>
			<td>父级部门</td>
			<td>
                <select name="parentDept.deptId">
                    <c:forEach items="${deptList}" var="d">
                            <option value="${d.deptId}"
                                <c:if test="${dept.parentDept.deptId==d.deptId}">
                                    selected="selected"
                                </c:if>
                            >${d.deptName}</option>
                    </c:forEach>
                </select>
            </td>
		</tr>
        <tr class="odd">
            <td>状态</td>
            <td>
                <font color="#7fffd4">启用</font>
                    <input type="radio" value="1" name="state"
                        <c:if test="${dept.state==1}"> checked="checked"</c:if>
                    />
                <font color="#dc143c">停用</font>
                        <input type="radio" value="0" name="state"
                        <c:if test="${dept.state==0}"> checked="checked"</c:if>
                        />
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
            <td><fmt:formatDate value='${user.userInfo.createTime}'/></td>
        </tr>
        <tr class="odd">
            <td>修改人</td>
            <td>${dept.updateBy}</td>
        </tr>
        <tr>
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

