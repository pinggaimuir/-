package com.seckill.common;

import com.seckill.enums.SecKillStatEnum;
import com.seckill.pojo.SuccessKilled;
import org.springframework.stereotype.Component;

/**
 * 秒杀执行结果
 * Created by gao on 2016/11/20.
 */
public class SeckillExecution {
    //秒杀id
    private long seckillid;
    //秒杀返回状态
    private int state;
    //秒杀状态信息
    private String stateInfo;
    //秒杀成功实体
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillid, SecKillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillid = seckillid;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillid, SecKillStatEnum statEnum) {
        this.seckillid = seckillid;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillid() {
        return seckillid;
    }

    public void setSeckillid(long seckillid) {
        this.seckillid = seckillid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillid=" + seckillid +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
