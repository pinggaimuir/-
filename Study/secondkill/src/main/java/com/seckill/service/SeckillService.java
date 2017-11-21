package com.seckill.service;

import com.seckill.common.Exposer;
import com.seckill.common.SeckillExecution;
import com.seckill.pojo.Seckill;

import java.util.List;

/**
 * 秒杀业务接口
 * Created by gao on 2016/11/20.
 */
public interface SeckillService {
    /**
     * 查询秒杀商品列表
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询秒杀商品
     * @param seckillId
     * @return
     */
    Seckill getSeckill(Long seckillId);

    /**
     * 秒杀开启则暴露秒杀地址
     * 否则返回系统时间个秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(Long seckillId);

    /**
     * 通过存储过程执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProcedure(Long seckillId, Long userPhone, String md5);

}
