var timeoutid;
$(document).ready(function() {
	/*
	$("li").mouseover(function() {
		//alert("over");
	}).mouseout(function() {
		//alert("out");
	});
	*/
	$("#tabfirst li").each(function(index) {
		//ÿһlijQueryִfunctionеĴ
		//indexǵǰִfunctionliӦliɵе
		//ͿҵǰǩӦ
		$(this).mouseover(function() {
				timeoutid = setTimeout(function() {
				//ԭʾ
				$("div.contentin").removeClass("contentin"); 
				//classΪtabinliclass
				$("#tabfirst li.tabin").removeClass("tabin");
				//ʾǰǩӦ
				$("div.contentfirst").eq(index).addClass("contentin");
				$("#tabfirst li").eq(index).addClass("tabin");//<==>$("li:eq("+index+")").addClass("tabin");			
			},1000);
			
			}).mouseout(function(){
				clearTimeout(timeoutid);
		});
	});
	
	//ҳװ֮󣬱ǩ2װ뾲̬HTMLҳ
	//Ҫҵ
	$("#realcontent").load("load.html");
	
	//ҵǩ2Ӧǩע¼
	
	$("#tabsecond li").each(function(index) {
		
		
		$(this).click(function() {
			$("#tabsecond li.tabin").removeClass("tabin");
			$(this).addClass("tabin");
			if(index == 0) {
				//װҳ
				$("#realcontent").load("load.html");
			} else if(index == 1) {
				//װ붯̬ҳ
				$("#realcontent").load("dt.php div");
			} else if(index == 2) {
				$("#realcontent").load("yc.php");
			}
		});
	});
	//对图片绑定ajax请求开始和交互结束事件
	$("#contentsecond img").bind("ajaxStart",function() {
		$(this).show();											  }).bind("ajaxStop",function() {
		$(this).hide(1000);										  });
});