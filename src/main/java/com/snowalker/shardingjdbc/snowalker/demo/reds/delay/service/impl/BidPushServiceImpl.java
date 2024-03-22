package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.impl;

import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.annotations.ConsumerType;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.ConsumerService;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.RedisDelayQueueService;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.Task;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @Method_Name : 消费者类，对ReadyQueue中的消息进行消费
 * @Description : * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
@Slf4j
@ConsumerType(value = ConsumerTypeEnum.BIDPUSH)
public class BidPushServiceImpl implements ConsumerService {

	@Resource
	private RedisDelayQueueService redisDelayQueueService;

	/**
	 * 竞价待发布消费方法
	 *
	 * @param prjNo 消息内容
	 * @return 执行结果
	 */
	@Override
	public Boolean consumerMessage(String prjNo) {
		log.info("修改竞价单为待发布开始 ---->");
		try {

			if (true) {

			} else if (true) {
				log.info("修改竞价单为待发布开始 ---->待发布竞价单【{}】" , prjNo);
				List<Task> tasks =new ArrayList<>();
				redisDelayQueueService.addTasks(tasks);
			}
			return true;
		} catch (Exception e) {
			log.error("修改竞价单为待发布异常-->" , e);
		}
		return Boolean.FALSE;
	}

}
