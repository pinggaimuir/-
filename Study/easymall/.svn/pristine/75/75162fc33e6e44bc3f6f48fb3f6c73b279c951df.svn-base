<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" href="css/index.css"/>
		<link rel="stylesheet" href="css/head.css"/>
		<link rel="stylesheet" href="css/1.css"/>
		<title>欢迎光临EasyMall</title>
		<script type="text/javascript">
			function ajaxFunction(){
				var xmlHttp;
				try{ // Firefox, Opera 8.0+, Safari
					xmlHttp=new XMLHttpRequest();
				}
				catch (e){
					try{// Internet Explorer
						xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
					}
					catch (e){
						try{
							xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
						}
						catch (e){}
					}
				}
				return xmlHttp;
			}
		
			function login(){
				var username = document.getElementsByName("username")[0].value;
				var password = document.getElementsByName("password")[0].value;
				//发送AJAX请求检验用户名是否已经存在
				//--创建XMLHttpReqeust对象
				var xhr = ajaxFunction();
				//--设置监听
				var data = null;
				xhr.onreadystatechange=function(){
					if(xhr.readyState==4){
						if(xhr.status==200||xhr.status==304){
							data = xhr.responseText;
							data = (new Function("return " + data))(); 
							if(data.result =='true'){
								var content = document.getElementById("content");
								content.innerHTML = "<span style='color: red;font-size:16px'>欢迎, " + data.username + "</span>";
								
							}
						}
					}
				}
				//--打开链接
				xhr.open("POST","<%=request.getContextPath()%>/LoginServlet_2",true);
				//--发送请求
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");//通知服务器当前发送的数据时表单数据，请将实体内容中的值当做请求参数来处
				xhr.send("username="+encodeURI(username));
				return true;
			}
			
			function loginView(){
			
			}
			
		</script>
	</head>
	<body>
		<div id="common_head">
	<div id="line1">
		<div id="content">
			<a href="javascript:void(0)" onclick="loginView()" class="click_show">登录</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="<%=request.getContextPath() %>/regist.jsp">注册</a>
		</div>
	</div>
	<div id="line2">
		<img id="logo" src="img/head/logo.jpg"/>
		<input type="text" name=""/>
		<input type="button" value="搜索"/>
		<span id="goto">
			<a id="goto_order" href="#">我的订单</a>
			<a id="goto_cart" href="#">我的购物车</a>
		</span>
		<img id="erwm" src="img/head/qr.jpg"/>
	</div>
	<div id="line3">
		<div id="content">
			<ul>
				<li><a href="#">首页</a></li>
				<li><a href="#">全部商品</a></li>
				<li><a href="#">手机数码</a></li>
				<li><a href="#">电脑平板</a></li>
				<li><a href="#">家用电器</a></li>
				<li><a href="#">汽车用品</a></li>
				<li><a href="#">食品饮料</a></li>
				<li><a href="#">图书杂志</a></li>
				<li><a href="#">服装服饰</a></li>
				<li><a href="#">理财产品</a></li>
			</ul>
		</div>
	</div>
