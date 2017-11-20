// JavaScript Document
$(document).ready(function() {
	$(".main ul li").addClass("old"); 
	//$(".main ul li:even").addClass("alt");
	$(".main ul li").hover(function(){
		$(this).addClass("hover");},function(){$(this).removeClass("hover");}
	);
		
	$(".main").each(function(index) {
		$(".main h5").eq(index).click(function(){
			if($(".main").children("ul").hasClass("navin")) {
				$(".main").children("ul").slideUp(100);
			}
			$(".main h5").css("background-image","url('../images/q.gif')");
			if($(this).next("ul").css("display") == "none")
			{
				$(this).css("background-image","url('../images/h.gif')");
				$(this).next("ul").slideDown(1000);
				$(this).next("ul").addClass("navin");
			}
			else {
				$(this).css("background-image","url('../images/q.gif')");
			}			
			/*if($(this).css("background-image").indexOf("q.gif")>=0) {
				$(this).css("background-image","url('y.gif')");
			} else {
				$(this).css("background-image","url('q.gif')");
			}*/
		});
	});

});