<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./head.jsp"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>个人中心</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link href="${ctx }/web/css/g.css" rel="stylesheet" />
		<link rel="stylesheet" href="${ctx }/web/css/5grid/core.css" />
		<link rel="stylesheet" href="${ctx }/web/css/5grid/core-desktop.css" />
		<link rel="stylesheet" href="${ctx }/web/css/5grid/core-1200px.css" />
		<link rel="stylesheet" href="${ctx }/web/css/5grid/core-noscript.css" />
		<link rel="stylesheet" href="${ctx }/web/css/style.css" />
		<link rel="stylesheet" href="${ctx }/web/css/style-desktop.css" />
		<script src="${ctx }/web/css/5grid/jquery.js"></script>
		<script src="${ctx }/web/css/5grid/init.js?use=mobile,desktop,1000px&amp;mobileUI=1&amp;mobileUI.theme=none&amp;mobileUI.titleBarHeight=60&amp;mobileUI.openerWidth=52"></script>
		<!--[if IE 9]><link rel="stylesheet" href="${ctx }/web/css/style-ie9.css" /><![endif]-->
	</head>
	<body>

		<!-- Header -->

			<div id="header-wrapper">
				 <jsp:include page="./menu.jsp"><jsp:param value="user" name="type"/> </jsp:include> 
			</div>

		<!-- Main -->

			<div id="main-wrapper" class="subpage">
				<div class="5grid-layout">
					<div class="row">
						<div class="3u">
						
							<!-- Sidebar -->
							
								<section>
									<h3>个人中心</h3>
									<ul class="link-list">
										<li><a href="${ctx }/com/user.do?type=note">我喜欢的歌曲</a></li>
										<li><a href="${ctx }/com/user.do?type=info">个人信息</a></li>
										<li><a href="${ctx }/com/user.do?type=pwd">修改密码</a></li>
									</ul>
								</section>
						</div>
						<div class="9u mobileUI-main-content">
					
							<!-- Content -->
									
								<article class="first" style="margin-top: 50px">
									 <c:if test="${type=='info' }">
									 
									 <form action="${ctx }/com/userUpdate.do" method="post" class="basic-grey" id="updateForm" style="margin-left: 50px">
										    <label>
										    <span>账号 :</span>
										    <input id="regname" type="text" value="${SimpleUser.user.uname }" readonly="readonly" name="uname" placeholder="您的账号" />
										    </label>
										     
										 	<label>
										    <span>姓名 :</span>
										    <input id="reguname" type="text" name="userName" value="${SimpleUser.user.userName }" placeholder="您的姓名" />
										    </label>
										     <label>
											<span>性别 :</span><select name="userGender">
											<option value="1" <c:if test="${SimpleUser.user.userGender=='1' }">selected</c:if> >男</option>
											<option value="0" <c:if test="${SimpleUser.user.userGender=='0' }">selected</c:if> >女</option>
											</select>
											</label>
											<label>
										    <span>生日 :</span>
										    <input id="regphone" type="text" name="userBirth" value="${SimpleUser.user.userBirth}" placeholder="您的生日.如:1990-08-08" />
										    </label>
											
											<label>
										    <span>联系电话 :</span>
										    <input id="regphone" type="text" name="userPhone" value="${SimpleUser.user.userPhone }" placeholder="您的联系电话" />
										    </label>
										    
										    <label>
										    <span>电子邮箱 :</span>
										    <input id="regemail" type="text" name="userEmail" value="${SimpleUser.user.userEmail }" placeholder="您的电子邮箱" />
										    </label>
										    
										    <label>
										    <span>联系地址 :</span>
										    <input id="regaddress" type="text" name="userAddress" value="${SimpleUser.user.userAddress }" placeholder="您的联系地址" />
										    </label>
											
										    <label>
										    <span>&nbsp;</span>
										    <input type="button" class="button" onclick="toupdate()" value="修改" />
										    </label>
										    </form>
									 </c:if>
									 
									 <c:if test="${type=='pwd' }">
									 	<form action="" method="post" class="basic-grey" id="updateForm" style="margin-left: 50px">
										    	<label>
											    <span>旧密码 :</span>
											    <input id="regpwd1" type="password" name="" placeholder="您的旧密码" />
											    </label>
											    <label>
											    <span>新密码 :</span>
											    <input id="regpwd2" type="password" name="" placeholder="您的新密码" />
											    </label>
											    <label>
											    <span>确认新密码 :</span>
											    <input id="regpwd3" type="password" name="" placeholder="您的新密码" />
											    </label>
											     <label>
											    <span>&nbsp;</span>
											    <input type="button" class="button" onclick="updatepwd()" value="修改密码" />
											    </label>
										</form>
									 </c:if>
								</article>
								 <c:if test="${type=='note' }">
									 	  <table width="100%" style="margin-top: 20px">
										 	<thead>
										 	<tr style="background: #f1f1f1;font-weight: bold">
										 		<th align="left" width="30%">歌曲名称</th>
										 		<th align="left"  width="10%">歌手</th>
										 		<th align="left"  width="10%">地区</th>
										 		<th align="left"  width="10%">风格</th>
										 		<th align="left"  width="10%">分类</th>
										 		<th align="left"  width="10%">歌手类型</th>
										 	</tr>
										 	</thead>
										 	<tbody style="font-size: 13px">
										 	<c:if test="${empty list }"><tr><td colspan="3" style="color: blue">木有找到呢 -_-</td></tr></c:if>
										 	<c:forEach items="${list}" var="item">
										 		<tr>
										 		<td align="left"><a href="${ctx }/com/getMusic.do?uid=${item.music.id}" target="_blank">${item.music.name }</a></td>
										 		<td>${item.music.singer }</td>
										 		<td>${item.music.area.name }</td>
										 		<td>${item.music.style.name }</td>
										 		<td>${item.music.musicType.name }</td>
										 		<td>${item.music.singerType.name }</td>
										 		</tr>
										 	</c:forEach>
										 	</tbody>
										 </table>
										
									 </c:if>					

						</div>
					</div>
				</div>
			</div>
			  <jsp:include page="./footer.jsp"></jsp:include> 
			  <script type="text/javascript">
				function tonote(){
					var replyUsername =$("#replyUsername").val();
					if(replyUsername==""){
						alert("请先登录");return false;
					}
					var message =$("#message").val();
					if(message==""){
						alert("请输入留言内容");return false;
					}
					if(message.length < 10){
						alert("留言内容至少10个字");return false;
					}
					if(message.length > 250){
						alert("留言内容最多250个字");return false;
					}
			  		$("#noteForm").submit();
			  	}
			  	function toupdate(){
			  		$("#updateForm").submit();
			  	}
			  	function updatepwd(){
			  		var regpwd1 = $("#regpwd1").val();
			  		var regpwd2 = $("#regpwd2").val();
			  		var regpwd3 = $("#regpwd3").val();
			  		if(regpwd1==""){
			  			alert("请输入旧密码");return false;
			  		}
			  		if(regpwd2==""){
			  			alert("请输入新密码");return false;
			  		}
			  		if(regpwd3==""){
			  			alert("请输入确认新密码");return false;
			  		}
			  		if(regpwd2!=regpwd3){
			  			alert("密码输入不一致");return false;
			  		}
			  		$.ajax({
			  	     		url:"${ctx }/com/userPwd.do",
			  	     		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			  	     		type:"post",
			  	     		dataType:"json",
			  	     		data:{"oldpwd":regpwd1,"newpwd":regpwd2},//window.encodeURI(中文值)：对字符串进行编码
			  	     		success:function(json){
			  	     			if(json.msg=="成功"){
			  	     				alert("修改密码成功");
			  						layer.close(pageii);
			  						window.location.href="${ctx }/com/user.do?type=pwd";
			  	     			}else{
			  	     				alert(json.msg);
			  	     			}
			  	     		},
			  	     		error:function(json){}
			  		});
			  	}
			  </script>
	</body>
</html>