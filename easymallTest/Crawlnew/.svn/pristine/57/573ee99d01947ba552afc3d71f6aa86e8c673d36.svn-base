
var loc =  "http://dbm.ncpqh.com/tablemeta/excute?id=";
var loc2 =  "http://dbm.ncpqh.com/tablemeta/status?status=7&id=";
//var loc = window.location.origin + "/tablemeta/excute?id=";
//var loc2 = window.location.origin + "/tablemeta/status?status=7&id=";
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
    var requesturl = loc+id;  
    var a1 = new XMLHttpRequest();
    a1.onreadystatechange=function(){
        if (a1.readyState==4 && a1.status==200){
            if (JSON.parse(a1.responseText).result != "success"){
                console.log(id+" : error 1!");           
	            var requesturl2 = loc2+id;
	            var a12 = new XMLHttpRequest();
	            a12.onreadystatechange=function(){
	                if (a12.readyState==4 && a12.status==200){
	                    if (JSON.parse(a12.responseText).result != "success"){
	                        console.log(id+" : error! 2");
	                    }
	                }
	                if (a12.readyState==4 && a12.status!=200){
	                    alert("failed!"+id);
	                    console.log("failed!"+id);
	                }
	            };
	            a12.open("post",requesturl2,false);
	            a12.send();
            }
        }
        if (a1.readyState==4 && a1.status!=200){
            alert("failed!"+id);
            console.log("failed!"+id);
        }        
    };
    a1.open("post",requesturl,false);
    a1.send();
}
for (var i=126829 ; i<=126906 ; ++i){
    SendAjax(i);
}
alert("ok");




/*
var list= new Array();
list.push(
		100033
		,100054
		,100075
		,100096
		,100117
		,100138
		,100159
		,100258
		,100285
		,100317
		,100349
		,100381
		,100413
		,100445
		,100477
		,100509
		,100541
		,100573
		,100856
		,100888
		,100920
		,100952
		,100984
);
for( var j=0;j<list.length;j++ ){
	SendAjax(j);
}
alert("ok");
*/
