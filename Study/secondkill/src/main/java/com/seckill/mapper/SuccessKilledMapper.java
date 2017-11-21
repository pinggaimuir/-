package com.seckill.mapper;

import com.seckill.pojo.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by gao on 2016/11/20.
 */
public interface SuccessKilledMapper {

    /**
     * 根据Id查询SuccessKilled,包含秒杀对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
