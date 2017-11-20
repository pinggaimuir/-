/**
 * Created by tarena on 2016/8/25.
 */
//获取表单后的span标签 显示提示内容
//function gspan(cogj){
//    if(cogj.nextSibling.nodeName!="SPAN"){
//        gspan(cogj.nextSibling);
//    }else{
//        return  cogj.nextSibling;
//    }
//}
//检查表单 obj表单对象 info提示信息 fun处理函数 click是否需要提交表单，提交时验证/

function check(obj,info,fun,click) {
    //获得焦点时提示
    // /获得表单后的span元素
    var sp = obj.parentNode.getElementsByTagName("span")[0];
    obj.onfocus = function () {
        sp.innerHTML = info;
    }
    //失去焦点时提示
    obj.onblur = function () {
        if (fun(obj.value)) {
            sp.innerHTML = "输入正确！";
        } else {
            sp.innerHTML = info;
        }
    }

    //需要点击
    if (click == "click") {
        obj.onblur();
    }
}
//检验性别
function checkGender(){
    var genders = document.getElementsByName("gender");
    var sp=genders[0].parentNode.getElementsByTagName("span")[0];
    sp.innerHTML="";
    var gender_flag = false;
    for(var i = 0;i<genders.length;i++){
        if(genders[i].checked == true){
            gender_flag = true;
        }
    }
    if(gender_flag == false){
        sp.innerHTML="请选择性别！";
    }
    return gender_flag;
}

//页面加载完毕执行
onload=regs;
function $(id){
    return document.getElementsByName(id)[0];
}
function regs(click){
    var stat=true;//返回状态
    username=$("username");
    password=$("password");
    chkpass=$("chkpass");
    email=$("email");
    gender=$("gender");
    nickname=$("nickname");
    like=$("like");
    type=$("type");
    img=$("img");
    desc=$("img");
    valistr=$("valistr");


    check(username,"用户名的长度在3-20之间",function(val){
        if(val.match(/^\S+$/)&&val.length>=2&&val.length<=20){
            return true;
        }else{
            stat=false;
            return false;
        }
    },click);

    check(password,"密码必须为5-20位之间,且以字母开头",function(val){
        if(val.match(/^[a-zA-Z]+\w+$/)&&val.length>=5&&val.length<=20){
            return true;
        }else{
            stat=false;
            return false;
        }
    },click);

    check(chkpass,"确认密码根密码一致",function(val){
        if(val.match(/^[a-zA-Z]+\w+$/)&&val.length>=5&&val==password.value){
            return true;
        }else{
            stat=false;
            return false;
        }
    },click);

    check(email,"请按邮箱规则输入",function(val){
        if(val.match(/^\w+@\w+(\.\w+)+$/)){
            return true;
        }else{
            stat=false;
            return false;
        }
    },click);

    stat=checkGender();



    return stat;
}