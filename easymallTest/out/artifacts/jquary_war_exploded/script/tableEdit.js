/**
 * Created by tarena on 2016/9/25.
 */
//加载页面时 让所有的td有变成可点击事件
$(document).ready(function(){
    $("td").dblclick(editTable);
});
function editTable(){
    td=$(this);
    //1.取出当前td中的内容保存起来
    var text=td.text();
    //2.清空td里面的内容
    td.empty();
    //3.建立一个文本框，也就是一个input元素节点
    var input=$("<input>");
    //4.设置input文本框里的值是保存起来文本内容
    input.attr("value",text);
    //5.为input添加键盘事件
    input.keyup(function(event){
        var e=event||window.event;
       if(e.keyCode==13){
           //获得文本框中的属性
           var inputstr=input.val();
           var tdnode=input.parent();
           //删除并替换td中的input为输入的文本
           tdnode.html(inputstr);
           //为tdnode重新添加点击事件
           tdnode.dblclick(editTable);
       }
    });
    //5.将文本加入到td当中
    td.append(input);//或者input.appendTo(td);
    //将jquery对象转化成dom对象
    var inputdom=input.get(0);
    //将input中的文本高亮全选
    inputdom.select();
    //6.清除掉td上的点击时间
    td.unbind("dblclick");

}