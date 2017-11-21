<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../baselist.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>部门列表</title>
    <script type="text/javascript" src="${ctx}\staticfile\js\datepicker\WdatePicker.js"></script>
</head>

<body>
<form name="icform" method="post">

<div id="menubar">
<div id="middleMenubar">
<div id="innerMenubar">
  <div id="navMenubar">
<ul>
	<li id="new"><a href="#" onclick="formSubmit('saveUser','_self');this.blur();">保存</a></li>
	<li id="new"><a href="#" onclick="window.history.go(-1)">返回</a></li>
</ul>
  </div>
</div>
</div>
</div>

  <div class="textbox-title">
	<img src="../../staticfile/skin/default/images/icon/currency_yen.png"/>
	  新增列表
  </div>

<div>


<div class="eXtremeTable" >
<table id="ec_table" class="tableRegion" width="98%" >
	<tbody class="tableBody" >
		<c:if test="${errors!=null}">
			<c:forEach items="${errors}" var="error">
				${error}
			</c:forEach>
		</c:if>
		<tr class="odd">
			<td>所在部门</td>
			<td>
				<select name="dept.deptId">
					<option value="">--请选择--</option>
					<c:forEach items="${deptList}" var="dept">
						<option value="${dept.deptId}"
								<c:if test="${d.deptId==user.dept.deptId}">selected="selected"</c:if>
						>${dept.deptName}</option>
					</c:forEach>
				</select>
			</td>
			<td>用户名</td>
			<td><input type="text" value="${user.username}"  name="username"/></td>
		</tr>
		<tr class="odd">
			<td>密码</td>
			<td><input type="text" value="${user.password}" name="password"/></td>
			<td>真实姓名</td>
			<td><input type="text" value="${user.userInfo.name}" name="userInfo.name"/></td>
		</tr>
		<tr class="odd">
			<td>身份证号</td>
			<td><input type="text" value="${user.userInfo.cardNo}" name="userInfo.cardNo"/></td>
			<td>入职时间</td>
			<td><input type="text" value="<fmt:formatDate value='${user.userInfo.birthday}'/>" name="userInfo.joinDate" style="width:127px;"
            onclick="WdatePicker({el:this,isShowOthers:true,dateFmt:'yyyy-MM-dd'});"/></td>
		</tr>
		<tr class="odd">
			<td>工资</td>
			<td><input type="text" value="${user.userInfo.salary}" name="userInfo.salary"/></td>
			<td>出生年月</td>
			<td>
                <input type="text" value="<fmt:formatDate value='${user.userInfo.birthday}'/>" name="userInfo.birthday" style="width:127px;"
                       onclick="WdatePicker({el:this,isShowOthers:true,dateFmt:'yyyy-MM-dd'});"/>
            </td>
		</tr>
		<tr class="odd">
			<td>性别</td>
			<td><input type="radio" name="userInfo.gender" value="1"
                       <c:if test="${user.userInfo.gender==1}">checked</c:if>/>男
                <input type="radio" name="userInfo.gender" value="0"
                       <c:if test="${user.userInfo.gender==0}">checked</c:if>/>女</td>
			<td>岗位</td>
			<td><input type="text" value="${user.userInfo.station}" name="userInfo.station"/></td>
		</tr>
		<tr class="odd">
			<td>电话</td>
			<td><input type="text" value="${user.userInfo.telephone}" name="userInfo.telephone"/></td>
			<td>用户级别</td>
			<td>
                <select name="userInfo.userLevel">
                    <option value="1" <c:if test="${user.userInfo.userLevel==1}">selected="selected"</c:if>>总经理</option>
                    <option value="2" <c:if test="${user.userInfo.userLevel==1}">selected="selected"</c:if>>副总</option>
                    <option value="3" <c:if test="${user.userInfo.userLevel==1}">selected="selected"</c:if>>部门经理</option>
                    <option value="4" <c:if test="${user.userInfo.userLevel==1}">selected="selected"</c:if>>普通用户</option>
                </select>
            </td>
		</tr>
		<tr class="odd">
			<td>直属领导</td>
			<td colspan="3">
                <select name="userInfo.manager.userInfoId">
                    <c:forEach items="${userInfoList}" var="m">
                        <option value="${m.userInfoId}"
                                <c:if test="${m.userInfoId==user.userInfo.manager.userInfoId}">
                                    selected="selected"
                                </c:if>
                        >${m.name}</option>
                    </c:forEach>
                </select>
            </td>
		</tr>
		<tr class="odd">
			<td>备注信息</td>
			<td colspan="3">
                <textarea style="width:200px" name="userInfo.remark">${user.userInfo.remark}</textarea>
            </td>
            </td>
		</tr>
	</tbody>
</table>
</div>
 
</div>
 
 
</form>
</body>
</html>

