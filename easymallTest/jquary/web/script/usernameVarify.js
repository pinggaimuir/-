/**
 * Created by tarena on 2016/9/25.
 */
$(document).ready(function(){
   $("#username").blur(function(){
       //解决中文乱码1、浏览器发送数据encodeURI，服务器接收数据iso8859-1
       //解决中文乱码2、浏览器做两次encodeURI，第二次是在第一次的基础上把%变成%25服务器端做一次URLDecode.decode,
       // 因为服务器在接收数据时回自动做一次decode
       var url="/CheckUsernameServlet?username="+encodeURI(encodeURI($("#username").val()));
       url=convertUrl(url);
       /*$.get(url,null,function(data){
           $("#tip").html(data);
       });*/
       $("#tip").load(url);
   });
    /*窗口的显示与隐藏*/
    $("#btn1").click(function(){
        //$("#win").css("display","block");
        $("#win").fadeToggle(1000).css("background-color","yellow");
    });
    /*小窗口的关闭*/
    $("#close").click(function(){
        //$("#win").fadeOut(1);
        $("#win").css("display","none");
    });
    /*级联菜单的显示与隐藏*/
    $("#menu1>a").click(function(){
        //$("#menu1 li").toggle(500);
        var lis=$(this).nextAll("li");
        lis.toggle("show");
    });
    /*点击加载元素中的a属性中的页面到content的div中*/
    $("#menu1>li>a").click(function(){
       $("#content").load($(this).attr("id"));
    });
});
//给url加一个时间戳，每次访问都是不同的地址，使浏览器不使用缓存
function convertUrl(url){
    var timestemp=(new Date()).valueOf();
    if(url.indexOf("?")){
        url+="&t="+timestemp;
    }else{
        url+="?t="+timestemp;
    }
    return url;
}

