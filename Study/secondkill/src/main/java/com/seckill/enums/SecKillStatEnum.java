package com.seckill.enums;

/**
 * Created by gao on 2016/11/21.
 */
public enum  SecKillStatEnum {
    SUCCESS(1,"秒杀成功！"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(3,"数据篡改");

    private int state;

    private String stateInfo;

    SecKillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 根据所给的state的状态返回对应的enum
     * @param index
     * @return
     */
    public static SecKillStatEnum stateOf(int index){
        for(SecKillStatEnum Enum:values()){
            if(Enum.getState()==index){
                return Enum;
            }
        }
        return null;
    }
}
