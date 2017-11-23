/**
 * Created by gao on 2017/3/11.
 */
var legal={
    //请求URL
    URL:{
        lawinfolist:function(keyWord,page){
            return "/lawinfo/"+keyWord+"/"+page+"/search.html";
        }
    },
    //点击搜索
    search:function(){
      $("#searchBtn").click(function(){
          var keyword=$("#keyword").val();
          window.location.href=legal.URL.lawinfolist(keyword,1);
      });
    },
    //上一页
    prepage:function(){
        $("#prepage").click(function(){
            var curpage=parseInt($("#page").val());
            var keyword=$("#keyword").val();
            if(curpage!=1) {
                window.location.href=legal.URL.lawinfolist(keyword,curpage-1);
            }
        });
    },
    //下一页
    nextpage:function(){
        $("#nextpage").click(function() {
            var curpage = parseInt($("#page").val());
            var total =parseInt($("#total").val());
            var keyword=$("#keyword").val();
            if (curpage != total) {
                window.location.href=legal.URL.lawinfolist(keyword,curpage+1);
            }
        });
    }
}
