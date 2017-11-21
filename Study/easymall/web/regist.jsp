<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>欢迎注册EasyMall</title>
	<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="css/regist.css"/>
	<script type="text/javascript">
		/*
			表单校验
		 */
		function checkForm(){
			var flag = true;
			//非空校验
			flag = checkNull("username", "用户名不能为空") && flag;
			flag = checkNull("password", "密码不能为空") && flag;
			flag = checkNull("password2", "确认密码不能为空") && flag;
			flag = checkNull("nickname", "昵称不能为空") && flag;
			flag = checkNull("email", "邮箱不能为空") && flag;
			flag = checkNull("valistr", "验证码不能为空") && flag;
			
			//两次密码是否一致校验
			flag = checkPassword("password", "两次密码不一致") && flag;
			//邮箱格式校验
			flag = checkEmail("email", "邮箱格式不正确") && flag;
			
			
			
			return flag;
		}
		
		/*
			邮箱格式校验
		 */
		function checkEmail(name, msg){
			var email = document.getElementsByName(name)[0].value;
			var reg = /^\w+@\w+(\.\w+)+$/;
			setMsg(name, "");
			if(email == ""){
				setMsg(name , "邮箱不能为空");
				return false;
			}
			
			if(!reg.test(email)){
				setMsg(name , msg);
				return false;
			}
			
			return true;
		}
		/*
			校验两次密码是否一直
		 */
		function checkPassword(name, msg){
			var psw1 = document.getElementsByName(name)[0].value;
			var psw2 = document.getElementsByName(name+"2")[0].value;
			
			setMsg(name+"2", "");
			if(psw2 == ""){
				setMsg(name+"2", "确认密码不能为空");
				return false;
			}
			
			if(psw1 != "" && psw2 != "" && psw1 != psw2 ){
				setMsg(name+"2", msg);
				return false;
			}
			return true;
		}
		
		/*
			非空校验
		 */
		function checkNull(name, msg){
			var value = document.getElementsByName(name)[0].value;
			setMsg(name, "");
			if(value == ""){
				setMsg(name, msg);
				return false;
			}
			return true;
		}
		
		/*
			设置提示消息的方法
		 */
		function setMsg(name, msg) {
			document.getElementById(name + "_msg").innerHTML = "<font style='color:red;'>&nbsp;&nbsp;" + msg + "</font>";
		}
	</script>
</head>
<body>
	<h1>欢迎注册EasyMall</h1>
	<form action="<%=request.getContextPath() %>/RegistServlet" method="POST" onsubmit="return checkForm()">
		<table>
			<tr>
				<%
					Random random=new Random();
					String token=random.nextInt()+"";
					session.setAttribute("token",token);
				%>
				<input type="hidden" name="token" value="${token}"/>
			</tr>
			<tr>
				<td colspan="2" style="color:red;text-align:center;">
					<%=	request.getAttribute("msg") == null? "" :  request.getAttribute("msg") %>
				</td>
			</tr>
			<tr>
				
				<td class="tds">用户名：</td>
				<td><input type="text" name="username" value="<%= request.getParameter("username") == null ? "": request.getParameter("username") %>" onblur="checkNull('username', '用户名不能为空')">
					<span id="username_msg"></span>
				</td>
			</tr>
			<tr>
				<td class="tds">密码：</td>
				<td><input type="password" name="password" onblur="checkNull('password', '密码不能为空')">
					<span id="password_msg"></span>
				</td>
			</tr>
			<tr>
				<td class="tds">确认密码：</td>
				<td><input type="password" name="password2"  onblur="checkPassword('password', '两次密码不一致')">
					<span id="password2_msg"></span>
				</td>
			</tr>
			<tr>
				<td class="tds">昵称：</td>
				<td><input type="text" name="nickname"  value="<%= request.getParameter("nickname") == null ? "": request.getParameter("nickname") %>" onblur="checkNull('nickname', '昵称不能为空')">
					<span id="nickname_msg"></span>
				</td>
			</tr>
			<tr>
				<td class="tds">邮箱：</td>
				<td><input type="text" name="email"  value="<%= request.getParameter("email") == null ? "": request.getParameter("email") %>" onblur="checkEmail('email', '邮箱格式不正确')">
					<span id="email_msg"></span>
				</td>
			</tr>
			<tr>
				<td class="tds">验证码：</td>
				<td><input type="text" name="valistr"  onblur="checkNull('valistr', '验证码不能为空')"><img id="yzm_img" src="img/regist/yzm.png" style="cursor: pointer" />
					<span id="valistr_msg"></span>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="注册用户"/>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
