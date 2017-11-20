/**
 * Created by tarena on 2016/8/20.
 */
onload=function(){
    validate();
}
function $(id){
    return document.getElementsByName(id)[0];
}
var flag=false;
function validate() {
    var name = $("username");

        name.onblur =function(){flag=vali(name,"用户名不能为空");};
    var pwd1 = $("password1");
       pwd1.onblur =function(){flag=vali(pwd1,"密码不能为空");};
    var pwd2 = $("password2");
        pwd2.onblur =function(){flag=vali(pwd2,"确认密码不能为空");};
    var nick = $("nickname");
        nick.onblur =function(){flag=vali(nick,"昵称不能为空");};
    var email = $("email");
        email.onblur =function(){flag=vali(email,"邮箱不能为空");};
    var valistr = $("valistr");
    valistr.onblur =function(){flag=vali(valistr,"验证码不能为空");};
}
function regs(){
    return flag;
}
 function vali(Obj,str){
     var sp=Obj.parentNode.getElementsByTagName("span");
     if(Obj.value==0){
         Obj.style.backgroundColor="white";
         if(sp.length==0){
             var span=document.createElement("span");
             span.innerHTML=str;
             span.style.color="red";
             span.style.fontFamily="宋体";
             Obj.parentNode.appendChild(span);
             return false;
         }
     }else{
         Obj.style.backgroundColor="cyan";
         if(sp.length!=0){
            sp[0].parentNode.removeChild(sp[0]);
             return true;
         }
     }
}