</div>

		<div id="index">
			<div id="line1">
				<img src="img/index/banner_big.jpg"/>
			</div>
			<div id="line2">
				<img id="line2_1" src="img/index/adv1.jpg"/>
				<img id="line2_2" src="img/index/adv2.jpg"/>
				<img id="line2_3" src="img/index/adv_l1.jpg"/>
			</div>
			<div id="line3">
				<img id="line3_1" src="img/index/adv3.jpg"/>
				<img id="line3_2" src="img/index/adv4.jpg"/>
				<div id="line3_right">
					<img id="line3_3" src="img/index/adv_l2.jpg"/>
					<img id="line3_4" src="img/index/adv_l3.jpg"/>
				</div>
			</div>
			<div id="line4">
				<img src="img/index/217.jpg"/>
			</div>
			<div id="line5">
				<span id="line5_1">
					<img src="img/index/icon_g1.png"/>&nbsp;&nbsp;500强企业 品质保证
				</span>
				<span id="line5_2">
					<img src="img/index/icon_g2.png"/>&nbsp;&nbsp;7天退货 15天换货 
				</span>
				<span id="line5_3">
					<img src="img/index/icon_g3.png"/>&nbsp;&nbsp;100元起免运费 
				</span>
				<span id="line5_4">
					<img src="img/index/icon_g4.png"/>&nbsp;&nbsp;448家维修网点 全国联保 
				</span>
			</div>
		</div>
		<%@ include file="_foot.jsp" %>
	</body>
	
	
	<!--div 盒子模型，高度，宽度 放置其它内容-->
	<div id="login">
		<div id="title">用户登录 <a href="#" class="close"></a></div>
		<!-- <p class="desc"><img src="images/new.png" alt="" width="40" height="40" align="absmiddle" />
		号外！我的DIY主题管理功能上线 了！ 即刻起，登陆360手机美化，可以将您制作的DIY主题保存到360美化云端，还可以使用我的DIY主题管理等高级功能！</p> -->
		
		<div id="lgcon">
			<from action="#" method="post">
				<p class="txt" >账号<input type="text" name="username"/></p>
				<p class="txt" >密码<input type="password" name="password"/></p>
				<p class="check"><input type="checkbox" />下次自动登陆 		<a href="#">忘记密码</a></p>
				
				<P class="btn"><input type="submit" value=""  class="close"/></P>
			</from>
		</div>
	</div>
	
	<div class="layer"></div>

	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript">
		//显示灰色图层和登陆界面
		$(".click_show").click(function(){
			$(".layer").show(); //显示灰色图层
			$("#login").show(); //显示登陆界面
			tm();
		});
		
		//计算并设置登陆窗口距左右边的距离
		function tm(){
			var _left = ($(window).width()-$("#login").width()) / 2;	//登陆窗口距左边的距离
			var _top = ($(window).height()-$("#login").height()) / 2;	//登陆窗口距上边的距离
			//alert(_top);
			$("#login").css({left:_left,top:_top });
		}
		
		//关闭灰色图层和登陆界面
		$("a.close").click(function(){
			$(".layer").hide(); //关闭灰色图层
			$("#login").hide(); //关闭登陆界面
		});
		
		//关闭灰色图层和登陆界面
		$("input.close").click(function(){
			$(".layer").hide(); //关闭灰色图层
			$("#login").hide(); //关闭登陆界面
			
			//登陆..
			login();
		});
		
		//注册窗口拖动事件的函数
		
		
	</script>
	
	<script type="text/javascript">
		//注册窗口拖动事件的函数
		window.onload = function(){
			//只需在这里填上要拖动窗口的id值，其他不用改变
			init(document.getElementById("title"));
		}
		
		var l = 0, t = 0, x = 0, y = 0;
		var isOver = false;
		var zindex = 3;
		
		function init(titleDom){
			//第一种
			var thisDom = titleDom;//获取当前title节点对象
			var parentDom = thisDom.parentNode;
			titleDom.onmousedown = function(event){
				var e = event || window.event; //为了兼容ie和火狐
				x = e.clientX;	//获取鼠标所在点的x坐标
				y = e.clientY;	//获取鼠标所在点的Y坐标
				
				l = parseInt(parentDom.offsetLeft);	//距离浏览器左边的位置left
				t = parseInt(parentDom.offsetTop);	//距离浏览器上边的位置top
				isOver = true;	//定义拖动标识，防止卡顿
				zindex++;
				parentDom.style.zIndex = zindex;
				document.onmousemove = function(event){
					if(isOver){
						var e = event || window.event; //为了兼容ie和火狐
						var newLeft = l + e.clientX - x;	//新的zuobianju
						var newTop = t + e.clientY - y;		//新的顶部边距
						parentDom.style.left = newLeft+"px";
						parentDom.style.top = newTop+"px";
						
					}
				};	//鼠标移动的事件
				document.onmouseup = function(event){
					if(isOver){
						isOver = false;	//还原标识
					}
				};	//鼠标松开的事件
				
			}
		}
	</script>
	
</html>
