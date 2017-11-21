package com.seckill.service;

import com.seckill.common.Exposer;
import com.seckill.common.RedisDao;
import com.seckill.common.SeckillExecution;
import com.seckill.enums.SecKillStatEnum;

import com.seckill.mapper.SeckillMapper;
import com.seckill.mapper.SuccessKilledMapper;
import com.seckill.pojo.Seckill;
import com.seckill.pojo.SuccessKilled;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gao on 2016/11/21.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private  final Logger logger= LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Resource
    private RedisDao redisDao;

    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private SuccessKilledMapper successKilledMapper;
    //盐
    private final String salt="fdsjlfvvvvvvvvvvdfnvsdfnvrewugvhndmz452343667*%cvns";

    public List<Seckill> getSeckillList() {
        return seckillMapper.queryAll();
    }

    public Seckill getSeckill(Long seckillId) {
        return seckillMapper.queryById(seckillId);
    }

    /**
     * 暴露秒杀地址
     * @param seckillId
     * @return
     */
    public Exposer exportSeckillUrl(Long seckillId) {
        //从redis中获取
        Seckill seckill=redisDao.getSeckill(seckillId);
        if(seckill==null) {
            //查询数据库
            seckill = seckillMapper.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }else{
                //放入redis
                redisDao.setSeckill(seckill);
            }
        }
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        //当前系统时间
        Date nowTime=new Date();
        //如果当前时间小于秒杀开始时间或大于秒杀关闭时间，则秒杀未开启，返回当前时间和开启时间
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),
                    endTime.getTime());
        }
        //MD5混淆，防止url造假
        String md5= DigestUtils.md5DigestAsHex((seckillId+"/"+salt).getBytes());
        return new Exposer(true,md5,seckillId);
    }


    /**
     * 调用存储过程来执行秒杀
     */
    @Override
    public SeckillExecution executeSeckillProcedure(Long seckillId, Long userPhone, String md5) {
        String newMd5=DigestUtils.md5DigestAsHex((seckillId+"/"+salt).getBytes());
        if(md5==null||!newMd5.equals(md5)){
            return new SeckillExecution(seckillId,SecKillStatEnum.DATA_REWRITE);
        }
        Date killTime=new Date();
        Map map=new HashMap<String,Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try{
            //执行存储过程，map被赋值
            seckillMapper.killByProcedure(map);
            //获取map中的result
            int result=MapUtils.getInteger(map,"result",-2);
            //返回1代表执行成功
            if(result==1){
                SuccessKilled sk=successKilledMapper.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId, SecKillStatEnum.SUCCESS,sk);
            }else{
                return new SeckillExecution(seckillId,SecKillStatEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SecKillStatEnum.INNER_ERROR);
        }
    }
}
