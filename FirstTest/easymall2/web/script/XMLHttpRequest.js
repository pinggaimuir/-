/**
 * Created by tarena on 2016/9/7.
 */
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
