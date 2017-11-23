/**
 * Created by tarena on 2016/9/18.
 */
function ajaxFunction(){
    var xmlhttp;
    if(window.XMLHttpRequest){
        xmlhttp=new XMLHttpRequest();
    }else if(window.ActiveXObejct){
        try{
            xmlhttp=new ActiveXObject("Msxm12.XMLHTTP");
        }catch(e){
            try{
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }catch(e){}
        }
    }
    return xmlhttp;
}
function check(){
    var xhr=ajaxFunction();
    xhr.open("GET","/",true);
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xhr.send(null);
    xhr.onreadystatechange=callback;
    function callback(){
        if(xhr.readyState==4){
            if(xhr.status==200||xhr.status==304){
                var data=xhr.responseText;
            }
        }
    }
}
//$(document).ready(function() {
//        $("#user").blur(function(){
//            $()
//        });
//});

