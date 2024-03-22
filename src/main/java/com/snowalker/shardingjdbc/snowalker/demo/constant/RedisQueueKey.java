package com.snowalker.shardingjdbc.snowalker.demo.constant;

/**
 * @Method_Name : redis常量key
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/12
 * @Author : fangwenhui
 */
public class RedisQueueKey {

    /**
     * 是一个Hash_Table结构；里面存储了所有的延迟队列的信息;KV结构；
     * K=prefix + projectName    field= topic+taskId    V=CONENT;  V由客户端传入的数据,消费的时候回传；
     */
    public static final String TASK_POOL_KEY = "FPL:DELAY_QUEUE:TASK_POOL";


    /**
     * 延迟队列的有序集合; 存放K=ID 和需要的执行时间戳;
     * 根据时间戳排序;
     */
    public static final String RD_ZSET_BUCKET_PRE = "FPL:DELAY_QUEUE:BUCKET";


    /**
     * list结构; 每个Topic一个list；list存放的都是当前需要被消费的Job;
     */
    public static final String RD_LIST_TOPIC_PRE = "FPL:DELAY_QUEUE:QUEUE";

    /**
     * 返回成功
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 搬运线程分布式锁
     */
    public static final String CARRY_THREAD_LOCK = "FPL:DELAY_QUEUE:CARRY_THREAD_LOCK";

    /**
     * 添加TASK分布式锁
     */
    public static final String ADD_TASK_LOCK = "FPL:DELAY_QUEUE:ADD_TASK_LOCK:";

    /**
     * 删除TASK分布式锁
     */
    public static final String DELETE_TASK_LOCK = "FPL:DELAY_QUEUE:DELETE_TASK_LOCK:";

    /**
     * 分布式锁
     */
    public static final String CONSUMER_TOPIC_LOCK = "FPL:DELAY_QUEUE:CONSUMER_TOPIC_LOCK:";

    /**
     * 竞价单操作锁
     */
    public static final String ENQUITY_LOCK = "FPL:ENQUITY_LOCK:";

    /**
     * 锁等待时间
     */
    public static final long LOCK_WAIT_TIME = 3;

    /**
     * 锁释放时间
     */
    public static final long LOCK_RELEASE_TIME = 62;

    /**
     * 拼接TOPICID
     */
    public static String getTopicId(String topic, String taskId) {
        return topic.concat(":").concat(taskId);
    }

}
