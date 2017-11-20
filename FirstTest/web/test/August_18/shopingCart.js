/**
 * Created by tarena on 2016/8/18.
 */
/*增加数量*/
function increase(num){
   var input=document.getElementById("amount"+num);
    var amount=parseInt(input.value);
        input.value=++amount;
    //计算
    calculate();
}
/*减少数量*/
function decrease(btnObj){
   var nodes=btnObj.parentNode.childNodes;
    for(var i=0;i<nodes.length;i++){
        if(nodes[i].nodeName=="INPUT"&&nodes[i].type=="text"){
            var oldCount=parseInt(nodes[i].value);
            if(oldCount>=1){
                nodes[i].value=--oldCount;
            }
        }
    }
    //计算
    calculate();
}
function calculate(){
    //定义总计
    var total=0.00;
    //获得所有行
    var trNodes=document.getElementsByTagName("tr");
    //从第二行统计，不统计最后一行
    for(var i=1;i<trNodes.length-1;i++){
        var tr=trNodes[i];
        var price=tr.getElementsByTagName("td")[1].innerHTML;
        var quantity=tr.getElementsByTagName("input")[1].value;
        //计算小计
        var sum=parseFloat(parseFloat(price)*parseFloat(quantity));
        tr.getElementsByTagName("td")[3].innerHTML=sum.toFixed(2);
        //计算总计
        total+=sum;
    }
    var totalTD=trNodes[trNodes.length-1].getElementsByTagName("td")[1];
    totalTD.innerHTML=total.toFixed(2);
}