/**
 * Created by gao on 2016/8/27.
 */
var xmlHttp=ajaxFunction();
function ajaxFunction(){
    var xmlHttp;
    try {
        xmlHttp = new XMLHttpRequest();
    }catch(e){
        try{
            xmlHttp=new ActiveXObject("Msxml12.XMlHTTP");
        }catch(e){
            try{
                xmlHttp=new ActiveXObject("Mocrosoft.XMLHTTP");
            }catch(e){
                alert("您的浏览器不支持ajax");
                throw e;
            }
        }
    }
}

//打开与服务器的链接
xmlHttp.open(get,"/",true);

//f发送请求
xmlHttp.senRequestHeader("Context-Type","application/x-www-form-urlencode");
xmlHttp.send(null);//get请求可以为空，post为请求参数
//注册参数
xmlHttp.onreadystateChange=function(){
    if(xmlHttp.state==4&&(xmlHttp.status==200||xmlHttp.status==304)){
        var data=xmlHttp.responseText;
    }
}

