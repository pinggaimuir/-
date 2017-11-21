var loc3 = window.location.origin + "/tablemeta/savechecked?metaid=";
/*如果是执行则使用"/dbm/tablemeta/excute?id="
如果是审批则使用"/dbm/tablemeta/checked?id="
如果是跳过则使用"/dbm/tablemeta/status?status=7&id="
*/
// var rows = document.getElementsByClassName("xx");
// var tableID = new Array();
// for (var i=0 ; i<rows.length ; ++i){
//     tableID.push(rows[i].firstElementChild.innerHTML);
// }
// console.log(rows);
// console.log(tableID);
var result;
function SendAjax(id){
    var requesturl3 = loc3+id;
    var a13 = new XMLHttpRequest();
    a13.onreadystatechange=function(){
        if (a13.readyState==4 && a13.status==200){
            if (JSON.parse(a13.responseText).result != "success"){
                console.log(id+" : error!");
            }
        }

        if (a13.readyState==4 && a13.status!=200){
            alert("failed!"+id);
            console.log("failed!"+id);
        }
    }	
    a13.open("post",requesturl3,false);
    a13.send();
}
for (var i=126652 ; i<=126906 ; ++i){
    SendAjax(i);
    console.log(i);
}
alert("ok");

118001
