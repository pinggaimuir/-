/**
 * Created by tarena on 2016/8/18.
 */
//第一套方案
function append(){
    //获得name和price的值
    var name=document.getElementsByTagName("input")[0].value;
    var price=document.getElementsByTagName("input")[1].value;

    //第一列
    var tdNode1=document.createElement("td");
    tdNode1.innerHTML=name;
    //第二列
    var tdNode2=document.createElement("td");
    tdNode2.innerHTML=price;
    //第三列
    var tdNode3=document.createElement("td");
    var btnNode=document.createElement("input");
    btnNode.type="button";
    btnNode.value="删除";
    btnNode.onclick=function(){
        var what=confirm("确定要删除么");
        if(what){
            var tr=btnNode.parentNode.parentNode
            btnNode.parentNode.parentNode.parentNode.removeChild(tr);
        }
    };
    tdNode3.appendChild(btnNode);
    //创建新的表格项
    var trNode=document.createElement("tr");
    //为tr添加三个td
    trNode.appendChild(tdNode1);
    trNode.appendChild(tdNode2);
    trNode.appendChild(tdNode3);
    document.getElementsByTagName("table")[0].appendChild(trNode);
}

/*第二套方案*/
function addRow(){
    //获得表格对象
    var Tab=document.getElementsByTagName("table")[0];
    //创建新行
    var trObj=Tab.insertRow(Tab.rows.length);
    //为行添加name单元格
    var nameObj=trObj.insertCell(0);
    nameObj.innerHTML=document.getElementById("name").value;
    //为行添加price单元格
    var priceObj=trObj.insertCell(1);
    priceObj.innerHTML=document.getElementById("price").value;
    //为行添加删除单元格
    var deleteObj=trObj.insertCell(2);
    var btnObj=document.createElement("input");
    btnObj.type="button";
    btnObj.value="删除";
    btnObj.onclick=function(){
      deleteFun(this);
    };
    deleteObj.appendChild(btnObj);
}
function deleteFun(Obj){
    var reObj=Obj.parentNode.parentNode;
    var flag=confirm("确定要删除么？");
    if(flag){
        reObj.parentNode.removeChild(reObj);
    }
}


















