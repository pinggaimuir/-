package com.seckill.mapper;

import com.seckill.pojo.Seckill;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.ognl.ObjectElementsAccessor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gao on 2016/11/20.
 */
public interface SeckillMapper {

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @return
     */
    List<Seckill> queryAll();

    /**
     * 利用存储过程执行秒杀
     * @param params
     */
    void killByProcedure(Map<String,Object> params);
}
