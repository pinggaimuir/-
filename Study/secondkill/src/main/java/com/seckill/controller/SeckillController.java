package com.seckill.controller;

import com.seckill.common.Exposer;
import com.seckill.common.SeckillExecution;
import com.seckill.common.SeckillResult;
import com.seckill.enums.SecKillStatEnum;
import com.seckill.pojo.Seckill;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by gao on 2016/11/21.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    /**
     * 获取商品列表
     * @param model
     * @return 商品列表页
     */
    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String list(Model model){
        //获取商品列表
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    /**
     * 获取商品详情
     * @param seckillId  秒杀商品id
     * @param model
     * @return 商品详情页
     */
    @RequestMapping(value="/{seckillId}/detail",method= RequestMethod.GET)
    public String detail(@PathVariable("seckillId")Long seckillId,Model model){
        if(seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getSeckill(seckillId);
        //如果查询可空，重定向到list
        if(seckill==null){
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    /**
     *暴露秒杀信息
     * @param seckillId
     * @return
     */
    @RequestMapping(value="/{seckillId}/exposer",
                     method= RequestMethod.POST,
                     produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId){
        SeckillResult<Exposer> result;
        try{
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }

        return result;
    }

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param md5
     * @param phone
     * @return
     */
    @RequestMapping(value="/{seckillId}/{md5}/execute",
            method= RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value="killPhone",required=false)Long phone){
        //cookie中没有phone，表示用户未注册
        if(phone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        }catch (Exception e){
            //系统错误
            LOGGER.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId, SecKillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }

    }

    /**
     * 返回系统时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now=new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
