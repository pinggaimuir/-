/**
 * Created by tarena on 2016/8/22.
 */
/*获取表单后的span标签 显示提示信息*/
function gspan(cobj){
    if(cobj.nextSibling.nodeName!='SPAN'){
        gspan(cobj.nextSilibing());
    }else{
        return cobj.nextSibling;
    }
}
/*检查表单obj(表单对象)，info（提示信息）fun（处理函数）click（是否需要单击，提交时需要触发）*/
function check(obj,info,fun,click){
    var sp=gspan(obj);
    obj.onfocus=function(){
        sp.innerHTML=info;
    }
    obj.onblur=function(){
        if(fun(this.value)){
            sp.innerHTML="输入正确！";
        }else{
            sp.innerHTML=info;
        }
    }
    if(click=='ckick'){
        obj.onblur();
    }
}
function $(name){
    return document.getElementsByName(name)[0];
}
/*加载页面时*/
onload=regs;
function regs(click){
    //返回状态
    var stat=true;
    username=$("username");
    password=$("password");
    chkpass=$("chkpass");
    password=$("password");
}