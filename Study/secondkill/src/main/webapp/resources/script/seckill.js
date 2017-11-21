/**
 * Created by gao on 2016/11/21.
 */
//交互逻辑代码
var seckill={
    //封装秒杀相关ajax的地址
    URL:{
        now:function(){
            return "/seckill/time/now";
        },
        exposer:function(seckillId){
            return "/seckill/"+seckillId+"/exposer";
        },
        execution:function(seckillId,md5){
            return "/seckill/"+seckillId+"/"+md5+"/execute";
        }
    },
    //处理秒杀逻辑
    handleSeckill:function(seckillId,node){
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function(data){
            //在回调函数中，执行交互函数
            if(data&&data['success']){
                var exposer=data['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    var md5=exposer['md5'];
                    var killUrl=seckill.URL.execution(seckillId,md5);
                    console.log("killUrl"+killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1 先禁用按钮
                        $(this).addClass('disabled');
                        //2 发送求情
                        $.post(killUrl,{},function(data){
                           if(data&&data['success']){
                               var killResult=data['data'];
                               var state=killResult['state'];
                               var stateInfo=killResult['stateInfo'];
                               node.html('<span calss="label label-success">'+stateInfo+'</span>');
                           }
                        });
                    });
                    node.show();
                }else{
                    //未开启秒杀
                    var now=exposer['now'];
                    var start=exposer['start'];
                    var end=exposer['end'];
                    seckill.countdown(seckillId,now,start,end);
                }
            }else{
                console.log("exposer:"+exposer);
            }
        })
    },
    //验证手机号
    validatePhone:function(phone){
        if(phone&&phone.length==11&&!isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },
    //计时器
    countdown:function(seckillId,nowTime,startTime,endTime){
        var seckillBox=$('#seckill-box');
        //判断时间
        if(nowTime>endTime){
            //秒杀结束
            seckillBox.html('秒杀结束!');
        }else if(nowTime<startTime){
            //秒杀未开启,计时时间绑定
            var killTime=new Date(startTime);
            console.log('killTime'+killTime);
            seckillBox.countdown(killTime,function(event){
                var format= event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //秒杀时间到达时回调事件
            }).on('finish.countdown', function () {
                //暴露秒杀地址，执行秒杀逻辑
                seckill.handleSeckill(seckillId,seckillBox);
            })
        }else{
            //秒杀开启
            seckill.handleSeckill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail:{
         //详情页初始化
         init:function(params){
            //用户手机验证操作,计时操作
             //从cookie中获取手机号
            var killPhone= $.cookie("killPhone");
             //验证手机号’
             if(!seckill.validatePhone(killPhone)) {
                 //绑定phone
                 var killPhoneModal = $('#killPhoneModal');
                 killPhoneModal.modal({
                     show: true,  //显示弹出层
                     backdrop: 'static', //禁止位置关闭
                     keyboard: false, //禁止键盘关闭
                 });
                 $('#killPhoneBtn').click(function () {
                     var inputPhone=$('#killPhoneKey').val();
                     if(seckill.validatePhone(inputPhone)){
                         //phone写入cookie
                         $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                         //刷新页面
                         window.location.reload();
                     }else{
                         $('#killPhoneMessage').html('<label class="label label-danger">手机号错误！</label>').show(300);
                     }
                 });
             }
             //已经登录
             var startTime=params['startTime'];
             var endTime=params['endTime'];
             var seckillId=params['seckillId'];
             //计时交互
             $.get(seckill.URL.now(),null,function(data){
                 if(data&&data['success']){
                     var nowTime=data['data'];
                     //计时器计时或暴露秒杀按钮
                     seckill.countdown(seckillId,nowTime,startTime,endTime);
                 }else{
                     console.log('result:'+data);
                 }
             });
         }

    }

}