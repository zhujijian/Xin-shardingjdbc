package com.snowalker.shardingjdbc.snowalker.demo.reds.delay;


import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.util.DateUtil;
import com.snowalker.shardingjdbc.snowalker.demo.constant.RedisQueueKey;
import com.snowalker.shardingjdbc.snowalker.demo.constant.RetryStrategyEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.factory.ConsumerFactory;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.ConsumerService;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@ConditionalOnProperty(name = { "spring.application.name" }, havingValue = "fpl-provider-prj")
public class ReadyTaskContext {

	@Resource
	private RedissonClient redissonClient;

	@Value("${spring.profiles.active:dev'}")
	private String PROFILE;

	/**
	 * TOPIC消费线程
	 */
	@PostConstruct
	public void startTopicConsumer() throws InterruptedException {
		if("prod2".equals(PROFILE)||"prod3".equals(PROFILE)){
			return;
		}
		TaskManager.doTask(this::runTopicThreads , "开启TOPIC消费线程");

	}

	/**
	 * 开启TOPIC消费线程 将所有可能出现的异常全部catch住，确保While(true)能够不中断
	 */
	@SuppressWarnings("InfiniteLoopStatement")
	private void runTopicThreads() {
		while (true) {
			RLock lock = null;
			try {
				lock = redissonClient.getLock(RedisQueueKey.CONSUMER_TOPIC_LOCK);
			} catch (Exception e) {
				log.error("runTopicThreads getLock error" , e);
			}
			try {
				if (lock == null) {
					continue;
				}
				// 分布式锁时间比Blpop阻塞时间多1S，避免出现释放锁的时候，锁已经超时释放，unlock报错
				boolean lockFlag = lock.tryLock(RedisQueueKey.LOCK_WAIT_TIME , RedisQueueKey.LOCK_RELEASE_TIME , TimeUnit.SECONDS);
				if (! lockFlag) {
					continue;
				}

				// 1. 获取ReadyQueue中待消费的数据
				RBlockingQueue<String> queue = redissonClient.getBlockingQueue(RedisQueueKey.RD_LIST_TOPIC_PRE);
				String topicId = queue.poll(60 , TimeUnit.SECONDS);
				if (StringUtils.isEmpty(topicId)) {
					continue;
				}

				// 2. 获取job元信息内容
				RMap<String, Task> taskPoolMap = redissonClient.getMap(RedisQueueKey.TASK_POOL_KEY);
				Task task = taskPoolMap.get(topicId);
				if (null == task) {
					continue;
				}

				String topic = task.getTopic();
				// 3. 获取实现类
				ConsumerService consumers = ConsumerFactory.getConsumers(ConsumerTypeEnum.formatEnum(task.getTopic()));
				if (consumers == null) {
					continue;
				}
				System.out.println(task.getBody());
				// 4. 消费
				FutureTask<Boolean> taskResult = TaskManager.doFutureTask(() -> consumers.consumerMessage(task.getBody()) ,
						topic + "-->消费taskId-->" + task.getTaskId());
				if (taskResult.get()) {
					// 4.1 消费成功，删除JobPool和DelayBucket的job信息
					taskPoolMap.remove(topicId);
				} else {
					int retrySum = task.getRetry() + 1;
					// 4.2 消费失败，则根据策略重新加入Bucket

					// 如果重试次数大于5，则将jobPool中的数据删除
					if (retrySum > RetryStrategyEnum.RETRY_FIVE.getRetry()) {
						taskPoolMap.remove(topicId);
						//持久化消息到db中  TODO
						continue;
					}
					task.setRetry(retrySum);
					long nextTime = task.getDelay() + RetryStrategyEnum.getDelayTime(task.getRetry()) * 1000;
					log.info("next retryTime is [{}]" , DateUtil.formatNormalDate(new Date(nextTime)));
					RScoredSortedSet<Object> delayBucket = redissonClient.getScoredSortedSet(RedisQueueKey.RD_ZSET_BUCKET_PRE);
					delayBucket.add(nextTime , topicId);
					// 4.3 更新元信息失败次数
					taskPoolMap.put(topicId , task);
				}
			} catch (Exception e) {
				log.error("runTopicThreads error" , e);
			} finally {
				if (lock != null) {
					try {
						boolean heldByCurrentThread = lock.isHeldByCurrentThread();
						if (heldByCurrentThread) {
							lock.unlock();
						}
					} catch (Exception e) {
						log.error("runTopicThreads unlock error" , e);
					}
				}
			}
		}
	}


}
