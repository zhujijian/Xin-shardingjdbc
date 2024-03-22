package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.scheduled;

import com.snowalker.shardingjdbc.snowalker.demo.constant.ErrorMessageEnum;
import com.snowalker.shardingjdbc.snowalker.demo.constant.RedisQueueKey;
import com.snowalker.shardingjdbc.snowalker.demo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = {"carryJobScheduled.enable"}, havingValue = "true")
public class CarryJobScheduled {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 启动定时开启搬运JOB信息
     */
    @Scheduled(cron = "*/1 * * * * *")
    public void carryJobToQueue() {
        RLock lock = redissonClient.getLock(RedisQueueKey.CARRY_THREAD_LOCK);
        try {
            boolean lockFlag = lock.tryLock(RedisQueueKey.LOCK_WAIT_TIME, RedisQueueKey.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!lockFlag) {
                throw new ServiceException(String.valueOf(ErrorMessageEnum.ACQUIRE_LOCK_FAIL.getCode()), ErrorMessageEnum.ACQUIRE_LOCK_FAIL.getInfo());
            }
            RScoredSortedSet<Object> bucketSet = redissonClient.getScoredSortedSet(RedisQueueKey.RD_ZSET_BUCKET_PRE);
            long now = System.currentTimeMillis();
            Collection<Object> taskCollection = bucketSet.valueRange(0, true, now, true);
            List<String> jobList = taskCollection.stream().map(String::valueOf).collect(Collectors.toList());
            RList<String> readyQueue = redissonClient.getList(RedisQueueKey.RD_LIST_TOPIC_PRE);
            readyQueue.addAll(jobList);
            bucketSet.removeAllAsync(jobList);
        } catch (InterruptedException e) {
            log.error("carryJobToQueue error", e);
        } finally {
            if (lock != null) {
                if(lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        }
    }
}
