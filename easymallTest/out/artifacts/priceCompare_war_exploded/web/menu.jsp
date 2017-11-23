<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./head.jsp"%>
<!-- <script src="${ctx }/js/jquery-1.7.2.js" type="text/javascript"></script> -->
<link type="text/css" rel="stylesheet" href="${ctx }/layer/skin/layer.css" />
<link type="text/css" rel="stylesheet" href="${ctx }/web/css/form.css" />
<script type="text/javascript" src="${ctx }/layer/layer.min.js"></script>
<header class="5grid-layout" id="site-header">
	<div class="row">
		<div class="12u">
			<div id="logo">
				<h1 class="mobileUI-site-name" style="font-size: 30px">${appTitle}</h1>
			</div>
			<nav class="mobileUI-site-nav" id="site-nav">
				<ul>
					<li id="menuli_list"><a href="${ctx}/0/queryGoods.do">商品</a></li>
					<%--<c:if test="${empty SimpleUser }">--%>
						<%--<li><a href="javascript:;" onclick="tologin()" >登录</a></li>--%>
						<%--<li><a href="javascript:;" onclick="toreg()" >注册</a></li>--%>
					<%--</c:if>--%>
					<%--<c:if test="${not empty SimpleUser }">--%>
						<%--<li id="menuli_user"><span style="font-size: 16px;color:red">欢迎您:</span><a href="${ctx}/com/user.do" style="font-size: 15px;color:red">${SimpleUser.user.userName }</a></li>--%>
						<%--<li ><a href="${ctx}/com/userLogout.do">退出</a></li>--%>
					<%--</c:if>--%>
				</ul>
			</nav>
		</div>
	</div>
</header>
 
<div id="regbox" style="display: none;width: 700px">
    <form action="" method="post" class="basic-grey">
    <label>
    <span>*账号 :</span>
    <input id="regname" type="text" name="" placeholder="您的账号" />
    </label>

    <label>
    <span>*密码 :</span>
    <input id="regpwd" type="password" name="" placeholder="您的密码" />
    </label>
 	<label>
    <span>*姓名 :</span>
    <input id="reguname" type="text" name="" placeholder="您的姓名" />
    </label>
     <label>
	<span>性别 :</span><select id="reggender">
	<option value="1">男</option>
	<option value="0">女</option>
	</select>
	</label>
	
	
	<label>
    <span>联系电话 :</span>
    <input id="regphone" type="text" name="" placeholder="您的联系电话" />
    </label>
    
    <label>
    <span>电子邮箱 :</span>
    <input id="regemail" type="text" name="" placeholder="您的电子邮箱" />
    </label>
    
    <label>
    <span>联系地址 :</span>
    <input id="regaddress" type="text" name="" placeholder="您的联系地址" />
    </label>
	
    <label>
    <span>&nbsp;</span>
    <input type="button" class="button" onclick="doreg()" value="注册" />
    </label>
    </form>
</div>
<div id="loginbox" style="display: none;width: 700px">
    <form action="${ctx }/com/userLogin.do" method="post" class="basic-grey">
    <label>
    <span>账号 :</span>
    <input id="loginname" type="text" name="name" placeholder="您的账号" />
    </label>

    <label>
    <span>密码 :</span>
    <input id="loginpwd" type="password" name="email" placeholder="您的密码" />
    </label>
 
    <label>
    <span>&nbsp;</span>
    <input type="button" class="button" onclick="doLogin()" value="登录" />
    </label>
    </form>
</div>
<%String type = request.getParameter("type");
%>
<script type="text/javascript">
<!--
var pageii;
$("li[id^='menuli_']").each(function(){
	  if($(this).attr("id")=="menuli_"+"<%=type%>"){
		  $(this).addClass("current_page_item");
	  }else{
		  $(this).removeClass("current_page_item");
	  }
});
function tologin(){
	layer.close(pageii);
	pageii = $.layer({
	    type: 1,
	    title: '用户登录',
	    area: ['720', '240'],
	    border: [10, 0.3, '#000'],
	    shade: [0.3, '#000'],
	    closeBtn: [1, true],
	    shadeClose: true,
	    fadeIn: 300,
	    fix: true,
	    page: {dom: '#loginbox',},
	    close:function(){
	    }
	});
}
function toreg(){
	layer.close(pageii);
	pageii = $.layer({
	    type: 1,
	    title: '用户注册',
	    area: ['720', '580'],
	    border: [10, 0.3, '#000'],
	    shade: [0.3, '#000'],
	    closeBtn: [1, true],
	    shadeClose: true,
	    fadeIn: 300,
	    fix: true,
	    closeBtn: [1, true],
	    page: {dom: '#regbox',},
	    close:function(){
	    	 
	    }
	});
}
function doLogin(){
	var username = $("#loginname").val();
	var password = $("#loginpwd").val();
	if(username==""){
		alert("请输入账号");return false;
	}
	if(password==""){
		alert("请输入密码");return false;
	}
	$.ajax({
     		url:"${ctx }/com/userLogin.do",
     		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
     		type:"post",
     		dataType:"json",
     		data:{"name":""+username+"","password":""+password+""},//window.encodeURI(中文值)：对字符串进行编码
     		success:function(json){
     			if(json.msg=="成功"){
					layer.close(pageii);
					window.location.href="${ctx }/web/index.jsp";
     			}else{
     				alert(json.msg);
     			}
     		},
     		error:function(json){}
	});
}
function doreg(){
	var username = $("#regname").val();
	var password = $("#regpwd").val();
	var reguname = $("#reguname").val();
	var reggender = $("#reggender").val();
	var regphone = $("#regphone").val();
	var regemail = $("#regemail").val();
	var regaddress = $("#regaddress").val();
	if(username==""){
		alert("请输入账号");return false;
	}
	if(password==""){
		alert("请输入密码");return false;
	}
	if(reguname==""){
		alert("请输入姓名");return false;
	}
	$.ajax({
     		url:"${ctx }/com/userReg.do",
     		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
     		type:"post",
     		dataType:"json",
     		data:{"user.uname":username,"user.userPassword":password,"user.userName":reguname,"user.userGender":reggender,"user.userPhone":regphone,"user.userEmail":regemail,"user.userAddress":regaddress},//window.encodeURI(中文值)：对字符串进行编码
     		success:function(json){
     			if(json.msg=="成功"){
					alert("恭喜您,注册成功");
					layer.close(pageii);
					window.location.href="${ctx }/web/index.jsp";
     			}else{
     				alert(json.msg);
     			}
     		},
     		error:function(json){}
	});
}
//-->
</script>
