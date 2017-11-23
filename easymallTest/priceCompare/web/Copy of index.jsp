<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<% String title = "TITLE"; %>
<title><%=title %></title> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="login/js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="login/images/login.js"></script>
<link href="login/css/login2.css" rel="stylesheet" type="text/css" />
<script src="login/bootstrap.min.js" type="text/javascript"></script> 
</head>
<body>
<h1><%=title %><sup>2017</sup></h1>

<div class="login" style="margin-top:50px;">
    
    <div class="header">
        <div class="switch" id="switch"><a class="switch_btn_focus" id="switch_qlogin" href="javascript:void(0);" tabindex="7">登录</a>
<!-- 			<a class="switch_btn" id="switch_login" href="javascript:void(0);" tabindex="8">快速注册</a><div class="switch_bottom" id="switch_bottom" style="position: absolute; width: 64px; left: 0px;"></div> -->
        </div>
    </div>    
  
    
 <div class="web_qr_login" id="web_qr_login" style="display: block;">    

         <!--登录-->
         <div class="web_login" id="web_login">
            <div class="login-box">
	<div class="login_form">
	<form action="${pageContext.request.contextPath }/com/login.do" name="loginform" id="loginform" accept-charset="utf-8" id="login_form" class="loginForm" method="post">
             <div class="uinArea" id="uinArea">
              <label class="input-tips" for="u">帐号：</label>
              <div class="inputOuter" id="uArea">
                  <input type="text" name="loginid" id="username" value="admin" class="inputstyle" rel="tooltip" data-original-title="请输入您的登陆账号" data-placement="button"/>
              </div>
             </div>
             <div class="pwdArea" id="pwdArea">
		<label class="input-tips" for="p">密码：</label> 
		<div class="inputOuter" id="pArea">
			<input type="password" name="password" id="password" value="123456" rel="tooltip" data-original-title="请输入您的登录密码" class="inputstyle" data-placement="button"/>
		</div>
             </div>
             <div class="uinArea" id="uinArea">
              <label class="input-tips" for="u">角色：</label>
              <div class="inputOuter" id="uArea">
                  <select name="logintype" class="selectstyle">
                  	<option value="SysUser">管理员</option>
                  </select>
              </div>
             </div>
             <div class="uinArea" id="uinArea">
              <label class="input-tips" for="u">验证码:</label>
              <div class="inputOuter" id="uArea">
                 <a href="javascript:void(0);"><img style="border: 0px; float: right" width="110px" height="40px" src="${pageContext.request.contextPath }/checkcode" alt="验证码" align="left" onclick="this.src = '${pageContext.request.contextPath }/checkcode?' + Math.random();" /></a>
                 <input type="text" id="captcha-code" name="checkcode" class="codestyle" rel="tooltip" data-original-title="请输入验证码" data-placement="button"/>
              </div>
             </div>
             <c:if test="${not empty signErrorMessage }">
				<div class="alert alert-error"><strong>提示：</strong><br /><i class="icon-exclamation-sign"></i> ${signErrorMessage }</div>
			</c:if>
			<%session.removeAttribute("signErrorMessage"); %>
             <div style="padding-left:50px;margin-top:20px;"><input type="submit" value="登 录" style="width:150px;" class="button_blue"/></div>
           </form>
    </div>
    </div>
    </div>
         <!--登录end-->
</div>

  <!--注册-->
    <div class="qlogin" id="qlogin" style="display: none; ">
   
    <div class="web_login"><form name="form2" id="regUser" accept-charset="utf-8"  action="" method="post">
	      <input type="hidden" name="to" value="reg"/>
		      		       <input type="hidden" name="did" value="0"/>
        <ul class="reg_form" id="reg-ul">
        		<div id="userCue" class="cue">快速注册请注意格式</div>
                <li>
                	
                    <label for="user"  class="input-tips2">用户名：</label>
                    <div class="inputOuter2">
                        <input type="text" id="user" name="user" maxlength="16" class="inputstyle2"/>
                    </div>
                    
                </li>
                
                <li>
                <label for="passwd" class="input-tips2">密码：</label>
                    <div class="inputOuter2">
                        <input type="password" id="passwd"  name="passwd" maxlength="16" class="inputstyle2"/>
                    </div>
                    
                </li>
                <li>
                <label for="passwd2" class="input-tips2">确认密码：</label>
                    <div class="inputOuter2">
                        <input type="password" id="passwd2" name="" maxlength="16" class="inputstyle2" />
                    </div>
                    
                </li>
                
                <li>
                 <label for="qq" class="input-tips2">QQ：</label>
                    <div class="inputOuter2">
                       
                        <input type="text" id="qq" name="qq" maxlength="10" class="inputstyle2"/>
                    </div>
                   
                </li>
                
                <li>
                    <div class="inputArea">
                        <input type="button" id="reg"  style="margin-top:10px;margin-left:85px;" class="button_blue" value="同意协议并注册"/> <a href="#" class="zcxy" target="_blank">注册协议</a>
                    </div>
                    
                </li><div class="cl"></div>
            </ul></form>
           
    
    </div>
   
    
    </div>
    <!--注册end-->
</div>
<script type="text/javascript">
	$("[rel=tooltip]").tooltip();
	$("#captcha-code").keyup(function() {
		if (this.value.match(/[^a-zA-Z0-9 ]/g)) this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
	});
	$("#loginform").submit(function() {
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
		if($("#captcha-code").val().length!=4) {
			$("#captcha-codecontrol").addClass("error");
			$("#captcha-code").tooltip("show");
			$("#captcha-code").focus();
			return false;
		} else $("#captcha-codecontrol").removeClass("error");
		return true;
	});
	function toreg(){
		window.location="./reg.jsp";
	}
</script>
</body></html>