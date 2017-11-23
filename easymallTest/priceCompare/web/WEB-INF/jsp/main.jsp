<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${appTitle }</title>

<link href="${ctx }/themes/azure/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${ctx }/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${ctx }/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
<link href="${ctx }/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
<!--[if IE]>
<link href="${ctx }/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->

<!--[if lte IE 9]>
<script src="${ctx }/js/speedup.js" type="text/javascript"></script>
<![endif]-->

<script src="${ctx }/js/jquery-1.7.2.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.bgiframe.js" type="text/javascript"></script>
<script src="${ctx }/xheditor/xheditor-1.2.1.min.js" type="text/javascript"></script>
<script src="${ctx }/xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
<script src="${ctx }/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>

<!-- svg图表  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+ -->
<!-- <script type="text/javascript" src="${ctx }/chart/raphael.js"></script> -->
<!-- <script type="text/javascript" src="${ctx }/chart/g.raphael.js"></script> -->
<!-- <script type="text/javascript" src="${ctx }/chart/g.bar.js"></script> -->
<!-- <script type="text/javascript" src="${ctx }/chart/g.line.js"></script> -->
<!-- <script type="text/javascript" src="${ctx }/chart/g.pie.js"></script> -->
<!-- <script type="text/javascript" src="${ctx }/chart/g.dot.js"></script> -->

<script src="${ctx }/js/dwz.core.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.util.date.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.validate.method.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.regional.zh.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.barDrag.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.drag.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.tree.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.accordion.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.ui.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.theme.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.switchEnv.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.alertMsg.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.contextmenu.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.navTab.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.tab.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.resize.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.dialog.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.dialogDrag.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.sortDrag.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.cssTable.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.stable.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.taskBar.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.ajax.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.pagination.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.database.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.datepicker.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.effects.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.panel.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.checkbox.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.history.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.combox.js" type="text/javascript"></script>
<script src="${ctx }/js/dwz.print.js" type="text/javascript"></script>
<!--
<script src="${ctx }/bin/dwz.min.js" type="text/javascript"></script>
-->
<script src="${ctx }/js/dwz.regional.zh.js" type="text/javascript"></script>

<script type="text/javascript">
$(function(){
	DWZ.init("${ctx }/dwz.frag.xml", {
		loginUrl:"login_dialog.html", loginTitle:"登录",
//		loginUrl:"login.html",	
		statusCode:{ok:200, error:300, timeout:301}, 
		pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, 
		debug:false,	
		callback:function(){
			initEnv();
			$("#themeList").theme({themeBase:"${ctx }/themes"});
		}
	});
});

</script>
</head>

<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<span   style="line-height:normal; color:white;font-size: 30px;font-weight: bold;margin-left: 210px;margin-top: 120px;text-align:center;">
				${appTitle }</span>
				<ul class="nav">
					<li  >${SESSION_BEAN.user.user.userName} | <a href="${ctx }/com/logout.do">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList">
				</ul>
			</div>
			<!-- navMenu -->
		</div>
		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>操作菜单</h2><div>收缩</div></div>
				<div class="accordion" fillSpace="sidebar">
						 <c:if test="${SESSION_BEAN.role=='SysUser' }">
							<div class="accordionHeader">
								<h2><span>Folder</span>系统管理</h2>
							</div>
							<div class="accordionContent">
								<ul class="tree treeFolder">
<!-- 									 <li><a href="${ctx}/sys/querySimpleUser.do" target="navTab" rel="mainquery">用户列表</a></li> -->
									 <li><a href="${ctx}/sys/add2GoodsObject.do" target="navTab" rel="baseAdd">爬虫配置</a></li>
									 <li><a href="${ctx}/sys/queryGoodsObject.do" target="navTab" rel="mainquery">商品列表</a></li>
								</ul>
							</div>
						</c:if>
						<div class="accordionHeader">
								<h2><span>Folder</span>个人资料</h2>
							</div>
						<div class="accordionContent">
							<ul class="tree treeFolder">
								 <li><a href="${ctx}/com/toSelf.do" target="navTab" rel="mainquery">修改个人资料</a></li>
							</ul>
						</div>
						 
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div>
					<div class="tabsRight">right</div>
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的主页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="pageFormContent" layoutH="80" style="margin-right:230px">
							<p style="color:red">欢迎您: ${SESSION_BEAN.user.user.uname}&nbsp;&nbsp;&nbsp;
							</p>
							<p style="color:red"> </p>
									<div class="divider"></div>
									<h2> </h2>
									<p> </p>
									<div class="divider"></div>
									<h2> </h2>
									<div class="unit"></div>
									<div class="divider"></div>
									<h2></h2>
									<pre style="margin:5px;line-height:1.4em">
									</pre>
									<div class="divider"></div>
									<h2></h2>
									<pre style="margin:5px;line-height:1.4em;">
									</pre>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">Copyright &copy; 2017  ${appTitle }</div>
</body>
</html>