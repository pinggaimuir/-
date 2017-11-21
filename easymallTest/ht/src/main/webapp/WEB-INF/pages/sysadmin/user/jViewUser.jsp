<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>查看用户</title>
    <script type="text/javascript" src="${ctx}\staticfile\js\datepicker\WdatePicker.js"></script>
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
	  查看用户
  </div>

<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >
	<tbody class="tableBody" >
		<tr class="odd">
			<td>用户名</td>
			<td>${user.username}</td>
			<td>所在部门</td>
			<td>
				${user.dept.deptName}
			</td>
		</tr>
		<tr class="odd">
			<td>密码</td>
			<td>${user.password}</td>
			<td>真实姓名</td>
			<td>${user.userInfo.name}</td>
		</tr>
		<tr class="odd">
			<td>身份证号</td>
			<td>${user.userInfo.cardNo}</td>
			<td>入职时间</td>
			<td><fmt:formatDate value='${user.userInfo.joinDate}'/></td>
		</tr>
		<tr class="odd">
			<td>工资</td>
			<td>${user.userInfo.salary}</td>
			<td>出生年月</td>
			<td>
                <fmt:formatDate value='${user.userInfo.birthday}'/>
            </td>
		</tr>
		<tr class="odd">
			<td>性别</td>
			<td>${user.userInfo.gender}</td>
			<td>岗位</td>
			<td>${user.userInfo.station}</td>
		</tr>
		<tr class="odd">
			<td>电话</td>
			<td>${user.userInfo.telephone}</td>
			<td>用户级别</td>
			<td>
				${user.userInfo.userLevel}
            </td>
		</tr>
		<tr class="odd">
			<td>直属领导</td>
			<td colspan="3">
				${user.userInfo.manager.userInfoId}
            </td>
		</tr>
		<tr class="odd">
			<td>备注信息</td>
			<td colspan="3">
				${user.userInfo.remark}
            </td>
		</tr>
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

