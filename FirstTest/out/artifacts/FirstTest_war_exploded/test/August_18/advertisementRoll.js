/**
 * Created by tarena on 2016/8/18.
 */
/*广告轮播*/
function rolling(num){
    var img=document.getElementById("image");
    img.src="../img/index/icon_g"+num+".png";
}


function autoRoll(){
    var img=document.getElementById("image");
    var arr=new Array();
    arr[1]="../img/index/icon_g1.png";
    arr[2]="../img/index/icon_g2.png";
    arr[3]="../img/index/icon_g3.png";
    arr[4]="../img/index/icon_g4.png";
    var i=1;
    if(i==4){
        i=1;
    }else{
        i++;
        img.src=arr[i];

    }
}
window.onload=function(){
    var img=document.getElementById("image");
    //用定时器设置图片轮播
    var times=setInterval(autoRoll,1000);
    //清除定时器
    img.onmouseover=function(){
        clearInterval(times);
    }
    //继续定时器
    img.onmouseout=function(){
        times=setInterval(autoRoll,1000);
    }
}

