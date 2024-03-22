package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.impl;


import com.snowalker.shardingjdbc.snowalker.demo.constant.ErrorMessageEnum;
import com.snowalker.shardingjdbc.snowalker.demo.constant.RedisQueueKey;
import com.snowalker.shardingjdbc.snowalker.demo.enums.ErrorCodeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.exception.ServiceException;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.RedisDelayQueueService;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.Task;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.TaskDie;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @Method_Name : 操作实现类
 * @Description : * @param null
 * @return
 * @Creation Date : 2020/11/11
 * @Author : fangwenhui
 */
@Slf4j
@Service
public class RedisDelayQueueServiceImpl implements RedisDelayQueueService {

	@Resource
	private RedissonClient redissonClient;

	/**
	 * 添加task
	 *
	 * @param task
	 */
	@Override
	public void addTask(Task task) {

		RLock lock = redissonClient.getLock(RedisQueueKey.ADD_TASK_LOCK + RedisQueueKey.getTopicId(task.getTopic() , task.getTaskId()));
		try {
			lock.lock();
			String topicId = RedisQueueKey.getTopicId(task.getTopic() , task.getTaskId());
			// 1. 将task添加到 taskPool中
			RMap<String, Task> taskPool = redissonClient.getMap(RedisQueueKey.TASK_POOL_KEY);
			taskPool.put(topicId , task);
			// 2. 将task添加到 DelayBucket中
			RScoredSortedSet<Object> delayBucket = redissonClient.getScoredSortedSet(RedisQueueKey.RD_ZSET_BUCKET_PRE);
			delayBucket.add(task.getDelay() , topicId);
		} catch (Exception e) {
			log.error("addTask error" , e);
		} finally {
		}
		if (lock != null) {
			lock.unlock();
		}
	}


	/**
	 * 删除task信息
	 *
	 * @param taskDie 元信息
	 */
	@Override
	public void deleteTask(TaskDie taskDie) {
		RLock lock = redissonClient.getLock(RedisQueueKey.DELETE_TASK_LOCK + RedisQueueKey.getTopicId(taskDie.getTopic() , taskDie.getTaskId()));
		try {
			boolean lockFlag = lock.tryLock(RedisQueueKey.LOCK_WAIT_TIME , RedisQueueKey.LOCK_RELEASE_TIME , TimeUnit.SECONDS);
			if (! lockFlag) {
				throw new ServiceException(ErrorCodeEnum.E000404 , ErrorMessageEnum.ACQUIRE_LOCK_FAIL.getInfo());
			}
			String topicId = RedisQueueKey.getTopicId(taskDie.getTopic() , taskDie.getTaskId());

			RMap<String, Task> taskPool = redissonClient.getMap(RedisQueueKey.TASK_POOL_KEY);
			taskPool.remove(topicId);

			RScoredSortedSet<Object> delayBucket = redissonClient.getScoredSortedSet(RedisQueueKey.RD_ZSET_BUCKET_PRE);
			delayBucket.remove(topicId);
		} catch (InterruptedException e) {
			log.error("addJob error" , e);
		} finally {
			if (lock != null) {
				lock.unlock();
			}
		}
	}

	/**
	 * 批量添加task
	 *
	 * @param tasks
	 */
	@Override
	public void addTasks(List<Task> tasks) {
		if (tasks == null || tasks.isEmpty()) {
			return;
		}
		for (Task task : tasks) {
			addTask(task);
		}
	}
}
