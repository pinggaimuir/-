/**
 * Created by tarena on 2016/8/25.
 */
//根据name获得元素
function $(id){
    return document.getElementsByName(id)[0];
}
function $2(obj){
    return obj.parentNode.getElementsByTagName("span")[0];
}
//表单验证提示
onload=function regs(){
    checkname();
}
function checkname(){
    var username=$("username");
    var sp=$2("username");
    username.onfocus=function(){
        sp.innerHTML="用户名为3-10位的字母加数字";
    };
    username.onblur=function(){
        if(username.value.match(/^\w+$/)&&username.value.length>=3&&username.value.length<=10){
            sp.innerHTML="";
        }else{
            sp.innerHTML="格式输入错误";
        }
    }
}
//表单提交验证方法


