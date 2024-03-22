package com.snowalker.shardingjdbc.snowalker.demo.reds.delay;

/**
 * @Method_Name : 回调的任务接口
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
public interface IFutureTask<T> {

    /**
     * 执行任务
     *
     * @return 任务返回值
     */
    T doTask();
}
