/**
 * Created by gao on 2017/1/8.
 */
function ar_encode(str){
    return encodeURI(str);
}
/*屏幕分辨率*/
function ar_get_screen(){
    var c="";
    if(self.screen){
        c=screen.width+"x"+screen.height;
    }
    return c;
}
/*颜色质量*/
function ar_get_color(){
    var c="";
    if(self.screen){
        c=screen.colorDepth+"-bit";
    }
    return c;
}
/**返回当前的浏览器语言*/
function ar_get_language()
{
    var l = "";
    var n = navigator;

    if (n.language) {
        l = n.language.toLowerCase();
    }
    else
    if (n.browserLanguage) {
        l = n.browserLanguage.toLowerCase();
    }

    return l;
}
/*返回浏览器类型*/
function ar_get_agent(){
    var a="";
    var n=navigator;
    if(n.userAgent){
        a= n.userAgent;
    }
    return a;
}
/**方法可返回一个布尔值，该值指示浏览器是否支持并启用了Java*/
function ar_get_jvm_enabled()
{
    var j = "";
    var n = navigator;

    j = n.javaEnabled() ? 1 : 0;

    return j;
}

/**返回浏览器是否支持(启用)cookie */
function ar_get_cookie_enabled()
{
    var c = "";
    var n = navigator;
    c = n.cookieEnabled ? 1 : 0;

    return c;
}

/**检测浏览器是否支持Flash或有Flash插件*/
function ar_get_flash_ver()
{
    var f="",n=navigator;

    if (n.plugins && n.plugins.length) {
        for (var ii=0;ii<n.plugins.length;ii++) {
            if (n.plugins[ii].name.indexOf('Shockwave Flash')!=-1) {
                f=n.plugins[ii].description.split('Shockwave Flash ')[1];
                break;
            }
        }
    }
    else
    if (window.ActiveXObject) {
        for (var ii=10;ii>=2;ii--) {
            try {
                var fl=eval("new ActiveXObject('ShockwaveFlash.ShockwaveFlash."+ii+"');");
                if (fl) {
                    f=ii + '.0';
                    break;
                }
            }
            catch(e) {}
        }
    }
    return f;
}


/**匹配顶级域名*/
function ar_c_ctry_top_domain(str)
{
    var pattern = "/^aero$|^cat$|^coop$|^int$|^museum$|^pro$|^travel$|^xxx$|^com$|^net$|^gov$|^org$|^mil$|^edu$|^biz$|^info$|^name$|^ac$|^mil$|^co$|^ed$|^gv$|^nt$|^bj$|^hz$|^sh$|^tj$|^cq$|^he$|^nm$|^ln$|^jl$|^hl$|^js$|^zj$|^ah$|^hb$|^hn$|^gd$|^gx$|^hi$|^sc$|^gz$|^yn$|^xz$|^sn$|^gs$|^qh$|^nx$|^xj$|^tw$|^hk$|^mo$|^fj$|^ha$|^jx$|^sd$|^sx$/i";

    if(str.match(pattern)){ return 1; }

    return 0;
}
/**处理域名地址*/
function ar_get_domain(host)
{
    //如果存在则截去域名开头的 "www."
    var d=host.replace(/^www\./, "");

    //剩余部分按照"."进行split操作，获取长度
    var ss=d.split(".");
    var l=ss.length;

    //如果长度为3，则为xxx.yyy.zz格式
    if(l == 3){
        //如果yyy为顶级域名，zz为次级域名，保留所有
        if(ar_c_ctry_top_domain(ss[1]) && ar_c_ctry_domain(ss[2])){
        }
        //否则只保留后两节
        else{
            d = ss[1]+"."+ss[2];
        }
    }
    //如果长度大于3
    else if(l >= 3){

        //如果host本身是个ip地址，则直接返回该ip地址为完整域名
        var ip_pat = "^[0-9]*\.[0-9]*\.[0-9]*\.[0-9]*$";
        if(host.match(ip_pat)){
            return d;
        }
        //如果host后两节为顶级域名及次级域名，则保留后三节
        if(ar_c_ctry_top_domain(ss[l-2]) && ar_c_ctry_domain(ss[l-1])) {
            d = ss[l-3]+"."+ss[l-2]+"."+ss[l-1];
        }
        //否则保留后两节
        else{
            d = ss[l-2]+"."+ss[l-1];
        }
    }

    return d;
}

/**返回cookie信息*/
function ar_get_cookie(name)
{
    var mn=name+"=";
    var b,e;
    var co=document.cookie;

    if (mn=="=") {
        return co;
    }

    b=co.indexOf(mn);

    if (b < 0) {
        return "";
    }

    e=co.indexOf(";", b+name.length);

    if (e < 0) {
        return co.substring(b+name.length + 1);
    }
    else {
        return co.substring(b+name.length + 1, e);
    }
}

/**设置cookie信息*/
function ar_set_cookie(name, val, cotp)
{
    var date=new Date;
    var year=date.getFullYear();
    var hour=date.getHours();

    var cookie="";

    if (cotp == 0) {
        cookie=name+"="+val+";";
    }
    else if (cotp == 1) {
        year=year+10;
        date.setYear(year);
        cookie=name+"="+val+";expires="+date.toGMTString()+";";
    }
    else if (cotp == 2) {
        hour=hour+1;
        date.setHours(hour);
        cookie=name+"="+val+";expires="+date.toGMTString()+";";
    }

    var d=ar_get_domain(document.domain);
    if(d != ""){
        cookie +="domain="+d+";";
    }
    cookie +="path="+"/;";

    document.cookie=cookie;
}
/**返回客户端时间*/
function ar_get_stm()
{
    return new Date().getTime();
}

