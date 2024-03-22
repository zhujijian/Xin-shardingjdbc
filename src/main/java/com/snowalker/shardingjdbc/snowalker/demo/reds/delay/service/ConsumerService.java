package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service;


/**
 * @Method_Name : 消费者类，对ReadyQueue中的消息进行消费
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
public interface ConsumerService {

    /**
     * 消费ReadyQueue的消息
     * @param body 消息内容
     * @return 执行结果
     */
    Boolean consumerMessage(String body);
}
