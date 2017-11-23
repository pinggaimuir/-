<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./head.jsp"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>${item.goodsName }</title>
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
		<link rel="stylesheet" type="text/css" href="${ctx }/web/page/style.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/web/page/simplePagination.css" />
		<script src="${ctx }/js/jquery-1.7.2.js" type="text/javascript"></script>
		<script src="${ctx }/web/css/5grid/init.js?use=mobile,desktop,1000px&amp;mobileUI=1&amp;mobileUI.theme=none&amp;mobileUI.titleBarHeight=60&amp;mobileUI.openerWidth=52"></script>
		<!--[if IE 9]><link rel="stylesheet" href="${ctx }/web/css/style-ie9.css" /><![endif]-->
		<link rel="stylesheet" href="${ctx }/web/css/btn.css" />
		
		<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
		<script src="http://cdn.hcharts.cn/highcharts/modules/exporting.js"></script>
		
		
		<style type="text/css">
		.menu_list{width:268px;margin:0 auto;}
		.menu_head{height:47px;line-height:47px;padding-left:38px;font-size:14px;color:#525252;cursor:pointer;border:1px solid #e1e1e1;position:relative;margin:0px;font-weight:bold;background:#f1f1f1 url(images/pro_left.png) center right no-repeat;}
		.menu_list .current{background:#f1f1f1 url(images/pro_down.png) center right no-repeat;}
		.menu_body{line-height:38px;border-left:1px solid #e1e1e1;backguound:#fff;border-right:1px solid #e1e1e1;}
		.menu_body a{display:block;height:38px;line-height:38px;padding-left:38px;color:#777777;background:#fff;text-decoration:none;border-bottom:1px solid #e1e1e1;}
		.menu_body a:hover{text-decoration:none;}
		</style>
	</head>
	<body>

		<!-- Header -->

			<div id="header-wrapper">
				 <jsp:include page="./menu.jsp"><jsp:param value="list" name="type"/> </jsp:include> 
			</div>

		<!-- Main -->

			<div id="main-wrapper" class="subpage">
				<div class="5grid-layout">
					<div class="row">
						<div class="3u">
								<section>
								</section>
						</div>
						<div class="9u mobileUI-main-content">
					
							<!-- Content -->
								 
									<article class="first" style="margin-top: 1px;font-size: 14px">
									<div style="width: 250px;float: left">
									<img alt="" src="${item.picUrl}" width="250px" height="250px">
									</div>
									<div style="float: left;margin-left: 20px">
										<h3>${item.goodsName }</h3>
										<h4>来源: <a href="${item.goodsUrl }">${item.goodsFrom}</a></h4>
										<h4>价格: ${item.price}</h4>
										
										 
									</div>
									</article>	
								 <div style="clear:both"></div>
								 <article class="first" style="margin-top: 1px;font-size: 14px">
								 <div style="width: 800px;height: 450px">
								<div id="container2" style="min-width: 310px; width:800px; height: 450px; "></div>
								</div>
								 </article>
						</div>
					</div>
				</div>
			</div>

		<!-- Footer -->

			  <jsp:include page="./footer.jsp"></jsp:include> 
<script type="text/javascript">
function tolike(){
	if("${SimpleUser.id}"==""){
		alert("请选登录");
		return false;
	}
	var score = $("#userscore").val();
	window.location.href="${ctx }/com/like.do?uid=${item.id}&score="+score;
	return true;
}
$(function () {
    $('#container2').highcharts({
        title: {
            text: '一周价格变动信息',
            x: -20 //center
        },
        subtitle: {
            text: '${item.goodsName }',
            x: -20
        },
        xAxis: {
            categories: ${times}
        },
        yAxis: {
            title: {
                text: '价格 (元)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '元'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [${values}]
    });
});
</script>
	</body>
</html>