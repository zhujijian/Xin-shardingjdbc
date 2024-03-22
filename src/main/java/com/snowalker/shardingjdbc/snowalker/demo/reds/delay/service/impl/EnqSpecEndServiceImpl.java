package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.impl;

import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.annotations.ConsumerType;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Method_Name : 消费者类，对ReadyQueue中的消息进行消费
 * @Description : * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
@Slf4j
@ConsumerType(value = ConsumerTypeEnum.BIDSPECEND)
public class EnqSpecEndServiceImpl implements ConsumerService {




	/**
	 * 竞价专场结束消费方法
	 *
	 * @param id 消息内容
	 * @return 执行结果
	 */
	@Override
	public Boolean consumerMessage(String id) {
		log.info("发布竞价专场结束消息开始 ---->");
		try {

			log.info("发布竞价专场结束消息结束 ---->");
			return true;
		} catch (Exception e) {
			log.error("发布竞价专场结束消息异常-->" , e);
		}
		return Boolean.FALSE;
	}

}