/**返回指定个数的随机数字串*/
function ar_get_random(n) {
    var str = "";
    for (var i = 0; i < n; i ++) {
        str += String(parseInt(Math.random() * 10));
    }
    return str;
}
/* main function */
function ar_main() {

    var dest_path   = "http://127.0.0.1:8080/servlet/LogServlet?";
    var expire_time = 30 * 60 * 1000;//会话超时时长

    //处理uv
    //--获取cookie ar_stat_uv的值
    var uv_str = ar_get_cookie("ar_stat_uv");
    var uv_id = "";
    //--如果cookie ar_stat_uv的值为空
    if (uv_str == ""){
        //--为这个新uv配置id，为一个长度20的随机数字
        uv_id = ar_get_random(20);
        //--设置cookie ar_stat_uv 保存时间为10年
        ar_set_cookie("ar_stat_uv", uv_id, 1);
    }
    //--如果cookie ar_stat_uv的值不为空
    else{
        //--获取uv_id
        uv_id  = uv_str;
    }

    //处理ss
    //--获取cookie ar_stat_ss
    var ss_str = ar_get_cookie("ar_stat_ss");
    var ss_id = "";  //sessin id
    var ss_no = 0;   //session有效期内访问页面的次数

    //--如果cookie中不存在ar_stat_ss 说明是一次新的会话
    if (ss_str == ""){
        //--随机生成长度为10的session id
        ss_id = ar_get_random(10);
        //--session有效期内页面访问次数为0
        ss_no = 0;
        //--拼接cookie ar_stat_ss 值 格式为 会话编号_会话期内访问次数_客户端时间_网站id
        value = ss_id+"_"+ss_no+"_"+ar_get_stm();
        //--设置cookie ar_stat_ss
        ar_set_cookie("ar_stat_ss", value, 0);
    }
    //--如果cookie中存在ar_stat_ss
    else {
        //获取ss相关信息
        var items = ss_str.split("_");
        //--ss_id
        var cookie_ss_id  = items[0];
        //--ss_no
        var cookie_ss_no  = parseInt(items[1]);
        //--ss_stm
        var cookie_ss_stm = items[2];

        //如果当前时间-当前会话上一次访问页面的时间>30分钟,虽然cookie还存在，但是其实已经超时了！仍然需要重新生成cookie
        if (ar_get_stm() - cookie_ss_stm > expire_time) {
            //--重新生成会话id
            ss_id = ar_get_random(10);
            //--设置会话中的页面访问次数为0
            ss_no = 0;
        }
        //--如果会话没有超时
        else{
            //--会话id不变
            ss_id = cookie_ss_id;
            //--设置会话中的页面方位次数+1
            ss_no = cookie_ss_no + 1;
        }

        //--重新拼接cookie ar_stat_ss的值
        value = ss_id+"_"+ss_no+"_"+ar_get_stm();
        ar_set_cookie("ar_stat_ss", value, 0);
    }


    //返回导航到当前网页的超链接所在网页的URL
    var ref = document.referrer;
    ref = ar_encode(String(ref));

    //当前地址
    var url = document.URL;
    url = ar_encode(String(url));

    //当前资源名
    var urlname = document.URL.substring(document.URL.lastIndexOf("/")+1);
    urlname = ar_encode(String(urlname));

    //网页标题
    var title = document.title;
    title = ar_encode(String(title));

    //网页字符集
    var charset = document.charset;
    charset = ar_encode(String(charset));

    //屏幕信息
    var screen = ar_get_screen();
    screen = ar_encode(String(screen));

    //颜色信息
    var color =ar_get_color();
    color =ar_encode(String(color));

    //语言信息
    var language = ar_get_language();
    language = ar_encode(String(language));

    //浏览器类型
    var agent =ar_get_agent();
    agent =ar_encode(String(agent));

    //浏览器是否支持并启用了java
    var jvm_enabled =ar_get_jvm_enabled();
    jvm_enabled =ar_encode(String(jvm_enabled));

    //浏览器是否支持并启用了cookie
    var cookie_enabled =ar_get_cookie_enabled();
    cookie_enabled =ar_encode(String(cookie_enabled));

    //浏览器flash版本
    var flash_ver = ar_get_flash_ver();
    flash_ver = ar_encode(String(flash_ver));

    //当前uv状态 格式为"会话id_会话次数_当前时间"
    var stat_ss = ss_id+"_"+ss_no+"_"+ar_get_stm();

    //拼接访问地址 增加如上信息
    dest=dest_path+"url="+url+"&urlname="+urlname+"&title="+title+"&chset="+charset+"&scr="+screen+"&col="+color+"&lg="+language+"&je="+jvm_enabled+"&ce="+cookie_enabled+"&fv="+flash_ver+"&cnv="+String(Math.random())+"&ref="+ref+"&uagent="+agent+"&stat_uv="+uv_id+"&stat_ss="+stat_ss;
    //通过插入图片访问该地址
    document.getElementsByTagName("body")[0].innerHTML += "<img src=\""+dest+"\" border=\"0\" width=\"1\" height=\"1\" />";

}

window.onload = function(){
    //触发main方法
    ar_main();
}