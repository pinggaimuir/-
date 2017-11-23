/**
 * tooltip
 * 跟随鼠标的小动态小窗口
 * Created by tarena on 2016/9/27.
 */
var obj;
var sid;
$(document).ready(function(){
    getInfo();
    setInterval(getInfo,1000);
    var as=$("a");
    as.mouseover(function(event){
        sid=$(this).parent().attr("id");

        updatediv();
        //悬浮窗在鼠标位置显示
        var e=event||window.event;
        $("#window").show().css("position","absolute").css("left", e.clientX+5).css("top", e.clientY+5);
    });
    as.mouseout(function(event){
       $("#window").hide();
    });

});
function getInfo(){
    $.get("/ajaxMinWindowWervlet",null,function(data){
        obj=eval(data);
        var zhy=obj["s1001"];
        var puyh=obj["s1002"];
        var span1=$("#s1001 span");
        span1.html(zhy.now);
        if(zhy.now>zhy.yesterday){
            span1.css("color","red");
        }else{
            span1.css("color","blue");
        }
        var span2=$("#s1002 span");
        span2.html(puyh.now);
        if(puyh.now>puyh.yesterday){
            span2.css("color","red");
        }else{
            span2.css("color","blue");
        }
        updatediv();
    });
}
//更新弹出框显示的内容
function updatediv(){
    var stockobj=obj[sid];
    for(var proname in stockobj){
        if(proname!='name'){
            $("#"+proname).children("span").html(stockobj[proname]);
        }
    }
}