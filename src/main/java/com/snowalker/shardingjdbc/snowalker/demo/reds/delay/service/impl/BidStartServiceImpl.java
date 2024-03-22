package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.impl;

import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.annotations.ConsumerType;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Method_Name : 消费者类，对ReadyQueue中的消息进行消费
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
@Slf4j
@ConsumerType(value = ConsumerTypeEnum.BIDSTART)
public class BidStartServiceImpl implements ConsumerService {



    /**
     * 竞价开始消费方法
     *
     * @param prjNo 消息内容
     * @return 执行结果
     */
    @Override
    public Boolean consumerMessage( String prjNo) {
        log.info("redis延时消费 修改竞价单为竞价中开始 ---->");
        try {
            return true;
        } catch (Exception e) {
            log.error("redis延时消费 修改竞价单为竞价中异常-->" , e);
        }
        return Boolean.FALSE;
    }

}
