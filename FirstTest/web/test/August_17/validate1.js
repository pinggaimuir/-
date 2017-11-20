/**
 * Created by tarena on 2016/8/17.
 */
/*验证账号的录入*/
function validateAccount(){
    //获取账号，并且复原文本框的样式
    var txtA=document.getElementById("txtAccount")
        txtA.className="txt";
    var account=document.getElementById("txtAcconut").value;
    //定义正则表达式进行验证
    var reg=/^\w{1,10}$/;
    var error=reg.test(account);
    //判断验证结果
    var spanObj=document.getElementById("accountInfo");
    if(error){
        spanObj.innerHTML="";
        spanObj.className="vali_success";
    }else{
        spanObj.innerHTML="10长度以内的字母、数字的组合";
        spanObj.className="vali_fail";
    }
    //返回验证结果
    return error;
}
/*验证电话号码的录入*/
function validatephone(){
    //获取电话号码，并且复原文本框的样式
    document.getElementById("txtPhone").className="txt";
    var phone=document.getElementById("txtPhone").value;
    //定义正则表达式进行验证
    var reg=/^\d{3}-\d{8}$/;
    var error=reg.test(phone);
    //判断验证结果
    var spanObj=document.getElementById("phoneInfo");
    if(error){
        spanObj.innerHTML="形如：010-12345678";
        spanObj.className="vali_fail";
    }
    //返回验证结果
    return error;
}
/*验证各项的录入*/
function validateDatas(){
    var r1=validateAccount();
    var r2=validatephone();
    //返回结果
    return r1&&r2;
}



