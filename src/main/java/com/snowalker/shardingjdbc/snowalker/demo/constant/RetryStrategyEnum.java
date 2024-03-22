package com.snowalker.shardingjdbc.snowalker.demo.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Method_Name : 重试策略枚举
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/12
 * @Author : fangwenhui
 */
@Getter
public enum RetryStrategyEnum {
    RETRY_ONE(1,5),
    RETRY_TWO(2,10),
    RETRY_THREE(3,60),
    RETRY_FOUR(4,300),
    RETRY_FIVE(5,1800);

    /**
     * 重试次数
     */
    private int retry;

    /**
     * 延时
     */
    private int delay;


    RetryStrategyEnum(Integer retry, Integer delay) {
        this.retry = retry;
        this.delay = delay;
    }

    /**
     * 根据重试次数
     * @param retry 次数
     * @return 延时
     */
    public static int getDelayTime(int retry){
        return Arrays.stream(values()).filter(e->e.getRetry() == retry).map(RetryStrategyEnum::getDelay).findFirst().orElse(RETRY_FIVE.getDelay());
    }
}
