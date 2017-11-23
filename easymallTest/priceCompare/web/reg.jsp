<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
		<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<title>注册</title>
			<link href="login/css.css" rel="stylesheet" />
			<link href="login/style.css" rel="stylesheet" />
			<script src="login/jquery.js" type="text/javascript"></script> 
    		<script src="login/bootstrap.min.js" type="text/javascript"></script> 
		</head>
		<body background="">
			<div align="center">
				<div class="navbar navbar-inverse navbar-fixed-top">
					<div class="navbar-inner" style="height: 10px">
					</div>
				</div>
				<div class="container" style="margin-top:50px; width:500px;" align="left">
			<div class="well">
			<c:if test="${not empty regErrorMessage }">
				<div class="alert alert-error"><strong>提示：</strong><br /><i class="icon-exclamation-sign"></i> ${regErrorMessage}</div>
			</c:if>
			<%session.removeAttribute("regErrorMessage"); %>
			<div class="container" style="width: 500px;">
				<form action="${pageContext.request.contextPath }/com/reg.do" method="post" id="login" name="login">
					<h3 style="color: #802C59" align="center">用户注册</h3>
					<div class="control-group">
						<label style="font-size:18px;">登录账号:</label>
						<input type="text" name="user.uname" id="username" value="" style=" width: 283px;" maxlength="14" rel="tooltip" data-original-title="请输入您的登录账号" data-placement="right"/>
					</div>
					<div class="control-group">
						<label style="font-size:18px;">姓名:</label>
						<input type="text" name="user.userName" id="username" value="" style=" width: 283px;" maxlength="14" rel="tooltip" data-original-title="请输入您的姓名" data-placement="right"/>
					</div>
					
					<div class="control-group">
						<label style="font-size:18px">登录密码:</label>
						<input type="password" name="user.userPassword" id="password" value="" style="width: 283px;" maxlength="30" rel="tooltip" data-original-title="请输入您的登录密码" data-placement="right"/>
					</div>
					<div class="control-group">
						<label style="font-size:18px">性别:</label>
							<select name="user.userGender">
									<option value="1">男</option>
									<option value="0">女</option>
								</select>
					</div>
					<div class="control-group">
						<label style="font-size:18px;">联系电话:</label>
						<input type="text" name="user.userPhone"   value="" style=" width: 283px;" maxlength="14" rel="tooltip" data-original-title="请输入您的联系电话" data-placement="right"/>
					</div>
					<div class="control-group">
						<label style="font-size:18px;">电子邮箱:</label>
						<input type="text" name="user.userEmail"   value="" style=" width: 283px;" maxlength="14" rel="tooltip" data-original-title="请输入您的电子邮箱" data-placement="right"/>
					</div>
					<div class="control-group">
						<label style="font-size:18px;">联系地址:</label>
						<input type="text" name="user.userAddress"   value="" style=" width: 283px;" maxlength="14" rel="tooltip" data-original-title="请输入联系地址" data-placement="right"/>
					</div>
					<div class="control-group" style="margin-bottom:0px;">
						<div class="input-prepend">
						<span class="add-on" style="border:0px;">
								<label style="margin-left:2px; width:1px;">
								</label>
							</span>
							<button type="submit" class="btn btn-info" style="margin-bottom: 10px; margin-left: 80px;width:167px;">注     册</button>
							<button type="button" class="btn btn-info" style="margin-bottom: 10px; margin-left: 20px;width:167px;" onclick="tologin()">返回登录</button>
						</div>
					</div>
				</form>
			</div>
			</div>
			<script type="text/javascript">
				$("[rel=tooltip]").tooltip();
<%--				$("#username").keyup(function() {--%>
<%--					if (this.value.match(/[^a-zA-Z0-9 ]/g)) this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');--%>
<%--				});--%>
				$("#captcha-code").keyup(function() {
					if (this.value.match(/[^a-zA-Z0-9 ]/g)) this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
				});
				$("#login").submit(function() {
					if($("#username").val().length<2 || $("#username").val().length>14) {
						$("#usernamecontrol").addClass("error");
						$("#username").focus();
						return false;
					} else $("#usernamecontrol").removeClass("error");
					if($("#password").val().length<4 || $("#password").val().length>30) {
						$("#passwordcontrol").addClass("error");
						$("#password").focus();
						return false;
					} else $("#passwordcontrol").removeClass("error");
					 
					return true;
				});
				function tologin(){
					window.location="./index.jsp";
				}
			</script>
						<div align="left" style="width:100%; border-top:1px #CCC solid; margin-top:10px;">
							<table style="width:100%;">
								<tr>
									<td align="left">Copyright &copy; 2017  管理系统</td>
								</tr>
							</table>
						</div>
						
					</div>
				</div>
			</body>
		</html>	
