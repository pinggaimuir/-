/**
 * Created by gao on 2017/3/11.
 */
var shop={
    //请求URL
    URL:{
        searchUrl:function(keyWord,page){
            return "/shop/"+keyWord+"/skip.html";
        }
    },
    //点击搜索
    search:function(){
      $("#searchBtn").click(function(){
          var keyword=$("#keyword").val();
          window.location.href=shop.URL.searchUrl(keyword);
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
