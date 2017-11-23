$(function(){
	//在heatop处设置年月日；
	function dates(){
		var dateobj = new Date();
		var timedate = dateobj.getDate();
		var timeday	= dateobj.getDay()+1;
		var timearr = {"1":"一","2":"二","3":"三","4":"四","5":"五","6":"六","7":"日"}
		var timemonth = dateobj.getMonth()+1;
		var timeyear = dateobj.getFullYear();
		var timehours = dateobj.getHours();
		var timemin = dateobj.getMinutes();
		var time = timeyear+"年"+timemonth+"月"+timedate+"日"+"  星期"+timearr[timeday]+"  "+timehours+":"+timemin;
		$("#heatime").html(time);
	}
	dates();
	setInterval(function(){
		dates();
	},10000);

	function searchs(con){
		console.log(con)
	}
	var searchcon = $(".search>#searchbox").val();
	searchs(searchcon);


})