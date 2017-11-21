// JavaScript Document
function selectSeller(obj){
    var url = window.location.href;//http://localhost:8080/search.jsp?key=%E9%9E%8B%E5%AD%90&page=2
//    alert(obj.className);

    if($(obj).hasClass("selected")){
        if($(obj).attr("id")=="b2c"){
            url = url.replace("&getFromB2C=1","");
            if(url.contains("&getFromJd=1")) url = url.replace("&getFromJd=1","");
            if(url.contains("&getFromAz=1")) url = url.replace("&getFromAz=1","");
        }
        if($(obj).attr("id")=="jd")
            url = url.replace("&getFromJd=1","");
        if($(obj).attr("id")=="az")
            url = url.replace("&getFromAz=1","");
        if($(obj).attr("id")=="tb")
            url = url.replace("&getFromTb=1","");
    }else {
        if($(obj).attr("id")=="b2c")
            url += "&getFromB2C=1";
        if($(obj).attr("id")=="jd")
            url += "&getFromJd=1";
        if($(obj).attr("id")=="az")
            url += "&getFromAz=1";
        if($(obj).attr("id")=="tb")
            url += "&getFromTb=1";
    }
//    alert(url);
    window.location.href = url;
}

function sortByPrice(){
    var url =window.location.href;
    var icon = $("#sort");
    if(icon.hasClass("not-clicked-price-icon"))
        url+="&sortByPrice=1";
    else url=url.replace("&sortByPrice=1","");
    window.location.href = url;
}