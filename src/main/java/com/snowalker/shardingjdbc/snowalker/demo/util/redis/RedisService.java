package com.snowalker.shardingjdbc.snowalker.demo.util.redis;

import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务接口，利用此接口来访问缓存
 */
public interface RedisService {

	//=================缓存键操作======================

    /**
     * 得到缓存的键值列表
     * @param pattern 缓存键匹配模式
     * @return 匹配的缓存键
     */
    Set<String> keys(String pattern);

	//=================缓存形式(KEY-VALUE)======================
	/**
	 * 将数据放入缓存
	 * @param key 缓存键
	 * @param obj 缓存对象
	 * @param seconds 过期秒数
	 */
    void set(String key, Object obj, int seconds);
	void set(String fromCityCodeKey, Object fromInfo, int i, TimeUnit hours);

    /**
     * 根据缓存键获得缓存数据
     * @param <T> 缓存数据类型
     * @param key 缓存键
     * @return 缓存数据
     */
    <T> T get(String key);

    /**
          * 根据缓存键获得缓存数据列表
     * @param <T> 缓存数据类型
     * @param keys 缓存键
     * @return 缓存数据列表
     */
    <T> List<T> mget(Collection<String> keys);

    /**
     * 删除缓存
     * @param key 缓存键
     */
    void del(String... key);

    //==================哈希缓存形式(KEY-FIELD-VALUE)=====================
    /**
     * 将数据放入缓存
     * @param key 缓存键
     * @param field 哈希键
     * @param obj 缓存数据
     * @param seconds 过期秒数
     */
    void hset(String key, String field, Object obj, int seconds);

    /**
     * 批量将数据放入缓存
     * @param key 缓存键
     * @param objs Map<哈希键, 缓存数据>
     * @param seconds 过期秒数
     */
    void hmset(String key, Map<String, Object> objs, int seconds);

    /**
     * 得到对应的缓存值
     * @param <T> 缓存数据类型
     * @param key 缓存键
     * @param field 哈希键
     * @return 缓存数据
     */
    <T> T hget(String key, String field);

    /**
         * 得到对应的缓存值列表
     * @param <T> 缓存数据类型
     * @param key 缓存键列表
     * @param fields 哈希键
     * @return 缓存数据
     */
    <T> List<T> hmget(String key, Collection<String> fields);

    /**
     * 删除缓存的键值
     * @param key 缓存键
     * @param fields 哈希键
     */
    void hdel(String key, String... fields);


	/**
	 *
	 * @param key 缓存键
	 * @param start 开始
	 * @param end 结束
	 * @param <T>
	 * @return
	 */
	<T> List<T> lrange(String key, int start, int end);

	/**
     * 返回哈希键的数量
     * @param key 缓存键
     * @return 缓存个数
     */
    Long hlen(String key);

    /***
        * 过期时间设置
    * @param key 缓存键
    * @param seconds 过期秒数
     */
    void expire(String key, int seconds);


	/**
	 * 执行lua脚本  返回String
	 *
	 * @param redisScript   lua脚本
	 * @param keyList  KEYS
	 * @param args   ARGV
	 * @return
	 */
	<T> T execute(DefaultRedisScript<T> redisScript, List<Object> keyList, Object... args);


	boolean exists(String key);

	//=============================== sort set =================================
	/**
	 * 添加指定元素到有序集合中
	 * @param key
	 * @param score
	 * @param value
	 * @return
	 */
	 boolean sortSetAdd(String key, String value, double score, Long time, TimeUnit unit) ;

	/**
	 * 有序集合中对指定成员的分数加上增量 increment
	 * @param key
	 * @param value
	 * @param i
	 * @return
	 */
	public double sortSetZincrby(String key, String value, double i) ;

	/**
	 * 向zset结构中指定元素的分数加上增量 i，新增key设置有效期
	 * @param key
	 * @param value
	 * @param i
	 * @param time
	 * @param unit
	 * @return
	 */
	double sortSetZincrby(String key, String value, double i, Long time, TimeUnit unit) ;

	/**
	 * 获得有序集合指定范围元素 (从大到小)
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	Set<Object> sortSetReverseRange(String key, int start, int end) ;

	/**
	 * 将有序集合oZset和oZsetList的交集合并为nZset
	 *
	 * @param oZset
	 * @param oZsetList
	 * @param nZset
	 */
	Long sortSetIntersection(String oZset, List<String> oZsetList,
                             String nZset) ;

	/**
	 * 将有序集合oZset和oZsetList的并集合并为nZset,每个集合的权重相同
	 *
	 * @param oZset
	 * @param oZsetList
	 * @param nZset
	 */
	Long sortSetUnionAndStore(String oZset, List<String> oZsetList,
                              String nZset) ;

	/**
	 * 从zset结构中移除元素
	 */
	Long sortSetRemove(String key, Object... values) ;


	Long rPush(String redisKey, String pickAndSign, long l, TimeUnit hours);

	Boolean setNx(String key, Object obj, long time, TimeUnit timeUnit);

	Boolean unlock(String key, String value);

	Long getExpireTime(String screenV2StockKey);
}
