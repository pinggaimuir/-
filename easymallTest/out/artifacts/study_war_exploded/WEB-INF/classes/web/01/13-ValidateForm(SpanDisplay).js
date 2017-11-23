/*
	描述信息处理
*/
function mouseIn(){
	var desc = document.getElementById("desc");
	if(desc.value == "请输入描述信息~！"){
		desc.value="";
	}
}
/*
	描述信息处理
*/
function mouseOut(){
	var desc = document.getElementById("desc");
	if(desc.value == ""){
		desc.value="请输入描述信息~！";
	}
	checkDescription("描述信息不能为空");
}

/*
	表单提交时的数据校验
*/
function checkData(){
	var canSub = true;
	
	canSub = checkNull("username","用户名不能为空！") && canSub;
	canSub = checkNull("password","密码不能为空！") && canSub;
	canSub = checkPassword2() && canSub;
	canSub = checkGender("性别不能为空!") && canSub;
	canSub = checkNull("nickname","昵称不能为空！") && canSub;
	canSub = checkEmail();
	canSub = checkLike("爱好不能为空！") && canSub;
	canSub = checkNull("img","头像不能为空！") && canSub;
	canSub = checkNull("valistr","验证码不能为空！") && canSub;
	canSub = checkDescription("描述信息不能为空！") && canSub;
	
	return canSub;
}


/*
	数据校验——通用非空校验
*/
function checkNull(name,msg){
	setMsg(name,"");
	var inp = document.getElementsByName(name)[0];
	if(inp.value ==null || inp.value == ""){
		setMsg(name,msg);
		return false;
	}
	return true;
}

/*
	数据校验——确认密码校验
*/
function checkPassword2(){
	checkNull("password2","确认密码不能为空！");
	var password = document.getElementsByName("password")[0].value;
	var password2 = document.getElementsByName("password2")[0].value;
	if(password !='' && password2!='' && password!=password2){
		setMsg("password2","")
		setMsg("password2","两次密码不一致");
		return false;
	}
	return true;
}

/*
	数据校验——性别非空校验
*/
function checkGender(msg){
	setMsg("gender","");
	var genders = document.getElementsByName("gender");
	var gender_flag = false;
	for(var i = 0;i<genders.length;i++){
		if(genders[i].checked == true){
			gender_flag = true;
		}
	}
	if(gender_flag == false){
		setMsg("gender",msg);
	}
	return gender_flag;
}

/*
	数据校验——爱好非空校验
*/
function checkLike(msg){
	setMsg("like","");
	var likes = document.getElementsByName("like");
	var like_flag = false;
	for(var i = 0;i<likes.length;i++){
		if(likes[i].checked == true){
			like_flag = true;
		}
	}
	if(like_flag == false){
		setMsg("like",msg);
		return false;
	}
	return true;
	
}
/*
	数据校验——描述信息非空校验
*/
function checkDescription(msg){
	setMsg("desc","");
	var desc = document.getElementsByName("desc")[0];
	if(desc.value == "请输入描述信息~！"){
		setMsg("desc",msg);
		return false;
	}
	return true;
}

/*
校验数据——校验邮箱
xxxxx@xxxx.xx.xx.xx
^\w+@\w+(\.\w+)+$

在js中正则表达式也是一个对象
定义一个正则表达式对象
	var reg = /^\w+@\w+(\.\w+)+$/;
	var reg = new RegExp("^\\w+@\\w+(\\.\\w+)+$");
*/
function checkEmail(){
checkNull("email","邮箱不能为空！");
var email = document.getElementsByName("email")[0];
if(email.value!=""){
	setMsg("email","");
	if(!/^\w+@\w+(\.\w+)+$/.test(email.value)){
		setMsg("email","邮箱格式不正确");
		return false;
	}
}
return true;
}


function setMsg(name,msg){
	document.getElementById(name+"_msg").innerHTML="<font color='red' size='-1'>"+msg+"</font>";
}