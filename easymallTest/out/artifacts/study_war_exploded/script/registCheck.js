/**
 * Created by tarena on 2016/8/28.
 */
function checkForm(){
    var flag=true;
    flag=checkUserName()&&flag;
    flag=checkNull("password","密码不能为空")&&flag;
    flag=checkpassword("password","确认密码与密码要一致")&&flag;
    flag=checkNull("nickname","昵称不能为空")&&flag;
    flag=checkEmail("email","邮箱格式不正确")&&flag;
    flag=checkNull("valistr","验证码不能为空")&&flag;
    return flag;
}
//校验是否为空
function checkNull(name,msg){
    var temp=document.getElementsByName(name)[0].value;
    setMsg(name,"");
    if(temp==""){
        setMsg(name,msg);
        return false;
    }
    return true;
}
//邮箱格式校验
function checkEmail(name,msg){
    var email=document.getElementsByName(name)[0].value;
    setMsg(name,"");
    if(email==""){
        setMsg(name,"邮箱不能为空");
        return false;
    }
    var reg=/^\w+@\w+(\.\w+)+$/;
    if(!reg.test(email)){
        setMsg(name,msg);
        return false;
    }
    return true;
}
//校验两次密码是否一致
function checkpassword(name,msg){
    var psw1=document.getElementsByName(name)[0].value;
    var psw2=document.getElementsByName(name+"2")[0].value;
    setMsg(name+"2","");
    if(psw2==""){
        setMsg(name+"2","确认密码不能为空");
        return false;
    }
    if(psw1==""||psw2==""||psw1!=psw2){
        setMsg(name+"2",msg);
        return false;
    }
    return true;
}
function setMsg(name,msg){
    document.getElementById(name+"_msg").innerHTML=msg;
}
//检查用户名是否已经存在
//1. 创建XMLHttpResuqst对象
function ajaxFunction(){
    var xmlHttp;
    if(window.XMLHttpRequest){
        xmlHttp=new XMLHttpRequest();//ie6以上
    }else if(window.ActiveXObject){
        try{
            xmlHttp=new ActiveXObject("Msxm12.XMLHTTP");//ie6
        }catch (e){
            try{
                xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");//ie5
            }catch(e){}
        }
    }
    return xmlHttp;
}
/*function checkUserName(){
    var flag=checkNull("username","用户名不能为空");
    var username=document.getElementsByName("username")[0].value;
    var xhr=ajaxFunction();
    //建立连接
    xhr.open("POST","http://www.easymall.com/checkUserNameServlet",true);
    //发送请求
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xhr.send("username="+username);
    //xhr.setRequestHeader("Content-Type","application/json");
    //xhr.send(JSON.stringify({
    //    username:username
    //}));
    //处理结果
  /!*  xhr.onload=function(){//支持level2
        if(xhr.status==200){}
    }*!/
    if(flag){
        xhr.onreadystatechange=function(e){
            if(xhr.readyState==4){
                if(xhr.status==200||xhr.status==304){
                    var data=xhr.responseText;
                    if(data=="true"){
                        alert(data);
                        setMsg("username","用户名已经存在");
                        flag=false;
                    }
                }
            }
        }
    }
}*/
